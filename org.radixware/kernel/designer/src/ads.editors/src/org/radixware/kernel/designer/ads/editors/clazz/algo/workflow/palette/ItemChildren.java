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

import java.util.ArrayList;
import org.openide.nodes.Index;
import org.openide.nodes.Node;
import org.radixware.kernel.common.defs.ads.AdsDefinition;


public class ItemChildren  extends Index.ArrayChildren {
    
    private final Group_ group;
    final AdsDefinition context;

    public ItemChildren(Group_ group) {
        this(group, null);
    }

    public ItemChildren(Group_ group, AdsDefinition context) {
        this.group = group;
        this.context = context;
    }

    @Override
    protected java.util.List<Node> initCollection() {
        ArrayList<Node> childrenNodes = new ArrayList<Node>();
        if (context != null)
            for (Item item: Item.getItems(context))
                if (group.equals(item.getGroup())) {
                    if (item.isUserClass() && item.getUserDef(context) == null)
                        continue;
                    childrenNodes.add(new ItemNode(item));
                }
        return childrenNodes;
    }
}