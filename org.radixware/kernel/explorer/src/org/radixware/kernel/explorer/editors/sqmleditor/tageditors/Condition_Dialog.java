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

package org.radixware.kernel.explorer.editors.sqmleditor.tageditors;

import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QComboBox;
import com.trolltech.qt.gui.QDialog.DialogCode;
import com.trolltech.qt.gui.QGridLayout;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QLineEdit;
import com.trolltech.qt.gui.QWidget;
import java.util.EnumSet;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EDefinitionDisplayMode;
import org.radixware.kernel.common.client.meta.RadSelectorPresentationDef;
import org.radixware.kernel.common.client.meta.mask.EditMaskNone;
import org.radixware.kernel.common.client.meta.sqml.ISqmlColumnDef;
import org.radixware.kernel.common.client.types.ArrRef;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.client.types.Reference;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.types.Arr;
import org.radixware.kernel.common.types.ArrBin;
import org.radixware.kernel.common.types.ArrBool;
import org.radixware.kernel.common.types.ArrChar;
import org.radixware.kernel.common.types.ArrDateTime;
import org.radixware.kernel.common.types.ArrInt;
import org.radixware.kernel.common.types.ArrNum;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.explorer.dialogs.ArrayEditorDialog;
import org.radixware.kernel.explorer.editors.valeditors.ValEditor;
import org.radixware.kernel.explorer.editors.valeditors.ValRefEditor;

import org.radixware.schemas.xscml.Sqml.Item.ParentCondition.Operator;


public class Condition_Dialog extends ValPropEdit_Dialog {

    private Operator.Enum operator;
    private String str_operatop;
    private boolean isValid = true;
    private final QGridLayout gridLayout = new QGridLayout();
    private final QLabel lbValue;

    private final QLineEdit editLineProp = new QLineEdit(this);
    private final QComboBox editOperators = new QComboBox(this);
    private ValEditor<String> valEditorArr;
    private ValEditor betweenValEditor1;
    private ValEditor betweenValEditor2;

    public Condition_Dialog(final IClientEnvironment environment, final QWidget w, final ISqmlColumnDef prop, final Operator.Enum op, final Reference ref, final EDefinitionDisplayMode showMode) {
        super(environment, w, prop, environment.getMessageProvider().translate("SqmlEditor", "Edit Condition"), true);
        this.setWindowTitle(environment.getMessageProvider().translate("SqmlEditor", "Edit Condition"));//�������� �������
        lbValue = new QLabel(environment.getMessageProvider().translate("SqmlEditor", "Value:"), this);
        this.operator = op;
        //editLineProp=new QLineEdit(this);
        editLineProp.setObjectName("editLineProp");
        if (valEditor == null) {
            isValid = false;
        } else {
            if ((ref != null) && (operator != null) && (operator != Operator.IS_NULL) && (operator != Operator.IS_NOT_NULL)) {
                ((ValRefEditor) valEditor).setValue(ref);
            }
            valEditor.valueChanged.connect(this, "onValueChanged()");
            createUI();
            setFixedSize(sizeHint().width() + 100, sizeHint().height());
            editLineProp.setEnabled(false);
            final String displName = getDisplaiedName(prop, showMode);
            editLineProp.setText(displName);
        }
    }

    private void createUI() {
        //gridLayout=new QGridLayout();
        final QLabel lbPropName = new QLabel(getEnvironment().getMessageProvider().translate("SqmlEditor", "Property:"), this);
        final QLabel lbOperation = new QLabel(getEnvironment().getMessageProvider().translate("SqmlEditor", "Operation:"), this);
        //lbValue=new QLabel(Environment.translate("SqmlEditor","Value:"),this);
        lbValue.setObjectName("lbValue");
        //editOperators = new QComboBox(this);
        editOperators.setObjectName("editOperators");
        addOperators();
        editOperators.currentIndexChanged.connect(this, "operatorChanged()");

        gridLayout.addWidget(lbPropName, 0, 0);
        gridLayout.addWidget(editLineProp, 0, 1);
        gridLayout.addWidget(lbOperation, 1, 0);
        gridLayout.addWidget(editOperators, 1, 1);
        gridLayout.addWidget(lbValue, 2, 0);
        gridLayout.addWidget(valEditor, 2, 1);
        dialogLayout().addLayout(gridLayout);
        addButtons(EnumSet.of(EDialogButtonType.OK, EDialogButtonType.CANCEL), true);
        editOperators.currentIndexChanged.emit(0);
        onValueChanged();        
    }

