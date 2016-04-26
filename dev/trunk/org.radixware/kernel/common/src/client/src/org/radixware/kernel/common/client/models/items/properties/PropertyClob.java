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
import org.radixware.kernel.common.client.meta.mask.EditMaskStr;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.views.IPropEditor;
import org.radixware.kernel.common.enums.EEditMaskType;
import org.radixware.kernel.common.enums.EValType;

public class PropertyClob extends SimpleProperty<String> {

    public PropertyClob(Model owner, RadPropertyDef propDef) {
        super(owner, propDef);
    }
    
    @Override
    public final RadPropertyDef getDefinition() {
        return super.getDefinition();
    }        

    @Override
    public void setValObjectImpl(Object x) {
        setInternalVal((String) x);
    }

    @Override
    public IPropEditor createPropertyEditor() {
        return getEnvironment().getApplication().getStandardViewsFactory().newPropClobEditor(this);
    }

    @Override
    public Class<?> getValClass() {
        return String.class;
    }

    @Override
    public final EValType getType() {
        return EValType.CLOB;
    }

    @Override
    public boolean hasValidMandatoryValue() {//RADIX-4803
        if (!super.hasValidMandatoryValue()) {
            return false;
        }
        if (getEditMask() != null && getEditMask().getType() == EEditMaskType.STR) {
            final EditMaskStr mask = (EditMaskStr) getEditMask();
            final String value = (String) getValueObject();
            return value == null || !value.isEmpty() || mask.isEmptyStringAllowed();
        }
        return true;
    }
}
