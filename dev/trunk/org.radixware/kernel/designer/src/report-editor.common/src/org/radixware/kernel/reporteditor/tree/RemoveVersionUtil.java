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

package org.radixware.kernel.reporteditor.tree;

import javax.swing.SwingUtilities;
import org.netbeans.api.progress.ProgressUtils;
import org.radixware.kernel.common.userreport.repository.UserReport;
import org.radixware.kernel.common.userreport.repository.UserReport.ReportVersion;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;


public abstract class RemoveVersionUtil {

    private final UserReport.ReportVersion version;

    public RemoveVersionUtil(final ReportVersion version) {
        this.version = version;
    }

    public void remove() {
        if (version.isCurrent()) {
            DialogUtils.messageError(version.getName() + " is current version for report " + version.getOwnerReport().getName() + " and can not be removed");
            return;
        } else {
            if (version.getUserReport() != null) {
                if (DialogUtils.messageConfirmation("Delete version #" + version.getOrder() + " (" + version.getVersion() + ") of report " + version.getUserReport().getName() + "?")) {
                    ProgressUtils.showProgressDialogAndRun(new Runnable() {

                        @Override
                        public void run() {
                            version.getUserReport().getVersions().removeVersion(version);
                            SwingUtilities.invokeLater(new Runnable() {

                                @Override
                                public void run() {
                                    done();
                                }
                            });
                        }
                    }, "Delete Report Version");
                }
            }
        }
    }

    protected abstract void done();
}
