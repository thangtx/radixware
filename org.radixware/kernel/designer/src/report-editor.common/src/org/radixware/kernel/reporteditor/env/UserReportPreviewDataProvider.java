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
package org.radixware.kernel.reporteditor.env;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;
import org.apache.xmlbeans.XmlObject;
import org.openide.util.Exceptions;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.preview.AbstractReportPreviewDataProvider;
import org.radixware.kernel.common.exceptions.RadixObjectError;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Bin;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.userreport.repository.UserReport;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.common.utils.XmlObjectProcessor;
import org.radixware.kernel.designer.ads.common.reports.ReportPreviewSettings;
import org.radixware.kernel.designer.common.dialogs.utils.InputOutputPrinter;
import org.radixware.kernel.reporteditor.common.RequestExecutor;
import org.radixware.kernel.reporteditor.common.UserExtensionManager;
import org.radixware.schemas.reports.ReportPreviewRq;
import org.radixware.schemas.reports.ReportPreviewRqDocument;
import org.radixware.schemas.reports.ReportPreviewRsDocument;
import org.radixware.schemas.types.BinBase64;

public class UserReportPreviewDataProvider extends AbstractReportPreviewDataProvider {

    private static final String SERVER_PATH_PART = "/server/";
    private static final String COMMON_PATH_PART = "/common/";
    private static final Id PREVIEW_REPORT_COMMAND_ID = Id.Factory.loadFrom("clcUZFK3NCYPRGAVC47FWX5CGBQ5I");
    
    private final InputOutputPrinter printer = new InputOutputPrinter(ReportPreviewSettings.PREVIEW_OUTPUT_NAME);

    @Override
    public byte[] preview(final Id reportId, final String reportLayerUri, final String exportFormat, final byte[] reportJar, final byte[] testDataZip, final String exportLocale) {
        final ReportPreviewRsDocument rs[] = new ReportPreviewRsDocument[1];
        UserExtensionManager.getInstance().getRequestExecutor().submitAction(new RequestExecutor.ExplorerActionWithWaitImpl() {
            @Override
            public void execute(final IClientEnvironment env) {
                try {
                    ReportPreviewRqDocument rqDoc = ReportPreviewRqDocument.Factory.newInstance();
                    ReportPreviewRq rq = rqDoc.addNewReportPreviewRq();
                    rq.setReportId(reportId);                    
                    rq.setExportFormat(exportFormat);

                    BinBase64 reportJarBytes = BinBase64.Factory.newInstance();
                    reportJarBytes.setByteArrayValue(reportJar);

                    BinBase64 testDataZipBytes = BinBase64.Factory.newInstance();
                    testDataZipBytes.setByteArrayValue(testDataZip);

                    rq.setReportJar(reportJarBytes.getByteArrayValue());
                    rq.setTestDataZip(testDataZipBytes.getByteArrayValue());

                    rq.setExportLocale(exportLocale);

                    XmlObject output = UserExtensionManager.getInstance().getEnvironment().getEasSession().executeContextlessCommand(PREVIEW_REPORT_COMMAND_ID, rqDoc, ReportPreviewRsDocument.class);

                    rs[0] = XmlObjectProcessor.cast(getClass().getClassLoader(), output, ReportPreviewRsDocument.class);
                } catch (ServiceClientException | InterruptedException ex) {
                    try {
                        printer.printlnWarning("Unable to generate report preview: " + ex.getMessage());
                        UserExtensionManager.getInstance().getEnvironment().processException(ex);                        
                    } catch (IOException ex1) {
                        UserExtensionManager.getInstance().getEnvironment().processException(ex1);
                    }
                }
            }
        });
        return rs[0] == null || rs[0].getReportPreviewRs() == null ? null : rs[0].getReportPreviewRs().getReportData();
    }

    @Override
    protected void addReporToJar(final AdsReportClassDef report, final JarOutputStream reportsJar) throws IOException {
        final AtomicReference<Bin> reportRuntime = new AtomicReference<>();
        UserExtensionManager.getInstance().getRequestExecutor().submitAction(new RequestExecutor.ExplorerActionWithWaitImpl() {
            @Override
            public void execute(final IClientEnvironment env) {
                try {
                    UserReport userReport = UserExtensionManager.getInstance().getUserReports().findReportById(report.getId());
                    EntityModel versionModel = UserReport.openReportVersionModel(UserExtensionManager.getInstance().getEnvironment(), report.getId(), userReport.getVersions().getCurrent().getVersion());
                    reportRuntime.set((Bin) versionModel.getProperty(UserReport.REPORT_VERSION_RUNTIME_PROP_ID).getValueObject());
                } catch (ServiceClientException | InterruptedException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        });

        if (reportRuntime.get() == null) {
            return;
        }

        ByteArrayInputStream baos = new ByteArrayInputStream(reportRuntime.get().get());
        try (JarInputStream jis = new JarInputStream(baos)) {
            JarEntry entry = jis.getNextJarEntry();
            while (entry != null) {
                if (entry.getName().contains(SERVER_PATH_PART) || entry.getName().contains(COMMON_PATH_PART)) {
                    JarEntry outputEntry = new JarEntry(entry.getName());
                    reportsJar.putNextEntry(outputEntry);
                    FileUtils.copyStream(jis, reportsJar);
                }

                entry = jis.getNextJarEntry();
            }
        }
    }
}
