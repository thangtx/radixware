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
import org.radixware.kernel.common.client.meta.mask.EditMaskFilePath;
import org.radixware.kernel.common.client.meta.mask.EditMaskStr;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.models.items.properties.PropertyClob;
import org.radixware.kernel.common.client.models.items.properties.PropertyStr;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.wps.rwt.InputBox.DisplayController;
import org.radixware.wps.rwt.Label;
import org.radixware.wps.views.editors.valeditors.AdvancedValBoolEditorController;
import org.radixware.wps.views.editors.valeditors.IValEditor;
import org.radixware.wps.views.editors.valeditors.ValEditorController;
import org.radixware.wps.views.editors.valeditors.ValEditorFactory;
import org.radixware.wps.views.editors.valeditors.ValFilePathEditorController;
import org.radixware.wps.views.editors.valeditors.ValStrEditorController;


public class PropStrEditor extends PropEditor {

    private static class ValEditorFactoryImpl extends AbstractValEditorFactoryImpl<String> {

        public ValEditorFactoryImpl(final Property property) {
            super(property);
        }

        @Override
        public IValEditor createValEditor(final EValType valType, final EditMask editMask, final IClientEnvironment env) {
            final ValStrEditorController controller = new ValStrEditorController(env) {
                @Override
                protected DisplayController<String> createDisplayController() {
                    return ValEditorFactoryImpl.this.createDisplayControllerWrapper(super.createDisplayController());
                }

                @Override
                protected Label createLabel() {
                    return ValEditorFactoryImpl.this.createLabel();
                }
            };
            if (property.getDefinition().isMemo()) {
                controller.addMemo();
            }
            return controller.getValEditor();
        }
    }
    
        private static class ValFilePathEditorFactoryImpl extends AbstractValEditorFactoryImpl<String> {

        public ValFilePathEditorFactoryImpl(final Property property) {
            super(property);
        }

        @Override
        public IValEditor createValEditor(final EValType valType, final EditMask editMask, final IClientEnvironment env) {
            final ValFilePathEditorController controller = new ValFilePathEditorController(env) {
                @Override
                protected DisplayController<String> createDisplayController() {
                    return ValFilePathEditorFactoryImpl.this.createDisplayControllerWrapper(super.createDisplayController());
                }

                @Override
                protected Label createLabel() {
                    return ValFilePathEditorFactoryImpl.this.createLabel();
                }
            };
            controller.setEditMask((EditMaskFilePath)editMask);
            return controller.getValEditor();
        }
    }

    private static class ValBoolEditorFactoryImpl extends AbstractValEditorFactoryImpl<String> {

        public ValBoolEditorFactoryImpl(final Property property) {
            super(property);
        }

        @Override
        public IValEditor createValEditor(final EValType valType, final EditMask editMask, final IClientEnvironment env) {
            final ValEditorController controller = new AdvancedValBoolEditorController<String>(env) {
                @Override
                protected DisplayController<String> createDisplayController() {
                    return ValBoolEditorFactoryImpl.this.createDisplayControllerWrapper(super.createDisplayController());
                }

                @Override
                protected Label createLabel() {
                    return ValBoolEditorFactoryImpl.this.createLabel();
                }
            };
            return controller.getValEditor();
        }
    }

    public PropStrEditor(final PropertyStr property) {
        super(property, createValEditorFactoryInstance(property));
    }

    public PropStrEditor(final PropertyClob property) {
        super(property, createValEditorFactoryInstance(property));
    }

    private static ValEditorFactory createValEditorFactoryInstance(Property prop) {
        if (prop.getEditMask() instanceof EditMaskStr) {
            return new ValEditorFactoryImpl(prop);
        } else if (prop.getEditMask() instanceof EditMaskFilePath){
            return new ValFilePathEditorFactoryImpl(prop);
        }else {
            return new ValBoolEditorFactoryImpl(prop);
        }
    }

    @Override
    protected void updateEditor(final Object currentValue, final Object initialValue, final PropEditorOptions options) {
        super.updateEditor(currentValue, initialValue, options);
        if (getValEditor() instanceof ValFilePathEditorController){
            ((ValFilePathEditorController)getValEditor()).setReadOnly(!((EditMaskFilePath)getValEditor().getEditMask()).getHandleInputAvailable() || isReadOnly());
        }
    }
}
