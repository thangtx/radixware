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

package org.radixware.kernel.designer.common.dialogs.defs.module.creation;

import org.radixware.kernel.designer.common.dialogs.wizards.WizardSteps.Step;
import org.radixware.kernel.designer.common.dialogs.wizards.newobject.CreatureSetupStep;
import org.radixware.kernel.designer.common.dialogs.wizards.newobject.NamedRadixObjectCreature;

/**
 * Wizard step for module creature, allows to choose creation way: new or overwrite.
 */
public class ModuleCreatureWizardStep1 extends CreatureSetupStep<ModuleCreature, ModuleCreatureWizardStep1Panel> {

    final ModuleCreature creature;

    public ModuleCreatureWizardStep1(ModuleCreature creature) {
        this.creature = creature;
    }

    @Override
    public void open(ModuleCreature creature) {
        super.open(creature);
        getVisualPanel().open(this);
    }

    @Override
    public String getDisplayName() {
        return "Choose Module Creation Way";
    }

    @Override
    protected ModuleCreatureWizardStep1Panel createVisualPanel() {
        return new ModuleCreatureWizardStep1Panel();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Step createNextStep() {
        return new NamedRadixObjectCreature.SpecifyNameStep(creature.getSegment().getModules());
    }

    @Override
    public boolean hasNextStep() {
        return true;
    }

    @Override
    public boolean isComplete() {
        return getVisualPanel().isComplete();
    }

    @Override
    public boolean isFinishiable() {
        return false;
    }
}
