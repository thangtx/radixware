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

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;


public class CharMap<ValueType> {

    public static enum CharCase {
        LOWER, UPPER
    }

    public interface IEntry<ValueType> {

        char getKey();

        ValueType getValue();
    }

    private static final class Entry<ValueType> implements IEntry<ValueType> {

        char key;
        ValueType value;
        Entry<ValueType> next;

        public Entry(char key, ValueType value, Entry<ValueType> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        public char getKey() {
            return key;
        }

        @Override
        public ValueType getValue() {
            return value;
        }
    }
    
    public static final class Factory {

        public static <ValueType> CharMap<ValueType> newInstance() {
            return new CharMap<ValueType>();
        }

        public static <ValueType> CharMap<ValueType> newInstance(int capacity) {
            return new CharMap<ValueType>(capacity);
        }

        public static <ValueType> CharMap<ValueType> newInsensitiveInstance(CharCase charCase) {
            return Factory.<ValueType>newInsensitiveInstance(charCase, INIT_CAPACITY);
        }

        public static <ValueType> CharMap<ValueType> newInsensitiveInstance(CharCase charCase, int capacity) {
            if (charCase == CharCase.LOWER) {
                return new LowerCaseCharMap(capacity);
            } else {
                return new UpperCaseCharMap(capacity);
            }
        }
    }

    private static final int MAX_CAPACITY = 1 << 16;
    private static final int INIT_CAPACITY = 32;
    private Entry[] map;
    private int size;
    private int modCount;

    CharMap() {
        this(INIT_CAPACITY);
    }

    CharMap(int capacity) {
        this.size = 0;
        this.map = new Entry[getCapacity(capacity)];
    }

    private int getCapacity(int capacity) {
        if (capacity >= MAX_CAPACITY) {
            return MAX_CAPACITY;
        }

        char multTwoCapacity = 1;
        while (capacity > multTwoCapacity) {
            multTwoCapacity <<= 1;
        }
        return multTwoCapacity;
    }

    private void transfer(Entry[] newMap) {
        for (int i = 0; i < map.length; ++i) {
            Entry<ValueType> entry = map[i];
            if (entry != null) {
                map[i] = null;

                do {
                    int index = indexFor(entry.key, newMap.length);

                    Entry<ValueType> next = entry.next;
                    entry.next = newMap[index];
                    newMap[index] = entry;
                    entry = next;

                } while (entry != null);
            }
        }
    }

    private void resize(int capacity) {
        if (capacity < MAX_CAPACITY) {
            Entry[] newMap = new Entry[capacity];
            transfer(newMap);
            map = newMap;
        }
    }

    private void addEntry(int index, char key, ValueType value) {
        Entry<ValueType> entry = map[index];
        map[index] = new Entry<ValueType>(key, value, entry);
        if (++size > map.length) {
            resize(getCapacity(map.length * 2));
        }
    }

    private Entry<ValueType> removeEntryForKey(char key) {

        int index = indexFor(key, map.length);
        Entry<ValueType> prev = null;

        for (Entry<ValueType> entry = map[index]; entry != null; entry = entry.next) {

            if (key == entry.key) {

                ++modCount;
                --size;

                if (prev == null) {
                    map[index] = entry.next;
                } else {
                    prev.next = entry.next;
                }
                return entry;
            }
            prev = entry;
        }
        return null;
    }

    private int indexFor(char key, int length) {
        return key & (length - 1);
    }

