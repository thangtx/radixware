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
import org.openide.util.actions.CookieAction;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.designer.common.dialogs.callhierarchy.CallHierarchyView;


public class CallHierarchyAction extends CookieAction {

    public static final class CallHierarchyCookie implements Node.Cookie {

        public CallHierarchyCookie() {
            super();
        }
    }

    @Override
    protected Class<?>[] cookieClasses() {
        return new Class[]{ CallHierarchyCookie.class };
    }

    @Override
    protected void performAction(Node[] activatedNodes) {
        if (activatedNodes.length > 0) {
            final Node node = activatedNodes[0];
            if (node != null) {
                final AdsMethodDef method = node.getLookup().lookup(AdsMethodDef.class);
                if (method != null) {
                    SwingUtilities.invokeLater(new Runnable() {

                        @Override
                        public void run() {
                            CallHierarchyView.openTopComponent(method);
                        }
                    });
                }
            }

        }
    }

    @Override
    public String getName() {
        return "Call Hierarchy";
    }

    @Override
    public HelpCtx getHelpCtx() {
        return null;
    }

    @Override
    protected int mode() {
        return MODE_EXACTLY_ONE;
    }
}
