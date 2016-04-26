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

package org.radixware.kernel.common.utils;

/**
 * Utility class providing several operations on character arrays
 *
 */
public final class CharOperations {

    public static final char[] empty_array = new char[0];
    public static final char[][] empty_arrays = new char[0][0];

    private static final char[] clone(char[] arr) {
        char[] copy = new char[arr.length];
        System.arraycopy(arr, 0, copy, 0, copy.length);
        return copy;
    }

    /**
     * Returns concatenation of first and second if both first and second is
     * null empty array will return if one of given arrays is null copy of
     * another array will return
     */
    public static final char[] merge(char[] first, char[] second) {
        if (first == null) {
            if (second == null) {
                return empty_array;
            } else {
                return clone(second);
            }
        } else if (second == null) {
            if (first == null) {
                return empty_array;
            } else {
                return clone(first);
            }
        } else {
            char[] result = new char[first.length + second.length];
            System.arraycopy(first, 0, result, 0, first.length);
            System.arraycopy(second, 0, result, first.length, second.length);
            return result;
        }
    }

    public static boolean suffixEquals(char[] text, char[] suffix) {
        if (suffix.length > text.length) {
            return false;
        }
        for (int i = text.length - 1, j = suffix.length - 1; j >= 0; j--, i--) {
            if (text[i] != suffix[j]) {
                return false;
            }
        }
        return true;
    }

    public static boolean suffixEqualsIgnoreCase(char[] text, char[] suffix) {
        if (suffix.length > text.length) {
            return false;
        }
        for (int i = text.length - 1, j = suffix.length - 1; j >= 0; j--, i--) {
            if (Character.toLowerCase(text[i]) != Character.toLowerCase(suffix[j])) {
                return false;
            }
        }
        return true;
    }

    public static final char[] merge(char[] first, char[] second, char delimiter) {
        if (first == null) {
            if (second == null) {
                return empty_array;
            } else {
                return clone(second);
            }
        } else if (second == null) {
            if (first == null) {
                return empty_array;
            } else {
                return clone(first);
            }
        } else {
            char[] result = new char[first.length + second.length + 1];
            System.arraycopy(first, 0, result, 0, first.length);
            result[first.length] = delimiter;
            System.arraycopy(second, 0, result, first.length + 1, second.length);
            return result;
        }
    }

