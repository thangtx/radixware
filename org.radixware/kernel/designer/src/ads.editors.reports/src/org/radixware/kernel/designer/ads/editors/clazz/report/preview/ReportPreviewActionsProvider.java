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
package org.radixware.kernel.designer.ads.editors.clazz.report.preview;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.radixware.kernel.common.builder.BuildActionExecutor;
import org.radixware.kernel.common.builder.api.IBuildEnvironment;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.build.BuildOptions;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.userreport.common.UserExtensionManagerCommon;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.designer.ads.common.reports.ReportPreviewSettings;
import org.radixware.kernel.designer.common.dialogs.build.DesignerBuildEnvironment;

public class ReportPreviewActionsProvider {

    public static final String PREVIEW_ACTION_NAME = "Preview (CTRL+P)";
    public static final String COMPILE_AND_PREVIEW_ACTION_NAME = "Compile and preview (CTRL+SHIFT+P)";

    public static AbstractAction getPreviewAction(final AdsReportClassDef report) {
        return new AbstractAction(PREVIEW_ACTION_NAME) {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isActionAllowed(report.getId())) {
                    return;
                }
                new ShowReportPreviewActionListener(report).actionPerformed(e);
            }
        };
    }

    public static AbstractAction getCompileAndPreviewAction(final AdsReportClassDef report) {
        return new AbstractAction(COMPILE_AND_PREVIEW_ACTION_NAME) {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isActionAllowed(report.getId())) {
                    return;
                }
                IBuildEnvironment env = new DesignerBuildEnvironment(BuildActionExecutor.EBuildActionType.COMPILE_SINGLE);
                BuildActionExecutor executor = new BuildActionExecutor(env);
                RadixObject[] context = {report};
                
                BuildOptions result = executor.execute(context, env.getActionType(), null, true, BuildOptions.UserModeHandlerLookup.getUserModeHandler() != null ? UserExtensionManagerCommon.getInstance() : null);                
                if (result != null) {
                    new ShowReportPreviewActionListener(report).actionPerformed(e);
                }
            }
        };
    }

    private static boolean isActionAllowed(Id reportId) {
        boolean isAasAddressEmpty = Utils.emptyOrNull(ReportPreviewSettings.AAS_ADDRESS.get(reportId));
        boolean isTestDataFolderPathEmpty = Utils.emptyOrNull(ReportPreviewSettings.TEST_DATA_FOLDER_PATH.get(reportId));
        boolean isUserMode = BuildOptions.UserModeHandlerLookup.getUserModeHandler() != null;

        return (!isAasAddressEmpty || isUserMode) && !isTestDataFolderPathEmpty;
    }
}
