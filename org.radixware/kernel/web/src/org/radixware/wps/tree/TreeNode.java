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

import java.awt.Color;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.tree.nodes.ExplorerTreeNode;
import org.radixware.kernel.common.client.tree.nodes.IExplorerTreeNode;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.client.views.IExplorerItemView;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.WpsSettings;
import org.radixware.wps.icons.WpsIcon;
import org.radixware.wps.rwt.tree.Node;
import org.radixware.wps.settings.ISettingsChangeListener;

class TreeNode extends Node {

    private final IExplorerTreeNode node;
    private final WpsTree tree;
    private final WpsEnvironment env;
    private final ISettingsChangeListener l = new ISettingsChangeListener() {
        @Override
        public void onSettingsChanged() {
            applySettings();
        }
    };

    private static Children createChildren(WpsTree tree, IExplorerTreeNode node) {
        IExplorerItemView view = node.getView();
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

    protected TreeNode(WpsTree tree, IExplorerTreeNode node) {
        super(createChildren(tree, node));
        this.node = node;
        this.tree = tree;
        this.env = ((WpsEnvironment) getEnvironment());
        update();
    }

    private void applySettings() {
        ExplorerTreeNode n = getExplorerTreeNode();
        showIcons(n.getView());
        applyColorSettings(n.getView());
    }

    private void showIcons(IExplorerItemView view) {
        Icon icon = node.getView().getIcon();
        WpsSettings settings = env.getConfigStore();
        if (settings != null) {
            try {
                settings.beginGroup(SettingNames.SYSTEM);
                settings.beginGroup(SettingNames.EXPLORER_TREE_GROUP);
                settings.beginGroup(getSettingsGroup(view));
                final boolean show_icons = settings.readBoolean(SettingNames.ExplorerTree.Common.SHOW_ICONS, true);
                if (show_icons) {
                    setIcon((WpsIcon) icon);
                } else {
                    setIcon(null);
                }
            } finally {
                settings.endGroup();
                settings.endGroup();
                settings.endGroup();
            }
        } else {
            if (icon instanceof WpsIcon) {
                setIcon((WpsIcon) icon);
            }
        }
    }

    private void applyColorSettings(final IExplorerItemView view) {        
        final WpsSettings settings = env.getConfigStore();
        if (settings != null) {
            settings.beginGroup(SettingNames.SYSTEM);
            settings.beginGroup(SettingNames.EXPLORER_TREE_GROUP);            
            try {                
                final Color background, foreground;
                settings.beginGroup(getSettingsGroup(view));
                try{
                    background = 
                        settings.readColor(SettingNames.ExplorerTree.Common.BACKGROUND_COLOR, getDefaultBackgroundColor(view));
                    foreground = 
                        settings.readColor(SettingNames.ExplorerTree.Common.FOREGROUND_COLOR, getDefaultForegroundColor(view));
                }finally{
                    settings.endGroup();
                }
                setBackground(background);
                setForeground(foreground);
                
                final Color selectedFont, selectedBackground;
                settings.beginGroup(SettingNames.ExplorerTree.COMMON_GROUP);
                try{
                    selectedFont = 
                        settings.readColor(SettingNames.ExplorerTree.Common.TREE_SELECTED_ITEM_FONT_COLOR, Color.BLACK);
                    selectedBackground = 
                        settings.readColor(SettingNames.ExplorerTree.Common.TREE_SELECTED_ITEM_BACKGROUND, Color.decode("#3399ff"));
                }finally{
                    settings.endGroup();    
                }                
                //setSelectedNodeBackground(selectedBackground);
                setSelectedNodeForeground(selectedFont);
            } finally {
                settings.endGroup();
                settings.endGroup();
            }
        }
    }
    
    private static Color getDefaultBackgroundColor(final IExplorerItemView v){
        return Color.WHITE;
    }
    
    private static Color getDefaultForegroundColor(final IExplorerItemView v){
        if (v.isUserItemView()) {
            return Color.decode("#0000ff");
        } else if (v.isEntityView()) {
            return Color.BLACK;
        } else if (v.isGroupView()) {
            return Color.BLACK;
        } else if (v.isParagraphView()) {
            return Color.decode("#000068");
        } else {
            throw new IllegalArgumentException("unknown type of explorer item " + v.toString());
        }        
    }

    private static String getSettingsGroup(final IExplorerItemView v) {
        if (v.isUserItemView()) {
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

    public ExplorerTreeNode getExplorerTreeNode() {
        return (ExplorerTreeNode) node;
    }

    public TreeNode add(int index, IExplorerTreeNode childNode) {
        Node.Children children = getChildNodes();
        if (children == Node.Children.LEAF) {
            children = new org.radixware.wps.tree.DefaultChildren(tree, node);
            setChildNodes(children);
        }

        TreeNode treeNode = new TreeNode(tree, childNode);
        children.add(index, treeNode);
        int realIndex = children.getNodes().indexOf(treeNode);

        getExplorerTreeNode().addNode(childNode, realIndex);
        return treeNode;
    }

    @Override
    public void remove() {
        super.remove();
        if (l != null && env != null) {
            env.removeSettingsChangeListener(l);
        }
    }

    @Override
    public final void update() {
        super.update();
        IExplorerItemView view = node.getView();
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
        if(env != null && l != null){
            env.removeSettingsChangeListener(l);
        }
        if (env != null) {//npe after disconnect while saving current position in the tree
            env.addSettingsChangeListener(l);
            showIcons(view);
            applyColorSettings(view);
        }
    }

}
