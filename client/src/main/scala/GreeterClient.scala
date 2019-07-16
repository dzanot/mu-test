package example

import cats.effect.{IO, IOApp, Resource, Sync}
import cats.implicits._
import higherkindness.mu.rpc.{ChannelFor, ChannelForAddress}
import higherkindness.mu.rpc.config._
import higherkindness.mu.rpc.config.channel._
import io.chrisdavenport.log4cats.Logger
import io.chrisdavenport.log4cats.slf4j.Slf4jLogger
import rpc.pkg.protocol.{Greeter, HelloRequest, HelloResponse}

trait GreeterClient[F[_]] {
  def sayHello(p: HelloRequest): F[HelloResponse]
}

class GreeterHandler[F[_]: Sync: Logger](client: Resource[F, Greeter[F]]) extends GreeterClient[F] {
  override def sayHello(req: HelloRequest): F[HelloResponse] =
    client
      .use(_.SayHello(req))
      .flatMap(res => Logger[F].info(s"Response received $res").as(res))
}

object GreeterClient {
  val ec = scala.concurrent.ExecutionContext.global

  implicit val timer = cats.effect.IO.timer(ec)
  implicit val cs = cats.effect.IO.contextShift(ec)

  implicit lazy val logger: Logger[IO] = Slf4jLogger.getLogger[IO]


  lazy val channelFor: ChannelFor = ChannelForAddress("localhost", 8080)
  lazy val greeter: Resource[IO, Greeter[IO]] = Greeter.client[IO](channelFor)
  lazy val client = new GreeterHandler[IO](greeter)
}