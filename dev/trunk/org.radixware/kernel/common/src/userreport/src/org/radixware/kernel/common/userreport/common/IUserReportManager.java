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

package org.radixware.kernel.common.userreport.common;

import java.util.List;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.userreport.repository.UserReport;
import org.radixware.kernel.common.userreport.repository.UserReport.ReportVersion;
import org.radixware.kernel.common.userreport.repository.UserReport.ReportVersions;


public interface IUserReportManager extends IUserReportModuleManager {

    void save(final AdsTypeDeclaration contextParamType, final UserReport report);

    boolean deleteReport(final List<String> deletedPubs, final Throwable[] exceptions, final Id reportId);

    void openEditor(final UserReport report);
    
    void closeEditor(final UserReport report);

    IUserDefChangeSuppert createVersionChangeSuppert(ReportVersion reportVersion);

    IUserDefChangeSuppert createVersionsChangeSuppert(ReportVersions reportVersions);
}
