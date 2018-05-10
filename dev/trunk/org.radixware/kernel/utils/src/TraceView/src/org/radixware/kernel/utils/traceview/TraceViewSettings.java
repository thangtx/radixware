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
package org.radixware.kernel.utils.traceview;

import java.awt.Color;
import javax.swing.ImageIcon;
import org.radixware.kernel.utils.traceview.utils.ThreadLocalSimpleDateFormat;
import org.radixware.kernel.utils.traceview.utils.TraceViewImageDistributor;

public class TraceViewSettings {

    public static final ThreadLocalSimpleDateFormat DATE_FORMAT_GUI = new ThreadLocalSimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
    public static final ThreadLocalSimpleDateFormat DATE_FORMAT_XML = new ThreadLocalSimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS"); //XXX
    public static final ThreadLocalSimpleDateFormat DATE_FORMAT_XML_NO_MS = new ThreadLocalSimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"); //XXX
    public static final ThreadLocalSimpleDateFormat DATE_FORMAT_PLAIN_TEXT = new ThreadLocalSimpleDateFormat("dd/MM/yy HH:mm:ss.SSS");
    public static final ThreadLocalSimpleDateFormat DATE_FORMAT_MINUTE = new ThreadLocalSimpleDateFormat("yyyy_MM_dd_HH_mm");
    public static final ThreadLocalSimpleDateFormat DATE_FORMAT_HOUR = new ThreadLocalSimpleDateFormat("yyyy_MM_dd_HH");
    public static final ThreadLocalSimpleDateFormat DATE_FORMAT_DAY = new ThreadLocalSimpleDateFormat("yyyy_MM_dd");

    public static final String EXAMPLE_FOR_REFERENCE = "\nExample:\n"
            + "java -jar TraceView-all.jar "
            + "-files \"C:\\foo\\bar\\;C:\\foo\\baz\\File1.log\" "
            + "-filter \"date >= '13/02/17 14:02:05.000' and context like '%Term#64%' and severity='WARNING'\" "
            + "-split \"hour;source\" "
            + "-outputDir C:\\foo\\filtered_dir "
            + "-outputFilePrefix parsedFile "
            + "-outputFileSuffix .txt \n";

    public static final String PREFIX_XML_SCHEMA = "<EventListxmlns=\"http://schemas.radixware.org/systemcommands.xsd\"";

    public static final String PATH_TO_RESOURCES = "/org/radixware/kernel/utils/traceview/resources/";

    //RP - relative path -/- AP - absolute path
    public static final String ICON_CONTEXT_ADD_RP = "contextIcon_add.png";
    public static final String ICON_CONTEXT_REMOVE_RP = "contextIcon_remove.png";
    public static final String ICON_PASTE_FROM_CLIPBOARD_RP = "pasteIcon.png";
    public static final String ICON_ADD_ALL_RP = "add_all.png";
    public static final String ICON_REMOVE_ALL_RP = "remove_all.png";
    public static final String ICON_NEXT = "next.png";
    public static final String ICON_PREV = "prev.png";

    //Get the data from the trace parsing results map
    public static final String DATA = "List<TraceEvent>";
    public static final String FICTIVE_ELEMENT = "TraceEvent";
    public static final String UNIQUE_CONTEXTS = "UniqueContexts";
    public static final String UNIQUE_SOURCES = "UniqueSources";
    public static final String CONTEXT_ARRAY = "ContextArray";

    public static final TraceViewImageDistributor DATE_TIME_PICKER_IMAGE_DISTRIBUTOR = new TraceViewImageDistributor();

    public static final int SIZE_OF_FILE_IS_CONSIDERED_INSIGNIFICANT = 1024 * 1024;

    public static final Color COLOR_EVEN_LINE = new Color(220, 220, 220);
    public static final Color COLOR_NOT_EVEN_LINE = new Color(236, 233, 216);
    public static final Color COLOR_DEBUG = Color.GRAY; //new Color(51, 51, 51);
    public static final Color COLOR_EVENT = new Color(50, 50, 238);
    public static final Color COLOR_WARNING = new Color(255, 100, 100);
    public static final Color COLOR_DEFAULT = new Color(255, 0, 0);
    public static final Color COLOR_BACKGROUND_SELECTED_ROW = new Color(0, 0, 255);
    public static final Color COLOR_FOREGROUND_SELECTED_ROW = new Color(255, 255, 255);

    //save and load constant
    public static final String SIMPLE_SEPARATOR = ",";
    public static final String SAVE_PROP_CURR_DIR = "curr_dir";
    public static final String SAVE_PROP_FILE_X = "file_";
    public static final String SAVE_PROP_LAST_SELECTED_TAB = "last_selected_tab";
    public static final String SAVE_PROP_INVISIBLE_COLUMNS_IN_FILE_X = "invisible_columns_in_file_";
    public static final String SAVE_PROP_FILTER_PANEL_SCROLL_BAR_VALUE_IN_FILE_X = "filter_panel_scroll_bar_value_in_file_";
    public static final String SAVE_PROP_TABLE_SCROLL_BAR_VALUE_IN_FILE_X = "table_scroll_bar_value_in_file_";
    public static final String X_FILE_SAVE_PROP_VISIBLE_CONTEXT_Y = "_file_visible_context_";
    public static final String X_FILE_SAVE_PROP_SELECTED_CONTEXT_Y = "_file_selected_context_";
    public static final String X_FILE_SAVE_PROP_SELECTED_SOURCE_Y = "_file_selected_source_";
    public static final String X_FILE_SAVE_PROP_SELECTED_SEVERITY_Y = "_file_selected_severity_";
    public static final String X_FILE_SAVE_PROP_BOOKMARK_LIST_Y = "_file_bookmark_list_";
    public static final String SAVE_PROP_FIRST_TIME_IN_FILE_X = "first_time_in_file_";
    public static final String SAVE_PROP_LAST_TIME_IN_FILE_X = "last_time_in_file_";
    public static final String SAVE_PROP_FIND_TEXT_IN_FILE_X = "find_text_in_file_";

