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
import javax.swing.Action;
import org.openide.nodes.Node;
import org.openide.util.actions.SystemAction;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.ExtendableMembers;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.*;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.utils.events.IRadixEventListener;
import org.radixware.kernel.common.utils.events.RadixEvent;
import org.radixware.kernel.designer.common.general.creation.ICreatureGroup;
import org.radixware.kernel.designer.common.general.displaying.AutomaticTreeUpdater;
import org.radixware.kernel.designer.tree.ads.nodes.actions.InheritEditorPageAction;
import org.radixware.kernel.designer.tree.ads.nodes.actions.OverrideClassMemberAction;
import org.radixware.kernel.designer.tree.ads.nodes.defs.AdsMixedNode;
import org.radixware.kernel.designer.tree.ads.nodes.defs.MixedNodeChildrenAdapter;
import org.radixware.kernel.designer.tree.ads.nodes.defs.MixedNodeChildrenProvider;
import org.radixware.kernel.designer.tree.ads.nodes.defs.MixedNodeChildrenProvider.Key;

public abstract class CollectionNode extends AdsMixedNode<ExtendableDefinitions<? extends AdsDefinition>> {

    public static class EditorPresentations extends CollectionNode {

        public EditorPresentations(ExtendableDefinitions<? extends AdsDefinition> definition) {
            super(definition);
        }

        @Override
        protected Class[] getChildrenProviders() {
            return new Class[]{PresentationComponentsProvider.EditorPresentationsProvider.class};
        }
    }

    public static class SelectorPresentations extends CollectionNode {

        public SelectorPresentations(ExtendableDefinitions<? extends AdsDefinition> definition) {
            super(definition);
        }

        @Override
        protected Class<?>[] getChildrenProviders() {
            return new Class[]{PresentationComponentsProvider.SelectorPresentationsProvider.class};
        }
    }

    public static class Filters extends CollectionNode {

        public Filters(ExtendableDefinitions<? extends AdsDefinition> definition) {
            super(definition);
        }

        @Override
        protected Class[] getChildrenProviders() {
            return new Class[]{PresentationComponentsProvider.FiltersProvider.class};
        }
    }

    public static class Sortings extends CollectionNode {

        public Sortings(ExtendableDefinitions<? extends AdsDefinition> definition) {
            super(definition);
        }

        @Override
        protected Class[] getChildrenProviders() {
            return new Class[]{PresentationComponentsProvider.SortingsProvider.class};
        }
    }

    public static class ClassCatalogs extends CollectionNode {

        public ClassCatalogs(ExtendableDefinitions<? extends AdsDefinition> definition) {
            super(definition);
        }

        @Override
        protected Class[] getChildrenProviders() {
            return new Class[]{PresentationComponentsProvider.ClassCatalogsProvider.class};
        }
    }

    public static class Commands extends CollectionNode {

        public Commands(ExtendableDefinitions<? extends AdsDefinition> definition) {
            super(definition);
        }

        @Override
        protected Class[] getChildrenProviders() {
            return new Class[]{PresentationComponentsProvider.CommandsProvider.class};
        }
    }

    private static final Class getEditorPagesProviderClass() {
        try {
            return Thread.currentThread().getContextClassLoader().loadClass("org.radixware.kernel.designer.tree.ads.nodes.defs.clazz.presentation.EditorPagesProvider");
        } catch (ClassNotFoundException ex) {
            throw new RadixError("No class def found", ex);
        }
    }

    public static class EditorPages extends CollectionNode {

        private final InheritEditorPageAction.InheritEditorPageCookie inheritPageCookie;

        public EditorPages(org.radixware.kernel.common.defs.ads.clazz.presentation.EditorPages definition) {
            super(definition);
            inheritPageCookie = new InheritEditorPageAction.InheritEditorPageCookie(definition.getOrder());
            addCookie(inheritPageCookie);
        }

        @Override
        protected Class[] getChildrenProviders() {
            return new Class[]{getEditorPagesProviderClass()};
        }

        @Override
        public void addCustomActions(List<Action> actions) {
            super.addCustomActions(actions);
            actions.add(SystemAction.get(InheritEditorPageAction.class));
        }
    }

