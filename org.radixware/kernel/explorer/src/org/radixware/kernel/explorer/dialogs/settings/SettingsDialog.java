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

import com.trolltech.qt.core.QSettings;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.core.Qt.Alignment;
import com.trolltech.qt.core.Qt.AlignmentFlag;
import com.trolltech.qt.core.Qt.ItemFlag;
import com.trolltech.qt.core.Qt.WindowModality;
import com.trolltech.qt.gui.QDialogButtonBox.StandardButton;
import com.trolltech.qt.gui.QListView.Flow;
import com.trolltech.qt.gui.QSizePolicy.Policy;
import com.trolltech.qt.gui.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.enums.ESelectorRowStyle;
import org.radixware.kernel.explorer.dialogs.ExplorerDialog;
import org.radixware.kernel.explorer.editors.sqmleditor.SqmlProcessor;
import org.radixware.kernel.explorer.editors.xscmleditor.Highlighter;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.env.ExplorerSettings;
import org.radixware.kernel.explorer.text.ExplorerTextOptions;

public class SettingsDialog extends ExplorerDialog {

    private final static class Icons extends ClientIcon.CommonOperations {

        private Icons(final String fileName) {
            super(fileName, true);
        }
        private final static Icons PREVIEW = new Icons("classpath:images/preview.svg");
        private final static Icons SRC_EDITOR_SETTINGS = new Icons("classpath:images/srcedit.svg");
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
    private final String previewSettingsFileName = com.trolltech.qt.core.QDir.tempPath() + "/preview.ini";
    private final static String SETTINGS_DIALOG_NAME_KEY = SettingNames.SYSTEM + "/" + "settingsDialog";
    private final ListWidget listWidget;
    private final QMainWindow win;
    private ExplorerSettings backedupSettings;
    private ExplorerSettings previewSettings;
    private final ArrayList<SettingsWidget> settingsArrayList = new ArrayList<SettingsWidget>(); // ArrayList
    // под
    // все
    // виджеты
    // с
    // настройками
    private QStackedWidget innerStackedWidget;
    private PreviewWidget previewWidget;
    public QAction previewAction = new QAction(ExplorerIcon.getQIcon(Icons.PREVIEW),
            Application.translate("Settings Dialog", "Preview"), this);
    public QAction restoreDefaultSettingsAction = new QAction(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.CANCEL),
            Application.translate("Settings Dialog", "Restore Default Settings"),
            this);
    public QAction loadSettingsFromFileAction = new QAction(ExplorerIcon.getQIcon(Icons.OPEN),
            Application.translate("Settings Dialog", "Load Settings from File"), this);
    public QAction saveSettingsToFileAction = new QAction(ExplorerIcon.getQIcon(Icons.SAVE),
            Application.translate("Settings Dialog", "Save Settings to File"), this);

    private void setupActions() {
        previewAction.triggered.connect(this, "onPreview()");
        restoreDefaultSettingsAction.triggered.connect(this,
                "restoreDefaultSettings()");
        loadSettingsFromFileAction.triggered.connect(this,
                "loadSettingsFromFile()");
        saveSettingsToFileAction.triggered.connect(this, "saveSettingsToFile()");
    }

    @SuppressWarnings("unused")
    private void restoreDefaultSettings() {
        final String title = Application.translate("ExplorerMessage", "Confirm to Clear Settings");
        final String message = Application.translate("ExplorerMessage", "Do you really want to reset all your appearance settings to default values?");
        if (Application.messageConfirmation(title, message)) {
            for (SettingsWidget w : settingsArrayList) {
                w.restoreDefaults();
            }
            updatePreviewWidget();
        }
    }

    @Override
    protected void closeEvent(QCloseEvent event) {
        final com.trolltech.qt.core.QFile file = new com.trolltech.qt.core.QFile(
                previewSettingsFileName);
        if (file.exists()) {
            file.remove();
        }
        ExplorerSettings currentSettings = (ExplorerSettings) getEnvironment().getConfigStore();
        if (!currentSettings.equals(backedupSettings)) {
            onAbort();
            super.closeEvent(event);
        }
    }

    @Override
    public int exec() {
        // забэкапили текущие настройки
        backedupSettings = (ExplorerSettings) getEnvironment().getConfigStore();
        readAllSettings(backedupSettings);
        listWidget.repaint();
        return super.exec();
    }

    @SuppressWarnings("unused")
    private void onAbort() {
        Application.applySettings(backedupSettings); // откатываемся назад
        close();
    }

    @SuppressWarnings("unused")
    private void onPreview() {
        // собираем все настройки с SettingsWidget-ов и применяем их без
        // сохранения в файл
        writeAllSettings(previewSettings);

        Application.applySettings(previewSettings);

        if (Application.getMainWindow() != null) {
            Application.getMainWindow().activateWindow();
        }
        activateWindow();
    }

    @SuppressWarnings("unused")
    private void onApply() {
        // собираем все настройки с SettingsWidget-ов, применяем их и сохраняем
        // в файл
        writeAllSettings(backedupSettings);
        Application.applySettings(backedupSettings);
        // обновляем файл настроек
        if (getEnvironment().getConfigStore().isWritable()) {
            getEnvironment().getConfigStore().sync();
        }

        if (Application.getMainWindow() != null) {
            Application.getMainWindow().activateWindow();
        }
        activateWindow();
        close();
    }

    @SuppressWarnings("unused")
    private void saveSettingsToFile() {
        final String title = Application.translate("Settings Dialog", "Save settings");
        final String filter = Application.translate("Settings Dialog", "Setting Files (%s);;All files (%s)");
        final String fname = QFileDialog.getSaveFileName(this, title, "", new QFileDialog.Filter(String.format(filter, "*.ini", "*.*")));
        if (fname != null && !fname.isEmpty()) {

            ExplorerSettings settings = new ExplorerSettings(getEnvironment(), fname,
                    QSettings.Format.IniFormat);
            settings.beginGroup("Settings");
            writeAllSettings(settings);
            settings.endGroup();
        }
    }

    @SuppressWarnings("unused")
    private void loadSettingsFromFile() {
        final String title = Application.translate("Settings Dialog", "Load settings");
        final String filter = Application.translate("Settings Dialog", "Setting Files (%s);;All files (%s)");
        final String fname = QFileDialog.getOpenFileName(this, title, "", new QFileDialog.Filter(String.format(filter, "*.ini", "*.*")));
        if (fname != null && !fname.isEmpty()) {

            ExplorerSettings settings = new ExplorerSettings(getEnvironment(), fname,
                    QSettings.Format.IniFormat);
            settings.beginGroup("Settings");
            try {
                readAllSettings(settings);
            } finally {
                settings.endGroup();
            }
        }
    }

    private class BottomWidget extends QWidget {

        private QDialogButtonBox dialogButtonBox;

        public BottomWidget(QWidget parent) {
            super(parent);

            dialogButtonBox = new QDialogButtonBox(this);

            final QPushButton applyPushBtn = dialogButtonBox.addButton(StandardButton.Ok);
            applyPushBtn.setText(Application.translate("ExplorerDialog",
                    "&OK"));
            applyPushBtn.setIcon(ExplorerIcon.getQIcon(ClientIcon.Dialog.BUTTON_OK));
            applyPushBtn.show();
            dialogButtonBox.accepted.connect(SettingsDialog.this, "onApply()");

            final QPushButton cancelPushBtn = dialogButtonBox.addButton(StandardButton.Cancel);
            cancelPushBtn.setText(Application.translate("ExplorerDialog",
                    "&Cancel"));
            cancelPushBtn.setIcon(ExplorerIcon.getQIcon(ClientIcon.Dialog.BUTTON_CANCEL));
            cancelPushBtn.show();
            dialogButtonBox.rejected.connect(SettingsDialog.this, "onAbort()");

            QVBoxLayout layout = new QVBoxLayout(this);
            layout.setContentsMargins(0, 0, 0, 10);

            layout.addWidget(dialogButtonBox);
        }
    }

