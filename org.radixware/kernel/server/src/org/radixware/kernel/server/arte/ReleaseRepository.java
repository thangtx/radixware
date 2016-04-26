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

package org.radixware.kernel.server.arte;

import org.apache.commons.logging.LogFactory;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.starter.meta.RevisionMeta;


public class ReleaseRepository extends org.radixware.kernel.release.fs.ReleaseRepository {

    private final Release release;

    @SuppressWarnings("unchecked")
    public ReleaseRepository(final Release release) {
        this.release = release;
    }

    public AdsIndices getAdsIndices() {
        if (adsIndices == null) {
            adsIndices = new AdsIndices(release);
        }
        return adsIndices;
    }
    private AdsIndices adsIndices = null;

    @Override
    protected void close() {
        super.close();
        adsIndices = null;
    }

    @Override
    protected RevisionMeta getRevisionMeta() {
        return release.getRevisionMeta();
    }

    @Override
    public void processException(final Throwable e) {
        LogFactory.getLog(this.getClass()).error("Error on definition loading", e);
    }
}
