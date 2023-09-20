package ru.kapinuss.lib.data

case class Tag(id: Int, owners_title: Option[String], english_title: Option[String], russian_title: Option[String],
               german_title: Option[String], owners_description: Option[String], english_description: Option[String],
               russian_description: Option[String], german_description: Option[String], colour: Option[String],
               owner: Option[Int])
