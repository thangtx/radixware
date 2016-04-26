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
import org.radixware.kernel.designer.common.dialogs.choosetype.TypeWizard.Settings;
import org.radixware.kernel.designer.common.dialogs.wizards.WizardSteps;
import org.radixware.kernel.designer.common.dialogs.wizards.WizardSteps.Step;


class ChooseTypeNatureStep extends WizardSteps.Step<ChooseTypeNaturePanel> implements ChangeListener {

    @Override
    public void open(Object settings) {
        Settings s = (Settings) settings;

        getVisualPanel().enableJavaPrimitives(s.filter.acceptNature(ETypeNature.JAVA_PRIMITIVE));
        getVisualPanel().enableJavaClass(s.filter.acceptNature(ETypeNature.JAVA_CLASS));
        getVisualPanel().enableArrayButton(s.filter.acceptNature(ETypeNature.JAVA_CLASS) && s.filter.acceptNature(ETypeNature.JAVA_ARRAY));
        getVisualPanel().enableTypeParameter(s.filter.acceptNature(ETypeNature.TYPE_PARAMETER));

        boolean arrayChosen = getVisualPanel().getSelectedTypeNature() == ETypeNature.JAVA_ARRAY;
        getVisualPanel().enableDimEditing(arrayChosen);

        s.typeModel.reset();

        fireChange();
    }

    @Override
    public void apply(Object settings) {
        Settings s = (Settings) settings;

        ETypeNature nature = getVisualPanel().getSelectedTypeNature();
        s.typeModel.setNature(nature);
        if (nature == ETypeNature.JAVA_ARRAY) {
            s.typeModel.setDimension((int) getVisualPanel().getArrayDimension());
            s.typeModel.switchToSubtype();
        }
    }

    @Override
    protected ChooseTypeNaturePanel createVisualPanel() {
        ChooseTypeNaturePanel panel = new ChooseTypeNaturePanel();
        panel.addChangeListener(this);
        return panel;
    }

    WizardSteps.Step nextStep = new ChooseRadixTypeStep();

    @Override
    public void stateChanged(ChangeEvent e) {

        ETypeNature nature = getVisualPanel().getSelectedTypeNature();

        if (nature == ETypeNature.JAVA_ARRAY) {
            nextStep = new ChooseArrayTypeNatureStep();
        } else if (nature == ETypeNature.JAVA_PRIMITIVE) {
            nextStep = new ChooseJavaTypeStep();
        } else if (nature == ETypeNature.JAVA_CLASS) {
            nextStep = new ChooseJavaClassStep();
        } else if (nature == ETypeNature.TYPE_PARAMETER) {
            nextStep = new ChooseGenericTypeStep();
        } else {
            nextStep = new ChooseRadixTypeStep();
        }

        fireChange();
    }

    @Override
    public Step createNextStep() {
        return nextStep;
    }

    @Override
    public boolean hasNextStep() {
        return true;
    }

    @Override
    public boolean isComplete() {
        return true;
    }

    @Override
    public String getDisplayName() {
        return NbBundle.getMessage(ChooseTypeNatureStep.class, "TypeWizard-ChooseTypeNatureStep-DisplayName");
    }
}
