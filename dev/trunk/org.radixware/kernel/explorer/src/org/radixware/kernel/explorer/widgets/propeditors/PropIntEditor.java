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

import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.meta.mask.EditMaskInt;
import org.radixware.kernel.common.client.meta.mask.EditMaskTimeInterval;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.explorer.editors.valeditors.AdvancedValBoolEditor;

import org.radixware.kernel.explorer.editors.valeditors.ValEditor;
import org.radixware.kernel.explorer.editors.valeditors.ValEditorFactory;
import org.radixware.kernel.explorer.editors.valeditors.ValIntEditor;
import org.radixware.kernel.explorer.editors.valeditors.ValTimeIntervalEditor;

public class PropIntEditor extends PropEditor {

    private static class ValBoolEditorFactoryImpl extends ValEditorFactory {

        public final static ValBoolEditorFactoryImpl INSTANCE = new ValBoolEditorFactoryImpl();

        private ValBoolEditorFactoryImpl() {
        }

        @Override
        public ValEditor<Long> createValEditor(final EValType valType, final EditMask editMask, final IClientEnvironment environment, final QWidget parentWidget) {
            return new AdvancedValBoolEditor<>(environment, parentWidget, editMask, false, false);
        }
    }

    private static class ValIntEditorFactoryImpl extends ValEditorFactory {

        public final static ValIntEditorFactoryImpl INSTANCE = new ValIntEditorFactoryImpl();

        private ValIntEditorFactoryImpl() {
        }

        @Override
        public ValEditor createValEditor(final EValType valType, final EditMask editMask, final IClientEnvironment environment, final QWidget parentWidget) { 
            if (editMask instanceof EditMaskInt) {
                return new ValIntEditor(environment, parentWidget, (EditMaskInt) editMask, false, false);
            } else {
                return new ValTimeIntervalEditor(environment, parentWidget, (EditMaskTimeInterval) editMask, false, false);
            }
        }
    }

    public PropIntEditor(final Property property) {
        this(property, getValEditorFactory(property));
    }
    
    protected PropIntEditor(final Property property, final ValEditorFactory factory){
        super(property,factory);
    }

    public static ValEditorFactory getValEditorFactory(final Property prop) {
        if (prop.getEditMask() instanceof EditMaskInt || prop.getEditMask() instanceof EditMaskTimeInterval) {
            return new ValIntEditorFactoryImpl();
        } else {
            return new ValBoolEditorFactoryImpl();
        }
    }
}
