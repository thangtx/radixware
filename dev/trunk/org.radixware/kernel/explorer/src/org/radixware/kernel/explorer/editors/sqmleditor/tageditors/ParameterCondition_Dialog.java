/*
* Copyright (c) 2008-2016, Compass Plus Limited. All rights reserved.
*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at http://mozilla.org/MPL/2.0/.
* This Source Code is distributed WITHOUT ANY WARRANTY; including any 
* implied warranties but not limited to warranty of MERCHANTABILITY 
* or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
* License, v. 2.0. for more details.
*/

package org.radixware.kernel.explorer.editors.sqmleditor.tageditors;

import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QFormLayout;
import com.trolltech.qt.gui.QLabel;
import java.math.BigDecimal;
import java.util.EnumSet;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EDefinitionDisplayMode;
import org.radixware.kernel.common.client.enums.ETextOptionsMarker;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.meta.RadEnumPresentationDef;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.meta.mask.EditMaskInt;
import org.radixware.kernel.common.client.meta.mask.EditMaskList;
import org.radixware.kernel.common.client.meta.mask.EditMaskConstSet;
import org.radixware.kernel.common.client.meta.mask.EditMaskNum;
import org.radixware.kernel.common.client.meta.mask.EditMaskStr;
import org.radixware.kernel.common.client.meta.mask.validators.ValidationResult;
import org.radixware.kernel.common.client.meta.sqml.ISqmlParameter;
import org.radixware.kernel.common.client.meta.sqml.ISqmlParameters;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.enums.EIfParamTagOperator;
import org.radixware.kernel.common.enums.ESelectorRowStyle;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.exceptions.WrongFormatError;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.dialogs.ExplorerDialog;
import org.radixware.kernel.explorer.editors.sqmleditor.SqmlEditor;
import org.radixware.kernel.explorer.editors.valeditors.MultiValEditor;
import org.radixware.kernel.explorer.editors.valeditors.ValListEditor;
import org.radixware.kernel.explorer.editors.xscmleditor.AbstractXscmlEditor;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.text.ExplorerTextOptions;
import org.radixware.schemas.xscml.Sqml;


public class ParameterCondition_Dialog extends ExplorerDialog{
    
    private final Sqml.Item.IfParam ifParamItem;
    private final ISqmlParameters parameters;
    private final EDefinitionDisplayMode displayMode;
    private final ValListEditor operatorSelector;
    private final MultiValEditor valEditor;
    private final ExplorerTextOptions labelOptions;
    private final QLabel lbValue = new QLabel(this);
    private final boolean isReadOnly;
    
    public ParameterCondition_Dialog(final ISqmlParameters parameters, 
                                                       final Sqml.Item.IfParam ifParam,  
                                                       final EDefinitionDisplayMode displayMode,
                                                       final AbstractXscmlEditor editText,
                                                       final IClientEnvironment environment){
        super(environment, editText, null);        
        this.parameters = parameters;
        if (ifParam==null){
            ifParamItem = Sqml.Item.IfParam.Factory.newInstance();
        }else{
            ifParamItem = (Sqml.Item.IfParam)ifParam.copy();
        }
        this.displayMode = displayMode;
        isReadOnly = editText.isReadOnly();
        operatorSelector = new ValListEditor(getEnvironment(), this, new EditMaskList(), true, isReadOnly);        
        valEditor = new MultiValEditor(getEnvironment(), this);
        final EnumSet<ETextOptionsMarker> textMarkers = EnumSet.of(ETextOptionsMarker.EDITOR, ETextOptionsMarker.LABEL);
        textMarkers.add(isReadOnly ? ETextOptionsMarker.READONLY_VALUE : ETextOptionsMarker.REGULAR_VALUE);
        labelOptions = 
            (ExplorerTextOptions) getEnvironment().getTextOptionsProvider().getOptions(textMarkers, ESelectorRowStyle.NORMAL);
        setupUi();
    }
    
