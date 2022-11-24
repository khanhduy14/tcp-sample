package sample.tcp.handler.repository

import javax.inject.Inject
import sample.tcp.handler.repository.PostgresProfile.api._
import slick.jdbc.JdbcBackend._

import monix.reactive.Observable

class ItemRepository @Inject()(db: DatabaseDef) {
  def findItemByISBN(isbn: String): Observable[Item] = {
    Observable.fromReactivePublisher {
      db.stream {
        ItemTable.Table.filter { item =>
          item.isbn === isbn
        }.result
      }
    }
  }
}


