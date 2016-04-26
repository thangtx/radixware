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

import java.awt.event.ActionEvent;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.actions.CookieAction;
import org.radixware.kernel.common.defs.dds.DdsDefinition;
import org.radixware.kernel.designer.common.dialogs.sqlscript.actions.ShowReCreateScriptAction;


public class DdsDefinitionViewRecreateSqlAction extends CookieAction {

    public static class Cookie implements Node.Cookie {

        private DdsDefinition definition;

        public Cookie(DdsDefinition definition) {
            this.definition = definition;
        }

        public DdsDefinition getDefinition() {
            return definition;
        }
    }

    @Override
    protected int mode() {
        return CookieAction.MODE_ANY;
    }

    @Override
    protected Class<?>[] cookieClasses() {
        return new Class[]{Cookie.class};
    }

    @Override
    protected void performAction(Node[] nodes) {
        for (int i = 0; i < nodes.length; i++) {
            Cookie cookie = nodes[i].getCookie(DdsDefinitionViewRecreateSqlAction.Cookie.class);
            if (cookie != null && cookie.getDefinition() != null) {
                new ShowReCreateScriptAction(cookie.getDefinition()).actionPerformed(new ActionEvent(this, 0, ""));
            }
        }
    }

    @Override
    public String getName() {
        return "View Re-create SQL...";
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }

    @Override
    protected String iconResource() {
        return super.iconResource();
        //return DdsDefinitionIcon.SQL_SCRIPT.getResourceUri();
    }
}
