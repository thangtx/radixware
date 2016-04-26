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

package org.radixware.kernel.common.repository.uds.fs;

import java.io.File;
import java.io.FileFilter;
import java.util.HashMap;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.uds.UdsDefinition;
import org.radixware.kernel.common.defs.uds.module.UdsModule;
import org.radixware.kernel.common.repository.ads.fs.FSRepositoryAdsModule;
import org.radixware.kernel.common.repository.ads.fs.IRepositoryAdsDefinition;
import org.radixware.kernel.common.repository.uds.IRepositoryUdsModule;


public class FSRepositoryUdsModule extends FSRepositoryAdsModule implements IRepositoryUdsModule {

    public FSRepositoryUdsModule(UdsModule module) {
        super(module);
    }

    public FSRepositoryUdsModule(File moduleDir) {
        super(moduleDir);
    }

    @Override
    public void processException(Throwable e) {
        e.printStackTrace();
    }
    
    

    private File[] listFiles(File dir) {
        return dir.listFiles(new FileFilter() {

            @Override
            public boolean accept(File pathname) {
                final String name = pathname.getName();
                if (name == null) {
                    return false;
                }
                if (name.endsWith(".xml")) {
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public IRepositoryAdsDefinition[] listDefinitions() {
        final File moduleDir = getDirectory();
        if (moduleDir == null) {
            return null; // module removed before definitions loaded
        }
        File srcDir = new File(moduleDir, AdsModule.SOURCES_DIR_NAME);

        if (srcDir.isDirectory()) {

            final HashMap<String, FSRepositoryUdsDefinition> loadedFiles = new HashMap<String, FSRepositoryUdsDefinition>();

            File[] definitionFiles = listFiles(srcDir);

            if (definitionFiles != null) {
                for (File file : definitionFiles) {
                    loadedFiles.put(file.getName(), new FSRepositoryUdsDefinition(file));
                }
            }
            return loadedFiles.values().toArray(new IRepositoryAdsDefinition[loadedFiles.size()]);
        } else {
            return new IRepositoryAdsDefinition[0];
        }
    }

    @Override
    public File getJavaSrcDirContainer() {
        return getDirectory();
    }

    @Override
    public File getBinariesDirContainer() {
        return getDirectory();
    }

    @Override
    public IRepositoryAdsDefinition getDefinitionRepository(Definition def) {
        if (def instanceof UdsDefinition) {
            return new FSRepositoryUdsDefinition((UdsDefinition) def);
        }
        return super.getDefinitionRepository(def);
    }
    
}
