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

package org.radixware.kernel.common.sqlscript.parser;

import org.radixware.kernel.common.sqlscript.parser.SQLConstants.StatementType;


public class SQLParseConnectStatement extends SQLParseStatement {
    private final String user;
    private final String password;
    private final String baseAlias;

    public SQLParseConnectStatement(final SQLPosition pPosition, final String pUser, final String pPassword, final String pBaseAlias) {
        super(pPosition, StatementType.ST_CONNECT);
        if (pUser == null || pUser.isEmpty()) {
            throw new IllegalArgumentException("User name can't be null or empty");
        }
        else if (pPassword == null) {
            throw new IllegalArgumentException("Password can't be null");
        }
        else {
            this.user = pUser;
            this.password = pPassword;
            this.baseAlias = pBaseAlias;
        }
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String getBaseAlias() {
        return baseAlias;
    }

    @Override
    public String toString() {
        return "SQLParseConnectStatement{" + "user=" + user + ", password=" + password + ", baseAlias=" + baseAlias + '}';
    }
}
