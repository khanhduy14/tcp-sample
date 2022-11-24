package sample.tcp.module

import com.google.inject.{AbstractModule, Scopes}
import net.codingwell.scalaguice.ScalaModule
import sample.tcp.config.{ConfigurationReader, DatabaseConfiguration, RawConfiguration}
import sample.tcp.guice.PostgresClientProvider

object MainModule extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    val configuration: RawConfiguration = ConfigurationReader.load()
    bind[DatabaseConfiguration].toInstance(configuration.database)

    bind[PostgresClientProvider].in(Scopes.SINGLETON)
    bind[_root_.slick.jdbc.JdbcBackend.DatabaseDef].toProvider[PostgresClientProvider]
  }
}
