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

import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QComboBox;
import com.trolltech.qt.gui.QDialog.DialogCode;
import com.trolltech.qt.gui.QFormLayout;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QSizePolicy;
import com.trolltech.qt.gui.QStackedWidget;
import com.trolltech.qt.gui.QWidget;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EDefinitionDisplayMode;
import org.radixware.kernel.common.client.enums.ETextOptionsMarker;
import org.radixware.kernel.common.client.meta.RadSelectorPresentationDef;
import org.radixware.kernel.common.client.meta.mask.EditMaskNone;
import org.radixware.kernel.common.client.meta.mask.EditMaskStr;
import org.radixware.kernel.common.client.meta.sqml.ISqmlColumnDef;
import org.radixware.kernel.common.client.types.Reference;
import org.radixware.kernel.common.client.utils.ArrFactory;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Arr;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.dialogs.ArrayEditorDialog;
import org.radixware.kernel.explorer.editors.sqmleditor.ESqlConditionOperator;
import org.radixware.kernel.explorer.editors.valeditors.ValEditor;
import org.radixware.kernel.explorer.editors.valeditors.ValRefEditor;
import org.radixware.kernel.explorer.editors.valeditors.ValStrEditor;
import org.radixware.kernel.explorer.text.ExplorerTextOptions;
import org.radixware.kernel.explorer.utils.WidgetUtils;

public final class Condition_Dialog extends ValPropEdit_Dialog {    
    
    private final QLabel lbValue;    
    private final ValStrEditor propDefEditor;
    private final QComboBox editOperators = new QComboBox(this);
    private final QStackedWidget valEditorContainer = new QStackedWidget(this);
    private final QWidget valIntervalContainer = new QWidget();
    private final ValEditor valEditor;
    private final ValEditor<String> valArrEditor;    
    private final ValEditor betweenValEditor1;
    private final ValEditor betweenValEditor2;
    private ESqlConditionOperator operator;
    private final List<Object> values = new ArrayList<>();

    public Condition_Dialog(final IClientEnvironment environment,  
                                        final ISqmlColumnDef prop, 
                                        final ESqlConditionOperator operator, 
                                        final Reference ref,
                                        final EDefinitionDisplayMode showMode,
                                        final QWidget parentWidget) {
        super(environment, parentWidget, prop, "SqmlConditionDialog", true);        
        lbValue = new QLabel(environment.getMessageProvider().translate("SqmlEditor", "Value:"), this);
        lbValue.setObjectName("lbValue");
        
        valEditor  = createValEditor(null);
        valArrEditor = new ValEditor<>(getEnvironment(), null, new EditMaskNone(), false, true);        
        betweenValEditor1 = createValEditor(valIntervalContainer);        
        betweenValEditor2 = createValEditor(valIntervalContainer);        
        
        this.operator = operator;
        
        final EditMaskStr editMask = new EditMaskStr();
        propDefEditor = new ValStrEditor(environment, this, editMask, true, true);
        final String propName = 
            showMode==EDefinitionDisplayMode.SHOW_FULL_NAMES ? prop.getShortName() : prop.getDisplayableText(showMode);
        propDefEditor.setValue(propName);
        propDefEditor.setObjectName("editLineProp");
        createUI();
        if (valEditor instanceof ValRefEditor 
            && (operator==ESqlConditionOperator.EQUAL || operator==ESqlConditionOperator.NOT_EQUAL)){
            ((ValRefEditor)valEditor).setValue(ref);
        }
    }

