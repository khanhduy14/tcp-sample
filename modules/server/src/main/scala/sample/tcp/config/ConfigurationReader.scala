package sample.tcp.config

import com.typesafe.config.{Config, ConfigFactory}
import org.apache.logging.log4j.scala.Logging


case class RawConfiguration(database: DatabaseConfiguration)
case class DatabaseConfiguration(username: String, password: String, jdbcUrl: String, port: String)

object ConfigurationReader extends Logging {

  import pureconfig._
  import pureconfig.generic.ProductHint
  import pureconfig.generic.auto._

  implicit def hint[A]: ProductHint[A] = ProductHint[A](ConfigFieldMapping(CamelCase, CamelCase))

  def load(): RawConfiguration = parseConfig(ConfigFactory.load())

  def parseConfig(cfg: Config): RawConfiguration = {
    ConfigSource.default.loadOrThrow[RawConfiguration]
  }
}