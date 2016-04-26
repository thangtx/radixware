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
import java.util.LinkedList;
import org.openide.nodes.Node;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsMethodGroup;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.utils.agents.IObjectAgent;
import org.radixware.kernel.designer.common.general.nodes.NodesManager;
import org.radixware.kernel.designer.tree.ads.nodes.defs.MixedNodeChildrenProvider.IdKey;
import org.radixware.kernel.designer.tree.ads.nodes.defs.MixedNodeChildrenProvider.Key;

public class ClassMethodsProvider extends ClassMemberProvider<AdsMethodGroup> {

    @Override
    public Node findOrCreateNode(Key key) {
        if (key instanceof IdKey) {
            AdsMethodDef childMethod = getSource().getOwnerClass().getMethods().findById(((IdKey) key).getId(), EScope.LOCAL).get();
            if (childMethod != null) {
                return NodesManager.findOrCreateNode(childMethod);
            }
        }

        return null;
    }

    @Override
    public Collection<Key> updateKeys() {
        if (getSource() != null) {
            final LinkedList<AdsMethodDef> constructors = new LinkedList<>();
            final LinkedList<AdsMethodDef> regularMethods = new LinkedList<>();
            Collection<AdsMethodDef> methods = getSource().listMembers();
            for (AdsMethodDef m : methods) {
                if (m.isConstructor()) {
                    constructors.add(m);
                } else {
                    regularMethods.add(m);
                }
            }

            Collections.sort(constructors, nameComparator);
            Collections.sort(regularMethods, nameComparator);

            final ArrayList<Key> ids = new ArrayList<>(constructors.size() + regularMethods.size());

            for (AdsMethodDef g : constructors) {
                ids.add(Key.Factory.forId(g.getId()));
            }
            for (AdsMethodDef g : regularMethods) {
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
        getSource().getOwnerClass().getMethods().getLocal().getContainerChangesSupport().addEventListener(containerListener);
        return true;
    }

    @Override
    protected IObjectAgent<AdsMethodGroup> createAgent(AdsDefinition context) {
        return createMethodGroupAgent(context);
    }
}
