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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.openide.nodes.Node;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsMethodGroup;
import org.radixware.kernel.common.defs.ads.clazz.algo.AdsAlgoClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.AdsProcedureClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.AdsStatementClassDef;
import org.radixware.kernel.common.defs.ads.type.AdsTransparence;
import org.radixware.kernel.common.utils.agents.IObjectAgent;
import org.radixware.kernel.designer.ads.editors.clazz.creation.MemberGroupCreature;
import org.radixware.kernel.designer.ads.editors.clazz.members.transparent.TransparentMethodCreature;


import org.radixware.kernel.designer.ads.method.creation.MethodCreature;
import org.radixware.kernel.designer.common.general.creation.ICreatureGroup;
import org.radixware.kernel.designer.common.general.nodes.NodesManager;

public class ClassMethodGroupsProvider extends ClassMemberProvider<AdsMethodGroup> {

    @Override
    public Node findOrCreateNode(Key key) {
        if (key instanceof IdKey) {
            AdsMethodGroup childGroup = getSource().getChildGroups().findById(((IdKey) key).getId());
            if (childGroup != null) {
                return NodesManager.findOrCreateNode(childGroup);
            }
        }
        return null;
    }

    @Override
    public Collection<Key> updateKeys() {
        if (getSource() != null) {
            List<AdsMethodGroup> groups = getSource().getChildGroups().list();
            Collections.sort(groups, nameComparator);

            final ArrayList<Key> ids = new ArrayList<>(groups.size());

            for (AdsMethodGroup g : groups) {
                ids.add(Key.Factory.forId(g.getId()));
            }

            return ids;
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public ICreatureGroup[] createCreatureGroups() {

        return new ICreatureGroup[]{new ICreatureGroup() {
                @Override
                public List<ICreature> getCreatures() {
                    final ArrayList<ICreature> result = new ArrayList<>();
                    result.add(MemberGroupCreature.Factory.newMethodGroup(getAgent()));

                    //by ygalkina RADIX-1847
                    final AdsClassDef owner = getSource().getOwnerClass();
                    if (owner instanceof AdsAlgoClassDef) {
                        result.add(new MethodCreature(getAgent(), MethodCreature.EMethodType.ALGO_METHOD));
                        result.add(new MethodCreature(getAgent(), MethodCreature.EMethodType.ALGO_STROB));
                    } else if (owner != null) {
                        final AdsTransparence transparence = owner.getTransparence();
                        if (transparence != null && transparence.isTransparent() && !(owner instanceof AdsStatementClassDef) && !(owner instanceof AdsProcedureClassDef)) {
                            result.add(new TransparentMethodCreature(owner, getSource(), owner.getMethods().getLocal()));
                        }

                        if (transparence == null || !transparence.isTransparent() || transparence.isExtendable()) {
                            result.add(new MethodCreature(getAgent(), MethodCreature.EMethodType.USER_DEF));
                        }
                    }
                    return result;
                }

                @Override
                public String getDisplayName() {
                    return "Methods And Method Groups";
                }
            }};
    }

    @Override
    protected boolean updateListeners() {
        getSource().getChildGroups().getContainerChangesSupport().addEventListener(containerListener);
        return true;
    }

    @Override
    protected IObjectAgent<AdsMethodGroup> createAgent(AdsDefinition context) {
        return createMethodGroupAgent(context);
    }
}
