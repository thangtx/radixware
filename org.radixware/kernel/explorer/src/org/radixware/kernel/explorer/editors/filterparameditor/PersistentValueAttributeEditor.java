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
import org.radixware.kernel.common.client.meta.mask.validators.EValidatorState;
import org.radixware.kernel.common.client.meta.sqml.ISqmlColumnDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlParameter;
import org.radixware.kernel.common.client.meta.sqml.ISqmlParameterPersistentValue;
import org.radixware.kernel.common.client.types.FilterParameterPersistentValue;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.client.types.Reference;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSelectorPresentationDef;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.editors.valeditors.MultiValEditor;
import org.radixware.kernel.explorer.editors.valeditors.ValRefEditor;
import org.radixware.kernel.explorer.text.ExplorerTextOptions;



final class PersistentValueAttributeEditor extends AbstractAttributeEditor<ValAsStr> {

    private final QLabel label;
    private final MultiValEditor editor;
    private boolean isReadonly;
    private Id editorPresentationId;

    protected PersistentValueAttributeEditor(final IClientEnvironment environment, final boolean isReadonly, final QWidget parent) {
        super(environment);
        setObjectName("attrEditor_" + getAttribute().name());
        label = new QLabel(getAttribute().getTitle(), parent);
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
    public boolean updateParameter(ISqmlParameter parameter) {
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
                    value = new FilterParameterPersistentValue(refEditor.getEntityModel());
                } else if (refEditor.getValue() != null) {

                    value = new FilterParameterPersistentValue(refEditor.getValue(), editorPresentationId);
                } else {
                    value = new FilterParameterPersistentValue(null, EValType.PARENT_REF);
                }
            } else {
                final Object valObj = editor.getValue() == null ? null : editor.getValue().toObject(parameter.getType());
                value = new FilterParameterPersistentValue(valObj, parameter.getType());
            }
            parameter.setPersistentValue(value);
        } else {
            parameter.setPersistentValue(null);
        }
        return true;
    }

    @Override
    public void updateEditor(final ISqmlParameter parameter) {
        isReadonly = !parameter.canHavePersistentValue();
        final ISqmlParameterPersistentValue value = parameter.getPersistentValue();
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
                }
                editorPresentationId = value == null ? null : value.getEditorPresentationId();
                valEditor.setValue(value == null ? null : (Reference) value.getValObject());
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
                EFilterParamAttribute.PERSISTENT_VALUE_DEFINED);
    }

    @Override
    public void onBaseAttributeChanged(AbstractAttributeEditor baseEditor) {
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
            case SELECTOR_PRESENTATION: {
                final SelectorPresentationAttributeEditor selectorPresEditor =
                        (SelectorPresentationAttributeEditor) baseEditor;
                final AdsSelectorPresentationDef presentationDef = selectorPresEditor.getAttributeValue();
                final ValRefEditor refEditor = (ValRefEditor) editor.getCurrentValEditor();
                editor.setValue(null);
                if (presentationDef != null) {
                    refEditor.setSelectorPresentation(presentationDef.getOwnerClass().getId(), presentationDef.getId());
                }
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
                    setEnabled(definedEditor.getAttributeValue());
                    editor.setValue(null);
                    applySettings(isValueValid);
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
                    editor.setEditMask(newEditMask);
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
