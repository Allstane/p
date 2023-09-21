package world.library.data

case class Chapter(id: Int, book: Int, title: String, head: Option[Int], txt: Option[String])

object Chapter {
  //Apply for a version of a book based on existing one
  def apply(book: String, chapterId: Int, newBook: Int, head: Option[Int]): Chapter = {
    val symbol = "{"
    val endTitleSymbol = "|"
    val begin = book.indexOf(symbol)
    val cuttedBook = book.substring(begin+1)
    val end = cuttedBook.indexOf(symbol)
    val endTitle = cuttedBook.indexOf(endTitleSymbol)
    val title = cuttedBook.substring(0, endTitle).trim
    val txt = cuttedBook.substring(endTitle + 1, end).trim
    val forTxt = txt match
      {case "" => None
      case _ => Some(txt)}
    Chapter(chapterId, newBook, title, head, forTxt)
  }

  //Apply for a first version of a book without chapter.heads
  def apply(book: String, chapterId: Int, newBook: Int): Chapter = {
    val symbol = "{"
    val endTitleSymbol = "|"
    val begin = book.indexOf(symbol)
    val cuttedBook = book.substring(begin+1)
    val end = cuttedBook.indexOf(symbol)
    val endTitle = cuttedBook.indexOf(endTitleSymbol)
    val title = cuttedBook.substring(0, endTitle).trim
    val txt = cuttedBook.substring(endTitle + 1, end).trim
    val forTxt = txt match
    {case "" => None
      case _ => Some(txt)}
    Chapter(chapterId, newBook, title, None, forTxt)
  }
}