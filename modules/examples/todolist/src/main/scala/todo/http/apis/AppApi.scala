/*
 * Copyright 2017 47 Degrees, LLC. <http://www.47deg.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package todo
package http
package apis

import cats.~>
import com.twitter.util.Future
import freestyle._
import freestyle.http.finch._
import io.circe.generic.auto._
import io.finch._
import io.finch.circe._
import todo.model._
import todo.services._

class AppApi[F[_]](implicit service: AppServices[F], handler: F ~> Future) {

  val reset: Endpoint[Int] =
    post("reset") {
      service.reset.map(Ok(_))
    }

  val list: Endpoint[List[TodoForm]] =
    get("list") {
      service.list.map(Ok(_))
    }

  val insert: Endpoint[TodoForm] =
    post("insert" :: jsonBody[TodoForm]) { form: TodoForm =>
      service.insert(form).map(Ok(_))
    } handle {
      case nse: NoSuchElementException => InternalServerError(nse)
    }

  val update: Endpoint[TodoForm] =
    put("update" :: jsonBody[TodoForm]) { form: TodoForm =>
      service.update(form).map(Ok(_))
    } handle {
      case nse: NoSuchElementException => BadRequest(nse)
    }

  val destroy: Endpoint[Int] =
    delete("delete" :: jsonBody[TodoForm]) { form: TodoForm =>
      service.destroy(form).map(Ok(_))
    } handle {
      case nse: NoSuchElementException => BadRequest(nse)
    }

  val endpoints = reset :+: list :+: insert :+: update :+: destroy
}

object AppApi {
  implicit def instance[F[_]](implicit service: AppServices[F], handler: F ~> Future): AppApi[F] =
    new AppApi[F]
}
