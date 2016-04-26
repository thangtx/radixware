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

import java.math.BigDecimal;
import org.apache.xmlbeans.SchemaType;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.meta.mask.EditMaskNum;
import org.radixware.kernel.common.client.meta.mask.validators.EValidatorState;
import org.radixware.kernel.common.client.meta.mask.validators.ValidationResult;
import org.radixware.kernel.common.client.editors.xmleditor.model.XmlValueConverter;
import org.radixware.kernel.common.client.editors.xmleditor.model.XmlValueValidator;
import org.radixware.kernel.explorer.editors.valeditors.ValNumEditor;


final class XmlValNumEditor extends ValNumEditor {

    private final SchemaType schemaType;

    public XmlValNumEditor(final IClientEnvironment environment, final EditMaskNum maskNum,
            final boolean mandatory, final boolean readOnly, final SchemaType schemaType) {
        super(environment, null, maskNum, mandatory, readOnly);
        this.schemaType = schemaType;
        if (maskNum.getPrecision()==0){
            showSpinButtons();
        }
    }

    @Override
    public void setEditMask(EditMask editMask) {
        super.setEditMask(editMask);
        final EditMaskNum editMaskNum = (EditMaskNum)getEditMask();
        if (editMaskNum.getPrecision()==0){
            showSpinButtons();
        }else{
            hideSpinButtons();
        }
    }
    
    public SchemaType getSchemaType() {
        return schemaType;
    }

    @Override
    protected ValidationResult calcValidationResult(final BigDecimal value) {
        final ValidationResult result = super.calcValidationResult(value);
        if (schemaType == null) {
            return result;
        } else if (result.getState() == EValidatorState.ACCEPTABLE) {
            final String valAsStr = 
                XmlValueConverter.getInstance().valueToXmlString(value, getEditMask(), getEnvironment(), schemaType);
            return XmlValueValidator.INSTANCE.validate(valAsStr, schemaType);
        } else {
            return result;
        }
    }
}