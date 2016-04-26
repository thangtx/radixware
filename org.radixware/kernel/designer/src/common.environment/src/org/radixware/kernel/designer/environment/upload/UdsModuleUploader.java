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
import org.openide.filesystems.FileUtil;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.module.ModuleDefinitions;
//import org.radixware.kernel.common.defs.uds.module.ModuleDefinitions;
import org.radixware.kernel.common.defs.uds.UdsDefinition;
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
    public AdsDefinition uploadChild(File file) throws IOException {
        FSRepositoryUdsDefinition rep = new FSRepositoryUdsDefinition(file);
        final AdsDefinition def = getModule().getDefinitions().addFromRepository(rep);
        return def;
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
                        final Id id = UdsDefinition.fileName2DefinitionId(defFile);
                        if (id != null && module.getDefinitions().findById(id) == null) {
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

        final ModuleDefinitions definitions = getModule().getDefinitionsIfLoaded();
        if (definitions != null) {
            for (AdsDefinition definition : definitions) {
                final UdsDefinitionUploader definitionUpdater = new UdsDefinitionUploader(definition);
                definitionUpdater.update();
            }
            loadNewDefinitions();
        }
        FileUtil.refreshFor(getModule().getEtcDir());


    }
}
