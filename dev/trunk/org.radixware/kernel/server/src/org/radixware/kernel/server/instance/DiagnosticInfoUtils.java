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
package org.radixware.kernel.server.instance;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrSubstitutor;
import org.apache.commons.logging.LogFactory;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.server.SrvRunParams;
import org.radixware.kernel.server.exceptions.DatabaseError;
import org.radixware.kernel.server.instance.arte.ArteInstance;
import org.radixware.kernel.server.jdbc.RadixConnection;
import org.radixware.kernel.server.jdbc.RadixResultSet;
import org.radixware.kernel.server.trace.IServerThread;
import org.radixware.kernel.starter.meta.LayerMeta;
import org.radixware.kernel.starter.radixloader.RadixLoader;
import org.radixware.kernel.starter.radixloader.RadixLoaderException;
import org.radixware.kernel.starter.radixloader.ReplacementFile;
import org.radixware.kernel.starter.utils.SystemTools;

public class DiagnosticInfoUtils {

    private static String threadDumpHtmlTemplate;
    private static final String DATE_FORMAT_STR = "yyyy-MM-dd_HH-mm-ss.SSSXXX";
    private static final String DATE_TIME_FORMAT_STR = "HH:mm:ss";
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat(DATE_FORMAT_STR);
    private static final DateFormat DATE_TIME_FORMAT = new SimpleDateFormat(DATE_TIME_FORMAT_STR);
    private static final int NUMBER_OF_SNAPSHOTS_IN_SELECTOR_ROW = 20;

    static {
        try {
            Class cl = Class.forName(DiagnosticInfoUtils.class.getCanonicalName());
            try (java.util.Scanner sc = new java.util.Scanner(cl.getResourceAsStream("thread_dump_template.html")).useDelimiter("\\A")) {
                threadDumpHtmlTemplate = sc.hasNext() ? sc.next() : "";
            }
        } catch (ClassNotFoundException ex) {
            Instance.get().getTrace().put(EEventSeverity.WARNING, ex.getLocalizedMessage(), EEventSource.INSTANCE);
            threadDumpHtmlTemplate = "Error during processing request.";
        }
    }

    public static String formatDiagnosticInfoAsHtmlPage(final int snapshotCnt, final String snapshotsAsStr) {
        HashMap<String, String> replaceMap = new HashMap<>();
        replaceMap.put("INSTANCE_NAME", Instance.get().getFullTitle());
        replaceMap.put("SNAPSHOT_SELECTOR", getSnapshotSelector(snapshotCnt));
        replaceMap.put("SNAPSHOT_LIST", snapshotsAsStr);
        StrSubstitutor sub = new StrSubstitutor(replaceMap);

        return sub.replace(threadDumpHtmlTemplate);
    }

    private static String getSnapshotSelector(final int snapshotCnt) {
        boolean isRowTagOpened = false;

        StringBuilder snapshotSelector = new StringBuilder();
        for (int snapshotNum = 1; snapshotNum <= snapshotCnt; snapshotNum++) {
            if (snapshotNum % NUMBER_OF_SNAPSHOTS_IN_SELECTOR_ROW == 1) {
                snapshotSelector.append("<tr>\n");
                if (snapshotNum == 1) {
                    snapshotSelector.append("<td>Snapshots:</td>");
                } else {
                    snapshotSelector.append("<td></td>");
                }
                isRowTagOpened = true;
            }

            snapshotSelector.append("<td>\n");

            snapshotSelector.append("<input type=\"radio\" name=\"snapshotBtn\" value=\"").
                    append(snapshotNum).append("\" id=\"snapshotBtn").
                    append(snapshotNum).append("\" onchange=\"snapshotSelect(event)\"/>");
            snapshotSelector.append("\n");
            snapshotSelector.append("<label for=\"snapshotBtn").append(snapshotNum).append("\">#").append(snapshotNum).append("</label>");
            snapshotSelector.append("\n");

            snapshotSelector.append("</td>\n");

            if (snapshotNum % NUMBER_OF_SNAPSHOTS_IN_SELECTOR_ROW == 0) {
                snapshotSelector.append("</tr>\n");
                isRowTagOpened = false;
            }
        }
        if (isRowTagOpened) {
            snapshotSelector.append("</tr>\n");
        }

        return snapshotSelector.toString();
    }