    // constructor
    public SettingsDialog(IClientEnvironment environment, QWidget parent) {
        super(environment, parent, SETTINGS_DIALOG_NAME_KEY);

        previewSettings = new ExplorerSettings(getEnvironment(), previewSettingsFileName, QSettings.Format.IniFormat);
        previewSettings.beginGroup("PreviewSettings");

        setWindowModality(WindowModality.ApplicationModal);
        setContentsMargins(0, 0, 0, 0);
        setWindowTitle(Application.translate("Settings Dialog", "Appearance Settings"));
        setWindowIcon(ExplorerIcon.getQIcon(DialogIcons.APPEARANCE_SETTINGS));
        setupActions();
        QWidget centralWidget = new QWidget(this);

        win = new QMainWindow();
        QToolBar tools = new QToolBar(win);
        tools.setContextMenuPolicy(Qt.ContextMenuPolicy.PreventContextMenu);
        tools.setMovable(false);
        tools.setFloatable(false);
        tools.setObjectName("SettingDialogToolBar");
        tools.addAction(previewAction);
        tools.addAction(restoreDefaultSettingsAction);
        tools.addAction(loadSettingsFromFileAction);
        tools.addAction(saveSettingsToFileAction);
        win.addToolBar(tools);
        win.setCentralWidget(centralWidget);

        // левая вертикальная панель
        listWidget = new ListWidget(centralWidget);
        listWidget.currentItemChanged.connect(this, "onListWidgetClicked(QListWidgetItem, QListWidgetItem)");

        QFont font = new QFont();
        font.setBold(true);
        font.setPixelSize(12);

        // элементы в левой вертикальной панели
        QListWidgetItem firstNode = new QListWidgetItem(listWidget);

        firstNode.setIcon(ExplorerIcon.getQIcon(ClientIcon.Definitions.TREE));
        firstNode.setText(Application.translate("Settings Dialog",
                "Explorer Tree"));
        firstNode.setTextAlignment(AlignmentFlag.AlignHCenter.value());
        firstNode.setFlags(ItemFlag.ItemIsSelectable, ItemFlag.ItemIsEnabled);
        firstNode.setFont(font);
        firstNode.setSelected(true);
        firstNode.setBackground(this.palette().highlight());

        QListWidgetItem secondNode = new QListWidgetItem(listWidget);
        secondNode.setIcon(ExplorerIcon.getQIcon(ClientIcon.Definitions.EDITOR));
        secondNode.setText(Application.translate("Settings Dialog", "Editor"));
        secondNode.setFont(font);
        secondNode.setTextAlignment(AlignmentFlag.AlignHCenter.value());
        secondNode.setFlags(ItemFlag.ItemIsSelectable, ItemFlag.ItemIsEnabled);

        QListWidgetItem thirdNode = new QListWidgetItem(listWidget);
        thirdNode.setIcon(ExplorerIcon.getQIcon(ClientIcon.Definitions.SELECTOR));
        thirdNode.setText(Application.translate("Settings Dialog", "Selector"));
        thirdNode.setFont(font);
        thirdNode.setTextAlignment(AlignmentFlag.AlignHCenter.value());
        thirdNode.setFlags(ItemFlag.ItemIsSelectable, ItemFlag.ItemIsEnabled);

        QListWidgetItem fourth = new QListWidgetItem(listWidget);
        fourth.setIcon(ExplorerIcon.getQIcon(Icons.SRC_EDITOR_SETTINGS));
        fourth.setText(Application.translate("Settings Dialog", "Source Code"));
        fourth.setFont(font);
        fourth.setTextAlignment(AlignmentFlag.AlignHCenter.value());
        fourth.setFlags(ItemFlag.ItemIsSelectable, ItemFlag.ItemIsEnabled);
        /* total refactoring is necessary!!!*/
        QListWidgetItem appearanceSettingsItem = new QListWidgetItem(listWidget);
        appearanceSettingsItem.setIcon(ExplorerIcon.getQIcon(Icons.Definitions.APPEARANCE));//заменить в будущем!
        appearanceSettingsItem.setText(Application.translate("Settings Dialog", "Appearance"));
        appearanceSettingsItem.setFont(font);
        appearanceSettingsItem.setTextAlignment(AlignmentFlag.AlignHCenter.value());
        appearanceSettingsItem.setFlags(ItemFlag.ItemIsSelectable, ItemFlag.ItemIsEnabled);

        listWidget.resizeToContents();
        listWidget.setCurrentRow(0);

        QHBoxLayout horizLayout = new QHBoxLayout();
        horizLayout.setSpacing(10);
        horizLayout.setContentsMargins(0, 10, 0, 0);
        horizLayout.addWidget(listWidget);
        centralWidget.setLayout(horizLayout);

        // нижняя панель с 3-мя кнопками
        BottomWidget bottomWidget = new BottomWidget(this);

        // стек под все страницы с настройками
        QStackedWidget stackedWidget = new QStackedWidget(centralWidget);

        // страница(ы) с настройками
        QTabWidget treeSettings = new QTabWidget(centralWidget);
        QTabWidget editorSettings = new QTabWidget(centralWidget);
        QTabWidget selectorSettings = new QTabWidget(centralWidget);
        QTabWidget sourceCodeSettings = new QTabWidget(centralWidget);

        // //// первый нод дерева (общие настройки
        // эксплорера)///////////////////////////////////////////////////////////////
        //QVBoxLayout vboxLayout = new QVBoxLayout();
        // первая вкладка
        QWidget w0 = new QWidget();
        w0.setObjectName("Rdx.SettingsDialog.w0");
        QGridLayout gridLayout0 = new QGridLayout(null);
        gridLayout0.setWidgetSpacing(16);
        gridLayout0.setContentsMargins(10, 20, 0, 0);
        gridLayout0.setAlignment(new Alignment(AlignmentFlag.AlignLeft,
                AlignmentFlag.AlignTop));

        SizesSettingsWidget fsw0 = new SizesSettingsWidget(getEnvironment(), w0,
                SettingNames.EXPLORER_TREE_GROUP,
                SettingNames.ExplorerTree.COMMON_GROUP,
                SettingNames.ExplorerTree.Common.ICON_SIZE, Application.translate("Settings Dialog", "Icon size"), gridLayout0);
        fsw0.addToParent(0);

        settingsArrayList.add(fsw0);

        GroupColorSettingsWidget gcsw = new GroupColorSettingsWidget(getEnvironment(), w0,
                SettingNames.EXPLORER_TREE_GROUP,
                SettingNames.ExplorerTree.COMMON_GROUP, gridLayout0);

        gcsw.addColorWidget(1,
                SettingNames.ExplorerTree.Common.TREE_BACKGROUND, Application.translate("Settings Dialog", "Background color"));
        gcsw.addColorWidget(2,
                SettingNames.ExplorerTree.Common.TREE_SELECTED_ITEM_FONT_COLOR,
                Application.translate("Settings Dialog",
                        "Tree selected item font color"));
        gcsw.addColorWidget(3,
                SettingNames.ExplorerTree.Common.TREE_SELECTED_ITEM_BACKGROUND,
                Application.translate("Settings Dialog",
                        "Tree selected item background color"));
        //vboxLayout.addWidget(gcsw);
        settingsArrayList.add(gcsw);

        ComboBoxAlignmentSettingsWidget treeAlignmentsSettings = new ComboBoxAlignmentSettingsWidget(getEnvironment(),
                w0, SettingNames.EXPLORER_TREE_GROUP,
                SettingNames.ExplorerTree.COMMON_GROUP,
                SettingNames.ExplorerTree.Common.TREE_AREA);
        treeAlignmentsSettings.setValues(
                Arrays.asList(AlignmentFlag.AlignLeft, AlignmentFlag.AlignRight, AlignmentFlag.AlignTop, AlignmentFlag.AlignBottom), true);
        gridLayout0.addWidget(new QLabel(Application.translate("Settings Dialog", "Position"), w0), 4, 0);
        gridLayout0.addWidget(treeAlignmentsSettings, 4, 1);
        settingsArrayList.add(treeAlignmentsSettings);

        final CheckBoxSettingsWidget swRestorePos = new CheckBoxSettingsWidget(getEnvironment(),
                w0,
                SettingNames.EXPLORER_TREE_GROUP,
                SettingNames.ExplorerTree.COMMON_GROUP,
                SettingNames.ExplorerTree.Common.RESTORE_POSITION,
                Application.translate("Settings Dialog", "Remember current item"));

        gridLayout0.addWidget(swRestorePos, 6, 0);
        settingsArrayList.add(swRestorePos);

        final CheckBoxSettingsWidget swKeepUserEI = new CheckBoxSettingsWidget(getEnvironment(),
                w0,
                SettingNames.EXPLORER_TREE_GROUP,
                SettingNames.ExplorerTree.COMMON_GROUP,
                SettingNames.ExplorerTree.Common.KEEP_USER_EI,
                Application.translate("Settings Dialog", "Save user-defined items"));

        gridLayout0.addWidget(swKeepUserEI, 7, 0);

        settingsArrayList.add(swKeepUserEI);

        w0.setLayout(gridLayout0);

        // вторая вкладка
        QWidget w1 = new QWidget();
        w1.setObjectName("Rdx.SettingsDialog.w1");
        QGridLayout gridLayout1 = new QGridLayout(w1);
        gridLayout1.setWidgetSpacing(16);
        gridLayout1.setContentsMargins(10, 20, 0, 0);
        gridLayout1.setAlignment(new Alignment(AlignmentFlag.AlignLeft,
                AlignmentFlag.AlignTop));

        CheckBoxSettingsWidget fsw11 = new CheckBoxSettingsWidget(getEnvironment(), this,
                SettingNames.EXPLORER_TREE_GROUP,
                SettingNames.ExplorerTree.PARAGRAPH_GROUP,
                SettingNames.ExplorerTree.Common.SHOW_ICONS, Application.translate("Settings Dialog", "Show icons"));

        settingsArrayList.add(fsw11);

        FontSettingsWidget fsw12 = new FontSettingsWidget(getEnvironment(), this,
                SettingNames.EXPLORER_TREE_GROUP,
                SettingNames.ExplorerTree.PARAGRAPH_GROUP,
                SettingNames.ExplorerTree.Common.FONT, Application.translate("Settings Dialog", "Font"));
        fsw12.addToParent(0, gridLayout1);

        settingsArrayList.add(fsw12);

        GroupColorSettingsWidget gcsw1 = new GroupColorSettingsWidget(getEnvironment(), this,
                SettingNames.EXPLORER_TREE_GROUP,
                SettingNames.ExplorerTree.PARAGRAPH_GROUP, gridLayout1);
        SettingsWidget sw11 = gcsw1.addColorWidget(1,
                SettingNames.ExplorerTree.Common.BACKGROUND_COLOR,
                Application.translate("Settings Dialog", "Background color"));
        SettingsWidget sw12 = gcsw1.addColorWidget(2,
                SettingNames.ExplorerTree.Common.FOREGROUND_COLOR,
                Application.translate("Settings Dialog", "Foreground color"));

        gridLayout1.addWidget(fsw11, 3, 0);

        settingsArrayList.add(sw11);
        settingsArrayList.add(sw12);

        // третья вкладка
        QWidget w2 = new QWidget();
        w2.setObjectName("Rdx.SettingsDialog.w2");
        QGridLayout gridLayout2 = new QGridLayout(w2);
        gridLayout2.setWidgetSpacing(16);
        gridLayout2.setContentsMargins(10, 20, 0, 0);
        gridLayout2.setAlignment(new Alignment(AlignmentFlag.AlignLeft,
                AlignmentFlag.AlignTop));

        CheckBoxSettingsWidget fsw21 = new CheckBoxSettingsWidget(getEnvironment(), this,
                SettingNames.EXPLORER_TREE_GROUP,
                SettingNames.ExplorerTree.SELECTOR_GROUP,
                SettingNames.ExplorerTree.Common.SHOW_ICONS, Application.translate("Settings Dialog", "Show icons"));

        settingsArrayList.add(fsw21);

        FontSettingsWidget fsw22 = new FontSettingsWidget(getEnvironment(), this,
                SettingNames.EXPLORER_TREE_GROUP,
                SettingNames.ExplorerTree.SELECTOR_GROUP,
                SettingNames.ExplorerTree.Common.FONT, Application.translate(
                        "Settings Dialog", "Font"));
        fsw22.addToParent(0, gridLayout2);

        settingsArrayList.add(fsw22);

        GroupColorSettingsWidget gcsw2 = new GroupColorSettingsWidget(getEnvironment(), this,
                SettingNames.EXPLORER_TREE_GROUP,
                SettingNames.ExplorerTree.SELECTOR_GROUP, gridLayout2);
        SettingsWidget sw21 = gcsw2.addColorWidget(1,
                SettingNames.ExplorerTree.Common.BACKGROUND_COLOR,
                Application.translate("Settings Dialog", "Background color"));
        SettingsWidget sw22 = gcsw2.addColorWidget(2,
                SettingNames.ExplorerTree.Common.FOREGROUND_COLOR,
                Application.translate("Settings Dialog", "Foreground color"));

        gridLayout2.addWidget(fsw21, 3, 0);

        settingsArrayList.add(sw21);
        settingsArrayList.add(sw22);

        // четвертая вкладка
        QWidget w3 = new QWidget();
        w3.setObjectName("Rdx.SettingsDialog.w3");
        QGridLayout gridLayout3 = new QGridLayout(w3);
        gridLayout3.setWidgetSpacing(16);
        gridLayout3.setContentsMargins(10, 20, 0, 0);
        gridLayout3.setAlignment(new Alignment(AlignmentFlag.AlignLeft,
                AlignmentFlag.AlignTop));

        CheckBoxSettingsWidget fsw31 = new CheckBoxSettingsWidget(getEnvironment(), this,
                SettingNames.EXPLORER_TREE_GROUP,
                SettingNames.ExplorerTree.EDITOR_GROUP,
                SettingNames.ExplorerTree.Common.SHOW_ICONS, Application.translate("Settings Dialog", "Show icons"));

        settingsArrayList.add(fsw31);

        FontSettingsWidget fsw32 = new FontSettingsWidget(getEnvironment(), this,
                SettingNames.EXPLORER_TREE_GROUP,
                SettingNames.ExplorerTree.EDITOR_GROUP,
                SettingNames.ExplorerTree.Common.FONT, Application.translate(
                        "Settings Dialog", "Font"));

        fsw32.addToParent(0, gridLayout3);

        settingsArrayList.add(fsw32);

        GroupColorSettingsWidget gcsw3 = new GroupColorSettingsWidget(getEnvironment(), this,
                SettingNames.EXPLORER_TREE_GROUP,
                SettingNames.ExplorerTree.EDITOR_GROUP, gridLayout3);
        SettingsWidget sw31 = gcsw3.addColorWidget(1,
                SettingNames.ExplorerTree.Common.BACKGROUND_COLOR, Application.translate("Settings Dialog", "Background color"));
        SettingsWidget sw32 = gcsw3.addColorWidget(2,
                SettingNames.ExplorerTree.Common.FOREGROUND_COLOR, Application.translate("Settings Dialog", "Foreground color"));

        CheckBoxSettingsWidget fsw33 = new CheckBoxSettingsWidget(getEnvironment(),
                this,
                SettingNames.EXPLORER_TREE_GROUP,
                SettingNames.ExplorerTree.EDITOR_GROUP,
                SettingNames.ExplorerTree.Editor.EDIT_AFTER_INSERT,
                Application.translate("Settings Dialog",
                        "Open editor after inserting object into tree"));

        gridLayout3.addWidget(fsw31, 3, 0);

        gridLayout3.addWidget(fsw33, 4, 0, 1, 3, AlignmentFlag.AlignLeft);

        settingsArrayList.add(fsw33);

        settingsArrayList.add(sw31);
        settingsArrayList.add(sw32);

        //вкладка для пользовательских элементов проводника
        final QWidget userEISettingsTab = new QWidget();
        {
            userEISettingsTab.setObjectName("Rdx.SettingsDialog.userEISettingsTab");
            final QGridLayout gridLayout = new QGridLayout(userEISettingsTab);
            gridLayout.setWidgetSpacing(16);
            gridLayout.setContentsMargins(10, 20, 0, 0);
            gridLayout.setAlignment(new Alignment(AlignmentFlag.AlignLeft,
                    AlignmentFlag.AlignTop));

            final FontSettingsWidget fontSettings = new FontSettingsWidget(getEnvironment(), this,
                    SettingNames.EXPLORER_TREE_GROUP,
                    SettingNames.ExplorerTree.USER_GROUP,
                    SettingNames.ExplorerTree.Common.FONT, Application.translate(
                            "Settings Dialog", "Font"));
            fontSettings.addToParent(0, gridLayout);

            settingsArrayList.add(fontSettings);

            final GroupColorSettingsWidget colorSettings = new GroupColorSettingsWidget(getEnvironment(), this,
                    SettingNames.EXPLORER_TREE_GROUP,
                    SettingNames.ExplorerTree.USER_GROUP, gridLayout);
            final SettingsWidget bgColorSettings = colorSettings.addColorWidget(1,
                    SettingNames.ExplorerTree.Common.BACKGROUND_COLOR,
                    Application.translate("Settings Dialog", "Background color"));
            final SettingsWidget fgColorSettings = colorSettings.addColorWidget(2,
                    SettingNames.ExplorerTree.Common.FOREGROUND_COLOR,
                    Application.translate("Settings Dialog", "Foreground color"));

            settingsArrayList.add(bgColorSettings);
            settingsArrayList.add(fgColorSettings);

            final CheckBoxSettingsWidget cbShowIcons = new CheckBoxSettingsWidget(getEnvironment(), this,
                    SettingNames.EXPLORER_TREE_GROUP,
                    SettingNames.ExplorerTree.USER_GROUP,
                    SettingNames.ExplorerTree.Common.SHOW_ICONS, Application.translate("Settings Dialog", "Show icons"));

            gridLayout.addWidget(cbShowIcons, 3, 0);
            settingsArrayList.add(cbShowIcons);
        }

        // конкретные вкладки для страниц
        treeSettings.addTab(w0, Application.translate("Settings Dialog", "General"));
        treeSettings.addTab(w1, Application.translate("Settings Dialog", "Paragraph"));
        treeSettings.addTab(w2, Application.translate("Settings Dialog", "Selector"));
        treeSettings.addTab(w3, Application.translate("Settings Dialog", "Editor"));
        treeSettings.addTab(userEISettingsTab, Application.translate("Settings Dialog", "User-Defined Item"));

        // // /второй нод дерева
        // (editor)/////////////////////////////////////////////////////////////////////////////////////////
        // первая вкладка
        QWidget w4 = new QWidget();
        w4.setObjectName("Rdx.SettingsDialog.w4");
        QGridLayout gridLayout4 = new QGridLayout(w4);
        gridLayout4.setWidgetSpacing(16);
        gridLayout4.setContentsMargins(10, 20, 0, 0);
        gridLayout4.setAlignment(new Alignment(AlignmentFlag.AlignLeft,
                AlignmentFlag.AlignTop));

        SizesSettingsWidget ssw = new SizesSettingsWidget(getEnvironment(), this,
                SettingNames.EDITOR_GROUP, SettingNames.Editor.COMMON_GROUP,
                SettingNames.Editor.Common.ICON_SIZE_IN_TOOLBARS, Application.translate("Settings Dialog", "Icon size in toolbars"),
                gridLayout4);
        ssw.addToParent(0);
        settingsArrayList.add(ssw);

        //
        SizesSettingsWidget ssw2 = new SizesSettingsWidget(getEnvironment(), this,
                SettingNames.EDITOR_GROUP, SettingNames.Editor.COMMON_GROUP,
                SettingNames.Editor.Common.ICON_SIZE_IN_TABS, Application.translate("Settings Dialog", "Icon size in tabs"),
                gridLayout4);
        ssw2.addToParent(1);
        settingsArrayList.add(ssw2);
        //

        FontSettingsWidget fsw = new FontSettingsWidget(getEnvironment(), this,
                SettingNames.EDITOR_GROUP, SettingNames.Editor.COMMON_GROUP,
                SettingNames.Editor.Common.FONT_IN_TABS, Application.translate(
                        "Settings Dialog", "Font in tabs"));
        fsw.addToParent(2, gridLayout4);
        settingsArrayList.add(fsw);

        final CheckBoxSettingsWidget swRestoreTab = new CheckBoxSettingsWidget(getEnvironment(), this,
                SettingNames.EDITOR_GROUP,
                SettingNames.Editor.COMMON_GROUP,
                SettingNames.Editor.Common.RESTORE_TAB,
                Application.translate("Settings Dialog", "Remember current tab"));
        gridLayout4.addWidget(swRestoreTab, 3, 0, 1, 2, Qt.AlignmentFlag.AlignLeft);
        settingsArrayList.add(swRestoreTab);
        
        final CheckBoxSettingsWidget swWarnAboutMandatory = new CheckBoxSettingsWidget(getEnvironment(), this,
                SettingNames.EDITOR_GROUP,
                SettingNames.Editor.COMMON_GROUP,
                SettingNames.Editor.Common.CHECK_MANDATORY_ON_CLOSE,
                Application.translate("Settings Dialog", "Warn about undefined value for mandatory property when closing editor"));
        gridLayout4.addWidget(swWarnAboutMandatory, 4, 0, 1, 2, Qt.AlignmentFlag.AlignLeft);
        settingsArrayList.add(swWarnAboutMandatory);
        

        //
        editorSettings.addTab(w4, Application.translate("Settings Dialog",
                Application.translate("Settings Dialog", "General")));
                
        // вторая вкладка
        editorSettings.addTab(createEditorPropertiesTabs(), Application.translate("Trace Message", Application.translate(
                "Settings Dialog", "Properties")));

        // //// третий нод дерева
        // (selector)////////////////////////////////////////////////////////////////////////////////////////////
        // первая вкладка
        QWidget w7 = new QWidget();
        w7.setObjectName("Rdx.SettingsDialog.w7");
        QGridLayout gridLayout7 = new QGridLayout();
        gridLayout7.setWidgetSpacing(16);
        gridLayout7.setContentsMargins(10, 20, 0, 0);
        gridLayout7.setAlignment(new Alignment(AlignmentFlag.AlignLeft,
                AlignmentFlag.AlignTop));

        SizesSettingsWidget ssw7 = new SizesSettingsWidget(getEnvironment(), this,
                SettingNames.SELECTOR_GROUP,
                SettingNames.Selector.COMMON_GROUP,
                SettingNames.Selector.Common.ICON_SIZE_IN_SELECTOR_TOOLBARS,
                Application.translate("Settings Dialog",
                        "Icon size in selector toolbars"), gridLayout7);
        ssw7.addToParent(0);
        settingsArrayList.add(ssw7);

        final CheckBoxSettingsWidget swRestoreFilter = new CheckBoxSettingsWidget(getEnvironment(), this,
                SettingNames.SELECTOR_GROUP,
                SettingNames.Selector.COMMON_GROUP,
                SettingNames.Selector.Common.SAVE_FILTER,
                Application.translate("Settings Dialog", "Remember current filter"));
        gridLayout7.addWidget(swRestoreFilter, 1, 0, 1, 2, Qt.AlignmentFlag.AlignLeft);
        settingsArrayList.add(swRestoreFilter);

        FontSettingsWidget fswww7 = new FontSettingsWidget(getEnvironment(), this,
                SettingNames.SELECTOR_GROUP,
                SettingNames.Selector.COMMON_GROUP,
                SettingNames.Selector.Common.HEADER_FONT_IN_SELECTOR,
                Application.translate("Settings Dialog",
                        "Header font in selector"));
        fswww7.addToParent(2, gridLayout7);
        settingsArrayList.add(fswww7);

        selectorSettings.addTab(w7, Application.translate("Trace Message",
                Application.translate("Settings Dialog", "General")));

        QLabel titleAlignLabel = new QLabel(Application.translate(
                "Settings Dialog", "Header alignment"));
        gridLayout7.addWidget(titleAlignLabel, 3, 0);
        ComboBoxAlignmentSettingsWidget titleAlignmentsSettings = new ComboBoxAlignmentSettingsWidget(getEnvironment(),
                this, SettingNames.SELECTOR_GROUP,
                SettingNames.Selector.COMMON_GROUP,
                SettingNames.Selector.Common.TITLES_ALIGNMENT);
        gridLayout7.addWidget(titleAlignmentsSettings, 3, 1);
        settingsArrayList.add(titleAlignmentsSettings);

        AlternativeColorSettingsWidget altColorSettingsWidget = new AlternativeColorSettingsWidget(getEnvironment(),
                this, SettingNames.SELECTOR_GROUP,
                SettingNames.Selector.COMMON_GROUP,
                SettingNames.Selector.Common.SLIDER_VALUE);
        QHBoxLayout hbl = new QHBoxLayout();
        hbl.setAlignment(new Alignment(AlignmentFlag.AlignLeft));
        hbl.addWidget(altColorSettingsWidget);
        QGroupBox groupBox = new QGroupBox(Application.translate(
                "Settings Dialog", "Alternative Background Color"));
        groupBox.setLayout(hbl);
        altColorSettingsWidget.setSizePolicy(Policy.Maximum, Policy.Maximum);

        final ColorSettingsWidget frameColorWidget
                = new ColorSettingsWidget(getEnvironment(), this, SettingNames.SELECTOR_GROUP,
                        SettingNames.Selector.COMMON_GROUP,
                        SettingNames.Selector.Common.FRAME_COLOR,
                        Application.translate("Settings Dialog", "Current cell frame color"));
        frameColorWidget.addToParent(5, gridLayout7);
        settingsArrayList.add(frameColorWidget);
        
        final ColorSettingsWidget rowFrameColorWidget
                = new ColorSettingsWidget(getEnvironment(), this, SettingNames.SELECTOR_GROUP,
                        SettingNames.Selector.COMMON_GROUP,
                        SettingNames.Selector.Common.ROW_FRAME_COLOR,
                        Application.translate("Settings Dialog", "Current row frame color"));
        rowFrameColorWidget.addToParent(6, gridLayout7);
        settingsArrayList.add(rowFrameColorWidget);
        
        final ColorSettingsWidget selectedRowColorWidget
                = new ColorSettingsWidget(getEnvironment(), this, SettingNames.SELECTOR_GROUP,
                        SettingNames.Selector.COMMON_GROUP,
                        SettingNames.Selector.Common.SELECTED_ROW_COLOR,
                        Application.translate("Settings Dialog", "Selected object background color"));
        selectedRowColorWidget.addToParent(7, gridLayout7);
        settingsArrayList.add(selectedRowColorWidget);        

        QVBoxLayout vbl = new QVBoxLayout(w7);
        groupBox.setSizePolicy(Policy.Maximum, Policy.Maximum);
        vbl.setAlignment(new Alignment(AlignmentFlag.AlignTop,
                AlignmentFlag.AlignLeft));

        vbl.addLayout(gridLayout7);
        vbl.addWidget(groupBox);
        settingsArrayList.add(altColorSettingsWidget);
        // вторая вкладка (Styles)
        selectorSettings.addTab(createInnerListWidget(), Application.translate(
                "Trace Message", Application.translate("Settings Dialog",
                        "Styles")));
        // третья вкладка (настройки выравнивания)
        final ColumnAlignmentSettings alignmentSettings = new ColumnAlignmentSettings(getEnvironment(), selectorSettings);
        settingsArrayList.add(alignmentSettings);
        selectorSettings.addTab((QWidget) alignmentSettings, getEnvironment().getMessageProvider().translate("Settings Dialog", "Alignment"));

        //четвертый нод дерева
        sourceCodeSettings.addTab(createSourceCodeOptionsWidget(SettingNames.SourceCode.SYNTAX_JML), Application.translate(
                "Trace Message", Application.translate("Settings Dialog",
                        "Source Code Options for Jml")));
        sourceCodeSettings.addTab(createSourceCodeOptionsWidget(SettingNames.SourceCode.SYNTAX_SQML), Application.translate(
                "Trace Message", Application.translate("Settings Dialog",
                        "Source Code Options for Sqml")));
        //sourceCodeSettings.addTab(createSourceCodeOptionsWidget(), Environment.translate(
        //        "Trace Message", Environment.translate("Settings Dialog",
        //        "Sqml Code Options")));

        //пятый нод - настройка внешнего вида
        QWidget appearance = new AppearanceStyleSettingWidget(environment, this);
        settingsArrayList.add((AppearanceStyleSettingWidget) appearance);
        // заносим страницы в стек
        stackedWidget.addWidget(treeSettings);
        stackedWidget.addWidget(editorSettings);
        stackedWidget.addWidget(selectorSettings);
        stackedWidget.addWidget(sourceCodeSettings);
        stackedWidget.addWidget(appearance);
        // считали все настройки для всех сеттингс виджетов
        readAllSettings((ExplorerSettings) getEnvironment().getConfigStore());

        horizLayout.addWidget(stackedWidget);
        listWidget.currentRowChanged.connect(stackedWidget,
                "setCurrentIndex(int)");

        QVBoxLayout mainLayout = dialogLayout();
        mainLayout.setContentsMargins(10, 0, 10, 0);
        mainLayout.addWidget(win);
        mainLayout.addWidget(bottomWidget);
        setLayout(mainLayout);
    }

