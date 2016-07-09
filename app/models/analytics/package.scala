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
package models

import scalaz._
import Scalaz._
import scalaz.NonEmptyList
import scalaz.NonEmptyList._

import net.liftweb.json._
import net.liftweb.json.scalaz.JsonScalaz._
import java.nio.charset.Charset
import controllers.Constants._
import models.analytics._

/**
 * @author ranjitha
 *
 */
package object analytics {

  type KeyValueList = List[KeyValueField]

  object KeyValueList {
    val OJA_EMAIL = "email"
    val OJA_API_KEY = "api_key"
    val OJA_ASSEMBLY_ID = "assembly_id"
    val OJA_COMP_ID = "component_id"
    val OJA_SPARK_JOBSERVER = "spark_jobserver"

    val MKT_FLAG_EMAIL = "<email>"
    val MKT_FLAG_APIKEY = "<api_key>"
    val MKT_FLAG_ASSEMBLY_ID = "<assembly_id>"
    val MKT_FLAG_COMP_ID = "<component_id>"
    val MKT_FLAG_HOST = "<host>"

    val emptyRR = List(KeyValueField.empty)

    def toJValue(nres: KeyValueList): JValue = {
      import net.liftweb.json.scalaz.JsonScalaz.toJSON
      import models.json.analytics.KeyValueListSerialization.{ writer => KeyValueListWriter }
      toJSON(nres)(KeyValueListWriter)
    }

    def fromJValue(jValue: JValue)(implicit charset: Charset = UTF8Charset): Result[KeyValueList] = {
      import net.liftweb.json.scalaz.JsonScalaz.fromJSON
      import models.json.analytics.KeyValueListSerialization.{ reader => KeyValueListReader }
      fromJSON(jValue)(KeyValueListReader)
    }

    def toJson(nres: KeyValueList, prettyPrint: Boolean = false, flagsMap: Map[String, String] = Map()): String = {
      val nrec = nres.map { x => KeyValueField(x.key, flagsMap.get(x.value).getOrElse(x.value)) }
      if (prettyPrint) {
        prettyRender(toJValue(nrec))
      } else {
        compactRender(toJValue(nrec))
      }
    }

    def apply(plansList: List[KeyValueField]): KeyValueList = plansList

    def empty: List[KeyValueField] = emptyRR

    def toMap(nres: KeyValueList) = (nres.map { x => (x.key, x.value) }).toMap

  }

  type SparkjobsResults = NonEmptyList[Option[SparkjobsResult]]

  object SparkjobsResults {
    val emptyNR = List(Option.empty[SparkjobsResult])
    //screwy. you pass an instance. may be FunnelResponses needs be to a case class
    def toJValue(nres: SparkjobsResults): JValue = {
      import net.liftweb.json.scalaz.JsonScalaz.toJSON
      import models.json.analytics.SparkjobsResultsSerialization.{ writer => SparkjobsResultsWriter }
      toJSON(nres)(SparkjobsResultsWriter)
    }

    //screwy. you pass an instance. may be FunnelResponses needs be to a case class
    def toJson(nres: SparkjobsResults, prettyPrint: Boolean = false): String = if (prettyPrint) {
      prettyRender(toJValue(nres))
    } else {
      compactRender(toJValue(nres))
    }

    def apply(m: Option[SparkjobsResult]) = nels(m)
    def apply(m: SparkjobsResult): SparkjobsResults = SparkjobsResults(m.some)
    def empty: SparkjobsResults = nel(emptyNR.head, emptyNR.tail)
  }

  type WorkbenchesResults = NonEmptyList[Option[WorkbenchesResult]]

  object WorkbenchesResults {
    val emptyNR = List(Option.empty[WorkbenchesResult])
    //screwy. you pass an instance. may be FunnelResponses needs be to a case class
    def toJValue(nres: WorkbenchesResults): JValue = {
      import net.liftweb.json.scalaz.JsonScalaz.toJSON
      import models.json.analytics.WorkbenchesResultsSerialization.{ writer => WorkbenchesResultsWriter }
      toJSON(nres)(WorkbenchesResultsWriter)
    }

    //screwy. you pass an instance. may be FunnelResponses needs be to a case class
    def toJson(nres: WorkbenchesResults, prettyPrint: Boolean = false): String = if (prettyPrint) {
      prettyRender(toJValue(nres))
    } else {
      compactRender(toJValue(nres))
    }

    def apply(m: Option[WorkbenchesResult]) = nels(m)
    def apply(m: WorkbenchesResult): WorkbenchesResults = WorkbenchesResults(m.some)
    def empty: WorkbenchesResults = nel(emptyNR.head, emptyNR.tail)
  }

  type TablesList = List[Tables]

  object TablesList {
    val emptyRR = List(Tables.empty)
    def toJValue(nres: TablesList): JValue = {

      import net.liftweb.json.scalaz.JsonScalaz.toJSON
      import models.json.analytics.TablesListSerialization.{ writer => TablesListWriter }
      toJSON(nres)(TablesListWriter)
    }

    def fromJValue(jValue: JValue)(implicit charset: Charset = UTF8Charset): Result[TablesList] = {
      import net.liftweb.json.scalaz.JsonScalaz.fromJSON
      import models.json.analytics.TablesListSerialization.{ reader => TablesListReader }
      fromJSON(jValue)(TablesListReader)
    }

    def toJson(nres: TablesList, prettyPrint: Boolean = false): String = if (prettyPrint) {
      prettyRender(toJValue(nres))
    } else {
      compactRender(toJValue(nres))
    }

    def apply(plansList: List[Tables]): TablesList = plansList

    def empty: List[Tables] = emptyRR

  }

  type YonpiConnectorsList = List[YonpiConnector]

  object YonpiConnectorsList {
    val emptyRR = List(YonpiConnector.empty)
    def toJValue(nres: YonpiConnectorsList): JValue = {

      import net.liftweb.json.scalaz.JsonScalaz.toJSON
      import models.json.analytics.YonpiConnectorsListSerialization.{ writer => YonpiConnectorsListWriter }
      toJSON(nres)(YonpiConnectorsListWriter)
    }

    def fromJValue(jValue: JValue)(implicit charset: Charset = UTF8Charset): Result[YonpiConnectorsList] = {
      import net.liftweb.json.scalaz.JsonScalaz.fromJSON
      import models.json.analytics.YonpiConnectorsListSerialization.{ reader => YonpiConnectorsListReader }
      fromJSON(jValue)(YonpiConnectorsListReader)
    }

    def toJson(nres: YonpiConnectorsList, prettyPrint: Boolean = false): String = if (prettyPrint) {
      prettyRender(toJValue(nres))
    } else {
      compactRender(toJValue(nres))
    }

    def apply(plansList: List[YonpiConnector]): YonpiConnectorsList = plansList

    def empty: List[YonpiConnector] = emptyRR

  }

  type YonpiinputResults = NonEmptyList[Option[YonpiinputResult]]

  object YonpiinputResults {
    val emptyNR = List(Option.empty[YonpiinputResult])
    //screwy. you pass an instance. may be FunnelResponses needs be to a case class
    def toJValue(nres: YonpiinputResults): JValue = {
      import net.liftweb.json.scalaz.JsonScalaz.toJSON
      import models.json.analytics.YonpiinputResultsSerialization.{ writer => YonpiinputResultsWriter }
      toJSON(nres)(YonpiinputResultsWriter)
    }

    //screwy. you pass an instance. may be FunnelResponses needs be to a case class
    def toJson(nres: YonpiinputResults, prettyPrint: Boolean = false): String = if (prettyPrint) {
      prettyRender(toJValue(nres))
    } else {
      compactRender(toJValue(nres))
    }

    def apply(m: Option[YonpiinputResult]) = nels(m)
    def apply(m: YonpiinputResult): YonpiinputResults = YonpiinputResults(m.some)
    def empty: YonpiinputResults = nel(emptyNR.head, emptyNR.tail)
  }
  type ConnectorsList = List[Connectors]

  object ConnectorsList {
    val emptyRR = List(Connectors.empty)
    def toJValue(nres: ConnectorsList): JValue = {

      import net.liftweb.json.scalaz.JsonScalaz.toJSON
      import models.json.analytics.ConnectorsListSerialization.{ writer => ConnectorsListWriter }
      toJSON(nres)(ConnectorsListWriter)
    }

    def fromJValue(jValue: JValue)(implicit charset: Charset = UTF8Charset): Result[ConnectorsList] = {
      import net.liftweb.json.scalaz.JsonScalaz.fromJSON
      import models.json.analytics.ConnectorsListSerialization.{ reader => ConnectorsListReader }
      fromJSON(jValue)(ConnectorsListReader)
    }

    def toJson(nres: ConnectorsList, prettyPrint: Boolean = false): String = if (prettyPrint) {
      prettyRender(toJValue(nres))
    } else {
      compactRender(toJValue(nres))
    }

    def apply(plansList: List[Connectors]): ConnectorsList = plansList

    def empty: List[Connectors] = emptyRR

  }

}
