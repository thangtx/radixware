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

package org.radixware.kernel.explorer.editors.jmleditor.dialogs;

import com.trolltech.qt.core.QAbstractItemModel;
import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QAbstractItemView;
import com.trolltech.qt.gui.QGridLayout;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QHeaderView;
import com.trolltech.qt.gui.QItemDelegate;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QLineEdit;
import com.trolltech.qt.gui.QPushButton;
import com.trolltech.qt.gui.QTableWidget;
import com.trolltech.qt.gui.QTableWidgetItem;
import com.trolltech.qt.gui.QToolButton;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.meta.mask.EditMaskStr;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodThrowsList;
import org.radixware.kernel.common.defs.ads.clazz.members.MethodParameter;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.ads.type.IAdsTypeSource;
import org.radixware.kernel.common.defs.ads.userfunc.AdsUserFuncDef;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.dialogs.ExplorerDialog;
import org.radixware.kernel.explorer.editors.jmleditor.JmlEditor;
import org.radixware.kernel.explorer.editors.valeditors.ValStrEditor;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.text.ExplorerTextOptions;


public class ProfileDialog extends ExplorerDialog {

    private final JmlEditor editor;
    private final ValStrEditor edReturnType;
    private final QLineEdit edName;
    private final QPushButton btnParamDelete;
    private final QPushButton btnParamUp;
    private final QPushButton btnParamDown;
    private final QPushButton btnDeleteException;
    private final QTableWidget tableParamWidget;
    private final QTableWidget tableExceptionWidget;
    private final boolean isReadOnly;
    private final static int PARAM_NAME_COLUMN = 0;
    private final static int PARAM_TYPE_COLUMN = 1;
    private final static String VOID = "void";
    private final AdsMethodDef.Profile ufProfile;
    private final List<MethodParameter> parametersList;
    private final Map<Id, MethodParameter> parametersMap;
    private final List<AdsMethodThrowsList.ThrowsListItem> throwsList;
    private final MessageProvider messageProvider;
    private AdsTypeDeclaration returnType;

    public ProfileDialog(final JmlEditor editor) {
        super(editor.getEnvironment(), editor, "ProfileDialog");
        setWindowTitle(Application.translate("JmlEditor", "Profile Editor"));
        ufProfile = editor.getUserFunc().findProfile();
        this.isReadOnly = editor.isReadOnly();
        this.editor = editor;
        this.messageProvider = editor.getEnvironment().getMessageProvider();
        tableParamWidget = new QTableWidget(this);
        tableExceptionWidget = new QTableWidget(this);

        parametersList = new LinkedList<>();
        parametersMap = new HashMap<>();
        for (MethodParameter p : ufProfile.getParametersList()) {
            MethodParameter paramCopy = MethodParameter.Factory.newInstance(p);
            parametersList.add(paramCopy);
            parametersMap.put(paramCopy.getId(), p);
        }
        throwsList = new ArrayList<>();
        throwsList.addAll(ufProfile.getThrowsList().list());
        returnType = /*AdsTypeDeclaration.Factory.newCopy(*/ ufProfile.getReturnValue().getType();//);

        btnParamDelete = new QPushButton();
        btnParamUp = new QPushButton();
        btnParamDown = new QPushButton();
        btnDeleteException = new QPushButton();
        edName = new QLineEdit(this);
        final EditMaskStr editMask = new EditMaskStr();
        editMask.setNoValueStr(VOID);
        edReturnType = new ValStrEditor(getEnvironment(), this, editMask, true, isReadOnly);
        createUi();
    }

