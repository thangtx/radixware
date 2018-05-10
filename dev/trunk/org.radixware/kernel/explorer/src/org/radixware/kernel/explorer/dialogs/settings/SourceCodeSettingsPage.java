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
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QFont;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QListView;
import com.trolltech.qt.gui.QListWidget;
import com.trolltech.qt.gui.QListWidgetItem;
import com.trolltech.qt.gui.QSizePolicy;
import com.trolltech.qt.gui.QStackedWidget;
import com.trolltech.qt.gui.QTextEdit;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.explorer.editors.sqmleditor.SqmlProcessor;
import org.radixware.kernel.explorer.editors.xscmleditor.Highlighter;


final class SourceCodeSettingsPage  extends SettingsTabSet implements ISettingsPage{
    
    private abstract static class AbstractSubPage extends QWidget implements ISettingsPage{
                
        private QStackedWidget sourceStackedWidget;
        private QListWidget innerListWidget;        
        private SourceCodePageSettingsWidget defaultSourcePage;
        private List<SettingsWidget> settingWidgets;
        private Highlighter hightlighter;
        private final String exampleCode;
        private final String subgroup;
        private final Map<String, Integer> widgetIndexByTagSetting = new HashMap<>();
        private final IClientEnvironment environment;
        private final ISettingsProvider settingsProvider;
        
        public AbstractSubPage(final String groupName, 
                                            final String exampleCode, 
                                            final IClientEnvironment env,
                                            final ISettingsProvider settingsProvider){
            this.exampleCode = exampleCode;
            this.subgroup = groupName;
            setObjectName("rx_settings_dialog_source_code_"+groupName+"_page");
            environment = env;
            this.settingsProvider = settingsProvider;
        }

        @Override
        public void open(final IClientEnvironment environment, 
                                 final ISettingsProvider settingsProvider,
                                 final List<SettingsWidget> settingWidgets) {
            this.settingWidgets = settingWidgets;
            final MessageProvider mp = environment.getMessageProvider();
            
            final QVBoxLayout mainLayout = new QVBoxLayout(this);
            final QHBoxLayout horizontalLayout = new QHBoxLayout();
            horizontalLayout.setWidgetSpacing(0);            
            horizontalLayout.setAlignment(new Alignment(AlignmentFlag.AlignLeft, AlignmentFlag.AlignTop));

            innerListWidget = new QListWidget(this);
            innerListWidget.setViewMode(QListView.ViewMode.ListMode);
            innerListWidget.setFlow(QListView.Flow.TopToBottom);
            innerListWidget.setMovement(QListView.Movement.Static);
            innerListWidget.setSpacing(0);            
            innerListWidget.currentRowChanged.connect(this, "onChangeIndex(int)");
            innerListWidget.setMouseTracking(true);
            innerListWidget.setSizePolicy(QSizePolicy.Policy.Minimum, QSizePolicy.Policy.MinimumExpanding);
            horizontalLayout.addWidget(innerListWidget);

            final LinkedHashMap<String, String> params = new LinkedHashMap<>(); // "имя вкладки"
            params.put(SettingNames.SourceCode.Syntax.DEFAULT, mp.translate("Settings Dialog", "Default"));
            params.put(SettingNames.SourceCode.Syntax.RESERVED_WORDS, mp.translate("Settings Dialog", "Reserved Words"));
            params.put(SettingNames.SourceCode.Syntax.COMMENTS, mp.translate("Settings Dialog", "Comments"));
            params.put(SettingNames.SourceCode.Syntax.STRINGS, mp.translate("Settings Dialog", "Strings"));
            params.put(SettingNames.SourceCode.Syntax.NUMBERS, mp.translate("Settings Dialog", "Numbers"));
            params.put(SettingNames.SourceCode.Syntax.SYMBOLS, mp.translate("Settings Dialog", "Symbols"));
            params.put(SettingNames.SourceCode.Syntax.SEPARATORS, mp.translate("Settings Dialog", "Separators"));
            params.put(SettingNames.SourceCode.Syntax.SEPARATORS, mp.translate("Settings Dialog", "Separators"));

            final QTextEdit exampleCodeEditor = new QTextEdit(this);
            exampleCodeEditor.setText(exampleCode);
            hightlighter = createHighlighter(exampleCodeEditor);
            sourceStackedWidget = new QStackedWidget(this);             
            final Map<String, String> tagSettings = getTagSettingTitles();
            
            for (Map.Entry<String,String> entry : tagSettings.entrySet()) {
                final QListWidgetItem item = new QListWidgetItem(entry.getValue());
                item.setData(Qt.ItemDataRole.UserRole, entry.getKey());
                innerListWidget.addItem(item);
            }                        
            horizontalLayout.addWidget(sourceStackedWidget);
            mainLayout.addLayout(horizontalLayout);
            mainLayout.addWidget(exampleCodeEditor);
            innerListWidget.setCurrentRow(0);
        }
        
