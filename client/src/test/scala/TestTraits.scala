package example

import cats.effect.{IO, Resource}
import higherkindness.mu.rpc.{ChannelFor, ChannelForAddress}
import io.chrisdavenport.log4cats.noop.NoOpLogger
import rpc.pkg.protocol.Greeter

trait Runtime {
  lazy val ec = scala.concurrent.ExecutionContext.global

  implicit lazy val timer = cats.effect.IO.timer(ec)
  implicit lazy val cs = cats.effect.IO.contextShift(ec)

  implicit lazy val logger = NoOpLogger.impl[IO]
}

trait Client {
  this: Runtime =>

  lazy val channelFor: ChannelFor = ChannelForAddress("localhost", 8080)
  lazy val greeterClient: Resource[IO, Greeter[IO]] = Greeter.client[IO](channelFor)
  lazy val client = new GreeterHandler[IO](greeterClient)
}