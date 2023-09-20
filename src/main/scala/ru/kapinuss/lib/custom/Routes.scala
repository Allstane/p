package ru.kapinuss.lib.custom

import cats.effect.IO
import doobie.hikari.HikariTransactor
import org.http4s.dsl.io.{->, /, GET, Ok, Root}
import org.http4s.HttpRoutes
import org.http4s.dsl.io._
import ru.kapinuss.lib.custom.DAO._
import play.twirl.api.Html
import ru.kapinuss.lib.templates.Index.template
import org.http4s.twirl._

object Routes {

  def htmlRoutes(xa: HikariTransactor[IO]): HttpRoutes[IO] = {

    val books = getBooks(0)(xa).sortBy(_.metabook)

    HttpRoutes.of[IO] {
      case GET -> Root => Ok(Html(template(books)))
      case GET -> Root / IntVar(lbId) / IntVar(rbId) / IntVar(chId) => Ok(s"$lbId $rbId $chId")
    }
  }
}
