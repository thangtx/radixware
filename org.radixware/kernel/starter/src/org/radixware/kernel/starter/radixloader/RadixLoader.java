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
import org.radixware.kernel.starter.StarterArguments;
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
import org.radixware.kernel.starter.utils.SystemTools;

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
    protected static final String ZIP_FILE_SUFFIX = ".zip";
    protected static final long MAX_EXPORTED_FILE_BYTES = Long.getLong("rdx.loader.max.exported.file.bytes", 100l * 1024 * 1024);

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

    private static volatile RadixLoader theInstance;
    private final TempFiles tempFiles = new TempFiles();
    private volatile boolean isClosed = false;
    private final List<String> topLayerUris;
    protected volatile IActualizeController actualizeController;
    protected volatile RevisionMeta currentRevisionMeta;
    protected volatile long prevRevisionNum = -1;
    private final FileCache fileCache;
    private final Object tempFilesCacheSem = new Object();
    private final Map<String, File> tempFilesCache = new HashMap<>();
    protected Map<Long, WeakReference<RevisionMeta>> revisionMetas = new HashMap<>();
    protected final ReentrantReadWriteLock stateLock = new ReentrantReadWriteLock();
    private volatile String status = "Initializing...";
    private final File tempFilesDir;
    private final LocalFiles localFiles;
    private volatile Boolean useHardlinks;

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

    public void clearFileCache() {
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

    public long getLatestRevision() {
        return -1; //throw new UnsupportedOperationException();
    }

    public long getPreloadedRevision() {
        return -1;
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

    public void copyMaybeHardlink(final File dest, final File src) throws IOException {
        if (!src.exists()) {
            throw new FileNotFoundException(src.getPath());
        }
        final Boolean useHardlinkSnapshot = useHardlinks;
        boolean tryHardlink = false;
        if (useHardlinkSnapshot == null) {
            if (Starter.getArguments() != null && Starter.getArguments().getStarterParameters() != null && Starter.getArguments().getStarterParameters().containsKey(StarterArguments.DISABLE_HARDLINKS)) {
                useHardlinks = false;
            } else if (!SystemTools.isWindows || (SystemTools.isWindows && Starter.getArguments() != null && Starter.getArguments().getStarterParameters() != null && Starter.getArguments().getStarterParameters().containsKey(StarterArguments.ENABLE_HARDLINKS_ON_WINDOWS))) {
                tryHardlink = true;
            }
        } else {
            tryHardlink = useHardlinkSnapshot;
        }
        if (tryHardlink) {
            try {
                Files.createLink(dest.toPath(), src.toPath());
                return;
            } catch (IOException ex) {
                useHardlinks = false;
                SafeLogger.getInstance().info(RadixLoader.class, "Unable to use hardlinks to copy file from '" + src.getParentFile().getAbsolutePath() + "' to '" + dest.getParentFile().getAbsolutePath() + "."
                        + "\nThis will not affect application logic."
                        + "\nYou can disable attempts to use hardlinks (and suppress this message) by passing '-disableHardlinks' parameter to starter. Hardlink creation error message: " + ex.getMessage());
            }
        }
        try (InputStream in = new FileInputStream(src)) {
            Files.copy(in, Paths.get(dest.toURI()), StandardCopyOption.REPLACE_EXISTING);
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
        return getRevisionMeta(revisionNum, ERevisionMetaType.FULL);
    }

    public RevisionMeta getRevisionMeta(final long revisionNum, final ERevisionMetaType metaType) throws RadixLoaderException {
        RevisionMeta meta = null;
        readLock();
        try {
            meta = findCachedRevMeta(revisionNum);
        } finally {
            readUnlock();
        }
        if (meta == null) {
            if (metaType == ERevisionMetaType.FULL) {
                writeLock();
            } else {
                readLock();
            }
            try {
                meta = findCachedRevMeta(revisionNum);
                if (meta == null) {
                    try {
                        meta = createRevisionMetaImpl(revisionNum, metaType);
                    } catch (Exception ex) {
                        if (ex instanceof RadixLoaderException) {
                            throw (RadixLoaderException) ex;
                        } else {
                            throw new RadixLoaderException("Unable to load revision meta", ex);
                        }
                    }
                    if (metaType == ERevisionMetaType.FULL) {
                        revisionMetas.put(revisionNum, new WeakReference<>(meta));
                    }
                }
            } finally {
                if (metaType == ERevisionMetaType.FULL) {
                    writeUnlock();
                } else {
                    readUnlock();
                }
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

    abstract protected RevisionMeta createRevisionMetaImpl(long revisionNum, ERevisionMetaType type) throws IOException, XMLStreamException;

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

    protected RevisionMeta readRevisionMeta(
            final long revisionNum,
            final HowGetFile howGet,
            final Set<String> changedGroups,
            final Set<String> modifiedFiles,
            final Set<String> removedFiles,
            final String explicitKernelVersionStr,
            final String explicitAppVersionStr,
            final String kernelRevisions,
            final String compatibleKernelRevisions) throws IOException, XMLStreamException {
        final RevisionMeta new_revision_meta = new RevisionMeta(new DefaultAccessor(this), revisionNum, howGet, explicitKernelVersionStr, explicitAppVersionStr, kernelRevisions, compatibleKernelRevisions);
        new_revision_meta.getChanges(currentRevisionMeta, changedGroups, modifiedFiles, removedFiles);
        return new_revision_meta;
    }

    protected final void setCurrentRevisionMeta(final RevisionMeta meta) throws RadixLoaderException {
        writeLock();
        try {
            if (currentRevisionMeta != null) {
                prevRevisionNum = currentRevisionMeta.getNum();
            }
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

    public long getPreviousRevision() {
        readLock();
        try {
            return prevRevisionNum;
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

    public byte[] readFileData(final FileMeta fileMeta, final RevisionMeta revMeta, final long cacheTimeout) throws IOException {
        readLock();
        try {
            return fileCache.readFileData(fileMeta, revMeta, cacheTimeout);
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

    public byte[] readResourceData(final String file_name, final Set<String> groupTypes, String archive, final RevisionMeta revisionMeta, final long cacheTimeout) throws IOException {
        readLock();
        try {
            return fileCache.readResourceData(file_name, groupTypes, archive, revisionMeta, cacheTimeout);
        } finally {
            readUnlock();
        }
    }
    
    public Set<String> actualize(Collection<RevisionMeta> oldRevisionMeta, Set<String> modifiedFiles, Set<String> removedFiles, Set<String> preloadGroupSuffixes) throws RadixLoaderException {
        return actualize(-1, oldRevisionMeta, modifiedFiles, removedFiles, preloadGroupSuffixes);
    }

    public Set<String> actualize(final long targetRevNum, Collection<RevisionMeta> oldRevisionMeta, Set<String> modifiedFiles, Set<String> removedFiles, Set<String> preloadGroupSuffixes) throws RadixLoaderException {
        actualizeAuxInfo();
        setStatus("Actualizing");
        try {
            return actualizeImpl(targetRevNum, oldRevisionMeta, modifiedFiles, removedFiles, preloadGroupSuffixes);
        } catch (Exception ex) {
            if (ex instanceof RadixLoaderException) {
                throw (RadixLoaderException) ex;
            }
            throw new RadixLoaderException("Error on version actualizing", ex);
        } finally {
            setStatus("Actualization finished");
        }
    }

    /**
     * Acquiring appropriate locks is under implementation responsibility
     */
    abstract protected Set<String> actualizeImpl(final long targetRevNum, Collection<RevisionMeta> oldRevisionMeta, Set<String> modifiedFiles, Set<String> removedFiles, Set<String> preloadGroupSuffixes) throws RadixLoaderException;

    /**
     * Update auxiliary information about repository (if any), but do not update
     * version.
     *
     * This procedure is always called as first part of "actualize" procedure.
     *
     * @throws RadixLoaderException
     */
    public void actualizeAuxInfo() throws RadixLoaderException {
        setStatus("Actualizing auxiliary information");
        try {
            actualizeAuxInfoImpl();
        } catch (Exception ex) {
            if (ex instanceof RadixLoaderException) {
                throw (RadixLoaderException) ex;
            }
            throw new RadixLoaderException("Error on actualizing auxiliary reposiotry infomation", ex);
        } finally {
            setStatus("Actualizing auxiliary information finished");
        }
    }

    protected void actualizeAuxInfoImpl() throws RadixLoaderException {
    }

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
            final String localFilePath = localFiles.getLocalFilePath(fileMeta.getName(), revisionMeta.getNum());
            if (localFilePath != null) {
                return localFilePath;
            }
            final File tmp_file = createTempFileWithExactName(System.mapLibraryName("radix-starter-nativelib-" + UUID.randomUUID().toString()));
            final InputStream in = new ByteArrayInputStream(readFileData(fileMeta, revisionMeta, 0));
            Files.copy(in, Paths.get(tmp_file.toURI()), StandardCopyOption.REPLACE_EXISTING);
            return tmp_file.getPath();
        } catch (IOException e) {
            throw new RadixLoaderException("Exception on RadixLoader.getNativeLibrary(): " + ExceptionTextFormatter.getExceptionMess(e), e);
        }
    }

    @Deprecated
    protected final CacheEntry getLocalFile(final String localFile) throws RadixLoaderException {
        return localFiles.getCacheEntry(localFile, -1);
    }
    
    protected final CacheEntry getLocalFile(final String localFile, final long revisionNum) throws RadixLoaderException {
        return localFiles.getCacheEntry(localFile, revisionNum);
    }

    public byte[] export(final String file, final long revNum) throws RadixLoaderException {
        return exportFile(file, revNum).getData();
    }

    public IRepositoryEntry exportFile(final String file, final long revNum) throws RadixLoaderException {
        throw new UnsupportedOperationException("Export is not supported by " + getClass().getName());
    }

    public Collection<IRepositoryEntry> listDir(final String path, final long revNum, final boolean downloadFileData) throws RadixLoaderException {
        throw new UnsupportedOperationException("List is not supported by " + getClass().getName());
    }

    public String concatPath(final String path1, final String path2) {
        if (path1 == null || path1.isEmpty()) {
            return path2;
        }
        if (path2 == null || path2.isEmpty()) {
            return path1;
        }
        final String separator = "/";
        if (path1.endsWith(separator)) {
            return path1 + path2;
        }
        return path1 + separator + path2;
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
    
    public List<ReplacementFile> getUsedLocalReplacements(final long revNumber) {
        return localFiles.getUsedReplacements(revNumber);
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
        System.out.println(new Date() + " " + getDebugDesc() + ": " + message + (ex == null ? "" : ":\n" + ExceptionTextFormatter.throwableToString(ex)));
    }

    protected static void internalDebug(final String message, final Throwable ex) {
        if (INTERNAL_DEBUG_ENABLED) {
            writeInternalLog(message, ex);
        }
    }
      
    public static class LocalFiles {

        private static final LocalFiles EMPTY_INSTANCE = new LocalFiles(null, null);
        private static final String REMOTE_KEY = "remote";
        private static final String LOCAL_KEY = "local";
        private static final String REPLACEMENT_CFG = "replacement.cfg";

        private final String localReplacementsDir;
        private final String localFileListPath;
        private final List<ReplacementFile> uncompatibleReplacements;
        private final List<ConflictInfo> replacementConflicts;
        private final Map<Long, List<ReplacementFile>> replacementsByRevNum;
        private final List<ReplacementFile> allReplacements;
        
        private boolean localFileListParsed = false;
        private File rootTmpReplacementsDir;

        private LocalFiles(final String localFileListPath, final String replacementDirPath) {
            this.localFileListPath = localFileListPath;
            localReplacementsDir = replacementDirPath;
            replacementConflicts = new ArrayList<>();
            uncompatibleReplacements = new ArrayList<>();
            allReplacements = new ArrayList<>();
            replacementsByRevNum = new HashMap<>();
        }

        public static LocalFiles getInstance(final String localFileListPath) {
            return getInstance(localFileListPath, null);
        }
        
        public static LocalFiles getInstance(final String localFileListPath, final String localReplacementsDir) {
            if (localFileListPath == null && localReplacementsDir == null) {
                return EMPTY_INSTANCE;
            }
            return new LocalFiles(localFileListPath, localReplacementsDir);
        }
        
        private static List<ReplacementFile> scanLocalReplacementDir(File rootTmpDir, String localReplacementDirPath) {
            if (localReplacementDirPath == null || localReplacementDirPath.isEmpty()) {
                return Collections.<ReplacementFile>emptyList();
            }
            final File replacementSrcDir = new File(localReplacementDirPath);
            if (!replacementSrcDir.exists() || !replacementSrcDir.isDirectory()) {
                traceError("Unable to load files from replacement directory " + String.valueOf(replacementSrcDir) + ": directory does not exists");
                return Collections.<ReplacementFile>emptyList();
            }
            if (!rootTmpDir.exists() || !rootTmpDir.isDirectory()) {
                traceError("Unable to create temporary directory for replacements");
                return Collections.<ReplacementFile>emptyList();
            }
            
            final FileFilter onlyZipFiles = new FileFilter() {
                @Override
                public boolean accept(File fName) {
                    return fName.getName().toLowerCase().endsWith(ZIP_FILE_SUFFIX);
                }
            };
            
            final List<ReplacementFile> replacementFiles = new ArrayList<>();
            
            final String revDirName = Long.toString(System.currentTimeMillis()); //use current timestamp to generate unique dir name for revision
            final File revTmpDir = new File(rootTmpDir, revDirName);
            revTmpDir.delete();
            revTmpDir.mkdirs();
            
            for (final File zipFile : replacementSrcDir.listFiles(onlyZipFiles)) {
                try (FileInputStream fis = new FileInputStream(zipFile)) {
                    final File tmpReplacementDir = new File(revTmpDir, zipFile.getName().substring(0, zipFile.getName().length() - ZIP_FILE_SUFFIX.length()));
                    tmpReplacementDir.delete();
                    tmpReplacementDir.mkdirs();
                    
                    FileUtils.unpackZipStream(fis, tmpReplacementDir, new ArrayList());
                    if (tmpReplacementDir.isDirectory()) {
                        final List<ReplacementEntry> repEntries = new ArrayList<>();
                        final List<File> cfgFileContainer = new ArrayList<>(1);
                        tmpReplacementDir.listFiles(new FileFilter() {
                            @Override
                            public boolean accept(File file) {
                                if (REPLACEMENT_CFG.equals(file.getName().toLowerCase())) {
                                    cfgFileContainer.add(file);
                                } else if (file.isDirectory()) {
                                    file.listFiles(this);
                                } else {
                                    final String remote = tmpReplacementDir.toPath().normalize().relativize(file.toPath()).toString();
                                    repEntries.add(new ReplacementEntry(remote, file.getAbsolutePath()));
                                }
                                return true;
                            }
                        });
                        
                        final ReplacementFile repFile = ReplacementFile.newInstance(zipFile.getAbsolutePath(), cfgFileContainer.isEmpty() ? null : cfgFileContainer.get(0).getAbsolutePath());
                        if (repFile != null) {
                            for (ReplacementEntry e : repEntries) {
                                repFile.addEntry(e);
                            }
                            replacementFiles.add(repFile);
                        }
                    }
                } catch (Exception ex) {
                     traceError("Unable to load files from replacement archive: " + zipFile.getName(), ex);
                }
            }
            return replacementFiles;
        }
        
        private static List<ReplacementFile> parseLocalFileList(String localFileListPath) {
            if (localFileListPath == null || localFileListPath.isEmpty()) {
                return Collections.emptyList();
            }
            final File localFileListFile = new File(localFileListPath);
            if (!localFileListFile.exists() || localFileListFile.isDirectory()) {
                traceError("Unable to load local file list from " + String.valueOf(localFileListFile) + ": it is not a text file");
                return Collections.emptyList();
            }
            final List<ReplacementEntry> entries = new ArrayList<>();
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
                            entries.add(new ReplacementEntry(remote, entry.getValue()));
                            remote = null;
                        } else {
                            throw new IllegalStateException("Expected '" + LOCAL_KEY + "', but got '" + entry.getKey() + "'");
                        }
                    }
                }
            } catch (Exception ex) {
                traceError("Unable to load local file list", ex);
            }
            if (entries.isEmpty()) {
                return Collections.emptyList();
            } else {
                final ReplacementFile repFile = ReplacementFile.newInstance(localFileListPath, null);
                if (repFile == null) {
                    return Collections.emptyList();
                }
                traceEvent("Local file list is loaded from '" + localFileListFile + "'");
                for (ReplacementEntry entry : entries) {
                    boolean exists = false;
                    try {
                        exists = new File(entry.getLocal()).exists();
                    } catch (Exception ex) {
                        //ignore
                    }
                    if (exists) {
                        repFile.addEntry(entry);
                        traceEvent("File is locally overwritten: '" + entry.getRemote() + "'='" + entry.getLocal() + "'");
                    } else {
                        traceError("Replacement of '" + entry.getRemote() + "' with '" + entry.getLocal() + "' is invalid, local file does not exist");
                    }
                }
                return Arrays.asList(repFile);
            }
        }
        
        //For RadixDirLoader only
        public void setRevisionNum(long revisionNum) {
            final List<ReplacementFile> repFiles = replacementsByRevNum.remove(0l);
            if (repFiles != null) {
                replacementsByRevNum.put(revisionNum, repFiles);
            }
        }
        
        public void loadForVersion(long revision, String kernLayerVers, String appLayerVers) {
            if (localFileListPath != null) {
                if (!localFileListParsed) {
                    allReplacements.clear();
                    allReplacements.addAll(parseLocalFileList(localFileListPath));
                    localFileListParsed = true;
                }
            } else if (replacementsByRevNum.get(revision) == null) {
                allReplacements.clear();
                uncompatibleReplacements.clear();
                replacementConflicts.clear();
                
                if (rootTmpReplacementsDir == null) {
                    rootTmpReplacementsDir = RadixLoader.getInstance().createTempFile("local-replacement-dir");
                    rootTmpReplacementsDir.delete();
                    rootTmpReplacementsDir.mkdirs();
                }
                allReplacements.addAll(scanLocalReplacementDir(rootTmpReplacementsDir, localReplacementsDir));
                
                final List<ReplacementFile> revReplacements = new ArrayList<>();
                for (ReplacementFile repFile : allReplacements) {
                    if (repFile.compatibleFor(kernLayerVers, appLayerVers)) {
                        addIfNotConflicted(repFile, revReplacements);
                    } else {
                        uncompatibleReplacements.add(repFile);
                    }
                }
                replacementsByRevNum.put(revision, revReplacements);
            }
        }
        
        private void addIfNotConflicted(ReplacementFile fileToAdd, List<ReplacementFile> revReplacements) {
            final List<ConflictInfo> conflicts = new ArrayList<>();
            for (ReplacementFile file : revReplacements) {
                final Set<String> conflictEntries = file.checkEntriesConflict(fileToAdd);
                if (!conflictEntries.isEmpty()) {
                    conflicts.add(registerConflict(fileToAdd, file, conflictEntries));
                }
            }
            if (conflicts.isEmpty()) {
                revReplacements.add(fileToAdd);
            } else {
                boolean fileToAddIsNewerThanAllOther = true;
                for (ConflictInfo conflict : conflicts) {
                    if (!fileToAdd.isNewer(conflict.otherFile)) {
                        fileToAddIsNewerThanAllOther = false;
                        break;
                    }
                }
                if (fileToAddIsNewerThanAllOther) {
                    for (ConflictInfo conflict : conflicts) {
                        revReplacements.remove(conflict.otherFile);
                    }
                    revReplacements.add(fileToAdd);
                }
            }
        }
        
        private ConflictInfo registerConflict(ReplacementFile repFile, ReplacementFile otherFile, Set<String> conflictEntries) {
            final ConflictInfo conflictInfo = new ConflictInfo(repFile, otherFile, conflictEntries);
            replacementConflicts.add(conflictInfo);
            return conflictInfo;
        }

        @Deprecated
        public String getLocalFilePath(final String remoteFile) {
            return getLocalFilePath(remoteFile, -1);
        }
        
        public String getLocalFilePath(final String remoteFile, final long revision) {
            try {
                final ReplacementEntry repEntry = findReplacement(remoteFile, revision);
                return repEntry != null ? repEntry.getLocal() : null;
            } catch (RadixLoaderException ex) {
                traceError("Unable to load replacement info", ex);
                return null;
            }
        }

        @Deprecated
        public CacheEntry getCacheEntry(final String remoteFile) throws RadixLoaderException {
            return getCacheEntry(remoteFile, -1);
        }
        
        public CacheEntry getCacheEntry(final String remoteFile, final long revision) throws RadixLoaderException {
            final ReplacementEntry repEntry = findReplacement(remoteFile, revision);
            if (repEntry != null) {
                final String localFile = repEntry.getLocal();
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
            return null;
        }
        
        private ReplacementEntry findReplacement(final String remoteFile, final long revNumber) throws RadixLoaderException {
            final List<ReplacementFile> repFiles = getUsedReplacements(revNumber);
            if (repFiles != null) {
                for (ReplacementFile repFile : repFiles) {
                    final ReplacementEntry repEntry = repFile.getByRemotePath(remoteFile);
                    if (repEntry != null) {
                        return repEntry;
                    }
                }
            }
            return null;
        }
                
        @Deprecated
        public Map<String, String> getAllReplacements() {
            final List<ReplacementFile> repFiles = getUsedReplacements(-1);
            if (repFiles == null) {
                return Collections.emptyMap();
            }

            final Map<String, String> remote2Local = new HashMap<>();
            for (ReplacementFile repFile : repFiles) {
                for (ReplacementEntry repEntry : repFile.getEntries()) {
                    remote2Local.put(repEntry.getRemote(), repEntry.getLocal());
                }
            }
            return remote2Local;
        }
        
        public List<ReplacementFile> getAllReplacementsEx() {
            return allReplacements;
        }
        
        public List<ReplacementFile> getUsedReplacements(final long revNumber) {
            return localFileListPath != null ? allReplacements : replacementsByRevNum.get(revNumber);
        }
        
        public List<ReplacementFile> getUncompatibleReplacements() {
            return uncompatibleReplacements;
        }
        
        public List<ConflictInfo> getReplacementConflicts() {
            return replacementConflicts;
        }
                
        public static class ConflictInfo {
            
            private final ReplacementFile repFile;
            private final ReplacementFile otherFile;
            private final Set<String> conflictEntries;

            public ConflictInfo(ReplacementFile repFile, ReplacementFile otherFile, Set<String> conflictEntries) {
                this.repFile = repFile;
                this.otherFile = otherFile;
                this.conflictEntries = conflictEntries;
            }

            public ReplacementFile getRepFile() {
                return repFile;
            }

            public ReplacementFile getOtherFile() {
                return otherFile;
            }

            public Set<String> getConflictEntries() {
                return conflictEntries;
            }
            
        }
    }

    public static class HowGetFile {

        public static enum Mode {

            GET_FROM_DIR,
            GET_FROM_SVN,
            GET_FROM_SVN_TO_DIR
        }
        private static final HowGetFile FROM_SVN = new HowGetFile(Mode.GET_FROM_SVN, null, false);
        private static final HowGetFile FROM_SVN_SKIP_LOCAL_CACHE = new HowGetFile(Mode.GET_FROM_SVN, null, true);
        public final Mode mode;
        public final File directory;
        public final boolean skipLocalSvnCache;

        private HowGetFile(Mode mode, File directory, final boolean skipLocalSvnCache) {
            this.mode = mode;
            this.directory = directory;
            this.skipLocalSvnCache = skipLocalSvnCache;
        }

        public static HowGetFile fromDir(final File dir) {
            return new HowGetFile(Mode.GET_FROM_DIR, dir, false);
        }

        public static HowGetFile fromSvnToDir(final File dir) {
            return new HowGetFile(Mode.GET_FROM_SVN_TO_DIR, dir, false);
        }

        public static HowGetFile fromSvn() {
            return FROM_SVN;
        }

        public static HowGetFile fromSvnToDirSkipLocalCache(final File dir) {
            return new HowGetFile(Mode.GET_FROM_SVN_TO_DIR, dir, true);
        }

        public static HowGetFile fromSvnSkipLocalCache() {
            return FROM_SVN_SKIP_LOCAL_CACHE;
        }
    };

    protected static class FileRepositoryEntry implements IRepositoryEntry {

        private final String path;
        private final byte[] data;
        private final long revNum;
        private final long revCreationTimeMillis;
        private final String name;

        public FileRepositoryEntry(String path, String name, byte[] data, long revNum, long revCreationTimeMillis) {
            this.path = path;
            this.data = data;
            this.revNum = revNum;
            this.revCreationTimeMillis = revCreationTimeMillis;
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public boolean isDirectory() {
            return false;
        }

        @Override
        public String getPath() {
            return path;
        }

        @Override
        public long getRevisionNum() {
            return revNum;
        }

        @Override
        public long getRevisionCreationTimeMillis() {
            return revCreationTimeMillis;
        }

        @Override
        public byte[] getData() {
            return data;
        }
    }

    protected static class DirRepositoryEntry implements IRepositoryEntry {

        private final String path;
        private final long revNum;
        private final long revCreationTimeMillis;
        private final String name;

        public DirRepositoryEntry(String path, String name, long revNum, long revCreationTimeMillis) {
            this.path = path;
            this.revNum = revNum;
            this.revCreationTimeMillis = revCreationTimeMillis;
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public boolean isDirectory() {
            return true;
        }

        @Override
        public String getPath() {
            return path;
        }

        @Override
        public long getRevisionNum() {
            return revNum;
        }

        @Override
        public long getRevisionCreationTimeMillis() {
            return revCreationTimeMillis;
        }

        @Override
        public byte[] getData() {
            return null;
        }

    }
}
