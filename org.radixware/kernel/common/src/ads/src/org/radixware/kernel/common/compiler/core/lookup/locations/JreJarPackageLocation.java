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
package org.radixware.kernel.common.compiler.core.lookup.locations;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.LinkedList;
import java.util.List;
import org.eclipse.jdt.internal.compiler.env.NameEnvironmentAnswer;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.repository.fs.IJarDataProvider;
import org.radixware.kernel.common.repository.fs.JarFileDataProvider;

public final class JreJarPackageLocation extends JarBasedPackageLocation {

    private static final JreJarPackageLocation instance = new JreJarPackageLocation();

    public static final JreJarPackageLocation getInstance() {
        return instance;
    }

    private JreJarPackageLocation() {
        super(null);
    }

    protected File[] getLibDirs() {
        List<File> lookupDirs = new LinkedList<>();
        String javaHomePath = System.getProperty("java.home");
        File javaHome = javaHomePath == null ? null : new File(javaHomePath);
        if (javaHome != null && javaHome.exists()) {
            final File javaLib = new File(javaHome, "lib");
            if (javaLib.exists()) {
                collectDirs(javaLib, lookupDirs);
            }
        }
        return lookupDirs.toArray(new File[lookupDirs.size()]);
    }

    private void collectDirs(File root, final List<File> dirs) {
        dirs.add(root);
        root.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                if (pathname.isDirectory()) {
                    dirs.add(pathname);
                }
                return false;
            }
        });
    }

    @Override
    protected List<IJarDataProvider> getJarFiles() {
        List<IJarDataProvider> allFiles = new LinkedList<>();
        File[] libDirs = getLibDirs();
        for (File libDir : libDirs) {
            getJarFiles(libDir, allFiles);
        }
        return allFiles;
    }

    private void getJarFiles(File libDir, List<IJarDataProvider> files) {
        File[] jars = libDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".jar");
            }
        });
        if (jars != null) {
            for (File file : jars) {
                files.add(JarFileDataProvider.getInstance(file));
            }
        }

    }

    @Override
    public boolean containsPackageName(char[][] packageName) {
        return super.containsPackageName(packageName);
    }

    @Override
    public NameEnvironmentAnswer findAnswer(Definition definition, String suffix) {
        return null;
    }
}
