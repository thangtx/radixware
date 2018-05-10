/*
 * Copyright (c) 2008-2018, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */

package org.radixware.kernel.common.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;
import java.util.TreeSet;

public class ExpiringSet<T> {
    
    final int maxSize;
    final long expirationPeriodMillis;
    final HashMap<T, Long> items = new HashMap<>(); // Value -> Expiration time
    final TreeSet<ExpiringItem<T>> queue = new TreeSet<>(); // sorted by expiration time

    public ExpiringSet(int maxSize, long expirationPeriodMillis) {
        this.maxSize = maxSize;
        this.expirationPeriodMillis = expirationPeriodMillis;
    }

    public int size() {
        removeExpired();
        return items.size();
    }

    public boolean isEmpty() {
        removeExpired();
        return items.isEmpty();
    }

    public boolean contains(T o) {
        removeExpired();
        return items.containsKey(o);
    }

    public boolean add(T e) {
        final boolean result = remove(e);
        final long expTimeMillis = System.currentTimeMillis() + expirationPeriodMillis;
        items.put(e, expTimeMillis);
        queue.add(new ExpiringItem<>(e, expTimeMillis));
        removeFirstIfRequired();
        return result;
    }

    public boolean remove(T o) {
        removeExpired();
        final Long expTime = items.remove(o);
        if (expTime != null) {
            queue.remove(new ExpiringItem(o, expTime));
        }
        return expTime != null;
    }

    private void removeExpired() {
        final long now = System.currentTimeMillis();
        ExpiringItem<T> item;
        Iterator<ExpiringItem<T>> iter = queue.iterator();
        while (iter.hasNext() && (item = iter.next()).expTimeMillis <= now) {
            iter.remove();
            items.remove(item.value);
        }
    }

    private void removeFirstIfRequired() {
        if (items.size() > maxSize) {
            final ExpiringItem<T> item = queue.pollFirst();
            items.remove(item.value);
        }
    }

    private static final class ExpiringItem<T> implements Comparable<ExpiringItem<T>> {

        final T value; // not-null
        final long expTimeMillis;

        ExpiringItem(T value, long expTimeMillis) {
            super();
            if (value == null) {
                throw new NullPointerException("Expiring item value should not be null");
            }
            this.expTimeMillis = expTimeMillis;
            this.value = value;
        }

        @Override
        public boolean equals(Object obj) {
            if (getClass() != obj.getClass()) {
                return false;
            }
            final ExpiringItem<?> other = (ExpiringItem<?>) obj;
            if (this.expTimeMillis != other.expTimeMillis) {
                return false;
            }
            return Objects.equals(this.value, other.value);
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 43 * hash + (int) (this.expTimeMillis ^ (this.expTimeMillis >>> 32));
            hash = 43 * hash + Objects.hashCode(this.value);
            return hash;
        }

        @Override
        public int compareTo(ExpiringItem<T> o) {
            if (this.expTimeMillis != o.expTimeMillis) {
                return Long.compare(this.expTimeMillis, o.expTimeMillis);
            }
            return Long.compare(this.value.hashCode(), o.value.hashCode());
        }
    }

}
