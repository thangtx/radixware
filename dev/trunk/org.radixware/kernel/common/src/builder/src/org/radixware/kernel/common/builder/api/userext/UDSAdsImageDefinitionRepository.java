/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.kernel.common.builder.api.userext;

import java.io.FileNotFoundException;
import java.io.InputStream;
import org.radixware.kernel.common.defs.ads.module.AdsImageDef;
import org.radixware.kernel.common.repository.ads.fs.FSRepositoryAdsImageDefinition;

/**
 *
 * @author akrylov
 */
public class UDSAdsImageDefinitionRepository extends FSRepositoryAdsImageDefinition {

    private UDSAdsModuleRepository module;
    private final String fileName;

    public UDSAdsImageDefinitionRepository(UDSAdsModuleRepository module, AdsImageDef def,String fileName) {
        super(def);
        this.module = module;
        this.fileName = fileName;
    }

    @Override
    public InputStream getImageData() throws FileNotFoundException {
        return module.loader.getImageData(module, getId());
    }

    @Override
    public String getImageFileName() {
        return fileName;
    }

}
