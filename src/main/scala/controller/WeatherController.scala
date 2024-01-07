package controller

import service.WeatherService
import zio.http.*

object WeatherController {

  val weatherService = new WeatherService

  def apply(): Http[Any, Throwable, Request, Response] =
    Http.collect[Request] {
      // GET /meteo/{condition}
      case Method.GET -> Root / "meteo" / condition =>
        Response.text(weatherService.getCitiesMatchingWeatherCondition(condition))

    }
}
