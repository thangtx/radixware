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

package org.radixware.kernel.common.client.meta.mask.validators;

import java.text.DecimalFormatSymbols;
import java.util.Objects;
import org.radixware.kernel.common.client.IClientEnvironment;


public final class LongValidator implements org.radixware.kernel.common.client.meta.mask.validators.IInputValidator {

    private Long maxValue, minValue;
    private boolean isMaxValueDefined, isMinValueDefined;
    private Character triadDelimeter;
    private int radix = 10;

    public LongValidator(final Long minValue, final Long maxValue, final Byte radix) {
        this(minValue,maxValue,radix,null);
    }
    
    public LongValidator(final Long minValue, final Long maxValue, final Byte radix, final Character triadDelimeter) {
        this.maxValue = maxValue == null ? Long.MAX_VALUE : maxValue;
        this.minValue = minValue == null ? Long.MIN_VALUE : minValue;
        this.radix = radix == null ? 10 : radix;
        this.triadDelimeter = triadDelimeter;
        isMaxValueDefined = maxValue!=null;
        isMinValueDefined = minValue!=null;        
    }

    public void setMaxValue(final Long maxValue){
        this.maxValue = maxValue==null ? Long.MAX_VALUE : maxValue;
        isMaxValueDefined = maxValue!=null;
    }

    public void setMinValue(final Long minValue){
        this.minValue = minValue==null ? Long.MIN_VALUE : minValue;
        isMinValueDefined = minValue!=null;
    }
    
    public void setRadix(final int radix){
        this.radix = radix;
    }
    
    public void setTriadDelimeter(final Character triadDelimeter){
        this.triadDelimeter = triadDelimeter;
    }

    @Override
    public ValidationResult validate(final IClientEnvironment environment, final String input, final int position) {
        if (input==null || input.isEmpty())
            return ValidationResult.INTERMEDIATE;       
        
        final InvalidValueReason smallValueReason =
            InvalidValueReason.Factory.createForTooSmallValue(environment, Long.toString(minValue, radix).toUpperCase());
        final InvalidValueReason bigValueReason =
            InvalidValueReason.Factory.createForTooBigValue(environment, Long.toString(maxValue, radix).toUpperCase());        
        final DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance(environment.getLocale());            
        final char minusSign = symbols.getMinusSign();
        final char plusSign = '+';
        if (minValue >= 0 && input.charAt(0)==minusSign)
            return ValidationResult.Factory.newInvalidResult(smallValueReason);

        if (maxValue < 0 && input.charAt(0)==plusSign)
            return ValidationResult.Factory.newInvalidResult(bigValueReason);

        if (String.valueOf(plusSign).equals(input) || String.valueOf(minusSign).equals(input))
            return ValidationResult.INTERMEDIATE;
        
        final String preparsedInput;
        if (triadDelimeter==null || triadDelimeter.charValue()=='\0'){
            preparsedInput = input;
        }else{
            preparsedInput = input.replaceAll(String.valueOf(triadDelimeter.charValue()), "");
        }
        
        final long entered;
        try{
            entered = Long.parseLong(preparsedInput, radix);
        }
        catch(NumberFormatException ex){
            return ValidationResult.Factory.newInvalidResult(InvalidValueReason.WRONG_FORMAT);
        }
        if (entered >= minValue && entered <= maxValue)
            return ValidationResult.ACCEPTABLE;

        final InvalidValueReason reason = entered > maxValue ? bigValueReason : smallValueReason;
        if (entered >= 0) {
            // the -entered < minValue condition is necessary to allow people to type
            // the minus last (e.g. for right-to-left languages)
            if (entered > maxValue && -entered < minValue){
                return ValidationResult.Factory.newInvalidResult(reason);
            }
            else{
                return ValidationResult.Factory.newIntermediateResult(reason);
            }
        } else {
            if (entered < minValue){
                return ValidationResult.Factory.newInvalidResult(reason);
            }
            else{
                return ValidationResult.Factory.newIntermediateResult(reason);
            }
        }
    }

    @Override
    public String fixup(String input) {
        return input;
    }

    public static LongValidator loadFromXml(final org.radixware.schemas.editmask.EditMaskStr editMaskStr){
        Long minValue,maxValue;
        try{
            minValue = editMaskStr.isSetMinValue() ? Long.parseLong(editMaskStr.getMinValue()) : null;
        }
        catch(NumberFormatException ex){
            minValue = null;
        }
        try{
            maxValue = editMaskStr.isSetMaxValue() ? Long.parseLong(editMaskStr.getMaxValue()) : null;
        }
        catch(NumberFormatException ex){
            maxValue = null;
        }
        return new LongValidator(minValue, maxValue, null);
    }

    public void appendToXml(final org.radixware.schemas.editmask.EditMaskStr editMaskStr){
        if (isMinValueDefined)
            editMaskStr.setMinValue(minValue.toString());
        if (isMaxValueDefined)
            editMaskStr.setMaxValue(maxValue.toString());
    }
    
    public Long getMaxValue(){
        return isMaxValueDefined ? maxValue : null;
    }
    
    public Long getMinValue(){
        return isMinValueDefined ? minValue : null;
    }
    
    public int getRadix(){
        return radix;
    }
    
    public Character getTriadDelimeter(){
        return triadDelimeter;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 13 * hash + Objects.hashCode(this.maxValue);
        hash = 13 * hash + Objects.hashCode(this.minValue);
        hash = 13 * hash + (this.isMaxValueDefined ? 1 : 0);
        hash = 13 * hash + (this.isMinValueDefined ? 1 : 0);
        hash = 13 * hash + this.radix;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj){
            return true;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final LongValidator other = (LongValidator) obj;
        if (!Objects.equals(this.maxValue, other.maxValue)) {
            return false;
        }
        if (!Objects.equals(this.minValue, other.minValue)) {
            return false;
        }
        if (this.isMaxValueDefined != other.isMaxValueDefined) {
            return false;
        }
        if (this.isMinValueDefined != other.isMinValueDefined) {
            return false;
        }
        if (this.radix != other.radix) {
            return false;
        }
        return true;
    }        
}