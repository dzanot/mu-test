package example

import cats.effect.{IO, Resource, Sync}
import cats.implicits._
import higherkindness.mu.rpc.{ChannelFor, ChannelForAddress}
import io.chrisdavenport.log4cats.Logger
import io.chrisdavenport.log4cats.slf4j.Slf4jLogger
import rpc.pkg.protocol._

trait GreeterClient[F[_]] {
  def echoCoproduct(req: CoproductRequest): F[CoproductRequest]
  def echoNonlikeCoproduct(req: NonlikeCoproductRequest): F[NonlikeCoproductRequest]
  def echoInt(req: IntRequest): F[IntRequest]
  def echoBool(req: BoolRequest): F[BoolRequest]
  def echoDouble(req: DoubleRequest): F[DoubleRequest]
  def echoString(req: StringRequest): F[StringRequest]

  def sayHello(req: HelloRequest): F[HelloResponse]
}

class GreeterHandler[F[_]: Sync: Logger](client: Resource[F, Greeter[F]]) extends GreeterClient[F] {
  override def sayHello(req: HelloRequest): F[HelloResponse] =
    client
      .use(_.SayHello(req))
      .flatMap(res => Logger[F].info(s"SayHello response received $res").as(res))

  override def echoCoproduct(req: CoproductRequest): F[CoproductRequest] =
    client
    .use(_.EchoCoproduct(req))
    .flatMap(res => Logger[F].info(s"Coproduct Request response received $res").as(res))

  override def echoNonlikeCoproduct(req: NonlikeCoproductRequest): F[NonlikeCoproductRequest] =
    client
      .use(_.EchoNonlikeCoproduct(req))
      .flatMap(res => Logger[F].info(s"NonlikeCoproduct Request response received $res").as(res))

  override def echoInt(req: IntRequest): F[IntRequest] =
    client
      .use(_.EchoInt(req))
      .flatMap(res => Logger[F].info(s"Int Request response received $res").as(res))

  override def echoBool(req: BoolRequest): F[BoolRequest] =
    client
      .use(_.EchoBool(req))
      .flatMap(res => Logger[F].info(s"Bool Request response received $res").as(res))

  override def echoDouble(req: DoubleRequest): F[DoubleRequest] =
    client
      .use(_.EchoDouble(req))
      .flatMap(res => Logger[F].info(s"Double Request response received $res").as(res))

  override def echoString(req: StringRequest): F[StringRequest] =
    client
      .use(_.EchoString(req))
      .flatMap(res => Logger[F].info(s"String Request response received $res").as(res))
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