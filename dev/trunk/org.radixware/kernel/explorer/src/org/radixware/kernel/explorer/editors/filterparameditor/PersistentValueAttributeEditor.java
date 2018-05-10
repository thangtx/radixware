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

package org.radixware.kernel.explorer.editors.filterparameditor;

import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QSizePolicy;
import com.trolltech.qt.gui.QSizePolicy.Policy;
import com.trolltech.qt.gui.QWidget;
import java.util.EnumSet;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.ETextOptionsMarker;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.meta.mask.EditMaskConstSet;
import org.radixware.kernel.common.client.meta.mask.EditMaskRef;
import org.radixware.kernel.common.client.meta.mask.validators.EValidatorState;
import org.radixware.kernel.common.client.meta.sqml.ISqmlColumnDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlParameter;
import org.radixware.kernel.common.client.meta.sqml.ISqmlParameterPersistentValue;
import org.radixware.kernel.common.client.meta.sqml.ISqmlSelectorPresentationDef;
import org.radixware.kernel.common.client.types.ArrPid;
import org.radixware.kernel.common.client.types.ArrRef;
import org.radixware.kernel.common.client.types.FilterParameterPersistentValue;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.client.types.Reference;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.editors.valeditors.MultiValEditor;
import org.radixware.kernel.explorer.editors.valeditors.ValArrEditor;
import org.radixware.kernel.explorer.editors.valeditors.ValRefEditor;
import org.radixware.kernel.explorer.text.ExplorerTextOptions;
import org.radixware.schemas.xscml.Sqml;



final class PersistentValueAttributeEditor extends AbstractAttributeEditor<ValAsStr> {

    private final QLabel label;
    private final MultiValEditor editor;
    private boolean isReadonly;
    private Id editorPresentationId;
    private Sqml additionalSelectorCondition;

    protected PersistentValueAttributeEditor(final IClientEnvironment environment, final boolean isReadonly, final QWidget parent) {
        super(environment);
        setObjectName("attrEditor_" + getAttribute().name());
        label = new QLabel(getAttribute().getTitle(environment), parent);
        label.setSizePolicy(QSizePolicy.Policy.Maximum, QSizePolicy.Policy.Fixed);
        label.setObjectName("label");
        editor = new MultiValEditor(environment, parent);
        editor.setObjectName("editor");
        label.setBuddy(editor);
        applySettings(true);
        editor.setSizePolicy(Policy.Minimum, Policy.Fixed);
        editor.currentChanged.connect(this, "onValueChanged()");
    }

    @SuppressWarnings("unused")
    private void onValueChanged() {
        attributeChanged.emit(this);
    }
    
    private boolean isValueValid;

    private void applySettings(final boolean isValid) {
        isValueValid = isValid;        
        if (editor.isReadOnly() || !editor.isEnabled()) {
            editor.removeTextOptionsMarkers(ETextOptionsMarker.INVALID_VALUE);
            setupLabelTextOptions(label, true);            
        } else {
            final ETextOptionsMarker lbMarker;
            if (!isValid) {
                lbMarker = ETextOptionsMarker.INVALID_VALUE;
                editor.addTextOptionsMarkers(ETextOptionsMarker.INVALID_VALUE);
            } else if (editor.isMandatory() && getAttributeValue() == null) {
                lbMarker = ETextOptionsMarker.MANDATORY_VALUE;                
            } else {
                lbMarker = ETextOptionsMarker.REGULAR_VALUE;                
            }
            ExplorerTextOptions.Factory.getLabelOptions(lbMarker).applyTo(label);
        }
    }

