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

package org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.palette;

import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.radixware.kernel.designer.common.general.nodes.NodesManager;


public class GroupChildren extends Children.Keys<Group_> {

    static final Group_[] groups = new Group_[] {
        Group_.GROUP_ALGBLOCKS,
        Group_.GROUP_APPBLOCKS
    };

    public GroupChildren() {
    }

    @Override
    protected Node[] createNodes(Group_ key) {
        return new Node[] { NodesManager.findOrCreateNode(key) };
    }

    @Override
    protected void addNotify() {
        super.addNotify();
        setKeys(groups);
    }
}