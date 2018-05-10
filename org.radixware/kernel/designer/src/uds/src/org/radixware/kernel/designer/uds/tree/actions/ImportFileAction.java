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
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.Mutex;
import org.openide.util.MutexException;
import org.openide.util.actions.CookieAction;
import static org.openide.util.actions.CookieAction.MODE_EXACTLY_ONE;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.general.utils.RadixMutex;

public class ImportFileAction extends CookieAction {

    public static class Cookie implements Node.Cookie {

        private final RadixObjects container;

        public Cookie(RadixObjects container) {
            this.container = container;
        }

        private void importFile() {
            final File parentFile = container.getFile();
            final File[] files = ActionUtil.openFiles("Import UDS file");
            if (files != null) {
                try {
                    RadixMutex.writeAccess(new Mutex.ExceptionAction<Integer>() {
                        @Override
                        public Integer run() throws Exception {
                            final int[] count = {0};
                            for (File file : files) {
                                File newFile = new File(parentFile, file.getName());
                                if (newFile.exists()) {
                                    if (!DialogUtils.messageConfirmation("File " + newFile.getAbsolutePath() + " is already exists. Overwite?")) {
                                        continue;
                                    }
                                }
                                FileUtils.copyDirectory(file, newFile);
                            }
                            return count[0];
                        }
                    });
                } catch (MutexException ex) {
                    DialogUtils.messageError(ex);
                }
            }
        }

        public RadixObjects getContainer() {
            return container;
        }

    }

    @Override
    protected int mode() {
        return MODE_EXACTLY_ONE;
    }

    @Override
    protected Class<?>[] cookieClasses() {
        return new Class[]{ImportFileAction.Cookie.class};
    }

    @Override
    protected void performAction(Node[] nodes) {
        final ImportFileAction.Cookie c = nodes[0].getCookie(ImportFileAction.Cookie.class);
        if (c != null) {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    c.importFile();
                }
            });
            t.setDaemon(true);
            t.start();

        }
    }

    @Override
    public String getName() {
        return "Import Files...";
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean enable(final Node[] activatedNodes) {
        if (super.enable(activatedNodes)) {
            for (Node node : activatedNodes) {
                final Cookie c = node.getCookie(Cookie.class);
                RadixObjects obj;
                if (c == null || c.getClass() != cookieClasses()[0] || ((obj = c.getContainer()) == null) || obj.isReadOnly()) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

}