    @Override
    public boolean updateParameter(final ISqmlParameter parameter) {
        if (editor.isEnabled() && !editor.isReadOnly()) {
            if (!editor.checkInput()){
                return false;
            }
            if (parameter.isMandatory() && getAttributeValue() == null) {
                final MessageProvider messageProvider = getEnvironment().getMessageProvider();
                final String title = messageProvider.translate("SqmlEditor", "Constant Value Was not Specified!");
                final String message = messageProvider.translate("SqmlEditor", "Please specify constant value");
                getEnvironment().messageInformation(title,message);                        
                editor.setFocus();
                return false;
            }
            final FilterParameterPersistentValue value;
            if (parameter.getType() == EValType.PARENT_REF) {
                final ValRefEditor refEditor = (ValRefEditor) editor.getCurrentValEditor();
                if (refEditor.getEntityModel() != null) {
                    value = new FilterParameterPersistentValue(refEditor.getEntityModel(), isReadonly);
                } else if (refEditor.getValue() != null) {
                    value = new FilterParameterPersistentValue(refEditor.getValue(), editorPresentationId, isReadonly);
                } else {
                    value = new FilterParameterPersistentValue(null, EValType.PARENT_REF, isReadonly);
                }
            } else if (parameter.getType()==EValType.ARR_REF){
                final ArrRef arrRef = (ArrRef)editor.getCurrentValEditor().getValue();
                if (arrRef==null){
                    value = new FilterParameterPersistentValue(null, EValType.ARR_REF, isReadonly);
                }else{
                    value = new FilterParameterPersistentValue(arrRef, editorPresentationId, isReadonly);
                }
            } else {
                final Object valObj = editor.getValue() == null ? null : editor.getValue().toObject(parameter.getType());
                value = new FilterParameterPersistentValue(valObj, parameter.getType(), isReadonly);
            }
            parameter.setPersistentValue(value);
        } else {
            parameter.setPersistentValue(null);
        }
        return true;
    }
    
    private void setEditMaskRef(final EditMaskRef newEditMask){        
        final EditMask currentMask = editor.getEditMask();
        final EditMaskRef actualMask; 
        if (currentMask instanceof EditMaskRef){
            if (additionalSelectorCondition!=null){
                actualMask = (EditMaskRef)EditMask.newCopy(newEditMask);
                actualMask.setCondition(additionalSelectorCondition);
            }else{
                actualMask = newEditMask;
            }
        }else{
            actualMask = newEditMask;
        }
        editor.setEditMask(actualMask);
    }

    @Override
    public void updateEditor(final ISqmlParameter parameter) {        
        final ISqmlParameterPersistentValue value = parameter.getPersistentValue();
        isReadonly = !parameter.canHavePersistentValue() || value==null || value.isReadOnly();
        editor.showValEditor(parameter.getType(), parameter.getEditMask(), parameter.isMandatory(), isReadonly);
        setEnabled(value != null);
        if (parameter.getType() == EValType.PARENT_REF) {
            final ValRefEditor valEditor = (ValRefEditor) editor.getCurrentValEditor();
            try {
                if (value != null) {
                    value.isValid(getEnvironment());
                }
                if (parameter.getParentSelectorPresentationId() != null) {
                    valEditor.setSelectorPresentation(parameter.getParentSelectorPresentationClassId(), parameter.getParentSelectorPresentationId());
                    valEditor.setCondition(parameter.getParentSelectorAdditionalCondition());
                }
                editorPresentationId = value == null ? null : value.getEditorPresentationId();
                final Object valueObject = value==null ? null : value.getValObject();
                if (valueObject==null){
                    valEditor.setValue(null);
                }else if (valueObject instanceof Reference){
                    valEditor.setValue((Reference) valueObject);
                }else if (valueObject instanceof Pid){
                    valEditor.setValue(new Reference((Pid)valueObject, "", valueObject.toString()));
                }
                applySettings(value == null || value.isValid(getEnvironment()));
            } catch (DefinitionError error) {
                valEditor.setValue(null);
                applySettings(true);
            }
        } else if (parameter.getType() == EValType.ARR_REF){
            final ValArrEditor valEditor = (ValArrEditor) editor.getCurrentValEditor();
            try {
                if (value != null) {
                    value.isValid(getEnvironment());
                }
                final EditMaskRef mask = new EditMaskRef();
                if (parameter.getParentSelectorPresentationId() != null) {
                    mask.setSelectorPresentationId(parameter.getParentSelectorPresentationId());
                    mask.setCondition(parameter.getParentSelectorAdditionalCondition());
                }
                editorPresentationId = value == null ? null : value.getEditorPresentationId();
                mask.setEditorPresentationId(editorPresentationId);
                valEditor.setEditMask(mask);
                final Object valueObject = value==null ? null : value.getValObject();
                if (valueObject==null){
                    valEditor.setValue(null);
                }else if (valueObject instanceof ArrRef){
                    valEditor.setValue((ArrRef)valueObject);
                }else if (valueObject instanceof ArrPid){
                    valEditor.setValue(((ArrPid)valueObject).toArrRef());
                }
                applySettings(value == null || value.isValid(getEnvironment()));
            } catch (DefinitionError error) {
                valEditor.setValue(null);
                applySettings(true);
            }            
        } else {
            editor.setValue(value == null ? null : ValAsStr.Factory.newInstance(value.getValObject(), parameter.getType()));
            applySettings(true);
        }
    }

