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

package org.radixware.kernel.explorer.editors.jmleditor;

import com.trolltech.qt.core.Qt.Orientation;
import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QFileDialog;
import com.trolltech.qt.gui.QKeySequence;
import com.trolltech.qt.gui.QKeySequence.StandardKey;
import com.trolltech.qt.gui.QMenu;
import com.trolltech.qt.gui.QTextCursor;
import com.trolltech.qt.gui.QToolBar;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import org.radixware.kernel.common.client.enums.EDefinitionDisplayMode;
import org.radixware.kernel.common.client.env.ClientSettings;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.meta.RadClassPresentationDef;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.client.views.IDialog.DialogResult;
import org.radixware.kernel.common.defs.ads.userfunc.AdsUserFuncDef.UICallback;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.dialogs.EntityEditorDialog;
import org.radixware.kernel.explorer.dialogs.PresentationInfoDialog;
import org.radixware.kernel.explorer.dialogs.settings.SettingsDialog;
import org.radixware.kernel.explorer.editors.jmleditor.JmlEditor.EditorActionsProvider;
import org.radixware.kernel.explorer.editors.jmleditor.dialogs.AutosaveSettingsDialog;
import org.radixware.kernel.explorer.editors.jmleditor.dialogs.AutosaveVersionsDialog;
import org.radixware.kernel.explorer.editors.jmleditor.dialogs.ImportUserFuncDialog;
import org.radixware.kernel.explorer.editors.jmleditor.dialogs.PreviewExecutableSourceDialog;
import org.radixware.kernel.explorer.editors.jmleditor.dialogs.StackTraceDialog;
import org.radixware.kernel.explorer.editors.xscmleditor.XscmlEditor;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.schemas.udsdef.UserFunctionDefinition;
import org.radixware.schemas.xscml.JmlType;


public class EditToolBar {
    private final QAction btnCut = new QAction(null);
    private final QAction btnCopy = new QAction(null);
    private final QAction btnPast = new QAction(null);
    private final QAction btnUndo = new QAction(null);
    private final QAction btnRedo = new QAction(null);
    private final QAction btnFind = new QAction(null);
    private final QAction btnReplace = new QAction(null);
    private final QAction btnStackTrace = new QAction(null);
    private final QAction btnShowModuleName = new QAction(null);
    private final QAction btnDevelopmentMode = new QAction(null);
    private final QAction btnShowOwnerEditorDlg = new QAction(null);
    private final QAction btnShowExecutableSrc = new QAction(null);
    private final QAction btnShowPresentationInfo = new QAction(null);
    private final QAction btnAutosave = new QAction(null);
    private final QAction btnShowAutosaveVersions = new QAction(null);
    private final QAction btnShowAutosaveSettings = new QAction(null);
    private final QAction btnToggleComment = new QAction(null);
    private final QAction btnFormatCode = new QAction(null);
    private XscmlEditor editText;
    private final JmlEditor parent;
    private QToolBar toolBar;

    public EditToolBar(final JmlEditor editor) {
        parent = editor;
        this.editText = editor.getTextEditor();
        this.editText.selectionChanged.connect(this, "selectionChanged()");
        this.editText.textChanged.connect(this, "editorTextChange()");
        this.editText.undoAvailable.connect(this, "undoEnable(boolean)");
        this.editText.redoAvailable.connect(this, "redoEnable(boolean)");

        createToolBar();
    }

    public void open(final boolean isAnotherFunc) {
        redoEnable(false);
        undoEnable(false);
        if(isAnotherFunc){
            btnDevelopmentMode.setChecked(false);
            btnDevelopmentModeClicked();
        }
        if (parent.getUserFunc().getOwnerPid()==null){
            btnShowOwnerEditorDlg.setVisible(false);
        }
    }
    
    public void updateUndoRedoBtns(){
        redoEnable(editText.isRepoTextEnabled());
        undoEnable(editText.isUndoTextEnabled());
    }

