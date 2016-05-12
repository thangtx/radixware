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

import java.sql.Timestamp;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.editors.property.PropEditorOptions;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.models.items.properties.PropertyDateTime;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.wps.rwt.InputBox.DisplayController;
import org.radixware.wps.rwt.Label;
import org.radixware.wps.views.editors.valeditors.IValEditor;
import org.radixware.wps.views.editors.valeditors.ValDateTimeEditorController;
import org.radixware.wps.views.editors.valeditors.ValEditorController;


public class PropDateTimeEditor extends PropEditor {
    
    private static class ValEditorFactoryImpl extends AbstractValEditorFactoryImpl<Timestamp>{
        
        public ValEditorFactoryImpl(final Property property){
            super(property);
        }

        @Override
        public IValEditor createValEditor(final EValType valType, final EditMask editMask, final IClientEnvironment env) {
            final ValEditorController controller = new ValDateTimeEditorController(env){
                @Override
                protected DisplayController<Timestamp> createDisplayController() {
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

    public PropDateTimeEditor(final PropertyDateTime property) {
        super(property, new ValEditorFactoryImpl(property));
    }
    
    @Override
    protected void updateEditor(final Object currentValue, final Object initialValue, final PropEditorOptions options) {
        super.updateEditor(currentValue, initialValue, options);
        ((ValDateTimeEditorController)getValEditor()).setDialogTitle(getProperty().getTitle());
    }    
}