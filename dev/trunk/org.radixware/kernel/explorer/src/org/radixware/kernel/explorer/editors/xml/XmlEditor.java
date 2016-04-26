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

package org.radixware.kernel.explorer.editors.xml;

import java.util.ArrayList;

import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SchemaTypeSystem;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;

import com.trolltech.qt.core.QByteArray;
import com.trolltech.qt.core.QDir;
import com.trolltech.qt.core.QPoint;
import com.trolltech.qt.core.QRegExp;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt.DockWidgetArea;
import com.trolltech.qt.core.Qt.Orientation;
import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QCloseEvent;
import com.trolltech.qt.gui.QDialog;
import com.trolltech.qt.gui.QDockWidget;
import com.trolltech.qt.gui.QGridLayout;
import com.trolltech.qt.gui.QKeySequence;
import com.trolltech.qt.gui.QMainWindow;
import com.trolltech.qt.gui.QMenu;
import com.trolltech.qt.gui.QSizePolicy;
import com.trolltech.qt.gui.QStackedWidget;
import com.trolltech.qt.gui.QTableWidget;
import com.trolltech.qt.gui.QTextCursor;
import com.trolltech.qt.gui.QToolBar;
import com.trolltech.qt.gui.QToolButton;
import com.trolltech.qt.gui.QWidget;
import com.trolltech.qt.gui.QDockWidget.DockWidgetFeature;
import com.trolltech.qt.gui.QFileDialog;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.env.ClientSettings;
import org.radixware.kernel.common.client.exceptions.ClientException;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.exceptions.CancelXmlDocumentCreationException;
import org.radixware.kernel.explorer.exceptions.XmlDocumentCreationException;
import org.radixware.kernel.explorer.exceptions.XmlDocumentIsNotDefinedException;
import org.radixware.kernel.explorer.exceptions.XmlEditorException;
import org.radixware.kernel.explorer.exceptions.XmlSchemaCompilationException;
import org.radixware.kernel.explorer.exceptions.XmlSchemaLoadingException;
import org.radixware.kernel.common.client.utils.ClientValueFormatter;
import org.radixware.kernel.explorer.utils.WidgetUtils;
import org.radixware.kernel.common.client.utils.XmlSchemaTypeSystemCompiler;
import org.radixware.kernel.explorer.views.MainWindow;

public final class XmlEditor extends MainWindow {

    public final static class Icons extends ClientIcon {//yremizov

        private Icons(final String fileName) {
            super(fileName, true);
        }
        public static final ClientIcon WINDOW_ICON = ValueTypes.XML;
        public static final ClientIcon VIEW_AS_TREE = new Icons("classpath:images/tree.svg");
        public static final ClientIcon VIEW_AS_TEXT = new Icons("classpath:images/text.svg");
        public static final ClientIcon VIEW_AS_PRETTY_TEXT = new Icons("classpath:images/pretty_text.svg");
        public static final ClientIcon FORMAT_TEXT = new Icons("classpath:images/formatText.svg");
        public static final ClientIcon ADD_NODE = new Icons("classpath:images/addnode.svg");
        public static final ClientIcon ADD_CHILD_NODE = new Icons("classpath:images/addnodetonext.svg");
        public static final ClientIcon ADD_ATTRIBUTE = new Icons("classpath:images/addattribute.svg");
        public static final ClientIcon ADD_ATTRIBUTE_TO_CURRENT = new Icons("classpath:images/addattributenext.svg");
        public static final ClientIcon VALIDATE = new Icons("classpath:images/validate.svg");
        public static final ClientIcon UNION = new Icons("classpath:images/union.svg");        
    }
    private Ui_XmlEditor ui = new Ui_XmlEditor();
    private boolean readonly = false;
    private SchemaTypeSystem types;
    private Id schemaId;
    private XmlObject xml;
    public final int TREE_MODE = 0;
    public final int TEXT_MODE = 1;
    public final int PRETTY_TEXT_MODE = 2;
    private QToolBar tools;
    public QToolButton addButton = new QToolButton(this);
    public QAction addButtonAction;
    public QToolButton nextButton = new QToolButton(this);
    public QAction nextButtonAction;
    public QToolButton attrButton = new QToolButton(this);
    public QAction attrButtonAction;
    public QToolButton nextAttrButton = new QToolButton(this);
    public QAction nextAttrButtonAction;
    public final QAction textAction =
            new QAction(ExplorerIcon.getQIcon(Icons.VIEW_AS_TEXT), Application.translate("XmlEditor", "Text Mode"), this);
    public final QAction treeAction =
            new QAction(ExplorerIcon.getQIcon(Icons.VIEW_AS_TREE), Application.translate("XmlEditor", "Tree Mode"), this);
    public final QAction prettyTextAction =
            new QAction(ExplorerIcon.getQIcon(Icons.VIEW_AS_PRETTY_TEXT), Application.translate("XmlEditor", "Formatted Text"), this);
    public final QAction validateAction =
            new QAction(ExplorerIcon.getQIcon(Icons.VALIDATE), Application.translate("XmlEditor", "Validate"), this);
    public final QAction deleteAction =
            new QAction(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.DELETE), Application.translate("XmlEditor", "Delete Node"), this);
    public final QAction cutAction =
            new QAction(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.CUT), Application.translate("XmlEditor", "Cut Selected Text"), this);
    public final QAction copyAction =
            new QAction(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.COPY), Application.translate("XmlEditor", "Copy to Clipboard"), this);
    public final QAction pasteAction =
            new QAction(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.PASTE), Application.translate("XmlEditor", "Paste from Clipboard"), this);
    public final QAction undoAction =
            new QAction(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.UNDO), Application.translate("XmlEditor", "Undo Last Operation"), this);
    public final QAction redoAction =
            new QAction(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.REDO), Application.translate("XmlEditor", "Redo Last Operation"), this);
    public final QAction formatTextAction = 
            new QAction(ExplorerIcon.getQIcon(Icons.FORMAT_TEXT), Application.translate("XmlEditor", "Format Text"), this);
    public final QAction openFileAction =
            new QAction(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.OPEN), Application.translate("XmlEditor", "Open Document from File"), this);
    public final QAction saveFileAction =
            new QAction(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.SAVE), Application.translate("XmlEditor", "Save Document to File"), this);
    QDockWidget errorDock = new QDockWidget(Application.translate("XmlEditor", "Validation Errors"), this);
    public QMenu mFile = new QMenu(Application.translate("XmlEditor", "General"), this);
    public QMenu mEdit = new QMenu(Application.translate("XmlEditor", "Edit"), this);
    public QMenu mTree = new QMenu(Application.translate("XmlEditor", "Tree"), this);
    private final TreeWindow tw;
    private TextWindow txtW = new TextWindow(this);
    private TextWindow prettyTxtW = new TextWindow(this);
    private QStackedWidget pages;
    private Map<String, String> prefix2namespace;
