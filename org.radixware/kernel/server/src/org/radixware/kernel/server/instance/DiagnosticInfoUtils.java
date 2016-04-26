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

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.text.StrSubstitutor;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.server.instance.arte.ArteInstance;
import org.radixware.kernel.starter.meta.LayerMeta;
import org.radixware.kernel.starter.radixloader.RadixLoader;

public class DiagnosticInfoUtils {

    private static String threadDumpHtmlTemplate;
    private static final String DATE_FORMAT_STR = "yyyy-MM-dd_HH-mm-ss.SSSXXX";
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat(DATE_FORMAT_STR);
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
        String snapshot = "<div class=\"snapshot\">\n"
                + "        <div class=\"snapshotTime\">${SNAPSHOT_TIME}</div>\n"
                + "        <div class=\"snapshotMenu\">\n"
                + "            <a href=\"#geninfo_${SNAPSHOT_ID}\" onclick=\"selectGenericInfo(event)\">Generic Info</a>\n"
                + "            <a href=\"#tdump_${SNAPSHOT_ID}\" onclick=\"selectThreadDumpInfo(event)\">Thread Dump</a> \n"
                + "        </div>\n"
                + "        <div class = \"diagnosticInfo\">\n"
                + "            <div class=\"genericInfo\">\n${GENERIC_INFO}\n</div> \n"
                + "            <div class=\"threadDumpInfo\">\n${THREAD_DUMP_INFO}\n</div>\n"
                + "        </div>\n"
                + "    </div>";

        HashMap<String, String> replaceMap = new HashMap<>();
        replaceMap.put("SNAPSHOT_ID", String.valueOf(System.currentTimeMillis()));
        replaceMap.put("SNAPSHOT_TIME", DATE_FORMAT.format(new Date(System.currentTimeMillis())));
        replaceMap.put("GENERIC_INFO", getGenericInfo());
        replaceMap.put("THREAD_DUMP_INFO", getThreadDumpInfo(getAllThreadsDump()));

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

        return items;
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