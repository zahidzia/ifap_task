package controllers

import model.{ErrorResponse, PopulationRequest, PopulationResponse}
import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.libs.json.Json
import play.api.mvc.Results.NotFound
import play.api.test._
import play.api.test.Helpers._
import repo.CsvPopulationRepository

import scala.io.Source

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 *
 * For more information, see https://www.playframework.com/documentation/latest/ScalaTestingWithScalaTest
 */
class HomeControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {

  private val populationSource: Source = Source.fromResource("populations.csv")
  val controller = new HomeController(stubControllerComponents(), CsvPopulationRepository(populationSource))

  "HomeController" should {

    "return population for city name" in {
      val request = PopulationRequest("München", None, None)
      val result = controller.index().apply(FakeRequest(GET, "/").withJsonBody(Json.toJson(request)))
      val response = contentAsJson(result).as[PopulationResponse]
      status(result) mustBe OK
      response.population mustBe 1388308
    }

    "return population for city name and year" in {
      val request = PopulationRequest("München", Some(1999), None)
      val result = controller.index().apply(FakeRequest(GET, "/").withJsonBody(Json.toJson(request)))
      val response = contentAsJson(result).as[PopulationResponse]
      status(result) mustBe OK
      response.population mustBe 1194560
    }

    "return NotFound with no city given" in {
      val request = PopulationRequest("", None, None)
      val result = controller.index().apply(FakeRequest(GET, "/").withJsonBody(Json.toJson(request)))
      status(result) mustBe 404
    }

    "return population for city name and year and provisional flag" in {
      val request = PopulationRequest("Cairns", Some(2011), Some(true))
      val result = controller.index().apply(FakeRequest(GET, "/").withJsonBody(Json.toJson(request)))
      val response = contentAsJson(result).as[PopulationResponse]
      status(result) mustBe OK
      response.population mustBe 139693

    }
  }
}
