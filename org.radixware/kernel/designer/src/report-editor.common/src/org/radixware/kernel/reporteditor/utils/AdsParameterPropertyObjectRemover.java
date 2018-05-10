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

package org.radixware.kernel.reporteditor.utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.netbeans.api.progress.ProgressUtils;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProviderFactory;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsParameterPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsUserReportClassDef;
import org.radixware.kernel.common.userreport.repository.UserReport;
import org.radixware.kernel.designer.common.annotations.registrators.ObjectRemoverFactoryRegistration;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.general.nodes.operation.IObjectRemoverFactory;
import org.radixware.kernel.designer.common.general.nodes.operation.ObjectRemover;
import org.radixware.kernel.reporteditor.common.UserExtensionManager;
import org.radixware.schemas.reports.UserReportHeader;
import org.radixware.schemas.reports.UserReportListRsDocument;


public class AdsParameterPropertyObjectRemover extends ObjectRemover {

    private final AdsParameterPropertyDef prop;

    public AdsParameterPropertyObjectRemover(AdsParameterPropertyDef prop) {
        this.prop = prop;
    }

    @Override
    public void removeObject() {
        ProgressUtils.showProgressDialogAndRun(new Runnable() {

            @Override
            public void run() {
                UserReport owner = UserExtensionManager.getInstance().getUserReports().findReportById(prop.getOwnerDef().getId());
                if(owner == null) {
                    return;
                }
                UserReportListRsDocument xReports = UserExtensionManager.getInstance().getUFRequestExecutor().listReportsWithParam(owner, prop.getId());
                if(xReports == null) {
                    return;
                }
                
                if (!xReports.getUserReportListRs().getReportList().isEmpty()) {
                    List<UserReport> users = new LinkedList<>();
                    List<UserReport> reports = new LinkedList<>();
                    for(UserReportHeader xReport : xReports.getUserReportListRs().getReportList()) {
                        reports.add(UserExtensionManager.getInstance().getUserReports().findReportById(xReport.getId()));
                    }

                    for (UserReport r : reports) {
                        UserReport.ReportVersion v = r.getVersions().getCurrent();
                        AdsUserReportClassDef report = v.findReportDefinition();
                        final List<Definition> deps = new ArrayList<>();
                        if (report != null && !report.isParentOf(prop)) {
                            deps.clear();
                            report.visit(new IVisitor() {

                                @Override
                                public void accept(RadixObject radixObject) {
                                    radixObject.collectDirectDependences(deps);
                                }
                            }, VisitorProviderFactory.createDefaultVisitorProvider());


                            if (deps.contains(prop)) {
                                users.add(r);
                            }
                        }
                    }
                    if (!users.isEmpty()) {
                        StringBuilder message = new StringBuilder("Report parameter " + prop.getQualifiedName() + " is used by current versions of following reports:\n");
                        for (UserReport report : users) {
                            message.append("- ").append(report.getName()).append("\n");
                        }
                        message.append("Continue?");
                        if (!DialogUtils.messageConfirmation(message.toString())) {
                            return;
                        }

                    }
                }
                if (prop.isInBranch() && prop.canDelete()) {
                    prop.delete();
                }

            }
        }, "Delete Report Parameter");


    }

    @ObjectRemoverFactoryRegistration
    public static final class Factory implements IObjectRemoverFactory<AdsParameterPropertyDef> {

        @Override
        public ObjectRemover newInstance(AdsParameterPropertyDef obj) {
            return new AdsParameterPropertyObjectRemover(obj);
        }
    }
}
