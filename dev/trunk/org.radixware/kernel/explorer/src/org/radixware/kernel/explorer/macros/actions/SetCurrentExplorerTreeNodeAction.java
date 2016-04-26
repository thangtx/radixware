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

package org.radixware.kernel.explorer.macros.actions;

import com.trolltech.qt.gui.QIcon;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.tree.IExplorerTree;
import org.radixware.kernel.common.client.tree.nodes.IExplorerTreeNode;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.macros.MacrosSettings;
import org.radixware.kernel.explorer.macros.widgets.QWidgetPath;


final class SetCurrentExplorerTreeNodeAction extends UserInputAction {

    private final IExplorerTreeNode treeNode;
    private final boolean setExpanded, setCollapsed;

    SetCurrentExplorerTreeNodeAction(final QWidgetPath widgetPath, IExplorerTreeNode node, final boolean setExpanded, final boolean setCollapsed) {
        super(widgetPath);
        treeNode = node;
        this.setExpanded = setExpanded;
        this.setCollapsed = setCollapsed;
    }

    @Override
    public String toString() {
        final String title = "set current '" + treeNode.toString() + "' node";
        if (setExpanded) {
            return title + " and expand";
        } else if (setCollapsed) {
            return title + " and collapse";
        }
        return title;
    }

    @Override
    public MacroActionResult execute(MacrosSettings settings) {
        final IExplorerTree tree = treeNode.getExplorerTree();
        tree.setCurrent(treeNode);
        if (setExpanded) {
            tree.expand(treeNode);
        } else if (setCollapsed) {
            tree.collapse(treeNode);
        }
        return null;
    }

    @Override
    public QIcon getIcon() {
        return ExplorerIcon.getQIcon(ClientIcon.EXPLORER);
    }

    public String getTitle() {
        return "Set current node " + treeNode.getPath();
    }

    public boolean canExecuteNow() {
        return true;
    }
}
