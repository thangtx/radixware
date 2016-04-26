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

import java.io.IOException;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.actions.CookieAction;
import org.radixware.kernel.common.defs.RadixObject.EEditState;
import org.radixware.kernel.common.defs.dds.DdsModelDef;
import org.radixware.kernel.common.defs.dds.DdsModule;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.general.utils.RadixMutex;


public class DdsModuleReloadAction extends CookieAction {

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

            final ProgressHandle progressHandle = ProgressHandleFactory.createHandle("DDS Module Reloading");
            progressHandle.start();
            try {
                module.getModelManager().reloadModels();
            } catch (IOException cause) {
                final DefinitionError error = new DefinitionError("Unable to reload DDS module.", module, cause);
                DialogUtils.messageError(error);
            } finally {
                progressHandle.finish();
            }
        }

        public void reloadDdsModule() {
            String message = "Reload DDS module '" + module.getName() + "'?";
            final DdsModelDef modifiedModule = module.getModelManager().getModifiedModelIfLoaded();
            if (modifiedModule != null && modifiedModule.getEditState() != EEditState.NONE) {
                message += "\nALL UNSAVED CHANGES WILL BE LOST!";
            }
            if (!DialogUtils.messageConfirmation(message)) {
                return;
            }

            RadixMutex.writeAccess(this);
        }
    }

    @Override
    protected int mode() {
        return CookieAction.MODE_ALL;
    }

    @Override
    protected Class<?>[] cookieClasses() {
        return new Class[]{Cookie.class};
    }

    @Override
    protected void performAction(Node[] nodes) {
        for (Node node : nodes) {
            final Cookie cookie = node.getCookie(DdsModuleReloadAction.Cookie.class);
            if (cookie != null) {
                cookie.reloadDdsModule();
            }
        }
    }

    @Override
    public String getName() {
        return "Reload"; // TODO: translate
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return true; // to prevent deadlock because tree can be recreated
    }
}

