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

import com.trolltech.qt.core.QRegExp;
import com.trolltech.qt.core.Qt.ContextMenuPolicy;
import com.trolltech.qt.core.Qt.FocusPolicy;
import com.trolltech.qt.core.Qt.Orientation;
import com.trolltech.qt.core.Qt.ShortcutContext;
import com.trolltech.qt.gui.QTextCursor.MoveMode;
import com.trolltech.qt.gui.*;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.check.RadixProblem.ESeverity;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.mask.EditMaskStr;
import org.radixware.kernel.common.client.meta.sqml.ISqmlDefinition;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.views.IDialog;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.defs.ads.userfunc.AdsUserFuncDef;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.enums.EDialogIconType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.jml.Jml;
import org.radixware.kernel.common.jml.JmlTagLocalizedString;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.editors.jmleditor.JmlProblemList.ProblemItem;
import org.radixware.kernel.explorer.editors.jmleditor.dialogs.ImportUserFuncDialog;
import org.radixware.kernel.explorer.editors.jmleditor.dialogs.ImportUserFuncDialog.ImportAction;
import org.radixware.kernel.explorer.editors.jmleditor.dialogs.ProfileDialog;
import org.radixware.kernel.explorer.editors.valeditors.ValStrEditor;
import org.radixware.kernel.explorer.editors.xscmleditor.Highlighter;
import org.radixware.kernel.explorer.editors.xscmleditor.TagInfo;
import org.radixware.kernel.explorer.editors.xscmleditor.XscmlEditor;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.env.ExplorerSettings;
import org.radixware.kernel.explorer.views.MainWindow;
import org.radixware.kernel.explorer.views.Splitter;
import org.radixware.schemas.adsdef.AdsUserFuncDefinitionDocument;
import org.radixware.schemas.adsdef.LocalizedString;
import org.radixware.schemas.adsdef.LocalizingBundleDefinition;
import org.radixware.schemas.udsdef.UdsDefinitionDocument;
import org.radixware.schemas.udsdef.UdsDefinitionListDocument;
import org.radixware.schemas.xscml.JmlType;

public final class JmlEditor extends MainWindow {

    private XscmlEditor editText;
    private final JmlProcessor converter;
    private Highlighter hightlighter;
    private AdsUserFuncDef userFunc;
    private final Splitter splitter;
    private EditToolBar editToolBar;
    private VersionsToolBar versionsToolBar;
    private ToolBar toolBar;
    //private final QLineEdit edDescription = new QLineEdit();
    private long userFuncId;
    IUserFuncLocator userFuncLocator;
    private ValStrEditor profileLine;
    private EditorActionsProvider actionProvider;
    private boolean isReadOnly = false;
    private QLabel lbDescription;
    //private QToolBar toolBarDescription;
    private Map<RadixProblem, TagInfo> validationProblemList = new HashMap<>();
    private boolean isCurSrcVersionUnChecked = false;//true - текущая версия редактировалась, и после этого не было компиляции
    private final BracketHighlighter bracketHightlighter;
    private SearchPanel searchPanel;
    private boolean isDevelopmentMode = false;
    private boolean isNew;
    private final QToolButton btnEditProfile;
    private final static String NOT_DEFENED = "<not defened>";

    public static final class JmlEditorIcons extends ExplorerIcon.CommonOperations {

        private JmlEditorIcons(final String fileName) {
            super(fileName, true);
        }
        public static final JmlEditorIcons IMG_ADD_TAG_ID = new JmlEditorIcons("classpath:images/id.svg");
        public static final JmlEditorIcons IMG_ADD_TAG_INVOCATION = new JmlEditorIcons("classpath:images/invocation.svg");
        public static final JmlEditorIcons IMG_ADD_TAG_DATA_TYPE = new JmlEditorIcons("classpath:images/dataType.svg");
        public static final JmlEditorIcons IMG_ADD_TAG_DB_OBJECT = new JmlEditorIcons("classpath:images/db_object.svg");
        public static final JmlEditorIcons IMG_ADD_TAG_UF_OWNER = new JmlEditorIcons("classpath:images/db_owner.svg");
        public static final JmlEditorIcons IMG_COMPLETION = new JmlEditorIcons("classpath:images/complete.svg");
        public static final JmlEditorIcons IMG_VALIDATE = new JmlEditorIcons("classpath:images/validate.svg");
        public static final JmlEditorIcons IMG_REPLACE = new JmlEditorIcons("classpath:images/replace.svg");
        public static final JmlEditorIcons IMG_MODULE_NAME = new JmlEditorIcons("classpath:images/model_name.svg");
        public static final JmlEditorIcons IMG_DEVELOPMENT_MODE = new JmlEditorIcons("classpath:images/srcedit.svg");
        public static final JmlEditorIcons IMG_ERROR = new JmlEditorIcons("classpath:images/error.svg");
        public static final JmlEditorIcons IMG_XML = new JmlEditorIcons("classpath:images/xml.svg");
        public static final JmlEditorIcons IMG_CLASS = new JmlEditorIcons("classpath:images/class.svg");
        public static final JmlEditorIcons IMG_DOMAIN = new JmlEditorIcons("classpath:images/domain.svg");
        public static final JmlEditorIcons IMG_METHOD = new JmlEditorIcons("classpath:images/method.svg");
        public static final JmlEditorIcons IMG_PROP = new JmlEditorIcons("classpath:images/prop.svg");
        public static final JmlEditorIcons IMG_CONSTSET_INT = new JmlEditorIcons("classpath:images/constSet_int.svg");
        public static final JmlEditorIcons IMG_CONSTSET_STR = new JmlEditorIcons("classpath:images/constSet_str.svg");
        public static final JmlEditorIcons IMG_EXPORT = new JmlEditorIcons("classpath:images/export_u.svg");
        public static final JmlEditorIcons IMG_IMPORT = new JmlEditorIcons("classpath:images/import_u.svg");
        public static final JmlEditorIcons IMG_OK = new JmlEditorIcons("classpath:images/ok.svg");
        public static final JmlEditorIcons CREATE_SOURCE_VERSION = new JmlEditorIcons("classpath:images/newsrc.svg");
        public static final JmlEditorIcons GO_TO_OBJECT = new JmlEditorIcons("classpath:images/go_to_object.svg");
        public static final JmlEditorIcons STACK_TRACE = new JmlEditorIcons("classpath:images/stack.svg");
        public static final JmlEditorIcons TAG_DB_NAME = new JmlEditorIcons("classpath:images/db_name.svg");
        public static final JmlEditorIcons LIB_USER_FUNC = new JmlEditorIcons("classpath:images/userfunc.svg");
        public static final JmlEditorIcons TAG_INDEX_DB_NAME = new JmlEditorIcons("classpath:images/index.svg");
        public static final JmlEditorIcons TAG_LOCALIZED_STR = new JmlEditorIcons("classpath:images/open_document.svg");
        public static final JmlEditorIcons VOID = new JmlEditorIcons("classpath:images/void.svg");
        public static final JmlEditorIcons CLOSE = new JmlEditorIcons("classpath:images/close.svg");
    }

