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
package org.radixware.kernel.server.reports.fo;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.FopFactoryBuilder;
import org.apache.fop.apps.FopFactoryConfig;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportBand;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportCell;
import org.radixware.kernel.common.enums.EReportExportFormat;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.server.reports.AbstractReportGenerator;
import org.radixware.kernel.server.reports.DefaultReportFileController;
import org.radixware.kernel.server.reports.IReportFileController;
import org.radixware.kernel.server.reports.ReportGenerationException;
import org.radixware.kernel.server.reports.ReportStateInfo;
import org.radixware.kernel.server.types.Report;

public class FopReportGenerator extends AbstractReportGenerator {

    private final String mimeFormat;
    static final float DEFAULT_REPORT_DPI = 72f;
    private final Report report;

    public FopReportGenerator(Report report, final String mimeFormat, EReportExportFormat format, ReportStateInfo stateInfo) {
        super(report, format, stateInfo);
        this.report = report;
        this.mimeFormat = mimeFormat;
    }

    public FopReportGenerator(Report report, String mimeFormat, EReportExportFormat format) {
        this(report, mimeFormat, format, null);
    }

    @Override
    protected Map<AdsReportCell, AdjustedCell> adjustBand(AdsReportBand band) throws ReportGenerationException {
        throw new IllegalStateException();
    }

    @Override
    protected void viewBand(final List<ReportGenData> genDataList, AdsReportBand band, Map<AdsReportCell, AdjustedCell> adjustedCellContents) throws ReportGenerationException {
        throw new IllegalStateException();
    }

    @Override
    protected void adjustCellsPosition(AdsReportBand container) {
        throw new IllegalStateException();
    }

    @Override
    protected boolean isFormattingSupported() {
        return true;
    }

    private File generateFoFile(ReportStateInfo guide, ReportStateInfo target) throws ReportGenerationException {
        final File file;
        try {
            file = File.createTempFile("report", ".zip");
        } catch (IOException ex) {
            throw new ReportGenerationException(ex);
        }

        try {
            FileOutputStream fout = new FileOutputStream(file);
            try {
                final OutputStream stream = new BufferedOutputStream(fout);
                try {
                    final FoReportGenerator reportGenerator = new FoReportGenerator(report, format, guide);

                    reportGenerator.generateReport(stream);

                    if (target != null) {
                        target.load(reportGenerator.getStateInfo());
                    }
                } finally {
                    try {
                        stream.close();
                    } catch (IOException ex) {
                    }
                }
            } finally {
                try {
                    fout.close();
                } catch (IOException ex) {
                }
            }
        } catch (ReportGenerationException ex) {//IOException was catched earlier
            file.delete();
            throw ex;
        } catch (IOException ex) {
            file.delete();
            throw new ReportGenerationException(ex);
        }

        return file;
    }

    private void generateDocument(File foFile, OutputStream outputStream) throws Throwable {
        final FOUserAgent foUserAgent = FoUserAgentFactory.getFoUserAgent(mimeFormat);
        FopFactoryBuilder builder = new FopFactoryBuilder(new File(".").toURI());
        builder.setComplexScriptFeatures(true);
        builder.setSourceResolution(DEFAULT_REPORT_DPI);
        builder.setTargetResolution(DEFAULT_REPORT_DPI);
        builder.setConfiguration(FoUserAgentFactory.createConfiguration());
        FopFactory factory = builder.build();

        final Fop fop = factory.newFop(mimeFormat, foUserAgent, outputStream);
        final Transformer transformer = TransformerFactory.newInstance().newTransformer();
        final SAXResult saxResult = new SAXResult(fop.getDefaultHandler());
        final FileInputStream fin = new java.io.FileInputStream(foFile);
        try {
            final ZipInputStream zin = new ZipInputStream(fin);
            try {
                for (ZipEntry e = zin.getNextEntry(); e != null; e = zin.getNextEntry()) {
                    if ("report.fo".equals(e.getName())) {
                        final InputStream foInputStream = new java.io.BufferedInputStream(zin);
                        try {
                            final StreamSource foStreamSource = new StreamSource(foInputStream);
                            // parse specified xml (foStreamSource) by SAX
                            // and transform it by FOP handler
                            transformer.transform(foStreamSource, saxResult);
                        } finally {
                            foInputStream.close();
                        }
                        break;
                    }
                }
            } finally {
                try {
                    zin.close();
                } catch (IOException ex) {
                }
            }
        } finally {
            try {
                fin.close();
            } catch (IOException ex) {
            }
        }
        foFile.delete();
    }

