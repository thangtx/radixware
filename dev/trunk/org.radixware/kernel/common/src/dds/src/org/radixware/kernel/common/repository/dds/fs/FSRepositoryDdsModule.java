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

package org.radixware.kernel.common.repository.dds.fs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.dds.DdsModelManager;
import org.radixware.kernel.common.defs.dds.DdsModule;
import org.radixware.kernel.common.repository.fs.FSRepositoryModule;
import org.radixware.kernel.common.repository.fs.IRepositoryDefinition;


public class FSRepositoryDdsModule extends FSRepositoryModule<DdsModule> implements IRepositoryDdsModule {

    public FSRepositoryDdsModule(File moduleDir) {
        super(moduleDir);
    }

    public FSRepositoryDdsModule(DdsModule module) {
        super(module);
    }

    @Override
    public InputStream getFixedModelData() throws FileNotFoundException {
        return new FileInputStream(getFixedModelFile());
    }

    @Override
    public InputStream getModifiedModelData() throws FileNotFoundException {
        File file = getModifiedModelFile();
        if (file == null || !file.isFile()) {
            return null;
        } else {
            return new FileInputStream(file);
        }
    }

    @Override
    public File getFixedModelFile() {
        return new File(getDirectory(), DdsModelManager.FIXED_MODEL_XML_FILE_NAME);
    }

    @Override
    public File getModifiedModelFile() {
        return new File(getDirectory(), DdsModelManager.MODIFIED_MODEL_XML_FILE_NAME);
    }

    @Override
    public IRepositoryDefinition getDefinitionRepository(Definition def) {
        return null;
    }

    @Override
    public void processException(Throwable e) {
        e.printStackTrace();
    }
}
