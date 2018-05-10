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

package org.radixware.kernel.common.client.editors.property;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EReferenceStringFormat;
import org.radixware.kernel.common.client.enums.ETextOptionsMarker;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.exceptions.ClientException;
import org.radixware.kernel.common.client.meta.RadPropertyDef;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.meta.mask.EditMaskRef;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.types.ArrRef;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.client.types.Reference;
import org.radixware.kernel.common.client.utils.IValAsStrConverter;
import org.radixware.kernel.common.client.views.IProxyPropEditor;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.kernel.common.client.widgets.IButton.ClickHandler;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EEditMaskType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.exceptions.WrongFormatError;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.types.Id;


public abstract class ProxyPropEditorController {        
    
    private final IClientEnvironment environment;
    private final IProxyPropEditor propEditor;
    
    private boolean inErrorMode;
    private IButton btnClear;
    private IValAsStrConverter valueConverter;
    private EReferenceStringFormat referenceFormat;
    
    public ProxyPropEditorController(final IClientEnvironment environment, 
                                     final IProxyPropEditor propEditor,
                                     final IValAsStrConverter converter,
                                     final EReferenceStringFormat refFormat){
        this.environment = environment;
        this.propEditor = propEditor;
        this.valueConverter = converter;
        this.referenceFormat = refFormat==null ? EReferenceStringFormat.DEFAULT : refFormat;
        if (!getProperty().getType().equals(EValType.CLOB) && !getProperty().getType().equals(EValType.STR)) {
            throw new IllegalArgumentException("Cannot create proxy editor for type: " + getProperty().getType() + ". Property type must be one of EValType.STR or EValType.CLOB.");
        }
    }
    
    private Property getProperty(){
        return propEditor.getProperty();
    }
    
    private EValType getValueType(){
        return propEditor.getValueType();
    }
    
    private EditMask getEditMask(){
        return propEditor.getEditMask();
    }
    
    private void showError(final Throwable exception, final String valueAsStr){
        if (isInErrorMode()){
            updateErrorView(exception, valueAsStr);
        }else{
            inErrorMode = true;
            switchToErrorView(exception, valueAsStr);            
        }
    }
    
    private void showValue(){
        inErrorMode = false;
        btnClear = null;
        switchToEditor(getValueType(), getEditMask());
        if (wasBinded()){
            propEditor.refresh(null);
        }
    }
        
    
    public final boolean isInErrorMode(){
        return inErrorMode;
    }                
    
    private boolean checkValueTypeCompatible(final String value, final boolean quiet){
        if (value == null || getEditMask().getSupportedValueTypes().contains(getValueType())){
            return true;
        }else{
            if (!quiet){
                final String messageTemplate = 
                    environment.getMessageProvider().translate("Value", "%1$s edit mask is not compatible with value %2$s");
                final String message = 
                    String.format(messageTemplate, getEditMask().getType().getAsStr(),getValueType().getName());
                environment.getTracer().debug(message);
                showError(new IllegalUsageError(message), value);
            }
            return false;
        }
    }    
    
    private void processWrongFormatError(final WrongFormatError error, final String value){
        traceWrongFormatError(error, value, false);
        showError(error, value);
    }
    
    private void traceWrongFormatError(final WrongFormatError error, final String value, final boolean warning){
        final String stackTrace = ClientException.exceptionStackToString(error);
        final String messageTemplate = 
            environment.getMessageProvider().translate("Value", "Failed to convert value '%1$s' to %2$s type:\n%3$s");
        final String message = String.format(messageTemplate, value, getValueType().getName(), stackTrace);
        if (warning){
            environment.getTracer().warning(message);
        }else{
            environment.getTracer().debug(message);
        }
    }
    
