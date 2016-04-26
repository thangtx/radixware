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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.apache.xmlbeans.XmlException;
import org.radixware.kernel.common.check.spelling.*;
import org.radixware.kernel.common.defs.RadixObject.EEditState;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.uds.IRepositoryUdsSegment;
import org.radixware.kernel.common.repository.uds.UdsSegment;
import org.radixware.schemas.dictionary.Dictionary;
import org.radixware.schemas.dictionary.Dictionary.Comments;
import org.radixware.schemas.dictionary.Dictionary.Comments.Comment;
import org.radixware.schemas.dictionary.DictionaryDocument;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


class DictionaryLoader {

    static File getLayerFile(EIsoLanguage language, Layer layer) {
        UdsSegment segment = (UdsSegment) layer.getUds();
        File userDictionaryFile = ((IRepositoryUdsSegment) segment.getRepository()).getLayerDictionaryFile(language);
        return userDictionaryFile;
    }

    static IProvider<InputStream> getLayerInputStreamProvider(final EIsoLanguage language, Layer layer) {

        UdsSegment segment = (UdsSegment) layer.getUds();
        final IRepositoryUdsSegment repository = (IRepositoryUdsSegment) segment.getRepository();

        return new IProvider<InputStream>() {

            @Override
            public InputStream provide() {
                if (repository != null) {
                    return repository.getLayerDictionaryData(language);
                }
                return null;
            }
        };
    }

    static IProvider<InputStream> getCommonInputStreamProvider(EIsoLanguage language) {
        IProvider<InputStream> provider;
        if (language == null) {
            provider = new IProvider<InputStream>() {

                @Override
                public InputStream provide() {
                    return DictionaryLoader.class.getResourceAsStream("dict/" + "rdx-abb.dic");
                }
            };
        } else {
            final String lang = language.getValue();
            provider = new IProvider<InputStream>() {

                @Override
                public InputStream provide() {
                    return DictionaryLoader.class.getResourceAsStream("dict/" + lang + ".dic");
                }
            };
        }
        return provider;
    }

    private static IDictionaryDataProvider getCommonDictionaryDataProvider(EIsoLanguage language) {
        IProvider<InputStream> commonInputStreamProvider = getCommonInputStreamProvider(language);
        IDictionaryDataProvider dataProvider = new CommonDictionaryDataProvider(commonInputStreamProvider);
        return dataProvider;
    }

    public static IDictionaryDataProvider getLayerDictionaryDataProvider(EIsoLanguage language, Layer layer) {
        if (layer == null) {
            return null;
        }

        IProvider<InputStream> streamProvider = getLayerInputStreamProvider(language, layer);
        IDictionaryDataProvider dataProvider = new LayerDictionaryWordsDataProvider(streamProvider);
        return dataProvider;
    }

    public static IDictionaryDataProvider getLayerDictionaryFullDataProvider(EIsoLanguage language, Layer layer) {
        if (layer == null) {
            return null;
        }

        IProvider<InputStream> streamProvider = getLayerInputStreamProvider(language, layer);
        IDictionaryDataProvider dataProvider = new LayerDictionaryFullDataProvider(streamProvider);
        return dataProvider;
    }

    public static CommonDictionary loadCommonDictionary(EIsoLanguage language) {

        IDictionaryDataProvider dataProvider = getCommonDictionaryDataProvider(language);

        if (dataProvider != null) {
            CommonDictionary dictionary = new CommonDictionary(language, dataProvider);
            return dictionary;
        }
        return null;
    }

    public static LayerDictionary loadLayerDictionary(EIsoLanguage language, Layer layer) {
        IDictionaryDataProvider dataProvider = getLayerDictionaryDataProvider(language, layer);

        if (dataProvider != null) {
            LayerDictionary dictionary = new LayerDictionary(language, dataProvider, layer);
            dictionary.setEditState(EEditState.NONE);
            return dictionary;
        }
        return null;
    }

    public static LayerDictionary fullLoadLayerDictionary(EIsoLanguage language, Layer layer) {

        IDictionaryDataProvider dataProvider = getLayerDictionaryFullDataProvider(language, layer);

        if (dataProvider != null) {
            LayerDictionary dictionary = new LayerDictionary(language, dataProvider, layer);
            dictionary.setEditState(EEditState.NONE);
            return dictionary;
        }
        return null;
    }
}

class LayerDictionaryFullDataProvider implements IDictionaryDataProvider {

    private IProvider<InputStream> streamProvider;

    public LayerDictionaryFullDataProvider(IProvider<InputStream> streamProvider) {
        this.streamProvider = streamProvider;
    }