    /**
     * Returns char array wich is concatenation of elements of array arr with
     * given delimiter if given arr is null empty array will return Null entries
     * of given array is ignored Example:<br> merge ab c de with . -> ab.c.de
     * merge ab <null> de with . -> ab.de
     */
    public static final char[] merge(char[][] arr, char delimiter) {
        if (arr == null) {
            return empty_array;
        }
        int resultLength = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] != null) {
                resultLength += arr[i].length + 1;
            }
        }
        if (resultLength == 0) {
            return empty_array;
        }

        resultLength--;

        char[] result = new char[resultLength];
        int pos = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] != null) {
                System.arraycopy(arr[i], 0, result, pos, arr[i].length);
                pos += arr[i].length;
                if (i < arr.length - 1) {
                    result[pos] = delimiter;
                    pos++;
                }
            }
        }
        return result;
    }

    /**
     * Returns char array wich is concatenation of 'length' elements from index
     * 'from' of array arr with given delimiter if given arr is null empty array
     * will return Null entries of given array is ignored Example:<br> merge ab
     * c de with . -> ab.c.de merge ab <null> de with . -> ab.de
     */
    public static final char[] merge(char[][] arr, char delimiter, int from, int length) {
        if (arr == null || from < 0 || from >= arr.length) {
            return empty_array;
        }
        int resultLength = 0;
        int maxLen = Math.min(from + length, arr.length);

        for (int i = from; i < maxLen; i++) {
            if (arr[i] != null) {
                resultLength += arr[i].length + 1;
            }
        }
        if (resultLength == 0) {
            return empty_array;
        }

        resultLength--;

        char[] result = new char[resultLength];
        int pos = 0;
        for (int i = from; i < maxLen; i++) {
            if (arr[i] != null) {
                System.arraycopy(arr[i], 0, result, pos, arr[i].length);
                pos += arr[i].length;
                if (i < maxLen - 1) {
                    result[pos] = delimiter;
                    pos++;
                }
            }
        }
        return result;
    }

    public static int reverseFind(char[] source, char[] name, int from) {
        return reverseFind(source, name, from, 0);
    }

    public static int reverseFind(char[] source, char[] name, int from, int to) {
        if (from >= source.length) {
            return -1;
        }
        if (to < 0 || to >= from) {
            return -1;
        }
        int minLen = to + name.length - 1;
        int pos = from;
        while (pos >= minLen) {
            int local = pos;
            for (int i = name.length - 1; i >= 0; i--) {
                if (source[local] != name[i]) {
                    local--;
                    break;
                }

                if (i == 0) {
                    return local;
                } else {
                    local--;
                }
            }
            pos = local;
        }
        return -1;
    }

    public static boolean equals(char[] a1, char[] a2) {
        if (a1 == a2) {
            return true;
        }
        if (a1.length != a2.length) {
            return false;
        }
        for (int i = 0, len = a1.length; i < len; i++) {
            if (a1[i] != a2[i]) {
                return false;
            }
        }
        return true;
    }

    public static int hashCode(char[] array) {
        int length = array.length;
        int hash = length == 0 ? 31 : array[0];
        if (length < 8) {
            for (int i = length; --i > 0;) {
                hash = (hash * 31) + array[i];
            }
        } else {
            // 8 characters is enough to compute a decent hash code, don't waste
            // time examining every character
            for (int i = length - 1, last = i > 16 ? i - 16 : 0; i > last; i -= 2) {
                hash = (hash * 31) + array[i];
            }
        }
        return hash & 0x7FFFFFFF;
    }

    public static int indexOf(char toBeFound, char[] array) {
        for (int i = 0; i < array.length; i++) {
            if (toBeFound == array[i]) {
                return i;
            }
        }
        return -1;
    }

    public static int replace(char what, char with, char[] array) {
        int count = 0;
        for (int i = 0; i < array.length; i++) {
            if (what == array[i]) {
                array[i] = with;
                count++;
            }
        }
        return count;
    }

    public static int indexOf(char toBeFound, char[] array, int start) {
        for (int i = start; i < array.length; i++) {
            if (toBeFound == array[i]) {
                return i;
            }
        }
        return -1;
    }

    public static char[][] subarray(char[][] array, int start, int end) {
        if (end == -1) {
            end = array.length;
        }
        if (start > end) {
            return null;
        }
        if (start < 0) {
            return null;
        }
        if (end > array.length) {
            return null;
        }

        char[][] result = new char[end - start][];
        System.arraycopy(array, start, result, 0, end - start);
        return result;
    }

    public static final char[] subarray(char[] array, int start, int end) {
        if (end == -1) {
            end = array.length;
        }
        if (start > end) {
            return null;
        }
        if (start < 0) {
            return null;
        }
        if (end > array.length) {
            return null;
        }

        char[] result = new char[end - start];
        System.arraycopy(array, start, result, 0, end - start);
        return result;
    }

    public static char[][] splitOn(char divider, char[] array) {
        int length = array == null ? 0 : array.length;
        if (length == 0) {
            return empty_arrays;
        }

        int wordCount = 1;
        for (int i = 0; i < length; i++) {
            if (array[i] == divider) {
                wordCount++;
            }
        }
        char[][] split = new char[wordCount][];
        int last = 0, currentWord = 0;
        for (int i = 0; i < length; i++) {
            if (array[i] == divider) {
                split[currentWord] = new char[i - last];
                System.arraycopy(array, last, split[currentWord++], 0, i
                        - last);
                last = i + 1;
            }
        }
        split[currentWord] = new char[length - last];
        System.arraycopy(array, last, split[currentWord], 0, length - last);
        return split;
    }

    public static char[][] splitOn(char divider, char[] array, int start,
            int end) {
        int length = array == null ? 0 : array.length;
        if (length == 0 || start > end) {
            return empty_arrays;
        }

        int wordCount = 1;
        for (int i = start; i < end; i++) {
            if (array[i] == divider) {
                wordCount++;
            }
        }
        char[][] split = new char[wordCount][];
        int last = start, currentWord = 0;
        for (int i = start; i < end; i++) {
            if (array[i] == divider) {
                split[currentWord] = new char[i - last];
                System.arraycopy(array, last, split[currentWord++], 0, i
                        - last);
                last = i + 1;
            }
        }
        split[currentWord] = new char[end - last];
        System.arraycopy(array, last, split[currentWord], 0, end - last);
        return split;
    }

    public static boolean equals(char[] first, char[] second,
            int secondStart, int secondEnd) {
        if (first == second) {
            return true;
        }
        if (first == null || second == null) {
            return false;
        }
        if (first.length != secondEnd - secondStart) {
            return false;
        }

        for (int i = first.length; --i >= 0;) {
            if (first[i] != second[i + secondStart]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Compare two char's sequences.
     */
    public static int compareSequence(char[] first, char[] second) {
        if (Utils.equals(first, second)) {
            return 0;
        }

        if (first == null) {
            return -1;
        }

        if (second == null) {
            return 1;
        }

        int len1 = first.length, len2 = second.length,
                minLen = Math.min(len1, len2);

        for (int index = 0; index < minLen; ++index) {
            int compare = first[index] - second[index];
            if (compare != 0) {
                return compare;
            }
        }
        return len1 - len2;
    }

    /**
     * Insensitive comparison of two char's sequences.
     */
    public static int compareInsensitiveSequence(char[] first, char[] second) {
        if (Utils.equals(first, second)) {
            return 0;
        }

        if (first == null) {
            return -1;
        }

        if (second == null) {
            return 1;
        }

        int len1 = first.length, len2 = second.length,
                minLen = Math.min(len1, len2);

        for (int index = 0; index < minLen; ++index) {
            int compare = Character.toLowerCase(first[index]) - Character.toLowerCase(second[index]);
            if (compare != 0) {
                return compare;
            }
        }
        return len1 - len2;
    }

    public static char[] toLowerCase(char[] chars) {
        if (chars == null) {
            return null;
        }

        char[] lowerCaseChars = new char[chars.length];

        for (int index = 0; index < chars.length; ++index) {
            lowerCaseChars[index] = Character.toLowerCase(chars[index]);
        }
        return lowerCaseChars;
    }

    public static char[] toUpperCase(char[] chars) {
        if (chars == null) {
            return null;
        }

        char[] upperCaseChars = new char[chars.length];

        for (int index = 0; index < chars.length; ++index) {
            upperCaseChars[index] = Character.toUpperCase(chars[index]);
        }
        return upperCaseChars;
    }

    public static boolean endsWith(CharSequence source, CharSequence suffix) {
        return startsWith(source, suffix, source.length() - suffix.length());
    }

    public static boolean startsWith(CharSequence source, CharSequence prefix) {
        return startsWith(source, prefix, 0);
    }

    public static boolean startsWith(CharSequence source, CharSequence prefix, int offset) {
        int wordIndex = offset;
        int prefIndex = 0;
        int prefLen = prefix.length();

        if ((offset < 0) || (offset > source.length() - prefLen)) {
            return false;
        }
        while (--prefLen >= 0) {
            if (source.charAt(wordIndex++) != prefix.charAt(prefIndex++)) {
                return false;
            }
        }
        return true;
    }

    public static boolean startsWith(char[] source, char[] prefix, int offset) {
        int wordIndex = offset;
        int prefIndex = 0;
        int prefLen = prefix.length;

        if ((offset < 0) || (offset > source.length - prefLen)) {
            return false;
        }
        while (--prefLen >= 0) {
            if (source[wordIndex++] != prefix[prefIndex++]) {
                return false;
            }
        }
        return true;
    }

    public static int indexOf(CharSequence source, CharSequence sequence) {
        return indexOf(source, sequence, 0);
    }

    public static int indexOf(CharSequence source, CharSequence sequence, int from) {

        int wordLen = source.length();
        if (from < 0 || from >= wordLen) {
            // or if (from < 0) from = 0; else return -1;
            throw new IndexOutOfBoundsException("Char index out of sequance range");
        }

        int seqIndex,
                wordIndex = from,
                seqLen = sequence.length(),
                limit = wordLen - seqLen,
                startComp;

        start_serach:
        while (wordIndex <= limit) {
            if (sequence.charAt(0) == source.charAt(wordIndex++)) {
                startComp = wordIndex;
                for (seqIndex = 1; seqIndex < seqLen; ++seqIndex) {
                    if (sequence.charAt(seqIndex) != source.charAt(wordIndex++)) {
                        wordIndex = startComp;
                        continue start_serach;
                    }
                }
                return wordIndex - seqLen;
            }
        }
        return -1;
    }

    public static int lastIndexOf(CharSequence source, CharSequence sequence) {
        return lastIndexOf(source, sequence, source.length() - 1);
    }

    public static int lastIndexOf(CharSequence source, CharSequence sequence, int from) {

        int wordLen = source.length();
        if (from < 0 || from >= wordLen) {
            throw new IndexOutOfBoundsException("Char index out of sequance range");
        }

        int seqLen = sequence.length(),
                startSeqIndex = seqLen - 1,
                seqIndex = startSeqIndex,
                wordIndex = from,
                limit = seqLen - 1,
                startComp;

        start_serach:
        while (wordIndex >= limit) {
            if (sequence.charAt(startSeqIndex) == source.charAt(wordIndex--)) {
                startComp = wordIndex;
                for (seqIndex = startSeqIndex - 1; seqIndex >= 0; --seqIndex) {
                    if (sequence.charAt(seqIndex) != source.charAt(wordIndex--)) {
                        wordIndex = startComp;
                        continue start_serach;
                    }
                }
                return wordIndex + 1;
            }
        }
        return -1;
    }
}
