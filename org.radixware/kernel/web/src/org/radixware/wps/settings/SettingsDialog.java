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
package org.radixware.wps.settings;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.enums.ESelectorRowStyle;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.starter.radixloader.RadixLoader;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.WpsSettings;
import org.radixware.wps.rwt.Alignment;
import org.radixware.wps.rwt.Container;
import org.radixware.wps.rwt.Dialog;
import org.radixware.wps.rwt.GroupBox;
import org.radixware.wps.rwt.Label;
import org.radixware.wps.rwt.ListBox;
import org.radixware.wps.rwt.TabLayout;
import org.radixware.wps.rwt.TabLayout.Tab;
import org.radixware.wps.rwt.TableLayout;
import org.radixware.wps.rwt.ToolButton;
import org.radixware.wps.rwt.UIObject;
import org.radixware.wps.rwt.VerticalBox;
import org.radixware.wps.rwt.uploading.IUploadedDataReader;
import org.radixware.wps.rwt.uploading.LoadFileAction;
import org.radixware.wps.text.WpsTextOptions;
import org.radixware.wps.views.RwtAction;

public class SettingsDialog extends Dialog {

    private final ToolButton previewBtn = new ToolButton();
    private final  ToolButton restoreBtn = new ToolButton();
    private final  ToolButton loadBtn = new ToolButton();
    private final  ToolButton saveBtn = new ToolButton();
    private final  ListBox listItemsWidget = new ListBox();
    private TableLayout.Row.Cell tabSetCell;
    private ExplorerSettingsWidget explorerTreeTabs;
    private EditorSettingsWidget editorTabs;
    private SelectorSettingsWidget selectorTabs;
    private FormatSettingsWidget formatTabs;
    //private SourceCodeSettingsWidget srcCodeTabs;
    private LinkedList<UIObject> tabsList;
    private final  ListBox.ListBoxItem explorerTreeItem = new ListBox.ListBoxItem();
    private final  ListBox.ListBoxItem editorItem = new ListBox.ListBoxItem();
    private final  ListBox.ListBoxItem selectorItem = new ListBox.ListBoxItem();
    private final  ListBox.ListBoxItem formatItem = new ListBox.ListBoxItem();
    //private ListBox.ListBoxItem sourceCodeItem = new ListBox.ListBoxItem();
    private final String previewFileName = "preview.ini";
    private final  WpsSettings previewSettings;
    private WpsSettings backedupSettings;
    private final File previewFile;//preview settings store in radix loader temporary dir
    private final IClientEnvironment env;
    private final WpsEnvironment e;
    private final MessageProvider messageProvider;
    private final ArrayList<SettingsWidget> settingsArrayList = new ArrayList<>(); // ArrayList
    private PreviewWidget previewWidget;
    private final HashMap<ESelectorRowStyle, StylePageSettingsWidget> stylePageWidgets = new LinkedHashMap<>();
    private final  ListBox listBox = new ListBox();
    private static final LinkedHashMap<ESelectorRowStyle, String> stylesHashMap;
    private boolean validate = true;

    static {
        stylesHashMap = new LinkedHashMap<>();
        stylesHashMap.put(ESelectorRowStyle.NORMAL, getEnvironmentStatic().getApplication().getMessageProvider().translate(
                "Settings Dialog", "Normal"));
        stylesHashMap.put(ESelectorRowStyle.FAVORITE, getEnvironmentStatic().getApplication().getMessageProvider().translate(
                "Settings Dialog", "Favorite"));
        stylesHashMap.put(ESelectorRowStyle.UNIMPORTANT, getEnvironmentStatic().getApplication().getMessageProvider().translate(
                "Settings Dialog", "Unimportant"));
        stylesHashMap.put(ESelectorRowStyle.IMPORTANT, getEnvironmentStatic().getApplication().getMessageProvider().translate(
                "Settings Dialog", "Important"));
        stylesHashMap.put(ESelectorRowStyle.POOR, getEnvironmentStatic().getApplication().getMessageProvider().translate(
                "Settings Dialog", "Poor"));
        stylesHashMap.put(ESelectorRowStyle.BAD, getEnvironmentStatic().getApplication().getMessageProvider().translate(
                "Settings Dialog", "Bad"));
        stylesHashMap.put(ESelectorRowStyle.VERY_BAD, getEnvironmentStatic().getApplication().getMessageProvider().translate(
                "Settings Dialog", "Very Bad"));
        stylesHashMap.put(ESelectorRowStyle.RATHER_GOOD, getEnvironmentStatic().getApplication().getMessageProvider().translate(
                "Settings Dialog", "Rather Good"));
        stylesHashMap.put(ESelectorRowStyle.GOOD, getEnvironmentStatic().getApplication().getMessageProvider().translate(
                "Settings Dialog", "Good"));
        stylesHashMap.put(ESelectorRowStyle.VERY_GOOD, getEnvironmentStatic().getApplication().getMessageProvider().translate(
                "Settings Dialog", "Very Good"));
    }

    private final static class Icons extends ClientIcon.CommonOperations {

        private Icons(final String fileName) {
            super(fileName, true);
        }
        private final static Icons PREVIEW = new Icons("classpath:images/preview.svg");
        //private final static Icons SRC_EDITOR_SETTINGS = new Icons("classpath:images/srcedit.svg");
        private final static Icons APPEARANCE_SETTINGS = new Icons("classpath:images/appearance_settings.svg");
        private final static Icons FORMAT_SETTINGS = new Icons("classpath:images/format_settings.svg");
    }

    public SettingsDialog(IClientEnvironment env) {
        super(env, null);
        this.env = env;
        this.e = (WpsEnvironment) env;

        this.messageProvider = e.getApplication().getMessageProvider();
        this.setAdjustSizeEnabled(false);
        previewFile = RadixLoader.getInstance().createTempFileWithExactName(previewFileName);
        readAllSettings((WpsSettings) getEnvironment().getConfigStore());

        previewSettings = new WpsSettings();
        previewSettings.beginGroup("PreviewSettings");
        setupUI();
    }

    public File getPreviewSettingsFile() {
        return previewFile;
    }

    @Override
    public DialogResult execDialog() {
        // забэкапили текущие настройки
        backedupSettings = (WpsSettings) getEnvironment().getConfigStore();
        readAllSettings(backedupSettings);
        return super.execDialog();
    }

    private void writeAllSettings(final WpsSettings dst) {
        for (SettingsWidget w : settingsArrayList) {
            w.writeSettings(dst);
        }
    }
    
    private boolean validateAllSettings() {
        for (SettingsWidget w : settingsArrayList) {
            if (!w.validate()) {
                validate = false;
                return false;
            }
        }
        validate = true;
        return true;
    }

    private void readAllSettings(final WpsSettings src) {
        for (SettingsWidget w : settingsArrayList) {
            w.readSettings(src);
        }
        updatePreviewWidget();
    }

