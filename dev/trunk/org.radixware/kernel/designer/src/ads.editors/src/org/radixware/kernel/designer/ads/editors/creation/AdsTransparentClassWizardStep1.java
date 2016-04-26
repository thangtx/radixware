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

package org.radixware.kernel.designer.ads.editors.creation;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.designer.common.dialogs.wizards.WizardSteps.Step;
import org.radixware.kernel.designer.common.dialogs.wizards.newobject.CreatureSetupStep;


public class AdsTransparentClassWizardStep1 extends CreatureSetupStep<AdsTransparentClassCreature, AdsTransparentClassWizardStep1Panel> {

    @Override
    public synchronized AdsTransparentClassWizardStep1Panel createVisualPanel() {
        return new AdsTransparentClassWizardStep1Panel();
    }

    @Override
    public void open(final AdsTransparentClassCreature creature) {
        getVisualPanel().addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                creature.setEnvironment(getVisualPanel().getEnvironment());
                fireChange();
            }
        });
        getVisualPanel().open(creature);
    }

    @Override
    public void apply(AdsTransparentClassCreature creature) {
        creature.setClassName(getVisualPanel().getClassName());
        creature.setPlatformClass(getVisualPanel().getPublishedClass());
        creature.setIsExtendable(getVisualPanel().isExtendable());
    }

    @Override
    public String getDisplayName() {
        return NbBundle.getMessage(AdsTransparentClassWizardStep1.class, "Creature-Step");
    }

    @Override
    public boolean isComplete() {
        return getVisualPanel().isComplete();
    }

    @Override
    public boolean isFinishiable() {
        return !getVisualPanel().isPublishMethods();
    }

    @Override
    public boolean hasNextStep() {
        return getVisualPanel().isPublishMethods();
    }

    @Override
    public Step createNextStep() {
        return new PublishingClassMembersStep();
    }

}