    private void createUi() {
        final QVBoxLayout generalLayout = new QVBoxLayout();

        final QGridLayout gridLayout = new QGridLayout();
        final QLabel lbName = new QLabel(this);
        lbName.setText(Application.translate("JmlEditor", "Name") + ":");
        gridLayout.addWidget(lbName, 0, 0);


        gridLayout.addWidget(edName, 0, 1);
        edName.textChanged.connect(this, "functionNameWasChanged(String)");
        //edName.setReadOnly(true);
        final String methodName = getUserFunc().findMethod().getName();
        edName.setText(methodName);


        final QLabel lbReturnType = new QLabel(this);
        lbReturnType.setText(Application.translate("JmlEditor", "Result type") + ":");
        gridLayout.addWidget(lbReturnType, 1, 0);


        final QToolButton btnEditReturnType = new QToolButton();
        btnEditReturnType.setObjectName("btnEditReturnType");
        btnEditReturnType.setText("...");
        btnEditReturnType.clicked.connect(this, "btnEditReturnType_Clicked()");
        final QToolButton btnReturnTypeToVoid = new QToolButton();
        btnReturnTypeToVoid.setObjectName("btnReturnTypeToVoid");
        btnReturnTypeToVoid.setIcon(ExplorerIcon.getQIcon(JmlEditor.JmlEditorIcons.VOID));
        btnReturnTypeToVoid.clicked.connect(this, "setReturnTypeToVoid()");


        if (returnType != null) {
            //returnType= returnType==AdsTypeDeclaration.UNDEFINED? AdsTypeDeclaration.VOID: returnType;
            edReturnType.setValue(returnType.getQualifiedName(editor.getUserFunc()));
        }
        edReturnType.addButton(btnEditReturnType);
        edReturnType.addButton(btnReturnTypeToVoid);
        edReturnType.getLineEdit().setReadOnly(true);
        gridLayout.addWidget(edReturnType, 1, 1);

        final QVBoxLayout patamLayout = createParamLayout();
        final QVBoxLayout throwsLayout = createExceptionsLayout();

        generalLayout.addLayout(gridLayout);
        generalLayout.addLayout(patamLayout);
        generalLayout.addLayout(throwsLayout);
        dialogLayout().addLayout(generalLayout);

        if (isReadOnly) {
            addButton(EDialogButtonType.CLOSE);
        } else {
            addButton(EDialogButtonType.OK);
            addButton(EDialogButtonType.CANCEL);
            acceptButtonClick.connect(this, "accept()");
            rejectButtonClick.connect(this, "reject()");
        }
        //functionNameWasChanged(methodName);
    }

    @SuppressWarnings("unused")
    private void functionNameWasChanged(final String str) {
        if (getButton(EDialogButtonType.OK) != null) {
            getButton(EDialogButtonType.OK).setEnabled(!(str == null || str.isEmpty()));
        }
    }

    private QVBoxLayout createParamLayout() {
        final QVBoxLayout layout = new QVBoxLayout();
        final QHBoxLayout tableLayout = new QHBoxLayout();
        final QVBoxLayout btnLayout = createParamBtnsLayout();
        createParamsTable();
        tableParamWidget.currentItemChanged.connect(this, "paramTableCurItemChanged(QTableWidgetItem, QTableWidgetItem)");
        tableLayout.addWidget(tableParamWidget);
        tableLayout.addLayout(btnLayout);

        final QLabel lbParams = new QLabel(this);
        lbParams.setText(messageProvider.translate("JmlEditor", "Parameters") + ":");
        layout.addWidget(lbParams);
        layout.addLayout(tableLayout);
        final QLabel lbErrorInParams = new QLabel(this);
        lbErrorInParams.setObjectName("PARAM_ERRORS");
        layout.addWidget(lbErrorInParams);
        final ExplorerTextOptions options = ExplorerTextOptions.Factory.getOptions(Color.red);
        options.applyTo(lbErrorInParams);
        lbErrorInParams.setText("aaa");
        lbErrorInParams.setVisible(false);
        return layout;
    }

    @SuppressWarnings("unused")
    private void paramTableCurItemChanged(final QTableWidgetItem item1, final QTableWidgetItem item2) {
        setParamBtnsState();
    }

