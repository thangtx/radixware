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

import java.util.ArrayDeque;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Queue;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.mask.validators.InvalidValueReason;
import org.radixware.kernel.common.client.meta.mask.validators.ValidationResult;
import org.radixware.kernel.common.enums.EEditMaskType;


import org.radixware.kernel.common.types.Arr;
import org.radixware.kernel.common.types.ArrDateTime;
import org.radixware.kernel.common.types.ArrInt;

import org.radixware.kernel.common.defs.ads.clazz.presentation.editmask.EditMaskTimeInterval.Scale;
import org.radixware.kernel.common.enums.EValType;


public final class EditMaskTimeInterval extends org.radixware.kernel.common.client.meta.mask.EditMask {

    private final Scale scale;
    private Long minValue = null;
    private Long maxValue = null;
    private String displayFormat = null;
    private static final EnumSet<EValType> SUPPORTED_VALTYPES =
            EnumSet.of(EValType.INT, EValType.ARR_INT);

    public EditMaskTimeInterval(final long scale,
            final String mask,
            final Long minValue,
            final Long maxValue) {
        super();        
        this.scale = getScaleByValue(scale);
        displayFormat = mask;
        if (minValue != null) {
            this.minValue = minValue;
        }
        if (maxValue != null) {
            this.maxValue = maxValue;
        }
    }
    /*
    public EditMaskTimeInterval(long scale,
    String mask
    ){
    this(scale,mask,null,null);
    }

    public EditMaskTimeInterval(long scale) {
    this(scale,null,null,null);
    }*/

    public EditMaskTimeInterval(final Scale scale,
            final String mask,
            final Long minValue,
            final Long maxValue) {
        super();
        if (scale==null || scale==Scale.NONE){
            throw new IllegalArgumentException("Scale was not defined");
        }
        this.scale = scale;
        displayFormat = mask;
        if (minValue != null) {
            this.minValue = minValue;
        }
        if (maxValue != null) {
            this.maxValue = maxValue;
        }
    }

    public EditMaskTimeInterval(final Scale scale, final String mask) {
        this(scale, mask, null, null);
    }

    public EditMaskTimeInterval(final Scale scale) {
        this(scale, null, null, null);
    }

    private static Scale getScaleByValue(final long value) {
        if (Scale.HOUR.longValue() == value) {
            return Scale.HOUR;
        } else if (Scale.MINUTE.longValue() == value) {
            return Scale.MINUTE;
        } else if (Scale.SECOND.longValue() == value) {
            return Scale.SECOND;
        } else if (Scale.MILLIS.longValue() == value || value <= 0) {
            return Scale.MILLIS;
        } else {
            throw new IllegalArgumentException("Unsupported scale value: " + value);
        }
    }

    public EditMaskTimeInterval(final EditMaskTimeInterval source) {
        super();
        this.scale = source.scale;
        displayFormat = source.displayFormat;
        this.minValue = source.minValue;
        this.maxValue = source.maxValue;
    }

    public EditMaskTimeInterval(final org.radixware.schemas.editmask.EditMaskTimeInterval editMask) {
        super();
        if (editMask.isSetMinValue()) {
            minValue = editMask.getMinValue();
        }
        if (editMask.isSetMaxValue()) {
            maxValue = editMask.getMaxValue();
        }
        if (editMask.getScale() == org.radixware.schemas.editmask.EditMaskTimeInterval.Scale.MINUTE) {
            scale = Scale.MINUTE;
        } else if (editMask.getScale() == org.radixware.schemas.editmask.EditMaskTimeInterval.Scale.HOUR) {
            scale = Scale.HOUR;
        } else if (editMask.getScale() == org.radixware.schemas.editmask.EditMaskTimeInterval.Scale.SECOND) {
            scale = Scale.SECOND;
        } else {
            scale = Scale.MILLIS;
        }
        if (editMask.isSetMask()) {
            displayFormat = editMask.getMask();
        }
    }

