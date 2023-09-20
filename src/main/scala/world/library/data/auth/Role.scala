package world.library.data.auth

trait Role

case object Administrator extends Role {
  override def toString = "Administrator"
}

case object Contributor extends Role {
  override def toString = "Contributor"
}

case object Subscriber extends Role {
  override def toString = "Subscriber"
}

case object NonSubscriber extends Role {
  override def toString = "NonSubscriber"
}