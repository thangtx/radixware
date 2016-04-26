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

package org.radixware.wps.views.editor.xml.editors;

import org.apache.xmlbeans.SchemaType;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.meta.mask.validators.EValidatorState;
import org.radixware.kernel.common.client.meta.mask.validators.ValidationResult;
import org.radixware.kernel.common.client.views.IArrayEditorDialog;
import org.radixware.kernel.common.client.editors.xmleditor.model.XmlValueConverter;
import org.radixware.kernel.common.client.editors.xmleditor.model.XmlValueValidator;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Arr;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.dialogs.ArrayEditorDialog;
import org.radixware.wps.views.editors.valeditors.ValArrEditorController;


final class XmlValArrEditorController extends ValArrEditorController {

    private final SchemaType schemaType;
    private boolean validationEnabled;
    private final EValType valType;
    private EditMask mask;

    @SuppressWarnings("unchecked")
    public XmlValArrEditorController(final IClientEnvironment env, final EValType valType, final Class<?> valClass, final SchemaType schemaType) {
        super(env, valType, valClass);
        this.valType = valType;
        this.schemaType = schemaType;
    }

    public SchemaType getSchemaType() {
        return schemaType;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setValue(Object value) {
        validationEnabled = true;
        super.setValue(value);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected ValidationResult calcValidationResult(Object value) {
        ValidationResult result = super.calcValidationResult(value);
        if (schemaType == null) {
            return result;
        } else if (result.getState() == EValidatorState.ACCEPTABLE && value != null && validationEnabled) {
            final SchemaType itemType = schemaType.getListItemType();
            for (Object itemValue : (Arr) value) {
                if (itemValue != null) {
                    final String itemValAsStr =
                            XmlValueConverter.getInstance().valueToXmlString(itemValue, getEditMask(), getEnvironment(), itemType);
                    if (itemValAsStr.isEmpty()) {
                        result = ValidationResult.Factory.newInvalidResult(getEnvironment().getMessageProvider().translate("XmlEditor","List item cannot be empty"));
                        break;
                    }
                    result = XmlValueValidator.INSTANCE.validate(itemValAsStr, itemType);
                    if (result.getState() != EValidatorState.ACCEPTABLE) {
                        break;
                    }
                } else {
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
    protected IArrayEditorDialog createEditorDialog() {
        final WpsEnvironment wpsEnv = (WpsEnvironment) getEnvironment();
        final ArrayEditorDialog result =
                new ArrayEditorDialog(wpsEnv, getValType(), null, isReadOnly(), wpsEnv.getDialogDisplayer());
        result.setItemMandatory(true);
        if (mask != null) {
            result.setEditMask(mask);
        }
        return result;
    }

    public EValType getValType() {
        return valType;
    }

    public EditMask getMask() {
        return mask;
    }
}