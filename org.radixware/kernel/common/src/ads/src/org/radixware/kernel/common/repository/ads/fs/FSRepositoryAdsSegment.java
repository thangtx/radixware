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

package org.radixware.kernel.common.repository.ads.fs;

import java.io.File;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.enums.ERepositorySegmentType;
import org.radixware.kernel.common.repository.Segment;
import org.radixware.kernel.common.repository.fs.FSRepositorySegment;
import org.radixware.kernel.common.repository.fs.IRepositoryModule;


public class FSRepositoryAdsSegment extends FSRepositorySegment<AdsModule> implements IRepositoryAdsSegment {

    public FSRepositoryAdsSegment(Segment<AdsModule> segment) {
        super(segment);
    }

    @Override
    public IRepositoryModule<AdsModule> getModuleRepository(AdsModule module) {
        IRepositoryModule<AdsModule> cachedInstance = getCachedRepository(module);
        if (cachedInstance != null) {
            return cachedInstance;
        }
        return new FSRepositoryAdsModule(module);
    }

    protected IRepositoryModule<AdsModule> getCachedRepository(AdsModule module) {
        return super.getModuleRepository(module);
    }

    @Override
    public IRepositoryModule<AdsModule> getModuleRepository(File moduleDir) {
        return new FSRepositoryAdsModule(moduleDir);
    }

    @SuppressWarnings("unchecked")
    @Override
    public IRepositoryModule<AdsModule>[] listModules() {
        return super.listModules();
    }

    @Override
    public IKernelLookup getKernelLookup() {
        return new FSRepositoryKernelLookup(segment);
    }

    @Override
    public void processException(Throwable e) {
        e.printStackTrace();
    }

    @Override
    public ERepositorySegmentType getSegmentType() {
        return ERepositorySegmentType.ADS;
    }
}
