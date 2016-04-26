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

package org.radixware.kernel.designer.tree.ads.nodes.defs.clazz;

import java.util.Comparator;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.*;
import org.radixware.kernel.common.utils.agents.DefaultAgent;
import org.radixware.kernel.common.utils.agents.IObjectAgent;
import org.radixware.kernel.common.utils.agents.WrapAgent;
import org.radixware.kernel.designer.common.general.creation.ICreatureGroup;
import org.radixware.kernel.designer.common.general.utils.RadixMutex;
import org.radixware.kernel.designer.tree.ads.nodes.defs.MixedNodeChildrenAdapter;
import org.radixware.kernel.designer.tree.ads.nodes.defs.MixedNodeChildrenProvider;


abstract class ClassMemberProvider<OwnerType> extends MixedNodeChildrenProvider<AdsDefinition> {

    static IObjectAgent<AdsMethodGroup> createMethodGroupAgent(AdsDefinition context) {
        if (context instanceof AdsMethodGroup) {
            return new DefaultAgent<>((AdsMethodGroup) context);
        }

        if (context instanceof AdsClassDef) {
            return new DefaultAgent<>(((AdsClassDef) context).getMethodGroup());
        }

        if (context instanceof IClassInclusive) {
            final IClassInclusive classInclusive = (IClassInclusive) context;
            return new WrapAgent<AdsMethodGroup, AdsEmbeddedClassDef>(classInclusive.getEmbeddedClassAgent()) {

                @Override
                public AdsMethodGroup getObject() {
                    return getObjectSource().getMethodGroup();
                }
            };
        }

        return null;
    }

    static IObjectAgent<AdsPropertyGroup> createPropertyGroupAgent(AdsDefinition context) {
        if (context instanceof AdsPropertyGroup) {
            return new DefaultAgent<>((AdsPropertyGroup) context);
        }

        if (context instanceof AdsClassDef) {
            return new DefaultAgent<>(((AdsClassDef) context).getPropertyGroup());
        }

        if (context instanceof IClassInclusive) {
            final IClassInclusive classInclusive = (IClassInclusive) context;
            return new WrapAgent<AdsPropertyGroup, AdsEmbeddedClassDef>(classInclusive.getEmbeddedClassAgent()) {

                @Override
                public AdsPropertyGroup getObject() {
                    return getObjectSource().getPropertyGroup();
                }
            };
        }

        return null;
    }
    public static final Comparator<AdsDefinition> nameComparator = new Comparator<AdsDefinition>() {

        @Override
        public int compare(AdsDefinition o1, AdsDefinition o2) {
            return o1.getName().compareTo(o2.getName());
        }
    };
    protected final RadixObjects.ContainerChangesListener containerListener = new RadixObjects.ContainerChangesListener() {

        @Override
        public void onEvent(RadixObjects.ContainerChangedEvent e) {
            updateMembers();
        }
    };
    private final ChangeListener changeListener = new ChangeListener() {

        @Override
        public void stateChanged(ChangeEvent e) {
            RadixMutex.readAccess(new Runnable() {

                @Override
                public void run() {
                    updateListeners();
                    updateMembers();
                }
            });
        }
    };
    protected final MembersGroup.MembershipChangesListener membershipListener = new MembersGroup.MembershipChangesListener() {

        @Override
        public void onEvent(MembersGroup.MembershipChangedEvent e) {
            updateMembers();
        }
    };
    private MixedNodeChildrenAdapter<AdsDefinition> adapter;
    private IObjectAgent<OwnerType> agent;

    @Override
    public final boolean enterContext(MixedNodeChildrenAdapter<AdsDefinition> adapter, AdsDefinition context) {
        this.adapter = adapter;

        agent = createAgent(context);
        agent.removeActualizeListener(changeListener);
        agent.addActualizeListener(changeListener);

        if (agent.isActual()) {
            updateListeners();
        }

        return true;
    }

    @Override
    protected boolean isSorted() {
        return true;
    }

    @Override
    public ICreatureGroup[] createCreatureGroups() {
        return null;
    }

    protected final void updateMembers() {
        adapter.refresh(ClassMemberProvider.this);
    }

    public IObjectAgent<OwnerType> getAgent() {
        return agent;
    }

    protected final OwnerType getSource() {
        return getAgent().getObject();
    }

    protected abstract boolean updateListeners();

    protected abstract IObjectAgent<OwnerType> createAgent(AdsDefinition context);
}
