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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Sorted collection of sorted lists of words. Must enumerates the lists in alphabetical order.
 */
public abstract class WordCollection implements IWordContainer, Iterable<List<Word>> {

    private final class WordIterator implements Iterator<Word> {

        private Iterator<Word> wordIterator;
        private final Iterator<List<Word>> listIterator = iterator();

        public WordIterator() {
            findNextIterator();
        }

        @Override
        public boolean hasNext() {
            return wordIterator != null;
        }

        @Override
        public Word next() {
            if (wordIterator == null) {
                throw new NoSuchElementException();
            }

            Word current = wordIterator.next();

            if (!wordIterator.hasNext()) {
                findNextIterator();
            }

            return current;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        private void findNextIterator() {
            while (listIterator.hasNext()) {
                List<Word> list = listIterator.next();
                if (list != null) {
                    Iterator<Word> iterator = list.iterator();
                    if (iterator.hasNext()) {
                        wordIterator = iterator;
                        return;
                    }
                }
            }
            wordIterator = null;
        }
    }
    private IDictionaryDataProvider provider;

    public WordCollection(IDictionaryDataProvider provider) {
        setDataProvider(provider, false);
    }

    @Override
    public int getIndexOf(Word word) {
        if (!validWord(word)) {
            return -1;
        }

        final List<Word> list = getList(word.charAt(0));
        if (list == null || list.isEmpty()) {
            return -1;
        }

        int listIndex = 0;
        for (List<Word> currList : this) {
            if (currList == list) {
                break;
            } else if (!currList.isEmpty()) {
                listIndex += currList.size();
            }
        }

        final Word lesser;
        final int lesserIndex;
        synchronized (list) {
            lesserIndex = indexOfLesser(list, word);
            lesser = list.get(lesserIndex);
        }

        Word lWord = word.toLowerCase();

        if (lesser.equals(word) || lesser.equals(lWord)) {
            return listIndex + lesserIndex;
        } else {
            return -1;
        }
    }

    @Override
    public boolean containsWord(Word word) {
        return -1 < getIndexOf(word);
    }

    @Override
    public boolean containsWord(CharSequence sequence) {
        if (!validWord(sequence) || isEmpty()) {
            return false;
        }

        Word searchWord = Word.Factory.newInstance(sequence);
        return containsWord(searchWord);
    }

    @Override
    public boolean isEmpty() {
        return wordCount() == 0;
    }

    private boolean validWord(CharSequence word) {
        return word != null && word.length() > 0;
    }

    public Iterator<Word> getWordIterator() {
        return new WordIterator();
    }

    protected final IDictionaryDataProvider getWordProvider() {
        return provider;
    }

    public final void setDataProvider(IDictionaryDataProvider provider, boolean immediatelyReload) {

        this.provider = provider;

        if (immediatelyReload && provider != null) {
            init();
        }
    }

    public List<String> asList() {
        List<String> wordList = new ArrayList<String>(wordCount());
        for (List<Word> list : this) {
            for (Word word : list) {
                wordList.add(word.toString());
            }
        }

        return wordList;
    }

    public abstract List<Word> getList(char ch);

    public abstract void setList(char ch, List<Word> list);

    public abstract void init();

    public abstract void deallocate();

    public abstract boolean isReady();

    /**
     * Binary search in sorted list.
     * @param words sorted list
     * @param word required word
     * @return index if list contains required word or -1 otherwise
     */
    protected int indexOfLesser(List<Word> words, Word word) {

        int lower = 0;
        int upper = words.size() - 1;

        boolean last = false;

        while (true) {
            if (lower == upper) {
                break;
            }
            if (last) {
                break;
            }
            if ((upper - lower) == 1) {
                last = true;
            }
            int current = (lower + upper) / 2;
            Word currentObj = words.get(current);

            int result = currentObj.compareIgnoreCaseTo(word);

            if (result == 0) {
                return current;
            }

            if (result < 0) {
                lower = current + 1;
            }

            if (result > 0) {
                upper = current - 1;
            }
        }

        int comparison = words.get(lower).compareIgnoreCaseTo(word);
        if (comparison >= 0) {
            return lower;
        } else {
            return (lower + 1) < words.size() ? lower + 1 : lower;
        }
    }
}
