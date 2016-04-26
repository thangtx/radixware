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
package org.radixware.kernel.explorer.editors.xml.new_;

import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QFileDialog;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QListWidget;
import com.trolltech.qt.gui.QListWidgetItem;
import com.trolltech.qt.gui.QStackedWidget;
import com.trolltech.qt.gui.QWidget;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.editors.xmleditor.model.IXmlValueEditingOptionsProvider;
import org.radixware.kernel.common.client.editors.xmleditor.model.XmlDocument;
import org.radixware.kernel.common.client.editors.xmleditor.model.XmlModelItem;
import org.radixware.kernel.common.client.editors.xmleditor.model.schema.XmlSchemaAttributeItem;
import org.radixware.kernel.common.client.editors.xmleditor.model.schema.XmlSchemaElementItem;
import org.radixware.kernel.common.client.editors.xmleditor.model.schema.XmlSchemaItem;
import org.radixware.kernel.common.client.editors.xmleditor.view.IChoiceScheme;
import org.radixware.kernel.common.client.editors.xmleditor.view.ICreateAttributeDialog;
import org.radixware.kernel.common.client.editors.xmleditor.view.ICreateElementDialog;
import org.radixware.kernel.common.client.editors.xmleditor.view.IRenameDialog;
import org.radixware.kernel.common.client.editors.xmleditor.view.IXmlEditorPresenter;
import org.radixware.kernel.common.client.editors.xmleditor.view.XmlEditorController;
import org.radixware.kernel.explorer.editors.xml.new_.view.XmlTree;
import org.radixware.kernel.common.client.widgets.actions.Action;
import org.radixware.kernel.common.utils.XmlUtils;
import org.radixware.kernel.explorer.editors.xml.new_.view.ChoiceSchemeDlg;
import org.radixware.kernel.explorer.editors.xml.new_.view.XmlAttributeParamsDlg;
import org.radixware.kernel.explorer.editors.xml.new_.view.XmlChildsParamsDlg;
import org.radixware.kernel.explorer.editors.xml.new_.view.XmlRenameItem;
import org.radixware.kernel.explorer.editors.xml.new_.view.XmlTextEdit;
import org.radixware.kernel.explorer.editors.xml.new_.view.XmlValueEditorFactory;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.utils.WidgetUtils;
import org.radixware.kernel.explorer.views.Splitter;
import org.radixware.kernel.explorer.widgets.ExplorerAction;
import org.radixware.kernel.explorer.widgets.ExplorerToolBar;

final class XmlEditorPresenter implements IXmlEditorPresenter {

    private final static String CONFIG_PREFIX = SettingNames.SYSTEM + "/XmlEditor";
    private final IClientEnvironment environment;
    private final ExplorerToolBar toolBar;
    private final XmlTree xmlTree;
    private final XmlTextEdit xmlText;
    private final XmlTextEdit prettyXmlText;
    private final XmlTextEdit schemeTextArea;
    private final QStackedWidget mainContainer;
    private final XmlEditor editor;
    private final XmlValueEditorFactory<? extends QWidget> valueEditorFactory;
    private String fileName;
    private final QListWidget errorsList;
    private XmlEditorController.EXmlEditorMode currentMode;
    final Splitter splitter;
    private final XmlClipboard clipboard;

    private Action copyAction;
    private Action cutAction;
    private Action pasteAction;
    private final Action.ActionListener copyListener = new Action.ActionListener() {
        @Override
        public void triggered(Action action) {
            xmlText.copy();
            prettyXmlText.copy();
        }
    };
    private final Action.ActionListener cutListener = new Action.ActionListener() {
        @Override
        public void triggered(Action action) {
            xmlText.cut();
        }
    };
    private final Action.ActionListener pasteListener = new Action.ActionListener() {
        @Override
        public void triggered(Action action) {
            xmlText.paste();
        }
    };

