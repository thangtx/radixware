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

import java.math.BigDecimal;

import com.trolltech.qt.gui.QWidget;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.meta.mask.EditMaskNum;
import org.radixware.kernel.common.client.meta.mask.validators.IInputValidator;
import org.radixware.kernel.common.client.meta.mask.validators.ValidationResult;
import org.radixware.kernel.common.client.meta.mask.validators.ValidatorsFactory;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EValType;

public class ValNumEditor extends AbstractNumberEditor<BigDecimal> {
    

    public ValNumEditor(final IClientEnvironment environment, final QWidget parent, final EditMaskNum maskNum,
            final boolean mandatory, final boolean readOnly) {
        super(environment, parent, maskNum, mandatory, readOnly);        
        updateInputValidator();
    }

    public ValNumEditor(final IClientEnvironment environment, final QWidget parent){
        this (environment, parent, new EditMaskNum(), true, false);
    }
    
    public void showSpinButtons(){
        setSpinButtonsVisible(true);
    }
    
    public void hideSpinButtons(){
        setSpinButtonsVisible(false);
    }

    @Override
    protected void onFocusIn() {
        if (!isReadOnly()) {
            final BigDecimal val = getValue();
            if (val==null){
                setOnlyText("", null);
            }
        }
        super.onFocusIn();
    }

    @Override
    protected void onFocusOut() {
        if (inModificationState()){
            setOnlyValue(((EditMaskNum)getEditMask()).getNormalized(getValue()));
        }
        super.onFocusOut();
        if (!isReadOnly()) {
            setOnlyText(getStringToShow(getValue()), null);
            getLineEdit().home(false);
        }
    }
       
    private void updateInputValidator() {
        final EditMaskNum editMask = (EditMaskNum) getEditMask();
        final NumberFormat numberFormat = editMask.getNumberFormat(getEnvironment());
        final BigDecimal minValue = editMask.getMinValue(), maxValue = editMask.getMaxValue();
        final long scale = editMask.getScale();
        final int precision = editMask.getPrecision();
        final IInputValidator inputValidator = 
            ValidatorsFactory.createBigDecimalValidator(minValue, maxValue, scale, numberFormat, precision);
        setInputValidator(inputValidator);
    }

    @Override
    public void setReadOnly(final boolean readOnly) {
        super.setReadOnly(readOnly);
        getLineEdit().setReadOnly(readOnly);
    }

    @Override
    public void setEditMask(final EditMask editMask) {
        super.setEditMask(editMask);
        updateInputValidator();
    }

    @Override
    protected String valueAsStr(final BigDecimal value) {
        return ValAsStr.toStr(value, EValType.NUM);
    }

    @Override
    protected BigDecimal stringAsValue(final String stringVal) {
        return (BigDecimal) ValAsStr.fromStr(stringVal, EValType.NUM);
    }  
    
    /*Implementation of AbstractNumber editor*/

    @Override
    protected BigDecimal parseNumber(final String inputStr) throws NumberFormatException {
        final BigDecimal parsedValue = ((EditMaskNum)getEditMask()).fromStr(getEnvironment(), inputStr);
        return getUnscaledValue(parsedValue);
    }        

    @Override
    protected String formatInputString(final String inputStr) {
        return ((EditMaskNum)getEditMask()).formatInputString(getEnvironment(), inputStr);
    }

    @Override
    @SuppressWarnings("PMD.MissingBreakInSwitch")
    protected Character getCharacter(final ESymbol symbol) {
        switch (symbol){
            case TRIAD_DELIMETER:
                return ((EditMaskNum)getEditMask()).getTriadDelimeter(getEnvironment());
            case DECIMAL_DELIMETER:
                return ((EditMaskNum)getEditMask()).getDecimalDelimeter(getEnvironment());
            case PLUS:
                return Character.valueOf('+');
            case MINUS:{
                final EditMaskNum editMaskNum = (EditMaskNum)getEditMask();
                final char minusCharacter;
                if (editMaskNum.getNumberFormat(getEnvironment()) instanceof DecimalFormat){
                    minusCharacter = 
                        ((DecimalFormat)editMaskNum.getNumberFormat(getEnvironment())).getDecimalFormatSymbols().getMinusSign();                    
                }else{
                    minusCharacter = '-';
                }                
                return Character.valueOf(minusCharacter);
            }
            default:
                throw new IllegalArgumentException("Unknown symbol "+symbol.name());
        }
    }

    @Override
    protected boolean sameAsCurrentNumber(final BigDecimal number) {        
        final BigDecimal current = getValue();        
        if (number==null){
            return current==null;
        }
        return current!=null && current.compareTo(number)==0;
    }
            
