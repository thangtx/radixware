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
import java.lang.reflect.Method;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.X509TrustManager;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLStreamException;
import org.radixware.kernel.common.svn.RadixSvnException;
import org.radixware.kernel.common.svn.SVNRepositoryAdapter;
import org.radixware.kernel.common.svn.client.SvnEntry;
import org.radixware.kernel.starter.Starter;
import org.radixware.kernel.starter.StarterArguments;
import org.radixware.kernel.starter.config.ConfigEntry;
import org.radixware.kernel.starter.config.ConfigFileAccessor;
import org.radixware.kernel.starter.config.ConfigFileParseException;
import org.radixware.kernel.starter.config.ConfigFileParser;
import org.radixware.kernel.starter.filecache.CacheEntry;
import org.radixware.kernel.starter.filecache.FileCacheEntry;
import org.radixware.kernel.starter.filecache.JarCacheEntry;
import org.radixware.kernel.starter.meta.FileMeta;
import org.radixware.kernel.starter.meta.LayerMeta;
import org.radixware.kernel.starter.meta.RevisionMeta;
import org.radixware.kernel.starter.utils.FileUtils;
import org.radixware.kernel.starter.utils.HexConverter;
import org.radixware.kernel.starter.utils.SystemTools;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * Loads files from SVN, maintains cache in local file system.
 *
 * RadixSvnLoader holds file system cache which is divided into three parts:
 * <ul> <li> actually cached files from SVN;</li> <li> backup directory - files
 * that should be removed during actualization are backed up here, so they can
 * be restored in case actualization fails;</li> <li> precache directory -
 * during actualization, new files are downloaded there before they will be
 * moved to the actual cache</li> </ul>
 *
 * File system cache should be accessed only under RadixLoader state lock. Reads
 * from file system cache can be parallel, but all writes are synchronized.
 *
 */
public class RadixSVNLoader extends RadixLoader {

    private static final String REPLICATIONS_FILE = "config/replication.xml";
    private static final DateFormat REVISION_DATE_FORMAT_OLD = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss:SSS");
    private static final DateFormat REVISION_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
    private static final String MASTER_SVN_URL_ATTR = "MasterSvnUrl";
    private static final String SVN_HOME_URL_ATTR = "SvnHomeUrl";
    private static final int SLEEP_BETWEEN_SVN_PINGS_MILLIS = 60000;
    //
    private final String primarySvnUrl;
    private final List<String> additionalSvnUrls = new ArrayList<>();
    private final CacheMeta cacheMeta;
    private final BinaryDataSupplier dataSupplier;
    private volatile boolean preloadScheduled = false;
    private final Set<String> defaultPreloadedGroupSuffixes = new HashSet();
    //new revision meta, loaded to precache and awaiting for acception by actualizeController, can be null
    //guarded by actualizeLock
    private PendingRevisionInfo pendingRevision;
    private final Object actualizeLock = new Object();
    private final SvnPingerThread svnPingerThread;

    public RadixSVNLoader(final String primarySvnUrl, final List<String> additionalSvnUrls, final List<String> topLayerUris, final String authUser, final String authPassword, final String sshKeyFile, final String sshKeyPassword, final String svnServerCertFile, final boolean svnLog, final File fileCacheDir, final LocalFiles localFiles) throws RadixSvnException, RadixLoaderException {
        super(topLayerUris, localFiles);
        this.primarySvnUrl = primarySvnUrl;
        final int index = primarySvnUrl.indexOf("//");
        if (index == -1) {
            throw new RadixLoaderException("RadixSvnLoader(): invalid svnHomeUrl");
        }
        final String cacheName = primarySvnUrl.substring(index + 2).replace(":", "_colon_");
        cacheMeta = new CacheMeta();
        cacheMeta.captureFreeCacheDir(fileCacheDir != null ? fileCacheDir : new File(SystemTools.getApplicationDataPath(Starter.STARTER_APP_DATA_ROOT), "cache"), cacheName);
        final List<String> svnUrls = new ArrayList<>();
        svnUrls.add(primarySvnUrl);
        if (additionalSvnUrls != null) {
            svnUrls.addAll(additionalSvnUrls);
            this.additionalSvnUrls.addAll(additionalSvnUrls);
        }
        final SVNAuthData authData = new SVNAuthData(
                authUser,
                authPassword == null ? null : authPassword.toCharArray(),
                sshKeyFile,
                sshKeyPassword == null ? null : sshKeyPassword.toCharArray(),
                svnServerCertFile);
        dataSupplier = new BinaryDataSupplier(svnUrls, svnLog, authData);
        if (Starter.isRoot() || (Starter.getArguments() != null && Starter.getArguments().getStarterParameters() != null && Starter.getArguments().getStarterParameters().containsKey(StarterArguments.DISABLE_SVN_PING))) {
            svnPingerThread = null;
        } else {
            synchronized (dataSupplier.getSynchronizationObject()) {
                svnPingerThread = new SvnPingerThread(this);
                svnPingerThread.start();
            }
        }
    }

    @Override
    public File getRoot() {
        return cacheMeta.getRoot();
    }

    public FileLock getCacheLock() {
        return cacheMeta.getCacheLock();
    }

    @Override
    public String getRepositoryUri() { // by BAO
        return primarySvnUrl;
    }

    @Override
    public void close() {
        synchronized (actualizeLock) {
            writeLock();
            try {
                if (svnPingerThread != null) {
                    svnPingerThread.requestStop();
                }
                dataSupplier.close();
                //cacheMeta will be closed in closeCache() called from super.close()
                super.close();
            } finally {
                writeUnlock();
            }
        }
    }

    @Override
    public void closeCache() {
        readLock();
        try {
            cacheMeta.release();
        } finally {
            readUnlock();
        }
    }

    @Override
    public List<String> listAllLayerUrisInRepository(final long revisionNum, final HowGetFile howGet) throws IOException {
        if (howGet.mode == HowGetFile.Mode.GET_FROM_DIR) {//load cached revision
            return RadixLoaderUtils.listAllLayersInDir(howGet.directory);
        }
        try {
            final List<SvnEntry> topLevelDirs = dataSupplier.listDirectory("", revisionNum);
            final List<String> result = new ArrayList<>();
            if (topLevelDirs != null) {
                for (SvnEntry entry : topLevelDirs) {
                    if (entry.getKind() == SvnEntry.Kind.DIRECTORY) {
                        final List<SvnEntry> dirContent = dataSupplier.listDirectory(entry.getName(), revisionNum);
                        for (SvnEntry dirItem : dirContent) {
                            if (dirItem.getKind() == SvnEntry.Kind.FILE && dirItem.getName().equals(LayerMeta.LAYER_XML_FILE)) {
                                result.add(entry.getName());
                            }
                        }
                    }
                }
            }
            return result;
        } catch (RadixSvnException ex) {
            throw new IOException(ex);
        }
    }

    public void ensureConnected() throws RadixLoaderException {
        dataSupplier.ensureConnected();
    }

    @Override
    public RevisionMeta createRevisionMetaImpl(final long revisionNum) throws IOException, XMLStreamException {
        return new RevisionMeta(new DefaultAccessor(this), revisionNum, HowGetFile.fromSvn());
    }

    private RevisionMeta loadCachedRevision(final CacheMeta meta) throws IOException, XMLStreamException {
        final RevisionMeta revMeta = readRevisionMeta(meta.getRevNum(), HowGetFile.fromDir(meta.getRoot()), new HashSet<String>(), new HashSet<String>(), new HashSet<String>());
        revMeta.setTimestampMillis(meta.getRevDate().getTime());
        return revMeta;
    }

