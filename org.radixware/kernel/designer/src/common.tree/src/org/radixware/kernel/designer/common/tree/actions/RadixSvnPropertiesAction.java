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

import java.io.File;
import org.netbeans.modules.subversion.util.Context;
import org.netbeans.modules.subversion.util.SvnUtils;
import org.openide.nodes.Node;
import org.radixware.kernel.common.resources.RadixWareIcons;


public class RadixSvnPropertiesAction extends RadixSvnAction {

    @Override
    protected boolean enable(Node[] activatedNodes) {
        return activatedNodes.length == 1 && super.enable(activatedNodes);
    }

    @Override
    protected void performAction(Node[] activatedNodes) {
        final Context context = getCommitContext(activatedNodes);
        final String title = getContextDisplayName(activatedNodes);
        final File[] roots = SvnUtils.getActionRoots(context);
        if (roots == null || roots.length == 0) {
            return;
        }

        org.netbeans.modules.subversion.ui.properties.SvnPropertiesAction.openProperties(roots, title);
    }

    @Override
    public String getName() {
        return "Svn Properties";
    }

    @Override
    protected String iconResource() {
        return RadixWareIcons.SUBVERSION.PROPERTIES.getResourceUri();
    }
}