    private QWidget createEditorPropertiesTabs() {
        // стек под все вкладки значений свойств
        QStackedWidget stackedWidget = new QStackedWidget();
        // страница(ы) с настройками
        QTabWidget propertiesSettings = new QTabWidget();
        // вкладки
        // readonly
        QWidget readonlyTab = new QWidget();
        readonlyTab.setObjectName("Rdx.SettingsDialog.readonlyTab");
        QVBoxLayout layer1 = new QVBoxLayout();
        layer1.setAlignment(new Alignment(AlignmentFlag.AlignTop));

        PropertySettingsWidget gb11 = new PropertySettingsWidget(getEnvironment(), this,
                SettingNames.EDITOR_GROUP,
                SettingNames.Editor.PROPERTY_TITLES_GROUP,
                SettingNames.Properties.READONLY_PROPERTY,
                Application.translate("Settings Dialog", "Name"), false);
        layer1.addWidget(gb11, 0, AlignmentFlag.AlignTop);
        settingsArrayList.add(gb11);

        InheritablePropertySettingsWidget gb12 = new InheritablePropertySettingsWidget(getEnvironment(), this,
                SettingNames.EDITOR_GROUP,
                SettingNames.Editor.PROPERTY_VALUES_GROUP,
                SettingNames.Properties.READONLY_PROPERTY, Application.translate("Settings Dialog", "Value"));
        layer1.addWidget(gb12);
        settingsArrayList.add(gb12);

        readonlyTab.setLayout(layer1);
        // mandatory
        QWidget mandatoryTab = new QWidget();
        mandatoryTab.setObjectName("Rdx.SettingsDialog.mandatoryTab");
        QVBoxLayout layer2 = new QVBoxLayout();
        layer2.setAlignment(new Alignment(AlignmentFlag.AlignTop));

        PropertySettingsWidget gb21 = new PropertySettingsWidget(getEnvironment(), this,
                SettingNames.EDITOR_GROUP,
                SettingNames.Editor.PROPERTY_TITLES_GROUP,
                SettingNames.Properties.MANDATORY_PROPERTY, Application.translate("Settings Dialog", "Name"), false);
        layer2.addWidget(gb21, 0, AlignmentFlag.AlignTop);
        settingsArrayList.add(gb21);

        InheritablePropertySettingsWidget gb22 = new InheritablePropertySettingsWidget(getEnvironment(), this,
                SettingNames.EDITOR_GROUP,
                SettingNames.Editor.PROPERTY_VALUES_GROUP,
                SettingNames.Properties.MANDATORY_PROPERTY, Application.translate("Settings Dialog", "Value"));
        layer2.addWidget(gb22);
        settingsArrayList.add(gb22);

        mandatoryTab.setLayout(layer2);
        // other
        QWidget otherTab = new QWidget();
        otherTab.setObjectName("Rdx.SettingsDialog.otherTab");
        QVBoxLayout layer3 = new QVBoxLayout();
        layer3.setAlignment(new Alignment(AlignmentFlag.AlignTop));

        PropertySettingsWidget gb31 = new PropertySettingsWidget(getEnvironment(), this,
                SettingNames.EDITOR_GROUP,
                SettingNames.Editor.PROPERTY_TITLES_GROUP,
                SettingNames.Properties.OTHER_PROPERTY, Application.translate(
                        "Settings Dialog", "Name"), false);
        layer3.addWidget(gb31, 0, AlignmentFlag.AlignTop);
        settingsArrayList.add(gb31);

        InheritablePropertySettingsWidget gb32 = new InheritablePropertySettingsWidget(getEnvironment(), this,
                SettingNames.EDITOR_GROUP,
                SettingNames.Editor.PROPERTY_VALUES_GROUP,
                SettingNames.Properties.OTHER_PROPERTY, Application.translate(
                        "Settings Dialog", "Value"));
        layer3.addWidget(gb32);
        settingsArrayList.add(gb32);

        otherTab.setLayout(layer3);

        // конкретные вкладки
        propertiesSettings.addTab(readonlyTab, Application.translate(
                "Settings Dialog", "Readonly"));
        propertiesSettings.addTab(mandatoryTab, Application.translate(
                "Settings Dialog", "Mandatory"));
        propertiesSettings.addTab(otherTab, Application.translate(
                "Settings Dialog", "Other"));

        stackedWidget.addWidget(propertiesSettings);

        return stackedWidget;
    }

