package world.library.templates

import world.library.data.{Book, Chapter, Creator, Metabook}

case class Books(leftBook: Book, rightBook: Book, metabook: Metabook, author: Creator, l: Chapter, r: Chapter)
  extends Template {
  val content: String = {
    val table: String = s"<p align='center'> <table width='800' height='100%'> " +
      s"<tr> <td> ${leftBook.title} </td> <td> ${rightBook.title} </td> </tr>"
    val chapters: String = s"<tr> <td>${l.title}</td> <td>${r.title}</td>  </tr>" +
      s"<tr valign='top'> <td align='justify'>${l.txt.getOrElse("")}</td> <td align='justify'>${r.txt.getOrElse("")}</td>  </tr>"
    table + chapters + "</table></p>"
  }

  val currentHtml: String = html(content)
}
