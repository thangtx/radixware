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

import org.apache.xmlbeans.SchemaType;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.mask.EditMaskStr;
import org.radixware.kernel.common.client.meta.mask.validators.EValidatorState;
import org.radixware.kernel.common.client.meta.mask.validators.ValidationResult;
import org.radixware.kernel.common.client.editors.xmleditor.model.XmlValueValidator;
import org.radixware.kernel.explorer.editors.valeditors.ValStrEditor;


final class XmlValStrEditor extends ValStrEditor {

    private final SchemaType schemaType;

    public XmlValStrEditor(final IClientEnvironment environment, final EditMaskStr editMaskStr,
            final boolean mandatory, final boolean readOnly, final SchemaType schemaType) {
        super(environment, null, editMaskStr, mandatory, readOnly);
        this.schemaType = schemaType;
        getLineEdit().setReplaceNonPrintable(true);
        getLineEdit().setReplacementCharacter('\u2395');
    }

    public SchemaType getSchemaType() {
        return schemaType;
    }

    @Override
    public void setValue(final String value) {
        super.setValue(value);
        if (value!=null && value.contains("\n")){
            addMemo();
        }else{
            removeMemo();
        }        
    }
    
    

    @Override
    protected ValidationResult calcValidationResult(final String value) {
        final ValidationResult result = super.calcValidationResult(value);
        if (schemaType == null) {
            return result;
        } else if (result.getState() == EValidatorState.ACCEPTABLE) {
            return XmlValueValidator.INSTANCE.validate(value, schemaType);
        } else {
            return result;
        }
    }
}