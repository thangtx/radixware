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

package org.radixware.kernel.explorer.tree;

import org.radixware.kernel.common.client.tree.nodes.ExplorerTreeNode;
import com.trolltech.qt.core.QAbstractItemModel;
import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QFont;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.tree.ExplorerItemView;
import org.radixware.kernel.common.client.tree.nodes.IExplorerTreeNode;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.ExplorerSettings;

import org.radixware.kernel.common.client.views.IProgressHandle;
import org.radixware.kernel.explorer.env.ImageManager;


public class ExplorerTreeModel extends QAbstractItemModel {

    private final Map<Long, ExplorerTreeNode> data = new HashMap<>(64);
    private final Map<Long, QModelIndex> indexes = new HashMap<>(64);
    private final ExplorerTreeNode root;
    private final IClientEnvironment environment;

    public ExplorerTreeModel(final ExplorerTreeNode rootNode, final IClientEnvironment environment, final QObject parent) {
        super(parent);
        root = rootNode;
        this.environment = environment;
    }        

    public QModelIndex findIndexByNode(final IExplorerTreeNode node) {
        return node != null ? indexes.get(node.getIndexInExplorerTree()) : null;
    }

    public IExplorerTreeNode findNodeByIndex(final QModelIndex index) {
        return index != null ? data.get(index.internalId()) : null;
    }

    @Override
    public Object data(final QModelIndex index, final int role) {
        final ExplorerTreeNode node = (ExplorerTreeNode) findNodeByIndex(index);
        if (node == null) {
            return null;
        }
        try{
            if (role == Qt.ItemDataRole.DisplayRole) {
                return node.getTitle();
            }        
            if (!node.isValid()) {
                switch (role) {
                    case Qt.ItemDataRole.FontRole:
                        return QApplication.font();
                    case Qt.ItemDataRole.TextAlignmentRole:
                        return new Qt.Alignment(Qt.AlignmentFlag.AlignLeft);
                    default:
                        return null;
                }
            }
            final ExplorerSettings settings = (ExplorerSettings)root.getExplorerTree().getEnvironment().getConfigStore();
            final ExplorerItemView view = node.getView();
            try {
                settings.beginGroup(SettingNames.SYSTEM);
                settings.beginGroup(SettingNames.EXPLORER_TREE_GROUP);
                settings.beginGroup(getSettingsGroup(view));

                switch (role) {
                    case Qt.ItemDataRole.DecorationRole: {
                        final boolean show_icons = settings.readBoolean(SettingNames.ExplorerTree.Common.SHOW_ICONS);
                        try {
                            Icon icon = show_icons ? view.getIcon() : null;
                            if (icon != null) {
                                return ImageManager.getQIcon(icon);
                            } else {
                                return null;
                            }
                        } catch (Throwable ex) {
                            view.getModel().getEnvironment().getTracer().put(ex);
                            return null;
                        }
                    }
                    case Qt.ItemDataRole.BackgroundRole: {
                        final QColor color = settings.readQColor(SettingNames.ExplorerTree.Common.BACKGROUND_COLOR);
                        return color != null ? color : null;
                    }
                    case Qt.ItemDataRole.ForegroundRole: {
                        final QColor color = settings.readQColor(SettingNames.ExplorerTree.Common.FOREGROUND_COLOR);
                        return color != null ? color : null;
                    }
                    case Qt.ItemDataRole.FontRole: {
                        final QFont font = settings.readQFont(SettingNames.ExplorerTree.Common.FONT);
                        return font != null ? font : QApplication.font();
                    }
                    default:
                        return null;
                }
            } finally {
                settings.endGroup();
                settings.endGroup();
                settings.endGroup();
            }
        }
        catch(Throwable ex){
            final String title = environment.getMessageProvider().translate("ExplorerException", "Can't get data for explorer tree node");
            environment.getTracer().error(title, ex);
            
            switch (role) {
                case Qt.ItemDataRole.FontRole:
                    return QApplication.font();
                case Qt.ItemDataRole.TextAlignmentRole:
                    return new Qt.Alignment(Qt.AlignmentFlag.AlignLeft);
                case Qt.ItemDataRole.DisplayRole:
                    return "????";
                default:
                    return null;
            }            
        }
    }

    private String getSettingsGroup(final ExplorerItemView v) {
        if (v.isUserItemView()){
            return SettingNames.ExplorerTree.USER_GROUP;
        } else if (v.isEntityView()) {
            return SettingNames.ExplorerTree.EDITOR_GROUP;
        } else if (v.isGroupView()) {
            return SettingNames.ExplorerTree.SELECTOR_GROUP;
        } else if (v.isParagraphView()) {
            return SettingNames.ExplorerTree.PARAGRAPH_GROUP;
        } else {
            throw new IllegalArgumentException("unknown type of explorer item " + v.toString());
        }
    }