//    private QPushButton treeButton;
//    private QPushButton textButton;
//    private QPushButton prettyTextButton;
    private final IClientEnvironment environment;

    public XmlEditor(IClientEnvironment environment) {
        this.environment = environment;
        tw = new TreeWindow(this);
        setupUi();
    }

    public XmlEditor(IClientEnvironment environment, QWidget parent) {
        super(parent);
        this.environment = environment;
        tw = new TreeWindow(this);
        setupUi();
    }

    public IClientEnvironment getEnvironment() {
        return environment;
    }

    //************SETUP METHODS************
    private void setupUi() {
        ui.setupUi(this);
        setupMenus();
        setActions();
        setWindowIcon(ExplorerIcon.getQIcon(Icons.WINDOW_ICON));
        pages = new QStackedWidget(this.centralWidget());
        pages.addWidget(tw);
        pages.addWidget(txtW);
        pages.addWidget(prettyTxtW);
        QGridLayout grid = new QGridLayout(this.centralWidget());
        grid.setMargin(0);
        grid.addWidget(pages);
        //****************************************
        tools = addToolBar(Application.translate("XmlEditor", "Toolbar"));
        tools.setFloatable(false);
        WidgetUtils.updateActionToolTip(validateAction);
        tools.addAction(validateAction);
        tools.addSeparator();

        WidgetUtils.updateActionToolTip(treeAction);
        treeAction.setCheckable(true);
        tools.addAction(treeAction);
        WidgetUtils.updateActionToolTip(textAction);
        textAction.setCheckable(true);
        tools.addAction(textAction);
        WidgetUtils.updateActionToolTip(prettyTextAction);
        prettyTextAction.setCheckable(true);
        tools.addAction(prettyTextAction);

        tools.addSeparator();

        openFileAction.setIcon(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.OPEN));
        WidgetUtils.updateActionToolTip(openFileAction);
        tools.addAction(openFileAction);

        saveFileAction.setIcon(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.SAVE));
        WidgetUtils.updateActionToolTip(saveFileAction);
        tools.addAction(saveFileAction);

        tools.addSeparator();
        addButtonAction =
                initToolButton(addButton, Icons.ADD_NODE, Application.translate("XmlEditor", "Add Node"));
        nextButtonAction =
                initToolButton(nextButton, Icons.ADD_CHILD_NODE, Application.translate("XmlEditor", "Add Child Node"));
        attrButtonAction =
                initToolButton(attrButton, Icons.ADD_ATTRIBUTE, Application.translate("XmlEditor", "Add Attribute to Parent"));
        nextAttrButtonAction =
                initToolButton(nextAttrButton, Icons.ADD_ATTRIBUTE_TO_CURRENT, Application.translate("XmlEditor", "Add Attribute to Child"));
        WidgetUtils.updateActionToolTip(deleteAction);
        tools.addAction(deleteAction);
        WidgetUtils.updateActionToolTip(cutAction);
        tools.addAction(cutAction);
        WidgetUtils.updateActionToolTip(copyAction);
        tools.addAction(copyAction);
        WidgetUtils.updateActionToolTip(pasteAction);
        tools.addAction(pasteAction);
        WidgetUtils.updateActionToolTip(undoAction);
        tools.addAction(undoAction);
        WidgetUtils.updateActionToolTip(redoAction);
        tools.addAction(redoAction);
        WidgetUtils.updateActionToolTip(formatTextAction);
        tools.addAction(formatTextAction);

        tools.setObjectName("ToolBar");

        setupDocks();
        readSettings();

        errorDock.setVisible(false);
        modeSetup(TREE_MODE);
    }

    private QAction initToolButton(final QToolButton toolButton, final ClientIcon icon, final String toolTip) {
        toolButton.setIcon(ExplorerIcon.getQIcon(icon));

        final QMenu menu = new QMenu(toolButton);
        toolButton.setMenu(menu);
        toolButton.setToolTip(ClientValueFormatter.capitalizeIfNecessary(environment, toolTip));
        toolButton.setDisabled(true);
        return tools.addWidget(toolButton);
    }

    @SuppressWarnings("unused")
    private void toTreeMode() {
        if (!readonly) {
            if (txtW.isModified()) {
                boolean valid = setXml(txtW.getText());
                if (valid) {
                    switchToMode(TREE_MODE);
                    txtW.setModified(false);
                    clearNamespaceMap();
                    tw.open(isReadOnlyMode(), xml);
                    tw.setModified(true);
                    textAction.setChecked(false);
                    treeAction.setChecked(true);
                }else{
                    treeAction.setChecked(false);
                }
            } else {
                switchToMode(TREE_MODE);
                tw.setModified(false);
                textAction.setChecked(false);
                treeAction.setChecked(true);
            }
        } else {
            switchToMode(TREE_MODE);
            textAction.setChecked(false);
            treeAction.setChecked(true);
        }
        prettyTextAction.setChecked(false);
    }

    @SuppressWarnings("unused")
    private void toTextMode() {
        if (!readonly) {
            if (tw.isModified()) {
                txtW.openText(xml, readonly, false, prefix2namespace);
            }
        }
        switchToMode(TEXT_MODE);
        textAction.setChecked(true);
        treeAction.setChecked(false);
        prettyTextAction.setChecked(false);
        selectCurrentNodeInText(txtW);
    }

    @SuppressWarnings("unused")
    private void toPrettyText() {
        if (!readonly) {
            if (tw.isModified()) {
                prettyTxtW.openText(xml, readonly, true, prefix2namespace);
                textAction.setChecked(false);
                prettyTextAction.setChecked(true);

                switchToMode(PRETTY_TEXT_MODE);
                selectCurrentNodeInText(prettyTxtW);
            } else if (txtW.isModified()) {
                boolean valid = setXml(txtW.getText());
                if (valid) {
                    //txtW.setModified(false);
                    prettyTxtW.openText(xml, readonly, true, prefix2namespace);
                    textAction.setChecked(false);
                    prettyTextAction.setChecked(true);

                    switchToMode(PRETTY_TEXT_MODE);
                    selectCurrentNodeInText(prettyTxtW);
                }else{
                    prettyTextAction.setChecked(false);
                }
            } else {
                switchToMode(PRETTY_TEXT_MODE);
                selectCurrentNodeInText(prettyTxtW);
                textAction.setChecked(false);
                prettyTextAction.setChecked(true);
            }
        } else {
            switchToMode(PRETTY_TEXT_MODE);
            selectCurrentNodeInText(prettyTxtW);
            textAction.setChecked(false);
            prettyTextAction.setChecked(true);
        }
        treeAction.setChecked(false);
    }

    private void selectCurrentNodeInText(TextWindow win) {
        String curTitle = "";
        int index = 0;
        int end = 0;
        //*******ATTRIBUTE SEARCH*******
        if (tw.getTree().currentItem() instanceof XTreeAttribute) {
            XTreeTag tag = (XTreeTag) tw.getTree().currentItem().parent();
            String tagTitle = "";
            if (!tag.getElementPrefix().isEmpty()) {
                tagTitle = tag.getElementPrefix() + ":";
            }
            tagTitle = tagTitle + tag.getNode().getDomNode().getLocalName();
            index = findTagIndex(tag, tagTitle, win);
            XTreeAttribute atr = (XTreeAttribute) tw.getTree().currentItem();
            if (!atr.getElementPrefix().isEmpty()) {
                curTitle = atr.getElementPrefix() + ":";
            }
            curTitle = curTitle + atr.getNode().getDomNode().getLocalName();
            String val = atr.getNode().getDomNode().getNodeValue() != null ? atr.getNode().getDomNode().getNodeValue() : "";
            QRegExp exp = new QRegExp(curTitle + "=\\x0022" + val + "\\x0022");
            index = exp.indexIn(win.getText(), index);
            QRegExp end_exp = new QRegExp("[^=]\\x0022");
            end = end_exp.indexIn(win.getText(), index) + 2 - index;
        }
        //*******ELEMENT SEARCH********
        if (tw.getTree().currentItem() instanceof XTreeTag) {
            XTreeTag tag = (XTreeTag) tw.getTree().currentItem();
            if (!tag.getElementPrefix().isEmpty()) {
                curTitle = tag.getElementPrefix() + ":";
            }
            curTitle = curTitle + tag.getNode().getDomNode().getLocalName();
            index = findTagIndex(tag, curTitle, win);
            if (tag.getNode().getDomNode().getChildNodes().getLength() != 0) {
                QRegExp end_exp = new QRegExp("</" + curTitle);
                end = end_exp.indexIn(win.getText(), index) + curTitle.length() + 3 - index;//...title>
            } else {
                QRegExp end_exp = new QRegExp("/>");
                end = end_exp.indexIn(win.getText(), index) + 2 - index;
            }
        }
        //*****************************
        if (index != -1) {
            QTextCursor c = new QTextCursor(win.getDocument());
            c.setPosition(index);
            c.movePosition(QTextCursor.MoveOperation.Right, QTextCursor.MoveMode.KeepAnchor, end);
            win.getTextEditor().setTextCursor(c);
        }
    }

    private int findTagIndex(XTreeTag tag, String curTitle, TextWindow win) {
        QRegExp exp = new QRegExp("<" + curTitle + " *");
        String winText = win.getText();
        int index = exp.indexIn(winText);
        if (index >= 0 && !XElementTools.getCurrentAttributes(tag.getNode()).isEmpty()) {
            int childCount = tag.getNode().getDomNode().getChildNodes().getLength();
            QRegExp end_exp = childCount == 0 ? new QRegExp("/>") : new QRegExp(">");
            int end = end_exp.indexIn(winText, index) + 1;
            String str = winText.substring(index, end);
            boolean res = checkStr(str, tag);
            while (!res
                    && index != -1) {
                index = exp.indexIn(winText, index + 1);
                if (index != -1) {
                    end = end_exp.indexIn(winText, index) + 1;
                    str = winText.substring(index, end);
                    res = checkStr(str, tag);
                }
            }
            return index;
        } else {
            return index;
        }
    }

    private boolean checkStr(String res, XTreeTag tag) {
        if (res != null) {
            ArrayList<XmlObject> attrs = XElementTools.getCurrentAttributes(tag.getNode());
            if (attrs != null && !attrs.isEmpty()) {
                for (XmlObject a : attrs) {
                    String title = a.getDomNode().getLocalName();
                    String prefix = a.getDomNode().getPrefix();
                    if (prefix != null && !prefix.isEmpty()) {
                        title = prefix + ":" + title;
                    }
                    String val = a.getDomNode().getNodeValue() != null ? a.getDomNode().getNodeValue() : "";
                    QRegExp exp = new QRegExp(title + "=\\x0022.*\\x0022");
                    int exp_index = exp.indexIn(res);
                    if (exp_index == -1) {
                        return false;
                    } else {
                        exp = new QRegExp("[^=]\\x0022");
                        int end = exp.indexIn(res, exp_index);
                        if (end<0 || !res.substring(exp_index, end + 1).contains(val)) {
                            return false;
                        }
                    }
                }
                return true;
            }
        }
        return false;
    }

    private void switchToMode(int mode) {
        pages.setCurrentIndex(mode);
        if (!readonly) {
            modeSetup(mode);
        }
    }

    private void modeSetup(int mode) {
        if (mode == TREE_MODE) {
            switchTreeButtons(true);
            switchTextButtons(false);            
            tw.setupToolsForItem();
        } else if (mode == TEXT_MODE) {
            switchTreeButtons(false);
            switchTextButtons(true);            
        } else if (mode == PRETTY_TEXT_MODE) {
            switchTreeButtons(false);
            switchTextButtons(false);
            mTree.setDisabled(true);
            this.getMenuItem(mFile, "validate").setDisabled(true);
        }
        formatTextAction.setVisible(mode == TEXT_MODE);
    }

    private void switchTextButtons(boolean x) {
        cutAction.setVisible(x);
        copyAction.setVisible(x);
        pasteAction.setVisible(x);
        undoAction.setVisible(x);
        redoAction.setVisible(x);
        mEdit.setDisabled(!x);
        //this.getMenuItem(mFile, "parseitem").setDisabled(x);
        mTree.setDisabled(x);
    }

    private void switchTreeButtons(boolean x) {
        addButtonAction.setVisible(x);
        nextButtonAction.setVisible(x);
        deleteAction.setVisible(x);
        attrButtonAction.setVisible(x);
        nextAttrButtonAction.setVisible(x);
        mEdit.setDisabled(x);
        mTree.setDisabled(!x);
        //this.getMenuItem(mFile, "parseitem").setDisabled(!x);
    }

    private void setReadOnlyMode(boolean readonly) {
        addButtonAction.setVisible(!readonly);
        nextButtonAction.setVisible(!readonly);
        deleteAction.setVisible(!readonly);
        attrButtonAction.setVisible(!readonly);
        nextAttrButtonAction.setVisible(!readonly);
        cutAction.setVisible(!readonly);
        copyAction.setVisible(!readonly);
        pasteAction.setVisible(!readonly);
        undoAction.setVisible(!readonly);
        redoAction.setVisible(!readonly);
        formatTextAction.setVisible(!readonly);
        openFileAction.setVisible(!readonly);
        mEdit.setDisabled(readonly);
        //this.getMenuItem(mFile, "parseitem").setDisabled(readonly);
        mTree.setDisabled(readonly);
    }
    
    private XmlObject parseXml(String doc){
        if (xml == null) {
            return null;
        }
        XmlCursor c = xml.newCursor();
        SchemaType doctype = types.findDocumentType(c.getName());
        try {
            XmlOptions opt = new XmlOptions();
            return types.parse(doc, doctype, opt);
        } catch (XmlException xmle) {
            Application.messageWarning(xmle.getMessage());
            txtW.selectErrorLine(xmle.getError());
            return null;
        }             
    }

    public boolean setXml(String doc) {
        XmlObject newXml = parseXml(doc);
        if (newXml==null){
            return false;
        }
        else{
            xml = newXml.copy();
            return true;
        }
    }

    private void setActions() {
        cutAction.triggered.connect(txtW, "cutSelection()");
        copyAction.triggered.connect(txtW, "copySelection()");
        pasteAction.triggered.connect(txtW, "pasteFromClipboard()");
        undoAction.triggered.connect(txtW, "undoOperation()");
        redoAction.triggered.connect(txtW, "redoOperation()");
        formatTextAction.triggered.connect(txtW,"formatTextOperation()");

        validateAction.triggered.connect(this, "validate()");

        deleteAction.triggered.connect(this, "deleteNode()");
        deleteAction.setDisabled(true);
        deleteAction.setShortcut(QKeySequence.StandardKey.Delete);

        treeAction.triggered.connect(this, "toTreeMode()");
        textAction.triggered.connect(this, "toTextMode()");
        prettyTextAction.triggered.connect(this, "toPrettyText()");

        openFileAction.triggered.connect(this, "openXmlFromFile()");
        saveFileAction.triggered.connect(this, "saveXmlToFile()");
    }

    private void setupDocks() {
        errorDock.setObjectName("ValidationErrors");

        QMainWindow.DockOptions dOptions = new QMainWindow.DockOptions();
        dOptions.set(DockOption.createQFlags(DockOption.AllowTabbedDocks));
        this.setDockOptions(dOptions);
        QDockWidget.DockWidgetFeatures features = new QDockWidget.DockWidgetFeatures();
        features.set(DockWidgetFeature.DockWidgetMovable);
        //errors dock
        this.addDockWidget(DockWidgetArea.BottomDockWidgetArea, errorDock, Orientation.Vertical);
        errorDock.setAllowedAreas(DockWidgetArea.BottomDockWidgetArea);
        errorDock.setWidget(tw.getErrorWidget());
        errorDock.setMinimumHeight(50);
        errorDock.setMinimumWidth(100);
        errorDock.setFeatures(features);
        errorDock.setContentsMargins(0, 0, 0, 0);
    }

    private void setupMenus() {
        QAction mCut = new QAction(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.CUT), Application.translate("XmlEditor", "Cut"), this);
        mCut.setObjectName("cutitem");
        mCut.setDisabled(true);
        QAction mCopy = new QAction(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.COPY), Application.translate("XmlEditor", "Copy"), this);
        mCopy.setObjectName("copyitem");
        mCopy.setDisabled(true);
        QAction mPaste = new QAction(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.PASTE), Application.translate("XmlEditor", "Paste"), this);
        mPaste.setObjectName("pasteitem");
        QAction mUndo = new QAction(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.UNDO), Application.translate("XmlEditor", "Undo"), this);
        mUndo.setObjectName("undoitem");
        mUndo.setDisabled(true);
        QAction mRedo = new QAction(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.REDO), Application.translate("XmlEditor", "Redo"), this);
        mRedo.setObjectName("redoitem");
        mRedo.setDisabled(true);

        mCut.triggered.connect(txtW, "cutSelection()");
        mCopy.triggered.connect(txtW, "copySelection()");
        mPaste.triggered.connect(txtW, "pasteFromClipboard()");
        mUndo.triggered.connect(txtW, "undoOperation()");
        mRedo.triggered.connect(txtW, "redoOperation()");

        mEdit.addAction(mCut);
        mEdit.addAction(mCopy);
        mEdit.addAction(mPaste);
        mEdit.addSeparator();
        mEdit.addAction(mUndo);
        mEdit.addAction(mRedo);

        QAction mValidate = new QAction(ExplorerIcon.getQIcon(Icons.VALIDATE), Application.translate("XmlEditor", "Validate"), this);
        mValidate.setShortcut(QKeySequence.StandardKey.Refresh);        
        mValidate.setObjectName("validate");
        mValidate.triggered.connect(this, "validate()");
        mFile.addAction(mValidate);                
        /*QAction mParse = new QAction(null, Application.translate("XmlEditor", "Parse"), this);
        mParse.setObjectName("parseitem");
        mParse.setShortcut(QKeySequence.StandardKey.Print);        
        mParse.triggered.connect(this, "parseXml()");   
        mFile.addAction(mParse);*/        

        QAction mAdd = new QAction(ExplorerIcon.getQIcon(Icons.ADD_NODE), Application.translate("XmlEditor", "Add Node..."), this);
        mAdd.setObjectName("addnode");
        mAdd.setDisabled(true);
        QAction mAddNext = new QAction(ExplorerIcon.getQIcon(Icons.ADD_CHILD_NODE), Application.translate("XmlEditor", "Add Child Node..."), this);
        mAddNext.setObjectName("addchild");
        mAddNext.setDisabled(true);
        QAction mAddAttr = new QAction(ExplorerIcon.getQIcon(Icons.ADD_ATTRIBUTE), Application.translate("XmlEditor", "Add Attribute to Parent Node"), this);
        mAddAttr.setObjectName("addattr");
        mAddAttr.setDisabled(true);
        QAction mAddAttrNext = new QAction(ExplorerIcon.getQIcon(Icons.ADD_ATTRIBUTE_TO_CURRENT), Application.translate("XmlEditor", "Add Attribute to Current Node"), this);
        mAddAttrNext.setObjectName("addattrnext");
        mAddAttrNext.setDisabled(true);
        QAction mDelete = new QAction(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.DELETE), Application.translate("XmlEditor", "Delete Node"), this);
        mDelete.setObjectName("delete");
        mDelete.setDisabled(true);

        mDelete.triggered.connect(this, "deleteNode()");

        mTree.addAction(mAdd);
        mTree.addAction(mAddNext);
        mTree.addAction(mAddAttr);
        mTree.addAction(mAddAttrNext);
        mTree.addAction(mDelete);

        this.menuBar().addMenu(mFile);
        this.menuBar().addMenu(mEdit);
        this.menuBar().addMenu(mTree);

        mTree.setDisabled(false);
        mEdit.setDisabled(true);
    }

    public QAction getMenuItem(QMenu owner, String name) {
        for (QAction a : owner.actions()) {
            if (a.objectName().equals(name)) {
                return a;
            }
        }
        return null;
    }

    //************SETTINGS PROCESSING************
    private void writeSettings() {
        ClientSettings settings = environment.getConfigStore();//QSettings settings = new QSettings("Compas Plus","Xml Editor");
        settings.beginGroup("XmlEditor");
        settings.setValue("mainsize", size());
        settings.setValue("mainpos", pos());
        settings.setValue("state", this.saveState());
        settings.endGroup();
    }

    private void readSettings() {
        ClientSettings settings = environment.getConfigStore();//QSettings settings = new QSettings("Compas Plus","Xml Editor");
        settings.beginGroup("XmlEditor");
        QSize size = (QSize) settings.value("mainsize", this.minimumSize());
        this.setSizePolicy(QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Expanding);
        resize(size);
        QPoint pos = (QPoint) settings.value("mainpos", new QPoint(200, 200));
        move(pos);
        QByteArray state = (QByteArray) settings.value("state", null);
        this.restoreState(state);
        settings.endGroup();
    }

    @Override
    public void closeEvent(QCloseEvent event) {
        writeSettings();
        super.closeEvent(event);
    }

    //************DOCUMENT PROCESSING************
    public void createNew(final SchemaTypeSystem typeSystem) throws XmlDocumentCreationException {
        types = typeSystem;
        xml = null;
        txtW.clearText();
        final SchemaType[] documentTypes = types.documentTypes();
        if (documentTypes != null && documentTypes.length != 0) {
            if (documentTypes.length == 1) {
                createNew(documentTypes[0]);
            } else {
                final RootList dialog = new RootList(getEnvironment(), this, documentTypes);
                if (dialog.exec() != QDialog.DialogCode.Accepted.value() || dialog.getSchemaType() == null) {
                    throw new CancelXmlDocumentCreationException(getEnvironment().getMessageProvider());
                }
                createNew(dialog.getSchemaType());
            }
        } else {
            final String message = Application.translate("XmlEditor", "Can't determine type of document to be created");
            throw new XmlDocumentCreationException(getEnvironment().getMessageProvider(), message);
        }
    }

    private void createNew(final SchemaType documentType) {
        final XmlObject ins = types.newInstance(documentType, null);
        final XmlBuilder xb = new XmlBuilder();
        xb.buildAttributes(ins);
        final ArrayList<Integer> inserts = new ArrayList<Integer>();
        xb.buildNodeContent(ins, inserts);
        openDocument(ins, documentType.getTypeSystem(), false, null);
    }

    public void edit(final XmlObject document, final String schemaDocStr, final boolean readonly) throws XmlEditorException {
        if (schemaDocStr != null) {
            final List<String> errors = new ArrayList<String>();
            final ClassLoader radixClassLoader = environment.getApplication().getDefManager().getClassLoader();
            final SchemaTypeSystem typeSystem = XmlSchemaTypeSystemCompiler.compile(radixClassLoader, schemaDocStr, errors);
            if (typeSystem != null) {
                if (document == null) {
                    createNew(typeSystem);
                } else {
                    openDocument(document, typeSystem, readonly, null);
                }
            } else {
                final StringBuilder sb = new StringBuilder();
                sb.append(Application.translate("XmlEditor", "Error(s) during schema compilation"));
                sb.append("\n");
                for (String err : errors) {
                    sb.append(err);
                    sb.append("\n\n");
                }
                throw new XmlSchemaCompilationException(getEnvironment().getMessageProvider(), sb.toString());
            }
        } else if (document == null) {
            throw new XmlDocumentIsNotDefinedException(getEnvironment().getMessageProvider());
        } else {
            openDocument(document, document.schemaType().getTypeSystem(), readonly, null);
        }
    }

    public void createDoc(final XmlObject document, final Id schemaId) throws XmlEditorException {
        final XmlBuilder xb = new XmlBuilder();
        xb.buildAttributes(document);
        final ArrayList<Integer> inserts = new ArrayList<Integer>();
        xb.buildNodeContent(document, inserts);
        edit(document, false, schemaId);
    }

    public void edit(final XmlObject document, final boolean r, final Id schemaId) throws XmlEditorException {
        if (document == null && schemaId != null) {
            try {
                InputStream stream = getEnvironment().getApplication().getDefManager().getRepository().getInputStreamForXmlScheme(schemaId);
                XmlObject xmlObject = XmlObject.Factory.parse(stream);
                edit(null, xmlObject.xmlText(), r);
            } catch (XmlException xmlexception) {
                final String traceMessage = Application.translate("XmlEditor", "Can't load xml schema #%s: %s\n%s");
                final String reason = ClientException.getExceptionReason(getEnvironment().getMessageProvider(), xmlexception);
                final String stack = ClientException.exceptionStackToString(xmlexception);
                environment.getTracer().error(String.format(traceMessage, schemaId.toString(), reason, stack));
                final String message = Application.translate("XmlEditor", "Can't load xml schema");
                throw new XmlSchemaLoadingException(getEnvironment().getMessageProvider(), message, xmlexception);
            } catch (IOException ioEx) {
                final String traceMessage = Application.translate("XmlEditor", "Can't load xml schema #%s: %s\n%s");
                final String reason = ClientException.getExceptionReason(getEnvironment().getMessageProvider(), ioEx);
                final String stack = ClientException.exceptionStackToString(ioEx);
                environment.getTracer().error(String.format(traceMessage, schemaId.toString(), reason, stack));
                final String message = Application.translate("XmlEditor", "Can't load xml schema");
                throw new XmlSchemaLoadingException(getEnvironment().getMessageProvider(), message, ioEx);
            }
        } else if (document == null) {
            throw new XmlDocumentIsNotDefinedException(getEnvironment().getMessageProvider());
        } else {
            openDocument(document, document.schemaType().getTypeSystem(), r, schemaId);
        }
    }

    private void openDocument(final XmlObject document, final SchemaTypeSystem typeSystem, final boolean isReadonly, final Id schemaId) {
        errorDock.setVisible(false);
        tw.getErrorTable().setRowCount(0);

        xml = document.copy();
        readonly = isReadonly;
        types = typeSystem;
        setReadOnlyMode(readonly);

        if (schemaId != null) {
            this.schemaId = schemaId;
        }
        parseXml();
        txtW.openText(xml, readonly, false, prefix2namespace);
        prettyTxtW.openText(xml, readonly, true, prefix2namespace);
        
        toTreeMode();
    }

    public Id getSchemaId() {
        return schemaId;
    }

    public boolean isReadOnlyMode() {
        return readonly;
    }

    @SuppressWarnings("unused")
    public void validate() {
        if (pages.currentIndex() == TREE_MODE){
            validateImpl(xml);
        }
        else if (pages.currentIndex() == TEXT_MODE){
            final XmlObject xmlToValidate = parseXml(txtW.getText());
            if (xmlToValidate!=null){
                validateImpl(xmlToValidate);
            }
        }        
    }
    
    private void validateImpl(final XmlObject xmlToValidate){
        if (xmlToValidate != null) {
            ArrayList<XmlError> result = DocumentValidator.validate(xmlToValidate);
            QTableWidget table = tw.getErrorTable();
            table.setRowCount(0);
            if (result != null
                    && !result.isEmpty()) {
                errorDock.setVisible(true);
                for (int i = 0; i < result.size(); i++) {
                    XmlError error = result.get(i);
                    table.insertRow(i);
                    TableEditError tee = new TableEditError(error, tw);
                    tee.setText(String.valueOf(i + 1));
                    table.setVerticalHeaderItem(i, tee);
                    TableEdit cell_2 = new TableEdit(error.getErrorCode());
                    table.setCellWidget(i, 0, cell_2);
                    TableEdit cell_3 = new TableEdit(error.getMessage());
                    table.setCellWidget(i, 1, cell_3);
                }
            } else {
                errorDock.setVisible(false);
                Application.messageInformation(Application.translate("XmlEditor", "Validation Result"), Application.translate("XmlEditor", "Found no errors in this document!"));
            }
        } else {
            errorDock.setVisible(false);
            Application.messageWarning(Application.translate("XmlEditor", "Validation Result"), Application.translate("XmlEditor", "Document is empty!"));
        }        
    }

    @SuppressWarnings("unused")
    private void parseXml() {
        if (xml != null) {
            XmlOptions opt = new XmlOptions();
            boolean valid = setXml(xml.xmlText(opt));
            if (valid) {
                clearNamespaceMap();
                tw.open(readonly, xml);
            }
        } else {
            Application.messageWarning(Application.translate("XmlEditor", "Result"), Application.translate("XmlEditor", "Document is empty!"));
        }
    }

    @SuppressWarnings("unused")
    private void deleteNode() {
        tw.deleteCurrentNode();
    }

    public XmlObject getCurrentDocument() {
        if (pages != null
                && pages.currentIndex() == TEXT_MODE) {
            boolean valid = setXml(txtW.getText());
            if (!valid) {
                return null;
            }
        }
        return xml;
    }

    public SchemaTypeSystem getTypes() {
        return types;
    }

    private void openXmlFromFile() {
        try {
            String openFileName = QFileDialog.getOpenFileName(this, Application.translate("XmlEditor", "Open Document"), QDir.homePath(), new QFileDialog.Filter(Application.translate("XmlEditor", "XML Document (*.xml);;Any file (*)")));
            if (openFileName != null && !openFileName.isEmpty()) {
                File xml_file = new File(openFileName);
                xml_file.setReadable(true);
                if (!xml_file.getName().isEmpty()) {
                    XmlObject newXml = XmlObject.Factory.parse(xml_file);
                    boolean valid = setXml(newXml.xmlText());
                    if (valid) {
                        errorDock.setVisible(false);
                        tw.getErrorTable().setRowCount(0);
                        clearNamespaceMap();
                        tw.open(readonly, xml);
                        txtW.openText(xml, readonly, false, prefix2namespace);
                        prettyTxtW.openText(xml, readonly, true, prefix2namespace);
                    }
                }
            }
        } catch (XmlException xmlEx) {
            Application.messageError(Application.translate("XmlEditor", "Xml Exception"), xmlEx.getMessage());
        } catch (IOException ioEx) {
            Application.messageError(Application.translate("XmlEditor", "Input/Output Exception"), ioEx.getMessage());
        }
    }

    private void saveXmlToFile() {
        String saveFileName = QFileDialog.getSaveFileName(this, Application.translate("XmlEditor", "Save Document"), QDir.homePath(), new QFileDialog.Filter(Application.translate("XmlEditor", "XML Document (*.xml);;Any file (*)")));
        if (saveFileName != null && !saveFileName.isEmpty()) {
            File xml_file = new File(saveFileName);
            xml_file.setWritable(true);
            try {
                XmlOptions opt = new XmlOptions();
                opt.setSaveAggressiveNamespaces();
                xml.save(xml_file, opt);
            } catch (IOException io) {
                Application.messageError(Application.translate("XmlEditor", "Input/Output Exception"), io.getMessage());
            }
        }
    }

    public void clearNamespaceMap() {
        if (prefix2namespace != null) {
            prefix2namespace.clear();
        }
    }

    public void addToNamespaceMap(String namespace, String prefix) {
        if (prefix2namespace == null) {
            prefix2namespace = new HashMap<String, String>();
        }
        prefix2namespace.put(namespace, prefix);
    }
}
