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

import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.actions.CookieAction;
import org.radixware.kernel.common.repository.dds.DdsScriptsDir;
import org.radixware.kernel.designer.common.general.utils.RadixMutex;


public class CreateDdsScriptAction extends CookieAction {

    public static class Cookie extends CreateDdsScriptWorker implements Node.Cookie {

        private DdsScriptsDir scriptsDir;

        public Cookie(DdsScriptsDir scriptsDir) {
            super(scriptsDir, false);
        }

        public void createNewScript() {
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
            final Cookie cookie = nodes[i].getCookie(CreateDdsScriptAction.Cookie.class);
            if (cookie != null) {
                cookie.createNewScript();
            }
        }
    }

    @Override
    public String getName() {
        return "Create New Script";
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return true;

    }
}
