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
import com.trolltech.qt.gui.QGroupBox;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QListView;
import com.trolltech.qt.gui.QListWidget;
import com.trolltech.qt.gui.QListWidgetItem;
import com.trolltech.qt.gui.QSizePolicy;
import com.trolltech.qt.gui.QStackedWidget;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.enums.ESelectorRowStyle;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.explorer.env.IExplorerSettings;
import org.radixware.kernel.explorer.text.ExplorerTextOptions;


final class SelectorSettingsPage extends SettingsTabSet implements ISettingsPage{    
    
    private final static class GeneralPage extends AbstractSettingsTab{
        
        public GeneralPage(){
            setObjectName("rx_settings_dialog_selector_general_page");
        }

        @Override
        public void open(final IClientEnvironment environment, 
                                 final ISettingsProvider settingsProvider,
                                 final List<SettingsWidget> settingWidgets) {
            final QGridLayout layout = createLayout();
            final MessageProvider mp = environment.getMessageProvider();
            
            final SizesSettingsWidget swIconSizeInToolBars = new SizesSettingsWidget(environment, this,
                    SettingNames.SELECTOR_GROUP,
                    SettingNames.Selector.COMMON_GROUP,
                    SettingNames.Selector.Common.ICON_SIZE_IN_SELECTOR_TOOLBARS,
                    mp.translate("Settings Dialog", "Icon size in selector toolbars"), layout);
            swIconSizeInToolBars.addToParent(0);
            swIconSizeInToolBars.readSettings(settingsProvider.getSettings());
            settingWidgets.add(swIconSizeInToolBars);

            final CheckBoxSettingsWidget swRestoreFilter = new CheckBoxSettingsWidget(environment, this,
                    SettingNames.SELECTOR_GROUP,
                    SettingNames.Selector.COMMON_GROUP,
                    SettingNames.Selector.Common.SAVE_FILTER,
                    mp.translate("Settings Dialog", "Remember current filter"));
            layout.addWidget(swRestoreFilter, 1, 0, 1, 2, Qt.AlignmentFlag.AlignLeft);
            swRestoreFilter.readSettings(settingsProvider.getSettings());
            settingWidgets.add(swRestoreFilter);

            final FontSettingsWidget swColumnTitleFont = new FontSettingsWidget(environment, this,
                    SettingNames.SELECTOR_GROUP,
                    SettingNames.Selector.COMMON_GROUP,
                    SettingNames.Selector.Common.HEADER_FONT_IN_SELECTOR,
                    mp.translate("Settings Dialog","Header font in selector"));
            swColumnTitleFont.addToParent(2, layout);
            swColumnTitleFont.readSettings(settingsProvider.getSettings());
            settingWidgets.add(swColumnTitleFont);

            final QLabel titleAlignLabel = new QLabel(mp.translate("Settings Dialog", "Header alignment"));
            layout.addWidget(titleAlignLabel, 3, 0);
            final ComboBoxAlignmentSettingsWidget swColumnTitleAlignments = new ComboBoxAlignmentSettingsWidget(environment,
                    this, SettingNames.SELECTOR_GROUP,
                    SettingNames.Selector.COMMON_GROUP,
                    SettingNames.Selector.Common.TITLES_ALIGNMENT);
            layout.addWidget(swColumnTitleAlignments, 3, 1);
            swColumnTitleAlignments.readSettings(settingsProvider.getSettings());
            settingWidgets.add(swColumnTitleAlignments);

            final ColorSettingsWidget frameColorWidget
                    = new ColorSettingsWidget(environment, this, SettingNames.SELECTOR_GROUP,
                            SettingNames.Selector.COMMON_GROUP,
                            SettingNames.Selector.Common.FRAME_COLOR,
                            mp.translate("Settings Dialog", "Current cell frame color"));
            frameColorWidget.addToParent(5, layout);
            frameColorWidget.readSettings(settingsProvider.getSettings());
            settingWidgets.add(frameColorWidget);

            final ColorSettingsWidget swRowFrameColor
                    = new ColorSettingsWidget(environment, this, SettingNames.SELECTOR_GROUP,
                            SettingNames.Selector.COMMON_GROUP,
                            SettingNames.Selector.Common.ROW_FRAME_COLOR,
                            mp.translate("Settings Dialog", "Current row frame color"));
            swRowFrameColor.addToParent(6, layout);
            swRowFrameColor.readSettings(settingsProvider.getSettings());
            settingWidgets.add(swRowFrameColor);

            final ColorSettingsWidget swSelectedRowBackground
                    = new ColorSettingsWidget(environment, this, SettingNames.SELECTOR_GROUP,
                            SettingNames.Selector.COMMON_GROUP,
                            SettingNames.Selector.Common.SELECTED_ROW_COLOR,
                            mp.translate("Settings Dialog", "Selected object background color"));
            swSelectedRowBackground.addToParent(7, layout);
            swSelectedRowBackground.readSettings(settingsProvider.getSettings());
            settingWidgets.add(swSelectedRowBackground);
            
            final CheckBoxSettingsWidget swMultipleSelectionByDefault = new CheckBoxSettingsWidget(environment, this,
                    SettingNames.SELECTOR_GROUP,
                    SettingNames.Selector.COMMON_GROUP,
                    SettingNames.Selector.Common.MULTIPLE_SELECTION_MODE_ENABLED_BY_DEFAULT,
                    mp.translate("Settings Dialog", "Enable multiple selection mode by default"));
            layout.addWidget(swMultipleSelectionByDefault, 8, 0, 1, 2, Qt.AlignmentFlag.AlignLeft);
            swMultipleSelectionByDefault.readSettings(settingsProvider.getSettings());
            settingWidgets.add(swMultipleSelectionByDefault);

            final QVBoxLayout mainLayout = new QVBoxLayout(this);
            final AlternativeColorSettingsWidget altColorSettingsWidget = new AlternativeColorSettingsWidget(environment,
                    this, SettingNames.SELECTOR_GROUP,
                    SettingNames.Selector.COMMON_GROUP,
                    SettingNames.Selector.Common.SLIDER_VALUE);
            final QHBoxLayout horizontalLayout = new QHBoxLayout();
            horizontalLayout.setAlignment(new Alignment(AlignmentFlag.AlignLeft));
            horizontalLayout.addWidget(altColorSettingsWidget);
            final QGroupBox groupBox = new QGroupBox(mp.translate("Settings Dialog", "Alternative Background Color"));
            groupBox.setLayout(horizontalLayout);
            altColorSettingsWidget.setSizePolicy(QSizePolicy.Policy.Maximum, QSizePolicy.Policy.Maximum);
            
            groupBox.setSizePolicy(QSizePolicy.Policy.Maximum, QSizePolicy.Policy.Maximum);
            mainLayout.setAlignment(new Alignment(AlignmentFlag.AlignTop,
                    AlignmentFlag.AlignLeft));

            mainLayout.addLayout(layout);
            mainLayout.addWidget(groupBox);
            altColorSettingsWidget.readSettings(settingsProvider.getSettings());
            settingWidgets.add(altColorSettingsWidget);            
        }
    }
    
