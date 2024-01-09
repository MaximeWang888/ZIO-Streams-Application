package model

import java.sql.{Connection, PreparedStatement, ResultSet}

class DataModelDao(dbManager: DatabaseManager) {

  def getAllCities: Option[List[DataModel.City]] = {
    var preparedStatement: PreparedStatement = null
    var resultSet: ResultSet = null

    try {
      val connection: Connection = dbManager.getConnection
      val query = "SELECT name, latitude, longitude FROM City"
      preparedStatement = connection.prepareStatement(query)

      resultSet = preparedStatement.executeQuery()

      var cities: List[DataModel.City] = List()

      while (resultSet.next()) {
        val cityName = resultSet.getString("name")
        val latitude = resultSet.getDouble("latitude")
        val longitude = resultSet.getDouble("longitude")

        val coordinates = DataModel.Coordinates(latitude.asInstanceOf[DataModel.Latitude], longitude.asInstanceOf[DataModel.Longitude])
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
    var preparedStatement: PreparedStatement = null
    var resultSet: ResultSet = null

    try {
      val connection: Connection = dbManager.getConnection
      val query = "SELECT name, latitude, longitude FROM City WHERE name = ?"
      preparedStatement = connection.prepareStatement(query)
      preparedStatement.setString(1, cityName)

      resultSet = preparedStatement.executeQuery()

      if (resultSet.next()) {
        val cityName = resultSet.getString("name")
        val latitude = resultSet.getDouble("latitude")
        val longitude = resultSet.getDouble("longitude")

        val coordinates = DataModel.Coordinates(latitude.asInstanceOf[DataModel.Latitude], longitude.asInstanceOf[DataModel.Longitude])
        Some(DataModel.City(cityName.asInstanceOf[DataModel.CityName], coordinates))
      } else {
        None
      }
    } finally {
      if (preparedStatement != null) preparedStatement.close()
      if (resultSet != null) resultSet.close()
    }
  }

  def getCityRecommendation(cityName: String): Option[DataModel.TemperatureRecommendation] = {
    // First fetch temperature with API


    // Second give the temperature to DB in order to get the recommendation
    var preparedStatement: PreparedStatement = null
    var resultSet: ResultSet = null

    try {
      val connection: Connection = dbManager.getConnection
      val query = "SELECT temperatureRange, recommendation FROM TemperatureRecommendation WHERE temperatureRange IN " +
        "(SELECT temperatureRange FROM City WHERE name = ?)"
      preparedStatement = connection.prepareStatement(query)
      preparedStatement.setString(1, cityName)

      resultSet = preparedStatement.executeQuery()

      if (resultSet.next()) {
        val temperatureRange = resultSet.getString("temperatureRange")
        val recommendation = resultSet.getString("recommendation")

        Some(DataModel.TemperatureRecommendation(temperatureRange, recommendation))
      } else {
        None
      }
    } finally {
      if (preparedStatement != null) preparedStatement.close()
      if (resultSet != null) resultSet.close()
    }
  }

}
