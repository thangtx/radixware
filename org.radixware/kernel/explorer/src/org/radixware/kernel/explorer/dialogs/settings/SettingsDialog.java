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
package org.radixware.kernel.explorer.dialogs.settings;

import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.core.Qt.AlignmentFlag;
import com.trolltech.qt.core.Qt.ItemFlag;
import com.trolltech.qt.core.Qt.WindowModality;
import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QBrush;
import com.trolltech.qt.gui.QFileDialog;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QItemDelegate;
import com.trolltech.qt.gui.QListView;
import com.trolltech.qt.gui.QListView.Flow;
import com.trolltech.qt.gui.QListWidget;
import com.trolltech.qt.gui.QListWidgetItem;
import com.trolltech.qt.gui.QMainWindow;
import com.trolltech.qt.gui.QPalette;
import com.trolltech.qt.gui.QSizePolicy;
import com.trolltech.qt.gui.QStackedWidget;
import com.trolltech.qt.gui.QToolBar;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import java.util.ArrayList;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EFontWeight;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.explorer.dialogs.ExplorerDialog;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.env.ExplorerSettings;
import org.radixware.kernel.explorer.env.IExplorerSettings;
import org.radixware.kernel.explorer.text.ExplorerFont;

public final class SettingsDialog extends ExplorerDialog {
    
    private final static String SETTINGS_DIALOG_NAME_KEY = SettingNames.SYSTEM + "/" + "settingsDialog";

    private final static class Icons extends ClientIcon.CommonOperations {

        private Icons(final String fileName) {
            super(fileName, true);
        }
        private final static Icons PREVIEW = new Icons("classpath:images/preview.svg");
        private final static Icons SRC_EDITOR_SETTINGS = new Icons("classpath:images/srcedit.svg");
        private final static Icons FORMAT_SETTINGS = new Icons("classpath:images/format_settings.svg");
    }

    public final static class DialogIcons extends ExplorerIcon.Dialog {

        private DialogIcons(final String fileName) {
            super(fileName);
        }
        public final static DialogIcons APPEARANCE_SETTINGS = new DialogIcons("classpath:images/appearance_settings.svg");
        public final static DialogIcons RESET_SETTINGS = new DialogIcons("classpath:images/reset_settings.svg");
    }

    private final static class ListWidget extends QListWidget {

        public ListWidget(final QWidget parent) {
            super(parent);
            //по умолчанию используется QStyledItemDelegate и перенос по словам иногда не работает
            setItemDelegate(new QItemDelegate());
            setViewMode(QListView.ViewMode.IconMode);
            setFlow(Flow.TopToBottom);
            setHorizontalScrollBarPolicy(Qt.ScrollBarPolicy.ScrollBarAlwaysOff);
            setVerticalScrollBarPolicy(Qt.ScrollBarPolicy.ScrollBarAlwaysOff);
            setResizeMode(QListView.ResizeMode.Adjust);
            setIconSize(new QSize(64, 64));
            setMovement(QListView.Movement.Static);
            setWrapping(false);
            setSizePolicy(QSizePolicy.Policy.Fixed, QSizePolicy.Policy.Minimum);
            setSpacing(0);
            setTextElideMode(Qt.TextElideMode.ElideNone);
            setWordWrap(true);
            setMouseTracking(true);
        }

        public void resizeToContents() {
            int maxWidth = 0;
            QListWidgetItem item;
            for (int i = 0; i < count(); i++) {
                item = item(i);
                maxWidth = Math.max(maxWidth, visualItemRect(item).width());
            }

            for (int i = 0; i < count(); i++) {
                item = item(i);
                item.setSizeHint(new QSize(maxWidth + 16, visualItemRect(item).height() + 2));
            }
            updateGeometry();
            setFixedWidth(maxWidth + 20);
            setMinimumHeight(contentsSize().height() + frameWidth() * 2);
        }
    }
            
    private final ListWidget listWidget;
    private final QStackedWidget stackedWidget;
    private final boolean openedSettingGroups[];
    private final ArrayList<SettingsWidget> settingsArrayList = new ArrayList<>();    
    private final QAction previewAction;
    private final QAction restoreDefaultSettingsAction;
    private final QAction loadSettingsFromFileAction;
    private final QAction saveSettingsToFileAction;    
    private final ExplorerSettings applicationSettings;
    private AppearanceSettings appearanceSettings;
    private final ISettingsProvider settingsProvider = new ISettingsProvider() {
        @Override
        public ExplorerSettings getSettings() {
            return appearanceSettings==null ? applicationSettings : appearanceSettings;
        }
    };  
    
    public SettingsDialog(final IClientEnvironment environment, final QWidget parent) {
        super(environment, parent, SETTINGS_DIALOG_NAME_KEY);        
        applicationSettings = (ExplorerSettings)environment.getConfigStore();
        final MessageProvider mp = environment.getMessageProvider();
        
        previewAction = new QAction(ExplorerIcon.getQIcon(Icons.PREVIEW), mp.translate("Settings Dialog", "Preview"), this);
        restoreDefaultSettingsAction = new QAction(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.CANCEL), mp.translate("Settings Dialog", "Restore Default Settings"),this);
        loadSettingsFromFileAction = new QAction(ExplorerIcon.getQIcon(Icons.OPEN), mp.translate("Settings Dialog", "Load Settings from File"), this);
        saveSettingsToFileAction = new QAction(ExplorerIcon.getQIcon(Icons.SAVE), mp.translate("Settings Dialog", "Save Settings to File"), this);
        
