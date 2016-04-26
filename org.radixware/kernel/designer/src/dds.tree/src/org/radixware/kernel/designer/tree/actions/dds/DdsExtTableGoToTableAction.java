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
import org.radixware.kernel.common.defs.dds.DdsExtTableDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.general.editors.EditorsManager;
import org.radixware.kernel.common.resources.RadixWareIcons;


public class DdsExtTableGoToTableAction extends CookieAction {

    public static class Cookie implements Node.Cookie {

        private DdsExtTableDef extTable;

        public Cookie(DdsExtTableDef extTable) {
            this.extTable = extTable;
        }

        public void goToTable() {
            try {
                DdsTableDef table = extTable.getTable();
                EditorsManager.getDefault().open(table);
            } catch (DefinitionNotFoundError error) {
                DialogUtils.messageError(error.getMessage());
            }
        }
    }

    public DdsExtTableGoToTableAction() {
        setIcon(RadixWareIcons.ARROW.GO_TO_OBJECT.getIcon());
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
            Cookie cookie = nodes[i].getCookie(DdsExtTableGoToTableAction.Cookie.class);
            if (cookie != null) {
                cookie.goToTable();
            }
        }
    }

    @Override
    public String getName() {
        return "Go To Table";
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
