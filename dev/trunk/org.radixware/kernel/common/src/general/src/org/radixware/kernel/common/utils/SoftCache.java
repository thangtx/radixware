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

package org.radixware.kernel.common.utils;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**

 *
 * @param <K>
 * @param <T>
 */
public final class SoftCache<K, T> {

	private final Map<K, SoftReference<T>> data;
	private final int LAST_USED_HARD_CACHE_SIZE;

	public SoftCache(final int initialCapacity) {
		this(initialCapacity, initialCapacity / 4);
	}

	public SoftCache(final int initialCapacity, final int lastUsedHardCacheSize) {
		data = new HashMap<K, SoftReference<T>>(initialCapacity); 
		LAST_USED_HARD_CACHE_SIZE = lastUsedHardCacheSize;
		if (LAST_USED_HARD_CACHE_SIZE > 0) {
			hardList = new LinkedList<T>();
		} else {
			hardList = null;
		}
	}

	public void clear() {
		data.clear();
        hardList.clear();
	}

	public T put(final K key, final T obj) {
		addToHardList(obj);

		final SoftReference<T> ref = data.put(key, new SoftReference<T>(obj));

		if (ref == null) {
			return null;
		}

		return ref.get();
	}

	public T get(final K key) {
		final SoftReference<T> ref = data.get(key);

		if (ref == null) {
			return null;
		}

		//�� ������� softreference �� null �� ����
		//��� ��� ����� get-� ���������� null ������ ����� ����� put
		final T res = ref.get();
		if (res != null) {
			addToHardList(res);
		}
		return res;
	}

	public void removeDeadReferences() {
		final Iterator<Entry<K, SoftReference<T>>> iter = data.entrySet().iterator();
		Entry<K, SoftReference<T>> entry;
		while (iter.hasNext()) {
			entry = iter.next();
			if (entry.getValue().get() == null) {
				iter.remove();
			}
		}
	}

	public Collection<T> values() {
		final List<T> res = new ArrayList<T>(data.size());
		final Iterator<Entry<K, SoftReference<T>>> iter = data.entrySet().iterator();
		Entry<K, SoftReference<T>> entry;
		while (iter.hasNext()) {
			entry = iter.next();
			if (entry.getValue().get() == null) {
				iter.remove();
			} else {
				res.add(entry.getValue().get());
			}
		}
		return Collections.unmodifiableCollection(res);
	}
	private final LinkedList<T> hardList;

	private final void addToHardList(T obj) {
		if (LAST_USED_HARD_CACHE_SIZE > 0 && obj != null) {
			hardList.addFirst(obj);
			if (hardList.size() > LAST_USED_HARD_CACHE_SIZE) {
				hardList.removeLast();
			}
		}
	}
}
