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

import org.radixware.kernel.designer.tree.ads.nodes.defs.MixedNodeChildrenAdapter;
import org.radixware.kernel.designer.tree.ads.nodes.defs.MixedNodeChildrenProvider;
import java.util.ArrayList;
import java.util.Collection;
import org.openide.nodes.Node;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.RadixObjects.ContainerChangedEvent;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.*;
import org.radixware.kernel.designer.common.general.creation.ICreatureGroup;
import org.radixware.kernel.designer.common.general.nodes.NodesManager;


public class ClassPageProvider extends MixedNodeChildrenProvider<AdsPage> {

    private AdsPage page;
    @SuppressWarnings({"rawtypes", "unchecked"})
    private MixedNodeChildrenAdapter adapter;
    private final RadixObjects.ContainerChangesListener changeListener = new RadixObjects.ContainerChangesListener() {
        @Override
        public void onEvent(ContainerChangedEvent e) {
            if (page != null && adapter != null) {
                adapter.refresh(ClassPageProvider.this);
            }
        }
    };

    @Override
    public Node findOrCreateNode(Key key) {
        for (AdsBaseObject node : page.getNodes()) {
            if (!key.equals(Key.Factory.forId(node.getId()))) {
                continue;
            }
            if (node instanceof AdsScopeObject) {
                return NodesManager.findOrCreateNode(((AdsScopeObject) node).getPage());
            }
            if (node instanceof AdsCatchObject) {
                return NodesManager.findOrCreateNode(((AdsCatchObject) node).getPage());
            }
        }
        return null;
    }

    @Override
    public Collection<Key> updateKeys() {
        ArrayList<Key> ids = new ArrayList<Key>();
        for (AdsBaseObject node : page.getNodes()) {
            if (node instanceof AdsScopeObject) {
                ids.add(Key.Factory.forId(node.getId()));
            }
            if (node instanceof AdsCatchObject) {
                ids.add(Key.Factory.forId(node.getId()));
            }
        }
        return ids;
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public boolean enterContext(MixedNodeChildrenAdapter adapter, AdsPage context) {
        this.adapter = adapter;
        this.page = context;
        if (page != null) {
            page.addEventListener(changeListener);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public ICreatureGroup[] createCreatureGroups() {
        return null;
    }
}
