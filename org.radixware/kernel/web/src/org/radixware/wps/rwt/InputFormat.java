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

package org.radixware.wps.rwt;

import org.radixware.kernel.common.html.Html;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.mask.*;
import org.radixware.kernel.common.client.meta.mask.validators.BigDecimalValidator;
import org.radixware.kernel.common.client.meta.mask.validators.IInputValidator;
import org.radixware.kernel.common.client.meta.mask.validators.LongValidator;


public interface InputFormat {
    
    public static class Factory{
        
        private Factory(){                        
        }
        
        private final static FreeInputFormat FREE_INPUT_FORMAT = new FreeInputFormat();
        private final static BinInputFormat Bin_INPUT_FORMAT = new BinInputFormat();
        private final static BoolInputFormat BOOL_INPUT_FORMAT = new BoolInputFormat();
        private final static Character PLUS_SYMBOL = Character.valueOf('+');
        private final static Character DEFAULT_MINUS_SYMBOL = Character.valueOf('-');
        private final static Character DEFAULT_DECIMAL_SEPARATOR = Character.valueOf('.');
        private final static DecimalFormatSymbols DEFAULT_DECIMAL_SYMBOLS = new DecimalFormatSymbols();
        {
            DEFAULT_DECIMAL_SYMBOLS.setMinusSign(DEFAULT_MINUS_SYMBOL.charValue());
            DEFAULT_DECIMAL_SYMBOLS.setDecimalSeparator(DEFAULT_DECIMAL_SEPARATOR.charValue());
        }

        public static InputFormat intInputFormat(final long minValue, 
                                                 final long maxValue, 
                                                 final byte radix,
                                                 final Character triadDelimeter,
                                                 final DecimalFormatSymbols symbols                                                
                                                 ){
            final NumberSymbols numberSymbols = 
                new NumberSymbols(PLUS_SYMBOL, symbols.getMinusSign(), triadDelimeter, null);
            return NumInputFormat.getInstanceForInteger(radix, numberSymbols, minValue, maxValue);
        }
        
        public static InputFormat numInputFormat(final BigDecimal minValue, 
                                                final BigDecimal maxValue, 
                                                final int precision, 
                                                final Character triadDelimeter, 
                                                final char decimalDelimeter, 
                                                final DecimalFormatSymbols symbols){
            final NumberSymbols numberSymbols = 
                new NumberSymbols(PLUS_SYMBOL, symbols.getMinusSign(), triadDelimeter, decimalDelimeter);
            return NumInputFormat.getInstanceForDecimal(precision, numberSymbols, minValue, maxValue);
        }
        
        public static InputFormat numInputFormat(final BigDecimal minValue, 
                                                final BigDecimal maxValue, 
                                                final int precision,
                                                final DecimalFormatSymbols symbols,
                                                final boolean isGroupingUsed){
            return NumInputFormat.getInstanceForDecimal(precision, new NumberSymbols(symbols,isGroupingUsed), minValue, maxValue);
        }
                
        public static InputFormat maskInputFormat(final String inputMask){
            return new MaskInputFormat(inputMask);
        }
        
        public static InputFormat freeInputFormat(){
            return FREE_INPUT_FORMAT;
        }
        
        public static InputFormat binInputFormat(){
            return Bin_INPUT_FORMAT;
        }
        
        public static InputFormat boolInputFormat(){
            return BOOL_INPUT_FORMAT;
        }        
        
