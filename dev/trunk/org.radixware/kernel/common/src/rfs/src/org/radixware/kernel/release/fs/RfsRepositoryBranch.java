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
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.fs.IRepositoryBranch;
import org.radixware.kernel.common.repository.fs.IRepositoryLayer;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.starter.meta.LayerMeta;
import org.radixware.kernel.starter.radixloader.RadixLoader;


class RfsRepositoryBranch implements IRepositoryBranch {

    private final ReleaseRepository release;
    private File temporaryStorage;

    public RfsRepositoryBranch(ReleaseRepository release) throws IOException {
        this.release = release;
    }

    @Override
    public File getDescriptionFile() {
        return null;
    }

    @Override
    public File getDirectory() {
        return null;
    }

    @Override
    public String getName() {
        return "trunk";
    }

    @Override
    public InputStream getDescriptionData() throws IOException {
        return null;
    }
    
    private volatile List<RfsRepositoryLayer> layerRepos = null;

    @Override
    public IRepositoryLayer[] listLayers() {
        synchronized (this) {
            if (layerRepos == null) {
                layerRepos = new LinkedList<>();
                final List<LayerMeta> layers = release.getRevisionMeta().getAllLayersSortedFromBottom();

                for (LayerMeta layerMeta : layers) {
                    layerRepos.add(new RfsRepositoryLayer(this, layerMeta));
                }
            }
            return layerRepos.toArray(new IRepositoryLayer[layerRepos.size()]);
        }
    }

    @Override
    public IRepositoryLayer getLayerRepository(Layer layer) {
        if (layerRepos == null) {
            synchronized (this) {
                if (layerRepos == null) {
                    listLayers();
                }
            }
        }
        
        for (RfsRepositoryLayer layerRep : layerRepos) {
                if (layerRep.layerMeta.getUri().equals(layer.getURI())) {
                    return layerRep;
                }
        }
        return null;
    }

    @Override
    public void lock() throws IOException {
    }

    @Override
    public boolean lock(int timeout) throws IOException {

        return true;
    }

    @Override
    public void unlock() throws IOException {
    }

    File getTemporaryStorage() {
        synchronized (this) {
            if (temporaryStorage == null) {
                File temporaryFile = RadixLoader.getInstance().createTempFile("branch");
                temporaryFile.delete();
                temporaryStorage = temporaryFile;
                temporaryStorage.mkdirs();
            }
            return temporaryStorage;
        }
    }

    @Override
    public void close() {
        FileUtils.deleteDirectory(temporaryStorage);
    }

    ReleaseRepository getRelease() {
        return release;
    }

    @Override
    public void processException(Throwable e) {
        getRelease().processException(e);
    }

    @Override
    public boolean isBaseDevUriMeaningful() {
        return false;
    }
}