    private void setParamBtnsState() {
        final boolean tableIsNotEmpty = tableParamWidget.rowCount() > 0;
        final int row = tableParamWidget.currentRow();
        btnParamUp.setEnabled(row > 0 && tableIsNotEmpty);
        btnParamDown.setEnabled((row < tableParamWidget.rowCount() - 1) && tableIsNotEmpty);
        btnParamDelete.setEnabled(tableIsNotEmpty);
    }

    private void setThrowBtnsState() {
        final boolean tableIsNotEmpty = tableExceptionWidget.rowCount() > 0;
        btnDeleteException.setEnabled(tableIsNotEmpty);
    }

    private QVBoxLayout createExceptionsLayout() {
        final QVBoxLayout layout = new QVBoxLayout();
        final QHBoxLayout tableLayout = new QHBoxLayout();
        final QVBoxLayout btnLayout = createExceptionBtnsLayout();
        createExceptionsTable();
        tableLayout.addWidget(tableExceptionWidget);
        tableLayout.addLayout(btnLayout);

        final QLabel lbThrows = new QLabel(this);
        lbThrows.setText(messageProvider.translate("JmlEditor", "Thrown Exceptions") + ":");
        layout.addWidget(lbThrows);
        layout.addLayout(tableLayout);
        return layout;
    }

    private QVBoxLayout createExceptionBtnsLayout() {
        final QVBoxLayout btnLayout = new QVBoxLayout();
        final QPushButton btnAddException = new QPushButton();
        btnAddException.setObjectName("btnAddException");
        btnAddException.setParent(this);
        btnAddException.setToolTip(messageProvider.translate("JmlEditor", "Add Exception"));
        btnAddException.setIcon(ExplorerIcon.getQIcon(JmlEditor.JmlEditorIcons.CREATE));
        btnAddException.setIconSize(new QSize(22, 22));
        btnAddException.clicked.connect(this, "addException()");
        btnAddException.setEnabled(!isReadOnly);

        btnDeleteException.setObjectName("btnDeleteException");
        btnDeleteException.setParent(this);
        btnDeleteException.setToolTip(messageProvider.translate("JmlEditor", "Delete Exception"));
        btnDeleteException.setIcon(ExplorerIcon.getQIcon(JmlEditor.JmlEditorIcons.DELETE));
        btnDeleteException.setIconSize(new QSize(22, 22));
        btnDeleteException.clicked.connect(this, "deleteException()");
        btnDeleteException.setEnabled(!isReadOnly);

        btnLayout.addWidget(btnAddException/*,0,Qt.AlignmentFlag.AlignTop*/);
        btnLayout.addWidget(btnDeleteException, 0, Qt.AlignmentFlag.AlignTop);
        return btnLayout;
    }

