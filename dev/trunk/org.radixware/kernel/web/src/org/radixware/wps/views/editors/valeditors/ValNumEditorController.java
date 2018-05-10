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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.mask.EditMaskNum;
import org.radixware.kernel.common.client.meta.mask.validators.ValidationResult;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.wps.rwt.InputBox;
import org.radixware.wps.rwt.InputBox.InvalidStringValueException;
import org.radixware.wps.rwt.InputBox.ValueController;


public class ValNumEditorController extends InputBoxController<BigDecimal,EditMaskNum> {
    
    private class SpinBoxController implements InputBox.SpinBoxController<BigDecimal> {
        
        public SpinBoxController(){
            
        }
        
        private BigDecimal getStepSize(){
            final EditMaskNum maskNum = getEditMask();
            final int precision = maskNum.getPrecision();
            if (precision==0){
                return BigDecimal.ONE;
            }else if (precision>0){
                return BigDecimal.ONE.movePointLeft(precision);
            }else{
                return null;
            }
        }        
        
        private boolean canChange(final BigDecimal value, final int stepCount){            
            final BigDecimal stepSize = getStepSize();
            if (stepSize==null){
                return false;
            }            
            if (value == null) {//RADIX-2423
                return true;
            }
            final EditMaskNum maskNum = getEditMask();
            final BigDecimal min = maskNum.getMinValue();
            final BigDecimal max = maskNum.getMaxValue();
            final BigDecimal delta = stepSize.multiply(BigDecimal.valueOf(stepCount));
            final BigDecimal newValue = value.add(delta);

            if (min.compareTo(newValue) <= 0 && newValue.compareTo(max) <= 0) {
                return true;
            }
            if (   (min.compareTo(newValue)>0 && newValue.compareTo(value)>0) 
                || (max.compareTo(newValue) < 0 && newValue.compareTo(value) < 0)) {
                return true;
            }
            return false;
        }
        
        private BigDecimal change(final BigDecimal value, final int stepCount) {
            if (canChange(value, stepCount)){
                final EditMaskNum maskNum = getEditMask();               
                if (value == null) {//RADIX-2423
                    final BigDecimal newValue = 
                        maskNum.getMinValue().compareTo(BigDecimal.ZERO) <= 0 ? BigDecimal.ZERO : maskNum.getMinValue();
                    if (maskNum.validate(getEnvironment(),newValue)==ValidationResult.ACCEPTABLE){
                        return newValue;
                    }
                }
                else{
                    final BigDecimal stepSize = getStepSize();
                    if (stepSize!=null){
                        final BigDecimal delta = stepSize.multiply(BigDecimal.valueOf(stepCount));
                        return value.add(delta);
                    }                 
                }
            }
            return value;
        }
        

        @Override
        public BigDecimal getNext(final BigDecimal value, final int delta) {
            return change(value, delta);
        }

        @Override
        public BigDecimal getPrev(final BigDecimal value, final int delta) {
            return change(value, -delta);
        }

