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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.EnumSet;
import java.util.Locale;
import java.util.Objects;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.meta.mask.validators.InvalidValueReason;
import org.radixware.kernel.common.client.meta.mask.validators.ValidationResult;
import org.radixware.kernel.common.client.utils.ValueConverter;
import org.radixware.kernel.common.enums.EEditMaskType;
import org.radixware.kernel.common.enums.ETriadDelimeterType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Arr;
import org.radixware.kernel.common.types.ArrNum;

public final class EditMaskNum extends org.radixware.kernel.common.client.meta.mask.EditMask {
    
    private final static String GROUPING_SEPARATOR_SETTING_NAME=SettingNames.SYSTEM+"/"
        +SettingNames.FORMAT_SETTINGS+"/"
        +SettingNames.FormatSettings.NUMBER+"/"+SettingNames.FormatSettings.Number.GROUP_SEPARATOR;
    private final static String DECIMAL_MARK_SETTING_NAME=SettingNames.SYSTEM+"/"
        +SettingNames.FORMAT_SETTINGS+"/"
        +SettingNames.FormatSettings.NUMBER+"/"+SettingNames.FormatSettings.Number.DECIMAL_PART_SEPARATOR;    

    private BigDecimal minValue = null;
    private BigDecimal maxValue = null;
    private long scale = 1;
    private ETriadDelimeterType triadDelimeterType = ETriadDelimeterType.NONE;
    private Character customTriadDelimeter = null;
    private Character customDecimalDelimeter = null;
    private int precision = -1;
    private NumberFormat cachedNumberFormat;    
    private String cacheKey;
    private static final EnumSet<EValType> SUPPORTED_VALTYPES =
            EnumSet.of(EValType.NUM, EValType.ARR_NUM);

    public EditMaskNum(
            final BigDecimal minValue,
            final BigDecimal maxValue,
            final long scale, //Масштаб
            final ETriadDelimeterType triadDelimeterType,//Тип разделителя разрядов
            final Character triadDelimeter, //Разделитель разрядов
            final Character decimalDelimeter,//Разделитель целой и дробной части
            final byte precision //Точность
            ) {
        super();
        if (minValue != null) {
            this.minValue = minValue;
        }
        if (maxValue != null) {
            this.maxValue = maxValue;
        }
        this.scale = scale;
        if (triadDelimeterType == null) {
            this.triadDelimeterType = triadDelimeter == null ? ETriadDelimeterType.NONE : ETriadDelimeterType.SPECIFIED;
        } else {
            this.triadDelimeterType = triadDelimeterType;
        }
        checkTriadDelimeter(triadDelimeter);
        this.customTriadDelimeter = triadDelimeter;
        checkDecimalDelimeter(decimalDelimeter);
        this.customDecimalDelimeter = decimalDelimeter;
        this.precision = precision;
    }

    public EditMaskNum(final BigDecimal minValue,
            final BigDecimal maxValue,
            final long scale, //Масштаб
            final Character triadDelimeter, //Разделитель разрядов
            final byte precision //Точность
            ) {
        this(minValue, maxValue, scale, null, triadDelimeter, null, precision);
    }

    public EditMaskNum() {
        this(null, null, 1, null, null, null, (byte) -1);
    }

    public EditMaskNum(final EditMaskNum source) {
        super();
        this.minValue = source.minValue;
        this.maxValue = source.maxValue;
        this.scale = source.scale;
        this.triadDelimeterType = source.triadDelimeterType;
        this.customTriadDelimeter = source.customTriadDelimeter;
        this.customDecimalDelimeter = source.customDecimalDelimeter;
        this.precision = source.precision;
    }

