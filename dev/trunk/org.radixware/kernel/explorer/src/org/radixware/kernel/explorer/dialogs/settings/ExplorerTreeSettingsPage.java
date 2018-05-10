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

package org.radixware.kernel.explorer.dialogs.settings;

import com.trolltech.qt.core.Qt.AlignmentFlag;
import com.trolltech.qt.gui.QGridLayout;
import com.trolltech.qt.gui.QLabel;
import java.util.Arrays;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.localization.MessageProvider;

final class ExplorerTreeSettingsPage extends SettingsTabSet implements ISettingsPage{
    
    private final static class GeneralPage extends AbstractSettingsTab{
        
        public GeneralPage(){
            super();
            setObjectName("rx_settings_dialog_tree_general_page");
        }
        
        @Override
        public void open(final IClientEnvironment environment, 
                                  final ISettingsProvider settingsProvider,
                                  final List<SettingsWidget> settingWidgets) {            
            final QGridLayout layout = createLayout();
            final MessageProvider mp = environment.getMessageProvider();

            final SizesSettingsWidget iconSize = new SizesSettingsWidget(environment, this,
                    SettingNames.EXPLORER_TREE_GROUP,
                    SettingNames.ExplorerTree.COMMON_GROUP,
                    SettingNames.ExplorerTree.Common.ICON_SIZE, mp.translate("Settings Dialog", "Icon size"), layout);
            iconSize.addToParent(0);
            iconSize.readSettings(settingsProvider.getSettings());
            settingWidgets.add(iconSize);

            final GroupColorSettingsWidget colorSettingsGroup = new GroupColorSettingsWidget(environment, this,
                    SettingNames.EXPLORER_TREE_GROUP,
                    SettingNames.ExplorerTree.COMMON_GROUP, layout);
            colorSettingsGroup.addColorWidget(1,
                    SettingNames.ExplorerTree.Common.TREE_BACKGROUND, mp.translate("Settings Dialog", "Background color"));
            colorSettingsGroup.addColorWidget(2,
                    SettingNames.ExplorerTree.Common.TREE_SELECTED_ITEM_FONT_COLOR, mp.translate("Settings Dialog","Tree selected item font color"));
            colorSettingsGroup.addColorWidget(3,
                    SettingNames.ExplorerTree.Common.TREE_SELECTED_ITEM_BACKGROUND,
                    mp.translate("Settings Dialog","Tree selected item background color"));            
            colorSettingsGroup.readSettings(settingsProvider.getSettings());
            settingWidgets.add(colorSettingsGroup);

            final ComboBoxAlignmentSettingsWidget treeAlignmentsSettings = new ComboBoxAlignmentSettingsWidget(environment,
                    this, SettingNames.EXPLORER_TREE_GROUP,
                    SettingNames.ExplorerTree.COMMON_GROUP,
                    SettingNames.ExplorerTree.Common.TREE_AREA);
            treeAlignmentsSettings.setValues(
                    Arrays.asList(AlignmentFlag.AlignLeft, AlignmentFlag.AlignRight, AlignmentFlag.AlignTop, AlignmentFlag.AlignBottom), true);
            layout.addWidget(new QLabel(mp.translate("Settings Dialog", "Position"), this), 4, 0);
            layout.addWidget(treeAlignmentsSettings, 4, 1);
            treeAlignmentsSettings.readSettings(settingsProvider.getSettings());
            settingWidgets.add(treeAlignmentsSettings);

            final CheckBoxSettingsWidget swRestorePos = new CheckBoxSettingsWidget(environment,
                    this,
                    SettingNames.EXPLORER_TREE_GROUP,
                    SettingNames.ExplorerTree.COMMON_GROUP,
                    SettingNames.ExplorerTree.Common.RESTORE_POSITION,
                    mp.translate("Settings Dialog", "Remember current item"));
            layout.addWidget(swRestorePos, 6, 0);
            swRestorePos.readSettings(settingsProvider.getSettings());
            settingWidgets.add(swRestorePos);

            final CheckBoxSettingsWidget swKeepUserEI = new CheckBoxSettingsWidget(environment,
                    this,
                    SettingNames.EXPLORER_TREE_GROUP,
                    SettingNames.ExplorerTree.COMMON_GROUP,
                    SettingNames.ExplorerTree.Common.KEEP_USER_EI,
                    mp.translate("Settings Dialog", "Save user-defined items"));
            layout.addWidget(swKeepUserEI, 7, 0);
            swKeepUserEI.readSettings(settingsProvider.getSettings());
            settingWidgets.add(swKeepUserEI);
            
            setLayout(layout);
        }        
    }
    
