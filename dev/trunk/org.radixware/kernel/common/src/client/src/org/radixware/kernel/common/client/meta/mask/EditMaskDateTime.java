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

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Queue;
import java.util.logging.Logger;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.exceptions.DateTimeFieldNotDefinedException;
import org.radixware.kernel.common.client.exceptions.DateTimeFieldOutOfBoundsException;
import org.radixware.kernel.common.client.exceptions.WrongAmPmFieldValue;
import org.radixware.kernel.common.client.meta.mask.validators.InvalidValueReason;
import org.radixware.kernel.common.client.meta.mask.validators.ValidationResult;
import org.radixware.kernel.common.client.utils.ValueConverter;
import org.radixware.kernel.common.enums.EDateTimeStyle;
import org.radixware.kernel.common.enums.EEditMaskType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.WrongFormatException;
import org.radixware.kernel.common.types.Arr;
import org.radixware.kernel.common.types.ArrDateTime;


public final class EditMaskDateTime extends org.radixware.kernel.common.client.meta.mask.EditMask {
    
    private static enum Unit {
        YEAR(Calendar.YEAR, "9999"),
        MONTH(Calendar.MONTH, "99"),
        DAY(Calendar.DATE, "99"),
        HOUR(Calendar.HOUR_OF_DAY, "99"),
        MINUTE(Calendar.MINUTE, "99"),
        SECOND(Calendar.SECOND, "99"),
        MILSEC(Calendar.MILLISECOND, "999"),
        AMPM(Calendar.AM_PM, "AM"),
        DELIM(-1, ""),
        FILLER(-1, Character.toString('_')),
        LITERAL(-1, ""),
        QUOTE(-1, "");
        
        private final int javaCalendarScale;
        private final String inputMaskSymbol;
                
        Unit(final int javaCalendarScale, final String inputMaskSymbol) {
            this.javaCalendarScale = javaCalendarScale;
            this.inputMaskSymbol = inputMaskSymbol;
        }
        
        public int getCalendarScale() {
            return javaCalendarScale;
        }
        
        public String withYearLength(final int yearMaskLength) {
            if(this == YEAR) {
                return yearMaskLength == 2 ? "99" : "9999";
            }
            return getSymbol();
        }
        
        public String getSymbol() {
            return inputMaskSymbol;
        }
        
        public static Unit getUnit(final char input) {
            switch(input) {
                case 'y': 
                    return YEAR;
                case 'M': 
                    return MONTH;
                case 'd': 
                case 'E': 
                    return DAY;
                case 'H': 
                case 'h': 
                    return HOUR;
                case 'm': 
                    return MINUTE;
                case 's': 
                    return SECOND;
                case 'S': 
                    return MILSEC;
                case 'a':
                case 'A': 
                case 'p':
                case 'P': 
                    return AMPM;
                case '.':
                case ':':
                case '-':
                case '/':
                case ',':
                case ' ': 
                    return DELIM;
                case '_': 
                    return FILLER;
                case '\'': 
                    return QUOTE;
                default: 
                    return LITERAL;
            }
        }
        
        public static long[] getUnitsValues(final Calendar date) {
            final long[] unitValues = new long[Unit.values().length];
            for(int i = 0; i < unitValues.length; i++) {
                final int scale = Unit.values()[i].getCalendarScale();
                if(scale == -1) continue;
                unitValues[i] = date.get(scale);
                if(scale == Calendar.MONTH) {
                     unitValues[i] += 1;
                }
            }
            return unitValues;
        }
        
        public static int getMaximumValue(final Unit unit, final Timestamp time) {
            final Calendar date = Calendar.getInstance();
            date.setTimeInMillis(time.getTime());
            return date.getMaximum(unit.getCalendarScale());
        }
    }
    
    private static class Token {

        public final Unit unit;
        public final String literal;
        
        public Token(final Unit unit, final String literal) {
            this.unit = unit;
            this.literal = literal;
        }
    }
    
    private static class Scanner {
        private int position;
        private final String input;
        
        public Scanner(final String input) {
            this.position = 0;
            this.input = input;
        }
        
        public int getPosition() {
            return position;
        }
        
        public char getCurrentChar() {
            return isEof() ? '\0' : input.charAt(position);
        }
        
        public boolean isDigit() {
            return Character.isDigit(getCurrentChar());
        }
        
        public boolean isFiller() {
            return String.valueOf(getCurrentChar()).equals(Unit.FILLER.getSymbol());
        }
        
