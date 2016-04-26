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

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import org.openide.nodes.Node;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.IAdsPresentableClass;
import org.radixware.kernel.designer.common.general.creation.ICreatureGroup;
import org.radixware.kernel.designer.common.general.nodes.NodesManager;
import org.radixware.kernel.designer.tree.ads.nodes.defs.MixedNodeChildrenAdapter;
import org.radixware.kernel.designer.tree.ads.nodes.defs.MixedNodeChildrenProvider;

public class ClassPresentationsProvider extends MixedNodeChildrenProvider<AdsClassDef> {

    private static final Key PRESENTATION_NODE_KEY = new Key() {
    };
    private static final Collection<Key> ALL_KEYS = Collections.unmodifiableCollection(Arrays.asList(PRESENTATION_NODE_KEY));

    @Override
    public Collection<Key> updateKeys() {
        return ALL_KEYS;
    }
    private Node node = null;

    @Override
    public Node findOrCreateNode(Key key) {
        synchronized (this) {
            if (key == PRESENTATION_NODE_KEY) {
                if (node == null) {
                    node = NodesManager.findOrCreateNode(this.target.getPresentations());
                }
                return node;
            } else {
                return null;
            }
        }
    }
    private IAdsPresentableClass target;

    @Override
    public boolean enterContext(MixedNodeChildrenAdapter<AdsClassDef> adapter, AdsClassDef context) {
        if (context instanceof IAdsPresentableClass) {
            this.target = (IAdsPresentableClass) context;
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
