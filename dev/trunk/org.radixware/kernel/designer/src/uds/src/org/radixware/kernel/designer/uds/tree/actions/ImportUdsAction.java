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
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JFileChooser;
import org.netbeans.api.progress.ProgressUtils;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.HelpCtx;
import org.openide.util.Mutex;
import org.openide.util.MutexException;
import org.openide.util.actions.CookieAction;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.uds.module.UdsModule;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.general.utils.RadixMutex;


public class ImportUdsAction extends CookieAction {

    public static class Cookie implements Node.Cookie {

        private final UdsModule module;

        public Cookie(UdsModule module) {
            this.module = module;
        }

        private void exec() {
            final File file = ActionUtil.chooseFile(JFileChooser.OPEN_DIALOG, "Import User Definitions");
            if (file != null) {
                try {
                    final List<Definition> duplicates = new LinkedList<Definition>();
                    int count = 0;
                    try {
                        count = RadixMutex.writeAccess(new Mutex.ExceptionAction<Integer>() {
                            @Override
                            public Integer run() throws Exception {
                                final int[] count = {0};
                                final boolean removePrev = DialogUtils.messageConfirmation("Remove previously added definitions?");
                                ProgressUtils.showProgressDialogAndRun(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            count[0] = module.importUserDefinitions(file, removePrev, duplicates);
                                        } catch (IOException ex) {
                                            return;
                                        }
                                    }
                                }, "Importing definitions...");
                                return count[0];
                            }
                        });
                    } catch (MutexException ex) {
                        DialogUtils.messageError(ex);
                    }

                    if (count > 0) {
                        module.getDependences().actualize();
                        module.saveDescription();

                        StringBuilder sb = new StringBuilder();

                        sb.append(String.valueOf(count)).append(" definitions were imported");

                        if (!duplicates.isEmpty()) {
                            int c = 0;
                            sb.append("\nFollowing definitions were not replaced:\n");
                            for (Definition d : duplicates) {
                                sb.append(d.getName()).append("\n");
                                c++;
                                if (c == 11) {
                                    break;
                                }
                            }
                            if (duplicates.size() > 10) {
                                sb.append(duplicates.size() - 10).append(" more...");
                            }
                        }

                        DialogUtils.messageInformation(sb.toString());
                    } else {
                        StringBuilder sb = new StringBuilder();

                        sb.append("No definitions were imported");

                        if (!duplicates.isEmpty()) {
                            int c = 0;
                            sb.append("\nFollowing definitions were not replaced:\n");
                            for (Definition d : duplicates) {
                                sb.append(d.getName()).append("\n");
                                c++;
                                if (c == 11) {
                                    break;
                                }
                            }
                            if (duplicates.size() > 10) {
                                sb.append(duplicates.size() - 10).append(" more...");
                            }
                        }

                        DialogUtils.messageInformation(sb.toString());
                    }
                } catch (IOException ex) {
                    DialogUtils.messageError(ex);
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
        return new Class[]{Cookie.class};
    }

    @Override
    protected void performAction(Node[] nodes) {
        final Cookie c = nodes[0].getCookie(Cookie.class);
        if (c != null) {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {

                    c.exec();

                }
            });
            t.setDaemon(true);
            t.start();

        }
    }

    @Override
    public String getName() {
        return "Import User Definitions...";
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }
}