    @Override
    public int columnCount(final QModelIndex arg0) {
        return 1;
    }

    @Override
    public QModelIndex index(final int row, final int column, final QModelIndex parent) {
        if (row < 0 || row >= rowCount(parent)) {
            return null;//for internal call on remove last row.
        }
        if (column < 0 || column > 1) {
            return null;
        }
        final ExplorerTreeNode parentNode = parent != null ? (ExplorerTreeNode) findNodeByIndex(parent) : null,
                node = parentNode != null ? (ExplorerTreeNode) parentNode.getChildNodes().get(row) : root;
        QModelIndex result = indexes.get(node.getIndexInExplorerTree());
        if (result == null) {
            result = createIndex(row, column, node.hashCode());
            node.setInternalIndexInExplorerTree(result.internalId());
            indexes.put(node.getIndexInExplorerTree(), result);
            data.put(node.getIndexInExplorerTree(), node);
        }
        return result;
    }

    @Override
    public QModelIndex parent(final QModelIndex child) {
        final IExplorerTreeNode childNode = findNodeByIndex(child);
        return childNode != null ? findIndexByNode(childNode.getParentNode()) : null;
    }

    @Override
    public int rowCount(final QModelIndex parent) {
        if (parent == null) {
            return 1;
        }
        final IExplorerTreeNode parentNode = findNodeByIndex(parent);
        return parentNode.getChildNodes().size();
    }

    public void removeNode(final IExplorerTreeNode node) {
        final IExplorerTreeNode parent = node.getParentNode();
        if (parent != null) {
            final int row = parent.getChildNodes().indexOf(node);
            final QModelIndex parentIndex = findIndexByNode(parent);

            beginRemoveRows(parentIndex, row, row);
            try {
                for (int i = row + 1; i < parent.getChildNodes().size(); i++) {
                    clearCache(parent.getChildNodes().get(i), false);
                }
                clearCache(parent.getChildNodes().get(row), true);
                ((ExplorerTreeNode) parent).removeNode(row);
                for (int i = parent.getChildNodes().size() - 1; i >= 0; i--) {//create new indexes for child nodes
                    index(i, 0, parentIndex);
                }
            } finally {
                endRemoveRows();
            }
        }
    }

    private void clearCache(final IExplorerTreeNode node, final boolean recursively) {
        if (recursively) {
            final List<IExplorerTreeNode> toRemove = node.getChildNodesRecursively();
            for (IExplorerTreeNode childNode : toRemove) {
                indexes.remove(childNode.getIndexInExplorerTree());
                data.remove(childNode.getIndexInExplorerTree());
            }
        }
        indexes.remove(node.getIndexInExplorerTree());
        data.remove(node.getIndexInExplorerTree());
    }

    void insertNode(final ExplorerTreeNode parent, final IExplorerTreeNode newChild, final int index, final boolean fixIndex) {
        final QModelIndex parentIndex = findIndexByNode(parent);
        if (parentIndex != null) {            
            final int row = fixIndex ? parent.getActualIndexForNewChoosenEntity(index) : index;
            final int rowCount = rowCount(parentIndex);
            beginInsertRows(parentIndex, row, row);
            if (row < rowCount) {
                //remove cached indexes and nodes
                for (IExplorerTreeNode childNode : parent.getChildNodes()) {
                    clearCache(childNode, false);
                }
            }
            parent.addNode(newChild, row);
            for (int i = parent.getChildNodes().size() - 1; i >= 0; i--) {//create new indexes for child nodes
                index(i, 0, parentIndex);
            }
            endInsertRows();
        }
    }

    public ExplorerTreeNode getRootNode() {
        return root;
    }

    public final void reinit(final IProgressHandle progress) {

        {//reinit nodes
            final IExplorerTreeNode rootNode = getRootNode();
            final List<IExplorerTreeNode> nodesToView = new ArrayList<>();

            nodesToView.add(rootNode);
            nodesToView.addAll(rootNode.getChildNodesRecursively());
            if (progress != null) {
                progress.setText(Application.translate("ExplorerTree", "Updating Explorer Items"));
                progress.setMaximumValue(nodesToView.size());
            }
            int counter = 0;
            for (IExplorerTreeNode currentNode : nodesToView) {
                if (progress != null) {
                    progress.setValue(++counter);
                }
                currentNode.getView();//reinitNode
            }
            if (progress != null) {
                progress.setValue(0);
                progress.setMaximumValue(0);
            }
        }

        data.clear();
        indexes.clear();
        reset();
    }
}
