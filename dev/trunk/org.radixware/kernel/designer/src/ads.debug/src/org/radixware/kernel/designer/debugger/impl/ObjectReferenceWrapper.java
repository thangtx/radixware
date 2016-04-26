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

import com.sun.jdi.AbsentInformationException;
import com.sun.jdi.ArrayReference;
import com.sun.jdi.ClassNotLoadedException;
import com.sun.jdi.ClassType;
import com.sun.jdi.Field;
import com.sun.jdi.Method;
import com.sun.jdi.ObjectReference;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.StringReference;
import com.sun.jdi.Type;
import com.sun.jdi.Value;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import org.radixware.kernel.designer.debugger.RadixDebugger;


public class ObjectReferenceWrapper extends ValueWrapper {

    private final Map<Field, FieldWrapper> fields = new WeakHashMap<Field, FieldWrapper>();

    public ObjectReferenceWrapper(RadixDebugger debugger, ObjectReference value) {
        super(debugger, value);
    }

    @Override
    public String toString() {
        if (value == null) {
            return "null";
        }
        if (value instanceof StringReference) {
            StringReference string = (StringReference) value;
            return "\"" + string.value() + "\"";
        } else if (value instanceof ArrayReference) {
            ArrayReference ref = (ArrayReference) value;
            return "#" + String.valueOf(ref.uniqueID()) + " size=" + ref.length();
        } else {
            ObjectReference ref = (ObjectReference) value;
            return "#" + String.valueOf(ref.uniqueID());
        }
    }

    public ValueWrapper getFieldValue(Field field) {
        ObjectReference ref = (ObjectReference) value;
        return ValueWrapper.newInstance(debugger, ref.getValue(field));
    }

    public FieldWrapper getField(Field field) {
        synchronized (fields) {
            FieldWrapper wrapper = fields.get(field);
            if (wrapper == null) {
                wrapper = new FieldWrapper(this, field);
                fields.put(field, wrapper);
            }
            return wrapper;
        }
    }

    public List<FieldWrapper> getStaticFields() {
        ObjectReference ref = (ObjectReference) value;
        List<Field> allFields = new ArrayList<Field>(ref.referenceType().allFields());

        List<FieldWrapper> wrappers = new LinkedList<FieldWrapper>();
        for (Field field : allFields) {
            if (field.isStatic()) {
                wrappers.add(getField(field));
            }
        }
        return wrappers;
    }

    public boolean hasStaticFields() {
        ObjectReference ref = (ObjectReference) value;
        List<Field> allFields = new ArrayList<Field>(ref.referenceType().allFields());

        List<FieldWrapper> wrappers = new LinkedList<FieldWrapper>();
        for (Field field : allFields) {
            if (field.isStatic()) {
                return true;
            }
        }
        return false;
    }

    public boolean hasProperties() {
        ReferenceTypeWrapper type = getTypeWrapper();
        if (type != null) {
            return type.hasProperties();
        } else {
            return false;
        }
    }

    public List<PropertyWrapper> getProperties() {
        ReferenceTypeWrapper type = getTypeWrapper();
        if (type != null) {
            return type.getProperties(this);
        } else {
            return Collections.emptyList();
        }
    }

    public List<FieldWrapper> getOwnFields() {
        ObjectReference ref = (ObjectReference) value;
        List<Field> allFields = new ArrayList<Field>(ref.referenceType().fields());

        List<FieldWrapper> wrappers = new LinkedList<FieldWrapper>();
        for (Field field : allFields) {
            if (!field.isStatic()) {
                wrappers.add(getField(field));
            }
        }
        return wrappers;
    }

    public List<FieldWrapper> getInheritedFields() {
        ObjectReference ref = (ObjectReference) value;
        final ReferenceType type = ref.referenceType();
        List<Field> allFields = new ArrayList<Field>(type.allFields());

        List<FieldWrapper> wrappers = new LinkedList<FieldWrapper>();
        for (Field field : allFields) {
            if (!field.isStatic() && field.declaringType() != type) {
                wrappers.add(getField(field));
            }
        }
        return wrappers;
    }

    public boolean hasInheritedFields() {
        ObjectReference ref = (ObjectReference) value;
        final ReferenceType type = ref.referenceType();
        List<Field> allFields = new ArrayList<Field>(type.allFields());

        List<FieldWrapper> wrappers = new LinkedList<FieldWrapper>();
        for (Field field : allFields) {
            if (field.declaringType() != type) {
                return true;
            }
        }
        return false;
    }

    public static com.sun.jdi.Value invokeMethod(com.sun.jdi.ObjectReference a, com.sun.jdi.ThreadReference b, com.sun.jdi.Method c, java.util.List<? extends com.sun.jdi.Value> d, int e) throws com.sun.jdi.InvalidTypeException, com.sun.jdi.ClassNotLoadedException, com.sun.jdi.IncompatibleThreadStateException, com.sun.jdi.InvocationException, com.sun.jdi.InternalException, com.sun.jdi.VMDisconnectedException, com.sun.jdi.ObjectCollectedException {
        try {
            com.sun.jdi.Value ret;
            ret = a.invokeMethod(b, c, d, e);
            return ret;
        } catch (com.sun.jdi.InternalException iex) {
            if (iex.errorCode() == 502) { // ALREADY_INVOKING
                return null;
            } else {
                throw iex;
            }
        }
    }

    public ObjectReference getValue() {
        return (ObjectReference) value;
    }

    @Override
    protected String calcTypeName(String name) {
        return debugger.getNameResolver().className2DefinitionName(name);
    }

    public ReferenceTypeWrapper getTypeWrapper() {
        return debugger.getTypesCache().getReferenceType((ReferenceType) value.type());
    }

//    public void showRadixWareObjectInfo() {
//        ReferenceTypeWrapper typeWrapper = getTypeWrapper();
//        if (typeWrapper != null) {
//            ReferenceTypeWrapper.RadixWareClassInfo rci = typeWrapper.getRadixWareClassInfo(this);
//            if (rci == null) {
//                DialogUtils.messageError("Varaible type does not seems like RadixWare class");
//            } else {
//                RadixReferenceDetailsPanel panel = new RadixReferenceDetailsPanel(rci);
//                ModalDisplayer displayer = new ModalDisplayer(panel, "Details for " + rci.getClassDef().getQualifiedName());
//                displayer.showModal();
//            }
//        }
//    }
    public ValueWrapper getMethodResultAsStr(String methodName, Value[] arguments) {
        if (value == null) {
            return null;
        }

        Type type = value.type();
        if (type instanceof ClassType) {
            List<Method> methods = ((ClassType) type).methodsByName(methodName);
            Method method = null;
            for (Method m : methods) {
                if (!m.isAbstract() && !m.isConstructor()) {
                    try {
                        try {
                            if (m.returnType() != null && m.arguments().size() == arguments.length) {
                                method = m;
                                break;
                            }
                        } catch (ClassNotLoadedException ex) {
                            continue;
                        }
                    } catch (AbsentInformationException ex) {
                        continue;
                    }
                }
                System.out.println(m.toString());
            }

            //Method method = ((ClassType) type).concreteMethodByName(methodName, methodSignature);
            if (method == null) {
                return null;
            }
            Object val = debugger.invokeMethod((ObjectReference) value, method, arguments);
            if (val == ObjectReferenceWrapper.NOT_AVAILABLE_VALUE) {
                return null;
            } else if (val instanceof Value) {
                return ValueWrapper.newInstance(debugger, (Value) val);
            } else {
                return null;
            }
        } else {
            return null;
        }

    }
}
