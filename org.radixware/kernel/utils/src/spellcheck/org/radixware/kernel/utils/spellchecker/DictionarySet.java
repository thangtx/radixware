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

import java.util.*;
import org.radixware.kernel.common.check.spelling.IDictionary;
import org.radixware.kernel.common.check.spelling.IDictionaryProvider;
import org.radixware.kernel.common.check.spelling.IDictionarySet;
import org.radixware.kernel.common.check.spelling.Word;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.repository.Layer;


public class DictionarySet implements IDictionarySet {

    private final RadixObject context;
    private final IDictionaryProvider provider;

    public DictionarySet(IDictionaryProvider provider, RadixObject context) {

        assert provider != null : "Provider is null";

        this.context = context;
        this.provider = provider;
    }

    @Override
    public boolean containsWord(Word word) {
        if (word == null) {
            return false;
        }
        for (List<IDictionary> dictionaries : this) {
            for (IDictionary dictionary : dictionaries) {
                if (dictionary.containsWord(word)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean containsWord(CharSequence sequence) {
        if (sequence == null) {
            return false;
        }
        return containsWord(Word.Factory.newInstance(sequence));
    }

    @Override
    public int wordCount() {
        int count = 0;
        for (List<IDictionary> dictionaries : this) {
            for (IDictionary dictionary : dictionaries) {
                count += dictionary.wordCount();
            }
        }
        return count;
    }

    @Override
    public boolean isEmpty() {
        for (List<IDictionary> dictionaries : this) {
            for (IDictionary dictionary : dictionaries) {
                if (!dictionary.isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public Iterator<List<IDictionary>> iterator() {
        return new DictionaryIterator(provider, context);
    }

    @Override
    public int getIndexOf(Word word) {
        int dictIndex = 0;
        for (List<IDictionary> dictionaries : this) {
            for (IDictionary dictionary : dictionaries) {
                if (!dictionary.isEmpty()) {
                    int indexInDict = dictionary.getIndexOf(word);
                    if (-1 < indexInDict) {
                        return dictIndex + indexInDict;
                    } else {
                        dictIndex += dictionary.wordCount();
                    }
                }
            }
        }
        return -1;
    }

    @Override
    public Word getWordAt(int index) {
        if (index < 0) {
            return null;
        }

        int indexInDict = index;
        for (List<IDictionary> dictionaries : this) {
            for (IDictionary dictionary : dictionaries) {
                if (!dictionary.isEmpty()) {
                    int wordsCount = dictionary.wordCount();
                    if (indexInDict < wordsCount) {
                        return dictionary.getWordAt(indexInDict);
                    } else {
                        indexInDict -= dictionary.wordCount();
                    }
                }
            }
        }
        return null;
    }

    private static final class DictionaryIterator implements Iterator<List<IDictionary>> {

        private List<Layer> currentLayer = new LinkedList<>();
        private final IDictionaryProvider provider;
        private int dictionaryType = 0;
        private List<IDictionary> next;

        public DictionaryIterator(IDictionaryProvider provider, RadixObject context) {
            if (context != null) {
                currentLayer.add(context.getLayer());
            }
            this.provider = provider;

            findNextDictionary();
        }

        @Override
        public boolean hasNext() {
            return next != null && !next.isEmpty();
        }

        @Override
        public List<IDictionary> next() {

            List<IDictionary> current = next;

            findNextDictionary();

            return current;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Remove operation not supported.");
        }

        private List<IDictionary> getNextLayerDictionary() {
            List<IDictionary> dictionary = new LinkedList<>();
            if (currentLayer != null && !currentLayer.isEmpty()) {
                if (dictionaryType == 2) {
                    dictionaryType = 3;
                    for (Layer layer : currentLayer) {
                        IDictionary d = provider.getLayerDictionary(layer);
                        if (d != null) {
                            dictionary.add(d);
                        }
                    }
                } else {
                    dictionaryType = 2;
                    for (Layer layer : currentLayer) {
                        IDictionary d = DictionaryProvider.getDefaultInstance().getLayerDictionary(layer);
                        if (d != null) {
                            dictionary.add(d);
                        }
                    }
                    List<Layer> current = new ArrayList<>(currentLayer);
                    currentLayer.clear();
                    for (Layer l : current) {
                        List<Layer> base = l.listBaseLayers();
                        currentLayer.addAll(base);
                    }
                }
            }
            return dictionary;
        }

        private void findNextDictionary() {
            next = null;
            while (next == null) {
                switch (dictionaryType) {
                    case 0:
                        dictionaryType = 1;
                        next = Collections.singletonList(provider.getCommonDictionary());
                        break;
                    case 1:
                        dictionaryType = 2;
                        next = Collections.<IDictionary>singletonList(DictionaryProvider.getDefaultInstance().getCommonDictionary());
                        break;
                    case 2:
                    case 3:
                        if (currentLayer == null) {
                            return;
                        }
                        next = getNextLayerDictionary();
                }
            }
        }
    }
}
