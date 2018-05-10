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

package org.radixware.kernel.designer.environment.upload;

import java.io.File;
import java.io.IOException;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
//import org.radixware.kernel.common.defs.uds.module.ModuleDefinitions;
import org.radixware.kernel.common.defs.uds.UdsDefinition;
import org.radixware.kernel.common.defs.uds.module.UdsFiles;
import org.radixware.kernel.common.defs.uds.module.UdsModule;
import org.radixware.kernel.common.repository.fs.IRepositoryDefinition;
import org.radixware.kernel.common.repository.uds.IRepositoryUdsModule;
import org.radixware.kernel.common.repository.uds.fs.FSRepositoryUdsDefinition;
import org.radixware.kernel.common.types.Id;


class UdsModuleUploader extends ModuleUploader<UdsModule> {

    public UdsModuleUploader(UdsModule module) {
        super(module);
    }

    @Override
    public RadixObject uploadChild(File file) throws IOException {
        UdsModule module = getModule();
        FSRepositoryUdsDefinition rep = new FSRepositoryUdsDefinition(file);
        if (file.getAbsolutePath().contains(module.getEtcDir().getAbsolutePath())){
           RadixObject radixObject = module.getUdsFiles().addFromRepository(rep);
           return radixObject;
        } else {
            final AdsDefinition def = module.getDefinitions().addFromRepository(rep);
            return def;
        }
    }
    
    private void loadNewFiles() {
        UdsModule module = getModule();
        IRepositoryUdsModule rep = module.getRepository();
        if (rep != null) {
            IRepositoryDefinition[] list = rep.getListFiles();
            for (IRepositoryDefinition roRepository : list) {
                File defFile = roRepository.getFile();
                if (defFile != null) {
                    RadixObject radixObject = module.getUdsFiles().getRadixObjectByFileName(defFile.getName());
                    
                    if (radixObject == null) {
                        tryToUploadChild(defFile, "File");
                    }
                }
            }
        }
    }

    private void loadNewDefinitions() {
        UdsModule module = getModule();
        IRepositoryUdsModule rep = module.getRepository();
        if (rep != null) {
            IRepositoryDefinition[] defs = rep.listDefinitions();
            if (defs != null) {
                for (IRepositoryDefinition def : defs) {
                    File defFile = def.getFile();
                    if (defFile != null) {
                        Id id = null;
                        try{
                            id = UdsDefinition.fileName2DefinitionId(defFile);
                        }
                        catch (Exception ex){
                            throw new RuntimeException("Unable load file \'" + defFile.getAbsolutePath() + "\'", ex);
                        }
                        if (id != null && module.getTopContainer().findById(id) == null) {
                            tryToUploadChild(defFile, Definition.DEFINITION_TYPE_TITLE);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void updateChildren() {
        if (!getModule().isAutoAploadAllowed()) {
            return;
        }

        final UdsFiles udsFiles = getModule().getUdsFilesIfLoaded();
        if (udsFiles != null){
            UdsUploaderUtils.updateDirChildren(udsFiles);
            loadNewFiles();
        }
        

    }
}
