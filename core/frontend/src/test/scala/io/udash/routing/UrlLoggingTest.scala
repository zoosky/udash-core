package io.udash.routing

import io.udash._
import io.udash.testing._

import scala.collection.mutable.ListBuffer
import scala.concurrent.ExecutionContext

class UrlLoggingTest extends UdashFrontendTest with TestRouting {
  "UrlLogging" should {

    "call logging impl on url change" in {
      val urlWithRef = ListBuffer.empty[(String, Option[String])]
      initTestRoutingEngine(new TestRoutingRegistry with UrlLogging[TestState] {

        override implicit protected val loggingEC: ExecutionContext = testExecutionContext

        override protected def log(url: String, referrer: Option[String]): Unit =
          urlWithRef += ((url, referrer))
      })

      val urls = Seq("/", "/next", "/abc/1", "/next")
      val expected = (urls.head, Some("")) :: urls.sliding(2).map { case Seq(prev, current) => (current, Some(prev)) }.toList
      urls.foreach(str => routingEngine.handleUrl(Url(str)))
      urlWithRef.toList shouldBe expected
    }
  }
}