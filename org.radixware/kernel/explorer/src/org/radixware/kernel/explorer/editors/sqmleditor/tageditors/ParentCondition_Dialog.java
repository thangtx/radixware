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

import com.trolltech.qt.gui.QComboBox;
import com.trolltech.qt.gui.QGridLayout;
import com.trolltech.qt.gui.QLabel;
import java.util.EnumSet;
import java.util.List;
import org.radixware.kernel.common.client.enums.EDefinitionDisplayMode;
import org.radixware.kernel.common.client.meta.sqml.ISqmlColumnDef;
import org.radixware.kernel.common.client.types.Reference;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.explorer.dialogs.ExplorerDialog;
import org.radixware.kernel.explorer.editors.valeditors.ValRefEditor;
import org.radixware.kernel.explorer.editors.xscmleditor.XscmlEditor;
import org.radixware.schemas.xscml.Sqml.Item.ParentCondition.Operator;


public class ParentCondition_Dialog extends ExplorerDialog {

    private final QComboBox editParentProp = new QComboBox(this);
    private final ValRefEditor valRefEditor;
    private final QLabel lbValue = new QLabel(this);
    private final QComboBox editOperators = new QComboBox(this);
    private final boolean isReadOnly;
    private ISqmlColumnDef parentRef;
    private Operator.Enum operator;
    private Reference reference = null;

    public ParentCondition_Dialog(final XscmlEditor editText, final List<ISqmlColumnDef> prop, final ISqmlColumnDef selectedProp, final Operator.Enum op, final Reference ref, final EDefinitionDisplayMode showMode) {
        super(editText.getEnvironment(), editText, "ParentCondition Dialog");
        valRefEditor = new ValRefEditor(getEnvironment(), this, true, false);
        this.isReadOnly = editText.isReadOnly();
        this.setWindowTitle(getEnvironment().getMessageProvider().translate("SqmlEditor", "Edit Condition")); //�������� �������
        parentRef = selectedProp;
        operator = op;
        this.reference = ref == null ? null : new Reference(ref);
        criateUI();
        final int size = prop.size();
        for (int i = 0; i < size; i++) {
            final String propName = getDisplaiedName(prop.get(i), showMode);
            if (prop.get(i).getType() == EValType.PARENT_REF) {
                editParentProp.blockSignals(true);
                editParentProp.addItem(propName);
                editParentProp.setItemData(editParentProp.count() - 1, prop.get(i));
                if (prop.get(i).equals(selectedProp)) {
                    editParentProp.setCurrentIndex(editParentProp.count() - 1);
                    parentRefChanged();
                }
                editParentProp.blockSignals(false);
            }
        }
        if ((ref != null) && (operator != Operator.IS_NULL) && (operator != Operator.IS_NOT_NULL)) {
            //try {
            valRefEditor.setValue(ref);
            //valRefEditor.setValue(new Reference(pid, pid.getDefaultEntityTitle()));
            //} catch (ServiceClientException ex) {
            //    Logger.getLogger(ParentCondition_Dialog.class.getName()).log(Level.SEVERE, null, ex);
            //} catch (InterruptedException ex) {
            //    Logger.getLogger(ParentCondition_Dialog.class.getName()).log(Level.SEVERE, null, ex);
            //}
        } else {
            reference = null;
            valRefEditor.setValue(null);
        }
        valRefEditor.setReadOnly(isReadOnly);
        setFixedSize(sizeHint().width() + 100, sizeHint().height());
    }

    private String getDisplaiedName(final ISqmlColumnDef prop, final EDefinitionDisplayMode displayMode) {
        //final String name = prop.getDisplayableText(displayMode)/*getName()*/!=null ? prop.getDisplayableText(displayMode)/*getName()*/ : "#"+prop.getId();
        //if(displayMode==EDefinitionDisplayMode.SHOW_TITLES){
        //   return prop.getTitle()!=null/*hasTitle()*/ ? prop.getTitle() : name;
        //}else if((displayMode==EDefinitionDisplayMode.SHOW_SHORT_NAMES)&&(name.indexOf("::")!=-1)){
        //    return TagInfo.getNameWithoutModule(name);
        //}
        //return name;
        if (displayMode == EDefinitionDisplayMode.SHOW_TITLES) {
            return prop.getTitle();
        } else {
            return prop.getShortName();
        }
    }

    private void criateUI() {
        final QGridLayout gridLayout = new QGridLayout();
        final QLabel lbPropName = new QLabel(this);
        lbPropName.setText(getEnvironment().getMessageProvider().translate("SqmlEditor", "Property:"));
        final QLabel lbOperation = new QLabel(this);
        lbOperation.setText(getEnvironment().getMessageProvider().translate("SqmlEditor", "Operation:"));
        lbValue.setObjectName("lbValue");
        lbValue.setText(getEnvironment().getMessageProvider().translate("SqmlEditor", "Value:"));
        editParentProp.setObjectName("editParentProp");
        editParentProp.currentIndexChanged.connect(this, "parentRefChanged()");

        valRefEditor.setObjectName("valRefEditor");
        valRefEditor.valueChanged.connect(this, "valRefEditor_changed()");

        editOperators.setObjectName("editOperators");
        editOperators.currentIndexChanged.connect(this, "operatorChanged()");

        final EnumSet<EDialogButtonType> buttons;
        if (isReadOnly) {
            buttons = EnumSet.of(EDialogButtonType.CLOSE);
            editOperators.setEnabled(false);
            editParentProp.setEnabled(false);
            lbValue.setEnabled(false);
        } else {
            buttons = EnumSet.of(EDialogButtonType.OK, EDialogButtonType.CANCEL);
        }        
        
        gridLayout.addWidget(lbPropName, 0, 0);
        gridLayout.addWidget(editParentProp, 0, 1);
        gridLayout.addWidget(lbOperation, 1, 0);
        gridLayout.addWidget(editOperators, 1, 1);
        gridLayout.addWidget(lbValue, 2, 0);
        gridLayout.addWidget(valRefEditor, 2, 1);
        dialogLayout().addLayout(gridLayout);
        addButtons(buttons, true);
        

        valRefEditor_changed();
    }

