package sample.tcp

import akka.actor.{ActorRef, ActorSystem}
import akka.util.ByteString
import com.google.inject.{Guice, Module}
import net.codingwell.scalaguice.InjectorExtensions.ScalaInjector
import sample.tcp.handler.repository.ItemRepository
import sample.tcp.module.MainModule
import sample.tcp.server.TcpServer

import java.net.InetSocketAddress

object Main {
  val AllModules: Seq[Module] = Seq(MainModule)
  private val injector = Guice.createInjector(AllModules: _*)

  def main(args: Array[String]): Unit = {
    if (args.length < 2) {
      println("Insufficient arguments. You need to pass both host and port")
    } else {
      try {
        val repository = injector.instance[ItemRepository]
        val host = args.apply(0)
        val port = args.apply(1).toInt
        println(s"Server started! listening to ${host}:${port}")

        val serverProps = TcpServer.props(new InetSocketAddress(host, port), repository)
        val actorSystem: ActorSystem = ActorSystem.create("MyActorSystem")
        val serverActor: ActorRef = actorSystem.actorOf(serverProps)
        serverActor ! ByteString("Starting server...")
      } catch {
        case ex: Throwable => println(ex)
      }
    }
  }
}