package world.library.data.auth

case class RegData(login: String, firstname: Option[String], surname: Option[String], password: String, email: String,
                   tg: Option[String], favBooks: Option[String], origlang: Option[Short], langs: Option[String],
                   location: Option[String])