    @Override
    public void writeToXml(final org.radixware.schemas.editmask.EditMask editMask) {
        final org.radixware.schemas.editmask.EditMaskTimeInterval editMaskTimeInterval =
                editMask.addNewTimeInterval();
        if (maxValue != null) {
            editMaskTimeInterval.setMaxValue(maxValue);
        }
        if (minValue != null) {
            editMaskTimeInterval.setMinValue(minValue);
        }
        editMaskTimeInterval.setScale(scale.toXml());
        if (displayFormat != null) {
            editMaskTimeInterval.setMask(displayFormat);
        }
    }

    public String getDisplayFormat() {
        return displayFormat == null || displayFormat.isEmpty() ? scale.defaultMask() : displayFormat;
    }

    public void setDisplayFormat(final String displayFormat) {
        this.displayFormat = displayFormat;
        afterModify();
    }

    public long getScale() {
        return scale.longValue();
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

    @Override
    public ValidationResult validate(final IClientEnvironment environment, final Object value) {
		if (value == null)
            return ValidationResult.ACCEPTABLE;
		if (value instanceof Arr)
            return validateArray(environment,(Arr)value);
        final Long lvalue;
        if (value instanceof Long) {
			lvalue = (Long)value;
        } else {
            return ValidationResult.Factory.newInvalidResult(InvalidValueReason.Factory.createForInvalidValueType(environment));
        }
        
        if (lvalue<getMinValue()){
            final InvalidValueReason reason =
                InvalidValueReason.Factory.createForTooSmallValue(environment, toStr(environment,getMinValue()));
            return ValidationResult.Factory.newInvalidResult(reason);
        }
        if (lvalue>getMaxValue()){
            final InvalidValueReason reason =
                InvalidValueReason.Factory.createForTooBigValue(environment, toStr(environment,getMaxValue()));
            return ValidationResult.Factory.newInvalidResult(reason);
        }
        return ValidationResult.ACCEPTABLE;
    }

    @Override
    public String toStr(final IClientEnvironment environment, final Object o) {
        //if (wasInherited()) return INHERITED_VALUE;
        if (o == null) {
            return getNoValueStr(environment.getMessageProvider());
        }
        if (o instanceof Long) {
            Long value = (Long) o;
            return getFormattedString(value * scale.longValue(),false);
        }
        if (o instanceof ArrInt || o instanceof ArrDateTime) {
            return arrToStr(environment, (Arr) o);
        }

        return o.toString();
    }

    @Override
    public EEditMaskType getType() {
        return EEditMaskType.TIME_INTERVAL;
    }

    @Override
    public EnumSet<EValType> getSupportedValueTypes() {
        return SUPPORTED_VALTYPES;
    }

    private static enum Unit {

        DAY, HOUR, MINUTE, SECOND, MILLIS
    }
    private final static long[] unitScale = {24 * Scale.HOUR.longValue(),
        Scale.HOUR.longValue(),
        Scale.MINUTE.longValue(),
        Scale.SECOND.longValue(),
        Scale.MILLIS.longValue()};

    private static Unit getUnit(final char ch) {
        if (ch == 'd') {
            return Unit.DAY;
        } else if (ch == 'h') {
            return Unit.HOUR;
        } else if (ch == 'm') {
            return Unit.MINUTE;
        } else if (ch == 's') {
            return Unit.SECOND;
        } else if (ch == 'z') {
            return Unit.MILLIS;
        }
        return null;

    }

    private static class Token {

        public final Unit unit;
        public final int pos;
        public int length = 1;

        public Token(final Unit unit, final int pos) {
            this.unit = unit;
            this.pos = pos;
        }
    }
    private final static char QUOTE = '"';

    private String getFormattedString(final long millis, final boolean input) {
        final String format = getDisplayFormat();

        //Если единица присутствует в строке форматирования,
        //то соответствующий элемент массива становится равным true
        final boolean[] usedUnits = {false, false, false, false, false};
        //Для хранения позиции каждой единицы в строке форматирования
        final Queue<Token> tokens = new ArrayDeque<>();

        //Посимвольно просмотреть строку форматирования и заполнить
        //tokens и usedUnitsInSeconds, не учитывая символы внутри кавычек
        boolean wasQuote = false;

        Token last_token = null;
        Unit cur_unit = null;
        for (int i = 0; i < format.length(); ++i) {
            if (format.charAt(i) == QUOTE) {
                wasQuote = !wasQuote;
            } else if (!wasQuote) {
                final Unit unit = getUnit(format.charAt(i));
                if (unit != null) {
                    if (unit != cur_unit) {//NOPMD
                        usedUnits[unit.ordinal()] = true;
                        last_token = new Token(unit, i);
                        tokens.add(last_token);
                        cur_unit = unit;
                    } else {
                        last_token.length++;
                    }
                } else {
                    cur_unit = null;
                }
            }
        }
        
        //Вычислить значения единиц, встретившихся в строке форматирования
        final long[] unitValues = {0, 0, 0, 0, 0};
        long curValue = millis;
        for (int i = 0; i < unitValues.length; i++) {
            if (usedUnits[i] && unitScale[i] <= curValue) {
                unitValues[i] = curValue / unitScale[i];
                curValue = curValue % unitScale[i];
            }
        }
        
        //Вычислить максимально возможные значения единиц с учетом формата ввода        
        //и общее максимальное значение
        final long[] maxValues = {0, 0, 0, 0, 0};
        long maxInputValue = 0;
        for (Token token: tokens){
            int index = token.unit.ordinal();
            if (maxValues[index]==0){
                maxValues[index]=(long)Math.pow(10, token.length)-1;
                maxInputValue+=maxValues[index] * unitScale[index];
            }
        }
        
        if (millis>=maxInputValue && input){
            return getInputMask();
        }

        if (millis<=maxInputValue){
            //Скорректировать полученные значения единиц с учетом максимально возможных
            long remainderInMillis = 0;
            for (int i = 0; i < unitValues.length; i++) {
                if (usedUnits[i]) {
                    final long unitRemainder = remainderInMillis / unitScale[i];
                    if ((unitValues[i]+unitRemainder)>maxValues[i]){
                        remainderInMillis = ((unitValues[i]+unitRemainder) - maxValues[i]) * unitScale[i];
                        unitValues[i] = maxValues[i];
                    }else if (unitRemainder>0){
                        unitValues[i] += unitRemainder;
                        remainderInMillis = 0;
                    }
                }
            }

            if (remainderInMillis>0 && !input){
                for (int i = unitValues.length-1; i >= 0 ; i--) {
                    final long unitRemainder = remainderInMillis / unitScale[i];
                    if (usedUnits[i]) {
                        unitValues[i] += unitRemainder;
                    }
                }
            }
        }

        //Еще раз посимвольно просмотреть строку, вставляя вместо обозначения
        //единицы ее значение
        final StringBuffer result = new StringBuffer("");
        wasQuote = false;
        Token nextToken = tokens.poll();
        for (int i = 0; i < format.length(); ++i) {
            if (format.charAt(i) == QUOTE) {
                if (format.length() > (i + 1) && format.charAt(i + 1) == QUOTE) {
                    result.append(QUOTE);
                    i++;
                } else {
                    wasQuote = !wasQuote;
                }
            } else if (wasQuote) {
                result.append(format.charAt(i));
            } else {
                if (nextToken != null && nextToken.pos == i) {
                    String value = String.valueOf(unitValues[nextToken.unit.ordinal()]);
                    while (value.length() < nextToken.length) {
                        value = '0' + value;//NOPMD
                    }
                    result.append(value);
                    i += nextToken.length - 1;
                    nextToken = tokens.poll();
                } else {
                    result.append(format.charAt(i));
                }
            }
        }

        return result.toString();
    }

    /**
     * На основе displayFormat генерируется маска ввода для QLineEdit
     * @return маска ввода для QLineEdit
     */
    public String getInputMask() {
        final StringBuilder inputMaskBuilder = new StringBuilder();
        boolean wasQuote = false;
        final String format = getDisplayFormat();
        for (int i = 0; i < format.length(); ++i) {
            if (format.charAt(i) == QUOTE) {
                if (format.length() > (i + 1) && format.charAt(i + 1) == QUOTE) {
                    inputMaskBuilder.append(QUOTE);                    
                    i++;
                } else {
                    wasQuote = !wasQuote;
                }
            } else {
                final char ch = format.charAt(i);
                //Маскируем служебные символы маски ввода
                if (Character.toLowerCase(ch) == 'a'
                        || Character.toLowerCase(ch) == 'n'
                        || Character.toLowerCase(ch) == 'x'
                        || Character.toLowerCase(ch) == 'b'
                        || ch == '9' || ch == '0' || ch == '#'
                        || ch == '<' || ch == '>' || ch == '!'
                        || ch == '\\' || ch == 'H' || ch == 'D'
                        || (ch == 'h' && wasQuote)
                        || (ch == 'd' && wasQuote)) {
                    inputMaskBuilder.append('\\');
                    inputMaskBuilder.append(ch);                    
                } else if (wasQuote) {
                    inputMaskBuilder.append(ch);
                } else if (getUnit(ch) != null) {
                    inputMaskBuilder.append('9');
                } else {
                    inputMaskBuilder.append(ch);
                }
            }
        }

        return inputMaskBuilder.toString();
    }
    
    public String getInputTextForValue(final Long value) {
        if (isSpecialValue(value)){
            return "";
        }
        return getFormattedString(value * scale.longValue(),true);
    }

    public Long convertFromStringToLong(final String s) {
        final long[] values = {0, 0, 0, 0, 0};

        boolean wasQuote = false;        
        Unit curUnit = null;
        final StringBuffer buffer = new StringBuffer();
        final String format = getDisplayFormat();
        for (int i = 0, j=0; i < format.length() && j<s.length(); ++i) {
            if (format.charAt(i) == QUOTE) {
                if (format.length() > (i + 1) && format.charAt(i + 1) == QUOTE) {
                    j++;
                    i++;
                } else {
                    wasQuote = !wasQuote;
                }
            } else if (wasQuote) {
                j++;
            } else {
                final Unit unit = getUnit(format.charAt(i));
                if (unit != null) {
                    if (unit != curUnit) {//NOPMD
                        buffer.setLength(0);
                        while (s.length() > j && Character.isDigit(s.charAt(j))) {
                            buffer.append(s.charAt(j));
                            j++;
                        }
                        final String value = buffer.toString();
                        if (!value.isEmpty()) {
                            final int index = unit.ordinal();
                            values[index] = Long.valueOf(value) * unitScale[index];
                        }
                    }
                } else {
                    j++;
                }
                curUnit = unit;
            }
        }

        Long result = Long.valueOf(0);
        for (int i = 0; i < values.length; i++) {
            if (values[i] >= scale.longValue()) {
                result += values[i] / scale.longValue();
            }
        }
        return result;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + Objects.hashCode(this.scale);
        hash = 11 * hash + Objects.hashCode(this.minValue);
        hash = 11 * hash + Objects.hashCode(this.maxValue);
        hash = 11 * hash + Objects.hashCode(this.displayFormat);
        hash = 11 * hash + super.hashCode();
        return hash;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this){
            return true;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EditMaskTimeInterval other = (EditMaskTimeInterval) obj;
        if (!Objects.equals(this.scale, other.scale)) {
            return false;
        }
        if (!Objects.equals(this.minValue, other.minValue)) {
            return false;
        }
        if (!Objects.equals(this.maxValue, other.maxValue)) {
            return false;
        }
        if (!Objects.equals(this.displayFormat, other.displayFormat)) {
            return false;
        }
        return super.equals(obj);
    }        
}
