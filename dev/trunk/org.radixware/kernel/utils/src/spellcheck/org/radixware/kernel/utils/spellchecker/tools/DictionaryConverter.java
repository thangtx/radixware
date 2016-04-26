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

package org.radixware.kernel.utils.spellchecker.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.utils.CharOperations;
import org.radixware.kernel.common.utils.FileUtils;


class DictionaryConverter {

    List<char[]> words = new LinkedList<char[]>();

    private static boolean matches(char[] str, char[] suffix) {
        if (suffix.length > 0) {
            if (suffix[0] == '.') {
                if (str.length >= suffix.length) {
                    char[] matchSuffix = new char[suffix.length - 1];
                    System.arraycopy(suffix, 1, matchSuffix, 0, suffix.length - 1);
                    return CharOperations.suffixEqualsIgnoreCase(str, matchSuffix);
                }
            }
        }
        return CharOperations.suffixEqualsIgnoreCase(str, suffix);
    }

    public static DictionaryConverter loadDictionary(InputStream stream, String encoding, Affixes affixes) throws IOException {
        try {
            DictionaryConverter dict = new DictionaryConverter();
            String words = FileUtils.readTextStream(stream, encoding);
            String[] wordList = words.split("\n");
            words = null;
            for (String word : wordList) {
                int index = word.indexOf("/");
                if (index < 0) {
                    dict.words.add(word.toCharArray());
                } else {
                    String original = word.substring(0, index);
                    char[] baseWord = original.toCharArray();
                    dict.words.add(baseWord);
                    char[] ruleSet = word.substring(index + 1).toCharArray();
                    for (char liter : ruleSet) {
                        Affixes.Flag f = affixes.getFlag(liter);
                        if (f == null) {
                            throw new RadixError("No flag for liter " + liter);
                        } else {
                            nextRule:
                            for (Affixes.Rule rule : f.rules) {

                                if (matches(baseWord, rule.match)) {
                                    if (rule.list.length > 0) {
                                        int matchChar = baseWord.length - rule.match.length - 1;
                                        if (matchChar < 0) {
                                            throw new RadixError("Negative match char index");
                                        }
                                        if (rule.excludeList) {
                                            for (char c : rule.list) {
                                                if ((Character.toLowerCase(baseWord[matchChar]) == Character.toLowerCase(c))) {
                                                    continue nextRule;
                                                }
                                            }
                                        } else {
                                            boolean found = false;
                                            for (char c : rule.list) {
                                                if ((Character.toLowerCase(baseWord[matchChar]) == Character.toLowerCase(c))) {
                                                    found = true;
                                                    break;
                                                }
                                            }
                                            if (!found) {
                                                continue nextRule;
                                            }
                                        }
                                    }
                                    int trimToLength = baseWord.length;
                                    if (rule.remove.length > 0) {
                                        for (int i = rule.remove.length - 1, j = 1; i >= 0; i--, j++) {
                                            if (Character.toLowerCase(baseWord[baseWord.length - j]) == Character.toLowerCase(rule.remove[i])) {
                                                trimToLength--;
                                            } else {
                                                throw new RadixError("Unexpected replacement");
                                            }
                                        }
                                    }
                                    char[] newWord = new char[trimToLength];

                                    System.arraycopy(baseWord, 0, newWord, 0, trimToLength);

                                    if (rule.append.length > 0) {
                                        char[] appendix = new char[rule.append.length];
                                        if (Character.isLowerCase(newWord[newWord.length - 1])) {
                                            for (int i = 0; i < appendix.length; i++) {
                                                appendix[i] = Character.toLowerCase(rule.append[i]);
                                            }
                                        } else {
                                            System.arraycopy(rule.append, 0, appendix, 0, appendix.length);
                                        }
                                        newWord = CharOperations.merge(newWord, appendix);
                                    }
                                    dict.words.add(newWord);
                                }
                            }
                        }
                    }
                }
            }
            return dict;
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
            }
        }
    }

    public void save(File file, String encoding) throws IOException {
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        FileOutputStream stream = new FileOutputStream(file);
        OutputStreamWriter w = new OutputStreamWriter(stream, encoding);
        boolean first = true;
        for (char[] word : words) {
            if (first) {
                first = false;
            } else {
                w.append('\n');
            }
            w.append(String.valueOf(word));
        }
        try {
        } finally {
            try {
                w.flush();
                w.close();
                stream.flush();
                stream.close();
            } catch (IOException ex) {
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (char[] w : words) {
            sb.append(String.valueOf(w)).append('\n');
        }
        return sb.toString();
    }
}
