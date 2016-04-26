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
package org.radixware.kernel.release.fs;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.radixware.kernel.common.repository.fs.IJarDataProvider;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.common.utils.Hex;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.starter.meta.FileMeta;
import org.radixware.kernel.starter.radixloader.RadixLoader;
import org.radixware.kernel.starter.utils.SystemTools;

class RfsJarFileDataProvider implements IJarDataProvider {

    private final FileMeta fileMeta;
    private final File file;
    private ReleaseRepository release;
    private final boolean debug;

    RfsJarFileDataProvider(final ReleaseRepository release, final FileMeta fileMeta) {
        debug = Utils.equals(System.getProperty("org.radiwware.filesystem.rfs.debug"), "true");
        this.fileMeta = fileMeta;
        this.file = null;
        this.release = release;
    }

    RfsJarFileDataProvider(final ReleaseRepository release, final File file) {
        debug = Utils.equals(System.getProperty("org.radiwware.filesystem.rfs.debug"), "true");
        this.file = file;
        this.fileMeta = null;
    }

    private File loadedFile;

    private void resetDataFile() {
        synchronized (this) {
            loadedFile = null;
        }
    }

    private static final Object fileAccessLock = new Object();

    private File getDataFile(boolean force) throws IOException {
        //Sync on static monitor beacause of JVM crash when multiple threads trying to get same file
        synchronized (fileAccessLock) {
            if (fileMeta != null) {
                if (!force && loadedFile != null && loadedFile.exists()) {
                    return loadedFile;
                }

                byte[] digest = fileMeta.getDigest();
                if (digest == null) {
                    try {
                        final MessageDigest sha = MessageDigest.getInstance("SHA-1");
                        digest = sha.digest(fileMeta.getName().getBytes("UTF-8"));
                    } catch (NoSuchAlgorithmException ex) {
                    }
                }
                String fileName = digest == null ? null : Hex.encode(digest);

                File file = null;
                final RadixLoader loader = RadixLoader.getInstance();
                if (fileName != null) {
                    fileName += "_" + release.getRevisionMeta().getNum() + "_" + SystemTools.getCurrentProcessPid() + ".jar";
                    file = new File(SystemTools.getTmpDir(), fileName);
                    if (!file.exists()) {
                        file = loader.createTempFileWithExactName(fileName);
                    }
                } else {
                    file = loader.createTempFile("rfs-tmp-jar");
                }
                if (!force && file.exists()) {
                    return loadedFile = file;
                }

                final byte[] data = loader.readFileData(fileMeta, release.getRevisionMeta());
                if (data == null || data.length == 0) {
                    throw new IOException("Unable to load file: " + fileMeta.getStore());
                }

                try {
                    Files.write(Paths.get(file.toURI()), data);
                } catch (IOException ex) {
                    if (debug) {
                        Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Unable to write temporary file " + file.getAbsolutePath() + " " + ex.getMessage(), ex);
                    }
                    throw ex;
                }
                return loadedFile = file;
            } else {
                return file;
            }
        }
    }

    @Override
    public boolean isFileBased() {
        return file != null;
    }

    @Override
    public byte[] getEntryData(String name) throws IOException {
        try {
            return getEntryDataImpl(name, false);
        } catch (Throwable ex) {
            if (debug) {
                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                Logger.getLogger(getClass().getName()).log(Level.FINE, "Reset caches and try get entry data again");
            }
            resetDataFile();
            try {
                return getEntryDataImpl(name, true);
            } catch (IOException ex2) {
                if (debug) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex2.getMessage(), ex2);
                }
                throw ex;
            } catch (Throwable ex2) {
                if (debug) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex2.getMessage(), ex2);
                }
                return null;
            }
        }
    }

    private byte[] getEntryDataImpl(String name, boolean forceRewriteFile) throws IOException {
        ZipFile zip = null;
        try {
            zip = getZipFile(forceRewriteFile);
            if (zip != null) {
                if (debug) {
                    Logger.getLogger(getClass().getName()).log(Level.FINE, "Reading entry from zip file");
                }
                ZipEntry e = zip.getEntry(name);
                if (e != null) {
                    return FileUtils.getZipEntryByteContent(e, zip);
                }
            }
            return null;
        } finally {
            try {
                if (zip != null) {
                    zip.close();
                }
            } catch (IOException ex) {
            }
        }
    }

    private ZipFile getZipFile(boolean forceRewriteFile) throws IOException {
        try {

            File file = getDataFile(forceRewriteFile);
            if (file != null) {
                if (debug) {
                    Logger.getLogger(getClass().getName()).log(Level.FINE, "Opening zip file from " + file.getAbsolutePath());
                }
                return new ZipFile(file);
            } else {
                return null;
            }
        } catch (IOException ex) {
            if (debug) {
                Logger.getLogger(getClass().getName()).log(Level.FINE, "Error opening data file ");
                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
            }
            throw ex;
        }
    }

    @Override
    public InputStream getEntryDataStream(String name) throws IOException {
        try {
            final byte[] bytes = getEntryData(name);
            return new ByteArrayInputStream(bytes);
        } catch (IOException ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    @Override
    public boolean entryExists(String name) {
        entries();
        return entiries.containsKey(name);
    }

    @Override
    public boolean close() throws IOException {
        return true;
    }

    @Override
    public String getName() {
        return fileMeta != null ? fileMeta.getName() : file.getName();
    }

    @Override
    public File getFile() {
        synchronized (this) {
            try {
                return getDataFile(false);
            } catch (IOException ex) {
                if (debug) {
                    Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                    Logger.getLogger(getClass().getName()).log(Level.FINE, "Reset caches and try get file again");
                }
                resetDataFile();
                try {
                    return getDataFile(true);
                } catch (IOException ex1) {
                    if (debug) {
                        Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex1.getMessage(), ex1);
                    }
                    return null;
                }
            }
        }
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
    private Map<String, IJarEntry> entiries = null;

    @Override
    public Collection<IJarEntry> entries() {
        synchronized (this) {
            if (entiries == null) {
                try {
                    entiries = entriesImpl(false);
                } catch (Throwable ex) {//reason of exception is not known exactly. possible runtime error
                    if (debug) {
                        Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                        Logger.getLogger(getClass().getName()).log(Level.FINE, "Reset caches and try get entries again");
                    }
                    resetDataFile();
                    try {
                        entiries = entriesImpl(true);
                    } catch (Throwable ex2) {//serious error. had to be reported
                        if (debug) {
                            Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex2.getMessage(), ex2);
                        }
                        entiries = Collections.emptyMap();
                    }
                }
            }
            return entiries.values();
        }
    }

    private Map<String, IJarEntry> entriesImpl(boolean forceRewriteFile) throws IOException {
        synchronized (this) {
            if (debug) {
                Logger.getLogger(getClass().getName()).log(Level.FINE, "Try load entries from jar file");
            }
            ZipFile zip = null;
            try {
                zip = getZipFile(forceRewriteFile);
            } catch (IOException ex) {
                throw ex;
            }
            try {
                if (zip != null) {
                    final Map<String, IJarEntry> map = new HashMap<>(7);
                    try {
                        Enumeration<? extends ZipEntry> ze = zip.entries();
                        while (ze.hasMoreElements()) {
                            ZipEntry e = ze.nextElement();
                            EntryInfo info = new EntryInfo(e.getName(), e.isDirectory());
                            map.put(info.name, info);

                        }
                    } finally {
                    }
                    return map;
                } else {
                    return Collections.emptyMap();
                }
            } finally {
                try {
                    if (zip != null) {
                        zip.close();
                    }
                } catch (IOException e) {
                }
            }
        }
    }
}