        public boolean isEof() {
            return getPosition() == input.length();
        }
        
        public boolean isLiteral() {
            return !isDigit() && !isEof() && !isFiller(); 
        }
        
        public void moveNext() {
            if(!isEof()) {
                position++;
            }
        }
        
        public void movePrev() {
            position--;
        }
    }
    
    private static class DateTimeFormatParser{
        
        private final static DateTimeFormatParser INSTANCE = new DateTimeFormatParser();
        private final Map<String, Queue<Token>> tokensCache = new HashMap<>();
        private final Object semaphore = new Object();
        
        private DateTimeFormatParser(){
            
        }
        
        public static DateTimeFormatParser getInstance(){
            return INSTANCE;
        }
        
        private Queue<Token> getTokens(final String displayFormat){
            synchronized(semaphore){
                Queue<Token> tokens;

                tokens = tokensCache.get(displayFormat);
                if(tokens != null){
                    return tokens;
                }

                tokens = new ArrayDeque<>();
                String curToken = "";
                Unit lastUnit = null;
                boolean quoted = false;
                char curChar;

                for(int i = 0; i < displayFormat.length(); i++) {
                    curChar = displayFormat.charAt(i);
                    final Unit curUnit = Unit.getUnit(curChar);
                    if (curUnit==Unit.QUOTE){
                        if (quoted){
                            if (curToken.length()>0){//case when some text between two quotes
                                tokens.add(new Token(Unit.LITERAL, curToken));
                            }
                            lastUnit = null;
                        }else{
                            if (lastUnit!=null){
                                tokens.add(new Token(lastUnit, curToken));
                            }
                            lastUnit = Unit.LITERAL;                    
                        }
                        curToken = "";
                        quoted = !quoted;
                    }            
                    else if (!quoted && curUnit != lastUnit){//NOPMD
                        if (lastUnit!=null){
                            tokens.add(new Token(lastUnit, curToken));
                            curToken = String.valueOf(curChar);
                        }else{
                            curToken += curChar;//NOPMD
                        }
                        lastUnit = curUnit;                
                    }else{
                        curToken += curChar;//NOPMD
                    }
                }
                if (lastUnit!=null){
                    tokens.add(new Token(lastUnit, curToken));
                }        
                tokensCache.put(displayFormat, tokens);
                return tokens;
            }
        }        
        
    }
        
    final private static char QUOTE = '"';
    
    private Timestamp minimumTime = null;
    private Timestamp maximumTime = null;
    private EDateTimeStyle dateStyle = EDateTimeStyle.DEFAULT;
    private EDateTimeStyle timeStyle = EDateTimeStyle.DEFAULT;    
    
    private static final EnumSet<EValType> SUPPORTED_VALTYPES =
            EnumSet.of(EValType.DATE_TIME, EValType.ARR_DATE_TIME);
    private final Map<Locale,String> displayFormatCache = new HashMap<>();
    
    public EditMaskDateTime(final EDateTimeStyle dateStyle,
                            final EDateTimeStyle timeStyle,
                            final Timestamp minValue,
                            final Timestamp maxValue) {
        super();
        this.dateStyle = dateStyle;
        this.timeStyle = timeStyle;
        if (minValue != null) {
            setMinimumTime(minValue);
        }
        if (maxValue != null) {
            this.setMaximumTime(maxValue);
        }
    }    

    public EditMaskDateTime(final String mask,//маска в формате qt
            final Timestamp minValue,
            final Timestamp maxValue) {
        super();        
        if (mask != null && !mask.isEmpty()) {
            setCustomPattern(mask);
        }
        if (minValue != null) {
            setMinimumTime(minValue);
        }
        if (maxValue != null) {
            this.setMaximumTime(maxValue);
        }
    }

    public EditMaskDateTime() {
        super();
    }

    public EditMaskDateTime(final EditMaskDateTime source) {
        super();
        minimumTime = copyTime(source.minimumTime);
        maximumTime = copyTime(source.maximumTime);
        displayFormat = source.displayFormat;
        dateStyle = source.dateStyle;
        timeStyle = source.timeStyle;        
        displayFormatCache.putAll(source.displayFormatCache);
    }

