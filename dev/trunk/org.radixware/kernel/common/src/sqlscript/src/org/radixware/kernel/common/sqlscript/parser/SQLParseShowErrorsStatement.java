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


public class SQLParseShowErrorsStatement extends SQLParseStatement {
    private String objectType;
    private String objectName;

    public SQLParseShowErrorsStatement(SQLPosition position, String objectType, String objectName) {
        super(position, StatementType.ST_SHOW_ERRORS);
        this.objectType = objectType;
        this.objectName = objectName;
    }

    public String getObjectType() {
        return objectType;
    }

    public String getObjectName() {
        return objectName;
    }
}
