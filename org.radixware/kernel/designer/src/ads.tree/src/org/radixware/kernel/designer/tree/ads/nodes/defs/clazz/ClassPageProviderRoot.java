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
import org.radixware.kernel.common.defs.ads.clazz.algo.AdsAlgoClassDef;
import org.radixware.kernel.designer.common.general.creation.ICreatureGroup;
import org.radixware.kernel.designer.common.general.nodes.NodesManager;


public class ClassPageProviderRoot extends MixedNodeChildrenProvider<AdsAlgoClassDef> {

    private AdsAlgoClassDef classDef;

    @Override
    public Node findOrCreateNode(Key key) {
        if (((IdKey) key).getId().equals(classDef.getId())) {
            return NodesManager.findOrCreateNode(classDef.getPage());
        }
        return null;
    }

    @Override
    public Collection<Key> updateKeys() {
        ArrayList<Key> ids = new ArrayList<Key>();
        ids.add(Key.Factory.forId(classDef.getId()));
        return ids;
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public boolean enterContext(MixedNodeChildrenAdapter adapter, AdsAlgoClassDef context) {
        classDef = context;
        return true;
    }

    @Override
    public ICreatureGroup[] createCreatureGroups() {
        return null;
    }
}
