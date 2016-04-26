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

package org.radixware.kernel.designer.tree.ads.nodes.defs;

import java.util.ArrayList;
import java.util.Collection;
import org.openide.nodes.Node;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.RadixObjects.ContainerChangedEvent;
import org.radixware.kernel.common.defs.ads.ui.AdsLayout;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;
import org.radixware.kernel.designer.common.general.creation.ICreatureGroup;
import org.radixware.kernel.designer.common.general.nodes.NodesManager;


public class FormLayoutProvider extends MixedNodeChildrenProvider<AdsLayout> {

    private AdsLayout layout;
    @SuppressWarnings({"rawtypes"})
    private MixedNodeChildrenAdapter adapte;
    private final RadixObjects.ContainerChangesListener changeListener = new RadixObjects.ContainerChangesListener() {
        @Override
        public void onEvent(ContainerChangedEvent e) {
            if (layout != null && adapte != null) {
                adapte.refresh(FormLayoutProvider.this);
            }
        }
    };

    @Override
    public Node findOrCreateNode(Key key) {
        final RadixObject radixObject = ((ObjKey) key).getObject();
        return NodesManager.findOrCreateNode(radixObject);
        /*
         final Id id = ((IdKey)key).getId();
         for (AdsLayout.Item item: layout.getItems()) {
         if (AdsUIUtil.getItemId(item) == id) {
         final Node node = NodesManager.findOrCreateNode(AdsUIUtil.getItemNode(item));
         return node;
         }
         }
         return null;
         */
    }

    @Override
    public Collection<Key> updateKeys() {
        final ArrayList<Key> keys = new ArrayList<Key>();
        for (AdsLayout.Item item : layout.getItems()) {
            keys.add(Key.Factory.forObject(AdsUIUtil.getItemNode(item)));
        }
        return keys;
    }

    @Override
    @SuppressWarnings({"rawtypes"})
    public boolean enterContext(MixedNodeChildrenAdapter adapter, AdsLayout context) {
        this.adapte = adapter;
        this.layout = context;
        AdsUIUtil.addContainerListener(layout, changeListener);
        return layout != null;
    }

    @Override
    public ICreatureGroup[] createCreatureGroups() {
        return null;
    }
}
