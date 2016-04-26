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
    private String user;
    private String password;
    private String baseAlias;

    public SQLParseConnectStatement(SQLPosition pPosition, String pUser, String pPassword, String pBaseAlias) {
        super(pPosition, StatementType.ST_CONNECT);
        user = pUser;
        password = pPassword;
        baseAlias = pBaseAlias;
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
}
