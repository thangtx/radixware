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


public class SQLTextStatement extends SQLParseStatement {
    final private String text;

    public SQLTextStatement(final SQLPosition position, final String text) {
        super(position, SQLConstants.StatementType.ST_TEXT);
        if (text == null) {
            throw new IllegalArgumentException("Text string can't be null");
        }
        else {
            this.text = text;
        }
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "SQLTextStatement{" + "text=" + text + '}';
    }
}
