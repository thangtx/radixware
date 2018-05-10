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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.actions.CookieAction;
import static org.openide.util.actions.CookieAction.MODE_EXACTLY_ONE;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.uds.UserReportUtils;


public class ExportUserReportAction extends CookieAction {

    public static class Cookie implements Node.Cookie {

        private AdsReportClassDef uf;

        public Cookie(AdsReportClassDef uf) {
            this.uf = uf;
        }

        public void exec() {
            File file = ActionUtil.chooseXmlFile(JFileChooser.SAVE_DIALOG, "Export User-Defined Report Sample");
            if (file != null) {
                if (!file.getName().endsWith(".xml")) {
                    file = new File(file.getAbsolutePath() + ".xml");
                }
                if (file.exists()) {
                    if (!DialogUtils.messageConfirmation("File " + file.getPath() + " is already exists. Overwrite?")) {
                        return;
                    }
                }
                OutputStream out = null;
                try {
                    out = new FileOutputStream(file);
                    UserReportUtils.exportReport(uf, out);
                } catch (IOException ex) {
                    DialogUtils.messageError(ex);
                } finally {
                    if (out != null) {
                        try {
                            out.close();
                        } catch (IOException ex) {
                            Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                        }
                    }
                }
            }
        }
    }

    @Override
    protected int mode() {
        return MODE_EXACTLY_ONE;
    }

    @Override
    protected Class<?>[] cookieClasses() {
        return new Class[]{ExportUserReportAction.Cookie.class};
    }

    @Override
    protected void performAction(Node[] nodes) {
        ExportUserReportAction.Cookie c = nodes[0].getCookie(ExportUserReportAction.Cookie.class);
        if (c != null) {
            c.exec();
        }
    }

    @Override
    public String getName() {
        return "Export...";
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }
}