    private final static class StylesPage extends QWidget implements ISettingsPage{
        
        private PreviewWidget previewWidget;
        private QStackedWidget innerStackedWidget;
        private QListWidget innerListWidget;
        private List<SettingsWidget> settingWidgets;
        private final Map<ESelectorRowStyle, Integer> widgetIndexByRowStyle = new HashMap<>();
        private final IClientEnvironment environment;
        private final ISettingsProvider settingsProvider;
        
        public StylesPage(final IClientEnvironment env, final ISettingsProvider settingsProvider){
            setObjectName("rx_settings_dialog_selector_styles_page");
            environment = env;
            this.settingsProvider = settingsProvider;
        }

        @Override
        public void open(final IClientEnvironment environment, 
                                 final ISettingsProvider provider,
                                 final List<SettingsWidget> settingWidgets) {            
            this.settingWidgets = settingWidgets;
            final MessageProvider mp = environment.getMessageProvider();
            final QHBoxLayout mainLayout = new QHBoxLayout(this);
            
            mainLayout.setWidgetSpacing(0);
            mainLayout.setContentsMargins(8, 0, 0, 0);
            mainLayout.setAlignment(new Alignment(AlignmentFlag.AlignLeft, AlignmentFlag.AlignTop));

            final QVBoxLayout verticalLayout = new QVBoxLayout();
            verticalLayout.setContentsMargins(0, 9, 0, 10);

            innerListWidget = new QListWidget(this);
            innerListWidget.setViewMode(QListView.ViewMode.ListMode);
            innerListWidget.setFlow(QListView.Flow.TopToBottom);
            innerListWidget.setMovement(QListView.Movement.Static);
            innerListWidget.setSpacing(0);
            innerListWidget.currentRowChanged.connect(this,"onInnerListWidgetChangeIndex(int)");
            innerListWidget.setMouseTracking(true);
            verticalLayout.addWidget(innerListWidget);

            previewWidget = new PreviewWidget(this, environment);
            verticalLayout.addWidget(previewWidget);

            innerStackedWidget = new QStackedWidget();
            
            final LinkedHashMap<ESelectorRowStyle, String> params = new LinkedHashMap<>();

            params.put(ESelectorRowStyle.NORMAL, mp.translate("Settings Dialog", "Normal"));
            params.put(ESelectorRowStyle.FAVORITE, mp.translate("Settings Dialog", "Favorite"));
            params.put(ESelectorRowStyle.UNIMPORTANT, mp.translate("Settings Dialog", "Unimportant"));
            params.put(ESelectorRowStyle.IMPORTANT, mp.translate("Settings Dialog", "Important"));
            params.put(ESelectorRowStyle.POOR, mp.translate("Settings Dialog", "Poor"));
            params.put(ESelectorRowStyle.BAD, mp.translate("Settings Dialog", "Bad"));
            params.put(ESelectorRowStyle.VERY_BAD, mp.translate("Settings Dialog", "Very Bad"));
            params.put(ESelectorRowStyle.RATHER_GOOD, mp.translate("Settings Dialog", "Rather Good"));
            params.put(ESelectorRowStyle.GOOD, mp.translate("Settings Dialog", "Good"));
            params.put(ESelectorRowStyle.VERY_GOOD, mp.translate("Settings Dialog", "Very Good"));

            for (Map.Entry<ESelectorRowStyle, String> entry : params.entrySet()) {
                final QListWidgetItem item = new QListWidgetItem(entry.getValue());
                item.setData(Qt.ItemDataRole.UserRole, entry.getKey());
                innerListWidget.addItem(item);                
            }            
            innerStackedWidget.setSizePolicy(QSizePolicy.Policy.Maximum, QSizePolicy.Policy.Preferred);            

            mainLayout.addLayout(verticalLayout);
            mainLayout.addWidget(innerStackedWidget);
            
            innerListWidget.setCurrentRow(0);
        }
                        