    public ParameterCondition_Dialog(final ISqmlParameters parameters, 
                                                       final ISqmlParameter parameter,  
                                                       final EDefinitionDisplayMode displayMode,
                                                       final AbstractXscmlEditor editText,
                                                       final IClientEnvironment environment){
        this(parameters, createSqmlTagForParam(parameter, environment), displayMode, editText, environment);
    }    
    
    private void setupUi(){        
        setWindowIcon(ExplorerIcon.getQIcon(SqmlEditor.SqmlEditorIcons.IMG_INSERT_IF_TAG));        
        final MessageProvider mp = getEnvironment().getMessageProvider();
        setWindowTitle(mp.translate("SqmlEditor", "Condition"));
        
        final QFormLayout formLayout = new QFormLayout();
        final EditMaskList paramsMask = new EditMaskList();
        
        for (ISqmlParameter parameter: parameters.getAll()){
            final Icon icon = getEnvironment().getApplication().getImageManager().getIcon(parameter.getIcon());
            final String paramIdAsStr = parameter.getId().toString();
            paramsMask.addItem(parameter.getDisplayableText(displayMode), paramIdAsStr, icon);            
        }                        
        final ValListEditor paramsSelector = new ValListEditor(getEnvironment(), this,  paramsMask, true, isReadOnly);
        paramsSelector.setObjectName("rx_filter_param_selector");
        final String currentParamId = ifParamItem.getParamId()==null ? null : ifParamItem.getParamId().toString();        
        paramsSelector.setValue(currentParamId);
        paramsSelector.valueChanged.connect(this,"onParamSelected(Object)");
        final QLabel lbParameteter = new QLabel(mp.translate("SqmlEditor", "Parameter:"), this);
        lbParameteter.setObjectName("rx_parameter_label");
        labelOptions.applyTo(lbParameteter);
        formLayout.addRow(lbParameteter, paramsSelector);        
        
        operatorSelector.setObjectName("rx_operator_selector");
        final QLabel lbOperator = new QLabel(mp.translate("SqmlEditor", "Operator:"), this);
        lbOperator.setObjectName("rx_operator_label");
        labelOptions.applyTo(lbOperator);        
        formLayout.addRow(lbOperator, operatorSelector);
        updateOperatorSelector();
        
        valEditor.setObjectName("rx_value_editor");
        lbValue.setText(mp.translate("SqmlEditor", "Value:"));
        lbValue.setObjectName("rx_value_label");
        formLayout.addRow(lbValue, valEditor);
        updateValueEditor(false);                
        
        dialogLayout().addLayout(formLayout);
        final EnumSet<EDialogButtonType> buttons;
        if (isReadOnly){
            buttons = EnumSet.of(EDialogButtonType.CLOSE);
        }else{
            buttons = EnumSet.of(EDialogButtonType.OK, EDialogButtonType.CANCEL);
        }
        addButtons(buttons, true);
        
        setAttribute(Qt.WidgetAttribute.WA_DeleteOnClose, true);
        setAutoHeight(true);        
        updateOkBtn();
    }
    
    private ISqmlParameter getSqmlParameter(){
        final Id paramId = ifParamItem.getParamId();
        if (paramId==null){
            return null;
        }else{
            return parameters.getParameterById(paramId);
        }
    }
    
    private void updateOperatorSelector(){
        operatorSelector.valueChanged.disconnect(this);
        try{                        
            final ISqmlParameter parameter = getSqmlParameter();
            final EnumSet<EIfParamTagOperator> operators = getOperatorsForParameter(parameter);
            final EditMaskList editMask = new EditMaskList();
            editMask.setNoValueStr("");            
            if (operators.isEmpty()){
                operatorSelector.setEditMask(editMask);
                operatorSelector.setValue(null);
                operatorSelector.setEnabled(false);                
            }else{
                final boolean isArray = parameter.getType().isArrayType();
                final MessageProvider mp = getEnvironment().getMessageProvider();
                final String currentValue = ifParamItem.getOperation()==null ? null : ifParamItem.getOperation().getValue();
                boolean currentValueAccessible = false;
                for (EIfParamTagOperator operator: operators){
                    editMask.addItem(getOperatorTitle(operator, mp, isArray), operator.getValue());
                    if (operator.getValue().equals(currentValue)){
                        currentValueAccessible = true;
                    }
                }
                operatorSelector.setEditMask(editMask);
                if (currentValue==null){
                    operatorSelector.setValue(null);
                    updateOkBtn();
                }else{
                    if (currentValueAccessible){
                        operatorSelector.setValue(currentValue);
                    }else if (operators.isEmpty()){
                        operatorSelector.setValue(null);
                        updateOkBtn();
                    }else{
                        final EIfParamTagOperator firstOperator = operators.iterator().next();
                        operatorSelector.setValue(firstOperator.getValue());
                        ifParamItem.setOperation(firstOperator);
                    }
                }                
            }
        }finally{
            operatorSelector.valueChanged.connect(this,"onOperatorSelected(Object)");
        }
    }
    
