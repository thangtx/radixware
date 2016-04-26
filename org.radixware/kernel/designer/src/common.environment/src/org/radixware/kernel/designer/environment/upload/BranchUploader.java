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
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.fs.FSRepositoryLayer;
import org.radixware.kernel.common.repository.fs.IRepositoryLayer;


class BranchUploader extends RadixObjectUploader<Branch> {

    public BranchUploader(Branch branch) {
        super(branch);
    }

    public Branch getBranch() {
        return getRadixObject();
    }

    @Override
    public void close() {
        final Branch branch = getBranch();
        AbstractRadixObjectUploaderUI.getDefault().closeProject(branch);
    }

    @Override
    public void reload() throws IOException {
        getBranch().reloadDescription();
    }

    @Override
    public long getRememberedFileTime() {
        return getBranch().getFileLastModifiedTime();
    }

    @Override
    public Layer uploadChild(File layerPrimaryFile) throws IOException {
        final File layerDir = layerPrimaryFile.getParentFile();
        final FSRepositoryLayer layerRepository = new FSRepositoryLayer(layerDir);
        final Layer layer = getBranch().getLayers().addFromRepository(layerRepository);
        return layer;
    }

    private void loadChildren() {
        final Branch branch = getBranch();
        IRepositoryLayer[] layerRepositories = branch.getRepository().listLayers();
        if (layerRepositories != null) {
            for (IRepositoryLayer layerRepository : layerRepositories) {
                if (branch.getLayers().findByURI(layerRepository.getName()) == null) {
                    final File layerFile = layerRepository.getDescriptionFile();
                    tryToUploadChild(layerFile, Layer.LAYER_TYPE_TITLE);
                }
            }
        }
    }

    @Override
    public void updateChildren() {
        for (Layer layer : getBranch().getLayers()) {
            LayerUploader layerUpdater = new LayerUploader(layer);
            layerUpdater.update();
        }
        loadChildren();
    }
}
