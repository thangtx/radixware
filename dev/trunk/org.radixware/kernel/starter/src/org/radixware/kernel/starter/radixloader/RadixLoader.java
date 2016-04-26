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

import java.io.*;
import java.lang.ref.WeakReference;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.jar.JarFile;
import javax.xml.stream.XMLStreamException;
import org.radixware.kernel.starter.Starter;
import org.radixware.kernel.starter.config.ConfigEntry;
import org.radixware.kernel.starter.config.ConfigFileAccessor;
import org.radixware.kernel.starter.filecache.CacheEntry;
import org.radixware.kernel.starter.filecache.FileCacheEntry;
import org.radixware.kernel.starter.filecache.JarCacheEntry;
import org.radixware.kernel.starter.log.SafeLogger;
import org.radixware.kernel.starter.meta.FileMeta;
import org.radixware.kernel.starter.meta.RevisionMeta;
import org.radixware.kernel.starter.utils.ExceptionTextFormatter;
import org.radixware.kernel.starter.utils.FileUtils;

/**
 * Facility to obtain data from files for application.
 *
 * <h3>Syncronization notes</h3> RadixLoader can be used by multiple threads.
 * <p>
 * Generally, there ara three layers of shared data: <ul> <li>RadixLoader state
 * - current revision, opened/closed.</li> <li>In memory data cache -
 * represented by protected {@link FileCache} instance, should always be
 * accessed under RadixLoader state lock.</li> <li>Data cache in file system -
 * used only by {@link RadixSVNLoader}, its documentation contains some notes
 * about file system data cache access policy.</li> </ul> </p>
 *
 */
public abstract class RadixLoader {

    public static class LocalFiles {

        private static final LocalFiles EMPTY_INSTANCE = new LocalFiles(Collections.<String, String>emptyMap());
        private static final String REMOTE_KEY = "remote";
        private static final String LOCAL_KEY = "local";

        private final Map<String, String> localFileByRemoteFile;

        private LocalFiles(final Map<String, String> localFiles) {
            localFileByRemoteFile = localFiles;
        }

        public static LocalFiles getInstance(final String localFileListPath) {
            if (localFileListPath == null || localFileListPath.isEmpty()) {
                return EMPTY_INSTANCE;
            } else {
                final File localFileListFile = new File(localFileListPath);
                if (!localFileListFile.exists() || localFileListFile.isDirectory()) {
                    traceError("Unable to load local file list from " + String.valueOf(localFileListFile) + ": it is not a text file");
                    return EMPTY_INSTANCE;
                }
                final Map<String, String> localFiles = new LinkedHashMap<>();
                try {
                    final ConfigFileAccessor accessor = ConfigFileAccessor.get(localFileListFile.getAbsolutePath(), "");
                    String remote = null;
                    for (ConfigEntry entry : accessor.getEntries()) {
                        if (remote == null) {
                            if (REMOTE_KEY.equals(entry.getKey())) {
                                remote = entry.getValue();
                            } else {
                                throw new IllegalStateException("Expected '" + REMOTE_KEY + "', but got '" + entry.getKey() + "'");
                            }
                        } else {
                            if (LOCAL_KEY.equals(entry.getKey())) {
                                localFiles.put(remote, entry.getValue());
                                remote = null;
                            } else {
                                throw new IllegalStateException("Expected '" + LOCAL_KEY + "', but got '" + entry.getKey() + "'");
                            }
                        }
                    }
                } catch (Exception ex) {
                    traceError("Unable to load local file list", ex);
                }
                if (localFiles.isEmpty()) {
                    return EMPTY_INSTANCE;
                } else {
                    traceEvent("Local file list is loaded from '" + localFileListFile + "'");
                    for (Map.Entry<String, String> entry : localFiles.entrySet()) {
                        boolean exists = false;
                        try {
                            exists = new File(entry.getValue()).exists();
                        } catch (Exception ex) {
                            //ignore
                        }
                        if (exists) {
                            traceEvent("File is locally overwritten: '" + entry.getKey() + "'='" + entry.getValue() + "'");
                        } else {
                            traceError("Replacement of '" + entry.getKey() + "' with '" + entry.getValue() + "' is invalid, local file does not exists");
                        }

                    }
                    return new LocalFiles(localFiles);
                }
            }
        }

