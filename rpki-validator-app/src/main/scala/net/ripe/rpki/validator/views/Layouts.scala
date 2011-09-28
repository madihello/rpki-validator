/**
 * The BSD License
 *
 * Copyright (c) 2010, 2011 RIPE NCC
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *   - Redistributions of source code must retain the above copyright notice,
 *     this list of conditions and the following disclaimer.
 *   - Redistributions in binary form must reproduce the above copyright notice,
 *     this list of conditions and the following disclaimer in the documentation
 *     and/or other materials provided with the distribution.
 *   - Neither the name of the RIPE NCC nor the names of its contributors may be
 *     used to endorse or promote products derived from this software without
 *     specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package net.ripe.rpki.validator
package views

import scala.xml._
import org.joda.time._

object Layouts {
  def standard(view: View): NodeSeq =
    <html lang="en">
      <head>
        <meta charset="utf-8"/>
        <title>RPKI Validator - { view.title }</title>
        <link rel="stylesheet" href="http://twitter.github.com/bootstrap/1.3.0/bootstrap.css"/>
        <link rel="stylesheet" href="/stylesheets/application.css"/>
        <script src="/javascript/datatables/1.8.2/jquery.js"/>
        <script src="/javascript/datatables/1.8.2/jquery.dataTables.min.js"/>
      </head>
      <body>
        <div class="topbar">
          <div class="fill">
            <div class="container">
              <a class="brand" href="#">RPKI Validator</a>
              <ul class="nav">{
                for (tab <- Tabs.all) yield {
                  <li class={ if (tab == view.tab) "active" else "" }><a href={ tab.url }>{ tab.text }</a></li>
                }
              }</ul>
            </div>
          </div>
        </div>
        <div class="container">
          <h1>{ view.title }</h1>
          { view.body }
          <footer>
            <p>Copyright © { (2009 to (new DateTime).getYear).mkString(", ") } the Réseaux IP Européens Network Coordination Centre RIPE NCC. All rights restricted.</p>
          </footer>
        </div>
      </body>
    </html>
}