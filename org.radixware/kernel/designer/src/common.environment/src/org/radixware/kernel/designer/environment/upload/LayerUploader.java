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
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.dds.DdsSegment;


class LayerUploader extends RadixObjectUploader<Layer> {

    public LayerUploader(Layer layer) {
        super(layer);
    }

    public Layer getLayer() {
        return getRadixObject();
    }

    @Override
    public void close() {
        Layer layer = getLayer();
        // do not call layer.delete(), because layer directory will be deleted.
        Branch branch = layer.getBranch();
        if (branch != null) {
            branch.getLayers().remove(layer);
        }
    }

    @Override
    public void reload() throws IOException {
        getLayer().reloadDescription();
    }

    @Override
    public long getRememberedFileTime() {
        return getLayer().getFileLastModifiedTime();
    }

    @Override
    public RadixObject uploadChild(File file) throws IOException {
        throw new IllegalStateException();
    }

    @Override
    public void updateChildren() {
        Layer layer = getLayer();

        SegmentUploader ddsSegmentUpdater = new DdsSegmentUploader((DdsSegment) layer.getDds());
        ddsSegmentUpdater.update();

        SegmentUploader adsSegmentUpdater = new SegmentUploader(layer.getAds());
        adsSegmentUpdater.update();

        SegmentUploader udsSegmentUpdater = new SegmentUploader(layer.getUds());
        udsSegmentUpdater.update();
    }
}
