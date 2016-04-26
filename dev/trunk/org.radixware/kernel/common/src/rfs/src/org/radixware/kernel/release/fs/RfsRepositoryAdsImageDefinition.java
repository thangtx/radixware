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
import org.radixware.kernel.common.defs.ads.module.AdsImageDef;
import org.radixware.kernel.common.enums.EAccess;
import org.radixware.kernel.common.repository.ads.fs.IRepositoryAdsDefinition;
import org.radixware.kernel.common.repository.ads.fs.IRepositoryAdsImageDefinition;


class RfsRepositoryAdsImageDefinition extends RfsRepositoryAdsDefinition implements IRepositoryAdsImageDefinition {

//    public RfsRepositoryAdsImageDefinition(ReleaseRepository release, Id id, String moduleDir, ESaveMode uploadMode, ESystemComponent env) {
//        super(release, id, moduleDir, uploadMode, "Image", EDefType.IMAGE, env);
//    }
    public RfsRepositoryAdsImageDefinition(AdsImageDef image, ReleaseRepository release, String moduleDir) {
        super(image, release, moduleDir);
    }

    @Override
    public InputStream getImageData() throws IOException {
        return release.getRepositoryImageStream(moduleDir, getId());
    }

    @Override
    public IRepositoryAdsDefinition getMlsRepository() {
        return null;
    }
    

    @Override
    public File getImageFile() {
        return null;
    }

    @Override
    public String getImageFileName() {
        return getId().toString();
    }

    @Override
    public String getPath() {
        return moduleDir + "img/" + getId().toString() + ".xml";
    }

    @Override
    public boolean isPublished() {
        return true;
    }
    
    @Override
    public EAccess getAccessMode() {
        return null;
    }
}
