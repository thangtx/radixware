/*
 * Copyright (c) 2008-2017, Compass Plus Limited. All rights reserved.
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

import java.sql.ResultSet;
import java.sql.SQLException;

public interface RadixResultSet extends ResultSet {
    public Boolean getNullableBool(int columnIndex) throws SQLException;
    public Boolean getNullableBool(String columnLabel) throws SQLException;
    public Integer getNullableInt(int columnIndex) throws SQLException;
    public Integer getNullableInt(String columnLabel) throws SQLException;
    public Long getNullableLong(int columnIndex) throws SQLException;
    public Long getNullableLong(String columnLabel) throws SQLException;
}
