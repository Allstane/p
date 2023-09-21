package world.library.templates

import world.library.data.{Book, Chapter, Creator, Metabook}

case class Books(leftBook: Book, rightBook: Book, metabook: Metabook, author: Creator, currentChapters: List[Chapter])
  extends Template {
  val content: String = {
    val table: String = s"<p align='center'> <table width='600'> " +
      s"<tr> <td> ${leftBook.title} </td> <td> ${rightBook.title} </td> </tr>"
    val chapters: String = "Empty"
    table + chapters + "</table></p>"
  }

  val currentHtml: String = html(content)
}
