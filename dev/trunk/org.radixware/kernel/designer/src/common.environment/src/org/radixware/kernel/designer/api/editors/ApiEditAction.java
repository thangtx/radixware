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

package org.radixware.kernel.designer.api.editors;

import javax.swing.SwingUtilities;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.actions.CookieAction;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.designer.api.ApiEditorManager;


public class ApiEditAction extends CookieAction {

    public static class Cookie implements Node.Cookie {

        private final RadixObject object;

        public Cookie(RadixObject object) {
            this.object = object;
        }
    }

    public ApiEditAction() {
    }

    @Override
    protected int mode() {
        return CookieAction.MODE_EXACTLY_ONE;
    }

    @Override
    protected Class<?>[] cookieClasses() {
        return new Class[] { Cookie.class };
    }

    @Override
    protected void performAction(Node[] activatedNodes) {
        if (activatedNodes.length == 1) {
            final Cookie cookie = activatedNodes[0].getLookup().lookup(Cookie.class);

            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    ApiEditorManager.getDefault().open(cookie.object);
                }
            });
        }
    }

    @Override
    public String getName() {
        return "API...";
    }

    @Override
    public HelpCtx getHelpCtx() {
        return null;
    }
}