    public void setReadOnlyMode(final boolean isReadOnlyMode) {
        if (isReadOnlyMode) {
            btnCopy.setEnabled(!isReadOnlyMode);
            btnCut.setEnabled(!isReadOnlyMode);
            btnPast.setEnabled(!isReadOnlyMode);
        }
        btnReplace.setEnabled(!isReadOnlyMode);
        btnFormatCode.setEnabled(!isReadOnlyMode);
        btnToggleComment.setEnabled(!isReadOnlyMode);
        enableAutosave(!isReadOnlyMode);
    }

    private void createToolBar() {
        toolBar = new QToolBar(parent);
        toolBar.setObjectName("JmlEditorEditToolBar");
        toolBar.setOrientation(Orientation.Vertical);

        btnCut.setObjectName("btnCut");
        btnCut.setToolTip(Application.translate("JmlEditor", "Cut") + " (Ctrl+X)");
        btnCut.setEnabled(false);
        btnCut.setIcon(ExplorerIcon.getQIcon(JmlEditor.JmlEditorIcons.CUT));
        btnCut.triggered.connect(this, "btnCutClicked()");

        btnCopy.setObjectName("btnCopy");
        btnCopy.setToolTip(Application.translate("JmlEditor", "Copy") + " (Ctrl+C)");
        btnCopy.setEnabled(false);
        btnCopy.setIcon(ExplorerIcon.getQIcon(JmlEditor.JmlEditorIcons.COPY));
        btnCopy.triggered.connect(this, "btnCopyClicked()");

        btnPast.setObjectName("btnPast");
        btnPast.setToolTip(Application.translate("JmlEditor", "Paste") + " (Ctrl+V)");
        btnPast.setEnabled(false);
        btnPast.setIcon(ExplorerIcon.getQIcon(JmlEditor.JmlEditorIcons.PASTE));
        btnPast.triggered.connect(this, "btnPastClicked()");

        btnUndo.setObjectName("btnUndo");
        btnUndo.setToolTip(Application.translate("JmlEditor", "Undo") + " (Ctrl+Z)");
        btnUndo.setEnabled(false);
        btnUndo.setIcon(ExplorerIcon.getQIcon(JmlEditor.JmlEditorIcons.UNDO));
        btnUndo.triggered.connect(this, "btnUndoClicked()");

        btnRedo.setObjectName("btnRedo");
        btnRedo.setToolTip(Application.translate("JmlEditor", "Redo") + " (Ctrl+Y)");
        btnRedo.setEnabled(false);
        btnRedo.setIcon(ExplorerIcon.getQIcon(JmlEditor.JmlEditorIcons.REDO));
        btnRedo.triggered.connect(this, "btnRedoClicked()");

        btnFind.setObjectName("btnFind");
        btnFind.setToolTip(Application.translate("JmlEditor", "Find") + " (Ctrl+F)");
        btnFind.setIcon(ExplorerIcon.getQIcon(JmlEditor.JmlEditorIcons.FIND));
        btnFind.triggered.connect(this, "btnFindClicked()");
        btnFind.setShortcut(StandardKey.Find);//.setShortcut("Ctrl+F");
        btnFind.setCheckable(true);
        btnFind.setChecked(false);
        
        btnReplace.setObjectName("btnReplace");
        btnReplace.setToolTip(Application.translate("JmlEditor", "Replace") + " (Ctrl+H)");
        btnReplace.setIcon(ExplorerIcon.getQIcon(JmlEditor.JmlEditorIcons.IMG_REPLACE));
        btnReplace.triggered.connect(this, "btnReplaceClicked()");
        btnReplace.setShortcut(StandardKey.Replace);//.setShortcut("Ctrl+H");
        btnReplace.setCheckable(true);
        btnReplace.setChecked(false);
        
        btnStackTrace.setObjectName("btnStackTrace");
        btnStackTrace.setToolTip(Application.translate("JmlEditor", "Exception Stack Trace"));
        btnStackTrace.setIcon(ExplorerIcon.getQIcon(JmlEditor.JmlEditorIcons.STACK_TRACE));
        btnStackTrace.triggered.connect(this, "btnStackTraceClicked()");
        //btnStackTrace.setShortcut(StandardKey.Replace);//.setShortcut("Ctrl+H");

        final String settingsKey = SettingNames.SYSTEM + "/" + "user_func_editor_showModuleName";
        final int showMode = parent.getEnvironment().getConfigStore().readInteger(settingsKey, 1);
        final boolean checked = showMode == EDefinitionDisplayMode.SHOW_SHORT_NAMES.ordinal() ? false : true;
        btnShowModuleName.setObjectName("btnShowModuleName");
        btnShowModuleName.setToolTip(Application.translate("JmlEditor", "Show Module Name in Tags"));
        btnShowModuleName.setIcon(ExplorerIcon.getQIcon(JmlEditor.JmlEditorIcons.IMG_MODULE_NAME));
        btnShowModuleName.setCheckable(true);
        btnShowModuleName.setChecked(checked);
        btnShowModuleName.triggered.connect(this, "btnShowModuleNameClicked()");        
        
        //settingsKey = SettingNames.SYSTEM + "/" + "user_func_editor_is_development_mode";
        //boolean isDevelopmentMode = parent.getEnvironment().getConfigStore().readBoolean(settingsKey, false);
        btnDevelopmentMode.setObjectName("btnShowModuleName");
        btnDevelopmentMode.setToolTip(Application.translate("JmlEditor", "Development Mode"));
        btnDevelopmentMode.setIcon(ExplorerIcon.getQIcon(JmlEditor.JmlEditorIcons.IMG_DEVELOPMENT_MODE));
        btnDevelopmentMode.setCheckable(true);        
        btnDevelopmentMode.triggered.connect(this, "btnDevelopmentModeClicked()");
        
        btnShowOwnerEditorDlg.setObjectName("btnShowOwnerEditorDlg");
        btnShowOwnerEditorDlg.setToolTip(Application.translate("JmlEditor", "Show Owner Editor Presentation Dialog"));
        btnShowOwnerEditorDlg.setIcon(ExplorerIcon.getQIcon(ExplorerIcon.Editor.VIEW));
        btnShowOwnerEditorDlg.triggered.connect(this, "showOwnerEditorDialog()");
        
        btnShowExecutableSrc.setObjectName("btnShowExecutableSrc");
        btnShowExecutableSrc.setToolTip(Application.translate("JmlEditor", "Show Executable Source"));
        btnShowExecutableSrc.setIcon(ExplorerIcon.getQIcon(JmlEditor.JmlEditorIcons.IMG_SRC_VIEW));
        btnShowExecutableSrc.triggered.connect(this, "btnShowExecutableSrc_Clicked()");
        btnShowExecutableSrc.setVisible(btnDevelopmentMode.isChecked());
        
        btnShowPresentationInfo.setObjectName("btnShowPresentationInfo");
        btnShowPresentationInfo.setToolTip(Application.translate("JmlEditor", "About"));
        btnShowPresentationInfo.setIcon(ExplorerIcon.getQIcon(SettingsDialog.DialogIcons.ABOUT));
        btnShowPresentationInfo.triggered.connect(this, "btnShowPresentationInfo_Clicked()");
        btnShowPresentationInfo.setVisible(btnDevelopmentMode.isChecked());
        
        btnShowAutosaveVersions.setObjectName("btnShowAutosaveVersions");
        btnShowAutosaveVersions.setText(Application.translate("JmlEditor", "Local History"));
        btnShowAutosaveVersions.setIcon(ExplorerIcon.getQIcon(JmlEditor.JmlEditorIcons.IMG_SRC_VIEW));
        btnShowAutosaveVersions.triggered.connect(this, "btnShowAutoSaveVersions_Clicked()");
        
        btnShowAutosaveSettings.setObjectName("btnShowAutosaveSettings");
        btnShowAutosaveSettings.setText(Application.translate("JmlEditor", "Autosave Settings"));
        btnShowAutosaveSettings.setIcon(ExplorerIcon.getQIcon(SettingsDialog.DialogIcons.APPEARANCE_SETTINGS));
        btnShowAutosaveSettings.triggered.connect(this, "btnShowAutoSaveSettings_Clicked()");
        
        btnAutosave.setObjectName("btnDoAutoSave");
        btnAutosave.setToolTip(Application.translate("JmlEditor", "Save Locally") + " (Ctrl+Shift+S)");
        btnAutosave.setIcon(ExplorerIcon.getQIcon(JmlEditor.JmlEditorIcons.IMG_AUTOSAVE));
        btnAutosave.setShortcut(QKeySequence.fromString("Ctrl+Shift+S"));
        final QMenu autosaveMenu = new QMenu(toolBar);
        autosaveMenu.addAction(btnShowAutosaveVersions);
        autosaveMenu.addAction(btnShowAutosaveSettings);
        btnAutosave.setMenu(autosaveMenu);
        btnAutosave.setVisible(false);
        
        btnToggleComment.setObjectName("btnToggleComment");
        btnToggleComment.setToolTip(Application.translate("JmlEditor", "Comment") + " (Ctrl+/)");
        btnToggleComment.setIcon(ExplorerIcon.getQIcon(JmlEditor.JmlEditorIcons.IMG_TOGGLE_COMMENT));
        btnToggleComment.triggered.connect(this, "btnToggleCommentClicked()");
        btnToggleComment.setShortcuts(Arrays.asList(QKeySequence.fromString("Ctrl+/"), QKeySequence.fromString("Ctrl+Shift+C")));
        
        btnFormatCode.setObjectName("btnFormatCode");
        btnFormatCode.setToolTip(Application.translate("JmlEditor", "Format Code") + " (Alt+Shift+F)");
        btnFormatCode.setIcon(ExplorerIcon.getQIcon(JmlEditor.JmlEditorIcons.IMG_FORMAT_TEXT));
        btnFormatCode.triggered.connect(this, "btnFormatCodeClicked()");
        btnFormatCode.setShortcut(QKeySequence.fromString("Alt+Shift+F"));

        toolBar.addAction(btnAutosave);
        toolBar.addAction(btnFormatCode);
        toolBar.addAction(btnCut);
        toolBar.addAction(btnCopy);
        toolBar.addAction(btnPast);
        toolBar.addAction(btnUndo);
        toolBar.addAction(btnRedo);
        toolBar.addAction(btnFind);
        toolBar.addAction(btnReplace);
        toolBar.addAction(btnToggleComment);
        toolBar.addAction(btnStackTrace);
        toolBar.addAction(btnShowOwnerEditorDlg);
        toolBar.addAction(btnShowModuleName);
        toolBar.addSeparator();
        toolBar.addAction(btnDevelopmentMode);
        toolBar.addAction(btnShowExecutableSrc);
        toolBar.addAction(btnShowPresentationInfo);
        toolBar.addSeparator();
        
        for (QAction a : toolBar.actions()) {
            a.setParent(toolBar);
        }

    }
    
