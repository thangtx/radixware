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

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Test;
import org.radixware.kernel.common.check.spelling.Word;


public class WordReaderTest {

    private WordReader createReader() {
        return new WordReader(getClass().getResourceAsStream("dictionary.txt"));
    }

    private List<String> getLines() throws FileNotFoundException, IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("dictionary.txt")))) {
            String line = reader.readLine();
            while (line != null) {
                if (!line.trim().isEmpty()) {
                    lines.add(line);
                }
                line = reader.readLine();
            }
        }
        return lines;
    }

    @Test
    public void wordReaderTest() throws Exception {

        List<String> lines = getLines();

        int wordCount = 0;
        try (WordReader reader = createReader()) {

            String strWord = null;
            Word word = null;
            Word testWord = null;

            do {
                if (wordCount < lines.size()) {
                    strWord = lines.get(wordCount);
                    testWord = Word.Factory.newInstance(strWord);
                }
                word = reader.readWord();

                if (word == null || testWord == null) {
                    break;
                }

                assertEquals(word, testWord);
                assertEquals(word.toString(), strWord);
                assertEquals(testWord.toString(), strWord);

                ++wordCount;
                assertTrue(wordCount <= lines.size());

            } while (word != null);
        }

        assertTrue(wordCount == lines.size());


    }

    @Test
    public void allWordsReadTest() throws Exception {
        try (WordReader reader = createReader()) {
            List<String> lines = getLines();

            Word[] words = reader.readAllWords();

            for (int i = 0; i < words.length; ++i) {
                assertSame(words[i].compareTo(lines.get(i)), 0);
            }
        }
    }
}
