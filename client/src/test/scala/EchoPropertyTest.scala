package example


import org.scalacheck.{Arbitrary, Properties, Test}
import rpc.pkg.protocol._
import org.scalacheck.Prop.{BooleanOperators, forAll}
import org.scalacheck.ScalacheckShapeless._
import shapeless._



class EchoPropertyTest extends Properties("Echo") with  Runtime with Client {
  type BIS = Boolean :+: Int :+: String :+: CNil
  implicitly[Arbitrary[CoproductRequest]]
  implicitly[Arbitrary[NonlikeCoproductRequest]]

  override def overrideParameters(p: Test.Parameters): Test.Parameters = {
    p.withMinSize(1000).withMaxSize(2000).withMinSuccessfulTests(1000)
  }

  property("int") = forAll { req: IntRequest =>
    val res = client.echoInt(req).unsafeRunSync()
    ("res = " + res) |: ("req = " + req) |: (res == req)
  }
  property("bool") = forAll { req: BoolRequest =>
    val res = client.echoBool(req).unsafeRunSync()
    ("res = " + res) |: ("req = " + req) |: (res == req)
  }
  property("double") = forAll { req: DoubleRequest =>
    val res = client.echoDouble(req).unsafeRunSync()
    ("res = " + res) |:  ("req = " + req) |: (res == req)
  }
  property("string") = forAll { req: StringRequest =>
    val res = client.echoString(req).unsafeRunSync()
    ("res = " + res) |: ("req = " + req) |: (res == req)
  }
  property("coproduct") = forAll { req: CoproductRequest =>
    val res = client.echoCoproduct(req).unsafeRunSync()
    ("res = " + res) |: ("req = " + req) |:  (res == req)
  }
  property("coproduct no empty string") = forAll { req: CoproductRequest =>
    (req.coproduct != Coproduct[BIS]("")) ==> {
      val res = client.echoCoproduct(req).unsafeRunSync()
      ("res = " + res) |: ("req = " + req) |: (res == req)
    }
  }
  property("nonlikecoproduct") = forAll { req: NonlikeCoproductRequest =>
    val res = client.echoNonlikeCoproduct(req).unsafeRunSync()
    ("res = " + res) |: ("req = " + req) |: (res == req)
  }
}
