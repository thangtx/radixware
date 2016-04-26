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

package org.radixware.kernel.common.defs.ads.clazz.sql.report;

import org.radixware.kernel.common.enums.EReportBandType;
import org.radixware.schemas.adsdef.ReportBand;


public class AdsReportGroupBand extends AdsReportBand {

    public AdsReportGroupBand() {
        super();
    }

    protected AdsReportGroupBand(ReportBand xBand) {
        super(xBand);
    }

    @Override
    public String getName() {
        final AdsReportGroup ownerGroup = getOwnerGroup();
        if (ownerGroup != null) {
            final String groupName = ownerGroup.getName();
            if (ownerGroup.getHeaderBand() == this) {
                return "'" + groupName + "' Header";
            } else if (ownerGroup.getFooterBand() == this) {
                return "'" + groupName + "' Footer";
            } else {
                return "'" + groupName + "' Band";
            }
        } else {
            return "Group Band";
        }
    }

    /**
     * @return owner group of null if band is not in group.
     */
    public AdsReportGroup getOwnerGroup() {
        return (AdsReportGroup) getContainer();
    }

    // overrided in AdsReportGroupBand
    @Override
    public EReportBandType getType() {
        final AdsReportGroup group = getOwnerGroup();
        if (group.getHeaderBand() == this) {
            return EReportBandType.GROUP_HEADER;
        } else if (group.getFooterBand() == this) {
            return EReportBandType.GROUP_FOOTER;
        } else {
            throw new IllegalStateException();
        }
    }
}
