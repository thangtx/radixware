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

import javax.swing.JPanel;
import org.radixware.kernel.designer.common.dialogs.choosetype.TypeWizard.Settings;
import org.radixware.kernel.designer.common.dialogs.components.state.StateManager;
import org.radixware.kernel.designer.common.dialogs.wizards.WizardSteps;


abstract class GeneralChooseTypeStep<PanelType extends JPanel> extends WizardSteps.Step<PanelType> {

    boolean isComplete = false;
    boolean hasNextStep = false;
    boolean isFinishable = false;
    StateManager state;
    TypeWizard.TypeModel typeModel;

    private TypeWizard.Settings settings;

    public GeneralChooseTypeStep() {
        state = new StateManager(getVisualPanel());
        state.ok();
    }

    @Override
    public boolean isComplete() {
        return isComplete;
    }

    @Override
    public boolean isFinishiable() {
        return isFinishable;
    }

    @Override
    public boolean hasNextStep() {
        return hasNextStep;
    }

    @Override
    public final void open(Object settings) {
        this.settings = (TypeWizard.Settings) settings;
        typeModel = this.settings.typeModel;

        open(this.settings);
        fireChange();
    }

    @Override
    public final void apply(Object settings) {
        TypeWizard.Settings typeWizardSettings = (TypeWizard.Settings) settings;
        apply(typeWizardSettings);
    }

    Settings getSettings() {
        return settings;
    }

    abstract void apply(TypeWizard.Settings settings);

    abstract void open(TypeWizard.Settings settings);
}