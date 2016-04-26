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

package org.radixware.kernel.designer.tree.ads.nodes.actions;

import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.actions.CookieAction;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.designer.ads.editors.role.AdsUsedByRoleDialog;


public class ConfigureUsingRolesAction extends CookieAction {

    public static class Cookie implements Node.Cookie {

        private RadixObject object;

        public Cookie(RadixObject object) {
            this.object = object;
        }

        private void configure() {
            AdsUsedByRoleDialog d = new AdsUsedByRoleDialog(object);
            d.showModal();
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
        Cookie c = activatedNodes[0].getCookie(Cookie.class);
        if (c != null) {
            c.configure();
        }
    }

    @Override
    public String getName() {
        return "Set up Roles Usage...";
    }

    @Override
    public HelpCtx getHelpCtx() {
        return null;
    }
}