    @Override
    public Data provide() {

        final InputStream stream = streamProvider.provide();
        final DictionaryDocument dictionaryDocument;
        try {
            dictionaryDocument = DictionaryDocument.Factory.parse(stream);
        } catch (XmlException | IOException ex) {
            return null;
        }

        Dictionary dictionary = dictionaryDocument != null
                ? dictionaryDocument.getDictionary() : null;

        if (dictionary == null) {
            return null;
        }

        final CharMap<List<Word>> wordMap = CharMap.Factory.newInsensitiveInstance(CharMap.CharCase.LOWER);
        final List wordList = dictionary.getWords();
        if (wordList != null) {
            for (Object wordObject : wordList) {
                String word = (String) wordObject;
                char ch = word.charAt(0);

                List<Word> list = wordMap.get(ch);
                if (list == null) {
                    list = new LinkedList<>();
                    wordMap.put(ch, list);
                }
                list.add(Word.Factory.newInstance(word));
            }
        }

        final Map<String, String> commentaries = new HashMap<>();
        final Comments comments = dictionary.getComments();
        if (comments != null) {
            List<Comment> commentList = comments.getCommentList();

            for (Comment comment : commentList) {
                commentaries.put(comment.getKey(), comment.getStringValue());
            }
        }

        return new IDictionaryDataProvider.Data(wordMap, commentaries);
    }
}

class CommonDictionaryDataProvider implements IDictionaryDataProvider {

    private IProvider<InputStream> provider;

    public CommonDictionaryDataProvider(IProvider<InputStream> provider) {
        this.provider = provider;
    }

    @Override
    public Data provide() {

        CharMap<List<Word>> wordMap = CharMap.Factory.<List<Word>>newInsensitiveInstance(CharMap.CharCase.LOWER);
        InputStream stream = provider.provide();

        if (stream == null) {
            return null;
        }

        Word word;
        try (WordReader reader = new WordReader(stream)) {
            while ((word = reader.readWord()) != null) {
                char ch = word.charAt(0);

                List<Word> list = wordMap.get(ch);
                if (list == null) {
                    list = new LinkedList<>();
                    wordMap.put(ch, list);
                }
                list.add(word);
            }
            return new IDictionaryDataProvider.Data(wordMap, null);
        }
    }
}

class LayerDictionaryWordsDataProvider implements IDictionaryDataProvider {

    private static final class WordsHandler extends DefaultHandler {

        private final CharMap<List<Word>> wordMap;
        private boolean inWordsSection = false;

        public WordsHandler(CharMap<List<Word>> wordMap) {
            this.wordMap = wordMap;
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            if (inWordsSection) {
                loadWordList(ch, start, length);
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if ("Words".equals(qName)) {
                inWordsSection = false;
            }
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            if ("Words".equals(qName)) {
                inWordsSection = true;
            }
        }

        private void loadWordList(char[] chars, int start, int length) {

            int index = start, finish = start + length;
            int startWordIndex = start, wordLen = 0;

            while (index < finish) {
                if (Character.isWhitespace(chars[index])) {
                    if (startWordIndex < index) {
                        wordLen = index - startWordIndex;

                        Word word = Word.Factory.newInstance(chars, startWordIndex, wordLen);
                        addWordToMap(getWordMap(), word);
                    }

                    do {
                        ++index;
                    } while (Character.isWhitespace(chars[index]) && index < finish);
                    startWordIndex = index;
                } else {
                    ++index;
                }
            }

            if (startWordIndex < index) {
                wordLen = index - startWordIndex;
                Word word = Word.Factory.newInstance(chars, startWordIndex, wordLen);
                addWordToMap(getWordMap(), word);
            }
        }

        private void addWordToMap(CharMap<List<Word>> wordMap, Word word) {
            char startChar = word.charAt(0);
            List<Word> list = wordMap.get(startChar);

            if (list == null) {
                list = new LinkedList<>();
                wordMap.put(startChar, list);
            }

            list.add(word);
        }

        public CharMap<List<Word>> getWordMap() {
            return wordMap;
        }
    }
    private IProvider<InputStream> provider;

    public LayerDictionaryWordsDataProvider(IProvider<InputStream> provider) {
        this.provider = provider;
    }

    @Override
    public Data provide() {
        InputStream stream = provider.provide();
        if (stream == null) {
            return null;
        }

        CharMap<List<Word>> wordMap = CharMap.Factory.<List<Word>>newInsensitiveInstance(CharMap.CharCase.LOWER);
        try {
            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
            parser.parse(stream, new WordsHandler(wordMap));
        } catch (ParserConfigurationException | SAXException | IOException e) {
            Logger.getLogger(LayerDictionaryWordsDataProvider.class.getName()).log(Level.WARNING, null, e);
            return null;
        }
        return new IDictionaryDataProvider.Data(wordMap, null);
    }
}
