package world.library.custom

import doobie._
import doobie.implicits._
import cats.effect.{IO, Resource}
import cats.effect.unsafe.implicits.global
import doobie.hikari.HikariTransactor
import world.library.Main.{config, logger}
import world.library.data
import world.library.data.auth.{RegData, User}
import world.library.data.{Book, BookF, Chapter, Creator, Language, Metabook, MetabookF, Note, RawText, Tag, TagInUse}

object DAO {

  val transactor: Resource[IO, HikariTransactor[IO]] =
    for {
      ce <- ExecutionContexts.fixedThreadPool[IO](8)
      xa <- HikariTransactor
        .newHikariTransactor[IO]("org.postgresql.Driver", config.db.url, config.db.login, config.db.password, ce)
    } yield xa

  def getLangs(h: HikariTransactor[IO]): IO[List[Language]] = sql"select * from languages".query[Language].to[List].transact(h)

  def getAuthor(id: Int)(h: HikariTransactor[IO]): IO[Option[Creator]] =
    sql"select * from creators where id = $id;".query[Creator].option.transact(h)

  def getAuthors(h: HikariTransactor[IO]): List[Creator] =
    sql"select * from creators where is_author = true;".query[Creator].to[List].transact(h).unsafeRunSync()

  def getChapters(bId: Int)(h: HikariTransactor[IO]): IO[List[Chapter]] =
    sql"select id, book, title, head, txt from chapters where book = $bId order by id".query[Chapter].to[List].transact(h)

  def getChapters(h: HikariTransactor[IO]): List[Chapter] =
    sql"select id, book, title, head, txt from chapters order by id"
      .query[Chapter].to[List].transact(h).unsafeRunSync()

  def getChapter(chId: Int, bId: Int)(h: HikariTransactor[IO]): Option[Chapter] =
    sql"select * from chapters where book = $bId and id = $chId"
      .query[Chapter].option.transact(h).unsafeRunSync()

  def getBook(id: Int)(h: HikariTransactor[IO]): IO[Option[Book]] =
    sql"select * from books where id = $id;".query[Book].option.transact(h)

  def getBooks(author: Int = 0)(h: HikariTransactor[IO]): List[Book] = {
    val query: Fragment = if (author == 0) sql"select * from books where is_visible = true"
    else sql"select * from books where metabook in (select id from metabooks where author = $author) and is_visible = true"
    query.query[Book].to[List].transact(h).unsafeRunSync()
  }

  def getBookForLang(bookId: Int, lang: Short)(h: HikariTransactor[IO]): IO[Option[Book]] =
    sql"select * from books where metabook=(select metabook from books where id=$bookId) AND language=$lang"
      .query[Book].option.transact(h)

  def insertChapter(ch: Chapter)(h: HikariTransactor[IO]): Int = {
    val txt = ch.txt match {
      case Some(x) => s"$x"
      case None => "null"
    }
    val title = ch.title.length match {
      case 0 => "null"
      case _ => s"${ch.title}"
    }
    sql"insert into chapters (book, id, title, head, txt) values (${ch.book}, ${ch.id}, $title, ${ch.head}, $txt);"
      .update.run.transact(h).unsafeRunSync()
  }

  def updateChapter(chapter: Chapter)(h: HikariTransactor[IO]): Int =
    if (chapter.txt.isDefined)
      sql"update chapters set txt = ${chapter.txt} WHERE id = ${chapter.id} and book = ${chapter.book}"
        .update.run.transact(h).unsafeRunSync()
    else 0

  def getBookF(id: Int)(h: HikariTransactor[IO]): IO[BookF] =
    getBook(id)(h).flatMap(book => getChapters(id)(h).map(chapters => data.BookF(book, chapters)))

  def insertCreator(c: Creator, owner: Int)(h: HikariTransactor[IO]): Int = {
    logger.info(s"Owner: $owner $c")
    sql"insert into creators(id, english_name, russian_name, german_name, original_language, birth_date, death_date, is_author, is_translator, owner) values ( (select max(id) from creators)+1 ,${c.english_name}, ${c.russian_name}, ${c.german_name}, ${c.original_language}, ${c.birth_date}, ${c.death_date}, ${c.is_author}, ${c.is_translator}, $owner);"
      .update.run.transact(h).unsafeRunSync()
  }

  def getCreators(owner: Int)(h: HikariTransactor[IO]): IO[List[Creator]] =
    sql"select * from creators where is_author = true and owner is null or owner = $owner".query[Creator].to[List].transact(h)

  def getAllPrivateBooks(h: HikariTransactor[IO]): IO[List[Book]] =
    sql"select * from books where owner is not null and is_visible = false".query[Book].to[List].transact(h)

  def getPrivateBooks(owner: Int)(h: HikariTransactor[IO]): IO[List[Book]] =
    sql"select * from books where owner = $owner and is_visible = false".query[Book].to[List].transact(h)

  def insertBook(b: Book, owner: Int)(h: HikariTransactor[IO]): Int = {
    logger.info(s"Inserting a book. Owner: $owner. ${b.toString}")
    sql"insert into books(id, metabook, language, title, author, translation_date, translator, is_ready, is_visible, owner) values ( (select max(id) from books)+1 , ${b.metabook}, ${b.language}, ${b.title}, ${b.author}, ${b.translation_date}, ${b.translator}, ${b.is_ready}, ${b.is_visible}, $owner)"
      .update.run.transact(h).unsafeRunSync()
  }

