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

package org.radixware.wps.views.editor.property;

import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.editors.property.PropEditorOptions;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.models.items.properties.PropertyChar;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.wps.rwt.InputBox.DisplayController;
import org.radixware.wps.rwt.Label;
import org.radixware.wps.views.editors.valeditors.IValEditor;
import org.radixware.wps.views.editors.valeditors.ValCharEditorController;
import org.radixware.wps.views.editors.valeditors.ValEditorController;


public class PropCharEditor extends PropEditor {
    
    private static class ValEditorFactoryImpl extends AbstractValEditorFactoryImpl<String>{
        
        public ValEditorFactoryImpl(final Property property){
            super(property);
        }

        @Override
        public IValEditor createValEditor(final EValType valType, final EditMask editMask, final IClientEnvironment env) {
            final ValEditorController controller = new ValCharEditorController(env){
                
                @Override
                protected DisplayController<String> createDisplayController() {
                    return ValEditorFactoryImpl.this.createDisplayControllerWrapper(super.createDisplayController());
                }

                @Override
                protected Label createLabel() {
                    return ValEditorFactoryImpl.this.createLabel();
                }                
            };
            return controller.getValEditor();
        }    
    }

    public PropCharEditor(final PropertyChar property) {
        super(property, new ValEditorFactoryImpl(property));
    }

    @Override
    protected Object getCurrentValueInEditor() {
        final String s = (String) super.getCurrentValueInEditor();
        return s == null || s.isEmpty() ? null : Character.valueOf(s.charAt(0));
    }

    @Override
    protected void updateEditor(final Object currentValue, final Object initialValue, PropEditorOptions options) {
        final String curValAsStr = currentValue == null ? null : String.valueOf(currentValue);
        final String initValAsStr = initialValue == null ? null : String.valueOf(initialValue);
        super.updateEditor(curValAsStr, initValAsStr,  options);
    }
}
