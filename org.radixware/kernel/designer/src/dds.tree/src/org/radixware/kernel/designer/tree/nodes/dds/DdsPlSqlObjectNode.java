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

import java.util.List;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.radixware.kernel.common.defs.dds.DdsPlSqlObjectDef;
import org.radixware.kernel.designer.common.annotations.registrators.NodeFactoryRegistration;
import org.radixware.kernel.designer.common.tree.RadixObjectNode;
import org.radixware.kernel.designer.common.general.nodes.INodeFactory;
import org.radixware.kernel.designer.common.general.nodes.NodesManager;


public class DdsPlSqlObjectNode extends DdsDefinitionNode {

    private enum EDdsPlSqlObjectNodeType {

        HEADER,
        BODY
    }

    private static class ChildFactory extends org.openide.nodes.ChildFactory<EDdsPlSqlObjectNodeType> {

        private DdsPlSqlObjectDef plSqlObject;

        private ChildFactory(DdsPlSqlObjectDef plSqlObject) {
            this.plSqlObject = plSqlObject;
        }

        @Override
        protected boolean createKeys(List<EDdsPlSqlObjectNodeType> itemsKeys) {
            for (EDdsPlSqlObjectNodeType nodeType : EDdsPlSqlObjectNodeType.values()) {
                itemsKeys.add(nodeType);
            }
            return true;
        }

        @Override
        public Node createNodeForKey(EDdsPlSqlObjectNodeType key) {
            switch (key) {
                case HEADER:
                    return NodesManager.findOrCreateNode(plSqlObject.getHeader().getItems());
                case BODY:
                    return NodesManager.findOrCreateNode(plSqlObject.getBody().getItems());
                default:
                    return null;
            }
        }
    }

    protected DdsPlSqlObjectNode(DdsPlSqlObjectDef plSqlObject) {
        super(plSqlObject, Children.create(new ChildFactory(plSqlObject), true));
    }

    @NodeFactoryRegistration
    public static class Factory implements INodeFactory<DdsPlSqlObjectDef> {

        @Override // Registered in layer.xml
        public RadixObjectNode newInstance(DdsPlSqlObjectDef ddsPlSqlObject) {
            return new DdsPlSqlObjectNode(ddsPlSqlObject);
        }
    }
}

