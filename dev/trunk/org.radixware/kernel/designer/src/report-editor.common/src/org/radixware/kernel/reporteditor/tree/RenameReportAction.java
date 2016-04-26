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

import javax.swing.SwingUtilities;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.actions.CookieAction;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsUserReportClassDef;
import org.radixware.kernel.common.userreport.repository.UserReport;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.designer.common.dialogs.RadixObjectRenamePanel;


public class RenameReportAction extends CookieAction {

    public static class Cookie implements Node.Cookie {

        private final UserReport report;

        public Cookie(final UserReport module) {
            this.report = module;
        }

        public void execute() {

            if (report != null) {
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        final AdsClassDef clazz = AdsUserReportClassDef.Factory.newInstance();
                        clazz.setName(report.getName());
                        RadixObjectRenamePanel.renameRadixObject(clazz);
                        if (!Utils.equals(clazz.getName(), report.getName())) {
                            report.setName(clazz.getName());
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
        return new Class[]{RenameReportAction.Cookie.class};
    }

    @Override
    protected void performAction(final Node[] activatedNodes) {
        for (Node n : activatedNodes) {
            final RenameReportAction.Cookie c = n.getCookie(RenameReportAction.Cookie.class);
            if (c != null) {
                c.execute();
            }
        }
    }

    @Override
    public String getName() {
        return "Rename...";
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }
}
