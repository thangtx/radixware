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

import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.ads.platform.PlatformLib;
import org.radixware.kernel.common.defs.ads.platform.RadixPlatformClass;
import org.radixware.kernel.common.repository.ads.AdsSegment;
import org.radixware.kernel.designer.ads.editors.clazz.members.transparent.PublishingClassMembersPanel;
import org.radixware.kernel.designer.ads.editors.creation.UnpublishedMethodsPanel;
import org.radixware.kernel.designer.common.dialogs.wizards.WizardSteps.Step;
import org.radixware.kernel.designer.common.dialogs.wizards.newobject.CreatureSetupStep;


@Deprecated
public class AdsTransparentClassWizardStep2 extends CreatureSetupStep<AdsTransparentClassCreature, UnpublishedMethodsPanel> {

    @Override
    protected UnpublishedMethodsPanel createVisualPanel() {
        return new UnpublishedMethodsPanel();
    }

    @Override
    public void open(final AdsTransparentClassCreature creature) {
        String classname = creature.getPlatformClass();
        PlatformLib lib = ((AdsSegment)creature.getAdsModule().getSegment()).getBuildPath().getPlatformLibs().getKernelLib(creature.getEnvironment());
        RadixPlatformClass cl = lib.findPlatformClass(classname);
        getVisualPanel().open(cl, creature.getAdsModule());
    }

    @Override
    public void apply(AdsTransparentClassCreature creature) {
//        creature.setPublishedMethods(getVisualPanel().getPublishedMethods());
    }

    @Override
    public String getDisplayName() {
        return NbBundle.getMessage(AdsTransparentClassWizardStep2.class, "Creatuer-Step2");
    }

    @Override
    public boolean isComplete() {
        return true;
    }

    @Override
    public boolean isFinishiable() {
        return false;
    }

    @Override
    public boolean hasNextStep() {
        return true;
    }

    @Override
    public Step<PublishingClassMembersPanel> createNextStep() {
        return new PublishingClassMembersStep();
    }
}
