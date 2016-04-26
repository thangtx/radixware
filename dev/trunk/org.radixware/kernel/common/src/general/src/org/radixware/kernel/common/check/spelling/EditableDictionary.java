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

package org.radixware.kernel.common.check.spelling;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.xmlbeans.XmlOptions;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.schemas.dictionary.Dictionary;
import org.radixware.schemas.dictionary.DictionaryDocument;


public final class EditableDictionary extends AbstractDictionary {

    private static final class WordMap extends WordCollection {

        private final CharMap<List<Word>> parts;
        private final Map<Word, String> comments;
        private boolean ready = false;

        public WordMap(IDictionaryDataProvider provider) {
            super(provider);

            parts = CharMap.Factory.<List<Word>>newInsensitiveInstance(CharMap.CharCase.LOWER);
            comments = new HashMap<Word, String>();
        }

        @Override
        public List<Word> getList(char ch) {
            List<Word> part = null;

            synchronized (parts) {
                part = parts.get(ch);
            }
            return part;
        }

        @Override
        public void setList(char ch, List<Word> list) {
            synchronized (parts) {
                parts.put(ch, list);
            }
        }

        @Override
        public Word getWordAt(int index) {
            assert index >= 0 : "Negative word index : " + index;

            if (index < 0) {
                return null;
            }

            int local = index;

            synchronized (parts) {
                Collection<List<Word>> values = parts.getValues();
                if (values != null) {
                    for (List<Word> part : values) {
                        if (part == null || part.isEmpty()) {
                            continue;
                        }
                        int size = part.size();
                        if (local < size) {
                            return part.get(local);
                        } else {
                            local -= size;
                        }
                    }
                }
            }
            return null;
        }

        @Override
        public int wordCount() {

            int count = 0;
            synchronized (parts) {
                Collection<List<Word>> values = parts.getValues();
                if (values != null) {
                    for (List<Word> part : values) {
                        if (part != null) {
                            count += part.size();
                        }
                    }
                }
            }
            return count;
        }

        @Override
        public void deallocate() {
            synchronized (parts) {
                parts.clear();
            }
            synchronized (comments) {
                comments.clear();
            }
            ready = false;
        }

        @Override
        public boolean isReady() {
            return ready;
        }

        @Override
        public void init() {

            deallocate();
            ready = true;

            IDictionaryDataProvider provider = getWordProvider();

            if (provider == null) {
                return;
            }

            IDictionaryDataProvider.Data data = provider.provide();

            if (data == null) {
                return;
            }

            CharMap<List<Word>> words = data.words;
            if (words != null && !words.isEmpty()) {
                synchronized (parts) {
                    for (CharMap.IEntry<List<Word>> entry : words.getSortedEntryList()) {
                        setList(entry.getKey(), entry.getValue());
                    }
                }
            }

            Map<String, String> commentaries = data.comments;
            if (commentaries != null && !commentaries.isEmpty()) {
                synchronized (comments) {
                    for (Map.Entry<String, String> entry : commentaries.entrySet()) {
                        comments.put(Word.Factory.newInstance(entry.getKey()), entry.getValue());
                    }
                }
            }
        }

        @Override
        public Iterator<List<Word>> iterator() {
            return parts.newValueIterator();
        }

        public void setComment(Word word, String comment) {

            if (word != null) {
                if (comment != null && !comment.isEmpty()) {
                    comments.put(word, comment);
                } else {
                    comments.remove(word);
                }
            }
        }

        public String getComment(Word word) {
            if (word != null) {
                return comments.get(word);
            }
            return null;
        }
    }

    public EditableDictionary(EIsoLanguage language, IDictionaryDataProvider provider) {
        super(language, true, new DataController(new WordMap(provider), false));
    }

