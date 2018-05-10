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
 * 11/23/11 4:04 PM
 */
package org.radixware.kernel.utils.spellchecker;

import org.junit.Test;
import org.radixware.kernel.common.check.spelling.CharMap;
import org.radixware.kernel.common.check.spelling.IDictionary;
import org.radixware.kernel.common.check.spelling.Word;
import org.radixware.kernel.common.enums.EIsoLanguage;
import static org.junit.Assert.*;


public class LayerDictionaryTest {

    private static final String abcRussian = "АаБбВвГгДдЕеЁёЖжЗзИиЙйКкЛлМмНнОоПпРрСсТтУуФфХхЦцЧчШшЩщЪъЫыЬьЭэЮюЯя";

    CharMap<String> createCharMap(String[] words) {
        CharMap<String> wordMap = CharMap.Factory.<String>newInstance();

        for (String word : words) {
            wordMap.put(word.charAt(0), word);
        }
        return wordMap;
    }

    @Test
    public void baseTest() {

        String[] words = {
            "Азия", "бор", "восток", "гром", "дом", "еда", "ёж", "жизнь",
            "змея", "исток", "йод", "карма", "лимон", "мир", "ночь", "орел", "продукт",
            "Россия", "стандарт", "турист", "успех", "фортуна", "хобот",
            "цапля", "чушь", "шелк", "щупалец", "эстетика", "юг", "я"
        };

        CharMap<String> wordMap = createCharMap(words);

        IDictionary dictionary = DictionaryLoader.loadCommonDictionary(EIsoLanguage.RUSSIAN);
        assertNotNull(dictionary);

        for (char ch : wordMap.getKeyArray()) {
            Word word = Word.Factory.newInstance(wordMap.get(ch));

            assertTrue(word.toString(), dictionary.containsWord(wordMap.get(ch)));

            assertSame(word.toString(), Spellchecker.checkSingleWord(EIsoLanguage.RUSSIAN, word), Spellchecker.Validity.VALID);
        }

        assertFalse(dictionary.containsWord("Ыролро"));
        assertFalse(dictionary.containsWord("ьапрпар"));
        assertFalse(dictionary.containsWord("ъапрпар"));
        assertFalse(dictionary.containsWord("vnvbn"));
    }

    @Test
    public void test() {
        IDictionary dictionary = DictionaryLoader.loadCommonDictionary(EIsoLanguage.RUSSIAN);
        assertNotNull(dictionary);

        String[] words = {"эстетика"};

        CharMap<String> wordMap = createCharMap(words);
        
        for (char ch : wordMap.getKeyArray()) {
            Word word = Word.Factory.newInstance(wordMap.get(ch));

            assertTrue(word.toString(), dictionary.containsWord(wordMap.get(ch)));
        }
    }
}
