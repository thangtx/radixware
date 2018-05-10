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
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.jar.JarFile;
import javax.xml.stream.XMLStreamException;
import org.radixware.kernel.starter.filecache.CacheEntry;
import org.radixware.kernel.starter.filecache.FileCacheEntry;
import org.radixware.kernel.starter.filecache.JarCacheEntry;
import org.radixware.kernel.starter.meta.RevisionMeta;
import org.radixware.kernel.starter.utils.FileUtils;

public class RadixDirLoader extends RadixLoader {

    private final String repositoryUri; // by BAO
    private static final String BRANCH_XML = "branch.xml";
    private final BranchLocker branchFileLocker;
    private final Object actualizeLock = new Object();
    private final File root;
    public static final AtomicLong FILE_COPY_MILLIS = new AtomicLong();

    public RadixDirLoader(final String directory, final List<String> topLayerUris) {
        this(directory, topLayerUris, LocalFiles.getInstance(null, null));
    }

    public RadixDirLoader(final String directory, final List<String> topLayerUris, final LocalFiles localFiles) {
        super(topLayerUris, localFiles);
        this.root = new File(directory);

        //by BAO
        String computerName;
        try {
            computerName = InetAddress.getLocalHost().getCanonicalHostName();
        } catch (UnknownHostException ex) {
            computerName = "localhost";
        }
        String directoryCanonicalPath;
        try {
            directoryCanonicalPath = root.getCanonicalPath();
        } catch (IOException e) {
            directoryCanonicalPath = directory;
        }
        this.repositoryUri = "file://" + computerName + ":" + directoryCanonicalPath;
        branchFileLocker = new BranchLocker(new File(root, BRANCH_XML));
    }

    @Override
    protected RevisionMeta createRevisionMetaImpl(final long revisionNum, final ERevisionMetaType metaType) throws IOException, XMLStreamException {
        if (currentRevisionMeta != null && revisionNum == currentRevisionMeta.getNum()) {
            return currentRevisionMeta;
        }
        if (currentRevisionMeta == null || revisionNum == 0 || revisionNum == getLatestRevision()) {
            return new RevisionMeta(new DefaultAccessor(this), revisionNum, HowGetFile.fromDir(root), ERevisionMetaType.LAYERS_ONLY);
        }
        throw new RadixLoaderException("RadixDirLoader.createRevisionMeta() - revisionNum (#" + String.valueOf(revisionNum) + ") is not current (#" + String.valueOf(currentRevisionMeta.getNum()) + ")");
    }

    private long calcRevNum(final long timeMillis) {
        final SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        format.setTimeZone(TimeZone.getTimeZone("GMT+0"));
        return Long.parseLong(format.format(new Date(timeMillis)));
    }

    @Override
    public long getLatestRevision() {
        if (currentRevisionMeta != null) {
            return calcRevNum(currentRevisionMeta.getLastModificationTime());
        }
        return -1;
    }

    @Override
    public IRepositoryEntry exportFile(String file, long revNum) throws RadixLoaderException {
        readLock();
        try {
            try {
                return new FileRepositoryEntry(file, new File(root, file).getName(), readFileBytesIfSizeIsOk(new File(root, file)), currentRevisionMeta.getNum(), currentRevisionMeta.getTimestampMillis());
            } catch (IOException ex) {
                throw new RadixLoaderException("Unable to export file", ex);
            }
        } finally {
            readUnlock();
        }
    }

    private byte[] readFileBytesIfSizeIsOk(final File f) throws IOException {
        if (f.exists() && !f.isDirectory()) {
            final long len = f.length();
            if (len > MAX_EXPORTED_FILE_BYTES) {
                throw new IOException("File '" + f + "' is too large: " + len + " bytes");
            }
        }
        return Files.readAllBytes(f.toPath());
    }

    @Override
    public Collection<IRepositoryEntry> listDir(final String path, long revNum, boolean downloadFileData) throws RadixLoaderException {
        readLock();
        try {
            final List<IRepositoryEntry> result = new ArrayList<>();
            final File dir = new File(root, path);
            if (!dir.exists()) {
                throw new RadixLoaderException("Not existing path: '" + path + "'");
            }
            if (!dir.isDirectory()) {
                throw new RadixLoaderException("Not a directory: '" + path + "'");
            }
            for (File f : dir.listFiles()) {
                if (f.isDirectory()) {
                    result.add(new DirRepositoryEntry(concatPath(path, f.getName()), f.getName(), revNum, currentRevisionMeta.getTimestampMillis()));
                } else {
                    result.add(new FileRepositoryEntry(concatPath(path, f.getName()), f.getName(), downloadFileData ? readFileBytesIfSizeIsOk(f) : null, revNum, currentRevisionMeta.getTimestampMillis()));
                }
            }
            return result;
        } catch (IOException ex) {
            throw new RadixLoaderException("Error on export", ex);
        } finally {
            readUnlock();
        }
    }

