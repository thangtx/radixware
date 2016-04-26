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

import org.openide.nodes.Node;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPageDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.EditorPages.OrderedPage;
import org.radixware.kernel.common.exceptions.RadixObjectError;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;


public class OverrideInheritEditorPageByOrderAction extends AdsDefinitionAction {

    public static final class OverrideEditorPageByOrderCookie implements Node.Cookie {

        private OrderedPage page;

        public OverrideEditorPageByOrderCookie(OrderedPage page) {
            this.page = page;
        }

        private boolean isAllowed() {
            if (page.isReadOnly()) {
                return false;
            }
            AdsEditorPageDef pageDef = page.findPage();
            if (pageDef == null) {
                return false;
            }
            if (pageDef.isFinal()) {
                return false;
            }
            if (page.isOwnPage()) {
                if (page.isOwrPage()) {
                    return true;
                }
            } else {
                return true;
            }
            return false;
        }

        private void override() {
            //if (!page.isOwnPage()) {
            try {
                page.overrideOrInherit();
            } catch (RadixObjectError e) {
                DialogUtils.messageError(new RadixObjectError("Problem while overriding/deoverriding " + page.getName(), e));
            }
            //}
        }
    }

    @Override
    protected boolean calcEnabled(Node[] nodes) {
        for (Node node : nodes) {
            OverrideEditorPageByOrderCookie c = node.getCookie(OverrideEditorPageByOrderCookie.class);
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
        return new Class[]{OverrideEditorPageByOrderCookie.class};
    }

    @Override
    protected void performAction(Node[] activatedNodes) {
        for (Node node : activatedNodes) {
            OverrideEditorPageByOrderCookie c = node.getCookie(OverrideEditorPageByOrderCookie.class);
            if (c != null) {
                c.override();
            }
        }
    }

    @Override
    public String getName() {
        Node[] nodes = getActivatedNodes();
        for (Node node : nodes) {
            OverrideEditorPageByOrderCookie c = node.getCookie(OverrideEditorPageByOrderCookie.class);
            if (c != null) {
                if (c.page.isOwnPage()) {
                    if (c.page.isOwrPage()) {
                        return "Inherit This Page";
                    } else {
                        return "Override/Inherit";
                    }
                } else {
                    return "Override This Page";
                }
            }
        }
        return "Override/Inherit";
    }

   
}
