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

public class SQLParseStatement {
    private final SQLPosition position;
    private final SQLConstants.StatementType type;

    public SQLParseStatement(final SQLPosition position, final SQLConstants.StatementType type) {
        if (position == null) {
            throw new IllegalArgumentException("Position can't be null");
        }
        else if (type == null) {
            throw new IllegalArgumentException("Statement type can't be null");
        }
        else {
            this.position = position.fork();
            this.type = type;
        }
    }

    public SQLPosition getPosition() {
        return position;
    }

    public SQLConstants.StatementType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "SQLParseStatement{" + "position=" + position + ", type=" + type + '}';
    }
}
