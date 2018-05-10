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
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Queue;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.SettingNames;
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
    
    private final static String DATE_FORMAT_SETTING_NAME=SettingNames.SYSTEM+"/"
                                                                                            +SettingNames.FORMAT_SETTINGS+"/"
                                                                                            +SettingNames.FormatSettings.DATE;
    
    private final static String TIME_FORMAT_SETTING_NAME=SettingNames.SYSTEM+"/"
                                                                                            +SettingNames.FORMAT_SETTINGS+"/"
                                                                                            +SettingNames.FormatSettings.TIME;    
    
    private static final Pattern DATE_TIME_FORMAT_SYMBOLS = Pattern.compile(".*[a-zA-Z].*");        
    
    private static enum Unit {
        YEAR(Calendar.YEAR, "9999", null),
        MONTH(Calendar.MONTH, "99", null),
        DAY(Calendar.DATE, "99", null),
        HOUR(Calendar.HOUR_OF_DAY, "99", "HH"),
        MINUTE(Calendar.MINUTE, "99", "mm"),
        SECOND(Calendar.SECOND, "99", "ss"),
        MILSEC(Calendar.MILLISECOND, "999", "zzz"),
        AMPM(Calendar.AM_PM, "AM", "ap"),
        DELIM(-1, "", null),
        ERA(-1, "", null),
        TIME_ZONE(-1, "", null),
        FILLER(-1, Character.toString('_'), null),
        LITERAL(-1, "", null),
        QUOTE(-1, "", null);
        
        private final int javaCalendarScale;
        private final String inputMaskSymbol;
        private final String qtTimeField;
                
        Unit(final int javaCalendarScale, final String inputMaskSymbol, final String qtTimeMask) {
            this.javaCalendarScale = javaCalendarScale;
            this.inputMaskSymbol = inputMaskSymbol;
            this.qtTimeField = qtTimeMask;
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
        
        public String getQtTimeFild(){
            return qtTimeField;
        }
        
        public static Unit getUnitForQtFormat(final char input) {
            switch(input) {
                case 'y':
                    return YEAR;
                case 'M':
                    return MONTH;
                case 'd':
                    return DAY;
                case 'h':
                case 'H':
                    return HOUR;
                case 'm':
                    return MINUTE;
                case 's':
                    return SECOND;
                case 'z': 
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
                case '\"':
                    return QUOTE;
                case 'T':
                case 'Z':
                case 'X':
                    return TIME_ZONE;                
                default: 
                    return LITERAL;
            }
        }        
        
        public static Unit getUnitForJavaFormat(final char input) {
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
                case 'z':
                case 'Z':
                case 'X':
                    return TIME_ZONE;
                case 'G':
                    return ERA;
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
                if (scale == -1){
                    continue;
                }
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
    
    private static final List<Unit> TIME_UNITS= new LinkedList<>(Arrays.asList(Unit.HOUR, Unit.MINUTE, Unit.SECOND, Unit.MILSEC, Unit.AMPM));
    private static final List<Unit> DATE_UNITS= new LinkedList<>(Arrays.asList(Unit.DAY, Unit.MONTH, Unit.YEAR));
    
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
    
    private static abstract class DateTimeFormatParser{
        
        protected Queue<Token> parse(final String format, final boolean javaFormat){
            final Queue<Token> tokens = new ArrayDeque<>();
            String curToken = "";
            Unit lastUnit = null, curUnit;
            boolean quoted = false;
            char curChar;

            for(int i = 0, count=format.length(); i < count; i++) {
                curChar = format.charAt(i);
                curUnit = javaFormat ? Unit.getUnitForJavaFormat(curChar) : Unit.getUnitForQtFormat(curChar);
                if (curUnit==Unit.QUOTE 
                    && i+1<count 
                    && curChar==format.charAt(i+1)
                ){//"''" represents a single quote
                     curUnit = Unit.LITERAL;
                     i++;
                }
                if (curUnit==Unit.QUOTE){
                    if(!quoted){
                        if (i+1<count){
                            curChar=format.charAt(i+1);
                            i++;
                        }
                        if (lastUnit!=null && lastUnit!=Unit.LITERAL){
                            tokens.add(new Token(lastUnit, curToken));
                            curToken = String.valueOf(curChar);                            
                        }else{
                            curToken += curChar;
                        }
                        lastUnit = Unit.LITERAL;
                    }
                    quoted = !quoted;
                }else if (!quoted && curUnit != lastUnit){//NOPMD
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
            return tokens;            
        }        
    }
    
    private static class JavaDateTimeFormatParser extends DateTimeFormatParser{
        
        private final static JavaDateTimeFormatParser INSTANCE = new JavaDateTimeFormatParser();
        private final Map<String, Queue<Token>> tokensCache = new HashMap<>();
        private final Object semaphore = new Object();
        
        private JavaDateTimeFormatParser(){
            
        }
        
        public static JavaDateTimeFormatParser getInstance(){
            return INSTANCE;
        }
        
        private Queue<Token> getTokens(final String displayFormat){
            synchronized(semaphore){
                Queue<Token> tokens;

                tokens = tokensCache.get(displayFormat);
                if(tokens != null){
                    return new ArrayDeque<>(tokens);
                }
                
                tokens = parse(displayFormat, true);
                
                tokensCache.put(displayFormat, tokens);
                return new ArrayDeque<>(tokens);
            }
        }                
    }
    
    private static class QtDateTimeFormatParser extends DateTimeFormatParser{
        
        private final static QtDateTimeFormatParser INSTANCE = new QtDateTimeFormatParser();
        private final Map<String, Queue<Token>> tokensCache = new HashMap<>();
        private final Object semaphore = new Object();
        
        private QtDateTimeFormatParser(){
            
        }
        
        public static QtDateTimeFormatParser getInstance(){
            return INSTANCE;
        }
        
        private Queue<Token> getTokens(final String displayFormat){
            synchronized(semaphore){
                Queue<Token> tokens;

                tokens = tokensCache.get(displayFormat);
                if(tokens != null){
                    return new ArrayDeque<>(tokens);
                }

                tokens = parse(displayFormat, false);
                
                tokensCache.put(displayFormat, tokens);                
                return new ArrayDeque<>(tokens);
            }
        }                
    }            
    
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

    @Deprecated
    public String getDisplayFormat(final java.util.Locale locale) {//returns java format
        String actualDisplayFormat = displayFormatCache.get(locale);
        if (actualDisplayFormat==null){
            final String javaFormat = getJavaFormat(locale);
            actualDisplayFormat = extendJavaFormatToInputFormat(javaFormat);
            displayFormatCache.put(locale, actualDisplayFormat);
        }
        return actualDisplayFormat;
    }
    
    public String getDisplayFormat(final IClientEnvironment environment) {//returns java format
        final String javaFormat = getJavaFormat(environment);
        return extendJavaFormatToInputFormat(javaFormat);
    }
    
    private String getJavaFormat(final java.util.Locale locale){
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
            return ((java.text.SimpleDateFormat)format).toPattern();                        
        } else {                
            return qtFormat2JavaFormat(locale, displayFormat);
        }
    }   
    
    private String getJavaFormat(final IClientEnvironment environment){
        final Locale locale = environment==null ? Locale.getDefault() : environment.getLocale();
        if(isCustomPatternEmpty()) {            
            DateFormat format;
            if(timeStyle == EDateTimeStyle.NONE && dateStyle != EDateTimeStyle.NONE) {
                final String settingFormat =  
                    environment==null ? null : environment.getConfigStore().readString(DATE_FORMAT_SETTING_NAME+"/"+dateStyle.name().toLowerCase(), null);
                if (settingFormat!=null && !settingFormat.isEmpty()){
                    return qtFormat2JavaFormat(locale, settingFormat);
                }                
                format = DateFormat.getDateInstance(dateStyle.getJavaDateTimeStyle(), locale);
            } else if(timeStyle != EDateTimeStyle.NONE &&  dateStyle == EDateTimeStyle.NONE) {
                final String settingFormat =  
                    environment==null ? null : environment.getConfigStore().readString(TIME_FORMAT_SETTING_NAME+"/"+timeStyle.name().toLowerCase(), null);
                if (settingFormat!=null && !settingFormat.isEmpty()){
                    return qtFormat2JavaFormat(locale, settingFormat);
                }                
                format = DateFormat.getTimeInstance(timeStyle.getJavaDateTimeStyle(), locale);
            } else {
                final String settingDateFormat =  
                    environment==null ? null : environment.getConfigStore().readString(DATE_FORMAT_SETTING_NAME+"/"+dateStyle.name().toLowerCase(), null);
                final String settingTimeFormat =
                    environment==null ? null : environment.getConfigStore().readString(TIME_FORMAT_SETTING_NAME+"/"+timeStyle.name().toLowerCase(), null);    
                if ((settingDateFormat!=null && !settingDateFormat.isEmpty()) || (settingTimeFormat!=null && !settingTimeFormat.isEmpty())){
                    final String dateFormat;
                    if (settingDateFormat!=null && !settingDateFormat.isEmpty()){
                        dateFormat = qtFormat2JavaFormat(locale, settingDateFormat);
                    }else{
                        format = DateFormat.getDateInstance(dateStyle.getJavaDateTimeStyle(), locale);
                        dateFormat = ((java.text.SimpleDateFormat)format).toPattern();
                    }
                    final String timeFormat;
                    if (settingTimeFormat!=null && !settingTimeFormat.isEmpty()){
                        timeFormat = qtFormat2JavaFormat(locale, settingTimeFormat);
                    }else{
                        format = DateFormat.getTimeInstance(timeStyle.getJavaDateTimeStyle(), locale);
                        timeFormat =  ((java.text.SimpleDateFormat)format).toPattern();
                    }
                    return dateFormat+" "+timeFormat;
                }else{
                    format = DateFormat.getDateTimeInstance(dateStyle.getJavaDateTimeStyle(),
                            timeStyle.getJavaDateTimeStyle(),
                            locale);
                }
            }
            return ((java.text.SimpleDateFormat)format).toPattern();
        } else {                
            return qtFormat2JavaFormat(locale, displayFormat);
        }
    }    
    
    private String extendJavaFormatToInputFormat(final String displayFormat){
        final Queue<Token> currentTokens = JavaDateTimeFormatParser.getInstance().getTokens(displayFormat);
        final StringBuilder formatBuilder = new StringBuilder();
        for (Token token: currentTokens){            
            switch(token.unit){
                case MONTH:
                case DAY:
                case HOUR:
                case MINUTE:
                case SECOND:
                case MILSEC:
                case TIME_ZONE:
                case ERA:{
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
                    final String literals = token.literal.replace("\'", "\'\'");
                    if (DATE_TIME_FORMAT_SYMBOLS.matcher(literals).matches()){
                        formatBuilder.append('\'');
                        formatBuilder.append(literals);
                        formatBuilder.append('\'');                        
                    }else{
                        formatBuilder.append(literals);
                    }
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
    
    @Deprecated
    private Queue<Token> getTokens(final Locale locale){
        return JavaDateTimeFormatParser.getInstance().getTokens(getDisplayFormat(locale));
    }       
    
    private Queue<Token> getTokens(final IClientEnvironment environmnet){
        return JavaDateTimeFormatParser.getInstance().getTokens(getDisplayFormat(environmnet));
    }    
    
    @Deprecated // use getInputMask(final IClientEnvironment environment)
    public String getInputMask(final Locale locale) {
        return getInputMask(getTokens(locale));
    }
    
    public String getInputMask(final IClientEnvironment environment) {
        return getInputMask(getTokens(environment));
    }    
    
    private static String getInputMask(final Queue<Token> tokens){
        final StringBuilder sb = new StringBuilder();
        for(int i=0,count=tokens.size(); i<count; i++) {
            final Token currentToken = tokens.poll();
            if (currentToken.unit==Unit.DELIM 
               && tokens.peek()!=null 
               && (tokens.peek().unit==Unit.TIME_ZONE || tokens.peek().unit==Unit.ERA)){
                continue;
            }
            switch(currentToken.unit) {
                case DELIM:
                    sb.append(currentToken.literal);
                    break;
                case YEAR:
                    sb.append(currentToken.unit.withYearLength(currentToken.literal.length()));
                    break;
                case LITERAL:
                    sb.append(currentToken.literal);
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
        
    private String getInputTextForNullValue(final IClientEnvironment environmnet){
        return getInputTextForNullValue(getTokens(environmnet));
    }    
    
    private static String getInputTextForNullValue(final Queue<Token> tokens){
        final StringBuilder sb = new StringBuilder();
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
        
    @Deprecated // use getInputTextForValue(final Timestamp value, final IClientEnvironment environmnet)
    public String getInputTextForValue(final Timestamp value, final Locale locale) {
        return getInputTextForValue(value, getTokens(locale));
    }
    
    public String getInputTextForValue(final Timestamp value, final IClientEnvironment environmnet) {
        return getInputTextForValue(value, getTokens(environmnet));
    }    
    
    // The method is needed to perform possibly alphabetical values to numerical ones
    private static String getInputTextForValue(final Timestamp value, Queue<Token> tokens) {
        if (value==null){            
            return getInputTextForNullValue(tokens);
        }
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
                case QUOTE:
                case ERA:
                case TIME_ZONE:
                    continue;
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
    
    public boolean isEmptyInput(final String inputText, final IClientEnvironment environment){
        return inputText==null 
               || inputText.isEmpty() 
               || inputText.equals(getInputTextForNullValue(environment));
    }
        
    @SuppressWarnings({"fallthrough", "PMD.MissingBreakInSwitch"})
    private Timestamp getValueForInputText(final String inputText, final Queue<Token> tokens) throws WrongFormatException {
        final Calendar date = Calendar.getInstance();
        date.setLenient(false);
        date.clear();        
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
                    case TIME_ZONE:
                    case ERA:
                        //no input for time zone or Era
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
                        if(ampm!=null) {
                            isPm = ampm.equalsIgnoreCase("p") || ampm.equalsIgnoreCase("pm");
                            if (isPm){
                                if (isTwelveHourFormat && currentValue<1){
                                    throw new DateTimeFieldOutOfBoundsException(Calendar.HOUR_OF_DAY, 1, 12, currentValue, null);
                                }else if (currentValue<12){
                                    currentValue+=12;
                                }
                            }else{
                                if (currentValue==12){
                                    currentValue=0;
                                }else if (isTwelveHourFormat && currentValue>12){
                                    throw new DateTimeFieldOutOfBoundsException(Calendar.HOUR_OF_DAY, 1, 12, currentValue, null);
                                }
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
    
    @SuppressWarnings({"fallthrough", "PMD.MissingBreakInSwitch"})
    @Deprecated
    public Timestamp getValueForInputText(final String inputText, final Locale locale) throws WrongFormatException {
        if (inputText==null || inputText.isEmpty()){
            return null;
        }
        final String format = getDisplayFormat(locale);
        final Queue<Token> tokens = JavaDateTimeFormatParser.getInstance().getTokens(format);
        if (inputText.equals(getInputTextForNullValue(tokens))){
            return null;
        }
        return getValueForInputText(inputText, tokens);
    }
    
    @SuppressWarnings({"fallthrough", "PMD.MissingBreakInSwitch"})
    public Timestamp getValueForInputText(final String inputText, final IClientEnvironment environment) throws WrongFormatException {
        if (isEmptyInput(inputText, environment)){
            return null;
        }
        final String format = getDisplayFormat(environment);
        final Queue<Token> tokens = JavaDateTimeFormatParser.getInstance().getTokens(format);
        return getValueForInputText(inputText, tokens);
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
        final StringBuilder format = new StringBuilder();
        final Queue<Token> tokens = QtDateTimeFormatParser.getInstance().getTokens(qtFormat);                
        boolean isAmPmMarkPresent = false;
        for (Token token: tokens){
            if (token.unit==Unit.AMPM){
                isAmPmMarkPresent = true;
                break;
            }
        }
        for (Token token: tokens){
            if (token.unit==Unit.MONTH && "MMMM".equals(token.literal)){//full month name
                format.append(computeFullMonthMask(locale));
                continue;
            }
            if (token.unit==Unit.DAY){
                if ("dddd".equals(token.literal)){//full week day name
                    format.append(computeFullWeekDayMask(locale));
                    continue;
                }
                if ("ddd".equals(token.literal)){
                    format.append("EEE");//short week day name
                    continue;
                }                
            }
            if (token.unit==Unit.MILSEC){
                for (int i=0,count=token.literal.length(); i<count; i++){
                    format.append('S');
                }
                continue;
            }
            if (token.unit==Unit.AMPM){
                format.append('a');
                continue;
            }
            if (token.unit==Unit.HOUR && !isAmPmMarkPresent){
                for (int i=0,count=token.literal.length(); i<count; i++){
                    format.append('H');
                }
                continue;
            }
            if (token.unit==Unit.QUOTE){
                format.append('\'');
                continue;
            }
            if (token.unit==Unit.LITERAL){
                if (DATE_TIME_FORMAT_SYMBOLS.matcher(token.literal).matches()){                    
                    format.append('\'');
                    for (int i=0,count=token.literal.length(); i<count; i++){
                        char c = token.literal.charAt(i);
                        if (c=='\''){
                            format.append('\'');
                        }
                        format.append(c);
                    }
                    format.append('\'');
                }else{
                    format.append(token.literal);
                }
                continue;
            }
            if (token.unit==Unit.TIME_ZONE && token.literal.charAt(0)=='T'){
                for (int i=0,count=token.literal.length(); i<count; i++){
                    format.append('z');
                }
                continue;
            }
            format.append(token.literal);
        }
        return format.toString();
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
            return getNoValueStr(environment==null ? null : environment.getMessageProvider());
        }
        if (value instanceof Timestamp) {
            //final QDateTime dateTime = ValueConverter.timestamp2QtTime((Timestamp) value);
            final Locale loc = environment == null ? Locale.getDefault() : environment.getLocale();
            final String pattern = getDisplayFormat(environment);
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

    @Deprecated
    public boolean timeFieldPresent(final Locale locale) {
        final Queue<Token> tokens = getTokens(locale);
        for(Token token: tokens){
            if (TIME_UNITS.contains(token.unit) && token.unit!=Unit.AMPM){
                return true;
            }
        }
        return false;
    }

    @Deprecated
    public boolean dateFieldPresent(final Locale locale) {
        final Queue<Token> tokens = getTokens(locale);
        for(Token token: tokens){
            if (DATE_UNITS.contains(token.unit)){
                return true;
            }
        }
        return false;
    }
    
    @Deprecated
    public boolean halfDayFieldPresent(final Locale locale){
        return findTokenForUnit(Unit.AMPM, getTokens(locale))!=null;
    }
    
    public boolean timeFieldPresent(final IClientEnvironment environment) {
        final Queue<Token> tokens = getTokens(environment);
        for(Token token: tokens){
            if (TIME_UNITS.contains(token.unit) && token.unit!=Unit.AMPM){
                return true;
            }
        }
        return false;
    }

    public boolean dateFieldPresent(final IClientEnvironment environment) {
        final Queue<Token> tokens = getTokens(environment);
        for(Token token: tokens){
            if (DATE_UNITS.contains(token.unit)){
                return true;
            }
        }
        return false;
    }
        
    public boolean halfDayFieldPresent(final IClientEnvironment environment){
        return findTokenForUnit(Unit.AMPM, getTokens(environment))!=null;
    }    

    @Deprecated
    public Timestamp copyFields(final Timestamp source, final Locale locale) {
        if (source == null) {
            return null;
        }
        return copyFields(source, getTokens(locale));
    }
    
    public Timestamp copyFields(final Timestamp source, final IClientEnvironment environment) {
        if (source == null) {
            return null;
        }
        return copyFields(source, getTokens(environment));
    }    
    
    public static Timestamp copyFields(final Timestamp source, final Queue<Token> tokens) {
        if (source == null) {
            return null;
        }
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(source.getTime());
        int day = 1;
        int month = 0;
        int year = 1970;
        int hour = 0;
        int minute = 0;
        int second = 0;
        int msec = 0;
        for(Token token: tokens){
            switch(token.unit){
                case DAY:
                    day = cal.get(Calendar.DAY_OF_MONTH);
                    break;
                case MONTH:
                    month = cal.get(Calendar.MONTH);
                    break;
                case YEAR:
                    year = cal.get(Calendar.YEAR);
                    break;
                case HOUR:
                    hour = cal.get(Calendar.HOUR_OF_DAY);
                    break;
                case MINUTE:
                    minute = cal.get(Calendar.MINUTE);
                    break;
                case SECOND:
                    second = cal.get(Calendar.SECOND);
                    break;
                case MILSEC:
                    msec = cal.get(Calendar.MILLISECOND);
                    break;
            }
        }        
        cal.set(year, month, day, hour, minute, second);
        cal.set(Calendar.MILLISECOND, msec);
        return new Timestamp(cal.getTimeInMillis());
    }    
    
    private static Token findTokenForUnit(final Unit unit, final Queue<Token> tokens){
        for(Token token: tokens){
            if (token.unit==unit){
                return token;
            }
        }
        return null;
    }

    @Deprecated
    public String getInputTimeFormat(final Locale locale) {
        return getInputTimeFormat(getTokens(locale));
    }
    
    public String getInputTimeFormat(final IClientEnvironment environmnet) {
        return getInputTimeFormat(getTokens(environmnet));
    }    
    
    private static String getInputTimeFormat(final Queue<Token> tokens) {
        final List<Token> timeTokens = new LinkedList<>();
        for (Unit timeUnit: TIME_UNITS){
            final Token timeToken = findTokenForUnit(timeUnit, tokens);
            if (timeToken!=null && timeToken.literal.length()>0){
                timeTokens.add(timeToken);
            }
        }
        final StringBuilder timeFormat = new StringBuilder();
        for (Token timeToken: timeTokens){
            if (timeFormat.length()>0){
                timeFormat.append(timeToken.unit==Unit.AMPM ? ' ' : ':');                    
            }
            if (timeToken.unit==Unit.HOUR){
                if (Character.isUpperCase(timeToken.literal.charAt(0))){
                    timeFormat.append(timeToken.unit.getQtTimeFild().toUpperCase());
                }else{
                    timeFormat.append(timeToken.unit.getQtTimeFild().toLowerCase());
                }
            }else{
                timeFormat.append(timeToken.unit.getQtTimeFild());
            }
        }
        return timeFormat.toString();        
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
        
    public static String javaFormat2QtFormat(final Locale locale, final String javaFormat){
        return javaFormat2QtFormat(locale, javaFormat, false, false);
    }
        
    public static String javaFormat2QtFormat(final Locale locale, final String javaFormat, final boolean ignoreDate, final boolean ignoreTime){
        final StringBuilder format = new StringBuilder();
        final Queue<Token> tokens = JavaDateTimeFormatParser.getInstance().getTokens(javaFormat);
        for (Token token: tokens){
            final Unit tokenUnit = token.unit;
            if (ignoreDate && (DATE_UNITS.contains(tokenUnit) || tokenUnit==Unit.ERA)){
                format.append('\'');
                format.append(token.literal);
                format.append('\'');
            }else if (ignoreTime && (TIME_UNITS.contains(tokenUnit) || tokenUnit==Unit.TIME_ZONE)){
                format.append('\'');
                format.append(token.literal);
                format.append('\'');
            }else {
                switch (tokenUnit){
                    case AMPM:{
                        format.append("AP");
                        break;
                    }case DAY:{
                        final int length=token.literal.length();
                        if (length>3){
                            format.append("dddd");
                        }else{
                            for (int i=0; i<length; i++){
                                format.append("d");
                            }
                        }
                        break;
                    }case HOUR:{
                        final int length=token.literal.length();
                        final char c = token.literal.charAt(0);
                        format.append(c);
                        if (length>1){
                            format.append(c);
                        }
                        break;
                    }case LITERAL:{
                        if (DATE_TIME_FORMAT_SYMBOLS.matcher(token.literal).matches()){
                            format.append('\'');
                            for (int i=0,count=token.literal.length(); i<count; i++){
                                char c = token.literal.charAt(i);
                                if (c=='\''){
                                    format.append('\'');
                                }
                                format.append(c);
                            }
                            format.append('\'');
                        }else{
                            format.append(token.literal);
                        }
                        break;
                    }case MILSEC:{
                        final int length=token.literal.length();
                        if (length>1){
                            format.append("zzz");
                        }else{
                            format.append('z');
                        }
                        break;
                    }case MINUTE:{
                        final int length=token.literal.length();
                        if (length>1){
                            format.append("mm");
                        }else{
                            format.append('m');
                        }
                        break;
                    }case MONTH:{
                        final int length=token.literal.length();
                        if (length>3){
                            format.append("MMMM");
                        }else{
                            format.append(token.literal);
                        }
                        break;
                    }case QUOTE:{
                        format.append('\'');
                        break;
                    }case SECOND:{
                        final int length=token.literal.length();
                        if (length>1){
                            format.append("ss");
                        }else{
                            format.append('s');
                        }
                        break;
                    }case TIME_ZONE:{
                        if (token.literal.charAt(0)=='z'){
                            for (int i=0,count=token.literal.length(); i<count; i++){
                                format.append('T');
                            }                
                        }else{
                            format.append(token.literal);
                        }
                        break;
                    }case YEAR:{
                        final int length=token.literal.length();
                        if (length>2){
                            format.append("yyyy");
                        }else{
                            format.append("yy");
                        }
                        break;
                    }default:{
                        format.append(token.literal);
                    }
                }
            }
        }
        return format.toString();
    }
    
    public static String stripDateFormat(final String qtFormat){
        if (qtFormat==null || qtFormat.isEmpty()){
            return qtFormat;
        }
        final StringBuilder format = new StringBuilder();
        final Queue<Token> tokens = QtDateTimeFormatParser.getInstance().getTokens(qtFormat);
        for (Token token: tokens){
            final Unit tokenUnit = token.unit;
            if (DATE_UNITS.contains(tokenUnit) || tokenUnit==Unit.ERA){
                format.append('\'');
                format.append(token.literal);
                format.append('\'');
            }else if (tokenUnit==Unit.LITERAL){
                if (DATE_TIME_FORMAT_SYMBOLS.matcher(token.literal).matches()){
                    format.append('\'');
                    for (int i=0,count=token.literal.length(); i<count; i++){
                        char c = token.literal.charAt(i);
                        if (c=='\''){
                            format.append('\'');
                        }
                        format.append(c);
                    }
                    format.append('\'');
                }else{
                    format.append(token.literal);
                }                
            }else if (tokenUnit==Unit.QUOTE){
                format.append('\'');
            }else{
                format.append(token.literal);
            }
        }
        return format.toString();
    }
    
    public static String stripTimeFormat(final String qtFormat){
        if (qtFormat==null || qtFormat.isEmpty()){
            return qtFormat;
        }        
        final StringBuilder format = new StringBuilder();
        final Queue<Token> tokens = QtDateTimeFormatParser.getInstance().getTokens(qtFormat);
        for (Token token: tokens){
            final Unit tokenUnit = token.unit;
            if (TIME_UNITS.contains(tokenUnit) || tokenUnit==Unit.TIME_ZONE){
                format.append('\'');
                format.append(token.literal);
                format.append('\'');
            }else if (tokenUnit==Unit.LITERAL){
                if (DATE_TIME_FORMAT_SYMBOLS.matcher(token.literal).matches()){
                    format.append('\'');
                    for (int i=0,count=token.literal.length(); i<count; i++){
                        char c = token.literal.charAt(i);
                        if (c=='\''){
                            format.append('\'');
                        }
                        format.append(c);
                    }
                    format.append('\'');
                }else{
                    format.append(token.literal);
                }                
            }else if (tokenUnit==Unit.QUOTE){
                format.append('\'');
            }else{
                format.append(token.literal);
            }
        }
        return format.toString();
    }    
    
    
}