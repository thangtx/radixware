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

import com.sun.jdi.Field;
import com.sun.jdi.InternalException;
import com.sun.jdi.ObjectCollectedException;
import com.sun.jdi.VMDisconnectedException;
import org.radixware.kernel.designer.debugger.RadixDebugger;


public class FieldWrapper extends VariableWrapper {

    private final Field field;
    private final RadixDebugger debugger;
    private final ObjectReferenceWrapper wrapper;

    public FieldWrapper(ObjectReferenceWrapper wrapper, Field field) {
        this.field = field;
        this.wrapper = wrapper;
        this.debugger = wrapper.debugger;
    }

    public FieldWrapper(RadixDebugger debugger, Field field) {
        this.field = field;
        this.wrapper = null;
        this.debugger = debugger;
    }

    @Override
    public ValueWrapper getValue() {
        if (wrapper == null) {
            return ValueWrapper.newInstance(debugger, field.declaringType().getValue(field));
        } else {
            return wrapper.getFieldValue(field);
        }
    }

    @Override
    public String getDisplayName() {
        try {
            return field.name();
        } catch (VMDisconnectedException e) {
            return "<Disconnected from target VM>";
        } catch (ObjectCollectedException e) {
            return "<Collected Object>";
        } catch (InternalException e) {
            return "<Internal Exception in Target VM>";
        }
    }

    @Override
    public String getTypeName() {
        try {
            return field.typeName();
        } catch (VMDisconnectedException e) {
            return "<Disconnected from target VM>";
        } catch (ObjectCollectedException e) {
            return "<Collected Object>";
        } catch (InternalException e) {
            return "<Internal Exception in Target VM>";
        }
    }
}
