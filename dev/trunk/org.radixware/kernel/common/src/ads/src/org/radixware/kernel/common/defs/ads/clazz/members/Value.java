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

package org.radixware.kernel.common.defs.ads.clazz.members;

import java.util.List;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.type.AdsDefinitionType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.ads.type.IAdsTypedObject;
import org.radixware.kernel.common.utils.events.RadixEvent;
import org.radixware.kernel.common.utils.events.RadixEventSource;


public abstract class Value extends RadixObject implements IAdsTypedObject {

    private RadixEventSource typeChangeSupport = null;

    public RadixEventSource getTypeChangeSupport() {
        synchronized (this) {
            if (typeChangeSupport == null) {
                typeChangeSupport = new RadixEventSource();
            }
            return typeChangeSupport;
        }
    }
    private AdsTypeDeclaration type;

    @Override
    public AdsTypeDeclaration getType() {
        return type == null ? AdsTypeDeclaration.UNDEFINED : type;
    }

    public AdsTypeDeclaration getTypeForCodeWriter() {
        return getType();
    }

    protected Value(String name) {
        super(name);
    }

    protected Value(String name, AdsTypeDeclaration type) {
        super(name);
        this.type = type;
    }

    protected Value(AdsTypeDeclaration type) {
        this.type = type;
    }

    protected Value() {
    }

    @SuppressWarnings("unchecked")
    public void setType(AdsTypeDeclaration type) {
        synchronized (this) {
            this.type = type;
            if (typeChangeSupport != null) {
                typeChangeSupport.fireEvent(new RadixEvent());
            }
            setEditState(EEditState.MODIFIED);
        }
    }

    @Override
    public void collectDependences(List<Definition> list) {
        super.collectDependences(list);
        if (type != null) {
            List<AdsType> allTypes = type.resolveAllTypes((AdsDefinition) getDefinition());
            for (AdsType resolvedType : allTypes) {
                if (resolvedType instanceof AdsDefinitionType) {
                    Definition def = ((AdsDefinitionType) resolvedType).getSource();
                    if (def != null) {
                        list.add(def);
                    }
                }
            }
        }
    }
}
