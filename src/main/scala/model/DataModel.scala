package model

// Define the data model for the application
object DataModel {

  // Define opaque types for better type safety and readability
  opaque type Latitude = Double
  opaque type Longitude = Double
  opaque type CityName = String
  opaque type Date = String
  opaque type Temperature = Double

  // Define case classes to represent different entities in the data model

  // Represents geographical coordinates
  case class Coordinates(latitude: Latitude, longitude: Longitude)

  // Represents information about a city
  case class City(name: CityName, coordinates: Coordinates)

  // Represents the current weather conditions
  case class CurrentWeather(coordinates: Coordinates,
                            temperature: Temperature,
                            weatherDescription: WeatherStatus,
                            date: Date)

  // Represents a recommendation based on temperature range
  case class TemperatureRecommendation(
                                        temperatureRange: String,
                                        minTemperature: Option[Double],
                                        maxTemperature: Option[Double],
                                        recommendation: String
                                      )

  // Enumeration representing different weather statuses
  enum WeatherStatus:
    case Sunny, Rainy, Snowy, Cloudy, Clear, PartlyCloudy
}
