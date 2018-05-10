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

import java.awt.Color;
import org.radixware.kernel.designer.ads.common.reports.ReportPreviewSettings;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ServiceLoader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import org.openide.util.Exceptions;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsUserReportClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.preview.AbstractReportPreviewDataProvider;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.ExecAppByFileName;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.designer.common.dialogs.utils.InputOutputPrinter;

public class ShowReportPreviewActionListener implements ActionListener {

    private final AdsReportClassDef report;
    private final InputOutputPrinter printer = new InputOutputPrinter(ReportPreviewSettings.PREVIEW_OUTPUT_NAME);

    public ShowReportPreviewActionListener(AdsReportClassDef report) {
        this.report = report;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            printer.reset();
            AbstractReportPreviewDataProvider dataProvider = ServiceLoader.load(AbstractReportPreviewDataProvider.class).iterator().next();

            byte[] reportJar = null;
            try {
                reportJar = dataProvider.getReportJar(report);
            } catch (IOException ex) {
                printer.printlnWarning("Unable to obtain report binaries: " + ex.getMessage());
                Exceptions.printStackTrace(ex);
            }

            byte[] testDataZip = getTestDataZip();
            Id reportId = report instanceof AdsUserReportClassDef ? ((AdsUserReportClassDef) report).getRuntimeId() : report.getId();

            byte[] result = dataProvider.preview(reportId, report.getLayer().getURI(), ReportPreviewSettings.EXPORT_FORMAT.get(report.getId()), reportJar, testDataZip, getExportLocale());
            if (result != null) {
                openReportData(result);
                printer.println("REPORT PREVIEW GENERATION SUCCESSFULL", new Color(102, 255, 142));
            } else {
                printer.printlnError("REPORT PREVIEW GENERATION FAILED");
            }
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private void openReportData(byte[] reportData) throws IOException {
        try {
            File tmpDir = Files.createTempDirectory("ReportPreviewTmp").toFile();

            ByteArrayInputStream bis = new ByteArrayInputStream(reportData);
            try (ZipInputStream zis = new ZipInputStream(bis)) {
                ZipEntry entry = zis.getNextEntry();
                while (entry != null) {
                    File reportResultFile = new File(tmpDir, entry.getName());
                    if (!reportResultFile.exists()) {
                        reportResultFile.createNewFile();
                        try (FileOutputStream fos = new FileOutputStream(reportResultFile)) {
                            FileUtils.copyStream(zis, fos);
                        }
                        entry = zis.getNextEntry();
                    }
                }
            }

            if (tmpDir.listFiles().length == 1) {
                ExecAppByFileName.exec(tmpDir.listFiles()[0].getAbsolutePath());
            } else {
                ExecAppByFileName.exec(tmpDir.getAbsolutePath());
            }
        } catch (IOException ex) {
            printer.printlnWarning("Unable to open report: " + ex.getMessage());
            Exceptions.printStackTrace(ex);
        }
    }

    private byte[] getTestDataZip() throws IOException {
        File testDataDir = new File(ReportPreviewSettings.TEST_DATA_FOLDER_PATH.get(report.getId()));
        if (!testDataDir.exists()) {
            printer.printlnWarning("Unable to obtain preview test data: folder '" + testDataDir.getAbsolutePath() + "' doesn't exists");
            return null;
        }

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try (ZipOutputStream zos = new ZipOutputStream(baos)) {
                for (File file : testDataDir.listFiles()) {
                    if (!file.isDirectory()) {
                        try (FileInputStream fis = new FileInputStream(file)) {
                            ZipEntry ze = new ZipEntry(file.getName());
                            zos.putNextEntry(ze);
                            FileUtils.copyStream(fis, zos);
                            zos.closeEntry();
                        }
                    }
                }
            }

            return baos.toByteArray();
        } catch (IOException ex) {
            printer.printlnWarning("Unable to obtain preview test data: " + ex.getMessage());
            Exceptions.printStackTrace(ex);
        }

        return null;
    }

    private String getExportLocale() {
        StringBuilder exportLocale = new StringBuilder();
        if (!Utils.emptyOrNull(ReportPreviewSettings.EXPORT_LANGUAGE.get(report.getId()))) {
            exportLocale.append(ReportPreviewSettings.EXPORT_LANGUAGE.get(report.getId()));
            if (!Utils.emptyOrNull(ReportPreviewSettings.EXPORT_REGION.get(report.getId()))) {
                exportLocale.append("_").append(ReportPreviewSettings.EXPORT_REGION.get(report.getId()));
            }
        }

        return exportLocale.toString();
    }
}
