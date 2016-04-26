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

import com.trolltech.qt.core.QDir;
import com.trolltech.qt.core.Qt.ShortcutContext;
import com.trolltech.qt.core.Qt.ToolButtonStyle;
import com.trolltech.qt.gui.QFileDialog;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QKeySequence;
import com.trolltech.qt.gui.QShortcut;
import com.trolltech.qt.gui.QToolBar;
import com.trolltech.qt.gui.QToolButton;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.meta.RadClassPresentationDef;
import org.radixware.kernel.common.client.meta.RadSelectorPresentationDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlColumnDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlDefinition;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableIndexDef;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.widgets.actions.Action;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.userfunc.AdsUserFuncDef.Lookup.DefInfo;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.dialogs.SelectEntityDialog;
import org.radixware.kernel.explorer.editors.jmleditor.dialogs.ChooseObjectDialog;
import org.radixware.kernel.explorer.editors.jmleditor.dialogs.InvocateWizard;
import org.radixware.kernel.explorer.editors.jmleditor.dialogs.LibUserFuncWizard;
import org.radixware.kernel.explorer.editors.jmleditor.dialogs.LocalizedStr_Dialog;
import org.radixware.kernel.explorer.editors.jmleditor.dialogs.TypeWizard;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.utils.WidgetUtils;
import org.radixware.kernel.explorer.widgets.ExplorerAction;
import org.radixware.kernel.explorer.widgets.ExplorerMenu;


public class ToolBar {

    private final JmlEditor editor;
    private final QToolButton btnSaveToFile = new QToolButton();
    private final QToolButton btnLoadFromFile = new QToolButton();
    private final QToolButton btnGetId = new QToolButton();
    private final QToolButton btnClassInvocation = new QToolButton();
    private final QToolButton btnType = new QToolButton();
    private final QToolButton btnDbEntity = new QToolButton();
    private final QToolButton btnUfOwner = new QToolButton();
    private final QToolButton btnDbNames = new QToolButton();
    private final QToolButton btnLibUserFunc = new QToolButton();
    private final QToolButton btnLocalizedStr = new QToolButton();
    private final QToolButton btnCompletion = new QToolButton();
    private final QToolButton btnCheck = new QToolButton();
    private final ExplorerMenu menuGetId;
    private final ExplorerMenu menuDbNames;
    //Menu Actions
    private ExplorerAction getClassIds;
    private ExplorerAction getConstSetIds;
    private ExplorerAction getRoleIds;
    private ExplorerAction getPropIds;
    private ExplorerAction getMethodIds;
    private ExplorerAction getTableIds;
    private ExplorerAction getColumnIds;
    private ExplorerAction getDbFuncIds;
    private ExplorerAction getTableIndexIds;
    private ExplorerAction getDomainIds;
    private ExplorerAction getTables;
    private ExplorerAction getColumns;
    private ExplorerAction getDbFuncs;
    private ExplorerAction getTableIndexs;
    private QToolBar toolBarWidget;
    
    public final static String NOT_COMPILED = Application.translate("JmlEditor", "Not Compiled");
    // private QShortcut saveShortcut;

    public ToolBar(JmlEditor editor) {
        this.editor = editor;
        createActionsForMenuId();
        menuGetId = createMenuId();
        menuDbNames = createMenuDbName();
        createToolBar();
    }

    public void setReadOnlyMode(boolean isReadOnlyMode) {
        btnSaveToFile.setEnabled(true);
        btnLoadFromFile.setEnabled(!isReadOnlyMode);
        btnGetId.setEnabled(!isReadOnlyMode);
        btnClassInvocation.setEnabled(!isReadOnlyMode);
        btnType.setEnabled(!isReadOnlyMode);
        btnDbEntity.setEnabled(!isReadOnlyMode);
        btnDbNames.setEnabled(!isReadOnlyMode);
        btnUfOwner.setEnabled(!isReadOnlyMode);
        btnLibUserFunc.setEnabled(!isReadOnlyMode);
        btnLocalizedStr.setEnabled(!isReadOnlyMode);

        btnCompletion.setEnabled(!isReadOnlyMode);
        btnCheck.setEnabled(!isReadOnlyMode);

    }

