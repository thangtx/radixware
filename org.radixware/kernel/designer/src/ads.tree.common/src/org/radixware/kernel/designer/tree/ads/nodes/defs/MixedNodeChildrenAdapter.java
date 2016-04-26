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

import org.radixware.kernel.designer.tree.ads.nodes.defs.clazz.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import javax.swing.SwingUtilities;
import org.openide.nodes.Children;
import org.openide.nodes.Index;
import org.openide.nodes.Node;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.build.BuildOptions;
import org.radixware.kernel.designer.common.general.creation.ICreatureGroup;
import org.radixware.kernel.designer.common.general.creation.ICreatureGroup.ICreature;
import org.radixware.kernel.designer.common.tree.RadixObjectNode;
import org.radixware.kernel.designer.tree.ads.nodes.defs.AdsMixedNode;
import org.radixware.kernel.designer.tree.ads.nodes.defs.MixedNodeChildrenProvider.Key;

/**
 * Children adapter class for class or class item node. Use definition Id as key
 * for node indexing. Use this adspter to combine together one or more instances
 * of {@linkplain ClassNodeChildrenProvider}
 *
 */
public final class MixedNodeChildrenAdapter<T extends RadixObject> extends Children.Keys<Key> {

    private static final boolean DEBUG = true;
    private final MixedNodeChildrenProvider<T>[] providers;
    private final AdsMixedNode<T> node;
    private boolean empty;
    private final Index index;

    private class MixedNodeChildrenIndex extends Index.Support {

        @Override
        public Node[] getNodes() {
            return MixedNodeChildrenAdapter.this.getNodes();
        }

        @Override
        public int getNodesCount() {
            return MixedNodeChildrenAdapter.this.getNodesCount();
        }

        @Override
        public void reorder(int[] perm) {
            providers[0].reorder(perm);
        }
    }

    protected Index getIndex() {
        return index;
    }
    private boolean transitiveCreation;

    public AdsMixedNode getParentNode() {
        return node;
    }

    @SuppressWarnings("unchecked")
    public MixedNodeChildrenAdapter(AdsMixedNode<T> node, T context, Class<? extends MixedNodeChildrenProvider<?>>[] providers, boolean transitiveCreation) {
        this.node = node;
        ArrayList<MixedNodeChildrenProvider<T>> pl = new ArrayList<>(providers.length);
        this.transitiveCreation = transitiveCreation;

        for (int i = 0; i < providers.length; i++) {
            try {
                final MixedNodeChildrenProvider<T> p = (MixedNodeChildrenProvider<T>) providers[i].newInstance();
                if (p.enterContext(this, context)) {
                    p.adapter = this;
                    pl.add(p);
                }
            } catch (InstantiationException | IllegalAccessException ex) {
                if (DEBUG) {
                    ex.printStackTrace();
                }
            }
        }
        if (!pl.isEmpty()) {
            this.providers = pl.toArray(new MixedNodeChildrenProvider[]{});
            if (this.providers.length > 0 && this.providers[0].isSorted()) {
                index = new MixedNodeChildrenIndex();
            } else {
                index = null;
            }
        } else {
            this.providers = null;
            index = null;
        }
        setKeys(updateKeys());

    }

    public boolean isEmpty() {
        return empty;
    }

    private List<Key> updateKeys() {
        @SuppressWarnings("unchecked")
        List<Key> newKeys = new ArrayList<>();
        if (this.providers != null) {
            for (int i = 0; i < this.providers.length; i++) {
                Collection<Key> providerKeys = this.providers[i].updateKeys();
                if (providerKeys != null && !providerKeys.isEmpty()) {
                    newKeys.addAll(providerKeys);
                }
            }
        }
        empty = newKeys.isEmpty();
        return newKeys;
    }

    @Override
    protected Node[] createNodes(Key key) {
        if (this.providers != null) {
            for (int i = 0; i < providers.length; i++) {
                Node[] createdNodes = providers[i].createNodes(key);
                if (createdNodes != null && createdNodes.length > 0) {
                    BuildOptions.UserModeHandler userModeHandler = getParentNode().getLookup().lookup(BuildOptions.UserModeHandler.class);
                    if (userModeHandler != null) {
                        for (int n = 0; n < createdNodes.length; n++) {
                            if (createdNodes[n] instanceof RadixObjectNode) {
                                ((RadixObjectNode) createdNodes[n]).expandLookup(BuildOptions.UserModeHandler.class, userModeHandler);
                            }
                        }
                    }
                    return createdNodes;
                }
            }
        }
        return new Node[]{};
    }
    //private static final Object lock = new Object();