    private BigDecimal getUnscaledValue(final BigDecimal value){
        if (value==null){
            return value;
        }else{
            final EditMaskNum editMask = (EditMaskNum)getEditMask();
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
    protected ValidationResult calcValidationResult(final BigDecimal value) {
        return ((EditMaskNum)getEditMask()).validate(getEnvironment(),value, inModificationState());
    }        

    @Override
    protected boolean canSpinUp() {
        if (canSpin()) {
            if (getValue()==null){
                return true;
            }            
            final BigDecimal cur;
            try {
                cur = getCurrentValue();
            } catch (NumberFormatException e) {
                return false;
            }
            final EditMaskNum maskNum = (EditMaskNum) getEditMask();                        
            final BigDecimal max = maskNum.getMaxValue();
            final BigDecimal stepSize = getStepSize();
            if (stepSize==null){
                return false;
            }
            return cur.add(stepSize).compareTo(max) <= 0;
        }
        return false;
    }

    @Override
    protected boolean canSpinDown() {
        if (canSpin()) {
            if (getValue()==null){
                return true;
            }            
            final BigDecimal cur;
            try {
                cur = getCurrentValue();
            } catch (NumberFormatException e) {
                return false;
            }
            final EditMaskNum maskNum = (EditMaskNum) getEditMask();                        
            final BigDecimal min = maskNum.getMinValue();            
            final BigDecimal stepSize = getStepSize();
            if (stepSize==null){
                return false;
            }
            return min.compareTo(cur.subtract(stepSize)) <= 0;
        }
        return false;
    }

    @Override
    protected void doSpin(final int step) {
        if (canSpin()){
            final EditMaskNum maskNum = (EditMaskNum) getEditMask();
            if (getValue() == null) {//RADIX-2423
                final BigDecimal newValue = 
                    maskNum.getMinValue().compareTo(BigDecimal.ZERO) <= 0 ? BigDecimal.ZERO : maskNum.getMinValue();
                if (maskNum.validate(getEnvironment(),newValue)==ValidationResult.ACCEPTABLE){
                    changeValue(newValue);
                }
                return;
            }            
            final BigDecimal cur;
            try {
                cur = getCurrentValue();
            } catch (NumberFormatException e) {
                return;
            }
            final BigDecimal stepSize = getStepSize();
            if (stepSize==null){
                return;
            }
            final BigDecimal delta = stepSize.multiply(BigDecimal.valueOf(step));
            final BigDecimal min = maskNum.getMinValue();
            final BigDecimal max = maskNum.getMaxValue();
            // check overflow
            //if (step > 0 && cur + d < cur) return;
            //if (step < 0 && cur + d > cur) return;
            final BigDecimal newValue = cur.add(delta);
            if (min.compareTo(newValue)  <= 0 && newValue.compareTo(max) <= 0) {
                changeValue(newValue);
            }
            if (   (min.compareTo(newValue)>0 && newValue.compareTo(cur)>0) 
                || (max.compareTo(newValue)<0 && newValue.compareTo(cur)<0)){
                changeValue(newValue);
            }
        }
    }
    
    @Override
    protected boolean canSpin(){
        if (isReadOnly()){
            return false;
        }
        final EditMaskNum maskNum = (EditMaskNum) getEditMask();
        if (maskNum.getPrecision()<0){
            return false;
        }
        if (getValue() == null) {//RADIX-2423
            final BigDecimal newValue = 
                maskNum.getMinValue().compareTo(BigDecimal.ZERO) <= 0 ? BigDecimal.ZERO : maskNum.getMinValue();
            return maskNum.validate(getEnvironment(),newValue)==ValidationResult.ACCEPTABLE;
        }else{
            return true;
        }
    }
    
    private BigDecimal getCurrentValue(){
        final String text = getLineEdit().text();
        return parseNumber(text);
    }    
    
    private BigDecimal getStepSize(){
        final EditMaskNum maskNum = (EditMaskNum) getEditMask();
        final int precision = maskNum.getPrecision();
        if (precision==0){
            return BigDecimal.ONE;
        }else if (precision>0){
            return BigDecimal.ONE.movePointLeft(precision);
        }else{
            return null;
        }
    }
    
    private void changeValue(final BigDecimal newValue){
        //ValIntEditor.this.setValue(newValue); Приводит к потере фокуса, поэтому текст меняем напрямую
        setOnlyValue(newValue);
        setOnlyText(getStringToShow(newValue), null);
    }   
    
}