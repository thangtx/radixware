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
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.meta.mask.EditMaskConstSet;
import org.radixware.kernel.common.client.meta.mask.validators.EValidatorState;
import org.radixware.kernel.common.client.meta.sqml.ISqmlColumnDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlModifiableParameter;
import org.radixware.kernel.common.client.meta.sqml.ISqmlParameter;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.explorer.editors.valeditors.MultiValEditor;
import org.radixware.kernel.explorer.text.ExplorerTextOptions;


final class DefaultValueAttributeEditor extends AbstractAttributeEditor<ValAsStr> {

    private final QLabel label;
    private final MultiValEditor editor;
    private final boolean isReadonly;

    protected DefaultValueAttributeEditor(IClientEnvironment environment, final boolean isReadonly, final QWidget parent) {
        super(environment);
        setObjectName("attrEditor_" + getAttribute().name());
        label = new QLabel(getAttribute().getTitle(environment), parent);
        label.setSizePolicy(QSizePolicy.Policy.Maximum, QSizePolicy.Policy.Fixed);
        label.setObjectName("label");
        editor = new MultiValEditor(environment, parent);
        editor.setObjectName("editor");
        label.setBuddy(editor);
        this.isReadonly = isReadonly;
        applySettings();
        editor.setSizePolicy(Policy.Minimum, Policy.Fixed);
        editor.currentChanged.connect(this, "onValueChanged()");
    }

    @SuppressWarnings("unused")
    private void onValueChanged() {
        attributeChanged.emit(this);
    }

    private void applySettings() {
        if (isReadonly) {
            ExplorerTextOptions.Factory.getOptions(ETextOptionsMarker.READONLY_VALUE).applyTo(editor);
            ExplorerTextOptions.Factory.getLabelOptions(ETextOptionsMarker.READONLY_VALUE).applyTo(label);
        } else {
            ExplorerTextOptions.Factory.getOptions(ETextOptionsMarker.REGULAR_VALUE).applyTo(editor);
            ExplorerTextOptions.Factory.getLabelOptions(ETextOptionsMarker.REGULAR_VALUE).applyTo(label);        }
    }

    @Override
    public boolean updateParameter(ISqmlParameter parameter) {
        if (editor.isEnabled() && (parameter instanceof ISqmlModifiableParameter)) {
            if (!editor.checkInput()){
                return false;
            }
            ((ISqmlModifiableParameter) parameter).setInitialValue(editor.getValue());
        }
        return true;
    }

    @Override
    public void updateEditor(ISqmlParameter parameter) {
        editor.showValEditor(parameter.getType(), parameter.getEditMask(), false, isReadonly);
        applySettings();
        editor.setValue(parameter.getInitialVal());
    }

    @Override
    public EFilterParamAttribute getAttribute() {
        return EFilterParamAttribute.DEFAULT_VALUE;
    }

    @Override
    public ValAsStr getAttributeValue() {
        return editor.getValue();
    }

    @Override
    public EnumSet<EFilterParamAttribute> getBaseAttributes() {
        return EnumSet.of(EFilterParamAttribute.VALUE_TYPE,
                          EFilterParamAttribute.EDIT_MASK,
                          EFilterParamAttribute.ENUM,
                          EFilterParamAttribute.PROPERTY);
    }

    @Override
    public void onBaseAttributeChanged(AbstractAttributeEditor baseEditor) {
        switch (baseEditor.getAttribute()) {
            case PROPERTY: {
                final TargetPropertyAttributeEditor propertyEditor = (TargetPropertyAttributeEditor) baseEditor;
                final ISqmlColumnDef column = propertyEditor.getAttributeValue();
                if (column != null) {
                    final EValType valType = column.getType();
                    if (valType == EValType.PARENT_REF || valType == EValType.ARR_REF) {
                        editor.showValEditor(EValType.STR, null, false, isReadonly);
                        setEnabled(false);
                    } else {
                        setEnabled(true);
                        editor.showValEditor(column.getType(), column.getEditMask(), false, isReadonly);
                        applySettings();
                    }
                } else {
                    setEnabled(false);
                }
                break;
            }
            case ENUM: {
                final EnumAttributeEditor enumEditor = (EnumAttributeEditor) baseEditor;
                if (enumEditor.getAttributeValue() != null) {
                    final EditMask mask = new EditMaskConstSet(enumEditor.getAttributeValue().getId(), null, null, null);
                    editor.showValEditor(editor.getCurrentValType(), mask, false, isReadonly);
                } else {
                    editor.showValEditor(editor.getCurrentValType(), null, false, isReadonly);
                }
                applySettings();
                break;
            }
            case VALUE_TYPE: {
                final ValueTypeAttributeEditor valTypeEditor = (ValueTypeAttributeEditor) baseEditor;
                final EValType valType = valTypeEditor.getAttributeValue();
                if (valType == EValType.PARENT_REF || valType == EValType.ARR_REF) {
                    editor.showValEditor(EValType.STR, null, false, isReadonly);
                    setEnabled(false);
                } else {
                    setEnabled(true);
                    editor.showValEditor(valType, null, false, isReadonly);
                    applySettings();
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