    private QWidget createInnerListWidget() {

        QWidget w = new QWidget();
        w.setObjectName("Rdx.SettingsDialog.w");
        QHBoxLayout hboxlayout = new QHBoxLayout(w);
        w.setLayout(hboxlayout);
        hboxlayout.setWidgetSpacing(0);
        hboxlayout.setContentsMargins(8, 0, 0, 0);
        hboxlayout.setAlignment(new Alignment(AlignmentFlag.AlignLeft,
                AlignmentFlag.AlignTop));

        // внутренняя вертикальная панель
        QVBoxLayout vboxlayout = new QVBoxLayout();
        vboxlayout.setContentsMargins(0, 9, 0, 10);

        QListWidget innerListWidget = new QListWidget();
        innerListWidget.setViewMode(QListView.ViewMode.ListMode);
        innerListWidget.setFlow(Flow.TopToBottom);
        innerListWidget.setMovement(QListView.Movement.Static);
        innerListWidget.setSpacing(0);
        innerListWidget.currentRowChanged.connect(this,
                "onInnerListWidgetChangeIndex(int)");
        innerListWidget.setMouseTracking(true);
        vboxlayout.addWidget(innerListWidget);

        previewWidget = new PreviewWidget(this,getEnvironment());
        vboxlayout.addWidget(previewWidget);

        innerStackedWidget = new QStackedWidget();
        // ///////////////////заполняем элементы для выбора всех стилей
        LinkedHashMap<ESelectorRowStyle, String> params = new LinkedHashMap<ESelectorRowStyle, String>(); // "имя													// стиля"
        // -->
        // "имя
        // вкладки"

        params.put(ESelectorRowStyle.NORMAL, Application.translate(
                "Settings Dialog", "Normal"));
        params.put(ESelectorRowStyle.FAVORITE, Application.translate(
                "Settings Dialog", "Favorite"));
        params.put(ESelectorRowStyle.UNIMPORTANT, Application.translate(
                "Settings Dialog", "Unimportant"));
        params.put(ESelectorRowStyle.IMPORTANT, Application.translate(
                "Settings Dialog", "Important"));
        params.put(ESelectorRowStyle.POOR, Application.translate(
                "Settings Dialog", "Poor"));
        params.put(ESelectorRowStyle.BAD, Application.translate(
                "Settings Dialog", "Bad"));
        params.put(ESelectorRowStyle.VERY_BAD, Application.translate(
                "Settings Dialog", "Very Bad"));
        params.put(ESelectorRowStyle.RATHER_GOOD, Application.translate(
                "Settings Dialog", "Rather Good"));
        params.put(ESelectorRowStyle.GOOD, Application.translate(
                "Settings Dialog", "Good"));
        params.put(ESelectorRowStyle.VERY_GOOD, Application.translate(
                "Settings Dialog", "Very Good"));

        PropertySettingsWidget psw = null;
        SelectorSettingsWidget pss = null;
        StylePageSettingsWidget stylePage = null;

        for (Map.Entry<ESelectorRowStyle, String> pair : params.entrySet()) {
            stylePage = new StylePageSettingsWidget(getEnvironment(), this,
                    SettingNames.SELECTOR_GROUP,
                    SettingNames.Selector.STYLES_GROUP, pair.getKey().getValue());

            // связывание сигналов с методами по изменению атрибутов
            // PreviewWidget
            pss = stylePage.getReadonlyPropertySettingsWidget();
            pss.updateDfColorSignal.connect(this,
                    "updatePreviewWidget()");
            pss.updateNdfColorSignal.connect(this,
                    "updatePreviewWidget()");
            pss.updateBgrnColorSignal.connect(this,
                    "updatePreviewWidget()");
            pss.updateFontSignal.connect(this,
                    "updatePreviewWidget()");

            psw = stylePage.getMandatoryPropertySettingsWidget();
            psw.updateFgrnColorSignal.connect(this,
                    "updatePreviewWidget()");
            psw.updateBgrnColorSignal.connect(this,
                    "updatePreviewWidget()");
            psw.updateFontSignal.connect(this,
                    "updatePreviewWidget()");

            pss = stylePage.getOtherPropertySettingsWidget();
            pss.updateDfColorSignal.connect(this,
                    "updatePreviewWidget()");
            pss.updateNdfColorSignal.connect(this,
                    "updatePreviewWidget()");
            pss.updateBgrnColorSignal.connect(this,
                    "updatePreviewWidget()");
            pss.updateFontSignal.connect(this, "updatePreviewWidget()");

            settingsArrayList.add(stylePage);
            innerStackedWidget.addWidget(stylePage);
            innerListWidget.addItem(pair.getValue());
        }
        innerStackedWidget.setSizePolicy(QSizePolicy.Policy.Maximum, QSizePolicy.Policy.Preferred);
        innerListWidget.setCurrentRow(0);

        hboxlayout.addLayout(vboxlayout);
        hboxlayout.addWidget(innerStackedWidget);
        return w;
    }
    private QStackedWidget sourceStackedJmlWidget;
    private QStackedWidget sourceStackedSqmlWidget;
    //private QStackedWidget sourceStackedWidget;
    private Highlighter hightlighterJml = null;
    private Highlighter hightlighterSqml = null;

