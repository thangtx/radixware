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


public class SQLScriptValue {
    private Type type;
    private Object value;

    public enum Type { UNDEFINED, STRING, INT };

    public SQLScriptValue() {
        type = Type.UNDEFINED;
        value = null;
    }

    public SQLScriptValue(Type type, String value) {
        this.type = type;
        switch (type) {
            case STRING:
                this.value = value;
                break;
            case INT:
                this.value = Integer.parseInt(value);
                break;
        }
        value = null;
    }

    public SQLScriptValue(String pValue)
    {
        type = Type.STRING;
        value = pValue;
    }

    public SQLScriptValue(Integer pValue)
    {
        type = Type.INT;
        value = pValue;
    }

    public SQLScriptValue(Boolean pValue)
    {
        type = Type.INT;
        value = pValue?new Integer(1):new Integer(0);
    }

    public Type getType() {
        return type;
    }

    public String getString() throws SQLScriptException
    {
        switch (type)
        {
        case STRING:
            return (String)value;
        case INT:
            return ((Integer)value).toString();
        }
        throw new SQLScriptException("SQLScriptValue::getString : the type of the SQL script value is undefined");
    }

    public Integer getInt() throws SQLScriptException
    {
        switch (type)
        {
        case STRING:
            if (((String)value).toUpperCase().equals("TRUE"))
                return 1;
            if (((String)value).toUpperCase().equals("FALSE"))
                return 0;
            return Integer.parseInt((String)value);
        case INT:
            return ((Integer)value);
        }
        throw new SQLScriptException("SQLScriptValue::getInt : the type of the SQL script value is undefined");
    }

    public boolean getBoolean() throws SQLScriptException
    {
        return getInt() != 0;
    }

    public SQLScriptValue negation() throws SQLScriptException {
        return new SQLScriptValue(!getBoolean());
    }

    public SQLScriptValue operatorAdd(SQLScriptValue val) throws SQLScriptException
    {
        if (type == Type.STRING || val.type == Type.STRING)
            return new SQLScriptValue(getString() + val.getString());
        if (type == Type.INT || val.type == Type.INT)
            return new SQLScriptValue(getInt() + val.getInt());
        throw new SQLScriptException("Operation '+' is not defined for operands");
    }

    public SQLScriptValue operatorSub(SQLScriptValue val) throws SQLScriptException
    {
        if (type == Type.INT || val.type == Type.INT)
            return new SQLScriptValue(getInt() - val.getInt());
        throw new SQLScriptException("Operation '+' is not defined for operands");
    }

    public int compare(SQLScriptValue val) throws SQLScriptException {
        if (type == Type.INT || val.type == Type.INT)
            return getInt().compareTo(val.getInt());
        return getString().compareTo(val.getString());
    }
}
