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

package org.radixware.kernel.designer.common.tree.actions;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CookieAction;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.designer.common.dialogs.RadixObjectRenamePanel;
import org.radixware.kernel.designer.common.general.utils.RadixMutex;


public class DefinitionRenameAction extends CookieAction {

    public static class RenameCookie implements Node.Cookie, Runnable {

        private final RadixObject radixObject;

        public RenameCookie(final RadixObject radixObject) {
            this.radixObject = radixObject;
        }

        @Override
        public void run() {
            try {
                SwingUtilities.invokeAndWait(new Runnable() {
                    @Override
                    public void run() {
                        RadixObjectRenamePanel.renameRadixObject(radixObject);
                    }
                });
            } catch (InterruptedException | InvocationTargetException ex) {
                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
            }

        }

        public void rename() {
            RadixMutex.writeAccess(this);
        }
    }

    @Override
    protected int mode() {
        return MODE_EXACTLY_ONE;
    }

    @Override
    protected Class<?>[] cookieClasses() {
        return new Class[]{RenameCookie.class};
    }

    @Override
    protected void performAction(Node[] activatedNodes) {
        for (int i = 0; i < activatedNodes.length; i++) {
            final RenameCookie cookie = activatedNodes[i].getCookie(RenameCookie.class);
            if (cookie != null) {
                cookie.rename();
            }
        }
    }

    @Override
    public String getName() {
        return NbBundle.getMessage(DefinitionRenameAction.class, "Action-Title-Rename");
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    public boolean asynchronous() {
        return true;
    }
}
