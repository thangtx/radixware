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
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.RadixObjects.ContainerChangedEvent;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPageDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.EditorPages;
import org.radixware.kernel.common.defs.ads.clazz.presentation.EditorPages.OrderedPage;
import org.radixware.kernel.designer.ads.editors.creation.AdsNamedDefinitionCreature;
import org.radixware.kernel.designer.common.general.creation.ICreatureGroup;
import org.radixware.kernel.designer.tree.ads.nodes.defs.MixedNodeChildrenAdapter;
import org.radixware.kernel.designer.tree.ads.nodes.defs.MixedNodeChildrenProvider;
import org.radixware.kernel.designer.tree.ads.nodes.defs.MixedNodeChildrenProvider.Key;

public class EditorPagesProvider extends MixedNodeChildrenProvider<RadixObject> {

    private EditorPages.PageOrder container;
    private MixedNodeChildrenAdapter adapter;
    private final RadixObjects.ContainerChangesListener containerListener = new RadixObjects.ContainerChangesListener() {
        @Override
        public void onEvent(ContainerChangedEvent e) {
            if (container != null && adapter != null) {
                adapter.refresh(EditorPagesProvider.this);
            }
        }
    };

    private static class PageKey implements Key {

        final OrderedPage page;

        public PageKey(OrderedPage page) {
            this.page = page;
        }
    }

    @Override
    public Collection<Key> updateKeys() {
        ArrayList<Key> result = new ArrayList<Key>();
        for (EditorPages.OrderedPage p : container) {
            result.add(new PageKey(p));
        }
        return result;
    }

    @Override
    protected Node findOrCreateNode(final Key key) {
        if (key instanceof PageKey) {
            final EditorPages.OrderedPage p = ((PageKey) key).page;
            if (p == null || p.getContainer() != container) {
                return null;
            }
            return new EditorPagesOrderedPageNode(p);
        }
        return null;
    }
    private RadixObject context;

    @Override
    public boolean enterContext(MixedNodeChildrenAdapter<RadixObject> adapter, RadixObject context) {
        this.adapter = adapter;
        this.context = context;

        if (context instanceof EditorPages) {
            this.container = ((EditorPages) context).getOrder();
        } else if (context instanceof EditorPages.OrderedPage) {
            this.container = ((EditorPages.OrderedPage) context).getSubpages();
        } else {
            return false;
        }
        this.container.getContainerChangesSupport().addEventListener(containerListener);
        //   this.container.getOwnerEditorPages().getLocal().getContainerChangesSupport().addEventListener(containerListener);
        return true;


    }

    private class Creature extends AdsNamedDefinitionCreature<AdsEditorPageDef> {

        public Creature(RadixObjects container) {
            super(container, "newEditorPage", "Editor Page");
        }

        @Override
        public AdsEditorPageDef createInstance() {
            return AdsEditorPageDef.Factory.newInstance();
        }

        @Override
        public void afterAppend(AdsEditorPageDef object) {
            super.afterAppend(object);
            EditorPages.OrderedPage order = EditorPagesProvider.this.container.getOwnerEditorPages().getOrder().findOrderByPageId(object.getId());
            if (order != null) {
                EditorPagesProvider.this.container.addPageToOrder(object);
            }
        }

        @Override
        public String getDisplayName() {
            if (context instanceof EditorPages.OrderedPage) {
                return "Sub Page";
            } else {
                return "Editor Page";
            }
        }
    }

    @Override
    public ICreatureGroup[] createCreatureGroups() {

        return new ICreatureGroup[]{
            new ICreatureGroup() {
                @Override
                public List<ICreature> getCreatures() {
                    return Collections.singletonList((ICreature) new Creature(container.getOwnerEditorPages().getLocal()));
                }

                @Override
                public String getDisplayName() {
                    return "Editor Page";
                }
            }
        };
    }

    @Override
    protected void reorder(int[] perm) {
        container.reorder(perm);
    }

    @Override
    protected boolean isSorted() {
        return true;
    }
}
