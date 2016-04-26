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

package org.radixware.kernel.server.aio;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import org.radixware.kernel.common.sc.SapClientOptions;

public interface ServiceManifestLoader {

	public List<SapClientOptions> readSaps(Long systemId, final Long thisInstanceId,
			String serviceUri, String scpName, long maxCachedValAgeMillis);
        
        public URL getWsdlUrl(final SapClientOptions SapClientOptions) throws IOException;
        
}