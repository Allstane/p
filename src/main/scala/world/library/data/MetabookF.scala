package world.library.data

case class MetabookF(metabook: Metabook, books: List[Book], tags: List[TagInUse] = List.empty[TagInUse])