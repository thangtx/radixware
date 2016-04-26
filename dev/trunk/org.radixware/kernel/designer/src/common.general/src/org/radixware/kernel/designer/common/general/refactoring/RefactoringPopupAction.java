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

package org.radixware.kernel.designer.common.general.refactoring;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.actions.CookieAction;
import org.openide.util.actions.SystemAction;


public class RefactoringPopupAction extends CookieAction {

    @Override
    protected int mode() {
        return MODE_EXACTLY_ONE;
    }

    @Override
    protected Class<?>[] cookieClasses() {
        return new Class[]{RefactoringProvider.class};
    }

    @Override
    protected void performAction(Node[] nodes) {
        //ignore
    }

    @Override
    public String getName() {
        return "Refactor...";
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    public JMenuItem getPopupPresenter() {
        Node[] nodes = getActivatedNodes();
        if (nodes.length == 1) {
            Node node = nodes[0];
            if (node != null) {
                RefactoringProvider provider = node.getLookup().lookup(RefactoringProvider.class);
                if (provider != null) {
                    Class<? extends SystemAction>[] actions = provider.allowedActions();
                    if (actions != null && actions.length > 0) {
                        JMenu menu = new JMenu(getName());
                        for (Class<? extends SystemAction> a : actions) {
                            if (a == null) {
                                menu.addSeparator();
                            } else {
                                menu.add(new JMenuItem(SystemAction.get(a)));
                            }
                        }
                        return menu;
                    }
                }
            }
        }
        return super.getPopupPresenter();
    }
}