        @SuppressWarnings("unused")
        private void onInnerListWidgetChangeIndex(final int index) {
            final Object data = innerListWidget.item(index).data(Qt.ItemDataRole.UserRole);
            if (data instanceof ESelectorRowStyle){
                ESelectorRowStyle style = (ESelectorRowStyle)data;
                Integer widgetIndex = widgetIndexByRowStyle.get(style);                                
                if (widgetIndex==null){
                    final StylePageSettingsWidget styleWidget = createStylePage(style, environment);
                    widgetIndex = innerStackedWidget.count();
                    innerStackedWidget.addWidget(styleWidget);
                    widgetIndexByRowStyle.put(style, widgetIndex);                    
                }
                innerStackedWidget.setCurrentIndex(widgetIndex);
                updatePreviewWidget();
            }
        }
        
        private StylePageSettingsWidget createStylePage(final ESelectorRowStyle style, final IClientEnvironment environment){
            final StylePageSettingsWidget stylePage = new StylePageSettingsWidget(environment, this, 
                    SettingNames.SELECTOR_GROUP,
                    SettingNames.Selector.STYLES_GROUP, style.getValue());

            connectSelectorSettingsWidget(stylePage.getReadonlyPropertySettingsWidget());
            connectPropertySettingsWidget(stylePage.getMandatoryPropertySettingsWidget());
            connectSelectorSettingsWidget(stylePage.getOtherPropertySettingsWidget());
            settingWidgets.add(stylePage);
            stylePage.readSettings(settingsProvider.getSettings());
            return stylePage;
        }
        
        private void connectSelectorSettingsWidget(final SelectorSettingsWidget ssw){
            ssw.updateDfColorSignal.connect(this,"updatePreviewWidget()");
            ssw.updateNdfColorSignal.connect(this,"updatePreviewWidget()");
            ssw.updateBgrnColorSignal.connect(this,"updatePreviewWidget()");
            ssw.updateFontSignal.connect(this,"updatePreviewWidget()");            
        }
        
        private void connectPropertySettingsWidget(final PropertySettingsWidget psw){
            psw.updateFgrnColorSignal.connect(this,"updatePreviewWidget()");
            psw.updateBgrnColorSignal.connect(this,"updatePreviewWidget()");
            psw.updateFontSignal.connect(this,"updatePreviewWidget()");            
        }
        