    public static enum EFileFormat {

        XML,
        BINARY,
        UNIDENTIFIED;
    }

    public static enum ESeverity {

        DEBUG("@ ", "Debug", 0),
        EVENT("  ", "Event", 1),
        WARNING("# ", "Warning", 2),
        ERROR("! ", "Error", 3),
        ALARM("^ ", "Alarm", 4);

        private final String shortName;
        private final String fullName;
        private final int severityAsInt;

        ESeverity(final String shortName, final String fullName, final int severityAsInt) {
            this.shortName = shortName;
            this.fullName = fullName;
            this.severityAsInt = severityAsInt;
        }

        public String getShortName() {
            return shortName;
        }

        public String getFullNameInUppercase() {
            return fullName.toUpperCase();
        }

        public String getFullNameInLowercase() {
            return fullName;
        }

        public int getSeverityAsInt() {
            return severityAsInt;
        }

        public static ESeverity getSeverity(int val) {
            for (ESeverity sev : ESeverity.values()) {
                if (sev.getSeverityAsInt() == val) {
                    return sev;
                }
            }
            throw new IllegalArgumentException(new StringBuilder(ESeverity.class.getSimpleName()).append(": Not supported value: [").append(val).append("]").toString());
        }

        public static ESeverity getSeverity(String val) {
            for (ESeverity sev : ESeverity.values()) {
                if (sev.getShortName().equals(val) || sev.getFullNameInUppercase().equals(val) || sev.getFullNameInLowercase().equals(val)) {
                    return sev;
                }
            }
            throw new IllegalArgumentException(new StringBuilder(ESeverity.class.getSimpleName()).append(": Not supported value: [").append(String.valueOf(val)).append("]").toString());
        }

        @Override
        public String toString() {
            return getFullNameInUppercase();
        }

        public static ESeverity getMaxSizeSeverity() {
            ESeverity max_size_sev = getSeverity(0);
            for (ESeverity sev : ESeverity.values()) {
                if (sev.getFullNameInUppercase().length() > max_size_sev.getFullNameInUppercase().length()) {
                    max_size_sev = sev;
                }
            }
            return max_size_sev;
        }
    }

    public static enum EEventColumn {

        SEVERITY(0, "Severity"),
        DATE(1, "Date"),
        SOURCE(2, "Source"),
        CONTEXT(3, "Context"),
        MESSAGE(4, "Message");

        private final int index;
        private final String name;

        private EEventColumn(final int index, final String name) {
            this.index = index;
            this.name = name;
        }

        public int getIndex() {
            return index;
        }

        public String getName() {
            return name;
        }

        public static String getColumnNameByIndex(int val) {
            for (EEventColumn event : EEventColumn.values()) {
                if (event.getIndex() == val) {
                    return event.getName();
                }
            }
            throw new IllegalArgumentException(new StringBuilder(EEventColumn.class.getSimpleName()).append(": Not supported value: [").append(String.valueOf(val)).append("]").toString());
        }
    }

    public static enum EIcon {

        CONTEXT_ADD(ICON_CONTEXT_ADD_RP),
        CONTEXT_REMOVE(ICON_CONTEXT_REMOVE_RP),
        PASTE_FROM_CLIPBOARD(ICON_PASTE_FROM_CLIPBOARD_RP),
        ADD_ALL(ICON_ADD_ALL_RP),
        REMOVE_ALL(ICON_REMOVE_ALL_RP),
        NEXT(ICON_NEXT),
        PREV(ICON_PREV);

        private final ImageIcon icon;

        EIcon(String path) {
            icon = new ImageIcon(getClass().getResource(TraceViewSettings.PATH_TO_RESOURCES + path));
        }

        public ImageIcon getIcon() {
            return icon;
        }
    }

    public static enum ESplitArgs {

        SEVERITY,
        SOURCE,
        CONTEXT,
        MINUTE,
        HOUR,
        DAY,
        SRC_FILE,
        SRC_FOLDER;

        public boolean isIncluded;
        public int position;

        ESplitArgs() {
            isIncluded = false;
            position = -1;
        }

        public static ESplitArgs getESplitArgs(String argsStr) {
            switch (argsStr) {
                case "":
                    return SEVERITY;
                case "source":
                    return SOURCE;
                case "context":
                    return CONTEXT;
                case "minute":
                    return MINUTE;
                case "hour":
                    return HOUR;
                case "day":
                    return DAY;
                case "srcfile":
                    return SRC_FILE;
                case "srcfolder":
                    return SRC_FOLDER;
                default:
                    throw new IllegalArgumentException("Not supported value");
            }
        }
    }
}