    public interface EditorActionsProvider {

        void saveChanges();

        boolean applyChanges(JmlEditor editor, boolean needCompile);

        void compile(IProblemHandler handler, boolean isEdited, boolean forSaving);

        void reread();

        boolean isValid();

        Id getOwnerClassId();

        Id getOwnerPropId();

        String getFunctionName();

        String getSourceVersionName();

        void openSourceVersion(JmlEditor editor, String sourceVersionName);

        void saveSourceVersions(List<String> changedSourceVersions);

        void setDisplayProfile(String sProfile);
        
        void updateLibUserFuncName();

        void setDescription(String description);

        String getObjectFromOptimizerCache(String key);

        Map<String, String> getAllowedLibNames2PipelineId();
    }

    public interface IPostOpenAction {

        void perform(IUserFuncLocator locator);
    }

    /**
     * Bridge to ads layer
     */
    public interface IUserFuncLocator {

        /**
         * Returns and userFunc definition for userfunc object with given pid
         */
        AdsUserFuncDef findUserFunc(long id, String sourceVersionName);

        String getUserFuncDisplayName(long id);

        boolean enshureObjectIsOpened(long id, String sourceVersionName, boolean showModalEditor, IPostOpenAction action);

        String getCurrentVersionName();

        void moveCursorToProblem(ProblemItem pi);

        void moveCursorToPosition(int[] pi);

        JmlEditor getEditorIfAny();
    }

    /**
     * @param parent родительский виджет
     */
    public JmlEditor(IClientEnvironment environment, QWidget parent) {
        //super(parent,new WindowType[]{WindowType.Widget});
        this.setParent(parent);
        btnEditProfile = new QToolButton();

        this.environment = environment;
        splitter = new Splitter(this, (ExplorerSettings) environment.getConfigStore());
        this.setVisible(false);
        this.setContextMenuPolicy(ContextMenuPolicy.NoContextMenu);

        if (parent != null) {
            if (!(parent.layout() instanceof QVBoxLayout)) {
                QVBoxLayout mainLayout = new QVBoxLayout();
                parent.setLayout(mainLayout);
            }
            parent.layout().setMargin(0);
            parent.layout().addWidget(this);
        }
        //QWidget centralWidget=new QWidget(this);
        //QVBoxLayout editorLayout = new QVBoxLayout();
        //editorLayout.setMargin(0);
        converter = new JmlProcessor(this);
        createEditor();
        bracketHightlighter = new BracketHighlighter(editText, hightlighter);
        bracketHightlighter.init();
        //editorLayout.addWidget(splitter);
        //editorLayout.addWidget(searchPanel);
        setSearchPanelVisible(false);
        //centralWidget.setLayout(editorLayout);
        this.setCentralWidget(splitter);
        //this.show();
    }

    public final void setSearchPanelVisible(boolean isVisible) {
        searchPanel.setVisible(isVisible);
        if (!isVisible) {
            setReplacePanelVisible(false);
            setSearchText(null);
        } else {
            QTextCursor tc = editText.textCursor();
            if (tc.hasSelection()) {
                String selectedText = tc.selectedText();
                searchPanel.setSearchingText(selectedText);
                //setSearchText(selectedText);
            }
            searchPanel.setFocusInSearchEditor();
        }
    }

    public final void setReplacePanelVisible(boolean isVisible) {
        searchPanel.setReplacePanelVisible(isVisible);
    }

    public void setPositionAndHighlitAsError(int start, int end) {
        editText.blockSignals(true);
        try {
            hightlighter.hightlightStackLine(end);
        } finally {
            editText.blockSignals(false);
        }
        editText.setFocus();
    }

    public void setPosition(int pos) {
        editText.setCursorInEditor(pos);
        editText.setFocus();
    }

    public void setPosition(int line, int pos) {
        if (line < 0 || pos < 0) {
            return;
        }

        if (line == 0) {
            QTextBlock textBlock = editText.document().begin();
            if (textBlock.length() > pos) {
                setPosition(pos);
            }
        } else {
            int posInEditor = pos;
            QTextBlock textBlock = editText.document().begin();
            while (textBlock != null && textBlock.isValid() && line > textBlock.blockNumber()) {
                posInEditor += textBlock.length();
                textBlock = textBlock.next();
            }
            if (textBlock != null && textBlock.isValid() && line == textBlock.blockNumber() && textBlock.length() > pos) {
                setPosition(posInEditor);
            }
        }
    }

    public boolean switchToContext(long id, String sourceVersionName, IPostOpenAction action) {
        if (id != userFuncId) {
            if (!(sourceVersionName == null && getEditingVersionName() == null
                    || sourceVersionName != null && sourceVersionName.equals(getEditingVersionName()))) {
                openSourceVersion(sourceVersionName);
            }
            return true;
        }
        return userFuncLocator.enshureObjectIsOpened(id, sourceVersionName, false, action);
    }

    public void moveCursorToProblem(ProblemItem item) {
        int[] posInfo = item == null ? null : item.getPosInfo(this);
        if (posInfo == null) {
            return;
        }
        int endErrPos = posInfo[1];
        editText.setCursorInEditor(endErrPos);
        editText.setFocus();
    }
    private final IClientEnvironment environment;

    public IClientEnvironment getEnvironment() {
        return environment;
    }
    private JmlProblemList problemList;

