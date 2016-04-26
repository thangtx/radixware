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
import java.math.BigDecimal;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.meta.mask.EditMaskNum;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.models.items.properties.PropertyNum;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.explorer.editors.valeditors.AdvancedValBoolEditor;
import org.radixware.kernel.explorer.editors.valeditors.ValEditor;
import org.radixware.kernel.explorer.editors.valeditors.ValEditorFactory;
import org.radixware.kernel.explorer.editors.valeditors.ValNumEditor;

public class PropNumEditor extends PropEditor {

    private static class ValBoolEditorFactoryImpl extends ValEditorFactory {

        public final static ValBoolEditorFactoryImpl INSTANCE = new ValBoolEditorFactoryImpl();

        private ValBoolEditorFactoryImpl() {
        }

        @Override
        public ValEditor<BigDecimal> createValEditor(final EValType valType, final EditMask editMask, final IClientEnvironment environment, final QWidget parentWidget) {
            return new AdvancedValBoolEditor<>(environment, parentWidget, editMask, false, false);
        }
    }

    private static class ValNumEditorFactoryImpl extends ValEditorFactory {

        public final static ValNumEditorFactoryImpl INSTANCE = new ValNumEditorFactoryImpl();

        private ValNumEditorFactoryImpl() {
        }

        @Override
        public ValEditor createValEditor(final EValType valType, final EditMask editMask, final IClientEnvironment environment, final QWidget parentWidget) {
            return new ValNumEditor(environment, parentWidget, (EditMaskNum) editMask, false, false);
        }
    }

    public PropNumEditor(final PropertyNum property) {
        this(property, getValEditorFactory(property));
    }
    
    protected PropNumEditor(final PropertyNum property, final ValEditorFactory factory){
        super(property,factory);
    }

    public static ValEditorFactory getValEditorFactory(final Property property) {
        if (property.getEditMask() instanceof EditMaskNum) {
            return new ValNumEditorFactoryImpl();
        } else {
            return new ValBoolEditorFactoryImpl();
        }
    }
}
