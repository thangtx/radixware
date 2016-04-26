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
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsDefinition.ESaveMode;
import org.radixware.kernel.common.defs.ads.localization.AdsLocalizingBundleDef;
import org.radixware.kernel.common.enums.EAccess;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.repository.ads.fs.IRepositoryAdsDefinition;
import org.radixware.kernel.common.types.Id;


class RfsRepositoryAdsDefinition implements IRepositoryAdsDefinition {

    protected final String moduleDir;
    protected final ReleaseRepository release;
    final AdsDefinition pointer;
    private AdsLocalizingBundleDef bundle;
    private RfsRepositoryAdsDefinition nlsRepository = null;

    RfsRepositoryAdsDefinition(final AdsDefinition pointer, final ReleaseRepository release, final String moduleDir) {
        this.pointer = pointer;
        this.release = release;
        this.moduleDir = moduleDir;
    }

    void setBundle(final AdsLocalizingBundleDef bundle) {
        this.bundle = bundle;
    }

    @Override
    public boolean willLoadFromAPI() {
        return true;
    }

    @Override
    public IRepositoryAdsDefinition getMlsRepository() {
        synchronized (this) {
            if (bundle != null) {
                if (nlsRepository == null) {
                    nlsRepository = new RfsRepositoryAdsDefinition(bundle, release, moduleDir);
                }
                return nlsRepository;
            } else {
                return new RfsRepositoryAdsLocaleDefinition(pointer, release, moduleDir);
            }
        }
    }

    @Override
    public File getFile(final ESaveMode saveMode) {
        return null;
    }

    @Override
    public ESaveMode getUploadMode() {
        return ESaveMode.API;
    }

    @Override
    public File getFile() {
        return null;
    }

    @Override
    public InputStream getData() throws IOException {
        //return release.getRepositoryFileStream(getPath());
        throw new IllegalUsageError("Never use getData() method with RFS repository");
    }

    @Override
    public String getPath() {
        //return moduleDir + "meta/" + pointer.getId().toString() + ".xml";
        throw new IllegalUsageError("Never use getPath() method with RFS repository");
    }

    @Override
    public Id getId() {
        return pointer.getId();
    }

    @Override
    public String getName() {
        return pointer.getName();
    }

    @Override
    public EDefType getType() {
        return pointer.getDefinitionType();
    }

    @Override
    public ERuntimeEnvironmentType getEnvironment() {
        return pointer.getUsageEnvironment();
    }

    @Override
    public void processException(final Throwable e) {
        release.processException(e);
    }

    @Override
    public AdsDefinition getPreLoadedDefinition() {
        return pointer;
    }

    @Override
    public boolean isPublished() {
        return pointer.isPublished();
    }

    @Override
    public EAccess getAccessMode() {
        return pointer.getAccessMode();
    }
}
