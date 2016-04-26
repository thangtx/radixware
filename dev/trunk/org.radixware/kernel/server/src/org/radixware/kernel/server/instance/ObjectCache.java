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

package org.radixware.kernel.server.instance;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.logging.LogFactory;


public class ObjectCache {

    public static enum EExpirationPolicy {

        SINCE_LAST_USAGE,
        SINCE_CREATION
    }
    private final ConcurrentHashMap<String, CacheEntry> store = new ConcurrentHashMap<>();

    public <T> T get(final String key, final Class<T> clazz) {
        final Object obj = get(key);
        if (obj != null && clazz != null) {
            try {
                return clazz.cast(obj);
            } catch (ClassCastException ex) {
                //ignore
            }
        }
        return null;
    }

    public Object get(final String key) {
        final CacheEntry entry = store.get(key);
        if (entry != null) {
            entry.touch();
            return entry.getData();
        }
        return null;
    }

    public boolean putExpiringSinceLastUsage(final String key, final Object data, final int expirationSeconds) {
        final CacheEntry newEntry = new CacheEntry(data, EExpirationPolicy.SINCE_LAST_USAGE, expirationSeconds);
        return store.putIfAbsent(key, newEntry) == null;
    }

    public boolean putExpiringSinceCreation(final String key, final Object data, final int expirationSeconds) {
        final CacheEntry newEntry = new CacheEntry(data, EExpirationPolicy.SINCE_CREATION, expirationSeconds);
        return store.putIfAbsent(key, newEntry) == null;
    }

    public void remove(final String key) {
        CacheEntry entry = store.remove(key);
        if (entry != null) {
            tryReleaseResources(entry);
        }
    }

    public void clear() {
        final List<String> keys = new ArrayList<>(store.keySet());
        for (String key : keys) {
            remove(key);
        }
    }

    public void maintenance() {
        final List<String> toRemove = new ArrayList<>();
        for (Map.Entry<String, CacheEntry> entry : store.entrySet()) {
            if (entry.getValue().isExpired()) {
                toRemove.add(entry.getKey());
            }
        }
        for (String key : toRemove) {
            remove(key);
        }
    }

    private void tryReleaseResources(final CacheEntry entry) {
        if (entry == null) {
            return;
        }
        if (entry.getData() instanceof ICachedUserObject) {
            try {
                ((ICachedUserObject) entry.getData()).release();
            } catch (Exception ex) {
                LogFactory.getLog(ObjectCache.class).error("Error on releasing resources of cached user object " + entry.getData(), ex);
            }
        } else if (entry.getData() instanceof AutoCloseable) {
            try {
                ((AutoCloseable) entry.getData()).close();
            } catch (Exception ex) {
                LogFactory.getLog(ObjectCache.class).error("Error on releasing resources of autocloseable" + entry.getData(), ex);
            }
        }
    }
    
    public int size() {
        return store.size();
    }

    private static class CacheEntry {

        private volatile long touchMillis;
        private final Object data;
        private final long expirationMillis;
        private final long creationMillis;
        private final EExpirationPolicy expirationPolicy;

        public CacheEntry(Object data, final EExpirationPolicy expirationPolicy, final int expirationSeconds) {
            this.data = data;
            this.touchMillis = this.creationMillis = System.currentTimeMillis();
            this.expirationMillis = expirationSeconds * 1000;
            this.expirationPolicy = expirationPolicy;
        }

        public boolean isExpired() {
            return expirationMillis <= 0 ? false : ((expirationPolicy == EExpirationPolicy.SINCE_CREATION ? creationMillis : touchMillis) + expirationMillis) < System.currentTimeMillis();
        }

        public void touch() {
            touchMillis = System.currentTimeMillis();
        }

        public Object getData() {
            return data;
        }
    }
}
