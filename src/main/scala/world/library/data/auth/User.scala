package world.library.data.auth

import cats.effect.IO
import org.http4s.Request
import org.typelevel.ci.CIString
import world.library.custom.Helper.hasher

import java.time
import scala.util.Random

case class User(id: Int, login: String, password: String, token: String, role: String = Subscriber.toString, ts: String) {
  def isAdmin: Boolean = this.role == Administrator.toString
}

object User {

  def ifUserExists(users: List[User], user: Credentials): Option[User] =
    users.find(t => t.login == user.login && t.password == hasher(user.password)) match {
      case Some(u) => Option(u.copy(id = 0, password = "", ts = time.LocalDateTime.now().toString))
      case None => None
    }

  def randomToken(size: Int = 256): String = Random.alphanumeric.filter(_.isLetter).take(size).mkString

  def getUser(users: List[User], request: Request[IO]): Option[User] = {
    val authValue: String = request.headers.get(CIString("Authorization")).get.head.value
    val user: Option[User] = users.find(t => t.token == authValue)
    user
  }
}