    private void updateClearValueButton(final boolean isMandatory, final boolean isReadonly){
        final boolean isButtonVisible = !isMandatory && !isReadonly;
        if (btnClear==null && isButtonVisible){
            //final ExceptionViewController editor = (ExceptionViewController)getValEditor();
            btnClear = environment.getApplication().getWidgetFactory().newToolButton();
            btnClear.setToolTip(environment.getMessageProvider().translate("Value", "Clear Value"));
            final Icon clearIcon =
                environment.getApplication().getImageManager().getIcon(ClientIcon.CommonOperations.CLEAR);            
            btnClear.setIcon(clearIcon);
            btnClear.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(IButton source) {
                    clearValue();
                }
            });           
            propEditor.addButton(btnClear);
        }else if (btnClear!=null){
            btnClear.setVisible(isButtonVisible);
        }
    }
        
    private Id getReferencedTableId(final EditMaskRef editMask){
        try{
            return editMask.getReferencedTableId(environment);
        }catch(DefinitionError error){
            environment.getTracer().error(error);
            return null;
        }
    }
    
    private Id getReferencedTableId(){
        return getReferencedTableId((EditMaskRef)getEditMask());
    }
        
    private static Id getPresentationId(final EditMaskRef editMask){
        if (editMask.getSelectorPresentationId()==null){
            if (editMask.getEditorPresentationIds().isEmpty()){
                return null;
            }else{
                return editMask.getEditorPresentationIds().get(0);
            }
        }else{
            return editMask.getSelectorPresentationId();
        }        
    }
    
    private Object actualizeValue(final Object value){
        final EditMaskRef editMask = (EditMaskRef)getEditMask();
        final Id tableId = getReferencedTableId(editMask);
        if (tableId==null){
            return value;
        }else{
            try{
                if (value instanceof Reference){
                    return ((Reference)value).actualizeTitle(environment, tableId, getPresentationId(editMask));
                }else if (value instanceof ArrRef){
                    return ((ArrRef)value).actualizeTitles(environment, tableId, getPresentationId(editMask));
                }else{
                    return value;
                }
            }catch(InterruptedException exception){
                return value;
            }catch(ServiceClientException | IllegalArgumentException exception){
                environment.getTracer().error(exception);
                return value;
            }
        }
    }
    
    private Object typifyValue(final String valAsStr){
        if (valAsStr==null){
            return null;
        }
        final Object value;
        if (valueConverter==null){
            if (referenceFormat==EReferenceStringFormat.DEFAULT || 
                (getValueType()!=EValType.PARENT_REF && getValueType()!=EValType.ARR_REF)
                ){
                value = IValAsStrConverter.DEFAULT.valAsStr2Obj(ValAsStr.Factory.loadFrom(valAsStr), getValueType());
            }else if (getValueType()==EValType.PARENT_REF){
                if (referenceFormat==EReferenceStringFormat.OBJECT_PID_WITH_TABLE_ID){
                    value = new Reference(Pid.fromStr(valAsStr));
                }else{//referenceFormat=EReferenceStringFormat.RAW_OBJECT_PID
                    final Id tableId = getReferencedTableId();
                    if (tableId==null){
                        throw new WrongFormatError("Failed to restore object reference from string \'"+valAsStr+"\'. Unable to get referenced tableId");
                    }
                    value = new Reference(new Pid(tableId,valAsStr));
                }
            }else{//getValueType()=EValType.ARR_REF
                if (referenceFormat==EReferenceStringFormat.OBJECT_PID_WITH_TABLE_ID){
                    value = IValAsStrConverter.DEFAULT.valAsStr2Obj(ValAsStr.Factory.loadFrom(valAsStr), EValType.ARR_REF);
                }else{//referenceFormat=EReferenceStringFormat.RAW_OBJECT_PID
                    final Id tableId = getReferencedTableId();
                    final ArrRef arrRef = new ArrRef();
                    for (String rawPidStr: ArrStr.fromValAsStr(valAsStr)){
                        arrRef.add(new Reference(new Pid(tableId,rawPidStr)));
                    }
                    value = arrRef;
                }
            }
        }else{
            value = valueConverter.valAsStr2Obj(ValAsStr.Factory.loadFrom(valAsStr), getValueType());
        }      
        if (getValueType()==EValType.PARENT_REF){
            if (value instanceof Pid){
                return new Reference((Pid)value);
            }else if (value instanceof Reference==false){
                throw new WrongFormatError("Failed to restore object reference from string \'"+valAsStr+"\'");
            }
        }        
        return value;
    }
    
    private void clearValue(){
        getProperty().setValueObject(null);
    }        
    
    public final void changeValueType(final EValType newValType, final EditMask newMask){
        propEditor.finishEdit();
        final String value = (String)getProperty().getValueObject();
        if (getEditMask().getType()==newMask.getType() && getValueType()==newValType){
            setEditMask(newMask);
            if (checkValueTypeCompatible(value, false) && wasBinded()){
                propEditor.refresh(getProperty());
            }                        
        }else{
            setValueType(newValType);
            setEditMask(newMask);
            if (checkValueTypeCompatible(value, false)){
                showValue();
            }
        }
    }
    
    public final List<Object> updateEditor(final Object value, final Object initialValue, final PropEditorOptions options) {// value - String        
        if (isInErrorMode()){
            if (checkValueTypeCompatible((String)value, true)){
                if (value==null){
                    showValue();//recursive call of updateEditor
                    return null;
                }else if ( !Objects.equals(value, getCurrentValueInEditor()) ){//recheck
                    try{
                        typifyValue((String)value);
                        showValue();//recursive call of updateEditor
                        return null;
                    }catch(WrongFormatError error){
                        processWrongFormatError(error, (String)value);
                    }
                }
            }
            updateClearValueButton(options.isMandatory(), options.isReadOnly());
            return null;
        }else{
            Object typifiedValue;
            if (value==null){
                typifiedValue = null;
            }else{
                try{
                    typifiedValue = typifyValue((String)value);
                }catch(WrongFormatError error){
                    processWrongFormatError(error, (String)value);
                    updateClearValueButton(options.isMandatory(), options.isReadOnly());
                    return null;
                }
            }
            if (getEditMask().getType()==EEditMaskType.OBJECT_REFERENCE
                && !Objects.equals(typifiedValue, getCurrentValueInEditor())){
                typifiedValue = actualizeValue(typifiedValue);
            }
            
            Object typifiedInitialValue;
            if (initialValue==null){
                typifiedInitialValue = null;
            }else{
                try{
                    typifiedInitialValue = typifyValue((String)initialValue);
                }catch(WrongFormatError error){
                    traceWrongFormatError(error, (String)initialValue, false);
                    typifiedInitialValue = typifiedValue;
                }
            }                  
            
            options.setEditMask(getEditMask());
            final List<Object> predefinedValues = getProperty().getPredefinedValues();
            if (predefinedValues!=null && !predefinedValues.isEmpty()
                && RadPropertyDef.isPredefinedValuesSupported(getValueType(), getEditMask().getType())){
                final List<Object> typifiedPredefinedValues = new LinkedList<>();
                for (Object valAsStr: predefinedValues){
                    if (valAsStr instanceof String){
                        try{
                            final Object typifiedPredefinedValue = typifyValue((String)valAsStr);
                            typifiedPredefinedValues.add(typifiedPredefinedValue);
                        }catch(WrongFormatError error){
                            traceWrongFormatError(error, (String)valAsStr, true);
                        }
                    }else if (valAsStr!=null){
                        typifiedPredefinedValues.add(valAsStr);
                    }
                }
                options.setPredefinedValues(typifiedPredefinedValues);
            }else{
                options.setPredefinedValues(null);
            }
            final List<Object> result = new ArrayList<>();
            result.add(typifiedValue);
            result.add(typifiedInitialValue);
            return result;
        }
    }    
    
    public final EnumSet<ETextOptionsMarker> getTextOptionsMarkers(final EnumSet<ETextOptionsMarker> defaultMarkers) {
        if (isInErrorMode()){
            defaultMarkers.add(ETextOptionsMarker.INVALID_VALUE);
        }
        return defaultMarkers;
    }

    public final IValAsStrConverter getValueConverter() {
        return valueConverter;
    }

    public final void setValueConverter(final IValAsStrConverter valueConverter) {
        if (valueConverter!=this.valueConverter){
            this.valueConverter = valueConverter;
            if (wasBinded()){
                propEditor.refresh(null);
            }
        }
    }

    public final EReferenceStringFormat getReferenceFormat() {
        return referenceFormat;
    }

    public final void setReferenceFormat(final EReferenceStringFormat referenceFormat) {
        if (this.referenceFormat != referenceFormat){
            this.referenceFormat = referenceFormat;
            if (wasBinded()){
                propEditor.refresh(null);
            }
        }
    }
    
    public final String writeValueToString(final Object value){
        if (value==null){
            return null;
        }
        if (valueConverter==null){
            if (referenceFormat==EReferenceStringFormat.DEFAULT || 
                (getValueType()!=EValType.PARENT_REF && getValueType()!=EValType.ARR_REF)
                ){
                return IValAsStrConverter.DEFAULT.obj2ValAsStr(value, getValueType()).toString();
            }else if (getValueType()==EValType.PARENT_REF){                
                if (referenceFormat==EReferenceStringFormat.OBJECT_PID_WITH_TABLE_ID){
                    return ((Reference)value).getPid().toStr();
                }else{//referenceFormat=EReferenceStringFormat.RAW_OBJECT_PID
                    return ((Reference)value).getPid().toString();
                }
            }else{//getValueType()=EValType.ARR_REF
                return ((ArrRef)value).toArrStr(referenceFormat==EReferenceStringFormat.OBJECT_PID_WITH_TABLE_ID).toString();
            }            
        }else{
            return valueConverter.obj2ValAsStr(value, getValueType()).toString();
        }
    }
    
    protected abstract void updateErrorView(final Throwable error, final String value);
    
    protected abstract void switchToErrorView(final Throwable error, final String value);
    
    protected abstract void switchToEditor(final EValType valType, final EditMask mask);
    
    protected abstract void setEditMask(final EditMask editMask);
    
    protected abstract void setValueType(final EValType valueType);
    
    protected abstract Object getCurrentValueInEditor();    
    
    protected abstract boolean wasBinded();
}
