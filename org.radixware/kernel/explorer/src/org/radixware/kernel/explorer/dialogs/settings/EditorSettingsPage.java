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

import com.trolltech.qt.core.Qt;
import com.trolltech.qt.core.Qt.Alignment;
import com.trolltech.qt.core.Qt.AlignmentFlag;
import com.trolltech.qt.gui.QGridLayout;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import java.util.Arrays;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.localization.MessageProvider;


final class EditorSettingsPage extends SettingsTabSet implements ISettingsPage{
    
    private final static class GeneralPage extends AbstractSettingsTab{
        
        public GeneralPage(){
            setObjectName("rx_settings_dialog_editor_general_page");
        }

        @Override
        public void open(final IClientEnvironment environment, 
                                 final ISettingsProvider settingsProvider,
                                 final List<SettingsWidget> settingWidgets) {
            final QGridLayout layout = createLayout();
            final MessageProvider mp = environment.getMessageProvider();

            final SizesSettingsWidget swIconSizeInToolBars = new SizesSettingsWidget(environment, this,
                    SettingNames.EDITOR_GROUP, SettingNames.Editor.COMMON_GROUP,
                    SettingNames.Editor.Common.ICON_SIZE_IN_TOOLBARS, mp.translate("Settings Dialog", "Icon size in toolbars"),
                    layout);
            swIconSizeInToolBars.addToParent(0);
            swIconSizeInToolBars.readSettings(settingsProvider.getSettings());
            settingWidgets.add(swIconSizeInToolBars);
            
            final SizesSettingsWidget swIconSizeInTabs = new SizesSettingsWidget(environment, this,
                    SettingNames.EDITOR_GROUP, SettingNames.Editor.COMMON_GROUP,
                    SettingNames.Editor.Common.ICON_SIZE_IN_TABS, mp.translate("Settings Dialog", "Icon size in tabs"),
                    layout);
            swIconSizeInTabs.addToParent(1);
            swIconSizeInTabs.readSettings(settingsProvider.getSettings());
            settingWidgets.add(swIconSizeInTabs);

            final FontSettingsWidget swFontInTabs = new FontSettingsWidget(environment, this,
                    SettingNames.EDITOR_GROUP, SettingNames.Editor.COMMON_GROUP,
                    SettingNames.Editor.Common.FONT_IN_TABS, mp.translate("Settings Dialog", "Font in tabs"));
            swFontInTabs.addToParent(2, layout);
            swFontInTabs.readSettings(settingsProvider.getSettings());
            settingWidgets.add(swFontInTabs);

            final IntegerSettingsWidget ddListMaxItemsSetting = new IntegerSettingsWidget(environment, this,
                    SettingNames.EDITOR_GROUP, SettingNames.Editor.COMMON_GROUP,
                    SettingNames.Editor.Common.DROP_DOWN_LIST_ITEMS_LIMIT,
                    mp.translate("Settings Dialog", "Maximum number of items in drop-down list"),
                    layout,
                    0,Integer.MAX_VALUE,
                    mp.translate("Settings Dialog", "<unlimited>"));
            ddListMaxItemsSetting.addToParent(3);
            ddListMaxItemsSetting.readSettings(settingsProvider.getSettings());
            settingWidgets.add(ddListMaxItemsSetting);

            final QLabel fieldsHeadersTitleAlignLabel = new QLabel(mp.translate("Settings Dialog", "Field name alignment"));
            layout.addWidget(fieldsHeadersTitleAlignLabel, 4, 0);
            final ComboBoxAlignmentSettingsWidget fieldsHeadersTitleAlignment = new ComboBoxAlignmentSettingsWidget(environment,
                    this, SettingNames.EDITOR_GROUP,
                    SettingNames.Editor.COMMON_GROUP,
                    SettingNames.Editor.Common.TITLES_ALIGNMENT);
            fieldsHeadersTitleAlignment.setValues(Arrays.asList(Qt.AlignmentFlag.AlignLeft, Qt.AlignmentFlag.AlignRight), false);
            layout.addWidget(fieldsHeadersTitleAlignment, 4, 1);
            fieldsHeadersTitleAlignment.readSettings(settingsProvider.getSettings());
            settingWidgets.add(fieldsHeadersTitleAlignment);

            final CheckBoxSettingsWidget swRestoreTab = new CheckBoxSettingsWidget(environment, this,
                    SettingNames.EDITOR_GROUP,
                    SettingNames.Editor.COMMON_GROUP,
                    SettingNames.Editor.Common.RESTORE_TAB,
                    mp.translate("Settings Dialog", "Remember current tab"));
            layout.addWidget(swRestoreTab, 5, 0, 1, 2, Qt.AlignmentFlag.AlignLeft);
            swRestoreTab.readSettings(settingsProvider.getSettings());
            settingWidgets.add(swRestoreTab);

            final CheckBoxSettingsWidget swWarnAboutMandatory = new CheckBoxSettingsWidget(environment, this,
                    SettingNames.EDITOR_GROUP,
                    SettingNames.Editor.COMMON_GROUP,
                    SettingNames.Editor.Common.CHECK_MANDATORY_ON_CLOSE,
                    mp.translate("Settings Dialog", "Warn about undefined value for mandatory property when closing editor"));
            layout.addWidget(swWarnAboutMandatory, 6, 0, 1, 2, Qt.AlignmentFlag.AlignLeft);
            swWarnAboutMandatory.readSettings(settingsProvider.getSettings());
            settingWidgets.add(swWarnAboutMandatory);
            
            setLayout(layout);
        }        
    }        
    
