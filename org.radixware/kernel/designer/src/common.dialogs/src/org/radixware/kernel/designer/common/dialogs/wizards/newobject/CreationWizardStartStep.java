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

package org.radixware.kernel.designer.common.dialogs.wizards.newobject;

import org.radixware.kernel.designer.common.dialogs.wizards.WizardSteps;
import org.radixware.kernel.designer.common.dialogs.wizards.WizardSteps.Step;
import org.radixware.kernel.designer.common.general.creation.ICreatureGroup;
import org.radixware.kernel.designer.common.general.creation.ICreatureGroup.ICreature;


class CreationWizardStartStep extends WizardSteps.Step<CreationWizardStartStepPanel> {

    final ICreatureGroup[] categories;

    CreationWizardStartStep(ICreatureGroup[] categories) {
        this.categories = categories;
    }

    @Override
    public String getDisplayName() {
        return "New Object";
    }

    @Override
    protected CreationWizardStartStepPanel createVisualPanel() {
        return new CreationWizardStartStepPanel(this);
    }
    private CreationWizardSettings settings;

    @Override
    public void open(Object settings) {
        super.open(settings);
        this.settings = (CreationWizardSettings) settings;
        this.getVisualPanel().open(this);
    }

    @Override
    public Step createNextStep() {
        ICreature c = this.settings.getCreature();
        if (c != null) {
            Object obj = c.getWizardInfo().createFirstStep();

            if (obj instanceof CreatureSetupStep) {
                return (CreatureSetupStep) obj;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public boolean hasNextStep() {
        ICreature c = this.settings.getCreature();

        if (c != null) {
            ICreature.WizardInfo info = c.getWizardInfo();
            return info != null && info.hasWizard();
        }

        return super.hasNextStep();
    }

    @Override
    public boolean isComplete() {
        ICreature c = this.settings.getCreature();
        return c != null;
    }

    ICreature getCurrent() {
        return this.settings.getCreature();
    }

    void setCurrent(ICreature c) {
        this.settings.setCreature(c);
        fireChange();
    }
}
