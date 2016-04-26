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

package org.radixware.kernel.reporteditor.tree;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import javax.swing.SwingUtilities;
import org.netbeans.api.progress.ProgressUtils;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.actions.CookieAction;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.userreport.repository.UserReport;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;


public class DeleteReportAction extends CookieAction {

    public static class Cookie implements Node.Cookie {

        private UserReport report;

        public Cookie(UserReport module) {
            this.report = module;
        }

        public void execute() {

            if (report != null) {
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        if (DialogUtils.messageConfirmation("Do you want to delete report " + report.getName() + " with all versions and publications?")) {


                            final List<String> deletedPubs = new LinkedList<>();
                            final CountDownLatch lock = new CountDownLatch(1);
                            ProgressUtils.showProgressDialogAndRun(new Runnable() {

                                @Override
                                public void run() {

                                    try {
                                        Set<UserReport> dependences = report.collectDependences();
                                        if (!dependences.isEmpty()) {
                                            StringBuilder message = new StringBuilder();
                                            message.append("Follwing reports contains references to ").append(report.getName()).append(":\n");
                                            int c = 1;
                                            for (UserReport r : dependences) {
                                                message.append(" - ").append(r.getName()).append("\n");
                                                c++;
                                                if (c >= 10 && dependences.size() > 10) {
                                                    message.append("...");
                                                    break;
                                                }
                                            }
                                            message.append("Operation may invalidate listed reports. Continue?");
                                            if (!DialogUtils.messageConfirmation(message.toString())) {
                                                return;
                                            }
                                        }
                                        report.delete(deletedPubs);
                                    } catch (RadixError e) {
                                        DialogUtils.messageError(e);
                                    } finally {
                                        lock.countDown();
                                    }
                                }
                            }, "Delete report " + report.getName());
                            try {
                                lock.await();
                            } catch (InterruptedException ex) {
                            }
                            if (deletedPubs.size() > 0) {
                                DialogUtils.messageInformation(deletedPubs.size() + " publications of report " + report.getName() + " were deleted");
                            }
                        }
                    }
                });
            }

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
    protected void performAction(Node[] activatedNodes) {
        for (Node n : activatedNodes) {
            Cookie c = n.getCookie(Cookie.class);
            if (c != null) {
                c.execute();
            }
        }
    }

    @Override
    public String getName() {
        return "Delete Report...";
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }
}
