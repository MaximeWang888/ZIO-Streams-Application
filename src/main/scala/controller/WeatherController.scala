package controller

import service.WeatherService.getCitiesMatchingWeatherCondition
import zio.http.*

object WeatherController {

  def apply(): Http[Any, Throwable, Request, Response] =
    Http.collect[Request] {
      // Définition d'un service HTTP qui traite les requêtes entrantes
      case Method.GET -> Root / "meteo" / condition =>
        // Pour les requêtes GET vers l'endpoint /meteo/{condition}

        // Appel de la fonction getCitiesMatchingWeatherCondition avec le paramètre de condition
        val result = getCitiesMatchingWeatherCondition(condition)

        // Création d'une réponse HTTP textuelle avec le résultat obtenu
        Response.text(result)
    }

}
