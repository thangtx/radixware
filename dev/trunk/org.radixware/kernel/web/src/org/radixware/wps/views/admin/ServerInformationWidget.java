/*
 * Copyright (c) 2008-2016, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.wps.views.admin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.html.Div;
import org.radixware.kernel.common.html.Html;
import org.radixware.kernel.common.html.Table;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.common.utils.VersionNumber;
import org.radixware.kernel.starter.meta.RevisionMeta;
import org.radixware.kernel.starter.radixloader.RadixDirLoader;
import org.radixware.kernel.starter.radixloader.RadixLoader;
import org.radixware.kernel.starter.radixloader.RadixLoaderException;
import org.radixware.kernel.starter.radixloader.RadixSVNLoader;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.rwt.AbstractContainer;
import org.radixware.wps.rwt.UIObject;
import org.radixware.wps.rwt.VerticalBox;

public class ServerInformationWidget extends AbstractContainer {

    private static void addSubHeader(final Table versionsTable, final String subHeader) {
        final Table.Row row = versionsTable.addRow();
        final Table.DataCell cell = createCell(row, subHeader);
        cell.setAttr("colspan", "2");
        cell.setAttr("align", "center");
        cell.setCss("font-weight", "bold");
    }

    private static LinkedHashMap<String, String> parseVersionString(final RevisionMeta revision, final String versionString) {
        final Map<String, VersionNumber> versionByUri = Utils.parseVersionsByKey(versionString);
        final List<String> layers = revision.getAllLayerUrisSortedFromBotton();
        final LinkedHashMap<String, String> result = new LinkedHashMap<>();
        VersionNumber version;
        for (String layerUri : layers) {
            version = versionByUri.get(layerUri);
            result.put(layerUri, version == null ? "" : version.toString());
        }
        return result;
    }

    private static int writeVersionsToTable(final Table versionsTable, final Map<String, String> versions) {
        for (Map.Entry<String, String> entry : versions.entrySet()) {
            final Table.Row row = versionsTable.addRow();
            setupTableBorder(row);
            createCell(row, entry.getKey());
            createCell(row, entry.getValue()).setAttr("align", "center");
        }
        return versions.size();
    }

    private static Html createHtmlHeader(final String title) {
        final Html paragraph = new Html("p");
        paragraph.setCss("align", "center");
        final Html header = new Html("h3");
        header.setInnerText(title);
        paragraph.add(header);
        return paragraph;
    }

    private static Table.DataCell createCell(final Table.Row row, final String text) {
        final Table.DataCell cell = row.addCell(text);
        setupTableBorder(cell);
        return cell;
    }

    private static Table.HeaderCell createHeaderCell(final Table.Header header, final String text) {
        final Table.HeaderCell cell = header.addCell(text);
        setupTableBorder(cell);
        return cell;
    }

    private static void setupTableBorder(final Html tablePart) {
        tablePart.setCss("border", "1px solid black");
        tablePart.addClass("rwt-ui-element-with-border");
    }

    private final WpsEnvironment env;
    
    public ServerInformationWidget(WpsEnvironment env) {
        super();
        this.env = env;
        setupUI();
    }

    private void setupUI() {
        VerticalBox vb = new VerticalBox();
        final UIObject content = new UIObject(createContent(env.getMessageProvider())) {
        };
        vb.add(content);
        add(vb);
    }

    private Html createContent(final MessageProvider mp) {
        final Html content = new Div();
        content.setCss("float", "left");
        content.setCss("padding-left", "10px");
        appendProductVersionsInfo(content, mp);
        appendStarterInfo(content, mp);
        writeSystemInformation(content, mp);
        writeServerInfo(content, mp);
        return content;
    }

    private int appendProductVersionsInfo(final Html content, final MessageProvider mp) {
        content.add(createHtmlHeader(mp.translate("About", "Version Information")));
        final Table versionsTable = new Table();
        setupTableBorder(versionsTable);
        final Table.Header header = versionsTable.getHeader();
        setupTableBorder(header);
        createHeaderCell(header, mp.translate("About", "Layer Name")).setCss("width", "70%");
        createHeaderCell(header, mp.translate("About", "Version")).setCss("width", "30%");
        final RevisionMeta revisionMeta = RadixLoader.getInstance().getCurrentRevisionMeta();
        final String kernelVersionString = revisionMeta.getKernelLayerVersionsString();
        final String appVersionString = revisionMeta.getAppLayerVersionsString();
        int rowsCount;
        if (kernelVersionString.equals(appVersionString)) {
            rowsCount = writeVersionsToTable(versionsTable, parseVersionString(revisionMeta, appVersionString));
        } else {
            addSubHeader(versionsTable, mp.translate("About", "Application"));
            rowsCount = writeVersionsToTable(versionsTable, parseVersionString(revisionMeta, appVersionString));
            addSubHeader(versionsTable, mp.translate("About", "Kernel"));
            rowsCount += writeVersionsToTable(versionsTable, parseVersionString(revisionMeta, kernelVersionString));
        }
        content.add(versionsTable);
        return rowsCount;
    }
    
    private void appendStarterInfo(Html content, MessageProvider mp) {
        final RadixLoader rxLoader = RadixLoader.getInstance();
        StringBuilder htmlSb = new StringBuilder();
        htmlSb.append("<p align=\"center\"><h3>");
        htmlSb.append(mp.translate("About", "Starter Information"));
        htmlSb.append("</h3></p>");

        htmlSb.append("<table border=\"0\"><tr><td>");
        htmlSb.append(mp.translate("About", "Revision number: "));
        try {
            htmlSb.append(String.valueOf(rxLoader.getCurrentRevision()));
        } catch (RadixLoaderException exception) {
            env.getTracer().error(exception);
            htmlSb.append(mp.translate("About", "unknown"));
        }
        htmlSb.append("</td></tr>");
        if (rxLoader instanceof RadixSVNLoader) {
            htmlSb.append("<tr><td>");
            htmlSb.append(mp.translate("About", "Directory for cache:"));
            htmlSb.append("</td></tr><tr><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
            htmlSb.append(rxLoader.getRoot());
            htmlSb.append("</td></tr>");
        } else if (rxLoader instanceof RadixDirLoader) {
            htmlSb.append("<tr><td>");
            htmlSb.append(mp.translate("About", "Working directory:"));
            htmlSb.append("</td></tr><tr><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
            htmlSb.append(rxLoader.getRoot().getAbsolutePath());
            htmlSb.append("</td></tr>");
        }
        htmlSb.append("<tr><td>");
        htmlSb.append(mp.translate("About", "Directory for temporary files:"));
        htmlSb.append("</td></tr><tr><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
        htmlSb.append(org.radixware.kernel.starter.utils.SystemTools.getTmpDir());
        htmlSb.append("</td></tr>");
        if (rxLoader instanceof RadixSVNLoader) {
            htmlSb.append("<tr><td>");
            htmlSb.append(mp.translate("About", "Subversion URL list:"));
            htmlSb.append("</td></tr>");
            final List<String> urls = ((RadixSVNLoader) rxLoader).getSvnUrls();
            for (String url : urls) {
                htmlSb.append("<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
                htmlSb.append(url);
                htmlSb.append("</td></tr>");
            }
        }
        htmlSb.append("</table>");
        List<Html> htmlList = Html.parseFromStr(htmlSb.toString());
        for (Html htmlList1 : htmlList) {
            content.add(htmlList1);
        }
    }

    private void writeSystemInformation(Html content, MessageProvider mp) {
        StringBuilder htmlSb = new StringBuilder();
        htmlSb.append("<p align=\"center\"><h3>");
        htmlSb.append(mp.translate("About", "System Information"));
        htmlSb.append("</h3></p>");

        htmlSb.append("<table border=\"0\">");
        htmlSb.append("<tr><td>Java:</td>");
        htmlSb.append("<td align=\"left\" style=\"white-space: nowrap\">");
        htmlSb.append(System.getProperty("java.runtime.version"));
        htmlSb.append("</td></tr>");
        htmlSb.append("<tr><td>Java VM:</td>");
        htmlSb.append("<td align=\"left\" style=\"white-space: nowrap\">");
        htmlSb.append(String.format("%s %s ",
                System.getProperty("java.vm.name"),
                System.getProperty("java.vm.version")));
        htmlSb.append("</td></tr>");
        htmlSb.append("<tr><td>");
        htmlSb.append(mp.translate("About", "System:"));
        htmlSb.append("&nbsp;</td>");
        htmlSb.append("<td align=\"left\" style=\"white-space: nowrap;\">");
        final String osVersion = System.getProperty("os.version").replace("-", "\u2011");
        htmlSb.append(String.format(mp.translate("About", "%s %s running on %s; %s; %s_%s"),
                System.getProperty("os.name"), osVersion, System.getProperty("os.arch"),
                System.getProperty("file.encoding"), System.getProperty("user.language"), System.getProperty("user.country")));
        htmlSb.append("</td></tr>");
        htmlSb.append("</table>");
        List<Html> htmlList = Html.parseFromStr(htmlSb.toString());
        for (Html htmlList1 : htmlList) {
            content.add(htmlList1);
        }
    }

    private void writeServerInfo(Html content, MessageProvider mp) {
        String serverInfo = null;
        try {
            Class<?> testClass = Class.forName("org.apache.catalina.util.ServerInfo");
            try {
                Method method = testClass.getMethod("getServerInfo");
                serverInfo = (String) method.invoke(null);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException | NoSuchMethodException ex) {
                Logger.getLogger(ServerInformationWidget.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServerInformationWidget.class.getName()).log(Level.SEVERE, null, ex);
        }
        StringBuilder htmlSb = new StringBuilder();
        htmlSb.append("<p align=\"center\"><h3>");
        htmlSb.append(mp.translate("About", "Tomcat Information"));
        htmlSb.append("</h3></p>");

        htmlSb.append("<table border=\"0\">");
        htmlSb.append("<tr><td>Catalina Base:</td>");
        htmlSb.append("<td align=\"left\" style=\"white-space: nowrap\">");
        htmlSb.append(System.getProperty("catalina.base"));
        htmlSb.append("</td></tr>");
        htmlSb.append("<tr><td>Catalina Home:</td>");
        htmlSb.append("<td align=\"left\" style=\"white-space: nowrap\">");
        htmlSb.append(System.getProperty("catalina.home"));
        htmlSb.append("</td></tr>");
        if (serverInfo != null) {
            htmlSb.append("<tr><td>" + mp.translate("AdminPanel", "Server Information:") + "</td>");
            htmlSb.append("<td align=\"left\" style=\"white-space: nowrap\">");
            htmlSb.append(serverInfo);
            htmlSb.append("</td></tr>");
        }
        List<Html> htmlList = Html.parseFromStr(htmlSb.toString());
        for (Html htmlList1 : htmlList) {
            content.add(htmlList1);
        }
    }

}
