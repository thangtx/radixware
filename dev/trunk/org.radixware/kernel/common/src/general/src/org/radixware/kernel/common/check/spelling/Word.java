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

/*
 * 12/2/11 11:56 AM
 */
package org.radixware.kernel.common.check.spelling;

import java.util.Arrays;
import org.radixware.kernel.common.utils.CharOperations;


public final class Word implements CharSequence, Comparable<CharSequence> {

    public static final class Factory {

        private Factory() {
        }

        public static Word newInstance(String word) {
            if (word != null && !word.isEmpty()) {
                return new Word(word);
            }
            return null;
        }

        public static Word newInstance(CharSequence sequence) {
            if (sequence != null && 0 < sequence.length()) {
                return new Word(sequence);
            }
            return null;
        }

        public static Word newInstance(Word word) {
            if (word != null) {
                return new Word(word);
            }
            return null;
        }

        public static Word newInstance(char[] chars) {
            if (chars != null && 0 < chars.length) {
                return new Word(chars);
            }
            return null;
        }

        public static Word newInstance(char[] chars, int start, int len) {
            if (chars != null && 0 < chars.length
                && 0 < start && start < chars.length && 0 < len) {
                return new Word(chars, start, len);
            }
            return null;
        }
    }
    private final char[] chars;
    private final int length;
    private final int hash;

    private int getHash() {
        return 67 * 7 + Arrays.hashCode(this.chars);
    }

    private Word(char[] chars, int offset, int len) {
        this.chars = new char[len];
        System.arraycopy(chars, offset, this.chars, 0, len);

        this.length = len;

        hash = getHash();
    }

    private Word(char[] chars) {
        this(chars, 0, chars.length);
    }

    private Word(Word word) {
        this(word.chars);
    }

    private Word(String word) {
        this.chars = word.toCharArray();
        this.length = word.length();

        hash = getHash();
    }

    private Word(CharSequence sequence) {
        this.length = sequence.length();
        this.chars = new char[this.length];
        for (int index = 0; index < this.length; ++index) {
            this.chars[index] = sequence.charAt(index);
        }

        hash = getHash();
    }

    @Override
    public int compareTo(CharSequence sequence) {
        int len1 = length(), len2 = sequence.length(),
            minLen = Math.min(len1, len2);

        for (int index = 0; index < minLen; ++index) {
            char ch1 = charAt(index),
                ch2 = sequence.charAt(index);
            int compare = ch1 - ch2;
            if (compare != 0) {
                return compare;
            }
        }
        return len1 - len2;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Word) {
            Word word = (Word) obj;
            int len1 = length(), len2 = word.length();

            if (len1 == len2) {
                for (int i = 0; i < len1; ++i) {
                    if (charAt(i) != word.charAt(i)) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return hash;
    }

    @Override
    public int length() {
        return length;
    }

    @Override
    public char charAt(int index) {

        if (index < 0 || index >= length) {
            throw new IndexOutOfBoundsException("Char index out of range: " + index);
        }
        return chars[index];
    }

    @Override
    public Word subSequence(int start, int end) {

        if (end > length) {
            end = length;
        }

        final int len = end - start;
        if (len < 0) {
            throw new IllegalArgumentException("Start index should be lesser than end index");
        }

        if (start < 0) {
            throw new IndexOutOfBoundsException("Char index out of word length: " + start);
        }

        char[] subWord = new char[len];

        System.arraycopy(chars, start, subWord, 0, len);

        return new Word(subWord);
    }

    @Override
    public String toString() {
        return String.valueOf(chars);
    }
    
    public boolean isEmpty() {
        return length() == 0;
    }

    public char[] getChars() {
        return chars;
    }

    public int compareIgnoreCaseTo(CharSequence sequence) {
        int len1 = length(), len2 = sequence.length(),
            minLen = Math.min(len1, len2);

        for (int index = 0; index < minLen; ++index) {
            char ch1 = Character.toLowerCase(charAt(index)),
                ch2 = Character.toLowerCase(sequence.charAt(index));
            int compare = ch1 - ch2;
            if (compare != 0) {
                return compare;
            }
        }
        return len1 - len2;
    }

    public boolean equalsIgnoreCase(Word word) {
        if (word == this) {
            return true;
        }
        int len1 = length(), len2 = word.length();

        if (len1 == len2) {
            for (int i = 0; i < len1; ++i) {
                char ch1 = Character.toLowerCase(charAt(i)),
                    ch2 = Character.toLowerCase(word.charAt(i));
                if (ch1 != ch2) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public Word toLowerCase() {
        return new Word(CharOperations.toLowerCase(chars));
    }

    public boolean startWith(CharSequence prefix) {
        int lenWord = length(), lenPref = prefix.length();

        if (lenWord < lenPref) {
            return false;
        }
        
        for (int index = 0; index < lenPref; ++index) {
            char ch1 = charAt(index),
                ch2 = prefix.charAt(index);
            if (ch1 != ch2) {
                return false;
            }
        }
        return true;
    }
}
