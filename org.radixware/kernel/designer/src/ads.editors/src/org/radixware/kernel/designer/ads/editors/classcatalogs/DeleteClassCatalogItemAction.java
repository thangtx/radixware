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

package org.radixware.kernel.designer.ads.editors.classcatalogs;

import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CookieAction;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsClassCatalogDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsClassCatalogDef.ClassReference;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsClassCatalogDef.Topic;


public class DeleteClassCatalogItemAction extends CookieAction {

    static class Cookie implements Node.Cookie {

        private ClassCatalogItemNode node;

        Cookie(ClassCatalogItemNode node) {
            this.node = node;
        }

        public void delete() {
            if (node.getParentNode() != null) {
                ItemChildren children = (ItemChildren) node.getParentNode().getChildren();
                RadixObject rdx = node.getRadixObject();
                if (rdx instanceof Topic) {
                    if (node.getNodesCount() == 0) {
                        if (rdx.delete()) {
                            children.removeNodes(new Node[]{node});
                        }
                    }
                } else {
                    ClassReference asRef = (ClassReference) node.getRadixObject();
                    AdsClassCatalogDef ownerClassCatalog = asRef.getOwnerClassCatalog();
                    if (ownerClassCatalog instanceof AdsClassCatalogDef.Virtual) {
                        ((AdsClassCatalogDef.Virtual) ownerClassCatalog).undefClassReference();
                        children.removeNodes(new Node[]{node});
                    } else {
                        if (rdx.delete()) {
                            children.removeNodes(new Node[]{node});
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void performAction(Node[] activatedNodes) {
        for (int i = 0; i < activatedNodes.length; i++) {
            Cookie cc = activatedNodes[i].getCookie(Cookie.class);

            if (cc != null) {
                cc.delete();
            }
        }
    }

    @Override
    protected int mode() {
        return MODE_EXACTLY_ONE;
    }

    @Override
    protected Class<?>[] cookieClasses() {
        return new Class[]{DeleteClassCatalogItemAction.Cookie.class};
    }

    @Override
    public String getName() {
        return NbBundle.getMessage(DeleteClassCatalogItemAction.class, "DeleteMenuTip");
    }

    @Override
    public HelpCtx getHelpCtx() {
        return null;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }
}
