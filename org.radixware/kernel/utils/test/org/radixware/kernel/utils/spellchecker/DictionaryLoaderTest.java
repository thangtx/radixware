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
import static org.junit.Assert.*;
import org.junit.Test;
import org.radixware.kernel.common.check.spelling.CommonDictionary;
import org.radixware.kernel.common.check.spelling.IDictionary;
import org.radixware.kernel.common.check.spelling.IDictionaryDataProvider;
import org.radixware.kernel.common.check.spelling.Word;
import org.radixware.kernel.common.enums.EIsoLanguage;
import static org.radixware.kernel.utils.spellchecker.DictionaryTestUtils.*;
import org.radixware.schemas.dictionary.Dictionary;
import org.radixware.schemas.dictionary.Dictionary.Comments.Comment;
import org.radixware.schemas.dictionary.DictionaryDocument;

public class DictionaryLoaderTest {

    final static String DICT_FILE_NAME = "dictionary.xml";
    final static String UNSORT_DICT_FILE_NAME = "unsortdict.xml";
    final static EIsoLanguage LANGUAGE = EIsoLanguage.ENGLISH;

    @Test
    public void xsdRead() throws Exception {
        InputStream stream = getInputStream(DICT_FILE_NAME);
        DictionaryDocument dictionaryDocument = DictionaryDocument.Factory.parse(stream);

        Dictionary dictionary = dictionaryDocument.getDictionary();

        String language = dictionary.getLanguage();
        List words = dictionary.getWords();
        Dictionary.Comments comments = dictionary.getComments();

        assertNotNull(dictionary);
        assertNotNull(language);
        assertNotNull(words);
        assertNotNull(comments);
    }

    @Test
    public void xsdWrite() throws Exception {
        DictionaryDocument dictionaryDocument = DictionaryDocument.Factory.newInstance();

        Dictionary dictionary = dictionaryDocument.addNewDictionary();

        String language = "ru";

        dictionary.setLanguage(language);
        dictionary.setWords(new ArrayList<String>());
        Comment comment = dictionary.addNewComments().addNewComment();
        comment.setKey("word1");
        comment.setStringValue("text comment");

        String xml = dictionaryDocument.xmlText();
        System.out.println(xml);
    }

    @Test
    public void fullLoadLayer() {

        LayerDictionary dictionary = fullLoadLayerDictionary(LANGUAGE, DICT_FILE_NAME);

        assertNotNull(dictionary);
        containsWordTest(dictionary);

        String comment = dictionary.getComment(Word.Factory.newInstance("word1"));
        assertNotNull(comment);

        System.out.println(comment);
    }

    @Test
    public void loadLayer() {

        LayerDictionary dictionary = fastLoadLayerDictionary(LANGUAGE, DICT_FILE_NAME);

        assertNotNull(dictionary);
        containsWordTest(dictionary);

        String comment = dictionary.getComment(Word.Factory.newInstance("word1"));
        assertTrue(comment == null);
    }

    @Test
    public void mixedLoad() {

        IDictionaryDataProvider wordsProvider = new LayerDictionaryWordsDataProvider(getStreamProvider(DICT_FILE_NAME));

        LayerDictionary dictionary = new LayerDictionary(LANGUAGE, wordsProvider, null);
        assertNotNull(dictionary);
        containsWordTest(dictionary);
        String comment = dictionary.getComment(Word.Factory.newInstance("word1"));
        assertTrue(comment == null);


        IDictionaryDataProvider fullProvider = new LayerDictionaryFullDataProvider(getStreamProvider(DICT_FILE_NAME));
        dictionary.setDataProvider(fullProvider, true);
        containsWordTest(dictionary);
        comment = dictionary.getComment(Word.Factory.newInstance("word1"));
        assertTrue(comment != null);
        System.out.println(comment);


        dictionary.setDataProvider(wordsProvider, true);
        containsWordTest(dictionary);
        comment = dictionary.getComment(Word.Factory.newInstance("word1"));
        assertTrue(comment == null);
    }

    private void containsWordTest(IDictionary dictionary) {
        String[] words = { "word1", "word2", "word3" };

        for (String word : words) {
            assertTrue(dictionary.containsWord(word));
        }
    }

    @Test
    public void loadCommon() {

        IDictionary dictionary = DictionaryLoader.loadCommonDictionary(LANGUAGE);

        assertNotNull(dictionary);
        assertTrue(dictionary.containsWord("test"));
    }

    @Test
    public void loadUnsortDict() {

        System.out.println(">>>>>>>> loadUnsortLoad");

        LayerDictionary dictionary = fullLoadLayerDictionary(EIsoLanguage.RUSSIAN, UNSORT_DICT_FILE_NAME);

        assertNotNull(dictionary);
        containsWordTest(dictionary);

        assertTrue(dictionary.containsWord("ooo"));
        assertTrue(dictionary.containsWord("uuu"));
        assertTrue(dictionary.containsWord("ccc"));

        String comment = dictionary.getComment(Word.Factory.newInstance("word1"));
        assertNotNull(comment);

        System.out.println(comment);
    }

    @Test
    public void notFoundDictionaryTest() {
        CommonDictionary loadCommonDictionary = DictionaryLoader.loadCommonDictionary(EIsoLanguage.AFAR);
        assertFalse(loadCommonDictionary.containsWord("test"));

        loadCommonDictionary = DictionaryLoader.loadCommonDictionary(null);
        assertFalse(loadCommonDictionary.containsWord("test"));
    }
}
