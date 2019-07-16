package example

import cats.effect._
import cats.syntax.flatMap._
import cats.syntax.functor._
import higherkindness.mu.rpc.server._
import io.chrisdavenport.log4cats.Logger
import io.chrisdavenport.log4cats.slf4j.Slf4jLogger
import io.grpc.ServerServiceDefinition
import rpc.pkg.protocol.Greeter
import collection.JavaConverters._

class GreeterServiceProgram[F[_]: ConcurrentEffect: Timer] {
  def runProgram(args: List[String]): F[ExitCode] = for {
    logger <- Slf4jLogger.fromName[F]("GreeterService")
    exitCode <- serverProgram()(logger)
  } yield exitCode
    def serverProgram()(implicit logger: Logger[F]): F[ExitCode] = {
      implicit val GS: GreeterServiceHandler[F] = new GreeterServiceHandler[F]
      for {
        greeterService <- Greeter.bindService[F]
        server <- GrpcServer.default[F](8080, List(AddService(greeterService)))
        _ <- logger.info("Server starting")
        exitCode <- GrpcServer.server(server).as(ExitCode.Success)
      } yield exitCode
    }
}
object Hello extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = {
    new GreeterServiceProgram[IO].runProgram(args)
  }
}

