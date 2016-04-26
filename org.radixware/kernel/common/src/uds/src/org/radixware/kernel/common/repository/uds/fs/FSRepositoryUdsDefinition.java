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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsDefinition.ESaveMode;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.uds.UdsDefinition;
import org.radixware.kernel.common.repository.ads.fs.FSRepositoryAdsDefinition;
import org.radixware.kernel.common.repository.fs.IRepositoryModule;
import org.radixware.kernel.common.types.Id;


public class FSRepositoryUdsDefinition extends FSRepositoryAdsDefinition {

    public FSRepositoryUdsDefinition(File file) {
        super(file);
    }

    public FSRepositoryUdsDefinition(UdsDefinition def) {
        super(def, false);
    }

    @Override
    public File getFile() {
        return getFileImpl();
    }

    @Override
    public ESaveMode getUploadMode() {
        return ESaveMode.NORMAL;
    }

    public File getFileImpl() {
        if (file != null) {
            return file;
        } else {
            AdsModule module = def.getModule();
            if (module == null) {
                return null;
            } else {
                IRepositoryModule repo = def.getModule().getRepository();
                if (repo == null) {
                    return null;
                } else {
                    File moduleDir = repo.getDirectory();
                    if (moduleDir == null) {
                        return null;
                    }
                    File srcDir = new File(moduleDir, AdsModule.SOURCES_DIR_NAME);
                    String fn = def instanceof UdsDefinition ? ((UdsDefinition) def).getFileName() : (def.getId().toString() + ".xml");
                    return new File(srcDir, fn);
                }
            }
        }
    }

    @Override
    public InputStream getData() throws FileNotFoundException {
        final File defFile = getFileImpl();
        if (defFile != null) {
            return new FileInputStream(defFile);
        } else {
            return null;
        }
    }

    @Override
    public String getPath() {
        File defFile = getFile();

        if (defFile != null) {
            return defFile.getAbsolutePath();
        } else {
            return "";
        }
    }

    @Override
    protected String getSrcDirShortName(ESaveMode saveMode) {
        return saveMode == AdsDefinition.ESaveMode.API ? AdsModule.STRIP_SOURCES_DIR_NAME : AdsModule.SOURCES_DIR_NAME;
    }

    @Override
    public Id getId() {
        if (def != null) {
            return def.getId();
        } else {
            return AdsDefinition.fileName2DefinitionId(file);
        }
    }

    @Override
    public void processException(Throwable e) {
        e.printStackTrace();
    }
}
