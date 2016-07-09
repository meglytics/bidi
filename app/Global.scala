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

import scalaz._
import Scalaz._
import scalaz.effect.IO
import scalaz.EitherT._
import scalaz.Validation
import scalaz.Validation.FlatMap._
import play.api._
import play.api.http.Status._
import play.api.http.HeaderNames._
import play.api.mvc._
import play.api.mvc.Results._
import play.filters.gzip.{ GzipFilter }
import io.megam.auth.stack.HeaderConstants._
import scala.concurrent.Future
import controllers._
import java.io._

/**
 * We do bunch of things in Global, a gzip response is sent back to the client when the
 * header has "Content-length" > 5000bytes
 */

object Global extends WithFilters(new GzipFilter(shouldGzip = (request, response) =>
  response.headers.get(CONTENT_TYPE).exists(_.startsWith(application_gzip)))) with GlobalSettings {

  override def onStart(app: play.api.Application) {
    /* website link for banner text - http://patorjk.com/software/taag/#p=display&f=ANSI%20Shadow&t=Meglytics */
    play.api.Logger.info("""
       ███╗   ███╗███████╗ ██████╗ ██╗  ██╗   ██╗████████╗██╗ ██████╗███████╗
       ████╗ ████║██╔════╝██╔════╝ ██║  ╚██╗ ██╔╝╚══██╔══╝██║██╔════╝██╔════╝
       ██╔████╔██║█████╗  ██║  ███╗██║   ╚████╔╝    ██║   ██║██║     ███████╗
       ██║╚██╔╝██║██╔══╝  ██║   ██║██║    ╚██╔╝     ██║   ██║██║     ╚════██║
       ██║ ╚═╝ ██║███████╗╚██████╔╝███████╗██║      ██║   ██║╚██████╗███████║
       ╚═╝     ╚═╝╚══════╝ ╚═════╝ ╚══════╝╚═╝      ╚═╝   ╚═╝ ╚═════╝╚══════╝
    """)
    play.api.Logger.info("started ...")

  }

  override def onStop(app: play.api.Application) {
    play.api.Logger.info("""
███████╗███████╗███████╗    ██╗   ██╗ █████╗
██╔════╝██╔════╝██╔════╝    ╚██╗ ██╔╝██╔══██╗
███████╗█████╗  █████╗       ╚████╔╝ ███████║
╚════██║██╔══╝  ██╔══╝        ╚██╔╝  ██╔══██║
███████║███████╗███████╗       ██║   ██║  ██║
╚══════╝╚══════╝╚══════╝       ╚═╝   ╚═╝  ╚═╝

     """)
    play.api.Logger.info("Shark bait...")
  }

  override def onError(request: RequestHeader, ex: Throwable): Future[play.api.mvc.Result] = {
    Future.successful(InternalServerError(
      views.html.errorPage(ex)))
  }

  override def onHandlerNotFound(request: RequestHeader): Future[play.api.mvc.Result] = {
    Future.successful(NotFound(
      views.html.errorPage(new Throwable(NOT_FOUND + ":" + request.path + " NOT_FOUND"))))
  }
}