    @SuppressWarnings("unused")
    private void editorTextChange() {
        if (editText.isUndoRedoEnabled()) {
            undoEnable(true);
        }
    }

    public void undoEnable(final boolean available) {
        btnUndo.setEnabled(available);
    }

    public void redoEnable(final boolean available) {
        btnRedo.setEnabled(available);
    }
    
    @SuppressWarnings("unused")
    private void selectionChanged() {
        final QTextCursor tc = editText.textCursor();
        buttonsEnabled(tc.hasSelection());
    }

    @SuppressWarnings("unused")
    private void btnCutClicked() {
        buttonsEnabled(false);
        editText.cutText();
        setEnabledForBtnPast(true);
        editText.setFocusInText();
    }

    @SuppressWarnings("unused")
    private void btnCopyClicked() {
        editText.copyText();
        setEnabledForBtnPast(true);
        editText.setFocusInText();
    }

    void setEnabledForBtnPast(final boolean enabled) {
        if (!editText.isReadOnly()) {
            btnPast.setEnabled(enabled);
        }
    }
    
    void setEnabledShowOwnerEditorDlg(final boolean enabled) {
        btnShowOwnerEditorDlg.setEnabled(enabled);
    }

    @SuppressWarnings("unused")
    private void btnPastClicked() {
        buttonsEnabled(false);
        editText.pastText();
        editText.setFocusInText();
    }