    public XmlEditorPresenter(final IClientEnvironment environment, final XmlValueEditorFactory<? extends QWidget> valueEditorFactory, final XmlEditor editor, final QListWidget errorsList) throws InstantiationException, IllegalAccessException {

        this.environment = environment;
        this.editor = editor;
        this.valueEditorFactory = valueEditorFactory == null ? XmlValueEditorFactory.getDefaultInstance() : valueEditorFactory;
        toolBar = new ExplorerToolBar(editor);
        mainContainer = new QStackedWidget();

        xmlTree = new XmlTree(editor, environment, this.valueEditorFactory);
        xmlTree.setColumnCount(0);
        currentMode = XmlEditorController.EXmlEditorMode.XmlTreeMode; 
        mainContainer.addWidget(xmlTree);

        xmlText = new XmlTextEdit(editor);
        mainContainer.addWidget(xmlText);

        prettyXmlText = new XmlTextEdit(editor);
        prettyXmlText.setXmlTextReadOnly(true);
        mainContainer.addWidget(prettyXmlText);

        schemeTextArea = new XmlTextEdit(editor);
        schemeTextArea.setXmlTextReadOnly(true);
        mainContainer.addWidget(schemeTextArea);

        final QWidget centralWidget = new QWidget();
        WidgetUtils.createVBoxLayout(centralWidget);
        splitter = new Splitter(centralWidget, environment.getConfigStore());
        centralWidget.layout().addWidget(splitter);
        editor.setCentralWidget(centralWidget);
        splitter.setOrientation(Qt.Orientation.Vertical);
        splitter.addWidget(mainContainer);
        splitter.addWidget(errorsList);
        this.errorsList = errorsList;
        clipboard = XmlClipboard.class.newInstance();
    }

    @Override
    public Action createOpenAction(final String title, final ClientIcon icon) {
        final ExplorerAction action = new ExplorerAction(ExplorerIcon.getQIcon(icon), title, null);
        action.addActionListener(new Action.ActionListener() {
            @Override
            public void triggered(Action action) {
                openXmlDocument();
            }
        });
        return action;
    }

    @Override
    public ExplorerToolBar getToolBar() {
        return toolBar;
    }

    @Override
    public XmlTree getXmlTree() {
        return xmlTree;
    }