    @Override
    public EFilterParamAttribute getAttribute() {
        return EFilterParamAttribute.PERSISTENT_VALUE;
    }

    @Override
    public ValAsStr getAttributeValue() {
        if (editor.getCurrentValType() == EValType.PARENT_REF) {
            final ValRefEditor refEditor = (ValRefEditor) editor.getCurrentValEditor();
            final Pid pid = refEditor.getValue() == null ? null : refEditor.getValue().getPid();
            return pid == null ? null : ValAsStr.Factory.newInstance(pid.toString(), EValType.STR);
        } else if (editor.getCurrentValType() == EValType.ARR_REF) {
            final ArrRef arr = (ArrRef)editor.getCurrentValEditor().getValue();
            return arr==null ? null : ValAsStr.Factory.newInstance(arr.toArrStr(false), EValType.ARR_STR);
        } else {
            return editor.getValue();
        }
    }

    @Override
    public EnumSet<EFilterParamAttribute> getBaseAttributes() {
        return EnumSet.of(EFilterParamAttribute.VALUE_TYPE,
                EFilterParamAttribute.EDIT_MASK,
                EFilterParamAttribute.ENUM,
                EFilterParamAttribute.PROPERTY,
                EFilterParamAttribute.IS_MANDATORY,
                EFilterParamAttribute.SELECTOR_PRESENTATION,
                EFilterParamAttribute.ADDITIONAL_SELECTOR_CONDITION,
                EFilterParamAttribute.PERSISTENT_VALUE_DEFINED);
    }

