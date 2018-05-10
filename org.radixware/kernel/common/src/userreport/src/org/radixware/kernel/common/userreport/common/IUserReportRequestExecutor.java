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

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.types.Bin;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.userreport.repository.ReportsModule;
import org.radixware.kernel.common.userreport.repository.UserReport;
import org.radixware.schemas.adsdef.AdsUserReportDefinitionDocument;
import org.radixware.schemas.adsdef.AdsUserReportExchangeDocument;
import org.radixware.schemas.reports.UserReportListRsDocument;
import org.radixware.schemas.reports.UserReportVersionListRs;

public interface IUserReportRequestExecutor {

    class ModuleInfo {

        private final String moduleName;
        private final String moduleDescription;
        private final Id moduleId;

        public ModuleInfo(final Id moduleId, final String moduleName, final String moduleDescription) {
            this.moduleId = moduleId;
            this.moduleName = moduleName;
            this.moduleDescription = moduleDescription;
        }

        public String getModuleName() {
            return moduleName;
        }

        public String getModuleDescription() {
            return moduleDescription;
        }

        public Id getModuleId() {
            return moduleId;
        }
    }

    class ReportDataInfo {

        private final AdsUserReportDefinitionDocument xml;
        private final Id lastRuntimeId;

        public ReportDataInfo() {
            this(null, null);
        }

        public ReportDataInfo(final Id lastRuntimeId, final AdsUserReportDefinitionDocument xml) {
            this.lastRuntimeId = lastRuntimeId;
            this.xml = xml;
        }

        public AdsUserReportDefinitionDocument getXml() {
            return xml;
        }

        public Id getLastRuntimeId() {
            return lastRuntimeId;
        }
    }

    Id importExistReport(final UserReport existingReport, final AdsUserReportExchangeDocument xDoc) throws IOException;

    List<ModuleInfo> listModules();

    UserReportListRsDocument listReports(final Id moduleId);

    UserReportListRsDocument listReportsWithParam(final UserReport ownerReport, final Id paramId);

    ReportDataInfo loadReportData(final Id reportId, final long version);
    
    byte[] listImages(final Id moduleId);

    void saveRuntime(final File runtimeFile, final Id reportId, final Id runtimeId, final long version, final AdsUserReportDefinitionDocument xDoc);

    boolean loadReportVersions(final Id reportId, final UserReportVersionListRs xReportVersionsRs);

    List<EIsoLanguage> getLanguages();

    void processException(final Throwable throwable);

    UserReport createReport(final ReportsModule module, final String name, final Id reportId, final AdsReportClassDef initialReport);

    UserReport.ReportVersion createVersionImpl(final AdsUserReportDefinitionDocument xDoc, final Id reportId, final UserReport userReport);

    void updateModuleRepository(final ReportsModule module, final String content, final Bin images);

    boolean setCurrentVersionId(final UserReport.ReportVersion version, final Id reportId);

    boolean compile(final AdsDefinition whatToCompile, final boolean isForValidation, boolean sync);

    boolean removeVersion(final UserReport.ReportVersion version, final Id reportId);
    
    void executeTask(final Runnable task);
    
    boolean messageConfirmation(final String message);
}