        setWindowModality(WindowModality.ApplicationModal);
        setContentsMargins(0, 0, 0, 0);
        setWindowTitle(getEnvironment().getMessageProvider().translate("Settings Dialog", "Appearance Settings"));
        setWindowIcon(ExplorerIcon.getQIcon(DialogIcons.APPEARANCE_SETTINGS));
        setupActions();
        
        final QMainWindow win = new QMainWindow();
        final QToolBar tools = new QToolBar(win);
        tools.setContextMenuPolicy(Qt.ContextMenuPolicy.PreventContextMenu);
        tools.setMovable(false);
        tools.setFloatable(false);
        tools.setObjectName("SettingDialogToolBar");
        tools.addAction(previewAction);
        tools.addAction(restoreDefaultSettingsAction);
        tools.addAction(loadSettingsFromFileAction);
        tools.addAction(saveSettingsToFileAction);
        win.addToolBar(tools);
        final QWidget centralWidget = new QWidget(win);
        win.setCentralWidget(centralWidget);
        
        listWidget = new ListWidget(centralWidget);        
                
        addSettingGroup(ClientIcon.Definitions.TREE, mp.translate("Settings Dialog", "Explorer Tree"));
        addSettingGroup(ClientIcon.Definitions.EDITOR, mp.translate("Settings Dialog", "Editor"));
        addSettingGroup(ClientIcon.Definitions.SELECTOR, mp.translate("Settings Dialog", "Selector"));
        addSettingGroup(Icons.SRC_EDITOR_SETTINGS, mp.translate("Settings Dialog", "Source Code"));
        addSettingGroup(Icons.FORMAT_SETTINGS, mp.translate("Settings Dialog", "Number and Date/Time Formats"));
        addSettingGroup(Icons.Definitions.APPEARANCE, mp.translate("Settings Dialog", "Appearance"));       

        listWidget.resizeToContents();        

        final QHBoxLayout horizLayout = new QHBoxLayout();
        horizLayout.setSpacing(10);
        horizLayout.setContentsMargins(0, 10, 0, 0);
        horizLayout.addWidget(listWidget);
        centralWidget.setLayout(horizLayout);
                
        stackedWidget = new QStackedWidget(centralWidget);        
        stackedWidget.addWidget(new ExplorerTreeSettingsPage(environment, settingsProvider, settingsArrayList));
        stackedWidget.addWidget(new EditorSettingsPage(environment, settingsProvider, settingsArrayList));
        stackedWidget.addWidget(new SelectorSettingsPage(environment, settingsProvider, settingsArrayList));
        stackedWidget.addWidget(new SourceCodeSettingsPage(environment, settingsProvider, settingsArrayList));
        stackedWidget.addWidget(new FormatSettingsWidget(environment, stackedWidget));
        stackedWidget.addWidget(new AppearanceStyleSettingWidget(environment, stackedWidget));        

        horizLayout.addWidget(stackedWidget);
        openedSettingGroups = new boolean[stackedWidget.count()];        
        for (int i=stackedWidget.count()-1; i>=0; i--){
            openedSettingGroups[i] = false;            
        }
                
        listWidget.currentItemChanged.connect(this, "onListWidgetClicked(QListWidgetItem, QListWidgetItem)");       
        listWidget.currentRowChanged.connect(this, "onChangeSettingsGroup(int)");

        final QVBoxLayout mainLayout = dialogLayout();
        mainLayout.setContentsMargins(10, 0, 10, 0);
        mainLayout.addWidget(win);
        
        addButton(EDialogButtonType.OK).addClickHandler(new IButton.ClickHandler(){
            @Override
            public void onClick(final IButton source) {
                onApply();
            }
        });
        addButton(EDialogButtonType.CANCEL).addClickHandler(new IButton.ClickHandler(){
            @Override
            public void onClick(final IButton source) {
                reject();
            }
        });
        
        openSettingsGroup(1);//preopen this pages to adjust dialog size
        openSettingsGroup(2);//preopen this pages to adjust dialog size
        openSettingsGroup(4);//preopen this pages to adjust dialog size
        