        public String getLocalFilePath(final String remoteFile) {
            return localFileByRemoteFile.get(remoteFile);
        }

        public CacheEntry getCacheEntry(final String remoteFile) throws RadixLoaderException {
            final String localFile = localFileByRemoteFile.get(remoteFile);
            if (localFile == null) {
                return null;
            }
            try {
                if (localFile.endsWith(JAR_FILE_SUFFIX)) {
                    return new JarCacheEntry(new JarFile(localFile), false);
                } else {
                    return new FileCacheEntry(Files.readAllBytes(new File(localFile).toPath()));
                }
            } catch (IOException ex) {
                throw new RadixLoaderException("Unable to get file from local file list", ex);
            }
        }

        public Map<String, String> getAllReplacements() {
            return Collections.unmodifiableMap(localFileByRemoteFile);
        }
    }

    public static final String SERVER_GROUP_SUFFIX = "Server";
    public static final String COMMON_GROUP_SUFFIX = "Common";
    public static final String DDS_GROUP_SUFFIX = "Dds";
    public static final String EXPLORER_GROUP_SUFFIX = "Explorer";
    public static final String CLIENT_GROUP_SUFFIX = "Client";
    public static final String WEB_GROUP_SUFFIX = "Web";
    public static final Collection<String> SERVER_GROUP_SUFFIXES = Collections.unmodifiableList(Arrays.asList(new String[]{COMMON_GROUP_SUFFIX, DDS_GROUP_SUFFIX, SERVER_GROUP_SUFFIX}));
    public static final Collection<String> EXPLORER_GROUP_SUFFIXES = Collections.unmodifiableList(Arrays.asList(new String[]{COMMON_GROUP_SUFFIX, EXPLORER_GROUP_SUFFIX, DDS_GROUP_SUFFIX, CLIENT_GROUP_SUFFIX}));
    public static final Collection<String> WEB_GROUP_SUFFIXES = Collections.unmodifiableList(Arrays.asList(new String[]{COMMON_GROUP_SUFFIX, DDS_GROUP_SUFFIX, CLIENT_GROUP_SUFFIX, WEB_GROUP_SUFFIX}));
    private static final boolean INTERNAL_DEBUG_ENABLED;
    private static final boolean INTERNAL_EVENT_ENABLED;
    protected static final String JAR_FILE_SUFFIX = ".jar";

    static {
        final String level = System.getProperty("debug.radix.svn.loader");
        if ("debug".equals(level)) {
            INTERNAL_EVENT_ENABLED = true;
            INTERNAL_DEBUG_ENABLED = true;
        } else if ("event".equals(level) || level != null) {
            INTERNAL_EVENT_ENABLED = true;
            INTERNAL_DEBUG_ENABLED = false;
        } else {
            INTERNAL_DEBUG_ENABLED = false;
            INTERNAL_EVENT_ENABLED = false;
        }
    }

    public static class HowGetFile {

        public static enum Mode {

            GET_FROM_DIR,
            GET_FROM_SVN,
            GET_FROM_SVN_TO_DIR
        }
        private static final HowGetFile FROM_SVN = new HowGetFile(Mode.GET_FROM_SVN, null);
        public final Mode mode;
        public final File directory;

        private HowGetFile(Mode mode, File directory) {
            this.mode = mode;
            this.directory = directory;
        }

        public static HowGetFile fromDir(final File dir) {
            return new HowGetFile(Mode.GET_FROM_DIR, dir);
        }

        public static HowGetFile fromSvnToDir(final File dir) {
            return new HowGetFile(Mode.GET_FROM_SVN_TO_DIR, dir);
        }

        public static HowGetFile fromSvn() {
            return FROM_SVN;
        }
    };
    private static volatile RadixLoader theInstance;
    private final TempFiles tempFiles = new TempFiles();
    private volatile boolean isClosed = false;
    private final List<String> topLayerUris;
    protected volatile IActualizeController actualizeController;
    protected volatile RevisionMeta currentRevisionMeta;
    private final FileCache fileCache;
    private final Object tempFilesCacheSem = new Object();
    private final Map<String, File> tempFilesCache = new HashMap<>();
    protected Map<Long, WeakReference<RevisionMeta>> revisionMetas = new HashMap<>();
    protected final ReentrantReadWriteLock stateLock = new ReentrantReadWriteLock();
    private volatile String status = "Initializing...";
    private final File tempFilesDir;
    private final LocalFiles localFiles;

