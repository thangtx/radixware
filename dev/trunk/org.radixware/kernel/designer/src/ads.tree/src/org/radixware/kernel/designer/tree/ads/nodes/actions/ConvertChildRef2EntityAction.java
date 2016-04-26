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
import org.radixware.kernel.common.defs.ads.explorerItems.AdsChildRefExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsEntityExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.ExplorerItems;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.general.utils.RadixMutex;
import org.radixware.kernel.designer.common.general.utils.RequestProcessor;
import org.radixware.kernel.designer.tree.ads.nodes.defs.exploreritems.AdsExplorerItemNode;


public class ConvertChildRef2EntityAction extends AdsDefinitionAction {

    public static class Cookie implements Node.Cookie {

        private final AdsChildRefExplorerItemDef explorerItem;
        private final AdsExplorerItemNode node;

        public Cookie(AdsExplorerItemNode node, AdsChildRefExplorerItemDef explorerItem) {
            this.explorerItem = explorerItem;
            this.node = node;
        }

        public boolean isEnabled() {
            if (node.isNodeInherited()) {
                return false;
            }
            return explorerItem != null && !explorerItem.isReadOnly();
        }

        public void perform() {
            if (explorerItem != null) {
                final AdsEntityExplorerItemDef entityItem = explorerItem.convertToEntityItem();
                if (entityItem == null) {
                    DialogUtils.messageError("Conversion is impossible");
                } else {
                    RequestProcessor.submit(new Runnable() {
                        @Override
                        public void run() {
                            RadixMutex.writeAccess(new Runnable() {
                                @Override
                                public void run() {
                                    final ExplorerItems.Children container = explorerItem.getOwnerExplorerItems().getChildren();
                                    if (entityItem.delete()) {
                                        container.getLocal().remove(explorerItem);

                                        container.getLocal().add(entityItem);
                                    }
                                }
                            });
                        }
                    });
                }
            }
        }
    }

    @Override
    protected boolean calcEnabled(Node[] activatedNodes) {
        if (activatedNodes.length == 0) {
            return false;
        }
        final Cookie c = activatedNodes[0].getCookie(Cookie.class);
        return c != null && c.isEnabled();
    }

    @Override
    protected Class<?>[] cookieClasses() {
        return new Class[]{Cookie.class};
    }

    @Override
    protected void performAction(Node[] activatedNodes) {
        if (activatedNodes.length == 0) {
            return;
        }
        final Cookie c = activatedNodes[0].getCookie(Cookie.class);
        if (c != null) {
            c.perform();
        }
    }

    @Override
    public String getName() {
        return "Convert to Entity Reference";
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }
}
