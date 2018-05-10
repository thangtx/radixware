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
package org.radixware.kernel.utils.traceview.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.mozilla.universalchardet.UniversalDetector;
import org.radixware.kernel.utils.traceview.TraceEvent;
import org.radixware.kernel.utils.traceview.TraceViewSettings;

public class TraceViewUtils {

    public static void printHelp(Options options) {
        PrintWriter writer = System.console().writer();
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp(writer, 80, "java -jar TraceView-all.jar", "Options:", options, 3, 5, TraceViewSettings.EXAMPLE_FOR_REFERENCE, true);
        writer.flush();
    }

    public static String detectEncoding(File file) {
        byte[] buf = new byte[4096];
        UniversalDetector detector = new UniversalDetector(null);
        int nread;
        java.io.FileInputStream fis;
        try {
            fis = new java.io.FileInputStream(file);

            try {
                while ((nread = fis.read(buf)) > 0 && !detector.isDone()) {
                    detector.handleData(buf, 0, nread);
                }
            } catch (IOException ex) {
                Logger.getLogger(TraceViewUtils.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                fis.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(TraceViewUtils.class.getName()).log(Level.SEVERE, null, ex);
        }

        detector.dataEnd();

        String encoding = detector.getDetectedCharset();

        detector.reset();

        if (encoding != null) {
            return encoding;
        } else {
            return "UTF-8";
        }
    }

    public static String getLogMessageByIndex(int index, JTable table) {
        return new StringBuilder(((TraceViewSettings.ESeverity) table.getValueAt(index, 0)).getShortName())
                .append(" ")
                .append(TraceViewSettings.DATE_FORMAT_PLAIN_TEXT.get().format(((TraceEvent.IndexedDate) table.getValueAt(index, 1)).getDate()))
                .append(" : ")
                .append(((String) table.getValueAt(index, 2)))
                .append(" : [")
                .append(((String) table.getValueAt(index, 3)))
                .append("] : ")
                .append(((String) table.getValueAt(index, 4))).toString();
    }

    public static void saveLogMessagesInFile(File file, int[] rowIndicesForSaving, JTable table) throws IOException {
        try (FileWriter fwr = new FileWriter(file); BufferedWriter bwr = new BufferedWriter(fwr)) {
            for (int i = 0; i < rowIndicesForSaving.length; i++) {
                bwr.write(getLogMessageByIndex(rowIndicesForSaving[i], table) + System.lineSeparator() + System.lineSeparator());
            }
        }
    }

    public static int getPreferredWidthColumn(int index, JTable fictiveTableForObtainingTableDimensions) {
        return fictiveTableForObtainingTableDimensions.prepareRenderer(fictiveTableForObtainingTableDimensions.getCellRenderer(fictiveTableForObtainingTableDimensions.getModel().getRowCount() - 1, index),
                fictiveTableForObtainingTableDimensions.getModel().getRowCount() - 1, index).getPreferredSize().width;
    }

    public static boolean isInGroup(TraceEvent event, TraceEvent templateEvent) {
        if (templateEvent == null) {
            return false;
        }

        if (TraceViewSettings.ESplitArgs.SEVERITY.isIncluded && !event.getSeverity().equals(templateEvent.getSeverity())) {
            return false;
        }

        if (TraceViewSettings.ESplitArgs.SOURCE.isIncluded && !event.getSource().equals(templateEvent.getSource())) {
            return false;
        }

        if (TraceViewSettings.ESplitArgs.CONTEXT.isIncluded && !event.getContext().equals(templateEvent.getContext())) {
            return false;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(event.getIndexedDate().getDate());
        Calendar templateCalendar = Calendar.getInstance();
        templateCalendar.setTime(templateEvent.getIndexedDate().getDate());
        if (TraceViewSettings.ESplitArgs.MINUTE.isIncluded && !(calendar.get(Calendar.MINUTE) == templateCalendar.get(Calendar.MINUTE)
                && calendar.get(Calendar.HOUR) == templateCalendar.get(Calendar.HOUR)
                && calendar.get(Calendar.DATE) == templateCalendar.get(Calendar.DATE))) {
            return false;
        } else if (TraceViewSettings.ESplitArgs.HOUR.isIncluded && !(calendar.get(Calendar.HOUR) == templateCalendar.get(Calendar.HOUR)
                && calendar.get(Calendar.DATE) == templateCalendar.get(Calendar.DATE))) {
            return false;
        } else if (TraceViewSettings.ESplitArgs.DAY.isIncluded && calendar.get(Calendar.DATE) != templateCalendar.get(Calendar.DATE)) {
            return false;
        }

        if (TraceViewSettings.ESplitArgs.SRC_FOLDER.isIncluded && !Objects.equals(event.getFile().getParent(), templateEvent.getFile().getParent())) {
            return false;
        }

        return !(TraceViewSettings.ESplitArgs.SRC_FILE.isIncluded && !(event.getFile() == templateEvent.getFile()));
    }

    public static String getFileName(TraceEvent event) {
        StringBuilder fileName = new StringBuilder();
        for (int i = 0; i < TraceViewSettings.ESplitArgs.values().length; i++) {
            for (TraceViewSettings.ESplitArgs args : TraceViewSettings.ESplitArgs.values()) {
                if (args.position == i) {
                    if (args == TraceViewSettings.ESplitArgs.CONTEXT) {
                        addSeparator(fileName, "_");
                        fileName.append(event.getContext());
                    } else if (args == TraceViewSettings.ESplitArgs.SOURCE) {
                        addSeparator(fileName, "_");
                        fileName.append(event.getSource());
                    } else if (args == TraceViewSettings.ESplitArgs.SEVERITY) {
                        addSeparator(fileName, "_");
                        fileName.append(event.getSeverity());
                    } else if (args == TraceViewSettings.ESplitArgs.MINUTE) {
                        addSeparator(fileName, "_");
                        fileName.append(TraceViewSettings.DATE_FORMAT_MINUTE.get().format(event.getIndexedDate().getDate()));
                    } else if (args == TraceViewSettings.ESplitArgs.HOUR) {
                        if (!TraceViewSettings.ESplitArgs.MINUTE.isIncluded) {
                            addSeparator(fileName, "_");
                            fileName.append(TraceViewSettings.DATE_FORMAT_HOUR.get().format(event.getIndexedDate().getDate()));
                        }
                    } else if (args == TraceViewSettings.ESplitArgs.DAY) {
                        if (!TraceViewSettings.ESplitArgs.MINUTE.isIncluded && !TraceViewSettings.ESplitArgs.HOUR.isIncluded) {
                            addSeparator(fileName, "_");
                            fileName.append(TraceViewSettings.DATE_FORMAT_DAY.get().format(event.getIndexedDate().getDate()));
                        }
                    } else if (args == TraceViewSettings.ESplitArgs.SRC_FILE) {
                        addSeparator(fileName, "_");
                        fileName.append(getPartialPath(event.getFile())).append("_").append(event.getFile().getName());
                    } else if (args == TraceViewSettings.ESplitArgs.SRC_FOLDER) {
                        if (!TraceViewSettings.ESplitArgs.SRC_FILE.isIncluded) {
                            addSeparator(fileName, "_");
                            fileName.append(getPartialPath(event.getFile()));
                        }
                    }
                }
            }
        }
        if (fileName.length() != 0) {
            fileName.insert(0, "_");
        }
        return fileName.toString();
    }

    private static StringBuilder getPartialPath(File file) {
        StringBuilder fileName = new StringBuilder();
        if (file.getParentFile() != null) {
            if (file.getParentFile().getParentFile() != null) {
                fileName.append(file.getParentFile().getParentFile().getName());
            }
            addSeparator(fileName, "_");
            fileName.append(file.getParentFile().getName());
        }
        return fileName;
    }

    private static void addSeparator(StringBuilder str, String separator) {
        if (str.length() != 0) {
            str.append(separator);
        }
    }

    public static String getQueryOrder() {
        boolean flag = true;
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < TraceViewSettings.ESplitArgs.values().length; i++) {
            for (TraceViewSettings.ESplitArgs args : TraceViewSettings.ESplitArgs.values()) {
                if (args.position == i) {
                    if (args == TraceViewSettings.ESplitArgs.CONTEXT) {
                        addSeparator(result, ", ");
                        result.append("context");
                    } else if (args == TraceViewSettings.ESplitArgs.SOURCE) {
                        addSeparator(result, ", ");
                        result.append("source");
                    } else if (args == TraceViewSettings.ESplitArgs.SEVERITY) {
                        addSeparator(result, ", ");
                        result.append("severity");
                    } else if (args == TraceViewSettings.ESplitArgs.MINUTE || args == TraceViewSettings.ESplitArgs.HOUR || args == TraceViewSettings.ESplitArgs.DAY) {
                        if (flag) {
                            addSeparator(result, ", ");
                            result.append("date");
                            flag = false;
                        }
                    } else if (args == TraceViewSettings.ESplitArgs.SRC_FILE || args == TraceViewSettings.ESplitArgs.SRC_FOLDER) {
                        addSeparator(result, ", ");
                        result.append("file");
                    }
                }
            }
        }
        if (flag) {
            addSeparator(result, ", ");
            result.append("date");
        }
        return result.toString();
    }
}