    public RadixLoader(final List<String> topLayerUris, final LocalFiles localFiles) {
        this.localFiles = localFiles;
        this.topLayerUris = Collections.unmodifiableList(new ArrayList(topLayerUris));
        fileCache = new FileCache(new DefaultAccessor(this));
        tempFilesDir = tempFiles.getContentDir();
    }

    protected void writeLock() {
        stateLock.writeLock().lock();
        if (isClosed) {
            stateLock.writeLock().unlock();
            throwClosedException();
        }
    }

    protected void writeUnlock() {
        stateLock.writeLock().unlock();
    }

    protected void readLock() {
        stateLock.readLock().lock();
        if (isClosed) {
            stateLock.readLock().unlock();
            throwClosedException();
        }
    }

    protected void readUnlock() {
        stateLock.readLock().unlock();
    }

    /**
     * If you want to override this method, you should always call super.close()
     * to clean up temp files created by
     * {@linkplain RadixLoader#createTempFile(java.lang.String)} and
     * {@linkplain RadixLoader#createTempFileWithExactName(java.lang.String)}
     */
    public void close() {
        writeLock();
        try {
            closeCache();
            clearFileCache();
            deleteTempFiles();
            isClosed = true;
        } finally {
            writeUnlock();
            status = "Closed";
        }
    }

    public String getStatus() {
        return status;
    }

    protected void setStatus(final String status) {
        this.status = status;
    }

    protected void clearFileCache() {
        readLock();
        try {
            fileCache.clear();
        } finally {
            readUnlock();
        }
    }

    public void setActualizeController(final IActualizeController actualizeController) {
        writeLock();
        try {
            this.actualizeController = actualizeController;
        } finally {
            writeUnlock();
        }
    }

    abstract public String getRepositoryUri(); // by BAO

    public List<String> listAllLayerUrisInRepository(final long revisionNum, final HowGetFile howGet) throws IOException {
        return Collections.emptyList();
    }

    public List<String> getTopLayerUris() {
        return topLayerUris;
    }

    public String getTopLayerUrisAsString() {
        final StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (String uri : topLayerUris) {
            if (first) {
                first = false;
            } else {
                sb.append(",");
            }
            sb.append(uri);
        }
        return sb.toString();
    }

    public boolean isClosed() {
        readLock();
        try {
            return isClosed;
        } catch (IllegalStateException ex) {
            return true;
        } finally {
            readUnlock();
        }
    }

    private void deleteTempFiles() {
        tempFiles.deleteRegisteredFiles();
    }

    public void closeCache() {
        clearFileCache();
    }

    public static RadixLoader getInstance() {
        return theInstance;
    }

    public static void setInstance(RadixLoader instance) {
        theInstance = instance;
    }

    //do not rename, used via reflection
    public abstract File getRoot();

    public RevisionMeta getCurrentRevisionMeta() {
        return currentRevisionMeta;
    }

    public RadixClassLoader createClassLoader(Set<String> groupTypes, ClassLoader parent) throws IOException {
        return createClassLoader(new RadixClassLoader.DefaultFactory(), groupTypes, parent);
    }

    /**
     * Get temporary file associated with specified key, if there is any. See
     * {@linkplain  RadixLoader#getOrCreateTempFile(java.lang.String, byte[])}.
     *
     * @return File object or null
     */
    public File getTempFile(final String key) {
        readLock();
        try {
            synchronized (tempFilesCacheSem) {
                return tempFilesCache.get(key);
            }
        } finally {
            readUnlock();
        }
    }

    /**
     * Creates temporary file associated with the specified key on disk. If file
     * for this key has already been created, than existing file is returned.
     * Actual file name can differ from key.
     * <p/>
     * API user SHOULD NEVER DELETE returned file. All created files will be
     * deleted upon RadixLoader.close() (at application shutdown or restart due
     * to kernel update).
     *
     * This method was designed to provide mechanism for storing long living
     * files with content totally dependent on key and required by many users
     * (such as ARTE threads on server).
     */
    public File getOrCreateTempFile(final String key, final byte[] data) throws IOException {
        readLock();
        try {
            synchronized (tempFilesCache) {
                final File existingFile = tempFilesCache.get(key);
                if (existingFile != null) {
                    return existingFile;
                }
                final File tempFile = createTempFile("radix-temp-file-");
                Files.write(tempFile.toPath(), data);
                tempFilesCache.put(key, tempFile);
                return tempFile;
            }
        } finally {
            readUnlock();
        }
    }