    @Override
    protected Set<String> actualizeImpl(final long targetRevNum, final Collection<RevisionMeta> oldRevisionMeta, final Set<String> modifiedFiles, final Set<String> removedFiles, final Set<String> preloadedGroupSuffixes) throws RadixLoaderException {
        synchronized (actualizeLock) {
            final Set<String> actuallyChangedGroups = new HashSet<>();
            final Set<String> actuallyModifiedFiles = new HashSet<>();
            final Set<String> actuallyRemovedFiles = new HashSet<>();
            RevisionMeta new_revision_meta = null;

            //we must lock loader before locking branch file. We should release
            //loader lock before asking actualizeController if update can be performed,
            //so the type of lock doesn't matter. 
            readLock();
            try {
                try {
                    branchFileLocker.lock();
                    try {
                        final long last_mtime = currentRevisionMeta != null ? currentRevisionMeta.getLastModificationTime() : 0;
                        if (currentRevisionMeta == null || currentRevisionMeta.getNum() != calcRevNum(last_mtime)) {
                            new_revision_meta = readRevisionMeta(0, HowGetFile.fromDir(root), actuallyChangedGroups, actuallyModifiedFiles, actuallyRemovedFiles);
                            final long revMillis = new_revision_meta.getLastModificationTime();
                            new_revision_meta.setNum(calcRevNum(revMillis));
                        } else {
                            return null;
                        }
                    } finally {
                        branchFileLocker.unlock();
                    }
                } catch (IOException | XMLStreamException e) {
                    throw new RadixLoaderException("Exception on RadixDirLoader.actualize()", e);
                }
            } finally {
                readUnlock();
            }

            EActualizeAction action = null;
            if (actualizeController != null) {
                action = actualizeController.canUpdateTo(new_revision_meta, actuallyModifiedFiles, actuallyRemovedFiles, actuallyChangedGroups);
            }

            if (action == EActualizeAction.POSTPONE || action == EActualizeAction.PRELOAD_AND_POSTPONE) {
                return null;
            }

            if (oldRevisionMeta != null) {
                oldRevisionMeta.add(currentRevisionMeta);
            }

            setCurrentRevisionMeta(new_revision_meta);
            if (modifiedFiles != null) {
                modifiedFiles.addAll(actuallyModifiedFiles);
            }
            if (removedFiles != null) {
                removedFiles.addAll(actuallyRemovedFiles);
            }
            return actuallyChangedGroups;
        }
    }

    @Override
    public List<String> listAllLayerUrisInRepository(final long revisionNum, final HowGetFile howGet) throws IOException {
        return RadixLoaderUtils.listAllLayersInDir(getRoot());
    }

    @Override
    public File getRoot() {
        return root;
    }

    @Override
    protected CacheEntry getFileImpl(final String file, final long revisionNum) throws RadixLoaderException {
        return getFileImpl(file, revisionNum, HowGetFile.fromDir(root));
    }

    @Override
    protected CacheEntry getFileImpl(final String file, final long revisionNum, final HowGetFile howGet) throws RadixLoaderException {
        if (root.equals(howGet.directory)) {
            try {
                branchFileLocker.lock();
                final CacheEntry localFile = getLocalFile(file, revisionNum);
                if (localFile != null) {
                    return localFile;
                }
                final File f = new File(root, file);
                if (file.endsWith(".jar")) {
                    final File tmp_file = createTempFile("radix-starter-jarlib-");
                    final long startMillis = System.currentTimeMillis();
                    copyMaybeHardlink(tmp_file, f);
                    FILE_COPY_MILLIS.addAndGet(System.currentTimeMillis() - startMillis);
                    return new JarCacheEntry(new JarFile(tmp_file), true);
                } else {
                    return new FileCacheEntry(FileUtils.readFile(f));
                }
            } catch (IOException e) {
                throw new RadixLoaderException("Exception on RadixDirLoader.getFile()", e);
            } finally {
                branchFileLocker.unlock();
            }
        }
        return null;
    }

    @Override
    public String getRepositoryUri() { // by BAO
        return repositoryUri;
    }

    @Override
    public String getDescription() {
        return "RadixDirLoader: " + (root == null ? "<null>" : root.getAbsolutePath());
    }

    private static class BranchLocker {

        private final File branchFile;
        private FileLock fileLock;
        private int lockCount = 0;

        public BranchLocker(File branchFile) {
            this.branchFile = branchFile;
        }

        public synchronized void lock() throws IOException {
            if (lockCount == 0) {
                fileLock = lockFile();
            }
            lockCount++;
        }

        public synchronized void unlock() {
            if (lockCount == 1) {
                unlockFile(fileLock);
            }
            lockCount--;
        }

        protected FileLock lockFile() throws IOException {
            final FileChannel fc = new RandomAccessFile(branchFile, "rw").getChannel();
            final long startMillis = System.currentTimeMillis();
            for (int i = 0;; i++) {
                final FileLock l = fc.tryLock(0L, Long.MAX_VALUE, false);
                if (l != null) {
                    return l;
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    throw new InterruptedIOException(ex.getMessage());
                }
                int timeoutMillis = 120_000;
                if (System.currentTimeMillis() - startMillis > timeoutMillis) {
                    throw new IOException("Unable to lock working copy in " + timeoutMillis / 1000 + " seconds");
                }
            }
        }

        protected void unlockFile(final FileLock lock) {
            if (lock != null) {
                try {
                    lock.release();
                    lock.channel().close();
                } catch (IOException ex) {
                    //do nothing
                }
            }
        }
    }
}