        listWidget.setCurrentRow(0);
        setDisposeAfterClose(true);
    }    

    private void setupActions() {
        previewAction.triggered.connect(this, "onPreview()");
        restoreDefaultSettingsAction.triggered.connect(this,"restoreDefaultSettings()");
        loadSettingsFromFileAction.triggered.connect(this,"loadSettingsFromFile()");
        saveSettingsToFileAction.triggered.connect(this, "saveSettingsToFile()");
    }

    @SuppressWarnings("unused")
    private void restoreDefaultSettings() {
        final String title = 
            getEnvironment().getMessageProvider().translate("ExplorerMessage", "Confirm to Clear Settings");
        final String message = 
            getEnvironment().getMessageProvider().translate("ExplorerMessage", "Do you really want to reset all your appearance settings to default values?");
        if (getEnvironment().messageConfirmation(title, message)) {
            ensureAppearanceSettings();
            appearanceSettings.read(appearanceSettings.getDefaultSettings());
            for (SettingsWidget w : settingsArrayList) {
                w.restoreDefaults();
            }
        }
    }

    @Override
    public void done(final int result) {        
        if (getEnvironment().getConfigStore()==appearanceSettings){
            Application.applySettings(applicationSettings);
        }
        if (appearanceSettings!=null){
            appearanceSettings.remove();
        }
        super.done(result);
    }

    @SuppressWarnings("unused")
    private void onPreview() {
        if (validateAllSettings() && ensureAppearanceSettings()) {
            writeAllSettings(appearanceSettings);
            Application.applySettings(appearanceSettings);
            if (Application.getMainWindow() != null) {
                Application.getMainWindow().activateWindow();
            }
            activateWindow();
        }
    }

    @SuppressWarnings("unused")
    private void onApply() {
        if (validateAllSettings()) {            
            writeAllSettings(settingsProvider.getSettings());
            if (appearanceSettings!=null){
                appearanceSettings.write(applicationSettings);
            }
            Application.applySettings(applicationSettings);
            if (getEnvironment().getConfigStore().isWritable()) {
                getEnvironment().getConfigStore().sync();
            }
            if (Application.getMainWindow() != null) {
                Application.getMainWindow().activateWindow();
            }
            accept();
        } 
    }

    @SuppressWarnings("unused")
    private void saveSettingsToFile() {
        if (validateAllSettings()) {
            final String title = getEnvironment().getMessageProvider().translate("Settings Dialog", "Save settings");
            final String filter = getEnvironment().getMessageProvider().translate("Settings Dialog", "Setting Files (%s);;All files (%s)");
            final String fname = QFileDialog.getSaveFileName(this, title, "", new QFileDialog.Filter(String.format(filter, "*.ini", "*.*")));            
            if (fname != null && !fname.isEmpty() && ensureAppearanceSettings()) {                
                writeAllSettings(appearanceSettings);
                appearanceSettings.writeToFile(fname);
            }
        }
    }

    @SuppressWarnings("unused")
    private void loadSettingsFromFile() {
        final String title = getEnvironment().getMessageProvider().translate("Settings Dialog", "Load settings");
        final String filter = getEnvironment().getMessageProvider().translate("Settings Dialog", "Setting Files (%s);;All files (%s)");
        final String fname = QFileDialog.getOpenFileName(this, title, "", new QFileDialog.Filter(String.format(filter, "*.ini", "*.*")));
        if (fname != null && !fname.isEmpty() && ensureAppearanceSettings()) {
            appearanceSettings.readFromFile(fname);
            readAllSettings(appearanceSettings);
        }
    }
    
    private boolean ensureAppearanceSettings(){
        if (appearanceSettings==null){
            appearanceSettings = AppearanceSettings.create(getEnvironment(), this);
        }
        return applicationSettings!=null;
    }
        
    private void addSettingGroup(final ClientIcon icon, final String title){
        final QListWidgetItem item = new QListWidgetItem(listWidget);
        item.setIcon(ExplorerIcon.getQIcon(icon));
        item.setText(title);
        item.setTextAlignment(AlignmentFlag.AlignHCenter.value());
        item.setFlags(ItemFlag.ItemIsSelectable, ItemFlag.ItemIsEnabled);
        item.setFont(ExplorerFont.Factory.getFont(8, EFontWeight.BOLD).getQFont());
    }

    private void writeAllSettings(final IExplorerSettings dst) {
        for (SettingsWidget w : settingsArrayList) {
            w.writeSettings(dst);
        }
    }

    private boolean validateAllSettings() {
        for (SettingsWidget w : settingsArrayList) {
            if (!w.validate()) {             
                return false;
            }
        }
        return true;
    }
    
    private void readAllSettings(final IExplorerSettings src) {
        for (SettingsWidget w : settingsArrayList) {
            w.readSettings(src);
        }
    }

    @SuppressWarnings("unused")
    private void onListWidgetClicked(final QListWidgetItem current, final QListWidgetItem previous) {
        final QPalette curPalette = palette();
        final QBrush highlightedTextBrush = curPalette.base(), highlightBrush = curPalette.highlight();
        if (previous != null) {
            previous.setBackground(highlightedTextBrush);
        }
        current.setBackground(highlightBrush);
    }
    
    @SuppressWarnings("unused")
    private void onChangeSettingsGroup(final int index){
        openSettingsGroup(index);
        stackedWidget.setCurrentIndex(index);
    }
    
    private void openSettingsGroup(final int index){
        if (!openedSettingGroups[index]){    
            ((ISettingsPage)stackedWidget.widget(index)).open(getEnvironment(), settingsProvider, settingsArrayList);
            openedSettingGroups[index] = true;            
        }        
    }
}
