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

package org.radixware.kernel.explorer.editors.xml.new_.view.editors;

import com.trolltech.qt.gui.QDialog;
import com.trolltech.qt.gui.QWidget;
import org.apache.xmlbeans.SchemaType;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.meta.mask.validators.EValidatorState;
import org.radixware.kernel.common.client.meta.mask.validators.ValidationResult;
import org.radixware.kernel.common.client.editors.xmleditor.model.XmlValueConverter;
import org.radixware.kernel.common.client.editors.xmleditor.model.XmlValueValidator;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Arr;
import org.radixware.kernel.explorer.dialogs.ArrayEditorDialog;
import org.radixware.kernel.explorer.editors.valeditors.ValArrEditor;
import org.radixware.kernel.explorer.env.Application;


final class XmlValArrEditor extends ValArrEditor {

    private final SchemaType schemaType;
    private final EValType arrType;
    private EditMask editMask;

    public XmlValArrEditor(final IClientEnvironment environment, final EValType valType, 
            final boolean mandatory, final boolean readOnly, final SchemaType schemaType) {
        super(environment, valType, null, null, mandatory, readOnly);
        this.schemaType = schemaType;
        this.arrType = valType.isArrayType() ? valType : valType.getArrayType();
    }

    public SchemaType getSchemaType() {
        return schemaType;
    }

    @Override
    public void setEditMask(final EditMask editMask) {
        this.editMask = editMask == null ? null : EditMask.newCopy(editMask);
    }

    @Override
    protected ValidationResult calcValidationResult(final Arr value) {
        ValidationResult result;
        if (editMask == null) {
            result = super.calcValidationResult(value);
        } else {
            result = editMask.validate(getEnvironment(), value);
        }
        if (schemaType == null) {
            return result;
        } else if (result.getState() == EValidatorState.ACCEPTABLE && value != null) {
            final SchemaType itemType = schemaType.getListItemType();
            for (Object itemValue : value) {
                if (itemValue != null) {
                    final String itemValAsStr;
                    itemValAsStr =
                            XmlValueConverter.getInstance().valueToXmlString(itemValue, editMask == null ? getEditMask() : editMask, getEnvironment(), itemType);
                    if (itemValAsStr.isEmpty()){
                        result = ValidationResult.Factory.newInvalidResult(getEnvironment().getMessageProvider().translate("XmlEditor","List item cannot be empty"));
                        break;
                    }
                    result = XmlValueValidator.INSTANCE.validate(itemValAsStr, itemType);
                    if (result.getState() != EValidatorState.ACCEPTABLE) {
                        break;
                    }
                }else{
                    result = ValidationResult.Factory.newInvalidResult(getEnvironment().getMessageProvider().translate("XmlEditor","List item cannot be null"));
                    break;                    
                }
            }
            return result;
        } else {
            return result;
        }
    }

    @Override
    public void edit() {
        QWidget parentWidget = (QWidget) parent();
        if (parentWidget == null) {
            parentWidget = Application.getMainWindow();
        }
        final ArrayEditorDialog dialog = new ArrayEditorDialog(getEnvironment(), arrType, null, isReadOnly(), parentWidget);
        dialog.setPredefinedValues(getPredefinedItemValues());
        dialog.setItemMandatory(true);
        if (editMask != null) {
            dialog.setEditMask(editMask);
        }
        dialog.setCurrentValue(getValue());
        if (QDialog.DialogCode.resolve(dialog.exec()) == QDialog.DialogCode.Accepted) {
            setValue(dialog.getCurrentValue());
            editingFinished.emit(getValue());
        }
    }
}