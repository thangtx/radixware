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

import java.util.*;
import org.openide.nodes.Node;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsPropertyGroup;
import org.radixware.kernel.designer.ads.editors.clazz.creation.MemberGroupCreature;
import org.radixware.kernel.designer.ads.editors.clazz.creation.PropertyCreature;
import org.radixware.kernel.designer.common.general.creation.ICreatureGroup;
import org.radixware.kernel.designer.common.general.nodes.NodesManager;
import org.radixware.kernel.common.utils.agents.IObjectAgent;

public class ClassPropertyGroupsProvider extends ClassMemberProvider<AdsPropertyGroup> {

    @Override
    public Node findOrCreateNode(Key key) {
        if (key instanceof IdKey) {
            AdsPropertyGroup childGroup = getSource().getChildGroups().findById(((IdKey) key).getId());
            if (childGroup != null) {
                return NodesManager.findOrCreateNode(childGroup);
            }
        }
        return null;
    }

    @Override
    public Collection<Key> updateKeys() {
        if (getSource() != null) {
            List<AdsPropertyGroup> groups_ = getSource().getChildGroups().list();
            Collections.sort(groups_, nameComparator);

            final ArrayList<Key> ids = new ArrayList<>(groups_.size());

            for (AdsPropertyGroup g : groups_) {
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
                    List<ICreature> result = PropertyCreature.Factory.createInstances(getAgent());
                    result.add(0, MemberGroupCreature.Factory.newPropertyGroup(getAgent()));
                    return result;
                }

                @Override
                public String getDisplayName() {
                    return "Properties and Property Groups";
                }
            }};
    }

    @Override
    protected boolean updateListeners() {
        getSource().getChildGroups().getContainerChangesSupport().addEventListener(containerListener);
        return true;
    }

    @Override
    protected IObjectAgent<AdsPropertyGroup> createAgent(AdsDefinition context) {
        return createPropertyGroupAgent(context);
    }
}
