package service

import model.DataModel.City
import model.{DataModel, DataModelDao, DatabaseManager}

trait WeatherService {
  def getCitiesMatchingWeatherCondition(condition: String): CharSequence
}

object WeatherService {
  // Initialisation d'un objet DatabaseManager et DataModelDao pour la gestion des données
  private val dbManager = new DatabaseManager()
  private val dataModelDao = new DataModelDao(dbManager)

  // Méthode qui retourne une séquence de caractères correspondant aux villes avec une condition météorologique spécifiée
  def getCitiesMatchingWeatherCondition(condition: String): CharSequence = {
    // Récupération de toutes les villes à partir de la base de données
    val allCitiesResult = dataModelDao.getAllCities

    // Utilisation du pattern matching pour gérer les cas où la récupération des villes réussit ou échoue
    val result: CharSequence = allCitiesResult match {
      case Some(cities) =>
        // Filtrage des villes qui correspondent à la condition météorologique spécifiée
        val matchingCities = cities.filter { city =>
          val weatherCondition = dataModelDao.getWeatherConditionForCity(city.name)
          weatherCondition.equalsIgnoreCase(condition)
        }

        // Vérification si des villes correspondent à la condition météorologique
        if (matchingCities.nonEmpty) {
          // Création d'une chaîne de caractères avec les noms des villes correspondantes
          val cityNames = matchingCities.map(_.name)
          cityNames.mkString(", ")
        } else {
          // Message en cas d'absence de villes correspondantes
          "No cities match the specified weather condition."
        }

      case None => "Failed to retrieve cities data."  // Message en cas d'échec de récupération des données des villes
    }
    result
  }
}
