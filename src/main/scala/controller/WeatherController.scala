package controller

import service.WeatherService.getCitiesMatchingWeatherCondition
import zio.http.*

object WeatherController {

  def apply(): Http[Any, Throwable, Request, Response] =
    Http.collect[Request] {
      // Define an HTTP service that handles incoming requests
      case Method.GET -> Root / "meteo" / condition =>
        // For GET requests to the /weather/{condition} endpoint

        // Call the getCitiesMatchingWeatherCondition function with the condition parameter
        val result = getCitiesMatchingWeatherCondition(condition)

        // Create a textual HTTP response with the obtained result
        Response.text(result)
    }


}
