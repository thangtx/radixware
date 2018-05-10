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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;
import org.radixware.kernel.common.defs.RadixObject;

public class RadixdocTreeModulesCellRenderer extends JPanel implements TreeCellRenderer {

    RadixdocCheckTreeSelectionModel treeSelectionModel;
    RadixdocStatesCheckBox checkBox = new RadixdocStatesCheckBox();

    Color selectionBorderColor, selectionForeground, selectionBackground,
            textForeground, textBackground;

    public RadixdocTreeModulesCellRenderer(TreeCellRenderer delegate, RadixdocCheckTreeSelectionModel treeModel) {
        this.treeSelectionModel = treeModel;

        setLayout(new BorderLayout());
        setOpaque(false);
        checkBox.setOpaque(false);
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        TreePath path = tree.getPathForRow(row);
        if (path != null) {
            if (treeSelectionModel.isPartiallySelected(path)) {
                checkBox.setState(RadixdocStatesCheckBox.DONT_CARE);
            } else if (treeSelectionModel.isPathSelected(path, true)) {
                checkBox.setState(RadixdocStatesCheckBox.SELECTED);
            } else {
                checkBox.setState(RadixdocStatesCheckBox.NOT_SELECTED);
            }
            
            UIManager.put("Tree.rendererFillBackground", false);
            UIManager.put("Tree.selectionForeground", Color.BLACK);
        }
        removeAll();
        add(checkBox, BorderLayout.WEST);
        JLabel elementLabel = new JLabel(treeSelectionModel.getModel().getName((RadixObject) value), treeSelectionModel.getModel().getIcon((RadixObject) value).getIcon(), JLabel.CENTER);
        add(elementLabel, BorderLayout.CENTER);
        tree.setForeground(getForeground());
        tree.setBackground(getBackground());
        return this;
    }
}