    private void createUI() {
        setWindowTitle(getEnvironment().getMessageProvider().translate("SqmlEditor", "Edit Condition"));        
        final QLabel lbPropName = new QLabel(getEnvironment().getMessageProvider().translate("SqmlEditor", "Property:"), this);        
        final QLabel lbOperation = new QLabel(getEnvironment().getMessageProvider().translate("SqmlEditor", "Operation:"), this);                
        final ExplorerTextOptions labelTextOptions = getLabelTextOptions();
        labelTextOptions.applyTo(lbPropName);
        labelTextOptions.applyTo(lbOperation);
        labelTextOptions.applyTo(lbValue);
        final ExplorerTextOptions editorTextOptions = getEditorTextOptions();
        editorTextOptions.applyTo(editOperators);
                
        valEditorContainer.setObjectName("rx_valEditorSpace");
        valIntervalContainer.setObjectName("rx_valIntervalContainer");        
        final QHBoxLayout intervalLayout = WidgetUtils.createHBoxLayout(valIntervalContainer);
        intervalLayout.addWidget(betweenValEditor1);
        betweenValEditor1.setObjectName("betweenValEditor1");
        intervalLayout.addWidget(betweenValEditor2);
        betweenValEditor2.setObjectName("betweenValEditor2");
        valEditorContainer.addWidget(valEditor);
        valEditor.valueChanged.connect(this,"updateOkButton()");
        valEditorContainer.addWidget(valArrEditor);
        valArrEditor.setObjectName("valEditorArr");
        valEditorContainer.addWidget(valIntervalContainer);
        valEditorContainer.setCurrentIndex(0);
        betweenValEditor1.valueChanged.connect(this, "updateOkButton()");
        betweenValEditor2.valueChanged.connect(this, "updateOkButton()");
        final QAction action = new QAction(valArrEditor);
        action.triggered.connect(this, "bntOpenDialogClick()");
        valArrEditor.addButton("...", action);
                
        editOperators.setObjectName("editOperators");
        editOperators.setSizePolicy(QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Preferred);
        addOperators();
        editOperators.currentIndexChanged.connect(this, "operatorChanged()");

        final QFormLayout formLayout = new QFormLayout();
        formLayout.addRow(lbPropName, propDefEditor);
        formLayout.addRow(lbOperation, editOperators);
        formLayout.addRow(lbValue, valEditorContainer);
        dialogLayout().addLayout(formLayout);
        addButtons(EnumSet.of(EDialogButtonType.OK, EDialogButtonType.CANCEL), true);
        operatorChanged();
        updateOkButton();
    }
    
    private ExplorerTextOptions getEditorTextOptions(){
        final EnumSet<ETextOptionsMarker> markers = 
            EnumSet.of(ETextOptionsMarker.EDITOR, ETextOptionsMarker.EDITABLE_VALUE, ETextOptionsMarker.REGULAR_VALUE);
        return (ExplorerTextOptions)getEnvironment().getTextOptionsProvider().getOptions(markers, null);
    }    

    private void addOperators() {
        editOperators.clear();
        final EValType valType = getProperty().getType();
        int currentIndex = 0;
        for (ESqlConditionOperator operator: ESqlConditionOperator.values()){
            if (operator.isApplicableToType(valType)){
                if (operator==this.operator){
                    currentIndex = editOperators.count();
                }
                editOperators.addItem(operator.getText(), operator);
            }
        }
        lbValue.setVisible(!valType.isArrayType());
        valEditorContainer.setVisible(!valType.isArrayType());
        editOperators.setCurrentIndex(currentIndex);
    }        

    private void operatorChanged() {
        values.clear();
        final int indOper = editOperators.currentIndex();        
        operator = (ESqlConditionOperator) editOperators.itemData(indOper, Qt.ItemDataRole.UserRole);
        switch(operator){
            case IS_NULL:
            case IS_NOT_NULL:{
                valEditorContainer.setCurrentIndex(0);
                lbValue.setText(getEnvironment().getMessageProvider().translate("SqmlEditor", "Value:"));
                lbValue.setEnabled(false);
                valEditor.setEnabled(false);
                valEditor.clear();                
                break;
            }
            case IN:
            case NOT_IN:{
                valEditorContainer.setCurrentIndex(1);
                lbValue.setText(getEnvironment().getMessageProvider().translate("SqmlEditor", "Value:"));
                lbValue.setEnabled(true);
                break;
            }
            case BETWEEN:{
                valEditorContainer.setCurrentIndex(2);
                lbValue.setText(getEnvironment().getMessageProvider().translate("SqmlEditor", "Range:"));
                lbValue.setEnabled(true);
                break;
            }
            default:{
                valEditorContainer.setCurrentIndex(0);
                final boolean isEnabled = checkRestriction();
                lbValue.setText(getEnvironment().getMessageProvider().translate("SqmlEditor", "Value:"));
                lbValue.setEnabled(isEnabled);
                valEditor.setEnabled(isEnabled);
            }
        }
        updateOkButton();
    }
    
