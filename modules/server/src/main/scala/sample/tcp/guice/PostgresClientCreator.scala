package sample.tcp.guice

import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import sample.tcp.config.DatabaseConfiguration
import slick.jdbc.JdbcBackend._
import slick.jdbc.hikaricp.HikariCPJdbcDataSource
import slick.util.AsyncExecutor

object PostgresClientCreator {
  private val DefaultPostgresDriverClassName: String = "org.postgresql.Driver"
  private val DefaultPoolName: String = "PoolName"
  private val DefaultMaximumPoolSize: Int = 10
  private val DefaultNumThreads: Int = 10
  private val DefaultConnectionTimeout: Long = 30000

  def forConfig(configuration: DatabaseConfiguration): DatabaseDef = {
    val hikariConf = new HikariConfig()
    hikariConf.setJdbcUrl(configuration.jdbcUrl)
    hikariConf.setDriverClassName(DefaultPostgresDriverClassName)
    hikariConf.setUsername(configuration.username)
    hikariConf.setPassword(configuration.password)
    hikariConf.setPoolName(DefaultPoolName)
    hikariConf.setMaximumPoolSize(DefaultMaximumPoolSize)
    hikariConf.setMinimumIdle(DefaultNumThreads)
    hikariConf.setConnectionTimeout(DefaultConnectionTimeout)
    val ds = new HikariDataSource(hikariConf)
    val hikariJdbcDatasource = new HikariCPJdbcDataSource(ds, hikariConf)
    val executor = AsyncExecutor(DefaultPoolName,
      DefaultNumThreads,
      DefaultNumThreads,
      1000,
      DefaultNumThreads,
      registerMbeans = false)
    slick.jdbc.JdbcBackend.Database.forSource(hikariJdbcDatasource, executor)
  }
}

trait PostgresClientCreator {
  def createClient(configuration: DatabaseConfiguration): DatabaseDef
}

object DefaultClientCreator extends PostgresClientCreator {
  override def createClient(configuration: DatabaseConfiguration): DatabaseDef = {
    PostgresClientCreator.forConfig(configuration)
  }
}
