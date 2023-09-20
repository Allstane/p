package ru.kapinuss.lib.data

case class Upload(id: Int,
                  book: Int,
                  owner: Int = 0,
                  txt: String,
                  result: String = "",
                  add_time: Long = 0,
                  req_size: Int = 0,
                  str_size: Int = 0)
