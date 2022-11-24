package sample.tcp.guice

import org.apache.logging.log4j.scala.Logging
import sample.tcp.config.DatabaseConfiguration
import slick.jdbc
import slick.jdbc.JdbcBackend

import java.util.concurrent.atomic.AtomicReference
import javax.inject.{Inject, Provider}

class PostgresClientProvider @Inject() (configuration: DatabaseConfiguration)  extends Provider[JdbcBackend.DatabaseDef] with AutoCloseable with Logging with Serializable {
  private val client: AtomicReference[JdbcBackend.DatabaseDef] = new AtomicReference[JdbcBackend.DatabaseDef]()

  override def get(): jdbc.JdbcBackend.DatabaseDef = {
    client.updateAndGet {
      oldClient =>
        if (oldClient == null) {
          DefaultClientCreator.createClient(configuration)
        }
        else {
          oldClient
        }
    }
  }

  override def close(): Unit = {
    Option(client.get()).foreach(_.close())
    logger.info("closed aurora clients")
  }
}