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

import org.joda.time._
import org.joda.time.format.PeriodFormat
import scala.xml.Text
import scala.xml.NodeSeq
import scala.collection.SortedMap
import lib.DateAndTime._
import models.TrustAnchors

class TrustAnchorsView(trustAnchors: TrustAnchors, now: DateTime = new DateTime) extends View {
  def tab = Tabs.TrustAnchorsTab
  def title = Text("Configured Trust Anchors")
  def body = {
    <table class="zebra-striped">
      <thead>
        <th>CA Name</th>
        <th>Subject</th>
        <th>Expires in</th>
        <th>Location</th>
      </thead>
      <tbody>{
        for (ta <- sortedTrustAnchors) yield {
          <tr>
            <td>{ ta.name }</td>{
              if (ta.certificate.isDefined) {
                <td>{ ta.certificate.get.getCertificate().getSubject() }</td>
                <td>{ expiresIn(ta.certificate.get.getCertificate().getValidityPeriod().getNotValidAfter()) }</td>
                <td>{ ta.certificate.get.getLocation() }</td>
              } else {
                <td colspan="3" class="info">Loading...</td>
              }
            }
          </tr>
        }
      }</tbody>
    </table>
  }

  private def expiresIn(notValidAfter: DateTime): NodeSeq = {
    if (now.isBefore(notValidAfter)) {
      val period = keepMostSignificantPeriodFields(new Period(now, notValidAfter), number = 2)
      Text(PeriodFormat.getDefault().print(period))
    } else {
      <strong>EXPIRED</strong>
    }
  }
  private def sortedTrustAnchors = trustAnchors.all.sortBy(_.name)
}