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
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.dds.DdsModelDef;
import org.radixware.kernel.common.defs.dds.DdsModule;


class DdsModelUploader extends RadixObjectUploader<DdsModelDef> {

    public DdsModelUploader(DdsModelDef model) {
        super(model);
    }

    public DdsModelDef getModel() {
        return getRadixObject();
    }

    @Override
    public void close() {
        final DdsModelDef model = getModel();
        final DdsModule module = model.getModule();
        module.getModelManager().setModel(null);
    }

    @Override
    public long getRememberedFileTime() {
        return getModel().getFileLastModifiedTime();
    }

    @Override
    public void reload() throws IOException {
        final DdsModelDef model = getModel();
        final DdsModule module = model.getModule();
        module.getModelManager().reloadModels();
    }

    // ===========================================
    @Override
    public RadixObject uploadChild(File file) throws IOException {
        throw new IllegalStateException();
    }

    @Override
    public void updateChildren() {
        // NOTHING
    }
}
