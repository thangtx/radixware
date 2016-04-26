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

package org.radixware.kernel.designer.common.dialogs.dependency;

import javax.swing.SwingUtilities;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.actions.CookieAction;
import org.radixware.kernel.common.defs.Module;


public class ModuleDependenciesAction extends CookieAction {

    public static final class ModuleDependenciesCookie implements Node.Cookie {

        public ModuleDependenciesCookie() {
            super();
        }
    }

    @Override
    protected Class<?>[] cookieClasses() {
        return new Class[]{ ModuleDependenciesCookie.class };
    }

    @Override
    protected void performAction(Node[] activatedNodes) {
        if (activatedNodes.length > 0) {
            final Node node = activatedNodes[0];
            if (node != null) {
                final Module module = node.getLookup().lookup(Module.class);
                if (module != null) {
                    SwingUtilities.invokeLater(new Runnable() {

                        @Override
                        public void run() {
                            ModuleDependencies.openTopComponent(module);
                        }
                    });
                }
            }
        }
    }

    @Override
    public String getName() {
        return "Dependencies";
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
