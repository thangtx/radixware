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

import javax.swing.JPanel;
import org.radixware.kernel.designer.common.dialogs.wizards.WizardSteps;
import org.radixware.kernel.designer.common.general.creation.ICreatureGroup.ICreature;


public abstract class CreatureSetupStep<C extends ICreature,T extends JPanel> extends WizardSteps.Step<T> {

    @Override
    @Deprecated
    public final void open(Object settings) {
        super.open(settings);
        this.open((C)((CreationWizardSettings) settings).getCreature());
    }

    public void open(C creature) {
    }
    @Override
    @Deprecated
    public void apply(Object settings){
        super.apply(settings);
        this.apply((C)((CreationWizardSettings) settings).getCreature());
    }
    public void apply(C creature){

    }
}