    public static String getArteInstSnapshot(ArteInstance arteInst) {
        String snapshot = "<div class=\"snapshot\">\n"
                + "        <div class=\"snapshotTime\">${SNAPSHOT_TIME}</div>\n"
                + "        <div class = \"diagnosticInfo\">\n"
                + "            <div class=\"threadDumpInfo\" style=\"display : block\">\n${THREAD_DUMP_INFO}\n</div>\n" //Style attribute
                + "        </div>\n"
                + "    </div>";

        HashMap<String, String> replaceMap = new HashMap<>();
        replaceMap.put("SNAPSHOT_TIME", DATE_FORMAT.format(new Date(System.currentTimeMillis())));
        replaceMap.put("THREAD_DUMP_INFO", getThreadDumpInfo(getArteInstDump(arteInst)));

        StrSubstitutor sub = new StrSubstitutor(replaceMap);

        return sub.replace(snapshot);
    }

    public static String getDiagnosticSnapshot() {
        return getDiagnosticSnapshot(false, 0);
    }

    public static String getDiagnosticSnapshot(boolean instanceStateHistoryRequired, long instanceStateHistoryStartTimeMillis) {
        String snapshot = "<div class=\"snapshot\">\n"
                + "        <div class=\"snapshotTime\">${SNAPSHOT_TIME}</div>\n"
                + "        <div class=\"snapshotMenu\">\n"
                + "            <a href=\"#geninfo_${SNAPSHOT_ID}\" onclick=\"selectGenericInfo(event)\">Generic Info</a>\n"
                + "            <a href=\"#tdump_${SNAPSHOT_ID}\" onclick=\"selectThreadDumpInfo(event)\">Thread Dump</a> \n"
                + (instanceStateHistoryRequired ? "            <a href=\"#instStateHist\" onclick=\"selectInstanceStateHistoryInfo(event)\">Threads State History</a> \n" : "")
                + "        </div>\n"
                + "        <div class = \"diagnosticInfo\">\n"
                + "            <div class=\"genericInfo\">\n${GENERIC_INFO}\n</div> \n"
                + "            <div class=\"threadDumpInfo\">\n${THREAD_DUMP_INFO}\n</div>\n"
                + (instanceStateHistoryRequired ? "            <div class=\"instanceStateHistory\">\n${INSTANCE_STATE_HISTORY}\n</div>\n" : "")
                + "        </div>\n"
                + "    </div>";

        HashMap<String, String> replaceMap = new HashMap<>();
        replaceMap.put("SNAPSHOT_ID", String.valueOf(System.currentTimeMillis()));
        replaceMap.put("SNAPSHOT_TIME", DATE_FORMAT.format(new Date(System.currentTimeMillis())));
        replaceMap.put("GENERIC_INFO", getGenericInfo());
        replaceMap.put("THREAD_DUMP_INFO", getThreadDumpInfo(getAllThreadsDump()));
        if (instanceStateHistoryRequired) {
            replaceMap.put("INSTANCE_STATE_HISTORY", getInstanceStateHistory(Instance.get().getId(), instanceStateHistoryStartTimeMillis));
        }

        StrSubstitutor sub = new StrSubstitutor(replaceMap);

        return sub.replace(snapshot);
    }

    private static String getGenericInfo() {
        StringBuilder genericInfo = new StringBuilder("<table class=\"genericInfoTable\">\n");
        genericInfo.append("<col class=\"genericInfoColNames\"/>\n").append("<col span=\"1\"/>\n");

        List<DiagnosticInfoItem> items = getDiagnosticInfoItems();
        for (DiagnosticInfoItem item : items) {
            genericInfo.append("<tr>\n");
            genericInfo.append("<td class=\"wrapText\">").append(item.getName()).append("</td>\n");
            if (item.isWrapText()) {
                genericInfo.append("<td class=\"wrapText\">");
            } else {
                genericInfo.append("<td class=\"noWrapText\">");
            }
            genericInfo.append(item.getValue()).append("</td>\n");
            genericInfo.append("</tr>\n");
        }

        genericInfo.append("</table>\n");

        return genericInfo.toString();
    }

