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

package org.radixware.kernel.msdleditor.tree;

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
import org.radixware.kernel.common.defs.ads.msdl.AdsMsdlSchemeDef;
import org.radixware.kernel.common.userreport.repository.msdl.MsdlScheme;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.general.utils.RadixMutex;
import org.radixware.kernel.designer.common.general.utils.RequestProcessor;
import org.radixware.kernel.reporteditor.common.UserExtensionManager;


public class DeleteMsdlSchemesAction extends CookieAction {

    public static class Cookie implements Node.Cookie {

        private final MsdlScheme msdlScheme;

        public Cookie(MsdlScheme msdlSchemes) {
            this.msdlScheme = msdlSchemes;
        }

        private void process() {
            RequestProcessor.submit(new Runnable() {
                @Override
                public void run() {
                    RadixMutex.writeAccess(new Runnable() {
                        @Override
                        public void run() {
                            if (!DialogUtils.messageConfirmation("Do you really want to delete msdlScheme " + msdlScheme.getName() + "?")) {
                                return;
                            }

                            ProgressUtils.showProgressDialogAndRun(new Runnable() {
                                @Override
                                public void run() {
                                    List<MsdlScheme> users = new LinkedList<>();

                                    List<MsdlScheme> msdlSchemes =  UserExtensionManager.getInstance().getMsdlSchemes().getMsdlSchemes();
                                    final AdsMsdlSchemeDef msdlSchemeDef = msdlScheme.findMsdlSchemeDefinition();
                                    if (msdlSchemeDef == null) {
                                        return;
                                    }
                                    for (MsdlScheme r : msdlSchemes) {

                                        AdsMsdlSchemeDef report = r.findMsdlSchemeDefinition();

                                        final List<Definition> deps = new ArrayList<>();

                                        if (report != null && report != msdlSchemeDef && !report.isParentOf(msdlSchemeDef)) {
                                            deps.clear();
                                            report.visit(new IVisitor() {
                                                @Override
                                                public void accept(RadixObject radixObject) {
                                                    radixObject.collectDirectDependences(deps);
                                                }
                                            }, VisitorProviderFactory.createDefaultVisitorProvider());


                                            if (deps.contains(msdlSchemeDef)) {
                                                users.add(r);
                                            }
                                        }
                                    }
                                    if (!users.isEmpty()) {
                                        StringBuilder message = new StringBuilder("MsdlScheme  " + msdlScheme.getName() + " is used by following msdlSchemes:\n");
                                        for (MsdlScheme report : users) {
                                            message.append("- ").append(report.getName()).append("\n");
                                        }
                                        message.append("Continue?");
                                        if (!DialogUtils.messageConfirmation(message.toString())) {
                                            return;
                                        }

                                    }
                                    msdlScheme.delete();


                                }
                            }, "Delete msdlScheme");

                        }
                    });
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
        return new Class[]{Cookie.class
        };
    }

    @Override
    protected void performAction(Node[] activatedNodes) {
        if (activatedNodes.length == 0) {
            return;
        }
        final Node node = activatedNodes[0];
        Cookie c = node.getCookie(Cookie.class);
        if (c != null) {
            c.process();
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