    public File createTempFile(final String prefix) {
        final File tmpFile = new File(tempFilesDir, prefix + UUID.randomUUID().toString());
        return tmpFile;
    }

    public File createTempFileWithExactName(final String name) {//before change file name computation rules check RfsJarFileDataProvider.java
        final File tmpFile = new File(tempFilesDir, name);
        return tmpFile;
    }

    //by BAO
    public RadixClassLoader createClassLoader(RadixClassLoader.Factory factory, Set<String> groupTypes, ClassLoader parent) throws IOException {
        readLock();
        try {
            return factory.createRadixClassLoader(currentRevisionMeta, this, groupTypes, parent);
        } finally {
            readUnlock();
        }
    }

    //by BAO
    public RadixClassLoader createClassLoader(long revisionNum, Set<String> groupTypes, ClassLoader parent) throws IOException, XMLStreamException {
        return createClassLoader(new RadixClassLoader.DefaultFactory(), revisionNum, groupTypes, parent);
    }

    public RadixClassLoader createClassLoader(RadixClassLoader.Factory factory, long revisionNum, Set<String> groupTypes, ClassLoader parent) throws IOException, XMLStreamException {
        return factory.createRadixClassLoader(getRevisionMeta(revisionNum), this, groupTypes, parent);
    }

    public RevisionMeta getRevisionMeta(final long revisionNum) throws RadixLoaderException {
        RevisionMeta meta = null;
        readLock();
        try {
            meta = findCachedRevMeta(revisionNum);
        } finally {
            readUnlock();
        }
        if (meta == null) {
            writeLock();
            try {
                meta = findCachedRevMeta(revisionNum);
                if (meta == null) {
                    try {
                        meta = createRevisionMetaImpl(revisionNum);
                    } catch (Exception ex) {
                        if (ex instanceof RadixLoaderException) {
                            throw (RadixLoaderException) ex;
                        } else {
                            throw new RadixLoaderException("Unable to load revision meta", ex);
                        }
                    }
                    revisionMetas.put(revisionNum, new WeakReference<>(meta));
                }
            } finally {
                writeUnlock();
            }
        }
        return meta;
    }

    private RevisionMeta findCachedRevMeta(final long revisionNum) {
        final WeakReference<RevisionMeta> ref = revisionMetas.get(revisionNum);
        if (ref != null) {
            return ref.get();
        }
        return null;
    }

    abstract protected RevisionMeta createRevisionMetaImpl(long revisionNum) throws IOException, XMLStreamException;

    /**
     * Acquiring appropriate locks is under the caller responsibility
     */
    protected RevisionMeta readRevisionMeta(
            final long revisionNum,
            final HowGetFile howGet,
            final Set<String> changedGroups,
            final Set<String> modifiedFiles,
            final Set<String> removedFiles) throws IOException, XMLStreamException {
        final RevisionMeta new_revision_meta = new RevisionMeta(new DefaultAccessor(this), revisionNum, howGet);
        new_revision_meta.getChanges(currentRevisionMeta, changedGroups, modifiedFiles, removedFiles);
        return new_revision_meta;
    }

    protected final void setCurrentRevisionMeta(final RevisionMeta meta) throws RadixLoaderException {
        writeLock();
        try {
            this.currentRevisionMeta = meta;
            if (meta != null) {
                revisionMetas.put(meta.getNum(), new WeakReference<>(meta));
            }
            afterSetCurrentRevisionMeta(meta);
            clearGarbageCollectedMetas();
        } finally {
            writeUnlock();
        }
    }

    protected void afterSetCurrentRevisionMeta(final RevisionMeta meta) throws RadixLoaderException {
    }

    private void clearGarbageCollectedMetas() {
        final Iterator<Map.Entry<Long, WeakReference<RevisionMeta>>> i
                = revisionMetas.entrySet().iterator();
        while (i.hasNext()) {
            if (i.next().getValue().get() == null) {
                i.remove();
            }
        }
    }

    public RevisionMeta findRevisionMeta(final long revisionNum) {
        readLock();
        try {
            return findCachedRevMeta(revisionNum);
        } finally {
            readUnlock();
        }

    }

