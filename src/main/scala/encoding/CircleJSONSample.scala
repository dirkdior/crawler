package encoding

import io.circe._, io.circe.generic.auto._, io.circe.parser._, io.circe.syntax._

object CircleJSONSample extends App {

//  sealed trait Foo
//  case class Bar(xs: Vector[String]) extends Foo
//  case class Qux(i: Int, d: Option[Double]) extends Foo
//
//  val foo: Foo = Qux(13, Some(14.0))
//
//  val json = foo.asJson.noSpaces
//  println(json)
//
//  val decodedFoo = decode[Foo](json)
//  println(decodedFoo)

  case class FAQs(
    segments: List[Segment]
  )
  case class Segment(
    header: String,
    topics: List[Topic]
  )
  case class Topic(
    header: String,
    content: String
  )

  val faqs = FAQs(
    segments = List(
      Segment(
        header = "Transactions",
        List(
          Topic(header = "How do I handle a failed transaction", content = "Some paragraph here..."),
          Topic(header = "How do I view my transaction history", content = "Some paragraph here...")
        )
      ),
      Segment(
        header = "Insurance",
        List(
          Topic(header = "Raising a claim", content               = "Some paragraph here..."),
          Topic(header = "What does the insurance cover", content = "Some paragraph here...")
        )
      )
    )
  )

  val json = faqs.asJson.noSpaces

  println(json)

}