    private boolean containsNullValue() {
        for (Entry<ValueType> entry : map) {
            for (; entry != null; entry = entry.next) {
                if (entry.value == null) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * For overriding in subclasses
     * @param key received char key
     * @return char key converted to required format
     */
    protected char getCasedKey(char key) {
        return key;
    }

    public ValueType remove(char key) {
        Entry<ValueType> entry = removeEntryForKey(getCasedKey(key));
        return entry != null ? entry.value : null;
    }

    public ValueType get(char key) {
        char casedKey = getCasedKey(key);
        int index = indexFor(casedKey, map.length);
        for (Entry<ValueType> entry = map[index]; entry != null; entry = entry.next) {
            if (casedKey == entry.key) {
                return entry.value;
            }
        }
        return null;
    }

    public ValueType put(char key, ValueType value) {
        char casedKey = getCasedKey(key);

        int index = indexFor(casedKey, map.length);

        for (Entry<ValueType> entry = map[index]; entry != null; entry = entry.next) {
            if (casedKey == entry.key) {
                ValueType oldValue = entry.value;
                entry.value = value;
                return oldValue;
            }
        }
        ++modCount;
        addEntry(index, casedKey, value);
        return null;
    }

    public char[] getKeyArray() {
        char[] keys = new char[size];

        int index = 0;
        for (Entry<ValueType> entry : map) {
            while (entry != null) {
                keys[index++] = entry.key;
                entry = entry.next;
            }
        }

        return keys;
    }
    private Collection<ValueType> values = null;

    public Collection<ValueType> getValues() {
        if (values == null) {
            values = new Values();
        }
        return values;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size() == 0;
    }
    
    public boolean containsKey(char key) {
        return get(key) != null;
    }

    public boolean containsValue(Object value) {

        if (value == null) {
            return containsNullValue();
        }

        for (Entry<ValueType> entry : map) {
            for (; entry != null; entry = entry.next) {
                if (value.equals(entry.value)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void clear() {
        modCount++;
        
        Entry[] oldMap = map;
        for (int i = 0; i < oldMap.length; ++i) {
            oldMap[i] = null;
        }
        size = 0;
    }
    
    Iterator<ValueType> newValueIterator() {
        return new ValueIterator();
    }

    Iterator<Entry<ValueType>> newEntryIterator() {
        return new EntryIterator();
    }

    List<Entry<ValueType>> getSortedEntryList() {
        List<Entry<ValueType>> entries = new ArrayList<Entry<ValueType>>(size);

        for (Entry<ValueType> entry : map) {
            while (entry != null) {
                entries.add(entry);
                entry = entry.next;
            }
        }

        Collections.sort(entries, new Comparator<Object>() {

            @Override
            public int compare(Object o1, Object o2) {
                char ch1 = Character.toLowerCase((((Entry<ValueType>) o1).key)),
                    ch2 = Character.toLowerCase(((Entry<ValueType>) o2).key);
                return ch1 - ch2;
            }
        });

        return entries;
    }

    class Values extends AbstractCollection<ValueType> {

        @Override
        public Iterator<ValueType> iterator() {
            return new ValueIterator();
        }

        @Override
        public int size() {
            return CharMap.this.size();
        }

        @Override
        public boolean contains(Object o) {
            return CharMap.this.containsValue(o);
        }
    }

    abstract class AbstractIterator<T> implements Iterator<T> {

        Entry<ValueType> current;
        int index = 0;
        int expectedModCount = modCount;
        List<Entry<ValueType>> entries = getSortedEntryList();

        @Override
        public boolean hasNext() {
            return index < entries.size();
        }

        @Override
        public void remove() {
            if (current == null) {
                throw new IllegalStateException();
            }

            if (modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }

            char key = current.key;
            current = null;
            CharMap.this.removeEntryForKey(key);
            expectedModCount = modCount;
        }

        public Entry<ValueType> netxEntry() {
            current = entries.get(index++);

            if (expectedModCount != modCount) {
                throw new ConcurrentModificationException();
            }

            if (current == null) {
                throw new NoSuchElementException();
            }

            return current;
        }
    }

    class EntryIterator extends AbstractIterator<Entry<ValueType>> {

        @Override
        public Entry<ValueType> next() {
            return netxEntry();
        }
    }

    class ValueIterator extends AbstractIterator<ValueType> {

        @Override
        public ValueType next() {
            return netxEntry().value;
        }
    }
}

class LowerCaseCharMap<ValueType> extends CharMap<ValueType> {

    public LowerCaseCharMap(int capacity) {
        super(capacity);
    }
    
    @Override
    protected char getCasedKey(char key) {
        return Character.toLowerCase(key);
    }
}

class UpperCaseCharMap<ValueType> extends CharMap<ValueType> {

    public UpperCaseCharMap(int capacity) {
        super(capacity);
    }
    
    @Override
    protected char getCasedKey(char key) {
        return Character.toUpperCase(key);
    }
}