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
import org.radixware.kernel.common.defs.EntireChangesSupport;
import org.radixware.kernel.common.defs.EntireChangesSupport.EntireChangeEvent;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.RadixObjects.ContainerChangedEvent;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsDefinitions;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsFilterDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSelectorPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSortingDef;
import org.radixware.kernel.designer.ads.editors.clazz.creation.ScopeCommandCreature;
import org.radixware.kernel.designer.ads.editors.creation.AdsClassCatalogCreature;
import org.radixware.kernel.designer.ads.editors.creation.AdsNamedDefinitionCreature;
import org.radixware.kernel.designer.ads.editors.creation.AdsPresentationCreature;
import org.radixware.kernel.designer.common.general.creation.ICreatureGroup;
import org.radixware.kernel.designer.common.general.creation.ICreatureGroup.ICreature;
import org.radixware.kernel.designer.common.general.nodes.NodesManager;
import org.radixware.kernel.designer.tree.ads.nodes.defs.MixedNodeChildrenAdapter;
import org.radixware.kernel.designer.tree.ads.nodes.defs.MixedNodeChildrenProvider;
import org.radixware.kernel.designer.tree.ads.nodes.defs.MixedNodeChildrenProvider.Key;


public abstract class PresentationComponentsProvider extends MixedNodeChildrenProvider<ExtendableDefinitions<? extends AdsDefinition>> {

    public static class EditorPresentationsProvider extends PresentationComponentsProvider {

        private class Creature extends AdsPresentationCreature<AdsEditorPresentationDef> {

            public Creature() {
                super(nodesProvider.local(), "newEditorPresentation", AdsPresentationCreature.EDITOR_PRESENTATION);
            }

            @Override
            public AdsEditorPresentationDef createInstance() {
                return AdsEditorPresentationDef.Factory.newInstance();
            }
        }

        @Override
        @SuppressWarnings("rawtypes")
        protected ICreature newItemCreature() {
            return new Creature();
        }
    }

    public static class SelectorPresentationsProvider extends PresentationComponentsProvider {

        private class Creature extends AdsPresentationCreature<AdsSelectorPresentationDef> {

            public Creature() {
                super(nodesProvider.local(), "newSelectorPresentation", AdsPresentationCreature.SELECTOR_PRESENTATION);
            }

            @Override
            public AdsSelectorPresentationDef createInstance() {
                return AdsSelectorPresentationDef.Factory.newInstance();
            }
        }

        @Override
        @SuppressWarnings("rawtypes")
        protected ICreature newItemCreature() {
            return new Creature();
        }
    }

    public static class FiltersProvider extends PresentationComponentsProvider {

        private class Creature extends AdsNamedDefinitionCreature<AdsFilterDef> {

            public Creature() {
                super(nodesProvider.local(), "newFilter", "Filter");
            }

            @Override
            public AdsFilterDef createInstance() {
                return AdsFilterDef.Factory.newInstance();
            }
        }

        @Override
        @SuppressWarnings("rawtypes")
        protected ICreature newItemCreature() {
            return new Creature();
        }
    }

    public static class SortingsProvider extends PresentationComponentsProvider {

        private class Creature extends AdsNamedDefinitionCreature<AdsSortingDef> {

            public Creature() {
                super(nodesProvider.local(), "newSorting", "Sorting");
            }

            @Override
            public AdsSortingDef createInstance() {
                return AdsSortingDef.Factory.newInstance();
            }
        }

        @Override
        @SuppressWarnings("rawtypes")
        protected ICreature newItemCreature() {
            return new Creature();
        }
    }

    public static class CommandsProvider extends PresentationComponentsProvider {

        @Override
        @SuppressWarnings("rawtypes")
        protected ICreature newItemCreature() {
            return new ScopeCommandCreature(nodesProvider.local());
        }
    }

    public static class ClassCatalogsProvider extends PresentationComponentsProvider {

        @Override
        @SuppressWarnings("rawtypes")
        protected ICreature newItemCreature() {
            AdsEntityObjectClassDef clazz = (AdsEntityObjectClassDef) nodesProvider.local().getOwnerDefinition();
            if (clazz == null || !clazz.isPolymorphic()) {
                return null;
            } else {
                return new AdsClassCatalogCreature(nodesProvider.local());
            }
        }
//        @Override
//        protected Class<? extends AdsDefinition> extensionClass() {
//            return AdsClassCatalogDef.class;
//        }
    }

    private static class DefKey implements Key {

        private final AdsDefinition definition;

        public DefKey(AdsDefinition definition) {
            this.definition = definition;
        }
    }

    protected abstract static class NodesProvider {

        @SuppressWarnings("rawtypes")
        MixedNodeChildrenAdapter adapter;
        PresentationComponentsProvider provider;

        @SuppressWarnings("rawtypes")
        NodesProvider(PresentationComponentsProvider provider, MixedNodeChildrenAdapter adapter) {
            this.adapter = adapter;
            this.provider = provider;
        }

