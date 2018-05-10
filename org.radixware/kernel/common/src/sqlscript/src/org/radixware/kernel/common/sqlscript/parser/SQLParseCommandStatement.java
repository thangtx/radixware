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


public class SQLParseCommandStatement extends SQLParseStatement {
    private final String command;
    private final String newObjectType;
    private final String newObjectName;
    private final boolean ignoreSQLErrors;

    public SQLParseCommandStatement(final SQLPosition position, final String command, final String newObjectType, final String newObjectName, final boolean ignoreSQLErrors) {
        super(position, StatementType.ST_COMMAND);
        if (command == null || command.isEmpty()) {
            throw new IllegalArgumentException("Command can't be null or empty");
        }
        else{
            this.command = command;
            this.newObjectType = newObjectType;
            this.newObjectName = newObjectName;
            this.ignoreSQLErrors = ignoreSQLErrors;
        }
    }

    public SQLParseCommandStatement(final SQLPosition position, final String command, final String newObjectType, final String newObjectName) {
        this(position, command, newObjectType, newObjectName, false);
    }

    public String getCommand() {
        return command;
    }

    public String getNewObjectType() {
        return newObjectType;
    }

    public String getNewObjectName() {
        return newObjectName;
    }

    public boolean isIgnoreSQLErrors() {
        return ignoreSQLErrors;
    }

    @Override
    public String toString() {
        return "SQLParseCommandStatement{" + "command=" + command + ", newObjectType=" + newObjectType + ", newObjectName=" + newObjectName + ", ignoreSQLErrors=" + ignoreSQLErrors + '}';
    }
}
