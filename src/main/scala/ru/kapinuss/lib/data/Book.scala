package ru.kapinuss.lib.data

case class Book(id: Int, metabook: Int, language: Short, title: String, author: String, translation_date: Option[Short],
                translator: Option[Short], is_ready: Boolean, is_visible: Boolean, owner: Option[Int])
