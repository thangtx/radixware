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
import java.util.EnumSet;
import java.util.List;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.type.AdsTransparence;
import org.radixware.kernel.common.enums.EClassType;
import org.radixware.kernel.designer.ads.editors.clazz.creation.NestedClassCreature;
import org.radixware.kernel.designer.common.general.creation.ICreatureGroup;
import org.radixware.kernel.designer.common.general.nodes.NodesManager;
import org.radixware.kernel.designer.tree.ads.nodes.defs.MixedNodeChildrenAdapter;
import org.radixware.kernel.designer.tree.ads.nodes.defs.MixedNodeChildrenProvider;


public class NestedClassesProvider extends MixedNodeChildrenProvider<AdsClassDef> {

    private AdsClassDef ownerClss;

    @Override
    public Collection<Key> updateKeys() {
        if (ownerClss != null) {
            final Collection<AdsClassDef> classes = ownerClss.getNestedClasses().getLocal().list();
//            Collections.sort(fields, nameComparator);

            final ArrayList<Key> ids = new ArrayList<>(classes.size());
            for (final AdsClassDef c : classes) {
                ids.add(Key.Factory.forId(c.getId()));
            }
            return ids;
        } else {
            return null;
        }
    }

    @Override
    protected Node findOrCreateNode(Key key) {
        if (key instanceof IdKey) {
            final AdsClassDef clazz = ownerClss.getNestedClasses().findById(((IdKey) key).getId(), ExtendableDefinitions.EScope.LOCAL).get();
            if (clazz != null) {
                return new FilterNode(NodesManager.findOrCreateNode(clazz));
            }
        }

        return null;
    }
    private MixedNodeChildrenAdapter<AdsClassDef> adapter;
    private final RadixObjects.ContainerChangesListener changesListener = new RadixObjects.ContainerChangesListener() {
        @Override
        public void onEvent(RadixObjects.ContainerChangedEvent e) {
            adapter.refresh(NestedClassesProvider.this);
        }
    };

    @Override
    public boolean enterContext(final MixedNodeChildrenAdapter<AdsClassDef> adapter, AdsClassDef context) {
        this.adapter = adapter;
        this.ownerClss = context;

        if (this.ownerClss != null) {
            this.ownerClss.getNestedClasses().getLocal().getContainerChangesSupport().removeEventListener(changesListener);
            this.ownerClss.getNestedClasses().getLocal().getContainerChangesSupport().addEventListener(changesListener);

            return true;
        } else {
            return false;
        }
    }

    @Override
    public ICreatureGroup[] createCreatureGroups() {
        return new ICreatureGroup[]{
            new ICreatureGroup() {
                @Override
                public List<ICreatureGroup.ICreature> getCreatures() {
                    final ArrayList<ICreatureGroup.ICreature> creatures = new ArrayList<>();

                    if (!AdsTransparence.isTransparent(ownerClss) || ownerClss.getTransparence().isExtendable()) {
                        creatures.addAll(NestedClassCreature.Factory.instanceList(ownerClss, EnumSet.of(
                                EClassType.DYNAMIC, EClassType.ENUMERATION,
                                EClassType.EXCEPTION, EClassType.INTERFACE, EClassType.COMMAND_MODEL)));

                    }
                    return creatures;
                }

                @Override
                public String getDisplayName() {
                    return "Nested Classes";
                }
            }
        };
    }
}
