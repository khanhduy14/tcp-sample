package sample.tcp

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.util.ByteString
import org.apache.logging.log4j.scala.Logging
import sample.tcp.client.TcpClient

import java.net.InetSocketAddress

object Main extends Logging {
  def main(args: Array[String]): Unit = {
    if (args.length < 2) {
      println("Insufficient arguments. You need to pass both host and port")
    } else {
      try {
        val host = args.apply(0)
        val port = args.apply(1).toInt
        println(s"Started client! connecting to ${host}:${port}")

        val clientProps = TcpClient.props(new InetSocketAddress(host, port), null)

        val actorSystem: ActorSystem = ActorSystem.create("MyActorSystem")
        val clientActor: ActorRef = actorSystem.actorOf(clientProps)
        println("Please input isbn value!")
        while (true) {
          val input = scala.io.StdIn.readLine()
          clientActor ! ByteString(input)
        }
      } catch {
        case ex: Throwable => println(ex)
      }
    }
  }
}