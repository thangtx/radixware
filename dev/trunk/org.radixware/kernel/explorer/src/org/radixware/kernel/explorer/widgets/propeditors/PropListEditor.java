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

package org.radixware.kernel.explorer.widgets.propeditors;

import org.radixware.kernel.common.client.editors.property.PropertyProxy;
import org.radixware.kernel.common.client.meta.mask.EditMaskConstSet;
import org.radixware.kernel.common.client.meta.mask.EditMaskList;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.types.IKernelEnum;
import org.radixware.kernel.explorer.editors.valeditors.ValConstSetEditor;
import org.radixware.kernel.explorer.editors.valeditors.ValEditor;
import org.radixware.kernel.explorer.editors.valeditors.ValEditorFactory;
import org.radixware.kernel.explorer.editors.valeditors.ValListEditor;

public class PropListEditor extends PropEditor {

    public PropListEditor(final Property property) {
        this(property, getValEditorFactory());
    }
    
    protected PropListEditor(final Property property, final ValEditorFactory factory){
        super(property,factory);
        setup(property);
    }

    @Override
    void setProperty(final Property property) {
        super.setProperty(property);
        if (property!=null){
            setup(property);
        }
    }

    @Override
    void clear() {
        final ValEditor valEditor = getValEditor();
        valEditor.valueChanged.disconnect(this);
        super.clear();
        if (valEditor instanceof ValConstSetEditor){
            ((ValConstSetEditor)valEditor).setEditMask(EditMaskConstSet.getEmptyMask());
        }else if (valEditor instanceof ValListEditor){
            ((ValListEditor)valEditor).setEditMask(new EditMaskList());
        }
    }
            
    private void setup(final Property property){
        setPropertyProxy(new PropertyProxy(property){

            @Override
            public Object getPropertyInitialValue() {
                final Object value = super.getPropertyInitialValue();
                if ((getProperty().getEditMask() instanceof EditMaskConstSet) && (value instanceof IKernelEnum)) {
                    return ((IKernelEnum) value).getValue();
                }
                return value;
            }                        

            @Override
            public Object getPropertyValue() {
                final Object value = super.getPropertyValue();
                if ((getProperty().getEditMask() instanceof EditMaskConstSet) && (value instanceof IKernelEnum)) {
                    return ((IKernelEnum) value).getValue();
                }
                return value;
            }

        });        
    }

    @Override
    public void bind() {
        super.bind();
        getValEditor().valueChanged.disconnect();
        getValEditor().valueChanged.connect(this,"finishEdit()");                
    }
    
    
    
    public static ValEditorFactory getValEditorFactory(){
        return ValEditorFactory.getDefault();
    }
}