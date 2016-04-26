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
import org.radixware.kernel.designer.common.general.creation.ICreatureGroup;
import org.radixware.kernel.designer.common.general.creation.ICreatureGroup.ICreature;


class CreationWizardSteps extends WizardSteps {

    private ICreature initialCreature;
    private ICreatureGroup[] creatureGroups = null;

    CreationWizardSteps(ICreatureGroup[] creatureGroups, ICreature initialCreature) {
        this.initialCreature = initialCreature;
        this.creatureGroups = creatureGroups == null ? new ICreatureGroup[0] : creatureGroups;
        if (this.initialCreature != null) {
            moveForward();
        }
    }

    public ICreature<?> getCreature() {
        return ((CreationWizardSettings) getSettings()).getCreature();
    }

    @Override
    public Step createInitial() {
        return new CreationWizardStartStep(creatureGroups);
    }

    @Override
    public Object createSettings() {
        return new CreationWizardSettings(initialCreature);
    }

    @Override
    public String getDisplayName() {
        CreationWizardSettings settings = (CreationWizardSettings) getSettings();
        ICreature creature = settings.getCreature();

        if (creature != null) {
            return creature.getDisplayName();
        } else {
            return "Create New Object";
        }
    }
}
