package world.library.templates

import world.library.data.BookF

case class BookT(bookF: BookF) extends Template {
  val content: String = {
    val table: String = s"<p align='center'> <table width='800'> " +
      s"<tr> <td> ${bookF.book.title} </td> </tr>"
    val chapters: String = bookF.chapters.map( ch =>
      s"<tr> <td>${ch.title}</td> </tr>" +
      s"<tr valign='top'> <td align='justify'>${ch.txt.getOrElse("")}</td> </tr>"
    ).mkString
    table + chapters + "</table></p>"
  }

  val currentHtml: String = html(content)
}
