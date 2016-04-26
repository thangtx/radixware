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
package org.radixware.wps.views.editor.xml.view;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.client.enums.ETextOptionsMarker;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.text.ITextOptions;
import org.radixware.kernel.common.client.editors.xmleditor.IXmlSchemaProvider;
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
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.kernel.common.client.widgets.IToolButton;
import org.radixware.kernel.common.client.widgets.actions.Action;
import org.radixware.kernel.common.html.Html;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.common.utils.XmlUtils;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.rwt.Container;
import org.radixware.wps.rwt.ListBox;
import org.radixware.wps.rwt.ListBox.ListBoxItem;
import org.radixware.wps.rwt.Splitter;
import org.radixware.wps.rwt.TextArea;
import org.radixware.wps.rwt.ToolBar;
import org.radixware.wps.rwt.UIObject;
import org.radixware.wps.rwt.uploading.IUploadedDataReader;
import org.radixware.wps.rwt.uploading.LoadFileAction;
import org.radixware.wps.text.WpsTextOptions;

final class XmlEditorPresenter implements IXmlEditorPresenter {

    private static class XmlDocumentReader implements IUploadedDataReader {

        private volatile XmlObject doc;
        private volatile Exception exception;
        private volatile String fileName;

        @Override
        public void readData(InputStream stream, String fileName, long fileSize) {
            try {
                exception = null;
                this.fileName = fileName;
                doc = XmlObject.Factory.parse(stream);
            } catch (IOException | XmlException ex) {
                exception = ex;
            }
        }

        public XmlObject getDocument() throws Exception {
            if (exception != null) {
                throw exception;
            }
            return doc;
        }

        public String getFileName() {
            return fileName;
        }
    }

    private final XmlTextArea.ISelectedTextListener selectedTextListener = new XmlTextArea.ISelectedTextListener() {
        @Override
        public void selectedTextReceived(final String selectedText, final String initiatorId) {
            XmlEditorPresenter.this.clipboard.putText(selectedText);
        }
    };

    private final XmlTextArea.ICaretListener caretListener = new XmlTextArea.ICaretListener() {

        @Override
        public void caretReceived(final XmlTextArea textArea, XmlTextArea.Caret caret, String initiatorId) {
            final String cutButtonId = ((UIObject) cutButton).getHtmlId();
            final String pasteButtonId = ((UIObject) pasteButton).getHtmlId();
            if (cutButtonId.equals(initiatorId)) {
                final String selectedText = textArea.getFragment(caret);
                if (selectedText != null && !selectedText.isEmpty()) {
                    clipboard.putText(selectedText);
                    textArea.deleteFragment(caret);
                }
            }
            if (pasteButtonId.equals(initiatorId)) {
                final String insertingText = clipboard.getText();
                if (insertingText != null && !insertingText.isEmpty()) {
                    textArea.replaceFragment(caret, insertingText);
                }
            }
        }
    };

    private final static String CONFIG_PREFIX = SettingNames.SYSTEM + "/XmlEditor";
    private final WpsEnvironment environment;
    private final ToolBar toolBar;
    private final XmlTree xmlTree;
    private final XmlTextArea xmlText = new XmlTextArea();
    private final XmlTextArea prettyXmlText = new XmlTextArea();
    private final XmlTextArea schemeTextArea = new XmlTextArea();
    private final XmlEditor editor;
    private final XmlValueEditorFactory<? extends UIObject> valueEditorFactory;
    public final LoadFileAction openAction;
    private final ListBox errorsList;
    private XmlEditorController.EXmlEditorMode currentMode;
    final Splitter splitter = new Splitter();
    private XmlClipboard clipboard;

    private IToolButton copyButton;
    private Action copyAction;
    private IToolButton cutButton;
    private Action cutAction;
    private IToolButton pasteButton;
    private Action pasteAction;

