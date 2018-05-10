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

package org.radixware.kernel.release.fs;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.repository.fs.IJarDataProvider;
import org.radixware.kernel.common.repository.fs.IRepositoryModule;
import org.radixware.kernel.common.repository.fs.JarFileDataProvider;
import org.radixware.kernel.common.repository.fs.RepositoryInjection.ModuleInjectionInfo;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.starter.meta.FileMeta;
import org.radixware.kernel.starter.meta.LayerMeta;
import org.radixware.kernel.starter.radixloader.RadixLoaderException;


abstract class RfsRepositoryModule<T extends Module> implements IRepositoryModule<T> {

    protected final String dirName;
    private final String name;
    protected final RfsRepositorySegment<T> segment;
    protected Module module;

    public RfsRepositoryModule(RfsRepositorySegment<T> segment, String dirName) {
        if (!dirName.endsWith("/")) {
            this.dirName = dirName + "/";
        } else {
            this.dirName = dirName;
        }
        String[] names = this.dirName.split("/");
        if (names.length > 0) {
            this.name = names[names.length - 1];
        } else {
            this.name = "NONAME";
        }
        this.segment = segment;
    }

    @Override
    public File getDirectory() {
        return null;
    }

    public File getAPIDirectory() {
        return getDirectory();
    }

    @Override
    public File getDescriptionFile() {
        return null;
    }

    @Override
    public InputStream getDescriptionData() throws IOException {
        return getRelease().getRepositoryFileStream(dirName + Module.MODULE_XML_FILE_NAME);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getPath() {
        return dirName;
    }

    File getTemporaryStorage() {
        return new File(segment.getTemporaaryStorage(), dirName);
    }

    ReleaseRepository getRelease() {
        return segment.getRelease();
    }

    protected RfsRepositoryLayer getLayer() {
        return segment.layer;
    }

    @Override
    public boolean isInjection() {
        return false;
    }

    @Override
    public InputStream getDirectoryXmlData() throws IOException {
        return getRelease().getRepositoryFileStream(dirName + FileUtils.DIRECTORY_XML_FILE_NAME);
    }

    @Override
    public void installInjection(ModuleInjectionInfo injection) {
        throw new UnsupportedOperationException("Injections are not supported for RFS modules");
    }

    @Override
    public void uninstallInjection() {
        //do nothing
    }

    @Override
    public List<IJarDataProvider> getBinaries() {
        LayerMeta layer;
        if (getLayer() == null) {
            return Collections.emptyList();
        }
        layer = getLayer().getMeta();
        if (layer != null) {
            final List<IJarDataProvider> result = new ArrayList<>();
            HashMap<String, FileMeta> files = new HashMap<>();
            try {
                layer.toMap(files, null, null, null);
                for (Map.Entry<String, FileMeta> e : files.entrySet()) {
                    if (e.getKey().startsWith(dirName) && e.getKey().endsWith(".jar")) {
                        FileMeta fileMeta = e.getValue();
                        if ("KernelCommon".equals(fileMeta.getGroupType())
                                || "KernelServer".equals(fileMeta.getGroupType())
                                || "KernelExplorer".equals(fileMeta.getGroupType())) {

                            result.add(new RfsJarFileDataProvider(getRelease(), fileMeta));
                        }
                    }
                }
                String bp = System.getProperty("org.radixware.kernel.uds.buildPath");
                if (bp != null) {
                    String[] strings = bp.split(File.pathSeparator);
                    for (String path : strings) {
                        File file = new File(path);
                        if (file.exists()) {
                            result.add(new RfsJarFileDataProvider(getRelease(), file));
                        }
                    }
                }

            } catch (RadixLoaderException ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
            }
            return result;
        }
        return Collections.emptyList();
    }

    @Override
    public void setModule(T module) {
    }
}
