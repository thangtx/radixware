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
import java.io.InputStream;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.ERepositorySegmentType;
import org.radixware.kernel.common.repository.fs.IRepositoryModule;
import org.radixware.kernel.common.repository.uds.IRepositoryUdsSegment;
import org.radixware.kernel.starter.meta.DirectoryMeta;


class RfsRepositoryUdsSegment extends RfsRepositoryAdsSegment implements IRepositoryUdsSegment {

    RfsRepositoryUdsSegment(RfsRepositoryLayer layer, DirectoryMeta dir) {
        super(layer, dir, null, ERepositorySegmentType.UDS);
    }

    @Override
    protected IRepositoryModule<AdsModule> createModule(String moduleDirName) {
        throw new UnsupportedOperationException("Operation not supported");
    }

    @Override
    public ERepositorySegmentType getSegmentType() {
        return ERepositorySegmentType.UDS;
    }

    @Override
    public void processException(Throwable e) {
        getRelease().processException(e);
    }

    @Override
    public IRepositoryModule<AdsModule>[] listModules() {
        return new IRepositoryModule[0];
    }

    @Override
    public InputStream getLayerDictionaryData(EIsoLanguage language) {
        return null;
    }

    @Override
    public File getLayerDictionaryFile(EIsoLanguage language) {
        return null;
    }
}