    public XmlEditorPresenter(final WpsEnvironment environment, final XmlValueEditorFactory<? extends UIObject> valueEditorFactory, final IXmlSchemaProvider schemaProvider, final XmlEditor editor, final ListBox errorsList) {

        this.environment = environment;
        this.editor = editor;
        this.valueEditorFactory = valueEditorFactory == null ? XmlValueEditorFactory.getDefaultInstance() : valueEditorFactory;
        toolBar = new ToolBar();
        final Container mainContainer = new Container();

        xmlTree = new XmlTree(this.valueEditorFactory);
        xmlTree.setSizePolicy(UIObject.SizePolicy.EXPAND, UIObject.SizePolicy.EXPAND);
        currentMode = XmlEditorController.EXmlEditorMode.XmlTreeMode;
        mainContainer.add(xmlTree);

        xmlText.setSizePolicy(UIObject.SizePolicy.EXPAND, UIObject.SizePolicy.EXPAND);
        xmlText.setVisible(false);
        mainContainer.add(xmlText);
        xmlText.addSelectedTextListener(selectedTextListener);
        xmlText.addCaretListener(caretListener);

        prettyXmlText.setSizePolicy(UIObject.SizePolicy.EXPAND, UIObject.SizePolicy.EXPAND);
        prettyXmlText.setVisible(false);
        prettyXmlText.setReadOnly(true);
        prettyXmlText.setTextOptions(environment.getTextOptionsProvider().getOptions(ETextOptionsMarker.READONLY_VALUE));
        mainContainer.add(prettyXmlText);
        prettyXmlText.addSelectedTextListener(selectedTextListener);

        schemeTextArea.setSizePolicy(UIObject.SizePolicy.EXPAND, UIObject.SizePolicy.EXPAND);
        schemeTextArea.setVisible(false);
        schemeTextArea.setReadOnly(true);
        schemeTextArea.setTextOptions(environment.getTextOptionsProvider().getOptions(ETextOptionsMarker.READONLY_VALUE));
        mainContainer.add(schemeTextArea);
        schemeTextArea.addSelectedTextListener(selectedTextListener);

        splitter.setOrientation(Splitter.Orientation.VERTICAL);
        splitter.add(mainContainer);
        splitter.add(errorsList);
        splitter.remove(editor);
        editor.add(toolBar);
        editor.add(splitter);
        editor.setAutoSize(splitter, true);
        openAction = new LoadFileAction(environment, new XmlDocumentReader());
        errorsList.setVisible(false);
        this.errorsList = errorsList;
        try {
            clipboard = XmlClipboard.class.newInstance();
        } catch (IllegalAccessException | InstantiationException ex) {
            clipboard = null;
        }
    }

    @Override
    public LoadFileAction createOpenAction(String title, ClientIcon icon) {
        openAction.setToolTip(title);
        return openAction;
    }

    @Override
    public ToolBar getToolBar() {
        return toolBar;
    }

    @Override
    public XmlTree getXmlTree() {
        return xmlTree;
    }

