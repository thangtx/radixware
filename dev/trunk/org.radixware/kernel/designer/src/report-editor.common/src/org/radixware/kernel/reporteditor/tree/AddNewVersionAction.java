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

import org.netbeans.api.progress.ProgressUtils;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.actions.CookieAction;
import org.radixware.kernel.common.userreport.repository.UserReport;


public class AddNewVersionAction extends CookieAction {

    public static final class Cookie implements Node.Cookie {

        private final UserReport report;

        public Cookie(final UserReport report) {
            this.report = report;
        }

        private void process() {
            ProgressUtils.showProgressDialogAndRun(new Runnable() {

                @Override
                public void run() {
                    report.getVersions().addNewVersion(report.getVersions().getCurrent());
                }
            }, "Create New Version");
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
        for (Node n : activatedNodes) {
            final Cookie c = n.getCookie(Cookie.class);
            if (c != null) {
                c.process();
                return;
            }
        }
    }

    @Override
    public String getName() {
        return "Add New Version";
    }

    @Override
    public HelpCtx getHelpCtx() {
        return null;
    }
}
