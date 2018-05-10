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

package org.radixware.kernel.common.builder.check.java;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.repository.fs.IJarDataProvider;
import org.radixware.kernel.common.repository.fs.JarFileDataProvider;
import org.radixware.kernel.common.utils.JarFiles;


public class JarFileClassLinker {

    private static class MyClassLoader extends ClassLoader {

        List<File> files = null;
        private final File srcFile;
        private Map<String, Class> classes = new HashMap<>();

        public MyClassLoader(File srcFile) {
            this.srcFile = srcFile;
        }

        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            Class result = classes.get(name);
            if (result != null) {
                return result;
            }
            lookupFiles();
            String[] parts = name.split("\\.");
            for (File file : files) {
                JarFileDataProvider provider = JarFileDataProvider.getInstance(file);
                int offset = parts.length - 1;
                while (offset >= 0) {
                    StringBuilder entryName = new StringBuilder();
                    for (int i = 0; i < parts.length; i++) {
                        if (i > 0) {
                            if (i > offset) {
                                entryName.append("$");
                            } else {
                                entryName.append("/");
                            }
                        }
                        entryName.append(parts[i]);
                    }
                    offset--;
                    entryName.append(".class");
                    String classFileName = entryName.toString();
                    if (provider.entryExists(classFileName)) {
                        try {
                            byte[] bytes = provider.getEntryData(classFileName);
                            Class c = defineClass(name, bytes, 0, bytes.length);
                            classes.put(name, c);
                            return c;
                        } catch (IOException ex) {
                            Logger.getLogger(JarFileClassLinker.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }

            return super.findClass(name); //To change body of generated methods, choose Tools | Templates.
        }

        public List<File> lookupFiles() {
            if (files != null) {
                return files;
            }
            files = new LinkedList<>();
            files.add(srcFile);
            File rootDir = new File("/home/akrylov/ssd/twrbs/1.1.18.10");
            processLayer(new File(rootDir, "org.radixware"));
            processLayer(new File(rootDir, "com.tranzaxis"));

            return files;
        }

        private void processLayer(File layerDir) {
            File kernel = new File(layerDir, "kernel");
            kernel.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    if (pathname.isDirectory()) {
                        pathname.listFiles(new FileFilter() {
                            @Override
                            public boolean accept(File pathname) {
                                if (pathname.isDirectory() && ("lib".equals(pathname.getName()) || "bin".equals(pathname.getName()))) {
                                    File[] result = JarFiles.lookupJars(pathname);
                                    if (result != null && result.length > 0) {
                                        for (File f : result) {
                                            files.add(f);
                                        }
                                    }
                                    return false;
                                }
                                return false;
                            }
                        });

                    }
                    return false;
                }
            });
            File ads = new File(layerDir, "ads");
            ads.listFiles(new FileFilter() {
                @Override
                public boolean accept(File adsModule) {
                    if (adsModule.isDirectory()) {
                        adsModule.listFiles(new FileFilter() {
                            @Override
                            public boolean accept(File dirOrFile) {
                                if (dirOrFile.isDirectory() && ("lib".equals(dirOrFile.getName()) || "bin".equals(dirOrFile.getName()))) {
                                    File[] result = JarFiles.lookupJars(dirOrFile);
                                    if (result != null && result.length > 0) {
                                        for (File f : result) {                                            
                                            files.add(f);
                                        }
                                    }
                                }
                                return false;
                            }
                        });

                    }
                    return false;
                }
            });
        }

        public void resolve(Class c) {
            resolveClass(c);
            c.getDeclaredAnnotations();
            c.getDeclaredClasses();
            c.getDeclaredConstructors();
            c.getDeclaredFields();
            c.getDeclaredMethods();
        }
    }

    public static void main(String[] args) throws ClassNotFoundException {
        File src = new File("/home/akrylov/BUG/Binary/rep.zip");

        JarFileDataProvider provider = JarFileDataProvider.getInstance(src);
        MyClassLoader cl = new MyClassLoader(src);
        for (IJarDataProvider.IJarEntry name : provider.entries()) {

            String cn = name.getName();
            if (cn.endsWith(".class")) {
                cn = cn.substring(0, cn.length() - 6);
                String className = cn.replace("/", ".");
                System.out.print("Resolving class " + className + "...");
                Class c = cl.loadClass(className);
                cl.resolve(c);
                System.out.println("done");
            }
        }
    }
}
