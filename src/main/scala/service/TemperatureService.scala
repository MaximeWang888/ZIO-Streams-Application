package service

import zio._
import zio.http._
import zio.stream._
import model.DataModel._
import zio.Duration


trait TemperatureService {
  def getTemperature(city: CityName): ZStream[Client, Throwable, Double]
}

object TemperatureService {
  def live: ZLayer[Has[Client], Nothing, Has[TemperatureService]] =
    ZLayer.fromFunction { client =>
      new TemperatureService {
        val baseUrl = URL
          .decode("https://api.openweathermap.org/data/2.5/weather")
          .toOption
          .get

        def getTemperature(city: CityName): ZStream[Client, Throwable, Double] = {
          ZStream
            .fromEffect(CoordinatesService.getCityToCoord(city))
            .flatMap(coord => fetchData(coord.latitude.toString, coord.longitude.toString))
            .mapM(res => res.body.asString.map(body => decodeJson[CurrentWeather](body)))
            .collect { case Right(weather) => weather.temperature }
        }

        private def fetchData(latitude: String, longitude: String): ZStream[Client, Throwable, Response] = {
          val url = baseUrl
            .addQueryParams(
              QueryParams(
                Map(
                  "lat" -> Chunk(latitude),
                  "lon" -> Chunk(longitude),
                  "appid" -> Chunk("d1973fd69722dbf4497b5701fb60f939")
                )
              )
            )

          ZStream.fromEffect(client.get(url))
        }
      }
    }

  def getTemperature(city: CityName): ZStream[Has[TemperatureService] with Has[Client], Throwable, Double] =
    ZStream.accessStream(_.get[Has[TemperatureService]].get.getTemperature(city))
}