    private void addOperators() {
        editOperators.clear();

        int index = 0;
        editOperators.addItem("is null");
        editOperators.setItemData(index, Operator.IS_NULL);
        index++;
        editOperators.addItem("is not null");
        editOperators.setItemData(index, Operator.IS_NOT_NULL);        
        index++;
        
        if (!prop.getType().isArrayType()){
        
            editOperators.addItem("=");
            editOperators.setItemData(index, Operator.EQUAL);
            index++;
            if (prop.getType() != EValType.BOOL) {
                editOperators.addItem("<>");
                editOperators.setItemData(index, Operator.NOT_EQUAL);
                index++;
                editOperators.addItem(IN);
                editOperators.setItemData(index, null);
                index++;
                editOperators.addItem(NOT_IN);
                editOperators.setItemData(index, null);
                index++;
                if (prop.getType() != EValType.PARENT_REF) {
                    editOperators.addItem("<");
                    editOperators.setItemData(index, null);
                    index++;
                    editOperators.addItem(">");
                    editOperators.setItemData(index, null);
                    index++;

                    editOperators.addItem("<=");
                    editOperators.setItemData(index, null);
                    index++;
                    editOperators.addItem(">=");
                    editOperators.setItemData(index, null);
                    index++;
                    if ((prop.getType() == EValType.STR) || (prop.getType() == EValType.INT)
                            || (prop.getType() == EValType.NUM)) {
                        editOperators.addItem("like");
                        editOperators.setItemData(index, null);
                        index++;
                    }

                    editOperators.addItem(BETWEEN);
                    editOperators.setItemData(index, null);
                    index++;
                }
            }
        }
        else{
            lbValue.setVisible(false);
        }
        final int curIndex = editOperators.findData(operator);
        if ((operator != null) && (curIndex > 0)) {
            editOperators.setCurrentIndex(curIndex);
        } else {
            editOperators.setCurrentIndex(0);
        }
    }
    private static final String IN = "in";
    private static final String NOT_IN = "not in";
    private static final String BETWEEN = "between";
    private Arr arrValues;

    @SuppressWarnings("unused")
    private void operatorChanged() {
        val.clear();
        final int indOper = editOperators.currentIndex();
        lbValue.setText(getEnvironment().getMessageProvider().translate("SqmlEditor", "Value:"));
        operator = (Operator.Enum) editOperators.itemData(indOper);
        if ((operator == Operator.IS_NULL) || (operator == Operator.IS_NOT_NULL)) {
            valEditor.setEnabled(false);
            valEditor.clear();
            if ((betweenValEditor1 != null) && (betweenValEditor2 != null)) {
                betweenValEditor1.setEnabled(false);
                betweenValEditor2.setEnabled(false);
            }
            if (valEditorArr != null) {
                valEditorArr.setEnabled(false);
                valEditorArr.setValue(null);
            }
            lbValue.setEnabled(false);
            setCanAccept(true);
        } else if ((IN.equals(editOperators.currentText())) || (NOT_IN.equals(editOperators.currentText())) || (BETWEEN.equals(editOperators.currentText()))) {
            changeValEditor();
            lbValue.setEnabled(true);
        } else {
            removeValEditors();
            if (!gridLayout.children().contains(valEditor)) {
                gridLayout.addWidget(valEditor, 2, 1);
                valEditor.setParent(this);
                valEditor.setEnabled(checkRestriction());
                lbValue.setEnabled(checkRestriction());
                setCanAccept(valEditor.getValue()!=null);
            }
        }
    }
    
    private void setCanAccept(final boolean canAccept){
        if (getButton(EDialogButtonType.OK)!=null){
            getButton(EDialogButtonType.OK).setEnabled(canAccept);
        }
    }


