package sample.tcp.handler

import akka.actor.Actor
import akka.io.Tcp
import akka.util.ByteString
import sample.tcp.handler.repository.ItemRepository

import javax.inject.Inject

class SimplisticHandler @Inject() (repository: ItemRepository) extends Actor {

  import Tcp._

  def receive = {
    case Received(data) =>
      println(s"Data received - ${data.utf8String}")
      val price = repository.findItemByISBN(data.utf8String).map(i => i.price).firstOptionL.runSyncUnsafe()
      if (price.isEmpty) sender() ! Write(ByteString("SERVER_RES: ").concat(ByteString(s"ISBN not found on server!")))
      else sender() ! Write(ByteString("SERVER_RES: ").concat(ByteString(s"[Server Reply to ISBN ${data.utf8String}]: [Price is ${price.getOrElse(0)}]")))
    case PeerClosed => context stop self
  }
}