        protected final IClientEnvironment getEnvironment(){
            return environment;                    
        }
        
        protected abstract Map<String,String> getTagSettingTitles();        
        
        protected abstract Highlighter createHighlighter(final QTextEdit textEditor);
        
        @SuppressWarnings("unused")
        private void onChangeIndex(final int index){
            final Object data = innerListWidget.item(index).data(Qt.ItemDataRole.UserRole);
            if (data instanceof String){
                String tagSetting = (String)data;
                Integer widgetIndex = widgetIndexByTagSetting.get(tagSetting);
                if (widgetIndex==null){
                    final SourceCodePageSettingsWidget sourceSettingsWidget = 
                            createSourceCodePageSettingsWidget(tagSetting);
                    if (index==0){
                        defaultSourcePage = sourceSettingsWidget;
                        defaultSourcePage.updateSrcEditorSettingsSignal.connect(this, "srcCodeEditorSettingsChanged(SourceCodePageSettingsWidget)");
                    }
                    widgetIndex = sourceStackedWidget.count();
                    sourceStackedWidget.addWidget(sourceSettingsWidget);
                    widgetIndexByTagSetting.put(tagSetting, widgetIndex);
                }
                sourceStackedWidget.setCurrentIndex(widgetIndex);
            }          
        }
        
        @SuppressWarnings("unused")
        private void srcCodeEditorSettingsChanged(final SourceCodePageSettingsWidget defaultSettingsWidget) {
            final QFont font = defaultSettingsWidget.getPropertySettingsWidget().getFont();
            final QColor frgColor = defaultSettingsWidget.getPropertySettingsWidget().getForegroundColor();
            final QColor bgrColor = defaultSettingsWidget.getPropertySettingsWidget().getBackgroundColor();
            int index = defaultSettingsWidget.getSettingCfgName().lastIndexOf("/");
            String defSettingsName = defaultSettingsWidget.getSettingCfgName().substring(0, index);
            for (int i=sourceStackedWidget.count()-1; i>0; i--) {//index 0 corresponds to defaultSettingsWidget
                SourceCodePageSettingsWidget settingsWidget = (SourceCodePageSettingsWidget)sourceStackedWidget.widget(i);
                index = settingsWidget.getSettingCfgName().lastIndexOf("/");
                String settingsName = settingsWidget.getSettingCfgName().substring(0, index);
                if (defSettingsName.equals(settingsName)) {//settings in this widget must be the same as in defaultSettingsWidget
                    settingsWidget.change(frgColor, bgrColor, font);
                }                
            }
        }
        
        private SourceCodePageSettingsWidget createSourceCodePageSettingsWidget(final String tagSetting){
            final SourceCodePageSettingsWidget sourceSettingsWidget = 
                        new SourceCodePageSettingsWidget(environment, this,
                        SettingNames.SOURCE_EDITOR, subgroup, tagSetting, defaultSourcePage);
            final PropertySettingsWidget psw = sourceSettingsWidget.getPropertySettingsWidget();
            psw.updateFgrnColorSignal.connect(hightlighter, "setForegroundColor(QColor,String)");
            psw.updateBgrnColorSignal.connect(hightlighter, "setBackgroundColor(QColor,String)");
            psw.updateFontSignal.connect(hightlighter, "setFont(QFont,String)");
            sourceSettingsWidget.readSettings(settingsProvider.getSettings());
            settingWidgets.add(sourceSettingsWidget);            
            return sourceSettingsWidget;
        }        
    }
    
    private final static class SqmlCodeSettingsPage extends AbstractSubPage{
        
        private static final String EXAMPLE_CODE = 
            "Select column1, column2 From someTable\nWhere column3=0 and column4=\"some string\"  --comment";
        
        public SqmlCodeSettingsPage(final IClientEnvironment env, final ISettingsProvider settingsProvider){
            super(SettingNames.SourceCode.SYNTAX_SQML, EXAMPLE_CODE, env, settingsProvider);
        }

