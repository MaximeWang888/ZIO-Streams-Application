package controller

import service.WeatherService.getCitiesMatchingWeatherCondition
import zio.http.*

object WeatherController {

  def apply(): Http[Any, Throwable, Request, Response] =
    Http.collect[Request] {
      // GET /meteo/{condition}
      case Method.GET -> Root / "meteo" / condition =>
        Response.text(getCitiesMatchingWeatherCondition(condition))

    }
}
