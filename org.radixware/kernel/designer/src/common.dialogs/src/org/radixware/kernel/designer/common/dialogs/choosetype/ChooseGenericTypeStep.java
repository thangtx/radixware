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

package org.radixware.kernel.designer.common.dialogs.choosetype;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;


public class ChooseGenericTypeStep extends GeneralChooseTypeStep<TypeParamsListPanel> implements ChangeListener {

    private String currentValue;

    @Override
    public String getDisplayName() {
        return NbBundle.getMessage(ChooseGenericTypeStep.class, "TypeWizard-ChooseGenericTypeStep-DisplayName");
    }

    @Override
    protected TypeParamsListPanel createVisualPanel() {
        TypeParamsListPanel typeParamsListPanel = new TypeParamsListPanel();
        typeParamsListPanel.addChangeListener(this);

        return typeParamsListPanel;
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        currentValue = getVisualPanel().getCurrentParameter();
        if (currentValue != null && !currentValue.isEmpty()) {
            isComplete = true;
            state.ok();
        } else {
            isComplete = false;
        }
        fireChange();
    }

    @Override
    void open(TypeWizard.Settings settings) {

        Definition definition = settings.filter.getContext();

        if (definition instanceof AdsClassDef) {
            getVisualPanel().open((AdsClassDef) definition);
        } else if (definition.getOwnerDefinition() instanceof AdsClassDef) {
            getVisualPanel().open((AdsClassDef) definition.getOwnerDefinition());
        } else if (definition instanceof AdsPropertyDef) {
            AdsClassDef classOwner = ((AdsPropertyDef) definition).getOwnerClass();
            if (classOwner != null) {
                getVisualPanel().open(classOwner);
            }
        }
    }

    @Override
    void apply(TypeWizard.Settings settings) {
        if (currentValue != null && !currentValue.isEmpty()) {
            typeModel.setType(AdsTypeDeclaration.Factory.newTypeParam(currentValue));
        }
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
