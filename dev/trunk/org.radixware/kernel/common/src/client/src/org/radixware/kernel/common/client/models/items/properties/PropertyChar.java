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

package org.radixware.kernel.common.client.models.items.properties;

import org.radixware.kernel.common.client.meta.RadPropertyDef;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.views.IPropEditor;
import org.radixware.kernel.common.enums.EEditMaskType;
import org.radixware.kernel.common.enums.EValType;

public class PropertyChar extends SimpleProperty<Object> {

    public PropertyChar(Model owner, RadPropertyDef propDef) {
        super(owner, propDef);
    }
    
    @Override
    public final RadPropertyDef getDefinition() {
        return super.getDefinition();
    }        

    @Override
    public void setValObjectImpl(Object x) {
        setInternalVal(x);
    }

    /*@Override
    public void setServerValObject(Object x) {
    setServerVal((Character) x);
    }
     */
    @Override
    //Must be overriden for enum
    public Class<?> getValClass() {
        return Character.class;
    }

    @Override
    public IPropEditor createPropertyEditor() {
        if (getEditMask() != null && getEditMask().getType() == EEditMaskType.STR) {
            return getEnvironment().getApplication().getStandardViewsFactory().newPropCharEditor(this);
        } else {
            return getEnvironment().getApplication().getStandardViewsFactory().newPropListEditor(this);
        }
    }

    @Override
    public final EValType getType() {
        return EValType.CHAR;
    }
}