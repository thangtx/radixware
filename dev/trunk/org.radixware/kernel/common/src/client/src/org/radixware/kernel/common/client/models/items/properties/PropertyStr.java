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
import org.radixware.kernel.common.types.IKernelStrEnum;

public class PropertyStr extends SimpleProperty<Object> {

    public PropertyStr(final Model owner, final RadPropertyDef propDef) {
        super(owner, propDef);
    }
    
    @Override
    public final RadPropertyDef getDefinition() {
        return super.getDefinition();
    }        

    @Override
    public void setValObjectImpl(final Object value) {
        setInternalVal(value);
    }
    
    @Override
    protected void setInternalVal(final Object value) {
        if (value!=null && value instanceof String==false && value instanceof IKernelStrEnum==false){
            throw new IllegalArgumentException("Unexpected value type "+value.getClass().toString());
        }
        super.setInternalVal(value);
    }    
    

    @Override
    //Must be overriden if enum
    public Class<?> getValClass() {
        return String.class;
    }

    @Override
    public IPropEditor createPropertyEditor() {
        if (getEditMask() != null && getEditMask().getType() == EEditMaskType.STR || getEditMask().getType()==EEditMaskType.BOOL || getEditMask().getType()==EEditMaskType.FILE_PATH) {
            return getEnvironment().getApplication().getStandardViewsFactory().newPropStrEditor(this);
        } else {
            return getEnvironment().getApplication().getStandardViewsFactory().newPropListEditor(this);
        }
    }

    @Override
    public final EValType getType() {
        return EValType.STR;
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
