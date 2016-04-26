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
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.actions.CookieAction;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.common.userreport.repository.ReportsModule;
import org.radixware.kernel.common.userreport.repository.UserReport;
import org.radixware.kernel.common.userreport.repository.UserReport.ReportVersion;
import org.radixware.kernel.reporteditor.common.UserExtensionManager;

public class CompileAllReportsAction extends CookieAction {

    public static class Cookie implements Node.Cookie {

        public void perform() {
            for (ReportsModule module : UserExtensionManager.getInstance().getUserReports().getReportModules()) {
                for (UserReport rep : UserExtensionManager.getInstance().getUserReports().listReports(module.getId())) {
                    final ReportVersion version = rep.getVersions().getCurrent();
                    final List<RadixObject> reports = new LinkedList<>();
                    if (version != null) {
                        final AdsReportClassDef report = version.findReportDefinition();
                        if (report != null) {
                            reports.add(report);
                        }
                    }
                    UserExtensionManager.getInstance().compileOnSave(reports.toArray(new RadixObject[reports.size()]), false);
                }
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
                c.perform();
            }
        }
    }

    @Override
    public String getName() {
        return "Compile All";
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }
}
