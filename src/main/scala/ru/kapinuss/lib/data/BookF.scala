package ru.kapinuss.lib.data

case class BookF(book: Option[Book], chapters: List[Chapter]) {
  def size: Int = chapters.size
  def sizeNonEmpty: Int = chapters.count(_.txt.isDefined)
}