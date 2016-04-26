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
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.designer.common.dialogs.utils.ClipboardUtils;


public class CopyQualifiedNameAction extends CookieAction {

    public static class CopyQualifiedNameCookie implements org.openide.nodes.Node.Cookie {

        private final RadixObject radixObject;

        public CopyQualifiedNameCookie(final RadixObject radixObject) {
            this.radixObject = radixObject;
        }

        public RadixObject getRadixObject() {
            return radixObject;
        }
    }

    public CopyQualifiedNameAction() {
    }

    @Override
    protected Class<?>[] cookieClasses() {
        return new Class[]{CopyQualifiedNameCookie.class};
    }

    @Override
    protected int mode() {
        return MODE_ALL;
    }

    @Override
    public String getName() {
        return "Copy Qualified Name";
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
            final CopyQualifiedNameCookie copyQualifiedNameCookie = node.getLookup().lookup(CopyQualifiedNameCookie.class);
            if (copyQualifiedNameCookie != null) {
                final RadixObject radixObject = copyQualifiedNameCookie.getRadixObject();
                if (printed) {
                    sb.append('\n');
                } else {
                    printed = true;
                }
                sb.append(radixObject.getQualifiedName());
            }
        }
        if (printed) {
            ClipboardUtils.copyToClipboard(sb.toString());
        }
    }
}