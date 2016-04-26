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
import org.radixware.kernel.common.repository.dds.DdsScripts;
import org.radixware.kernel.designer.common.annotations.registrators.NodeFactoryRegistration;
import org.radixware.kernel.designer.common.general.nodes.INodeFactory;
import org.radixware.kernel.designer.common.general.nodes.NodesManager;
import org.radixware.kernel.designer.common.tree.RadixObjectNode;


public class DdsScriptsNode extends RadixObjectNode {

//    private static class ChildFactory extends org.openide.nodes.ChildFactory<DdsScript> {
//
//        private DdsScripts scripts;
//
//        private ChildFactory(DdsScripts scripts) {
//            this.scripts = scripts;
//        }
//
//        @Override
//        protected boolean createKeys(List<DdsScript> itemsKeys) {
//            itemsKeys.add(scripts.getOracleScripts().getCreateScript());
////            itemsKeys.add(scripts.getOracleScripts().getInstallScript());
////            itemsKeys.add(scripts.getOracleScripts().);
//            return true;
//        }
//
//        @Override
//        public Node createNodeForKey(DdsScript key) {
//            return NodesManager.findOrCreateNode(key);
//        }
//    }
    
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
    
    
    protected DdsScriptsNode(DdsScripts scripts) {
        super(scripts, Children.create(new ChildFactory(scripts.getDbScripts()), true));
    }

//    private static class ScriptsEditCookie extends RadixObjectEditCookie {
//
//        public ScriptsEditCookie(DdsScripts scripts) {
//            super(scripts);
//        }
//
//        public DdsScripts getScripts() {
//            return (DdsScripts) getRadixObject();
//        }
//
//        @Override
//        public void edit() {
//            final DdsScripts scripts = getScripts();
//            final File file = scripts.getFile();
//            DialogUtils.editFile(file);
//        }
//    }

    public DdsScripts getScripts() {
        return (DdsScripts) getRadixObject();
    }

//    @Override
//    protected RadixObjectEditCookie createEditCookie() {
//        final DdsScripts script = getScripts();
//        return new ScriptsEditCookie(script);
//    }

    @Override
    public boolean canCheck() {
        return true;
    }

    @NodeFactoryRegistration
    public static class Factory implements INodeFactory<DdsScripts> {

        @Override
        public RadixObjectNode newInstance(DdsScripts scripts) {
            return new DdsScriptsNode(scripts);
        }
    }
}
