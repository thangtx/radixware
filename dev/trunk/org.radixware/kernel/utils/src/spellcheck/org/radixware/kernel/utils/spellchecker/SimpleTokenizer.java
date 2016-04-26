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

package org.radixware.kernel.utils.spellchecker;

import java.util.LinkedList;
import java.util.List;


public class SimpleTokenizer {

    private final String string;
    private int start = 0;
    private int len = 0;

    public SimpleTokenizer(String string) {
        this.string = string;
        len = string.length();
    }

    public String nextWord() {
        Token t = nextToken();
        if (t == null) {
            return null;
        }
        return t.text;
    }

    private boolean isInternalQuote(char c) {
        if (c == '\'') {
            if (start + 1 < len) {
                char next = string.charAt(start + 1);
                if (Character.isLetter(next)) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    Token nextToken() { 
        int wordStart = -1;
        main:
        while (start < len) {
            char c = string.charAt(start);
            if (wordStart >= 0) {
                if (!Character.isLetter(c) && !isInternalQuote(c)) {
                    if (Character.isDigit(c)) {//generic word, possibly tecnical abbreviature
                        while (start < len) {
                            c = string.charAt(start);
                            if (!Character.isDigit(c) && !Character.isLetter(c)) {
                                wordStart = -1;
                                continue main;
                            }
                            start++;
                        }
                    }
                    int len = start - wordStart;
                    if (len == 1) {
                        wordStart = -1;
                        continue main;
                    }
                    if (c == '.') {
                        start++;
                        return new Token(string.substring(wordStart, start), wordStart, len);
                    } else {
                        return new Token(string.substring(wordStart, start), wordStart, len);
                    }
                }
            } else {
                if (Character.isLetter(c)) {
                    wordStart = start;
                }
                if (c == '%') {
                    int formatLength = formatLenght(start);
                    if (formatLength > 1) {
                        wordStart = start;
                        start += formatLength;
                        return new Token(string.substring(wordStart, wordStart+formatLength), wordStart, formatLength);
                    }
                }
            }
            start++;
        }
        if (wordStart >= 0) {
            return new Token(string.substring(wordStart), wordStart, start - wordStart);
        }

        return null;
    }
    
    
    public static int conversationLength(String conversation){
        if (conversation==null || conversation.isEmpty()){
            return 0;
        }
        
        switch (conversation.charAt(0)){
            case 'b':case 'B':
            case 'h':case 'H':
            case 's':case 'S':
            case 'c':case 'C':
            case 'd':case 'o':
            case 'x':case 'X':
            case 'e':case 'E':
            case 'f':case 'n':
            case 'g':case 'G':
            case 'a':case 'A':
            case '%':           return 1; 
            case 't':case 'T': 
                               if (conversation.length() < 2){
                                    return 0;
                                } 
                                //Date/Time Conversions
                                switch (conversation.charAt(1)){
                                    //for formatting times
                                    case 'H':case 'k':
                                    case 'I':case 'l':
                                    case 'M':case 'S':
                                    case 'L':case 'N':
                                    case 'p':case 'z':
                                    case 'Z':case 's':
                                    case 'Q':           
                                    // for formatting dates 
                                    case 'B':case 'b':
                                    case 'h':case 'A':
                                    case 'a':case 'C':
                                    case 'Y':case 'y':
                                    case 'j':case 'm':
                                    case 'd':case 'e':  
                                    //  for formatting common date/time compositions
                                    case 'R':case 'T':
                                    case 'r':case 'D':
                                    case 'F':case 'c':  return 2;    
                                    default : return 0; 
                                }
            default :       return 0;
        }  
    }
    
    private int formatLenght(int formatStart) {
        int currentIndex = formatStart + 1;
        boolean containsIndex = false;
        boolean containsWidth = false;
        boolean containsPrecision = false;
        while (currentIndex < len) {
            char c = string.charAt(currentIndex);
            if (Character.isLetter(c)) {
                int startConversation = currentIndex;
                currentIndex++;
                if (currentIndex < len) {
                    c = string.charAt(currentIndex);
                    if (Character.isLetter(c)){
                        currentIndex++;
                    } 
                } 
                int length = conversationLength(string.substring(startConversation,currentIndex));
                if (length > 0) {
                    return startConversation+length-formatStart;
                } else {
                    return 0;
                }
            } else {
            if (Character.isDigit(c) && !containsWidth) {
                char beforeDigits = string.charAt(currentIndex-1);
                currentIndex++;
                while (currentIndex < len) {
                    c = string.charAt(currentIndex);
                    if (!Character.isDigit(c)){
                        if (c=='$' && beforeDigits=='%'){
                            containsIndex = true;
                            currentIndex++;
                        } else {
                            containsWidth = true;
                        }
                        break;
                    } else currentIndex++;
                }
            } else  {
                if (c == '-' || c == '#' || c == '+' || c == '0' || c==','|| c=='(' || c==' '){
                    if (!containsWidth && !containsPrecision){
                        currentIndex++;
                    } else {
                        return currentIndex-formatStart;
                    }
                }
                else {
                        if (c=='.'){
                            currentIndex++;
                            while (currentIndex < len){
                                c = string.charAt(currentIndex);
                                if (Character.isDigit(c)){
                                    currentIndex++;
                                    containsPrecision=true;
                                } else break;
                            }
                        } else {
                            if (!containsIndex){
                                if (c=='<' && (currentIndex==formatStart+1)){
                                    currentIndex++;
                                    containsIndex = true;
                                }
                                else {
                                    if (c == '%'){
                                        currentIndex++;
                                    }
                                    return currentIndex-formatStart;
                                }
                            } else {
                                return currentIndex-formatStart;
                            }
                        }
                    }
                }
            }
        }
        
        return currentIndex-formatStart;
    }
    
    
    
    public static class Token {

        public final String text;
        public final int start;
        public final int length;

        public Token(String text, int start, int length) {
            this.text = text;
            this.start = start;
            this.length = length;
        }
    }

    public List<Token> tokenize() {
        List<Token> tokens = new LinkedList<Token>();
        Token t = nextToken();
        while (t != null) {
            tokens.add(t);
            t = nextToken();
        }
        return tokens;
    }
}
