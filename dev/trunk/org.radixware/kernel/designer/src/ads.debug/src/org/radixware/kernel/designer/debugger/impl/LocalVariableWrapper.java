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

import com.sun.jdi.LocalVariable;


public class LocalVariableWrapper extends VariableWrapper {

    private String variableName;
    private String typeName;
    private CallStackFrame frame;

    public LocalVariableWrapper(CallStackFrame frame, LocalVariable var) {
        this.variableName = var.name();
        this.typeName = var.typeName();
        this.frame = frame;
    }

    @Override
    public ValueWrapper getValue() {
        return frame.getValue(this);
    }

    public String getVariableName() {
        return variableName;
    }

    @Override
    public String getDisplayName() {
        return variableName;
    }

    @Override
    public String getTypeName() {
        return typeName;
    }
}
