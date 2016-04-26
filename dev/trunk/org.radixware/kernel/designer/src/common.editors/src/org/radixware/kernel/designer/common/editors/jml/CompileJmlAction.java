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
package org.radixware.kernel.designer.common.editors.jml;

import java.awt.event.ActionEvent;
import java.util.EnumSet;
import javax.swing.AbstractAction;
import org.openide.util.RequestProcessor;
import org.radixware.kernel.common.builder.BuildActionExecutor;
import org.radixware.kernel.common.builder.BuildActionExecutor.EBuildActionType;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.build.BuildOptions;
import org.radixware.kernel.common.defs.ads.build.BuildOptions.UserModeHandlerLookup;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsUserReportClassDef;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.jml.Jml;
import org.radixware.kernel.designer.common.dialogs.build.DesignerBuildEnvironment;

public class CompileJmlAction extends AbstractAction {

    public static final String COMPILE_JML_ACTION = "compile-scml-action";
    private final JmlEditor editor;

    public CompileJmlAction(final JmlEditor editor) {
        super(COMPILE_JML_ACTION);
        this.editor = editor;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        final Jml jml = editor.getSource();
        RequestProcessor.getDefault().post(new Runnable() {

            @Override
            public void run() {
                RadixObject buildTarget = getBuildTarget(jml);
                if (buildTarget == null) {
                    return;
                }
                BuildOptions.UserModeHandler userModeHandler = findUserModeHandler(buildTarget);
                if (buildTarget instanceof AdsUserReportClassDef && userModeHandler == null) {
                    return;
                }
                try {
                    if (userModeHandler != null) {
                        userModeHandler.startBuild();
                    }

                    new BuildActionExecutor(new AdaptedEnvironment(BuildActionExecutor.EBuildActionType.COMPILE_SINGLE, EnumSet.allOf(ERuntimeEnvironmentType.class))).execute(buildTarget, userModeHandler);
                } finally {
                    if (userModeHandler != null) {
                        userModeHandler.finishBuild();
                    }
                }
            }
        });

    }

    private BuildOptions.UserModeHandler findUserModeHandler(RadixObject buildTarget) {
        return UserModeHandlerLookup.getUserModeHandler();
    }

    private RadixObject getBuildTarget(final Jml jml) {
        RadixObject target = jml.getOwnerDefinition();
        while (target != null && !(target instanceof AdsClassDef)) {
            target = target.getOwnerDefinition();
        }
        if (target == null) {
            target = jml.getOwnerDefinition();
        }
        if (target instanceof AdsReportModelClassDef) {
            AdsReportClassDef report = ((AdsReportModelClassDef) target).getOwnerClass();
            if (report instanceof AdsUserReportClassDef) {
                return report;
            }
        }
        return target;
    }

    private static class AdaptedEnvironment extends DesignerBuildEnvironment {

        private final BuildOptions buildOptions = BuildOptions.Factory.newInstance();

        public AdaptedEnvironment(final EBuildActionType actionType, final EnumSet<ERuntimeEnvironmentType> environmet) {
            super(actionType);
            buildOptions.setEnvironment(environmet);
        }

        @Override
        public BuildOptions getBuildOptions() {
            return buildOptions;
        }
    }
}
