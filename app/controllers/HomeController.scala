// Copyright (C) 2011-2017 the original author or authors.
// See the LICENCE.txt file distributed with this work for additional
// information regarding copyright ownership.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package controllers

import javax.inject._

import models.LoadedPage
import play.api.Logger
import play.api.libs.ws.{WSClient, WSRequest, WSResponse}
import play.api.mvc._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class HomeController @Inject() (ws: WSClient) extends Controller {
  val server = "http://localhost:9001"
  val defaultUrl = s"$server/any/page"

  def fetchPage(page: String = "")(f: => WSResponse => Result) = Action.async { response =>
    val url = page.isEmpty match {
      case true => defaultUrl
      case false => s"$defaultUrl/$page"
    }

    Logger.debug(s"Calling ${url}")

    val request: WSRequest = ws.url(url)
    val complexRequest: WSRequest =
      request.withHeaders("Accept" -> "application/json")

    val futureResponse: Future[WSResponse] = complexRequest.get()
    futureResponse.map {
      case response if response.status == OK => {
        f(response)
      }
      case _ => Ok(views.html.index("This is an error"))
    }
  }

  def index: Action[AnyContent] = fetchPage() { response =>
    val loadedPage = response.json.as[LoadedPage]
    Ok(views.html.renderTemplate(loadedPage))
  }

  def loadPage(page: String): Action[AnyContent] = fetchPage(page) { response =>
    val loadedPage = response.json.as[LoadedPage]
    Ok(views.html.renderTemplate(loadedPage))
  }
}
