/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. This Source Code is distributed
 * WITHOUT ANY WARRANTY; including any implied warranties but not limited to
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Mozilla Public License, v. 2.0. for more details.
 */

package org.radixware.kernel.server.exceptions;

import java.sql.SQLException;

import org.radixware.kernel.common.exceptions.RadixError;

public class DatabaseError extends RadixError {

    private static final long serialVersionUID = 1983861909256448235L;
    private final int sqlErrorCode;

    public DatabaseError(String mess, Throwable cause) {
        super(mess, cause);
        if (cause instanceof SQLException) {
            this.sqlErrorCode = ((SQLException) cause).getErrorCode();
        } else {
            this.sqlErrorCode = 0;
        }
    }

    public DatabaseError(SQLException cause) {
        this(cause.getMessage(), cause);

    }

    /**
     * @return SQL code of error if cause is instance of SQLException, or 0.
     */
    public int getSqlErrorCode() {
        return sqlErrorCode;
    }
}
