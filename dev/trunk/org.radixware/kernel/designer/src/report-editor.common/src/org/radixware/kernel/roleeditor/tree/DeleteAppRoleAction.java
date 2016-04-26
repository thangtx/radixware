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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.netbeans.api.progress.ProgressUtils;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.actions.CookieAction;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProviderFactory;
import org.radixware.kernel.common.defs.ads.rights.AdsRoleDef;
import org.radixware.kernel.common.userreport.repository.role.AppRole;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.general.utils.RadixMutex;
import org.radixware.kernel.designer.common.general.utils.RequestProcessor;
import org.radixware.kernel.reporteditor.common.UserExtensionManager;


public class DeleteAppRoleAction extends CookieAction {

    public static class Cookie implements Node.Cookie {

        private final AppRole role;

        public Cookie(AppRole role) {
            this.role = role;
        }

        private void process() {
            RequestProcessor.submit(new Runnable() {
                @Override
                public void run() {
                    RadixMutex.writeAccess(new Runnable() {
                        @Override
                        public void run() {
                            if (!DialogUtils.messageConfirmation("Do you really want to delete application role " + role.getName() + "?")) {
                                return;
                            }

                            ProgressUtils.showProgressDialogAndRun(new Runnable() {
                                @Override
                                public void run() {
                                    List<AppRole> users = new LinkedList<>();

                                    List<AppRole> reports = UserExtensionManager.getInstance().getAppRoles().getRoles();
                                    final AdsRoleDef roleDef = role.findRoleDefinition();
                                    if (roleDef == null) {
                                        return;
                                    }
                                    for (AppRole r : reports) {

                                        AdsRoleDef report = r.findRoleDefinition();

                                        final List<Definition> deps = new ArrayList<>();

                                        if (report != null && report != roleDef && !report.isParentOf(roleDef)) {
                                            deps.clear();
                                            report.visit(new IVisitor() {
                                                @Override
                                                public void accept(RadixObject radixObject) {
                                                    radixObject.collectDirectDependences(deps);
                                                }
                                            }, VisitorProviderFactory.createDefaultVisitorProvider());


                                            if (deps.contains(roleDef)) {
                                                users.add(r);
                                            }
                                        }
                                    }
                                    if (!users.isEmpty()) {
                                        StringBuilder message = new StringBuilder("Application role  " + role.getName() + " is used by following application roles:\n");
                                        for (AppRole report : users) {
                                            message.append("- ").append(report.getName()).append("\n");
                                        }
                                        message.append("Continue?");
                                        if (!DialogUtils.messageConfirmation(message.toString())) {
                                            return;
                                        }

                                    }
                                    role.delete();


                                }
                            }, "Delete Report Parameter");

                        }
                    });
                }
            });
        }
    }

    @Override
    protected int mode() {
        return MODE_ALL;
    }

    @Override
    @SuppressWarnings("rawtypes")
    protected Class<?>[] cookieClasses() {
        return new Class[]{Cookie.class
        };
    }

    @Override
    protected void performAction(Node[] activatedNodes) {
        if (activatedNodes.length == 0) {
            return;
        }
        Cookie c;
        for (Node node : activatedNodes) {
            c = node.getCookie(Cookie.class);
            if (c != null) {
                c.process();
            }
        }
    }

    @Override
    public String getName() {
        return "Delete...";
    }

    @Override
    public HelpCtx getHelpCtx() {
        return null;
    }
}