    public void open(IUserFuncLocator ufLocator, long ufId, EditorActionsProvider actionProvider, JmlProblemList problemList) {
        this.setFocusPolicy(FocusPolicy.StrongFocus);
        openingEditor = true;
        this.problemList = problemList;

        this.actionProvider = actionProvider;
        this.userFuncLocator = ufLocator;
        boolean isAnotherFunc = userFuncId != ufId;
        if (getEditingVersionName() == null && isCurSrcVersionUnChecked && !isAnotherFunc) {//если текущая версия редактировалась, и после этого не было компиляции
            this.toolBar.setCheckButtonState(false);
        } else {
            this.toolBar.setCheckButtonState();
        }
        this.userFuncId = ufId;

        hightlighter.updateSettings();

        if (actionProvider != null) {
            this.userFunc = ufLocator.findUserFunc(ufId, actionProvider.getSourceVersionName());

            updateProfileString(false);
            converter.open(userFunc.getSource());
            validationProblemList.clear();

            editText.open(userFunc);

            Set<ProblemItem> problems = problemList.getProblems(ufId);
            if (problems != null && !problems.isEmpty()) {
                hightlightErrorsInTextEditor(problems);
            }

            hightlighter.clearBracketIndex();
            hightlighter.rehighlight();
            editToolBar.open(isAnotherFunc);
            editToolBar.setEnabledForBtnPast(editText.canPaste());
            versionsToolBar.updateSourceVersionEditLine();
        }

        openingEditor = false;
        editToolBar.undoEnable(false);
        editText.setCursorInEditor(0);
        setEnabledToBtnSave(false);

    }

    public XscmlEditor getTextEditor() {
        return editText;
    }

    public void updateUndoRedoBtns() {
        editToolBar.updateUndoRedoBtns();
    }

    public void setPastEnabled() {
        editToolBar.setEnabledForBtnPast(true);
    }

    @SuppressWarnings("unused")
    private void checkPastOnMlStrings(JmlType jmlType) {
        try {
            long anotherFuncId = Long.parseLong(jmlType.getDescription());
            if (anotherFuncId != userFuncId) {
                AdsUserFuncDef uf = userFuncLocator.findUserFunc(anotherFuncId, null);
                if (uf != null) {
                    for (JmlType.Item item : jmlType.getItemList()) {
                        if (item.isSetLocalizedString()) {
                            Id strId = Id.Factory.loadFrom(item.getLocalizedString().getStringId());
                            AdsMultilingualStringDef src = uf.findLocalizedString(strId);
                            if (src != null && userFunc.findLocalizingBundle() != null) {
                                AdsMultilingualStringDef str = src.cloneString(userFunc.findLocalizingBundle());
                                item.getLocalizedString().setStringId(str.getId().toString());
                            }
                        }
                    }
                }
            }
        } catch (NumberFormatException ex) {
        }
    }

    public AdsUserFuncDef getUserFunc() {
        return userFunc;
    }

    public long getUserFuncPid() {
        return userFuncId;
    }

    @SuppressWarnings("unused")
    private void updateEditorHightlight() {
        if (hightlighter != null) {
            try {
                editText.blockSignals(true);
                hightlighter.updateSettings();
                editText.updateTagsFormat();
            } finally {
                editText.blockSignals(false);
            }
        }
    }

    public void setSearchText(QRegExp getExp) {
        hightlighter.addSearchText(getExp);
        rehighlight();
    }

    public void rehighlight() {
        try {
            editText.blockSignals(true);
            hightlighter.rehighlight();
        } finally {
            editText.blockSignals(false);
        }
    }

    private void createEditor() {
        //     edDescription.textChanged.connect(this, "descriptionChange()");
        ((Application) getEnvironment().getApplication()).getActions().settingsChanged.connect(this, "updateEditorHightlight()");
        splitter.setOrientation(Orientation.Vertical);
        createEditText();
        createToolBar();
    }
    private boolean openingEditor = true;
    private boolean srcWasChange = false;
    private boolean isSaveEnabled = false;
    private boolean disableTrackChanges = false;

    @SuppressWarnings("unused")
    private void textChange() {
        //editText.document().modificationChanged.emit(true);
        if (!disableTrackChanges && !isReadOnly && !openingEditor) {
            toolBar.setCheckButtonState(false);
            if (getEditingVersionName() == null) {
                isCurSrcVersionUnChecked = true;
            }
            setEnabledToBtnSave(true);
            if (!srcWasChange) {
                actionProvider.saveChanges();
            }
            //saveUserFunc(false, false);           
            srcWasChange = true;
            //bracketHightlighter.clearHightlightBracket(editText.textCursor());  
        }
    }

    public boolean isDisableTrackChanges() {
        return disableTrackChanges;
    }

    public void disableTrackChanges(boolean disableTrackChanges) {
        this.disableTrackChanges = disableTrackChanges;
    }
    
    void setEnabledToBtnSave(boolean enable) {
        isSaveEnabled = enable;
    }

    public boolean isSaveEnabled() {
        return isSaveEnabled;
    }

    public void setSaveEnabled(boolean enabled) {
        setEnabledToBtnSave(enabled);
    }

    private void createToolBar() {
        toolBar = new ToolBar(this);
        this.addToolBar(toolBar.getToolBar());
        editToolBar = new EditToolBar(this);
        this.addToolBar(editToolBar.getToolBar());
        versionsToolBar = new VersionsToolBar(this);
        this.addToolBar(versionsToolBar.getToolBar());
        this.addToolBarBreak();
        //profileLine = new QLineEdit();
        EditMaskStr editMask = new EditMaskStr();
        editMask.setNoValueStr(NOT_DEFENED);
        profileLine = new ValStrEditor(getEnvironment(), this, editMask, true, isReadOnly);

        btnEditProfile.setObjectName("btnEditReturnType");
        btnEditProfile.setText("...");
        btnEditProfile.clicked.connect(this, "btnEditProfile_Clicked()");
        profileLine.addButton(btnEditProfile);
        profileLine.setReadOnly(true);
        profileLine.setValue("");
        //editLine.home(true);

//        edDescription.setReadOnly(false);
//        edDescription.setText("");
//        edDescription.home(true);
//        edDescription.setVisible(false);
        profileLine.setVisible(false);
        /*QToolBar newToolBar = new QToolBar();

         QWidget widget = new QWidget(this);
         QGridLayout layout = new QGridLayout(widget);
         widget.setLayout(layout);
         newToolBar.setMovable(false);
         layout.addWidget(new QLabel(Application.translate("JmlEditor", "Profile: "), this), 0, 0);
         layout.addWidget(lbDescription = new QLabel(Application.translate("JmlEditor", "Description").trim() + ": ", this), 1, 0);
         layout.addWidget(profileLine, 0, 1);
         layout.addWidget(edDescription, 1, 1);
         newToolBar.addWidget(widget);
         this.addToolBarBreak();
         this.addToolBar(newToolBar);*/

    }