    private QWidget createSourceCodeOptionsWidget(final String subgroup) {

        QWidget w = new QWidget();
        w.setObjectName("Rdx.SettingsDialog.w");
        //внешняя вертикальная панель
        QVBoxLayout vboxlayout = new QVBoxLayout(w);
        //vboxlayout.setContentsMargins(0, 9, 0, 10);
        //внутренняя горизонтальная панель
        QHBoxLayout hboxlayout = new QHBoxLayout();
        hboxlayout.setWidgetSpacing(0);
        //hboxlayout.setContentsMargins(8, 0, 0, 0);
        hboxlayout.setAlignment(new Alignment(AlignmentFlag.AlignLeft, AlignmentFlag.AlignTop));

        QListWidget innerListWidget = new QListWidget();
        innerListWidget.setViewMode(QListView.ViewMode.ListMode);
        innerListWidget.setFlow(Flow.TopToBottom);
        innerListWidget.setMovement(QListView.Movement.Static);
        innerListWidget.setSpacing(0);
        String method = subgroup.equals(SettingNames.SourceCode.SYNTAX_JML) ? "onHilightListJmlWidgetChangeIndex(int)" : "onHilightListSqmlWidgetChangeIndex(int)";
        innerListWidget.currentRowChanged.connect(this, method);
        innerListWidget.setMouseTracking(true);
        innerListWidget.setSizePolicy(Policy.Preferred, Policy.Maximum);
        hboxlayout.addWidget(innerListWidget);

        //previewWidget = new PreviewWidget(this);
        //vboxlayout.addWidget(previewWidget);
        // QStackedWidget sourceStackedWidget = new QStackedWidget();
        /////////////////////заполняем элементы для выбора всех стилей
        LinkedHashMap<String, String> params = new LinkedHashMap<String, String>(); // "имя вкладки"
        params.put(SettingNames.SourceCode.Syntax.DEFAULT, Application.translate(
                "Settings Dialog", "Default"));
        params.put(SettingNames.SourceCode.Syntax.RESERVED_WORDS, Application.translate(
                "Settings Dialog", "Reserved Words"));
        params.put(SettingNames.SourceCode.Syntax.COMMENTS, Application.translate(
                "Settings Dialog", "Comments"));
        params.put(SettingNames.SourceCode.Syntax.STRINGS, Application.translate(
                "Settings Dialog", "Strings"));
        params.put(SettingNames.SourceCode.Syntax.NUMBERS, Application.translate(
                "Settings Dialog", "Numbers"));
        params.put(SettingNames.SourceCode.Syntax.SYMBOLS, Application.translate(
                "Settings Dialog", "Symbols"));
        params.put(SettingNames.SourceCode.Syntax.SEPARATORS, Application.translate(
                "Settings Dialog", "Separators"));
        params.put(SettingNames.SourceCode.Syntax.SEPARATORS, Application.translate(
                "Settings Dialog", "Separators"));

        QTextEdit ed = new QTextEdit();
        QStackedWidget sourceStackedWidget = addTagsToSettings(params, subgroup, ed);
        Highlighter hightlighter = subgroup.equals(SettingNames.SourceCode.SYNTAX_JML) ? hightlighterJml : hightlighterSqml;

        PropertySettingsWidget psw = null;
        SourceCodePageSettingsWidget sourcePage = null;
        SourceCodePageSettingsWidget defaultSourcePage = null;
        for (Entry pair : params.entrySet()) {
            sourcePage = new SourceCodePageSettingsWidget(getEnvironment(), this,
                    SettingNames.SOURCE_EDITOR,
                    subgroup, (String) pair.getKey(), defaultSourcePage);

            // связывание сигналов с методами по изменению атрибутов
            // PreviewWidget
            psw = sourcePage.getPropertySettingsWidget();
            psw.updateFgrnColorSignal.connect(hightlighter,
                    "setForegroundColor(QColor,String)");
            psw.updateBgrnColorSignal.connect(hightlighter,
                    "setBackgroundColor(QColor,String)");
            psw.updateFontSignal.connect(hightlighter,
                    "setFont(QFont,String)");

            settingsArrayList.add(sourcePage);
            sourceStackedWidget.addWidget(sourcePage);
            innerListWidget.addItem((String) pair.getValue());
            if (defaultSourcePage == null) {
                defaultSourcePage = sourcePage;
            }
        }
        defaultSourcePage.updateSrcEditorSettingsSignal.connect(this, "srcCodeEditorSettingsChanged(SourceCodePageSettingsWidget)");
        innerListWidget.setCurrentRow(0);

        hboxlayout.addWidget(sourceStackedWidget);

        vboxlayout.addLayout(hboxlayout);

        vboxlayout.addWidget(ed);

        return w;
    }

