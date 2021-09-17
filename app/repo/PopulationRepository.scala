package repo


import scala.io.Source
import kantan.csv._
import kantan.csv.ops._
import utils.NoDataFoundException

import scala.util.{Failure, Success, Try}

trait PopulationRepository {
  def getPopulation(cityName: String, year: Option[Short] = None): Try[Int]
}

case class CsvPopulationRepository(populations: Source) extends PopulationRepository{
  implicit lazy val headerDecoder: HeaderDecoder[PopulationData] = HeaderDecoder.decoder(
    "Country or Area", "Year", "Area", "Sex",
    "City","City type","Record Type","Reliability",
    "Source Year","Value","Value Footnotes")(PopulationData.apply)

  lazy val cityNamePopulationDataMap: Map[String, Map[Short, List[PopulationData]]] = {
    val filteredCsvData = populations
      .getLines()
      .takeWhile(!_.startsWith("footnoteSeqID"))
      .mkString("\n")
    val csvReader = filteredCsvData
      .asCsvReader[PopulationData](rfc.withHeader)
    csvReader.toList.flatMap{ readData =>
      readData match {
        case Right(populationData) =>
          Some(populationData)
        case Left(error) =>
          throw error
          None
      }
    }
      .groupBy(_.city)
      .map{case(city, cityData) =>
        (city, cityData.groupBy(_.year))
      }
  }
  override def getPopulation(cityName: String, year: Option[Short] = None): Try[Int] = {
    cityNamePopulationDataMap.get(cityName)
      .flatMap{ cityData =>
        val maybeData = year
          .map(cityData.get)
          .getOrElse(cityData.get(cityData.keys.max))
        maybeData
          .map(_.map(_.value).sum.toInt)
      }.map(Success(_))
      .getOrElse(Failure(NoDataFoundException(s"Not data for city: $cityName found")))
  }
}