package model

import play.api.libs.json.{Json, OFormat}

case class PopulationRequest(cityName: String, year: Option[Short])

object PopulationRequest {

  implicit val format: OFormat[PopulationRequest] = Json.format[PopulationRequest]
}
