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

package org.radixware.kernel.common.client.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.enums.EIsoLanguage;

public final class ClientValueFormatter {
//Constructor

    private ClientValueFormatter() {
    }

    public static String[] split(String s, char ch) {
        List<String> list = new ArrayList<String>();
        String cur = "";
        for (int i = 0; i < s.length(); ++i) {
            if (s.charAt(i) == ch) {
                if (!cur.isEmpty()) {
                    list.add(cur);
                    cur = "";
                }
            } else {
                cur += s.charAt(i);
            }
        }
        if (!cur.isEmpty()) {
            list.add(cur);
        }
        return list.toArray(new String[list.size()]);
    }

    public static String capitalizeString(final String string) {
        if (string == null) {
            return null;
        }
        final String[] words = string.split("[\\s-]");
        final StringBuffer result = new StringBuffer();
        for (int i = 0; i < words.length; ++i) {
            if (words[i].length() > 0) {
                result.append((i > 0 ? " " : ""));
                result.append(words[i].substring(0, 1).toUpperCase());
                result.append(words[i].substring(1));
            }
        }
        return result.toString();
    }    

    public static String capitalizeIfNecessary(IClientEnvironment env, final String text) {
        return capitalizeIfNecessary(env.getLanguage(), text);
    }
    
    private final static List<String> CAPITALIZATION_EXCEPTIONS = 
            Arrays.asList("a", "an", "the", "is", "no", "not", "or", "and",
                "about", "above", "across", "after", "against", "along", "among", "around", "as", "at",
                "before", "behind", "below", "beside", "between", "by", "down", "during", "except", "for", "from",
                "in", "inside", "into", "like", "near", "of", "off", "on", "onto", "outside", "over", "past", "since", "through",
                "to", "toward", "under", "underneath", "until", "up", "upon", "with", "within", "without");
    
    public static String capitalizeIfNecessary(EIsoLanguage language, final String text) {
        if (language == EIsoLanguage.ENGLISH
                && text != null && !text.isEmpty()) {

            final StringBuffer result = new StringBuffer(8);
            final StringBuffer wordBuffer = new StringBuffer(8);            
            char currentChar;
            boolean escape = false;
            boolean firstWord = true;
            
            for (int i = 0; i < text.length(); ++i) {
                currentChar = text.charAt(i);
                if (Character.isLetter(currentChar)){
                    wordBuffer.append(currentChar);
                }else{
                    if (wordBuffer.length()>0){
                        final String word = wordBuffer.toString();                        
                        if (!firstWord && (CAPITALIZATION_EXCEPTIONS.contains(word) || escape)){
                            result.append(word);
                        } else {
                            result.append(word.substring(0, 1).toUpperCase());
                            result.append(word.substring(1));
                            firstWord = false;
                        }
                        wordBuffer.setLength(0);
                    }
                    escape = i>0 && Character.isLetter(text.charAt(i-1)) && (currentChar=='\'' || currentChar=='`');
                    result.append(currentChar);
                }
            }
            if (wordBuffer.length()>0){
                final String word = wordBuffer.toString();
                if (!firstWord && (CAPITALIZATION_EXCEPTIONS.contains(word) || escape)) {
                    result.append(word);
                } else {
                    result.append(word.substring(0, 1).toUpperCase());
                    result.append(word.substring(1));                            
                }
            }
            return result.toString();
        }
        return text;
    }
    
    public static String decapitalizeIfNecessary(IClientEnvironment env, final String text) {
        return decapitalizeIfNecessary(env.getLanguage(), text);
    }    
    
    public static String decapitalizeIfNecessary(EIsoLanguage language, final String text) {
        if (language == EIsoLanguage.ENGLISH && text != null && !text.isEmpty()) {

            final StringBuffer result = new StringBuffer(32);
            final StringBuffer wordBuffer = new StringBuffer(8);            
            char currentChar;
            boolean escape = false;
            boolean ignore = false;
            boolean firstWord = true;
            boolean firstChar = true;
            
            for (int i = 0; i < text.length(); ++i) {
                currentChar = text.charAt(i);
                if (Character.isLetter(currentChar)){
                    if (!firstChar && Character.isUpperCase(currentChar)){
                        ignore = true;
                    }
                    firstChar = false;
                    wordBuffer.append(currentChar);
                }else{
                    firstChar = true;
                    if (wordBuffer.length()>0){
                        final String word = wordBuffer.toString();                        
                        if (firstWord || escape || ignore){
                            result.append(word);
                            firstWord = false;
                            ignore = false;
                        } else {
                            result.append(word.substring(0, 1).toLowerCase());
                            result.append(word.substring(1));                            
                        }
                        wordBuffer.setLength(0);
                    }
                    escape = i>0 && Character.isLetter(text.charAt(i-1)) && (currentChar=='\'' || currentChar=='`');                    
                    result.append(currentChar);
                }
            }
            if (wordBuffer.length()>0){
                final String word = wordBuffer.toString();
                if (firstWord || escape || ignore) {
                    result.append(word);
                } else {
                    result.append(word.substring(0, 1).toLowerCase());
                    result.append(word.substring(1));                            
                }
            }
            return result.toString();
        }
        return text;
    }
    
}
