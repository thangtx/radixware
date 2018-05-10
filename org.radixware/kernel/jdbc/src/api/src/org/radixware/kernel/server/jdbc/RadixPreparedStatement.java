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

package org.radixware.kernel.server.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.radixware.kernel.server.jdbc.RadixStatement;

public interface RadixPreparedStatement extends RadixStatement, PreparedStatement {
    int getExecuteBatch();
    void setExecuteBatch(int batchSize) throws SQLException;
    int sendBatch() throws SQLException;
    boolean isReadOnly();
    void setReadOnly(boolean readOnly) throws SQLException;
    public void setInt(int parameterIndex, Integer x) throws SQLException;;
    public void setLong(int parameterIndex, Long x) throws SQLException;;
}
