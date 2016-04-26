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

package org.radixware.kernel.designer.ads.editors.clazz.creation;

import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.clazz.AdsMethodGroup;
import org.radixware.kernel.common.defs.ads.clazz.AdsPropertyGroup;
import org.radixware.kernel.common.defs.ads.clazz.MembersGroup;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.designer.common.dialogs.wizards.newobject.Creature;
import org.radixware.kernel.designer.common.dialogs.wizards.newobject.CreatureSetupStep;
import org.radixware.kernel.common.utils.agents.IObjectAgent;
import org.radixware.kernel.common.utils.agents.WrapAgent;


public class MemberGroupCreature extends Creature<MembersGroup> {

    public static final class Factory {

        public static MemberGroupCreature newPropertyGroup(final IObjectAgent<? extends AdsPropertyGroup> agent) {
            return new MemberGroupCreature(new WrapAgent<RadixObjects, MembersGroup>(agent) {

                @Override
                public RadixObjects getObject() {
                    return ((AdsPropertyGroup) agent.getObject()).getChildGroups();
                }
            }, agent, true, "newPropGroup");
        }

        public static MemberGroupCreature newMethodGroup(final IObjectAgent<? extends AdsMethodGroup> agent) {
            return new MemberGroupCreature(new WrapAgent<RadixObjects, MembersGroup>(agent) {

                @Override
                public RadixObjects getObject() {
                    return ((AdsMethodGroup) agent.getObject()).getChildGroups();
                }
            }, agent, false, "newMethodGroup");
        }
    }

    private boolean propGroup;
    private final IObjectAgent<? extends MembersGroup> membersAgent;

    private MemberGroupCreature(final IObjectAgent<? extends RadixObjects> agent,
        final IObjectAgent<? extends MembersGroup> membersAgent, final boolean propGroup, String groupName) {

        super(agent);

        this.propGroup = propGroup;
        this.membersAgent = membersAgent;
        this.groupName = groupName;
    }

    private MembersGroup getGroup() {
        return membersAgent.getObject();
    }

    @Override
    @SuppressWarnings("unchecked")
    public WizardInfo getWizardInfo() {
        return new Creature.WizardInfo() {

            @Override
            public CreatureSetupStep createFirstStep() {
                return new MemberGroupSetupStep1();
            }

            @Override
            public boolean hasWizard() {
                return true;
            }
        };
    }

    @Override
    public String getDescription() {
        return propGroup ? NbBundle.getMessage(MemberGroupCreature.class, "Type-Description-PropGroup") : NbBundle.getMessage(MemberGroupCreature.class, "Type-Description-MethodGroup");
    }

    @Override
    public String getDisplayName() {
        return propGroup ? NbBundle.getMessage(MemberGroupCreature.class, "Type-Display-Name-PropGroup") : NbBundle.getMessage(MemberGroupCreature.class, "Type-Display-Name-MethodGroup");
    }

    @Override
    public boolean isEnabled() {
        return !getGroup().isReadOnly();
    }

    @Override
    public MembersGroup createInstance() {
        return propGroup ? AdsPropertyGroup.Factory.newInstance("newPropertyGroup") : AdsMethodGroup.Factory.newInstance("newMethodGroup");
    }

    @Override
    public boolean afterCreate(MembersGroup object) {
        if (groupName != null) {
            object.setName(groupName);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void afterAppend(MembersGroup object) {
        //
    }
    private String groupName;

    public void setGroupName(String groupname) {
        this.groupName = groupname;
    }

    public String getGroupName() {
        return this.groupName;
    }

    @Override
    public RadixIcon getIcon() {
        return propGroup ? AdsDefinitionIcon.PROPERTY_GROUP : AdsDefinitionIcon.METHOD_GROUP;
    }
}
