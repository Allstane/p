package world.library.custom

import cats.effect.IO
import doobie.hikari.HikariTransactor
import org.http4s.dsl.io.{->, /, GET, Ok, Root}
import org.http4s.HttpRoutes
import org.http4s.dsl.io._
import DAO._
import play.twirl.api.Html
import world.library.templates.Index
import org.http4s.twirl._
import world.library.data.{Book, Creator, Metabook}

object Routes {

  def htmlRoutes(xa: HikariTransactor[IO]): HttpRoutes[IO] = {

    val books: List[Book] = getBooks()(xa).sortBy(_.metabook)
    val metabooks: List[Metabook] = getMetabooks(xa)
    val authors: List[Creator] = getAuthors(xa)

    HttpRoutes.of[IO] {
      case GET -> Root => Ok(Html(Index(books, metabooks, authors).currentHtml))
      case GET -> Root / IntVar(lbId) / IntVar(rbId) / IntVar(chId) => Ok(s"$lbId $rbId $chId")
    }
  }

  def techRoutes(version: String): HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root / "check" => Ok()
    case GET -> Root / "version" => Ok(version)
  }
}