    @SuppressWarnings("unused")
    private void btnUndoClicked() {
        if (editText.undoText()) {
            editText.undo();
        }
        editText.setFocusInText();
        editText.updateUndoRedoBtns.emit();
    }

    @SuppressWarnings("unused")
    private void btnRedoClicked() {
        editText.redoText();
        editText.setFocusInText();
    }
    //private FindAndReplaceDialog findDialog;

    //void setFindDialog(FindAndReplaceDialog d) {
        //findDialog = d;
    //}

    @SuppressWarnings("unused")
    private void btnFindClicked() {
        /*final ExplorerSettings settings = (ExplorerSettings) parent.getEnvironment().getConfigStore();
        FindAndReplaceDialog dialog = new FindAndReplaceDialog((QWidget) editText, settings, "jmlEditor/findDialog", false);
        dialog.setWindowTitle(Application.translate("JmlEditor", "Find") + "...");
        findDialog = dialog;
        dialog.find.connect(this, "findText()");
        dialog.exec();
        btnFindNext.setEnabled(true);*/
        if(!btnFind.isChecked()){
             btnReplace.setChecked(false);
        }
        btnFind.setChecked(true);
        parent.setSearchPanelVisible(/*btnFind.isChecked()*/true);
    }
    
    public void closeSearchPanel(){
        btnFind.setChecked(false);
        btnReplace.setChecked(false);
    }

