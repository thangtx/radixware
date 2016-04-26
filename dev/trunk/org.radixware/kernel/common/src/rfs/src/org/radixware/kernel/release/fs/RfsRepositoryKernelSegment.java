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

import org.radixware.kernel.common.enums.ERepositorySegmentType;
import org.radixware.kernel.common.repository.fs.IRepositoryModule;
import org.radixware.kernel.common.repository.kernel.KernelModule;
import org.radixware.kernel.starter.meta.DirectoryMeta;


class RfsRepositoryKernelSegment extends RfsRepositorySegment<KernelModule> {

    RfsRepositoryKernelSegment(RfsRepositoryLayer layer, DirectoryMeta dir) {
        super(layer, dir, ERepositorySegmentType.DDS);
    }

    @Override
    protected IRepositoryModule<KernelModule> createModule(String moduleDirName) {
        return new RfsRepositoryKernelModule(this, moduleDirName);
    }

    @Override
    public void processException(Throwable e) {
        getRelease().processException(e);
    }

    @Override
    public ERepositorySegmentType getSegmentType() {
        return ERepositorySegmentType.KERNEL;
    }
}
