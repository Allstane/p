package ru.kapinuss.lib.data

case class Metabook(id: Int, author: Int, language: Short, title: String, create_date: Short, size: Int, owner: Option[Int])
