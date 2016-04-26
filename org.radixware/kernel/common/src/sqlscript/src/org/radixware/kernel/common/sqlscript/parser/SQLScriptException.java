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

import java.sql.SQLException;


public class SQLScriptException extends SQLException {
    private SQLPosition position;

    public SQLScriptException() {
    }

    public SQLScriptException(String message) {
        super(message);
    }

    public SQLScriptException(String message, SQLPosition position) {
        super(message);
        this.position = position.fork();
    }

    public SQLScriptException(SQLException ex, SQLPosition position) {
        super(ex.toString(), ex);
        this.position = position.fork();
    }

    public SQLScriptException(Exception ex, SQLPosition position) {
        super(ex.toString(), ex);
        this.position = position.fork();
    }

    public void setPosition(SQLPosition position) {
        this.position = position.fork();
    }

    public SQLPosition getPosition() {
        return position;
    }

    @Override
    public String toString() {
        if (position != null)
            return super.toString().trim() + " : " + position.toString();
        return super.toString();
    }

}
