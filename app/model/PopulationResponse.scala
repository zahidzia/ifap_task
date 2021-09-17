package model

import play.api.libs.json.{Json, OFormat}

case class PopulationResponse(population: Int)

object PopulationResponse {

  implicit val format: OFormat[PopulationResponse] = Json.format[PopulationResponse]
}