    public void refresh(MixedNodeChildrenProvider source) {
        MUTEX.postWriteRequest(new Runnable() {
            @Override
            public void run() {

                setKeys(updateKeys());
                node.checkChildren();
            }
        });



//        synchronized (lock) {
//            refresh();
//            //setKeys(updateKeys());
//
//        }
    }
    @SuppressWarnings("unchecked")
    private static final Class<? extends MixedNodeChildrenProvider<?>>[] simpleClassProviders = new Class[]{
        NestedClassesProvider.class,
        ClassMethodGroupsProvider.class,
        ClassMethodsProvider.class,
        ClassPropertyGroupsProvider.class,
        ClassPropertiesProvider.class
    };
    @SuppressWarnings("unchecked")
    private static final Class<? extends MixedNodeChildrenProvider<?>>[] entityClassProviders = new Class[]{
        NestedClassesProvider.class,
        ClassMethodGroupsProvider.class,
        ClassMethodsProvider.class,
        ClassPropertyGroupsProvider.class,
        ClassPropertiesProvider.class,
        ClassPresentationsProvider.class
    };
    @SuppressWarnings("unchecked")
    private static final Class<? extends MixedNodeChildrenProvider<?>>[] modelClassProviders = simpleClassProviders;
    @SuppressWarnings("unchecked")
    private static final Class<? extends MixedNodeChildrenProvider<?>>[] enumClassFieldProviders = new Class[]{
        ClassMethodGroupsProvider.class,
        ClassMethodsProvider.class,
        ClassPropertyGroupsProvider.class,
        ClassPropertiesProvider.class
    };
    @SuppressWarnings("unchecked")
    private static final Class<? extends MixedNodeChildrenProvider<?>>[] enumClassProviders = new Class[]{
        EnumClassFieldsProvider.class,
        NestedClassesProvider.class,
        ClassMethodGroupsProvider.class,
        ClassMethodsProvider.class,
        ClassPropertyGroupsProvider.class,
        ClassPropertiesProvider.class
    };

    public static final Class<? extends MixedNodeChildrenProvider<?>>[] simpleClassProviders() {
        return simpleClassProviders;
    }

    public static final Class<? extends MixedNodeChildrenProvider<?>>[] presentableClassProviders() {
        return entityClassProviders;
    }

    public static final Class<? extends MixedNodeChildrenProvider<?>>[] modelClassProviders() {
        return modelClassProviders;
    }

    public static final Class<? extends MixedNodeChildrenProvider<?>>[] enumClassFieldProviders() {
        return enumClassFieldProviders;
    }

    public static final Class<? extends MixedNodeChildrenProvider<?>>[] enumClassProviders() {
        return enumClassProviders;
    }

    public final MixedNodeChildrenProvider<T>[] getProviders() {
        return providers;
    }

    public ICreatureGroup[] createCreatureGroups() {
        if (transitiveCreation) {
            if (this.providers != null) {
                HashMap<String, List<ICreatureGroup>> groupsByName = new HashMap<>();
                for (int i = 0; i < this.providers.length; i++) {
                    if (this.providers[i].adapter.providers != null) {
                        for (int j = 0; j < this.providers[i].adapter.providers.length; j++) {
                            ICreatureGroup[] group = this.providers[i].adapter.providers[j].createCreatureGroups();
                            if (group != null) {
                                for (int g = 0; g < group.length; g++) {
                                    String name = group[g].getDisplayName();
                                    List<ICreatureGroup> list = groupsByName.get(name);
                                    if (list == null) {
                                        list = new ArrayList<>();
                                        groupsByName.put(name, list);
                                    }
                                    list.add(group[g]);
                                }
                            }
                        }

                    }
                }
                ArrayList<ICreatureGroup> groups = new ArrayList<>();
                for (java.util.Map.Entry<String, List<ICreatureGroup>> e : groupsByName.entrySet()) {
                    final ArrayList<ICreature> creatures = new ArrayList<>();
                    final String name = e.getKey();
                    for (ICreatureGroup g : e.getValue()) {
                        creatures.addAll(g.getCreatures());
                    }
                    groups.add(new ICreatureGroup() {
                        @Override
                        public List<ICreature> getCreatures() {
                            return creatures;
                        }

                        @Override
                        public String getDisplayName() {
                            return name;
                        }
                    });
                }
                if (!groups.isEmpty()) {
                    ICreatureGroup[] res = new ICreatureGroup[groups.size()];
                    groups.toArray(res);
                    return res;
                }
            }
        } else {
            if (this.providers != null) {
                ArrayList<ICreatureGroup> cpc = new ArrayList<>();
                for (int i = 0; i
                        < this.providers.length; i++) {
                    ICreatureGroup[] _providers = this.providers[i].createCreatureGroups();
                    if (_providers != null) {
                        for (int j = 0; j
                                < _providers.length; j++) {
                            cpc.add(_providers[j]);
                        }

                    }
                }
                if (!cpc.isEmpty()) {
                    ICreatureGroup[] cpcs = new ICreatureGroup[cpc.size()];
                    cpc.toArray(cpcs);
                    return cpcs;
                } else {
                    return null;
                }

            }
        }
        return null;
    }
}