    protected EditMaskDateTime(final org.radixware.schemas.editmask.EditMaskDateTime editMask) {
        super();
        if (editMask.getMask() != null && !editMask.getMask().isEmpty()) {
            displayFormat = editMask.getMask();
        }
        if (editMask.isSetMinValue()) {
            this.setMinimumTime(new Timestamp(editMask.getMinValue().getTimeInMillis()));

        }
        if (editMask.isSetMaxValue()) {
            this.setMaximumTime(new Timestamp(editMask.getMaxValue().getTimeInMillis()));
        }
        dateStyle = editMask.getDateStyle();
        timeStyle = editMask.getTimeStyle();
    }

    @Override
    public void writeToXml(final org.radixware.schemas.editmask.EditMask editMask) {
        final org.radixware.schemas.editmask.EditMaskDateTime editMaskDateTime = editMask.addNewDateTime();
        if (maximumTime != null) {
            editMaskDateTime.setMaxValue(ValueConverter.timestamp2Calendar(maximumTime));
        }
        if (minimumTime != null) {
            editMaskDateTime.setMinValue(ValueConverter.timestamp2Calendar(minimumTime));
        }
        if (!isCustomPatternEmpty()) {
            editMaskDateTime.setMask(displayFormat);
        }
        editMaskDateTime.setDateStyle(dateStyle);
        editMaskDateTime.setTimeStyle(timeStyle);
    }

    public Timestamp getMinimumTime() {
        return copyTime(minimumTime);
    }

    public void setMinimumTime(final Timestamp minimumTime) {
        this.minimumTime = copyTime(minimumTime);
        afterModify();
    }

    public Timestamp getMaximumTime() {
        return copyTime(maximumTime);
    }

    public void setMaximumTime(final Timestamp maximumTime) {
        this.maximumTime = copyTime(maximumTime);
        afterModify();
    }

    public String getDisplayFormat(final java.util.Locale locale) {
        String actualDisplayFormat = displayFormatCache.get(locale);
        if (actualDisplayFormat==null){
            final String javaFormat;
            if(isCustomPatternEmpty()) {
                DateFormat format;
                if(timeStyle == EDateTimeStyle.NONE && dateStyle != EDateTimeStyle.NONE) {
                    format = DateFormat.getDateInstance(dateStyle.getJavaDateTimeStyle(), locale);
                } else if(timeStyle != EDateTimeStyle.NONE &&  dateStyle == EDateTimeStyle.NONE) {
                    format = DateFormat.getTimeInstance(timeStyle.getJavaDateTimeStyle(), locale);
                } else {
                    format = DateFormat.getDateTimeInstance(dateStyle.getJavaDateTimeStyle(),
                            timeStyle.getJavaDateTimeStyle(),
                            locale);
                }
                javaFormat = ((java.text.SimpleDateFormat)format).toPattern();                        
            } else {
                javaFormat = qtFormat2JavaFormat(locale, displayFormat);
            }
            actualDisplayFormat = extendJavaFormatToInputFormat(javaFormat);
            displayFormatCache.put(locale, actualDisplayFormat);
        }
        return actualDisplayFormat;
    }
    
    private String extendJavaFormatToInputFormat(final String displayFormat){
        final Queue<Token> currentTokens = DateTimeFormatParser.getInstance().getTokens(displayFormat);
        final StringBuilder formatBuilder = new StringBuilder();
        for (Token token: currentTokens){            
            switch(token.unit){
                case MONTH:
                case DAY:
                case HOUR:
                case MINUTE:
                case SECOND:
                case MILSEC:{
                    final int inputFormatLength = token.unit.getSymbol().length();
                    if (token.literal.length()<inputFormatLength){
                        final char smb = token.literal.charAt(0);
                        for (int i=1; i<=inputFormatLength; i++){
                            formatBuilder.append(smb);
                        }
                    }else{
                        formatBuilder.append(token.literal);
                    }
                }
                break;
                case LITERAL:{
                    formatBuilder.append('\'');
                    formatBuilder.append(token.literal);
                    formatBuilder.append('\'');
                }
                break;
                case QUOTE:{
                    throw new IllegalStateException("Unexpected token type");
                }
                default:
                    formatBuilder.append(token.literal);
            }
        }
        return formatBuilder.toString();
    }
    
    public EDateTimeStyle getDateStyle(){
        return dateStyle;
    }
    
    public void setDateStyle(final EDateTimeStyle style){
        clearCaches();
        dateStyle = style;
        afterModify();
    }
    
    public EDateTimeStyle getTimeStyle(){
        return timeStyle;
    }
    
    public void setTimeStyle(final EDateTimeStyle style){
        clearCaches();
        timeStyle = style;
        afterModify();
    }

