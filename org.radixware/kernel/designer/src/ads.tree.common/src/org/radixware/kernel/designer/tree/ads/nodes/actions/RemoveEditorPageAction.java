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

package org.radixware.kernel.designer.tree.ads.nodes.actions;

import java.util.Collection;
import org.openide.nodes.Node;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPageDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.EditorPages.OrderedPage;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;


public class RemoveEditorPageAction extends AdsDefinitionAction {

    public static final class RemoveEditorPageCookie extends AdsDefinitionActionCookie {

        OrderedPage page;

        public RemoveEditorPageCookie(OrderedPage page) {
            this.page = page;
        }

        boolean isAllowed() {
            return !page.isReadOnly();
        }

        void remove() {
            boolean delete = false;

            if (!page.isOwnPage()) {
                delete = DialogUtils.messageConfirmation("Delete editor page reference ?");
            } else {
                AdsEditorPageDef p = page.findPage();
                if (p == null) {
                    delete = DialogUtils.messageConfirmation("Delete editor page reference?");
                } else {
                    Collection<Id> ids = p.getOwnerEditorPages().getOrder().getOrderedPageIds();
                    int count = 0;
                    for (Id id : ids) {
                        if (id == page.getPageId()) {
                            count++;
                        }
                    }
                    if (count > 1) {
                        delete = DialogUtils.messageConfirmation("Delete editor page reference (one of " + String.valueOf(count) + ")?");
                    } else {
                        delete = DialogUtils.messageConfirmation("Delete editor page " + p.getQualifiedName() + "?");
                    }
                }
            }
            if (delete) {
                page.removeFromOrder();
            }
        }

        @Override
        protected RadixObject getDefinition() {
            return page;
        }
    }

    @Override
    protected boolean calcEnabled(Node[] nodes) {
        for (Node n : nodes) {
            RemoveEditorPageCookie c = n.getCookie(RemoveEditorPageCookie.class);
            if (c == null || !c.isAllowed()) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected int mode() {
        return MODE_ALL;
    }

    @Override
    protected Class<?>[] cookieClasses() {
        return new Class[]{RemoveEditorPageCookie.class};
    }

    @Override
    protected void performAction(Node[] activatedNodes) {
        for (Node n : activatedNodes) {
            RemoveEditorPageCookie c = n.getCookie(RemoveEditorPageCookie.class);
            if (c != null) {
                c.remove();
            }
        }
    }

    @Override
    public String getName() {
        return "Delete";
    }

    
    @Override
    protected boolean asynchronous() {
        return true;
    }
}
