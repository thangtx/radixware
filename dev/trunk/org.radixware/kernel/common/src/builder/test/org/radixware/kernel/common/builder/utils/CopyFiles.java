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

package org.radixware.kernel.common.builder.utils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.utils.FileUtils;


public class CopyFiles {

    public static void main(String args[]) {
        try {
            //  copyAds(new File("/home/akrylov/ssd/radix/dev/trunk/org.radixware/ads"), new File("/home/akrylov/ssd/radix/dev/1.2.12.9/org.radixware/ads"));
            Map<File, File> copy = new HashMap<File, File>();
            List<File> delete = new LinkedList<File>();
            for (String s : new String[]{/*"explorer/test",*/ /*"server/src", "server/xsd", */ /*"common/src/general/src",
                 */
                /*     "common/src/ads/src/org/radixware/kernel/common/defs/ads/ui/",
                 "common/src/ads/src/org/radixware/kernel/common/defs/ads/src/ui/",*/
                /*
                 "common/src/client/src",*/
                //"common/src/general/xsd",/*

                //  "common/xsd/adsdef.xsd",
                //"common/src/builder/src",
                "web/src",
                "web-app/project", /*     "designer/src/ads.editors/src/org/radixware/kernel/designer/ads/editors/clazz/forms"/*,
             "utils/src"*/}) {
                System.out.println(" ==================================================== " + s + " ==================================================== ");

                copyKernelModule(new File("/home/akrylov/ssd/radix/dev/trunk/org.radixware/kernel"), new File("/home/akrylov/ssd/radix/dev/1.2.12.9/org.radixware/kernel"), s, copy, delete);
            }
            boolean showOnly = false;
            if (showOnly) {
                return;
            }
            if (!copy.isEmpty()) {
                System.out.println("Following " + copy.size() + " files and directories will be replaced:");
                for (File f : copy.values()) {
                    if (f.isFile()) {
                        System.out.print("file: ");
                    } else {
                        System.out.print("dir:  ");
                    }
                    System.out.println(f.getAbsolutePath());
                }
                if (!showOnly) {
                    for (Map.Entry<File, File> e : copy.entrySet()) {
                        if (e.getKey().isDirectory()) {
                            if (e.getValue().isDirectory() || !e.getValue().exists()) {
                                FileUtils.copyDirectory(e.getKey(), e.getValue(), new FileFilter() {
                                    @Override
                                    public boolean accept(File pathname) {
                                        if (pathname.isFile()) {
                                            return !pathname.getName().endsWith(".class") && !pathname.getName().endsWith(".jar") && !pathname.getName().endsWith("~") && !pathname.getName().endsWith(".zip");
                                        } else {
                                            return pathname.isDirectory() && !pathname.getName().equals(".svn") && !(pathname.getName().equals("private") && pathname.getParentFile().getName().equals("nbproject")) && !(pathname.getParentFile().getName().equals("build") && (pathname.getName().startsWith("nbexec") || pathname.getName().startsWith("rest") || pathname.getName().equals("cluster")));
                                        }
                                    }
                                });
                            } else {
                                System.err.println("Error on copy " + e.getKey().getAbsolutePath());
                            }
                        } else {
                            if (e.getValue().isFile() || !e.getValue().exists()) {
                                FileUtils.copyFile(e.getKey(), e.getValue());
                            } else {
                                System.err.println("Error on copy " + e.getKey().getAbsolutePath());
                            }
                        }
                    }
                }
            }
            if (!delete.isEmpty()) {
                System.out.println("Following " + copy.size() + " files and directories will be deleted:");
                for (File f : delete) {
                    if (f.isFile()) {
                        System.out.print("file: ");
                        if (!showOnly) {
                            FileUtils.deleteFile(f);
                        }
                    } else {
                        System.out.print("dir:  ");
                        if (!showOnly) {
                            FileUtils.deleteDirectory(f);
                        }
                    }
                    System.out.println(f.getAbsolutePath());
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static void processKernelDirDeleted(File srcKernel, File destKernel, Map<File, File> copy, List<File> delete) throws IOException {


        //----------deletion------------
        File[] files = destKernel.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isFile() && !pathname.getName().endsWith(".class") && !pathname.getName().endsWith(".jar") && !pathname.getName().endsWith("~") && !pathname.getName().endsWith(".zip");
            }
        });

        File[] dirs = destKernel.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory() && !pathname.getName().equals(".svn") && !(pathname.getName().equals("private") && pathname.getParentFile().getName().equals("nbproject")) && !(pathname.getParentFile().getName().equals("build") && (pathname.getName().startsWith("nbexec") || pathname.getName().startsWith("rest") || pathname.getName().equals("cluster")));
            }
        });

        for (File d : dirs) {
            File target = new File(srcKernel, d.getName());
            if (!target.isDirectory()) {
                System.out.println("D:" + target.getAbsolutePath());
                delete.add(d);
            } else {
                processKernelDirDeleted(target, d, copy, delete);
            }
        }
        for (File f : files) {
            File target = new File(srcKernel, f.getName());
            if (!target.isFile()) {
                System.out.println("D:" + target.getAbsolutePath());
                delete.add(f);
            }
        }
    }

    private static void processKernelDirModified(File srcKernel, File destKernel, Map<File, File> copy, List<File> delete) throws IOException {
        File[] dirs = srcKernel.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory() && !pathname.getName().equals(".svn") && !(pathname.getName().equals("private") && pathname.getParentFile().getName().equals("nbproject")) && !(pathname.getParentFile().getName().equals("build") && (pathname.getName().startsWith("nbexec") || pathname.getName().startsWith("rest") || pathname.getName().equals("cluster") || pathname.getName().startsWith(".")));
            }
        });
        File[] files = srcKernel.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isFile() && !pathname.getName().endsWith(".class") && !pathname.getName().endsWith(".jar") && !pathname.getName().endsWith("~") && !pathname.getName().endsWith(".zip") && !pathname.getName().startsWith(".");
            }
        });
        for (File d : dirs) {
            File target = new File(destKernel, d.getName());
            if (!target.isDirectory()) {
                System.out.println("A:" + target.getAbsolutePath());
                copy.put(d, target);
            } else {
                processKernelDirModified(d, target, copy, delete);
            }
        }
        for (File f : files) {
            File target = new File(destKernel, f.getName());
            if (!target.isFile()) {
                System.out.println("A:" + target.getAbsolutePath());
                copy.put(f, target);
            } else {
                if (!equals(f, target)) {
                    System.out.println("M:" + target.getAbsolutePath());
                    copy.put(f, target);
                }
            }
        }

    }

    private static void copyKernelModule(File srcKernel, File destKernel, String modulePath, Map<File, File> copy, List<File> delete) throws IOException {
        File srcPath = new File(srcKernel, modulePath);
        File destPath = new File(destKernel, modulePath);
        processKernelDirModified(srcPath, destPath, copy, delete);
        processKernelDirDeleted(srcPath, destPath, copy, delete);

    }

    private static void copyAds(File src, File dest) throws IOException {
        File[] m1 = listModules(src);
        File[] m2 = listModules(dest);
        Map<String, File> m2ns = new HashMap<String, File>();
        for (File f : m1) {
            m2ns.put(f.getName(), f);
        }
        Map<String, File> m2nd = new HashMap<String, File>();
        for (File f : m2) {
            m2nd.put(f.getName(), f);
        }

        List<File> newModules = new LinkedList<File>();
        for (File f : m1) {
            if (!m2nd.containsKey(f.getName())) {
                newModules.add(f);
            }
        }
        System.out.println("New modules:");
        for (File f : newModules) {
            System.out.println(f.getPath());
        }

        for (File srcModule : m1) {
            if (newModules.contains(srcModule)) {
                continue;
            }
            File destModule = m2nd.get(srcModule.getName());
            File[] srcDefs = listDefinitions(srcModule);
            File[] destDefs = listDefinitions(destModule);

            Map<String, File> destDefsMap = new HashMap<String, File>();
            for (File f : destDefs) {
                destDefsMap.put(f.getName(), f);
            }

            List<File> newDefs = new LinkedList<File>();
            List<File> modifiedDefs = new LinkedList<File>();
            for (File f : srcDefs) {
                if (destDefsMap.containsKey(f.getName())) {
                    if (!equals(f, destDefsMap.get(f.getName()))) {
                        modifiedDefs.add(f);
                    }
                } else {
                    newDefs.add(f);
                }
            }
            File destSrcFile = new File(destModule, "src");
            if (!newDefs.isEmpty() || !modifiedDefs.isEmpty()) {
                System.out.println("============= Module " + srcModule.getName() + " =============");
                if (!newDefs.isEmpty()) {
                    System.out.println("New Definitions:");
                    for (File f : newDefs) {
                        System.out.println("    " + f.getName());
                        File destFile = new File(destSrcFile, f.getName());
                        FileUtils.copyFile(f, destFile);
                    }
                }
                if (!modifiedDefs.isEmpty()) {
                    System.out.println("Modified Definitions:");
                    for (File f : modifiedDefs) {
                        System.out.println("    " + f.getName());
                        File destFile = new File(destSrcFile, f.getName());
                        FileUtils.copyFile(f, destFile);
                    }
                }
            }
        }
    }

    private static File[] listModules(File s) {
        return s.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                if (pathname.isDirectory()) {
                    File moduleXml = new File(pathname, "module.xml");
                    if (moduleXml.isFile()) {
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private static boolean equals(File f1, File f2) throws IOException {
        byte[] b1 = FileUtils.readBinaryFile(f1);
        byte[] b2 = FileUtils.readBinaryFile(f2);
        if (b1.length != b2.length) {
            return false;
        }
        for (int i = 0; i < b1.length; i++) {
            if (b1[i] != b2[i]) {
                return false;
            }
        }
        return true;
    }

    private static final File[] listDefinitions(File module) {
        File srcDir = new File(module, "src");
        File[] defs = srcDir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isFile() && pathname.getName().endsWith(".xml");
            }
        });
        return defs;
    }
}
