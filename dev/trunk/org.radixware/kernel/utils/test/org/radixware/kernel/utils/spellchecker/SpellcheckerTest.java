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

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.radixware.kernel.common.check.spelling.Word;
import static org.junit.Assert.*;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.utils.spellchecker.Spellchecker.Validity;


public class SpellcheckerTest {

    public SpellcheckerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of checkSingleWord method, of class Spell checker.
     */
    @Test
    public void testCheckSingleWord() {
        EIsoLanguage lang = EIsoLanguage.RUSSIAN;
        String[] words = new String[]{
            "Вася",
            "Вялая",
            "запущена",
            "ёжик",
            "самый",
            "определен",
            "Ид."
        };
        for (String word : words) {
            Validity result = Spellchecker.checkSingleWord(lang, Word.Factory.newInstance(word));
            assertEquals(word, Validity.VALID, result);
        }
        lang = EIsoLanguage.ENGLISH;
        words = new String[]{
                    "Change",
                    "new",
                    "keystore", "wasn't"};
        for (String word : words) {
            Validity result = Spellchecker.checkSingleWord(lang, Word.Factory.newInstance(word));
            assertEquals(word, Validity.VALID, result);
        }

        SimpleTokenizer tokenizer = new SimpleTokenizer("This method wasn't checked for errors");
        String s;
        while ((s = tokenizer.nextWord()) != null) {
            Validity result = Spellchecker.checkSingleWord(lang, Word.Factory.newInstance(s));
            assertEquals(s, Validity.VALID, result);
        }
    }
}
