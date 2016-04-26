package org.radixware.kernel.license;

import java.io.IOException;
import java.io.Writer;
import java.sql.Connection;

/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
/**
 *
 * @author akrylov
 */
public interface ILicenseReport {

    class Entry {

        private final String name, sql;

        public Entry(String sql, String name) {
            this.name = name;
            this.sql = sql;
        }

        public final String getSql() {
            return sql;
        }

        public final String getName() {
            return name;
        }
    }

    void execute(Connection dbConnection, Entry[] entries, Writer xmlOutput, Writer htmlOutput) throws IOException;
}
