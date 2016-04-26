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
import org.radixware.kernel.designer.common.dialogs.wizards.WizardSteps.Step;
import org.radixware.kernel.designer.common.dialogs.wizards.newobject.CreatureSetupStep;


public class NewAdsEnumDefinitionStep extends CreatureSetupStep<AdsEnumCreature, NewAdsEnumDefinitionPanel> {

    private AdsEnumCreature.E_CREATION_TYPE choice;

    public NewAdsEnumDefinitionStep() {
        super();
        choice = AdsEnumCreature.E_CREATION_TYPE.SIMPLE_ENUM_CREATION;
    }

    @Override
    public synchronized NewAdsEnumDefinitionPanel createVisualPanel() {
        final NewAdsEnumDefinitionPanel panel = new NewAdsEnumDefinitionPanel();
        panel.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                choice = panel.getCreationType();
                NewAdsEnumDefinitionStep.this.fireChange();
            }

        });
        return panel;
    }

    @Override
    public void open(final AdsEnumCreature creature) {
        getVisualPanel().open(creature);
    }

    @Override
    public String getDisplayName() {
        return NbBundle.getMessage(NewAdsEnumDefinitionStep.class, "Enum-FirstStepTitle");
    }

    @Override
    public boolean isComplete() {
        return getVisualPanel().isComplete();
    }

    @Override
    public Step createNextStep() {

        if (choice == AdsEnumCreature.E_CREATION_TYPE.SIMPLE_ENUM_CREATION) {
            return new SimpleTypesInitializationStep();
        } else if (choice == AdsEnumCreature.E_CREATION_TYPE.OVERRIDEN_ADS_ENUM_CREATION) {
            return new OverwriteEnumerationStep();
        } else if (choice == AdsEnumCreature.E_CREATION_TYPE.PLATFORM_PUBLICATED_ENUM_CREATION) {
            return new PlatformPublicatedEnumDefinitionStep();
        }

        assert(false);
        return null;
    }

    @Override
    public boolean isFinishiable() {
        return false;
    }

    @Override
    public boolean hasNextStep() {
        return true;
    }
}
