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

package org.radixware.wps.views.editors.valeditors;

import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.meta.mask.EditMaskStr;
import org.radixware.kernel.common.meta.InputMask;
import org.radixware.kernel.common.client.meta.mask.validators.ValidationResult;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.kernel.common.client.widgets.IMemoController;
import org.radixware.wps.rwt.DropDownTextAreaDelegate;
import org.radixware.wps.rwt.InputBox;
import org.radixware.wps.rwt.InputBox.InvalidStringValueException;
import org.radixware.wps.rwt.InputBox.ValueController;


public class ValStrEditorController extends InputBoxController<String, EditMaskStr> {

    private ValueController<String> valueController;
    private IButton memoButton;
    private IMemoController memoController;
    private DropDownTextAreaDelegate dropDownDelegate;

    public ValStrEditorController(final IClientEnvironment env) {
        super(env);
        setEditMask(new EditMaskStr());
    }

    public IMemoController getMemoController() {
        return memoController;
    }

    public void setMemoController(final IMemoController memoController) {
        this.memoController = memoController;
        if (dropDownDelegate != null) {
            dropDownDelegate.setMemoController(memoController);
        }
    }
    
    @Override
    protected ValueController<String> createValueController() {
        valueController = new ValueController<String>() {

            @Override
            public String getValue(final String text) throws InvalidStringValueException {
                final EditMaskStr editMask = getEditMask();
                if (!editMask.getInputMask().isEmpty()) {
                    return editMask.getInputMask().stripDisplayString(editMask.getInputMask().applyTo(text), editMask.isSaveSeparators());
                } else {
                    return text;
                }
            }
        };
        return valueController;
    }

    @Override
    public void setEditMask(final EditMaskStr editMask) {
        if (!editMask.getInputMask().isEmpty() && editMask.getInputMask().getPattern().contains("\u0000")) {
            final EditMaskStr newEditMask = (EditMaskStr) EditMask.newCopy(editMask);
            //0 code is not supported in HTML, so replacing that symbol with special space
            final String newPattern = editMask.getInputMask().getPattern().replace('\0', '\u2007');
            newEditMask.setInputMask(InputMask.Factory.newInstance(newPattern));
            super.setEditMask(newEditMask);
        } else {
            super.setEditMask(editMask);
        }
    }

    @Override
    protected void applyEditMask(final InputBox box) {
        super.applyEditMask(box);
        final EditMaskStr editMask = getEditMask();
        if (editMask.getInputMask().isEmpty()) {
            box.setMaxLength(editMask.getMaxLength());
        }
        box.setPassword(editMask.isPassword());
    }

    @Override
    protected String calcFocusedText(final String value, final EditMaskStr editMask) {
        if (editMask.getInputMask().isEmpty()) {
            return super.calcFocusedText(value, editMask);
        } else {
            return editMask.getInputMask().applyTo(value);
        }
    }

    public void addMemo() {
        if (memoButton == null) {
            dropDownDelegate  = new DropDownTextAreaDelegate();
            dropDownDelegate.setMemoController(memoController);
            memoButton = getInputBox().addDropDownDelegate(dropDownDelegate);
        } else {
            memoButton.setVisible(true);
        }
        doValidation();
    }

    public void removeMemo() {
        if (memoButton != null) {
            memoButton.setVisible(false);
        }
        doValidation();
    }

    public boolean hasMemo() {
        return memoButton != null && memoButton.isVisible();
    }

    @Override
    protected ValidationResult calcValidationResult(String value) {
        if (getInputBox().isPrintable() || !hasMemo()) {
            getInputBox().setValueController(valueController);
            return super.calcValidationResult(value);
        } else {
            final String text = getEditMask().toStr(getEnvironment(), value);
            if (text.replaceAll("\n", "").matches("[^\\p{Cc}\\p{Cn}]*")) {
                getInputBox().setValueController(null);//edit only throw drop down text area
                return getEditMask().validate(getEnvironment(), value);
            } else {
                getInputBox().setValueController(valueController);
                return super.calcValidationResult(value);
            }
        }
    }
}