   /* @SuppressWarnings("unused")
    private void btnFindNext_Clicked() {
        if (findDialog == null) {
            return;
        }
        if (findDialog.getFindWhat() == null) {
            return;
        }
        String findText = findDialog.getFindWhat();
        FindFlags flags = getFindOptions();
        if ((findText != null) && (flags != null) && (!editText.find(findText, flags))) {
            exposeMessageInformation(findText);
        } else {
            isFindTextInTag(editText.textCursor(), false);
        }
    }*/

    @SuppressWarnings("unused")
    private void btnReplaceClicked() {
        if(btnReplace.isChecked() && !btnFind.isChecked()){
            btnFind.setChecked(true);
        }
        parent.setReplacePanelVisible(btnReplace.isChecked());
        //parent.getSearchPanel().setreplaceTextCall();
       /* final ExplorerSettings settings = (ExplorerSettings) parent.getEnvironment().getConfigStore();
        FindAndReplaceDialog dialog = new FindAndReplaceDialog((QWidget) editText, settings, "jmlEditor/findAndReplaseDialog", true);
        dialog.setWindowTitle(Application.translate("JmlEditor", "Replace") + "...");
        findDialog = dialog;
        dialog.find.connect(this, "findText()");
        dialog.replace.connect(this, "replaceText()");
        dialog.replaceAll.connect(this, "replaceTextAll()");
        dialog.exec();*/
    }
    
    @SuppressWarnings("unused")
    private void btnStackTraceClicked() {
        final StackTraceDialog stackTraceDialog=new StackTraceDialog(parent.getEnvironment(),parent,parent.userFuncLocator,null);
        stackTraceDialog.exec();
    }