    private void removeValEditors() {
        if ((valEditor != null) && valEditor.parentWidget() != null) {
            gridLayout.removeWidget(valEditor);
            valEditor.setParent(null);
        }
        if ((valEditorArr != null) && valEditorArr.parentWidget() != null) {
            gridLayout.removeWidget(valEditorArr);
            valEditorArr.setParent(null);
        }
        if ((betweenValEditor1 != null) && (betweenValEditor2 != null) && (betweenValEditor1.parentWidget() != null) && (betweenValEditor2.parentWidget() != null)) {
            gridLayout.removeWidget(betweenValEditor1);
            betweenValEditor1.setParent(null);
            gridLayout.removeWidget(betweenValEditor2);
            betweenValEditor2.setParent(null);
        }
    }

    private void changeValEditor() {
        if ((IN.equals(editOperators.currentText())) || (NOT_IN.equals(editOperators.currentText()))) {
            removeValEditors();
            if (valEditorArr == null) {
                valEditorArr = new ValEditor<>(getEnvironment(), this, new EditMaskNone(), false, true);
                valEditorArr.setObjectName("valEditorArr");
                final QAction action = new QAction(this);
                action.triggered.connect(this, "bntOpenDialogClick()");
                valEditorArr.addButton("...", action);
            }
            valEditorArr.setParent(this);
            valEditorArr.setEnabled(checkRestriction());
            gridLayout.addWidget(valEditorArr, 2, 1);
            setCanAccept(valEditorArr.getValue()!=null);
        } else if (BETWEEN.equals(editOperators.currentText())) {
            removeValEditors();
            lbValue.setText(getEnvironment().getMessageProvider().translate("SqmlEditor", "Range:"));
            betweenValEditor1 = createValEditor();
            betweenValEditor1.setObjectName("betweenValEditor1");
            betweenValEditor2 = createValEditor();
            betweenValEditor2.setObjectName("betweenValEditor2");
            betweenValEditor1.setEnabled(true);
            betweenValEditor2.setEnabled(true);
            betweenValEditor1.valueChanged.connect(this, "onValueChanged()");
            betweenValEditor2.valueChanged.connect(this, "onValueChanged()");
            final QHBoxLayout layout = new QHBoxLayout();
            layout.addWidget(betweenValEditor1);
            layout.addWidget(betweenValEditor2);
            gridLayout.addLayout(layout, 2, 1);
            if ((betweenValEditor1.getValue() != null) && (betweenValEditor2.getValue() != null)) {
                setCanAccept(true);
            } else {
                setCanAccept(false);
            }
        }
        
    }

    private boolean checkRestriction() {
        if (prop.getType() == EValType.PARENT_REF) {
            if (prop.getSelectorPresentationId() != null) {
                final RadSelectorPresentationDef def = getEnvironment().getApplication().getDefManager().getSelectorPresentationDef(prop.getSelectorPresentationId());
                return !def.getRestrictions().getIsContextlessUsageRestricted();
            } else {
                return false;
            }
        }
        return true;
    }

    @SuppressWarnings({"unused", "unchecked"})
    private void bntOpenDialogClick() {
        ArrayEditorDialog dialog;
        if (prop.getType() == EValType.PARENT_REF) {
            final RadSelectorPresentationDef def = getEnvironment().getApplication().getDefManager().getSelectorPresentationDef(prop.getSelectorPresentationId());
            dialog = new ArrayEditorDialog(getEnvironment(), def, false, this);
        } else {
            dialog = new ArrayEditorDialog(getEnvironment(), prop.getType(), null, false, this);
        }
        final Arr arr = createArr(prop.getType());
        for (int i = 0, size = val.size(); i < size; i++) {
            arr.add(val.get(i));
        }
        dialog.setWindowTitle(getEnvironment().getMessageProvider().translate("SqmlEditor", "Values List"));//������ ��������
        dialog.setCurrentValue(arr);
        dialog.setEditMask(prop.getEditMask());
        dialog.setMandatory(true);
        dialog.setDuplicatesEnabled(false);
        if (dialog.exec() == DialogCode.Accepted.value()) {
            arrValues = dialog.getCurrentValue();
            final String forEdit = arrToString(arrValues);
            valEditorArr.setValue(forEdit);
            if ((arrValues != null) && (arrValues.size() > 0)) {
                setCanAccept(true);
            } else {
                setCanAccept(false);
            }
        }
    }

