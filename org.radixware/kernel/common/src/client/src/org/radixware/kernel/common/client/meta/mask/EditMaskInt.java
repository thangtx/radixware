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

package org.radixware.kernel.common.client.meta.mask;

import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.EnumSet;
import java.util.Locale;
import java.util.Objects;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.mask.validators.InvalidValueReason;
import org.radixware.kernel.common.client.meta.mask.validators.ValidationResult;
import org.radixware.kernel.common.enums.EEditMaskType;
import org.radixware.kernel.common.enums.ETriadDelimeterType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Arr;
import org.radixware.kernel.common.types.ArrInt;

public final class EditMaskInt extends org.radixware.kernel.common.client.meta.mask.EditMask {

    //	Если количество цифр в числе < length
    //  дополняется слева символами symbol до length.
    // Значения <=0 не учитываются.
    private byte minLength = 0;
    private Character padChar = null;
    private long stepSize = 1L;
    private Long minValue = null;
    private Long maxValue = null;
    private ETriadDelimeterType triadDelimeterType = ETriadDelimeterType.NONE;
    private Character customTriadDelimeter = null;
    private byte numberBase = 10;
    private NumberFormat numberFormat;
    private static final EnumSet<EValType> SUPPORTED_VALTYPES =
            EnumSet.of(EValType.INT, EValType.ARR_INT);

    public EditMaskInt(final Long minValue,
            final Long maxValue,
            final byte minLength,
            final Character padChar,
            final long stepSize,
            final ETriadDelimeterType triadDelimeterType,//Тип разделителя разрядов
            final Character triadDelimeter,
            final byte numberBase) {
        super();
        this.minLength = minLength;
        this.padChar = padChar;
        this.stepSize = stepSize;
        if (triadDelimeterType == null) {
            this.triadDelimeterType = triadDelimeter == null ? ETriadDelimeterType.NONE : ETriadDelimeterType.SPECIFIED;
        } else {
            this.triadDelimeterType = triadDelimeterType;
        }
        this.customTriadDelimeter = triadDelimeter;
        checkTriadDelimeter(triadDelimeter);
        this.numberBase = numberBase;
        if (minValue != null) {
            this.minValue = minValue;
        }
        if (maxValue != null) {
            this.maxValue = maxValue;
        }
    }

    public EditMaskInt(final Long minValue,
            final Long maxValue,
            final byte minLength,
            final Character padChar,
            final long stepSize,
            final Character triadDelimeter,
            final byte numberBase) {
        this(minValue, maxValue, minLength, padChar, stepSize, null, triadDelimeter, numberBase);
    }

    public EditMaskInt() {
        this(null, null, (byte) 0, null, 1l, null, null, (byte) 10);
    }

    public EditMaskInt(final EditMaskInt source) {
        super();
        this.minLength = source.minLength;
        this.padChar = source.padChar;
        this.stepSize = source.stepSize;
        this.triadDelimeterType = source.triadDelimeterType;
        this.customTriadDelimeter = source.customTriadDelimeter;
        this.numberBase = source.numberBase;
        this.minValue = source.minValue;
        this.maxValue = source.maxValue;
        this.numberFormat = source.numberFormat;
    }

    protected EditMaskInt(final org.radixware.schemas.editmask.EditMaskInt editMask) {
        super();
        if (editMask.isSetMinValue()) {
            minValue = editMask.getMinValue();
        }
        if (editMask.isSetMaxValue()) {
            maxValue = editMask.getMaxValue();
        }
        minLength = editMask.getMinLength();
        if (editMask.isSetPadChar() && editMask.getPadChar().length() > 0) {
            padChar = Character.valueOf(editMask.getPadChar().charAt(0));
        }
        stepSize = editMask.getStepSize();
        if (editMask.isSetTriadDelimeter() && editMask.getTriadDelimeter().length() > 0) {
            final Character delimeter = Character.valueOf(editMask.getTriadDelimeter().charAt(0));
            checkTriadDelimeter(delimeter);
            customTriadDelimeter = delimeter;
        }
        if (editMask.getTriadDelimeterType() == null) {
            this.triadDelimeterType = customTriadDelimeter == null ? ETriadDelimeterType.DEFAULT : ETriadDelimeterType.SPECIFIED;
        } else {
            this.triadDelimeterType = editMask.getTriadDelimeterType();
        }
        numberBase = editMask.getNumberBase();
    }

