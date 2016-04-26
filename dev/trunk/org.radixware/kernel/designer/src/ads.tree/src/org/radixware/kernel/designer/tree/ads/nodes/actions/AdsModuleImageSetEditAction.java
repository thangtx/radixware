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
import org.openide.util.NbBundle;
import org.openide.util.actions.CookieAction;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.designer.common.editors.module.images.AdsModuleImageSetEditor;


public class AdsModuleImageSetEditAction extends CookieAction {

    public static class Cookie implements org.openide.nodes.Node.Cookie {

        private final AdsModule adsModule;

        public Cookie(AdsModule adsModule) {
            this.adsModule = adsModule;
        }

        public void editImages() {
            AdsModuleImageSetEditor editor = new AdsModuleImageSetEditor(adsModule);
            editor.showAsDialog();
        }
    }

    @Override
    protected int mode() {
        return MODE_ONE;
    }

    @Override
    @SuppressWarnings({"rawtypes"})
    protected Class<?>[] cookieClasses() {

        return new Class[]{AdsModuleImageSetEditAction.Cookie.class};
    }

    @Override
    protected void performAction(Node[] activatedNodes) {
        for (int i = 0; i < activatedNodes.length; ++i) {
            AdsModuleImageSetEditAction.Cookie cookie = activatedNodes[i].getCookie(AdsModuleImageSetEditAction.Cookie.class);
            if (cookie != null) {
                cookie.editImages();
            }
        }
    }

    @Override
    public String getName() {
        return NbBundle.getMessage(AdsModuleImageSetEditAction.class, "Action-Configure-Images");
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    public boolean asynchronous() {
        return false;
    }
}