    private QVBoxLayout createParamBtnsLayout() {
        final QVBoxLayout btnLayout = new QVBoxLayout();
        final QPushButton btnAdd = new QPushButton();
        btnAdd.setObjectName("btnAddParam");
        btnAdd.setParent(this);
        btnAdd.setToolTip(messageProvider.translate("JmlEditor", "Add Parameter"));
        btnAdd.setIcon(ExplorerIcon.getQIcon(JmlEditor.JmlEditorIcons.CREATE));
        btnAdd.setIconSize(new QSize(22, 22));
        btnAdd.clicked.connect(this, "addParam()");
        btnAdd.setEnabled(!isReadOnly);

        btnParamDelete.setObjectName("btnParamDelete");
        btnParamDelete.setParent(this);
        btnParamDelete.setToolTip(messageProvider.translate("JmlEditor", "Delete Parameter"));
        btnParamDelete.setIcon(ExplorerIcon.getQIcon(JmlEditor.JmlEditorIcons.DELETE));
        btnParamDelete.setIconSize(new QSize(22, 22));
        btnParamDelete.clicked.connect(this, "deleteParam()");
        btnParamDelete.setEnabled(!isReadOnly);

        btnParamUp.setObjectName("btnParamUp");
        btnParamUp.setParent(this);
        btnParamUp.setToolTip(messageProvider.translate("JmlEditor", "Move Up"));
        btnParamUp.setIcon(ExplorerIcon.getQIcon(JmlEditor.JmlEditorIcons.UP));
        btnParamUp.setIconSize(new QSize(22, 22));
        btnParamUp.clicked.connect(this, "paramUp()");
        btnParamUp.setEnabled(!isReadOnly);

        btnParamDown.setObjectName("btnParamDown");
        btnParamDown.setParent(this);
        btnParamDown.setToolTip(messageProvider.translate("JmlEditor", "Move Down"));
        btnParamDown.setIcon(ExplorerIcon.getQIcon(JmlEditor.JmlEditorIcons.DOWN));
        btnParamDown.setIconSize(new QSize(22, 22));
        btnParamDown.clicked.connect(this, "paramDown()");
        btnParamDown.setEnabled(!isReadOnly);

        btnLayout.addWidget(btnAdd/*,0,Qt.AlignmentFlag.AlignTop*/);
        btnLayout.addWidget(btnParamDelete/*,0,Qt.AlignmentFlag.AlignTop*/);
        btnLayout.addWidget(btnParamUp/*,0,Qt.AlignmentFlag.AlignTop*/);
        btnLayout.addWidget(btnParamDown, 0, Qt.AlignmentFlag.AlignTop);
        return btnLayout;
    }

    @SuppressWarnings("unused")
    private void deleteParam() {
        final int index = tableParamWidget.currentRow();
        tableParamWidget.removeRow(index);
        parametersList.remove(index);
        setParamBtnsState();

        tableParamWidget.resizeColumnToContents(0);
        getButton(EDialogButtonType.OK).setEnabled(checkParamName());
    }

    @SuppressWarnings("unused")
    private void addParam() {
        final NewParamDialog dlg = new NewParamDialog(this);
        if (dlg.exec() == 1) {
            final MethodParameter newParam = dlg.getNewParameter();
            final int index = tableParamWidget.rowCount();
            parametersList.add(index, newParam);
            insertParamRow(index, newParam);
            tableParamWidget.setCurrentCell(index, 0);
            setParamBtnsState();

            getButton(EDialogButtonType.OK).setEnabled(checkParamName());
            tableParamWidget.resizeColumnToContents(0);
        }
    }

    private boolean checkParamName() {
        final QObject labelObj = this.findChild(QLabel.class, "PARAM_ERRORS");
        QLabel label = null;
        if (labelObj instanceof QLabel) {
            label = (QLabel) labelObj;
        }
        boolean isWrongParamName = false;
        String wrongName = "";
        String errorMessage = "";
        try {
            for (int i = 0; i < parametersList.size(); i++) {
                MethodParameter newParam = parametersList.get(i);
                if (!isValidJavaIdentifierName(newParam.getName())) {
                    isWrongParamName = true;
                    wrongName = newParam.getName();
                    errorMessage = messageProvider.translate("JmlEditor", "Incorrect parameter name (not Java identifier name)") + ": ";
                    return false;
                }

                for (int j = i + 1; j < parametersList.size(); j++) {
                    MethodParameter param = parametersList.get(j);
                    if (param.getName().equals(newParam.getName())) {
                        isWrongParamName = true;
                        wrongName = newParam.getName();
                        errorMessage = messageProvider.translate("JmlEditor", "Duplicated parameter") + ": ";
                        return false;
                    }
                }
            }
            
        } finally {
            if (label != null) {
                label.setVisible(isWrongParamName);
                label.setText(errorMessage + wrongName);
            }
        }
        return !isWrongParamName;
    }
    
    private boolean isValidJavaIdentifierName(final String identifierName) {
        if(identifierName == null || identifierName.isEmpty()) {
            return false;
        }
        char[] nameAsCharMas = identifierName.toCharArray();
        if (!Character.isJavaIdentifierStart(nameAsCharMas[0])) {
            return false;
        }
        for (int pos = 1; pos < nameAsCharMas.length; pos++) {
            if (!Character.isJavaIdentifierPart(nameAsCharMas[pos])) {
                return false;
            }
        }
        return true;
    }