    private void createToolBar() {
        toolBarWidget = new QToolBar(editor);
        toolBarWidget.setObjectName("JmlEditorToolBar");

        btnGetId.setObjectName("btnGetId");
        btnGetId.setToolTip(Application.translate("JmlEditor", "Identifier"));
        btnGetId.setIcon(ExplorerIcon.getQIcon(JmlEditor.JmlEditorIcons.IMG_ADD_TAG_ID));
        btnGetId.setMenu(menuGetId);
        btnGetId.clicked.connect(this, "btnGetId_Clicked()");

        btnClassInvocation.setObjectName("btnClassInvocation");
        btnClassInvocation.setToolTip(Application.translate("JmlEditor", "Class Invocation") + " (Ctrl+Alt+I)");
        btnClassInvocation.setIcon(ExplorerIcon.getQIcon(JmlEditor.JmlEditorIcons.IMG_ADD_TAG_INVOCATION));
        btnClassInvocation.clicked.connect(this, "btnClassInvoc_Clicked()");
        createShortCut("Ctrl+Alt+I", "btnClassInvoc_Clicked()");

        btnType.setObjectName("btnType");
        btnType.setToolTip(Application.translate("JmlEditor", "Type") + " (Ctrl+Alt+Y)");
        btnType.setIcon(ExplorerIcon.getQIcon(JmlEditor.JmlEditorIcons.IMG_ADD_TAG_DATA_TYPE));
        btnType.clicked.connect(this, "btnType_Clicked()");
        createShortCut("Ctrl+Alt+Y", "btnType_Clicked()");

        btnDbEntity.setObjectName("btnDbEntity");
        btnDbEntity.setToolTip(Application.translate("JmlEditor", "Database Object"));
        btnDbEntity.setIcon(ExplorerIcon.getQIcon(JmlEditor.JmlEditorIcons.IMG_ADD_TAG_DB_OBJECT));
        btnDbEntity.clicked.connect(this, "btnDbEntity_Clicked()");

        btnUfOwner.setObjectName("btnUfOwner");
        btnUfOwner.setToolTip(Application.translate("JmlEditor", "User-Defined Function Owner"));
        btnUfOwner.setIcon(ExplorerIcon.getQIcon(JmlEditor.JmlEditorIcons.IMG_ADD_TAG_UF_OWNER));
        btnUfOwner.clicked.connect(this, "btnUfOwner_Clicked()");

        btnDbNames.setObjectName("btnDbNames");
        btnDbNames.setToolTip(Application.translate("JmlEditor", "Definition Database Name"));
        btnDbNames.setIcon(ExplorerIcon.getQIcon(JmlEditor.JmlEditorIcons.TAG_DB_NAME));        
        btnDbNames.setMenu(menuDbNames);
        btnDbNames.clicked.connect(this, "btnDbNames_Clicked()");

        btnLibUserFunc.setObjectName("btnLibUserFunc");
        btnLibUserFunc.setToolTip(Application.translate("JmlEditor", "Library User-Defined Function"));
        btnLibUserFunc.setIcon(ExplorerIcon.getQIcon(JmlEditor.JmlEditorIcons.LIB_USER_FUNC));
        //btnLibUserFunc.setMenu(menuDbNames);
        btnLibUserFunc.clicked.connect(this, "btnLibUserFunc_Clicked()");

        btnLocalizedStr.setObjectName("btnLocalizedStr");
        btnLocalizedStr.setToolTip(Application.translate("JmlEditor", "Localized String"));
        btnLocalizedStr.setIcon(ExplorerIcon.getQIcon(JmlEditor.JmlEditorIcons.TAG_LOCALIZED_STR));
        btnLocalizedStr.clicked.connect(this, "btnLocalizedStr_Clicked()");

        btnCompletion.setObjectName("btnCompletion");
        btnCompletion.setToolTip(Application.translate("JmlEditor", "Show Completion") + " (Ctrl+\" \")");
        btnCompletion.setIcon(ExplorerIcon.getQIcon(JmlEditor.JmlEditorIcons.IMG_COMPLETION));
        btnCompletion.clicked.connect(this, "btnCompletion_Clicked()");

        btnCheck.setObjectName("btnCheck");
        btnCheck.setToolTip(Application.translate("JmlEditor", "Check") + " (F9)");
        btnCheck.setIcon(ExplorerIcon.getQIcon(JmlEditor.JmlEditorIcons.IMG_VALIDATE));
        btnCheck.clicked.connect(this, "btnCheck_Clicked()");
        btnCheck.setToolButtonStyle(ToolButtonStyle.ToolButtonTextBesideIcon);
        createShortCut("F9", "btnCheck_Clicked()");

        btnLoadFromFile.setObjectName("btnLoadFromFile");
        btnLoadFromFile.setToolTip(Application.translate("JmlEditor", "Load from File"));
        btnLoadFromFile.setIcon(ExplorerIcon.getQIcon(JmlEditor.JmlEditorIcons.IMG_IMPORT));
        btnLoadFromFile.clicked.connect(this, "btnLoadFromFile_Clicked()");

        btnSaveToFile.setObjectName("btnSaveToFile");
        btnSaveToFile.setToolTip(Application.translate("JmlEditor", "Save to File"));
        btnSaveToFile.setIcon(ExplorerIcon.getQIcon(JmlEditor.JmlEditorIcons.IMG_EXPORT));
        btnSaveToFile.clicked.connect(this, "btnSaveToFile_Clicked()");

        toolBarWidget.addWidget(btnGetId);
        toolBarWidget.addWidget(btnClassInvocation);
        toolBarWidget.addWidget(btnType);
        toolBarWidget.addWidget(btnDbEntity);
        toolBarWidget.addWidget(btnUfOwner);
        toolBarWidget.addWidget(btnDbNames);
        toolBarWidget.addWidget(btnLibUserFunc);
        toolBarWidget.addWidget(btnLocalizedStr);
        toolBarWidget.addSeparator();
        toolBarWidget.addWidget(btnCompletion);
        toolBarWidget.addSeparator();
        toolBarWidget.addWidget(btnCheck);
        //  toolBarWidget.addWidget(btnSave);
//        toolBarWidget.addWidget(btnReread);
        toolBarWidget.addSeparator();
        toolBarWidget.addWidget(btnLoadFromFile);
        toolBarWidget.addWidget(btnSaveToFile);
    }

