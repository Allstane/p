package world.library.templates

import world.library.data.{Book, Creator, Metabook}

object Index {
  def template(books: List[Book], metabooks: List[Metabook], authors: List[Creator]): String =
    s"""
       |<html>
       |<head>
       |<meta http-equiv="Content-Type" content="text/html; charset="UTF-8">
       |<title>Alefowl.com: bilingual books</title></head>
       |<body>
       |<header align='center'> Alefowl.com </p>
       |<p align='center'>Книжки: ${books.size}</p>
       |${booksComponent(books, metabooks, authors)}
       |<p align='center'> </p>
       |<footer> </footer>
       |</body>
       |</html>
       |""".stripMargin

  def booksComponent(bs: List[Book], ms: List[Metabook], authors: List[Creator]): String = ms.map(m => {
    val ul: String = s"<p align='center'> <table width='600'> <tr> <td> " +
      s"${m.title} (${authors.find(a => a.id == m.author).get.english_name.get})<ul>"
    val li: String = bs.filter(b => b.metabook == m.id).map(b => s"<li>${b.title} (${b.author})</li>").mkString
    ul + li + "</ul></td></tr></table></p>"
  }
  ).mkString
}
