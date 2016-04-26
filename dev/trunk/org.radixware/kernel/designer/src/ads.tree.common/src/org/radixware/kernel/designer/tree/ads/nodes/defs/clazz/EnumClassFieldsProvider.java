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
import java.util.List;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.RadixObjects.ContainerChangedEvent;
import org.radixware.kernel.common.defs.ads.clazz.AdsEnumClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsEnumClassFieldDef;
import org.radixware.kernel.designer.ads.editors.clazz.enumeration.creature.EnumClassFieldCreature;
import org.radixware.kernel.designer.common.general.creation.ICreatureGroup;
import org.radixware.kernel.designer.common.general.creation.ICreatureGroup.ICreature;
import org.radixware.kernel.designer.common.general.nodes.NodesManager;
import org.radixware.kernel.designer.tree.ads.nodes.defs.MixedNodeChildrenAdapter;
import org.radixware.kernel.designer.tree.ads.nodes.defs.MixedNodeChildrenProvider;


public class EnumClassFieldsProvider extends MixedNodeChildrenProvider<AdsEnumClassDef> {

    private MixedNodeChildrenAdapter<AdsEnumClassDef> adapter;
    private AdsEnumClassDef enumClassDef;

    private final RadixObjects.ContainerChangesListener changesListener = new RadixObjects.ContainerChangesListener() {

        @Override
        public void onEvent(ContainerChangedEvent e) {
            adapter.refresh(EnumClassFieldsProvider.this);
        }
    };

    @Override
    public Collection<Key> updateKeys() {
        if (enumClassDef != null) {
            final Collection<AdsEnumClassFieldDef> fields = enumClassDef.getFields().getLocal().list();
//            Collections.sort(fields, nameComparator);

            final ArrayList<Key> ids = new ArrayList<>(fields.size());
            for (AdsEnumClassFieldDef g : fields) {
                ids.add(Key.Factory.forId(g.getId()));
            }
            return ids;
        } else {
            return null;
        }
    }

    @Override
    protected Node findOrCreateNode(Key key) {
        if (key instanceof IdKey) {
            final AdsEnumClassFieldDef field = enumClassDef.getFields().findById(((IdKey) key).getId(), ExtendableDefinitions.EScope.LOCAL).get();
            if (field != null) {
                return new FilterNode(NodesManager.findOrCreateNode(field));
            }
        }

        return null;
    }

    @Override
    public boolean enterContext(final MixedNodeChildrenAdapter<AdsEnumClassDef> adapter, AdsEnumClassDef context) {
        this.adapter = adapter;
        this.enumClassDef = context;

        if (this.enumClassDef != null) {
            this.enumClassDef.getFields().getLocal().getContainerChangesSupport().removeEventListener(changesListener);
            this.enumClassDef.getFields().getLocal().getContainerChangesSupport().addEventListener(changesListener);

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
                    public List<ICreature> getCreatures() {
                        final ArrayList<ICreature> creatures = new ArrayList<>();
                        creatures.add(new EnumClassFieldCreature(enumClassDef.getFields().getLocal()));
                        return creatures;
                    }

                    @Override
                    public String getDisplayName() {
                        return "Enum Items";
                    }
                }
            };
    }
}
