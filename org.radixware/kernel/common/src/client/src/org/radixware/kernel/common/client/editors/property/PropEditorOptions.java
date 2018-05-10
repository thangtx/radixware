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

package org.radixware.kernel.common.client.editors.property;

import java.util.List;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.types.UnacceptableInput;


public final class PropEditorOptions {
    
    private List<Object> predefinedValues;
    private String tooltip;
    private EditMask editMask;
    private boolean isMandatory;
    private boolean isReadonly;
    private boolean isEnabled;
    private UnacceptableInput unacceptableInput;
            
    public PropEditorOptions(){
        
    }

    public EditMask getEditMask() {
        return editMask;
    }

    public void setEditMask(final EditMask editMask) {
        this.editMask = editMask;
    }

    public boolean isMandatory() {
        return isMandatory;
    }

    public void setMandatory(final boolean isMandatory) {
        this.isMandatory = isMandatory;
    }

    public boolean isReadOnly() {
        return isReadonly;
    }

    public void setReadOnly(final boolean isReadonly) {
        this.isReadonly = isReadonly;
    }

    public List<Object> getPredefinedValues() {
        return predefinedValues;
    }

    public void setPredefinedValues(final List<Object> predefinedValues) {
        this.predefinedValues = predefinedValues;
    }

    public String getTooltip() {
        return tooltip;
    }

    public void setTooltip(final String tooltip) {
        this.tooltip = tooltip;
    } 
    
    public UnacceptableInput getUnacceptableInput(){
        return unacceptableInput;
    }
    
    public void setUnacceptableInput(final UnacceptableInput input){
        unacceptableInput = input;
    }
    
    public boolean isEnabled(){
        return isEnabled;
    }
    
    public void setEnabled(final boolean isEnabled){
        this.isEnabled = isEnabled;
    }
}