    private void updateValueEditor(final boolean reinitValue){
        valEditor.valueChanged.disconnect(this);
        try{
            final ISqmlParameter parameter = getSqmlParameter();
            final EValType valType = parameter==null ? null : parameter.getType();
            final EIfParamTagOperator operator = ifParamItem.getOperation();
            if (valType !=null && (operator==EIfParamTagOperator.EQUAL || operator==EIfParamTagOperator.NOT_EQUAL)){
                final EValType itemType = valType.isArrayType() ? valType.getArrayItemType() : valType;
                final EditMask mask = parameter.getEditMask();
                valEditor.showValEditor(itemType, mask, true, isReadOnly);//NOPMD
                Object value;                
                if (!reinitValue && ifParamItem.getValue()!=null && !ifParamItem.getValue().isEmpty()){
                    try{
                        value = ValAsStr.fromStr(ifParamItem.getValue(), itemType);                        
                        if (mask.validate(getEnvironment(), value)!=ValidationResult.ACCEPTABLE){
                            value = getInitialValue(itemType, mask, getEnvironment());
                        }
                    }catch(WrongFormatError error){
                        value = getInitialValue(itemType, mask, getEnvironment());
                    }                    
                }else{
                    value = getInitialValue(itemType, mask, getEnvironment());
                }                
                final ValAsStr valAsStr = value==null ? null : ValAsStr.Factory.newInstance(value, itemType);
                valEditor.setValue(valAsStr);
                ifParamItem.setValue(valAsStr==null ? null : valAsStr.toString());
                valEditor.setEnabled(true);
                lbValue.setEnabled(true);
            }else{
                valEditor.showValEditor(EValType.STR, new EditMaskStr(), false, true);
                ifParamItem.setValue(null);
                valEditor.setEnabled(false);
                lbValue.setEnabled(false);                
            }
            labelOptions.applyTo(lbValue);
        }finally{
            valEditor.valueChanged.connect(this, "onValueChanged(ValAsStr)");
        }
    }
    
    private void updateOkBtn(){
        if (!isReadOnly){
            getButton(EDialogButtonType.OK).setEnabled(ifParamItem.getParamId()!=null && ifParamItem.getOperation()!=null);
        }
    }
    
    @SuppressWarnings("PMD.MissingBreakInSwitch")
    private static Object getInitialValue(final EValType valType, final EditMask mask, final IClientEnvironment environment){
        if (mask instanceof EditMaskList){
            final List<EditMaskList.Item> items = ((EditMaskList)mask).getItems();
            return items.isEmpty() ? null : items.get(0).getValue();
        }else if (mask instanceof EditMaskConstSet){
            final RadEnumPresentationDef.Items items = ((EditMaskConstSet)mask).getItems(environment.getApplication());
            return items.isEmpty() ? null : items.getItem(0).getValue();
        }                
        switch(valType){
            case BOOL:
                return Boolean.FALSE;
            case CLOB:
            case STR:
                return "";
            case DATE_TIME:
                return environment.getCurrentServerTime();
            case INT:{                
                final EditMaskInt maskInt = (EditMaskInt)mask;
                return 0l<maskInt.getMinValue() || 0l>maskInt.getMaxValue() ? maskInt.getMinValue() : Long.valueOf(0);
            }case NUM:{
                final EditMaskNum maskNum = (EditMaskNum)mask;
                if (BigDecimal.ZERO.compareTo(maskNum.getMinValue())<0
                    || BigDecimal.ZERO.compareTo(maskNum.getMaxValue())>0
                   ){
                    return maskNum.getMinValue();
                }else{
                    return BigDecimal.ZERO;
                }
            }default:
                return null;
        }
    }