        @Deprecated
        public static InputFormat inputFormatFromEditMask(final EditMask editMask, final Locale locale){
            if (editMask.getType()!=null){
                switch (editMask.getType()){                
                    case INT:{
                        final EditMaskInt maskInt = (EditMaskInt)editMask;
                        return intInputFormat(maskInt.getMinValue(), 
                                              maskInt.getMaxValue(), 
                                              maskInt.getNumberBase(), 
                                              maskInt.getTriadDelimeter(locale), 
                                              DecimalFormatSymbols.getInstance(locale));
                    }
                    case NUM: {
                        final EditMaskNum maskNum = (EditMaskNum)editMask;
                        final NumberFormat format = maskNum.getNumberFormat(locale);
                        if (format instanceof DecimalFormat){
                            return numInputFormat(maskNum.getMinValue(),
                                                  maskNum.getMaxValue(),
                                                  maskNum.getPrecision(),
                                                  ((DecimalFormat)format).getDecimalFormatSymbols(),
                                                  format.isGroupingUsed());
                        }else{
                            return numInputFormat(maskNum.getMinValue(),
                                                  maskNum.getMaxValue(),
                                                  maskNum.getPrecision(),
                                                  maskNum.getTriadDelimeter(locale),
                                                  maskNum.getDecimalDelimeter(locale),                                                  
                                                  new DecimalFormatSymbols(locale));
                        }
                    }
                    case STR: {
                        final EditMaskStr maskStr = (EditMaskStr)editMask;
                        final IInputValidator validator = maskStr.getValidator();
                        if (validator instanceof LongValidator){
                            final LongValidator intValidator = (LongValidator)validator;
                            final long minValue = 
                                intValidator.getMinValue()==null ? Long.MIN_VALUE : intValidator.getMinValue().longValue();
                            final long maxValue = 
                                intValidator.getMaxValue()==null ? Long.MAX_VALUE : intValidator.getMaxValue().longValue();
                            return intInputFormat(minValue, maxValue, (byte)10, null, DEFAULT_DECIMAL_SYMBOLS);
                        }
                        else if (validator instanceof BigDecimalValidator){
                            final BigDecimalValidator numValidator = (BigDecimalValidator)validator;                            
                            return numInputFormat(numValidator.getMinValue(), 
                                                  numValidator.getMaxValue(),
                                                  numValidator.getPrecision(),
                                                  null,
                                                  DEFAULT_DECIMAL_SEPARATOR,
                                                  DEFAULT_DECIMAL_SYMBOLS);                            
                        }
                        else if (!maskStr.getInputMask().isEmpty()){
                            return maskInputFormat(maskStr.getInputMask().getPattern());
                        }
                        else{
                            return freeInputFormat();
                        }
                    }
                    case DATE_TIME: {
                        final EditMaskDateTime maskDateTime = (EditMaskDateTime)editMask;
                        return maskInputFormat(maskDateTime.getInputMask(locale));
                    }
                    case TIME_INTERVAL: {
                        final EditMaskTimeInterval maskDateTime = (EditMaskTimeInterval)editMask;
                        return maskInputFormat(maskDateTime.getInputMask());
                    }                        
                    default:{
                        return freeInputFormat();
                    }                
                }
            }
            else{
                return freeInputFormat();
            }
        }
        
