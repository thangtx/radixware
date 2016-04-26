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
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.repository.dds.DdsDatabaseScripts;
import org.radixware.kernel.designer.common.general.nodes.NodesManager;
import org.radixware.kernel.designer.common.tree.RadixObjectEditCookie;
import org.radixware.kernel.designer.common.tree.RadixObjectNode;


public class DdsDatabaseScriptsNode extends RadixObjectNode {

    private static class ChildFactory extends org.openide.nodes.ChildFactory<RadixObject> {

        private DdsDatabaseScripts databaseScripts;

        private ChildFactory(DdsDatabaseScripts databaseScripts) {
            this.databaseScripts = databaseScripts;
        }

        @Override
        protected boolean createKeys(final List<RadixObject> itemsKeys) {
            //final Layer prevLayer = databaseScripts.getOwnerScripts().getOwnerSegment().getLayer().findPrevLayer();
            //if (prevLayer == null) { // display create script only for base layer (org.radixware) RADIX-1577, commented to define place where user can create tablespaces
            itemsKeys.add(databaseScripts.getCreateScript());
            //}
            itemsKeys.add(databaseScripts.getInstallScript());
            itemsKeys.add(databaseScripts.getPreScripts());
            itemsKeys.add(databaseScripts.getPostScripts());
            itemsKeys.add(databaseScripts.getUpgradeScripts());
            return true;
        }

        @Override
        public Node createNodeForKey(RadixObject key) {
            return NodesManager.findOrCreateNode(key);
        }
    }

    protected DdsDatabaseScriptsNode(DdsDatabaseScripts databaseScripts) {
        super(databaseScripts, Children.create(new ChildFactory(databaseScripts), true));
    }

    @Override
    protected RadixObjectEditCookie createEditCookie() {
        return null;
    }

    @Override
    public boolean canCheck() {
        return false;
    }

//    @NodeFactoryRegistration
//    public static class Factory implements INodeFactory<DdsDatabaseScripts> {
//
//        @Override
//        public RadixObjectNode newInstance(DdsDatabaseScripts databaseScripts) {
//            return new DdsDatabaseScriptsNode(databaseScripts);
//        }
//    }
}
