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

import java.io.IOException;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.repository.fs.IRepositorySegment;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.userreport.repository.ReportsModule;
import org.radixware.kernel.common.userreport.repository.UserReport;

public interface IUserReportModuleManager {

    ReportsModule addNewModule();

    boolean createReportModule(final IRepositorySegment segment, final ReportsModule module);

    void deleteModule(final Id reportId);

    boolean moveTo(final AdsModule module, final Id reportId) throws IOException;

    UserReport addNewReport(final ReportsModule reportModule);
}
