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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.*;
import org.radixware.kernel.starter.Starter;
import org.radixware.kernel.starter.utils.FileUtils;
import org.radixware.kernel.starter.utils.HexConverter;
import org.radixware.kernel.starter.utils.SystemTools;

public class TempFiles {

    public static final String REMOVE_LIST_FILE_PREFIX = "temp-files-to-remove-";
    public static final String REMOVE_LIST_FILE_WITH_PID_PREFIX = "pid-temp-files-to-remove-";
    public static final String CONTENT_DIR_PREFIX = "content-";
    public static final String REMOVE_MARK = "[ready-to-remove]";
    private static final String FILE_CHARSET_NAME = "UTF-8";
    private final FileLock lock;
    private final File file;
    private File contentDir;

    protected TempFiles(final File registryFile) {
        try {
            lock = tryLockFile(registryFile);
        } catch (IOException ex) {
            throw new RuntimeException("Unable to lock registry for temporary files", ex);
        }
        if (lock != null) {
            file = registryFile;
        } else {
            file = null;
        }
    }

    private FileLock tryLockFile(final File file) throws IOException {
        final FileChannel channel = new RandomAccessFile(file, "rw").getChannel();
        return channel.tryLock(0, Long.MAX_VALUE, false);
    }

    public TempFiles() {
        this(createNewRegistryFile());
    }

    public synchronized File getContentDir() {
        if (contentDir != null) {
            return contentDir;
        }
        if (file == null) {
            return null;
        }
        contentDir = new File(file.getParentFile(), CONTENT_DIR_PREFIX + file.getName());
        register(contentDir.getAbsolutePath());
        if (!contentDir.mkdir()) {
            throw new IllegalStateException("Unable to create subdirectory for temp files: " + contentDir.getAbsolutePath());
        }
        return contentDir;
    }

    public synchronized void register(final String file) {
        register(Collections.singleton(file));
    }

    public synchronized void register(final Collection<String> files) {
        if (lock == null) {
            return;
        }
        try {
            appendToFile(lock.channel(), files);
        } catch (IOException ex) {
            //do nothing
        }
    }

    /**
     * Deletes registered temporary files.
     *
     * If all registered files were successfully removed, than the registry file
     * itself will be removed too, and true will be returned. Otherwise only
     * names of the undeleted files will be kept and false will be returned.
     *
     * @return true if all registered files were removed, false otherwise
     */
    public synchronized boolean deleteRegisteredFiles() {
        return deleteRegisteredFiles(false);
    }

    protected boolean deleteRegisteredFiles(boolean releaseLock) {
        if (lock == null || !lock.isValid()) {
            return false;
        }
        try {
            try {
                lock.channel().position(0);
            } catch (IOException ex) {
                return false;
            }
            final Scanner sc = new Scanner(lock.channel(), FILE_CHARSET_NAME);
            if (file.getName().startsWith(REMOVE_LIST_FILE_PREFIX) && !sc.hasNextLine()) {
                //this file is in construction, we've just locked it before the creator
                return false;
            }
            final List<String> undeletedFiles = new ArrayList<>();
            while (sc.hasNextLine()) {
                final String fileToRemove = sc.nextLine().trim();
                if (REMOVE_MARK.equals(fileToRemove)) {
                    return true;
                }
                if (!fileToRemove.isEmpty()) {
                    try {
                        FileUtils.removeRecursively(new File(fileToRemove));
                    } catch (RadixLoaderException ex) {
                        undeletedFiles.add(fileToRemove);
                    }
                }
            }
            if (undeletedFiles.isEmpty()) {
                try {
                    lock.channel().close();
                    if (file != null) {
                        FileUtils.removeRecursively(file);
                    }
                } catch (IOException ex) {
                    //do nothing
                }
                return true;
            } else {
                try {
                    writeToFile(lock.channel(), undeletedFiles);
                } catch (IOException ex) {
                    //do nothing
                }
                return false;
            }
        } finally {
            if (releaseLock) {
                try {
                    lock.channel().close();
                } catch (IOException ex) {
                    //do nothing
                }
            }
        }
    }

    public static Runnable createRemoveTask() {
        return new Runnable() {
            @Override
            public void run() {
                //it's important to get list of files before getting pids of currently running processes
                final File[] registryFiles = getFilesWithLists();
                final Set<Integer> runningPids = SystemTools.getRunningProcessPids();
                //if pids are not determined, then we will skip pid based clearing mechanism because of
                //risk of corrupting currently used files.
                final boolean pidsDetermined = runningPids != null && !runningPids.isEmpty();
                for (final File file : registryFiles) {
                    try {
                        if (Thread.interrupted()) {
                            return;
                        }
                        boolean remove = file.getName().startsWith(REMOVE_LIST_FILE_PREFIX);//old format
                        if (!remove && pidsDetermined) {
                            //pid of the process that created temporary files registry is encoded in registry file name
                            //delete temporary files only for non-running processes
                            final Integer pid = Integer.valueOf(file.getName().split("~")[1]);
                            if (!runningPids.contains(pid)) {
                                remove = true;
                            }
                        }
                        if (remove) {
                            new TempFiles(file).deleteRegisteredFiles(true);
                        }
                    } catch (Exception ex) {
                        //continue
                    }
                }
            }
        };
    }

    private static void writeToFile(final FileChannel channel, final Collection<String> fileNames) throws IOException {
        channel.truncate(0);
        appendToFile(channel, fileNames);
    }

    private static void appendToFile(final FileChannel channel, final Collection<String> fileNames) throws IOException {
        channel.write(ByteBuffer.wrap(toBytes(fileNames)));
    }

    private static byte[] toBytes(final Collection<String> fileNames) {
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        for (String fileName : fileNames) {
            try {
                bos.write(fileName.getBytes(FILE_CHARSET_NAME));
                bos.write("\n".getBytes());
            } catch (IOException ex) {
                //should not happen
            }
        }
        return bos.toByteArray();
    }

    private static File[] getFilesWithLists() {
        final File tmpDir = SystemTools.getTmpDir();
        return tmpDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(final File dir, final String name) {
                return name.startsWith(REMOVE_LIST_FILE_PREFIX) || name.startsWith(REMOVE_LIST_FILE_WITH_PID_PREFIX);
            }
        });
    }

    private static File createNewRegistryFile() {
        String appName;
        if (Starter.isRoot()) {
            appName = "starter";
        } else {
            appName = (Starter.getArguments() == null || Starter.getArguments().getAppParameters() == null || Starter.getArguments().getAppParameters().isEmpty()) ? "unknown" : Starter.getArguments().getAppParameters().get(0).toLowerCase();
            final int lastDotIdx = appName.lastIndexOf(".");
            if (lastDotIdx >= 0) {
                appName = appName.substring(lastDotIdx + 1);
            }
        }
        int randNumber = UUID.randomUUID().hashCode();
        ByteBuffer bb = ByteBuffer.wrap(new byte[Integer.SIZE / 8]);
        bb.putInt(randNumber);
        return new File(SystemTools.getTmpDir(), REMOVE_LIST_FILE_WITH_PID_PREFIX + appName + "-" + HexConverter.toHex(bb.array()) + "~" + SystemTools.getCurrentProcessPid());
    }
}