   /* private void isFindTextInTag(final QTextCursor tc, boolean replace) {
        if (editText.isCursorOnTag(tc.position(), true)) {
            TagInfo tag = editText.selectTagUnderCursor(tc);
            if (replace) {
                editText.getTagConverter().deleteTag(tag);
                tc.removeSelectedText();
                tc.setCharFormat(editText.getDefaultCharFormat());
            }
        }
    }    

    @SuppressWarnings("unused")
    private void findText() {
        String findText = findDialog.getFindWhat();
        FindFlags flags = getFindOptions();
        final QTextCursor tcOld=editText.textCursor();
       
        final QTextCursor tc=editText.textCursor();
        tc.setPosition((flags.value() & FindFlag.FindBackward.value()) > 0 ? editText.toPlainText().length()-1 : 0);
        editText.setTextCursor(tc);
        parent.clearFindTextPos();
        while(editText.find(findText,flags)){
            int start=editText.textCursor().selectionStart();
            int end=editText.textCursor().selectionEnd()-start;
            parent.addFindTextPos(start,end);
        }
       
        editText.setTextCursor(tcOld);
        if (!editText.find(findText, flags)) {
            exposeMessageInformation(findText);
        } else {
            isFindTextInTag(editText.textCursor(), false);            
            editText.focusWidget();
        }
        parent.rehighlight();
 
    }

    @SuppressWarnings("unused")
    private void replaceTextAll() {
        FindFlags flags = getFindOptions();
        flags.clear(FindFlag.FindBackward);
        String findWhat = findDialog.getFindWhat();
        String replaceWith = findDialog.getReplaceWith();

        QTextCursor tc = editText.textCursor();
        tc.setPosition(0);
        editText.setTextCursor(tc);
        while (editText.find(findDialog.getFindWhat(), flags)) {
            replaceText(flags, findWhat, replaceWith);
            editText.setTextCursor(editText.textCursor());
        }
        findDialog.accept();
        editText.setCursorInEditor(editText.toPlainText().length());
        editText.setFocusInText();
    }

    @SuppressWarnings("unused")
    private void replaceText() {
        FindFlags flags = getFindOptions();
        String findWhat = findDialog.getFindWhat();
        String replaceWith = findDialog.getReplaceWith();
        if (findWhat.equals(replaceWith)) {
            return;
        }
        replaceText(flags, findWhat, replaceWith);
        if ((flags.value() & FindFlag.FindBackward.value()) > 0) {
            editText.textCursor().movePosition(MoveOperation.Left, MoveMode.MoveAnchor, replaceWith.length());
        }
        editText.setTextCursor(editText.textCursor());
        editText.focusWidget();
    }*/

    @SuppressWarnings("unused")
    private void btnImportClicked() {
        final String fileName = QFileDialog.getOpenFileName(this.parent, Application.translate("JmlEditor", "Import User Function"), "", new QFileDialog.Filter("*.xml"));
        if (fileName != null && !fileName.isEmpty()) {
            final File file = new File(fileName);
            try {
                parent.getUserFunc().importBody(file, new UICallback() {

                    @Override
                    public void noSuitableFunctionFound() {
                        Application.getInstance().getEnvironment().messageError(Application.translate("JmlEditor", "No suitable function found"));
                    }

                    @Override
                    public Id chooseId(final Map<Id, UserFunctionDefinition> id2title) {
                        final UfNameChooser chooser = new UfNameChooser(parent.getEnvironment(), id2title);
                        if (chooser.execDialog() == DialogResult.ACCEPTED) {
                            return chooser.getSelectedId();
                        } else {
                            return null;
                        }
                    }
                });
                parent.updateFuncContent();
            } catch (IOException ex) {
                Application.getInstance().getEnvironment().processException(ex);
            }
        }
    }

