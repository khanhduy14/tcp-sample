package sample.tcp.client

import akka.actor.{Actor, ActorRef, Props}
import akka.event.Logging.ErrorLevel
import akka.io.{IO, Tcp}
import akka.util.ByteString

import java.net.InetSocketAddress

object TcpClient {
  def props(remote: InetSocketAddress, listener: ActorRef) =
    Props(new TcpClient(remote, listener))
}

class TcpClient(remote: InetSocketAddress, var listener: ActorRef) extends Actor {

  import Tcp._
  import context.system

  context.system.eventStream.setLogLevel(ErrorLevel)

  if (listener == null) listener = Tcp.get(context.system).manager

  IO(Tcp) ! Connect(remote)

  def receive = {
    case CommandFailed(_: Connect) =>
      listener ! "connect failed"
      context stop self

    case c@Connected(remote, local) =>
      listener ! c
      val connection = sender()
      connection ! Register(self)
      context become {
        case data: ByteString =>
          connection ! Write(data)
        case CommandFailed(w: Write) =>
          // O/S buffer was full
          listener ! "write failed"
        case Received(data) =>
          println(s"${data.utf8String}")
          println("Please input isbn value!")
          listener ! data
        case "close" =>
          connection ! Close
        case _: ConnectionClosed =>
          listener ! "connection closed"
          context stop self
      }
  }
}