    private boolean checkRestriction() {
        if (getProperty().getType() == EValType.PARENT_REF) {
            final Id selectorPresentationId = getProperty().getSelectorPresentationId();
            if ( selectorPresentationId != null) {
                final RadSelectorPresentationDef def = 
                    getEnvironment().getApplication().getDefManager().getSelectorPresentationDef(selectorPresentationId);
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
        final EValType propType = getProperty().getType();
        if (propType == EValType.PARENT_REF) {
            final RadSelectorPresentationDef def = 
                getEnvironment().getApplication().getDefManager().getSelectorPresentationDef(getProperty().getSelectorPresentationId());
            dialog = new ArrayEditorDialog(getEnvironment(), def, false, this);
        } else {
            dialog = new ArrayEditorDialog(getEnvironment(), propType, null, false, this);
        }
        final Arr arr = ArrFactory.DEFAULT.createArray(propType);
        arr.addAll(values);
        dialog.setWindowTitle(getEnvironment().getMessageProvider().translate("SqmlEditor", "Values List"));
        dialog.setCurrentValue(arr);
        dialog.setEditMask(getProperty().getEditMask());
        dialog.setMandatory(true);
        dialog.setDuplicatesEnabled(false);
        if (dialog.exec() == DialogCode.Accepted.value()) {
            values.clear();
            values.addAll(dialog.getCurrentValue());
            final String forEdit = arrToString(values);
            valArrEditor.setValue(forEdit);
            updateOkButton();
        }
    }

    private static String arrToString(final List<Object> arr) {
        final StringBuilder sb=new StringBuilder();
        for (Object item: arr) {
            if (item!=null) {
                if (sb.length()>0){
                    sb.append(", ");
                }            
                sb.append(item);
            }
        }
        return sb.toString();
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public void accept() {
        switch(operator){
            case BETWEEN:{
                if (!betweenValEditor1.checkInput() || !betweenValEditor2.checkInput()){
                    return;
                }
                values.clear();
                values.add(betweenValEditor1.getValue());
                values.add(betweenValEditor2.getValue());
                break;
            }
            case IN:
            case NOT_IN:
                break;
            default:
                if (!valEditor.checkInput()){
                    return;
                }
                values.clear();
                final Object value = valEditor.getValue();
                if (value!=null){
                    values.add(value);
                }
        }
        super.accept();
    }
    
    private void updateOkButton(){
        if (getButton(EDialogButtonType.OK)!=null){
            switch(operator){
                case IS_NULL:
                case IS_NOT_NULL:{
                    getButton(EDialogButtonType.OK).setEnabled(true);
                    break;
                }
                case IN:
                case NOT_IN:{
                    getButton(EDialogButtonType.OK).setEnabled(valArrEditor.getValue()!=null);
                    break;
                }
                case BETWEEN:{
                    getButton(EDialogButtonType.OK).setEnabled(betweenValEditor1.getValue() != null && betweenValEditor2.getValue() != null);
                    break;
                }
                default:
                    getButton(EDialogButtonType.OK).setEnabled(valEditor.getValue()!=null);                    
            }
        }
    }

    public ESqlConditionOperator getOperator() {
        return operator;
    }

    public boolean isParentCondition() {
        return getProperty().getType()==EValType.PARENT_REF 
                   && operator != null 
                   && operator.getParentConditionOperator()!=null;
    }
    
    public List<Object> getValues(){
        return Collections.unmodifiableList(values);
    }
}