    @Override
    protected Set<String> actualizeImpl(final Collection<RevisionMeta> oldRevisionMeta, final Set<String> modifiedFiles, final Set<String> removedFiles, Set<String> preloadGroupSuffixes) throws RadixLoaderException {
        traceDebug("Checking for new version...");
        //make a copy to prevent issues with unmodifiable collection
        preloadGroupSuffixes = new HashSet<>(preloadGroupSuffixes == null ? Collections.<String>emptySet() : preloadGroupSuffixes);
        preloadGroupSuffixes.addAll(defaultPreloadedGroupSuffixes);
        synchronized (actualizeLock) {
            if (isClosed()) {
                throw new RadixLoaderException("Unable to actualize closed loader");
            }
            if (cacheMeta.getCacheLock() == null) {
                throw new RadixLoaderException("Cache lock is not acquired");
            }
            if (currentRevisionMeta == null && cacheMeta.hasCachedRevision()) {//initialization during starter startup
                try {
                    final RevisionMeta revMetaFromCache = loadCachedRevision(cacheMeta);
                    if (revMetaFromCache != null) {
                        setCurrentRevisionMeta(revMetaFromCache);
                    }
                } catch (Throwable ex) {
                    traceError("Unable to load cached revision", ex);
                    cacheMeta.purge();
                    setCurrentRevisionMeta(null);
                }
            }
            dataSupplier.ensureConnected();
            try {
                final long svnLatestRevNum = dataSupplier.getLatestRevision();
                final long svnLatestRevTimestamp = dataSupplier.getRevisionCreationTime(svnLatestRevNum).getTime();
                RevisionMeta old_revision_meta;
                if (currentRevisionMeta == null || currentRevisionMeta.isOlderThen(svnLatestRevNum, svnLatestRevTimestamp)) {
                    if (pendingRevision == null || pendingRevision.isOlderThen(svnLatestRevNum, svnLatestRevTimestamp)) {
                        if (pendingRevision == null && cacheMeta.getPrecacheDir().exists()) {
                            internalEvent("Try to load pending revision from existing precache dir");
                            try {
                                final CacheMeta precacheMeta = new CacheMeta();
                                precacheMeta.capture(cacheMeta.getPrecacheDir());
                                try {
                                    final RevisionMeta revMetaFromPrecache = loadCachedRevision(precacheMeta);
                                    if (revMetaFromPrecache != null) {
                                        internalEvent("Rev #" + revMetaFromPrecache.getNum() + " [" + REVISION_DATE_FORMAT.format(new Date(revMetaFromPrecache.getTimestampMillis())) + "] has been loaded from precache");
                                        if (!revMetaFromPrecache.isOlderThen(svnLatestRevNum, svnLatestRevTimestamp)) {
                                            final Set<String> actually_changed_groups = new HashSet<>();
                                            final Set<String> actually_modified_files = new HashSet<>();
                                            final Set<String> actually_removed_files = new HashSet<>();
                                            revMetaFromPrecache.getChanges(currentRevisionMeta, actually_changed_groups, actually_modified_files, actually_removed_files);
                                            pendingRevision = new PendingRevisionInfo(revMetaFromPrecache, actually_changed_groups, actually_modified_files, actually_removed_files);
                                            internalEvent("Changed groups: " + actually_changed_groups + ", modified files: " + actually_modified_files + ", removed files: " + actually_removed_files);
                                            internalEvent("Using new revision meta loaded from precache");
                                        } else {
                                            internalEvent("Revision meta in the precache directory is outdated.");
                                        }
                                    } else {
                                        internalEvent("No revision meta in precache");
                                    }
                                } finally {
                                    precacheMeta.release();
                                }
                            } catch (Exception ex) {
                                internalEvent("Unable to load existing data from precache: " + ex.getMessage());
                            }
                        }
                        boolean needClearPrecache = pendingRevision == null;
                        if (pendingRevision != null && pendingRevision.isOlderThen(svnLatestRevNum, svnLatestRevTimestamp)) {
                            internalEvent("Previously loaded pending revision is outdated");
                            needClearPrecache = true;
                        }
                        if (needClearPrecache) {
                            clearPrecacheDir();
                        }
                        if (pendingRevision == null) {
                            internalEvent("Loading meta for revision #" + svnLatestRevNum + " from svn to precache");
                            cacheMeta.getPrecacheDir().mkdirs();
                            CacheMeta precacheMeta = new CacheMeta();
                            precacheMeta.capture(cacheMeta.getPrecacheDir());
                            try {
                                final Set<String> actually_changed_groups = new HashSet<>();
                                final Set<String> actually_modified_files = new HashSet<>();
                                final Set<String> actually_removed_files = new HashSet<>();
                                final RevisionMeta newRevMeta = readRevisionMeta(svnLatestRevNum, HowGetFile.fromSvnToDir(cacheMeta.getPrecacheDir()), actually_changed_groups, actually_modified_files, actually_removed_files);
                                newRevMeta.setTimestampMillis(svnLatestRevTimestamp);
                                precacheMeta.setCurrentRevisionMeta(newRevMeta);
                                precacheMeta.release();
                                pendingRevision = new PendingRevisionInfo(newRevMeta, actually_changed_groups, actually_modified_files, actually_removed_files);
                                internalEvent("Changed groups: " + actually_changed_groups + ", modified files: " + actually_modified_files + ", removed files: " + actually_removed_files);
                                internalEvent("Revision meta for rev#" + pendingRevision.revMeta.getNum() + " has been loaded from SVN");
                            } finally {
                                precacheMeta.release();
                            }
                        }
                        internalEvent("Groups that require preloading: " + preloadGroupSuffixes);
                    }

                    EActualizeAction action = null;
                    if (actualizeController != null) {
                        action = actualizeController.canUpdateTo(pendingRevision.revMeta, pendingRevision.modifiedFiles, pendingRevision.removedFiles, pendingRevision.modifiedGroups);
                        internalEvent("Actualize controller returned  " + action + " for #" + pendingRevision.revMeta.getNum());
                    }
                    if (action == null) {
                        action = EActualizeAction.PERFORM_ACTUALIZATION;
                    }

                    if (action == EActualizeAction.POSTPONE) {
                        return null;
                    }

                    if (!pendingRevision.isSuffixesPreloaded(preloadGroupSuffixes)) {
                        internalEvent("Prloading to precache: " + preloadGroupSuffixes);
                        preloadFilesToPrecache(pendingRevision, preloadGroupSuffixes);
                        pendingRevision.rememberPreloadedSuffixes(preloadGroupSuffixes);
                        internalEvent("Preloading compeleted");
                    } else {
                        internalEvent("Necessary groups are already prelaoded");
                    }

                    if (action == EActualizeAction.PRELOAD_AND_POSTPONE) {
                        return null;
                    }

                    if (pendingRevision.phase.stepNumber < EActualizePhase.BACKUP_COMPLETED.stepNumber) {
                        internalEvent("backing up old files");
                        clearBackupDir();
                        backupOldFiles(pendingRevision.removedAndModfiedFiles);
                        pendingRevision.phase = EActualizePhase.BACKUP_COMPLETED;
                        internalEvent("backing up old files completed");
                    }
                    boolean shouldClearPrecacheAndBackup = false;
                    final List<String> revAndRevDate = Arrays.asList(cacheMeta.getLockFileName());
                    writeLock();
                    try {
                        try {
                            internalEvent("closing file cache...");
                            clearFileCache();
                            internalEvent("removing old files");
                            removeFiles(getRoot(), pendingRevision.removedAndModfiedFiles);
                            internalEvent("transferring new files");
                            transferFilesFromDirToCache(cacheMeta.getPrecacheDir(), ETransferFailPolicy.DELETE_TRANSFERRED_FILES, revAndRevDate);
                            internalEvent("transferring completed");
                        } catch (RadixLoaderException ex) {
                            try {
                                transferFilesFromDirToCache(cacheMeta.getBackupDir(), ETransferFailPolicy.LEAVE_AS_IS, revAndRevDate);
                            } catch (Exception exOnRestore) {
                                traceError("Unable to restore files from backup after error on actualize", exOnRestore);
                            }
                            throw ex;
                        }
                        old_revision_meta = currentRevisionMeta;
                        setCurrentRevisionMeta(pendingRevision.revMeta);
                        cacheMeta.addPreloadedSuffixes(pendingRevision.preloadedGroupSuffixes);
                        if (oldRevisionMeta != null) {
                            oldRevisionMeta.add(old_revision_meta);
                        }
                        if (modifiedFiles != null) {
                            modifiedFiles.addAll(pendingRevision.modifiedFiles);
                        }
                        if (removedFiles != null) {
                            removedFiles.addAll(pendingRevision.removedFiles);
                        }
                        final Set<String> changedGroups = pendingRevision.modifiedGroups;
                        pendingRevision = null;
                        shouldClearPrecacheAndBackup = true;
                        internalEvent("Revision meta #" + currentRevisionMeta.getNum() + " has been loaded to cache");
                        return changedGroups;
                    } finally {
                        writeUnlock();
                        if (shouldClearPrecacheAndBackup) {
                            try {
                                clearPrecacheDir();
                            } catch (Exception ex) {
                                traceError("Unable to clear precache dir", ex);
                            }
                            try {
                                clearBackupDir();
                            } catch (Exception ex) {
                                traceError("Unable to clear backup dir", ex);
                            }
                        }
                    }
                } else if (preloadScheduled) {
                    //executed at the applicatoin start,
                    //no synchronization requred
                    preloadScheduled = false;
                    boolean alreadyPreloaded = true;
                    for (String s : preloadGroupSuffixes) {
                        if (!cacheMeta.getPreloadedSuffixes().contains(s)) {
                            alreadyPreloaded = false;
                            break;
                        }
                    }
                    if (!alreadyPreloaded) {
                        internalEvent("Do scheduled preload of " + preloadGroupSuffixes);
                        doPreload(currentRevisionMeta, preloadGroupSuffixes, getRoot(), null);
                        cacheMeta.addPreloadedSuffixes(preloadGroupSuffixes);
                    } else {
                        internalEvent("Groups " + preloadGroupSuffixes + " are already preloaded");
                    }
                }
                actualizeReplicaInformation();
                return null;
            } catch (RadixLoaderException e) {
                throw e;
            } catch (Exception e) {
                throw new RadixLoaderException("Exception on RadixSvnLoader.actualize()", e);
            }
        }
    }

