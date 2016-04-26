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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.repository.ads.fs.IKernelLookup;
import org.radixware.kernel.common.repository.fs.IJarDataProvider;
import org.radixware.kernel.starter.meta.DirectoryMeta;
import org.radixware.kernel.starter.meta.FileMeta;
import org.radixware.kernel.starter.meta.LayerMeta;
import org.radixware.kernel.starter.radixloader.RadixLoaderException;


class RfsRepositoryKernelLookup implements IKernelLookup {

    private final LayerMeta layer;
    private final ReleaseRepository release;
    private final DirectoryMeta dir;

    public RfsRepositoryKernelLookup(ReleaseRepository release, LayerMeta layer, DirectoryMeta dir) {
        this.layer = layer;
        this.release = release;
        this.dir = dir;
    }

    @Override
    public String[] listModules(ERuntimeEnvironmentType env) {
        switch (env) {
            case COMMON:
                return new String[]{"common"};
            case SERVER:
                return new String[]{"common", "server"};
            case EXPLORER:
                return new String[]{"common", "explorer"};
            default:
                return new String[0];
        }
    }

    private String getModuleDirName(String moduleName) {
        return dir.getDirectory() + "/" + moduleName;
    }

    @Override
    public IJarDataProvider[] listJars(String moduleName, ERuntimeEnvironmentType env) {
        if (layer != null && dir != null) {
            HashMap<String, FileMeta> files = new HashMap<String, FileMeta>();
            try {
                layer.toMap(files, null, null, null);
                ArrayList<IJarDataProvider> providers = new ArrayList<IJarDataProvider>();
                String moduleDirName = getModuleDirName(moduleName);
                for (Map.Entry<String, FileMeta> e : files.entrySet()) {
                    if (e.getKey().startsWith(moduleDirName) && e.getKey().endsWith(".jar")) {
                        FileMeta fileMeta = e.getValue();
                        if ("KernelCommon".equals(fileMeta.getGroupType())) {
                            if (env != ERuntimeEnvironmentType.COMMON) {
                                continue;
                            }
                        } else if ("KernelServer".equals(fileMeta.getGroupType())) {
                            if (env == ERuntimeEnvironmentType.EXPLORER) {
                                continue;
                            }
                        } else if ("KernelExplorer".equals(fileMeta.getGroupType())) {
                            if (env == ERuntimeEnvironmentType.SERVER) {
                                continue;
                            }
                        } else {
                            continue;
                        }
                        providers.add(new RfsJarFileDataProvider(release, fileMeta));
                    }
                }
                if (env == ERuntimeEnvironmentType.SERVER) {
                    String bp = System.getProperty("org.radixware.kernel.uds.buildPath");
                    if (bp != null) {
                        String[] strings = bp.split(File.pathSeparator);
                        for (String path : strings) {
                            File file = new File(path);
                            if (file.exists()) {
                                providers.add(new RfsJarFileDataProvider(release, file));
                            }
                        }
                    }
                }

                return providers.toArray(new IJarDataProvider[0]);
            } catch (RadixLoaderException ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
        return new IJarDataProvider[0];
    }

    @Override
    public IJarDataProvider[] listAllJars(ERuntimeEnvironmentType env) {
        String[] modules = listModules(env);
        ArrayList<IJarDataProvider> providers = new ArrayList<IJarDataProvider>();
        for (String module : modules) {
            IJarDataProvider[] pps = listJars(module, env);
            if (pps != null) {
                for (IJarDataProvider p : pps) {
                    providers.add(p);
                }
            }
        }
        return providers.toArray(new IJarDataProvider[0]);
    }
}
