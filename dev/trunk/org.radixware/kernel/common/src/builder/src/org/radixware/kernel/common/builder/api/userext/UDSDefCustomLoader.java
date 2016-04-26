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

import java.io.InputStream;
import org.radixware.kernel.common.repository.ads.fs.IRepositoryAdsDefinition;
import org.radixware.kernel.common.repository.ads.fs.IRepositoryAdsImageDefinition;
import org.radixware.kernel.common.types.Id;

/**
 *
 * @author akrylov
 */
public interface UDSDefCustomLoader {

    //returns true if did any significant job
    boolean loadDefinitionToModuleContext(UDSAdsModule contextModule);

    IRepositoryAdsDefinition[] listDefinitions(UDSAdsModuleRepository module);

    Id[] listModuleIds();

    IRepositoryAdsImageDefinition[] listImages(UDSAdsModuleRepository module);

    InputStream getImageData(UDSAdsModuleRepository module, Id imageId);
        

}
