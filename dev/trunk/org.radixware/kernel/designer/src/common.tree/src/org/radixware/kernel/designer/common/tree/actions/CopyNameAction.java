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


public class CopyNameAction extends CookieAction {

    public static class CopyNameCookie implements org.openide.nodes.Node.Cookie {

        private final RadixObject radixObject;

        public CopyNameCookie(final RadixObject radixObject) {
            this.radixObject = radixObject;
        }

        public RadixObject getRadixObject() {
            return radixObject;
        }
    }

    public CopyNameAction() {
    }

    @Override
    protected Class<?>[] cookieClasses() {
        return new Class[]{CopyNameCookie.class};
    }

    @Override
    protected int mode() {
        return MODE_ALL;
    }

    @Override
    public String getName() {
        return "Copy Name";
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
            final CopyNameCookie copyNameCookie = node.getLookup().lookup(CopyNameCookie.class);
            if (copyNameCookie != null) {
                final RadixObject radixObject = copyNameCookie.getRadixObject();
                if (printed) {
                    sb.append(", ");
                } else {
                    printed = true;
                }
                sb.append(radixObject.getName());
            }
        }
        if (printed) {
            ClipboardUtils.copyToClipboard(sb.toString());
        }
    }
}