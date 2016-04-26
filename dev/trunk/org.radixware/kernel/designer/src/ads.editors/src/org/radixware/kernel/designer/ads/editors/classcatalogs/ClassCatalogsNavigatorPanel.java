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

package org.radixware.kernel.designer.ads.editors.classcatalogs;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.util.HashMap;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.SwingUtilities;
import javax.swing.text.DefaultEditorKit;
import javax.swing.tree.TreeSelectionModel;

import org.openide.actions.EditAction;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.explorer.view.BeanTreeView;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.actions.SystemAction;
import org.openide.windows.TopComponent;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.Inheritance.ClassHierarchySupport;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsClassCatalogDef;


public class ClassCatalogsNavigatorPanel extends TopComponent implements Lookup.Provider, ExplorerManager.Provider {

    private final ExplorerManager manager;
    private final BeanTreeView treeView = new BeanTreeView();
    private final Lookup lookup;
    private final Action copyAction;

    public ClassCatalogsNavigatorPanel() {
        this.manager = new ExplorerManager();

        final ActionMap map = getActionMap();

        copyAction = ExplorerUtils.actionCopy(manager);
        map.put(DefaultEditorKit.copyAction, copyAction);
        map.put(DefaultEditorKit.cutAction, ExplorerUtils.actionCut(manager));
        map.put(DefaultEditorKit.pasteAction, ExplorerUtils.actionPaste(manager));

        map.put("delete", new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                SystemAction.get(DeleteClassCatalogItemAction.class).performAction(manager.getSelectedNodes());
            }
        });

        map.put("edit", SystemAction.get(EditAction.class));

        this.lookup = ExplorerUtils.createLookup(manager, map);
        setLayout(new BorderLayout());

        this.treeView.setRootVisible(false);
        treeView.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        add(treeView);

        associateLookup(lookup);
    }

    @Override
    public void setEnabled(boolean enabled) {
        treeView.setEnabled(enabled);
        super.setEnabled(enabled);
    }

    @Override
    public boolean isEnabled() {
        return treeView.isEnabled();
    }

    public boolean setSelectedNodes(Node[] selection) {
        try {
            manager.setSelectedNodes(selection);
            return true;
        } catch (PropertyVetoException ex) {
            return false;
        }
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        if (manager != null) {
            manager.addPropertyChangeListener(listener);
        } else {
            super.addPropertyChangeListener(listener);
        }
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        if (manager != null) {
            manager.removePropertyChangeListener(listener);
        } else {
            super.removePropertyChangeListener(listener);
        }
    }

    public ClassCatalogItemNode getSelectedNode() {
        Node[] nodes = manager.getSelectedNodes();
        return nodes != null && nodes.length > 0 ? (ClassCatalogItemNode) nodes[0] : null;
    }

    @Override
    public ExplorerManager getExplorerManager() {
        return manager;
    }

    @Override
    public Lookup getLookup() {
        return lookup;
    }

    public void updateTree(AdsClassCatalogDef rootContext, HashMap<RadixObject, Object> treeMap, ClassHierarchySupport hSupport) {
        ClassCatalogItemNode rootNode = new ClassCatalogItemNode(false, true, rootContext.isReadOnly(), rootContext, new ItemChildren(rootContext, treeMap, hSupport), hSupport);
        manager.setRootContext(rootNode);
        manager.setExploredContext(rootNode);
        if (SwingUtilities.isEventDispatchThread()) {
            ExplorerUtils.activateActions(manager, true);
        } else {
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    ExplorerUtils.activateActions(manager, true);
                }
            });
        }
    }

    public void expandOwnBranches() {
        ClassCatalogItemNode rootNode = (ClassCatalogItemNode) manager.getRootContext();
        expandSubBranches(rootNode);
    }

    private void expandSubBranches(ClassCatalogItemNode node) {
        for (int i = 0, size = node.getNodesCount() - 1; i <= size; i++) {
            ClassCatalogItemNode inode = node.getNodeAt(i);
            if (!inode.isInCurrentCatalog()) {
                expandSubBranches(inode);
            } else {
                if (inode.isLeaf()) {
                    treeView.expandNode(inode.getParentNode());
                } else {
                    treeView.expandNode(inode.getParentNode());
                    expandSubBranches(inode);
                }
            }
        }
    }
}