    private String buildNumber(final Scanner scanner, final int maxNumberLength) {
        StringBuilder result = new StringBuilder();
        while(!scanner.isEof()) {
            if(scanner.isDigit()) {
                result.append(scanner.getCurrentChar());
                if(result.length() == maxNumberLength) {
                    break;
                }
                scanner.moveNext();
            } else if(scanner.isFiller()) {
                scanner.moveNext();
            } else {
                scanner.movePrev();
                break;
            }
        }
        return result.toString();
    }
    
    private String buildAmPm(final Scanner scanner) {
        StringBuilder result = new StringBuilder();        
        while(!scanner.isEof()) {
            if (scanner.isLiteral()) {
                result.append(scanner.getCurrentChar());
                scanner.moveNext();
                if (scanner.isEof()){
                    return result.toString();
                }
                if (scanner.isFiller()){
                    return result.toString();
                }
                if (!scanner.isLiteral()){
                    scanner.movePrev();
                }else{
                    result.append(scanner.getCurrentChar());                    
                }             
                return result.toString();
            } else if(scanner.isFiller()) {
                scanner.moveNext();
            } else {
                scanner.movePrev();
                break;
            }
        }
        return result.toString();
    }
    
    public String getInputMask(final Locale locale) {
        final StringBuilder sb = new StringBuilder();
        final String format = getDisplayFormat(locale).replaceAll("[zZ]?", ""); // remove time zone designator in input mask
        final Queue<Token> tokens = DateTimeFormatParser.getInstance().getTokens(format);
        for(Token currentToken : tokens) {
            switch(currentToken.unit) {
                case DELIM:
                    sb.append(currentToken.literal);
                    break;
                case YEAR:
                    sb.append(currentToken.unit.withYearLength(currentToken.literal.length()));
                    break;
                case LITERAL:
                    sb.append(currentToken.literal.replaceAll("(\\.)", "\\\\1"));
                    break;
                default:
                    sb.append(currentToken.unit.getSymbol());
                    break;
            }
        }
        sb.append(';');
        sb.append(Unit.FILLER.getSymbol());
        return sb.toString();
    }
    
    private String getInputTextForNullValue(final Locale locale){
        final StringBuilder sb = new StringBuilder();
        final String format = getDisplayFormat(locale).replaceAll("[zZ]?", ""); // remove time zone designator in input mask
        final Queue<Token> tokens = DateTimeFormatParser.getInstance().getTokens(format);
        for(Token currentToken : tokens) {            
            switch(currentToken.unit) {
                case QUOTE:                
                    break;
                case DELIM:
                case LITERAL:
                    sb.append(currentToken.literal);
                    break;
                case YEAR:{
                    int length = currentToken.literal.length();
                    for (int i=1; i<=length; i++){
                        sb.append(Unit.FILLER.getSymbol());
                    }
                    break;
                }
                case AMPM:
                    sb.append("AM");
                    break;
                default:
                    for (int i=1,length=currentToken.unit.getSymbol().length(); i<=length; i++){
                        sb.append(Unit.FILLER.getSymbol());
                    }                    
            }            
        }
        return sb.toString();
    }
    
    // The method is needed to perform possibly alphabetical values to numerical ones
    public String getInputTextForValue(final Timestamp value, final Locale locale) {
        if (isSpecialValue(value)){
            return getInputTextForNullValue(locale);
        }
        final String format = getDisplayFormat(locale);
        final Queue<Token> tokens = DateTimeFormatParser.getInstance().getTokens(format);
        final StringBuffer result = new StringBuffer();        
        final Calendar date = Calendar.getInstance();
        date.setTimeInMillis(value.getTime());
        //final int 
        final long[] unitValues = Unit.getUnitsValues(date);                
        
        for(Token nextToken : tokens) {
            String strValue;
            final long longValue = unitValues[nextToken.unit.ordinal()];
            switch(nextToken.unit) {
                case AMPM: 
                    strValue = (longValue == 0) ? "AM" : "PM";
                    break;
                case YEAR:
                    strValue = nextToken.literal.length() == 2
                            ? String.valueOf(longValue % 100)
                            : String.valueOf(longValue);
                    while (strValue.length() < nextToken.literal.length()) {
                        strValue = '0' + strValue;//NOPMD
                    }
                    break;
                case HOUR:                    
                    if (nextToken.literal.charAt(0)=='h') {
                        final long hrsDividedBy12 = longValue % 12;
                        strValue = String.valueOf(hrsDividedBy12 == 0 ? 12 : hrsDividedBy12);
                    } else {
                        strValue = String.valueOf(longValue);
                    }
                    while (strValue.length() < nextToken.unit.getSymbol().length()) {
                            strValue = '0' + strValue;//NOPMD
                        }
                    break;
                case DELIM: 
                    strValue = String.valueOf(nextToken.literal);
                    break;
                case LITERAL:
                    strValue = nextToken.literal;
                    break;
                default:
                    strValue = String.valueOf(longValue); 
                    while (strValue.length() < nextToken.unit.getSymbol().length()) {
                        strValue = '0' + strValue;//NOPMD
                    }
                    break;
            }
            result.append(strValue);
        }
        
        return result.toString(); 
    }
    
