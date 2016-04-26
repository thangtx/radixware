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
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import org.radixware.kernel.common.enums.EIsoLanguage;


public final class CommonDictionary extends AbstractDictionary {

    private static final class WordArray extends WordCollection {

        private static final class Part {

            public List<Word> list;

            public Part(List<Word> list) {
                this.list = list;
            }
        }
        private Part[] parts;
        private char decrement;
        private final Object lock = new Object();

        public WordArray(IDictionaryDataProvider provider) {
            super(provider);
        }

        @Override
        public void setList(char ch, List<Word> list) {

            synchronized (lock) {
                if (parts != null) {
                    final int index = indexForChar(ch);

                    assert index != -1 : "Part's index out of range";

                    if (index != -1) {
                        parts[index] = new Part(list);
                    }
                }
            }
        }

        @Override
        public List<Word> getList(char ch) {

            Part part = null;
            synchronized (lock) {
                if (parts != null) {
                    int index = indexForChar(ch);
                    if (index != -1) {
                        part = parts[index];
                    }
                }
            }
            List<Word> list;
            if (part != null && (list = part.list) != null) {
                return list;
            }
            return null;
        }

        @Override
        public Word getWordAt(int index) {
            assert index >= 0 : "Negative word index : " + index;

            if (index < 0) {
                return null;
            }

            int local = index;
            synchronized (lock) {
                if (parts != null) {
                    for (Part part : parts) {
                        if (part == null || part.list == null) {
                            continue;
                        }
                        int size = part.list.size();
                        if (size > local) {
                            return part.list.get(local);
                        } else {
                            local -= size;
                        }
                    }
                }
            }
            return null;
        }

        @Override
        public void deallocate() {
            synchronized (lock) {
                if (parts != null) {
                    for (int i = 0; i < parts.length; ++i) {
                        parts[i] = null;
                    }
                    parts = null;
                }
            }
        }

        @Override
        public int wordCount() {
            int count = 0;
            synchronized (lock) {
                if (parts != null) {
                    for (Part part : parts) {
                        if (part != null && part.list != null) {
                            count += part.list.size();
                        }
                    }
                }
            }

            return count;
        }

        @Override
        public boolean isReady() {
            synchronized (lock) {
                return parts != null;
            }
        }

        @Override
        public void init() {
            final IDictionaryDataProvider provider = getWordProvider();
            CharMap<List<Word>> words = null;
            if (provider != null) {
                final IDictionaryDataProvider.Data data = provider.provide();
                if (data != null) {
                    words = data.words;
                }
            }

            if (words != null) {
                final char[] chars = words.getKeyArray();
                final char minChar = getMin(chars), maxChar = getMax(chars);

                decrement = minChar;
                synchronized (lock) {
                    parts = new Part[maxChar - minChar + 1];

                    for (char chr : chars) {
                        List<Word> part = new ArrayList<>(words.get(chr));
                        setList(chr, part);
                    }
                }
            } else {
                synchronized (lock) {
                    parts = new Part[0];
                }
            }
        }

        @Override
        public Iterator<List<Word>> iterator() {
            return new ListIterator();
        }

        private int indexForChar(char key) {
            key = Character.toLowerCase(key);
            final int index = key - decrement;

            if (0 <= index && index < parts.length) {
                return index;
            }
            return -1;
        }

        private char getMax(char[] chars) {
            int max = -1;

            for (char ch : chars) {
                if (max < ch) {
                    max = ch;
                }
            }
            return (char) max;
        }

        private char getMin(char[] chars) {
            int min = 2 << 17;

            for (char ch : chars) {
                if (ch < min) {
                    min = ch;
                }
            }
            return (char) min;
        }

        private final class ListIterator implements Iterator<List<Word>> {

            private final Part[] etalon;
            private int currentIndex = 0;
            private int listCount;
            private List<Word> next;

            public ListIterator() {
                etalon = parts;

                assert etalon != null;

                if (etalon != null) {
                    listCount = etalon.length;
                }

                findNextList();
            }

            @Override
            public boolean hasNext() {
                checkComodification();

                return next != null;
            }

            @Override
            public List<Word> next() {
                checkComodification();

                if (next == null) {
                    throw new NoSuchElementException();
                }

                List<Word> current = next;
                findNextList();
                return current;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }

            private void checkComodification() {
                if (etalon != parts) {
                    throw new ConcurrentModificationException();
                }
            }

            private void findNextList() {
                Part part = etalon[currentIndex];
                while (currentIndex < listCount && (part == null || part.list == null)) {
                    part = etalon[currentIndex++];
                }
                if (currentIndex < listCount) {
                    next = part.list;
                }
                next = null;
            }
        }
    }

    public CommonDictionary(EIsoLanguage language, IDictionaryDataProvider loader) {
        super(language, false, new DataController(new WordArray(loader), true));
    }

    @Override
    public boolean addWord(Word word) {
        throw new UnsupportedOperationException("Attempt to edit immutable dictionary");
    }

    @Override
    public boolean removeWord(Word word) {
        throw new UnsupportedOperationException("Attempt to edit immutable dictionary");
    }
}
