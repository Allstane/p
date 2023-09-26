package world.library.templates

import world.library.data.{Book, Chapter, MetabookF}

case class BooksT(leftBook: Book, rightBook: Book, metabookF: MetabookF, l: Chapter, r: Chapter) extends Template {

  def restBooks(mF: MetabookF, isLeft: Boolean, bid: Int): String =
    s"<select name='books' id='$isLeft'>" +
      mF.books.filter(_.id != bid).map(b => s"<option value='${b.id}'>${b.title}</option>").mkString +
      "</select>"

  val content: String = {
    val table: String = s"<p align='center'> <table width='1000' height='100%'> " +
      s"<tr valign='top'> " +
      s"<td align='left' width='49%'> <button> Ok </button> ${restBooks(metabookF, isLeft = true, leftBook.id)} </td> " +
      "<td width='2%'></td>" +
      s"<td align='right' width='49%'> <form  id='books-false'> ${restBooks(metabookF, isLeft = false, rightBook.id)} " +
      s"<button> Ok </button> </form> </td> </tr>" +
      s"<tr align='center'> <td> ${leftBook.title} </td><td></td><td> ${rightBook.title} </td> </tr>"
    val chapters: String = s"<tr> <td>${l.title}</td><td></td><td>${r.title}</td></tr>" +
      s"<tr valign='top'> <td align='justify'>${l.txt.getOrElse("")}</td><td></td><td align='justify'>${r.txt.getOrElse("")}</td> </tr>"
    table + chapters + "</table></p>"
  }

  val currentHtml: String = html(content)
}
