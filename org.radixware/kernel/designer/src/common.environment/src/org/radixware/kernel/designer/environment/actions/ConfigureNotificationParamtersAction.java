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

package org.radixware.kernel.designer.environment.actions;

import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.actions.CookieAction;
import org.radixware.kernel.common.repository.Branch;


public class ConfigureNotificationParamtersAction extends CookieAction {

    public static class ConfigureNotificationParamtersCookie implements Node.Cookie {

        private final Branch branch;

        public ConfigureNotificationParamtersCookie(Branch branch) {
            this.branch = branch;
        }
    }

    @Override
    protected int mode() {
        return MODE_EXACTLY_ONE;
    }

    @Override
    protected Class<?>[] cookieClasses() {
        return new Class[]{ConfigureNotificationParamtersCookie.class};
    }

    @Override
    protected void performAction(Node[] nodes) {
//        ConfigureNotificationParamtersCookie c = nodes[0].getCookie(ConfigureNotificationParamtersCookie.class);
//        if (c != null) {
//            try {
//                SVNRepository repository;
//                try {
//                    repository = SVN.findRepository(c.branch.getDirectory(), SVN.SVNPreferences.getUserName(), SVN.SVNPreferences.getAuthType(), SVN.SVNPreferences.getSSHKeyFilePath());
//                } catch (AuthenticationCancelledException ex) {
//                    DialogUtils.messageError("Authentification cancelled by user");
//                    return;
//                }                
//                NotificationConfig config = NotificationConfig.Factory.loadFrom(repository, ReleaseUtils.getBranchPath(c.branch, repository));
//                if (config != null) {
//                    NotificationConfigPanel panel = new NotificationConfigPanel();
//                    panel.open(config);
//                    ModalDisplayer displayer = new ModalDisplayer(panel, "Notification Parameters");
//                    if (displayer.showModal()) {
//                        try {
//                            if (!config.saveToRepository(repository)) {
//                                DialogUtils.messageError("Unable to save notification config to svn repository");
//                            }
//                        } catch (Exception e) {
//                            DialogUtils.messageError(new RadixError("Unable to save notification config to svn repository", e));
//                        }
//                    }
//                } else {
//                    DialogUtils.messageError("Unable to load notification config from svn repository");
//                }
//            } catch (SVNException ex) {
//                DialogUtils.messageError(new RadixError("Unable to connect to svn repository", ex));
//            }
//        }
    }

    @Override
    public String getName() {
        return "Notification Parameters...";
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return true;
    }
}
