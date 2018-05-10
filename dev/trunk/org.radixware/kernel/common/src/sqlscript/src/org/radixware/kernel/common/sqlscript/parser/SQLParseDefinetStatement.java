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


public class SQLParseDefinetStatement extends SQLParseStatement {
    private final String var;
    private final String value;

    public SQLParseDefinetStatement(final SQLPosition position, final String var, final String value) {
        super(position, StatementType.ST_DEFINE);
        if (var == null || var.isEmpty()) {
            throw new IllegalArgumentException("Var name can't be null or empty");
        }
        else {
            this.var = var;
            this.value = value;
        }
    }

    public String getVar() {
        return var;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "SQLParseDefinetStatement{" + "var=" + var + ", value=" + value + '}';
    }
}
