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
import org.radixware.kernel.common.defs.ads.command.AdsCommandModelClassDef;
import org.radixware.kernel.designer.common.general.utils.RadixMutex;


public class SyncCommandModelClassAction extends AdsDefinitionAction {

    public static class SyncCommandModelClassCookie implements Node.Cookie {

        private AdsCommandModelClassDef model;

        public SyncCommandModelClassCookie(AdsCommandModelClassDef model) {
            this.model = model;
        }

        private boolean isEnabled() {
            return !model.isReadOnly();
        }

        private void sync() {
            RadixMutex.writeAccess(new Runnable() {
                @Override
                public void run() {
                    model.sync();
                }
            });

        }
    }

    @Override
    protected boolean calcEnabled(final Node[] activatedNodes) {
        for (Node n : activatedNodes) {
            final SyncCommandModelClassCookie c = n.getCookie(SyncCommandModelClassCookie.class);
            if (c == null || !c.isEnabled()) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected int mode() {
        return MODE_EXACTLY_ONE;
    }

    @Override
    protected Class<?>[] cookieClasses() {
        return new Class[]{SyncCommandModelClassCookie.class};
    }

    @Override
    protected void performAction(Node[] activatedNodes) {
        for (Node n : activatedNodes) {
            final SyncCommandModelClassCookie c = n.getCookie(SyncCommandModelClassCookie.class);
            if (c != null && c.isEnabled()) {
                c.sync();
            }
        }
    }

    @Override
    public String getName() {
        return "Create/Update required methods";
    }
}
