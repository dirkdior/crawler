
case class Elements(
  userId: Int,
  b: String
)

object ListEx extends App {
  var randomList: List[Elements] = List(
    Elements(1, "one"),
    Elements(1, "two"),
    Elements(2, "three"),
    Elements(1, "four"),
    Elements(2, "five"),
    Elements(1, "six"),
    Elements(1, "seven"),
    Elements(2, "eight"),
    Elements(2, "nine")
  )

  def getElement(id: Int): List[Elements] = {
    elementMap.get(id) match {
      case Some(elements) => elements
      case None           => Nil
    }
  }

  var elementMap = Map[Int, List[Elements]]()
  def setElementMap( map: Map[Int, List[Elements]] ): Unit = {
    elementMap = map
  }

  def setEntries(x: List[Elements]): Unit = {
    setElementMap(
      x.foldLeft(Map.empty[Int, List[Elements]]) {
        case (m, element) =>
          m.get(element.userId) match {
            case Some(elements) => m.updated(element.userId, element :: elements)
            case None           => m.updated(element.userId, List(element))
          }
      }
    )
  }

  setEntries(randomList)

  elementMap foreach println

  println(getElement(3))

  val consList = "A" :: "B" :: Nil

  println(consList)
}
