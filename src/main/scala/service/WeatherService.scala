package service

class WeatherService {

  def getCitiesMatchingWeatherCondition(condition: String): CharSequence =
    s"""Les villes ayant de la "$condition" sont Paris, Boulogne, Villejuif."""

}