        @Override
        protected Map<String, String> getTagSettingTitles() {
            final MessageProvider mp = getEnvironment().getMessageProvider();
            final Map<String,String> tagTitles = new HashMap<>();
            tagTitles.put(SettingNames.SourceCode.Syntax.SQML_CONSTANT_VALUE, mp.translate("Settings Dialog", "Constant Value Tag"));
            tagTitles.put(SettingNames.SourceCode.Syntax.SQML_PARENT_CONDITION, mp.translate("Settings Dialog", "Parent Condition Tag"));
            tagTitles.put(SettingNames.SourceCode.Syntax.SQML_PROP_SQL_NAME, mp.translate("Settings Dialog", "Property Tag"));
            tagTitles.put(SettingNames.SourceCode.Syntax.SQML_TABLE_SQL_NAME, mp.translate("Settings Dialog", "Table Tag"));
            tagTitles.put(SettingNames.SourceCode.Syntax.SQML_THIS_TABLE_SQL_NAME, mp.translate("Settings Dialog", "Context Table Tag"));
            tagTitles.put(SettingNames.SourceCode.Syntax.SQML_TYPIFIED_VALUE, mp.translate("Settings Dialog", "Typified Value Tag"));
            tagTitles.put(SettingNames.SourceCode.Syntax.SQML_PARAMETER, mp.translate("Settings Dialog", "Parameter Tag"));
            tagTitles.put(SettingNames.SourceCode.Syntax.SQML_ENTITY_REF_PARAMETER, mp.translate("Settings Dialog", "Entity Reference Parameter Tag"));
            tagTitles.put(SettingNames.SourceCode.Syntax.SQML_DB_FUNC_CALL, mp.translate("Settings Dialog", "SQL Function Tag"));
            tagTitles.put(SettingNames.SourceCode.Syntax.SQML_EVENT_CODE, mp.translate("Settings Dialog", "Event Code Tag"));
            tagTitles.put(SettingNames.SourceCode.Syntax.SQML_ID_PATH, mp.translate("Settings Dialog", "Identifier Path Tag"));
            tagTitles.put(SettingNames.SourceCode.Syntax.SQML_DB_NAME, mp.translate("Settings Dialog", "Definition Database Name Tag"));
            tagTitles.put(SettingNames.SourceCode.Syntax.SQML_PREPROCESSOR, mp.translate("Settings Dialog", "Preprocessor Tag"));            
            tagTitles.put(SettingNames.SourceCode.Syntax.SQML_UNKNOWN_TAG, mp.translate("Settings Dialog", "Unknown Tag"));
            return tagTitles;
        }

        @Override
        protected Highlighter createHighlighter(final QTextEdit textEditor) {
            return new Highlighter(getEnvironment(), textEditor, new SqmlProcessor(getEnvironment(), null, null), "org.radixware.explorer/S_E/SYNTAX_SQML/");
        }
    }
    
    private final static class JmlCodeSettingsPage extends AbstractSubPage{
        
        private final static String EXAMPLE_CODE = "String s=\"str\";\nint i=12;  //comment";
        
        public JmlCodeSettingsPage(final IClientEnvironment env, final ISettingsProvider settingsProvider){
            super(SettingNames.SourceCode.SYNTAX_JML, EXAMPLE_CODE, env, settingsProvider);
        }

        @Override
        protected Map<String, String> getTagSettingTitles() {
            final MessageProvider mp = getEnvironment().getMessageProvider();
            final Map<String,String> tagTitles = new HashMap<>();            
            tagTitles.put(SettingNames.SourceCode.Syntax.JML_TAG_ID, mp.translate("Settings Dialog", "Identifier Tag"));
            tagTitles.put(SettingNames.SourceCode.Syntax.JML_TAG_INVOCATE, mp.translate("Settings Dialog", "Invocation Tag"));
            tagTitles.put(SettingNames.SourceCode.Syntax.JML_TAG_TYPE, mp.translate("Settings Dialog", "Type Tag"));
            tagTitles.put(SettingNames.SourceCode.Syntax.JML_TAG_DB_ENTITY, mp.translate("Settings Dialog", "Entity Tag"));
            tagTitles.put(SettingNames.SourceCode.Syntax.JML_DB_NAME, mp.translate("Settings Dialog", "Definition Database Name Tag"));
            tagTitles.put(SettingNames.SourceCode.Syntax.JML_TAG_LOCALIZED_STR, mp.translate("Settings Dialog", "Localized String"));
            return tagTitles;
        }

        @Override
        protected Highlighter createHighlighter(final QTextEdit textEditor) {
            return new Highlighter(getEnvironment(), textEditor, null, "org.radixware.explorer/S_E/SYNTAX_JML/");
        }        
    }
    
    public SourceCodeSettingsPage(final IClientEnvironment environment, 
                                                    final ISettingsProvider settingsProvider,
                                                    final List<SettingsWidget> settingWidgets){
        super(environment, settingsProvider, settingWidgets);
    }

    @Override
    public void open(final IClientEnvironment environment, 
                             final ISettingsProvider settingsProvider,
                             final List<SettingsWidget> settingWidgets) {
        final MessageProvider mp = environment.getMessageProvider();
        addTab(new JmlCodeSettingsPage(environment, settingsProvider), mp.translate("Settings Dialog", "Source Code Options for Jml"));
        addTab(new SqmlCodeSettingsPage(environment, settingsProvider), mp.translate("Settings Dialog", "Source Code Options for Sqml"));
    }

}
