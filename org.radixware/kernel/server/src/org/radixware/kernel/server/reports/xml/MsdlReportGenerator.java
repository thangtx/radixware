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
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportBand;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportCell;
import org.radixware.kernel.common.enums.EReportBandType;
import org.radixware.kernel.common.enums.EReportExportFormat;
import org.radixware.kernel.common.exceptions.SmioException;
import org.radixware.kernel.common.msdl.RootMsdlScheme;
import org.radixware.kernel.server.reports.AbstractReportGenerator;
import org.radixware.kernel.server.reports.DefaultReportFileController;
import org.radixware.kernel.server.reports.IReportFileController;
import org.radixware.kernel.server.reports.ReportGenerationException;
import org.radixware.kernel.server.reports.ReportStateInfo;
import org.radixware.kernel.server.reports.fo.AdjustedCell;
import org.radixware.kernel.server.types.Report;
import org.radixware.schemas.msdl.MessageElementDocument;
import org.radixware.schemas.reports.ReportMsdlType;

public class MsdlReportGenerator extends AbstractReportGenerator {

    private final ReportMsdlType xReportMsdl;
    private OutputStream outputStream;

    public MsdlReportGenerator(Report report, ReportMsdlType xReportMsdl, ReportStateInfo stateInfo) {
        super(report, EReportExportFormat.MSDL, stateInfo);
        this.xReportMsdl = xReportMsdl;
    }

    @Override
    protected boolean isInfiniteHeight() {
        return true;
    }

    @Override
    protected boolean isFormattingSupported() {
        return false;
    }

    @Override
    protected Map<AdsReportCell, AdjustedCell> adjustBand(AdsReportBand band) throws ReportGenerationException {
        // NOTHING - XML doesn't required any formatting
        return null;
    }

    @Override
    protected void adjustCellsPosition(AdsReportBand container) {
        // NOTHING - XML doesn't required any formatting
    }

    private ReportMsdlType.Band findReportMsdlBand(AdsReportBand band) {
        final EReportBandType bandType = band.getType();
        for (ReportMsdlType.Band xReportMsdlBand : xReportMsdl.getBandList()) {
            if (xReportMsdlBand.getType() == bandType) {
                int groupLevel = (xReportMsdlBand.isSetGroupLevel() ? xReportMsdlBand.getGroupLevel() : 0);
                int bandGroupLevel = XmlReportGenerator.getBandLevel(band);
                if (groupLevel == bandGroupLevel) {
                    return xReportMsdlBand;
                }
            }
        }
        return null;
    }

    private static byte[] performXslt(byte[] xml, String xslt) throws TransformerException {
        final java.io.StringReader xsltStringReader = new java.io.StringReader(xslt);
        final StreamSource xsltStreamSource = new StreamSource(xsltStringReader);
        final Transformer transformer = TransformerFactory.newInstance().newTransformer(xsltStreamSource);

        final ByteArrayInputStream xmlInputStream = new ByteArrayInputStream(xml);
        final StreamSource srcStreamSource = new StreamSource(xmlInputStream);

        final ByteArrayOutputStream xmlOutputStream = new ByteArrayOutputStream();
        final StreamResult streamResult = new StreamResult(xmlOutputStream);

        transformer.transform(srcStreamSource, streamResult);

        return xmlOutputStream.toByteArray();
    }

    private static byte[] performMsdl(byte[] xml, String msdl) throws XmlException, SmioException, IOException {
        MessageElementDocument me = MessageElementDocument.Factory.parse(msdl);
        RootMsdlScheme rmsdl = new RootMsdlScheme(me.getMessageElement());
        XmlObject rxml = XmlObject.Factory.parse(new ByteArrayInputStream(xml));
        ByteBuffer bf = rmsdl.getRootMsdlScheme().getStructureFieldModel().getParser().merge(rxml);
        byte[] res = new byte[bf.remaining()];
        bf.get(res);
        return res;
    }

    @Override
    protected void viewBand(final List<ReportGenData> genDataList, AdsReportBand band, Map<AdsReportCell, AdjustedCell> adjustedCellContents) throws ReportGenerationException {

        final ReportMsdlType.Band xReportMsdlBand = findReportMsdlBand(band);
        if (xReportMsdlBand == null) {
            return;
        }

        if (!XmlReportGenerator.isExportable(band)) {
            return;
        }
        final Report report = genDataList.get(genDataList.size() - 1).report;
        final XmlReportGenerator singleBandReportGenerator = new SingleBandXmlReportGenerator(report, band);
        final ByteArrayOutputStream tempOutputStream = new ByteArrayOutputStream();
        singleBandReportGenerator.generateReport(tempOutputStream);
        byte[] xml = tempOutputStream.toByteArray();

        try {
            if (xReportMsdlBand.isSetTransformation()) {
                final String xslt = xReportMsdlBand.getTransformation().getXslt();
                xml = performXslt(xml, xslt);
            }

            final String msdl = xReportMsdlBand.getMsdl();
            xml = performMsdl(xml, msdl);

            outputStream.write(xml);
        } catch (XmlException ex) {
            throw new ReportGenerationException(ex);
        } catch (SmioException ex) {
            throw new ReportGenerationException(ex);
        } catch (IOException ex) {
            throw new ReportGenerationException(ex);
        } catch (TransformerException ex) {
            throw new ReportGenerationException(ex);
        }
    }

//    @Override
//    public void generateReport(OutputStream stream) throws ReportGenerationException {
//        outputStream = stream;
//        super.generateReport(stream);
//    }

    @Override
    public void generateReport(final IReportFileController controller) throws ReportGenerationException {
        final IReportFileController writerManager = new DefaultReportFileController(controller) {

            @Override
            public void afterCreateFile(Report report, File file, OutputStream output) throws ReportGenerationException {
                super.afterCreateFile(report, file, output);
                outputStream = new BufferedOutputStream(output);
            }

            @Override
            public void beforeCloseFile(Report report, File file, OutputStream output) throws ReportGenerationException {
                if (outputStream != null) {
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
