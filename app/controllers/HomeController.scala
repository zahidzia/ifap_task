package controllers

import model.{ErrorResponse, PopulationRequest, PopulationResponse}

import javax.inject._
import repo.PopulationRepository
import play.api.libs.json.{Json, Reads, Writes}
import play.api.mvc._
import utils.{NoDataFoundException, RequestParsingException}

import scala.util.{Failure, Success, Try}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(val controllerComponents: ControllerComponents,
                               populationRepository: PopulationRepository) extends BaseController {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index():Action[AnyContent] = withBodyAs[PopulationRequest] {
    request => {
      val response = populationRepository.getPopulation(request.cityName, request.year)
        .map(PopulationResponse(_))
      sendOkIfSuccess(response)
    }
  }


  def withBodyAs[T](code: T => Result)(implicit fjs: Reads[T]): Action[AnyContent] = Action {
    request => {
      val maybeBodyAsType = request.body.asJson
        .map(Success(_))
        .getOrElse(Failure(RequestParsingException("No valid json body supplied")))
        .flatMap(jsonBody => Try(jsonBody.as[T]))
        .recoverWith { case error: Throwable => Failure(RequestParsingException(error.getMessage)) }
      resultWithTry(maybeBodyAsType) {
        bodyAsType => code(bodyAsType)
      }
    }
  }


  def sendOkIfSuccess[T](maybeValue: Try[T])(implicit tjs: Writes[T]): Result = {
    resultWithTry(maybeValue) {
      sendOk
    }
  }

  def sendOk[T](response: T)(implicit tjs: Writes[T]): Result = {
    Ok(Json.toJson(response))
  }

  def resultWithTry[T](maybeValue: Try[T])(code: T => Result): Result = {
    val result = maybeValue match {
      case Success(value) => code(value)
      case Failure(error) =>
        error match {
          case error: RequestParsingException => BadRequest(Json.toJson(ErrorResponse(error.getMessage)))
          case error: NoDataFoundException => NotFound(Json.toJson(ErrorResponse(error.getMessage)))
          case _ => InternalServerError(Json.toJson(ErrorResponse.INTERNAL_SERVER_ERROR))
        }
    }
    result
  }
}
