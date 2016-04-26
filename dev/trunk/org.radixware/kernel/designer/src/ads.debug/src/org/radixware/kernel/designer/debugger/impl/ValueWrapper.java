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

package org.radixware.kernel.designer.debugger.impl;

import com.sun.jdi.ArrayReference;
import com.sun.jdi.BooleanValue;
import com.sun.jdi.ByteValue;
import com.sun.jdi.CharValue;
import com.sun.jdi.ClassType;
import com.sun.jdi.DoubleValue;
import com.sun.jdi.FloatValue;
import com.sun.jdi.IntegerValue;
import com.sun.jdi.InternalException;
import com.sun.jdi.LongValue;
import com.sun.jdi.Method;
import com.sun.jdi.ObjectCollectedException;
import com.sun.jdi.ObjectReference;
import com.sun.jdi.PrimitiveValue;
import com.sun.jdi.ShortValue;
import com.sun.jdi.StringReference;
import com.sun.jdi.Value;
import com.sun.jdi.Type;
import com.sun.jdi.VMDisconnectedException;
import com.sun.jdi.VoidValue;
import org.radixware.kernel.designer.debugger.RadixDebugger;


public class ValueWrapper {

    public static final Object NOT_AVAILABLE_VALUE = new Object();

    public static ValueWrapper newInstance(RadixDebugger debugger, Value value) {
        if (value instanceof ObjectReference) {
            if (value instanceof ArrayReference) {
                return new ArrayReferenceWrapper(debugger, (ObjectReference) value);
            }
            return new ObjectReferenceWrapper(debugger, (ObjectReference) value);
        } else {
            return new ValueWrapper(debugger, value);
        }
    }
    protected final Value value;
    protected final RadixDebugger debugger;

    ValueWrapper(RadixDebugger debugger, Value value) {
        this.value = value;
        this.debugger = debugger;
    }

    protected String calcTypeName(String name) {
        return name;
    }

    public String getTypeName() {
        try {
            if (value == null) {
                return null;
            }
            Type type = value.type();
            return calcTypeName(type.name());
        } catch (VMDisconnectedException e) {
            return "<Disconnected from target VM>";
        } catch (ObjectCollectedException e) {
            return "<Object Collected>";
        } catch (InternalException e) {
            return "<Inrernal exception in target VM>";
        }
    }

    @Override
    public String toString() {
        if (value == null) {
            return "null";
        }
        if (value instanceof BooleanValue) {
            return String.valueOf(((BooleanValue) value).booleanValue());
        } else if (value instanceof ByteValue) {
            return String.valueOf(((ByteValue) value).byteValue());
        } else if (value instanceof CharValue) {
            return "'" + String.valueOf(((CharValue) value).charValue()) + "'";
        } else if (value instanceof DoubleValue) {
            return String.valueOf(((DoubleValue) value).doubleValue());
        } else if (value instanceof FloatValue) {
            return String.valueOf(((FloatValue) value).floatValue());
        } else if (value instanceof IntegerValue) {
            return String.valueOf(((IntegerValue) value).intValue());
        } else if (value instanceof LongValue) {
            return String.valueOf(((LongValue) value).longValue());
        } else if (value instanceof ShortValue) {
            return String.valueOf(((ShortValue) value).shortValue());
        } else if (value instanceof VoidValue) {
            return "";
        } else {
            return "<unknown value type>";
        }
    }
    public static final String NOT_AVAILABLE_TO_STRING_VALUE = "<html><font color=\"#FF0000\">&lt;N/A&gt;</font></html>";

    public String getToStringValue() {
        if (value == null) {
            return "null";
        }
        if (value instanceof PrimitiveValue) {
            return toString();
        } else if (value instanceof StringReference) {
            return ((StringReference) value).value();
        } else if (value instanceof ArrayReference) {
            return toString();
        } else {
            Type type = value.type();
            if (type instanceof ClassType) {
                Method toString = ((ClassType) type).concreteMethodByName("toString", "()Ljava/lang/String;");
                Object val = debugger.invokeMethod((ObjectReference) value, toString, new Value[0]);
                if (val == ObjectReferenceWrapper.NOT_AVAILABLE_VALUE) {
                    return NOT_AVAILABLE_TO_STRING_VALUE;
                } else if (val instanceof StringReference) {
                    return ((StringReference) val).value();
                } else {
                    return NOT_AVAILABLE_TO_STRING_VALUE;
                }
            } else {
                return NOT_AVAILABLE_TO_STRING_VALUE;
            }
        }
    }

    
}
