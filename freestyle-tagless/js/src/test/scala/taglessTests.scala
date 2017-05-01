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

import freestyle._
import freestyle.implicits._
import org.scalatest.{Matchers, WordSpec}
import cats._
import cats.implicits._
import algebras._
import cats.free.Free
import handlers._
import modules._
import utils._

import scala.scalajs.js.JavaScriptException

class taglessTestsJS extends WordSpec with Matchers {

  "Tagless final algebras" should {

    //Can't catch `StackOverflowError` in scala.js https://github.com/scala-js/scala-js/issues/212
    "blow up the stack when interpreted to stack unsafe monads" in {
      assertThrows[JavaScriptException] {
        SOProgram[Option](0)
      }
    }

    "remain stack safe when interpreted to stack safe monads" in {
      SOProgram[Free[Option, ?]](0).exec[Option] shouldBe Option(iterations)
    }

  }

}