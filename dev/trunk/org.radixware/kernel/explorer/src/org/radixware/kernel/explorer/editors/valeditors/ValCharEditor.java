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

package org.radixware.kernel.explorer.editors.valeditors;

import com.trolltech.qt.gui.QWidget;
import com.trolltech.qt.gui.QLineEdit.EchoMode;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.meta.mask.EditMaskStr;
import org.radixware.kernel.common.client.meta.mask.validators.ValidationResult;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EValType;



public class ValCharEditor extends ValEditor<Character> {

    public ValCharEditor(final IClientEnvironment environment, final QWidget parent, final EditMask editMask, final boolean mandatory, final boolean readOnly) {
        super(environment, parent, editMask, mandatory, readOnly);
    }

    public ValCharEditor(final IClientEnvironment environment, final QWidget parent) {
        this(environment, parent, new EditMaskStr(), true, false);
    }


    @Override
    protected void onTextEdited(final String newText) {
        final Character newValue;
        if (newText == null || newText.isEmpty()) {
            newValue = null;
        } else {
            newValue = newText.charAt(0);
        }
        if (getEditMask().validate(getEnvironment(),newValue)==ValidationResult.ACCEPTABLE) {
            setOnlyValue(newValue);
        }
    }

    @Override
    protected void onFocusIn() {
        if (!isReadOnly()) {
            getLineEdit().setMaxLength(1);            
            final Character currentValue = getValue();
            setOnlyText(currentValue == null ? "" : currentValue.toString(), null);
        }
        super.onFocusIn();
    }

    @Override
    protected void onFocusOut() {
        super.onFocusOut();
        if (!isReadOnly()) {
            getLineEdit().setMaxLength((1 << 16) - 1);
            setOnlyText(getStringToShow(getValue()), null);
        }
    }

    @Override
    public void setReadOnly(final boolean readOnly) {
        super.setReadOnly(readOnly);
        getLineEdit().setReadOnly(readOnly);
    }

    @Override
    public void setEditMask(final EditMask editMask) {
        super.setEditMask(editMask);
        final EditMaskStr editMaskStr = (EditMaskStr) editMask;
        if (!editMaskStr.getInputMask().isEmpty()) {
            getLineEdit().setInputMask(editMaskStr.getInputMask().getPattern());
        }
        if (editMaskStr.isPassword() && getValue() != null) {
            getLineEdit().setEchoMode(EchoMode.Password);
        } else {
            getLineEdit().setEchoMode(EchoMode.Normal);
        }
    }

    @Override
    public void setValue(final Character value) {
        super.setValue(value);
        final EditMaskStr editMaskStr = (EditMaskStr) getEditMask();
        if (value != null && editMaskStr.isPassword()) {
            getLineEdit().setEchoMode(EchoMode.Password);
        } else {
            getLineEdit().setEchoMode(EchoMode.Normal);
        }
    }

    @Override
    protected String valueAsStr(final Character value) {
        return ValAsStr.toStr(value, EValType.CHAR);
    }

    @Override
    protected Character stringAsValue(final String stringVal) {
        return (Character) ValAsStr.fromStr(stringVal, EValType.CHAR);
    }
}
