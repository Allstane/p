package ru.kapinuss.lib.templates

import ru.kapinuss.lib.data.Book

object Index {
  def template(books: List[Book]): String =
    s"""
      |<html>
      |<head>
      |<meta http-equiv="Content-Type" content="text/html; charset="UTF-8">
      |<title>Alefowl.com: bilingual books</title></head>
      |<body>
      |<header align='center'> Alefowl.com </p>
      |<p align='center'>Книжки: ${books.size}</p>
      |${booksComponent(books)}
      |<p align='center'> </p>
      |<footer> </footer>
      |</body>
      |</html>
      |""".stripMargin

  def booksComponent(bs: List[Book]): String = bs.map(b => s"<p>${b.title}</p>").mkString
}
