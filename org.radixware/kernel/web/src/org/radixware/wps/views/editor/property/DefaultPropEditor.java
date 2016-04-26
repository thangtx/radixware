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
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.wps.rwt.InputBox.DisplayController;
import org.radixware.wps.rwt.Label;
import org.radixware.wps.views.editors.valeditors.IValEditor;
import org.radixware.wps.views.editors.valeditors.ValEditorController;
import org.radixware.wps.views.editors.valeditors.ValEditorFactory;
import org.radixware.wps.views.editors.valeditors.ValObjectAsStrEditorController;


public class DefaultPropEditor extends PropEditor {
    
    private static class ValEditorFactoryImpl extends ValEditorFactory{
                
        private final Property property;
        
        public ValEditorFactoryImpl(final Property property){
            this.property = property;
        }
        
        @Override
        public IValEditor createValEditor(final EValType valType, final EditMask editMask, final IClientEnvironment env) {
                final ValEditorController controller = new ValObjectAsStrEditorController(env) {

                    @Override
                    protected DisplayController<Object> createDisplayController() {
                        return new PropertyDisplayController<>(super.createDisplayController(),property);
                    }

                    @Override
                    protected Label createLabel() {
                        return LabelComponentFactory.getDefault().createPropLabelComponent(property);
                    }
                };
                return controller.getValEditor();
        }
        
    }

    public DefaultPropEditor(final Property property) {
        super(property, new ValEditorFactoryImpl(property));
    }
}
