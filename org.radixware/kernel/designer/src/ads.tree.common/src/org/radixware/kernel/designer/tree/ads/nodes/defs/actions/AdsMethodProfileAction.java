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
package org.radixware.kernel.designer.tree.ads.nodes.defs.actions;

import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CookieAction;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.designer.ads.method.profile.ChangeProfilePanel;

public class AdsMethodProfileAction extends CookieAction {

    public static class Cookie implements Node.Cookie {

        private AdsMethodDef method;

        public Cookie(AdsMethodDef method) {
            this.method = method;
        }

        public void changeProfile() {
            ChangeProfilePanel panel = new ChangeProfilePanel();
            panel.editProfile(method);
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
    protected void performAction(Node[] activatedNodes) {
        for (int i = 0; i < activatedNodes.length; i++) {
            Cookie c = activatedNodes[i].getCookie(Cookie.class);
            if (c != null) {
                c.changeProfile();
            }
        }
    }

    @Override
    public String getName() {
        return NbBundle.getMessage(AdsMethodProfileAction.class, "Profile-Action-Title");
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }
}