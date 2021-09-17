package model

import play.api.libs.json.{Json, OFormat}

case class ErrorResponse(errorMessage: String)

object ErrorResponse {

  lazy val INTERNAL_SERVER_ERROR: ErrorResponse =
    ErrorResponse("An internal server error has occurred")

  implicit val format: OFormat[ErrorResponse] = Json.format[ErrorResponse]
}