    public boolean isEmptyInput(final String inputText, final Locale locale){
        return inputText==null 
               || inputText.isEmpty() 
               || inputText.equals(getInputTextForNullValue(locale));
    }
    
    @SuppressWarnings({"fallthrough", "PMD.MissingBreakInSwitch"})
    public Timestamp getValueForInputText(final String inputText, final Locale locale) throws WrongFormatException {
        if (isEmptyInput(inputText, locale)){
            return null;
        }
        final String format = getDisplayFormat(locale);
        final Calendar date = Calendar.getInstance();
        date.setLenient(false);
        date.clear();        
        final Queue<Token> tokens = DateTimeFormatParser.getInstance().getTokens(format);
        final EnumMap<Unit, Integer> parsedValues = new EnumMap<>(Unit.class);
        boolean isYearTwoDigit = false;
        boolean isPm = false;
        long millis = 0;
        
        
        if(minimumTime == null) {
            date.set(1,0,1);
        } else {
            date.setTimeInMillis(minimumTime.getTime());
        }
        
        try {            
            String ampm = null;
            boolean isTwelveHourFormat = false;
            final EnumMap<Unit, String> valuesMap = new EnumMap<>(Unit.class);
            final Scanner scanner = new Scanner(inputText);
            for(Token t : tokens) {
                String curValue;
                switch(t.unit) {
                    case YEAR: 
                        isYearTwoDigit = (t.literal.length() == 2);
                    case MONTH:
                    case DAY:
                    case HOUR:
                        isTwelveHourFormat = (t.literal.charAt(0)=='h');
                    case MINUTE: 
                    case SECOND:
                    case MILSEC:
                        curValue = buildNumber(scanner, t.unit.getSymbol().length());
                        scanner.moveNext();
                        valuesMap.put(t.unit, curValue);
                        break;
                    case AMPM:
                        ampm = buildAmPm(scanner);
                        scanner.moveNext();
                        valuesMap.put(t.unit, ampm);
                        break;
                    default:
                        for (int i=0; i<t.literal.length(); i++){
                            if (t.literal.charAt(i)==scanner.getCurrentChar()){
                                scanner.moveNext();
                            }
                        }
                        break;
                }
            }
                        
            if (valuesMap.isEmpty()){
                return null;//empty input
            }
            
            if (ampm!=null 
                && !ampm.isEmpty()
                && !ampm.equalsIgnoreCase("a") 
                && !ampm.equalsIgnoreCase("p")
                && !ampm.equalsIgnoreCase("am")
                && !ampm.equalsIgnoreCase("pm")
               ){            
                throw new WrongAmPmFieldValue(ampm);
            }
            checkInput(valuesMap, tokens);
                        
            //evaluating the final date            
            for(Entry<Unit, String> entry : valuesMap.entrySet()) {
                final String currentStringValue = entry.getValue();
                if (currentStringValue == null){
                    continue;
                }
                Integer currentValue;
                
                switch(entry.getKey()) {
                    case MONTH:
                        currentValue = Integer.valueOf(currentStringValue);
                        currentValue--;
                        /*
                        if (currentValue==0){
                            continue;//ignoring 0 value
                        }
                        else{
                            currentValue--;
                        }*/
                        break;
                    case HOUR:
                        currentValue = Integer.valueOf(currentStringValue);
                        if (isTwelveHourFormat && (currentValue<1 || currentValue>12)){
                            throw new DateTimeFieldOutOfBoundsException(Calendar.HOUR_OF_DAY, 1, 12, currentValue, null);
                        }
                        if(ampm!=null) {
                            isPm = ampm.equalsIgnoreCase("p") || ampm.equalsIgnoreCase("pm");
                            if (isPm && currentValue<12){
                                currentValue+=12;
                            }else if (!isPm && currentValue==12){
                                currentValue = 0;
                            }                            
                        }
                        break;                    
                    case AMPM:
                        continue;                      
                    case YEAR:
                        currentValue = Integer.valueOf(currentStringValue);                        
                        if (isYearTwoDigit) currentValue += 2000;
                        break;
                        
                    default:
                        currentValue = Integer.valueOf(currentStringValue);
                        break;
                    
                }
                if (currentValue!=0 || entry.getKey()==Unit.MONTH){//TWRBS-2693
                    parsedValues.put(entry.getKey(), currentValue);
                    date.set(entry.getKey().getCalendarScale(), currentValue);
                }
            }
            
            millis = date.getTimeInMillis();
        } catch(NumberFormatException nfe) {
            throw new WrongFormatException(nfe.getMessage(), nfe);
        } catch(IllegalArgumentException iae) {
            checkBounds(parsedValues,tokens, date, isYearTwoDigit, isPm, iae);
            throw new WrongFormatException("Wrong unit value", iae);
        }
                
        return new Timestamp(millis);
    }
    