    @SuppressWarnings("unused")
    private void paramUp() {
        final int index = tableParamWidget.currentRow();
        if (index > 0 && index < tableParamWidget.rowCount()) {
            final MethodParameter param = parametersList.get(index);
            tableParamWidget.removeRow(index);
            insertParamRow(index - 1, param);

            parametersList.remove(index);
            parametersList.add(index - 1, param);
            tableParamWidget.setCurrentCell(index - 1, 0);
            setParamBtnsState();
        }
    }

    @SuppressWarnings("unused")
    private void paramDown() {
        final int index = tableParamWidget.currentRow();
        if (index < tableParamWidget.rowCount() - 1 && index >= 0) {
            final MethodParameter param = parametersList.get(index);
            tableParamWidget.removeRow(index);
            insertParamRow(index + 1, param);

            parametersList.remove(index);
            parametersList.add(index + 1, param);
            tableParamWidget.setCurrentCell(index + 1, 0);
            setParamBtnsState();
        }
    }

    @SuppressWarnings("unused")
    private void deleteException() {
        final int index = tableExceptionWidget.currentRow();
        if (index >= 0 && index < tableExceptionWidget.rowCount()) {
            tableExceptionWidget.removeRow(index);
            throwsList.remove(index);
            setThrowBtnsState();
            tableExceptionWidget.resizeColumnToContents(0);
        }
    }

    @SuppressWarnings("unused")
    private void addException() {
        final int index = tableExceptionWidget.rowCount();//currentRow();
        final Collection<AdsUserFuncDef.Lookup.DefInfo> defInfos = AdsUserFuncDef.Lookup.listTopLevelDefinitions(getUserFunc(), Collections.<EDefType>singleton(EDefType.CLASS));
        final Collection<AdsUserFuncDef.Lookup.DefInfo> exceptionDefInfos = new ArrayList<>();
        for (AdsUserFuncDef.Lookup.DefInfo defInfo : defInfos) {
            if (defInfo.getPath()[0].getPrefix().equals(EDefinitionIdPrefix.ADS_EXCEPTION_CLASS)) {
                if ((defInfo.getEnvironment() == ERuntimeEnvironmentType.SERVER) || (defInfo.getEnvironment() == ERuntimeEnvironmentType.COMMON)) {
                    exceptionDefInfos.add(defInfo);
                }
            }
        }
        final ChooseObjectDialog dlg = new ChooseObjectDialog(editor, exceptionDefInfos, "Select Definition", null, false);
        if (dlg.exec() == 1 && (dlg.getSelectedDef() instanceof IAdsTypeSource)) {
            final AdsTypeDeclaration type = AdsTypeDeclaration.Factory.newInstance((IAdsTypeSource) dlg.getSelectedDef());
            final AdsMethodThrowsList.ThrowsListItem throwExcept = AdsMethodThrowsList.ThrowsListItem.Factory.newInstance(type);
            throwsList.add(throwExcept);

            insertThrowExceptRow(index, throwExcept);
            tableExceptionWidget.setCurrentCell(index, 0);
            setThrowBtnsState();
            tableExceptionWidget.resizeColumnToContents(0);
        }
    }

