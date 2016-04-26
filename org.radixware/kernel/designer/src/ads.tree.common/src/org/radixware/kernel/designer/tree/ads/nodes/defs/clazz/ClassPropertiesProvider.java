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
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.designer.common.general.nodes.NodesManager;
import org.radixware.kernel.common.utils.agents.IObjectAgent;

public class ClassPropertiesProvider extends ClassMemberProvider<AdsPropertyGroup> {

    @Override
    public Node findOrCreateNode(Key key) {
        if (key instanceof IdKey) {
            AdsPropertyDef prop = getSource().getOwnerClass().getProperties().getLocal().findById(((IdKey) key).getId());
            if (prop != null) {
                return NodesManager.findOrCreateNode(prop);
            }
        }
        return null;
    }

    @Override
    public Collection<Key> updateKeys() {
        if (getSource().getOwnerClass() != null) {
            List<AdsPropertyDef> props = getSource().listMembers();
            Collections.sort(props, nameComparator);

            final ArrayList<Key> ids = new ArrayList<>(props.size());

            for (AdsPropertyDef g : props) {
                ids.add(Key.Factory.forId(g.getId()));
            }
            return ids;
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    protected boolean updateListeners() {
        getSource().getMembershipChangesSupport().addEventListener(membershipListener);
        getSource().getOwnerClass().getProperties().getLocal().getContainerChangesSupport().addEventListener(containerListener);
        return true;
    }

    @Override
    protected IObjectAgent<AdsPropertyGroup> createAgent(AdsDefinition context) {
        return createPropertyGroupAgent(context);
    }
}
