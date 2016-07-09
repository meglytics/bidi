package db.scyllaDB


import com.websudos.phantom.connectors.{ContactPoints, KeySpace}


trait Keyspace {
  implicit val space: KeySpace = new KeySpace("query_01")

}

object scylla extends Keyspace {
  val hosts = Seq("103.56.92.54")
  val Conn = ContactPoints(hosts).keySpace(space.name)

}

trait Connector extends scylla.Conn.Connector wth Keyspace
