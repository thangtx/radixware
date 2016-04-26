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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Objects;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.utils.ValueConverter;


public final class BigDecimalValidator implements org.radixware.kernel.common.client.meta.mask.validators.IInputValidator {

    private final BigDecimal maxValue, minValue;
    private final boolean isMaxValueDefined, isMinValueDefined;
    private final int precision;
    private final int inputPrecision;
    private final long scale;
    private final NumberFormat numberFormat;
    private final char minusChar,plusChar;

    public BigDecimalValidator(final BigDecimal minValue, final BigDecimal maxValue, final int precision) {
        this.maxValue = maxValue == null ? BigDecimal.valueOf(Double.MAX_VALUE) : maxValue;
        this.minValue = minValue == null ? BigDecimal.valueOf(-Double.MAX_VALUE) : minValue;
        isMinValueDefined = minValue!=null;
        isMaxValueDefined = maxValue!=null;        
        this.precision = precision;
        inputPrecision = precision;
        scale = 1;
        minusChar='-';
        plusChar='+';
        numberFormat = null;
    }
    
    public BigDecimalValidator(final BigDecimal minValue, final BigDecimal maxValue, final long scale, final NumberFormat numberFormat, final int precision) {
        this.maxValue = maxValue == null ? BigDecimal.valueOf(Double.MAX_VALUE) : maxValue;
        this.minValue = minValue == null ? BigDecimal.valueOf(-Double.MAX_VALUE) : minValue;
        isMinValueDefined = minValue!=null;
        isMaxValueDefined = maxValue!=null;
        this.scale = scale;
        this.numberFormat = numberFormat;
        this.precision = precision;
        if (precision>-1 && Math.abs(scale)>=10){
            final int scaledPrecision = (int)Math.log10(Math.abs(scale));
            if (scaledPrecision>=precision){
                inputPrecision = 0;
            }else{
                inputPrecision = precision - scaledPrecision;
            }
        }else{
            inputPrecision = precision;
        }
        plusChar='+';
        if (numberFormat instanceof DecimalFormat){
            minusChar = ((DecimalFormat)numberFormat).getDecimalFormatSymbols().getMinusSign();
        }else{
            minusChar = '-';
        }
    }

    @Override
    public String fixup(final String input) {
        return input;
    }

    private static int numDigits(final long number) {
        if (number == 0) {
            return 1;
        }
        return (int) Math.log10(number) + 1;
    }

    private static BigInteger pow10(final int exp) {
        BigInteger result = BigInteger.ONE;
        for (int i = 0; i < exp; ++i) {
            result = result.multiply(BigInteger.TEN);
        }
        return result;
    }
    private final static BigDecimal MAX_LONG = new BigDecimal(Long.MAX_VALUE);