    /*private String chooseFile(QFileDialog.FileMode fileMode, QFileDialog.AcceptMode acceptMode, String title) {
     return QFileDialog.getSaveFileName(editor,title,QDir.homePath(),new com.trolltech.qt.gui.QFileDialog.Filter("XML Files (*.xml)"));
     //QFileDialog dialog = new QFileDialog(editor, title, QDir.homePath());
     //dialog.setFileMode(fileMode);
     //dialog.setFilter("XML Files (*.xml)");
     //dialog.setAcceptMode(acceptMode);
     //if (QDialog.DialogCode.resolve(dialog.exec()).equals(QDialog.DialogCode.Accepted)){
     //    return dialog.selectedFiles().get(0);
     //} else {
     //    return null;
     //}
     }*/
    private QShortcut createShortCut(String strKeySequence, String method) {
        QShortcut shortcut = new QShortcut(new QKeySequence(strKeySequence), editor.getTextEditor());
        shortcut.setContext(ShortcutContext.WindowShortcut);
        shortcut.activated.connect(this, method);
        return shortcut;
    }

    public void setCheckButtonState() {
        setCheckButtonState(editor.isValid());
    }

    public void setCheckButtonState(boolean isCompiled) {
        boolean isCurrentSourceVersion = editor.getEditingVersionName() == null;
        QIcon icon = isCompiled && isCurrentSourceVersion ? ExplorerIcon.getQIcon(JmlEditor.JmlEditorIcons.IMG_OK) : ExplorerIcon.getQIcon(JmlEditor.JmlEditorIcons.IMG_VALIDATE);
        btnCheck.setIcon(icon);
        String text = "";
        if (isCurrentSourceVersion) {
            text = isCompiled ? Application.translate("JmlEditor", "Compiled") : NOT_COMPILED;
        }
        btnCheck.setText(text);
    }

    public void setRereadEnabled(boolean enabled) {
//        btnReread.setEnabled(enabled);
    }

    public void setInsertOwnerTagEnabled(boolean isNew) {
        btnUfOwner.setEnabled(!isNew);
    }

    private ExplorerMenu createMenuId() {
        ExplorerMenu menu = new ExplorerMenu(btnGetId);
        menu.addAction((Action) getClassIds);
        menu.addAction((Action) getConstSetIds);
        menu.addAction((Action) getRoleIds);
        menu.addAction((Action) getPropIds);
        menu.addAction((Action) getMethodIds);
        menu.addAction((Action) getTableIds);
        menu.addAction((Action) getColumnIds);
        menu.addAction((Action) getDbFuncIds);
        menu.addAction((Action) getTableIndexIds);
        menu.addAction((Action) getDomainIds);
        menu.addSeparator();
        return menu;
    }