    public CollectionNode(ExtendableDefinitions<? extends AdsDefinition> definition) {
        super(definition);
        addCookie(OverrideClassMemberAction.CookieFactory.newInstance(definition));
    }

    @Override
    public void addCustomActions(List<Action> actions) {
        super.addCustomActions(actions);
        actions.add(SystemAction.get(OverrideClassMemberAction.class));
    }

    public static class Provider extends MixedNodeChildrenProvider<RadixObject> {

        RadixObject presentations;

        private enum KEY {

            EPR, SPR, FLT, SRT, CMD, CC, CS, EP
        }

        private static class KeyImpl implements Key {

            KEY key;

            private KeyImpl(KEY key) {
                this.key = key;
            }
        }
        private static final Key[] keys = new Key[]{
            new KeyImpl(KEY.EPR),
            new KeyImpl(KEY.SPR),
            new KeyImpl(KEY.CMD),
            new KeyImpl(KEY.FLT),
            new KeyImpl(KEY.SRT),
            new KeyImpl(KEY.CS),
            new KeyImpl(KEY.CC),
            new KeyImpl(KEY.EP)
        };
        private static final ArrayList<Key> entityKeys = new ArrayList<Key>(7);
        private static final ArrayList<Key> nonPolymorphEntityKeys = new ArrayList<Key>(6);
        private static final ArrayList<Key> entityGroupKeys = new ArrayList<Key>(1);
        private static final ArrayList<Key> formKeys = new ArrayList<Key>(2);

        static {
            entityKeys.add(keys[0]);
            entityKeys.add(keys[1]);
            entityKeys.add(keys[2]);
            entityKeys.add(keys[3]);
            entityKeys.add(keys[4]);
            entityKeys.add(keys[5]);
            entityKeys.add(keys[6]);

            nonPolymorphEntityKeys.add(keys[0]);
            nonPolymorphEntityKeys.add(keys[1]);
            nonPolymorphEntityKeys.add(keys[2]);
            nonPolymorphEntityKeys.add(keys[3]);
            nonPolymorphEntityKeys.add(keys[4]);
            nonPolymorphEntityKeys.add(keys[5]);

            entityGroupKeys.add(keys[2]);

            formKeys.add(keys[7]);
            formKeys.add(keys[2]);

        }

        @Override
        public Collection<Key> updateKeys() {
            if (presentations instanceof AdsEditorPresentationDef) {
                if (((AdsEditorPresentationDef) presentations).isEditorPagesInherited()) {
                    return Collections.emptySet();
                }
                return Collections.singleton(keys[7]);
            } else if (presentations instanceof AdsFilterDef) {
                return Collections.singleton(keys[7]);
            } else if (presentations instanceof EntityObjectPresentations) {
                AdsEntityObjectClassDef clazz = ((EntityObjectPresentations) presentations).getOwnerClass();
                if (clazz.isPolymorphic() || !((EntityObjectPresentations) presentations).getClassCatalogs().getLocal().isEmpty()) {
                    return entityKeys;
                } else {
                    return nonPolymorphEntityKeys;
                }
            } else if (presentations instanceof EntityGroupPresentations) {
                return entityGroupKeys;
            } else if (presentations instanceof ReportPresentations) {
                if (((ReportPresentations) presentations).isEditorPagesInherited()) {
                    return Collections.emptyList();
                } else {
                    return Collections.singleton(keys[7]);
                }
            } else if (presentations instanceof AbstractFormPresentations) {
                if (((AbstractFormPresentations) presentations).isEditorPagesInherited()) {
                    return Collections.singleton(keys[2]);
                } else {
                    return formKeys;
                }
            } else {
                return Collections.emptySet();
            }
        }

