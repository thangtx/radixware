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
import org.radixware.kernel.common.client.meta.mask.validators.EValidatorState;
import org.radixware.kernel.common.client.meta.mask.validators.ValidationResult;
import org.radixware.kernel.common.client.editors.xmleditor.model.XmlValueValidator;
import org.radixware.wps.views.editors.valeditors.ValStrEditorController;


final class XmlValStrEditorController extends ValStrEditorController {

    private final SchemaType schemaType;
    private boolean validationEnabled;

    public XmlValStrEditorController(final IClientEnvironment env, final SchemaType schemaType) {
        super(env);
        this.schemaType = schemaType;
    }

    @Override
    public void setValue(String value) {
        validationEnabled = true;
        super.setValue(value);
        if (getInputBox().isPrintable()){
            removeMemo();
        }else{
            addMemo();
        }
    }

    public SchemaType getSchemaType() {
        return schemaType;
    }

    @Override
    protected ValidationResult calcValidationResult(String value) {
        final ValidationResult result = super.calcValidationResult(value);
        if (schemaType == null) {
            return result;
        } else if (result.getState() == EValidatorState.ACCEPTABLE && validationEnabled) {
            return XmlValueValidator.INSTANCE.validate(value, schemaType);
        } else {
            return result;
        }
    }
}
