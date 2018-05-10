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

import java.io.InputStream;
import java.util.Iterator;
import java.util.Random;
import org.radixware.kernel.common.check.spelling.CommonDictionary;
import org.radixware.kernel.common.check.spelling.IDictionaryDataProvider;
import org.radixware.kernel.common.check.spelling.IProvider;
import org.radixware.kernel.common.check.spelling.Word;
import org.radixware.kernel.common.enums.EIsoLanguage;


public class DictionaryTestUtils {

    public static InputStream getInputStream(String resource) {
        return DictionaryLoaderTest.class.getResourceAsStream(resource);
    }

    public static IProvider<InputStream> getStreamProvider(final String resource) {
        return new IProvider<InputStream>() {

            @Override
            public InputStream provide() {
                return getInputStream(resource);
            }
        };
    }
    
    public static LayerDictionary fastLoadLayerDictionary(EIsoLanguage language, String fileName) {
        IDictionaryDataProvider dataProvider = new LayerDictionaryWordsDataProvider(getStreamProvider(fileName));
        return new LayerDictionary(language, dataProvider, null);
    }
    
    public static LayerDictionary fullLoadLayerDictionary(EIsoLanguage language, String fileName) {
        IDictionaryDataProvider dataProvider = new LayerDictionaryFullDataProvider(getStreamProvider(fileName));
        return new LayerDictionary(language, dataProvider, null);
    }
    
    public static CommonDictionary loadCommonDictionary(EIsoLanguage language) {
        return DictionaryLoader.loadCommonDictionary(language);
    }
}


class WordGenerator {

    private Random random = new Random(System.nanoTime());
    private String abcString = null;
    private Alphabet alphabet = Alphabet.EN;

    public void setAlphabet(String abc) {
        this.abcString = abc;
        alphabet = null;
    }

    public void setAlphabet(Alphabet alphabet) {
        this.alphabet = alphabet;
        abcString = null;
    }

    public Word generate(CharSequence sequence, int length) {

        return Word.Factory.newInstance(getRandomCharArrayStartWith(sequence, length));
    }

    public Word generate(char ch, int length) {

        return Word.Factory.newInstance(getRandomCharArrayStartWith(ch, length));
    }

    public Word generate(int length) {

        return Word.Factory.newInstance(getRandomCharArray(length));
    }

    public Word[] generate(int count, int minLen, int maxLen) {
        Word[] words = new Word[count];

        for (int i = 0; i < count; ++i) {
            words[i] = generate(getRandomLen(minLen, maxLen));
        }

        return words;
    }

    public Word[] generate(char ch, int count, int minLen, int maxLen) {
        Word[] words = new Word[count];

        for (int i = 0; i < count; ++i) {
            words[i] = generate(ch, getRandomLen(minLen, maxLen));
        }

        return words;
    }

    public char[] getRandomCharArray(int length) {
        char[] chars = new char[length];

        for (int i = 0; i < length; ++i) {
            chars[i] = getRandomChar();
        }

        return chars;
    }

    public char[] getRandomCharArrayStartWith(CharSequence sequence, int length) {
        char[] chars = new char[length];


        for (int i = 0; i < length; ++i) {
            if (i < sequence.length()) {
                char ch = sequence.charAt(i);
                validChar(ch);
                chars[i] = ch;
            } else {
                chars[i] = getRandomChar();
            }
        }

        return chars;
    }

    public char[] getRandomCharArrayStartWith(char ch, int length) {
        return getRandomCharArrayStartWith(String.valueOf(ch), length);
    }

    private int getRandomLen(int min, int max) {
        return min + (max > min ? random.nextInt(max - min) : 0);
    }

    private char getRandomChar() {
        checkAlphabet();
        
        if (abcString != null) {
            return abcString.charAt(random.nextInt(abcString.length()));
        } else {
            return alphabet.charAt(random.nextInt(alphabet.length()));
        }
    }

    private void checkAlphabet() {
        assert alphabet != null || abcString != null : "Alphabet not set";
    }
    
    private void validChar(char ch) {
        checkAlphabet();
        
        String message = "Char '" + ch + "' not valid in current alphabet";
        if (alphabet != null) {
            assert alphabet.contains(ch) : message;
        } else {
            assert abcString.indexOf(ch) != -1 : message;
        }
    }
}

enum Alphabet implements Iterable<Character>, CharSequence {

    RU("АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэюя"),
    EN("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"),
    ANY(null) {

        @Override
        public char charAt(int index) {
            return (char) index;
        }

        @Override
        public int length() {
            return MAX_CHAR;
        }

        @Override
        public String toString() {
            return "";
        }

        @Override
        public Iterator<Character> iterator() {
            return new Iterator<Character>() {

                int current = 0;

                @Override
                public boolean hasNext() {
                    return current < MAX_CHAR;
                }

                @Override
                public Character next() {
                    return Character.valueOf((char) current++);
                }

                @Override
                public void remove() {
                    throw new UnsupportedOperationException("Not supported yet.");
                }
            };
        }

        @Override
        public boolean contains(char ch) {
            return true;
        }
    };
    static final int MAX_CHAR = 1 << 16;
    private final String abc;

    private Alphabet(String abc) {
        this.abc = abc;
    }

    @Override
    public String toString() {
        return abc;
    }

    @Override
    public Iterator<Character> iterator() {
        return new Iterator<Character>() {

            int current = 0;

            @Override
            public boolean hasNext() {
                return current < abc.length();
            }

            @Override
            public Character next() {
                return Character.valueOf(abc.charAt(current++));
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
    }

    @Override
    public char charAt(int index) {
        return abc.charAt(index);
    }

    @Override
    public int length() {
        return abc.length();
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean contains(char ch) {
        return abc.indexOf(ch) > -1;
    }
}