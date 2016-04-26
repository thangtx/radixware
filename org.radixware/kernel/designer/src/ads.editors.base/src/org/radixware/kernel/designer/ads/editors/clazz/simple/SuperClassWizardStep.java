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
import org.radixware.kernel.common.defs.ads.clazz.Inheritance.SuperClassLookupContext;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsApplicationClassDef;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;
import org.radixware.kernel.designer.common.dialogs.wizards.WizardSteps;
import org.radixware.kernel.designer.common.dialogs.components.CommonParametersEditorCellLib.TypeCellEditor;
import org.radixware.kernel.designer.common.dialogs.components.CommonParametersEditorCellLib.TypeCellRenderer;
import org.radixware.kernel.designer.common.dialogs.components.CommonParametersEditorCellLib.TypePresentation;


class SuperClassWizardStep extends WizardSteps.Step<ClassWizardPanel> {

    class SuperClassVisualPanel extends ClassWizardPanel {

        private boolean isAdsApplicationClass;

        public SuperClassVisualPanel(AdsClassDef context) {
            super(context);
            isAdsApplicationClass = context instanceof AdsApplicationClassDef;
        }

        @Override
        protected void initConfig() {
            chooseDefinitionCfg = ChooseDefinitionCfg.Factory.newInstance(context, context.getInheritance().createSuperClassLookupContext().getTypeSourceProvider(EValType.USER_CLASS));
        }

        @Override
        protected void onSelectedClassChanged() {
            //additional condition:
            //having AdsApplicationClassDef instance, it is forbidden to set its super class to null
            
            if (!(isAdsApplicationClass && resultClassObj == null)) {
                final TypeArgumentsTableModel typeArgumentsTableModel = new TypeArgumentsTableModel(context, resultClassObj);
                table.setModel(typeArgumentsTableModel);
                table.setDefaultEditor(TypePresentation.class, new TypeCellEditor(null, false, false,typeArgumentsTableModel));
                table.setDefaultRenderer(TypePresentation.class, new TypeCellRenderer());
                table.setEnabled(isSpecifySuperClassArgs);
            }
        }
    }
    protected AdsClassDef context;

    public SuperClassWizardStep(AdsClassDef context) {
        this.context = context;
    }

    @Override
    protected ClassWizardPanel createVisualPanel() {
        final ClassWizardPanel panel = new SuperClassVisualPanel(context);
        panel.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                SuperClassWizardStep.this.fireChange();
            }
        });

        return panel;
    }

    @Override
    public String getDisplayName() {
        return "Choose Super Class";
    }

    @Override
    public void open(Object settings) {
        fireChange();
    }

    @Override
    public void apply(Object settings) {
        final SuperClassWizardSettings superClassWizardSettings = (SuperClassWizardSettings) settings;
        superClassWizardSettings.setResultClass(getVisualPanel().getResultClassObj());
        superClassWizardSettings.setResultClassDeclaration(getVisualPanel().getResultAdsTypeDeclaration());
    }

    @Override
    public boolean isComplete() {
        return getVisualPanel().isComplete();
    }

    @Override
    public boolean isFinishiable() {
        return true;
    }

    @Override
    public boolean hasNextStep() {
        return false;
    }
}
