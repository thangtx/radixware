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

package org.radixware.kernel.common.repository.fs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;
import java.util.WeakHashMap;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.Segment;


public class FSRepositoryLayer implements IRepositoryLayer {

    final Layer layer;
    private final File layerDir;

    FSRepositoryLayer(Layer layer) {
        this.layer = layer;
        this.layerDir = null;
    }

    public FSRepositoryLayer(File layerDir) {
        this.layer = null;
        this.layerDir = layerDir;
    }

    @Override
    public File getDescriptionFile() {
        return new File(getDirectory(), Layer.LAYER_XML_FILE_NAME);
    }

    @Override
    public File getDirectory() {
        if (layerDir != null) {
            return layerDir;
        } else {
            Branch branch = layer.getBranch();
            if (branch != null) {
                File branchDir = branch.getDirectory();
                if (branchDir != null) {
                    return new File(branchDir, layer.getURI());
                }
            }
            return null;
        }
    }

    @Override
    public String getPath() {
        return getDirectory().getPath();
    }

    @Override
    public String getName() {
        return getDirectory().getName();
    }

    @Override
    public InputStream getDescriptionData() throws FileNotFoundException {
        return new FileInputStream(getDescriptionFile());
    }
    private final Map<Segment, FSRepositorySegment> cache = new WeakHashMap<Segment, FSRepositorySegment>();

    @Override
    public <T extends Module> IRepositorySegment<T> getSegmentRepository(Segment<T> segment) {
        synchronized (cache) {
            IRepositorySegment<T> cached = cache.get(segment);
            if (cached != null) {
                return cached;
            }
        }
        return FSRepositorySegmentFactory.createInstance(segment);
    }

    protected void keepInCache(FSRepositorySegment segment, boolean keep) {
        synchronized (cache) {
            if (keep) {
                cache.put(segment.segment, segment);
            } else {
                cache.remove(segment.segment);
            }
            final Branch branch = getBranch();
            if (branch != null) {
                ((FSRepositoryBranch) branch.getRepository()).keepInCache(this, !cache.isEmpty());
            }
        }
    }

    protected Branch getBranch() {
        return layer == null ? null : layer.getBranch();
    }

    @Override
    public void processException(Throwable e) {
        e.printStackTrace();
    }

    @Override
    public boolean isReadOnly() {
        return false;
    }

    @Override
    public void close() {
    }

    @Override
    public boolean isKernelSupported() {
        return true;
    }
}
