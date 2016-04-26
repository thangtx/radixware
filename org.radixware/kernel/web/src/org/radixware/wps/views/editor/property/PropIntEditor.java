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
import org.radixware.kernel.common.client.models.items.properties.PropertyInt;
import org.radixware.kernel.common.enums.EEditMaskType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.wps.rwt.InputBox.DisplayController;
import org.radixware.wps.rwt.Label;
import org.radixware.wps.views.editors.valeditors.AdvancedValBoolEditorController;
import org.radixware.wps.views.editors.valeditors.IValEditor;
import org.radixware.wps.views.editors.valeditors.ValEditorController;
import org.radixware.wps.views.editors.valeditors.ValEditorFactory;
import org.radixware.wps.views.editors.valeditors.ValIntEditorController;
import org.radixware.wps.views.editors.valeditors.ValTimeIntervalEditorController;


public class PropIntEditor extends PropEditor {

    private static class ValIntEditorFactoryImpl extends AbstractValEditorFactoryImpl<Long> {

        public ValIntEditorFactoryImpl(final Property property) {
            super(property);
        }

        @Override
        public IValEditor createValEditor(final EValType valType, final EditMask editMask, final IClientEnvironment env) {
            final ValEditorController controller = new ValIntEditorController(env) {
                @Override
                protected DisplayController<Long> createDisplayController() {
                    return ValIntEditorFactoryImpl.this.createDisplayControllerWrapper(super.createDisplayController());
                }

                @Override
                protected Label createLabel() {
                    return ValIntEditorFactoryImpl.this.createLabel();
                }
            };
            return controller.getValEditor();
        }
    }

    private static class ValTimeIntervalEditorFactoryImpl extends AbstractValEditorFactoryImpl<Long> {

        public ValTimeIntervalEditorFactoryImpl(final Property property) {
            super(property);
        }

        @Override
        public IValEditor createValEditor(final EValType valType, final EditMask editMask, final IClientEnvironment env) {
            final ValEditorController controller = new ValTimeIntervalEditorController(env) {
                @Override
                protected DisplayController<Long> createDisplayController() {
                    return ValTimeIntervalEditorFactoryImpl.this.createDisplayControllerWrapper(super.createDisplayController());
                }

                @Override
                protected Label createLabel() {
                    return ValTimeIntervalEditorFactoryImpl.this.createLabel();
                }
            };
            return controller.getValEditor();
        }
    }

    private static class ValBoolEditorFactoryImpl extends AbstractValEditorFactoryImpl<Long> {

        public ValBoolEditorFactoryImpl(final Property property) {
            super(property);

        }

        @Override
        public IValEditor createValEditor(final EValType valType, final EditMask editMask, final IClientEnvironment env) {
            final ValEditorController controller = new AdvancedValBoolEditorController<Long>(env) {
                @Override
                protected DisplayController<Long> createDisplayController() {
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

    public PropIntEditor(final PropertyInt property) {
        super(property, createValEditorFactory(property));
    }

    private static ValEditorFactory createValEditorFactory(final Property property) {
        if (property.getEditMask().getType() == EEditMaskType.INT) {
            return new ValIntEditorFactoryImpl(property);
        } else if (property.getEditMask().getType() == EEditMaskType.BOOL) {

            return new ValBoolEditorFactoryImpl(property);
        } else {
            return new ValTimeIntervalEditorFactoryImpl(property);
        }
    }
}
