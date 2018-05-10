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
package org.radixware.wps.tree;

import java.util.Objects;
import org.radixware.kernel.common.client.tree.nodes.ExplorerTreeNode;
import org.radixware.kernel.common.client.tree.nodes.IExplorerTreeNode;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.client.views.IExplorerItemView;
import org.radixware.wps.icons.WpsIcon;
import org.radixware.wps.rwt.tree.Node;

class TreeNode extends Node {

    private final ExplorerTreeNode node;
    private final WpsTree tree;
    private TreeNodeSettings currentSettings;

    protected TreeNode(final WpsTree tree, 
                                   final IExplorerTreeNode node) {
        super(createChildren(tree, node));
        this.node = (ExplorerTreeNode)node;
        this.tree = tree;
        setObjectName(node.getName());
        update();
        applySettings(tree.getNodeSettings(getType()));
    }
    
    public final void applySettings(final TreeNodeSettings settings){
        if (settings!=null && !Objects.equals(currentSettings, settings)){
            if (settings.isIconVisible()){
                final Icon icon = getExplorerTreeNode().getView().getIcon();
                if (icon instanceof WpsIcon){
                    setIcon((WpsIcon)icon);
                }            
            }else{
                setIcon(null);
            }
            if (currentSettings==null){
                setBackground(settings.getBackgroundColor());
                setForeground(settings.getForegroundColor());
                setSelectedNodeForeground(settings.getSelectedForegroundColor());
                setSelectedNodeBackground(settings.getSelectedBackgroundColor());
            }else{
                if (currentSettings.getBackgroundColor()!=settings.getBackgroundColor()){
                    setBackground(settings.getBackgroundColor());
                }
                if (currentSettings.getForegroundColor()!=settings.getForegroundColor()){
                    setForeground(settings.getForegroundColor());
                }
                if (currentSettings.getSelectedForegroundColor()!=settings.getSelectedForegroundColor()){
                    setSelectedNodeForeground(settings.getSelectedForegroundColor());
                }
                if (currentSettings.getSelectedBackgroundColor()!=settings.getSelectedBackgroundColor()){
                    setSelectedNodeBackground(settings.getSelectedBackgroundColor());
                }
            }
            currentSettings = settings;
        }
    }
    
    public final TreeNodeSettings.NodeType getType(){
        return TreeNodeSettings.NodeType.detect(getExplorerTreeNode().getView());
    }

    public final ExplorerTreeNode getExplorerTreeNode() {
        return node;
    }

    public final TreeNode add(final int index, final IExplorerTreeNode childNode) {
        Node.Children children = getChildNodes();
        if (children == Node.Children.LEAF) {
            children = new org.radixware.wps.tree.DefaultChildren(tree, node);
            setChildNodes(children);
        }

        final TreeNode treeNode = new TreeNode(tree, childNode);
        children.add(index, treeNode);
        final int realIndex = children.getNodes().indexOf(treeNode);

        getExplorerTreeNode().addNode(childNode, realIndex);
        return treeNode;
    }

    @Override
    public final void update() {
        super.update();
        final IExplorerItemView view = node.getView();
        if (view != null) {
            if (view.isParagraphView()) {
                html.addClass("rwt-ui-tree-radix-paragraph");
            } else if (view.isEntityView() || view.isChoosenObject()) {
                html.addClass("rwt-ui-tree-radix-entity");
            } else if (view.isGroupView()) {
                html.addClass("rwt-ui-tree-radix-group");
            }
        }

        setDisplayName(node.getView().getTitle());
    }
    
    private static Children createChildren(final WpsTree tree, final IExplorerTreeNode node) {
        final IExplorerItemView view = node.getView();
        if (view != null) {
            if (view.isParagraphView()) {
                if (view.getChildsCount() == 0) {
                    return Children.LEAF;
                } else {
                    return new org.radixware.wps.tree.DefaultChildren(tree, node);
                }
            } else if (view.isEntityView() || view.isChoosenObject()) {
                if (view.getChildsCount() == 0) {
                    return Children.LEAF;
                } else {
                    return new org.radixware.wps.tree.DefaultChildren(tree, node);
                }
            } else if (view.isGroupView()) {
                if (view.getChildsCount() == 0) {
                    return Children.LEAF;
                } else {
                    return new org.radixware.wps.tree.DefaultChildren(tree, node);
                }
            } else {
                return Children.LEAF;
            }
        } else {
            return Children.LEAF;
        }
    }
    

}