        @Override
        protected Node findOrCreateNode(Key key) {
            if (key instanceof KeyImpl) {
                switch (((KeyImpl) key).key) {
                    case EPR:
                        return new EditorPresentations(((EntityObjectPresentations) presentations).getEditorPresentations());
                    case SPR:
                        return new SelectorPresentations(((EntityObjectPresentations) presentations).getSelectorPresentations());
                    case CMD:
                        return new Commands(((ClassPresentations) presentations).getCommands());
                    case FLT:
                        return new Filters(((EntityObjectPresentations) presentations).getFilters());
                    case SRT:
                        return new Sortings(((EntityObjectPresentations) presentations).getSortings());
                    case CC:
                        return new ClassCatalogs(((EntityObjectPresentations) presentations).getClassCatalogs());
//                    case CS:
//                        return new ColorSchemes(((EntityObjectPresentations) presentations).getColorSchemes());
                    case EP:
                        if (presentations instanceof AbstractFormPresentations) {
                            return new EditorPages(((AbstractFormPresentations) presentations).getEditorPages());
                        } else if (presentations instanceof AdsFilterDef) {
                            return new EditorPages(((AdsFilterDef) presentations).getEditorPages());
                        } else if (presentations instanceof AdsEditorPresentationDef) {
                            return new EditorPages(((AdsEditorPresentationDef) presentations).getEditorPages());
                        } else {
                            return null;
                        }
                }
            }
            return null;
        }
        IRadixEventListener<RadixEvent> epListener;
        private MixedNodeChildrenAdapter<RadixObject> adapter;

        @Override
        @SuppressWarnings("unchecked")
        public boolean enterContext(MixedNodeChildrenAdapter<RadixObject> adapter, RadixObject context) {
            this.adapter = adapter;
            this.presentations = context;
            if (context instanceof AdsEditorPresentationDef || context instanceof AbstractFormPresentations) {
                epListener = new IRadixEventListener<RadixEvent>() {
                    @Override
                    public void onEvent(RadixEvent e) {
                        Provider.this.adapter.refresh(Provider.this);
                    }
                };
                if (context instanceof AdsEditorPresentationDef) {
                    ((AdsEditorPresentationDef) context).getEditorPagesInheritanceChangesSupport().addEventListener(epListener);
                } else {
                    ((AbstractFormPresentations) context).getEditorPagesInheritanceChangesSupport().addEventListener(epListener);
                }
            }
            if (context instanceof EntityObjectPresentations) {
                AdsEntityObjectClassDef clazz = ((EntityObjectPresentations) context).getOwnerClass();
                if (clazz != null) {
                    AutomaticTreeUpdater.getDefault().register(new ClassCatalogsNodeState(clazz, adapter, this));
                }
            }
            return true;
        }

        @Override
        public ICreatureGroup[] createCreatureGroups() {
            return null;
        }

        @Override
        protected boolean isSorted() {
            return true;
        }
    }

    private static class ClassCatalogsNodeState implements AutomaticTreeUpdater.State {

        private final AdsEntityObjectClassDef clazz;
        private boolean isPolymorphic;
        private MixedNodeChildrenAdapter<RadixObject> adapter;
        private MixedNodeChildrenProvider<RadixObject> provider;
        private int count;

        public ClassCatalogsNodeState(AdsEntityObjectClassDef clazz, MixedNodeChildrenAdapter<RadixObject> adapter, MixedNodeChildrenProvider<RadixObject> provider) {
            this.clazz = clazz;
            this.isPolymorphic = clazz.isPolymorphic();
            this.adapter = adapter;
            this.provider = provider;
            this.count = clazz.getPresentations().getClassCatalogs().getLocal().size();
        }

        @Override
        public boolean wasChanged() {
            boolean classIsPolimorphic = clazz.isPolymorphic();
            if (classIsPolimorphic) {
                if (!isPolymorphic) {
                    return true;
                } else {
                    return false;
                }
            } else {
                if (isPolymorphic) {
                    return true;
                } else {
                    return clazz.getPresentations().getClassCatalogs().getLocal().size() != count;
                }
            }
        }

        @Override
        public void update() {
            adapter.refresh(provider);
            this.isPolymorphic = clazz.isPolymorphic();
            this.count = clazz.getPresentations().getClassCatalogs().getLocal().size();
        }

        @Override
        public RadixObject getRadixObject() {
            return clazz;
        }
    }
}
