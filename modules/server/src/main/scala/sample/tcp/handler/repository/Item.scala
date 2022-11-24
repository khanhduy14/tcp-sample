package sample.tcp.handler.repository

import slick.collection.heterogeneous.HNil
import sample.tcp.handler.repository.PostgresProfile.api._

case class Item (isbn: String, price: BigDecimal) extends Serializable

class ItemTable(tag: Tag) extends Table[Item](tag, "item") {
  def isbn = column[String]("isbn", O.PrimaryKey)
  def price = column[BigDecimal]("price")

  def * = (isbn :: price :: HNil).mapTo[Item]

}
object ItemTable {
  val Table: TableQuery[ItemTable] = TableQuery[ItemTable]
}