    private static class TreeNodePage  extends AbstractSettingsTab{
        
        private final String groupName;
        
        public TreeNodePage(final String groupName){
            this.groupName = groupName;
            setObjectName("rx_settings_dialog_tree_"+groupName+"_page");
        }
        
        @Override
        public void open(final IClientEnvironment environment, 
                                  final ISettingsProvider settingsProvider,
                                  final List<SettingsWidget> settingWidgets) {            
            final QGridLayout layout = createLayout();
            final MessageProvider mp = environment.getMessageProvider();
            
            final FontSettingsWidget fontSettings = new FontSettingsWidget(environment, this,
                    SettingNames.EXPLORER_TREE_GROUP,
                    groupName,
                    SettingNames.ExplorerTree.Common.FONT, mp.translate("Settings Dialog", "Font"));
            fontSettings.addToParent(0, layout);
            fontSettings.readSettings(settingsProvider.getSettings());
            settingWidgets.add(fontSettings);

            final GroupColorSettingsWidget colorSettingsGroup = new GroupColorSettingsWidget(environment, this,
                    SettingNames.EXPLORER_TREE_GROUP,
                    groupName, layout);
            colorSettingsGroup.addColorWidget(1, 
                    SettingNames.ExplorerTree.Common.BACKGROUND_COLOR,
                    mp.translate("Settings Dialog", "Background color"));
            colorSettingsGroup.addColorWidget(2,
                    SettingNames.ExplorerTree.Common.FOREGROUND_COLOR,
                    mp.translate("Settings Dialog", "Foreground color"));
            colorSettingsGroup.readSettings(settingsProvider.getSettings());
            settingWidgets.add(colorSettingsGroup);
            
            final CheckBoxSettingsWidget showIcons = new CheckBoxSettingsWidget(environment, this,
                    SettingNames.EXPLORER_TREE_GROUP,
                    groupName,
                    SettingNames.ExplorerTree.Common.SHOW_ICONS, mp.translate("Settings Dialog", "Show icons"));                        
            layout.addWidget(showIcons, 3, 0);
            showIcons.readSettings(settingsProvider.getSettings());
            settingWidgets.add(showIcons);
            
            setLayout(layout);
        }
    }        
    
    private final static class ObjectNodePage extends TreeNodePage{
        
        public ObjectNodePage(){
            super(SettingNames.ExplorerTree.EDITOR_GROUP);
        }

        @Override
        public void open(final IClientEnvironment environment, 
                                 final ISettingsProvider settingsProvider, 
                                 final List<SettingsWidget> settingWidgets) {
            super.open(environment, settingsProvider, settingWidgets);
            final QGridLayout layout = (QGridLayout)layout();
            final CheckBoxSettingsWidget openAfterInsert = new CheckBoxSettingsWidget(environment,
                    this,
                    SettingNames.EXPLORER_TREE_GROUP,
                    SettingNames.ExplorerTree.EDITOR_GROUP,
                    SettingNames.ExplorerTree.Editor.EDIT_AFTER_INSERT,
                    environment.getMessageProvider().translate("Settings Dialog","Open editor after inserting object into tree"));
            layout.addWidget(openAfterInsert, 4, 0, 1, 3, AlignmentFlag.AlignLeft);
            openAfterInsert.readSettings(settingsProvider.getSettings());
            settingWidgets.add(openAfterInsert);
        }
        
    }    
    
    public ExplorerTreeSettingsPage(final IClientEnvironment environment,  
                                                     final ISettingsProvider settingsProvider, 
                                                     final List<SettingsWidget> settingWidgets){
        super(environment, settingsProvider, settingWidgets);
    }

    @Override
    public void open(final IClientEnvironment environment, 
                             final ISettingsProvider settingsProvider, 
                             final List<SettingsWidget> settingWidgets) {
        final MessageProvider mp = environment.getMessageProvider();
        addTab(new GeneralPage(), mp.translate("Settings Dialog", "General"));
        addTab(new TreeNodePage(SettingNames.ExplorerTree.PARAGRAPH_GROUP), mp.translate("Settings Dialog", "Paragraph"));
        addTab(new TreeNodePage(SettingNames.ExplorerTree.SELECTOR_GROUP), mp.translate("Settings Dialog", "Selector"));
        addTab(new ObjectNodePage(), mp.translate("Settings Dialog", "Editor"));
        addTab(new TreeNodePage(SettingNames.ExplorerTree.USER_GROUP), mp.translate("Settings Dialog", "User-Defined Item"));        
    }

}
