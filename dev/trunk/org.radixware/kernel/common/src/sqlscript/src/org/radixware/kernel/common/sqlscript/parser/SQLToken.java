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

import org.radixware.kernel.common.sqlscript.parser.SQLConstants.TokenType;


public class SQLToken {
    private final TokenType type;
    private final SQLPosition position;
    private final Object value;

    public SQLToken() {
        this.type = TokenType.TK_UNKNOWN;
        this.position = null;
        this.value = null;
    }

    public SQLToken(final SQLPosition pPosition, final TokenType pType) {
        if (pPosition == null) {
            throw new IllegalArgumentException("Position can't be null");
        }
        else if (pType == null) {
            throw new IllegalArgumentException("Token type can't be null");
        }
        else {
            this.type = pType;
            this.position = pPosition.fork();
            this.value = null;
        }
    }

    public SQLToken(final SQLPosition pPosition, final TokenType pType, final String name) {
        if (pPosition == null) {
            throw new IllegalArgumentException("Position can't be null");
        }
        else if (pType == null) {
            throw new IllegalArgumentException("Token type can't be null");
        }
        else if (name == null) {
            throw new IllegalArgumentException("Name can't be null");
        }
        else {
            this.position = pPosition.fork();
            this.type = pType;
            this.value = name;
        }
    }

    public SQLToken(final SQLPosition pPosition, final SQLScriptValue pValue) {
        if (pPosition == null) {
            throw new IllegalArgumentException("Position can't be null");
        }
        else if (pValue == null) {
            throw new IllegalArgumentException("Script value can't be null");
        }
        else {
            this.position = pPosition.fork();
            this.type = TokenType.TK_SCRIPT_VALUE;
            this.value = pValue;
        }
    }

    public TokenType getType() {
        return type;
    }

    public SQLPosition getPosition() {
        return position;
    }

    public boolean isOperation() {
        switch (getType()) {
            case TK_SCRIPT_EQUALS:
                return true;
            case TK_SCRIPT_NOT_EQUALS:
                return true;
            case TK_SCRIPT_LESS:
                return true;
            case TK_SCRIPT_MORE:
                return true;
            case TK_SCRIPT_LESS_OR_EQUALS:
                return true;
            case TK_SCRIPT_MORE_OR_EQUALS:
                return true;
        }
        return false;
    }

    public void checkType(final TokenType pType) throws SQLScriptException {
        if (type != pType) {
            if (type == TokenType.TK_EOF) {
                throw new SQLScriptException("Unexpected EOF", position);
            }
            throw new SQLScriptException("Invalid token", position);
        }
    }

    public String getName() throws SQLScriptException {
        checkType(TokenType.TK_NAME);
        return (String) value;
    }

    public String getScriptName() throws SQLScriptException {
        checkType(TokenType.TK_SCRIPT_NAME);
        return (String) value;
    }

    public SQLScriptValue getScriptValue() throws SQLScriptException {
        checkType(TokenType.TK_SCRIPT_VALUE);
        return (SQLScriptValue) value;
    }

    @Override
    public String toString() {
        return "Token " + getType().name() + " '" + value + "' [" + position + "]";
    }
}
