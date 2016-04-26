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

package org.radixware.kernel.designer.common.tree.actions;

import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.actions.CookieAction;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.common.dialogs.utils.ClipboardUtils;


public class CopyIdAction extends CookieAction {

    public static class CopyIdCookie implements org.openide.nodes.Node.Cookie {

        private final Definition definition;

        public CopyIdCookie(final Definition definition) {
            this.definition = definition;
        }

        public Definition getDefinition() {
            return definition;
        }
    }

    public CopyIdAction() {
    }

    @Override
    protected Class<?>[] cookieClasses() {
        return new Class[]{CopyIdCookie.class};
    }

    @Override
    protected int mode() {
        return MODE_ALL;
    }

    @Override
    public String getName() {
        return "Copy ID";
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
    protected void performAction(final Node[] activatedNodes) {
        final StringBuilder sb = new StringBuilder();
        boolean printed = false;

        for (Node node : activatedNodes) {
            final CopyIdCookie copyIdCookie = node.getLookup().lookup(CopyIdCookie.class);
            if (copyIdCookie != null) {
                final Definition definition = copyIdCookie.getDefinition();
                if (printed) {
                    sb.append('\n');
                } else {
                    printed = true;
                }
                final Id[] idPath = definition.getIdPath();
                boolean idPrinted = false;
                for (Id id : idPath) {
                    if (idPrinted) {
                        sb.append('.');
                    } else {
                        idPrinted = true;
                    }
                    sb.append(id);
                }
            }
        }
        if (printed) {
            ClipboardUtils.copyToClipboard(sb.toString());
        }
    }
}