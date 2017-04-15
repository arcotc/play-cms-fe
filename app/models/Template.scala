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

package models

import java.net.URLDecoder

import play.api.libs.json.Json
import play.twirl.api.Html

case class Template(name: String, headOption: Option[String], headerOption: Option[String], footerOption: Option[String]) {
  def renderedHead() = {
    headOption.fold(Html("")) { head => Html(URLDecoder.decode(head, "UTF-8")) }
  }

  def renderedHeader() = {
    headerOption.fold(Html("")) { header => Html(URLDecoder.decode(header, "UTF-8")) }
  }

  def renderedFooter() = {
    footerOption.fold(Html("")) { footer => Html(URLDecoder.decode(footer, "UTF-8")) }
  }
}

object Template {
  implicit val reads = Json.reads[Template]
}