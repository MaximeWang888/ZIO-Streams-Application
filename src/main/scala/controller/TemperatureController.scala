package controller

import service.TemperatureService
import zio.http.*

object TemperatureController {

  val temperatureService = new TemperatureService

  def apply(): Http[Any, Throwable, Request, Response] =
    Http.collect[Request] {
      // GET /temperature/{name}
      case Method.GET -> Root / "temperature" / cityName =>
        Response.text(temperatureService.getTemperatureOfCity(cityName))

      // GET /temperature/recommendation/{name}
      case Method.GET -> Root / "temperature" / "recommendation" / cityName =>
        Response.text(temperatureService.getRecommandationByCity(cityName))
    }
}