    @Override
    public void writeToXml(final org.radixware.schemas.editmask.EditMask editMask) {
        final org.radixware.schemas.editmask.EditMaskInt editMaskInt = editMask.addNewInt();
        if (minValue != null) {
            editMaskInt.setMinValue(minValue);
        }
        if (maxValue != null) {
            editMaskInt.setMaxValue(maxValue);
        }
        editMaskInt.setTriadDelimeterType(triadDelimeterType);
        if (customTriadDelimeter != null) {
            editMaskInt.setTriadDelimeter(Character.toString(customTriadDelimeter));
        }
        if (padChar != null) {
            editMaskInt.setPadChar(Character.toString(padChar));
        }
        editMaskInt.setMinLength(minLength);
        editMaskInt.setNumberBase(numberBase);
        editMaskInt.setStepSize(stepSize);
    }

    public long getStepSize() {
        return stepSize;
    }

    public void setStepSize(final long step) {
        this.stepSize = step;
        afterModify();
    }

    public ETriadDelimeterType getTriadDelimeterType() {
        return triadDelimeterType;
    }

    public void setTriadDelimeterType(final ETriadDelimeterType triadDelimeterType) {
        this.triadDelimeterType = triadDelimeterType;
    }

    public char getCustomTriadDelimeter() {
        return customTriadDelimeter == null ? '\0' : customTriadDelimeter;
    }

    public void setCustomTriadDelimeter(final Character delimeter) {
        checkTriadDelimeter(delimeter);
        this.customTriadDelimeter = delimeter;
        triadDelimeterType = customTriadDelimeter == null ? ETriadDelimeterType.NONE : ETriadDelimeterType.SPECIFIED;
        afterModify();
    }

    public byte getNumberBase() {
        return numberBase;
    }

    public void setNumberBase(final byte base) {
        this.numberBase = base;
        afterModify();
    }

    public byte getMinLength() {
        return minLength;
    }

    public char getPadChar() {
        return padChar == null ? '0' : padChar;
    }

    public void setMinLength(final byte length, final Character pad) {
        this.minLength = length;
        this.padChar = pad;
    }

    public long getMinValue() {
        return minValue == null ? Long.MIN_VALUE : minValue;
    }

    public void setMinValue(final long minValue) {
        this.minValue = minValue;
        afterModify();
    }

    public long getMaxValue() {
        return maxValue == null ? Long.MAX_VALUE : maxValue;
    }

    public void setMaxValue(final long maxValue) {
        this.maxValue = maxValue;
        afterModify();
    }
    
    private static DecimalFormatSymbols getSymbols(final Locale locale){
        return DecimalFormatSymbols.getInstance(locale);
    }
    
    public Character getTriadDelimeter(final Locale locale){
        switch (triadDelimeterType) {
            case NONE:
                return null;
            case SPECIFIED:
                return customTriadDelimeter;
            default:
                return Character.valueOf(getSymbols(locale).getGroupingSeparator());
        }        
    }

    private String insertDelimeters(String string, final Locale locale) {
        final Character triadDelimeter = getTriadDelimeter(locale);
        if (triadDelimeter == null || triadDelimeter == '\0') {
            return string;
        }
        string = string.replaceAll(String.valueOf(triadDelimeter.charValue()), "");
        final char minusChar = getSymbols(locale).getMinusSign();
        final char plusChar = '+';
        final StringBuffer result = new StringBuffer();
        final Character signChar;
        if (string.charAt(0) == minusChar || string.charAt(0) == plusChar) {
            signChar = Character.valueOf(string.charAt(0));
            string = string.substring(1);
        }else{
            signChar = null;
        }        
        final int len = string.length();
        int index = len % 3;                        
        result.append(string.substring(0, index));
        while (index < len) {            
            result.append(triadDelimeter);
            result.append(string.substring(index, index + 3));
            index += 3;
        }
        if (result.charAt(0) == triadDelimeter) {
            result.deleteCharAt(0);
        }
        if (signChar!=null){
            result.insert(0,signChar.charValue());
        }
        return result.toString();
    }

    private String insertPads(final String s, final Locale locale) {
        if (minLength <= 0) {
            return s;
        }
        final char minusChar = getSymbols(locale).getMinusSign();
        final char plusChar = '+';
        final Character signChar;
        String ret = s;
        if (s.charAt(0) == minusChar || s.charAt(0) == plusChar) {
            signChar = Character.valueOf(s.charAt(0));
            ret = ret.substring(1);            
        }else{
            signChar = null;
        }
        while (ret.length() < minLength) {
            ret = getPadChar() + ret;//NOPMD
        }
        return (signChar==null ? ret : signChar.charValue() + ret);
    }

