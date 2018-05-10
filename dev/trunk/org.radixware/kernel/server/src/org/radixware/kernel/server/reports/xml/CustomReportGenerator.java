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
package org.radixware.kernel.server.reports.xml;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import org.apache.xmlbeans.XmlException;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportBand;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportCell;
import org.radixware.kernel.common.enums.EReportExportFormat;
import org.radixware.kernel.server.reports.AbstractReportGenerator;
import org.radixware.kernel.server.reports.DefaultReportFileController;
import org.radixware.kernel.server.reports.IReportFileController;
import org.radixware.kernel.server.reports.ReportGenerationException;
import org.radixware.kernel.server.reports.ReportStateInfo;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.html.AdjustedCell;
import org.radixware.kernel.server.types.Report;
import org.radixware.schemas.reports.BandDocument;

public class CustomReportGenerator extends AbstractReportGenerator {

    private final IReportWriter writer;
    private OutputStream outputStream;

    public CustomReportGenerator(Report report, final IReportWriter writer, ReportStateInfo stateInfo) {
        super(report, EReportExportFormat.CUSTOM, stateInfo);
        this.writer = writer;
    }

    @Override
    protected boolean isFormattingSupported() {
        return false;
    }

    @Override
    protected Map<AdsReportCell, AdjustedCell> adjustBand(AdsReportBand band) throws ReportGenerationException {
        //do nothing
        return null;
    }

    @Override
    protected void adjustCellsPosition(AdsReportBand container) {
    }

    @Override
    protected boolean isInfiniteHeight() {
        return true;
    }

    @Override
    protected void viewBand(final List<ReportGenData> genDataList, AdsReportBand band, Map<AdsReportCell, AdjustedCell> adjustedCellContents, ReportGenData currentGenData) throws ReportGenerationException {
        if (!XmlReportGenerator.isExportable(band)) {
            return;
        }
        final Report report = genDataList.get(genDataList.size() - 1).report;
        final SingleBandXmlReportGenerator singleBandGenerator = new SingleBandXmlReportGenerator(report, band);
        final ByteArrayOutputStream tempOutputStream = new ByteArrayOutputStream();
        singleBandGenerator.generateReport(tempOutputStream);
        byte[] singleBandReportXml = tempOutputStream.toByteArray();
        try {
            final BandDocument bandDoc = BandDocument.Factory.parse(new ByteArrayInputStream(singleBandReportXml));
            writer.writeBand(bandDoc.getBand(), outputStream);
        } catch (XmlException | IOException ex) {
            throw new ReportGenerationException(ex);
        }
    }

//    @Override
//    public void generateReport(OutputStream stream) throws ReportGenerationException {
//        this.outputStream = stream;
//        writer.begin(stream);
//        super.generateReport(stream);
//        writer.end(stream);
//    }

    @Override
    public void generateReport(final IReportFileController controller) throws ReportGenerationException {
        final IReportFileController writerManager = new DefaultReportFileController(controller) {

            @Override
            public void afterCreateFile(Report report, File file, OutputStream output) throws ReportGenerationException {
                super.afterCreateFile(report, file, output);
                outputStream = new BufferedOutputStream(output);
                writer.begin(outputStream);

            }

            @Override
            public void beforeCloseFile(Report report, File file, OutputStream output) throws ReportGenerationException {
                if (outputStream != null) {
                    writer.end(outputStream);
                    try {
                        outputStream.flush();
                    } catch (IOException ex) {
                        throw new ReportGenerationException(ex);
                    } finally {
                        outputStream = null;
                    }
                }

                super.beforeCloseFile(report, file, output);
            }

        };

        super.generateReport(writerManager);
    }
}
