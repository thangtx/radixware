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

package org.radixware.kernel.roleeditor.tree;

import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.actions.CookieAction;
import org.radixware.kernel.designer.common.general.nodes.NodesManager;
import org.radixware.kernel.reporteditor.common.UserExtensionManager;
import org.radixware.kernel.common.defs.ads.rights.AdsRoleDef;
import org.radixware.kernel.common.userreport.repository.role.AppRole;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.general.utils.RequestProcessor;


public class CreateAppRoleAction extends CookieAction {

    public static class Cookie implements Node.Cookie {

        private void create() {
            RequestProcessor.submit(new Runnable() {
                @Override
                public void run() {
                    final AppRole role = UserExtensionManager.getInstance().getAppRoles().create();
                    if (role != null) {

                        final AdsRoleDef roleDef = role.findRoleDefinition();
                        if (roleDef != null) {
                            NodesManager.selectInProjects(roleDef);
                            DialogUtils.goToObject(roleDef);
                        }
                    }
                }
            });
        }
    }

    @Override
    protected int mode() {
        return MODE_EXACTLY_ONE;
    }

    @Override
    @SuppressWarnings("rawtypes")
    protected Class<?>[] cookieClasses() {
        return new Class[]{Cookie.class};
    }

    @Override
    protected void performAction(final Node[] activatedNodes) {
        if (activatedNodes.length == 0) {
            return;
        }
        final Node node = activatedNodes[0];
        final Cookie c = node.getCookie(Cookie.class);
        if (c != null) {
            c.create();
        }
    }

    @Override
    public String getName() {
        return "Create new role...";
    }

    @Override
    public HelpCtx getHelpCtx() {
        return null;
    }
}
