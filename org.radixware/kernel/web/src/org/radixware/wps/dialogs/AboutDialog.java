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

package org.radixware.wps.dialogs;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.types.TimeZoneInfo;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.html.Div;
import org.radixware.kernel.common.html.Html;
import org.radixware.kernel.common.html.Table;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.common.utils.VersionNumber;
import org.radixware.kernel.starter.meta.LayerMeta;
import org.radixware.kernel.starter.meta.RevisionMeta;
import org.radixware.kernel.starter.radixloader.RadixLoader;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.icons.WpsIcon;
import org.radixware.wps.rwt.Dialog;
import org.radixware.wps.rwt.Image;
import org.radixware.wps.rwt.UIObject;


public final class AboutDialog extends Dialog{
    
    private Html serverTimeLabel = new Html("label");
    
    public AboutDialog(final WpsEnvironment environment) { 
        super(environment,environment.getMessageProvider().translate("About", "About RadixWare Explorer"));
        setupUi();
    }
    
    private void setupUi(){
        setWindowIcon(getEnvironment().getApplication().getImageManager().getIcon(ClientIcon.Dialog.ABOUT));
        final Image explorerIcon = new Image();        
        explorerIcon.setIcon((WpsIcon)getEnvironment().getApplication().getImageManager().getIcon(ClientIcon.EXPLORER));
        explorerIcon.setWidth(128);
        explorerIcon.setHeight(128);
        explorerIcon.getHtml().setCss("float", "left");
        add(explorerIcon);        
        final UIObject content = new UIObject(createContent(getEnvironment().getMessageProvider())){            
        };
        add(content);        
        addButton(EDialogButtonType.OK);
        setResizable(false);
    }
    
    private Html createContent(final MessageProvider mp){
        final Html content = new Div();
        content.setCss("float", "right");
        content.setCss("padding-right", "3px");
        int height = 184;
        int width = 400;
        final int rowsCount = appendProductVersionsInfo(content,mp);
        height+=rowsCount*16;
        if (isConnected()){
            appendServerTime(content, mp);
            height+=30;
            width+=150;
            updateServerTimeLabel();
        }
        setMaxWidth(width);
        setMaxHeight(height);
        setMinimumWidth(width);
        setMinimumHeight(height);
        return content;
    }
    
    private int appendProductVersionsInfo(final Html content, final MessageProvider mp){                
        content.add(createHtmlHeader(mp.translate("About", "Version Information")));
        final Table versionsTable = new Table();        
        setupTableBorder(versionsTable);
        final Table.Header header = versionsTable.getHeader();
        setupTableBorder(header);        
        final RevisionMeta revisionMeta = RadixLoader.getInstance().getCurrentRevisionMeta();
        final String kernelVersionString =  revisionMeta.getKernelLayerVersionsString();
        final String appVersionString = revisionMeta.getAppLayerVersionsString();
        final String layerColumnWidth;
        final String versionColumnWidth;
        if (!appVersionString.isEmpty() && appVersionString.startsWith(kernelVersionString)){
            layerColumnWidth = "70%";
            versionColumnWidth = "30%";
        }else{
            layerColumnWidth = "37%";
            versionColumnWidth = "63%";
        }        
        createHeaderCell(header, mp.translate("About", "Layer Name")).setCss("width", layerColumnWidth);
        createHeaderCell(header, mp.translate("About", "Version")).setCss("width", versionColumnWidth);
        final Map<String,VersionNumber> appVersionByUri = Utils.parseVersionsByKey(appVersionString);
        final Map<String,VersionNumber> kernelVersionByUri = Utils.parseVersionsByKey(kernelVersionString);
        final List<LayerMeta> layers = revisionMeta.getAllLayersSortedFromBottom();                
        String appVersion, kernelVersion;
        String layerUri;
        VersionNumber versionNumber;
        for (LayerMeta layer: layers){
            layerUri = layer.getUri();
            versionNumber = appVersionByUri.get(layerUri);
            appVersion = versionNumber==null ? layer.getReleaseNumber() : versionNumber.toString();
            versionNumber = kernelVersionByUri.get(layerUri);
            kernelVersion = versionNumber==null ? layer.getReleaseNumber() : versionNumber.toString();
            writeVersionsToTable(versionsTable, mp, layerUri, appVersion==null ? "" : appVersion, kernelVersion==null ? "" : kernelVersion);
        }
        content.add(versionsTable);
        return layers.size();
    }
        
    private static void writeVersionsToTable(final Table versionsTable,  
                                                                 final MessageProvider mp, 
                                                                 final String layerUri, 
                                                                 final String appVersion, 
                                                                 final String kernelVersion){
        final Table.Row row = versionsTable.addRow();
        setupTableBorder(row);
        createCell(row, layerUri);
        final String version;
        if (kernelVersion.equals(appVersion)){
            version = appVersion;            
        }else{
            final StringBuilder versionBuilder = new StringBuilder();
            versionBuilder.append(mp.translate("About", "Application"));
            versionBuilder.append(": ");
            versionBuilder.append(appVersion);
            versionBuilder.append(", ");
            versionBuilder.append(mp.translate("About", "Kernel"));
            versionBuilder.append(": ");
            versionBuilder.append(kernelVersion);
            version = versionBuilder.toString();
        }        
        createCell(row, version).setAttr("align", "center");        
    }    
    
    private void appendServerTime(final Html content, final MessageProvider mp) {
        content.add(createHtmlHeader(mp.translate("About","Current Server Time")));
        serverTimeLabel.setCss("white-space", "nowrap");
        content.add(serverTimeLabel);
    }
    
    private void updateServerTimeLabel(){
        final Timestamp serverTime = getEnvironment().getCurrentServerTime();
        final TimeZoneInfo srvTimeZoneInfo =  getEnvironment().getServerTimeZoneInfo();
        final Locale locale =  getEnvironment().getLocale();
        final StringBuilder timeBuilder = new StringBuilder();
        timeBuilder.append(srvTimeZoneInfo.formatDateTime(SimpleDateFormat.LONG, SimpleDateFormat.MEDIUM, locale, serverTime));
        timeBuilder.append(srvTimeZoneInfo.formatDateTime("' GMT'XXX", locale, serverTime));
        timeBuilder.append(" (");
        timeBuilder.append(srvTimeZoneInfo.getTimeZoneDisplayName(TimeZone.LONG, locale));
        timeBuilder.append(')');
        serverTimeLabel.setInnerText(timeBuilder.toString());
    }
    
    private boolean isConnected(){
        return getEnvironment().getUserName()!=null && !getEnvironment().getUserName().isEmpty();
    }        
    
    private static Table.DataCell createCell(final Table.Row row, final String text){
        final Table.DataCell cell = row.addCell(text);
        cell.addClass("rwt-enable-text-selection");
        setupTableBorder(cell);
        return cell;
    }
    
    private static Table.HeaderCell createHeaderCell(final Table.Header header, final String text){
        final Table.HeaderCell cell = header.addCell(text);
        cell.addClass("rwt-enable-text-selection");
        setupTableBorder(cell);
        return cell;
    }    
    
    private static void setupTableBorder(final Html tablePart){
        tablePart.setCss("border", "1px solid black");
        tablePart.addClass("rwt-ui-element-with-border");
    }
    
    private static Html createHtmlHeader(final String title){
        final Html paragraph = new Html("p");
        paragraph.setCss("align", "center");
        final Html header = new Html("h3");
        header.addClass("rwt-enable-text-selection");
        header.setInnerText(title);
        paragraph.add(header);
        return paragraph;
    }    
}
