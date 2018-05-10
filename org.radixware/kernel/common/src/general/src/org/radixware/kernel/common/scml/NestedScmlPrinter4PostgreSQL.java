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

package org.radixware.kernel.common.scml;

import org.radixware.kernel.common.enums.EDatabaseType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.utils.ValueToSqlConverter;


class NestedScmlPrinter4PostgreSQL extends NestedScmlCodePrinter {
    private static final String COMMAND_SEPARATOR = "\n/\n\n";
//    private static final String COMMAND_SEPARATOR = ";\n\n";

    protected NestedScmlPrinter4PostgreSQL(final ScmlCodePrinter container) {
        super(container);
        putProperty(DATABASE_TYPE,EDatabaseType.POSTGRESQL);
    }

    @Override
    public CodePrinter printStringLiteral(final String text) {
        return print(ValueToSqlConverter.toSql(text, EValType.STR));
    }

    @Override
    public CodePrinter printCommandSeparator() {
        return print(COMMAND_SEPARATOR);
    }
}
