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

package org.radixware.kernel.common.repository.fs;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.radixware.kernel.common.utils.FileUtils;


public class JarFileDataProvider implements IJarDataProvider {

    public static enum EFileClosePolicy {

        CLOSE,
        FILE_WILL_BE_CLOSED_EXTERNALLY
    }
    private ZipFile zipFile;
    private final String fileName;
    private static long count = 0;

    private static final class Cache {

        private static class Entry {

            long modStamp;
            JarFileDataProvider instance;

            public Entry(long modStamp, JarFileDataProvider instance) {
                this.modStamp = modStamp;
                this.instance = instance;
            }
        }
        private final HashMap<String, Entry> pool = new HashMap<String, Entry>(10);

        private JarFileDataProvider findOrCreate(File file) {
            Entry e = pool.get(file.getPath());
            if (e != null) {
                if (e.modStamp == file.lastModified()) {
                    return e.instance;
                } else {
                    try {
                        e.instance.close();
                    } catch (IOException ex) {
                        //ignore
                        Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                    }
                }
            }
            e = new Entry(file.lastModified(), new JarFileDataProvider(file, EFileClosePolicy.CLOSE));
            pool.put(file.getPath(), e);
            return e.instance;
        }

        public void reset() {
            for (Entry e : pool.values()) {
                try {
                    e.instance.close();
                } catch (IOException ex) {
                    //ignore
                    Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                }
            }
            pool.clear();
        }
    }
    private static final Cache cache = new Cache();

    public static JarFileDataProvider getInstance(File file) {
        return new JarFileDataProvider(file, EFileClosePolicy.CLOSE);//cache.findOrCreate(file);
    }

    public static void reset() {
        cache.reset();
        //  System.gc();
    }

    private JarFileDataProvider(File zipFile, EFileClosePolicy fileClosePolicy /*by BAO*/) {
        this.fileName = zipFile.getAbsolutePath();
        this.fileClosePolicy = fileClosePolicy;
    }

    private ZipFile getZipFile() {
        synchronized (this) {
            try {
                if (fileClosePolicy == EFileClosePolicy.FILE_WILL_BE_CLOSED_EXTERNALLY) {
                    return new ZipFile(new File(fileName));
                } else {
                    return zipFile = new ZipFile(new File(fileName)); //by BAO for file to be closed in close()                    
                }
            } catch (IOException ex) {
                return null;
            }
        }
    }
    private final EFileClosePolicy fileClosePolicy; //by BAO

    private JarFileDataProvider(ZipFile zipFile, String fileName, EFileClosePolicy fileClosePolicy /*by BAO*/) {
        this.zipFile = zipFile;
        this.fileName = fileName;
        this.fileClosePolicy = fileClosePolicy;
    }

    @Override
    public byte[] getEntryData(String name) throws IOException {
        synchronized (this) {
            try {
                ZipFile zip = getZipFile();
                if (zip == null) {
                    return null;
                }
                ZipEntry e = zip.getEntry(name);
                if (e == null) {
                    return null;
                }
                return getZipEntryByteContent(e, zip);
            } finally {
                close();
            }
        }
    }

    @Override
    public boolean entryExists(String name) {
        entries();
        return entiries.containsKey(name);
    }

    @Override
    public boolean close() throws IOException {
        synchronized (this) {
            if (zipFile != null && fileClosePolicy == EFileClosePolicy.CLOSE) {
                try {
                    zipFile.close();
                } catch (IOException ex) {
                    Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                } finally {
                    zipFile = null;
                }
            }
            return true;
        }
    }

    @Override
    public String getName() {
        return fileName;
    }

    @Override
    public boolean isFileBased() {
        return true;
    }

    private static final class EntryInfo implements IJarEntry {

        final String name;
        final boolean isDirectory;

        public EntryInfo(String name, boolean isDirectory) {
            this.name = name;
            this.isDirectory = isDirectory;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public boolean isDirectory() {
            return isDirectory;
        }
    }
    private HashMap<String, IJarEntry> entiries = null;

    @Override
    public Collection<IJarEntry> entries() {
        synchronized (this) {
            if (entiries == null) {
                try {
                    ZipFile zip = getZipFile();

                    if (zip != null) {
                        //        final ArrayList<IJarEntry> list = new ArrayList<IJarEntry>();
                        entiries = new HashMap<String, IJarEntry>(7);
                        //try {
                        Enumeration<? extends ZipEntry> ze = zip.entries();
                        while (ze.hasMoreElements()) {
                            ZipEntry e = ze.nextElement();
                            EntryInfo info = new EntryInfo(e.getName(), e.isDirectory());
                            //list.add(info);
                            entiries.put(info.name, info);
                        }
                        //}
                    } else {
                        //return Collections.emptySet();
                        entiries = new HashMap<String, IJarEntry>(0);
                    }
                } finally {
                    try {
                        close();
                    } catch (IOException ex) {
                        Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                    }
                }
            }
            return entiries.values();
        }
    }

    @Override
    public InputStream getEntryDataStream(String name) throws IOException {
        synchronized (this) {
            try {
                ZipFile zip = getZipFile();
                if (zip == null) {
                    return null;
                }
                ZipEntry e = zip.getEntry(name);
                if (e == null) {
                    return null;
                }
                final byte[] bytes = getZipEntryByteContent(e, zip);

                return bytes == null ? null : new ByteArrayInputStream(bytes);
            } finally {
                close();
            }
        }
    }

    @Override
    public File getFile() {
        return new File(fileName);
    }

    public static byte[] getZipEntryByteContent(ZipEntry ze, ZipFile zip) throws IOException {
        return FileUtils.getZipEntryByteContent(ze, zip);
    }
}