    @Override
    public XmlDocument getXmlText() {
        try {
            final XmlDocument document = createXmlDoc(xmlText.toPlainText());
            if (document != null) {
                editor.getController().openXmlDocument(document);
                return editor.getController().getDocument();
            }
        } catch (XmlException ex) {
            Logger.getLogger(XmlEditorPresenter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private XmlDocument createXmlDoc(final String textDoc) {
        try {
            if (textDoc.isEmpty() || textDoc.trim().isEmpty()) {
                environment.messageWarning(environment.getMessageProvider().translate("XmlEditor", "Empty Document"), environment.getMessageProvider().translate("XmlEditor", "The document is empty."));
                errorsList.setVisible(false);
                return null;
            }
            XmlObject xmlDoc = XmlObject.Factory.parse(textDoc);
            return new XmlDocument(xmlDoc);
        } catch (XmlException exception) {
            final List<XmlError> valList = new LinkedList<>();
            valList.add(exception.getError());
            showErrors(valList, false);
        }
        return null;
    }

    private XmlDocument openXmlDocument() {
        fileName = QFileDialog.getOpenFileName(null, environment.getMessageProvider().translate("XmlEditor", "Open"), QFileDialog.FileMode.AnyFile.toString(), new QFileDialog.Filter("*.xml"));
        if (!fileName.isEmpty()) {
            final XmlObject xmlDoc;
            try {
                xmlDoc = XmlObject.Factory.parse(new File(fileName));
                final XmlDocument document = new XmlDocument(xmlDoc);
                if (mainContainer.currentIndex() == 0) {
                    xmlText.clear();
                    prettyXmlText.clear();
                } else if (mainContainer.currentIndex() == 1) {
                    prettyXmlText.clear();
                    xmlText.setText(document.getXmlText());
                } else {
                    xmlText.clear();
                    prettyXmlText.setText(document.getXmlText(XmlUtils.getPrettyXmlOptions()));
                }
                editor.getController().openXmlDocument(document);
                errorsList.setVisible(false);
            } catch (XmlException | IOException exception) {
                String title = environment.getMessageProvider().translate("XmlEditor", "Wrong format");
                String message = environment.getMessageProvider().translate("XmlEditor", "File '%s' has wrong format");
                environment.messageError(title, String.format(message, fileName));
                return null;
            }
        }
        return null;
    }

    @Override
    public void saveXmlDocument(final XmlDocument document, boolean sameFile, final boolean isTreeMode, final boolean isPrettyTextMode) {
        try {
            if (isTreeMode) {
                if (sameFile) {
                    save(document);
                } else {
                    saveas(document);
                }
            } else {
                final XmlDocument newDoc = createXmlDoc(isPrettyTextMode ? prettyXmlText.toPlainText() : xmlText.toPlainText());
                if (newDoc == null) {
                    String title = environment.getMessageProvider().translate("XmlEditor", "Wrong format");
                    String message = environment.getMessageProvider().translate("XmlEditor", "Document has wrong format");
                    environment.messageError(title, message);
                    return;
                } else if (sameFile) {
                    save(newDoc);
                } else {
                    saveas(newDoc);
                }
            }
        } catch (IOException exception) {
            environment.processException(exception);
        }
    }

    private void saveas(final XmlDocument document) throws IOException {
        String filePath = QFileDialog.getSaveFileName(null, environment.getMessageProvider().translate("XmlEditor", "Save as..."), QFileDialog.FileMode.AnyFile.toString(), new QFileDialog.Filter("*.xml"));
        if (!filePath.isEmpty()) {
            tranformToFile(filePath, document);
        }
    }

    private void save(final XmlDocument document) throws IOException {
        tranformToFile(fileName, document);
    }

    private void tranformToFile(final String file, final XmlDocument document) throws IOException {
        try (FileWriter writer = new FileWriter(file)) {
            writer.append(document.getXmlText());
            writer.flush();
        }
    }

    @Override
    public ICreateAttributeDialog newCreateAttributeDialog(final XmlModelItem parentItem, final IXmlValueEditingOptionsProvider editingOptionsProvider, final List<XmlSchemaAttributeItem> allowedItems, final List<QName> restrictedNames) {
        return new XmlAttributeParamsDlg(environment, xmlTree, valueEditorFactory, editingOptionsProvider, parentItem, allowedItems, restrictedNames);
    }

    @Override
    public ICreateElementDialog newCreateElementDialog(final XmlModelItem parentItem, final IXmlValueEditingOptionsProvider editingOptionsProvider, final List<XmlSchemaElementItem> allowedItems) {
        return new XmlChildsParamsDlg(environment, xmlTree, valueEditorFactory, editingOptionsProvider, parentItem, allowedItems);
    }

    @Override
    public IRenameDialog getNewReName(final List<QName> allowedNames, final List<QName> restrictedNames) {
        return new XmlRenameItem(environment, xmlTree, allowedNames, restrictedNames);
    }

    public IClientEnvironment getEnvironment() {
        return environment;
    }

    @Override
    public IChoiceScheme newCreateChoiceDialog(final XmlSchemaItem choiceItem, final int currentItemIndex) {
        return new ChoiceSchemeDlg(environment, xmlTree, choiceItem, currentItemIndex);
    }

    @Override
    public IChoiceScheme newCreateRootChoiceDialog(List<XmlSchemaItem> choiceItems) {
        return new ChoiceSchemeDlg(environment, xmlTree, choiceItems);
    }

    @Override
    public void showErrors(final List<XmlError> errors, final boolean isVisible) {
        if (errors != null && !errors.isEmpty()) {
            errorsList.clear();
            if (!errorsList.isVisible()) {
                errorsList.setVisible(true);
                restoreSplitterPosition();
            }
            for (XmlError error : errors) {
                if (editor.getController().getSchemaTypeSystem() == null && error.getMessage().equals("Invalid type")) {
                    if (errorsList.isVisible()) {
                        saveSplitterPosition();
                        errorsList.setVisible(false);
                    }
                } else {
                    QListWidgetItem item = new QListWidgetItem(error.getMessage());
                    item.setIcon((QIcon) environment.getApplication().getImageManager().getIcon(ClientIcon.TraceLevel.ERROR));
                    errorsList.addItem(item);
                }
            }
        } else {
            if (isVisible) {
                environment.messageInformation(environment.getMessageProvider().translate("XmlEditor", "Validation"), environment.getMessageProvider().translate("XmlEditor", "The document doesn't have of errors"));
            }
            if (errorsList.isVisible()) {
                saveSplitterPosition();
                errorsList.setVisible(false);
            }
        }
    }

    @Override
    public void close() {
        if (errorsList.isVisible()) {
            saveSplitterPosition();
        }
    }

    @Override
    public boolean switchToTreeMode() {
        if (!isCurrentMode(XmlEditorController.EXmlEditorMode.XmlSchemaMode)) {
            final String newDoc = isCurrentMode(XmlEditorController.EXmlEditorMode.XmlPrettyTextMode) ? prettyXmlText.toPlainText() : xmlText.toPlainText();
            final XmlDocument xmlDoc = editor.getController().getDocument();
            if (!xmlDoc.getXmlText().equals(newDoc.trim()) && !isCurrentMode(XmlEditorController.EXmlEditorMode.XmlPrettyTextMode)) {
                if (!canChangeDocument(newDoc.trim().isEmpty() ? xmlDoc.getXmlText() : newDoc.trim())) {
                    return false;
                }
            }
            if (!editor.isReadOnly() && !isCurrentMode(XmlEditorController.EXmlEditorMode.XmlPrettyTextMode)) {
                final List<XmlError> validateList = editor.validate(false);
                if (editor.getController().getSchemaTypeSystem() != null
                        && validateList != null
                        && !validateList.isEmpty()
                        && !environment.messageConfirmation(environment.getMessageProvider().translate("XmlEditor", "Confirm Changes"), environment.getMessageProvider().translate("XmlEditor", "The document contains errors. Do you really want to continue?"))) {
                    return false;
                }
            }
        }
        mainContainer.setCurrentIndex(0);
        setReadOnly(editor.isReadOnly());
        linkClipboardActions(null);
        return true;
    }

    @Override
    public boolean switchToTextMode(final XmlDocument document, final boolean isReadOnly) {
        mainContainer.setCurrentIndex(1);
        xmlText.setXmlText(document.getXmlText());
        xmlText.setXmlTextReadOnly(isReadOnly);
        setReadOnly(isReadOnly);
        linkClipboardActions(xmlText);
        return true;
    }

    @Override
    public boolean switchToPrettyTextMode(final XmlDocument document, final boolean isTreeCheked) {
        if (isTreeCheked || isCurrentMode(XmlEditorController.EXmlEditorMode.XmlPrettyTextMode)) {
            prettyXmlText.setXmlText(document.getXmlText(XmlUtils.getPrettyXmlOptions()), true);
        } else {
            final String newDoc = xmlText.toPlainText().isEmpty() ? document.getXmlText() : xmlText.toPlainText();
            if (!canChangeDocument(newDoc)) {
                return false;
            }
            prettyXmlText.setXmlText(createXmlDoc(newDoc).getXmlText(XmlUtils.getPrettyXmlOptions()), true);
        }
        mainContainer.setCurrentIndex(2);
        if (splitter.isVisible()) {
            restoreSplitterPosition();
            errorsList.setVisible(false);
        }
        linkClipboardActions(prettyXmlText);
        return true;
    }

    @Override
    public boolean switchToSchemeTextMode(final String schemeText) {
        if (isCurrentMode(XmlEditorController.EXmlEditorMode.XmlTextMode) && !xmlText.toPlainText().isEmpty()) {
            if (!canChangeDocument(xmlText.toPlainText())) {
                return false;
            }
        }
        if (splitter.isVisible()) {
            restoreSplitterPosition();
            errorsList.setVisible(false);
        }
        mainContainer.setCurrentIndex(3);
        schemeTextArea.setXmlText(schemeText, true);
        linkClipboardActions(schemeTextArea);
        return true;
    }

    private boolean canChangeDocument(final String text) {
        final XmlDocument doc = createXmlDoc(text);
        if (doc == null) {
            return false;
        }
        try {
            editor.getController().changeXmlDocument(doc);
        } catch (XmlException ex) {
            if (isCurrentMode(XmlEditorController.EXmlEditorMode.XmlTextMode)) {
                final List<XmlError> valList = new LinkedList<>();
                valList.add(ex.getError());
                showErrors(valList, false);
                return false;
            }
            Logger.getLogger(XmlEditorPresenter.class.getName()).log(Level.SEVERE, null, ex);
        }

        return true;
    }

    private void saveSplitterPosition() {
        splitter.saveRatioToConfig(CONFIG_PREFIX + "/splitterPart");
    }

    private void restoreSplitterPosition() {
        splitter.restoreRatioFromConfig(CONFIG_PREFIX + "/splitterPart", 0.7);
    }

    @Override
    public void setReadOnly(boolean isReadOnly) {
        if (getXmlTree() != null
                && getXmlTree().getTreeController() != null) {
            showErrors(null, false);
            getXmlTree().getTreeController().refreshAllItems();
            editor.setReadOnly(isReadOnly);
            if (xmlText != null && !xmlText.toPlainText().trim().isEmpty() && !isCurrentMode(XmlEditorController.EXmlEditorMode.XmlPrettyTextMode)) {
                xmlText.setXmlTextReadOnly(isReadOnly);
            }
        }
    }

    @Override
    public boolean isCurrentMode(XmlEditorController.EXmlEditorMode posMode) {
        return currentMode == posMode;
    }

    @Override
    public void setCurrentMode(XmlEditorController.EXmlEditorMode currentMode) {
        this.currentMode = currentMode;
    }

    @Override
    public XmlClipboard getClipboard() {
        return clipboard;
    }

    @Override
    public void setupClipboardActions(final Action copyAction, final Action cutAction, final Action pasteAction) {
        this.copyAction = copyAction;
        this.cutAction = cutAction;
        this.pasteAction = pasteAction;
    }

    private void linkClipboardActions(final XmlTextEdit textArea) {
        if (textArea == null) {
            copyAction.removeActionListener(copyListener);
            cutAction.removeActionListener(cutListener);
            pasteAction.removeActionListener(pasteListener);
        } else {
            copyAction.addActionListener(copyListener);
            cutAction.addActionListener(cutListener);
            pasteAction.addActionListener(pasteListener);
        }
    }

}
