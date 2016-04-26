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

package org.radixware.kernel.designer.uds;

import java.io.IOException;
import java.io.OutputStream;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.common.defs.ads.localization.AdsLocalizingBundleDef;
import org.radixware.kernel.common.utils.XmlFormatter;
import org.radixware.schemas.adsdef.AdsUserReportExchangeDocument;
import org.radixware.schemas.adsdef.UserReportDefinitionType;
import org.radixware.schemas.adsdef.UserReportExchangeType;


public class UserReportUtils {

    public static void exportReport(AdsReportClassDef report, OutputStream out) throws IOException {
        AdsUserReportExchangeDocument xDoc = AdsUserReportExchangeDocument.Factory.newInstance();
        UserReportExchangeType xEx = xDoc.addNewAdsUserReportExchange();
        xEx.setName(report.getName());
        xEx.setDescription(report.getDescription());
        xEx.setId(report.getId());

        UserReportDefinitionType xDef = xEx.addNewAdsUserReportDefinition();

        report.appendTo(xDef.addNewReport(), AdsDefinition.ESaveMode.NORMAL);
        AdsLocalizingBundleDef sb = report.findExistingLocalizingBundle();
        if (sb != null) {
            sb.appendTo(xDef.addNewStrings(), AdsDefinition.ESaveMode.NORMAL);
        }

        XmlFormatter.save(xDoc, out);
    }
}
