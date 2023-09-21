package world.library.data

case class Language(id: Short, self_name: Option[String], english: String, russian: Option[String], german: Option[String],
                    french: Option[String], spanish: Option[String], italian: Option[String])
