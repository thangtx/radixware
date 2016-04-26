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
import org.radixware.kernel.designer.common.general.utils.RadixMutex;
import org.radixware.kernel.reporteditor.common.UserExtensionManager;


public class ReloadReportsAction extends CookieAction {

    public static final class Cookie implements Node.Cookie {

        public Cookie() {
        }

        public void execute() {
            final Thread t = new Thread(new Runnable() {

                @Override
                public void run() {
                    RadixMutex.writeAccess(new Runnable() {

                        @Override
                        public void run() {
                            ProgressUtils.showProgressDialogAndRun(new Runnable() {

                                @Override
                                public void run() {
                                    UserExtensionManager.getInstance().getUserReports().reload();
                                }
                            }, "Reload...");


                        }
                    });
                }
            });
            t.setDaemon(true);
            t.start();
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
        final Cookie c = activatedNodes[0].getCookie(Cookie.class);
        if (c != null) {
            c.execute();
        }
    }

    @Override
    public String getName() {
        return "Reload";
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }
}
