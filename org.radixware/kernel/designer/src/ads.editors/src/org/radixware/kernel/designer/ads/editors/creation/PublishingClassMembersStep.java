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

import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.defs.ads.platform.PlatformLib;
import org.radixware.kernel.common.defs.ads.platform.RadixPlatformClass;
import org.radixware.kernel.common.repository.ads.AdsSegment;
import org.radixware.kernel.designer.ads.editors.clazz.members.transparent.ClassMemberItem;
import org.radixware.kernel.designer.ads.editors.clazz.members.transparent.PublishingClassMembersPanel;
import org.radixware.kernel.designer.common.dialogs.wizards.newobject.CreatureSetupStep;


public class PublishingClassMembersStep extends CreatureSetupStep<AdsTransparentClassCreature, PublishingClassMembersPanel> {

    @Override
    public void open(AdsTransparentClassCreature creature) {

        String classname = creature.getPlatformClass();
        PlatformLib lib = ((AdsSegment) creature.getAdsModule().getSegment()).getBuildPath().getPlatformLibs().getKernelLib(creature.getEnvironment());
        RadixPlatformClass cl = lib.findPlatformClass(classname);
        getVisualPanel().open(cl, creature.getAdsModule());
    }

    @Override
    public String getDisplayName() {
        return "Wrap class members";
    }

    @Override
    protected PublishingClassMembersPanel createVisualPanel() {
        return new PublishingClassMembersPanel();
    }

    @Override
    public boolean isComplete() {
        return true;
    }

    @Override
    public boolean isFinishiable() {
        return true;
    }

    @Override
    public void apply(AdsTransparentClassCreature creature) {
        List<ClassMemberItem> selectedMembers = getVisualPanel().getSelectedMembers();
        List<RadixPlatformClass.ClassMember> members = new ArrayList<RadixPlatformClass.ClassMember>();

        for (ClassMemberItem classMemberItem : selectedMembers) {
            members.add(classMemberItem.getSource());
        }
        creature.setPublishedMembers(members);
    }
}
