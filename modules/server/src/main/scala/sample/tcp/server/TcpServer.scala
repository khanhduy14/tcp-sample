package sample.tcp.server

import akka.actor.{Actor, Props}
import akka.io.{IO, Tcp}
import sample.tcp.handler.SimplisticHandler
import sample.tcp.handler.repository.ItemRepository

import java.net.InetSocketAddress

object TcpServer {
  def props(remote: InetSocketAddress, repository: ItemRepository) =
    Props(new TcpServer(remote, repository))
}

class TcpServer(remote: InetSocketAddress, repository: ItemRepository) extends Actor {

  import Tcp._
  import context.system

  IO(Tcp) ! Bind(self, remote)

  def receive = {
    case b@Bound(localAddress) =>
      context.parent ! b

    case CommandFailed(_: Bind) ⇒ context stop self

    case c@Connected(remote, local) =>
      println(s"Client connected - Remote(Client): ${remote.getAddress} Local(Server): ${local.getAddress}")
      val pros: Props = Props(new SimplisticHandler(repository))
      val handler = context.actorOf(pros)
      val connection = sender()
      connection ! Register(handler)
  }

}