    private static class FlushOnCloseOutputStream extends OutputStream {

        private final OutputStream target;
        private boolean flushEnabled = false;

        public FlushOnCloseOutputStream(OutputStream target) {
            this.target = target;
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            target.write(b, off, len);
        }

        @Override
        public void write(byte[] b) throws IOException {
            target.write(b);
        }

        @Override
        public void write(int b) throws IOException {
            target.write(b);
        }

        @Override
        public void flush() throws IOException {
            if (flushEnabled) {
                target.flush();
            }
        }

        @Override
        public void close() throws IOException {
            flushEnabled = true;
            flush();
            target.close();
        }
    }

    @Override
    public void generateReport(OutputStream stream) throws ReportGenerationException {
        // FOP make flush very offen, it causes too many SOAP request/responses between server and explorer
        final OutputStream flushOnCloseOutputStream = new FlushOnCloseOutputStream(stream);

        final File foTemporaryFile = generateFoFile(getStateInfo().getSource(), getStateInfo());

        try {
            generateDocument(foTemporaryFile, flushOnCloseOutputStream);
        } catch (Throwable ex) {
            foTemporaryFile.deleteOnExit(); // 	RADIX-4699
            throw new ReportGenerationException("FO file saved in '" + foTemporaryFile.getAbsolutePath() + "'.", ex);
        }

        foTemporaryFile.delete();
    }

    private class InnerFileController extends DefaultReportFileController {

        private final List<File> foFiles = new LinkedList<>();

        private File dir;

        public InnerFileController(IReportFileController delegate) {
            super(delegate, delegate instanceof DefaultReportFileController ? ((DefaultReportFileController) delegate).createNullStream() : false);
        }

        @Override
        public File getDirectory() {
            if (dir == null) {
                File tmpDir = new File(System.getProperty("java.io.tmpdir"));
                dir = new File(tmpDir, UUID.randomUUID().toString());
            }
            return dir;
        }

        @Override
        public String adjustFileName(Report report, String fileName) {
            return fileName + ".fo";
        }

        @Override
        public void afterCreateFile(Report report, File file, OutputStream output) {
            foFiles.add(file);
        }

        @Override
        public void beforeCloseFile(Report report, File file, OutputStream output) {

        }

        @Override
        public void afterCloseFile(Report report, File file) throws ReportGenerationException {
            if (!file.exists()) {//
                return;
            }
            final String finalFileName = delegate.adjustFileName(report, FileUtils.getFileBaseName(file));
            final File fopFile = new File(getDirectory(), finalFileName);
            try {
                final FileOutputStream out = new FileOutputStream(fopFile);
                delegate.afterCreateFile(report, fopFile, out);
                generateDocument(file, out);
                delegate.beforeCloseFile(report, fopFile, out);
                out.flush();
                out.close();
                delegate.afterCloseFile(report, fopFile);
            } catch (Throwable ex) {
                ex.printStackTrace();
            }
        }

    }

    @Override
    public void generateReport(IReportFileController controller) throws ReportGenerationException {

        final IReportFileController foFileController = new InnerFileController(controller);

        final ReportStateInfo stateInfo = getStateInfo().getSource();
        final FoReportGenerator reportGenerator = new FoReportGenerator(report, format, stateInfo);
        reportGenerator.setSeparateFileGroupLevel(separateFileGroupNumber);

        reportGenerator.generateReport(foFileController);

        if (stateInfo == null) {
            getStateInfo().load(reportGenerator.getStateInfo());
        }
    }
}