    private ExplorerMenu createMenuDbName() {
        ExplorerMenu menu = new ExplorerMenu(btnDbNames);
        menu.addAction((Action) getTables);
        menu.addAction((Action) getColumns);
        menu.addAction((Action) getDbFuncs);
        menu.addAction((Action) getTableIndexs);
        menu.addSeparator();
        return menu;
    }

    /*private ImageManager getImageManager() {
        return (ImageManager) editor.getEnvironment().getApplication().getImageManager();
    }*/

    private void createActionsForMenuId() {
        QIcon icon = ExplorerIcon.getQIcon(ClientIcon.Definitions.CLASS);//getImageManager().loadSvgIcon("classpath:images/class.svg", QColor.transparent);
        getClassIds = new ExplorerAction(icon, Application.translate("JmlEditor", "Class") + "...", editor);
        getClassIds.triggered.connect(this, "getClassIds()");

        icon = ExplorerIcon.getQIcon(ClientIcon.Definitions.CONSTSET);//getImageManager().loadSvgIcon("classpath:images/constSet.svg", QColor.transparent);
        getConstSetIds = new ExplorerAction(icon, Application.translate("JmlEditor", "Enumeration") + "...", editor);
        getConstSetIds.triggered.connect(this, "getConstSetIds()");

        icon = ExplorerIcon.getQIcon(ClientIcon.Definitions.ROLE);//getImageManager().loadSvgIcon("classpath:images/role.svg", QColor.transparent);
        getRoleIds = new ExplorerAction(icon, Application.translate("JmlEditor", "Role") + "...", editor);
        getRoleIds.triggered.connect(this, "getRoleIds()");

        icon = ExplorerIcon.getQIcon(ClientIcon.Definitions.PROPERTY);//getImageManager().loadSvgIcon("classpath:images/prop.svg", QColor.transparent);
        getPropIds = new ExplorerAction(icon, Application.translate("JmlEditor", "Property") + "...", editor);
        getPropIds.triggered.connect(this, "getPropIds()");

        icon = ExplorerIcon.getQIcon(ClientIcon.Definitions.METHOD);//getImageManager().loadSvgIcon("classpath:images/prop.svg", QColor.transparent);
        getMethodIds = new ExplorerAction(icon, Application.translate("JmlEditor", "Method") + "...", editor);
        getMethodIds.triggered.connect(this, "getMethodIds()");

        icon = ExplorerIcon.getQIcon(ClientIcon.Definitions.TABLE_ID);//ExplorerIcon.getQIcon(SqmlEditor.SqmlEditorIcons.Definitions.TABLE);
        getTableIds = new ExplorerAction(icon, Application.translate("JmlEditor", "Table") + "...", editor);
        getTableIds.triggered.connect(this, "getTableIds_Clicked()");

        icon = ExplorerIcon.getQIcon(ClientIcon.Definitions.COLUMN);//ExplorerIcon.getQIcon(SqmlEditor.SqmlEditorIcons.IMG_INSERT_COLUMN);
        getColumnIds = new ExplorerAction(icon, Application.translate("JmlEditor", "Column") + "...", editor);
        getColumnIds.triggered.connect(this, "getColumnIds_Clicked()");

        icon = ExplorerIcon.getQIcon(ClientIcon.Definitions.SQL_FUNCTION);//ExplorerIcon.getQIcon(SqmlEditor.SqmlEditorIcons.Definitions.SQL_FUNCTION);
        getDbFuncIds = new ExplorerAction(icon, Application.translate("JmlEditor", "Function") + "...", editor);
        getDbFuncIds.triggered.connect(this, "getDbFuncIds_Clicked()");

        icon = ExplorerIcon.getQIcon(JmlEditor.JmlEditorIcons.TAG_INDEX_DB_NAME);
        getTableIndexIds = new ExplorerAction(icon, Application.translate("JmlEditor", "Index") + "...", editor);
        getTableIndexIds.triggered.connect(this, "getTableIndexIds_Clicked()");

        icon = ExplorerIcon.getQIcon(ClientIcon.Definitions.TABLE);//ExplorerIcon.getQIcon(SqmlEditor.SqmlEditorIcons.Definitions.TABLE);
        getTables = new ExplorerAction(icon, Application.translate("JmlEditor", "Table") + "...", editor);
        getTables.triggered.connect(this, "getTables_Clicked()");

        icon = ExplorerIcon.getQIcon(ClientIcon.Definitions.COLUMN);//ExplorerIcon.getQIcon(SqmlEditor.SqmlEditorIcons.IMG_INSERT_COLUMN);
        getColumns = new ExplorerAction(icon, Application.translate("JmlEditor", "Column") + "...", editor);
        getColumns.triggered.connect(this, "getColumns_Clicked()");

        icon = ExplorerIcon.getQIcon(ClientIcon.Definitions.SQL_FUNCTION);//ExplorerIcon.getQIcon(SqmlEditor.SqmlEditorIcons.Definitions.SQL_FUNCTION);
        getDbFuncs = new ExplorerAction(icon, Application.translate("JmlEditor", "Function") + "...", editor);
        getDbFuncs.triggered.connect(this, "getDbFuncs_Clicked()");

        icon = ExplorerIcon.getQIcon(JmlEditor.JmlEditorIcons.TAG_INDEX_DB_NAME);
        getTableIndexs = new ExplorerAction(icon, Application.translate("JmlEditor", "Index") + "...", editor);
        getTableIndexs.triggered.connect(this, "getTableIndexs_Clicked()");

        icon = ExplorerIcon.getQIcon(ClientIcon.Definitions.DOMAIN);//getImageManager().loadSvgIcon("classpath:images/domain.svg", QColor.transparent);
        getDomainIds = new ExplorerAction(icon, Application.translate("JmlEditor", "Domain") + "...", editor);
        getDomainIds.triggered.connect(this, "getDomainIds()");
    }
    private String filename = QDir.homePath();

