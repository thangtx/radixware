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

package org.radixware.kernel.reporteditor.repository;

import java.io.IOException;
import org.openide.cookies.SaveCookie;
import org.radixware.kernel.common.userreport.repository.UserReport;


public class UserReportSaveCookie implements SaveCookie {

    private final UserReport report;

    public UserReportSaveCookie(final UserReport report) {
        this.report = report;
    }

    @Override
    public void save() throws IOException {
        report.save();
    }
}