    @Override
    public boolean addWord(Word word) {
        if (word == null || word.isEmpty()) {
            return false;
        }

        final int lesserIndex;
        final Word lesser;
        final WordCollection collection = getDataController().getInitCollection();

        char ch = word.charAt(0);
        List<Word> list;

        synchronized (collection) {
            list = collection.getList(ch);
            if (list == null) {
                list = new ArrayList<Word>();
                collection.setList(ch, list);
            }
        }

        synchronized (list) {
            if (list.isEmpty()) {
                list.add(word);
            } else {

                lesserIndex = collection.indexOfLesser(list, word);
                lesser = list.get(lesserIndex);

                int compare = lesser.compareIgnoreCaseTo(word);
                if (compare > 0) {
                    list.add(lesserIndex, word);
                } else if (compare < 0) {
                    list.add(lesserIndex + 1, word);
                } else {
                    // if (compare == 0) then dictionary already contained this word
                    return false;
                }
            }
        }

        fireDictionaryChange(new DictionaryChangeEvent(this, DictionaryChangeEvent.ChangeType.ADD));
        return true;
    }

    @Override
    public boolean removeWord(Word word) {
        if (word != null && !word.isEmpty()) {
            final WordCollection collection = getDataController().getInitCollection();
            final List<Word> list = collection.getList(word.charAt(0));

            if (list != null) {
                synchronized (list) {
                    int lesserIndex = collection.indexOfLesser(list, word);
                    Word lesser = list.get(lesserIndex);

                    if (lesser.compareIgnoreCaseTo(word) == 0) {
                        // before removing word - remove comment
                        setComment(word, null);
                        list.remove(lesserIndex);
                        fireDictionaryChange(new DictionaryChangeEvent(this, DictionaryChangeEvent.ChangeType.REMOVE));
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void setComment(Word word, String comment) {
        if (word != null) {
            ((WordMap) getDataController().getInitCollection()).setComment(word, comment);
            fireDictionaryChange(new DictionaryChangeEvent(this, DictionaryChangeEvent.ChangeType.CHANGE));
        }
    }

    public String getComment(Word word) {
        return ((WordMap) getDataController().getInitCollection()).getComment(word);
    }

    public void setDataProvider(IDictionaryDataProvider provider, boolean reload) {
        getDataController().getCollection().setDataProvider(provider, reload);
    }

    public boolean save(File file) {

        final DictionaryDocument dictionaryDocument = DictionaryDocument.Factory.newInstance();
        Dictionary dict = dictionaryDocument.addNewDictionary();

        String language = getLanguage() != null ? getLanguage().getValue() : "common";
        dict.setLanguage(language);

        List<String> list = getDataController().getInitCollection().asList();
        dict.setWords(list);

        Dictionary.Comments comments = dict.addNewComments();

        Map<Word, String> commentMap = ((WordMap) getDataController().getInitCollection()).comments;
        for (Map.Entry<Word, String> entry : commentMap.entrySet()) {

            Dictionary.Comments.Comment comment = comments.addNewComment();
            comment.setKey(entry.getKey().toString());
            comment.setStringValue(entry.getValue());
        }

        try {
            if (!file.exists()) {
                File parent = file.getParentFile();
                if (!parent.exists() && !parent.mkdirs()) {
                    return false;
                }

                if (!file.createNewFile()) {
                    return false;
                }
            }
            XmlOptions options = new XmlOptions();
            options.setSavePrettyPrint();
            dictionaryDocument.save(file, options);

        } catch (IOException ex) {
            return false;
        }
        return true;
    }
    /**
     * 
     */
    private EMode mode = EMode.CHECKING;

    public boolean setMode(EMode mode) {
        this.mode = mode;
        return true;
    }

    public EMode getMode() {
        return mode;
    }

    public static enum EMode {

        EDITING, CHECKING, TRANSIENT
    }
    private final List<DictionaryChangeListener> listenerList = new LinkedList<DictionaryChangeListener>();

    public void addDictionaryChangeListener(DictionaryChangeListener listener) {
        if (listener != null) {
            listenerList.add(listener);
        }
    }

    public void removeDictionaryChangeListener(DictionaryChangeListener listener) {
        if (listener != null) {
            listenerList.remove(listener);
        }
    }

    protected void fireDictionaryChange(DictionaryChangeEvent event) {
        for (DictionaryChangeListener listener : listenerList) {
            listener.dictionaryChanged(event);
        }
    }
}