    @SuppressWarnings("unused")
    private void btnSaveToFile_Clicked() {
        String title = Application.translate("JmlEditor", "Save to File");
        String name = QFileDialog.getSaveFileName(editor, title, filename/*QDir.homePath()*/, new com.trolltech.qt.gui.QFileDialog.Filter("XML Files (*.xml)"));//chooseFile(QFileDialog.FileMode.AnyFile, QFileDialog.AcceptMode.AcceptSave, "Export to File");
        filename = name != null && name.isEmpty() ? name : filename;
        editor.saveUserFuncToFile(name);
    }

    @SuppressWarnings("unused")
    private void btnLoadFromFile_Clicked() {
        String title = Application.translate("JmlEditor", "Load from File");
        String name = QFileDialog.getOpenFileName(editor, title, filename/* QDir.homePath()*/, new com.trolltech.qt.gui.QFileDialog.Filter("XML Files (*.xml)"));//chooseFile(QFileDialog.FileMode.ExistingFile, QFileDialog.AcceptMode.AcceptOpen, "Import from File");
        filename = name != null && name.isEmpty() ? name : filename;
        editor.readTextFromFile(name);
    }

    @SuppressWarnings("unused")
    private void btnGetId_Clicked() {
        btnGetId.showMenu();
    }

    @SuppressWarnings("unused")
    private void btnDbNames_Clicked() {
        btnDbNames.showMenu();
    }

    @SuppressWarnings("unused")
    private void btnLibUserFunc_Clicked() {
        showLibUserFunc();
    }

    //Ids
    @SuppressWarnings("unused")
    private void getClassIds() {
        showChoceObjectId(EDefType.CLASS, Application.translate("JmlEditor", "Choose Class ID"), null);
    }

    @SuppressWarnings("unused")
    private void getRoleIds() {
        showChoceObjectId(EDefType.ROLE, Application.translate("JmlEditor", "Choose Role ID"), null);
    }

    @SuppressWarnings("unused")
    private void getConstSetIds() {
        showChoceObjectId(EDefType.ENUMERATION, Application.translate("JmlEditor", "Choose Enumeration Id"), null);
    }

    @SuppressWarnings("unused")
    private void getPropIds() {
        showChoceObjectId(EDefType.CLASS, Application.translate("JmlEditor", "Choose Property ID"), AdsPropertyDef.class);
    }

