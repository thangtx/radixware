/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.kernel.common.builder.api.userext;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.Segment;
import org.radixware.kernel.common.repository.fs.FSRepositoryLayer;
import org.radixware.kernel.common.repository.fs.IRepositorySegment;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.common.utils.XmlUtils;
import org.radixware.schemas.product.LayerDocument;

/**
 *
 * @author akrylov
 */
public class UDSLayerRepository extends FSRepositoryLayer {

    private UDSAdsSegmentRepository ads;
    private UDSDdsSegmentRepository dds;
    private final Branch branch;
    private final Layer layerObj;
    final UDSDefCustomLoader loader;

    private static File prepareLayerRepository(File projectDir, File[] rootFile, Branch branch, String name, String uri) throws IOException {
        File repositoryTmpDir = new File(projectDir, "layers.root");
        if (!repositoryTmpDir.isDirectory() && repositoryTmpDir.exists()) {
            FileUtils.deleteFile(repositoryTmpDir);
        }
        repositoryTmpDir.mkdirs();
        if (repositoryTmpDir.isDirectory()) {
            File layerDir = new File(repositoryTmpDir, uri);
            layerDir.mkdirs();
            LayerDocument xDoc = generateLayerDescription(branch, name, uri);
            try (final FileOutputStream out = new FileOutputStream(new File(layerDir, Layer.LAYER_XML_FILE_NAME))) {
                XmlUtils.saveXmlPretty(xDoc, out);
            }
            return layerDir;
        } else {
            throw new IOException("Unable to create directory for User Extensions layer");
        }
    }

    public UDSLayerRepository(File projectDir, File[] rootFile, Branch branch, String name, String uri, final UDSDefCustomLoader loader) throws IOException {
        super(prepareLayerRepository(projectDir, rootFile, branch, name, uri));
        this.repositoryTmpDir = rootFile[0];
        this.branch = branch;
        this.loader = loader;
        this.layerObj = new UDSLayer(this, loader);
        this.branch.getLayers().add(this.layerObj);
    }

    @Override
    public <T extends Module> IRepositorySegment<T> getSegmentRepository(Segment<T> segment) {
        switch (segment.getType()) {
            case ADS:
                synchronized (this) {
                    if (ads == null) {
                        ads = new UDSAdsSegmentRepository(this, (UDSAdsSegment) segment);
                    }
                    return (IRepositorySegment) ads;
                }
            case DDS:
                synchronized (this) {
                    if (dds == null) {
                        dds = new UDSDdsSegmentRepository(this);
                    }
                    return (IRepositorySegment) dds;
                }
            default:
                return super.getSegmentRepository(segment);
        }
    }

    @Override
    public Branch getBranch() {
        return branch;
    }

    @Override
    public File getDescriptionFile() {
        return new File(getDirectory(), Layer.LAYER_XML_FILE_NAME);
    }

    public Layer getLayer() {
        return layerObj;
    }

    private static LayerDocument generateLayerDescription(Branch branch, String name, String uri) {
        LayerDocument xDoc = LayerDocument.Factory.newInstance();
        org.radixware.schemas.product.Layer xLayer = xDoc.addNewLayer();
        xLayer.setName(name);
        xLayer.setUri(uri);
        List<String> baseUris = new LinkedList<>();
        for (final Layer l : branch.getLayers().findTop()) {
            baseUris.add(l.getURI());

        }
        if (!baseUris.isEmpty()) {
            xLayer.setBaseLayerURIs(baseUris);
        }
        return xDoc;
    }
    private File repositoryTmpDir;
    private final Object fileOperLock = new Object();

    @Override
    public boolean isReadOnly() {
        return false;
    }

    @Override
    public void close() {
        super.close();
        this.branch.getLayers().remove(layerObj);
        cleanup();
    }

    public void cleanup() {
        synchronized (fileOperLock) {
            if (repositoryTmpDir != null) {
                FileUtils.deleteDirectory(repositoryTmpDir);
            }
        }
    }
}