    private static List<DiagnosticInfoItem> getDiagnosticInfoItems() {
        List<DiagnosticInfoItem> items = new ArrayList<>();

        //Information about layers versions.
        List<LayerMeta> layers = RadixLoader.getInstance().getCurrentRevisionMeta().getAllLayersSortedFromBottom();
        StringBuilder layersInfo = new StringBuilder();
        for (LayerMeta layer : layers) {
            layersInfo.append(layer.getUri()).append(" = ").append(layer.getReleaseNumber()).append("<br/>");
        }
        items.add(new DiagnosticInfoItem("Layers:", layersInfo.toString()));
        items.add(new DiagnosticInfoItem("Host Network Interfaces:", getHostNetworkInterfacesStr(), false));
        items.add(new DiagnosticInfoItem("Loader:", RadixLoader.getInstance().getDescription()));
        String latestRevision;
        try {
            latestRevision = String.valueOf(RadixLoader.getInstance().getCurrentRevision());
        } catch (Exception ex) {
            latestRevision = ExceptionTextFormatter.throwableToString(ex);
        }
        items.add(new DiagnosticInfoItem("Latest revision:", latestRevision));
        items.add(new DiagnosticInfoItem("Starter temp dir:", SystemTools.getTmpDir().getAbsolutePath()));
        
        addReplacementsInfo(items);
        
        items.add(new DiagnosticInfoItem("License server address:", SrvRunParams.getLicenseServerAddress()));
        items.add(new DiagnosticInfoItem("License set:", String.valueOf(Instance.get().getLicenseSetAsStr()).replace(";", ";<br>"), false));
        items.add(new DiagnosticInfoItem("Load history:", getLoadHistoryStr(), false));

        items.add(new DiagnosticInfoItem("DDS Options:", getDdsOptionStr()));
        items.add(new DiagnosticInfoItem("DDS Version:", getDdsVersionStr()));

        items.add(new DiagnosticInfoItem("Trace profiles:", "<pre>" + getTraceProfileInfos() + "</pre>", false));
        items.add(new DiagnosticInfoItem("System properties:", "<pre>" + getSystemProperties() + "</pre>", false));
        items.add(new DiagnosticInfoItem("Resources:", "<pre>" + ResourceRegistry.resourcesAsStr(Instance.get().getResourceRegistry().getItems()) + "</pre>", false));
        
        return items;
    }
    
    private static void addReplacementsInfo(List<DiagnosticInfoItem> items) {
        String replacements;
        final List<ReplacementFile> allReplacements = RadixLoader.getInstance().getLocalFiles().getAllReplacementsEx();
        if (!allReplacements.isEmpty()) {
            try {
                final long curRevNum = RadixLoader.getInstance().getCurrentRevision();
                final List<ReplacementFile> usedReplacements = RadixLoader.getInstance().getLocalFiles().getUsedReplacements(curRevNum);
                StringBuilder sb = new StringBuilder();
                for (ReplacementFile repFile : usedReplacements) {
                    sb.append(repFile.getFullInfo(true));
                }
                for (ReplacementFile repFile : allReplacements) {
                    if (usedReplacements.contains(repFile)) {
                        continue;
                    }
                    sb.append("<font color=\"gray\">").append(repFile.getFullInfo(false)).append("</font>");
                }
                replacements = sb.toString();
            } catch (RadixLoaderException ex) {
                replacements = ex.getClass().toString() + ": " + ex.getMessage();
            }
        } else {
            replacements = "NONE";
        }

        items.add(new DiagnosticInfoItem("Replacements:", "<pre>" + replacements + "</pre>", false));
    }
    
    private static String getTraceProfileInfos() {
        final StringBuilder sb = new StringBuilder();
        final List<TraceProfileInfo> infos = new ArrayList<>(Instance.get().getTraceProfileInfos());
        Collections.sort(infos, new Comparator<TraceProfileInfo>() {

            @Override
            public int compare(TraceProfileInfo o1, TraceProfileInfo o2) {
                int profiles = o1.getProfile().compareTo(o2.getProfile());
                if (profiles == 0) {
                    return Long.compare(o2.getTouchMillis(), o1.getTouchMillis());
                }
                return profiles;
            }

        });
        String lastProfile = null;
        int lastProfileCnt = 0;
        final int printedCount = 3;
        for (TraceProfileInfo info : infos) {
            if (!Objects.equals(lastProfile, info.getProfile())) {
                if (lastProfile != null) {
                    sb.append("\n");
                }
                lastProfile = info.getProfile();
                lastProfileCnt = 0;
                sb.append("\"" + info.getProfile() + "\" from " + info.getDescription());
            }
            lastProfileCnt++;
            if (lastProfileCnt > 1) {
                if (lastProfileCnt <= printedCount) {
                    sb.append(", " + info.getDescription());
                }
                if (lastProfileCnt == printedCount + 1) {
                    sb.append(", ...");
                }
            }
        }
        return sb.toString();
    }

