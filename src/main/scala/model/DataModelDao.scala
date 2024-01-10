package model

import model.DataModel.{Coordinates, Temperature}

import java.sql.{Connection, PreparedStatement, ResultSet}

class DataModelDao(dbManager: DatabaseManager) {

  private lazy val connection: Connection = dbManager.getConnection
  private var preparedStatement: PreparedStatement = _
  private var resultSet: ResultSet = _

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

        val coordinates = DataModel.Coordinates(latitude.asInstanceOf[DataModel.Latitude],
                                                longitude.asInstanceOf[DataModel.Longitude])
        val city = DataModel.City(cityName.asInstanceOf[DataModel.CityName], coordinates)
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

        val coordinates = DataModel.Coordinates(latitude.asInstanceOf[DataModel.Latitude],
                                                longitude.asInstanceOf[DataModel.Longitude])
        Some(DataModel.City(cityName.asInstanceOf[DataModel.CityName], coordinates))
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
      preparedStatement.setDouble(1, temperature.asInstanceOf[Double])
      preparedStatement.setDouble(2, temperature.asInstanceOf[Double])

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
      preparedStatement.setString(1, cityName.asInstanceOf[String])
      preparedStatement.setString(2, cityName.asInstanceOf[String])

      resultSet = preparedStatement.executeQuery()

      if (resultSet.next()) {
        resultSet.getString("weatherDescription")
      } else {
        // You may handle the case when the weather description is not available
        "Unknown"
      }
    } finally {
      if (preparedStatement != null) preparedStatement.close()
      if (resultSet != null) resultSet.close()
    }
  }

}
