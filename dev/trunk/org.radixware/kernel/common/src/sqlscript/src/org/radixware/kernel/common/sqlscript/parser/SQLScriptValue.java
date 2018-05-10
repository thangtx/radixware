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

import java.util.Objects;


public class SQLScriptValue {
    private final Type type;
    private final Object value;

    public enum Type { UNDEFINED, STRING, INT };

    public SQLScriptValue() {
        type = Type.UNDEFINED;
        value = null;
    }

    public SQLScriptValue(final Type type, final String value) {
        if (type == null) {
            throw new IllegalArgumentException("Value type can't be null");
        }
        else if (value == null) {
            throw new IllegalArgumentException("String value can't be null");
        }
        else {
            switch (this.type = type) {
                case STRING:
                    this.value = value;
                    break;
                case INT:
                    try{this.value = Integer.parseInt(value);
                    } catch (NumberFormatException exc) {
                        throw new IllegalArgumentException("String ["+value+"] contain invalid integer value");
                    }
                    break;
                default :
                    this.value = null;
            }
        }
    }

    public SQLScriptValue(final String pValue) {
        this(Type.STRING,pValue);
    }

    public SQLScriptValue(final Integer pValue) {
        if (pValue == null) {
            throw new IllegalArgumentException("Integer value can't be null");
        }
        else {
            this.type = Type.INT;
            this.value = pValue;
        }
    }

    public SQLScriptValue(final Boolean pValue) {
        if (pValue == null) {
            throw new IllegalArgumentException("Integer value can't be null");
        }
        else {
            this.type = Type.INT;
            this.value = pValue ? Integer.valueOf(1) : Integer.valueOf(0);
        }
    }

    public Type getType() {
        return type;
    }

    public String getString() throws SQLScriptException {
        switch (type) {
            case STRING:
                return (String)value;
            case INT:
                return ((Integer)value).toString();
            default :
                throw new SQLScriptException("SQLScriptValue::getString : the type of the SQL script value ["+value+"] is undefined");
        }
    }

    public Integer getInt() throws SQLScriptException {
        switch (type) {
            case STRING:
                if (((String)value).equalsIgnoreCase("TRUE")) {
                    return 1;
                }
                else if (((String)value).equalsIgnoreCase("FALSE")) {
                    return 0;
                }
                else {
                    try{return Integer.parseInt((String)value);
                    } catch (NumberFormatException exc) {
                        throw new SQLScriptException("SQLScriptValue::getInt : content of the string ["+value+"] is not a valid integer");
                    }
                }
            case INT:
                return ((Integer)value);
            default :
                throw new SQLScriptException("SQLScriptValue::getInt : the type of the SQL script value ["+value+"] is undefined");
        }
    }

    public boolean getBoolean() throws SQLScriptException {
        return getInt() != 0;
    }

    public SQLScriptValue negation() throws SQLScriptException {
        return new SQLScriptValue(!getBoolean());
    }

    public SQLScriptValue operatorAdd(final SQLScriptValue val) throws SQLScriptException {
        if (val == null) {
            throw new IllegalArgumentException("Value to add can't be null");
        }
        else {
            if (type == Type.UNDEFINED || val.type == Type.UNDEFINED) {
                throw new SQLScriptException("Operation '+' is not supported for operands ["+type+"] and ["+val.type+"]");
            }
            if (type == Type.STRING || val.type == Type.STRING) {
                return new SQLScriptValue(getString() + val.getString());
            }
            return new SQLScriptValue(getInt() + val.getInt());
        }
    }

    public SQLScriptValue operatorSub(final SQLScriptValue val) throws SQLScriptException {
        if (val == null) {
            throw new IllegalArgumentException("Value to subtract can't be null");
        }
        else {
            if (type == Type.UNDEFINED || val.type == Type.UNDEFINED) {
                throw new SQLScriptException("Operation '+' is not supported for operands ["+type+"] and ["+val.type+"]");
            }
            if (type == Type.INT || val.type == Type.INT) {
                return new SQLScriptValue(getInt() - val.getInt());
            }
            throw new SQLScriptException("Operation '-' is not supported for operands ["+getType()+"] and ["+val.getType()+"]");
        }
    }

    public int compare(final SQLScriptValue val) throws SQLScriptException {
        if (val == null) {
            throw new IllegalArgumentException("Value to compare can't be null");
        }
        else {
            if (type == Type.UNDEFINED || val.type == Type.UNDEFINED) {
                throw new SQLScriptException("Operation '+' is not supported for operands ["+type+"] and ["+val.type+"]");
            }
            if (type == Type.INT || val.type == Type.INT) {
                return getInt().compareTo(val.getInt());
            }
            else {
                return getString().compareTo(val.getString());
            }
        }
    }

    @Override
    public String toString() {
        return "SQLScriptValue{" + "type=" + type + ", value=" + value + '}';
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + Objects.hashCode(this.type);
        hash = 17 * hash + Objects.hashCode(this.value);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SQLScriptValue other = (SQLScriptValue) obj;
        if (this.type != other.type) {
            return false;
        }
        if (!Objects.equals(this.value, other.value)) {
            return false;
        }
        return true;
    }
}
