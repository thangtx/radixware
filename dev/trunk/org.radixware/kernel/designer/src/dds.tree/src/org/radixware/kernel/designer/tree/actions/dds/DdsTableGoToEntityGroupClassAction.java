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

package org.radixware.kernel.designer.tree.actions.dds;

import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.actions.CookieAction;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityGroupClassDef;
import org.radixware.kernel.common.defs.ads.common.AdsUtils;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;


public class DdsTableGoToEntityGroupClassAction extends CookieAction {

    public static class Cookie implements Node.Cookie {

        private DdsTableDef table;

        public Cookie(DdsTableDef table) {
            this.table = table;
        }

        public void goToEntityGroupClass() {
            final AdsEntityGroupClassDef egc = AdsUtils.findEntityGroupClass(table);
            if (egc != null) {
                DialogUtils.goToObject(egc);
            }
        }
    }

    public DdsTableGoToEntityGroupClassAction() {
        setIcon(AdsDefinitionIcon.CLASS_ENTITY_GROUP.getIcon());
    }

    @Override
    protected int mode() {
        return CookieAction.MODE_EXACTLY_ONE;
    }

    @Override
    protected Class<?>[] cookieClasses() {
        return new Class[]{Cookie.class};
    }

    @Override
    protected void performAction(Node[] nodes) {
        for (int i = 0; i < nodes.length; i++) {
            Cookie cookie = nodes[i].getCookie(DdsTableGoToEntityGroupClassAction.Cookie.class);
            if (cookie != null) {
                cookie.goToEntityGroupClass();
            }
        }
    }

    @Override
    public String getName() {
        return "Go To Entity Group Class";
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
