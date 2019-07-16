package example

import org.scalatest.{FlatSpec, Matchers}
import rpc.pkg.protocol.CoproductRequest
import shapeless.{:+:, CNil, Coproduct}

class EchoTest extends FlatSpec with Matchers with Runtime with Client {
  type BIS = Boolean :+: Int :+: String :+: CNil

  def coproductRequest(s: String) = new CoproductRequest(Coproduct[BIS](s))
  def coproductRequest(b: Boolean) = new CoproductRequest(Coproduct[BIS](b))
  def coproductRequest(i: Int) = new CoproductRequest(Coproduct[BIS](i))

  "EchoCoproduct" should "round trip empty string" in {
    val req = coproductRequest("")
    client.echoCoproduct(req).unsafeRunSync() shouldBe req
  }
  it should "round trip non empty string" in {
    val req = coproductRequest("foo")
    client.echoCoproduct(req).unsafeRunSync() shouldBe req
  }
  it should "round trip an int" in {
    val req = coproductRequest(42)
    client.echoCoproduct(req).unsafeRunSync() shouldBe req
  }
  it should "round trip a zero int" in {
    val req = coproductRequest(0)
    client.echoCoproduct(req).unsafeRunSync() shouldBe req
  }
  it should "round trip true" in {
    val req = coproductRequest(true)
    client.echoCoproduct(req).unsafeRunSync() shouldBe req
  }
  it should "round trip false" in {
    val req = coproductRequest(false)
    client.echoCoproduct(req).unsafeRunSync() shouldBe req
  }
}
