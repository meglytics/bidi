/*
 ** Copyright [2013-2016] [Megam Systems]
**
** Licensed under the Apache License, Version 2.0 (the "License");
** you may not use this file except in compliance with the License.
** You may obtain a copy of the License at
**
** http://www.apache.org/licenses/LICENSE-2.0
**
** Unless required by applicable law or agreed to in writing, software
** distributed under the License is distributed on an "AS IS" BASIS,
** WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
** See the License for the specific language governing permissions and
** limitations under the License.
*/

package models.analytics

import scalaz._
import Scalaz._
import scalaz.effect.IO
import scalaz.EitherT._
import scalaz.Validation
import scalaz.Validation.FlatMap._
import scalaz.NonEmptyList._
import scalaz.syntax.SemigroupOps

import cache._
import db._
import controllers.Constants._
import io.megam.auth.funnel.FunnelErrors._
import app.MConfig
import models.json.analytics._
import models.analytics._

import com.stackmob.scaliak._
import com.basho.riak.client.core.query.indexes.{ RiakIndexes, StringBinIndex, LongIntIndex }
import com.basho.riak.client.core.util.{ Constants => RiakConstants }
import io.megam.util.Time
import io.megam.common.riak.GunnySack
import io.megam.common.uid.UID
import net.liftweb.json._
import net.liftweb.json.scalaz.JsonScalaz._
import java.nio.charset.Charset

/**
 * @author ranjitha
 *
 */

case class KeyValueField(key: String, value: String) {
  val json = "{\"key\":\"" + key + "\",\"value\":\"" + value + "\"}"

  def toJValue: JValue = {
    import net.liftweb.json.scalaz.JsonScalaz.toJSON
    val preser = new KeyValueFieldSerialization()
    toJSON(this)(preser.writer)
  }

  def toJson(prettyPrint: Boolean = false): String = if (prettyPrint) {
    prettyRender(toJValue)
  } else {
    compactRender(toJValue)
  }

}

object KeyValueField {
  def empty: KeyValueField = new KeyValueField(new String(), new String())

  def fromJValue(jValue: JValue)(implicit charset: Charset = UTF8Charset): Result[KeyValueField] = {
    import net.liftweb.json.scalaz.JsonScalaz.fromJSON
    val preser = new KeyValueFieldSerialization()
    fromJSON(jValue)(preser.reader)
  }

  def fromJson(json: String): Result[KeyValueField] = (Validation.fromTryCatchThrowable[net.liftweb.json.JValue, Throwable] {
    parse(json)
  } leftMap { t: Throwable =>
    UncategorizedError(t.getClass.getCanonicalName, t.getMessage, List())
  }).toValidationNel.flatMap { j: JValue => fromJValue(j) }

}

case class YonpiInputBuilder(query: String, wks: WorkbenchesResults) {
  play.api.Logger.debug("%-20s -->[%s]".format("Q", query))
  play.api.Logger.debug("%-20s -->[%s]".format("wk", wks))
  play.api.Logger.debug("%-20s -->[%s]".format("wk", wks.head.get.connectors))

  val connectors = wks.head.get.connectors.map(x => YonpiConnectors(x).json)
  play.api.Logger.debug("%-20s -->[%s]".format("conn", connectors))
  val connectorsjson = connectors.mkString("")
  play.api.Logger.debug("%-20s -->[%s]".format("json", connectorsjson))
  val json = "{\"query\":\"" + query + "\",\"connectors\":[" + connectorsjson + "]}"
  val toMap = Map(controllers.Constants.SPARKJOBSERVER_INPUT -> json, "claz" -> "io.megam.meglytics.Main")
}

case class YonpiConnectors(conn: Connectors) {
  play.api.Logger.debug("%-20s -->[%s]".format("YC", "?????????????"))
  play.api.Logger.debug("%-20s -->[%s]".format("YC", conn.source))
  val source = conn.source
  val tables = conn.tables.map(x => x.name).mkString(" ")
  //val credential  =  conn.inputs.map { input =>
  val user = conn.inputs.find(_.key.equalsIgnoreCase(USERNAME))
  val pass = conn.inputs.find(_.key.equalsIgnoreCase(PASSWORD))
  val credential = user.get.value + ":" + pass.get.value
  val dbname = conn.dbname
  val endpoint = conn.endpoint
  val port = conn.port

  val json = "{\"source\":\"" + source + "\",\"credentials\":\"" + credential + "\", \"tables\":\"" + tables + "\", \"dbname\":\"" + dbname + "\", \"endpoint\":\"" + endpoint + "\", \"port\":\"" + port + "\"}"

  play.api.Logger.debug("%-20s -->[%s]".format("YC", json))
}

case class SparkjobsInput(source: String, assembly_id: String, inputs: models.analytics.KeyValueList) {
  val json = "{\"source\":\"" + source + "\",\"assembly_id\":\"" + assembly_id + "\",\"inputs\":" + models.analytics.KeyValueList.toJson(inputs, true) + "}"
}

case class SparkjobsResult(id: String, code: Int, status: String, job_id: String, created_at: String) {
  def toJValue: JValue = {
    import net.liftweb.json.scalaz.JsonScalaz.toJSON
    import models.json.analytics.SparkjobsResultSerialization
    val preser = new SparkjobsResultSerialization()
    toJSON(this)(preser.writer) //where does this JSON from?
  }

  def toJson(prettyPrint: Boolean = false): String = if (prettyPrint) {
    prettyRender(toJValue)
  } else {
    compactRender(toJValue)
  }
}

object SparkjobsResult {

  def fromJValue(jValue: JValue)(implicit charset: Charset = UTF8Charset): Result[SparkjobsResult] = {
    import net.liftweb.json.scalaz.JsonScalaz.fromJSON
    import models.json.analytics.SparkjobsResultSerialization
    val preser = new SparkjobsResultSerialization()
    fromJSON(jValue)(preser.reader)
  }

  def fromJson(json: String): Result[SparkjobsResult] = (Validation.fromTryCatchThrowable[net.liftweb.json.JValue, Throwable] {
    parse(json)
  } leftMap { t: Throwable =>
    UncategorizedError(t.getClass.getCanonicalName, t.getMessage, List())
  }).toValidationNel.flatMap { j: JValue => fromJValue(j) }

}
