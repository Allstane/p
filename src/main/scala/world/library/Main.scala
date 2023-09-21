package world.library

import cats.effect.{ExitCode, IO, IOApp}
import com.comcast.ip4s._
import doobie.hikari.HikariTransactor
import org.http4s.Method
import org.http4s.ember.server._
import org.http4s.server.middleware._
import pureconfig.generic.auto._
import pureconfig.ConfigSource
import world.library.custom.DAO._
import org.slf4j.{Logger, LoggerFactory}
import world.library.custom.Routes.htmlRoutes
import world.library.data.util.Config

import scala.concurrent.duration.DurationInt


object Main extends IOApp {

  val logger: Logger = LoggerFactory.getLogger("")

  val config: Config =
    ConfigSource.resources("local.conf").load[Config].getOrElse(ConfigSource.default.load[Config].getOrElse(null))

  val version: String = getClass.getPackage.getImplementationVersion

  logger.info(s"Application Library-view v.$version started with $config")

  def run(args: List[String]): IO[ExitCode] = transactor.use { xa: HikariTransactor[IO] =>

    val corsService = CORS.policy
      .withAllowOriginAll
      .withAllowMethodsIn(Set(Method.GET, Method.POST))
      .withAllowCredentials(false)
      .withMaxAge(1.day)
      .apply(htmlRoutes(version)(xa).orNotFound)

    EmberServerBuilder.default[IO].withHost(Host.fromString(config.host).get).withPort(Port.fromInt(config.port).get)
      .withHttpApp(corsService).build.use(_ => IO.never).as(ExitCode.Success)
  }
}