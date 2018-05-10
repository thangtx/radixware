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
package org.radixware.kernel.designer.environment.reports;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import org.netbeans.api.progress.ProgressUtils;
import org.openide.util.Exceptions;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportBand;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsSubReport;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.preview.AasReportPreviewServiceClient;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.preview.AbstractReportPreviewDataProvider;
import org.radixware.kernel.common.exceptions.ServiceCallException;
import org.radixware.kernel.common.exceptions.ServiceCallFault;
import org.radixware.kernel.common.exceptions.ServiceCallTimeout;
import org.radixware.kernel.common.repository.ads.fs.IRepositoryAdsModule;
import org.radixware.kernel.common.repository.fs.IJarDataProvider;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.ads.common.reports.ReportPreviewSettings;
import org.radixware.kernel.designer.common.dialogs.utils.InputOutputPrinter;

public class ReportPreviewDataProvider extends AbstractReportPreviewDataProvider {

    private final InputOutputPrinter printer = new InputOutputPrinter(ReportPreviewSettings.PREVIEW_OUTPUT_NAME);

    @Override
    public byte[] preview(final Id reportId, final String reportLayerUri, final String exportFormat, final byte[] reportJar, final byte[] testDataZip, final String exportLocale) {
        final AtomicReference<byte[]> result = new AtomicReference<>();
        ProgressUtils.runOffEventDispatchThread(new Runnable() {
            @Override
            public void run() {
                try {
                    int requestTimeout = Integer.valueOf(ReportPreviewSettings.AAS_REQUEST_TIMEOUT.get(reportId));
                    AasReportPreviewServiceClient client = new AasReportPreviewServiceClient(ReportPreviewSettings.AAS_ADDRESS.get(reportId), requestTimeout);
                    result.set(client.preview(reportId, reportLayerUri, exportFormat, reportJar, testDataZip, exportLocale));
                } catch (IOException | ServiceCallException | ServiceCallTimeout | ServiceCallFault | InterruptedException ex) {
                    try {
                        printer.printlnWarning("Unable to generate report preview: " + ex.getMessage());
                        Exceptions.printStackTrace(ex);
                    } catch (IOException ex1) {
                        Exceptions.printStackTrace(ex1);
                    }
                }
            }
        }, "Please wait...", new AtomicBoolean(false), false, 0, 1500);

        return result.get();
    }

    @Override
    protected void addReporToJar(final AdsReportClassDef report, final JarOutputStream reportsJar) throws IOException {
        IRepositoryAdsModule repository = report.getModule().getRepository();
        IJarDataProvider serverJarDataProvider = repository.getJarFile("bin/server.jar");

        if (serverJarDataProvider != null) {
            for (IJarDataProvider.IJarEntry entry : serverJarDataProvider.entries()) {
                String[] entryParts = entry.getName().replace("\\", "/").split("/");
                if (!entry.isDirectory() && entry.getName().endsWith(".class")
                        && entryParts[entryParts.length - 1].startsWith(report.getId().toString())) {
                    JarEntry jarEntry = new JarEntry(entry.getName());
                    reportsJar.putNextEntry(jarEntry);
                    reportsJar.write(serverJarDataProvider.getEntryData(entry.getName()));
                    reportsJar.closeEntry();
                }
            }
        }

        if (report.getForm() != null && report.getForm().getBands() != null) {
            for (AdsReportBand band : report.getForm().getBands()) {
                if (band.getPreReports() != null) {
                    for (AdsSubReport subReport : band.getPreReports()) {
                        addReporToJar(subReport.getReport(), reportsJar);
                    }
                }

                if (band.getPostReports() != null) {
                    for (AdsSubReport subReport : band.getPostReports()) {
                        addReporToJar(subReport.getReport(), reportsJar);
                    }
                }
            }
        }
    }
}
