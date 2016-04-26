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

package org.radixware.kernel.designer.ads.editors.clazz.forms.palette;

import java.util.ArrayList;
import org.openide.nodes.Index;
import org.openide.nodes.Node;


public class ItemChildren extends Index.ArrayChildren {

    private Group group;

    public ItemChildren(Group group) {
        this.group = group;
    }

    @Override
    protected java.util.List<Node> initCollection() {
        ArrayList<Node> childrenNodes = new ArrayList<Node>();
        for (Item item : Item.getItems(group.getEnvironment())) {
            if (item != null) {
                if (group.equals(item.getGroup())) {
                    childrenNodes.add(new ItemNode(item));
                }
            }
        }
        return childrenNodes;
    }
}