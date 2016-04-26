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

package org.radixware.kernel.designer.uds.tree.actions;

import java.io.File;
import org.openide.filesystems.FileUtil;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.actions.CookieAction;
import org.radixware.kernel.common.defs.uds.module.UdsModule;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;


public class CreateFileStorageAction extends CookieAction {

    public static class Cookie implements Node.Cookie {

        private final UdsModule module;

        public Cookie(UdsModule module) {
            this.module = module;
        }

        private void exec() {
            File dir = module.getEtcDir();
            if (dir.exists() && !dir.isDirectory()) {
                DialogUtils.messageError("File " + dir.getPath() + " is already exists and it is not a directory. Remove this file before create UDS File Storage");
                return;
            }
            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    DialogUtils.messageError("Can not create directory " + dir.getPath());
                } else {
                }
            } else {
                DialogUtils.messageError("Directory " + dir.getPath() + " is already exists");
            }
            FileUtil.refreshFor(module.getDirectory());
        }

        private boolean isEnabled() {
            return !module.getEtcDir().isDirectory();
        }
    }

    @Override
    protected int mode() {
        return MODE_EXACTLY_ONE;
    }

    @Override
    protected Class<?>[] cookieClasses() {
        return new Class[]{Cookie.class};
    }

    @Override
    public boolean isEnabled() {
        if (super.isEnabled()) {
            return isEnabled(getActivatedNodes());
        } else {
            return false;
        }
    }

    private boolean isEnabled(Node[] nodes) {

        if (nodes.length == 1) {
            Cookie c = nodes[0].getCookie(Cookie.class);
            if (c != null) {
                return c.isEnabled();
            } else {
                return false;
            }
        } else {
            return false;
        }

    }

    @Override
    protected boolean enable(Node[] activatedNodes) {
        if (super.enable(activatedNodes)) {
            return isEnabled(activatedNodes);
        } else {
            return false;
        }
    }

    @Override
    protected void performAction(Node[] nodes) {
        Cookie c = nodes[0].getCookie(Cookie.class);
        if (c != null) {
            c.exec();
        }
    }

    @Override
    public String getName() {
        return "Create File Storage";
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }
}
