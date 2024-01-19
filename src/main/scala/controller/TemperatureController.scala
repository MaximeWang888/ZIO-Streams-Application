package controller

import service.TemperatureService.{getRecommandationByCity, getTemperatureOfCity}
import zio.http.*

object TemperatureController {

  // Define an HTTP route for handling temperature-related requests
  def apply(): Http[Any, Throwable, Request, Response] =
    Http.collect[Request] {
      // Handle GET requests to /temperature/{name}
      case Method.GET -> Root / "temperature" / cityName =>
        // Respond with the temperature of the specified city
        Response.text(getTemperatureOfCity(cityName))

      // Handle GET requests to /temperature/recommendation/{name}
      case Method.GET -> Root / "temperature" / "recommendation" / cityName =>
        // Respond with a temperature recommendation for the specified city
        Response.text(getRecommandationByCity(cityName))
    }
}
