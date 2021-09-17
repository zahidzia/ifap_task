import com.google.inject.AbstractModule
import play.api.{Configuration, Environment}
import repo.{CsvPopulationRepository, PopulationRepository}
import scala.io.Source


class Module(environment: Environment, configuration: Configuration)
    extends AbstractModule {

  override def configure():Unit = {

    val populationSource : Source = Source.fromResource("populations.csv")
    bind(classOf[PopulationRepository]).toInstance(CsvPopulationRepository(populationSource))
  }
}