    /**
     * Acquiring appropriate locks is under the caller responsibility
     */
    protected void removeFiles(final File dir, final Collection<String> fileNames) throws RadixLoaderException {
        if (fileNames.isEmpty()) {
            return;
        }
        for (String fileName : fileNames) {
            final File file = new File(dir, fileName);
            if (!FileUtils.deleteFile(file)) {
                throw new RadixLoaderException("Unable to remove " + file.getAbsolutePath());
            }
            internalDebug(fileName + " hase been removed from " + dir.getAbsolutePath());
        }
    }

    /**
     * Acquiring appropriate locks is under caller responsibility
     */
    protected void copyFiles(final Collection<String> fileNames, final File srcDir, final File targetDir, final Set<String> copiedFiles, boolean deleteOriginal) throws RadixLoaderException {
        if (fileNames == null || fileNames.isEmpty() || targetDir == null || srcDir == null) {
            return;
        }
        try {
            for (String fileName : fileNames) {
                final File sourceFile = new File(srcDir, fileName);
                if (sourceFile.exists()) {
                    final File targetFile = new File(targetDir, fileName);
                    targetFile.getParentFile().mkdirs();
                    try (FileOutputStream fos = new FileOutputStream(targetFile)) {
                        Files.copy(Paths.get(sourceFile.toURI()), fos);
                        internalDebug(sourceFile.getAbsolutePath() + " has been copied to " + targetFile.getAbsolutePath());
                    }
                    if (copiedFiles != null) {
                        copiedFiles.add(fileName);
                    }
                    if (deleteOriginal && !FileUtils.deleteFile(sourceFile)) {
                        throw new RadixLoaderException("Unable to delete " + fileName);
                    }
                }
            }
        } catch (Exception ex) {
            if (ex instanceof RadixLoaderException) {
                throw (RadixLoaderException) ex;
            }
            throw new RadixLoaderException("Unable to copy files", ex);
        }
    }

    public long getCurrentRevision() throws RadixLoaderException {
        readLock();
        try {
            return currentRevisionMeta == null ? -1 : currentRevisionMeta.getNum();
        } finally {
            readUnlock();
        }
    }

    public Set<String> actualize() throws RadixLoaderException {
        try {
            return actualize(null, null, null, null);
        } catch (Exception ex) {
            if (ex instanceof RadixLoaderException) {
                throw (RadixLoaderException) ex;
            } else {
                throw new RadixLoaderException("Error on actualize", ex);
            }
        }
    }

    private void throwClosedException() {
        throw new IllegalStateException("Loader has already been closed");
    }

    public byte[] readFileData(final FileMeta fileMeta, final RevisionMeta revMeta) throws IOException {
        readLock();
        try {
            return fileCache.readFileData(fileMeta, revMeta);
        } finally {
            readUnlock();
        }
    }

    byte[] readClassData(final String file_name, final Set<String> groupTypes, final RevisionMeta revisionMeta) throws IOException {
        readLock();
        try {
            return fileCache.readClassData(file_name, groupTypes, revisionMeta);
        } finally {
            readUnlock();
        }
    }

    public FileCache.ClassFileData readClassFileData(final String file_name, final Set<String> groupTypes, final RevisionMeta revisionMeta) throws IOException {
        readLock();
        try {
            return fileCache.readClassFileData(file_name, groupTypes, revisionMeta);
        } finally {
            readUnlock();
        }
    }

    public byte[] readResourceData(final String file_name, final Set<String> groupTypes, String archive, final RevisionMeta revisionMeta) throws IOException {
        readLock();
        try {
            return fileCache.readResourceData(file_name, groupTypes, archive, revisionMeta);
        } finally {
            readUnlock();
        }
    }

    public Set<String> actualize(Collection<RevisionMeta> oldRevisionMeta, Set<String> modifiedFiles, Set<String> removedFiles, Set<String> preloadGroupSuffixes) throws RadixLoaderException {
        setStatus("Actualizing");
        try {
            return actualizeImpl(oldRevisionMeta, modifiedFiles, removedFiles, preloadGroupSuffixes);
        } finally {
            setStatus("Actualization finished");
        }
    }

    /**
     * Acquiring appropriate locks is under implementation responsibility
     */
    abstract protected Set<String> actualizeImpl(Collection<RevisionMeta> oldRevisionMeta, Set<String> modifiedFiles, Set<String> removedFiles, Set<String> preloadGroupSuffixes) throws RadixLoaderException;

