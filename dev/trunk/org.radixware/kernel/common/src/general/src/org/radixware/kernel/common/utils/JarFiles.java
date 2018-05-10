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
package org.radixware.kernel.common.utils;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;
import org.radixware.kernel.common.repository.fs.IJarDataProvider;

public class JarFiles {

    public static File[] lookupJars(File dir) {

        return dir.listFiles(new FileFilter() {

            @Override
            public boolean accept(File pathname) {
                return pathname.getName().endsWith(".jar");
            }
        });
    }

    public static File[] lookupJarsRecursive(File dir) {
        ArrayList<File> files = new ArrayList<File>();
        lookupJarsRecursive(dir, files);
        return files.toArray(new File[]{});
    }

    public static void lookupJarsRecursive(File dir, Collection<File> files) {
        File[] selection = dir.listFiles(new FileFilter() {

            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory() || (pathname.isFile() && pathname.getName().endsWith(".jar"));
            }
        });
        if (selection != null) {
            for (File f : selection) {
                if (f.isDirectory()) {
                    lookupJarsRecursive(f, files);
                } else {
                    files.add(f);
                }
            }
        }
    }

    public static File[] lookupJars(File[] dirs) {
        ArrayList<File> files = new ArrayList<File>();
        for (int i = 0; i < dirs.length; i++) {
            File[] jars = lookupJars(dirs[i]);
            if (jars != null) {
                files.addAll(Arrays.asList(jars));
            }
        }
        File[] res = new File[files.size()];
        files.toArray(res);
        return res;
    }

    public static List<String> lookupClasses(IJarDataProvider jar) {

        try {
            int ld = ".class".length();
            List<String> resultClasses = new ArrayList<String>();
            for (IJarDataProvider.IJarEntry e : jar.entries()) {
                if (!e.isDirectory() && e.getName().endsWith(".class")) {
                    int newLength = e.getName().length() - ld;
                    resultClasses.add(e.getName().replace('/', '.').replace('$', '.').substring(0, newLength));
                }
            }
            return resultClasses;
        } finally {
            try {
                jar.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

    public static List<String> lookupClassFileNames(IJarDataProvider jar) {

        try {

            List<String> resultClasses = new ArrayList<String>();
            int ld = ".class".length();
            for (IJarDataProvider.IJarEntry e : jar.entries()) {
                if (!e.isDirectory() && e.getName().endsWith(".class")) {
                    int newLength = e.getName().length() - ld;
                    resultClasses.add(e.getName().substring(0, newLength));
                }
            }
            return resultClasses;
        } finally {
            try {
                jar.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private static void collectFilesForJar(File sourceDir, ArrayList<File> result, FileFilter filter) {
        File[] files = sourceDir.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                if (file.isDirectory() && filter.accept(file)) {
                    collectFilesForJar(file, result, filter);
                } else {
                    if (filter == null) {
                        result.add(file);
                    } else if (filter.accept(file)) {
                        result.add(file);
                    }
                }
            }
        }
    }

    public static boolean mkJar(File sourceDir, File jarFile, FileFilter filter, boolean cleanUpBinaries) throws IOException {
        return mkJar(sourceDir, jarFile, filter, null, cleanUpBinaries);
    }

    public static boolean mkJar(File[] sourceDirs, File jarFile, FileFilter filter, boolean cleanUpBinaries) throws IOException {
        return mkJar(sourceDirs, jarFile, filter, null, cleanUpBinaries);
    }

    public static Collection<File> listFiles(File sourceDir, FileFilter filter, String pathPrefix) {
        ArrayList<File> fileList = new ArrayList<File>();
        collectFilesForJar(sourceDir, fileList, filter);
        return fileList;
    }
    
    public static boolean mkJar(File sourceDir, File jarFile, FileFilter filter, String pathPrefix, boolean cleanUpBinaries) throws IOException {
        return mkJar(new File[]{sourceDir}, jarFile, filter, pathPrefix, cleanUpBinaries);
    }
    
    public static boolean mkJar(File sourceDirs[], File jarFile, FileFilter filter, String pathPrefix, boolean cleanUpBinaries) throws IOException {
        return mkJarFile(sourceDirs, jarFile, filter, pathPrefix == null ? null : new FilePathPrefix(pathPrefix), cleanUpBinaries);
    }
    
    public static boolean mkJarFile(File sourceDir, File jarFile, FileFilter filter, FilePathPrefix pathPrefix, boolean cleanUpBinaries) throws IOException {
        return mkJarFile(new File[]{sourceDir}, jarFile, filter, pathPrefix, cleanUpBinaries);
    }

    public static boolean mkJarFile(File sourceDirs[], File jarFile, FileFilter filter, FilePathPrefix pathPrefix, boolean cleanUpBinaries) throws IOException {

        assert filter != null;

        class File2RelPath {

            File file;
            String relPath;

            public File2RelPath(File file, String relPath) {
                this.file = file;
                this.relPath = relPath;
            }
        }
        List<File2RelPath> filePathList = new ArrayList<>();
        ArrayList<File> fileList = new ArrayList<>();

//        String prefix = null;
//
//        if (pathPrefix != null) {
//            prefix = pathPrefix.replace(File.separatorChar, '/');
//            if (!prefix.endsWith("/")) {
//                prefix += "/";
//            }
//        }
        for (File sourceDir : sourceDirs) {
            fileList.clear();
            collectFilesForJar(sourceDir, fileList, filter);
            for (File file : fileList) {
                String entryPath;
                if (pathPrefix == null) {
                    entryPath = FileUtils.getRelativePath(sourceDir, file).replace(File.separatorChar, '/');
                } else {
                    entryPath = pathPrefix.getFilePath(file);
                }
                filePathList.add(new File2RelPath(file, entryPath));
            }

        }
        if (fileList.isEmpty()) {
            return false;
        }
        if (!jarFile.exists()) {
            File dir = jarFile.getParentFile();
            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    return false;
                }
            }
        }

        JarOutputStream stream = null;
        try {
            //Manifest manifest = new Manifest();
            //manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");

            stream = new JarOutputStream(new FileOutputStream(jarFile)/*
             * , manifest
             */);

            for (File2RelPath file2path : filePathList) {
                File file = file2path.file;
                String entryPath = file2path.relPath;

                JarEntry newEntry = new JarEntry(entryPath);
                newEntry.setMethod(ZipEntry.DEFLATED);
                newEntry.setTime(-1);

                try {
                    stream.putNextEntry(newEntry);
                    FileInputStream fileStream = null;
                    try {
                        fileStream = new FileInputStream(file);
                        FileUtils.copyStream(fileStream, stream);
                    } finally {
                        if (fileStream != null) {
                            fileStream.close();
                        }
                        if (cleanUpBinaries) {
                            if (file.getName().endsWith(".class")) {
                                file.delete();
                            }
                        }
                    }
                    stream.closeEntry();

                } catch (IOException e) {
                }
            }
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
        return true;
    }
    
    public static File[] lookupJreJars() {
        String javaHomePath = System.getProperty("java.home");
        File javaHome = javaHomePath == null ? null : new File(javaHomePath);
        if (javaHome != null && javaHome.exists()) {
//            File javaLib = null;
//            if ("Mac OS X".equals(System.getProperty("os.name"))) {
//                javaLib = new File(javaHome.getParentFile(), "Classes");
//            } else {
            final File javaLib = new File(javaHome, "lib");
//            }
            if (javaLib.exists()) {
                return JarFiles.lookupJarsRecursive(javaLib);
            }
        }
        return new File[0];
    }
}