    private final static class PropertiesPage extends QWidget implements ISettingsPage{
        
        private final String groupName;
        
        public PropertiesPage(final String groupName){
            this.groupName = groupName;
            setObjectName("rx_settings_dialog_editor_"+groupName+"_page");
        }

        @Override
        public void open(final IClientEnvironment environment, 
                                 final ISettingsProvider settingsProvider,
                                 final List<SettingsWidget> settingWidgets) {            
            final QVBoxLayout layout = new QVBoxLayout(this);
            layout.setAlignment(new Alignment(AlignmentFlag.AlignTop));
            final MessageProvider mp = environment.getMessageProvider();

            final PropertySettingsWidget swTitleSettings = new PropertySettingsWidget(environment, this,
                    SettingNames.EDITOR_GROUP,
                    SettingNames.Editor.PROPERTY_TITLES_GROUP,
                    groupName,
                    mp.translate("Settings Dialog", "Name"), false);
            layout.addWidget(swTitleSettings, 0, AlignmentFlag.AlignTop);
            swTitleSettings.readSettings(settingsProvider.getSettings());
            settingWidgets.add(swTitleSettings);

            final InheritablePropertySettingsWidget swValueSettings = new InheritablePropertySettingsWidget(environment, this,
                    SettingNames.EDITOR_GROUP,
                    SettingNames.Editor.PROPERTY_VALUES_GROUP,
                    groupName, mp.translate("Settings Dialog", "Value"));
            layout.addWidget(swValueSettings);
            swValueSettings.readSettings(settingsProvider.getSettings());
            settingWidgets.add(swValueSettings);
        }
        
    }
    
    public EditorSettingsPage(final IClientEnvironment environment, 
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
        
        final SettingsTabSet propertiesSettings = new SettingsTabSet(environment, settingsProvider, settingWidgets);
        propertiesSettings.setObjectName("rx_settings_dialog_editor_properties_page");
        propertiesSettings.addTab(new PropertiesPage(SettingNames.Properties.READONLY_PROPERTY), mp.translate("Settings Dialog", "Readonly"));
        propertiesSettings.addTab(new PropertiesPage(SettingNames.Properties.MANDATORY_PROPERTY), mp.translate("Settings Dialog", "Mandatory"));
        propertiesSettings.addTab(new PropertiesPage(SettingNames.Properties.OTHER_PROPERTY), mp.translate("Settings Dialog", "Other"));        
        addTab(propertiesSettings,  mp.translate("Settings Dialog", "Properties"));
    }
    
}
