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

import com.trolltech.qt.core.QRegExp;
import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QClipboard.Mode;
import com.trolltech.qt.gui.QMenu;
import com.trolltech.qt.gui.QRegExpValidator;
import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.mask.EditMaskNone;
import org.radixware.kernel.common.client.meta.mask.validators.ValidationResult;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.IllegalArgumentError;
import org.radixware.kernel.common.types.Bin;

import org.radixware.kernel.explorer.utils.ValueConverter;

/**
 * ValBinEditor - редактор для значений типа Bin.
 */
public class ValBinEditor extends ValEditor<Bin> {

    protected final static QRegExp HEX_VALUE_PATTERN = new QRegExp("((\\d|[a-f]|[A-F])(\\d|[a-f]|[A-F]) ?)+");

    public ValBinEditor(final IClientEnvironment environment, final QWidget parent, final EditMaskNone editMask, final boolean mandatory, final boolean readOnly) {
        super(environment, parent, editMask, mandatory, readOnly);
        getLineEdit().setValidator(new QRegExpValidator(HEX_VALUE_PATTERN, this));
    }
    
    public ValBinEditor(final IClientEnvironment environment, final QWidget parent){
        this(environment,parent,new EditMaskNone(),true,false);
    }

    @Override
    protected void onTextEdited(final String newText) {
        final Bin newValue;
        try {
            newValue = convertText2Bin(newText);
        } catch (IllegalArgumentError error) {
            return;
        }
        if (getEditMask().validate(getEnvironment(),newValue)==ValidationResult.ACCEPTABLE) {
            setOnlyValue(newValue);
        }
    }

    private static Bin convertText2Bin(final String text) {
        final String hexadecimalString = text.replace(" ", "");
        if (hexadecimalString.isEmpty()) {
            return null;
        }
        return ValueConverter.hexadecimalString2Bin(hexadecimalString, null);
    }

    @Override
    protected void onFocusIn() {
        if (!isReadOnly()) {
            final Bin bin = getValue();
            setOnlyText(bin == null ? "" : ValueConverter.arrByte2Str(bin.get(), " "), null);
        }
        super.onFocusIn();
    }

    @Override
    protected void onFocusOut() {
        super.onFocusOut();
        if (!isReadOnly()) {
            setOnlyText(getStringToShow(getValue()), null);
        }
    }

    @Override
    protected QMenu createCustomContextMenu(QMenu standardMenu) {
        final int pasteActionIdx = isReadOnly() ? 2 : 5;
        if (standardMenu.actions().size() > pasteActionIdx) {
            final QAction copySolidAction = new QAction(getEnvironment().getMessageProvider().translate("ValBinEditor", "Copy without separators"), standardMenu);
            copySolidAction.setEnabled(getLineEdit().hasSelectedText());
            copySolidAction.triggered.connect(this, "copyWithoutSeparators()");
            standardMenu.insertAction(standardMenu.actions().get(pasteActionIdx), copySolidAction);
        }
        return standardMenu;
    }

    @SuppressWarnings("unused")
    private void copyWithoutSeparators() {
        final String selectedText = getLineEdit().selectedText();
        if (!selectedText.isEmpty()) {
            blockSignals(true);
            getLineEdit().blockSignals(true);
            try {
                QApplication.clipboard().setText(selectedText.replace(" ", ""), Mode.Clipboard);
            } finally {
                blockSignals(false);
                getLineEdit().blockSignals(false);
            }
        }
    }

    @Override
    protected boolean hasCustomContextMenu() {
        return true;
    }

    @Override
    public void setReadOnly(final boolean readOnly) {
        super.setReadOnly(readOnly);
        getLineEdit().setReadOnly(readOnly);
    }

    @Override
    protected Bin stringAsValue(final String stringVal) {
        return (Bin) ValAsStr.fromStr(stringVal, EValType.BIN);
    }

    @Override
    protected String valueAsStr(final Bin value) {
        return ValAsStr.toStr(value, EValType.BIN);
    }
}