    @SuppressWarnings("unused")
    private void onParamSelected(final Object value){
        if (value instanceof String){
            final Id paramId = Id.Factory.loadFrom((String)value);
            ifParamItem.setParamId(paramId);
            updateOperatorSelector();
            updateValueEditor(true);            
        }
    }
    
    @SuppressWarnings("unused")
    private void onOperatorSelected(final Object value){
        if (value instanceof String){
            final EIfParamTagOperator operator;
            try{
                operator = EIfParamTagOperator.getForValue((String)value);
            }catch(NoConstItemWithSuchValueError error){
                return;
            }
            ifParamItem.setOperation(operator);
            updateOkBtn();
            updateValueEditor(false);
        }
    }
    
    @SuppressWarnings("unused")
    private void onValueChanged(final ValAsStr value){
        if (valEditor.isEnabled()){
            ifParamItem.setValue(value==null ? null : value.toString());
        }else{
            ifParamItem.setValue(null);
        }
    }
    
    @SuppressWarnings("PMD.MissingBreakInSwitch")
    private static String getOperatorTitle(final EIfParamTagOperator operator, final MessageProvider mp, final boolean isArray){
        switch(operator){
            case EQUAL:{
                if (isArray){
                    return mp.translate("SqmlEditor", "Contains");                    
                }else{
                    return mp.translate("SqmlEditor", "Equals");
                }
            } case NOT_EQUAL:{
                if (isArray){
                    return mp.translate("SqmlEditor", "Not contains");
                }else{
                    return mp.translate("SqmlEditor", "Not equals");
                }
            } case NULL: 
                return mp.translate("SqmlEditor", "Not defined");
            case NOT_NULL:
                return mp.translate("SqmlEditor", "Defined");
            case EMPTY:
                return mp.translate("SqmlEditor", "Is empty");
            case NOT_EMPTY:
                return mp.translate("SqmlEditor", "Is not empty");
            default:
                return "Unknown operator "+operator.getName();
        }
    }
    
    private static Sqml.Item.IfParam createSqmlTagForParam(final ISqmlParameter parameter, final IClientEnvironment environment){
        final Sqml.Item.IfParam ifParam = Sqml.Item.IfParam.Factory.newInstance();
        ifParam.setParamId(parameter.getId());
        final EnumSet<EIfParamTagOperator> operators = getOperatorsForParameter(parameter);
        if (!operators.isEmpty()){
            ifParam.setOperation(operators.iterator().next());
        }
        final EValType valType = parameter.getType();
        if (valType!=null){
            final Object defaultValue = getInitialValue(valType, parameter.getEditMask(), environment);
            if (defaultValue!=null){
                ifParam.setValue(ValAsStr.toStr(defaultValue, valType));
            }
        }
        return ifParam;
    }
    
    private static EnumSet<EIfParamTagOperator> getOperatorsForParameter(final ISqmlParameter parameter){
        if (parameter==null || parameter.getType()==null){
            return EnumSet.noneOf(EIfParamTagOperator.class);
        }else{
            final EnumSet<EIfParamTagOperator> operators = EnumSet.allOf(EIfParamTagOperator.class);
            final EValType valType = parameter.getType();
            if (!valType.isArrayType()){
                operators.remove(EIfParamTagOperator.EMPTY);
                operators.remove(EIfParamTagOperator.NOT_EMPTY);
            } if (valType==EValType.ARR_REF || valType==EValType.PARENT_REF){
                operators.remove(EIfParamTagOperator.EQUAL);
                operators.remove(EIfParamTagOperator.NOT_EQUAL);
            }
            return operators;
        }
    }
    
    public Sqml.Item.IfParam getIfParamTag(){
        return (Sqml.Item.IfParam)ifParamItem.copy();
    }
}
