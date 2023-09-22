package world.library.data

case class BookF(book: Book, chapters: List[Chapter]) {
  val size: Int = chapters.size
  val sizeNonEmpty: Int = chapters.count(_.txt.isDefined)
  val nonEmptyChapters: List[Chapter] = chapters.filter(_.txt.isDefined)
}