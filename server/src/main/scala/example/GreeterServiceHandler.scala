package example

import cats.effect.{Sync, Timer}
import cats.syntax.functor._
import io.chrisdavenport.log4cats.Logger
import rpc.pkg.protocol
import rpc.pkg.protocol.Greeter

class GreeterServiceHandler[F[_]: Timer](implicit F: Sync[F], L: Logger[F]) extends Greeter[F] {

  val serviceName = "Greeter"

  private def echo[T](req: T): F[T] = L.info(s"$serviceName - Request: $req").as(req)

  override def EchoCoproduct(req: protocol.CoproductRequest): F[protocol.CoproductRequest] =
    echo(req)

  override def EchoNonlikeCoproduct(req: protocol.NonlikeCoproductRequest): F[protocol.NonlikeCoproductRequest] =
    echo(req)

  override def EchoInt(req: protocol.IntRequest): F[protocol.IntRequest] =
    echo(req)

  override def EchoBool(req: protocol.BoolRequest): F[protocol.BoolRequest] =
    echo(req)

  override def EchoDouble(req: protocol.DoubleRequest): F[protocol.DoubleRequest] =
    echo(req)

  override def EchoString(req: protocol.StringRequest): F[protocol.StringRequest] =
    echo(req)

  override def SayHello(req: protocol.HelloRequest): F[protocol.HelloResponse] = {
    L.info(s"$serviceName - Request: $req").as(protocol.HelloResponse(s"hi"))
  }
}