        public abstract Collection<Key> updateKeys();

        protected abstract Node findOrCreateNode(Key key);

        protected abstract AdsDefinitions<? extends AdsDefinition> local();
    }

    private static class SimpleNodesProvider extends NodesProvider {

        private final RadixObjects.ContainerChangesListener containerListener = new RadixObjects.ContainerChangesListener() {

            @Override
            public void onEvent(ContainerChangedEvent e) {
                if (collection != null && adapter != null) {
                    adapter.refresh(provider);
                }
            }
        };
        protected AdsDefinitions<? extends AdsDefinition> collection;

        @SuppressWarnings("rawtypes")
        SimpleNodesProvider(PresentationComponentsProvider provider, MixedNodeChildrenAdapter adapter, ExtendableDefinitions<? extends AdsDefinition> context) {
            super(provider, adapter);
            this.collection = (AdsDefinitions<? extends AdsDefinition>) context.getLocal();
            this.collection.getContainerChangesSupport().addEventListener(containerListener);
        }

        @Override
        public Collection<Key> updateKeys() {
            ArrayList<Key> keys = new ArrayList<Key>();
            for (AdsDefinition def : collection) {
                keys.add(new DefKey(def));
            }
            return keys;
        }

        @Override
        protected Node findOrCreateNode(Key key) {
            if (key instanceof DefKey) {
                return NodesManager.findOrCreateNode(((DefKey) key).definition);
            } else {
                return null;
            }
        }

        @Override
        protected AdsDefinitions<? extends AdsDefinition> local() {
            return collection;
        }
    }

    private static class ExtendableNodesProvider extends NodesProvider {

        ExtendableDefinitions<? extends AdsDefinition> context;
        private final EntireChangesSupport.EntireChangeListener listener = new EntireChangesSupport.EntireChangeListener() {

            @Override
            public void onEvent(EntireChangeEvent e) {
                if (adapter != null && provider != null) {
                    adapter.refresh(provider);
                }
            }
        };

        @SuppressWarnings("rawtypes")
        public ExtendableNodesProvider(Class<? extends AdsDefinition> specificator, PresentationComponentsProvider provider, MixedNodeChildrenAdapter adapter, ExtendableDefinitions<? extends AdsDefinition> context) {
            super(provider, adapter);
            this.context = context;
            EntireChangesSupport.getInstance(specificator).addEventListener(listener);
        }

        @Override
        public Collection<Key> updateKeys() {
            ArrayList<Key> keys = new ArrayList<Key>(10);
            for (AdsDefinition def : context.get(ExtendableDefinitions.EScope.ALL)) {
                keys.add(new DefKey(def));
            }
            return keys;
        }

        @Override
        protected Node findOrCreateNode(Key key) {
            if (key instanceof DefKey) {
                return new OverridableNodeDelegate(context.getLocal(), NodesManager.findOrCreateNode(((DefKey) key).definition));
            } else {
                return null;
            }
        }

        @Override
        protected AdsDefinitions<? extends AdsDefinition> local() {
            return (AdsDefinitions<? extends AdsDefinition>) context.getLocal();
        }
    }
    protected NodesProvider nodesProvider;

    @Override
    public Collection<Key> updateKeys() {
        return nodesProvider.updateKeys();
    }

    @Override
    protected Node findOrCreateNode(Key key) {
        return nodesProvider.findOrCreateNode(key);
    }

    protected Class<? extends AdsDefinition> extensionClass() {
        return null;
    }

    @Override
    public boolean enterContext(MixedNodeChildrenAdapter<ExtendableDefinitions<? extends AdsDefinition>> adapter, ExtendableDefinitions<? extends AdsDefinition> context) {
        Class<? extends AdsDefinition> extensionSpecificator = extensionClass();
        this.nodesProvider = extensionSpecificator != null ? new ExtendableNodesProvider(extensionSpecificator, this, adapter, context) : new SimpleNodesProvider(this, adapter, context);
        return true;
    }

    @SuppressWarnings("rawtypes")
    protected abstract ICreature newItemCreature();

    @Override
    @SuppressWarnings("rawtypes")
    public ICreatureGroup[] createCreatureGroups() {
        final ICreature c = newItemCreature();
        if (c != null) {
            return new ICreatureGroup[]{
                    new ICreatureGroup() {

                        @Override
                        public List<ICreature> getCreatures() {
                            return Collections.singletonList(c);
                        }

                        @Override
                        public String getDisplayName() {
                            return c.getDisplayName();
                        }
                    }
                };
        } else {
            return null;
        }
    }

    @Override
    protected boolean isSorted() {
        return true;
    }

    @Override
    protected void reorder(int[] pos) {
        if (pos != null && pos.length > 1) {
            this.nodesProvider.local().reorder(pos);
        }
    }
}
