package model

import model.DataModel.{Coordinates, Temperature}

import java.sql.{Connection, PreparedStatement, ResultSet}

class DataModelDao(dbManager: DatabaseManager) {

  private lazy val connection: Connection = dbManager.getConnection
  private var preparedStatement: PreparedStatement = _
  private var resultSet: ResultSet = _

  // Méthode pour insérer les coordonnées dans la table Coordinates de la base de données
  def insertCoordinates(coord: Coordinates): Unit = {
    try {
      // Définition de la requête SQL pour insérer les coordonnées dans la table Coordinates
      val query =
        "INSERT IGNORE INTO Coordinates (latitude, longitude) VALUES (?, ?)" +
          "ON DUPLICATE KEY UPDATE latitude = VALUES(latitude), longitude = VALUES(longitude)"

      // Préparation de la requête SQL avec la connexion à la base de données
      preparedStatement = connection.prepareStatement(query)

      // Attribution des valeurs des coordonnées aux paramètres de la requête SQL
      preparedStatement.setDouble(1, coord.latitude.asInstanceOf)
      preparedStatement.setDouble(2, coord.longitude.asInstanceOf)

      // Exécution de la requête SQL
      preparedStatement.executeUpdate()
    } finally {
      // Fermeture de la PreparedStatement dans le bloc finally pour garantir la libération des ressources
      if (preparedStatement != null) preparedStatement.close()
    }
  }

  // Méthode pour insérer la température dans la table CurrentWeather de la base de données
  def insertTempInDB(temperature: Double, coord: Coordinates): Unit = {
    // Assurer que les coordonnées existent dans la table Coordinates
    insertCoordinates(coord)

    try {
      // Définition de la requête SQL pour insérer la température dans la table CurrentWeather
      val query =
        "INSERT INTO CurrentWeather (latitude, longitude, temperature, weatherDescription, date) " +
          "VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP) " +
          "ON DUPLICATE KEY UPDATE temperature = VALUES(temperature), date = CURRENT_TIMESTAMP"

      // Préparation de la requête SQL avec la connexion à la base de données
      preparedStatement = connection.prepareStatement(query)

      // Attribution des valeurs des coordonnées et de la température aux paramètres de la requête SQL
      preparedStatement.setDouble(1, coord.latitude.asInstanceOf)
      preparedStatement.setDouble(2, coord.longitude.asInstanceOf)
      preparedStatement.setDouble(3, temperature)
      preparedStatement.setString(4, "SUNNY")

      // Exécution de la requête SQL
      preparedStatement.executeUpdate()
    } finally {
      // Fermeture de la PreparedStatement dans le bloc finally pour garantir la libération des ressources
      if (preparedStatement != null) preparedStatement.close()
    }
  }


  def getTemperatureFromCoord(coordinates: Coordinates): Option[DataModel.Temperature] = {
    try {
      val query = "SELECT temperature, weatherDescription, date FROM CurrentWeather WHERE latitude = ? AND longitude = ?"
      preparedStatement = connection.prepareStatement(query)
      preparedStatement.setDouble(1, coordinates.latitude.asInstanceOf)
      preparedStatement.setDouble(2, coordinates.longitude.asInstanceOf)

      resultSet = preparedStatement.executeQuery()

      if (resultSet.next()) {
        val temperature = resultSet.getDouble("temperature")
        Some(temperature.asInstanceOf[DataModel.Temperature])
      } else {
        None
      }
    } finally {
      if (preparedStatement != null) preparedStatement.close()
      if (resultSet != null) resultSet.close()
    }
  }

  def getAllCities: Option[List[DataModel.City]] = {
    try {
      val query = "SELECT name, latitude, longitude FROM City"
      preparedStatement = connection.prepareStatement(query)

      resultSet = preparedStatement.executeQuery()

      var cities: List[DataModel.City] = List()

      while (resultSet.next()) {
        val cityName = resultSet.getString("name")
        val latitude = resultSet.getDouble("latitude")
        val longitude = resultSet.getDouble("longitude")

        val coordinates = DataModel.Coordinates(latitude.asInstanceOf,
                                                longitude.asInstanceOf)
        val city = DataModel.City(cityName.asInstanceOf, coordinates)
        cities = cities :+ city
      }

      if (cities.nonEmpty) {
        Some(cities)
      } else {
        None
      }
    } finally {
      if (preparedStatement != null) preparedStatement.close()
      if (resultSet != null) resultSet.close()
    }
  }

  def getCityToCoord(cityName: String): Option[DataModel.City] = {
    try {
      val query = "SELECT name, latitude, longitude FROM City WHERE name = ?"
      preparedStatement = connection.prepareStatement(query)
      preparedStatement.setString(1, cityName)

      resultSet = preparedStatement.executeQuery()

      if (resultSet.next()) {
        val cityName = resultSet.getString("name")
        val latitude = resultSet.getDouble("latitude")
        val longitude = resultSet.getDouble("longitude")

        val coordinates = DataModel.Coordinates(latitude.asInstanceOf,
                                                longitude.asInstanceOf)
        Some(DataModel.City(cityName.asInstanceOf, coordinates))
      } else {
        None
      }
    } finally {
      if (preparedStatement != null) preparedStatement.close()
      if (resultSet != null) resultSet.close()
    }
  }

  def getCityRecommendation(temperature: Temperature): Option[DataModel.TemperatureRecommendation] = {
    try {
      val query =
        "SELECT temperatureRange, minTemperature, maxTemperature, recommendation " +
          "FROM TemperatureRecommendation " +
          "WHERE ? >= minTemperature OR (minTemperature IS NULL AND ? <= maxTemperature)"
      preparedStatement = connection.prepareStatement(query)
      preparedStatement.setDouble(1, temperature.asInstanceOf)
      preparedStatement.setDouble(2, temperature.asInstanceOf)

      resultSet = preparedStatement.executeQuery()

      if (resultSet.next()) {
        val temperatureRange = resultSet.getString("temperatureRange")
        val minTemperature = Option(resultSet.getDouble("minTemperature"))
        val maxTemperature = Option(resultSet.getDouble("maxTemperature"))
        val recommendation = resultSet.getString("recommendation")

        Some(DataModel.TemperatureRecommendation(temperatureRange, minTemperature, maxTemperature, recommendation))
      } else {
        None
      }
    } finally {
      if (preparedStatement != null) preparedStatement.close()
      if (resultSet != null) resultSet.close()
    }
  }

  def getWeatherConditionForCity(cityName: DataModel.CityName): String = {
    try {
      val query = "SELECT weatherDescription FROM CurrentWeather WHERE latitude = " +
        "(SELECT latitude FROM City WHERE name = ?) AND longitude = (SELECT longitude FROM City WHERE name = ?)"
      preparedStatement = connection.prepareStatement(query)
      preparedStatement.setString(1, cityName.asInstanceOf)
      preparedStatement.setString(2, cityName.asInstanceOf)

      resultSet = preparedStatement.executeQuery()

      if (resultSet.next()) {
        resultSet.getString("weatherDescription")
      } else {
        "Unknown"
      }
    } finally {
      if (preparedStatement != null) preparedStatement.close()
      if (resultSet != null) resultSet.close()
    }
  }

}