    @SuppressWarnings("unused")
    private void srcCodeEditorSettingsChanged(SourceCodePageSettingsWidget defaultSettingsWidget) {
        QFont font = defaultSettingsWidget.getPropertySettingsWidget().getFont();
        QColor frgColor = defaultSettingsWidget.getPropertySettingsWidget().getForegroundColor();
        QColor bgrColor = defaultSettingsWidget.getPropertySettingsWidget().getBackgroundColor();
        int index = defaultSettingsWidget.getSettingCfgName().lastIndexOf("/");
        String defSettingsName = defaultSettingsWidget.getSettingCfgName().substring(0, index);
        for (SettingsWidget settingsWidget : settingsArrayList) {
            if (settingsWidget instanceof SourceCodePageSettingsWidget) {
                index = settingsWidget.getSettingCfgName().lastIndexOf("/");
                String settingsName = settingsWidget.getSettingCfgName().substring(0, index);
                if (defSettingsName.equals(settingsName)) {
                    SourceCodePageSettingsWidget srcCodePage = (SourceCodePageSettingsWidget) settingsWidget;
                    srcCodePage.change(frgColor, bgrColor, font);
                }
            }
        }
    }

    private QStackedWidget addTagsToSettings(LinkedHashMap<String, String> params, final String subgroup, QTextEdit textEditor) {
        if (subgroup.equals(SettingNames.SourceCode.SYNTAX_JML)) {
            params.put(SettingNames.SourceCode.Syntax.JML_TAG_ID, Application.translate(
                    "Settings Dialog", "Identifier Tag"));
            params.put(SettingNames.SourceCode.Syntax.JML_TAG_INVOCATE, Application.translate(
                    "Settings Dialog", "Invocation Tag"));
            params.put(SettingNames.SourceCode.Syntax.JML_TAG_TYPE, Application.translate(
                    "Settings Dialog", "Type Tag"));
            params.put(SettingNames.SourceCode.Syntax.JML_TAG_DB_ENTITY, Application.translate(
                    "Settings Dialog", "Entity Tag"));
            params.put(SettingNames.SourceCode.Syntax.JML_DB_NAME, Application.translate(
                    "Settings Dialog", "Definition Database Name Tag"));
            params.put(SettingNames.SourceCode.Syntax.JML_TAG_LOCALIZED_STR, Application.translate(
                    "Settings Dialog", "Localized String"));

            textEditor.setText("String s=\"str\";\nint i=12;  //comment");
            hightlighterJml = new Highlighter(getEnvironment(), textEditor, null, "org.radixware.explorer/S_E/SYNTAX_JML/");
            sourceStackedJmlWidget = new QStackedWidget();
            return sourceStackedJmlWidget;
        } else {
            params.put(SettingNames.SourceCode.Syntax.SQML_CONSTANT_VALUE, Application.translate(
                    "Settings Dialog", "Constant Value Tag"));
            params.put(SettingNames.SourceCode.Syntax.SQML_PARENT_CONDITION, Application.translate(
                    "Settings Dialog", "Parent Condition Tag"));
            //params.put(SettingNames.SourceCode.Syntax.SQML_PARENT_PROP_REF_SQL_NAME, Environment.translate(
            //        "Settings Dialog", "SqmlParentPropRefSqlName"));
            params.put(SettingNames.SourceCode.Syntax.SQML_PROP_SQL_NAME, Application.translate(
                    "Settings Dialog", "Property Tag"));
            params.put(SettingNames.SourceCode.Syntax.SQML_TABLE_SQL_NAME, Application.translate(
                    "Settings Dialog", "Table Tag"));
            //params.put(SettingNames.SourceCode.Syntax.SQML_THIS_TABLE_SQL_ID, Environment.translate(
            //        "Settings Dialog", "This Table Tag"));
            params.put(SettingNames.SourceCode.Syntax.SQML_THIS_TABLE_SQL_NAME, Application.translate(
                    "Settings Dialog", "Context Table Tag"));
            params.put(SettingNames.SourceCode.Syntax.SQML_TYPIFIED_VALUE, Application.translate(
                    "Settings Dialog", "Typified Value Tag"));
            params.put(SettingNames.SourceCode.Syntax.SQML_PARAMETER, Application.translate(
                    "Settings Dialog", "Parameter Tag"));
            params.put(SettingNames.SourceCode.Syntax.SQML_ENTITY_REF_PARAMETER, Application.translate(
                    "Settings Dialog", "Entity Reference Parameter Tag"));
            params.put(SettingNames.SourceCode.Syntax.SQML_DB_FUNC_CALL, Application.translate(
                    "Settings Dialog", "SQL Function Tag"));
            params.put(SettingNames.SourceCode.Syntax.SQML_EVENT_CODE, Application.translate(
                    "Settings Dialog", "Event Code Tag"));
            params.put(SettingNames.SourceCode.Syntax.SQML_ID_PATH, Application.translate(
                    "Settings Dialog", "Identifier Path Tag"));
            params.put(SettingNames.SourceCode.Syntax.SQML_DB_NAME, Application.translate(
                    "Settings Dialog", "Definition Database Name Tag"));
            params.put(SettingNames.SourceCode.Syntax.SQML_PREPROCESSOR, Application.translate(
                    "Settings Dialog", "Preprocessor Tag"));            
            params.put(SettingNames.SourceCode.Syntax.SQML_UNKNOWN_TAG, Application.translate(
                    "Settings Dialog", "Unknown Tag"));

            //JmlProcessor converter=new JmlProcessor();
            textEditor.setText("Select column1, column2 From someTable\nWhere column3=0 and column4=\"some string\"  --comment");
            hightlighterSqml = new Highlighter(getEnvironment(), textEditor, new SqmlProcessor(getEnvironment(), null, null), "org.radixware.explorer/S_E/SYNTAX_SQML/");
            sourceStackedSqmlWidget = new QStackedWidget();
            return sourceStackedSqmlWidget;
        }

    }

