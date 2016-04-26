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

package org.radixware.kernel.designer.ads.editors.classcatalogs;

import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CookieAction;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsClassCatalogDef.ClassReference;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsClassCatalogDef.Topic;
import org.radixware.kernel.designer.common.general.editors.EditorsManager;


public class OpenOwnerClassCatalogAction extends CookieAction {

    static class Cookie implements Node.Cookie {

        private RadixObject rdx;

        Cookie(RadixObject rdx) {
            this.rdx = rdx;
        }

        public void openOwnerClassCatalog() {
            if (rdx instanceof ClassReference) {
                EditorsManager.getDefault().open(((ClassReference) rdx).getOwnerClassCatalog());
            } else {
                EditorsManager.getDefault().open(((Topic) rdx).getOwnerClassCatalog());
            }
        }
    }

    @Override
    protected void performAction(Node[] activatedNodes) {
        for (int i = 0; i < activatedNodes.length; i++) {
            Cookie cc = activatedNodes[i].getCookie(Cookie.class);

            if (cc != null) {
                cc.openOwnerClassCatalog();
            }
        }
    }

    @Override
    protected int mode() {
        return MODE_EXACTLY_ONE;
    }

    @Override
    protected Class<?>[] cookieClasses() {
        return new Class[]{OpenOwnerClassCatalogAction.Cookie.class};
    }

    @Override
    public String getName() {
        return NbBundle.getMessage(OpenOwnerClassCatalogAction.class, "MenuItem-GoToOwnerCatalog");
    }

    @Override
    public HelpCtx getHelpCtx() {
        return null;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }
}