    @Override
    public ValidationResult validate(final IClientEnvironment environment, final Object value) {
        if (value == null) {
            return ValidationResult.ACCEPTABLE;
        }
        if (value instanceof Arr) {
            return validateArray(environment, (Arr) value);
        }
        if (value instanceof Long) {
            Long lvalue = (Long) value;
            if (lvalue < getMinValue()) {
                final String minValueAsStr = NumberFormat.getIntegerInstance().format(getMinValue());
                final InvalidValueReason reason =
                        InvalidValueReason.Factory.createForTooSmallValue(environment, minValueAsStr);
                return ValidationResult.Factory.newInvalidResult(reason);
            }
            if (lvalue > getMaxValue()) {
                final String maxValueAsStr = NumberFormat.getIntegerInstance().format(getMaxValue());
                final InvalidValueReason reason =
                        InvalidValueReason.Factory.createForTooBigValue(environment, maxValueAsStr);
                return ValidationResult.Factory.newInvalidResult(reason);
            }
            return ValidationResult.ACCEPTABLE;
        }
        return ValidationResult.Factory.newInvalidResult(InvalidValueReason.Factory.createForInvalidValueType(environment));
    }

    @Override
    public String toStr(final IClientEnvironment environment, final Object o) {
        if (o == null) {
            return getNoValueStr(environment.getMessageProvider());
        } else if (o instanceof Long) {
            Long x = (Long) o;
            String s = Long.toString(x, numberBase).toUpperCase();
            return formatInputString(environment, insertPads(s,environment.getLocale()));
        } else if (o instanceof ArrInt) {
            return arrToStr(environment, (ArrInt) o);
        } /*
         * else if (o instanceof LongEnum) { return ((LongEnum)o).getTitle();
        }
         */ else {
            return "type mismatch";
        }
    }
    
    public String formatInputString(final IClientEnvironment environment, final String numberAsStr){        
        if (getNumberBase() == 10){
            return insertDelimeters(numberAsStr, environment.getLocale());
        }else{
            return numberAsStr;
        }
    }
    
    public Long fromStr(final IClientEnvironment environment, final String string){
        final Character triadDelimeter = getTriadDelimeter(environment.getLocale());
        final String preparsedString;
        if (triadDelimeter==null || triadDelimeter.charValue()=='\0'){
            preparsedString = string;
        }else{            
            preparsedString = string.replaceAll(String.valueOf(triadDelimeter.charValue()), "");
        }
        return Long.parseLong(preparsedString,getNumberBase());
    }
    
    @Override
    public EEditMaskType getType() {
        return EEditMaskType.INT;
    }

    @Override
    public EnumSet<EValType> getSupportedValueTypes() {
        return SUPPORTED_VALTYPES;
    }

    private static void checkTriadDelimeter(final Character delimeter) {
        if (delimeter != null) {
            if (Character.isDigit(delimeter.charValue())) {
                throw new IllegalArgumentException("Triad delimeter character cannot be a digit character");
            }
            if (delimeter.charValue() == '-') {
                throw new IllegalArgumentException("Triad delimeter character cannot be equal to '-' character");
            }
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + this.minLength;
        hash = 29 * hash + Objects.hashCode(this.padChar);
        hash = 29 * hash + (int) (this.stepSize ^ (this.stepSize >>> 32));
        hash = 29 * hash + Objects.hashCode(this.minValue);
        hash = 29 * hash + Objects.hashCode(this.maxValue);
        hash = 29 * hash + (this.triadDelimeterType != null ? this.triadDelimeterType.hashCode() : 0);
        hash = 29 * hash + Objects.hashCode(this.customTriadDelimeter);
        hash = 29 * hash + this.numberBase;
        hash = 29 * hash + super.hashCode();
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
        final EditMaskInt other = (EditMaskInt) obj;
        if (this.minLength != other.minLength) {
            return false;
        }
        if (!Objects.equals(this.padChar, other.padChar)) {
            return false;
        }
        if (this.stepSize != other.stepSize) {
            return false;
        }
        if (!Objects.equals(this.minValue, other.minValue)) {
            return false;
        }
        if (!Objects.equals(this.maxValue, other.maxValue)) {
            return false;
        }
        if (this.triadDelimeterType != other.triadDelimeterType) {
            return false;
        }
        if (!Objects.equals(this.customTriadDelimeter, other.customTriadDelimeter)) {
            return false;
        }
        if (this.numberBase != other.numberBase) {
            return false;
        }
        return super.equals(obj);
    }        
}
