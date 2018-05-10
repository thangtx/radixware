/*
* Copyright (c) 2008-2016, Compass Plus Limited. All rights reserved.
*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at http://mozilla.org/MPL/2.0/.
* This Source Code is distributed WITHOUT ANY WARRANTY; including any 
* implied warranties but not limited to warranty of MERCHANTABILITY 
* or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
* License, v. 2.0. for more details.
*/

package org.radixware.kernel.common.client.widgets.arreditor;

import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.RadSelectorPresentationDef;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.meta.mask.EditMaskRef;
import org.radixware.kernel.common.client.models.items.properties.PropertyArrRef;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Id;


public final class ArrayItemEditingOptions {        
    
    private final EValType valType;
    private final EditMask editMask;
    private final boolean isMandatory;
    private final boolean isDuplicatesEnabled;
    private final PropertyArrRef propertyRef;
    private final RadSelectorPresentationDef presentation;

    public ArrayItemEditingOptions(final EValType valType, final EditMask editMask, final boolean isMandatory, final boolean isDuplicatesEnabled) {
        this(valType, editMask, isMandatory, isDuplicatesEnabled, (RadSelectorPresentationDef) null);
    }

    public ArrayItemEditingOptions(final EValType valType, final EditMask editMask, final boolean isMandatory, final boolean isDuplicatesEnabled, final PropertyArrRef propertyRef) {
        this.valType = valType;
        this.editMask = editMask;
        this.isMandatory = isMandatory;
        this.isDuplicatesEnabled = isDuplicatesEnabled;
        this.propertyRef = propertyRef;
        this.presentation = null;
    }

    public ArrayItemEditingOptions(final EValType valType, final EditMask editMask, final boolean isMandatory, final boolean isDuplicatesEnabled, final RadSelectorPresentationDef presentation) {
        this.valType = valType;
        this.editMask = editMask;
        this.isMandatory = isMandatory;
        this.isDuplicatesEnabled = isDuplicatesEnabled;
        this.presentation = presentation;
        this.propertyRef = null;
    }

    public EValType getValType() {
        return valType;
    }

    public EditMask getEditMask() {
        return editMask;
    }

    public boolean isMandatory() {
        return isMandatory;
    }

    public boolean isDuplicatesEnabled() {
        return isDuplicatesEnabled;
    }

    public PropertyArrRef getPropertyRef() {
        return propertyRef;
    }

    public RadSelectorPresentationDef getSelectorPresentation(final IClientEnvironment environment) {
        if (propertyRef == null) {
            if (presentation == null) {
                if (editMask instanceof EditMaskRef) {
                    final Id presentationId = ((EditMaskRef) editMask).getSelectorPresentationId();
                    if (presentationId != null) {
                        return environment.getDefManager().getSelectorPresentationDef(presentationId);
                    }
                }
                return null;
            } else {
                return presentation;
            }
        } else {
            return propertyRef.getParentSelectorPresentation();
        }
    }

}
