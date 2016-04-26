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

import org.netbeans.modules.subversion.Subversion;
import org.radixware.kernel.designer.subversion.ui.status.SvnVersioningTopComponent;
//import org.netbeans.modules.subversion.ui.status.SvnVersioningTopComponent;
import org.netbeans.modules.subversion.util.Context;
import org.openide.nodes.Node;
import org.radixware.kernel.common.resources.RadixWareIcons;


public class RadixSvnStatusAction extends RadixSvnAction {

    @Override
    protected void performAction(Node[] activatedNodes) {
        final Context context = getUpdateContext(activatedNodes);
        final String title = getContextDisplayName(activatedNodes);

        if (!Subversion.getInstance().checkClientAvailable()) {
            return;
        }

        final SvnVersioningTopComponent stc = SvnVersioningTopComponent.getInstance();
        stc.setContentTitle(title);
        stc.setContext(context);
        stc.open();
        stc.requestActive();
        stc.performRefreshAction();
    }

    @Override
    public String getName() {
        return "Show Changes...";
    }

    @Override
    protected String iconResource() {
        return RadixWareIcons.SUBVERSION.SHOW_CHANGES.getResourceUri();
    }
}