        public static InputFormat inputFormatFromEditMask(final EditMask editMask, final IClientEnvironment environment){
            if (editMask.getType()!=null){
                switch (editMask.getType()){
                    case INT:{
                        final EditMaskInt maskInt = (EditMaskInt)editMask;
                        return intInputFormat(maskInt.getMinValue(), 
                                                maskInt.getMaxValue(), 
                                                maskInt.getNumberBase(), 
                                                maskInt.getTriadDelimeter(environment), 
                                                DecimalFormatSymbols.getInstance(environment.getLocale()));
                    }
                    case NUM: {
                        final EditMaskNum maskNum = (EditMaskNum)editMask;
                        final NumberFormat format = maskNum.getNumberFormat(environment);
                        if (format instanceof DecimalFormat){
                            return numInputFormat(maskNum.getMinValue(),
                                                    maskNum.getMaxValue(),
                                                    maskNum.getPrecision(),
                                                    ((DecimalFormat)format).getDecimalFormatSymbols(),
                                                    format.isGroupingUsed());
                        }else{
                            return numInputFormat(maskNum.getMinValue(),
                                                  maskNum.getMaxValue(),
                                                  maskNum.getPrecision(),
                                                  maskNum.getTriadDelimeter(environment),
                                                  maskNum.getDecimalDelimeter(environment),                                                  
                                                  new DecimalFormatSymbols(environment.getLocale()));
                        }
                    }
                    case STR: {
                        final EditMaskStr maskStr = (EditMaskStr)editMask;
                        final IInputValidator validator = maskStr.getValidator();
                        if (validator instanceof LongValidator){
                            final LongValidator intValidator = (LongValidator)validator;
                            final long minValue = 
                                intValidator.getMinValue()==null ? Long.MIN_VALUE : intValidator.getMinValue().longValue();
                            final long maxValue = 
                                intValidator.getMaxValue()==null ? Long.MAX_VALUE : intValidator.getMaxValue().longValue();
                            return intInputFormat(minValue, maxValue, (byte)10, null, DEFAULT_DECIMAL_SYMBOLS);
                        }
                        else if (validator instanceof BigDecimalValidator){
                            final BigDecimalValidator numValidator = (BigDecimalValidator)validator;                            
                            return numInputFormat(numValidator.getMinValue(), 
                                                  numValidator.getMaxValue(),
                                                  numValidator.getPrecision(),
                                                  null,
                                                  DEFAULT_DECIMAL_SEPARATOR,
                                                  DEFAULT_DECIMAL_SYMBOLS);                            
                        }
                        else if (!maskStr.getInputMask().isEmpty()){
                            return maskInputFormat(maskStr.getInputMask().getPattern());
                        }
                        else{
                            return freeInputFormat();
                        }
                    }
                    case DATE_TIME: {
                        final EditMaskDateTime maskDateTime = (EditMaskDateTime)editMask;
                        return maskInputFormat(maskDateTime.getInputMask(environment));
                    }
                    case TIME_INTERVAL: {
                        final EditMaskTimeInterval maskDateTime = (EditMaskTimeInterval)editMask;
                        return maskInputFormat(maskDateTime.getInputMask());
                    }                        
                    default:{
                        return freeInputFormat();
                    }                
                }
            }else{
                return freeInputFormat();
            }
        }    
    }
        
    static enum EInputType{
        Text, Int, Num, Bin, Bool, Mask
    }    

    
    void writeToHtml(Html html);
    
    static class IntInputFormat implements InputFormat{
        
        private final boolean canBeNegative;
        private final boolean canBePositive;
        private final int maxDigitsCount;
        private final byte radix;
        
        private IntInputFormat(final long minValue, final long maxValue, final byte radix){
            canBeNegative = minValue<0;
            canBePositive = maxValue>0;            
            int minValueLength = Long.toString(minValue,radix).length();
            if (minValue<0){
                minValueLength--;
            }                        
            final int maxValueLength = Long.toString(maxValue,radix).length();        
            maxDigitsCount = Math.max(minValueLength,maxValueLength);
            this.radix = radix;
        }

        @Override
        public void writeToHtml(Html html) {
            html.setAttr("inputtype", EInputType.Int.name());
            html.setAttr("maxDigitsCount", maxDigitsCount);
            if (canBeNegative){
                html.setAttr("canBeNegative",null);
            }
            else{
                html.setAttr("canBeNegative",false);
            }
            if (canBePositive){
                html.setAttr("canBePositive",null);
            }
            if (radix==10){
                html.setAttr("radix", null);
            }
            else{
                html.setAttr("radix", radix);
            }            
        }        
    }
    
    static class NumberSymbols{
        public final Character plus;
        public final Character minus;
        public final Character triadsDelimeter;
        public final Character decimalPoint;
        
        public NumberSymbols(final Character plusSign, 
                             final Character minusSign,
                             final Character triadsDelimeter,
                             final Character decimalDelimeter){
            plus = plusSign;
            minus = minusSign;
            this.triadsDelimeter = triadsDelimeter;
            this.decimalPoint = decimalDelimeter;
        }
        
        public NumberSymbols(final DecimalFormatSymbols symbols, final boolean isGroupingUsed){
            plus = Character.valueOf('+');
            minus = Character.valueOf(symbols.getMinusSign());
            triadsDelimeter = isGroupingUsed ? Character.valueOf(symbols.getGroupingSeparator()) : null;
            decimalPoint = Character.valueOf(symbols.getDecimalSeparator());
        }
    }
    
    static class NumInputFormat implements InputFormat{
        
