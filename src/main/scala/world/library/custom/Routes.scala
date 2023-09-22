package world.library.custom

import cats.effect.IO
import doobie.hikari.HikariTransactor
import org.http4s.dsl.io.{->, /, GET, Ok, Root}
import org.http4s.HttpRoutes
import org.http4s.dsl.io._
import DAO._
import play.twirl.api.Html
import world.library.templates.{Books, Index, BookT}
import org.http4s.twirl._
import world.library.data.{Book, BookF, Chapter, Creator, Metabook}

object Routes {

  def htmlRoutes(implicit xa: HikariTransactor[IO]): HttpRoutes[IO] = {

    val books: List[Book] = getBooks().sortBy(_.metabook)
    val metabooks: List[Metabook] = getMetabooks(xa)
    val authors: List[Creator] = getAuthors(xa)

    HttpRoutes.of[IO] {
      case GET -> Root => Ok(Html(Index(books, metabooks, authors).currentHtml))
      case GET -> Root / IntVar(lbid) / IntVar(rbid) / IntVar(chid) =>
        val leftBook: Book = books.find(b => b.id == lbid).get
        val rightBook: Book = books.find(b => b.id == rbid).get
        val metabook: Metabook = metabooks.find(m => m.id == leftBook.metabook).get
        val author: Creator = authors.find(a => a.id == metabook.author).get
        val leftChapter: Chapter = getChapter(chid, lbid)(xa).get
        val rightChapter: Chapter = getChapter(chid, rbid)(xa).get
        Ok(Html(Books(leftBook, rightBook, metabook, author, leftChapter, rightChapter).currentHtml))
      case GET -> Root / IntVar(bid) =>
        val book: Book = books.find(b => b.id == bid).get
        val chapters: List[Chapter] = getChapters(bid)(xa)
        val bookF: BookF = BookF(book, chapters)
        Ok(Html(BookT(bookF).currentHtml))
    }
  }

  def techRoutes(version: String): HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root / "check" => Ok()
    case GET -> Root / "version" => Ok(version)
  }
}