    @SuppressWarnings("unused")
    private void getMethodIds() {
        showChoceObjectId(EDefType.CLASS, Application.translate("JmlEditor", "Choose Method ID"), AdsMethodDef.class);
    }

    @SuppressWarnings("unused")
    private void getTableIds_Clicked() {
        ISqmlTableDef table = editor.getJmlProcessor().chooseSqmlTable(null, editor.isReadOnly(), editor, false);        
        if (table != null) {
            List<Id> ids = new ArrayList<>();
            ids.addAll(Arrays.<Id>asList(table.getIdPath()));
            if(!ids.get(0).getPrefix().equals(EDefinitionIdPrefix.DDS_TABLE)){
                Id tableId = Id.Factory.changePrefix(table.getId(), EDefinitionIdPrefix.DDS_TABLE);
                ids.clear();
                ids.add(tableId);                          
            }
            Id arr[] = new Id[ids.size()];
            arr = ids.toArray(arr);
            editor.addJmlTagId(arr, table.isDeprecated());
        }
    }

    @SuppressWarnings("unused")
    private void getColumnIds_Clicked() {
        ISqmlColumnDef column = editor.getJmlProcessor().chooseSqmlColumn(null, null, editor.isReadOnly(), editor);
        if (column != null) {
            editor.addJmlTagId(column.getIdPath(), column.isDeprecated());
        }
    }

    @SuppressWarnings("unused")
    private void getDbFuncIds_Clicked() {
        ISqmlDefinition dbFunc = editor.getJmlProcessor().chooseDbFunc(null, editor.isReadOnly(), editor, true);
        if (dbFunc != null) {
            editor.addJmlTagId(dbFunc.getIdPath(), dbFunc.isDeprecated());
        }
    }

    @SuppressWarnings("unused")
    private void getTableIndexIds_Clicked() {
        ISqmlTableIndexDef index = editor.getJmlProcessor().chooseSqmlIndex(null, null, editor.isReadOnly(), editor);
        if (index != null) {
            editor.addJmlTagId(index.getIdPath(), index.isDeprecated());
        }
    }

    @SuppressWarnings("unused")
    private void getDomainIds() {
        showChoceObjectId(EDefType.DOMAIN, Application.translate("JmlEditor", "Choose Domain ID"), null);
    }

    //ClassInvocation
    @SuppressWarnings("unused")
    private void btnClassInvoc_Clicked() {
        showChoceObjectInvocate();
    }

    //Type
    @SuppressWarnings("unused")
    private void btnType_Clicked() {
        showChoceType();
    }

    //DbEntity
    @SuppressWarnings("unused")
    private void btnDbEntity_Clicked() {
        showChoceDbEntity();
    }

    @SuppressWarnings("unused")
    private void btnUfOwner_Clicked() {
        editor.addJmlTagUfOwner();
    }

    //LocalizedString
    @SuppressWarnings("unused")
    private void btnLocalizedStr_Clicked() {
        LocalizedStr_Dialog dialog = new LocalizedStr_Dialog(editor.getTextEditor(), null, editor.getEnvironment().getMessageProvider().translate("SqmlEditor", "Edit Localized String"), editor.getUserFunc());
        if (dialog.exec() == 1) {
            editor.addJmlTagLocalizedStr(dialog.getTag());
        }
    }

    //DbNames
    @SuppressWarnings("unused")
    private void getTables_Clicked() {
        ISqmlTableDef table = editor.getJmlProcessor().chooseSqmlTable(null, editor.isReadOnly(), editor, false);
        if (table != null) {
            editor.addJmlTagDbName(table, table.isDeprecatedDdsDef());
        }
    }

    @SuppressWarnings("unused")
    private void getColumns_Clicked() {
        ISqmlColumnDef column = editor.getJmlProcessor().chooseSqmlColumn(null, null, editor.isReadOnly(), editor);
        if (column != null) {
            editor.addJmlTagDbName(column, column.isDeprecatedDdsDef());
        }
    }

    @SuppressWarnings("unused")
    private void getDbFuncs_Clicked() {
        ISqmlDefinition dbFunc = editor.getJmlProcessor().chooseDbFunc(null, editor.isReadOnly(), editor, true);
        if (dbFunc != null) {
            editor.addJmlTagDbName(dbFunc, dbFunc.isDeprecated());
        }
        /*EDefinitionDisplayMode showMode=editor.getJmlProcessor().getShowMode();
         final DbFuncCall_Dialog dialog = 
         new DbFuncCall_Dialog(editor.getEnvironment(), showMode, false, editor);
         if (dialog.exec()==QDialog.DialogCode.Accepted.value()){            
             
         }*/
    }

