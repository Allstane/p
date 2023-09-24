package world.library.custom

import cats.effect.IO
import doobie.hikari.HikariTransactor
import org.http4s.dsl.io.{->, /, GET, Ok, Root}
import org.http4s.HttpRoutes
import org.http4s.dsl.io._
import DAO._
import play.twirl.api.Html
import world.library.templates.{BookT, BooksT, IndexT, RegT}
import org.http4s.twirl._
import world.library.data.{Book, BookF, Chapter, Creator, Metabook}
import io.circe.generic.auto._
import org.http4s.circe.CirceEntityCodec._
import world.library.custom.Helper.hasher
import world.library.data.auth.RegData

object Routes {

  def htmlRoutes(implicit xa: HikariTransactor[IO]): HttpRoutes[IO] = {

    val books: List[Book] = getBooks().sortBy(_.metabook)
    val metabooks: List[Metabook] = getMetabooks
    val authors: List[Creator] = getAuthors

    HttpRoutes.of[IO] {
      case GET -> Root => Ok(Html(IndexT(books, metabooks, authors).currentHtml))
      case GET -> Root / IntVar(lbid) / IntVar(rbid) / IntVar(chid) =>
        val leftBook: Book = books.find(b => b.id == lbid).get
        val rightBook: Book = books.find(b => b.id == rbid).get
        val metabook: Metabook = metabooks.find(m => m.id == leftBook.metabook).get
        val author: Creator = authors.find(a => a.id == metabook.author).get
        val leftChapter: Chapter = getChapter(chid, lbid).get
        val rightChapter: Chapter = getChapter(chid, rbid).get
        Ok(Html(BooksT(leftBook, rightBook, metabook, author, leftChapter, rightChapter).currentHtml))
      case GET -> Root / IntVar(bid) =>
        val book: Book = books.find(b => b.id == bid).get
        val chapters: List[Chapter] = getChapters(bid)
        val bookF: BookF = BookF(book, chapters)
        Ok(Html(BookT(bookF).currentHtml))
      case GET -> Root / "regform" => Ok(Html(RegT.currentHtml))
      case request@POST -> Root / "registration" =>
        request.as[RegData].flatMap(r => { Ok(insertUser(r.copy(password = hasher(r.password)))) } )
    }
  }

  def techRoutes(version: String): HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root / "check" => Ok()
    case GET -> Root / "version" => Ok(version)
  }
}
