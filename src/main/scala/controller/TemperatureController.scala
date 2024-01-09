package controller

import service.TemperatureService.{getRecommandationByCity, getTemperatureOfCity}
import zio.http.*

object TemperatureController {

  def apply(): Http[Any, Throwable, Request, Response] =
    Http.collect[Request] {
      // GET /temperature/{name}
      case Method.GET -> Root / "temperature" / cityName =>
        Response.text(getTemperatureOfCity(cityName))

      // GET /temperature/recommendation/{name}
      case Method.GET -> Root / "temperature" / "recommendation" / cityName =>
        Response.text(getRecommandationByCity(cityName))
    }
}
