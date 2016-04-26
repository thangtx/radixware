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

import java.util.Vector;
import org.radixware.kernel.common.sqlscript.parser.SQLConstants.StatementType;


public class SQLScriptStatement extends SQLParseStatement {

    private int index;
    private Vector<SQLToken> tokens = new Vector<SQLToken>();

    public SQLScriptStatement(SQLPosition pPosition) {
        super(pPosition, StatementType.ST_SCRIPT);
        index = 0;
    }

    public void appendToken(SQLToken token) {
        tokens.add(token);
    }

    public boolean hasMoreTokens() {
        return index < tokens.size();
    }

    public SQLToken nextToken() throws SQLScriptException {
        if (index >= tokens.size()) {
            throw new SQLScriptException("SQL script statement : no more tokens", getPosition());
        }
        return tokens.get(index++);
    }

    public SQLToken seeToken() throws SQLScriptException {
        if (index >= tokens.size()) {
            throw new SQLScriptException("SQL script statement : no more tokens", getPosition());
        }
        return tokens.get(index);
    }

    public Vector<SQLToken> getTokens() {
        return tokens;
    }

    public int getIndex() {
        return index;
    }

    void setIndex(int pIndex) {
        index = pIndex;
    }
}