    private QTableWidget createParamsTable() {
        final List<String> columnName = new ArrayList<>();
        columnName.add(messageProvider.translate("JmlEditor", "Name"));
        columnName.add(messageProvider.translate("JmlEditor", "Type"));
        tableParamWidget.setColumnCount(2);
        tableParamWidget.setHorizontalHeaderLabels(columnName);
        tableParamWidget.setSelectionBehavior(QAbstractItemView.SelectionBehavior.SelectRows);
        tableParamWidget.verticalHeader().setVisible(false);
        tableParamWidget.setSelectionMode(QAbstractItemView.SelectionMode.SingleSelection);

        final NameItemDelegate paramNameItemDelegate = new NameItemDelegate();
        tableParamWidget.setItemDelegateForColumn(PARAM_NAME_COLUMN, paramNameItemDelegate);

        final TypeItemDelegate paramTypeItemDelegate = new TypeItemDelegate();
        tableParamWidget.setItemDelegateForColumn(PARAM_TYPE_COLUMN, paramTypeItemDelegate);

        fillTable();
        if (tableParamWidget.rowCount() > 0) {
            tableParamWidget.setCurrentCell(0, 0);
        }
        tableParamWidget.resizeColumnsToContents();
        tableParamWidget.horizontalHeader().setResizeMode(PARAM_NAME_COLUMN, QHeaderView.ResizeMode.ResizeToContents);
        tableParamWidget.horizontalHeader().setResizeMode(PARAM_TYPE_COLUMN, QHeaderView.ResizeMode.Stretch);
        return tableParamWidget;
    }

    private QTableWidget createExceptionsTable() {
        final List<String> columnName = new ArrayList<>();
        columnName.add(messageProvider.translate("JmlEditor", "Thrown Exception"));
        columnName.add(messageProvider.translate("JmlEditor", "Desctiption"));
        tableExceptionWidget.setColumnCount(2);
        tableExceptionWidget.setHorizontalHeaderLabels(columnName);
        tableExceptionWidget.setSelectionBehavior(QAbstractItemView.SelectionBehavior.SelectRows);
        tableExceptionWidget.verticalHeader().setVisible(false);
        tableExceptionWidget.setSelectionMode(QAbstractItemView.SelectionMode.SingleSelection);
        int rowCount = 0;
        if (throwsList != null) {
            for (AdsMethodThrowsList.ThrowsListItem throwExcept : throwsList) {
                insertThrowExceptRow(rowCount, throwExcept);
                rowCount++;
            }
        }
        if (tableExceptionWidget.rowCount() > 0) {
            tableExceptionWidget.setCurrentCell(0, 0);
        }
        tableExceptionWidget.resizeColumnsToContents();
        tableExceptionWidget.horizontalHeader().setResizeMode(0, QHeaderView.ResizeMode.ResizeToContents);
        tableExceptionWidget.horizontalHeader().setResizeMode(1, QHeaderView.ResizeMode.Stretch);
        return tableExceptionWidget;
    }

    private void fillTable() {
        if (parametersList != null) {
            int rowCount = 0;
            for (MethodParameter param : parametersList) {
                insertParamRow(rowCount, param);
                rowCount++;
                //tableWidget.setItem(rowCount, PARAM_TYPE_COLUMN,  new  QTableWidgetItem(param.getType().getQualifiedName(editor.getUserFunc())));
            }
        }
    }

    private void insertThrowExceptRow(int row, AdsMethodThrowsList.ThrowsListItem throwExcept) {
        tableExceptionWidget.insertRow(row);
        final QTableWidgetItem exceptionNameItem = new QTableWidgetItem(throwExcept.getException().getQualifiedName(editor.getUserFunc()));
        exceptionNameItem.setFlags(new Qt.ItemFlags(Qt.ItemFlag.ItemIsEnabled, Qt.ItemFlag.ItemIsSelectable));
        tableExceptionWidget.setItem(row, 0, exceptionNameItem);
        final QTableWidgetItem exceptionDescriptionItem = new QTableWidgetItem(throwExcept.getDescription());
        tableExceptionWidget.setItem(row, 1, exceptionDescriptionItem);
    }

    private void insertParamRow(int row, MethodParameter param) {
        tableParamWidget.insertRow(row);
        tableParamWidget.setItem(row, PARAM_NAME_COLUMN, new QTableWidgetItem(param.getName()));
        final AdsTypeDeclaration paramType = param.getType();

        final QTableWidgetItem paramTypeItem = new QTableWidgetItem(paramType.getQualifiedName(editor.getUserFunc()));
        tableParamWidget.setItem(row, PARAM_TYPE_COLUMN, paramTypeItem);

    }

