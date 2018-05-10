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

package org.radixware.kernel.starter.radixloader;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.radixware.kernel.starter.filecache.CacheEntry;
import org.radixware.kernel.starter.filecache.CacheKey;
import org.radixware.kernel.starter.filecache.FileCacheEntry;
import org.radixware.kernel.starter.filecache.JarCacheEntry;
import org.radixware.kernel.starter.log.SafeLogger;
import org.radixware.kernel.starter.meta.FileMeta;
import org.radixware.kernel.starter.meta.RevisionMeta;
import org.radixware.kernel.starter.url.UrlFactory;
import org.radixware.kernel.starter.utils.FileUtils;

/**
 * FileCache should always be used under RadixLoader state lock (read or write)
 *
 * This is a package private class.
 *
 */
public class FileCache {

    private static final long CHECK_EXPIRED_TIMEOUT_MILLIS = 30 * 1000; // 30 sec in millesecs
    private final RadixLoaderAccessor loaderAccessor;
    private final Map<CacheKey, CacheEntry> cache;
    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    private volatile long nextCheckTime;

    static {
        ClassFileData.class.getName();//preload class
    }

    public FileCache(RadixLoaderAccessor loaderAccessor) {
        this.loaderAccessor = loaderAccessor;
        cache = new HashMap<>();
        nextCheckTime = new Date().getTime() + CHECK_EXPIRED_TIMEOUT_MILLIS;
    }
    
    public byte[] readFileData(final FileMeta fileMeta, final RevisionMeta revMeta) throws IOException {
        return getEntryData(null, fileMeta, revMeta, null);
    }

    public byte[] readFileData(final FileMeta fileMeta, final RevisionMeta revMeta,long cacheTimeout) throws IOException {
        return getEntryData(null, fileMeta, revMeta, cacheTimeout);
    }

    public byte[] readClassData(String name, Set<String> groupTypes, RevisionMeta revisionMeta) throws IOException {
        final ClassFileData classFileData = readClassFileData(name, groupTypes, revisionMeta);
        if (classFileData != null) {
            return classFileData.data;
        }
        return null;
    }

    public ClassFileData readClassFileData(String name, Set<String> groupTypes, RevisionMeta revisionMeta) throws IOException {
        FileMeta meta = revisionMeta.findClass(name);
        while (meta != null) {
            if ((groupTypes == null || groupTypes.contains(meta.getGroupType()))) {
                final byte[] data = getEntryData(name, meta, revisionMeta);
                if (data != null) {
                    URL url = null;
                    try {
                        url = new URL(UrlFactory.protocolForRev(revisionMeta.getNum()), null, 0, "/" + meta.getArchive() + ';' + meta.getName());
                    } catch (MalformedURLException e) {
                    }
                    return new ClassFileData(data, url);
                }
            }
            meta = meta.getNext();
        }
        return null;
    }
    
    public byte[] readResourceData(String name, Set<String> groupTypes, String archive, RevisionMeta revisionMeta) throws IOException {
        return readResourceDataImpl(name, groupTypes, archive, revisionMeta, null);
    }
    
    public byte[] readResourceData(String name, Set<String> groupTypes, String archive, RevisionMeta revisionMeta, long cacheTimeout) throws IOException {
        return readResourceDataImpl(name, groupTypes, archive, revisionMeta, cacheTimeout);
    }    

    private byte[] readResourceDataImpl(String name, Set<String> groupTypes, String archive, RevisionMeta revisionMeta, Long cacheTimeout) throws IOException {
        FileMeta meta = revisionMeta.findResources(name);
        if (archive != null) {
            while (meta != null) {
                if (meta.getArchive().equals(archive)) {
                    break;
                }
                meta = meta.getNext();
            }
        }
        if (meta != null && (groupTypes == null || groupTypes.contains(meta.getGroupType()))) {
            return getEntryData(name, meta, revisionMeta, cacheTimeout);
        }
        return null;
    }
    
    private byte[] getEntryData(final String name, FileMeta meta, RevisionMeta revisionMeta) throws IOException {
        return getEntryData(name, meta, revisionMeta, null);
    }

    private byte[] getEntryData(final String name, FileMeta meta, RevisionMeta revisionMeta, Long cacheTimeout) throws IOException {
        final long current_time = System.currentTimeMillis();

        if (current_time >= nextCheckTime) {
            clearExpired(current_time);
        }

        final CacheKey key = new CacheKey(meta.getStore(), revisionMeta.getNum());
        rwLock.readLock().lock();
        try {
            final CacheEntry entry = cache.get(key);
            if (entry != null) {
                entry.touch(current_time);
                return getEntryBytes(entry, name);
            }
        } finally {
            rwLock.readLock().unlock();
        }

        //there is no existing entry, create new one
        rwLock.writeLock().lock();
        try {
            CacheEntry entry = cache.get(key);
            if (entry == null) {
                entry = loaderAccessor.getFile(meta.getStore(), revisionMeta.getNum());
                if (cacheTimeout==null){
                    cache.put(key, entry);
                }else if (cacheTimeout!=0){
                    entry.setExpireTimeout(cacheTimeout);
                    cache.put(key, entry);
                }//else do not keep entry in cache
            }
            entry.touch(current_time);
            return getEntryBytes(entry, name);
        } finally {
            rwLock.writeLock().unlock();
        }


    }

    private byte[] getEntryBytes(final CacheEntry entry, final String name) throws IOException {
        if (entry instanceof FileCacheEntry) {
            return ((FileCacheEntry) entry).getData();
        } else if (entry instanceof JarCacheEntry) {
            if (name == null) {
                return FileUtils.readFile(new File(((JarCacheEntry) entry).getFileName()));
            } else {
                return ((JarCacheEntry) entry).getData(name);
            }
        } else {
            throw new IllegalStateException("Unknown entry type: " + entry.getClass().getName());
        }
    }

    private void clearExpired(long currentTime) {
        rwLock.writeLock().lock();
        try {
            Iterator<CacheEntry> it = cache.values().iterator();
            while (it.hasNext()) {
                CacheEntry entry = it.next();
                if (entry.isExpired(currentTime)) {
                    try {
                        entry.close();
                    } catch (IOException e) {
                    }
                    it.remove();
                }
            }
            nextCheckTime = new Date().getTime() + CHECK_EXPIRED_TIMEOUT_MILLIS;
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    public void clear() {
        rwLock.writeLock().lock();
        try {
            Iterator<CacheEntry> it = cache.values().iterator();
            while (it.hasNext()) {
                CacheEntry entry = it.next();
                try {
                    entry.close();
                } catch (IOException ex) {
                    SafeLogger.getInstance().error(FileCache.class, null, ex);
                }
                it.remove();
            }
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    public static class ClassFileData {

        public final byte[] data;
        public final URL url;

        ClassFileData(byte[] data, URL url) {
            this.data = data;
            this.url = url;
        }
    }
}