    private Arr createArr(final EValType type) {
        if ((type == EValType.BIN) || (type == EValType.ARR_BIN)
                || (type == EValType.ARR_BLOB) || (type == EValType.BLOB)) {
            return new ArrBin();
        }
        if ((type == EValType.ARR_BOOL) || (type == EValType.BOOL)) {
            return new ArrBool();
        }
        if ((type == EValType.ARR_CHAR) || (type == EValType.CHAR)) {
            return new ArrChar();
        }
        if ((type == EValType.ARR_CLOB) || (type == EValType.CLOB)
                || (type == EValType.ARR_STR) || (type == EValType.STR)) {
            return new ArrStr();
        }
        if ((type == EValType.ARR_DATE_TIME) || (type == EValType.DATE_TIME)) {
            return new ArrDateTime();
        }
        if ((type == EValType.ARR_INT) || (type == EValType.INT)) {
            return new ArrInt();
        }
        if ((type == EValType.ARR_NUM) || (type == EValType.NUM)) {
            return new ArrNum();
        }
        if ((type == EValType.ARR_REF) || (type == EValType.PARENT_REF)) {
            return new ArrRef();
        }
        throw new IllegalUsageError("Cannot create array of \"" + type.name() + "\" type");
    }

    private String arrToString(final Arr a) {
        //String res = "";
        val.clear();
        final StringBuilder sb=new StringBuilder();
        for (int i = 0; i <= a.size() - 1; i++) {
            if (a.get(i) != null) {
                final String item = a.get(i).toString();
                sb.append(item).append( ',');
                //res += item + ',';
                val.add(a.get(i));
            }
        }
        return sb.length() != 0 ? sb.substring(0, sb.length() - 1) : "";
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public void accept() {
        final int indOperator = editOperators.currentIndex();
        operator = (Operator.Enum) editOperators.itemData(indOperator);
        if (operator == null) {
            str_operatop = editOperators.itemText(indOperator);
        }
        val.clear();
        final boolean singleValue = 
                (!IN.equals(editOperators.currentText())) && (!NOT_IN.equals(editOperators.currentText())) && (!BETWEEN.equals(editOperators.currentText()));
        if (singleValue && !valEditor.checkInput()){
            return;
        }
        if (valEditor.getValue() != null) {
            if ((valEditor instanceof ValRefEditor)) {
                final Pid pid = ((ValRefEditor) valEditor).getValue().getPid();
                val.add(pid);
            } else if (singleValue) {
                val.add(valEditor.getValue());
            }
        }
        if (BETWEEN.equals(editOperators.currentText())) {
            if (!betweenValEditor1.checkInput() || !betweenValEditor2.checkInput()){
                return;
            }
            val.add(betweenValEditor1.getValue());
            val.add(betweenValEditor2.getValue());
        }
        if ((IN.equals(editOperators.currentText())) || (NOT_IN.equals(editOperators.currentText()))) {
            if (val.isEmpty()) {
                val.addAll(arrValues);
            }
        }
        saveGeometryToConfig();
        super.accept();
    }

    private void onValueChanged() {
        setCanAccept(false);
        final int indOper = editOperators.currentIndex();
        operator = (Operator.Enum) editOperators.itemData(indOper);
        if ((operator == Operator.IS_NULL) || (operator == Operator.IS_NOT_NULL) || (valEditor.getValue() != null)) {
            setCanAccept(true);
        } else if ((BETWEEN.equals(editOperators.currentText())) && (betweenValEditor1.getValue() != null) && (betweenValEditor2.getValue() != null)) {
            setCanAccept(true);
        }
    }

    public Operator.Enum getOperator() {
        return operator;
    }

    public String getStrOperator() {
        return str_operatop;
    }

    public boolean isValid() {
        return isValid;
    }

    public boolean isParentCondition() {
        return prop.getType() == EValType.PARENT_REF && operator != null
                && ((operator == Operator.IS_NULL) || (operator == Operator.IS_NOT_NULL)
                || (operator == Operator.EQUAL) || (operator == Operator.NOT_EQUAL));
    }
}