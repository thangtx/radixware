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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.radixware.kernel.common.defs.Module;
import static org.radixware.kernel.common.enums.ERepositorySegmentType.UDS;
import org.radixware.kernel.common.repository.Segment;
import org.radixware.kernel.common.repository.fs.IRepositoryLayer;
import org.radixware.kernel.common.repository.fs.IRepositorySegment;
import org.radixware.kernel.starter.meta.DirectoryMeta;
import org.radixware.kernel.starter.meta.LayerMeta;


class RfsRepositoryLayer implements IRepositoryLayer {

    final LayerMeta layerMeta;
    private final RfsRepositoryBranch branch;

    RfsRepositoryLayer(RfsRepositoryBranch branch, LayerMeta layerMeta) {
        this.layerMeta = layerMeta;
        this.branch = branch;
    }

    @Override
    public File getDescriptionFile() {
        return null;
    }

    @Override
    public InputStream getDescriptionData() throws IOException {
        return layerMeta.getXmlInputStream();
    }

    @Override
    public File getDirectory() {
        return null;
    }

    @Override
    public String getPath() {
        return layerMeta.getUri();
    }

    @Override
    public String getName() {
        return layerMeta.getUri();
    }
    private RfsRepositoryAdsSegment ads = null;
    private RfsRepositoryDdsSegment dds = null;
    private RfsRepositoryUdsSegment uds = null;
    private RfsRepositoryKernelSegment kernel = null;
    private boolean noAds = false;
    private boolean noDds = false;
    private boolean noUds = false;
    private boolean noKernel = false;

    @Override
    public <T extends Module> IRepositorySegment<T> getSegmentRepository(Segment<T> segment) {
        switch (segment.getType()) {
            case DDS:
                if (dds == null) {
                    if (noDds) {
                        return null;
                    }
                    for (DirectoryMeta seg : layerMeta.getDirectories()) {
                        if (seg.getDirectory().endsWith("/dds")) {
                            dds = new RfsRepositoryDdsSegment(this, seg);
                            break;
                        }
                    }
                    if (dds == null) {
                        noDds = true;
                    }
                }
                return (IRepositorySegment<T>) dds;
            case ADS:
                if (ads == null) {
                    if (noAds) {
                        return null;
                    }
                    DirectoryMeta adsDir = null;
                    DirectoryMeta kernelDir = null;
                    for (DirectoryMeta seg : layerMeta.getDirectories()) {
                        if (seg.getDirectory().endsWith("/ads")) {
                            adsDir = seg;
                            if (kernelDir != null) {
                                break;
                            }
                        } else if (seg.getDirectory().endsWith("/kernel")) {
                            kernelDir = seg;
                            if (adsDir != null) {
                                break;
                            }
                        }
                    }
                    if (adsDir != null) {
                        ads = new RfsRepositoryAdsSegment(this, adsDir, kernelDir);
                    }
                    if (ads == null) {
                        noAds = true;
                    }
                }
                return (IRepositorySegment<T>) ads;
            case UDS:
                if (uds == null) {
                    if (noUds) {
                        return null;
                    }
                    DirectoryMeta udsDir = null;
                    for (DirectoryMeta seg : layerMeta.getDirectories()) {
                        if (seg.getDirectory().endsWith("/uds")) {
                            udsDir = seg;
                        }
                    }
                    if (udsDir != null) {
                        uds = new RfsRepositoryUdsSegment(this, udsDir);
                    }
                    if (uds == null) {
                        noUds = true;
                    }
                }
                return (IRepositorySegment<T>) uds;
            case KERNEL:
                if (kernel == null) {
                    if (noKernel) {
                        return null;
                    }
                    DirectoryMeta kernelDir = null;
                    for (DirectoryMeta seg : layerMeta.getDirectories()) {
                        if (seg.getDirectory().endsWith("/kernel")) {
                            kernelDir = seg;
                        }
                    }
                    if (kernelDir != null) {
                        kernel = new RfsRepositoryKernelSegment(this, kernelDir);
                    }
                    if (kernel == null) {
                        noKernel = true;
                    }
                }
                return (IRepositorySegment<T>) kernel;
            default:
                return null;
        }
    }

    File getTemporaryStorage() {
        return new File(branch.getTemporaryStorage(), layerMeta.getUri());
    }

    ReleaseRepository getRelease() {
        return branch.getRelease();
    }

    LayerMeta getMeta() {
        return layerMeta;
    }

    @Override
    public void processException(Throwable e) {
        getRelease().processException(e);
    }

    @Override
    public boolean isReadOnly() {
        return true;
    }

    @Override
    public void close() {
    }

    @Override
    public boolean isKernelSupported() {
        return false;
    }
}