        @Override
        public void updateButtons(final InputBox box, final BigDecimal value) {
            final boolean isSpinVisible;
            if (ValNumEditorController.this.isSpinButtonsVisible()){
                final EditMaskNum editMask = ValNumEditorController.this.getEditMask();
                isSpinVisible = editMask.getPrecision()>=0;
            }else{
                isSpinVisible = false;
            }
            box.showSpinButtons(isSpinVisible);
            if (isSpinVisible){
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
    
    private final NumberFormat numberFormat;
    private boolean internalValueChange;
    private boolean editMaskChange;
    private Character customDecimalDelimeter = null;
    private SpinBoxController spinBoxController;
    private boolean isSpinButtonsVisible;

    public ValNumEditorController(final IClientEnvironment env) {
        this(env,null);
    }
    
    public ValNumEditorController(final IClientEnvironment env, final LabelFactory factory) {
        super(env, factory);
        numberFormat = (NumberFormat)NumberFormat.getNumberInstance(env.getLocale()).clone();
        numberFormat.setGroupingUsed(false);
        numberFormat.setMaximumFractionDigits(Integer.MAX_VALUE);
        if (numberFormat instanceof DecimalFormat){
            ((DecimalFormat)numberFormat).setParseBigDecimal(true);
        }
        setEditMask(new EditMaskNum());        
    }

    @Override
    protected ValueController<BigDecimal> createValueController() {
        return new ValueController<BigDecimal>() {
            @Override
            public BigDecimal getValue(final String text) throws InvalidStringValueException {                                                
                try{
                    final BigDecimal newValue = getEditMask().fromStr(getEnvironment(), text);
                    return getEditMask().getNormalized(getUnscaledValue(newValue));
                }
                catch(NumberFormatException ex){
                    throw new InvalidStringValueException(text);//NOPMD
                }
            }
        };
    }

    @Override
    protected void setupValEditor(final InputBox<BigDecimal> inputBox) {
        super.setupValEditor(inputBox);        
        spinBoxController = new SpinBoxController();        
        inputBox.setSpinBoxController(spinBoxController);
    }    
    
    private BigDecimal getUnscaledValue(final BigDecimal value){
        if (value==null){
            return value;
        }else{
            final long scale = editMask.getScale();
            if (scale==0 || scale==1){
                return value;
            }
            final int precision = editMask.getPrecision();
            if (precision>=0){
                if (Math.abs(editMask.getScale())>=10){
                    final int scaledPrecision = (int)Math.log10(Math.abs(editMask.getScale()));
                    return value.divide(BigDecimal.valueOf(scale), scaledPrecision+precision, RoundingMode.HALF_EVEN).stripTrailingZeros();
                }else{
                    return value.divide(BigDecimal.valueOf(scale), precision, RoundingMode.HALF_EVEN);
                }                 
            }else{
                return value.divide(BigDecimal.valueOf(scale));
            }
        }
    }    

    @Override
    public final void setEditMask(final EditMaskNum editMask) {
        if (!Utils.equals(editMask.getCustomDecimalDelimeter(),customDecimalDelimeter) && 
             (numberFormat instanceof DecimalFormat)
           ){
            customDecimalDelimeter = editMask.getCustomDecimalDelimeter();
            final DecimalFormatSymbols symbols = new DecimalFormatSymbols(getEnvironment().getLocale());
            if (customDecimalDelimeter!=null){
                symbols.setDecimalSeparator(customDecimalDelimeter.charValue());
            }
            ((DecimalFormat)numberFormat).setDecimalFormatSymbols(symbols);
        }
        editMaskChange = true;
        try{
            super.setEditMask(editMask);        
        }finally{
            editMaskChange = false;
        }
    }

    @Override
    protected String calcFocusedText(final BigDecimal value, final EditMaskNum editMask) {
        if (editMask.isSpecialValue(value)){
            return "";
        }else{
            return editMask.toStr(getEnvironment(), value);
        }        
    }

    @Override
    protected ValidationResult calcValidationResult(final BigDecimal value) {
        if (getEditMask() == null) {
            return ValidationResult.ACCEPTABLE;
        } else {
            return getEditMask().validate(getEnvironment(), value, !internalValueChange && !editMaskChange);
        }
    }

    @Override
    public void setValue(final BigDecimal value) {        
        internalValueChange = true;
        try{
            super.setValue(value);
        }finally{
            internalValueChange = false;
        }        
    }
    
    @Override
    protected void applyEditMask(final InputBox box) {
        super.applyEditMask(box);
        spinBoxController.updateButtons(box,getValue());
    }    
    
    public void showSpinButtons(){
        isSpinButtonsVisible = true;
        spinBoxController.updateButtons(getInputBox(), getValue());
    }
    
    public void hideSpinButtons(){
        isSpinButtonsVisible = false;
        spinBoxController.updateButtons(getInputBox(), getValue());
    }
    
    public final boolean isSpinButtonsVisible(){
        if (isSpinButtonsVisible && !isReadOnly()){
            final EditMaskNum editMask = getEditMask();
            return editMask!=null && editMask.getPrecision()>=0;
        }else{
            return false;
        }
    }
    
}