    //private static final String IN="in";
    //private static final String NOT_IN="not in";
    private void addOperators() {
        editOperators.blockSignals(true);
        editOperators.clear();
        editOperators.addItem("=");
        editOperators.setItemData(0, Operator.EQUAL);
        editOperators.addItem("<>");
        editOperators.setItemData(1, Operator.NOT_EQUAL);
        editOperators.addItem("is null");
        editOperators.setItemData(2, Operator.IS_NULL);
        editOperators.addItem("is not null");
        editOperators.setItemData(3, Operator.IS_NOT_NULL);
        /*editOperators.addItem(IN);
        editOperators.setItemData(4, null);
        editOperators.addItem(NOT_IN);
        editOperators.setItemData(5, null);*/
        editOperators.blockSignals(false);
        final int curIndex = editOperators.findData(operator);
        if (curIndex > 0) {
            editOperators.setCurrentIndex(curIndex);
        } else {
            editOperators.setCurrentIndex(0);
        }
    }

    private void operatorChanged() {
        final int indOper = editOperators.currentIndex();
        operator = (Operator.Enum) editOperators.itemData(indOper);
        if (isReadOnly) {
            return;
        }
        if ((operator != Operator.IS_NULL) && (operator != Operator.IS_NOT_NULL)) {
            valRefEditor.setEnabled(true);
            lbValue.setEnabled(true);
            if (valRefEditor.getValue() == null) {
                getButton(EDialogButtonType.OK).setEnabled(false);
            }
            return;
        }
        valRefEditor.setValue(null);
        valRefEditor.setEnabled(false);
        lbValue.setEnabled(false);
        getButton(EDialogButtonType.OK).setEnabled(true);
    }

    /*private void changeValEditor(){
    removeValEditors();
    if(valEditorArr==null){
    valEditorArr=   new ValEditor<String>(this,new EditMaskNone(),false,true);
    valEditorArr.setObjectName("valEditorArr");
    final QAction action =  new QAction(this);
    action.triggered.connect(this, "bntOpenDialogClick()");
    valEditorArr.addButton("...", action);
    }
    valEditorArr.setParent(this);
    valEditorArr.setEnabled(true);
    gridLayout.addWidget(valEditorArr, 2, 1);
    canAccepted(valEditorArr);
    }

    private void removeValEditors(){
    if((valRefEditor!=null) && valRefEditor.parentWidget()!=null){
    gridLayout.removeWidget(valRefEditor);
    valRefEditor.setParent(null);
    }else if((valEditorArr!=null) && valEditorArr.parentWidget()!=null){
    gridLayout.removeWidget(valEditorArr);
    valEditorArr.setParent(null);
    }
    }

    private void canAccepted(ValEditor v){
    if(v.getValue()==null)
    buttonBox.button(StandardButton.Ok).setEnabled(false);
    else
    buttonBox.button(StandardButton.Ok).setEnabled(true);
    }*/
    @SuppressWarnings("unused")
    private void parentRefChanged() {
        final int indProp = editParentProp.currentIndex();
        final ISqmlColumnDef/*RadPropertyDef*/ selectedProp = (ISqmlColumnDef)/*(RadPropertyDef)*/ editParentProp.itemData(indProp);
        addOperators();
        operatorChanged();
        /*if(selectedProp instanceof RadParentRefPropertyDef){
        RadParentRefPropertyDef propRef=(RadParentRefPropertyDef)selectedProp;
        valRefEditor.setValue(null);
        valRefEditor.setSelectorPresentation(propRef.getParentSelectorPresentation());
        }*/
        valRefEditor.setValue(null);
        valRefEditor.setSelectorPresentation(selectedProp.getSelectorPresentationClassId(), selectedProp.getSelectorPresentationId());

    }

    @Override
    public void accept() {
        final int indOperator = editOperators.currentIndex();
        operator = (Operator.Enum) editOperators.itemData(indOperator);
        //if(operator==null)
        //    str_operatop=editOperators.itemText(indOperator);

        final int indParentRef = editParentProp.currentIndex();
        parentRef = (ISqmlColumnDef)/*(RadPropertyDef)*/ editParentProp.itemData(indParentRef);

        /*if((valRefEditor.getValue()!=null)&&(valRefEditor.getValue().getPid()!=null))
        pid=valRefEditor.getValue().getPid();
        else
        pid=null;
        if(((operator==Operator.EQUAL)||(operator==Operator.NOT_EQUAL))&&(pid==null))
        return;
        super.accept();*/
        reference = valRefEditor.getValue();
        if (((operator == Operator.EQUAL) || (operator == Operator.NOT_EQUAL)) && (reference == null)) {
            return;
        }
        super.accept();
    }

    @SuppressWarnings("unused")
    private void valRefEditor_changed() {
        boolean enabl = true;
        if (valRefEditor.getValue() == null) {
            enabl = false;
        }
        if (!isReadOnly) {
            getButton(EDialogButtonType.OK).setEnabled(enabl);
        }
    }

    public ISqmlColumnDef getParentRef() {
        return parentRef;
    }

    public Operator.Enum getOperator() {
        return operator;
    }

    // public String getStrOperator(){
    //    return str_operatop;
    //}
    public Reference getEntityReference() {
        return reference == null ? null : new Reference(reference);
    }
}
