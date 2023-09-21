package world.library.templates

import world.library.data.{Book, Creator, Metabook}

case class Index(books: List[Book], metabooks: List[Metabook], authors: List[Creator]) extends Template {
  val content: String = metabooks.map(m => {
    val ul: String = s"<p align='center'> <table width='600'> <tr> <td> " +
      s"${m.title} (${authors.find(a => a.id == m.author).get.english_name.get})<ul>"
    val li: String = books.filter(b => b.metabook == m.id)
      .map(b => s"<li><a href='/${b.id}/${b.id}/1'>${b.title}</a> (${b.author})</li>").mkString
    ul + li + "</ul></td></tr></table></p>"
  }
  ).mkString

  val currentHtml: String = html(content)
}
