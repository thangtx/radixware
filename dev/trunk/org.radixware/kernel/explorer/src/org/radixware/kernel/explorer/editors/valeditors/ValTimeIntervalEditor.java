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
import java.util.Objects;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.meta.mask.EditMaskTimeInterval;
import org.radixware.kernel.common.client.meta.mask.validators.IInputValidator;
import org.radixware.kernel.common.client.meta.mask.validators.ValidationResult;
import org.radixware.kernel.common.defs.ads.clazz.presentation.editmask.EditMaskTimeInterval.Scale;


import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EValType;

// T = Long or Timestamp
public final class ValTimeIntervalEditor extends ValEditor<Long> {

    private static class TimeIntervalValidator implements IInputValidator{

        private final EditMaskTimeInterval editMask;

        public TimeIntervalValidator(final EditMaskTimeInterval mask){
            editMask = mask;
        }

        @Override
        public String fixup(String input) {
            return input;
        }

        @Override
        public ValidationResult validate(final IClientEnvironment environment, String input, int position) {
            final long val = editMask.convertFromStringToLong(input);

            if (val > editMask.getMaxValue()){
                final String  maxValue = editMask.toStr(environment,editMask.getMaxValue());
                final String bigValueReason =
                    String.format(environment.getMessageProvider().translate("Value", "The highest acceptable interval is %s"),maxValue);
                return ValidationResult.Factory.newInvalidResult(bigValueReason);
            }
            if (val<editMask.getMinValue())
            {
                final String minValue = editMask.toStr(environment,editMask.getMinValue());
                final String smallValueReason =
                    String.format(environment.getMessageProvider().translate("Value", "The lowest acceptable interval is %s"),minValue);
                return ValidationResult.Factory.newIntermediateResult(smallValueReason);
            }
            return ValidationResult.ACCEPTABLE;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 79 * hash + Objects.hashCode(this.editMask);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (obj == this){
                return true;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final TimeIntervalValidator other = (TimeIntervalValidator) obj;
            if (!Objects.equals(this.editMask, other.editMask)) {
                return false;
            }
            return true;
        }       
    }

    private boolean wasEdited, initialValueWasChanged;
    private Long initialValue;

    public ValTimeIntervalEditor(IClientEnvironment environment, final QWidget parent, final EditMaskTimeInterval editMask, final boolean mandatory, final boolean readOnly) {
        super(environment, parent, editMask, mandatory, readOnly);
        updateInputValidator();
    }

    public ValTimeIntervalEditor(final IClientEnvironment environment, final QWidget parent, final Scale scale){
        this(environment,parent,new EditMaskTimeInterval(scale),true,false);
    }

    @Override
    protected void onTextEdited(final String newText) {
        final EditMaskTimeInterval mask = (EditMaskTimeInterval) getEditMask();
        final Long newValue = mask.convertFromStringToLong(newText);        
        setOnlyValue(newValue);
    }

    private void updateInputValidator(){
        setInputValidator(new TimeIntervalValidator((EditMaskTimeInterval)getEditMask()));
    }

    @Override
    protected void onFocusIn() {
        if (!isReadOnly()) {
            final Long val = this.getValue();
            final EditMaskTimeInterval mask = (EditMaskTimeInterval) getEditMask();
            if (mask.isSpecialValue(val)) {                
                initialValue = val;                
                initialValueWasChanged = true;
                wasEdited = false;
                blockSignals(true);//to prevent emmiting of valueChanged signal
                try{
                    super.setOnlyValue(0L);
                }
                finally{
                    blockSignals(false);
                }
                setOnlyText(getStringToShow(Long.valueOf(0)), mask.getInputMask());
            }else{
                setOnlyText(mask.getInputTextForValue(val), mask.getInputMask());
            }
        }
        super.onFocusIn();
    }

    @Override
    protected boolean setOnlyValue(final Long value) {
        if (initialValueWasChanged && !eq(this.value, value)) {
            wasEdited = true;
        }
        return super.setOnlyValue(value);
    }

    @Override
    protected void onFocusOut() {
        super.onFocusOut();
        if (!isReadOnly()) {
            if (initialValueWasChanged && !wasEdited) {
                setValue(initialValue);
                initialValueWasChanged = false;
            }
            setOnlyText(getStringToShow(getValue()), "");
        }
    }

    @Override
    public void setReadOnly(final boolean readOnly) {
        super.setReadOnly(readOnly);
        getLineEdit().setReadOnly(readOnly);
    }

    @Override
    public Long getValue() {
        if (initialValueWasChanged && !wasEdited) {
            return initialValue;
        } else {
            return super.getValue();
        }
    }

    @Override
    public void setEditMask(EditMask editMask) {
        super.setEditMask(editMask);
        updateInputValidator();
        refresh();
    }

    @Override
    public void refresh() {                
        setOnlyText(getStringToShow(getValue()), "");
        getLineEdit().clearFocus();
        clearBtn.setVisible(!isMandatory() && !isReadOnly() && getValue() != null);
    }

    @Override
    protected Long stringAsValue(final String stringVal) {
        return (Long) ValAsStr.fromStr(stringVal, EValType.INT);
    }

    @Override
    protected String valueAsStr(final Long value) {
        return ValAsStr.toStr(value, EValType.INT);
    }
}