    @SuppressWarnings("unused")
    private void btnEditProfile_Clicked() {
        ProfileDialog editProfileDialog = new ProfileDialog(this);
        if (editProfileDialog.exec() == 1) {
            setEnabledToBtnSave(true);
            actionProvider.updateLibUserFuncName();
            srcWasChange = true;
            updateProfileString(true);
        }
    }

    public IDialog.DialogResult editProfile() {
        ProfileDialog editProfileDialog = new ProfileDialog(this);
        if (editProfileDialog.execDialog() == IDialog.DialogResult.ACCEPTED) {
            setEnabledToBtnSave(true);
            actionProvider.updateLibUserFuncName();
            srcWasChange = true;
            updateProfileString(true);
            return IDialog.DialogResult.ACCEPTED;
        } else {
            return IDialog.DialogResult.REJECTED;
        }
    }

    public void updateProfileString(boolean update) {
        AdsMethodDef.Profile profile = userFunc.findProfile();
        AdsUserFuncDef.LibFuncNameGetter libFuncNameGetter = userFunc.getLibName();
        String libName = libFuncNameGetter == null ? null : libFuncNameGetter.getLibFuncName();
        String profileString = profile == null ? NOT_DEFENED
                : libName == null
                        ? profile.getName() : profile.getNameForSelector(libName);
        profileLine.setValue(profileString);
        if (update) {
            actionProvider.setDisplayProfile(profileString);
        }
        btnEditProfile.setVisible(userFunc.isFreeForm());
    }

    public String getProfileString() {
        return profileLine.getValue();
    }

    /* private String calcProfileName(AdsMethodDef.Profile profile){
     StringBuilder sProfile = new StringBuilder();

     if (profile.getReturnValue() != null) {
     sProfile.append(profile.getReturnValue().getType().getQualifiedName(userFunc));
     sProfile.append(' ');
     }

     sProfile.append(userFunc.getName());

     sProfile.append('(');

     profile.getParametersList().printProfileString(sProfile, true);

     sProfile.append(')');

     profile.getThrowsList().printSignatureString(profile);

     return sProfile.toString();
        
     }*/
    /*private QToolBar createEditLineInToolBar(boolean readOnly, String text, String lbText, QWidget editLine) {

     QLabel lb = new QLabel(lbText);
     QToolBar newToolBar = new QToolBar();

     newToolBar.setMovable(false);
     newToolBar.addWidget(lb);
     newToolBar.addWidget(editLine);
     return newToolBar;
     }*/
    public void saveSourceVersions(List<String> changedSourceVersions) {
        actionProvider.saveSourceVersions(changedSourceVersions);
    }

    void clearErrorListFromOldVersions(List<String> oldSourceVersions) {
        if (oldSourceVersions != null) {
            for (String name : oldSourceVersions) {
                problemList.clear(userFuncId, name);
            }
        }
    }

    public boolean saveUserFunc(boolean needCompile, boolean needApply) {
        if (needApply && userFunc.isFreeForm() && userFunc.findMethod() != null) {
            String methodName = userFunc.findMethod().getName();
            if (methodName == null || methodName.isEmpty()) {
                Application.messageError(Application.translate("JmlEditor", "Profile is not defened!"));
                return false;
            }
        }
        QTextCursor tc = editText.textCursor();
        try {
            if ((actionProvider != null) && (!isReadOnly)) {
                boolean wasCompiled = false;
                if (needCompile) {
                    isCurSrcVersionUnChecked = false;
                    if (!actionProvider.isValid() || srcWasChange) {
                        compileUserFunc(true, true);
                        wasCompiled = true;
                        if (!actionProvider.isValid() && !isDevelopmentMode) {
                            String message = Application.translate("JmlEditor", "Function is not compiled. Do you really want to save it?");
                            Set<EDialogButtonType> buttons = new HashSet<>();
                            buttons.add(EDialogButtonType.OK);
                            buttons.add(EDialogButtonType.CANCEL);
                            if (Application.messageBox("Confirmation", message, EDialogIconType.QUESTION, buttons) == EDialogButtonType.CANCEL) {
                                return false;
                            }
                        }
                    }
                } else if (srcWasChange) {
                    userFunc.getSource().getItems().clear();
                    converter.toXml(editText.toPlainText(), tc);
                }
                int pos = tc.position();
                actionProvider.saveChanges();
                if (needApply) {
                    //compileUserFunc();//для перехода по ошибкам в редакторе
                    boolean res = actionProvider.applyChanges(this, !wasCompiled);
                    //if (res && !actionProvider.isValid()) {
                    //    compileUserFunc(false, false);//для перехода по ошибкам в редакторе
                    //}

                    if (pos > 0 && editText.toPlainText().length() > pos) {
                        tc.setPosition(pos);
                        editText.setTextCursor(tc);
                    }

                    return res;
                }
                srcWasChange = false;
                return true;
            }
        } finally {
            tc.dispose();
        }
        return true;
    }

