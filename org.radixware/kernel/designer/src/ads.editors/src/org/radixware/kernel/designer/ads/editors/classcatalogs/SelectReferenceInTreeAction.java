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
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsClassCatalogDef.ClassReference;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.general.nodes.NodesManager;


public class SelectReferenceInTreeAction extends CookieAction {

    static class Cookie implements Node.Cookie {

        private RadixObject rdx;

        Cookie(RadixObject rdx) {
            this.rdx = rdx;
        }

        public void selectInTree() {
            AdsClassDef reference = ((ClassReference) rdx).findReferencedClass();
            if (reference != null) {
                NodesManager.selectInProjects(reference);
            } else {
                DialogUtils.messageError("Cannot find referenced class: " + ((ClassReference) rdx).getClassId());
            }
        }
    }

    @Override
    protected void performAction(Node[] activatedNodes) {
        for (int i = 0; i < activatedNodes.length; i++) {
            Cookie cc = activatedNodes[i].getCookie(Cookie.class);

            if (cc != null) {
                cc.selectInTree();
            }
        }
    }

    @Override
    protected int mode() {
        return MODE_EXACTLY_ONE;
    }

    @Override
    protected Class<?>[] cookieClasses() {
        return new Class[]{SelectReferenceInTreeAction.Cookie.class};
    }

    @Override
    public String getName() {
        return NbBundle.getMessage(SelectReferenceInTreeAction.class, "TreeMenu-SelectInTree");
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
