package controller

import zio._
import zio.http._
import zio.stream._
import service.TemperatureService
import model.DataModel._
import zio.Duration


trait TemperatureController {
  def getTemperature(city: CityName): ZIO[Any, HttpError, Double]
}

object TemperatureController {
  def getTemperature(city: CityName): ZIO[Has[TemperatureService] with Has[Client], HttpError, Double] =
    TemperatureService.getTemperature(city).runHead.flatMap {
      case Some(temperature) => ZIO.succeed(temperature)
      case None => ZIO.fail(HttpError.NotFound(s"Temperature not found for city: $city"))
    }
}