    @Override
    public void onBaseAttributeChanged(final AbstractAttributeEditor baseEditor) {
        switch (baseEditor.getAttribute()) {
            case PROPERTY: {
                final TargetPropertyAttributeEditor propertyEditor = (TargetPropertyAttributeEditor) baseEditor;
                final ISqmlColumnDef column = propertyEditor.getAttributeValue();
                if (column != null) {
                    final EValType valType = column.getType();
                    if (valType == EValType.PARENT_REF) {
                        editor.showValEditor(valType, null, column.isNotNull(), isReadonly);
                        final ValRefEditor valEditor = (ValRefEditor) editor.getCurrentValEditor();
                        valEditor.setSelectorPresentation(column.getSelectorPresentationClassId(), column.getSelectorPresentationId());
                        editor.setValue(null);
                        setEnabled(false);
                        applySettings(true);
                    } else if (valType==EValType.ARR_REF){
                        editor.showValEditor(valType, null, column.isNotNull(), isReadonly);
                        final EditMaskRef mask = new EditMaskRef(column.getSelectorPresentationClassId());
                        editor.setValue(null);
                        setEditMaskRef(mask);
                        setEnabled(false);
                        applySettings(true);
                    } else {
                        editor.showValEditor(column.getType(), column.getEditMask(), column.isNotNull(), isReadonly);
                        editor.setValue(null);
                        setEnabled(false);
                        applySettings(true);
                    }
                } else {
                    setEnabled(false);
                }
                break;
            }
            case IS_MANDATORY: {
                final NotNullAttributeEditor notNullEditor = (NotNullAttributeEditor) baseEditor;
                if (notNullEditor.getAttributeValue() != null) {
                    editor.getCurrentValEditor().setMandatory(notNullEditor.getAttributeValue());
                    applySettings(isValueValid);
                }
                break;
            }
            case ADDITIONAL_SELECTOR_CONDITION:{
                final AdditionalSelectorConditionAttributeEditor conditionEditor = (AdditionalSelectorConditionAttributeEditor) baseEditor;
                additionalSelectorCondition = conditionEditor.getAttributeValue();
                final EditMaskRef editMask;
                if (editor.getEditMask() instanceof EditMaskRef){
                    editMask = (EditMaskRef)EditMask.newCopy(editor.getEditMask());
                    editMask.setCondition(additionalSelectorCondition);
                    editor.setEditMask(editMask);
                }
                break;
            }
            case SELECTOR_PRESENTATION: {
                final SelectorPresentationAttributeEditor selectorPresEditor =
                        (SelectorPresentationAttributeEditor) baseEditor;
                final ISqmlSelectorPresentationDef presentationDef = selectorPresEditor.getAttributeValue();
                editor.setValue(null);
                final EditMaskRef editMask = presentationDef==null ? new EditMaskRef() : new EditMaskRef(presentationDef.getId());
                setEditMaskRef(editMask);
                applySettings(true);
                break;
            }
            case ENUM: {
                final EnumAttributeEditor enumEditor = (EnumAttributeEditor) baseEditor;
                final boolean isMandatory = editor.isMandatory();
                if (enumEditor.getAttributeValue() != null) {
                    final EditMask mask =
                                        new EditMaskConstSet(enumEditor.getAttributeValue().getId(), null, null, null);
                    editor.showValEditor(editor.getCurrentValType(), mask, isMandatory, isReadonly);
                } else {
                    editor.showValEditor(editor.getCurrentValType(), null, isMandatory, isReadonly);
                }
                editor.setValue(null);
                applySettings(true);
                break;
            }
            case VALUE_TYPE: {
                final ValueTypeAttributeEditor valTypeEditor = (ValueTypeAttributeEditor) baseEditor;
                final EValType valType = valTypeEditor.getAttributeValue();
                final boolean isMandatory = editor.isMandatory();
                if (valType == EValType.PARENT_REF) {
                    editor.showValEditor(valType, null, isMandatory, isReadonly);
                    editor.setValue(null);
                    setEnabled(false);
                } else {
                    setEnabled(false);
                    editor.showValEditor(valTypeEditor.getAttributeValue(), null, isMandatory, isReadonly);
                    editor.setValue(null);
                }
                applySettings(true);
                break;
            }
            case PERSISTENT_VALUE_DEFINED: {
                final PersistentValDefinedAttributeEditor definedEditor = (PersistentValDefinedAttributeEditor) baseEditor;
                if (definedEditor.getAttributeValue() != null) {
                    if (definedEditor.getAttributeValue()){
                        setEnabled(true);
                        editor.setReadOnly(false);
                        isReadonly = false;
                    }else{
                        setEnabled(false);
                    }
                    editor.setValue(null);
                    applySettings(true);
                }
                break;
            }
            case EDIT_MASK: {
                final EditMask newEditMask  = (EditMask) baseEditor.getAttributeValue();
                if (newEditMask.getType()==editor.getEditMask().getType()){
                    final Object curValue = editor.getCurrentValEditor().getValue();
                    if (newEditMask.validate(getEnvironment(), curValue).getState()!=EValidatorState.ACCEPTABLE){
                        editor.setValue(null);
                    }
                    if (newEditMask instanceof EditMaskRef){
                        setEditMaskRef((EditMaskRef)newEditMask);
                    }else{
                        editor.setEditMask(newEditMask);
                    }
                }
                else{
                    editor.setValue(null);
                }
                EValType valType = editor.getCurrentValType();
                if (!newEditMask.getSupportedValueTypes().contains(valType)){
                    valType = newEditMask.getSupportedValueTypes().iterator().next();
                }
                final boolean isMandatory = editor.isMandatory();
                editor.showValEditor(valType, newEditMask, isMandatory, isReadonly);
                break;
           }
        }
    }

    private void setEnabled(final boolean isEnabled) {
        label.setEnabled(isEnabled);
        editor.setEnabled(isEnabled);
    }

    @Override
    public QLabel getLabel() {
        return label;
    }

    @Override
    public QWidget getEditorWidget() {
        return editor;
    }

    @Override
    public void free() {
        editor.close();
    }
}
