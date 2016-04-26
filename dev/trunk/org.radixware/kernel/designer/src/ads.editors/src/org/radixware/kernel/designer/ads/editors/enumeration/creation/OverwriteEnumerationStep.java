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

package org.radixware.kernel.designer.ads.editors.enumeration.creation;


import javax.swing.event.ChangeEvent;

import javax.swing.event.ChangeListener;
import org.openide.util.NbBundle;
import org.radixware.kernel.designer.common.dialogs.wizards.newobject.CreatureSetupStep;


public class OverwriteEnumerationStep extends CreatureSetupStep<AdsEnumCreature, OverwrittenEnumChooser> {

    public OverwriteEnumerationStep() {
        super();
    }

    @Override
    public synchronized OverwrittenEnumChooser createVisualPanel() {
        OverwrittenEnumChooser chooser = new OverwrittenEnumChooser();
        chooser.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                OverwriteEnumerationStep.this.fireChange();
            }

        });
        return chooser;
    }

    @Override
    public String getDisplayName() {
        return NbBundle.getMessage(OverwriteEnumerationStep.class, "Enum-OverwriteStepTitle");
    }

    @Override
    public void open(final AdsEnumCreature creature) {
        getVisualPanel().open(creature.getModule());
    }

    @Override
    public void apply(AdsEnumCreature creature) {
        creature.setName(getVisualPanel().getEnumName());
        creature.setOverridenDefinition(getVisualPanel().getOverwrittenEnum()); 
    }

    @Override
    public boolean isComplete() {
        return getVisualPanel().isComplete();
    }

    @Override
    public boolean isFinishiable() {
        return true;
    }
}