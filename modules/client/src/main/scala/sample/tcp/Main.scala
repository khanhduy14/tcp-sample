package sample.tcp

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.util.ByteString
import org.apache.logging.log4j.scala.Logging
import sample.tcp.client.TcpClient

import java.net.InetSocketAddress

object Main extends Logging {
  def main(args: Array[String]): Unit = {
    val host = "localhost"
    val port = 9900
    println(s"Started client! connecting to ${host}:${port}")

    val clientProps = TcpClient.props(new InetSocketAddress(host, port), null)

    val actorSystem: ActorSystem = ActorSystem.create("MyActorSystem")
    val clientActor: ActorRef = actorSystem.actorOf(clientProps)
    println("Please input isbn value or press (q) to exit!")
    while (true) {
      val input = scala.io.StdIn.readLine()
      clientActor ! ByteString(input)
    }
  }
}