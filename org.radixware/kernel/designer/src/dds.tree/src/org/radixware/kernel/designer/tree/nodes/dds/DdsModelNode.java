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

package org.radixware.kernel.designer.tree.nodes.dds;

import java.util.ArrayList;
import java.util.List;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.radixware.kernel.common.defs.dds.DdsModelDef;
import org.radixware.kernel.designer.common.annotations.registrators.NodeFactoryRegistration;
import org.radixware.kernel.designer.common.general.nodes.INodeFactory;
import org.radixware.kernel.designer.common.general.nodes.NodesManager;
import org.radixware.kernel.designer.common.tree.RadixObjectNode;


public class DdsModelNode extends RadixObjectNode {

    private static class ChildFactory extends org.openide.nodes.ChildFactory<Object> {

        private final DdsModelDef model;

        private ChildFactory(DdsModelDef ddsModel) {
            this.model = ddsModel;
        }

        @Override
        protected boolean createKeys(List<Object> itemsKeys) {
            itemsKeys.add(new Object());
            return true;
        }

        @Override
        protected Node[] createNodesForKey(Object key) {
            final List<Node> nodes = new ArrayList<Node>();

            nodes.add(NodesManager.findOrCreateNode(model.getTables()));
            nodes.add(NodesManager.findOrCreateNode(model.getViews()));
            nodes.add(NodesManager.findOrCreateNode(model.getReferences()));
            nodes.add(NodesManager.findOrCreateNode(model.getSequences()));

            return nodes.toArray(new Node[0]);
        }
    }

    protected DdsModelNode(DdsModelDef ddsModel) {
        super(ddsModel, Children.create(new ChildFactory(ddsModel), true));
    }

    @NodeFactoryRegistration
    public static final class Factory implements INodeFactory<DdsModelDef> {

        @Override
        public RadixObjectNode newInstance(DdsModelDef ddsModel) {
            return new DdsModelNode(ddsModel);
        }
    }
}
