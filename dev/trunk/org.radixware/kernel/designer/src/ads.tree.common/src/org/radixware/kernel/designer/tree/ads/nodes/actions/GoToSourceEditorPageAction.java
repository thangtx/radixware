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

import javax.swing.SwingUtilities;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPageDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.EditorPages.OrderedPage;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.general.nodes.NodesManager;


public class GoToSourceEditorPageAction extends AdsDefinitionAction {

    public static class GoToSourceEditorPageCookie implements Node.Cookie {

        private final OrderedPage page;

        public GoToSourceEditorPageCookie(OrderedPage page) {
            this.page = page;
        }

        private boolean isAllowed() {
            return page != null && page.findPage() != null && !page.isOwnPage();
        }

        private void go() {
            final AdsEditorPageDef p = page.findPage();
            if (p != null) {
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        NodesManager.selectInProjects(p);
                    }
                });

            } else {
                DialogUtils.messageError("Original page not found");
            }
        }
    }

    @Override
    protected boolean calcEnabled(Node[] nodes) {
        for (Node n : nodes) {
            GoToSourceEditorPageCookie c = n.getCookie(GoToSourceEditorPageCookie.class);
            if (c == null || !c.isAllowed()) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected int mode() {
        return MODE_EXACTLY_ONE;
    }

    @Override
    protected Class<?>[] cookieClasses() {
        return new Class[]{GoToSourceEditorPageCookie.class};
    }

    @Override
    protected void performAction(Node[] activatedNodes) {
        for (Node n : getActivatedNodes()) {
            GoToSourceEditorPageCookie c = n.getCookie(GoToSourceEditorPageCookie.class);
            if (c == null || c.isAllowed()) {
                c.go();
            }
        }
    }

    @Override
    public String getName() {
        return "Go to Original Page";
    }

   
}
