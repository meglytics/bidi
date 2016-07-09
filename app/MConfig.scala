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
/**
 * @author ram
 *
 */
package app

object MConfig {
  val baseurl = play.api.Play.application(play.api.Play.current).configuration.getString("application.baseUrl")
  val riakurl = play.api.Play.application(play.api.Play.current).configuration.getString("riak.url").get
  val spark_jobserver = play.api.Play.application(play.api.Play.current).configuration.getString("spark.jobserver").get
  val yanpi_timeout = play.api.Play.application(play.api.Play.current).configuration.getString("spark.yanpi.timeout").get
}
