/**
 * The BSD License
 *
 * Copyright (c) 2010-2012 RIPE NCC
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
package net.ripe.rpki.validator.store

import java.io.File
import javax.sql.DataSource

import com.googlecode.flyway.core.Flyway
import net.ripe.rpki.validator.config.ApplicationOptions
import org.apache.commons.dbcp.BasicDataSource
import org.apache.derby.jdbc.EmbeddedDataSource

object DataSources {

  System.setProperty("derby.system.home", ApplicationOptions.workDirLocation.getCanonicalPath)

  private object DSSingletons extends SimpleSingletons[String, DataSource]({ dataDirBasePath =>
    val result = new EmbeddedDataSource()
    result.setDatabaseName(dataDirBasePath + File.separator + "rpki-object-cache")
    result.setCreateDatabase("create")
    migrate(result)
    result
  })

  /**
   * Store data on disk.
   */
  def DurableDataSource(dataDirBasePath: File) = DSSingletons(dataDirBasePath.getAbsolutePath)

  /**
   * For unit testing
   */
  def InMemoryDataSource = {
    val result = new BasicDataSource
    result.setUrl("jdbc:derby:memory:rpki-object-cache;create=true")
    result.setDriverClassName("org.apache.derby.jdbc.EmbeddedDriver")
    result.setDefaultAutoCommit(true)
    migrate(result)
    result
  }

  private def migrate(dataSource: DataSource) {
    val flyway = new Flyway
    flyway.setDataSource(dataSource)
    flyway.setLocations("/db/objectstore/migration")
    flyway.migrate
  }
}
