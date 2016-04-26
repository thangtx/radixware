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

import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.gui.QMouseEvent;
import com.trolltech.qt.gui.QTreeView;
import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.tree.IExplorerTree;
import org.radixware.kernel.common.client.tree.nodes.IExplorerTreeNode;
import org.radixware.kernel.explorer.macros.widgets.QWidgetPath;


final class ExplorerTreeMouseClickActionFactory implements IMacroActionFactory {

    private ExplorerTreeMouseClickActionFactory() {
    }
    private final static ExplorerTreeMouseClickActionFactory INSTANCE = new ExplorerTreeMouseClickActionFactory();

    public static ExplorerTreeMouseClickActionFactory getInstance() {
        return INSTANCE;
    }

    @Override
    public IMacroAction createAction(QEvent event, QWidget targetWidget) {
        if ((targetWidget instanceof QTreeView) && (targetWidget instanceof IExplorerTree)) {
            final QTreeView tree = (QTreeView) targetWidget;
            final IExplorerTree explorerTree = (IExplorerTree) targetWidget;
            if (event.type() == QEvent.Type.MouseButtonPress && (event instanceof QMouseEvent)) {
                final QMouseEvent mouseEvent = (QMouseEvent) event;
                final QModelIndex index = tree.indexAt(mouseEvent.pos());
                final IExplorerTreeNode node = explorerTree.findNodeByPosition(mouseEvent.pos().x(), mouseEvent.pos().y());
                if (index != null && node != null && event.isAccepted()) {
                    final QWidgetPath widgetPath = QWidgetPath.Factory.newInstance(targetWidget);
                    if (!tree.visualRect(index).contains(mouseEvent.pos())) {//кликнули на "+" или "-"
                        final boolean isExpanded = explorerTree.isExpanded(node);
                        return new SetCurrentExplorerTreeNodeAction(widgetPath, node, isExpanded, !isExpanded);
                    } else {
                        return new SetCurrentExplorerTreeNodeAction(widgetPath, node, false, false);
                    }
                }
            }
        }
        return null;
    }
}
