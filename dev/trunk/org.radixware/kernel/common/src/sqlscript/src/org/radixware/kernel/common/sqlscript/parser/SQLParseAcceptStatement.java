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
import org.radixware.kernel.common.sqlscript.parser.SQLScriptValue.Type;


public class SQLParseAcceptStatement extends SQLParseStatement {
    private SQLScriptValue.Type varType;
    private String var;
    private final String defaultValue;
    private String prompt;
    private boolean hide;

    public SQLParseAcceptStatement(SQLPosition position, Type varType, String var, String defaultValue, String prompt, boolean hide) {
        super(position, StatementType.ST_ACCEPT);
        this.varType = varType;
        this.var = var;
        this.defaultValue = defaultValue;
        this.prompt = prompt;
        this.hide = hide;
    }

    public Type getVarType() {
        return varType;
    }

    public String getVar() {
        return var;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public String getPrompt() {
        return prompt;
    }

    public boolean isHide() {
        return hide;
    }

}
