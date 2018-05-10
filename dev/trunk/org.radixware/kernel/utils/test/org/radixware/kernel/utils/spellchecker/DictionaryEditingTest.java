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
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import org.radixware.kernel.common.check.spelling.IDictionary;
import org.radixware.kernel.common.check.spelling.Word;


public class DictionaryEditingTest {

    private static final WordGenerator WORD_GENERATOR = new WordGenerator();
    private static final String TEST_DICT = "testdict.txt";
    private static final String EMPTY_DICT = "emptydict.txt";

    
    private static InputStream getDictionaryStream(String filePath) {
        return DictionaryEditingTest.class.getResourceAsStream(filePath);
    }

    private static IDictionary loadDictionary(boolean editable) {
        return null;
//        IDictionary dict = DictionaryLoader.loadLayer(EIsoLanguage.RUSSIAN, getDictionaryStream(TEST_DICT), null);
//
//        return dict;
    }
    
    private static IDictionary loadEmptyDictionary(boolean editable) {
        return null;
//        IDictionary dict = DictionaryLoader.loadLayer(EIsoLanguage.RUSSIAN, getDictionaryStream(EMPTY_DICT), null);
//
//        return dict;
    }

    private static Word[] loadWords() {
        WordReader reader = new WordReader(getDictionaryStream(EMPTY_DICT));
        return reader.readAllWords();
    }

    @Test
    public void loadTest() {
        IDictionary dictionary = loadDictionary(true);

        assertNotNull(dictionary);
        assertTrue(dictionary.wordCount() == 24);
        assertTrue(!dictionary.isEmpty());
        assertTrue(dictionary.isEditable());

        Word[] words = loadWords();
        for (Word word : words) {
            assertTrue(dictionary.containsWord(word));
        }
    }

    @Test
    public void addWordTest() {

        IDictionary dictionary = loadEmptyDictionary(true);

        String[] words = {"center", "color", "cache", "campaign", "cursor", 
            "test1", "test", "test2", "culture", "current", "column", "cubic", "cuckoo"};

        for (String word : words) {
            Word newWord = Word.Factory.newInstance(word);
            dictionary.addWord(newWord);
        }

        for (String word : words) {
            assertTrue(dictionary.containsWord(word));
        }
    }

    @Test
    public void addGeneratedWordTest() {

        for (Alphabet alphabet : new Alphabet[]{Alphabet.EN, Alphabet.RU}) {

            WORD_GENERATOR.setAlphabet(alphabet);
            IDictionary dictionary = loadEmptyDictionary(true);

            List<Word[]> wordList = new ArrayList<Word[]>();

            for (char ch : alphabet) {
                Word[] words = WORD_GENERATOR.generate(ch, 1000, 10, 15);
                wordList.add(words);
                for (Word word : words) {
                    dictionary.addWord(word);
                }
            }

            for (Word[] words : wordList) {
                for (Word word : words) {
                    assertTrue(dictionary.containsWord(word));
                }
            }
        }
    }

    @Test
    public void removeWordTest() {
        for (Alphabet alphabet : new Alphabet[]{Alphabet.EN, Alphabet.RU}) {

            WORD_GENERATOR.setAlphabet(alphabet);
            IDictionary dictionary = loadEmptyDictionary(true);

            List<Word[]> wordList = new ArrayList<Word[]>();

            for (char ch : alphabet) {
                Word[] words = WORD_GENERATOR.generate(ch, 1000, 10, 15);
                wordList.add(words);
                for (Word word : words) {
                    dictionary.addWord(word);
                }
            }

            for (Word[] words : wordList) {
                for (Word word : words) {
                    assertTrue(dictionary.containsWord(word));
                    dictionary.removeWord(word);
                    assertFalse(dictionary.containsWord(word));
                }
            }
        }
    }
}