        private void updatePreviewWidget() {
            final StylePageSettingsWidget styleSettingsWidget = 
                (StylePageSettingsWidget) (innerStackedWidget.currentWidget());
            if (styleSettingsWidget!=null){
                final ExplorerTextOptions readonlyDefTextOptions
                        = styleSettingsWidget.getReadonlyPropertySettingsWidget().getTextOptions(true);
                final ExplorerTextOptions readonlyUndefTextOptions
                        = styleSettingsWidget.getReadonlyPropertySettingsWidget().getTextOptions(false);
                final ExplorerTextOptions mandatoryDefTextOptions
                        = styleSettingsWidget.getMandatoryPropertySettingsWidget().getTextOptions();
                final ExplorerTextOptions otherDefTextOptions
                        = styleSettingsWidget.getOtherPropertySettingsWidget().getTextOptions(true);
                final ExplorerTextOptions otherUndefTextOptions
                        = styleSettingsWidget.getOtherPropertySettingsWidget().getTextOptions(false);
                previewWidget.update(readonlyDefTextOptions, mandatoryDefTextOptions, otherDefTextOptions, true);
                previewWidget.update(readonlyUndefTextOptions, mandatoryDefTextOptions, otherUndefTextOptions, false);
            }
        }
    }
    
    private final static class ColumnAlignmentPage extends SettingsWidget implements ISettingsPage{

        private final static EnumSet<EValType> valueTypes = EnumSet.of(
                EValType.BIN,
                EValType.BLOB,  EValType.CHAR,
                EValType.CLOB,  EValType.DATE_TIME,
                EValType.INT,   EValType.NUM,
                EValType.STR,   EValType.PARENT_REF);

        private final Map<EValType, ColumnAlignmentSetting> vtypesToSettings = new HashMap<>();

        public ColumnAlignmentPage(final IClientEnvironment env, final QWidget parent) {
            super(env, parent, SettingNames.SELECTOR_GROUP, null, "");
        }

        @Override
        public void readSettings(final IExplorerSettings src) {
            for(ColumnAlignmentSetting s : vtypesToSettings.values()) {
                s.readSettings(src);
            }
        }

        @Override
        public void writeSettings(final IExplorerSettings dst) {
            for(ColumnAlignmentSetting s : vtypesToSettings.values()) {
                s.writeSettings(dst);
            }
        }

        @Override
        public void restoreDefaults() {
            for(ColumnAlignmentSetting s : vtypesToSettings.values()) {
                s.restoreDefaults();
            }
        }

        @Override
        public void open(final IClientEnvironment environment, 
                                 final ISettingsProvider settingsProvider,
                                 final List<SettingsWidget> settingWidgets) {
            setUpUi();
            settingWidgets.add(this);
            readSettings(settingsProvider.getSettings());
        }        

        private void setUpUi() {
            final QGridLayout layout = new QGridLayout();
            layout.setAlignment(new Qt.Alignment(Qt.AlignmentFlag.AlignLeft, Qt.AlignmentFlag.AlignTop));
            this.setLayout(layout);

            int i = 0;
            for(EValType t : valueTypes) {
                final QLabel label = new QLabel(labelText(getEnvironment().getMessageProvider(), t), this);
                layout.addWidget(label, i, 0);

                ColumnAlignmentSetting setting = new ColumnAlignmentSetting(getEnvironment(), this, t);
                vtypesToSettings.put(t, setting);
                layout.addWidget(setting, i, 1);

                i++;
            }
        }

        private static String labelText(final MessageProvider msgProvider, EValType type) {
            switch(type) {
                case BIN:
                    return msgProvider.translate("ColumnAlignmentSettings", "Binary");
                case BLOB:
                    return msgProvider.translate("ColumnAlignmentSettings", "BLOB");
                case CHAR:
                    return msgProvider.translate("ColumnAlignmentSettings", "Character");
                case CLOB:
                    return msgProvider.translate("ColumnAlignmentSettings", "CLOB");
                case DATE_TIME:
                    return msgProvider.translate("ColumnAlignmentSettings", "Date/time");
                case INT:
                    return msgProvider.translate("ColumnAlignmentSettings", "Integer");
                case NUM:
                    return msgProvider.translate("ColumnAlignmentSettings", "Real number");
                case STR:
                    return msgProvider.translate("ColumnAlignmentSettings", "String");
                case PARENT_REF:
                    return msgProvider.translate("ColumnAlignmentSettings", "Parent reference");
                default:
                    return type.getName();
            }
        }
    }
    
    public SelectorSettingsPage(final IClientEnvironment environment, 
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
        addTab(new StylesPage(environment, settingsProvider), mp.translate("Settings Dialog","Styles"));        
        addTab(new ColumnAlignmentPage(environment, this), mp.translate("Settings Dialog", "Alignment"));
        openPage(1);//to adjust dialog size
    }
}