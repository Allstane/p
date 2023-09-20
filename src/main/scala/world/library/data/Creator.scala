package world.library.data

case class Creator(id: Int, english_name: Option[String], russian_name: Option[String], german_name: Option[String],
                   french_name: Option[String], original_language: Short, birth_date: Option[Short],
                   death_date: Option[Short], is_author: Boolean, is_translator: Boolean, owner: Option[Int])
