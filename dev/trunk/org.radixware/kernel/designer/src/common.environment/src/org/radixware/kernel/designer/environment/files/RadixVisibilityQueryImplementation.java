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

package org.radixware.kernel.designer.environment.files;

import java.io.File;
import javax.swing.event.ChangeListener;
import org.netbeans.spi.queries.VisibilityQueryImplementation2;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.radixware.kernel.common.enums.ERepositorySegmentType;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;

/**
 * Filter for KERNEL segment.
 * Registered in META-INF.
 */
public class RadixVisibilityQueryImplementation implements VisibilityQueryImplementation2 {

    public RadixVisibilityQueryImplementation() {
    }

    static boolean isFileVisible(File file) {
        // ignore Kernel segment fully.
        for (File f = file; f != null; f = f.getParentFile()) {
            if (ERepositorySegmentType.KERNEL.getValue().equals(f.getName())) {
                File layerDir = f.getParentFile();
                if (layerDir != null && Layer.isLayerDir(layerDir)) {
                    File branchDir = layerDir.getParentFile();
                    if (branchDir != null && Branch.isBranchDir(branchDir)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public boolean isVisible(File file) {
        return isFileVisible(file);
    }

    @Override
    public boolean isVisible(FileObject fileObject) {
        final File file = FileUtil.toFile(fileObject);
        return isFileVisible(file);
    }

    @Override
    public void addChangeListener(ChangeListener l) {
        // NOTHING
    }

    @Override
    public void removeChangeListener(ChangeListener l) {
        // NOTHING
    }
}
