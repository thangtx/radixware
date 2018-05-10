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
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.repository.fs.IJarDataProvider;
import org.radixware.kernel.common.repository.fs.IRepositoryModule;
import org.radixware.kernel.common.types.Id;


public interface IRepositoryAdsModule extends IRepositoryModule<AdsModule>, IRepositoryAdsLocalizedDefinition {

    @Override
    IRepositoryAdsDefinition getDefinitionRepository(Definition def);

    /**
     * Returns definitions list
     */
    IRepositoryAdsDefinition[] listDefinitions();

    IRepositoryAdsImageDefinition[] listImages();

    IRepositoryAdsDefinition[] listStrings();

    File getImagesDirectory();

    File getJavaSrcDirContainer();

    File getBinariesDirContainer();

    IJarDataProvider getJarFile(String pathInModule) throws IOException;

    boolean containsDefinition(Id id);

    List<Id> getDefinitionIdsByIdPrefix(EDefinitionIdPrefix prefix);

    void close();

    InputStream getUsagesXmlInputStream() throws IOException;
}
