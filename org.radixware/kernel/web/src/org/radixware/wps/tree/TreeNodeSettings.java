/*
* Copyright (c) 2008-2016, Compass Plus Limited. All rights reserved.
*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at http://mozilla.org/MPL/2.0/.
* This Source Code is distributed WITHOUT ANY WARRANTY; including any 
* implied warranties but not limited to warranty of MERCHANTABILITY 
* or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
* License, v. 2.0. for more details.
*/

package org.radixware.wps.tree;

import java.awt.Color;
import java.util.EnumMap;
import java.util.Objects;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.client.views.IExplorerItemView;
import org.radixware.wps.WpsSettings;
import org.radixware.wps.icons.WpsIcon;


final class TreeNodeSettings {
    
    private final static Color DEFAULT_SELECTED_BACKGROUND_COLOR = Color.decode("#3399ff");
    
    public enum NodeType {        
        
        SELECTOR(SettingNames.ExplorerTree.SELECTOR_GROUP, Color.WHITE, Color.BLACK),
        EDITOR(SettingNames.ExplorerTree.EDITOR_GROUP, Color.WHITE, Color.BLACK),
        PARAGRAPH(SettingNames.ExplorerTree.PARAGRAPH_GROUP, Color.WHITE, Color.decode("#000068")),
        CUSTOM(SettingNames.ExplorerTree.USER_GROUP, Color.WHITE, Color.decode("#0000ff"));
        
        private final String settingName;
        private final Color defaultBackgroundColor;
        private final Color defaultForegroundColor;
        
        private NodeType(final String settingName,
                                    final Color background,
                                    final Color foreground){
            this.settingName = settingName;
            this.defaultBackgroundColor = background;
            this.defaultForegroundColor = foreground;
        }
        
        public static NodeType detect(final IExplorerItemView v){
            if (v==null){
                return null;
            }else if (v.isUserItemView()) {
                return CUSTOM;
            } else if (v.isEntityView()) {
                return EDITOR;
            } else if (v.isGroupView()) {
                return SELECTOR;
            } else if (v.isParagraphView()) {
                return PARAGRAPH;
            } else {
                throw new IllegalArgumentException("unknown type of explorer item " + v.toString());
            }
        }
        
        public final String getSettingName(){
            return settingName;
        }

        public Color getDefaultBackgroundColor() {
            return defaultBackgroundColor;
        }

        public Color getDefaultForegroundColor() {
            return defaultForegroundColor;
        }
                
    }
    
    private final boolean isIconVisible;
    private final Color backgroundColor;
    private final Color foregroundColor;
    private final Color selectedForegroundColor;
    private final Color selectedBackgroundColor;

    private TreeNodeSettings(final Color foregroundColor, 
                                           final Color backgroundColor,                                            
                                           final Color selectedForegroundColor,
                                           final Color selectedBackgroundColor,                                           
                                           final boolean isIconVisible) {
        this.isIconVisible = isIconVisible;
        this.backgroundColor = backgroundColor;
        this.foregroundColor = foregroundColor;
        this.selectedForegroundColor = selectedForegroundColor;
        this.selectedBackgroundColor = selectedBackgroundColor;
    }

    public boolean isIconVisible() {
        return isIconVisible;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public Color getForegroundColor() {
        return foregroundColor;
    }

    public Color getSelectedForegroundColor() {
        return selectedForegroundColor;
    }
    
    public Color getSelectedBackgroundColor() {
        return selectedBackgroundColor;
    }    

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + (this.isIconVisible ? 1 : 0);
        hash = 67 * hash + Objects.hashCode(this.backgroundColor);
        hash = 67 * hash + Objects.hashCode(this.foregroundColor);
        hash = 67 * hash + Objects.hashCode(this.selectedForegroundColor);
        hash = 67 * hash + Objects.hashCode(this.selectedBackgroundColor);
        return hash;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TreeNodeSettings other = (TreeNodeSettings) obj;
        if (this.isIconVisible != other.isIconVisible) {
            return false;
        }
        if (!Objects.equals(this.backgroundColor, other.backgroundColor)) {
            return false;
        }
        if (!Objects.equals(this.foregroundColor, other.foregroundColor)) {
            return false;
        }
        if (!Objects.equals(this.selectedForegroundColor, other.selectedForegroundColor)) {
            return false;
        }
        if (!Objects.equals(this.selectedBackgroundColor, other.selectedBackgroundColor)) {
            return false;
        }
        return true;
    }
    
    public void apply(final TreeNode node){
        if (isIconVisible){
            final Icon icon = node.getExplorerTreeNode().getView().getIcon();
            if (icon instanceof WpsIcon){
                node.setIcon((WpsIcon)icon);
                node.setBackground(backgroundColor);
                node.setForeground(foregroundColor);
                node.setSelectedNodeForeground(selectedForegroundColor);
            }
        }else{
            node.setIcon(null);
        }
    }
    
    public static EnumMap<NodeType,TreeNodeSettings> readSettings(final WpsSettings configStore){
        final EnumMap<NodeType,TreeNodeSettings> settings = new EnumMap<>(NodeType.class);
        configStore.beginGroup(SettingNames.SYSTEM);
        configStore.beginGroup(SettingNames.EXPLORER_TREE_GROUP);
        try{
            final Color selectedForegroundColor;
            final Color selectedBackgroundColor;
            configStore.beginGroup(SettingNames.ExplorerTree.COMMON_GROUP);
            try{
                selectedForegroundColor = 
                    configStore.readColor(SettingNames.ExplorerTree.Common.TREE_SELECTED_ITEM_FONT_COLOR, Color.WHITE);
                selectedBackgroundColor =
                    configStore.readColor(SettingNames.ExplorerTree.Common.TREE_SELECTED_ITEM_BACKGROUND, DEFAULT_SELECTED_BACKGROUND_COLOR);
            }finally{
                configStore.endGroup();
            }                
            for (NodeType nodeType: NodeType.values()){
                configStore.beginGroup(nodeType.getSettingName());
                try{
                    settings.put(nodeType, readSettings(configStore, nodeType, selectedForegroundColor, selectedBackgroundColor));
                }finally{
                    configStore.endGroup();
                }
            }
        }finally{
            configStore.endGroup();
            configStore.endGroup();
        }
        return settings;
    }

    private static TreeNodeSettings readSettings(final WpsSettings configStore, 
                                                                       final NodeType nodeType,
                                                                       final Color selectedForegroundColor,
                                                                       final Color selectedBackgroundColor){
        final boolean show_icons = 
            configStore.readBoolean(SettingNames.ExplorerTree.Common.SHOW_ICONS, true);
        final Color backgroundColor = 
            configStore.readColor(SettingNames.ExplorerTree.Common.BACKGROUND_COLOR, nodeType.getDefaultBackgroundColor());
        final Color foregroundColor = 
            configStore.readColor(SettingNames.ExplorerTree.Common.FOREGROUND_COLOR, nodeType.getDefaultForegroundColor());
        return new TreeNodeSettings(foregroundColor, 
                                                     backgroundColor,
                                                     selectedForegroundColor,
                                                     selectedBackgroundColor,
                                                     show_icons);
    }    
}
