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
import java.util.logging.Logger;
import javax.swing.Action;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.actions.SystemAction;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.ads.common.AdsUtils;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.designer.common.annotations.registrators.NodeFactoryRegistration;
import org.radixware.kernel.designer.common.general.nodes.INodeFactory;
import org.radixware.kernel.designer.common.general.nodes.NodesManager;
import org.radixware.kernel.designer.tree.actions.dds.DdsTableGoToApplicationClassAction;
import org.radixware.kernel.designer.tree.actions.dds.DdsTableGoToEntityClassAction;
import org.radixware.kernel.designer.tree.actions.dds.DdsTableGoToEntityGroupClassAction;
import org.radixware.kernel.designer.tree.actions.dds.DdsTableGoToOverwriteAction;
import org.radixware.kernel.designer.tree.actions.dds.DdsTableGoToOverwrittenAction;


public class DdsTableNode extends DdsDefinitionNode {

    private static class DdsTableColumns extends Children.Keys<DdsColumnDef> implements RadixObjects.ContainerChangesListener {

        private final DdsTableDef table;

        public DdsTableColumns(DdsTableDef table) {
            this.table = table;
            updateKeys();
            table.getColumns().getLocal().getContainerChangesSupport().addEventListener(this);
        }

        private void updateKeys() {
            this.setKeys(table.getColumns().getLocal().list());
        }

        @Override
        protected Node[] createNodes(DdsColumnDef key) {
            Node node = NodesManager.findOrCreateNode(key);
            if (node == null) {
                node = new DdsColumnNode(key);
            }
            return new Node[]{node};
        }

        @Override
        public void onEvent(RadixObjects.ContainerChangedEvent e) {
            updateKeys();
        }
    }
    private final DdsTableGoToOverwrittenAction.Cookie goToOvewrittenCookie;
    private final DdsTableGoToOverwriteAction.Cookie goToOverwriteCookie;
    private final DdsTableGoToEntityClassAction.Cookie goToAecCookie;
    private final DdsTableGoToEntityGroupClassAction.Cookie goToEgcCookie;
    private final DdsTableGoToApplicationClassAction.Cookie goToAacCookie;

    public DdsTableNode(DdsTableDef table) {
        super(table, new DdsTableColumns(table));
        goToOvewrittenCookie = new DdsTableGoToOverwrittenAction.Cookie(table);
        goToOverwriteCookie = new DdsTableGoToOverwriteAction.Cookie(table);
        goToAecCookie = new DdsTableGoToEntityClassAction.Cookie(table);
        goToEgcCookie = new DdsTableGoToEntityGroupClassAction.Cookie(table);
        goToAacCookie = new DdsTableGoToApplicationClassAction.Cookie(table);
    }

    public DdsTableDef getTable() {
        return (DdsTableDef) getRadixObject();
    }

    private void updateLookupContent() {
        if (goToOvewrittenCookie.findOverwritten() != null) {
            addCookie(goToOvewrittenCookie);
        } else {
            removeCookie(goToOvewrittenCookie);
        }

        if (goToOverwriteCookie.findOverwrite() != null) {
            addCookie(goToOverwriteCookie);
        } else {
            removeCookie(goToOverwriteCookie);
        }

        final DdsTableDef table = getTable();

        if (AdsUtils.isEntityClassExists(table)) {
            addCookie(goToAecCookie);
        } else {
            removeCookie(goToAecCookie);
        }

        if (AdsUtils.isEntityGroupClassExists(table)) {
            addCookie(goToEgcCookie);
        } else {
            removeCookie(goToEgcCookie);
        }

        if (getTable().isDetailTable()) {
            addCookie(goToAacCookie);
        } else {
            removeCookie(goToAacCookie);
        }
    }

    @Override
    public void addCustomActions(List<Action> actions) {
        super.addCustomActions(actions);
        updateLookupContent();
        actions.add(null);
        actions.add(SystemAction.get(DdsTableGoToOverwrittenAction.class));
        actions.add(SystemAction.get(DdsTableGoToOverwriteAction.class));
        actions.add(null);
        actions.add(SystemAction.get(DdsTableGoToEntityClassAction.class));
        actions.add(SystemAction.get(DdsTableGoToEntityGroupClassAction.class));

        if (getTable().isDetailTable()) {
            actions.add(SystemAction.get(DdsTableGoToApplicationClassAction.class));
        }
    }

    @NodeFactoryRegistration
    public static final class Factory implements INodeFactory<DdsTableDef> {

        public Factory() {
        }

        @Override
        public DdsTableNode newInstance(DdsTableDef table) {
            return new DdsTableNode(table);
        }
    }
}