    @Override
    public ValidationResult validate(final IClientEnvironment environment, final String input, final int position) {
        if (input==null || input.isEmpty())
            return ValidationResult.INTERMEDIATE;

        final InvalidValueReason smallValueReason;
        final InvalidValueReason bigValueReason;
        if (scale==0 || scale==1){
            smallValueReason = InvalidValueReason.Factory.createForTooSmallValue(environment, minValue.toString());
            bigValueReason = InvalidValueReason.Factory.createForTooBigValue(environment, maxValue.toString());
        }else{
            final BigDecimal multiplicand = new BigDecimal(scale);
            smallValueReason = 
                InvalidValueReason.Factory.createForTooSmallValue(environment, minValue.multiply(multiplicand).toString());
            bigValueReason = 
                InvalidValueReason.Factory.createForTooBigValue(environment, maxValue.multiply(multiplicand).toString());
        }

        if (minValue.compareTo(BigDecimal.ZERO) >= 0 && input.charAt(0)==minusChar)
            return ValidationResult.Factory.newInvalidResult(smallValueReason);

        if (maxValue.compareTo(BigDecimal.ZERO)<0  && input.charAt(0)==plusChar)
            return ValidationResult.Factory.newInvalidResult(bigValueReason);

        if (String.valueOf(plusChar).equals(input) || String.valueOf(minusChar).equals(input))
            return ValidationResult.INTERMEDIATE;
        
        if (input.length()>1 && (input.charAt(1)==plusChar || input.charAt(1)==minusChar)){
            return ValidationResult.Factory.newInvalidResult(InvalidValueReason.WRONG_FORMAT);
        }
        
        if (inputPrecision==0){//no fraction part
            final char decimalDelimeter = 
                numberFormat instanceof DecimalFormat ? ((DecimalFormat)numberFormat).getDecimalFormatSymbols().getDecimalSeparator() : '.';
            int decimalDelimeterPos = input.indexOf('.');
            if (decimalDelimeterPos<0 && decimalDelimeter != '.') {
                decimalDelimeterPos = input.indexOf(decimalDelimeter);
            }
            if (decimalDelimeterPos>=0){
                final String reason = environment.getMessageProvider().translate("Value", "Number precision too large");
                return ValidationResult.Factory.newInvalidResult(reason);
            }
        }
        
        BigDecimal value;
        try{            
            value = ValueConverter.parseBigDecimal(input, numberFormat);
        }
        catch(NumberFormatException ex){
            return ValidationResult.Factory.newInvalidResult(InvalidValueReason.WRONG_FORMAT);
        }
        if (value==null){
            return ValidationResult.INTERMEDIATE;
        }
        final BigDecimal unscaledValue;
        if (scale!=1 && scale!=0){
            if (precision>=0){
                unscaledValue = value.divide(BigDecimal.valueOf(scale),precision, RoundingMode.HALF_EVEN);
            }else{
                unscaledValue = value.divide(BigDecimal.valueOf(scale));
            }
        }else{
            unscaledValue = value;
        }

        if (unscaledValue.compareTo(minValue)>=0  && unscaledValue.compareTo(maxValue)<=0){
            if (inputPrecision<0 || value.scale()<=inputPrecision){
                return ValidationResult.ACCEPTABLE;
            }
            else{
                final String reason = environment.getMessageProvider().translate("Value", "Number precision too large");
                return ValidationResult.Factory.newIntermediateResult(reason);
            }
        }

        final InvalidValueReason reason = unscaledValue.compareTo(maxValue)>=0 ? bigValueReason : smallValueReason;

        final BigDecimal maxBound = minValue.abs().max(maxValue.abs());
        if (maxBound.compareTo(BigDecimal.ONE)<0 && unscaledValue.abs().compareTo(BigDecimal.ONE)<0){
            return ValidationResult.Factory.newIntermediateResult(reason);
        }
        if (maxBound.compareTo(unscaledValue.abs())<0){
            return ValidationResult.Factory.newInvalidResult(reason);
        }
        if (maxBound.compareTo(MAX_LONG)<0){
            final BigInteger roundTop = pow10(numDigits(maxBound.longValue()));
            final BigDecimal bound = new BigDecimal(roundTop.subtract(BigInteger.ONE));
            if (unscaledValue.abs().compareTo(bound)>0)
                return ValidationResult.Factory.newInvalidResult(reason);
        }
        return ValidationResult.Factory.newIntermediateResult(reason);
    }

    public static BigDecimalValidator loadFromXml(final org.radixware.schemas.editmask.EditMaskStr editMaskStr){
        BigDecimal minValue,maxValue;
        try{
            minValue = editMaskStr.isSetMinValue() ? new BigDecimal(editMaskStr.getMinValue()) : null;
        }
        catch(NumberFormatException ex){
            minValue = null;
        }
        try{
            maxValue = editMaskStr.isSetMaxValue() ? new BigDecimal(editMaskStr.getMaxValue()) : null;
        }
        catch(NumberFormatException ex){
            maxValue = null;
        }
        return new BigDecimalValidator(minValue, maxValue, (editMaskStr.isSetPrecision() ? editMaskStr.getPrecision() : -1));
    }

    public void appendToXml(final org.radixware.schemas.editmask.EditMaskStr editMaskStr){
        if (isMinValueDefined)
            editMaskStr.setMinValue(minValue.toString());
        if (isMaxValueDefined)
            editMaskStr.setMaxValue(maxValue.toString());
        if (precision>=0)
            editMaskStr.setPrecision(precision);
    }
    
    public BigDecimal getMaxValue(){
        return isMaxValueDefined ? maxValue : null;
    }
    
    public BigDecimal getMinValue(){
        return isMinValueDefined ? minValue : null;
    }
    
    public int getPrecision(){
        return precision;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + Objects.hashCode(this.maxValue);
        hash = 53 * hash + Objects.hashCode(this.minValue);
        hash = 53 * hash + (this.isMaxValueDefined ? 1 : 0);
        hash = 53 * hash + (this.isMinValueDefined ? 1 : 0);
        hash = 53 * hash + this.precision;
        hash = 53 * hash + (int) (this.scale ^ (this.scale >>> 32));
        hash = 53 * hash + Objects.hashCode(this.numberFormat);
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
        final BigDecimalValidator other = (BigDecimalValidator) obj;
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
        if (this.precision != other.precision) {
            return false;
        }
        if (this.scale != other.scale) {
            return false;
        }
        if (!Objects.equals(this.numberFormat, other.numberFormat)) {
            return false;
        }
        return true;
    }        
}
