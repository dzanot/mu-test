package example

import cats.effect.{Sync, Timer}
import cats.syntax.functor._
import io.chrisdavenport.log4cats.Logger
import rpc.pkg.protocol
import rpc.pkg.protocol.Greeter

class GreeterServiceHandler[F[_]:Timer](implicit F: Sync[F], L: Logger[F]) extends Greeter[F] {

  val serviceName = "Greeter"

  override def SayHello(req: protocol.HelloRequest): F[protocol.HelloResponse] = {
    println("Hi dayvus")
    L.info(s"$serviceName - Request: $req").as(protocol.HelloResponse("hi"))
  }
}