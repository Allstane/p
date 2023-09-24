package world.library.custom

import java.security.MessageDigest

object Helper {
  def hasher(s: String): String = MessageDigest.getInstance("SHA-256").digest(s.getBytes).mkString
}
