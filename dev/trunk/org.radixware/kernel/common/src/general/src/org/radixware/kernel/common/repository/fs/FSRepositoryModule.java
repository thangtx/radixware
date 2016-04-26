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
package org.radixware.kernel.common.repository.fs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.repository.Segment;
import org.radixware.kernel.common.repository.fs.RepositoryInjection.ModuleInjectionInfo;
import org.radixware.kernel.common.utils.FileUtils;

public abstract class FSRepositoryModule<T extends Module> implements IRepositoryModule<T> {

    private final File moduleDir;
    protected final T module;
    protected RepositoryInjection.ModuleInjectionInfo injectionInfo = null;

    protected FSRepositoryModule(File moduleDir) {
        this.moduleDir = moduleDir;
        this.module = null;
    }

    protected FSRepositoryModule(T module) {
        this.moduleDir = null;
        this.module = module;
    }

    @Override
    public File getDirectory() {
        if (moduleDir != null) {
            return moduleDir;
        } else {
            Segment segment = module.getSegment();
            if (segment != null) {
                File segmentDir = segment.getDirectory();
                if (segmentDir != null) {
                    return new File(segmentDir, getDirName());
                }
            }
            return null;
        }
    }

    protected String getDirName() {
        return getName();
    }

    @Override
    public File getDescriptionFile() {
        File dir = getDirectory();
        if (dir == null) {
            return null;
        }
        return new File(dir, Module.MODULE_XML_FILE_NAME);
    }

    @Override
    public InputStream getDescriptionData() throws IOException {
        if (injectionInfo != null && injectionInfo.isHasModuleXml()) {
            return injectionInfo.getDescriptionFileStream();
        }
        return new FileInputStream(getDescriptionFile());
    }

    @Override
    public String getName() {
        if (moduleDir != null) {
            return moduleDir.getName();
        } else {
            return module.getName();
        }
    }

    @Override
    public String getPath() {
        return getDirectory().getPath();
    }

    @Override
    public boolean isInjection() {
        return injectionInfo != null;
    }

    @Override
    public InputStream getDirectoryXmlData() throws IOException {
        if (injectionInfo != null && injectionInfo.isHasDirectoryXml()) {
            return injectionInfo.getDirectoryXmlFileStream();
        }
        return new FileInputStream(new File(getDirectory(), FileUtils.DIRECTORY_XML_FILE_NAME));
    }

    @Override
    public void installInjection(ModuleInjectionInfo injection) throws IOException {
        this.injectionInfo = injection;
        ((FSRepositorySegment) module.getSegment().getRepository()).keepInCache(this, true);
    }

    @Override
    public void uninstallInjection() {
        this.injectionInfo = null;
        ((FSRepositorySegment) module.getSegment().getRepository()).keepInCache(this, false);
    }

    protected void registerAsAPI() {
        if (module != null) {
            ((FSRepositorySegment) module.getSegment().getRepository()).keepInCache(this, true);
        }
    }

    private static void fillWithJars(File lib, final List<IJarDataProvider> files) {
        if (lib.exists() && lib.isDirectory()) {
            final File[] jars = lib.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    if (name.endsWith(".jar")) {
                        return true;
                    } else {
//                        File file = new File(dir, name);
//                        if (file.isDirectory()) {
//                            return true;
//                        } else {
//                            return false;
//                        }
                        return false;
                    }
                }
            });
            if (jars != null) {
                for (final File file : jars) {
                    if (file.isDirectory()) {
                        fillWithJars(file, files);
                    } else {
                        files.add(JarFileDataProvider.getInstance(file));
                    }
                }
            }
        }
    }

    @Override
    public List<IJarDataProvider> getBinaries() {
        final File dir = getDirectory();
        final List<IJarDataProvider> files = new ArrayList<>();
        if (dir != null && dir.exists()) {
            final File bin = new File(dir, "bin");
            fillWithJars(bin, files);
            final File lib = new File(dir, "lib");
            fillWithJars(lib, files);
        }
        return files;
    }
}