    protected EditMaskNum(final org.radixware.schemas.editmask.EditMaskNum editMask) {
        super();
        if (editMask.isSetMinValue()) {
            minValue = editMask.getMinValue();
        }
        if (editMask.isSetMaxValue()) {
            maxValue = editMask.getMaxValue();
        }
        scale = editMask.getScale();
        if (editMask.isSetTriadDelimeter() && editMask.getTriadDelimeter().length() > 0) {
            final Character delimeter = Character.valueOf(editMask.getTriadDelimeter().charAt(0));
            checkTriadDelimeter(delimeter);
            customTriadDelimeter = delimeter;
        }
        if (editMask.getTriadDelimeterType() == null) {
            this.triadDelimeterType = customTriadDelimeter == null ? ETriadDelimeterType.NONE : ETriadDelimeterType.SPECIFIED;
        } else {
            this.triadDelimeterType = editMask.getTriadDelimeterType();
        }
        if (editMask.isSetDecimalDelimeter() && editMask.getDecimalDelimeter().length() > 0) {
            final Character delimeter = Character.valueOf(editMask.getDecimalDelimeter().charAt(0));
            checkDecimalDelimeter(delimeter);
            customDecimalDelimeter = delimeter;
        }
        if (editMask.isSetPrecision()) {
            precision = editMask.getPrecision();
        }
    }

    @Override
    public void writeToXml(final org.radixware.schemas.editmask.EditMask editMask) {
        final org.radixware.schemas.editmask.EditMaskNum editMaskNum = editMask.addNewNum();
        if (minValue != null) {
            editMaskNum.setMinValue(minValue);
        }
        if (maxValue != null) {
            editMaskNum.setMaxValue(maxValue);
        }
        editMaskNum.setTriadDelimeterType(triadDelimeterType);
        if (customTriadDelimeter != null) {
            editMaskNum.setTriadDelimeter(Character.toString(customTriadDelimeter));
        }
        if (customDecimalDelimeter != null) {
            editMaskNum.setDecimalDelimeter(Character.toString(customDecimalDelimeter));
        }
        editMaskNum.setScale(scale);
        if (precision > -1) {
            editMaskNum.setPrecision((byte) precision);
        }
    }

    public BigDecimal getMinValue() {
        return minValue == null ? BigDecimal.valueOf(Double.MAX_VALUE).negate() : minValue;
    }

    public void setMinValue(final BigDecimal minValue) {
        this.minValue = minValue;
        afterModify();
    }

    public BigDecimal getMaxValue() {
        return maxValue == null ? BigDecimal.valueOf(Double.MAX_VALUE) : maxValue;
    }

    public void setMaxValue(final BigDecimal maxValue) {
        this.maxValue = maxValue;
        afterModify();
    }

    public ETriadDelimeterType getTriadDelimeterType() {
        return triadDelimeterType;
    }

    public void setTriadDelimeterType(final ETriadDelimeterType triadDelimeterType) {
        this.triadDelimeterType = triadDelimeterType;
        cachedNumberFormat = null;
        afterModify();
    }

    public Character getCustomTriadDelimeter() {
        return customTriadDelimeter;
    }

    public void setCustomTriadDelimeter(final Character delimeter) {
        checkTriadDelimeter(delimeter);
        this.customTriadDelimeter = delimeter;
        triadDelimeterType = customTriadDelimeter == null ? ETriadDelimeterType.NONE : ETriadDelimeterType.SPECIFIED;
        cachedNumberFormat = null;
        afterModify();
    }

    public Character getCustomDecimalDelimeter() {
        return customDecimalDelimeter;
    }

    public void setCustomDecimalDelimeter(final Character delimeter) {
        checkDecimalDelimeter(delimeter);
        this.customDecimalDelimeter = delimeter;
        cachedNumberFormat = null;
        afterModify();
    }

    public int getPrecision() {
        return precision;
    }

    public void setPrecision(final int precision) {
        this.precision = precision;
        cachedNumberFormat = null;
        afterModify();
    }

    public long getScale() {
        return scale == 0 ? 1 : scale;
    }

