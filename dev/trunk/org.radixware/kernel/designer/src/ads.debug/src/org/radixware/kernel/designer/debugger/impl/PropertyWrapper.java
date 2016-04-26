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

import com.sun.jdi.Value;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.designer.debugger.RadixDebugger;


public class PropertyWrapper extends VariableWrapper {

    private final ObjectReferenceWrapper context;
    private final RadixDebugger debugger;
    private final AdsPropertyDef prop;

    public PropertyWrapper(ObjectReferenceWrapper context, RadixDebugger debugger, AdsPropertyDef prop) {
        this.context = context;
        this.debugger = debugger;
        this.prop = prop;
    }

    @Override
    public String getDisplayName() {
        return prop.getName();
    }

    @Override
    public String getTypeName() {
        return prop.getValue().getType().getQualifiedName(prop);
    }

    @Override
    public ValueWrapper getValue() {
        return context.getMethodResultAsStr("get" + prop.getId().toString(), new Value[0]);
    }
}
