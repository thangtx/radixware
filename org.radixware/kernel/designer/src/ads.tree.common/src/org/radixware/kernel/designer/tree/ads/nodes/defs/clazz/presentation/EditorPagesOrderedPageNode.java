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

import java.awt.Image;
import java.io.IOException;
import java.util.List;
import javax.swing.Action;
import javax.swing.SwingUtilities;
import org.openide.util.actions.SystemAction;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObject.RenameEvent;
import org.radixware.kernel.common.defs.RadixObjectIcon;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPageDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.EditorPages.EditorPagesOperationSupport;
import org.radixware.kernel.common.defs.ads.clazz.presentation.EditorPages.OrderedPage;
import org.radixware.kernel.common.utils.agents.DefaultAgent;
import org.radixware.kernel.common.utils.events.IRadixEventListener;
import org.radixware.kernel.common.utils.events.RadixEvent;
import org.radixware.kernel.designer.common.annotations.registrators.NodeFactoryRegistration;
import org.radixware.kernel.designer.common.dialogs.usages.FindUsagesAction;
import org.radixware.kernel.designer.common.general.nodes.INodeFactory;
import org.radixware.kernel.designer.common.general.utils.RadixMutex;
import org.radixware.kernel.designer.common.tree.RadixObjectEditCookie;
import org.radixware.kernel.designer.common.tree.RadixObjectNode;
import org.radixware.kernel.designer.common.tree.RadixObjectNodeDeleteAction;
import org.radixware.kernel.designer.common.tree.actions.DefinitionRenameAction;
import org.radixware.kernel.designer.common.tree.actions.DefinitionRenameAction.RenameCookie;
import org.radixware.kernel.designer.tree.ads.nodes.actions.GoToSourceEditorPageAction;
import org.radixware.kernel.designer.tree.ads.nodes.actions.GoToSourceEditorPageAction.GoToSourceEditorPageCookie;
import org.radixware.kernel.designer.tree.ads.nodes.actions.InheritEditorPageAction;
import org.radixware.kernel.designer.tree.ads.nodes.actions.OverrideInheritEditorPageByOrderAction;
import org.radixware.kernel.designer.tree.ads.nodes.defs.AdsMixedNode;
import org.radixware.kernel.designer.tree.ads.nodes.defs.MixedNodeChildrenProvider;


public class EditorPagesOrderedPageNode extends AdsMixedNode<OrderedPage> {

    @Override
    @SuppressWarnings("unchecked")
    protected Class<? extends MixedNodeChildrenProvider<?>>[] getChildrenProviders() {
        return new Class[]{EditorPagesProvider.class, CustomViewProvider.class};
    }

    @NodeFactoryRegistration
    public static class Factory implements INodeFactory<OrderedPage> {

        @Override // Registered in layer.xml
        public RadixObjectNode newInstance(OrderedPage editorPage) {
            return new EditorPagesOrderedPageNode(editorPage);
        }
    }
    private final IRadixEventListener pageStateChangeListener = new IRadixEventListener() {
        @Override
        public void onEvent(RadixEvent e) {
            updatePage();
        }
    };
    private AdsEditorPageDef page;
    private final InheritEditorPageAction.InheritEditorPageCookie inheritPageCookie;
    private final OverrideInheritEditorPageByOrderAction.OverrideEditorPageByOrderCookie overrideCookie;

    private class PageGoToOverridingCookie extends FindUsagesAction.FindUsagesCookie {

        private OrderedPage page;

        public PageGoToOverridingCookie(OrderedPage page) {
            super(null);
            this.page = page;
        }

        @Override
        public Definition getDefinition() {
            return page.findPage();
        }
    }
    private final PageGoToOverridingCookie gotoOvrCookie;
    private final RadixObject.RenameListener renameListener = new RadixObject.RenameListener() {
        @Override
        public void onEvent(RenameEvent e) {
            fireDisplayNameChange(null, null);
        }
    };
    private final GoToSourceEditorPageCookie gotoCookie;

    private void updatePage() {
        if (page != null) {
            page.removeRenameListener(renameListener);
            getLookupContent().remove(page);
        }
        page = (getRadixObject()).findPage();
        if (page != null) {
            page.addRenameListener(renameListener);
            getLookupContent().add(page);
        }
        fireDisplayNameChange(null, null);
    }

    private AdsDefinition.AccessListener accessListener;
    
    @SuppressWarnings("unchecked")
    public EditorPagesOrderedPageNode(OrderedPage def) {
        super(def);
        EditorPagesOperationSupport.getDefault().addEventListener(pageStateChangeListener);
        inheritPageCookie = new InheritEditorPageAction.InheritEditorPageCookie(def.getSubpages());
        addCookie(inheritPageCookie);
        overrideCookie = new OverrideInheritEditorPageByOrderAction.OverrideEditorPageByOrderCookie(def);
        addCookie(overrideCookie);
        gotoOvrCookie = new PageGoToOverridingCookie(def);
        addCookie(gotoOvrCookie);
        gotoCookie = new GoToSourceEditorPageCookie(def);
        addCookie(gotoCookie);
        updatePage();

        if (page != null) {
            accessListener = new AdsDefinition.AccessListener() {
                @Override
                public void onEvent(AdsDefinition.AccessChangedEvent e) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            updateIcon();
                        }
                    });
                }
            };
            page.getAccessChangeSupport().addEventListener(accessListener);
        }
    }

    @Override
    protected RenameCookie createRenameCookie() {
        return new RenameCookie(getRadixObject()); // enable for RadixObject
    }

//    private static final class MyEditCookie extends RadixObjectEditCookie {
//
//        public MyEditCookie(OrderedPage radixObject) {
//            super(radixObject);
//        }
//
//        @Override
//        public void edit() {
//            super.edit();
//        }
//
//        @Override
//        public RadixObject getRadixObject() {
//            OrderedPage p = (OrderedPage) super.getRadixObject();
//            AdsEditorPageDef page = p.findPage();
//            return page;
//        }
//    }
//    RadixObjectEditCookie c;
    @Override
    protected RadixObjectEditCookie createEditCookie() {
        return new RadixObjectEditCookie(new DefaultAgent<RadixObject>(null) {
            @Override
            public RadixObject getObject() {
                return EditorPagesOrderedPageNode.this.getRadixObject().findPage();
            }
        });
    }

    @Override
    public void addCustomActions(List<Action> actions) {
        super.addCustomActions(actions);
        actions.add(SystemAction.get(DefinitionRenameAction.class));
        actions.add(null);
        actions.add(SystemAction.get(InheritEditorPageAction.class));
        actions.add(SystemAction.get(OverrideInheritEditorPageByOrderAction.class));
        actions.add(null);
        actions.add(SystemAction.get(GoToSourceEditorPageAction.class));
        actions.add(SystemAction.get(FindUsagesAction.class));
        actions.add(null);
        actions.add(SystemAction.get(RadixObjectNodeDeleteAction.class));
    }

    @Override
    public void destroy() throws IOException {
        final OrderedPage op = getRadixObject();
        RadixMutex.writeAccess(new Runnable() {
            @Override
            public void run() {
                if (op.isInBranch() && op.canDelete()) {
                    op.removeFromOrder();
                }
            }
        });
    }

    @Override
    public boolean canDestroy() {
        return true;
    }

    @Override
    protected RadixObject getClipboardPresentation() {
        return super.getClipboardPresentation();//getRadixObject().findPage();
    }

    @Override
    protected Image annotateIcon(Image icon) {

        if (page != null && page.isPublished()) {
            return RadixObjectIcon.annotatePublished(icon);
        }

        return icon;
    }
}
