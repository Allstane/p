package world.library.data.util

sealed trait AuthMethod

case class Login(username: String, password: String) extends AuthMethod

case class PrivateKey(pkFile: java.io.File) extends AuthMethod

case class DatabaseCreds(url: String, login: String, password: String)

case class Config(host: String, port: Int, useHttps: Boolean, authMethods: List[AuthMethod], db: DatabaseCreds)