  def insertNotes(notes: List[Note])(h: HikariTransactor[IO]): Int = {
    val sql = "insert into notes (id, book, chapter, txt) values (?, ?, ?, ?)"
    Update[Note](sql).updateMany(notes).transact(h).unsafeRunSync()
  }

  def deleteBook(book: Int, owner: Int)(h: HikariTransactor[IO]): Int = {
    logger.info(s"Deleting a book. Owner: $owner. Book: $book.")
    sql"delete from books where id = $book and owner = $owner".update.run.transact(h).unsafeRunSync()
  }

  def deleteMetabook(metabook: Int, owner: Int)(h: HikariTransactor[IO]): Int = {
    logger.info(s"Deleting a metabook. Owner: $owner. Metabook: $metabook.")
    sql"delete from metabooks where id = $metabook and owner = $owner".update.run.transact(h).unsafeRunSync()
  }

  def deleteCreator(creator: Int, owner: Int)(h: HikariTransactor[IO]): Int = {
    logger.info(s"Deleting a creator. Owner: $owner. Creator: $creator.")
    sql"delete from creators where id = $creator and owner = $owner".update.run.transact(h).unsafeRunSync()
  }

  def insertMetabook(m: Metabook, owner: Int)(h: HikariTransactor[IO]): Int = {
    logger.info(s"Inserting metabook. Owner: $owner. ${m.toString}")
    sql"insert into metabooks(id, author, language, title, create_date, size, owner) values ( (select max(id) from metabooks)+1, ${m.author}, ${m.language}, ${m.title}, ${m.create_date}, ${m.size}, $owner)"
      .update.run.transact(h).unsafeRunSync()
  }

  def getMetabooks(owner: Int)(h: HikariTransactor[IO]): IO[List[Metabook]] =
    sql"select * from metabooks where owner is null or owner = $owner".query[Metabook].to[List].transact(h)

  def insertUser(r: RegData)(h: HikariTransactor[IO]): Int =
    sql"insert into users (login, firstname, surname, password, email, tg, favBooks, origlang, langs, location, token, role, ts) values (${r.login}, ${r.firstname}, ${r.surname}, ${r.password}, ${r.email}, ${r.tg}, ${r.favBooks}, ${r.origlang}, ${r.langs}, ${r.location}, '', 'Subscriber', '')"
      .update.run.transact(h).unsafeRunSync()

  def putChapter(ch: Chapter)(h: HikariTransactor[IO]): Int =
    sql"insert into chapters (book, id, title, txt) values (${ch.book}, ${ch.id}, ${ch.title}, ${ch.txt.getOrElse("")})"
      .update.run.transact(h).unsafeRunSync()

  def insertBookF(bF: BookF)(h: HikariTransactor[IO]): Unit =
    bF.chapters.foreach(ch => {
      val r = insertChapter(ch)(h)
      logger.info(s"Inserting into book_c ${ch.book} chapter ${ch.id}, result: $r.")
    })

  def insertUpload(rt: RawText)(h: HikariTransactor[IO]): Int =
    sql"insert into uploads (book, txt) values (${rt.book}, ${rt.txt})"
      .update.run.transact(h).unsafeRunSync()

  def getMetabooks(h: HikariTransactor[IO]): List[Metabook] =
    sql"select * from metabooks where owner is null".query[Metabook].to[List].transact(h).unsafeRunSync()

  def getMetabookF(book: Int, isBook: Boolean = true)(h: HikariTransactor[IO]): IO[MetabookF] = {

    def getMetabook(bookId: Int, isBook: Boolean)(h: HikariTransactor[IO]): IO[Option[Metabook]] =
      if (isBook) sql"select * from metabooks where id=(select metabook from books where id=$bookId)".query[Metabook].option.transact(h)
      else sql"select * from metabooks where id=$book".query[Metabook].option.transact(h)

    def getBooks(metabook: Int)(h: HikariTransactor[IO]): IO[List[Book]] =
      sql"select * from books where metabook = $metabook order by id".query[Book].to[List].transact(h)

    def getPublicTags(metabook: Int)(h: HikariTransactor[IO]): IO[List[TagInUse]] =
      sql"select chapter, owners_title, owners_description from tag_chapters left join tags on tags.id = tag_chapters.tag where tags.owner is null and tag_chapters.metabook=$metabook"
        .query[TagInUse].to[List].transact(h)

    for {
      metabook: Option[Metabook] <- getMetabook(book, isBook)(h)
      books: List[Book] <- metabook match {
        case Some(m) => getBooks(m.id)(h)
        case None => IO(List.empty)
      }
      tags: List[TagInUse] <- metabook match {
        case Some(m) => getPublicTags(m.id)(h)
        case None => IO(List.empty)
      }
    } yield data.MetabookF(metabook.get, books, tags)
  }

  def getBookSize(book: Int)(h: HikariTransactor[IO]): IO[Option[Int]] =
    sql"select count(*) from chapters where book = $book".query[Int].option.transact(h)

  def getNotes(book: Int)(h: HikariTransactor[IO]): IO[List[Note]] =
    sql"select * from notes where book = $book order by id".query[Note].to[List].transact(h)

  def getTags(metabook: Int)(h: HikariTransactor[IO]): IO[List[Tag]] =
    sql"select * from tags where id in (select id from tag_chapters where metabook = $metabook)".query[Tag].to[List].transact(h)

  def getUsers(h: HikariTransactor[IO]): List[User] =
    sql"select id, login, password, token, role, ts from users".query[User].to[List].transact(h).unsafeRunSync()
}
