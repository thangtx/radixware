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

package org.radixware.kernel.designer.tree.ads.nodes.defs.clazz.presentation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.openide.nodes.Node;
import org.radixware.kernel.common.defs.Definitions;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.RadixObjects.ContainerChangedEvent;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsFilterDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsFilterDef.Parameter;
import org.radixware.kernel.designer.ads.editors.creation.AdsNamedDefinitionCreature;
import org.radixware.kernel.designer.ads.editors.filters.parameter.AdsFilterParameterCreature;
import org.radixware.kernel.designer.common.general.creation.ICreatureGroup;
import org.radixware.kernel.designer.common.general.nodes.NodesManager;
import org.radixware.kernel.designer.tree.ads.nodes.defs.MixedNodeChildrenAdapter;
import org.radixware.kernel.designer.tree.ads.nodes.defs.MixedNodeChildrenProvider;
import org.radixware.kernel.designer.tree.ads.nodes.defs.MixedNodeChildrenProvider.Key;


public class FilterParamsProvider extends MixedNodeChildrenProvider<AdsFilterDef> {

    @SuppressWarnings({"rawtypes", "unchecked"})
    Definitions<Parameter> paramList = null;
    @SuppressWarnings({"rawtypes", "unchecked"})
    MixedNodeChildrenAdapter adapter = null;
    private final RadixObjects.ContainerChangesListener containerListener = new RadixObjects.ContainerChangesListener() {
        @Override
        public void onEvent(ContainerChangedEvent e) {
            if (paramList != null && adapter != null) {
                adapter.refresh(FilterParamsProvider.this);
            }
        }
    };

    @Override
    public Collection<Key> updateKeys() {
        ArrayList<Key> result = new ArrayList<Key>();
        for (AdsFilterDef.Parameter p : paramList) {
            result.add(Key.Factory.forId(p.getId()));
        }
        return result;
    }

    @Override
    protected Node findOrCreateNode(Key key) {
        if (key instanceof IdKey) {
            AdsFilterDef.Parameter p = paramList.findById(((IdKey) key).getId());
            if (p == null) {
                return null;
            }
            return NodesManager.findOrCreateNode(p);
        }
        return null;
    }

    @Override
    public ICreatureGroup[] createCreatureGroups() {
        return new ICreatureGroup[]{
            new ICreatureGroup() {
                @Override
                public List<ICreature> getCreatures() {
                    return Collections.singletonList((ICreature) new AdsFilterParameterCreature(paramList));
                }

                @Override
                public String getDisplayName() {
                    return "Ads Filter Parameter";
                }
            }
        };
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public boolean enterContext(MixedNodeChildrenAdapter adapter, AdsFilterDef context) {
        this.paramList = context.getParameters();
        this.adapter = adapter;
        this.paramList.getContainerChangesSupport().addEventListener(containerListener);
        return true;
    }
}
