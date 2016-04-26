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

package org.radixware.kernel.common.meta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class InputMask {//based on qlineedit.cpp
   
    public static class Factory{
        private Factory(){};
        
        private static final Map<String,InputMask> INSTANCE_CACHE = new HashMap<>(128);
        private static final InputMask NO_MASK = new InputMask("", "", ' ');
        
        public static synchronized InputMask newInstance(final String pattern){
            if (pattern==null || pattern.isEmpty()){
                return NO_MASK;
            }
            InputMask instance = INSTANCE_CACHE.get(pattern);
            if (instance==null){
                final int delimiter = pattern.indexOf(";");
                if (delimiter == 0) {
                    return NO_MASK;
                }

                final String mask;
                char blankChar;
                if (delimiter == -1) {
                    blankChar = ' ';
                    mask = pattern;
                } else {
                    mask = pattern.substring(0, delimiter);
                    blankChar = delimiter + 1 < pattern.length() ? pattern.charAt(delimiter + 1) : ' ';
                }
        
                instance = new InputMask(pattern,mask,blankChar);
                INSTANCE_CACHE.put(pattern, instance);
            }
            return instance;
        }
    }
    
    private static enum CaseMode{
        Upper,
        Lower,
        None
    }
    
    private static class InputMaskToken {

        public final char maskChar;
        public final boolean isSeparator;
        public final CaseMode caseMode;

        public InputMaskToken(final char _maskChar, final boolean separator, final CaseMode _caseMode) {
            maskChar = _maskChar;
            caseMode = _caseMode;
            isSeparator = separator;
        }
    }
    
    private final List<InputMaskToken> tokens = new ArrayList<>();
    private final String pattern;
    private final char blankChar;
    
    private static char[] IGNORING_CHARACTERS = new char[]{'[',']','{','}'};
    {
        Arrays.sort(IGNORING_CHARACTERS);
    }        
    
    
    private InputMask(final String pattern, final String mask, final char blankChar){        
        this.pattern = pattern;
        this.blankChar = blankChar;
        if (!mask.isEmpty())
            parseInputMask(mask);
    }
    
    private void parseInputMask(final String mask){
        
        char c = 0;
        boolean separator;
        boolean escape = false;
        CaseMode caseMode = CaseMode.None;
        
        for (int i = 0; i < mask.length(); i++) {
            c = mask.charAt(i);
            if (escape) {
                separator = true;
                tokens.add(new InputMaskToken(c, separator, caseMode));
                escape = false;
            }else if (c == '<') {
                caseMode = CaseMode.Lower;
            }else if (c == '>') {            
                caseMode = CaseMode.Upper;
            }else if (c == '!'){
                caseMode = CaseMode.None;
            }else if (Arrays.binarySearch(IGNORING_CHARACTERS, c)<0){
                switch (c) {
                    case 'A':
                    case 'a':
                    case 'N':
                    case 'n':
                    case 'X':
                    case 'x':
                    case '9':
                    case '0':
                    case 'D':
                    case 'd':
                    case '#':
                    case 'H':
                    case 'h':
                    case 'B':
                    case 'b':
                        separator = false;
                        break;
                    case '\\':
                        escape = true;
                        separator = true;
                        break;
                    default:
                        separator = true;
                        break;
                }

                if (!escape) {
                    tokens.add(new InputMaskToken(c, separator, caseMode));
                }
            }
        }        
    }    
    
    private boolean isValidInput(final char inputChar, final char maskChar){
        switch (maskChar) {
            case 'A':
                if (Character.isLetter(inputChar))
                    return true;
                break;
            case 'a':
                if (Character.isLetter(inputChar) || inputChar == blankChar)
                    return true;
                break;
            case 'N':
                if (Character.isLetterOrDigit(inputChar))
                    return true;
                break;
            case 'n':
                if (Character.isLetterOrDigit(inputChar) || inputChar == blankChar)
                    return true;
                break;
            case 'X':                
                if (isPrintable(inputChar))
                    return true;
                break;
            case 'x':
                if (isPrintable(inputChar) || inputChar == blankChar)
                    return true;
                break;
            case '9':
                if (Character.isDigit(inputChar))
                    return true;
                break;
            case '0':
                if (Character.isDigit(inputChar) || inputChar == blankChar)
                    return true;
                break;
            case 'D':                
                if (Character.isDigit(inputChar) && Character.getNumericValue(inputChar) > 0)
                    return true;
                break;
            case 'd':
                if ((Character.isDigit(inputChar) && Character.getNumericValue(inputChar) > 0) || inputChar == blankChar)
                    return true;
                break;
            case '#':
                if (Character.isDigit(inputChar) || inputChar == '+' || inputChar == '-' || inputChar == blankChar)
                    return true;
                break;
            case 'B':
                if (inputChar == '0' || inputChar == '1')
                    return true;
                break;
            case 'b':
                if (inputChar == '0' || inputChar == '1' || inputChar == blankChar)
                    return true;
                break;
            case 'H':
                if (Character.getNumericValue(inputChar)>=0 && Character.getNumericValue(inputChar)<16)
                    return true;
                break;
            case 'h':
                if ((Character.getNumericValue(inputChar)>=0 && Character.getNumericValue(inputChar)<16)  || inputChar == blankChar)
                    return true;
                break;
            default:
                break;
        }
        return false;
    }
    
    private static boolean isPrintable(final char c){
        return Character.getType(c)!=Character.CONTROL && Character.getType(c)!=Character.UNASSIGNED && c!='\u2007';
    }
    
    public boolean isAcceptableInput(final String inputStr){
        
        if (isEmpty())
            return true;
        
        if (inputStr==null){
            return false;
        }        

        if (inputStr.length() > tokens.size())
            return false;
        
        final String maskedStr = applyTo(inputStr);

        for (int i=0; i<maskedStr.length(); ++i) {
            if (tokens.get(i).isSeparator) {
                if (maskedStr.charAt(i) != tokens.get(i).maskChar){
                    return false;
                }
            }
            else if (!isValidInput(maskedStr.charAt(i), tokens.get(i).maskChar)){
                return false;
            }
        }
        
        for (int i=maskedStr.length(); i<tokens.size(); i++){
            if (!tokens.get(i).isSeparator && !isValidInput(blankChar, tokens.get(i).maskChar)){
                return false;
            }
        }
            
        return true;
    }        
    
    public String applyTo(final String inputString){
        
        final String clearString = getClearString(0, 0);
        
        if (inputString==null || inputString.isEmpty()){
            return clearString;
        }                
        
        final StringBuffer buffer = new StringBuffer();
        
        for (int i=0,j=0; i<tokens.size() && j<inputString.length();){
            if (tokens.get(i).isSeparator){
                buffer.append(tokens.get(i).maskChar);
                if (inputString.charAt(j)==tokens.get(i).maskChar){
                    j++;
                }
                i++;
            }
            else{
                if (isValidInput(inputString.charAt(j), tokens.get(i).maskChar)){
                    switch(tokens.get(i).caseMode){
                        case Upper:
                            buffer.append(Character.toUpperCase(inputString.charAt(j)));
                            break;
                        case Lower:
                            buffer.append(Character.toLowerCase(inputString.charAt(j)));
                            break;
                        default:
                            buffer.append(inputString.charAt(j));
                    }
                    i++;
                }
                else{
                    // search for separator first                    
                    int pos = findSeparatorPosition(i, inputString.charAt(j));
                    if (pos>-1){
                        //example: For pattern "9999-9999-9999;_" and input string "33-22-33" 
                        //result must be 33__-22__-33__
                        if (inputString.length() != 1 || i == 0 || 
                            (i > 0 && (!tokens.get(i-1).isSeparator || tokens.get(i-1).maskChar != inputString.charAt(j)))
                           ) {
                            buffer.append(clearString.subSequence(i, pos+1));
                            i = pos+1;
                        }
                    }
                    else{// search for valid significant if not
                        pos = findSignificantCharacterPosition(i, inputString.charAt(j));
                        if (pos>0){
                            //example: For pattern "9999>AAAA9999;_" and input string "33bb22" 
                            //result must be 33__BB__22__
                            if (i<pos){
                                buffer.append(clearString.subSequence(i, pos));
                            }                                                            
                            switch (tokens.get(pos).caseMode) {
                                case Upper:
                                    buffer.append(Character.toUpperCase(inputString.charAt(j)));
                                    break;
                                case Lower:
                                    buffer.append(Character.toLowerCase(inputString.charAt(j)));
                                    break;
                                default:
                                    buffer.append(inputString.charAt(j));
                            }
                            i = pos + 1; // updates i to find + 1                            
                        }
                    }
                    
                }
                j++;
            }
        }
        
        if (buffer.length()<clearString.length()){
            buffer.append(clearString.subSequence(buffer.length(), clearString.length()));
        }        

        return buffer.toString();
    }
    
    public String stripDisplayString(final String displayString, final boolean saveSeparators) {
        if (isEmpty()) {
            return displayString;
        }
        final StringBuilder result = new StringBuilder();        
        for (int i = 0; i < tokens.size() && i < displayString.length(); i++) {
            if (tokens.get(i).isSeparator){
                if (saveSeparators && 
                    (hasSignificantSymbolAfterPosition(displayString, i) || isLastSeparator(i))
                    ){
                        result.append(tokens.get(i).maskChar);
                    }
            }
            else{
                if (displayString.charAt(i) != blankChar){
                    result.append(displayString.charAt(i));                
                }
            }
        }
        return result.toString();
    }
    
    public int getPositionOfFirstBlankCharInDisplayString(final String displayString) {
        int blankCharPosition = -1;
        for (int i = displayString.length() - 1; i >= 0; i--) {
            if (i < tokens.size() && !tokens.get(i).isSeparator) {
                if (displayString.charAt(i) == blankChar) {
                    blankCharPosition = i;
                } else {
                    return blankCharPosition;
                }
            }
        }
        return blankCharPosition;
    }    
    
    private String getClearString(final int startPos, final int length){    
        if (startPos >= tokens.size())
            return "";

        StringBuilder result = new StringBuilder();
        int endPos = length>0 ? Math.min(tokens.size(), startPos+length) : tokens.size();
        for (int i=startPos; i<endPos; i++){
            if (tokens.get(i).isSeparator){
                result.append(tokens.get(i).maskChar);
            }
            else{
                result.append(blankChar);
            }
        }
        return result.toString();
    }
    
    private int findSeparatorPosition(final int pos, final char searchChar){    
        if (pos >= tokens.size() || pos < 0)
            return -1;

        for (int i=pos; i<tokens.size(); i++){
            if (tokens.get(i).isSeparator && tokens.get(i).maskChar==searchChar){
                return i;
            }
        }
        
        return -1;
    } 
    
    private int findSignificantCharacterPosition(final int pos, final char searchChar){    
        if (pos >= tokens.size() || pos < 0)
            return -1;

        for (int i=pos; i<tokens.size(); i++){
            if (!tokens.get(i).isSeparator && isValidInput(searchChar, tokens.get(i).maskChar)){
                return i;
            }
        }        
        return -1;
    }
        
    private boolean hasSignificantSymbolAfterPosition(final String displayText, final int pos){
        for (int i=pos+1; i<tokens.size() && i<displayText.length(); i++){
            if (!tokens.get(i).isSeparator && displayText.charAt(i) != blankChar){
                return true;
            }
        }
        return false;
    }
    
    private boolean isLastSeparator(final int pos){
        for (int i=pos+1; i<tokens.size(); i++){
            if (!tokens.get(i).isSeparator){
                return false;
            }
        }
        return true;
    }
    
    public String getPattern(){
        return pattern;
    }
    
    public boolean isEmpty(){
        return tokens.isEmpty();
    }
    
    public int getLength(){
        return tokens.size();
    }    
    
    public String checkPattern(final boolean saveSeparators){
        return checkPattern(saveSeparators, "Pattern \'%1$s\' for required symbol cannot be used after pattern \'%2$s\' for optional symbol");
    }
    
    public String checkPattern(final boolean saveSeparators, final String errorMessagePattern){
        final List<Character> optionalSymbols = new ArrayList<>();
        final List<Character> requiredSymbols = new ArrayList<>();
        for (InputMaskToken token: tokens){
            if (token.isSeparator){
                if (saveSeparators){
                    optionalSymbols.clear();
                    requiredSymbols.clear();
                }
            }else if (isPatternForOptionalSymbol(token.maskChar)){
                if (!optionalSymbols.contains(Character.valueOf(token.maskChar))){
                    optionalSymbols.add(Character.valueOf(token.maskChar));
                }
                requiredSymbols.clear();
            }else if (!requiredSymbols.contains(Character.valueOf(token.maskChar))){
                for(Character optionalChar: optionalSymbols){
                    if (isPatternCharactersCompatible(token.maskChar, optionalChar)){
                        return String.format(errorMessagePattern, token.maskChar, optionalChar);
                    }
                }
                requiredSymbols.add(token.maskChar);
            }
        }
        return null;        
    }
        
    
    private static boolean isPatternCharactersCompatible(final char char1, final char char2){
        switch (char1) {
            case 'A':
            case 'a':
                return !(char2=='9' || char2=='0' || char2=='d' || char2=='D' || 
                       char2=='#' || char2=='b' || char2=='B');
            case '9':
            case '0':
            case 'D':            
            case 'd':
            case 'B':
            case 'b':
            case '#':
                return char2!='A' && char2!='a';                
            default:
                return true;
        }
    }
    
    private static char[] OPTIONAL_CHARACTERS = new char[]{'#','0','a','b','d','h','n','x'};
    {
        Arrays.sort(OPTIONAL_CHARACTERS);
    }
    
    private static boolean isPatternForOptionalSymbol(final char patternChar){
        return Arrays.binarySearch(OPTIONAL_CHARACTERS, patternChar)>0;
    }    
}
