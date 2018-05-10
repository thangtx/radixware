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
package org.radixware.kernel.designer.common.dialogs.radixdoc;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JCheckBox;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

public class RadixdocCheckTreeManager extends MouseAdapter implements TreeSelectionListener {

    RadixdocCheckTreeSelectionModel selectionModel;
    RadixdocSelectModulesPanel modulesPanel;
    private JTree tree = new JTree();

    int hotspot = new JCheckBox().getPreferredSize().width;

    public RadixdocCheckTreeManager(JTree tree, RadixdocCheckTreeSelectionModel checkTreeSelectionModel, RadixdocSelectModulesPanel modulesPanel) {
        this.tree = tree;
        this.modulesPanel = modulesPanel;

        if (checkTreeSelectionModel != null) {
            selectionModel = checkTreeSelectionModel;

        } else {
            selectionModel = new RadixdocCheckTreeSelectionModel(tree.getModel());
        }

        tree.setCellRenderer(new RadixdocTreeModulesCellRenderer(tree.getCellRenderer(), selectionModel));

        tree.addMouseListener(this);
        selectionModel.addTreeSelectionListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent me) {

        TreePath path = tree.getPathForLocation(me.getX(), me.getY());

        if (path == null) {
            return;
        }

        if (me.getX() / 1.2 > tree.getPathBounds(path).x + hotspot) {
            return;
        }

        boolean selected = selectionModel.isPathSelected(path, true);
        selectionModel.removeTreeSelectionListener(this);

        try {
            if (selected) {
                selectionModel.removeSelectionPath(path);
            } else {
                selectionModel.addSelectionPath(path);
            }
        } finally {
            selectionModel.addTreeSelectionListener(this);
            tree.treeDidChange();
            modulesPanel.updateDialogButtonsState();
        }
    }

    public RadixdocCheckTreeSelectionModel getSelectionModel() {
        return selectionModel;
    }

    public void setSelectionModel(RadixdocCheckTreeSelectionModel selectionModel) {
        this.selectionModel = selectionModel;
    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {
        tree.treeDidChange();
    }
}