    //Check if some field was not defined
    private static void checkInput(final EnumMap<Unit, String> valuesMap, final Queue<Token> tokens) throws WrongFormatException {
        for (Token token: tokens){
            if (token.unit!=null && token.unit.getCalendarScale()>=0){
                final String input = valuesMap.get(token.unit);                
                if (input==null || input.isEmpty()){
                    throw new DateTimeFieldNotDefinedException(token.unit.getCalendarScale());
                }
            }
        }
    }
    
    private static void checkBounds(final EnumMap<Unit, Integer> values, 
                                    final Queue<Token> tokens, 
                                    final Calendar invalidDate, 
                                    final boolean isYearTwoDigit,
                                    final boolean isPm,
                                    final Throwable cause) throws DateTimeFieldOutOfBoundsException{
        final Calendar date = (Calendar)invalidDate.clone();
        date.set(Calendar.DAY_OF_MONTH, 1);
        for (Token token: tokens){
            final int calendarField = token.unit==null ? -1 : token.unit.getCalendarScale();
            if (calendarField>-1){
                Integer value = values.get(token.unit);
                if (value!=null){
                    int minVal = date.getActualMinimum(calendarField);
                    int maxVal = date.getActualMaximum(calendarField);
                    
                    if (value<minVal || value>maxVal){
                        if (calendarField==Calendar.MONTH){
                            minVal++;
                            maxVal++;
                            value++;
                        }else if (calendarField==Calendar.HOUR_OF_DAY && isPm){
                            maxVal = 11;
                        }else if (calendarField==Calendar.YEAR){
                            minVal = 0;
                            if (isYearTwoDigit){
                                maxVal -= 2000;
                                value -= 2000;
                            }
                        }   
                        throw new DateTimeFieldOutOfBoundsException(calendarField, minVal, maxVal, value, cause);
                    }                    
                }
            }
        }
    }
    
    static String qtFormat2JavaFormat(final Locale locale, final String qtFormat) {
                
        final boolean isAmPmMarkPresent = qtFormat.toLowerCase().contains("ap");
        
        String result = isAmPmMarkPresent ? qtFormat : qtFormat.replace('h', 'H');
        
        if (result.contains("MMMM")) {//full month name
            result = result.replace("MMMM", computeFullMonthMask(locale));
        }
        if (result.contains("dddd")){//full week day name
            result = result.replace("dddd", computeFullWeekDayMask(locale));
        }
        if (result.contains("ddd")){//short week day name
            result = result.replace("ddd", "EEE");
        }
        if (result.contains("z")){//milliseconds
            result = result.replace("z", "S");
        }
        
        result = result.replaceAll("(?i)(?u)ap?", "a"); // ("a" | "ap" | "A" | "AP") -> "a"
        
        return result;
    }

