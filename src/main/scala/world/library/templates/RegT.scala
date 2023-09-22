package world.library.templates

case object RegT extends Template {
  val content: String =
    s"""
       |<form action="/registrtion" method="get">
       |  <div>
       |    <label for="name">Enter your name: </label>
       |    <input type="text" name="name" id="name" required />
       |  </div>
       |  <div>
       |    <label for="email">Enter your email: </label>
       |    <input type="email" name="email" id="email" required />
       |  </div>
       |  <div>
       |    <input type="submit" value="Subscribe!" />
       |  </div>
       |</form>
       |""".stripMargin

  val currentHtml: String = html(content)
}
