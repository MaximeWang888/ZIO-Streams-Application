package service

import io.getquill._
import zio._
import zio.http._
import zio.stream._
import model.DataModel._

trait Database {
  def getCityToCoord(city: CityName): Task[Option[Coordinates]]
}

object Database {
  lazy val ctx = new H2JdbcContext(SnakeCase, "ctx")

  import ctx._

  def live: ZLayer[Any, Nothing, Has[Database]] =
    ZLayer.succeed {
      new Database {
        def getCityToCoord(city: CityName): Task[Option[Coordinates]] =
          ZIO.fromFuture { implicit ec =>
            ctx.run(quote(query[City].filter(_.name == lift(city)).map(_.coordinates)))
          }.map(_.headOption)
      }
    }

  def getCityToCoord(city: CityName): RIO[Has[Database], Option[Coordinates]] =
    ZIO.accessM(_.get.getCoordinates(city))
}