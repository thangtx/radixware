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

import java.util.HashSet;
import java.util.Set;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.actions.CookieAction;
import org.radixware.kernel.common.defs.dds.DdsDefinition;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.dds.script.ScriptGenerator;
import org.radixware.kernel.designer.dds.script.defs.DdsScriptGeneratorUtils;


public class DdsDefinitionViewSqlAction extends CookieAction {

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

    private static void viewSql(Set<DdsDefinition> definitions) {
        final String sql = DdsScriptGeneratorUtils.generateCreationScript(definitions);
        DialogUtils.showText(sql, "Script", "sql");
    }

    @Override
    protected void performAction(Node[] nodes) {
        final Set<DdsDefinition> definitions = new HashSet<DdsDefinition>();
        for (int i = 0; i < nodes.length; i++) {
            Cookie cookie = nodes[i].getCookie(DdsDefinitionViewSqlAction.Cookie.class);
            if (cookie != null) {
                definitions.add(cookie.getDefinition());
            }
        }
        if (!definitions.isEmpty()) {
            viewSql(definitions);
        }
    }

    @Override
    public String getName() {
        return "View SQL...";
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
