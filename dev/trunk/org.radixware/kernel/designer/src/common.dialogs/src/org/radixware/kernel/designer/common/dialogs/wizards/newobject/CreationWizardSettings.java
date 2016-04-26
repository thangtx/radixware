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

import org.radixware.kernel.designer.common.general.creation.ICreatureGroup.ICreature;


class CreationWizardSettings {

    CreationWizardSettings(ICreature initialCreature) {
        if(this.creature == null)
            this.creature = initialCreature;
    }

    protected ICreature creature;

    /**
     * Get the value of creature
     *
     * @return the value of creature
     */
    public ICreature<?> getCreature() {
        return creature;
    }

    /**
     * Set the value of creature
     *
     * @param creature new value of creature
     */
    public void setCreature(ICreature creature) {
        this.creature = creature;
    }
}
