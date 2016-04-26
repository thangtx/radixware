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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;
import org.netbeans.api.progress.ProgressUtils;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.actions.CookieAction;
import org.radixware.kernel.common.userreport.repository.UserReport;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;


public class ImportReportVersionAction extends CookieAction {

    public static class Cookie implements Node.Cookie {

        private final UserReport report;

        public Cookie(final UserReport report) {
            this.report = report;
        }

        public void execute() {
            final JFileChooser fileChooser = new JFileChooser(ExportReportUtil.currentDirectory);

            final FileFilter filter = new FileFilter() {
                @Override
                public boolean accept(final File f) {
                    if (f.isDirectory()) {
                        return true;
                    } else {
                        return f.getName().endsWith(".xml");
                    }
                }

                @Override
                public String getDescription() {
                    return "User-Defined Report Exchange File (*.xml)";
                }
            };
            fileChooser.addChoosableFileFilter(filter);
            fileChooser.setFileFilter(filter);
            fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
            if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                final File file = fileChooser.getSelectedFile();
                ExportReportUtil.currentDirectory = file.getParentFile();
                ProgressUtils.showProgressDialogAndRun(new Runnable() {
                    @Override
                    public void run() {
                        FileInputStream in;
                        try {
                            in = new FileInputStream(file);
                        } catch (final FileNotFoundException ex) {
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    DialogUtils.messageError(ex);
                                }
                            });
                            return;
                        }
                        try {
                            report.importNewVersion(in);
                        } catch (final IOException e) {
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    DialogUtils.messageError(e);
                                }
                            });
                        } finally {
                            try {
                                in.close();
                            } catch (IOException ex) {
                                //ignore
                            }
                        }

                    }
                }, "Import Versions...");
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
    protected void performAction(final Node[] activatedNodes) {
        for (Node n : activatedNodes) {
            final Cookie c = n.getCookie(Cookie.class);
            if (c != null) {
                c.execute();
            }
        }
    }

    @Override
    public String getName() {
        return "Import Versions...";
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }
}