    @SuppressWarnings("unused")
    private void setReturnTypeToVoid() {
        returnType = AdsTypeDeclaration.VOID;
        edReturnType.setValue(returnType.getQualifiedName(editor.getUserFunc()));
    }

    @SuppressWarnings("unused")
    private void btnEditReturnType_Clicked() {
        final AdsTypeDeclaration type = getType();
        if (type != null) {
            returnType = type;
            edReturnType.setValue(returnType.getQualifiedName(editor.getUserFunc()));
        }
    }

    @SuppressWarnings("unused")
    private void btnEditParamType_Clicked() {
        final AdsTypeDeclaration type = getType();
        final QTableWidgetItem curItem = tableParamWidget.currentItem();
        if (curItem != null && type != null) {
            final QModelIndex index = tableParamWidget.model().index(curItem.row(), curItem.column());
            final TypeItemDelegate delegate = (TypeItemDelegate) tableParamWidget.itemDelegate(index);
            delegate.srcEditor.setValue(type.getQualifiedName(editor.getUserFunc()));
            parametersList.get(curItem.row()).setType(type);
        }
    }

    AdsUserFuncDef getUserFunc() {
        return editor.getUserFunc();
    }

    AdsTypeDeclaration getType() {
        AdsTypeDeclaration type = null;
        final TypeWizard choceObj = new TypeWizard(editor, true);
        if (choceObj.exec() == 1) {
            type = choceObj.calcType();
            /* EValType valType = choceObj.getValTypeDef();
             RadixObject obj = choceObj.getSelectedDef();

             if (obj instanceof AdsTypeDeclaration) {
             type = (AdsTypeDeclaration) obj;

             } else {
             if (choceObj.getDimension() > 0) {
             type = AdsTypeDeclaration.Factory.newInstance(valType, (IAdsTypeSource) obj, null, choceObj.getDimension());
             } else {
             if (valType == null) {
             type = AdsTypeDeclaration.Factory.newInstance((IAdsTypeSource) obj);
             } else {
             type = AdsTypeDeclaration.Factory.newInstance(valType, (IAdsTypeSource) obj, null, 0);
             }
             }
             }*/
        }
        return type;
    }

    @Override
    public void reject() {
        super.reject();
    }

    @Override
    public void accept() {
        tableParamWidget.setCurrentIndex(null);
        
        if(!isValidJavaIdentifierName(edName.text())) {
            editor.getEnvironment().messageError(messageProvider.translate("JmlEditor", "Current function name is not valid according to Java identifier name rules. Please change function name."));
            return;
        }
        if(!getButton(EDialogButtonType.OK).isEnabled()){
            return;
        }
        
        super.accept();

        if (!ufProfile.getReturnValue().getType().equals(returnType)) {
            if (returnType == null) {
                returnType = AdsTypeDeclaration.VOID;
            }
            ufProfile.getReturnValue().setType(returnType);
        }
        if (!ufProfile.getParametersList().list().equals(parametersList)) {
            ufProfile.getParametersList().clear();
            for (MethodParameter param : parametersList) {
                if (parametersMap.containsKey(param.getId())) {
                    MethodParameter oldParam = parametersMap.get(param.getId());
                    oldParam.setName(param.getName());
                    oldParam.setType(param.getType());
                    ufProfile.getParametersList().add(oldParam);
                } else {
                    ufProfile.getParametersList().add(param);
                }
            }
        }
        if (!ufProfile.getThrowsList().list().equals(throwsList)) {
            ufProfile.getThrowsList().clear();
            for (AdsMethodThrowsList.ThrowsListItem throwItem : throwsList) {
                ufProfile.getThrowsList().add(throwItem);
            }
        }
        if (!getUserFunc().findMethod().getName().equals(edName.text())) {
            getUserFunc().findMethod().setName(edName.text());

        }
    }