    @Override
    public XmlDocument getXmlText() {
        try {
            final String newDoc = xmlText.getText();
            if (createXmlDoc(newDoc) != null) {
                editor.getController().openXmlDocument(createXmlDoc(newDoc));
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

    XmlDocument getXmlDocument(final IUploadedDataReader reader) {
        try {
            final XmlDocument document
                    = new XmlDocument(((XmlDocumentReader) reader).getDocument());
            if (xmlTree.isVisible()) {
                xmlText.setText("");
                prettyXmlText.setText("");
            } else if (xmlText.isVisible()) {
                prettyXmlText.setText("");
                xmlText.setText(document.getXmlText());
            } else {
                xmlText.setText("");
                prettyXmlText.setText(document.getXmlText(XmlUtils.getPrettyXmlOptions()));
            }
            errorsList.setVisible(false);
            return document;
        } catch (Exception exception) {
            String title = environment.getMessageProvider().translate("XmlEditor", "Wrong format");
            String message = environment.getMessageProvider().translate("XmlEditor", "File '%s' has wrong format");
            environment.messageError(title, String.format(message, ((XmlDocumentReader) reader).getFileName()));
            return null;
        }
    }

    @Override
    public void saveXmlDocument(final XmlDocument document, boolean sameFile, final boolean isTreeMode, final boolean isPrettyTextMode) {
        final File tmpFile;
        try {
            tmpFile = File.createTempFile("xml-editor", "xml");
            final String newText;
            if (isTreeMode) {
                newText = document.getXmlText();
            } else if (isPrettyTextMode) {
                newText = prettyXmlText.getText();
            } else {
                newText = xmlText.getText();
            }
            if (createXmlDoc(newText) == null) {
                String title = environment.getMessageProvider().translate("XmlEditor", "Wrong format");
                String message = environment.getMessageProvider().translate("XmlEditor", "Document has wrong format");
                environment.messageError(title, message);
                return;
            }
            FileUtils.writeString(tmpFile, newText, "UTF-8");
        } catch (IOException exception) {
            environment.processException(exception);
            return;
        }
        environment.sendFileToTerminal("", tmpFile, "xml", false);
    }

    @Override
    public ICreateAttributeDialog newCreateAttributeDialog(final XmlModelItem parentItem, final IXmlValueEditingOptionsProvider editingOptionsProvider, final List<XmlSchemaAttributeItem> allowedItems, final List<QName> restrictedNames) {
        return new CreateAttrDialog(environment, valueEditorFactory, editingOptionsProvider, parentItem, allowedItems, restrictedNames);
    }

    @Override
    public ICreateElementDialog newCreateElementDialog(final XmlModelItem parentItem, final IXmlValueEditingOptionsProvider editingOptionsProvider, final List<XmlSchemaElementItem> allowedItems) {
        return new CreateElemDialog(environment, valueEditorFactory, editingOptionsProvider, parentItem, allowedItems);
    }

    @Override
    public IRenameDialog getNewReName(final List<QName> allowedNames, final List<QName> restrictedNames) {
        return new RenameDialog(environment, allowedNames, restrictedNames);
    }

    @Override
    public IChoiceScheme newCreateChoiceDialog(XmlSchemaItem choiceItem, int currentItemIndex) {
        return new ChoiceSchemeDialog(choiceItem, currentItemIndex);
    }

    @Override
    public IChoiceScheme newCreateRootChoiceDialog(List<XmlSchemaItem> choiceItems) {
        return new ChoiceSchemeDialog(choiceItems);
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
                    ListBoxItem item = new ListBoxItem();
                    item.setText(error.getMessage());
                    item.setTextWrapDisabled(true);
                    item.setIcon(environment.getApplication().getImageManager().getIcon(ClientIcon.TraceLevel.ERROR));
                    errorsList.add(item);
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
            final XmlDocument xmlDoc = editor.getController().getDocument();
            final String newDoc = isCurrentMode(XmlEditorController.EXmlEditorMode.XmlPrettyTextMode) ? prettyXmlText.getText().trim() : xmlText.getText().trim();
            if (!isCurrentMode(XmlEditorController.EXmlEditorMode.XmlPrettyTextMode)) {
                if (!editor.getController().getDocument().getXmlText().equals(newDoc) && !canChangeDocument(newDoc.trim().isEmpty() ? xmlDoc.getXmlText() : newDoc.trim())) {
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
        xmlTree.setVisible(true);
        xmlText.setVisible(false);
        prettyXmlText.setVisible(false);
        schemeTextArea.setVisible(false);
        setReadOnly(editor.isReadOnly());
        linkClipboardActions(null);
        return true;
    }

    @Override
    public boolean switchToTextMode(final XmlDocument document, final boolean isReadOnly) {
        xmlTree.setVisible(false);
        prettyXmlText.setVisible(false);
        xmlText.setVisible(true);
        schemeTextArea.setVisible(false);
        xmlText.setText(document.getXmlText());
        setReadOnly(isReadOnly);
        linkClipboardActions(xmlText);
        return true;
    }

    @Override
    public boolean switchToPrettyTextMode(final XmlDocument document, final boolean isTreeCheked) {
        if (isTreeCheked || isCurrentMode(XmlEditorController.EXmlEditorMode.XmlPrettyTextMode)) {
            prettyXmlText.setText(document.getXmlText(XmlUtils.getPrettyXmlOptions()));
        } else {
            final String newDoc = xmlText.getText() == null || xmlText.getText().isEmpty() ? document.getXmlText() : xmlText.getText();
            if (!document.getXmlText().equals(newDoc)) {
                if (!canChangeDocument(newDoc)) {
                    return false;
                }
            }
            prettyXmlText.setText(createXmlDoc(newDoc).getXmlText(XmlUtils.getPrettyXmlOptions()));
        }
        xmlTree.setVisible(false);
        xmlText.setVisible(false);
        prettyXmlText.setVisible(true);
        schemeTextArea.setVisible(false);
        linkClipboardActions(prettyXmlText);
        return true;
    }

    @Override
    public boolean switchToSchemeTextMode(final String schemeText) {
        if (isCurrentMode(XmlEditorController.EXmlEditorMode.XmlTextMode) && !xmlText.getText().isEmpty()) {
            if (!canChangeDocument(xmlText.getText())) {
                return false;
            }
        }
        schemeTextArea.setVisible(true);
        xmlTree.setVisible(false);
        xmlText.setVisible(false);
        prettyXmlText.setVisible(false);
        schemeTextArea.setText(schemeText);
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
        final float part = splitter.getPart(0);
        environment.getConfigStore().writeDouble(CONFIG_PREFIX + "/splitterPart", part);
    }

    private void restoreSplitterPosition() {
        double splitterPos
                = environment.getConfigStore().readDouble(CONFIG_PREFIX + "/splitterPart", 0.8);
        splitter.setPart(0, (float) splitterPos);
    }

    @Override
    public void setReadOnly(final boolean isReadOnly) {
        if (getXmlTree() != null
                && getXmlTree().getTreeController() != null) {
            showErrors(null, false);
            getXmlTree().getTreeController().refreshAllItems();
            if (xmlText != null && xmlText.getText() != null) {
                xmlText.setReadOnly(isReadOnly);
                setBackgroundCol(isReadOnly);
            }
        }
    }

    private void setBackgroundCol(final boolean isReadOnly) {
        final EnumSet<ETextOptionsMarker> markers = EnumSet.of(ETextOptionsMarker.EDITOR);
        if (isReadOnly) {
            markers.add(ETextOptionsMarker.READONLY_VALUE);
        }
        final ITextOptions options
                = environment.getTextOptionsProvider().getOptions(markers, null);
        xmlText.setTextOptions((WpsTextOptions) options);
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
        copyButton = toolBar.getWidgetForAction(copyAction);
        this.copyAction = copyAction;
        cutButton = toolBar.getWidgetForAction(cutAction);
        this.cutAction = cutAction;
        pasteButton = toolBar.getWidgetForAction(pasteAction);
        this.pasteAction = pasteAction;
    }

    private void linkClipboardActions(final TextArea textArea) {
        final Html copyButtonHtml = ((UIObject) copyButton).getHtml();
        if (textArea == null) {
            copyButtonHtml.setAttr("textAreaId", null);
            copyButton.addClickHandler(new IButton.ClickHandler() {
                @Override
                public void onClick(final IButton source) {
                    copyAction.trigger();
                }
            });
        } else {
            copyButton.clearClickHandlers();
            final String textAreaId = textArea.getHtml().getChildAt(0).getId();
            copyButtonHtml.setAttr("onclick", "$RWT.textArea.getSelectedText");
            copyButtonHtml.setAttr("textAreaId", textAreaId);
        }
        final Html cutButtonHtml = ((UIObject) cutButton).getHtml();
        if (textArea == null) {
            cutButtonHtml.setAttr("textAreaId", null);
            cutButton.addClickHandler(new IButton.ClickHandler() {
                @Override
                public void onClick(final IButton source) {
                    cutAction.trigger();
                }
            });
        } else {
            cutButton.clearClickHandlers();
            final String textAreaId = textArea.getHtml().getChildAt(0).getId();
            cutButtonHtml.setAttr("onclick", "$RWT.textArea.getCursorPosition");
            cutButtonHtml.setAttr("textAreaId", textAreaId);
        }
        final Html pasteButtonHtml = ((UIObject) pasteButton).getHtml();
        if (textArea == null) {
            pasteButtonHtml.setAttr("textAreaId", null);
            pasteButton.addClickHandler(new IButton.ClickHandler() {
                @Override
                public void onClick(final IButton source) {
                    pasteAction.trigger();
                }
            });
        } else {
            pasteButton.clearClickHandlers();
            final String textAreaId = textArea.getHtml().getChildAt(0).getId();
            pasteButtonHtml.setAttr("onclick", "$RWT.textArea.getCursorPosition");
            pasteButtonHtml.setAttr("textAreaId", textAreaId);
        }
    }
}
