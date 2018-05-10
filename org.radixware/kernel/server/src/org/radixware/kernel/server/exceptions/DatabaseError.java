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
import org.radixware.kernel.common.utils.SystemPropUtils;
import org.radixware.kernel.server.arte.DbConnection;
import org.radixware.kernel.server.instance.arte.IArteProvider;
import org.radixware.kernel.server.jdbc.RadixConnection;

public class DatabaseError extends RadixError {

    private static final boolean APPEND_SQL_TO_MESSAGE = SystemPropUtils.getBooleanSystemProp("rdx.database.error.append.sql.to.message", true);
    private static final String AUTO_APPEND_SQL_BEGIN = "--last sql begin";
    private static final String AUTO_APPEND_SQL_END = "--last sql end";
    private static final long serialVersionUID = 1983861909256448235L;
    private final int sqlErrorCode;

    public DatabaseError(String mess, Throwable cause) {
        super(appendSqlToMessage(mess), cause);
        if (cause instanceof SQLException) {
            this.sqlErrorCode = ((SQLException) cause).getErrorCode();
        } else {
            this.sqlErrorCode = 0;
        }
    }

    public DatabaseError(SQLException cause) {
        this(appendSqlToMessage(cause.getMessage()), cause);
    }

    /**
     * @return SQL code of error if cause is instance of SQLException, or 0.
     */
    public int getSqlErrorCode() {
        return sqlErrorCode;
    }

    private static String appendSqlToMessage(final String message) {
        if (message != null && message.contains(AUTO_APPEND_SQL_BEGIN)) {
            return message;
        }
        if (APPEND_SQL_TO_MESSAGE && Thread.currentThread() instanceof IArteProvider) {
            final DbConnection arteDbConn = ((IArteProvider) Thread.currentThread()).getArte().getDbConnection();
            if (arteDbConn != null && arteDbConn.get() != null && ((RadixConnection)arteDbConn.get()).getLastSql() != null) {
                return message + "\n" + AUTO_APPEND_SQL_BEGIN + "\n" + ((RadixConnection)arteDbConn.get()).getLastSql() + "\n" + AUTO_APPEND_SQL_END;
            }
        }
        return message;
    }
}
