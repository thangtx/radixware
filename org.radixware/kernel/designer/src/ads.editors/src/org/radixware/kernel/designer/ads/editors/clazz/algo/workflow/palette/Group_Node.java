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

import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.lookup.Lookups;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.designer.common.annotations.registrators.NodeFactoryRegistration;
import org.radixware.kernel.designer.common.general.nodes.INodeFactory;


public class Group_Node extends AbstractNode {

    @NodeFactoryRegistration
    public static final class Factory implements INodeFactory<Group_> {

        @Override
        public Node newInstance(Group_ group) {
            return new Group_Node(group);
        }
    }
    private final Group_ group;
    private ItemChildren children = null;

    public Group_Node(Group_ group) {
        super(new ItemChildren(group), Lookups.singleton(group));
        setDisplayName(group.getTitle());
        this.group = group;
    }

    public void update(AdsDefinition context) {
        synchronized (this) {
            if (children != null && children.context == context) {
                return;
            }
            children = new ItemChildren(group, context);
            setChildren(children);
        }
    }
}