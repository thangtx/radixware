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
import com.sun.jdi.ReferenceType;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.IModelPublishableProperty;
import org.radixware.kernel.designer.debugger.RadixDebugger;


public class ReferenceTypeWrapper {

    private RadixDebugger debugger;
    private ReferenceType type;
    private final Map<Field, FieldWrapper> fields = new WeakHashMap<Field, FieldWrapper>();

    public ReferenceTypeWrapper(RadixDebugger debugger, ReferenceType type) {
        this.debugger = debugger;
        this.type = type;
    }

    public FieldWrapper getFieldWrapper(Field f) {
        synchronized (fields) {
            FieldWrapper w = fields.get(f);
            if (w == null) {
                w = new FieldWrapper(debugger, f);
                fields.put(f, w);
            }
            return w;
        }
    }

    public List<FieldWrapper> getStaticFields() {
        List<FieldWrapper> statics = new LinkedList<FieldWrapper>();
        for (Field f : type.allFields()) {
            if (f.isStatic()) {
                statics.add(getFieldWrapper(f));
            }
        }
        return statics;
    }

    public List<PropertyWrapper> getProperties(ObjectReferenceWrapper context) {
        List<PropertyWrapper> list = new LinkedList<PropertyWrapper>();
        AdsDefinition def = this.debugger.getNameResolver().findDefinitionByClassName(type.name());
        boolean done = false;
        if (def instanceof AdsClassDef) {
            if (def instanceof AdsModelClassDef) {
                IModelPublishableProperty.Provider provider = ((AdsModelClassDef) def).findModelPropertyProvider();
                if (provider != null) {
                    IModelPublishableProperty.Support support = provider.getModelPublishablePropertySupport();
                    if (support != null) {
                        for (IModelPublishableProperty prop : support.list(((AdsClassDef) def).getClientEnvironment(), EScope.LOCAL, null)) {
                            if (prop instanceof AdsPropertyDef) {
                                list.add(new PropertyWrapper(context, debugger, (AdsPropertyDef) prop));
                            }
                        }
                        done = true;
                    }
                }
            }
            if (!done) {
                for (AdsPropertyDef prop : ((AdsClassDef) def).getProperties().get(EScope.ALL)) {
                    list.add(new PropertyWrapper(context, debugger, prop));
                }
            }
        }
        return list;

    }

    public boolean hasProperties() {
        AdsDefinition def = this.debugger.getNameResolver().findDefinitionByClassName(type.name());
        if (def instanceof AdsClassDef) {
            return true;
        } else {
            return false;
        }
    }
}
