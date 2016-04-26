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

package org.radixware.kernel.common.build.xbeans;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import org.apache.xmlbeans.SystemProperties;
import org.apache.xmlbeans.impl.common.JarHelper;

class FSDirManager implements DirManager {

    @Override
    public File createDir(File rootdir, String subdir) {
        final File newdir = (subdir == null) ? rootdir : new File(rootdir, subdir);
        final boolean created = (newdir.exists() && newdir.isDirectory()) || newdir.mkdirs();
        assert created : "Could not create " + newdir.getAbsolutePath();
        return newdir;
    }

    @Override
    public File createTempDir() throws IOException {
        try {
            final File tmpDirFile = new File(SystemProperties.getProperty("java.io.tmpdir"));
            tmpDirFile.mkdirs();
        } catch (Exception e) {
            java.util.logging.Logger.getLogger(FSDirManager.class.getName()).log(Level.FINE,"", e);
        }
        

        File tmpFile = File.createTempFile("xbean", null);
        String path = tmpFile.getAbsolutePath();
        if (!path.endsWith(".tmp")) {
            throw new IOException(
                    "Error: createTempFile did not create a file ending with .tmp");
        }
        path = path.substring(0, path.length() - 4);
        File tmpSrcDir = null;

        for (int count = 0; count < 100; count++) {
            String name = path + ".d"
                    + (count == 0 ? "" : Integer.toString(count++));

            tmpSrcDir = new File(name);

            if (!tmpSrcDir.exists()) {
                boolean created = tmpSrcDir.mkdirs();
                assert created : "Could not create "
                        + tmpSrcDir.getAbsolutePath();
                break;
            }
        }
        tmpFile.deleteOnExit();

        return tmpSrcDir;
    }

    @Override
    public void removeDir(File dir) {
        tryHardToDelete(dir);
    }

    @Override
    public void deleteObsoleteFiles(File rootDir, File srcDir, Set seenFiles) {
        if (!(rootDir.isDirectory() && srcDir.isDirectory())) {
            throw new IllegalArgumentException();
        }
        String absolutePath = srcDir.getAbsolutePath();
        // Do a sanity check to make sure we don't delete by mistake some important dir
        if (absolutePath.length() <= 5) {
            return;
        }
        if (absolutePath.startsWith("/home/")
                && (absolutePath.indexOf('/', 6) >= absolutePath.length() - 1
                || absolutePath.indexOf('/', 6) < 0)) {
            return;
        }

        // Go recursively starting with srcDir and delete all files that are
        // not in the given Set
        File[] files = srcDir.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                deleteObsoleteFiles(rootDir, files[i], seenFiles);
            } else if (!seenFiles.contains(files[i])) {
                deleteXmlBeansFile(files[i]);
                deleteDirRecursively(rootDir, files[i].getParentFile());
            }
        }
    }

    static public void copyFile(File from, File to) throws IOException {
        FileInputStream sf = null;
        FileOutputStream st = null;
        try {
            sf = new FileInputStream(from);
            st = new FileOutputStream(to);

            byte[] buf = new byte[16384];
            while (true) {
                int bytesRead = sf.read(buf, 0, buf.length);
                if (bytesRead < 0) {
                    break;
                }
                st.write(buf, 0, bytesRead);
            }
        } finally {
            if (sf != null) {
                sf.close();
            }
            if (st != null) {
                st.close();
            }
        }
    }

    @Override
    public List<String> list(File path) {
        String[] r = path.list();
        List<String> ret = new ArrayList<>(r.length);
        ret.addAll(Arrays.asList(r));
        return ret;
    }

    @Override
    public InputStream read(File f) throws FileNotFoundException {
        return new FileInputStream(f);
    }

    @Override
    public OutputStream write(File f) throws FileNotFoundException {
        return new FileOutputStream(f);
    }

    @Override
    public void jarDir(File dir, File jar) throws IOException {
        new JarHelper().jarDir(dir, jar);
    }

    protected static void tryHardToDelete(File dir) {
        tryToDelete(dir);
        if (dir.exists()) {
            tryToDeleteLater(dir);
        }
    }

    private static void tryToDelete(File dir) {
        if (dir.exists()) {
            if (dir.isDirectory()) {
                String[] list = dir.list(); // can return null if I/O error
                if (list != null) {
                    for (int i = 0; i < list.length; i++) {
                        tryToDelete(new File(dir, list[i]));
                    }
                }
            }
            dir.delete();
        }
    }
    private static final Set<File> deleteFileQueue = new HashSet<>();
    private static int triesRemaining = 0;

    private static boolean tryNowThatItsLater() {
        List<File> files;

        synchronized (deleteFileQueue) {
            files = new ArrayList<>(deleteFileQueue);
            deleteFileQueue.clear();
        }

        List<File> retry = new ArrayList<>();

        for (Iterator<File> i = files.iterator(); i.hasNext();) {
            File file = i.next();
            tryToDelete(file);
            if (file.exists()) {
                retry.add(file);
            }
        }

        synchronized (deleteFileQueue) {
            if (triesRemaining > 0) {
                triesRemaining -= 1;
            }

            if (triesRemaining <= 0 || retry.isEmpty()) // done?
            {
                triesRemaining = 0;
            } else {
                deleteFileQueue.addAll(retry); // try again?
            }
            return (triesRemaining <= 0);
        }
    }

    private static void giveUp() {
        synchronized (deleteFileQueue) {
            deleteFileQueue.clear();
            triesRemaining = 0;
        }
    }

    private static void tryToDeleteLater(File dir) {
        synchronized (deleteFileQueue) {
            deleteFileQueue.add(dir);
            if (triesRemaining == 0) {
                new Thread() {
                    @Override
                    public void run() {
                        // repeats tryNow until triesRemaining == 0
                        try {
                            for (;;) {
                                if (tryNowThatItsLater()) {
                                    return; // succeeded
                                }
                                Thread.sleep(1000 * 3); // wait three seconds
                            }
                        } catch (InterruptedException e) {
                            giveUp();
                        }
                    }
                };
            }

            if (triesRemaining < 10) {
                triesRemaining = 10;
            }
        }
    }

    private static void deleteXmlBeansFile(File file) {
        if (file.getName().endsWith(".java")) {
            file.delete();
        }
    }

    private static void deleteDirRecursively(File root, File dir) {
        String[] list = dir.list();
        while (list != null && list.length == 0 && !dir.equals(root)) {
            dir.delete();
            dir = dir.getParentFile();
            list = dir.list();
        }
    }
}
