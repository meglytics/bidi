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
package models.base

import scalaz._
import Scalaz._
import scalaz.effect.IO
import scalaz.EitherT._
import scalaz.Validation
import scalaz.Validation.FlatMap._
import scalaz.NonEmptyList._

import cache._
import db._
import io.megam.auth.funnel.FunnelErrors._
import controllers.Constants._

import com.stackmob.scaliak._
import io.megam.auth.stack.AccountResult
import io.megam.common.riak.GunnySack
import io.megam.common.uid.UID
import io.megam.util.Time
import net.liftweb.json._
import net.liftweb.json.scalaz.JsonScalaz._
import java.nio.charset.Charset
import com.basho.riak.client.core.query.indexes.{ RiakIndexes, StringBinIndex, LongIntIndex }
import com.basho.riak.client.core.util.{ Constants => RiakConstants }

/**
 * @author morpheyesh
 *
 */
case class PushdInput(table_name: String, inputs: Inputs[]) {}

case class Inputs(key: String, value: String) {}



object Pushd {



  /**
   * A private method which chains computation to make GunnySack when provided with an input json, email.
   * parses the json, and converts it to profile input, if there is an error during parsing, a MalformedBodyError is sent back.
   * After that flatMap on its success and the account id information is looked up.
   * If the account id is looked up successfully, then yield the GunnySack object.
   */
  private def mkGunnySack(input: String): ValidationNel[Throwable, Option[GunnySack]] = {
    val pushdInput: ValidationNel[Throwable, PushdInput] = (Validation.fromTryCatchThrowable[PushdInput,Throwable] {
      parse(input).extract[PushdInput]
    } leftMap { t: Throwable => new MalformedBodyError(input, t.getMessage) }).toValidationNel //capture failure
     println("YEAHHHHHHHHHHHHHHHHHHH")
     println(pushdInput)

  /*  for {
      m <- accountInput
      uid <- (UID("act").get leftMap { ut: NonEmptyList[Throwable] => ut })
     // org <- models.team.Organizations.create(m.email, OrganizationsInput(DEFAULT_ORG_NAME).json)
    } yield {
      val bvalue = Set(uid.get._1 + uid.get._2)
      val json = AccountResult(uid.get._1 + uid.get._2, m.first_name, m.last_name, m.phone, m.email, m.api_key, m.password, m.authority, m.password_reset_key, m.password_reset_sent_at, Time.now.toString).toJson(false)
      new GunnySack(m.email, json, RiakConstants.CTYPE_TEXT_UTF8, None,
        Map(metadataKey -> metadataVal), Map((bindex, bvalue))).some
    } */
  }

  /*
   *  Receives the data from nodes(mobiles) and this checks for the table name,
   *  if table_name exists in scyllaDB -> post to tables API
   * if table_name does not exist in scyllaDB -> create and post to tablesAPI
   */
  def create(input: String): ValidationNel[Throwable, Option[PushdResult]] = {
  //two cases
  //1. with existing table_name
  //2. Without existing table_name



  }




  implicit val sedimentAccountEmail = new Sedimenter[ValidationNel[Throwable, Option[AccountResult]]] {
    def sediment(maybeASediment: ValidationNel[Throwable, Option[AccountResult]]): Boolean = {
      val notSed = maybeASediment.isSuccess
      notSed
    }
  }

}
