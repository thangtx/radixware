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
import org.radixware.kernel.common.client.meta.mask.EditMaskInt;
import org.radixware.wps.rwt.InputBox;
import org.radixware.wps.rwt.InputBox.InvalidStringValueException;
import org.radixware.wps.rwt.InputBox.ValueController;


public class ValIntEditorController extends InputBoxController<Long,EditMaskInt> {

    public ValIntEditorController(final IClientEnvironment env) {
        this(env,null);
    }
    
    public ValIntEditorController(final IClientEnvironment env, final LabelFactory factory) {
        super(env,factory);
        setEditMask(new EditMaskInt());
    }
    
    private class SpinBoxController implements InputBox.SpinBoxController<Long> {
        
        public SpinBoxController(){
            
        }
        
        private boolean canChange(Long value, final int stepCount){
            final EditMaskInt maskInt = getEditMask();        
            if (value == null) {//RADIX-2423
                return true;
            }
            final long delta = stepCount * maskInt.getStepSize();
            if (delta==0){
                return true;
            }        

            final long min = maskInt.getMinValue();
            final long max = maskInt.getMaxValue();
            // check overflow
            //if (step > 0 && cur + d < cur) return;
            //if (step < 0 && cur + d > cur) return;
            final long newValue = value + delta;

            if (min <= newValue && newValue <= max) {
                return true;
            }
            if ((min > newValue && newValue > value) || (max < newValue && newValue < value)) {
                return true;
            }
            return false;
        }
        
        private Long change(Long value, int stepCount) {
            if (canChange(value, stepCount)){
                final EditMaskInt maskInt = getEditMask();               
                if (value == null) {//RADIX-2423
                    return maskInt.getMinValue() <= 0 ? 0 : maskInt.getMinValue();
                }
                else{
                    return value + stepCount * maskInt.getStepSize();
                }
            }
            return value;
        }
        

        @Override
        public Long getNext(Long value, int delta) {
            return change(value, delta);
        }

        @Override
        public Long getPrev(Long value, int delta) {
            return change(value, -delta);
        }

        @Override
        public void updateButtons(InputBox box, Long value) {
            final boolean isSpinButtonsVisible = !isReadOnly() && getEditMask().getStepSize() > 0;                
            box.showSpinButtons(isSpinButtonsVisible);
            if (isSpinButtonsVisible){
                final int arrStepCount[] = {100,10,1,0};
                for (int stepCount: arrStepCount){
                    if (canChange(value, stepCount)){
                        box.setMaxSpinUpStepCount(stepCount);
                        break;
                    }
                }
                for (int stepCount: arrStepCount){
                    if (canChange(value, -stepCount)){
                        box.setMaxSpinDownStepCount(stepCount);
                        break;
                    }
                }
            }
            else{
                box.setMaxSpinUpStepCount(0);
                box.setMaxSpinDownStepCount(0);
            }
        }
        
    }
    
    private SpinBoxController spinBoxController;

    @Override
    protected void setupValEditor(InputBox<Long> inputBox) {
        super.setupValEditor(inputBox);        
        spinBoxController = new SpinBoxController();
        inputBox.setSpinBoxController(spinBoxController);
    }

    @Override
    protected ValueController<Long> createValueController() {
        return new ValueController<Long>() {

            @Override
            public Long getValue(String newText) throws InvalidStringValueException {
                if (newText != null && !newText.isEmpty()) {
                    try {
                        return getEditMask().fromStr(getEnvironment(), newText);
                    } catch (NumberFormatException e) {                        
                        throw new InvalidStringValueException(newText);//NOPMD
                    }
                } else {
                    return null;
                }
            }
        };
    }

    @Override
    protected String calcFocusedText(final Long value, final EditMaskInt editMask) {
        if (editMask.isSpecialValue(value)){
            return "";
        }
        else{
            final String valAsStr = String.valueOf(value);
            return getEditMask().formatInputString(getEnvironment(), valAsStr);
        }        
    }
    
    @Override
    protected void applyEditMask(InputBox box) {
        super.applyEditMask(box);
        spinBoxController.updateButtons(box,getValue());
    }
}