    private void writeAllSettings(final ExplorerSettings dst) {
        for (SettingsWidget w : settingsArrayList) {
            w.writeSettings(dst);
        }
    }

    private void readAllSettings(final ExplorerSettings src) {
        for (SettingsWidget w : settingsArrayList) {
            w.readSettings(src);
        }
        updatePreviewWidget();
    }

    @SuppressWarnings("unused")
    private void onListWidgetClicked(QListWidgetItem current, QListWidgetItem previous) {
        final QPalette curPalette = palette();
        final QBrush highlightedTextBrush = curPalette.base(), highlightBrush = curPalette.highlight();
        if (previous != null) {
            previous.setBackground(highlightedTextBrush);
        }
        current.setBackground(highlightBrush);
    }

    @SuppressWarnings("unused")
    private void onInnerListWidgetChangeIndex(int i) {
        if (i != innerStackedWidget.currentIndex()) {
            innerStackedWidget.setCurrentIndex(i);
            updatePreviewWidget();
        }
    }

    @SuppressWarnings("unused")
    private void onHilightListJmlWidgetChangeIndex(int i) {
        if (i != sourceStackedJmlWidget.currentIndex()) {
            sourceStackedJmlWidget.setCurrentIndex(i);

        }
    }

    @SuppressWarnings("unused")
    private void onHilightListSqmlWidgetChangeIndex(int i) {
        if (i != sourceStackedSqmlWidget.currentIndex()) {
            sourceStackedSqmlWidget.setCurrentIndex(i);

        }
    }

