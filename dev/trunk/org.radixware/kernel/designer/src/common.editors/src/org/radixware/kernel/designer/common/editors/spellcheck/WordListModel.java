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

package org.radixware.kernel.designer.common.editors.spellcheck;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.swing.AbstractListModel;
import javax.swing.JList;
import org.radixware.kernel.common.check.spelling.DictionaryChangeEvent;
import org.radixware.kernel.common.check.spelling.DictionaryChangeListener;
import org.radixware.kernel.common.check.spelling.IWordContainer;
import org.radixware.kernel.common.check.spelling.Word;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.utils.spellchecker.LayerDictionary;


final class WordListModel extends AbstractListModel {

    public enum SortOrder {

        ASC, DESC;
    }
    private final LayerDictionary dictionary;
    private final WordContainerAdapter containerAdapter = new WordContainerAdapter();
    private SortOrder sortOrder = SortOrder.ASC;
    private String regExp;
    private Pattern pattern;
    private final JList ownerList;
    private IWordContainer container;

    public WordListModel(JList ownerList, LayerDictionary dictionary, String regExp) {
        this.dictionary = dictionary;
        this.ownerList = ownerList;

        dictionary.addDictionaryChangeListener(new DictionaryChangeListener() {

            @Override
            public void dictionaryChanged(DictionaryChangeEvent event) {
                selectWords();
            }
        });

        setFilter(regExp);
    }

    @Override
    public int getSize() {
        return container.wordCount();
    }

    @Override
    public Word getElementAt(int index) {
        int indexByOrder = getIndexByOrder(index);
        if (indexByOrder != -1) {
            return container.getWordAt(indexByOrder);
        }
        return null;
    }

    public SortOrder getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(SortOrder sortOrder) {
        if (this.sortOrder != sortOrder) {
            this.sortOrder = sortOrder;
            selectWords();
        }
    }

    public void removeWords(Word[] words) {
        for (Word word : words) {
            dictionary.removeWord(word);
        }
        selectWords();
    }

    public int addWord(Word word, String comment) {
        if (dictionary.addWord(word)) {
            dictionary.setComment(word, comment);
            selectWords();

            int addIndex = getIndexOf(word);
            setSelectedIndex(addIndex);
            return addIndex;
        }
        return -1;
    }

    public int addWord(CharSequence word, String comment) {
        return addWord(Word.Factory.newInstance(word), comment);
    }

    private boolean validIndex(int index) {
        return 0 <= index && index < container.wordCount();
    }

    private int getIndexByOrder(int index) {
        if (!validIndex(index)) {
            return -1;
        }
        if (sortOrder == SortOrder.ASC) {
            return index;
        }
        return getSize() - index - 1;
    }

    private void setSelectedIndex(int index) {
        if (index >= 0) {
            ownerList.setSelectedIndex(index);
            ownerList.ensureIndexIsVisible(index);
        } else {
            ownerList.clearSelection();
        }
    }

    public void setFilter(String regExp) {
        if (!Utils.equals(this.regExp, regExp)) {
            this.regExp = regExp;
            if (regExp != null) {
                try {
                    pattern = Pattern.compile(regExp + ".*");
                } catch (PatternSyntaxException  exception) {
                    pattern = Pattern.compile(" ");
                }
            }
            selectWords();
        }
    }

    private boolean accept(Word word) {
        if (word != null && isFiltering()) {
            return pattern.matcher(word.toLowerCase()).matches();
        }
        return word != null;
    }

    private void selectWords() {
        Object selectedValue = ownerList.getSelectedValue();

        if (isFiltering()) {
            containerAdapter.clear();
            container = containerAdapter;
            for (Word word : dictionary) {
                if (accept(word)) {
                    containerAdapter.addWord(word);
                }
            }
        } else {
            container = dictionary;
        }

        int index = getIndexOf((Word) selectedValue);
        setSelectedIndex(index);
        fireContentsChanged(this, 0, getSize() - 1);
    }

    private int getIndexOf(Word word) {
        if (word == null) {
            return -1;
        } else {
            return getIndexByOrder(container.getIndexOf(word));
        }
    }

    private boolean isFiltering() {
        return regExp != null && !regExp.isEmpty();
    }

    private final class WordContainerAdapter implements IWordContainer {

        private final List<Word> words = new ArrayList<Word>();

        @Override
        public boolean containsWord(Word word) {
            return words.contains(word);
        }

        @Override
        public boolean containsWord(CharSequence sequence) {
            return containsWord(Word.Factory.newInstance(sequence));
        }

        @Override
        public int getIndexOf(Word word) {
            return words.indexOf(word);
        }

        @Override
        public Word getWordAt(int index) {
            return validIndex(index) ? words.get(index) : null;
        }

        @Override
        public int wordCount() {
            return words.size();
        }

        @Override
        public boolean isEmpty() {
            return wordCount() == 0;
        }

        private boolean validIndex(int index) {
            return 0 <= index && index < words.size();
        }

        void clear() {
            words.clear();
        }

        void addWord(Word word) {
            words.add(word);
        }
    }
}
