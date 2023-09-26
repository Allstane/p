package world.library.templates

import world.library.data.{Book, Chapter, MetabookF}

case class BooksT(leftBook: Book, rightBook: Book, metabookF: MetabookF, l: Chapter, r: Chapter)
  extends Template {

  def restBooks(mF: MetabookF, isLeft: Boolean, bid: Int): String =
    s"<select name='books' id='$isLeft'>" +
      mF.books.filter(_.id != bid).map(b => s"<option value='${b.id}'>${b.title}</option>").mkString +
      "</select>"

  val content: String = {
    val table: String = s"<p align='center'> <table width='800' height='100%'> " +
      s"<tr align='center'> <td width='45%'> ${restBooks(metabookF, isLeft = true, leftBook.id)} </td> " +
      s"<td width='45%'> ${restBooks(metabookF, isLeft = false, rightBook.id)} </td> </tr>" +
      s"<tr align='center'> <td width='45%'> ${ leftBook.title} </td> <td width='45%'> ${rightBook.title} </td> </tr>"
    val chapters: String = s"<tr> <td>${l.title}</td> <td>${r.title}</td>  </tr>" +
      s"<tr valign='top'> <td align='justify'>${l.txt.getOrElse("")}</td> <td align='justify'>${r.txt.getOrElse("")}</td> </tr>"
    table + chapters + "</table></p>"
  }

  val currentHtml: String = html(content)
}
