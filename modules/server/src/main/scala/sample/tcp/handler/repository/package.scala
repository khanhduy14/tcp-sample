package sample.tcp.handler

import com.github.tminglei.slickpg.{ExPostgresProfile, PgArraySupport, PgCirceJsonSupport}


package object repository {
  trait PostgresProfile extends ExPostgresProfile with PgArraySupport with PgCirceJsonSupport {
    object PostgresAPI extends API with ArrayImplicits with JsonImplicits with CirceJsonPlainImplicits

    override val api = PostgresAPI

    def pgjson = "jsonb"
  }

  object PostgresProfile extends PostgresProfile
}
