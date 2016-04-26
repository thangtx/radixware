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
import org.radixware.kernel.common.defs.dds.DdsModelDef;
import org.radixware.kernel.common.defs.dds.DdsModelManager;
import org.radixware.kernel.common.defs.dds.DdsModule;


class DdsModuleUploader extends ModuleUploader<DdsModule> {

    public DdsModuleUploader(DdsModule module) {
        super(module);
    }

    @Override
    public DdsModule uploadChild(File file) throws IOException {
        DdsModule module = getModule();
        module.getModelManager().reloadModels();
        return module;
    }

    @Override
    public void updateChildren() {
        DdsModule module = getModule();
        if (!module.getModelManager().isInitialized()) {
            return;
        }

        // modified, firstly - because it can contain local changes
        final DdsModelDef modifiedModel = module.getModelManager().getModifiedModelIfLoaded();
        if (modifiedModel != null) {
            final DdsModelUploader modifiedModelUpdater = new DdsModelUploader(modifiedModel);
            modifiedModelUpdater.update();
        } else {
            final File moduleDir = module.getDirectory();
            final File modifiedModelFile = new File(moduleDir, DdsModelManager.MODIFIED_MODEL_XML_FILE_NAME);
            if (modifiedModelFile.isFile()) {
                tryToUploadChild(modifiedModelFile, "Database model");
            }
        }

        // fixed
        final DdsModelDef fixedModel = module.getModelManager().getFixedModelIfLoaded();
        if (fixedModel != null) {
            final DdsModelUploader fixedModelUpdater = new DdsModelUploader(fixedModel);
            fixedModelUpdater.update();
        } else {
            final File moduleDir = module.getDirectory();
            final File fixedModelFile = new File(moduleDir, DdsModelManager.FIXED_MODEL_XML_FILE_NAME);
            if (fixedModelFile.isFile()) {
                tryToUploadChild(fixedModelFile, "Database model");
            }
        }
    }
}