    @SuppressWarnings("unused")
    private void btnExportClicked() {
        final String fileName = QFileDialog.getSaveFileName(this.parent, Application.translate("JmlEditor", "Import User Function"), "", new QFileDialog.Filter("*.xml"));
        if (fileName != null && !fileName.isEmpty()) {
            final File file = new File(fileName);
            try {
                final EditorActionsProvider actProvider=parent.getActionProvider();
                parent.getUserFunc().exportBody(file,actProvider.getOwnerClassId(), actProvider.getOwnerPropId(), actProvider.getFunctionName());
            } catch (IOException ex) {
                Application.getInstance().getEnvironment().processException(ex);
            }
        }
    }

    /*private void replaceText(FindFlags flags, String findWhat, String replaceWith) {
        if (editText.textCursor().hasSelection()) {
            replaseSelectedText(replaceWith);
        } else {
            if (editText.find(findWhat, flags)) {
                replaseSelectedText(replaceWith);
            } else {
                exposeMessageInformation(findWhat);
            }
        }
    }

    private void replaseSelectedText(String replaceWith) {
        QTextCursor tc = editText.textCursor();
        try {
            editText.undoTextChange();
            editText.blockSignals(true);
            //editText.document().blockSignals(true);
            tc.beginEditBlock();

            isFindTextInTag(tc, true);
            tc.insertText(replaceWith);
        } finally {
            tc.endEditBlock();
            //editText.document().blockSignals(false);
            editText.blockSignals(false);
            editText.textChanged.emit();
        }

    }

   private FindFlags getFindOptions() {
        FindFlags flags = new FindFlags();
        if (findDialog.isWholeWordChecked()) {
            flags.set(FindFlag.FindWholeWords);
        }
        if (findDialog.isMatchCaseChecked()) {
            flags.set(FindFlag.FindCaseSensitively);
        }
        if (!findDialog.isForwardChecked()) {
            flags.set(FindFlag.FindBackward);
        }
        return flags;
    }
    
    private void exposeMessageInformation(String text) {
        String title = Application.translate("ExplorerDialog", "Information");
        String message = Application.translate("ExplorerDialog", "String \'%s\' not found");
        Application.messageInformation(title, String.format(message, text));
    }*/
        
    @SuppressWarnings("unused")
    private void btnFormatCodeClicked() {
        parent.formatCode();
    }
    
    @SuppressWarnings("unused")
    private void btnDevelopmentModeClicked(){
        parent.setDevelopmentMode(btnDevelopmentMode.isChecked());
        btnShowExecutableSrc.setVisible(btnDevelopmentMode.isChecked());
        btnShowPresentationInfo.setVisible(btnDevelopmentMode.isChecked());
        //final ClientSettings settings = parent.getEnvironment().getConfigStore();

        //String settingsKey = SettingNames.SYSTEM + "/" + "user_func_editor_is_development_mode";
        //settings.writeBoolean(settingsKey, btnDevelopmentMode.isChecked());
    }
    
    @SuppressWarnings("unused")
    private void btnShowPresentationInfo_Clicked() {
        String[] presArgs = parent.getActionProvider().getPresentationInfo();
        PresentationInfoDialog dlg = new PresentationInfoDialog(parent.getEnvironment(), Application.translate("JmlEditor", "About"), presArgs[0],presArgs[1],presArgs[2],presArgs[3], null, presArgs[4]);
        dlg.addNewRow(Application.translate("JmlEditor", "ID") + ":", presArgs[5], presArgs.length);
        dlg.exec();
    }
    
    @SuppressWarnings("unused")
    private void btnShowExecutableSrc_Clicked() {
        new PreviewExecutableSourceDialog(parent.getEnvironment(), parent, parent.getUserFunc().getJavaSourceSupport()).exec();
    }
    
    void enableAutosave(boolean isEnabled) {
        if (isEnabled && parent.getActionProvider() != null && parent.getActionProvider().getPid() != null) {
            btnAutosave.triggered.connect(this, "btnDoAutoSave_Clicked()");
            btnAutosave.setVisible(true);
        } else {
            btnAutosave.triggered.disconnect();
            btnAutosave.setVisible(false);
        }
    }
    