    /**
     * Acquiring appropriate locks is under implementation responsibility
     */
    abstract protected CacheEntry getFileImpl(String file, long revisionNum) throws RadixLoaderException;

    abstract protected CacheEntry getFileImpl(String file, long revision, HowGetFile howGet) throws RadixLoaderException;

    /**
     * @return some string that describes this loader (wich cache dir it uses,
     * etc.). Designed to be used for server logging.
     */
    abstract public String getDescription();

    public String getNativeLibrary(FileMeta fileMeta, RevisionMeta revisionMeta) throws RadixLoaderException {
        try {
            final String localFilePath = localFiles.getLocalFilePath(fileMeta.getName());
            if (localFilePath != null) {
                return localFilePath;
            }
            final File tmp_file = createTempFileWithExactName(System.mapLibraryName("radix-starter-nativelib-" + UUID.randomUUID().toString()));
            final InputStream in = new ByteArrayInputStream(readFileData(fileMeta, revisionMeta));
            Files.copy(in, Paths.get(tmp_file.toURI()), StandardCopyOption.REPLACE_EXISTING);
            return tmp_file.getPath();
        } catch (IOException e) {
            throw new RadixLoaderException("Exception on RadixLoader.getNativeLibrary(): " + ExceptionTextFormatter.getExceptionMess(e), e);
        }
    }

    protected final CacheEntry getLocalFile(final String localFile) throws RadixLoaderException {
        return localFiles.getCacheEntry(localFile);
    }
    
    public byte[] export(final String file, final long revNum) throws RadixLoaderException {
        throw new UnsupportedOperationException("Export is not supported by " + getClass().getName());
    }

    protected static class DefaultAccessor implements RadixLoaderAccessor {

        private final RadixLoader radixLoader;

        public DefaultAccessor(RadixLoader radixLoader) {
            this.radixLoader = radixLoader;
        }

        @Override
        public CacheEntry getFile(String file, long revision, HowGetFile howGet) throws RadixLoaderException {
            return radixLoader.getFileImpl(file, revision, howGet);
        }

        @Override
        public CacheEntry getFile(String file, long revision) throws RadixLoaderException {
            return radixLoader.getFileImpl(file, revision);
        }

        @Override
        public RadixLoader getLoader() {
            return radixLoader;
        }
    }

    protected String getInternalDescription() {
        if (Starter.isRoot()) {
            return "Root starter loader";
        } else {
            return "App starter loader";
        }
    }

    public LocalFiles getLocalFiles() {
        return localFiles;
    }

    protected static void traceDebug(final String message) {
        SafeLogger.getInstance().debugFromSource(getDebugDesc(), message);
        internalDebug(message);
    }

    protected static void traceEvent(final String message) {
        traceEvent(message, null);
    }

    protected static void traceEvent(final String message, final Throwable t) {
        SafeLogger.getInstance().eventFromSource(getDebugDesc(), message, t);
        internalEvent(message, t);
    }

    protected static void traceError(final String message) {
        traceError(message, null);
    }

    protected static void traceError(final String message, final Throwable t) {
        SafeLogger.getInstance().errorFromSource(getDebugDesc(), message, t);
        internalEvent(message, t);
    }

    protected static String getDebugDesc() {
        final RadixLoader instance = RadixLoader.getInstance();
        return (Starter.isRoot() ? "Root " : "App ") + (instance == null ? "loader" : instance.getClass().getSimpleName());
    }

    protected static void internalDebug(final String message) {
        internalDebug(message, null);
    }

    protected static void internalEvent(final String message) {
        internalEvent(message, null);
    }

    protected static void internalEvent(final String message, final Throwable ex) {
        if (INTERNAL_EVENT_ENABLED) {
            writeInternalLog(message, ex);
        }
    }

    private static void writeInternalLog(final String message, final Throwable ex) {
        System.out.println(new Date() + " " + getDebugDesc() + ": " + message + (ex == null ? "" : ": " + ExceptionTextFormatter.throwableToString(ex)));
    }

    protected static void internalDebug(final String message, final Throwable ex) {
        if (INTERNAL_DEBUG_ENABLED) {
            writeInternalLog(message, ex);
        }
    }
}
