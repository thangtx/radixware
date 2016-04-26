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

package org.radixware.kernel.common.repository.ads.fs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.compiler.CompilerConstants;
import org.radixware.kernel.common.repository.fs.IJarDataProvider;
import org.radixware.kernel.common.repository.fs.JarFileDataProvider;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.enums.ERepositorySegmentType;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.repository.Segment;


public class FSRepositoryKernelLookup implements IKernelLookup {

    private File kernelDir;

    FSRepositoryKernelLookup(Segment<AdsModule> segment) {
        this(new File(segment.getDirectory().getParentFile(), ERepositorySegmentType.KERNEL.getValue()));
    }

    public FSRepositoryKernelLookup(File kernelDir) {
        this.kernelDir = kernelDir;
    }

    private File getDirectory() {
        return kernelDir;
    }

    @Override
    public IJarDataProvider[] listJars(String moduleName, ERuntimeEnvironmentType env) {
        File moduleDir = new File(getDirectory(), moduleName);
        if (moduleDir.isDirectory()) {
            final ArrayList<IJarDataProvider> providers = new ArrayList<>();
            acceptBinariesDir(new File(moduleDir, "bin"), providers);
            acceptBinariesDir(new File(moduleDir, "lib"), providers);
            return providers.toArray(new IJarDataProvider[providers.size()]);
        } else {
            return new IJarDataProvider[0];
        }
    }

    public void acceptBinariesDir(File dir, List<IJarDataProvider> jars) {
        if (dir.isDirectory()) {
            final File[] files = dir.listFiles();
            if (files != null) {
                for (final File file : files) {
                    if (file.isDirectory()) {
                        acceptBinariesDir(file, jars);
                    } else {
                        if (file.getName().endsWith(CompilerConstants.SUFFIX_STRING_JAR)) {
                            jars.add(JarFileDataProvider.getInstance(file));
                        }
                    }
                }
            }
        }
    }

    @Override
    public IJarDataProvider[] listAllJars(ERuntimeEnvironmentType env) {
        final String[] modules = listModules(env);
        final ArrayList<IJarDataProvider> pls = new ArrayList<>();
        for (final String m : modules) {
            final IJarDataProvider ps[] = listJars(m, env);
            if (ps.length > 0) {
                for (final IJarDataProvider p : ps) {
                    pls.add(p);
                }
            }
        }
        return pls.toArray(new IJarDataProvider[pls.size()]);
    }

    @Override
    public String[] listModules(ERuntimeEnvironmentType env) {
        switch (env) {
            case COMMON:
                return new String[]{"common", "starter"};
            case SERVER:
                return new String[]{"common", "server"};
            case EXPLORER:
                return new String[]{"common", "explorer"};
            case WEB:
                return new String[]{"common", "web"};
            case COMMON_CLIENT:
                return new String[]{"common"};
            default:
                return new String[0];
        }
    }
}