    public void setScale(final long scale) {
        this.scale = scale;
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

    private static void checkDecimalDelimeter(final Character delimeter) {
        if (delimeter != null) {
            if (Character.isDigit(delimeter.charValue())) {
                throw new IllegalArgumentException("Decimal delimeter character cannot be a digit character");
            }
            if (delimeter.charValue() == '-') {
                throw new IllegalArgumentException("Decimal delimeter character cannot be equal to '-' character");
            }
        }
    }

    public String getRounded(final IClientEnvironment environment, final Object o) {
        if (o == null) {
            return getNoValueStr(environment.getMessageProvider());
        } else if (o instanceof ArrNum) {
            return arrToStr(environment, (ArrNum) o);
        } else {
            //final NumberFormat roundedNumberFormat = (NumberFormat) NumberFormat.getNumberInstance(environment.getLocale()).clone();
            //roundedNumberFormat.setGroupingUsed(false);
            final NumberFormat roundedNumberFormat = (NumberFormat)getNumberFormat(environment).clone();
            if (getPrecision() >= 0) {
                roundedNumberFormat.setRoundingMode(RoundingMode.HALF_EVEN);
                roundedNumberFormat.setMaximumFractionDigits(getPrecision());
            } else {
                roundedNumberFormat.setMaximumFractionDigits(Integer.MAX_VALUE);
            }
            /*
            if (getCustomDecimalDelimeter() != null && roundedNumberFormat instanceof DecimalFormat) {
                final DecimalFormatSymbols symbols = new DecimalFormatSymbols(environment.getLocale());
                symbols.setDecimalSeparator(getCustomDecimalDelimeter().charValue());
                ((DecimalFormat) roundedNumberFormat).setDecimalFormatSymbols(symbols);
            }*/
            if (o instanceof Double) {
                return roundedNumberFormat.format((Double) o * getScale());
            } else if (o instanceof BigDecimal) {
                return roundedNumberFormat.format(((BigDecimal) o).multiply(BigDecimal.valueOf(getScale())));
            } else {
                return "type mismatch";
            }
        }
    }
    
    public BigDecimal getNormalized(final BigDecimal value){
        if (value == null){
            return null;
        }else{
            if (getPrecision()>=0 && 
                value.scale()!=getPrecision() && 
                (value.compareTo(BigDecimal.ZERO)==0 || value.stripTrailingZeros().scale()<getPrecision()))
            {
                return value.setScale(getPrecision());
            }else{
                return value;
            }
        }
    }
    
    public NumberFormat getNumberFormat(final IClientEnvironment environment) {
        return getNumberFormat(environment, environment.getLocale());
    }
    
    public NumberFormat getNumberFormat(final Locale locale) {
        return getNumberFormat(null, locale);
    }
    
    private NumberFormat getNumberFormat(final IClientEnvironment environment, final Locale locale){
        if (cachedNumberFormat == null || !Objects.equals(cacheKey, calcCacheKey(environment))) {
            cachedNumberFormat = (NumberFormat) NumberFormat.getNumberInstance(locale).clone();
            cachedNumberFormat.setRoundingMode(RoundingMode.UNNECESSARY);
            if (getPrecision()>=0){
                if (Math.abs(getScale())>=10){
                    final int scaledPrecision = (int)Math.log10(Math.abs(getScale()));
                    if (scaledPrecision<precision){
                        cachedNumberFormat.setMinimumFractionDigits(precision - scaledPrecision);
                    }
                }else{
                    cachedNumberFormat.setMinimumFractionDigits(getPrecision());
                }                                
            }
            cachedNumberFormat.setMaximumFractionDigits(Integer.MAX_VALUE);
            if (cachedNumberFormat instanceof DecimalFormat) {
                setupDecimalFormatSymbols((DecimalFormat) cachedNumberFormat, locale, environment);
                cacheKey = calcCacheKey(environment);
            }
        }
        return cachedNumberFormat;           
    }
    
    private void setupDecimalFormatSymbols(final DecimalFormat format, final Locale locale, final IClientEnvironment environment){
        final Character triadDelimeter = getTriadDelimeterImpl(environment);
        final boolean noTriadDelimeter = triadDelimeter!=null && Character.valueOf('\0').equals(triadDelimeter);
        final Character decimalDelimeter = getDecimalDelimeterImpl(environment);        
        if (noTriadDelimeter){
            format.setGroupingUsed(false);
        }
        if ((triadDelimeter!=null && !noTriadDelimeter) || decimalDelimeter!=null){            
            final DecimalFormatSymbols symbols = new DecimalFormatSymbols(locale);
            if (triadDelimeter!=null && !noTriadDelimeter){
                format.setGroupingUsed(true);
                symbols.setGroupingSeparator(triadDelimeter);                
            }
            if (decimalDelimeter!=null){
                symbols.setDecimalSeparator(decimalDelimeter);
            }
            format.setDecimalFormatSymbols(symbols);
        }
        format.setParseBigDecimal(true);
    }
    
    private Character getTriadDelimeterImpl(final IClientEnvironment environment){
        if (triadDelimeterType == ETriadDelimeterType.NONE || (triadDelimeterType == ETriadDelimeterType.SPECIFIED && customTriadDelimeter == null)) {
            return '\0';
        }
        if (triadDelimeterType == ETriadDelimeterType.SPECIFIED && customTriadDelimeter != null){
            return customTriadDelimeter;
        }
        return environment==null ? null : readTriadDelimeterFromConfig(environment);
    }
    
    private Character getDecimalDelimeterImpl(final IClientEnvironment environment){
        if (customDecimalDelimeter == null){
            return environment==null ? null : readDecimalDelimeterFromConfig(environment);
        }else{
            return customDecimalDelimeter;
        }
    }
    
    private static Character readTriadDelimeterFromConfig(final IClientEnvironment environment){
        final String delimeter = environment.getConfigStore().readString(GROUPING_SEPARATOR_SETTING_NAME,null);
        return delimeter==null || delimeter.isEmpty() ? null : delimeter.charAt(0);
    }
    
    private static Character readDecimalDelimeterFromConfig(final IClientEnvironment environment){
        final String delimeter = environment.getConfigStore().readString(DECIMAL_MARK_SETTING_NAME, null);
        return delimeter==null || delimeter.isEmpty() ? null : delimeter.charAt(0);
    }
    
    private String calcCacheKey(final IClientEnvironment environment){                
        final Character triadDelimeter = getTriadDelimeterImpl(environment);
        final Character decimalDelimeter = getDecimalDelimeterImpl(environment);
        if (triadDelimeter==null && decimalDelimeter==null){
            return null;
        }
        String key = triadDelimeter==null ? "0" : String.valueOf(triadDelimeter);
        if (decimalDelimeter!=null){
            key+=String.valueOf(decimalDelimeter);
        }
        return key;
    }

    @Override
    public String toStr(final IClientEnvironment environmnet, final Object o) {
        //if (wasInherited()) return INHERITED_VALUE;
        if (o == null) {
            return getNoValueStr(environmnet.getMessageProvider());
        } else if (o instanceof Double) {
            final Double value = (Double) o * getScale();
            return getNumberFormat(environmnet).format(value);
        } else if (o instanceof BigDecimal) {
            final BigDecimal value = ((BigDecimal) o).multiply(BigDecimal.valueOf(getScale()));
            return getNumberFormat(environmnet).format(value);
        } else if (o instanceof ArrNum) {
            return arrToStr(environmnet, (ArrNum) o);
        } else {
            return "type mismatch";
        }
    }
    
    public BigDecimal fromStr(final IClientEnvironment environment, final String inputString) throws NumberFormatException{
        return ValueConverter.parseBigDecimal(inputString, getNumberFormat(environment));
    }
    
    public char getDecimalDelimeter(final IClientEnvironment environment){
        final NumberFormat numFormat = getNumberFormat(environment);
        if (numFormat instanceof DecimalFormat){
            return ((DecimalFormat)numFormat).getDecimalFormatSymbols().getDecimalSeparator();
        }else{
            return '.';
        }
    }
    
    @Deprecated //use getDecimalDelimeter(final IClientEnvironment environment)
    public char getDecimalDelimeter(final Locale locale){
        final NumberFormat numFormat = getNumberFormat(locale);
        if (numFormat instanceof DecimalFormat){
            return ((DecimalFormat)numFormat).getDecimalFormatSymbols().getDecimalSeparator();
        }else{
            return '.';
        }
    }
    
    @Deprecated //use getTriadDelimeter(final IClientEnvironment environment)
    public Character getTriadDelimeter(final Locale locale){
        final NumberFormat numFormat = getNumberFormat(locale);
        if (numFormat.isGroupingUsed() && numFormat instanceof DecimalFormat){
            return ((DecimalFormat)numFormat).getDecimalFormatSymbols().getGroupingSeparator();
        }else{
            return null;
        }
    }
        
    public Character getTriadDelimeter(final IClientEnvironment environment){
        final NumberFormat numFormat = getNumberFormat(environment);
        if (numFormat.isGroupingUsed() && numFormat instanceof DecimalFormat){
            return ((DecimalFormat)numFormat).getDecimalFormatSymbols().getGroupingSeparator();
        }else{
            return null;
        }
    } 
    
    @Deprecated //use getSymbols(final IClientEnvironment environment)
    private DecimalFormatSymbols getSymbols(final Locale locale){
        final NumberFormat numFormat = getNumberFormat(locale);
        if (numFormat instanceof DecimalFormat){
            return ((DecimalFormat)numFormat).getDecimalFormatSymbols();
        }else{
            return DecimalFormatSymbols.getInstance(locale);
        }
    }
    
    private DecimalFormatSymbols getSymbols(final IClientEnvironment environment){
        final NumberFormat numFormat = getNumberFormat(environment);
        if (numFormat instanceof DecimalFormat){
            return ((DecimalFormat)numFormat).getDecimalFormatSymbols();
        }else{
            return DecimalFormatSymbols.getInstance(environment.getLocale());
        }
    }    
    
    public String formatInputString(final IClientEnvironment environment, final String inputString) throws NumberFormatException{
        final BigDecimal value = fromStr(environment, inputString);
        if (value==null){
            return null;
        }
        final StringBuffer buffer = new StringBuffer();
        final Locale locale = environment.getLocale();
        final char decimalDelimeter = getDecimalDelimeter(environment);
        final char minusCharacter = getSymbols(environment).getMinusSign();
        final char plusCharacter = '+';        
        final char zeroDigitChar = getSymbols(environment).getZeroDigit();
        final Character triadDelimeter = getTriadDelimeter(environment);                
        final int integerPartLength;
        int leadingZeroDigits = 0;
        int trailingZeroDigits = 0;        
        final boolean forcedDecimalDelimeter;
        {//Analize input string
            final int inputLength = inputString.length();
            final boolean forcedPositive = inputString.charAt(0)==plusCharacter;
            if (forcedPositive){
                buffer.append(plusCharacter);
            }else if (inputString.charAt(0)==minusCharacter && value.compareTo(BigDecimal.ZERO)==0){
                buffer.append(minusCharacter);
            }            
            final boolean signSymbol = forcedPositive || inputString.charAt(0)==minusCharacter;
            for (int i = signSymbol ? 1 : 0; i<inputLength; i++){
                if (inputString.charAt(i)==zeroDigitChar){
                    leadingZeroDigits++;
                }else if (triadDelimeter==null || triadDelimeter.charValue()!=inputString.charAt(i)){
                    break;
                }
            }
            int decimalDelimeterPos = inputString.lastIndexOf('.');
            if (decimalDelimeter!='.'){
                decimalDelimeterPos = 
                    Math.max(decimalDelimeterPos, inputString.lastIndexOf(decimalDelimeter));
            }
            int triadDelimetersCount = 0;
            if (triadDelimeter!=null){
                for (int i=0,length= decimalDelimeterPos>-1 ? decimalDelimeterPos : inputLength; i<length; i++){
                    if (triadDelimeter.charValue()==inputString.charAt(i)){
                        triadDelimetersCount++;
                    }
                }
            }
            if (decimalDelimeterPos>-1){
                integerPartLength = decimalDelimeterPos - (signSymbol ? 1 : 0) - triadDelimetersCount;
            }else{
                integerPartLength = inputLength - (signSymbol ? 1 : 0) - triadDelimetersCount;
            }
            forcedDecimalDelimeter = decimalDelimeterPos==inputLength-1;        
            if (!forcedDecimalDelimeter && decimalDelimeterPos>-1){            
                for (int i = inputLength - 1; i>=0; i--){
                    if (inputString.charAt(i)==zeroDigitChar){
                        trailingZeroDigits++;
                    }else if (triadDelimeter!=null && triadDelimeter.charValue()==inputString.charAt(i)){
                        continue;
                    }else {
                        break;
                    }
                }
            }        
        }
        {//format value
            final int valueScale = value.scale();
            final NumberFormat format = (NumberFormat)cachedNumberFormat.clone();
            if (leadingZeroDigits>0){                                        
                format.setMinimumIntegerDigits(integerPartLength);
            }
            if (valueScale<=0 && forcedDecimalDelimeter && format instanceof DecimalFormat){
                ((DecimalFormat)format).setDecimalSeparatorAlwaysShown(true);
                format.setMinimumFractionDigits(0);
            }else if (trailingZeroDigits>0){
                final int minFractionDigits = valueScale>0 ? valueScale : trailingZeroDigits;
                format.setMinimumFractionDigits(minFractionDigits);
            }else{
                format.setMinimumFractionDigits(0);
            }
            format.setMaximumFractionDigits(Integer.MAX_VALUE);
            buffer.append(format.format(value));
        }
        return buffer.toString();
    }    

    @Override
    public ValidationResult validate(final IClientEnvironment environment, final Object value) {
        return validate(environment, value, true);
    }
    
    public ValidationResult validate(final IClientEnvironment environment, final Object value, final boolean checkPrecision) {
        if (value == null) {
            return ValidationResult.ACCEPTABLE;
        }
        if (value instanceof Arr) {
            return validateArray(environment, (Arr) value);
        }
        if (value instanceof BigDecimal) {
            final BigDecimal val = (BigDecimal) value, minVal = getMinValue(), maxVal = getMaxValue();
            if (minVal != null && minVal.compareTo(val) > 0) {
                final InvalidValueReason reason =
                        InvalidValueReason.Factory.createForTooSmallValue(environment, toStr(environment, minVal));
                return ValidationResult.Factory.newInvalidResult(reason);
            }
            if (maxVal != null && maxVal.compareTo(val) < 0) {
                final InvalidValueReason reason =
                        InvalidValueReason.Factory.createForTooBigValue(environment, toStr(environment, maxVal));
                return ValidationResult.Factory.newInvalidResult(reason);
            }
            if (checkPrecision && precision >= 0 && val.scale() > precision) {
                final String reason = environment.getMessageProvider().translate("Value", "Number precision too large");
                return ValidationResult.Factory.newInvalidResult(reason);
            }            
            return ValidationResult.ACCEPTABLE;
        }
        return ValidationResult.Factory.newInvalidResult(InvalidValueReason.Factory.createForInvalidValueType(environment));
    }    

    @Override
    public EEditMaskType getType() {
        return EEditMaskType.NUM;
    }

    @Override
    public EnumSet<EValType> getSupportedValueTypes() {
        return SUPPORTED_VALTYPES;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.minValue);
        hash = 47 * hash + Objects.hashCode(this.maxValue);
        hash = 47 * hash + (int) (this.scale ^ (this.scale >>> 32));
        hash = 47 * hash + (this.triadDelimeterType != null ? this.triadDelimeterType.hashCode() : 0);
        hash = 47 * hash + Objects.hashCode(this.customTriadDelimeter);
        hash = 47 * hash + Objects.hashCode(this.customDecimalDelimeter);
        hash = 47 * hash + this.precision;
        hash = 47 * hash + super.hashCode();
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
        final EditMaskNum other = (EditMaskNum) obj;
        if (!Objects.equals(this.minValue, other.minValue)) {
            return false;
        }
        if (!Objects.equals(this.maxValue, other.maxValue)) {
            return false;
        }
        if (this.scale != other.scale) {
            return false;
        }
        if (this.triadDelimeterType != other.triadDelimeterType) {
            return false;
        }
        if (!Objects.equals(this.customTriadDelimeter, other.customTriadDelimeter)) {
            return false;
        }
        if (!Objects.equals(this.customDecimalDelimeter, other.customDecimalDelimeter)) {
            return false;
        }
        if (this.precision != other.precision) {
            return false;
        }
        return super.equals(obj);
    }        
}
