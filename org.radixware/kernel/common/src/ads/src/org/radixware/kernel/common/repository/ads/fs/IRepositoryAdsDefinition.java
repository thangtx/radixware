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
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.enums.EAccess;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.repository.fs.IRepositoryDefinition;
import org.radixware.kernel.common.types.Id;


public interface IRepositoryAdsDefinition extends IRepositoryDefinition<AdsDefinition> {

    @Override
    Id getId();

    AdsDefinition getPreLoadedDefinition();

    File getFile(AdsDefinition.ESaveMode saveMode);

    String getName();

    EDefType getType();

    AdsDefinition.ESaveMode getUploadMode();

    IRepositoryAdsDefinition getMlsRepository();

    ERuntimeEnvironmentType getEnvironment();

    boolean isPublished();

    EAccess getAccessMode();

    boolean willLoadFromAPI();
}