    @SuppressWarnings("unused")
    private void btnDoAutoSave_Clicked() {
        parent.doAutosave();
    }
    
    @SuppressWarnings("unused")
    private void btnShowAutoSaveVersions_Clicked() {
        AutosaveVersionsDialog dialog = new AutosaveVersionsDialog(parent);
        parent.pauseAutosave();
        try {
            if (dialog.execDialog() == DialogResult.ACCEPTED) {
                final JmlType jmlType = dialog.getSelectedJml();
                parent.openSourceVersion(null);
                parent.loadXmlToEditor(jmlType, ImportUserFuncDialog.ImportAction.REPLACE);
            }
        } finally {
            parent.resumeAutosave();
        }
    }
    
    @SuppressWarnings("unused")
    private void btnShowAutoSaveSettings_Clicked() {
        new AutosaveSettingsDialog(parent.getEnvironment(), parent).execDialog();
    }
    
    @SuppressWarnings("unused")
    private void btnToggleCommentClicked() {
        editText.toggleComment();
    }

    @SuppressWarnings("unused")
    private void btnShowModuleNameClicked() {
        try {
            parent.disableTrackChanges(true);
            editText.getTagConverter().setShowMode(btnShowModuleName.isChecked() ? EDefinitionDisplayMode.SHOW_FULL_NAMES : EDefinitionDisplayMode.SHOW_SHORT_NAMES);
            //editText/*.blockSignals(true);*/.setUndoRedoEnabled(false);
            editText.updateTagsName();
            parent.rehighlight();
            //editText/*.blockSignals(false);*/.setUndoRedoEnabled(true);
            final ClientSettings settings = parent.getEnvironment().getConfigStore();

            final String settingsKey = SettingNames.SYSTEM + "/" + "user_func_editor_showModuleName";
            settings.writeInteger(settingsKey, editText.getTagConverter().getShowMode().ordinal());

            this.undoEnable(false);
            this.redoEnable(false);
        } finally {
            parent.disableTrackChanges(false);
        }
    }
    
    @SuppressWarnings("unused")
    private void showOwnerEditorDialog(){
        try {
            final Id ownerClassId= parent.getUserFunc().getOwnerClassId();
            final RadClassPresentationDef ownerClassDef = parent.getEnvironment().getDefManager().getClassPresentationDef(ownerClassId);
            final Pid pid=new Pid(ownerClassDef.getTableId(),parent.getUserFunc().getOwnerPid());
            final EntityModel model;
            if(ownerClassDef.getDefaultSelectorPresentation()!=null && ownerClassDef.getDefaultSelectorPresentation().getEditorPresentationIds()!=null){
                model=EntityModel.openContextlessModel(parent.getEnvironment(), pid,ownerClassId, ownerClassDef.getDefaultSelectorPresentation().getEditorPresentationIds());  
            }else{
                model=EntityModel.openContextlessModel(parent.getEnvironment(), pid,ownerClassId, ownerClassDef.getEditorPresentationIds());  
            }
            if(model.getView()!=null){
                model.getView().close(true);
            }           
            final EntityEditorDialog dialog = new EntityEditorDialog( model);
            com.trolltech.qt.gui.QDialog.DialogCode.resolve(dialog.exec()).equals(com.trolltech.qt.gui.QDialog.DialogCode.Accepted);
        } catch (ServiceClientException ex) {
            parent.getEnvironment().processException(ex);
        } catch (InterruptedException ex) {
        }
    }

    private void buttonsEnabled(final boolean state) {
        btnCopy.setEnabled(state);
        if (!editText.isReadOnly()) {
            btnCut.setEnabled(state);
        }
    }

    public QToolBar getToolBar() {
        return toolBar;
    }
    
    void setTextEditor(final XscmlEditor textEditor){
        editText=textEditor;        
    }
}