    @SuppressWarnings("unused")
    private void getTableIndexs_Clicked() {
        ISqmlTableIndexDef index = editor.getJmlProcessor().chooseSqmlIndex(null, null, editor.isReadOnly(), editor);
        if (index != null) {
            editor.addJmlTagDbName(index, index.isDeprecated());
        }
    }

    //////////////////////////////////////////////////////////////////
    @SuppressWarnings("unused")
    private void btnCompletion_Clicked() {
        editor.showCompleter();
    }

    @SuppressWarnings("unused")
    private void btnCheck_Clicked() {
        editor.compileUserFunc(true, false);
        setCheckButtonState();
    }

    private void showChoceObjectId(EDefType definitionClass, String title, Class<? extends Definition> objTempl) {
        Set<EDefType> templList = EnumSet.noneOf(EDefType.class);
        templList.add(definitionClass);

        boolean invocate = objTempl != null;
        if (invocate) {
            InvocateWizard choceObj = new InvocateWizard(editor, /*null,*/ title, objTempl, templList);
            if (choceObj.exec() == 1) {
                editor.addJmlTagId(choceObj.getSelectedDef());
            }
        } else {
            ChooseObjectDialog choceObj = new ChooseObjectDialog(editor, null, title, templList, false);
            if (choceObj.exec() == 1) {
                editor.addJmlTagId(choceObj.getSelectedDef());
            }
        }
    }

    private void showLibUserFunc() {
        LibUserFuncWizard choceObj = new LibUserFuncWizard(editor);
        if (choceObj.exec() == 1) {
            editor.addJmlTagInvocate(choceObj.getSelectedDef(), choceObj.getParams());
        }
    }

    private void showChoceObjectInvocate() {
        Set<EDefType> templList = EnumSet.noneOf(EDefType.class);
        templList.add(EDefType.CLASS);
        Collection<DefInfo> allowedDefinitions = null;
        InvocateWizard choceObj = new InvocateWizard(editor, /*allowedDefinitions,*/ Application.translate("JmlEditor", "Choose Invocation Wizard"), null, templList);
        if (choceObj.exec() == 1) {
            editor.addJmlTagInvocate(choceObj.getSelectedDef());
        }
    }

    private void showChoceType() {
        TypeWizard choceObj = new TypeWizard(editor, false);
        if (choceObj.exec() == 1) {
            editor.addJmlTagType(choceObj.getSelectedDef(), choceObj.getValTypeDef());
        }
    }

    private void showChoceDbEntity() {
        Set<EDefType> templList = EnumSet.noneOf(EDefType.class);
        templList.add(EDefType.CLASS);
        ChooseObjectDialog choceObj = new ChooseObjectDialog(editor, null, Application.translate("JmlEditor", "Choose Entity"), templList, true);
        if ((choceObj.exec() == 1) && (choceObj.getSelectedDef() != null)) {
            RadClassPresentationDef classDef = editor.getEnvironment().getApplication().getDefManager().getClassPresentationDef(choceObj.getSelectedDef().getId());
            RadSelectorPresentationDef selPresDef = classDef.getDefaultSelectorPresentation();
            GroupModel groupModel = GroupModel.openTableContextlessSelectorModel(WidgetUtils.findNearestModel(editor), selPresDef);

            final SelectEntityDialog dialog = new SelectEntityDialog(groupModel, false);
            if (com.trolltech.qt.gui.QDialog.DialogCode.resolve(dialog.exec()).equals(com.trolltech.qt.gui.QDialog.DialogCode.Accepted)) {
                editor.addJmlTagDbEntity(dialog.selectedEntity);
            }
        }
    }

    public void close() {
        /*for(QAction act:menuDbNames.actions()){
         if(act instanceof ExplorerAction)
         ((ExplorerAction) act).close();
         }
         for(QAction act:menuGetId.actions()){
         if(act instanceof ExplorerAction)
         ((ExplorerAction) act).close();
         }*/
        menuDbNames.removeAllActions();
        menuGetId.removeAllActions();
    }

    public QToolBar getToolBar() {
        return toolBarWidget;
    }
}
