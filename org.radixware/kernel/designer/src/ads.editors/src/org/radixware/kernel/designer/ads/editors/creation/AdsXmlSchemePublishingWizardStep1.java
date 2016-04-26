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
import org.radixware.kernel.designer.common.dialogs.wizards.newobject.CreatureSetupStep;


public class AdsXmlSchemePublishingWizardStep1 extends CreatureSetupStep<AdsXmlSchemePublishingCreature, AdsXmlSchemePublishingWizardStep1Panel>{

    @Override
    public void open(AdsXmlSchemePublishingCreature creature) {
        getVisualPanel().open(creature);
    }

    @Override
    public void apply(AdsXmlSchemePublishingCreature creature) {
        creature.setPublishingUrl(getVisualPanel().getPublishingUrl());
        creature.setTargetEnvironment(getVisualPanel().getTargetEnvironment());
    }

    @Override
    public String getDisplayName() {
        return NbBundle.getMessage(AdsXmlSchemePublishingWizardStep1.class, "XmlPublishingWizard-Step1");
    }

    @Override
    protected AdsXmlSchemePublishingWizardStep1Panel createVisualPanel() {
        AdsXmlSchemePublishingWizardStep1Panel panel = new AdsXmlSchemePublishingWizardStep1Panel();
        panel.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                fireChange();
            }
        });
        return panel;
    }

    @Override
    public boolean isComplete() {
        return getVisualPanel().isComplete();
    }

    @Override
    public boolean isFinishiable() {
        return getVisualPanel().isComplete();
    }

}
