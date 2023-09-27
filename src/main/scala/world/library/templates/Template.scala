package world.library.templates

trait Template {
  def html(content: String): String =
    s"""
       |<html>
       |<head>
       |<meta http-equiv="Content-Type" content="text/html; charset="UTF-8">
       |<title>Alefowl.com: bilingual books</title></head>
       |<body>
       |<header align='center'><a href='/'>Alefowl.com: bilingual books</a></p>
       |$content
       |<p align='center'> </p>
       |<footer>About Vacancies Policies <a href='/auth'>Login</a> <a href='/regform'>Registration</a></footer>
       |</body>
       |</html>
       |""".stripMargin

  val content: String

  val currentHtml: String
}
