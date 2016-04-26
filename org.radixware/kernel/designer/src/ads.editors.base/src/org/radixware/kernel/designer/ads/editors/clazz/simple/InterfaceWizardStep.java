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

package org.radixware.kernel.designer.ads.editors.clazz.simple;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;
import org.radixware.kernel.designer.common.dialogs.components.CommonParametersEditorCellLib.TypeCellEditor;
import org.radixware.kernel.designer.common.dialogs.components.CommonParametersEditorCellLib.TypeCellRenderer;
import org.radixware.kernel.designer.common.dialogs.components.CommonParametersEditorCellLib.TypePresentation;


class InterfaceWizardStep extends SuperClassWizardStep {

    class InterfaceClassVisualPanel extends ClassWizardPanel {

        public InterfaceClassVisualPanel(AdsClassDef context) {
            super(context);
        }

        @Override
        protected void initConfig() {
            chooseDefinitionCfg = ChooseDefinitionCfg.Factory.newInstance(context, context.getInheritance().createSuperInterfaceLookupContext().getTypeSourceProvider(EValType.USER_CLASS));
        }

        @Override
        protected void onSelectedClassChanged() {
            if (resultClassObj != null) {
                final TypeArgumentsTableModel typeArgumentsTableModel = new TypeArgumentsTableModel(context, resultClassObj);
                table.setModel(typeArgumentsTableModel);
                table.setDefaultEditor(TypePresentation.class, new TypeCellEditor(null, false, false,typeArgumentsTableModel));
                table.setDefaultRenderer(TypePresentation.class, new TypeCellRenderer());
                table.setEnabled(isSpecifySuperClassArgs);
            }
        }
    }

    public InterfaceWizardStep(AdsClassDef context) {
        super(context);
    }

    @Override
    protected ClassWizardPanel createVisualPanel() {
        ClassWizardPanel panel = new InterfaceClassVisualPanel(context);
        panel.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                InterfaceWizardStep.super.fireChange();
            }
        });
        return panel;
    }

    @Override
    public String getDisplayName() {
        return "Choose Interface Class";
    }
}