    private void updatePreviewWidget() {
        final ExplorerTextOptions readonlyDefTextOptions
                = ((StylePageSettingsWidget) (innerStackedWidget.currentWidget())).getReadonlyPropertySettingsWidget().getTextOptions(true);
        final ExplorerTextOptions readonlyUndefTextOptions
                = ((StylePageSettingsWidget) (innerStackedWidget.currentWidget())).getReadonlyPropertySettingsWidget().getTextOptions(false);
        final ExplorerTextOptions mandatoryDefTextOptions
                = ((StylePageSettingsWidget) (innerStackedWidget.currentWidget())).getMandatoryPropertySettingsWidget().getTextOptions();
        final ExplorerTextOptions otherDefTextOptions
                = ((StylePageSettingsWidget) (innerStackedWidget.currentWidget())).getOtherPropertySettingsWidget().getTextOptions(true);
        final ExplorerTextOptions otherUndefTextOptions
                = ((StylePageSettingsWidget) (innerStackedWidget.currentWidget())).getOtherPropertySettingsWidget().getTextOptions(false);
        previewWidget.update(readonlyDefTextOptions, mandatoryDefTextOptions, otherDefTextOptions, true);
        previewWidget.update(readonlyUndefTextOptions, mandatoryDefTextOptions, otherUndefTextOptions, false);
    }
}