    private static String getSystemProperties() {
        final StringWriter sw = new StringWriter(2048);
        System.getProperties().list(new PrintWriter(sw));
        return sw.toString();
    }

    private static String getDdsOptionStr() {
        StringBuilder result = new StringBuilder("<table border=\"1\" cellpadding=\"5\" style=\"border-collapse: collapse; border: 1px solid black;\">\n");

        Connection connectDB = Instance.get().getDbConnection();
        try {
            if (connectDB != null) {
                Statement stat = Instance.get().getDbConnection().createStatement();
                //int countColumnsDdsOption = stat.executeQuery("select count(*) from user_tab_columns where table_name = 'RDX_DDSOPTION'").getInt(1);
                ResultSet resultDdsOption = stat.executeQuery("Select * from RDX_DDSOPTION");
                ResultSetMetaData metaDate = resultDdsOption.getMetaData();

                result.append("<tr>");
                for (int i = 1; i <= metaDate.getColumnCount(); i++) {
                    result.append("<td>").append(metaDate.getColumnName(i)).append("</td>\n");
                }
                result.append("</tr>\n");

                while (resultDdsOption.next()) {
                    result.append("<tr>");
                    for (int i = 1; i <= metaDate.getColumnCount(); i++) {
                        result.append("<td>").append(resultDdsOption.getObject(i) == null ? "null" : resultDdsOption.getObject(i).toString()).append("</td>\n");
                    }
                    result.append("</tr>\n");
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DiagnosticInfoUtils.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result.append("</table>\n").toString();
    }

    private static String getDdsVersionStr() {
        StringBuilder result = new StringBuilder("<table border=\"1\" cellpadding=\"5\" style=\"border-collapse: collapse; border: 1px solid black;\">\n");

        Connection connectDB = Instance.get().getDbConnection();
        try {
            if (connectDB != null) {
                Statement stat = Instance.get().getDbConnection().createStatement();
                //int countColumnsDdsOption = stat.executeQuery("select count(*) from user_tab_columns where table_name = 'RDX_DDSOPTION'").getInt(1);
                ResultSet resultDdsVersion = stat.executeQuery("Select * from RDX_DDSVERSION");
                ResultSetMetaData metaDate = resultDdsVersion.getMetaData();

                result.append("<tr>");
                for (int i = 1; i <= metaDate.getColumnCount(); i++) {
                    result.append("<td>").append(metaDate.getColumnName(i)).append("</td>\n");
                }
                result.append("</tr>\n");

                while (resultDdsVersion.next()) {
                    result.append("<tr>");
                    for (int i = 1; i <= metaDate.getColumnCount(); i++) {
                        result.append("<td>").append(resultDdsVersion.getObject(i) == null ? "null" : resultDdsVersion.getObject(i).toString()).append("</td>\n");
                    }
                    result.append("</tr>\n");
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DiagnosticInfoUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result.append("</table>\n").toString();
    }

    private static String getLoadHistoryStr() {
        final Instance inst = Instance.get();
        if (inst != null) {
            final InstanceLoadHistory loadHistory = inst.getLoadHistory();
            if (loadHistory != null) {
                final List<InstanceLoadHistory.LoadHistoryItem> historyItems = loadHistory.getItems();
                double[][] data = new double[4][historyItems.size()];
                int idx = 0;
                for (InstanceLoadHistory.LoadHistoryItem item : historyItems) {
                    data[0][idx] = item.getInstCpuLoadPercent();
                    data[1][idx] = item.getHostCpuLoadPercent();
                    data[2][idx] = item.getHeapUsagePercent();
                    data[3][idx] = item.getAvgActiveArteCount();
                    item.getTimestampMillis();
                    idx++;
                }

                final int grains = 10;
                final StringBuilder sb = new StringBuilder();
                sb.append("<pre>");
                sb.append("Instance CPU Usage Percent:\n");
                sb.append(getAsciiGraph(data[0], 100, grains, historyItems.get(0).getTimestampMillis(), historyItems.get(historyItems.size() - 1).getTimestampMillis()));
                sb.append("Host CPU Usage Percent:\n");
                sb.append(getAsciiGraph(data[1], 100, grains, historyItems.get(0).getTimestampMillis(), historyItems.get(historyItems.size() - 1).getTimestampMillis()));
                sb.append("Heap Usage Percent:\n");
                sb.append(getAsciiGraph(data[2], 100, grains, historyItems.get(0).getTimestampMillis(), historyItems.get(historyItems.size() - 1).getTimestampMillis()));
                int maxArteUsageCount = inst.getArtePool().getSize();
                for (int i = 0; i < data[3].length; i++) {
                    maxArteUsageCount = Math.max(maxArteUsageCount, (int) (data[3][i]));
                }
                sb.append("Avg ARTE Usage Count (full load means " + maxArteUsageCount + "):\n");
                sb.append(getAsciiGraph(data[3], maxArteUsageCount, grains, historyItems.get(0).getTimestampMillis(), historyItems.get(historyItems.size() - 1).getTimestampMillis()));
                sb.append("</pre>");
                return sb.toString();
            }
        }
        return "";
    }

    private double[] toDoubleArr(final int[] arr) {
        if (arr == null) {
            return null;
        }
        double[] dArr = new double[arr.length];
        for (int i = 0; i < arr.length; i++) {
            dArr[i] = arr[i];
        }
        return dArr;
    }

    private static String getAsciiGraph(final double[] data, double max, int grains, long timesTampMillisBeg, long timesTampMillisEnd) {
        char[][] field = new char[grains + 1][data.length + 1];
        for (int i = 0; i < field.length; i++) {
            Arrays.fill(field[i], ' ');
        }
        for (int i = 0; i < grains; i++) {
            field[i][0] = '|';
            for (int j = 0; j < data.length; j++) {
                field[grains][j + 1] = '-';
                final boolean fill = max == 0 ? false : ((int) Math.floor(data[j] / max * grains + 0.5)) > i;
                field[grains - i][j + 1] = fill ? 'x' : ' ';
            }
        }
        final StringBuilder sb = new StringBuilder();
        for (char[] field1 : field) {
            sb.append(new String(field1));
            sb.append("\n");
        }

        sb.append(DATE_TIME_FORMAT.format(new Date(timesTampMillisBeg)));

        if ((data.length + 1) - DATE_TIME_FORMAT.format(new Date(timesTampMillisBeg)).length() * 2 > 3) {
            for (int i = 0; i < (data.length + 1) - DATE_TIME_FORMAT.format(new Date(timesTampMillisBeg)).length() * 2; i++) {
                sb.append(" ");
            }
        } else {
            sb.append("   ");
        }

        sb.append(DATE_TIME_FORMAT.format(new Date(timesTampMillisEnd)));
        return sb.append("\n\n").toString();
    }

    private static String getHostNetworkInterfacesStr() {
        try {
            final StringBuilder sb = new StringBuilder();
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                final NetworkInterface iface = en.nextElement();
                sb.append(iface.getDisplayName());
                sb.append(" - ");
                for (InterfaceAddress ifaceAddr : iface.getInterfaceAddresses()) {
                    sb.append("'").append(ifaceAddr.getAddress()).append("'; ");
                }
                sb.append("<br/>");
            }
            return sb.toString();
        } catch (SocketException ex) {
            return "Error on retrieving: " + ex.getMessage();
        }
    }

    private static class DiagnosticInfoItem {

        private final String name;
        private final String value;
        private final boolean wrapText;

        public DiagnosticInfoItem(String name, String value, boolean wrapText) {
            this.name = name;
            this.value = value;
            this.wrapText = wrapText;
        }

        public DiagnosticInfoItem(String name, String value) {
            this.name = name;
            this.value = value;
            wrapText = true;
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }

        public boolean isWrapText() {
            return wrapText;
        }
    }

    private static String getThreadDumpInfo(String threadDump) {
        String tInfo = "                 <ul class=\"legend\">\n"
                + "                     <li class=\"NEW\"> - NEW;</li>\n"
                + "                     <li class=\"RUNNABLE\"> - RUNNABLE;</li>\n"
                + "                     <li class=\"BLOCKED\"> - BLOCKED;</li>\n"
                + "                     <li class=\"WAITING\"> - WAITING;</li>\n"
                + "                     <li class=\"TIMED_WAITING\"> - TIMED_WAITING;</li>\n"
                + "                     <li class=\"TERMINATED\"> - TERMINATED.</li>\n"
                + "                 </ul>\n"
                + "                 <a href=\"#expandAll_${SNAPSHOT_ID}\" onclick=\"expandAll(event)\">Expand All</a>\n"
                + "                 <a href=\"#collapseAll_${SNAPSHOT_ID}\" onclick=\"collapseAll(event)\">Collapse All</a> \n"
                + "                 <ul>THREAD_DUMP</ul>\n";

        return tInfo.replace("THREAD_DUMP", threadDump);
    }

    public static List<InstanceThreadStateRecord> getInstanceStateHistoryRecords(final RadixConnection dbConnection, final int instanceId, long periodStartTimeMillis) {
        final List<InstanceThreadStateRecord> result = new ArrayList<>();

        try {
            try (final PreparedStatement stmtReadInstStateHistory = dbConnection.prepareStatement("select * from RDX_SM_InstanceStateView where instanceId = ? and regTimeMillis > ? order by regTimeMillis, threadId")) {
                stmtReadInstStateHistory.setInt(1, instanceId);
                stmtReadInstStateHistory.setLong(2, periodStartTimeMillis);

                RadixResultSet rs = null;
                try {
                    rs = (RadixResultSet) stmtReadInstStateHistory.executeQuery();
                    while (rs.next()) {
                        final InstanceThreadStateRecord record = InstanceThreadStateRecord.createRecord(rs);
                        result.add(record);
                    }
                } catch (SQLException e) {
                    throw new DatabaseError("Can't read ThreadStateRecord: " + ExceptionTextFormatter.getExceptionMess(e), e);
                } finally {
                    if (rs != null) {
                        rs.close();
                    }
                }
            }
        } catch (Exception ex) {
            LogFactory.getLog(DiagnosticInfoUtils.class).warn("Can't close ResultSet", ex);
        }

        return result;
    }

    private static String getInstanceStateHistory(int instanceId, long periodStartTimeMillis) {
        final StringBuilder sb = new StringBuilder();
        try {
            final List<InstanceThreadStateRecord> records = getInstanceStateHistoryRecords((RadixConnection) ((IServerThread) Thread.currentThread()).getConnection(), instanceId, periodStartTimeMillis);

            sb.append("<table>");
            {
                sb.append("<tr>");
                {
                    String[] colNames = new String[]{"Time", "Forced", "Thread Kind", "Id", "Name", "Stack", "Ancestor Id", "Unit Id", "ARTE Seq", "ARTE Serial", "DB Sid", "DB Serial", "Trace Contexts", "CPU Diff (ms)", "DB Diff (ms)", "Ext Diff (ms)", "Queue Diff (ms)", "Uptime (s)", "Rq Start Time", "Lock Name", "Lock Owner Name"};
                    for (String colName : colNames) {
                        sb.append("<th>").append(colName).append("</th>");
                    }
                }
                sb.append("</tr>");

                for (InstanceThreadStateRecord record : records) {
                    sb.append(getInstanceStateRecordAsHtml(record));
                }
            }
            sb.append("</table>");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return sb.toString();
    }

    private static String getThreadStackAsHtml(String rawStack) {
        final char nline = '\n';
        final String stackTop = rawStack.indexOf(nline) == -1 ? rawStack : rawStack.substring(0, rawStack.indexOf(nline));

        StringBuilder sb = new StringBuilder();

        sb.append(nline);
        sb.append("<span class=\"closed\">\n");
        sb.append("<a href=\"#recordInfo_").append("id").append("\" onclick=\"toggleThreadDetails(event)\">");
        sb.append(stackTop);
        sb.append("</a>");
        sb.append(nline);

        sb.append("<span class=\"threadDetails\">");
        sb.append(nline);
        sb.append("<code>");
        sb.append(nline);

        sb.append(rawStack.replace("\n", "<br/>"));
        sb.append(nline);
        sb.append("</code>");
        sb.append(nline);

        sb.append("</span>");
        sb.append(nline);
        sb.append("</span>");
        sb.append(nline);

        return sb.toString();
    }

    private static String getInstanceStateRecordAsHtml(InstanceThreadStateRecord record) {
        final char nline = '\n';
        StringBuilder sb = new StringBuilder();
        final String[] columnsValues = new String[]{
            DATE_FORMAT.format(new Date(record.regTimeMillis)),
            Objects.toString(record.forced, ""),
            record.threadKind == null ? "" : record.threadKind.getValue(),
            "" + record.threadId,
            "" + record.name,
            StringUtils.isEmpty(record.stack) ? "" : getThreadStackAsHtml(record.stack),
            Objects.toString(record.ancestorThreadId, ""),
            Objects.toString(record.unitId, ""),
            Objects.toString(record.arteSeq, ""),
            Objects.toString(record.arteSerial, ""),
            Objects.toString(record.dbSid, ""),
            Objects.toString(record.dbSerial, ""),
            Objects.toString(record.traceContexts, ""),
            Objects.toString(record.cpuDiffMillis, ""),
            Objects.toString(record.dbDiffMillis, ""),
            Objects.toString(record.extDiffMillis, ""),
            Objects.toString(record.queueDiffMillis, ""),
            Objects.toString(record.uptimeSec, ""),
            record.rqStartTimeMillis == null ? "" : DATE_FORMAT.format(new Date(record.rqStartTimeMillis)),
            Objects.toString(record.lockName, ""),
            Objects.toString(record.lockOwnerName, "")
        };

        sb.append("<tr>").append(nline);
        for (String columnsValue : columnsValues) {
            sb.append("<td>").append(columnsValue).append("</td>").append(nline);
        }
        sb.append("</tr>").append(nline);

        return sb.toString();
    }

    private static String getAllThreadsDump() {
        try {
            final ThreadMXBean bean = ManagementFactory.getThreadMXBean();
            if (bean != null) {
                final ThreadInfo[] threadInfos = bean.dumpAllThreads(true, true);
                Arrays.sort(threadInfos, new Comparator<ThreadInfo>() {
                    @Override
                    public int compare(ThreadInfo o1, ThreadInfo o2) {
                        return o1.getThreadName().compareTo(o2.getThreadName());
                    }
                });

                return getAllThreadInfosAsHtmlStr(threadInfos);
            }
            return "Management bean is null.";
        } catch (Exception ex) {
            final String message = "Error on writing debug thread dump: " + ExceptionTextFormatter.throwableToString(ex);
            return message;
        }
    }

    private static String getArteInstDump(ArteInstance arteInst) {
        try {
            final ThreadMXBean bean = ManagementFactory.getThreadMXBean();
            if (bean != null) {
                final ThreadInfo[] threadInfos = bean.getThreadInfo(new long[]{arteInst.getThread().getId()}, true, true);
                return getAllThreadInfosAsHtmlStr(threadInfos);
            }
            return "Management bean is null.";
        } catch (Exception ex) {
            final String message = "Error on writing debug thread dump: " + ExceptionTextFormatter.throwableToString(ex);
            return message;
        }
    }

    private static String getAllThreadInfosAsHtmlStr(ThreadInfo[] threadInfos) {
        if (threadInfos == null) {
            return "";
        }

        StringBuilder threadDumpSb = new StringBuilder();
        for (ThreadInfo info : threadInfos) {
            threadDumpSb.append(getThreadInfoAsHtmlStr(info));
        }

        return threadDumpSb.toString();
    }

    private static String getThreadInfoAsHtmlStr(ThreadInfo info) {
        if (info == null) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        final char nline = '\n';

        sb.append("<li class=\"");
        sb.append(info.getThreadState());
        sb.append("\">");

        sb.append(nline);
        sb.append("<span class=\"closed\">\n");
        sb.append("<a href=\"#info_").append(info.getThreadId()).append("\" onclick=\"toggleThreadDetails(event)\">");
        sb.append("'");
        sb.append(info.getThreadName());
        sb.append("' - ");
        sb.append("Thread #");
        sb.append(info.getThreadId());
        sb.append("</a>");
        sb.append(nline);

        sb.append("<span class=\"threadDetails\">");
        sb.append(nline);
        sb.append("<p>State: ");
        sb.append(info.getThreadState());

        if (info.getLockName() != null) {
            sb.append(". Waiting for " + info.getLockName() + (info.getLockOwnerName() != null ? " owned by " + info.getLockOwnerName() : ""));
        }
        sb.append("</p>");
        sb.append(nline);

        sb.append("<code>");
        sb.append(nline);
        if (info.getStackTrace() != null) {
            for (StackTraceElement ste : info.getStackTrace()) {
                sb.append(ste.toString()).append("<br/>");
                sb.append(nline);
            }
            sb.append("<br/>");
        }
        sb.append(nline);
        sb.append("</code>");
        sb.append(nline);

        sb.append("</span>");
        sb.append(nline);
        sb.append("</span>");
        sb.append(nline);
        sb.append("</li>");
        sb.append(nline);

        return sb.toString();
    }
}
