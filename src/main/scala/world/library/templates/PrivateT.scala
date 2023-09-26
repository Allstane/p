package world.library.templates

case object PrivateT extends Template {

  val content: String =
    s"""
       |  <div align='center'><table align = 'center' height='800' width='500'>
       |  <tr><td>Личный кабинет</td></tr>
       |  <tr><td> Add author </td></tr>
       |  <tr><td> Add book </td></tr>
       |  <tr><td> Add metabook </td></tr>
       |  <tr><td> Add text </td></tr>
       |  </table>
       |  </div>
       |""".stripMargin

  val currentHtml: String = html(content)
}
