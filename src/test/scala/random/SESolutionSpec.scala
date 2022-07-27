package random

import org.scalatest._
import matchers.should._
import org.scalatest.flatspec.AnyFlatSpec

class SESolutionSpec extends AnyFlatSpec with Matchers {
  SESolution.order("t-shirt") shouldBe SESolution.Processed(3)
}