    private class NameItemDelegate extends QItemDelegate {

        private ValStrEditor srcEditor;

        @Override
        public QWidget createEditor(final QWidget parent, final com.trolltech.qt.gui.QStyleOptionViewItem option, final QModelIndex index) {
            final EditMaskStr editMask = new EditMaskStr();
            editMask.setNoValueStr("");
            srcEditor = new ValStrEditor(getEnvironment(), parent, editMask, true, false);
            srcEditor.setValue(tableParamWidget.item(index.row(), index.column()).text());
            return srcEditor;
        }

        @Override
        public void setModelData(final QWidget editor, final QAbstractItemModel model, final QModelIndex index) {
            final String oldName = tableParamWidget.item(index.row(), index.column()).text();
            super.setModelData(editor, model, index);

            final String newName = srcEditor.getValue();
            if (!newName.equals(oldName)) {
                //if(srcVersions.containsKey(newName)){
                //    String errorMessage=Application.translate("JmlEditor", "Parameter with such name is already exists");
                //    Application.messageError(Application.translate("JmlEditor", "Parameter Name Error"), errorMessage);
                //    tableParamWidget.item(index.row(), index.column()).setText(oldName);
                //    return;
                //}
                //isEditVersionList=true;
                //srcVersions.put(newName, srcVersions.get(oldName));
                //srcVersions.remove(oldName); 
                //changedSrcVersions.add(newName);
                //changedSrcVersions.remove(oldName);
                //oldSrcVersions.add(oldName);
                //}

                parametersList.get(index.row()).setName(srcEditor.getValue());
                boolean isWrongParamName = !checkParamName();

                getButton(EDialogButtonType.OK).setEnabled(!isWrongParamName);
            }
            tableParamWidget.item(index.row(), index.column()).setText(newName);
            tableParamWidget.resizeColumnToContents(0);
        }
    }

    private class TypeItemDelegate extends QItemDelegate {

        private ValStrEditor srcEditor;

        @Override
        public QWidget createEditor(final QWidget parent, final com.trolltech.qt.gui.QStyleOptionViewItem option, final QModelIndex index) {
            final EditMaskStr editMask = new EditMaskStr();
            editMask.setNoValueStr("");
            srcEditor = new ValStrEditor(getEnvironment(), parent, editMask, true, false);
            //srcEditor.setBackgroundRole(C);
            srcEditor.setFrame(false);
            final QToolButton btnEditParamType = new QToolButton();
            btnEditParamType.setObjectName("btnEditParamType");
            btnEditParamType.setText("...");
            btnEditParamType.clicked.connect(ProfileDialog.this, "btnEditParamType_Clicked()");
            srcEditor.getLineEdit().setReadOnly(true);
            srcEditor.addButton(btnEditParamType);
            srcEditor.setValue(tableParamWidget.item(index.row(), index.column()).text());
            return srcEditor;
        }

        @Override
        public void setModelData(final QWidget editor, final QAbstractItemModel model, final QModelIndex index) {
            /* String oldName=tableParamWidget.item(index.row(), index.column()).text();
             super.setModelData(editor, model, index);

             String newName=srcEditor.getValue();*/
            /*if(!newName.equals(oldName)){
             if(srcVersions.containsKey(newName)){
             String errorMessage=Application.translate("JmlEditor", "Source version with such name is already exists");
             Application.messageError(Application.translate("JmlEditor", "Source Version Name Error"), errorMessage);
             tableParamWidget.item(index.row(), index.column()).setText(oldName);
             return;
             }
             isEditVersionList=true;
             srcVersions.put(newName, srcVersions.get(oldName));
             srcVersions.remove(oldName); 
             changedSrcVersions.add(newName);
             changedSrcVersions.remove(oldName);
             oldSrcVersions.add(oldName);
             }*/
            tableParamWidget.item(index.row(), index.column()).setText(srcEditor.getValue());
        }
    }
}