    private String getTextFromFile(String filename) {
        //String res = "";
        StringBuilder sb = new StringBuilder();
        try {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "UTF-8"))) {
                String str;
                while ((str = in.readLine()) != null) {
                    sb.append(str).append("\n");
                    //res += str + "\n";
                }
            }
        } catch (FileNotFoundException e) {
            String title = Application.translate("ExplorerDialog", "Error!");
            String message = Application.translate("ExplorerDialog", "File \'%s\' not found");
            Application.messageException(title, String.format(message, filename), e);
            Logger.getLogger(JmlEditor.class.getName()).log(Level.SEVERE, null, e);
        } catch (IOException ex) {
            String title = Application.translate("ExplorerDialog", "Error!");
            String message = Application.translate("ExplorerDialog", "IOException");
            Application.messageException(title, String.format(message, filename), ex);
            Logger.getLogger(JmlEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sb.toString();
    }

    /* private final Charset UTF8_CHARSET = Charset.forName("UTF-8");
    
     private String decodeUTF8(byte[] bytes) {
     return new String(bytes, UTF8_CHARSET);
     }
     private byte[] encodeUTF8(String string) {
     return string.getBytes(UTF8_CHARSET);
     }*/
    public void readTextFromFile(String filename) {
        if (filename == null || filename.isEmpty()) {
            return;
        }
        String res = getTextFromFile(filename);
        JmlType jmlTypeFromFile = getJmlType(res);
        String description = getDescription(res);
        if (jmlTypeFromFile != null) {
//            if (userFuncPid == null) {
//                loadXmlToEditor(jmlTypeFromFile, ImportAction.REPLACE);
//            } else {
            ImportUserFuncDialog dialog = new ImportUserFuncDialog(this, jmlTypeFromFile);
            if (dialog.exec() == 1) {
                boolean isSaveAsNewSrcVersion = dialog.getIsSaveAsNewSrcVersion();
                ImportAction importAction = dialog.getImportAction();
                if (isSaveAsNewSrcVersion) {
                    String srcVersionName = dialog.getSrcVersionName();
                    createNewSrcVersion(srcVersionName, true);
                }
                JmlType jmlType = dialog.getJmlType();
                loadXmlToEditor(jmlType, importAction);

                compileUserFunc(true, false);
                actionProvider.setDescription(description);
            }
            //  }
        }
    }

    public void loadXmlToEditor(JmlType jmlType, ImportAction importAction) {
        QTextCursor tc = editText.textCursor();
        try {
            tc.setPosition(0);
            tc.movePosition(QTextCursor.MoveOperation.Right, MoveMode.KeepAnchor, editText.toPlainText().length());

            editText.setUndoRedoEnabled(false);
            editText.strToHtml(jmlType, tc, getEditingVersionName());
            editText.setUndoRedoEnabled(true);
            converter.clearHystory();
            editToolBar.undoEnable(false);
            editToolBar.redoEnable(false);
            editText.setTextCursor(tc);
        } finally {
            tc.endEditBlock();
            tc.dispose();
        }
    }

    private JmlType getJmlType(String str) {
        JmlType jmlType = null;
        try {
            AdsUserFuncDefinitionDocument doc = AdsUserFuncDefinitionDocument.Factory.parse(str);
            jmlType = doc.getAdsUserFuncDefinition().getSource();//JmlType.Factory.parse(str);  

            LocalizingBundleDefinition xStrings = doc.getAdsUserFuncDefinition().getStrings();
            if (xStrings != null) {
                for (LocalizedString xString : xStrings.getStringList()) {
                    AdsMultilingualStringDef mlStringdef = AdsMultilingualStringDef.Factory.loadFrom(xString);
                    userFunc.findLocalizingBundle().getStrings().getLocal().add(mlStringdef);
                }
            }
        } catch (XmlException e) {
            try {
                UdsDefinitionDocument doc = UdsDefinitionDocument.Factory.parse(str);
                if (doc.getUdsDefinition() != null && doc.getUdsDefinition().getUserFunc() != null) {
                    jmlType = doc.getUdsDefinition().getUserFunc().getSource();
                }
            } catch (XmlException ex) {
                try {
                    UdsDefinitionListDocument doc = UdsDefinitionListDocument.Factory.parse(str);
                    if (doc.getUdsDefinitionList() != null && doc.getUdsDefinitionList().getUdsDefinitionList() != null) {
                        for (UdsDefinitionDocument.UdsDefinition def : doc.getUdsDefinitionList().getUdsDefinitionList()) {
                            if (def.getUserFunc() != null) {
                                jmlType = def.getUserFunc().getSource();
                                break;
                            }
                        }
                    }
                } catch (XmlException ex1) {
                    try {
                        XmlObject xObj = XmlObject.Factory.parse(str);
                        if (xObj instanceof JmlType) {
                            jmlType = JmlType.Factory.parse(str);
                        }
                    } catch (XmlException e1) {
                        environment.messageError(Application.translate("JmlEditor", "Can't load user function from file!"));
                    }
                }
            }
        }
        if (jmlType == null) {
            environment.messageError(Application.translate("JmlEditor", "Can't load user function from file!"));
            //jmlType = JmlType.Factory.newInstance();
            //jmlType.addNewItem().setJava(str);
        }
        return jmlType;
    }

    private String getDescription(String str) {
        String description = null;
        try {
            AdsUserFuncDefinitionDocument doc = AdsUserFuncDefinitionDocument.Factory.parse(str);
            description = doc.getAdsUserFuncDefinition().getDescription();//JmlType.Factory.parse(str);  

            LocalizingBundleDefinition xStrings = doc.getAdsUserFuncDefinition().getStrings();
            if (xStrings != null) {
                for (LocalizedString xString : xStrings.getStringList()) {
                    AdsMultilingualStringDef mlStringdef = AdsMultilingualStringDef.Factory.loadFrom(xString);
                    userFunc.findLocalizingBundle().getStrings().getLocal().add(mlStringdef);
                }
            }
        } catch (XmlException e) {
            try {
                UdsDefinitionDocument doc = UdsDefinitionDocument.Factory.parse(str);
                if (doc.getUdsDefinition() != null && doc.getUdsDefinition().getUserFunc() != null) {
                    description = doc.getUdsDefinition().getUserFunc().getDescription();
                }
            } catch (XmlException ex) {
                try {
                    UdsDefinitionListDocument doc = UdsDefinitionListDocument.Factory.parse(str);
                    if (doc.getUdsDefinitionList() != null && doc.getUdsDefinitionList().getUdsDefinitionList() != null) {
                        for (UdsDefinitionDocument.UdsDefinition def : doc.getUdsDefinitionList().getUdsDefinitionList()) {
                            if (def.getUserFunc() != null) {
                                description = def.getUserFunc().getDescription();
                                break;
                            }
                        }
                    }
                } catch (XmlException ex1) {

                }
            }
        }
        return description;
    }

    /* public void saveUserFuncToFile1(String filename){
     if(filename==null)return;
     JmlType xDef= JmlType.Factory.newInstance();
     userFunc.getSource().appendTo(xDef, ESaveMode.NORMAL);
     try {
     BufferedWriter out = new BufferedWriter(new FileWriter(filename));
     StringWriter w = new StringWriter();
     xDef.save(w);
     out.write(w.toString());
     out.close();
     } catch (IOException e){
     Logger.getLogger(JmlEditor.class.getName()).log(Level.SEVERE, null, e);
     }
     }*/

    /* private String createDoc() throws ParserConfigurationException, TransformerConfigurationException, TransformerException{
     DocumentBuilderFactory factory  = DocumentBuilderFactory.newInstance();
     DocumentBuilder builder = factory.newDocumentBuilder();
     DOMImplementation impl = builder.getDOMImplementation();
    
     Document doc = impl.createDocument(null,null,null);
     Element e1 =doc.createElement("source");
     doc.appendChild(e1);
     e1.setTextContent(userFunc.getSource().toString());//setNodeValue(userFunc.getSource().toString());
    
     //Element e2 = doc.createElement("java");
     //e1.appendChild(e2);
    
     //e2.setAttribute("url","http://www.rgagnon.com/howto.html");
    
    
     // transform the Document into a String
     DOMSource domSource = new DOMSource(doc);
     TransformerFactory tf = TransformerFactory.newInstance();
     Transformer transformer = tf.newTransformer();
     //transformer.setOutputProperty
     //    (OutputKeys.OMIT_XML_DECLARATION, "yes");
     transformer.setOutputProperty(OutputKeys.METHOD, "xml");
     transformer.setOutputProperty(OutputKeys.ENCODING,"UTF-8");
     transformer.setOutputProperty
     ("{http://xml.apache.org/xslt}indent-amount", "4");
     transformer.setOutputProperty(OutputKeys.INDENT, "yes");
     java.io.StringWriter sw = new java.io.StringWriter();
     StreamResult sr = new StreamResult(sw);
     transformer.transform(domSource, sr);
     String xml = sw.toString();
     return xml;
     }*/
    public void saveUserFuncToFile(String filename) {
        if (filename == null || filename.isEmpty()) {
            return;
        }
        //saveUserFunc(false, false);
        /*try {
         createDoc();
         } catch (ParserConfigurationException ex) {
         Logger.getLogger(JmlEditor.class.getName()).log(Level.SEVERE, null, ex);
         } catch (TransformerConfigurationException ex) {
         Logger.getLogger(JmlEditor.class.getName()).log(Level.SEVERE, null, ex);
         } catch (TransformerException ex) {
         Logger.getLogger(JmlEditor.class.getName()).log(Level.SEVERE, null, ex);
         }*/
        AdsUserFuncDefinitionDocument.AdsUserFuncDefinition xUfDef = AdsUserFuncDefinitionDocument.AdsUserFuncDefinition.Factory.newInstance();
        userFunc.getSource().getItems().clear();
        converter.toXml(editText.toPlainText(), editText.textCursor());
        userFunc.appendTo(xUfDef);
        xUfDef.setOwnerClassId(actionProvider.getOwnerClassId());
        xUfDef.setPropId(actionProvider.getOwnerPropId());
        xUfDef.setName(actionProvider.getFunctionName());
        if (this.userFunc != null) {
            xUfDef.setDescription(this.userFunc.getName());
        }
        AdsUserFuncDefinitionDocument doc = AdsUserFuncDefinitionDocument.Factory.newInstance();
        doc.setAdsUserFuncDefinition(xUfDef);
        try {
            /*DOMWriter writer = new org.apache.xml.serialize.XMLSerializer();
             writer.setNewLine("\r\n");
             writer.setEncoding("UTF-8");
             writer.writeNode(new FileOutputStream(new File("outputFile.xml")), myDocument);*/

            //BufferedWriter out = new BufferedWriter(new FileWriter(filename));
            StringWriter w = new StringWriter();
            doc.save(w);
            String string = w.toString();
            //byte[] utf8 = string.getBytes("UTF-8");
            try (Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), "UTF-8"))) {
                out.write(string);
            }
        } catch (FileNotFoundException e) {
            String title = Application.translate("ExplorerDialog", "Error!");
            String message = Application.translate("ExplorerDialog", "File \'%s\' not found");
            Application.messageException(title, String.format(message, filename), e);
            Logger.getLogger(JmlEditor.class.getName()).log(Level.SEVERE, null, e);
        } catch (IOException ex) {
            String title = Application.translate("ExplorerDialog", "Error!");
            String message = Application.translate("ExplorerDialog", "IOException");
            Application.messageException(title, String.format(message, filename), ex);
            Logger.getLogger(JmlEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void createEditText() {//throws XmlException, SQLException {
        QWidget editorPanel = new QWidget(this);
        QVBoxLayout editorLayout = new QVBoxLayout();
        editorLayout.setMargin(0);

        editText = new XscmlEditor(getEnvironment(), converter, this);
        hightlighter = new Highlighter(getEnvironment(), editText, converter, "org.radixware.explorer/S_E/SYNTAX_JML/");
        editText.textChanged.connect(this, "textChange()");
        editText.pastEnabled.connect(this, "setPastEnabled()");
        editText.updateUndoRedoBtns.connect(this, "updateUndoRedoBtns()");
        editText.checkPastOnMlStrings.connect(this, "checkPastOnMlStrings(JmlType)");
        editText.cursorPositionChanged.connect(hightlighter, "clearLineHighlight()");

        searchPanel = new SearchPanel(this);
        searchPanel.setObjectName("SearchPanel");
        //editText.setStyleSheet("selection-background-color: green;");
        //splitter.addWidget(editText);
        editorLayout.addWidget(editText);
        editorLayout.addWidget(searchPanel);
        editorPanel.setLayout(editorLayout);
        splitter.addWidget(editorPanel);

        QAction insertDataType = new QAction(this);
        insertDataType.setShortcut("Ctrl+D");
        insertDataType.setShortcutContext(ShortcutContext.WidgetShortcut);
        editText.addAction(insertDataType);
        editText.setMinimumHeight(5);
    }

    public void hightlightErrors(Set<ProblemItem> problemSet) {
        editText.clearHightlightError();
        QTextCursor tc = editText.textCursor();
        try {
            int pos = tc.position();
            //tc.blockSignals(true);            
            if (problemSet != null && !problemSet.isEmpty()) {
                hightlightErrorsInTextEditor(problemSet);
            }
            //tc.blockSignals(false);
            //   calcErrorListSize();
            tc.setPosition(pos);
            editText.setTextCursor(tc);
            editText.setFocus();
        } finally {
            tc.dispose();
        }
    }

    /*public void calcErrorListSize() {
     List<Integer> listSize = new ArrayList<>();
     int fontSize = errorList.font().pointSize() == -1 ? errorList.font().pixelSize() : errorList.font().pointSize();
     int size = 10 + fontSize * 2 * errorList.getItemCount();
     size = Math.min(size, splitter.size().height() / 2);
     listSize.add(splitter.size().height() * 2 / 3);
     listSize.add(size);
     splitter.setSizes(listSize);
     }*/
    private void hightlightErrorsInTextEditor(Set<ProblemItem> problemSet) {
        for (ProblemItem item : problemSet) {
            //RadixProblem rpt = item.ggetProblem();
            //problems.add(rpt);
            calcErrorPosInEditor(item);
            int[] posInfo = item.getPosInfo(this);
            if (posInfo != null) {
                int startErrPos = posInfo[0];
                int endErrPos = posInfo[1];

                if (startErrPos > 0 && endErrPos > 0) {
                    ESeverity severity = item.getSeverity();
                    editText.hightlightError(startErrPos, endErrPos, severity);
                }
            }
        }
    }

    private void calcErrorPosInEditor(ProblemItem item) {
        int[] posInfo = item.getPosInfo(this);
        if (posInfo == null) {
            return;
        }
        int startErrPos = posInfo[0];
        int endErrPos = posInfo[1];
        final QTextCursor tc = editText.textCursor();
        try {
            if (endErrPos > editText.toPlainText().length()) {
                endErrPos = editText.toPlainText().length();
            }
            if (startErrPos >= endErrPos) {
                startErrPos = posInfo[0] == posInfo[1] ? endErrPos - 1 : startErrPos - (posInfo[1] - posInfo[0]);
                if (startErrPos >= 0 && startErrPos <= getTextEditor().toPlainText().length()) {
                    tc.setPosition(startErrPos);
                    tc.movePosition(QTextCursor.MoveOperation.Right, MoveMode.KeepAnchor);
                    while ((tc.selectedText().equals("") || (tc.selectedText().equals("\n"))) && (!tc.atStart()) && (!tc.atEnd())) {
                        startErrPos = startErrPos - 1;
                        tc.setPosition(startErrPos);
                        tc.movePosition(QTextCursor.MoveOperation.Right, MoveMode.KeepAnchor);
                    }
                }
            }
            if (editText.isCursorOnTag(startErrPos, false)) {
                tc.setPosition(startErrPos);
                TagInfo tag = editText.getTagUnderCursor(tc.position());
                if (tag != null) {
                    startErrPos = (int) tag.getStartPos() - 1;
                }
            }
            if (editText.isCursorOnTag(endErrPos, false)) {
                tc.setPosition(endErrPos);
                TagInfo tag = editText.getTagUnderCursor(tc.position());
                if (tag != null) {
                    endErrPos = (int) tag.getEndPos() - 1;
                }
            }
            item.setPosInfo(startErrPos, endErrPos);
        } finally {
            tc.dispose();
        }
    }

    public void compileUserFunc(boolean isEdited, boolean forSaving) {
        if (actionProvider != null) {
            problemList.clear(userFuncId);
            QTextCursor tc = editText.textCursor();
            int pos = tc.position();
            try {
                editText.document().setUndoRedoEnabled(false);
                editText.blockSignals(true);
                editText.document().blockSignals(true);
                tc.beginEditBlock();
                if (srcWasChange) {
                    userFunc.getSource().getItems().clear();
                    converter.toXml(editText.toPlainText(), tc);
                    actionProvider.saveChanges();
                    srcWasChange = false;
                }
                actionProvider.compile(problemList.getProblemHandler(), isEdited, forSaving);
                if (getEditingVersionName() == null) {
                    setEnabledToBtnSave(true);
                    isCurSrcVersionUnChecked = false;
                }

                for (RadixProblem problem : validationProblemList.keySet()) {
                    problemList.acceptProblem(userFuncId, problem);
                }
                problemList.fillErrorList(userFuncId);
                //errorList.fillValidationProblemItems( userFuncLocator,  userFuncPid);
                hightlightErrors(problemList.getProblems(userFuncId));
                converter.clearHystory();
            } finally {
                tc.endEditBlock();
                editText.document().blockSignals(false);
                editText.blockSignals(false);
                editText.document().setUndoRedoEnabled(true);
                tc.dispose();
                setPosition(pos);
                toolBar.setCheckButtonState();
            }
        }
    }

    public Map<RadixProblem, TagInfo> getValidProblemList() {
        return validationProblemList;
    }

    /*public void addValidateProblem(String mess, TagInfo notValidTag) {
     //Pid pid = new Pid(userFunc.getId(), userFunc.getId().toString());
     RadixProblem problem = RadixProblem.Factory.newError(userFunc, mess);
     validationProblemList.put(problem, notValidTag);
     errorList.acceptProblem(userFuncLocator, userFuncPid, problem);
     errorList.fillErrorList(userFuncPid);
     errorList.setVisible(true);
     }*/
    /*public void updateValidationProblem() {
     Map<RadixProblem, TagInfo> res = new HashMap<>();
     res.putAll(validationProblemList);
     validationProblemList.clear();
     for (RadixProblem problem : res.keySet()) {
     JmlTag problemTag = (JmlTag) res.get(problem);
     //validationProblemList.remove(problem);
     for (TagInfo tag : converter.getCurrentTagList()) {
     if (((JmlTag) tag).getTag().equals(problemTag.getTag())) {
     validationProblemList.put(problem, tag);
     }
     }
     }
     }*/
    public void rereadUserFunc() {
        String message = Application.translate("Editor", "Reread Data in Editor" + "?");
        if (actionProvider != null && Application.messageConfirmation(message)) {
            actionProvider.reread();
            this.open(userFuncLocator, userFuncId, this.actionProvider, this.problemList);
        }
    }

    public boolean isValid() {
        if (actionProvider != null) {
            return actionProvider.isValid();
        }
        return false;
    }
    
    private int getPosForAddTag() {
        return editText.textCursor().selectionStart(); //if no selection return position()
    } 

    public void addJmlTagId(AdsDefinition selectedDef) {
        //editText.prepareInsertNewTag();
        TagInfo tag = converter.createTagId(selectedDef, getPosForAddTag(), userFunc.getSource().getItems()/*, invocate,newItem,(int)tagType*/);
        editText.insertTag(tag, "");
    }

    public void addJmlTagLocalizedStr(final JmlTagLocalizedString jmlTag) {
        TagInfo tag = converter.createTagLocalizedStr(jmlTag, getPosForAddTag(), userFunc.getSource().getItems()/*, invocate,newItem,(int)tagType*/);
        editText.insertTag(tag, "");
    }

    public void addJmlTagId(Id[] ids, boolean isDeprecated) {
        //if(ids.length>0 && ids[0]!=null && ids[0].getPrefix()!=EDefinitionIdPrefix.DDS_TABLE)
        //    ids[0]=Id.Factory.changePrefix(ids[0], EDefinitionIdPrefix.DDS_TABLE);
        TagInfo tag = converter.createTagId(ids, getPosForAddTag(), isDeprecated, userFunc.getSource().getItems()/*, invocate,newItem,(int)tagType*/);
        editText.insertTag(tag, "");
    }

    public void addJmlTagInvocate(AdsDefinition selectedDef) {
        //editText.prepareInsertNewTag();
        TagInfo tag = converter.createTagInvocate(selectedDef, getPosForAddTag(), userFunc.getSource().getItems()/*, invocate,newItem,(int)tagType*/);
        String suffix = selectedDef instanceof AdsMethodDef ? "()" : "";
        editText.insertTag(tag, suffix);
    }

    public void addJmlTagInvocate(AdsDefinition selectedDef, List<String> params) {
        //editText.prepareInsertNewTag();
        TagInfo tag = converter.createTagInvocate(selectedDef, getPosForAddTag(), userFunc.getSource().getItems());
        editText.insertTag(tag, "");
    }

    public void addJmlTagType(RadixObject selectedDef, EValType valType) {
        //editText.prepareInsertNewTag();
        TagInfo tag = converter.createTagType(selectedDef, getPosForAddTag(), userFunc.getSource().getItems(), valType/*, invocate,newItem,(int)tagType*/);
        editText.insertTag(tag, "");
    }

    public void addJmlTagDbName(ISqmlDefinition selectedDef, boolean isDeprecated) {
        TagInfo tag = converter.createTagDbName(selectedDef, getPosForAddTag(), isDeprecated, userFunc.getSource().getItems()/*, invocate,newItem,(int)tagType*/);
        editText.insertTag(tag, "");
    }

    public void addJmlTagDbEntity(EntityModel selectedEntity) {
        //editText.prepareInsertNewTag();
        TagInfo tag = converter.createTagDbEntity(selectedEntity.getClassId(), selectedEntity.getPid().toString(), getPosForAddTag(), userFunc.getSource().getItems(), false);
        if (tag != null) {
            editText.insertTag(tag, "");
        }
    }

    public void addJmlTagUfOwner() {
        //editText.prepareInsertNewTag();
        if (userFunc.getOwnerClassId() != null) {
            TagInfo tag = converter.createTagDbEntity(userFunc.getOwnerClassId(),/*getClassPresentationDef().getTableId()*/ userFunc.getOwnerPid(), getPosForAddTag(), userFunc.getSource().getItems()/*, invocate,newItem,(int)tagType*/, true);
            if (tag != null) {
                editText.insertTag(tag, "");
            }
        } else {
            String message = Application.translate("JmlEditor", "User function owner class Id is null");
            String title = Application.translate("JmlEditor", "Can't insert tag!");
            Application.messageError(title, message);
        }
    }

//    @SuppressWarnings("unused")
//    private void descriptionChange() {
//        if (userFunc != null && edDescription.text() != null) {
//            userFunc.setName(edDescription.text());
//            setEnabledToBtnSave(true);
//            int pos = edDescription.cursorPosition();
//            actionProvider.saveChanges();
//            if ((edDescription.text() != null) && (edDescription.text().length() > pos)) {
//                edDescription.setCursorPosition(pos);
//            }
//        }
//    }
//    public void updateDescription(String text) {
//        edDescription.setText(text);
//        descriptionChange();
//    }
//
//    public String getDescription() {
//        return edDescription.text();
//    }
//
//    public void setDescription(String name) {
//        edDescription.setText(name);
//        //descriptionChange();
//    }
    public void setTitleVisible(boolean isVisible) {
//        edDescription.setVisible(isVisible);
//        lbDescription.setVisible(isVisible);
    }

    public void showCompleter() {
        editText.setFocus();
        editText.exposeCompleter();
    }

    public JmlProcessor getJmlProcessor() {
        return converter;
    }

    public boolean isReadOnly() {
        return isReadOnly;
    }

    public void setReadOnlyMode(boolean isReadOnly) {
        this.isReadOnly = isReadOnly;
        editText.setReadOnly(isReadOnly);
        toolBar.setReadOnlyMode(isReadOnly);
        editToolBar.setReadOnlyMode(isReadOnly);
        versionsToolBar.setReadOnlyMode(isReadOnly);
//        edDescription.setReadOnly(isReadOnly);
    }

    public void openSourceVersion(String versionName) {
        if (actionProvider != null /*&& Application.messageConfirmation(message)*/) {
            actionProvider.openSourceVersion(this, versionName);
            srcWasChange = true;
            //errorList.clear(userFuncPid);
            //errorList.setVisible(false);
            //errorList.setMinimumHeight(5);
        }
    }

    public void removeSourceVersion(String removedSrcVersionName) {
        saveSourceVersions(null);
        setEnabledToBtnSave(true);
//        errorList.removeSourceVersionProblems(removedSrcVersionName, userFuncPid);
    }

    void updateFuncContent() {
        openingEditor = true;
        //editingVersionName=versionName;
        converter.open(userFunc.getSource());
        validationProblemList.clear();//clear "Can not actualize entity" problems
        editText.open(userFunc);
        openingEditor = false;
        srcWasChange = true;
    }

    public EditorActionsProvider getActionProvider() {
        return actionProvider;
    }

    public String getEditingVersionName() {
        if (actionProvider != null) {
            return actionProvider.getSourceVersionName();
        }
        return null;
    }

    public AdsUserFuncDef findUserFunc(String versName) {
        return userFuncLocator.findUserFunc(userFuncId, versName);
    }

    public boolean wasSrcChanged() {
        return srcWasChange;
    }

    @Override
    protected void closeEvent(final QCloseEvent closeEvent) {
        ((Application) getEnvironment().getApplication()).getActions().settingsChanged.disconnect();
        if (toolBar != null) {
            toolBar.close();
        }
        /*if (errorList != null) {
         errorList.clear(null);
         }*/
        actionProvider = null;
        if (converter != null) {
            List<TagInfo> currentTagList = converter.getCurrentTagList();
            if (currentTagList != null && !currentTagList.isEmpty()) {
                currentTagList.clear();
            }
            //converter.clearHystory();
        }
        super.closeEvent(closeEvent);
    }

    public void setDevelopmentMode(boolean isDevelopmentMode) {
        this.isDevelopmentMode = isDevelopmentMode;
    }

    void createNewSrcVersion(String name, boolean isCopySource) {
        saveUserFunc(false, false);
        String editingVersiomName = getEditingVersionName();
        AdsUserFuncDef curUserFunc = findUserFunc(null);
        Jml source = isCopySource ? curUserFunc.getSource(editingVersiomName) : null;
        String userName = getEnvironment().getUserName();
        curUserFunc.addSourceVersion(name, userName, source);
        //List<String> changedSrcVersions = new ArrayList<>();
        //changedSrcVersions.add(name);
        saveSourceVersions(null);
        //actionProvider.saveChanges();
    }

    void closeSearchPanel() {
        editToolBar.closeSearchPanel();
    }
}