    public void saveSettingsToFile() {
        if (validateAllSettings()) { 
            FileOutputStream output = null;
            File f = null;
            try {
                File dir = RadixLoader.getInstance().createTempFile("PropertyValueStore");
                boolean isDir = dir.mkdir();
                String fileName = "wpsSettings.ini";
                if (isDir && dir.isDirectory() && dir.exists()) {

                    f = new File(dir, fileName);
                    f.createNewFile();
                    output = new FileOutputStream(f);

                    StringBuilder strSett = new StringBuilder();
                    WpsSettings saveSettings = new WpsSettings();
                    writeAllSettings(saveSettings);
                    for (SettingsWidget s : settingsArrayList) {
                        String settingName = s.getSettingCfgName();
                        Object widgetSetting = saveSettings.getValue(settingName);
                        if (widgetSetting != null) {
                            strSett.append(settingName).append("=").append(widgetSetting).append("\n");
                        }
                    }
                    try {
                        FileUtils.writeString(output, strSett.toString(), FileUtils.XML_ENCODING);
                    } catch (IOException ex) {
                        Logger.getLogger(SettingsDialog.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if (f.exists() && f.isFile()) {
                        e.sendFileToTerminal(f.getName(), f, null, false, false);//send file to terminal
                    } else {
                        throw new FileNotFoundException("Could not create file for webUI settings storing.");
                    }
                } else {
                    throw new FileNotFoundException("Could not create temporary directory for webUI settings storing.");
                }
            } catch (FileNotFoundException ex) {
                String mess = String.format("File not found \n%s", f);
                getEnvironment().processException(mess, ex);
            } catch (IOException ex) {
                String mess = String.format("Failed to save settings %s to a file\n%s", f);
                getEnvironment().processException(mess, ex);
            } finally {
                if (output != null) {
                    try {
                        output.close();
                    } catch (IOException ex) {
                        getEnvironment().getTracer().error(ex);
                    }
                }
            }
        }
    }

    public void restoreSettings() {
        final String title = messageProvider.translate("ExplorerMessage", "Confirm to Clear Settings");
        final String message = messageProvider.translate("ExplorerMessage", "Do you really want to reset all your appearance settings to default values?");
        if (e.messageConfirmation(title, message)) {
            for (SettingsWidget w : settingsArrayList) {
                w.restoreDefaults();
            }
        }
        updatePreviewWidget();
    }

    public void preview() {
        if (validateAllSettings()) {
            writeAllSettings(previewSettings);
            e.applySettings(previewSettings);
        }
    }

    public void abortSettings() {
        //apply backed up settings
        e.applySettings(backedupSettings);
        close(DialogResult.REJECTED);
    }

    public void applySettings() {

    }

    private void setupUI() {
        setWindowTitle(messageProvider.translate("Settings Dialog", "Appearance Settings"));
        setWindowIcon(env.getApplication().getImageManager().getIcon(Icons.APPEARANCE_SETTINGS));
        setWidth(560);
        setHeight(655);
        //this.setResizable(false);
        //getHtml().setCss("overflow", "auto");

        TableLayout layout = new TableLayout();

        //layout.getAnchors().setTop(new Anchors.Anchor(1.0f, -10, this));
        add(layout);

        TableLayout.Row firstRow = layout.addRow();
        layout.addVerticalSpace();
        TableLayout.Row.Cell buttonsCell = firstRow.addCell();
        buttonsCell.add(createButtonsContainer());        
        buttonsCell.getHtml().setAttr("colspan","3");

        TableLayout.Row secondRow = layout.addRow();
        TableLayout.Row.Cell listCell = secondRow.addCell();
        secondRow.addSpace();

        listCell.setWidth(75);
        listItemsWidget.setWidth(75);
        listCell.add(listItemsWidget);
        listCell.getHtml().setAttr("listCell", true);
        listCell.getHtml().getParent().setCss("vertical-align", "top");
        listCell.getHtml().setCss("box-shadow", "2px 2px 2px");
        listItemsWidget.getHtml().setCss("text-align", "center");

        tabSetCell = secondRow.addCell();

        secondRow.getHtml().setCss("display", "block");//!important
        secondRow.getHtml().setCss("width", "100%");//!important

        tabSetCell.getHtml().setAttr("tabSetCell", true);
        tabSetCell.getHtml().setCss("height", "100%");//!important avoid tabs squeezing IE-9
        tabSetCell.getHtml().setCss("width", "100%");//!important avoid tabs squeezing IE-9
        //tabSetCell.getHtml().getParent().setCss("height", "1px");//for vertical resizing of cell
        tabSetCell.getHtml().getParent().setCss("width", "100%");//for proper window horizontal resizing 
        tabSetCell.getHtml().setCss("box-shadow", "2px 2px 2px");

        tabSetCell.getHtml().getParent().setCss("vertical-align", "top");

        LinkedList<ListBox.ListBoxItem> items = createItemsSettingsList();
        for (ListBox.ListBoxItem item : items) {
            item.setIconSize(40, 40);
            listItemsWidget.add(item);
        }
        tabsList = new LinkedList<>();

        explorerTreeTabs = new ExplorerSettingsWidget();
        tabsList.add(explorerTreeTabs);

        editorTabs = new EditorSettingsWidget();
        tabsList.add(editorTabs);

        selectorTabs = new SelectorSettingsWidget();
        tabsList.add(selectorTabs);
        
        formatTabs = new FormatSettingsWidget((WpsEnvironment) getEnvironment());
        tabsList.add(formatTabs);
        settingsArrayList.add(formatTabs);
        
        /*srcCodeTabs = new SourceCodeSettingsWidget();
         tabsList.add(srcCodeTabs);*/
        listItemsWidget.addCurrentItemListener(new ListBox.CurrentItemListener() {
            @Override
            public void currentItemChanged(ListBox.ListBoxItem currentItem) {
                updateTabs(currentItem);
            }
        });
        listItemsWidget.setCurrentRow(0);

        previewBtn.addClickHandler(new IButton.ClickHandler() {
            @Override
            public void onClick(IButton source) {
                preview();
            }
        });
        previewBtn.setToolTip(messageProvider.translate("Settings Dialog", "Preview"));
        previewBtn.setIcon(env.getApplication().getImageManager().getIcon(Icons.PREVIEW));

        IUploadedDataReader reader = new IUploadedDataReader() {
            @Override
            public void readData(InputStream stream, String fileName, long fileSize) {
                if (fileName != null && !fileName.equals("")) {

                    try {
                        String settingsStr = FileUtils.readTextStream(stream, FileUtils.XML_ENCODING);
                        WpsSettings loadedSettings = new WpsSettings();
                        String[] str = settingsStr.split("\\r?\\n");
                        for (String s : str) {
                            ///*if (s.matches("")) {
                            String[] setting = s.split("=");
                            if (setting.length == 2) {
                                String key = setting[0];
                                String value = setting[1];
                                loadedSettings.setValue(key, value);
                            }
                        }
                        for (SettingsWidget w : settingsArrayList) {
                            w.readSettings(loadedSettings);

                        }
                    } catch (IOException ex) {
                        Logger.getLogger(SettingsDialog.class
                                .getName()).log(Level.SEVERE, null, ex);
                    } finally {
                        if (stream != null) {
                            try {
                                stream.close();

                            } catch (IOException ex) {
                                Logger.getLogger(SettingsDialog.class
                                        .getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                }
            }
        };
        final LoadFileAction action = new LoadFileAction(e, reader);
        action.addActionPresenter((RwtAction.IActionPresenter) loadBtn);
        loadBtn.addAction(action);
        loadBtn.setToolTip(messageProvider.translate("Settings Dialog", "Load Settings"));
        loadBtn.setIcon(env.getApplication().getImageManager().getIcon(Icons.OPEN));

        saveBtn.addClickHandler(new IButton.ClickHandler() {
            @Override
            public void onClick(IButton source) {
                saveSettingsToFile();

            }
        });
        saveBtn.setToolTip(messageProvider.translate("Settings Dialog", "Save Settings"));
        saveBtn.setIcon(env.getApplication().getImageManager().getIcon(Icons.SAVE));

        restoreBtn.setIcon(env.getApplication().getImageManager().getIcon(Icons.CANCEL));
        restoreBtn.setToolTip(messageProvider.translate("Settings Dialog", "Restore Default Settings"));
        restoreBtn.addClickHandler(new IButton.ClickHandler() {
            @Override
            public void onClick(IButton source) {
                restoreSettings();
            }
        });

        addCloseAction(EDialogButtonType.OK).addClickHandler(new IButton.ClickHandler() {
            @Override
            public void onClick(IButton source) {
                applySettings();
            }
        });

        addCloseAction(EDialogButtonType.CANCEL).addClickHandler(new IButton.ClickHandler() {
            @Override
            public void onClick(IButton source) {
                abortSettings();
            }
        });
    }

    @Override
    protected DialogResult onClose(String action, DialogResult actionResult) {
        if (actionResult == DialogResult.ACCEPTED) {
            if (validateAllSettings()) {
                // собираем все настройки с SettingsWidget-ов, применяем их и сохраняем
                // в файл
                writeAllSettings(backedupSettings);
                e.applySettings(backedupSettings);
                // обновляем файл настроек
                if (getEnvironment().getConfigStore().isWritable()) {
                    getEnvironment().getConfigStore().sync();
                }

                if (e.getMainWindow() != null) {
                    //do stuff
                }
                //do stuff
                return super.onClose(action, actionResult);
            } else {
                return null;
            }
        } else {
            return super.onClose(action, actionResult);
        }
    }
    
    

    private TableLayout createButtonsContainer() {
        TableLayout box = new TableLayout();

        box.getHtml().setCss("width", "auto");
        box.getHtml().setCss("box-shadow", "2px 2px 2px");

        previewBtn.getHtml().addClass("rwt-setting-buttons");
        previewBtn.setIconSize(25, 25);
        previewBtn.setWidth(30);

        TableLayout.Row r = box.addRow();

        r.addCell().add(previewBtn);

        restoreBtn.getHtml().addClass("rwt-setting-buttons");
        restoreBtn.setIconSize(25, 25);
        restoreBtn.setWidth(30);
        r.addCell().add(restoreBtn);

        loadBtn.getHtml().addClass("rwt-setting-buttons");
        loadBtn.setIconSize(25, 25);
        loadBtn.setWidth(30);
        r.addCell().add(loadBtn);

        saveBtn.getHtml().addClass("rwt-setting-buttons");
        saveBtn.setIconSize(25, 25);
        saveBtn.setWidth(30);
        r.addCell().add(saveBtn);

        return box;
    }

    protected class TabInners extends Container {

        protected TableLayout innerTable = new TableLayout();

        public TabInners() {
            super();
            this.add(innerTable);
            this.html.setCss("scroll", "auto");
            this.html.setCss("height", "inherit");
            this.html.setAttr("tabInners", getHtmlId());
            innerTable.getHtml().setCss("padding", "5px");
        }

        public void addSettingWidget(SettingsWidget o) {
            TableLayout.Row.Cell cell = innerTable.addRow().addCell();
            cell.add(o);
            innerTable.addVerticalSpace(2);
            if (!settingsArrayList.contains(o)) {
                settingsArrayList.add(o);
            }
        }

        public void addSettingWidget(SettingsWidget o, String title) {
            TableLayout.Row row = innerTable.addRow();
            addLabel(row, title);
            TableLayout.Row.Cell cell = row.addCell();
            cell.getHtml().setAttr("widgetcell", true);
            cell.add(o);
            innerTable.addVerticalSpace(2);
            if (!settingsArrayList.contains(o)) {
                settingsArrayList.add(o);
            }
        }

        public void addRegularWidget(UIObject child) {
            TableLayout.Row row = innerTable.addRow();
            TableLayout.Row.Cell cell = row.addCell();

            cell.add(child);
            cell.getHtml().setAttr("widgetcell", true);
            innerTable.addVerticalSpace(2);
        }

        private void addLabel(TableLayout.Row row, String text) {
            TableLayout.Row.Cell cell = row.addCell();
            cell.getHtml().setAttr("labelcell", true);
            /*cell.getHtml().setCss("max-width", "100px");
             cell.getHtml().setCss("width", "100%");*/
            Label l = new Label(text);
            cell.getHtml().addClass("rwt-table-cell-label");
            cell.add(l);
        }

        public void removeSettingWidget(UIObject o) {
            if (o != null) {
                this.remove(o);
                if (o instanceof SettingsWidget && o.getParent() != null) {
                    this.remove(o.getParent());//delete widget cell
                    if (o.getParent().getParent() != null) {
                        this.remove(o.getParent().getParent());//delete widget row with label
                    }
                }
            }
        }
    }

    private class ExplorerSettingsWidget extends TabLayout {

        TabInners generalSettings;
        TabInners paragraphSettings;
        TabInners selectorSettings;
        TabInners editorSettings;
        TabInners udiSettings;

        ExplorerSettingsWidget() {
            super();
            drawExplorerSettingsTabs();
            this.setHeight(550);
        }

        private void drawExplorerSettingsTabs() {
            Tab tabgeneralSettings = addTab(0, messageProvider.translate("Settings Dialog", "General"));

            tabgeneralSettings.add(generalSettings = new TabInners());

            Tab tabparagraphSettings = addTab(1, messageProvider.translate("Settings Dialog", "Paragraph"));
            tabparagraphSettings.add(paragraphSettings = new TabInners());

            Tab tabselectorSettings = addTab(2, messageProvider.translate("Settings Dialog", "Selector"));
            tabselectorSettings.add(selectorSettings = new TabInners());

            Tab tabeditorSettings = addTab(3, messageProvider.translate("Settings Dialog", "Editor"));
            tabeditorSettings.add(editorSettings = new TabInners());

            Tab tabudiSettings = addTab(4, messageProvider.translate("Settings Dialog", "User-Defined Item"));
            tabudiSettings.add(udiSettings = new TabInners());
            
            drawParagraph();
            drawGeneral();
            drawSelector();
            drawEditor();
            drawUDI();
        }

        private void drawUDI() {
            String title1 = messageProvider.translate("Settings Dialog", "Background color");
            ColorSettingsWidget backGroundColor = new ColorSettingsWidget(e, this,
                    SettingNames.EXPLORER_TREE_GROUP,
                    SettingNames.ExplorerTree.USER_GROUP,
                    SettingNames.ExplorerTree.Common.BACKGROUND_COLOR);

            udiSettings.addSettingWidget(backGroundColor, title1);

            String title2 = messageProvider.translate("Settings Dialog", "Foreground color");
            ColorSettingsWidget foreGroundColor = new ColorSettingsWidget(e, this,
                    SettingNames.EXPLORER_TREE_GROUP,
                    SettingNames.ExplorerTree.USER_GROUP,
                    SettingNames.ExplorerTree.Common.FOREGROUND_COLOR);

            udiSettings.addSettingWidget(foreGroundColor, title2);

            String title3 = messageProvider.translate("SettingDialog", "Show icons");
            CheckBoxSettingsWidget showIcons = new CheckBoxSettingsWidget(e, this,
                    SettingNames.EXPLORER_TREE_GROUP,
                    SettingNames.ExplorerTree.USER_GROUP,
                    SettingNames.ExplorerTree.Common.SHOW_ICONS,
                    title3);

            udiSettings.addSettingWidget(showIcons, title3);
        }

        private void drawEditor() {
            String t0 = messageProvider.translate("Settings Dialog", "Background color");
            ColorSettingsWidget backGroundColor = new ColorSettingsWidget(e, this,
                    SettingNames.EXPLORER_TREE_GROUP,
                    SettingNames.ExplorerTree.EDITOR_GROUP,
                    SettingNames.ExplorerTree.Common.BACKGROUND_COLOR);

            editorSettings.addSettingWidget(backGroundColor, t0);

            String t1 = messageProvider.translate("Settings Dialog", "Foreground color");
            ColorSettingsWidget foreGroundColor = new ColorSettingsWidget(e, this,
                    SettingNames.EXPLORER_TREE_GROUP,
                    SettingNames.ExplorerTree.EDITOR_GROUP,
                    SettingNames.ExplorerTree.Common.FOREGROUND_COLOR);

            editorSettings.addSettingWidget(foreGroundColor, t1);

            String t2 = messageProvider.translate("SettingDialog", "Show icons");
            final CheckBoxSettingsWidget showIcons = new CheckBoxSettingsWidget(e, this,
                    SettingNames.EXPLORER_TREE_GROUP,
                    SettingNames.ExplorerTree.EDITOR_GROUP,
                    SettingNames.ExplorerTree.Common.SHOW_ICONS,
                    t2);

            editorSettings.addSettingWidget(showIcons, t2);

            String t3 = messageProvider.translate("SettingDialog", "Open editor after inserting object into tree");
            final CheckBoxSettingsWidget openEditor = new CheckBoxSettingsWidget(e, this,
                    SettingNames.EXPLORER_TREE_GROUP,
                    SettingNames.ExplorerTree.EDITOR_GROUP,
                    SettingNames.ExplorerTree.Editor.EDIT_AFTER_INSERT,
                    t3);

            editorSettings.addSettingWidget(openEditor, t3);
        }

        private void drawSelector() {
            String t0 = messageProvider.translate("Settings Dialog", "Background color");
            ColorSettingsWidget backGroundColor = new ColorSettingsWidget(e, this,
                    SettingNames.EXPLORER_TREE_GROUP,
                    SettingNames.ExplorerTree.SELECTOR_GROUP,
                    SettingNames.ExplorerTree.Common.BACKGROUND_COLOR);

            selectorSettings.addSettingWidget(backGroundColor, t0);

            String t1 = messageProvider.translate("Settings Dialog", "Foreground color");
            ColorSettingsWidget foreGroundColor = new ColorSettingsWidget(e,
                    this,
                    SettingNames.EXPLORER_TREE_GROUP,
                    SettingNames.ExplorerTree.SELECTOR_GROUP,
                    SettingNames.ExplorerTree.Common.FOREGROUND_COLOR);

            selectorSettings.addSettingWidget(foreGroundColor, t1);

            String t2 = messageProvider.translate("SettingDialog", "Show icons");
            CheckBoxSettingsWidget showIcons = new CheckBoxSettingsWidget(e, this,
                    SettingNames.EXPLORER_TREE_GROUP,
                    SettingNames.ExplorerTree.SELECTOR_GROUP,
                    SettingNames.ExplorerTree.Common.SHOW_ICONS,
                    t2);

            selectorSettings.addSettingWidget(showIcons, t2);
        }

        private void drawParagraph() {
            String t0 = messageProvider.translate("Settings Dialog", "Background color");
            ColorSettingsWidget backGroundColor = new ColorSettingsWidget(e, this,
                    SettingNames.EXPLORER_TREE_GROUP,
                    SettingNames.ExplorerTree.PARAGRAPH_GROUP,
                    SettingNames.ExplorerTree.Common.BACKGROUND_COLOR);
            paragraphSettings.addSettingWidget(backGroundColor, t0);

            String t1 = messageProvider.translate("Settings Dialog", "Foreground color");
            ColorSettingsWidget foreGroundColor = new ColorSettingsWidget(e,
                    this,
                    SettingNames.EXPLORER_TREE_GROUP,
                    SettingNames.ExplorerTree.PARAGRAPH_GROUP,
                    SettingNames.ExplorerTree.Common.FOREGROUND_COLOR);
            paragraphSettings.addSettingWidget(foreGroundColor, t1);

            String t2 = messageProvider.translate("SettingDialog", "Show icons");
            CheckBoxSettingsWidget showIcons = new CheckBoxSettingsWidget(e, this,
                    SettingNames.EXPLORER_TREE_GROUP, SettingNames.ExplorerTree.PARAGRAPH_GROUP,
                    SettingNames.ExplorerTree.Common.SHOW_ICONS,
                    t2);
            paragraphSettings.addSettingWidget(showIcons, t2);

        }

        private void drawGeneral() {
            String t0 = messageProvider.translate("Settings Dialog", "Background color");
            ColorSettingsWidget backGroundColor = new ColorSettingsWidget(e, this, SettingNames.EXPLORER_TREE_GROUP,
                    SettingNames.ExplorerTree.COMMON_GROUP,
                    SettingNames.ExplorerTree.Common.TREE_BACKGROUND);
            //csw.setLabel(messageProvider.translate("AppearanceSettingsDialog", "Background Color"));
            generalSettings.addSettingWidget(backGroundColor, t0);

            String t1 = messageProvider.translate("Settings Dialog", "Tree selected item font color");
            ColorSettingsWidget treeItemFontColor = new ColorSettingsWidget(e, this,
                    SettingNames.EXPLORER_TREE_GROUP,
                    SettingNames.ExplorerTree.COMMON_GROUP,
                    SettingNames.ExplorerTree.Common.TREE_SELECTED_ITEM_FONT_COLOR);
            generalSettings.addSettingWidget(treeItemFontColor, t1);

            String t2 = messageProvider.translate("Settings Dialog", "Tree selected item background color");
            ColorSettingsWidget treeItemBackColor = new ColorSettingsWidget(e, this,
                    SettingNames.EXPLORER_TREE_GROUP, SettingNames.ExplorerTree.COMMON_GROUP,
                    SettingNames.ExplorerTree.Common.TREE_SELECTED_ITEM_BACKGROUND);
            generalSettings.addSettingWidget(treeItemBackColor, t2);

            String t5 = messageProvider.translate("Settings Dialog", "Position");
            LinkedList<Alignment> aligns = new LinkedList<>();
            aligns.add(Alignment.LEFT);
            aligns.add(Alignment.RIGHT);
            aligns.add(Alignment.TOP);
            aligns.add(Alignment.BOTTOM);
            AlignmentSettingsWidget position = new AlignmentSettingsWidget(e, this,
                    SettingNames.EXPLORER_TREE_GROUP, SettingNames.ExplorerTree.COMMON_GROUP,
                    SettingNames.ExplorerTree.Common.TREE_AREA, aligns);
            generalSettings.addSettingWidget(position, t5);

            String t3 = messageProvider.translate("Settings Dialog", "Remember current item");
            CheckBoxSettingsWidget rememberCurrent = new CheckBoxSettingsWidget(e, this,
                    SettingNames.EXPLORER_TREE_GROUP, SettingNames.ExplorerTree.COMMON_GROUP,
                    SettingNames.ExplorerTree.Common.RESTORE_POSITION,
                    t3);
            generalSettings.addSettingWidget(rememberCurrent, t3);

            String t4 = messageProvider.translate("Settings Dialog", "Save user-defined items");
            CheckBoxSettingsWidget saveUDI = new CheckBoxSettingsWidget(e, this,
                    SettingNames.EXPLORER_TREE_GROUP,
                    SettingNames.ExplorerTree.COMMON_GROUP,
                    SettingNames.ExplorerTree.Common.KEEP_USER_EI,
                    t4);
            generalSettings.addSettingWidget(saveUDI, t4);
        }
    }

    private class SelectorSettingsWidget extends TabLayout {

        TabInners generalSettings;
        TabInners stylesSettings;
        TabInners alignmentSettings;

        public SelectorSettingsWidget() {
            super();
            drawSelectorSettingsTabs();
            this.setHeight(550);
        }

        private void drawSelectorSettingsTabs() {
            Tab tabgeneralSettings = addTab(0, messageProvider.translate("Settings Dialog", "General"));
            tabgeneralSettings.add(generalSettings = new TabInners());

            Tab tabstylesSettings = addTab(1, messageProvider.translate("Settings Dialog", "Styles"));
            tabstylesSettings.add(stylesSettings = new TabInners());

            Tab tabalignmentSettings = addTab(2, messageProvider.translate("Settings Dialog", "Alignment"));
            tabalignmentSettings.add(alignmentSettings = new TabInners());

            drawGeneral();
            drawStyles();
            drawAlignment();
        }

        private void drawAlignment() {
            alignmentSettings.addSettingWidget(new ColumnAlignmentSettings(e, this));
        }

        private void drawStyles() {
            previewWidget = new PreviewWidget(this);

            TableLayout layout = new TableLayout();

            stylesSettings.addRegularWidget(layout);

            VerticalBox box1 = new VerticalBox();
            final VerticalBox box2 = new VerticalBox();
            listBox.getHtml().setCss("border", "1px solid #BBB");
            listBox.getHtml().setCss("border-radius", "2px");

            box1.add(listBox);
            box1.addSpace(5);
            box1.add(previewWidget);

            TableLayout.Row row_1 = layout.addRow();
            row_1.getHtml().setCss("vertical-align", "top");
            TableLayout.Row.Cell cell_1 = row_1.addCell();
            cell_1.add(box1);
            row_1.addSpace(5);

            TableLayout.Row.Cell cell_2 = row_1.addCell();
            cell_2.add(box2);

            //init widgets per style and default values
            for (Map.Entry<ESelectorRowStyle, String> pair : stylesHashMap.entrySet()) {
                ListBox.ListBoxItem item = new ListBox.ListBoxItem();
                item.setText(pair.getValue());
                listBox.add(item);
                if (pair.getValue() != null) {
                    ESelectorRowStyle style = pair.getKey();
                    StylePageSettingsWidget stylePage = new StylePageSettingsWidget(e,
                            SelectorSettingsWidget.this,
                            SettingNames.SELECTOR_GROUP,
                            SettingNames.Selector.STYLES_GROUP,
                            pair.getKey().getValue());
                    stylePageWidgets.put(style, stylePage);

                    //settingsArrayList.add(stylePage);
                    settingsArrayList.addAll(stylePage.getReadonlyPropertySettingsWidget().getPropertyWidgets());
                    settingsArrayList.addAll(stylePage.getMandatoryPropertySettingsWidget().getPropertyWidgets());
                    settingsArrayList.addAll(stylePage.getOtherPropertySettingsWidget().getPropertyWidgets());
                    stylePage.writeSettings(previewSettings);
                }
            }

            //switch widgets by style
            listBox.addCurrentItemListener(new ListBox.CurrentItemListener() {
                @Override
                public void currentItemChanged(final ListBox.ListBoxItem currentItem) {
                    if (currentItem == null && listBox.count() > 0) {
                        listBox.setCurrentRow(0);
                    } else {
                        ESelectorRowStyle style;
                        String currentStyleName = currentItem.getText();
                        for (Map.Entry<ESelectorRowStyle, String> pair : stylesHashMap.entrySet()) {
                            if (pair.getValue() != null && pair.getValue().equals(currentStyleName)) {
                                style = pair.getKey();
                                StylePageSettingsWidget stylePage = stylePageWidgets.get(style);
                                if (stylePage != null) {
                                    stylePage.writeSettings(previewSettings);
                                }
                                box2.clear();
                                box2.add(stylePage);
                                updatePreviewWidget();
                            }
                        }
                    }
                }
            });
            listBox.setCurrentRow(0);
        }

        private void drawGeneral() {
            String rememeberFilterLabel = messageProvider.translate("Settings Dialog", "Remember current filter");
            CheckBoxSettingsWidget rememeberFilter = new CheckBoxSettingsWidget(e, this, SettingNames.SELECTOR_GROUP,
                    SettingNames.Selector.COMMON_GROUP,
                    SettingNames.Selector.Common.SAVE_FILTER,
                    rememeberFilterLabel);
            generalSettings.addSettingWidget(rememeberFilter, rememeberFilterLabel);

            String headersAlignLabel = messageProvider.translate("Settings Dialog", "Header alignment");
            LinkedList<Alignment> aligns = new LinkedList<>();

            aligns.add(Alignment.CENTER);
            aligns.add(Alignment.LEFT);
            aligns.add(Alignment.RIGHT);

            AlignmentSettingsWidget headersAlign = new AlignmentSettingsWidget(e,
                    this, SettingNames.SELECTOR_GROUP,
                    SettingNames.Selector.COMMON_GROUP,
                    SettingNames.Selector.Common.TITLES_ALIGNMENT, aligns);
            generalSettings.addSettingWidget(headersAlign, headersAlignLabel);

            String currentCellFrameLabel = messageProvider.translate("Settings Dialog", "Current cell frame color");
            ColorSettingsWidget currentCellFrame = new ColorSettingsWidget(e, this, SettingNames.SELECTOR_GROUP,
                    SettingNames.Selector.COMMON_GROUP,
                    SettingNames.Selector.Common.FRAME_COLOR);
            generalSettings.addSettingWidget(currentCellFrame, currentCellFrameLabel);
            
            String currentRowFrameLabel = messageProvider.translate("Settings Dialog", "Current row frame color");
            ColorSettingsWidget currentRowFrame = new ColorSettingsWidget(e, this, SettingNames.SELECTOR_GROUP,
                    SettingNames.Selector.COMMON_GROUP,
                    SettingNames.Selector.Common.ROW_FRAME_COLOR);
            generalSettings.addSettingWidget(currentRowFrame, currentRowFrameLabel);            
            
            String selectionBackgroundLabel = messageProvider.translate("Settings Dialog", "Selected object background color");
            ColorSettingsWidget selectionBackground = new ColorSettingsWidget(e, this, SettingNames.SELECTOR_GROUP,
                    SettingNames.Selector.COMMON_GROUP,
                    SettingNames.Selector.Common.SELECTED_ROW_COLOR);
            generalSettings.addSettingWidget(selectionBackground, selectionBackgroundLabel);
            
            String multipleSelectionByDefaultLabel = messageProvider.translate("Settings Dialog", "Enable multiple selection mode by default");
            CheckBoxSettingsWidget multipleSelectionByDefault = new CheckBoxSettingsWidget(e, this, SettingNames.SELECTOR_GROUP,
                    SettingNames.Selector.COMMON_GROUP,
                    SettingNames.Selector.Common.MULTIPLE_SELECTION_MODE_ENABLED_BY_DEFAULT,
                    multipleSelectionByDefaultLabel);
            generalSettings.addSettingWidget(multipleSelectionByDefault, multipleSelectionByDefaultLabel);

            String alternativeBackgroundLabel = messageProvider.translate("Settings Dialog", "Alternative Background color");
            AlternativeBackgroundWidget alternativeBackground = new AlternativeBackgroundWidget(e, this, SettingNames.SELECTOR_GROUP,
                    SettingNames.Selector.COMMON_GROUP,
                    SettingNames.Selector.Common.SLIDER_VALUE, alternativeBackgroundLabel);
            generalSettings.addSettingWidget(alternativeBackground);
        }
    }

    private class PropertyTabInners extends TabInners {

        GroupBox nameBox = new GroupBox();
        GroupBox valueBox = new GroupBox();
        GroupBox foregroundBox = new GroupBox();
        TableLayout nameLayout = new TableLayout();
        TableLayout valueLayout = new TableLayout();
        TableLayout foregroundLayout = new TableLayout();

        public PropertyTabInners() {
            super();
            //this.getHtml().setCss("overflow", "scroll");
            addRegularWidget(nameBox);
            nameBox.setTitle(messageProvider.translate("Settings Dialog", "Name"));
            nameBox.setTitleAlign(Alignment.LEFT);
            nameBox.setHSizePolicy(SizePolicy.MINIMUM_EXPAND);
            nameBox.add(nameLayout);
            nameLayout.getHtml().setAttr("cellspacing", "5");
            valueBox.setTitle(messageProvider.translate("Settings Dialog", "Value"));
            valueBox.setTitleAlign(Alignment.LEFT);
            valueBox.setHSizePolicy(SizePolicy.MINIMUM_EXPAND);
            valueLayout.getHtml().setAttr("cellspacing", "5");
            valueBox.add(valueLayout);
            addRegularWidget(valueBox);

            valueLayout.addRow().addCell().add(foregroundBox);

            foregroundBox.setTitle(messageProvider.translate("Settings Dialog", "Foreground Color"));
            foregroundBox.setTitleAlign(Alignment.CENTER);
            foregroundBox.setHSizePolicy(SizePolicy.MINIMUM_EXPAND);
            foregroundBox.add(foregroundLayout);
            foregroundLayout.getHtml().setAttr("cellspacing", "5");
        }

        protected void addToNameBox(SettingsWidget widget, String title) {
            TableLayout.Row row = nameLayout.addRow();
            if (title != null) {
                Label l = new Label(title);
                TableLayout.Row.Cell cell = row.addCell();
                cell.getHtml().addClass("rwt-table-cell-label");
                cell.add(l);
            }

            if (!settingsArrayList.contains(widget)) {
                settingsArrayList.add(widget);
            }
            row.addCell().add(widget);
            nameLayout.addVerticalSpace();
        }

        protected void addToValueBox(SettingsWidget widget, String title) {
            TableLayout.Row row = valueLayout.addRow();
            if (title != null) {
                Label l = new Label(title);
                TableLayout.Row.Cell cell = row.addCell();
                cell.getHtml().addClass("rwt-table-cell-label");
                cell.add(l);
            }

            if (!settingsArrayList.contains(widget)) {
                settingsArrayList.add(widget);
            }
            row.addCell().add(widget);
            valueLayout.addVerticalSpace();
        }

        protected void addToForegroundBox(SettingsWidget widget, String title) {
            TableLayout.Row row = foregroundLayout.addRow();
            if (title != null) {
                Label l = new Label(title);
                TableLayout.Row.Cell cell = row.addCell();
                cell.getHtml().addClass("rwt-table-cell-label");
                cell.add(l);
            }
            row.addCell().add(widget);
            if (!settingsArrayList.contains(widget)) {
                settingsArrayList.add(widget);
            }
            foregroundLayout.addVerticalSpace();
        }
    }

    private class EditorSettingsWidget extends TabLayout {

        TabInners generalSettings;
        PropertyTabInners readonlySettings;
        PropertyTabInners mandatorySettings;
        PropertyTabInners otherSettings;

        public EditorSettingsWidget() {
            super();
            this.setHeight(550);
            drawEditorSettingsTabs();
        }

        private void drawEditorSettingsTabs() {
            Tab tabgeneralSettings = addTab(0, messageProvider.translate("Settings Dialog", "General"));
            tabgeneralSettings.add(generalSettings = new TabInners());
            Tab tabpropertiesSettings = addTab(1, messageProvider.translate("Settings Dialog", "Properties"));
            TabLayout propertiesInnerTabs = new TabLayout();
            tabpropertiesSettings.getHtml().setAttr("TabPropertyInners", true);
            //tabpropertiesSettings.getHtml().getParent().setCss("height", "350px");
            drawEditorPropertiesSettingsTabs(propertiesInnerTabs);
            tabpropertiesSettings.add(propertiesInnerTabs);
            
            drawGeneral();
        }

        private void drawGeneral() {
            {
                final String title = messageProvider.translate("Settings Dialog", "Field name alignment");
                final LinkedList<Alignment> aligns = new LinkedList<>();
                aligns.add(Alignment.LEFT);
                aligns.add(Alignment.RIGHT);
                AlignmentSettingsWidget position = new AlignmentSettingsWidget(e, this,
                        SettingNames.EDITOR_GROUP, SettingNames.Editor.COMMON_GROUP,
                        SettingNames.Editor.Common.TITLES_ALIGNMENT, aligns);            
                generalSettings.addSettingWidget(position, title);
            }
            {
                final String title = messageProvider.translate("Settings Dialog", "Maximum number of items in drop-down list");
                final IntSettingsWidget settingsWidget = 
                    new IntSettingsWidget(e,                            
                                                       this, 
                                                       SettingNames.EDITOR_GROUP,
                                                       SettingNames.Editor.COMMON_GROUP,
                                                       SettingNames.Editor.Common.DROP_DOWN_LIST_ITEMS_LIMIT,
                                                       0,
                                                       Integer.MAX_VALUE);
                generalSettings.addSettingWidget(settingsWidget, title);
            }
            {
                final String title = messageProvider.translate("Settings Dialog", "Remember current tab");
                CheckBoxSettingsWidget rememberTab = new CheckBoxSettingsWidget(e, this,
                        SettingNames.EDITOR_GROUP,
                        SettingNames.Editor.COMMON_GROUP,
                        SettingNames.Editor.Common.RESTORE_TAB,
                        title);
                generalSettings.addSettingWidget(rememberTab, title);
            }
            {
                final String title = messageProvider.translate("Settings Dialog", "Warn about undefined value for mandatory property when closing editor");
                CheckBoxSettingsWidget swWarnAboutMandatory = new CheckBoxSettingsWidget(e, this,
                        SettingNames.EDITOR_GROUP,
                        SettingNames.Editor.COMMON_GROUP,
                        SettingNames.Editor.Common.CHECK_MANDATORY_ON_CLOSE,
                        title);
                generalSettings.addSettingWidget(swWarnAboutMandatory, title);
            }
        }

        private void drawEditorPropertiesSettingsTabs(TabLayout layout) {
            Tab tabreadonlyeSettings = layout.addTab(0, messageProvider.translate("Settings Dialog", "Readonly"));
            //tabreadonlyeSettings.getHtml().getParent().setCss("overflow", "scroll");
            tabreadonlyeSettings.add(readonlySettings = new PropertyTabInners());
            readonlySettings.getHtml().setAttr("readOnlyTab", true);

            Tab tabmandatorySettings = layout.addTab(1, messageProvider.translate("Settings Dialog", "Mandatory"));
            tabmandatorySettings.add(mandatorySettings = new PropertyTabInners());
            mandatorySettings.getHtml().setAttr("mandatoryTab", true);

            Tab tabotherSettings = layout.addTab(2, messageProvider.translate("Settings Dialog", "Other"));
            tabotherSettings.add(otherSettings = new PropertyTabInners());
            otherSettings.getHtml().setAttr("otherPropsTab", true);

            drawReadonly();
            drawMandatory();
            drawOther();
        }

        private void drawReadonly() {
            String t0 = messageProvider.translate("Settings Dialog", "Foreground color");
            ColorSettingsWidget foregroundColor = new ColorSettingsWidget(e, this,
                    SettingNames.EDITOR_GROUP,
                    SettingNames.Editor.PROPERTY_TITLES_GROUP,
                    SettingNames.Properties.READONLY_PROPERTY + "/" + SettingNames.TextOptions.FCOLOR);
            readonlySettings.addToNameBox(foregroundColor, t0);

            String t1 = messageProvider.translate("Settings Dialog", "Background color");
            ColorSettingsWidget backgroundColor = new ColorSettingsWidget(e, this,
                    SettingNames.EDITOR_GROUP,
                    SettingNames.Editor.PROPERTY_VALUES_GROUP,
                    SettingNames.Properties.READONLY_PROPERTY + "/" + SettingNames.TextOptions.BCOLOR);
            readonlySettings.addToValueBox(backgroundColor, t1);

            String t2 = messageProvider.translate("Settings Dialog", "Own");
            ColorSettingsWidget own = new ColorSettingsWidget(e, this,
                    SettingNames.EDITOR_GROUP, SettingNames.Editor.PROPERTY_VALUES_GROUP,
                    SettingNames.Properties.READONLY_PROPERTY + "/" + SettingNames.TextOptions.FCOLOR);
            readonlySettings.addToForegroundBox(own, t2);

            String t3 = messageProvider.translate("Settings Dialog", "Inherited");
            ColorSettingsWidget inhherited = new ColorSettingsWidget(e, this,
                    SettingNames.EDITOR_GROUP,
                    SettingNames.Editor.PROPERTY_VALUES_GROUP,
                    SettingNames.Properties.READONLY_PROPERTY + "/" + SettingNames.TextOptions.FCOLOR + "/" + SettingNames.Properties.INHERITED);
            readonlySettings.addToForegroundBox(inhherited, t3);

            String t4 = messageProvider.translate("Settings Dialog", "Overriden");
            ColorSettingsWidget overrriden = new ColorSettingsWidget(e, this,
                    SettingNames.EDITOR_GROUP,
                    SettingNames.Editor.PROPERTY_VALUES_GROUP,
                    SettingNames.Properties.READONLY_PROPERTY + "/" + SettingNames.TextOptions.FCOLOR + "/" + SettingNames.Properties.OVERRIDED);
            readonlySettings.addToForegroundBox(overrriden, t4);

            String t5 = messageProvider.translate("Settings Dialog", "Undefined");
            ColorSettingsWidget undeffined = new ColorSettingsWidget(e, this,
                    SettingNames.EDITOR_GROUP,
                    SettingNames.Editor.PROPERTY_VALUES_GROUP,
                    SettingNames.Properties.READONLY_PROPERTY + "/" + SettingNames.TextOptions.FCOLOR + "/" + SettingNames.Properties.UNDEFINED);
            readonlySettings.addToForegroundBox(undeffined, t5);
        }

        private void drawMandatory() {
            String t0 = messageProvider.translate("Settings Dialog", "Foreground color");
            ColorSettingsWidget foregroundColor = new ColorSettingsWidget(e, this,
                    SettingNames.EDITOR_GROUP,
                    SettingNames.Editor.PROPERTY_TITLES_GROUP,
                    SettingNames.Properties.MANDATORY_PROPERTY + "/" + SettingNames.TextOptions.FCOLOR);
            mandatorySettings.addToNameBox(foregroundColor, t0);

            String t1 = messageProvider.translate("Settings Dialog", "Background color");
            ColorSettingsWidget backgroundColor = new ColorSettingsWidget(e, this,
                    SettingNames.EDITOR_GROUP,
                    SettingNames.Editor.PROPERTY_VALUES_GROUP,
                    SettingNames.Properties.MANDATORY_PROPERTY + "/" + SettingNames.TextOptions.BCOLOR);
            mandatorySettings.addToValueBox(backgroundColor, t1);

            String t2 = messageProvider.translate("Settings Dialog", "Own");
            ColorSettingsWidget own = new ColorSettingsWidget(e, this,
                    SettingNames.EDITOR_GROUP,
                    SettingNames.Editor.PROPERTY_VALUES_GROUP,
                    SettingNames.Properties.MANDATORY_PROPERTY + "/" + SettingNames.TextOptions.FCOLOR);
            mandatorySettings.addToForegroundBox(own, t2);

            String t3 = messageProvider.translate("Settings Dialog", "Inherited");
            ColorSettingsWidget inhherited = new ColorSettingsWidget(e, this,
                    SettingNames.EDITOR_GROUP,
                    SettingNames.Editor.PROPERTY_VALUES_GROUP,
                    SettingNames.Properties.MANDATORY_PROPERTY + "/" + SettingNames.TextOptions.FCOLOR + "/" + SettingNames.Properties.INHERITED);
            mandatorySettings.addToForegroundBox(inhherited, t3);

            String t4 = messageProvider.translate("Settings Dialog", "Overriden");
            ColorSettingsWidget overrriden = new ColorSettingsWidget(e, this,
                    SettingNames.EDITOR_GROUP,
                    SettingNames.Editor.PROPERTY_VALUES_GROUP,
                    SettingNames.Properties.MANDATORY_PROPERTY + "/" + SettingNames.TextOptions.FCOLOR + "/" + SettingNames.Properties.OVERRIDED);
            mandatorySettings.addToForegroundBox(overrriden, t4);

            String t5 = messageProvider.translate("Settings Dialog", "Undefined");
            ColorSettingsWidget undeffined = new ColorSettingsWidget(e, this,
                    SettingNames.EDITOR_GROUP,
                    SettingNames.Editor.PROPERTY_VALUES_GROUP,
                    SettingNames.Properties.MANDATORY_PROPERTY + "/" + SettingNames.TextOptions.FCOLOR + "/" + SettingNames.Properties.UNDEFINED);
            mandatorySettings.addToForegroundBox(undeffined, t5);
        }

        private void drawOther() {
            String t0 = messageProvider.translate("Settings Dialog", "Foreground color");
            ColorSettingsWidget foregroundColor = new ColorSettingsWidget(e,
                    this, SettingNames.EDITOR_GROUP,
                    SettingNames.Editor.PROPERTY_TITLES_GROUP,
                    SettingNames.Properties.OTHER_PROPERTY + "/" + SettingNames.TextOptions.FCOLOR);
            otherSettings.addToNameBox(foregroundColor, t0);

            String t1 = messageProvider.translate("Settings Dialog", "Background color");
            ColorSettingsWidget backgroundColor = new ColorSettingsWidget(e, this,
                    SettingNames.EDITOR_GROUP,
                    SettingNames.Editor.PROPERTY_VALUES_GROUP,
                    SettingNames.Properties.OTHER_PROPERTY + "/" + SettingNames.TextOptions.BCOLOR);
            otherSettings.addToValueBox(backgroundColor, t1);

            String t2 = messageProvider.translate("Settings Dialog", "Own");
            ColorSettingsWidget own = new ColorSettingsWidget(e, this,
                    SettingNames.EDITOR_GROUP,
                    SettingNames.Editor.PROPERTY_VALUES_GROUP,
                    SettingNames.Properties.OTHER_PROPERTY + "/" + SettingNames.TextOptions.FCOLOR);
            otherSettings.addToForegroundBox(own, t2);

            String t3 = messageProvider.translate("Settings Dialog", "Inherited");
            ColorSettingsWidget inhherited = new ColorSettingsWidget(e, this, SettingNames.EDITOR_GROUP,
                    SettingNames.Editor.PROPERTY_VALUES_GROUP,
                    SettingNames.Properties.OTHER_PROPERTY + "/" + SettingNames.TextOptions.FCOLOR + "/" + SettingNames.Properties.INHERITED);
            otherSettings.addToForegroundBox(inhherited, t3);

            String t4 = messageProvider.translate("Settings Dialog", "Overriden");
            ColorSettingsWidget overrriden = new ColorSettingsWidget(e, this, SettingNames.EDITOR_GROUP,
                    SettingNames.Editor.PROPERTY_VALUES_GROUP,
                    SettingNames.Properties.OTHER_PROPERTY + "/" + SettingNames.TextOptions.FCOLOR + "/" + SettingNames.Properties.OVERRIDED);
            otherSettings.addToForegroundBox(overrriden, t4);

            String t5 = messageProvider.translate("Settings Dialog", "Undefined");
            ColorSettingsWidget undeffined = new ColorSettingsWidget(e, this, SettingNames.EDITOR_GROUP,
                    SettingNames.Editor.PROPERTY_VALUES_GROUP,
                    SettingNames.Properties.OTHER_PROPERTY + "/" + SettingNames.TextOptions.FCOLOR + "/" + SettingNames.Properties.UNDEFINED);
            otherSettings.addToForegroundBox(undeffined, t5);
        }
    }
    
    /* private class SourceCodeSettingsWidget extends TabLayout {

     public SourceCodeSettingsWidget() {
     super();
     drawSourceCodeSettingsTabs();
     }

     private void drawSourceCodeSettingsTabs() {
     Tab generalSettings = addTab(0, messageProvider.translate("AppearanceSettingsDialog", "Source Code Options for Jml"));
     Tab paragraphSettings = addTab(1, messageProvider.translate("AppereanceSettingsDialog", "Source Code Options for Sqml"));
     }
     }*/
    private void updateTabs(ListBox.ListBoxItem item) {//replace old tab layout
        if (item != null) {
            if (item.equals(explorerTreeItem)) {
                showExplorerTreeSettings();
            } else if (item.equals(editorItem)) {
                showEditorSettings();
            } else if (item.equals(selectorItem)) {
                showSelectorSettings();
            } else if (item.equals(formatItem)) {
                showFormatSettings();
            }
            /*else if (item.equals(sourceCodeItem)) {
             showSourceCodeSettings();
             }*/
        }
    }

    public void updatePreviewWidget() {
        ESelectorRowStyle style = null;
        if (listBox.getCurrent() == null) {
            style = ESelectorRowStyle.NORMAL;
        } else {
            for (Map.Entry<ESelectorRowStyle, String> pair : stylesHashMap.entrySet()) {
                if (pair.getValue().equals(listBox.getCurrent().getText())) {
                    style = pair.getKey();
                }
            }
        }
        StylePageSettingsWidget stylePage = stylePageWidgets.get(style);
        if (stylePage != null) {
            final WpsTextOptions readonlyDefTextOptions
                    = stylePage.getReadonlyPropertySettingsWidget().getTextOptions(true);
            final WpsTextOptions readonlyUndefTextOptions
                    = stylePage.getReadonlyPropertySettingsWidget().getTextOptions(false);
            final WpsTextOptions mandatoryTextOptions
                    = stylePage.getMandatoryPropertySettingsWidget().getTextOptions();
            final WpsTextOptions otherDefTextOptions
                    = stylePage.getOtherPropertySettingsWidget().getTextOptions(true);
            final WpsTextOptions otherUndefTextOptions
                    = stylePage.getOtherPropertySettingsWidget().getTextOptions(false);
            previewWidget.update(readonlyDefTextOptions, mandatoryTextOptions, otherDefTextOptions, true);
            previewWidget.update(readonlyUndefTextOptions, mandatoryTextOptions, otherUndefTextOptions, false);
        }
    }

    private void showExplorerTreeSettings() {
        enableTabs(explorerTreeTabs);
    }

    private void showSelectorSettings() {
        enableTabs(selectorTabs);
    }

    private void showEditorSettings() {
        enableTabs(editorTabs);
    }
    
    private void showFormatSettings() {
        enableTabs(formatTabs);
    }

    /*    private void showSourceCodeSettings() {
     enableTabs(srcCodeTabs);
     }
     */
    private void enableTabs(UIObject tabb) {
        if (tabb != null && tabsList != null && tabsList.contains(tabb)) {
            tabb.setVisible(true);
            tabb.setEnabled(true);
            tabSetCell.add(tabb);
            for (UIObject t : tabsList) {
                if (!t.equals(tabb)) {
                    t.setVisible(false);
                    t.setEnabled(false);
                }
            }
        }
    }

    private LinkedList<ListBox.ListBoxItem> createItemsSettingsList() {
        LinkedList<ListBox.ListBoxItem> list = new LinkedList<>();

        explorerTreeItem.setIcon(env.getApplication().getImageManager().getIcon(ClientIcon.Definitions.TREE));
        explorerTreeItem.setText(messageProvider.translate("Settings Dialog", "Explorer Tree"));
        list.add(explorerTreeItem);

        editorItem.setIcon(getApplication().getImageManager().getIcon(ClientIcon.Definitions.EDITOR));
        editorItem.setText(messageProvider.translate("Settings Dialog", "Editor"));
        list.add(editorItem);

        selectorItem.setIcon(getApplication().getImageManager().getIcon(ClientIcon.Definitions.SELECTOR));
        selectorItem.setText(messageProvider.translate("Settings Dialog", "Selector"));
        list.add(selectorItem);

        formatItem.setIcon(env.getApplication().getImageManager().getIcon(Icons.FORMAT_SETTINGS));
        formatItem.setText(messageProvider.translate("Settings Dialog", "Number and Date/ Time Formats"));
        list.add(formatItem);
        
        /*        sourceCodeItem.setIcon(getApplication().getImageManager().getIcon(Icons.SRC_EDITOR_SETTINGS));
         sourceCodeItem.setText(messageProvider.translate("AppereanceSettingsDialog", "Source Code"));
         list.add(sourceCodeItem);
         */
        return list;
    }
}