    @Override
    public byte[] export(String file, long revNum) throws RadixLoaderException {
        try {
            dataSupplier.ensureConnected();
            return dataSupplier.getFileThrice(file, revNum);
        } catch (RadixSvnException ex) {
            throw new RadixLoaderException("Error on export", ex);
        }
    }
    
    @Override
    protected void afterSetCurrentRevisionMeta(RevisionMeta meta) throws RadixLoaderException {
        cacheMeta.setCurrentRevisionMeta(meta);
    }

    private void actualizeReplicaInformation() {
        try {
            if (dataSupplier.isRootRepoAvailable()) {
                long latestRootRevision = dataSupplier.getLatestRootRevision();
                long latestRootRevisionTimeMillis = dataSupplier.getRootRevisionCreationTime(latestRootRevision).getTime();
                if (latestRootRevision > cacheMeta.getRootRevNum() || (latestRootRevision == cacheMeta.getRootRevNum() && latestRootRevisionTimeMillis != cacheMeta.getRootRevDate().getTime())) {
                    internalEvent("New repository root revision found, try to update replications list");
                    try {
                        final ByteArrayInputStream bis = new ByteArrayInputStream(dataSupplier.getRootFileThrice(REPLICATIONS_FILE, latestRootRevision));
                        final DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                        final Document doc = builder.parse(bis);
                        final Node replicsNode = doc.getChildNodes().item(0);
                        final List<String> replications = new ArrayList<>();
                        replications.add(replicsNode.getAttributes().getNamedItem(MASTER_SVN_URL_ATTR).getTextContent());
                        for (int i = 0; i < replicsNode.getChildNodes().getLength(); i++) {
                            final Node childNode = replicsNode.getChildNodes().item(i);
                            if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                                replications.add(childNode.getAttributes().getNamedItem(SVN_HOME_URL_ATTR).getTextContent());
                            }
                        }
                        cacheMeta.setReplicationsInfo(latestRootRevision, new Date(latestRootRevisionTimeMillis), replications);
                        internalEvent("Replications list updated");
                    } catch (RadixSvnException ex) {
                        internalDebug("Unable to read replications list. Reset to empty", ex);
                        cacheMeta.setReplicationsInfo(latestRootRevision, new Date(latestRootRevisionTimeMillis), Collections.<String>emptyList());
                    }
                }
            }
        } catch (Exception ex) {
            internalEvent("Unable to load information about replications", ex);
        }
    }

    private void transferFilesFromDirToCache(final File srcDir, final ETransferFailPolicy failPolicy, final Collection<String> exclude) throws RadixLoaderException {
        if (srcDir == null || !srcDir.exists()) {
            return;
        }
        final Set<String> transferredFiles = new HashSet<>();
        final int srcPathSize = srcDir.toPath().toAbsolutePath().getNameCount();
        try {

            Files.walkFileTree(srcDir.toPath().toAbsolutePath(), new FileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    if (dir.getNameCount() == srcPathSize) {
                        return FileVisitResult.CONTINUE;//src dir itself
                    }
                    final String dirName = extractRelativeName(dir);
                    new File(getRoot(), dirName).mkdirs();
                    if (exclude != null && exclude.contains(dirName)) {
                        return FileVisitResult.SKIP_SUBTREE;
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    final String fileName = extractRelativeName(file);
                    if (exclude != null && exclude.contains(fileName)) {
                        return FileVisitResult.CONTINUE;
                    }
                    Files.copy(file, Paths.get(new File(getRoot(), fileName).toURI()), StandardCopyOption.REPLACE_EXISTING);
                    internalDebug(fileName + " has been transferred to cache");
                    transferredFiles.add(fileName);
                    return FileVisitResult.CONTINUE;
                }

                private String extractRelativeName(final Path file) {
                    return file.subpath(srcPathSize, file.getNameCount()).toString();
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    throw exc;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    if (exc != null) {
                        throw exc;
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (Exception ex) {
            internalEvent("error while transferring files", ex);
            if (!transferredFiles.isEmpty() && failPolicy == ETransferFailPolicy.DELETE_TRANSFERRED_FILES) {
                internalEvent("Try to remove already transferred files");
                try {
                    removeFiles(getRoot(), transferredFiles);
                    internalEvent("transferred files removed");
                } catch (Exception exOnRemove) {//very strange
                    traceError("Error while removing transferred files after exception on transfer", exOnRemove);
                }
            }
            throw new RadixLoaderException("Error while transferring files", ex);
        }

    }

    /**
     * Backup all files removed or modified in pending revision (passed
     * explicitly), as well as all directory.xml files that will be replaced by
     * their new versions (this files can be determined by traversing precache
     * directory).
     *
     * @param removedOrModified
     * @throws RadixLoaderException
     */
    private void backupOldFiles(final Set<String> removedOrModified) throws RadixLoaderException {
        final File backupDir = cacheMeta.getBackupDir();
        final File precacheDir = cacheMeta.getPrecacheDir();
        final Set<String> filesToBackup = new HashSet<>(removedOrModified);
        if (precacheDir.exists()) {
            try {
                Files.walkFileTree(precacheDir.toPath().toAbsolutePath(), new FileVisitor<Path>() {
                    final int srcPathSize = precacheDir.toPath().getNameCount();

                    @Override
                    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        final String fileName = extractRelativeName(file);
                        if (new File(cacheMeta.getRoot(), fileName).exists()) {
                            filesToBackup.add(fileName);
                        }
                        return FileVisitResult.CONTINUE;
                    }

                    private String extractRelativeName(final Path file) {
                        return file.subpath(srcPathSize, file.getNameCount()).toString();
                    }

                    @Override
                    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                        throw exc;
                    }

                    @Override
                    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                        if (exc != null) {
                            throw exc;
                        }
                        return FileVisitResult.CONTINUE;
                    }
                });
            } catch (Exception ex) {
                throw new RadixLoaderException("Unable to collect list of files modified in new revision", ex);
            }
        }
        try {
            filesToBackup.remove(cacheMeta.getLockFileName());
            copyFiles(filesToBackup, getRoot(), backupDir, null, false);
        } catch (Exception ex) {
            throw new RadixLoaderException("Error while backing up old files", ex);
        }
    }

    private void preloadFilesToPrecache(final PendingRevisionInfo pendingRevision, final Set<String> preloadGroupSuffixes) throws RadixLoaderException {
        if (pendingRevision == null || preloadGroupSuffixes == null || preloadGroupSuffixes.isEmpty()) {
            return;
        }
        if (getCacheLock() != null) { //RADIX-4315
            boolean bNeedPreload = false;
            NEED_PRELOAD_CHECK:
            for (String mg : pendingRevision.modifiedGroups) {
                for (String s : preloadGroupSuffixes) {
                    if (mg.endsWith(s)) {
                        bNeedPreload = true;
                        break NEED_PRELOAD_CHECK;
                    }
                }
            }
            if (bNeedPreload) {
                final Set<String> filesForForcedPreload = new HashSet<>();
                filesForForcedPreload.addAll(pendingRevision.removedFiles);
                filesForForcedPreload.addAll(pendingRevision.modifiedFiles);
                doPreload(pendingRevision.revMeta, preloadGroupSuffixes, cacheMeta.getPrecacheDir(), filesForForcedPreload);
            }
        }
    }

    /**
     * Preloading can be performed to the precache, in which case it is guarded
     * by actualizatonLock, or to actual cache at the application start, when no
     * synchronization needed.
     *
     * Files for forced preload are used by actualizer to preload new and
     * modified files from svn to precache. If destination directory is an
     * actual cache, then this parameter has no effect.
     *
     */
    private void doPreload(final RevisionMeta revMeta, final Set<String> preloadGroupSuffixes, final File preloadDestDir, final Set<String> filesForForcedPreload) throws RadixLoaderException {
        if (preloadGroupSuffixes == null) {
            return;
        }
        if (preloadGroupSuffixes.contains(RadixLoader.SERVER_GROUP_SUFFIX)) {//RADIX-5087
            actualizeReplicaInformation();
        }
        if (!preloadGroupSuffixes.isEmpty()) {
            for (FileMeta f : revMeta.getFileMetas()) {
                boolean bFileNeedPreload = false;
                if (!f.getName().contains("/lib/native/")
                        || f.getName().contains("/lib/native/" + SystemTools.getNativeLibDirShortName() + "/")) {
                    for (String s : preloadGroupSuffixes) {
                        if (f.getGroupType().endsWith(s)) {
                            bFileNeedPreload = true;
                            break;
                        }
                    }
                }
                final File fileInCache = new File(getRoot(), f.getStore());
                final boolean bForcedPreload = filesForForcedPreload != null && filesForForcedPreload.contains(f.getStore());
                if (bFileNeedPreload && (bForcedPreload || !fileInCache.exists())) {
                    preloadFile(f.getStore(), revMeta, preloadDestDir);
                }
            }
        }
    }

    private void clearBackupDir() throws RadixLoaderException {
        final File backupDir = cacheMeta.getBackupDir();
        if (backupDir.exists()) {
            FileUtils.removeRecursively(backupDir);
            internalEvent("backup dir has been cleared");
        }
    }

    private void clearPrecacheDir() throws RadixLoaderException {
        final File precacheDir = cacheMeta.getPrecacheDir();
        if (precacheDir.exists()) {
            FileUtils.removeRecursively(precacheDir);
            internalEvent("precache dir has been cleared");
        }
    }

    @Override
    protected CacheEntry getFileImpl(final String file, final long revisionNum) throws RadixLoaderException {
        readLock();
        try {
            final CacheEntry localFile = getLocalFile(file);
            if (localFile != null) {
                return localFile;
            }
            HowGetFile how_get;
            if (currentRevisionMeta != null && revisionNum == currentRevisionMeta.getNum() && getCacheLock() != null) {
                final File f = new File(getRoot(), file);
                if (f.exists()) {
                    how_get = HowGetFile.fromDir(getRoot());
                } else {
                    how_get = HowGetFile.fromSvnToDir(getRoot());
                }
            } else {
                how_get = HowGetFile.fromSvn();
            }
            boolean checkDigest = !Starter.getArguments().getStarterParameters().containsKey(StarterArguments.DISABLE_DIGEST_CHECK)
                    && currentRevisionMeta != null && revisionNum == currentRevisionMeta.getNum();
            if (checkDigest) {
                try {
                    final FileMeta fileMeta = currentRevisionMeta.findFile(file);
                    if (fileMeta != null) {
                        synchronized (fileMeta) {
                            final CacheEntry entry = getFileImpl(file, revisionNum, how_get);
                            final CheckDigestResult checkResult = checkDigest(fileMeta, entry);
                            if (!checkResult.isOk()) {
                                if (how_get.mode == HowGetFile.Mode.GET_FROM_DIR) {
                                    traceError("Wrong digest of the cached file '" + file + "', expected '" + checkResult.getExpectedAsStr() + "', actual: '" + checkResult.getActualAsStr() + "', trying to reload file from svn");
                                    final File existingFile = new File(getRoot(), file);
                                    try {
                                        entry.close();
                                    } catch (IOException ex) {
                                        throw new RadixLoaderException("Unable to close cache entry for '" + file + "'");
                                    }
                                    if (!FileUtils.deleteFile(existingFile)) {
                                        throw new RadixLoaderException("Unable to delete " + existingFile.getAbsolutePath());
                                    }
                                    final CacheEntry reloadedEntry = getFileImpl(file, revisionNum);
                                    traceEvent("File  '" + file + "' was reloaded from SVN");
                                    return reloadedEntry;
                                } else {
                                    throw new RadixLoaderException("Wrong digest of the file '" + file + "', expected '" + checkResult.getExpectedAsStr() + "', actual: '" + checkResult.getActualAsStr() + "'");
                                }
                            } else {
                                return entry;
                            }
                        }
                    }
                } catch (RuntimeException ex) {
                    throw new RadixLoaderException("Unexpected error on digest checking", ex);
                }
            }
            return getFileImpl(file, revisionNum, how_get);
        } finally {
            readUnlock();
        }
    }

    private CheckDigestResult checkDigest(final FileMeta fileMeta, final CacheEntry entry) throws RadixLoaderException {
        if (fileMeta != null && fileMeta.getDigest() != null) {
            byte[] actualDigest;
            try {
                if (entry instanceof FileCacheEntry) {
                    boolean ignoreCr = fileMeta.getName().endsWith(".xml");//evil
                    actualDigest = FileUtils.calcFileDigest(entry.getData(null), ignoreCr);
                } else {
                    actualDigest = FileUtils.readJarDigest(((JarCacheEntry) entry).getJarFile());
                }
            } catch (IOException | NoSuchAlgorithmException ex) {
                throw new RadixLoaderException("Unable to check digest", ex);
            }
            return new CheckDigestResult(fileMeta.getDigest(), actualDigest);
        }
        return new CheckDigestResult(null, null);
    }

    @Override
    protected CacheEntry getFileImpl(final String file, final long revisionNum, final HowGetFile howGet) throws RadixLoaderException {
        try {
            final CacheEntry localFile = getLocalFile(file);
            if (localFile != null) {
                return localFile;
            }
            byte[] bytes;
            if (howGet.mode == HowGetFile.Mode.GET_FROM_SVN || howGet.mode == HowGetFile.Mode.GET_FROM_SVN_TO_DIR) {
                RadixSvnException initialEx = null;
                try {
                    bytes = dataSupplier.getFileThrice(file, revisionNum);
                } catch (RadixSvnException ex) {
                    initialEx = ex;
                    try {
                        dataSupplier.ensureConnected();
                        bytes = dataSupplier.getFileThrice(file, revisionNum);
                    } catch (Exception otherEx) {
                        throw initialEx;
                    }
                }
                if (howGet.mode == HowGetFile.Mode.GET_FROM_SVN_TO_DIR) {
                    readLock();
                    try {
                        synchronized (howGet.directory) {
                            return writeFileToDir(file, howGet.directory, bytes, false);
                        }
                    } finally {
                        readUnlock();
                    }
                } else if (file.endsWith(JAR_FILE_SUFFIX)) {
                    final File tmp_file = createTempFile("radix-starter-jarlib-");
                    try (FileOutputStream out = new FileOutputStream(tmp_file)) {
                        out.write(bytes);
                    }
                    return new JarCacheEntry(new JarFile(tmp_file), true);
                }
                return new FileCacheEntry(bytes);
            } else if (howGet.mode == HowGetFile.Mode.GET_FROM_DIR) {
                readLock();
                try {
                    final File f = new File(howGet.directory, file);
                    if (file.endsWith(JAR_FILE_SUFFIX)) {
                        return new JarCacheEntry(new JarFile(f), false);
                    } else {
                        return new FileCacheEntry(FileUtils.readFile(f));
                    }
                } finally {
                    readUnlock();
                }
            } else {
                throw new IllegalStateException("Unsupported howGet.mode: " + howGet.mode);
            }
        } catch (RadixSvnException | IOException e) {
            throw new RadixLoaderException("Exception on RadixSvnLoader.getFile()", e);
        }
    }

    @Override
    public String getDescription() {
        long curRevNum;
        try {
            curRevNum = getCurrentRevision();
        } catch (Exception ex) {
            curRevNum = -1;
        }
        return "RadixSVNLoader: primary repository: " + primarySvnUrl
                + ", additional repositories: " + getAdditionalSvnUrlAsStr()
                + ", currently connected to: " + dataSupplier.getCurrentSvnUrl()
                + ", current revision: " + curRevNum
                + ", file cache directory: " + (getRoot() == null ? "<null>" : getRoot().getAbsolutePath());
    }

    private String getAdditionalSvnUrlAsStr() {
        if (additionalSvnUrls.isEmpty()) {
            return "<none>";
        }
        final StringBuilder sb = new StringBuilder();
        for (String url : additionalSvnUrls) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(url);
        }
        return sb.toString();
    }

    private CacheEntry writeFileToDir(final String file, final File dir, final byte[] bytes, boolean overwrite) throws IOException {
        final File f = new File(dir, file);
        if (overwrite || !f.exists()) {
            f.getParentFile().mkdirs();
            try {
                try (FileOutputStream out = new FileOutputStream(f)) {
                    out.write(bytes);
                    internalDebug(file + " has been written to " + dir.getAbsolutePath());
                }
            } catch (IOException ex) {
                FileUtils.deleteFile(f);
                throw ex;
            }
        }
        if (file.endsWith(JAR_FILE_SUFFIX)) {
            return new JarCacheEntry(new JarFile(f), false);
        } else {
            return new FileCacheEntry(bytes);
        }
    }

    public synchronized void preloadServer() {
        defaultPreloadedGroupSuffixes.addAll(RadixLoader.SERVER_GROUP_SUFFIXES);
        preloadScheduled = true;
    }

    public synchronized void preloadExplorer() {
        defaultPreloadedGroupSuffixes.addAll(RadixLoader.EXPLORER_GROUP_SUFFIXES);
        preloadScheduled = true;
    }

    public synchronized void preloadWeb() {
        defaultPreloadedGroupSuffixes.addAll(RadixLoader.WEB_GROUP_SUFFIXES);
        preloadScheduled = true;
    }

    @Override
    public synchronized Set<String> actualize() throws RadixLoaderException {
        return actualize(Collections.<String>emptySet());
    }

    public synchronized Set<String> actualize(Set<String> preloadGroupSuffixes) throws RadixLoaderException {
        final Set<String> toPreloadGroupSuffixes = new HashSet<>();
        if (preloadGroupSuffixes != null) {
            toPreloadGroupSuffixes.addAll(preloadGroupSuffixes);
        }
        toPreloadGroupSuffixes.addAll(defaultPreloadedGroupSuffixes);
        return super.actualize(null, null, null, toPreloadGroupSuffixes);
    }

    private void preloadFile(final String file, final RevisionMeta rev, final File preloadDestDir) throws RadixLoaderException {
        try {
            final File preloadedFile = new File(preloadDestDir, file);
            if (!preloadedFile.exists()) {
                final byte[] bytes = dataSupplier.getFileThrice(file, rev.getNum());
                preloadedFile.getParentFile().mkdirs();
                try (FileOutputStream out = new FileOutputStream(preloadedFile)) {
                    out.write(bytes);
                    internalDebug(file + " has been preloaded to " + preloadDestDir.getAbsolutePath());
                }
            }
        } catch (IOException | RadixSvnException ex) {
            throw new RadixLoaderException("Unable to preload " + file, ex);
        }
    }

    /**
     * All svn urls with which this loader can work. Union of urls specified via
     * -svnHomeUrl and -additinalSvnUrls.
     *
     * @return
     */
    public List<String> getSvnUrls() {
        return Collections.unmodifiableList(dataSupplier.getSvnUrls());
    }

    /**
     * Urls of repositories that are considered idential. Elements of this
     * collections are urls of repositories itself (root urls).
     *
     * @return
     */
    public List<String> getReplicationUrls() {
        return Collections.unmodifiableList(cacheMeta.getReplications());
    }

    private class BinaryDataSupplier {

        private SVNRepositoryAdapter svn;
        private SVNRepositoryAdapter rootSvn;
        private int curSvnIndex;
        private final List<String> svnUrls;
        private final SVNAuthData authData;

        public BinaryDataSupplier(List<String> svnUrls, final boolean svnLog, final SVNAuthData authData) {
            this.svnUrls = new ArrayList<>(svnUrls);
            this.authData = authData;
            curSvnIndex = -1;
        }

        public Object getSynchronizationObject() {
            return this;
        }

        public synchronized void ensureConnected() throws RadixLoaderException {
            List<String> errorMessages = new ArrayList<>();
            Exception lastException = null;
            if (curSvnIndex >= 0) {
                try {
                    check(svn);
                    return;
                } catch (RadixLoaderException | RadixSvnException ex) {
                    errorMessages.add(svnUrls.get(curSvnIndex) + ": " + ex.getMessage());
                    lastException = ex;
                    traceError("Disconnected from SVN repositry " + svnUrls.get(curSvnIndex) + ": " + ex.getMessage());
                    curSvnIndex = -1;
                }
            }
            close();
            for (int i = 0; i < svnUrls.size(); i++) {
                try {
                    connect(svnUrls.get(i));
                    curSvnIndex = i;
                    if (SVNRepositoryAdapter.Factory.isUseRadixClient()) {
                        traceEvent("Radix client connected to SVN repository " + svnUrls.get(i));
                    } else {
                        traceEvent("Connected to SVN repository " + svnUrls.get(i));
                    }
                    
                    return;
                } catch (RadixSvnException | RadixLoaderException ex) {
                    errorMessages.add(svnUrls.get(i) + ": " + ex.getMessage());
                    lastException = ex;
                }
            }
            if (curSvnIndex == -1) {
                final StringBuilder exMessageBuilder = new StringBuilder();
                exMessageBuilder.append("Unable to connect to any of SVN repositories: ");
                for (String exMessage : errorMessages) {
                    exMessageBuilder.append(exMessage);
                    exMessageBuilder.append("; ");
                }
                exMessageBuilder.append("cause exception is last occurred exception.");
                throw new RadixLoaderException(exMessageBuilder.toString(), lastException);
            }
        }

        public List<String> getSvnUrls() {
            return svnUrls;
        }

        private synchronized String getCurrentSvnUrl() {
            if (curSvnIndex == -1) {
                return null;
            }
            return svnUrls.get(curSvnIndex);
        }

        private synchronized void connect(final String svnUrlString) throws RadixSvnException, RadixLoaderException {
            if (svn != null) {
                close();
            }
            svn = doConnect(svnUrlString);
        }

        private synchronized SVNRepositoryAdapter doConnect(final String svnUrlString) throws RadixSvnException, RadixLoaderException {
            SVNRepositoryAdapter.Builder builder = new SVNRepositoryAdapter.Builder();
            if (authData.user != null) {
                if (authData.password != null) {
                    builder.addUserName(authData.user, authData.password);
                    builder.addSSH(authData.user, authData.password);
                } else {
                    builder.addUserName(authData.user);
                }
            }
            if (authData.privateKeyPath != null) {
                if (authData.privateKeyPassword != null) {
                    builder.addSSL(authData.user, authData.privateKeyPath, authData.privateKeyPassword);
                } else {
                    builder.addSSL(authData.user, authData.privateKeyPath);
                }
                builder.addSSH(authData.user, authData.privateKeyPath, authData.privateKeyPassword);
            }
            builder.setTrustManager(authData.createTrustManager());
            SVNRepositoryAdapter adapter = builder.build(URI.create(svnUrlString));
            adapter.getLatestRevision();//basic check
            return adapter;
        }

        private synchronized void check(SVNRepositoryAdapter svn) throws RadixLoaderException, RadixSvnException {
            final SvnEntry.Kind nodeKind = svn.checkPath("", -1);
            if (nodeKind == SvnEntry.Kind.NONE) {
                throw new RadixLoaderException("RadixSvnLoader(): no entry at URL: " + svn.getLocation());
            } else if (nodeKind == SvnEntry.Kind.FILE) {
                throw new RadixLoaderException("RadixSvnLoader(): entry at URL " + svn.getLocation() + " is a file while directory was expected");
            }
        }

        public synchronized boolean isRootRepoAvailable() {
            if (svn != null) {
                try {
                    if (rootSvn == null) {
                        rootSvn = doConnect(svn.getRepositoryRoot());
                    } else {
                        check(rootSvn);
                    }
                    return true;
                } catch (RadixSvnException | RadixLoaderException ex) {
                    try {
                        rootSvn.close();
                    } catch (Exception ex1) {
                        //ignore
                    } finally {
                        rootSvn = null;
                    }
                }
            }
            return false;
        }

        public synchronized long getLatestRevision() throws RadixSvnException {
            return getLatestRevision(svn);
        }

        public synchronized long getLatestRootRevision() throws RadixSvnException {
            return getLatestRevision(rootSvn);
        }

        public synchronized Date getRevisionCreationTime(final long revision) throws RadixSvnException {
            return getRevisionCreationTime(svn, revision);
        }

        public synchronized Date getRootRevisionCreationTime(final long revision) throws RadixSvnException {
            return getRevisionCreationTime(rootSvn, revision);
        }

        private synchronized long getLatestRevision(final SVNRepositoryAdapter svn) throws RadixSvnException {
            final long latestRevision = svn.getLatestRevision();
            final SvnEntry launchSvnDir = svn.getDir("", latestRevision);
            if (launchSvnDir == null) {
                return latestRevision;
            } else {
                return launchSvnDir.getRevision();
            }
        }

        private synchronized Date getRevisionCreationTime(final SVNRepositoryAdapter svn, final long revision) throws RadixSvnException {
            final SvnEntry e = svn.getDir("", revision);
            if (e == null) {
                return null;
            } else {
                Calendar result = new GregorianCalendar();
                result.setTime(e.getLastModified());
                result.set(Calendar.MILLISECOND, 0);
                return result.getTime();
            }
        }

        public List<SvnEntry> listDirectory(final String dirPath, long revision) throws RadixSvnException {
            final List<SvnEntry> result = new ArrayList<>();
            svn.getDir(dirPath, revision, new SVNRepositoryAdapter.EntryHandler() {

                @Override
                public void accept(SvnEntry entry) throws RadixSvnException {
                    result.add(entry);
                }
            });
            return result;
        }

        public synchronized byte[] getFileThrice(String file, long revision) throws RadixSvnException {
            return getFileThrice(svn, file, revision);
        }

        public synchronized byte[] getRootFileThrice(String file, long revision) throws RadixSvnException {
            return getFileThrice(rootSvn, file, revision);
        }

        private synchronized byte[] getFileThrice(SVNRepositoryAdapter svn, String file, long revision) throws RadixSvnException {
            if (svn == null) {
                throw new RadixSvnException("Connection is null");
            }
            final String oldStatus = getStatus();
            setStatus("Loading " + file);
            try {
                for (int i = 1; true; i++) {
                    try {
                        traceDebug("SVN getFile " + file + " from " + svn.getLocation() + ", revision " + Long.toString(revision));
                        final ByteArrayOutputStream buf = new ByteArrayOutputStream();
                        svn.getFile(file, revision, null, buf);
                        return buf.toByteArray();
                    } catch (RadixSvnException ex) {
                        if (!ex.isIOError() && !ex.isMalformedDataError()) {
                            throw ex;
                        }

                        int timeout;
                        switch (i) {
                            case 1:
                                timeout = 10;
                                break;
                            case 2:
                                timeout = 500;
                                break;
                            case 3:
                                timeout = 2000;
                                break;
                            case 4:
                                timeout = 4000;
                                break;
                            default:
                                throw ex;
                        }
                        try {
                            Thread.sleep(timeout);
                        } catch (InterruptedException e) {
                            throw ex;
                        }
                    }
                }
            } finally {
                setStatus(oldStatus);
            }
        }

        public synchronized void close() {
            try {
                if (svn != null) {
                    try {
                        svn.close();
                    } catch (Exception ex) {
                        //ignore
                    }
                }
                if (rootSvn != null) {
                    try {
                        rootSvn.close();
                    } catch (Exception ex) {
                        //ignore
                    }
                }
            } finally {
                curSvnIndex = -1;
                svn = null;
                rootSvn = null;
            }
        }
    }

    private static class CacheMeta {

        private static final String REVISION_TXT = "revision.txt";
        private static final String REVISION_DATE_TXT = "revision_date.txt";
        private static final String PRECACHE_DIR = "precache";
        private static final String BACKUP_DIR = "backup";
        private static final String STATE_SECTION = "State";
        private static final String PRELOADED_SUFFIXES_KEY = "preloadedSuffixes";
        private static final String ROOT_REVISION_KEY = "rootRevision";
        private static final String ROOT_REVISION_DATE_KEY = "rootRevisionDate";
        private static final String REVISION_KEY = "revision";
        private static final String REVISION_DATE_KEY = "revisionDate";
        private static final String REPLICATIONS_SECTION = "Replications";
        //
        private final List<String> replications = new ArrayList<>();
        private final Set<String> preloadedSuffixes = new HashSet<>();
        private volatile boolean isCacheDirLockOwner = false;
        private volatile FileLock cacheDirLock;
        private volatile File root;
        private volatile long revNum;
        private volatile long rootRevNum;
        private volatile Date revDate;
        private volatile Date rootRevDate;
        private volatile File precacheDir;
        private volatile File backupDir;

        public CacheMeta() {
            reset();
        }

        public String getLockFileName() {
            return REVISION_TXT;
        }

        public File getRoot() {
            return root;
        }

        public FileLock getCacheLock() {
            return cacheDirLock;
        }

        public long getRevNum() {
            return revNum;
        }

        public long getRootRevNum() {
            return rootRevNum;
        }

        public Date getRevDate() {
            return revDate;
        }

        public Date getRootRevDate() {
            return rootRevDate;
        }

        public Set<String> getPreloadedSuffixes() {
            return preloadedSuffixes;
        }

        public List<String> getReplications() {
            return replications;
        }

        private boolean tryUseParentLock() {
            try {
                if (Starter.isRoot()) {
                    return false;
                }
                final Class loaderClass = Starter.getRootStarterClassLoader().loadClass(RadixLoader.class.getName());
                final Method method = loaderClass.getMethod("getInstance");
                final Object parentLoaderObj = method.invoke(null);
                if (parentLoaderObj != null) {
                    final Method getLockMethod = parentLoaderObj.getClass().getMethod("getCacheLock");

                    getLockMethod.setAccessible(true);
                    final Object cacheDirLockObj = getLockMethod.invoke(parentLoaderObj);
                    if (cacheDirLockObj instanceof FileLock) {
                        cacheDirLock = (FileLock) cacheDirLockObj;
                        final Method getCacheDirMethod = parentLoaderObj.getClass().getMethod("getRoot");
                        setRoot((File) getCacheDirMethod.invoke(parentLoaderObj));
                        isCacheDirLockOwner = false;
                        load();
                        return true;
                    }
                }
            } catch (Exception ex) {
                traceError("Unable to reuse parent starter cache", ex);
            }
            return false;
        }

        private void setRoot(final File root) {
            this.root = root;
            precacheDir = new File(root, PRECACHE_DIR);
            backupDir = new File(root, BACKUP_DIR);
        }

        private void captureFreeCacheDir(final File cacheDir, final String cacheName) throws RadixLoaderException {
            if (tryUseParentLock()) {
                return;
            }
            final File dir = new File(cacheDir, cacheName);
            dir.mkdirs();
            for (int i = 0; i < Integer.MAX_VALUE; i++) {
                try {
                    final File index_dir = new File(dir, Integer.toString(i));
                    index_dir.mkdir();
                    capture(index_dir);
                    return;
                } catch (Exception ex) {
                    //continue
                }
            }
            throw new RadixLoaderException("RadixSvnLoader(): can't capture free cache directory");
        }

        public void capture(final File dir) throws FileNotFoundException, IOException {
            final File revision_file = new File(dir, REVISION_TXT);
            boolean isNew = false;
            if (!revision_file.exists()) {
                revision_file.createNewFile();
                isNew = true;
            }
            final String[] filesInCache = dir.list();
            if (filesInCache == null || filesInCache.length <= 1) {
                isNew = true;
            }
            final FileChannel fc = new RandomAccessFile(revision_file, "rw").getChannel();
            cacheDirLock = fc.tryLock(0L, Long.MAX_VALUE, false);
            if (cacheDirLock != null) {
                setRoot(dir);
                isCacheDirLockOwner = true;
                if (isNew) {
                    resetLockFileContent();
                } else {
                    load();
                }
                return;
            }
            throw new RadixLoaderException("Unable to lock " + revision_file.getAbsolutePath());
        }

        private Date readRevisionCreationDateFromDir(final File dir) {
            try {
                final File revisionDateFile = new File(dir, REVISION_DATE_TXT);
                if (revisionDateFile.exists()) {
                    try (Scanner scanner = new Scanner(revisionDateFile)) {
                        final String dateString = scanner.nextLine();
                        return getRevisionDate(dateString);
                    }
                }
            } catch (Exception ex) {
                //skip
            }
            return null;
        }

        private Date getRevisionDate(final String string) throws ParseException {
            Calendar result = new GregorianCalendar();
            Date dateFromFile;
            try {
                dateFromFile = REVISION_DATE_FORMAT_OLD.parse(string);
            } catch (ParseException ex) {
                dateFromFile = REVISION_DATE_FORMAT.parse(string);
            }
            result.setTime(dateFromFile);
            result.set(Calendar.MILLISECOND, 0);
            return result.getTime();
        }

        private File getPrecacheDir() {
            return precacheDir;
        }

        private File getBackupDir() {
            return backupDir;
        }

        private byte[] readLockFileContent() throws IOException {
            cacheDirLock.channel().position(0);
            final int maxBufSize = 1024 * 1024;
            final ByteBuffer buf = ByteBuffer.allocate(maxBufSize);
            while (cacheDirLock.channel().read(buf) != -1) {
                if (!buf.hasRemaining()) {
                    return null;
                }
            }
            byte[] bytes = new byte[buf.position()];
            buf.flip();
            buf.get(bytes);
            return bytes;
        }

        private void load() throws IOException {
            reset();
            boolean valid = true;
            byte[] bytes = readLockFileContent();
            if (bytes == null) {
                valid = false;
            }
            if (valid) {
                try {
                    final Scanner sc = new Scanner(new ByteArrayInputStream(bytes));
                    String s = sc.nextLine();
                    if (s != null) {
                        if (("[" + STATE_SECTION + "]").equals(s.trim())) {
                            loadNewFormat(bytes);
                        } else {
                            loadOldFormat(bytes);
                        }
                    } else {
                        valid = false;
                    }
                } catch (Exception ex) {
                    traceError("Unable to load cache meta from " + root.getAbsolutePath() + " :", ex);
                    valid = false;
                }
            }
            if (!valid) {
                purge();
            }
        }

        public void purge() throws RadixLoaderException {
            reset();
            resetLockFileContent();
            for (File file : getRoot().listFiles()) {
                if (!file.getName().equals(getLockFileName())) {
                    FileUtils.removeRecursively(file);
                }
            }
        }

        private void resetLockFileContent() throws RadixLoaderException {
            setLockFileContent(("[" + STATE_SECTION + "]" + System.getProperty("line.separator") + "[" + REPLICATIONS_SECTION + "]" + System.getProperty("line.separator")).getBytes());
        }

        private void setLockFileContent(byte[] data) throws RadixLoaderException {
            try {
                cacheDirLock.channel().truncate(0);
                cacheDirLock.channel().write(ByteBuffer.wrap(data));
                cacheDirLock.channel().force(true);
            } catch (IOException ex) {
                throw new RadixLoaderException("Unable to clear lock file content", ex);
            }
        }

        private void reset() {
            revNum = -1;
            rootRevNum = -1;
            revDate = null;
            rootRevDate = null;
            replications.clear();
            preloadedSuffixes.clear();
        }

        private void loadNewFormat(byte[] data) throws ConfigFileParseException, ParseException, RadixLoaderException {
            final ConfigFileAccessor stateAccessor = ConfigFileAccessor.get(data, STATE_SECTION);
            for (ConfigEntry entry : stateAccessor.getEntries()) {
                switch (entry.getKey()) {
                    case REVISION_KEY:
                        revNum = Integer.valueOf(entry.getValue());
                        break;
                    case REVISION_DATE_KEY:
                        revDate = getRevisionDate(entry.getValue());
                        break;
                    case ROOT_REVISION_KEY:
                        rootRevNum = Integer.valueOf(entry.getValue());
                        break;
                    case ROOT_REVISION_DATE_KEY:
                        rootRevDate = getRevisionDate(entry.getValue());
                        break;
                    case PRELOADED_SUFFIXES_KEY:
                        loadPreloadedSuffixesFromString(entry.getValue());
                        break;
                }
            }
            if (revNum == -1 || revDate == null) {
                throw new RadixLoaderException("revision and revisionDate should be defined, but revision=" + revNum + " and revisionDate=" + revDate);
            }

            try {
                final ConfigFileAccessor replicsAccessor = ConfigFileAccessor.get(data, REPLICATIONS_SECTION);
                for (ConfigEntry entry : replicsAccessor.getEntries()) {
                    replications.add(entry.getValue());
                }
            } catch (Exception ex) {
                //sad, but we can live with it
            }
        }

        private void loadOldFormat(byte[] data) throws RadixLoaderException {
            revNum = Integer.valueOf(new String(data));
            revDate = readRevisionCreationDateFromDir(root);
            if (revNum == -1 || revDate == null) {
                throw new RadixLoaderException("revision and revisionDate should be defined, but revision=" + revNum + " and revisionDate=" + revDate);
            }
            save();
            FileUtils.deleteFile(new File(REVISION_DATE_TXT));
        }

        public void setCurrentRevisionMeta(final RevisionMeta meta) throws RadixLoaderException {
            if (revNum != (meta == null ? -1 : meta.getNum()) || (meta == null ? -1 : meta.getTimestampMillis()) != (revDate == null ? -1 : revDate.getTime())) {//NOPMD pmd is stupid
                revNum = meta == null ? -1 : meta.getNum();
                revDate = meta == null ? null : new Date(meta.getTimestampMillis());
                preloadedSuffixes.clear();
                save();
            }
        }

        public void setReplicationsInfo(final long revNum, final Date revDate, final List<String> replications) throws RadixLoaderException {
            rootRevNum = revNum;
            rootRevDate = revDate;
            this.replications.clear();
            if (replications != null) {
                this.replications.addAll(replications);
            }
            save();
        }

        public void addPreloadedSuffixes(final Collection<String> suffixes) throws RadixLoaderException {
            if (suffixes != null) {
                preloadedSuffixes.addAll(suffixes);
                save();
            }
        }

        private void save() throws RadixLoaderException {
            resetLockFileContent();
            if (hasCachedRevision()) {
                try {
                    ConfigFileParser.ConfigFileCursor cursor = new ConfigFileParser.ConfigFileCursor(new ByteArrayInputStream(readLockFileContent()));
                    cursor.nextSection();
                    cursor.insertAfter(new ConfigEntry(REVISION_KEY, String.valueOf(revNum)));
                    cursor.insertAfter(new ConfigEntry(REVISION_DATE_KEY, REVISION_DATE_FORMAT.format(revDate)));
                    if (rootRevNum != -1 && rootRevDate != null) {
                        cursor.insertAfter(new ConfigEntry(ROOT_REVISION_KEY, String.valueOf(rootRevNum)));
                        cursor.insertAfter(new ConfigEntry(ROOT_REVISION_DATE_KEY, REVISION_DATE_FORMAT.format(rootRevDate)));
                    }
                    cursor.insertAfter(new ConfigEntry(PRELOADED_SUFFIXES_KEY, preloadedSuffixesToString()));
                    cursor.nextSection();
                    int i = 1;
                    for (String rep : replications) {
                        cursor.insertAfter(new ConfigEntry(String.valueOf(i++), rep));
                    }
                    setLockFileContent(new String(cursor.flush()).getBytes());
                } catch (Exception ex) {
                    throw new RadixLoaderException("Error while modifing lock file content", ex);
                }
            }
        }

        private String preloadedSuffixesToString() {
            final StringBuilder sb = new StringBuilder();
            for (String s : preloadedSuffixes) {
                if (sb.length() > 0) {
                    sb.append(",");
                }
                sb.append(s);
            }
            return sb.toString();
        }

        private void loadPreloadedSuffixesFromString(String string) {
            preloadedSuffixes.clear();
            if (string == null || string.trim().isEmpty()) {
                return;
            }
            preloadedSuffixes.addAll(Arrays.asList(string.trim().split(",")));
        }

        public boolean hasCachedRevision() {
            return revNum != -1 && revDate != null;
        }

        public void release() {
            if (isCacheDirLockOwner && cacheDirLock != null) {
                try {
                    cacheDirLock.release();
                    if (cacheDirLock.channel() != null) {
                        cacheDirLock.channel().close();
                    }
                    cacheDirLock = null;
                } catch (IOException ex) {
                    traceError("error on release", ex);
                }
            }
        }
    }

    private static class PendingRevisionInfo {

        private final RevisionMeta revMeta;
        private EActualizePhase phase;
        final Set<String> modifiedGroups;
        final Set<String> modifiedFiles;
        final Set<String> removedFiles;
        final Set<String> removedAndModfiedFiles;
        final Set<String> preloadedGroupSuffixes = new HashSet<>();

        public PendingRevisionInfo(RevisionMeta revMeta, final Set<String> changedGroups, final Set<String> modifiedFiles, final Set<String> removedFiles) {
            this.revMeta = revMeta;
            this.phase = EActualizePhase.REVISION_META_LOADED;
            this.modifiedGroups = Collections.unmodifiableSet(changedGroups);
            this.modifiedFiles = Collections.unmodifiableSet(modifiedFiles);
            this.removedFiles = Collections.unmodifiableSet(removedFiles);
            this.removedAndModfiedFiles = new HashSet<>();
            removedAndModfiedFiles.addAll(removedFiles);
            removedAndModfiedFiles.addAll(modifiedFiles);
        }

        public void rememberPreloadedSuffixes(final Set<String> preloadedGroupSuffixes) {
            preloadedGroupSuffixes.addAll(preloadedGroupSuffixes);
        }

        public boolean isSuffixesPreloaded(final Set<String> preloadedGroupSuffixes) {
            if (preloadedGroupSuffixes == null || preloadedGroupSuffixes.isEmpty()) {
                return true;
            }
            for (String suffix : preloadedGroupSuffixes) {
                if (!this.preloadedGroupSuffixes.contains(suffix)) {
                    return false;
                }
            }
            return true;
        }

        public boolean isOlderThen(final long revNum, final long revDateMillis) {
            return revMeta.getNum() < revNum || (revMeta.getNum() == revNum && revMeta.getTimestampMillis() != revDateMillis);
        }
    }

    private static enum ETransferFailPolicy {

        LEAVE_AS_IS,
        DELETE_TRANSFERRED_FILES
    }

    private static enum EActualizePhase {

        REVISION_META_LOADED(0),
        BACKUP_COMPLETED(1);
        private final int stepNumber;

        private EActualizePhase(int step) {
            this.stepNumber = step;
        }
    }

    private static class CheckDigestResult {

        final byte[] expected;
        final byte[] actual;

        public CheckDigestResult(byte[] expected, byte[] actual) {
            this.expected = expected;
            this.actual = actual;
        }

        public String getExpectedAsStr() {
            return digestAsStr(expected);
        }

        public String getActualAsStr() {
            return digestAsStr(actual);
        }

        private String digestAsStr(final byte[] bytes) {
            if (bytes == null) {
                return "<null>";
            }
            if (bytes.length == 0) {
                return "<empty>";
            }
            return HexConverter.toHex(bytes);
        }

        public boolean isOk() {
            return Arrays.equals(expected, actual);
        }
    }

    private static class SvnPingerThread extends Thread {

        private final RadixSVNLoader loader;
        private volatile boolean shuttingDown = false;

        public SvnPingerThread(RadixSVNLoader loader) {
            super(getDebugDesc() + " svn pinger thread");
            this.loader = loader;
            setDaemon(true);
        }

        public void requestStop() {
            shuttingDown = true;
            interrupt();
        }

        @Override
        public void run() {
            while (!shuttingDown && !Thread.interrupted()) {
                try {
                    Thread.sleep(SLEEP_BETWEEN_SVN_PINGS_MILLIS);
                } catch (InterruptedException ex) {
                    return;
                }
                try {
                    loader.dataSupplier.ensureConnected();
                } catch (Throwable t) {
                    traceError("Error on svn availablity check", t);
                }
            }
        }
    }

    private static class SVNAuthData {

        public final String user;
        public final char[] password;
        public final String privateKeyPath;
        public final char[] privateKeyPassword;
        public final String trustedServerCertPath;

        public SVNAuthData(String user, char[] password, String privateKeyPath, char[] privateKeyPassword, String trustedServerCertPath) {
            this.user = user;
            this.password = password;
            this.privateKeyPath = privateKeyPath;
            this.privateKeyPassword = privateKeyPassword;
            this.trustedServerCertPath = trustedServerCertPath;
        }

        private X509TrustManager createTrustManager() {
            return new X509TrustManager() {
                @Override
                public void checkClientTrusted(final X509Certificate[] xcs, final String string) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(final X509Certificate[] xcs, final String string) throws CertificateException {
                    if (xcs != null) {
                        if (trustedServerCertPath == null) {
                            throw new CertificateException("Can't check SVN server certificate: parameter -svnServerCertFile is not defined.");
                        }
                        FileInputStream f = null;
                        try {
                            f = new FileInputStream(trustedServerCertPath);
                            final CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
                            final Certificate cer = certificateFactory.generateCertificate(f);
                            if (Arrays.asList(xcs).contains(cer)) {
                                return;
                            }
                        } catch (FileNotFoundException e) {
                            throw new CertificateException("Can't load trusted certificate for SVN server: " + e.getMessage());
                        } finally {
                            if (f != null) {
                                try {
                                    f.close();
                                } catch (IOException e) {
                                    //ignore
                                }
                            }
                        }
                    }
                    throw new CertificateException("SVN server's certificate chain is not trusted by Starter");
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
        }

    }
}