    static String computeFullMonthMask(final Locale locale) {
        int maxLen = -1;
        for (String month : DateFormatSymbols.getInstance(locale).getMonths()) {
            if (month.length() > maxLen) {
                maxLen = month.length();
            }
        }

        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < maxLen; i++) {
            sb.append('M');
        }
        return sb.toString();
    }

    static String computeFullWeekDayMask(final Locale locale) {
        int maxLen = -1;
        for (String weekDay : DateFormatSymbols.getInstance(locale).getWeekdays()) {
            if (weekDay.length() > maxLen) {
                maxLen = weekDay.length();
            }
        }

        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < maxLen; i++) {
            sb.append('E');
        }
        return sb.toString();
    }

    @Override
    public String toStr(final IClientEnvironment environment, final Object value) {
        //if (wasInherited()) return INHERITED_VALUE;
        if (value == null) {
            return getNoValueStr(environment.getMessageProvider());
        }
        if (value instanceof Timestamp) {
            //final QDateTime dateTime = ValueConverter.timestamp2QtTime((Timestamp) value);
            final Locale loc = environment == null ? Locale.getDefault() : environment.getLocale();
            final String pattern = getDisplayFormat(loc);
            try {
                if (environment==null){
                    return new SimpleDateFormat(pattern, loc).format(value);
                }else{
                    return environment.getServerTimeZoneInfo().formatDateTime(pattern, loc, (Timestamp)value);
                }
            } catch (Throwable e) {
                if (environment==null){
                    final String message = "Unable to format date/time value with \'%1s\' pattern";
                    Logger.getLogger(EditMaskDateTime.class.getName()).severe(message);
                }else{
                    final String message = environment.getMessageProvider().translate("ExplorerException", "Unable to format date/time value with \'%1s\' pattern");
                    environment.getTracer().error(String.format(message, String.valueOf(pattern)),e);
                }                
                return DateFormat.getInstance().format(value);
            }
        } else if (value instanceof ArrDateTime) {
            return arrToStr(environment, (ArrDateTime) value);
        }
        return value.toString();
    }

    @Override
    public ValidationResult validate(final IClientEnvironment environment, final Object value) {
        if (value == null)
            return ValidationResult.ACCEPTABLE;
        if (value instanceof Arr)
            return validateArray(environment,(Arr)value);
        if (!(value instanceof Timestamp))
            return ValidationResult.Factory.newInvalidResult(InvalidValueReason.Factory.createForInvalidValueType(environment));
        final Timestamp val = (Timestamp)value;
        final Timestamp min = getMinimumTime(), max = getMaximumTime();
        if (min != null && val.before(min)) {
            final InvalidValueReason reason;
            final String minValAsStr = toStr(environment, min);
            if (max==null){
                reason = InvalidValueReason.Factory.createForTooSmallValue(environment, minValAsStr);
            }else{
                reason = 
                    InvalidValueReason.Factory.createForOutOfRange(environment, minValAsStr, toStr(environment, max));
            }             
            return ValidationResult.Factory.newInvalidResult(reason);
        }
        if (max != null && val.after(max)) {
            final InvalidValueReason reason;
            final String maxValAsStr = toStr(environment, max);
            if (min==null){
                reason = InvalidValueReason.Factory.createForTooSmallValue(environment, maxValAsStr);
            }else{
                reason = 
                    InvalidValueReason.Factory.createForOutOfRange(environment, toStr(environment, min), maxValAsStr);
            }            
            return ValidationResult.Factory.newInvalidResult(reason);
        }
        return ValidationResult.ACCEPTABLE;
    }  

    public boolean timeFieldPresent(final Locale locale) {
        return checkForFields(Arrays.asList('h', 'H', 'm', 's', 'z'), locale);
    }

    public boolean dateFieldPresent(final Locale locale) {
        return checkForFields(Arrays.asList('d', 'M', 'y'), locale);
    }
    
    public boolean halfDayFieldPresent(final Locale locale){
        return checkForFields(Arrays.asList('a'), locale);
    }

    public Timestamp copyFields(final Timestamp source, final Locale locale) {
        if (source == null) {
            return null;
        }
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(source.getTime());

        final int day = checkForFields(Arrays.asList('d', 'E'), locale) ? cal.get(Calendar.DAY_OF_MONTH) : 1;
        final int month = checkForFields(Collections.singletonList('M'), locale) ? cal.get(Calendar.MONTH) : 0;
        final int year = checkForFields(Collections.singletonList('y'), locale) ? cal.get(Calendar.YEAR) : 1970;

        final int hour = checkForFields(Arrays.asList('h', 'H'), locale) ? cal.get(Calendar.HOUR_OF_DAY) : 0;
        final int minute = checkForFields(Collections.singletonList('m'), locale) ? cal.get(Calendar.MINUTE) : 0;
        final int second = checkForFields(Collections.singletonList('s'), locale) ? cal.get(Calendar.SECOND) : 0;
        final int msec = checkForFields(Collections.singletonList('S'), locale) ? cal.get(Calendar.MILLISECOND) : 0;

        cal.set(year, month, day, hour, minute, second);
        cal.set(Calendar.MILLISECOND, msec);
        return new Timestamp(cal.getTimeInMillis());
    }
    
    private static boolean checkForFields(final String format, final List<Character> fieldsForCheck) {
        boolean wasQuote = false;        
        for (int i = 0; i < format.length(); ++i) {
            if (format.charAt(i) == QUOTE) {
                if (format.length() > (i + 1) && format.charAt(i + 1) == QUOTE) {
                    i++;
                } else {
                    wasQuote = !wasQuote;
                }
            } else if (!wasQuote && fieldsForCheck.contains(format.charAt(i))) {
                return true;
            }
        }
        return false;
    }    

    private boolean checkForFields(final List<Character> fieldsForCheck, Locale locale) {
        return checkForFields(getDisplayFormat(locale), fieldsForCheck);
    }

    public String getInputTimeFormat(final Locale locale) {
        final StringBuilder result;
        if (checkForFields(Arrays.asList('H'), locale)) {
            result = new StringBuilder("HH");
        } else if (checkForFields(Arrays.asList('h'), locale)) {
            result = new StringBuilder("hh");
        }else{
            result = new StringBuilder();
        }
        if (checkForFields(Arrays.asList('m'), locale)) {
            if (result.length()>0){
                result.append(':');
            }
            result.append("mm");
        }
        if (checkForFields(Arrays.asList('s'), locale)) {
            if (result.length()>0){
                result.append(':');
            }
            result.append("ss");
        }
        if (checkForFields(Arrays.asList('S'), locale)) {
            if (result.length()>0){
                result.append(':');
            }
            result.append("zzz");          
        }
        if (halfDayFieldPresent(locale)) {
            if (result.length()>0){
                result.append(' ');
            }
            result.append("ap");
        }
        return result.toString();
    }

    private static Timestamp copyTime(final Timestamp time) {
        if (time == null) {
            return null;
        }
        return new Timestamp(time.getTime());
    }

    @Override
    public EEditMaskType getType() {
        return EEditMaskType.DATE_TIME;
    }

    @Override
    public EnumSet<EValType> getSupportedValueTypes() {
        return SUPPORTED_VALTYPES;
    }

    /**
     * Возвращает паттерн даты/времени в Qt-стиле
     * @return 
     */
    public String getCustomPattern() {
        return displayFormat;
    }
    
    /**
     * Устанавливает паттерн Qt-стиля для даты/времени
     * @param customPattern 
     */
    public void setCustomPattern(final String customPattern) {
        clearCaches();
        if(customPattern == null || customPattern.isEmpty()) {
            this.displayFormat = "";
            dateStyle = EDateTimeStyle.DEFAULT;
            timeStyle = EDateTimeStyle.DEFAULT;
        } else {
            this.displayFormat = customPattern;
            dateStyle = EDateTimeStyle.CUSTOM;
            timeStyle = EDateTimeStyle.CUSTOM;
        }
        afterModify();
    }
    
    // <editor-fold desc="RADIX-6098">
    private String displayFormat = "";
    
    private boolean isCustomPatternEmpty() {
        return displayFormat == null || displayFormat.isEmpty();
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.minimumTime);
        hash = 97 * hash + Objects.hashCode(this.maximumTime);
        hash = 97 * hash + (this.dateStyle != null ? this.dateStyle.hashCode() : 0);
        hash = 97 * hash + (this.timeStyle != null ? this.timeStyle.hashCode() : 0);
        hash = 97 * hash + Objects.hashCode(this.displayFormat);
        hash = 97 * hash + super.hashCode();
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj==this){
            return true;
        }        
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EditMaskDateTime other = (EditMaskDateTime) obj;
        if (!Objects.equals(this.minimumTime, other.minimumTime)) {
            return false;
        }
        if (!Objects.equals(this.maximumTime, other.maximumTime)) {
            return false;
        }
        if (this.dateStyle != other.dateStyle) {
            return false;
        }
        if (this.timeStyle != other.timeStyle) {
            return false;
        }
        if (!Objects.equals(this.displayFormat, other.displayFormat)) {
            return false;
        }
        return super.equals(obj);
    } 
    
    private void clearCaches(){
        displayFormatCache.clear();
    }
}