        private final int precision;
        private final NumberSymbols symbols;
        private final int radix;
        private final boolean isInteger;
        private final boolean canBeNegative;
        private final boolean canBePositive;
        private final int maxIntDigitsCount;
        
        private NumInputFormat(final boolean isInteger, 
                               final int precision, 
                               final int radix, 
                               final NumberSymbols symbols,
                               final boolean canBeNegative,
                               final boolean canBePositive,
                               final int maxDigitsCount
                               ){
            this.isInteger = isInteger || precision==0;
            this.precision = precision;
            this.radix = radix;
            this.symbols = symbols;
            this.canBeNegative = canBeNegative;
            this.canBePositive = canBePositive;
            this.maxIntDigitsCount = maxDigitsCount;
        }
        
        static NumInputFormat getInstanceForInteger(final int radix, final NumberSymbols symbols, final long minValue, final long maxValue){
            int minValueLength = Long.toString(minValue,radix).length();
            if (minValue<0){
                minValueLength--;
            }                        
            final int maxValueLength = Long.toString(maxValue,radix).length();                    
            return new NumInputFormat(true, -1, radix, symbols, minValue<0, maxValue>0, Math.max(minValueLength,maxValueLength));
        }
        
        static NumInputFormat getInstanceForDecimal(final int precision, final NumberSymbols symbols, final BigDecimal minValue, final BigDecimal maxValue){
            return new NumInputFormat(false, precision, 10, symbols, minValue==null || minValue.signum()<0, maxValue==null || maxValue.signum()>0, -1);
        }

        @Override
        public void writeToHtml(Html html) {
            html.setAttr("inputtype", EInputType.Num.name());                    
            html.setAttr("isIntegerNumber", isInteger);            
            if (precision<0){
                html.setAttr("precision", null);
            }
            else{
                html.setAttr("precision",precision);
            }
            if (radix==10){
                html.setAttr("radix", null);
            }else{
                html.setAttr("radix", radix);
            }
            if (symbols.plus==null){
                html.setAttr("plusSign", null);
            }else{
                html.setAttr("plusSign",symbols.plus.toString());
            }
            if (symbols.minus==null){
                html.setAttr("minusSign", null);
            }else{
                html.setAttr("minusSign",symbols.minus.toString());
            }
            if (symbols.decimalPoint==null){
                html.setAttr("decimalDelimeter", null);
            }else{
                html.setAttr("decimalDelimeter",symbols.decimalPoint.toString());
            }
            if (symbols.triadsDelimeter==null){
                html.setAttr("triadDelimeter", null);
            }else{
                html.setAttr("triadDelimeter", symbols.triadsDelimeter.toString());
            }
            if (canBeNegative){
                html.setAttr("canBeNegative", null);
            }else{
                html.setAttr("canBeNegative", "false");
            }
            if (canBePositive){
                html.setAttr("canBePositive", null);
            }else{
                html.setAttr("canBePositive", "false");
            }
            if (maxIntDigitsCount>0){
                html.setAttr("maxIntDigitsCount", maxIntDigitsCount);
            }else{
                html.setAttr("maxIntDigitsCount", null);
            }
        }
    }
    
    static class FreeInputFormat implements InputFormat{
        
        private FreeInputFormat(){            
        }
        
        @Override
        public void writeToHtml(Html html) {
            html.setAttr("inputtype", EInputType.Text.name());
        }        
    }
    
    static class BinInputFormat implements InputFormat{
        
        private BinInputFormat(){
        }
        
        @Override
        public void writeToHtml(Html html) {
            html.setAttr("inputtype", EInputType.Bin.name());
        }        
    }    
    
    static class BoolInputFormat implements InputFormat{
                
        
        private BoolInputFormat(){
        }
        
        @Override
        public void writeToHtml(Html html) {
            html.setAttr("inputtype", EInputType.Bool.name());
        }
    }    
    
    static class MaskInputFormat implements InputFormat{
        
        private final String pattern;
        
        public MaskInputFormat(final String pattern){
            this.pattern = pattern;
        }

        @Override
        public void writeToHtml(Html html) {
            html.setAttr("inputtype", EInputType.Mask.name());            
            html.setAttr("pattern",pattern);            
        }
        
    }
    
}