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

package org.radixware.kernel.designer.tree.actions.dds;

import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.actions.CookieAction;
import org.radixware.kernel.common.defs.dds.DdsModule;
import org.radixware.kernel.common.rights.SystemTablesBuilder;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.general.utils.RadixMutex;
import org.radixware.kernel.designer.common.general.nodes.NodesManager;


public class DdsModuleActualizeRightSystemObjectsAction extends CookieAction {

    public static class Cookie implements Node.Cookie, Runnable {

        private DdsModule module;

        public Cookie(DdsModule module) {
            this.module = module;
        }

        @Override
        public void run() {
            if (!module.isInBranch()) {
                return; // was deleted
            }

            final ProgressHandle progressHandle = ProgressHandleFactory.createHandle("Right System Objects Actualizing");
            progressHandle.start();
            try {
                SystemTablesBuilder.refresh(module);
                NodesManager.updateOpenedEditors();
            } finally {
                progressHandle.finish();
            }
        }

        public void actualizeRightSystemObjects() {
            if (!DialogUtils.messageConfirmation("Actualize right system objects?")) {
                return;
            }

            RadixMutex.writeAccess(this);
        }
    }

    @Override
    protected int mode() {
        return CookieAction.MODE_EXACTLY_ONE;
    }

    @Override
    protected Class<?>[] cookieClasses() {
        return new Class[]{Cookie.class};
    }

    @Override
    protected void performAction(Node[] nodes) {
        for (int i = 0; i < nodes.length; i++) {
            Cookie cookie = nodes[i].getCookie(DdsModuleActualizeRightSystemObjectsAction.Cookie.class);
            if (cookie != null) {
                cookie.actualizeRightSystemObjects();
            }
        }
    }

    @Override
    public String getName() {
        return "Actualize Right System Objects";
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }
}