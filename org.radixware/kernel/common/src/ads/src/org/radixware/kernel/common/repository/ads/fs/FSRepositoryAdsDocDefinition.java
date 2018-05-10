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
import org.radixware.kernel.common.defs.ads.AdsDefinition.ESaveMode;
import org.radixware.kernel.common.defs.ads.doc.AdsDocDef;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.types.Id;

public class FSRepositoryAdsDocDefinition extends FSRepositoryAdsDefinition {

    public FSRepositoryAdsDocDefinition(AdsDocDef def) {
        super(def, false);
    }

    public FSRepositoryAdsDocDefinition(File file) {
        super(file);
    }

    @Override
    protected String getSrcDirShortName(ESaveMode saveMode) {
        return AdsModule.SOURCES_DIR_NAME + File.separator + AdsModule.DOCUMENTATION_DIR_NAME;
    }

    @Override
    public void processException(Throwable e) {
        e.printStackTrace();
    }

    @Override
    public IRepositoryAdsDefinition getMlsRepository() {
        if (bundleDef != null) {
            return new FSRepositoryAdsLocaleDefinition(bundleDef, bundleDef.wasLoadedFromAPI());
        }
        FSRepositoryAdsLocaleDefinition localeRepository = null;
        final File definitionFile = getFile();
        if (definitionFile != null) {
            final File docDir = definitionFile.getParentFile();
            final File srcDir = docDir.getParentFile();
            final File moduleDir = srcDir.getParentFile();
            File mlsDir = new File(moduleDir, AdsModule.LOCALE_DIR_NAME);

            Id mlbId = Id.Factory.loadFrom(EDefinitionIdPrefix.ADS_LOCALIZING_BUNDLE.getValue() + getId().toString());
            localeRepository = new FSRepositoryAdsLocaleDefinition(mlsDir, mlbId);
            if (localeRepository.getRepositories().isEmpty()) {
                File mlsFile = new File(docDir, EDefinitionIdPrefix.ADS_LOCALIZING_BUNDLE.getValue() + definitionFile.getName());
                if (mlsFile.exists()) {
                    localeRepository.add(new FSRepositoryAdsDefinition(mlsFile), null);
                }
            }
            FSRepositoryAdsLocalizedDef.checkLocalizingLayers(moduleDir, mlbId, localeRepository);
        }
        return localeRepository;
    }
}
