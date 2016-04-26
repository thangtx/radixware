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
package org.radixware.kernel.designer.tree.ads.nodes.defs.exploreritems;

import java.util.List;
import javax.swing.Action;
import org.openide.nodes.Node;
import org.openide.util.actions.SystemAction;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsParagraphExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.ExplorerItems;
import org.radixware.kernel.designer.tree.ads.nodes.actions.InheritExplorerItemAction;

//import org.radixware.kernel.designer.tree.ads.nodes.actions.InheritOrderAction;
import org.radixware.kernel.designer.tree.ads.nodes.actions.OverrideClassMemberAction;
import org.radixware.kernel.designer.tree.ads.nodes.actions.UnhideExplorerItemAction;
import org.radixware.kernel.designer.tree.ads.nodes.defs.AdsMixedNode;
import org.radixware.kernel.designer.tree.ads.nodes.defs.AdsObjectNode;
import org.radixware.kernel.designer.tree.ads.nodes.defs.MixedNodeChildrenAdapter;
import org.radixware.kernel.designer.tree.ads.nodes.defs.MixedNodeChildrenProvider;

public class AdsExplorerItemsNode extends AdsMixedNode<ExplorerItems.Children> {

    private final OverrideClassMemberAction.OverrideCookie overrideCookie;
    //  private final InheritExplorerItemAction.Cookie inheritCookie;
    //  private final InheritOrderAction.Cookie inheritOrderCookie;
    private final UnhideExplorerItemAction.Cookie unhideCookie;

    public AdsExplorerItemsNode(MixedNodeChildrenAdapter adapter, ExplorerItems.Children definitions) {
        super(definitions, adapter);
        overrideCookie = OverrideClassMemberAction.CookieFactory.newInstance(getRadixObject());
        addCookie(overrideCookie);
        addCookie(unhideCookie = new UnhideExplorerItemAction.Cookie());
        if (isNodeInherited()) {
            setGrayed(true);
        }
        if (definitions.getOwnerDefinition() instanceof AdsEditorPresentationDef || definitions.getOwnerDefinition() instanceof AdsParagraphExplorerItemDef) {
            addCookie(new InheritExplorerItemAction.Cookie(definitions.getOwnerExplorerItems()));
        }
        //    inheritCookie = new InheritExplorerItemAction.Cookie(this);
        //    addCookie(inheritCookie);
        //     addCookie(inheritOrderCookie = new InheritOrderAction.Cookie(definitions.getOwnerExplorerItems()));
    }

    public final boolean isNodeInherited() {
        Node parentNode = parentAdapter.getParentNode();

        if (parentNode instanceof AdsExplorerItemNode && ((AdsExplorerItemNode) parentNode).isNodeInherited()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean noFilter() {
        return true;
    }

    public AdsDefinition findRootContext() {
        Node parentNode = parentAdapter.getParentNode();
        while (parentNode != null) {
            if (parentNode instanceof AdsObjectNode) {
                final RadixObject obj = ((AdsObjectNode) parentNode).getRadixObject();
                if (obj instanceof AdsEditorPresentationDef) {
                    return ((AdsDefinition) obj);
                } else if (obj instanceof AdsParagraphExplorerItemDef && ((AdsParagraphExplorerItemDef) obj).isTopLevelDefinition()) {
                    return ((AdsDefinition) obj);
                }
            }
            if (parentNode instanceof AdsMixedNode) {
                final MixedNodeChildrenAdapter adapter = ((AdsMixedNode) parentNode).parentAdapter;
                if (adapter != null) {
                    parentNode = adapter.getParentNode();
                } else {
                    parentNode = parentNode.getParentNode();
                }
            } else {
                parentNode = parentNode.getParentNode();
            }
        }
        return null;
    }

    public ExplorerItems findRootItems() {

        Node parentNode = parentAdapter.getParentNode();
        while (parentNode != null) {
            if (parentNode instanceof AdsObjectNode) {
                final RadixObject obj = ((AdsObjectNode) parentNode).getRadixObject();
                if (obj instanceof AdsEditorPresentationDef) {
                    return ((AdsEditorPresentationDef) obj).getExplorerItems();
                } else if (obj instanceof AdsParagraphExplorerItemDef && ((AdsParagraphExplorerItemDef) obj).isTopLevelDefinition()) {
                    return ((AdsParagraphExplorerItemDef) obj).getExplorerItems();
                }
            }
            if (parentNode instanceof AdsMixedNode) {
                final MixedNodeChildrenAdapter adapter = ((AdsMixedNode) parentNode).parentAdapter;
                if (adapter != null) {
                    parentNode = adapter.getParentNode();
                } else {
                    parentNode = parentNode.getParentNode();
                }
            } else {
                parentNode = parentNode.getParentNode();
            }
        }

        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected Class<? extends MixedNodeChildrenProvider>[] getChildrenProviders() {
        return new Class[]{
            ExplorerItemsProvider.class
        };
    }

    @Override
    public void addCustomActions(List<Action> actions) {
        super.addCustomActions(actions);
        actions.add(SystemAction.get(OverrideClassMemberAction.class));
        actions.add(SystemAction.get(UnhideExplorerItemAction.class));
        actions.add(SystemAction.get(UnhideExplorerItemAction.class));
        actions.add(SystemAction.get(InheritExplorerItemAction.class));

        //  actions.add(SystemAction.get(InheritExplorerItemAction.class));
        //   actions.add(SystemAction.get(InheritOrderAction.class));
    }
}
