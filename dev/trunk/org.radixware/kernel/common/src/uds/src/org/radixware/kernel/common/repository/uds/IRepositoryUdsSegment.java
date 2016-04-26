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

/*
 * 12/7/11 9:35 AM
 */
package org.radixware.kernel.common.repository.uds;

import java.io.File;
import java.io.InputStream;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.repository.ads.fs.IRepositoryAdsSegment;


public interface IRepositoryUdsSegment extends IRepositoryAdsSegment {

    InputStream getLayerDictionaryData(EIsoLanguage language);

    File getLayerDictionaryFile(EIsoLanguage language);
}
