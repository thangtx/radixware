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

package org.radixware.kernel.reporteditor.tree;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;
import org.netbeans.api.progress.ProgressUtils;
import org.radixware.kernel.common.userreport.repository.ReportsModule;
import org.radixware.kernel.common.userreport.repository.UserReport;
import org.radixware.kernel.common.userreport.repository.UserReports;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;


public class ExportReportUtil {

    static File currentDirectory = null;

    public void export(UserReports reports, ReportsModule context) {
        exportReports(reports, context, null);
    }

    private void exportReports(final UserReports reports, final ReportsModule context, File directory) {
        if (directory == null) {//first call. should choose directory to export
            JFileChooser fileChooser = new JFileChooser(ExportReportUtil.currentDirectory);
            for (FileFilter f : fileChooser.getChoosableFileFilters()) {
                fileChooser.removeChoosableFileFilter(null);
            }
            FileFilter filter = new FileFilter() {
                @Override
                public boolean accept(File f) {
                    if (f.isDirectory()) {
                        return true;
                    } else {
                        return false;
                    }
                }

                @Override
                public String getDescription() {
                    return "Directory";
                }
            };

            fileChooser.addChoosableFileFilter(filter);
            fileChooser.setFileFilter(filter);
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
            if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {

                File selectedDir = fileChooser.getSelectedFile();

                final File dir = new File(selectedDir, FileUtils.string2UniversalFileName(MessageFormat.format("{0,time,yyyy-MM-dd HH:mm:ss}", new Date(System.currentTimeMillis())), '.'));

                ProgressUtils.showProgressDialogAndRun(new Runnable() {
                    @Override
                    public void run() {
                        exportReports(reports, context, dir);
                    }
                }, "Export Reports...");
            }
        } else {
            if (context == null) {
                for (ReportsModule module : reports.getReportModules()) {
                    exportReports(reports, module, directory);
                }
            } else {
                //upload reports if nesessary
                File moduleDir = new File(directory, FileUtils.string2UniversalFileName(context.getName(), '_'));
                context.getDefinitions().list();
                List<UserReport> reportList = reports.listReports(context.getId());
                for (UserReport report : reportList) {
                    export(report, null, moduleDir);
                }
            }
        }

    }

    public void export(UserReport report, UserReport.ReportVersion version) {
        export(report, version, null);
    }

    public void export(UserReport report, UserReport.ReportVersion version, File dir) {
        final UserReport rpt;
        final List<Long> versions;
        if (version != null) {
            rpt = version.getUserReport();
            versions = new LinkedList<>();
            versions.add(version.getVersion());

        } else {
            rpt = report;
            versions = null;
        }
        File file;

        if (dir != null) {
            dir.mkdirs();
            String fileName = report.getName() + ".xml";
            fileName = FileUtils.string2UniversalFileName(fileName, '_');
            String baseFileName = fileName;
            File outFile = new File(dir, fileName);
            int index = 1;
            while (outFile.exists()) {
                outFile = new File(dir, baseFileName + "_" + String.valueOf(index));
                index++;
            }
            file = outFile;
        } else {
            JFileChooser fileChooser = new JFileChooser(ExportReportUtil.currentDirectory);
            for (FileFilter f : fileChooser.getChoosableFileFilters()) {
                fileChooser.removeChoosableFileFilter(f);
            }
            FileFilter filter = new FileFilter() {
                @Override
                public boolean accept(File f) {
                    if (f.isDirectory()) {
                        return true;
                    } else {
                        return f.getName().endsWith(".xml");
                    }
                }

                @Override
                public String getDescription() {
                    return "User-Defined Report Exchange File (*.xml)";
                }
            };

            fileChooser.addChoosableFileFilter(filter);
            fileChooser.setFileFilter(filter);
            fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
            if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                file = fileChooser.getSelectedFile();
                if (!file.getName().endsWith(".xml")) {
                    file = new File(file.getParentFile(), file.getName() + ".xml");
                }
                if (file.exists()) {
                    if (!DialogUtils.messageConfirmation("File " + file.getAbsolutePath() + " is already exists. Overwite?")) {
                        return;
                    }
                }
            } else {
                file = null;
            }
        }
        if (dir == null) {
            ProgressUtils.showProgressDialogAndRun(new SaveReportTask(file, rpt, versions), "Export Report...");
        } else {
            new SaveReportTask(file, rpt, versions).run();
        }
    }

    private class SaveReportTask implements Runnable {

        private final File file;
        private final UserReport rpt;
        private final List<Long> versions;

        public SaveReportTask(File file, UserReport rpt, List<Long> versions) {
            this.file = file;
            this.rpt = rpt;
            this.versions = versions;
        }

        @Override
        public void run() {
            try {
                if (file == null) {
                    return;
                }
                FileOutputStream out = new FileOutputStream(file);
                try {

                    try {

                        rpt.exportReport(out, versions);
                    } catch (final IOException ex) {
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                DialogUtils.messageError(ex);
                            }
                        });
                    }
                } finally {
                    try {
                        out.flush();
                    } catch (IOException ex) {
                    }
                    try {
                        out.close();
                    } catch (IOException ex) {
                    }
                }
            } catch (final FileNotFoundException ex) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        DialogUtils.messageError(ex);
                    }
                });
            }
        }
    }
}
