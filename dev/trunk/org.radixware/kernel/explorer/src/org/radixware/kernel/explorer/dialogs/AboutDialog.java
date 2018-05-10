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

package org.radixware.kernel.explorer.dialogs;

import com.trolltech.qt.core.QRect;
import com.trolltech.qt.core.QSizeF;
import com.trolltech.qt.core.QTimerEvent;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QAccessible;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QDialog;
import com.trolltech.qt.gui.QDialogButtonBox;
import com.trolltech.qt.gui.QFrame;
import com.trolltech.qt.gui.QGridLayout;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QLayout;
import com.trolltech.qt.gui.QPalette;
import com.trolltech.qt.gui.QShowEvent;
import com.trolltech.qt.gui.QSizePolicy;
import com.trolltech.qt.gui.QStyle;
import com.trolltech.qt.gui.QTextEdit;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.eas.DatabaseInfo;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.env.ProductInstallationOptions;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.types.TimeZoneInfo;
import org.radixware.kernel.common.utils.SystemTools;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.common.utils.VersionNumber;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.utils.WidgetUtils;
import org.radixware.kernel.starter.meta.LayerMeta;
import org.radixware.kernel.starter.meta.RevisionMeta;
import org.radixware.kernel.starter.radixloader.RadixDirLoader;
import org.radixware.kernel.starter.radixloader.RadixLoader;
import org.radixware.kernel.starter.radixloader.RadixLoaderException;
import org.radixware.kernel.starter.radixloader.RadixSVNLoader;


public final class AboutDialog extends QDialog {
    
    private static final Qt.WindowFlags WINDOW_FLAGS =
            new Qt.WindowFlags(Qt.WindowType.Dialog,
                               Qt.WindowType.MSWindowsFixedSizeDialogHint,
                               Qt.WindowType.WindowTitleHint, 
                               Qt.WindowType.WindowSystemMenuHint, 
                               Qt.WindowType.WindowCloseButtonHint);
    private static final Qt.Alignment CENTER_ALIGNMENT = new Qt.Alignment(Qt.AlignmentFlag.AlignCenter);
    
    private final MessageProvider mp;
    private final IClientEnvironment environment;
    private final QTextEdit teGeneralInformation = new QTextEdit(this);
    private final QLabel lbServerTime = new QLabel(this);
    private final QLabel lbIcon = new QLabel(this);    
    private final QGridLayout layout = new QGridLayout();
    private final QDialogButtonBox buttonBox = new QDialogButtonBox(Qt.Orientation.Horizontal, this);
    private final int timerId;

    public AboutDialog(final IClientEnvironment environment) { 
        super(QApplication.activeWindow());
        this.environment = environment;
        this.mp = environment.getMessageProvider();
        if (isConnected()) {
            timerId = startTimer(1000);
        } else {
            timerId = -1;
        }
        setupUi();
    }   
    
    private void setupUi() {
        setWindowFlags(WINDOW_FLAGS);
        setAttribute(Qt.WidgetAttribute.WA_DeleteOnClose, true);
        
        setWindowTitle(mp.translate("About", "About RadixWare Explorer"));
        setWindowIcon(ExplorerIcon.getQIcon(ClientIcon.Dialog.ABOUT));        
        setMaximumSize(WidgetUtils.shrinkWindowSize(WidgetUtils.MAXIMUM_SIZE, WidgetUtils.MAXIMUM_SIZE));
        
        teGeneralInformation.setObjectName("Rdx.AboutDialog.teGeneralInformation");
        teGeneralInformation.setReadOnly(true);
        teGeneralInformation.setFrameShape(QFrame.Shape.NoFrame);
        teGeneralInformation.setVerticalScrollBarPolicy(Qt.ScrollBarPolicy.ScrollBarAlwaysOff);
        teGeneralInformation.setHorizontalScrollBarPolicy(Qt.ScrollBarPolicy.ScrollBarAlwaysOff);
        teGeneralInformation.setHtml(getSystemInfo());
        teGeneralInformation.document().adjustSize();
        teGeneralInformation.adjustSize();
        final QSizeF documentSize = teGeneralInformation.document().documentLayout().documentSize();
        final double documentMargin = teGeneralInformation.document().documentMargin();
        teGeneralInformation.setFixedSize((int)(documentSize.width()+documentMargin), (int)(documentSize.height()+documentMargin));
        
        lbServerTime.setObjectName("Rdx.AboutDialog.lbServerTime");
        setupLabel(lbServerTime);
        if (isConnected()){
            lbServerTime.setText(getServerTime());
        }
        
        lbIcon.setObjectName("Rdx.AboutDialog.lbIcon");
        lbIcon.setSizePolicy(QSizePolicy.Policy.Fixed, QSizePolicy.Policy.Fixed);
        lbIcon.setPixmap(ExplorerIcon.getQIcon(ClientIcon.EXPLORER).pixmap(128, 128));
        buttonBox.setObjectName("Rdx.AboutDialog.buttonBox");
        buttonBox.addButton(QDialogButtonBox.StandardButton.Ok).setIcon(ExplorerIcon.getQIcon(ClientIcon.Dialog.BUTTON_OK));
        buttonBox.clicked.connect(this, "accept()");
        buttonBox.setCenterButtons(this.style().styleHint(QStyle.StyleHint.SH_MessageBox_CenterButtons, null, this) != 0);

        if (SystemTools.isOSX) {
            layout.setMargin(0);
            layout.setVerticalSpacing(8);
            layout.setHorizontalSpacing(0);
            setContentsMargins(24, 15, 24, 20);
            layout.addWidget(lbIcon, 0, 0, 2, 1, Qt.AlignmentFlag.AlignTop, Qt.AlignmentFlag.AlignLeft);
            layout.addWidget(teGeneralInformation, 0, 1, 1, 1);
            layout.addWidget(lbServerTime, 1, 1, 1, 1);            
            layout.setRowStretch(1, 100);
            layout.setRowMinimumHeight(2, 6);
            layout.addWidget(buttonBox, 4, 1, 1, 1);

        } else {
            layout.addWidget(lbIcon, 0, 0, 2, 1, Qt.AlignmentFlag.AlignTop);            
            layout.addWidget(teGeneralInformation, 0, 1, 1, 1);
            layout.addWidget(lbServerTime, 1, 1, 1, 1);            
            layout.addWidget(buttonBox, 3, 0, 1, 2);
        }

        layout.setSizeConstraint(QLayout.SizeConstraint.SetFixedSize);
        setLayout(layout);
        setModal(true);
    }
    
    private boolean isConnected(){
        return !environment.getUserName().isEmpty();
    }    
    
    private void setupLabel(final QLabel label){
        label.setWordWrap(false);
        label.setSizePolicy(QSizePolicy.Policy.Minimum, QSizePolicy.Policy.Minimum);        
        if (SystemTools.isOSX) {
            label.setContentsMargins(16, 0, 0, 0);
        } else {
            label.setContentsMargins(2, 0, 0, 0);
        }
    }  
    
    @Override
    public void showEvent(final QShowEvent e) {
        final QRect parentGeometry;
        if (parentWidget() == null) {
            parentGeometry = QApplication.desktop().availableGeometry();
        } else {
            parentGeometry = parentWidget().window().frameGeometry();
        }
        setGeometry(QStyle.alignedRect(QApplication.layoutDirection(), CENTER_ALIGNMENT, size(), parentGeometry));
        QAccessible.updateAccessibility(this, 0, QAccessible.Event.Alert);

        super.showEvent(e);
    }    

    @Override
    protected void timerEvent(final QTimerEvent timerEvent) {
        if (timerEvent.timerId() == timerId) {
            lbServerTime.setText(getServerTime());
            timerEvent.accept();
        } else {
            super.timerEvent(timerEvent);
        }
    }
    
    @Override
    public void done(final int result) {
        if (timerId != -1) {
            killTimer(timerId);
        }
        super.done(result);
    }
    
    @SuppressWarnings("PMD.ConsecutiveLiteralAppends")
    private String getServerTime() {
        final Timestamp serverTime = environment.getCurrentServerTime();
        final TimeZoneInfo srvTimeZoneInfo = environment.getServerTimeZoneInfo();
        final Locale locale = environment.getLocale();
        final StringBuilder html = new StringBuilder(128);
        html.append("<p align=\"center\"><h3>");
        html.append(mp.translate("About", "Current Server Time"));
        html.append("</h3></p>");
        html.append("<td align=\"left\" style=\"white-space: nowrap;\">");
        html.append(srvTimeZoneInfo.formatDateTime(SimpleDateFormat.LONG, SimpleDateFormat.MEDIUM, locale, serverTime));
        html.append(srvTimeZoneInfo.formatDateTime("' GMT'XXX", locale, serverTime));
        html.append(" (");
        html.append(srvTimeZoneInfo.getTimeZoneDisplayName(TimeZone.LONG, locale));
        html.append(')');
        return html.toString();
    }    

    @SuppressWarnings("PMD.ConsecutiveLiteralAppends")
    private String getSystemInfo() {
        final StringBuilder html = new StringBuilder(1024);
        final QPalette palette = new QPalette(palette());
        final QColor color = palette.color(QPalette.ColorRole.Window);        
        html.append("<html><body style=\"background-color: ");
        html.append(color.name());
        html.append(";\">");
        writeProductVersions(html);
        writeQtVersion(html);
        final RadixLoader rxLoader = RadixLoader.getInstance();
        if (rxLoader!=null){
            writeStarterInformation(html, rxLoader);
        }
        writeSystemInformation(html);
        if (isConnected()){
            final DatabaseInfo dbInfo = environment.getEasSession().getDatabaseInfo();
            if (dbInfo!=null && !dbInfo.isEmpty()){
                writeDatabaseInformation(html, dbInfo);
            }
            writeProductInstallationOptions(html, environment.getProductInstallationOptions());
        }
        html.append("</body></html>");
        return html.toString();
    }
    
    private void writeProductVersions(final StringBuilder html){
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
        writeHeader(html, mp.translate("About", "Version Information"));
        html.append("<table border=\"1\" width=\"100%\">");
        html.append("<tr><th width=\"");
        html.append(layerColumnWidth);
        html.append("\">");
        html.append(mp.translate("About", "Layer Name"));
        html.append("</th>");
        html.append("<th width=\"");
        html.append(versionColumnWidth);
        html.append("\">");
        html.append(mp.translate("About", "Version"));
        html.append("</th></tr>");
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
            writeVersionsToTable(html, layerUri, appVersion==null ? "" : appVersion, kernelVersion==null ? "" : kernelVersion);
        }
        html.append("</table>");
    }
    
    private void writeVersionsToTable(final StringBuilder table, final String layerUri, final String appVersion, final String kernelVersion){
        table.append("<tr><td>");
        table.append(layerUri);
        table.append("</td><td align=\"center\"> ");        
        if (kernelVersion.equals(appVersion)){
            table.append(appVersion);
        }else{
            table.append(mp.translate("About", "Application"));
            table.append(": ");
            table.append(appVersion);
            table.append(", ");
            table.append(mp.translate("About", "Kernel"));
            table.append(": ");
            table.append(kernelVersion);
        }
        table.append("</td></tr>");
    }
    
    private void writeQtVersion(final StringBuilder html){
        html.append("<table border=\"0\"><tr><td>");
        html.append("</td></tr><tr><td>");
        html.append(String.format(mp.translate("About", "This application uses Qt library version %s"), com.trolltech.qt.Utilities.VERSION_STRING));
        html.append("</td></tr></table>");
    }
    
    private void writeStarterInformation(final StringBuilder html, final RadixLoader rxLoader){
        writeHeader(html, mp.translate("About", "Starter Information"));

        html.append("<table border=\"0\"><tr><td>");
        html.append(mp.translate("About", "Revision number: "));            
        try{
            html.append(String.valueOf(rxLoader.getCurrentRevision()));
        }catch(RadixLoaderException exception){
            environment.getTracer().error(exception);
            html.append(mp.translate("About", "unknown"));
        }
        html.append("</td></tr>");
        if (rxLoader instanceof RadixSVNLoader){
            html.append("<tr><td>");
            html.append(mp.translate("About", "Directory for cache:"));
            html.append("</td></tr><tr><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
            html.append(rxLoader.getRoot());                
            html.append("</td></tr>");
        }else if (rxLoader instanceof RadixDirLoader){
            html.append("<tr><td>");
            html.append(mp.translate("About", "Working directory:"));
            html.append("</td></tr><tr><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
            html.append(rxLoader.getRoot().getAbsolutePath());
            html.append("</td></tr>");            
        }
        html.append("<tr><td>");
        html.append(mp.translate("About", "Directory for temporary files:"));
        html.append("</td></tr><tr><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
        html.append(org.radixware.kernel.starter.utils.SystemTools.getTmpDir());
        html.append("</td></tr>");
        if (rxLoader instanceof RadixSVNLoader){
            html.append("<tr><td>");
            html.append(mp.translate("About", "Subversion URL list:"));
            html.append("</td></tr>");
            final List<String> urls = ((RadixSVNLoader)rxLoader).getSvnUrls();
            for (String url: urls){
                html.append("<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
                html.append(url);
                html.append("</td></tr>");
            }                
        }
        html.append("</table>");        
    }
    
    private void writeSystemInformation(final StringBuilder html){
        writeHeader(html, mp.translate("About", "System Information"));

        html.append("<table border=\"0\">");
        html.append("<tr><td>Java:</td>");
        html.append("<td align=\"left\" style=\"white-space: nowrap\">");
        html.append(System.getProperty("java.runtime.version"));
        html.append("</td></tr>");
        html.append("<tr><td>Java VM:</td>");
        html.append("<td align=\"left\" style=\"white-space: nowrap\">");
        html.append(String.format("%s %s ",
                System.getProperty("java.vm.name"),
                System.getProperty("java.vm.version")));
        html.append("</td></tr>");
        html.append("<tr><td>");
        html.append(mp.translate("About", "System:"));
        html.append("&nbsp;</td>");
        html.append("<td align=\"left\" style=\"white-space: nowrap;\">");
        final String osVersion = System.getProperty("os.version").replace("-", "\u2011");
        html.append(String.format(mp.translate("About", "%s %s running on %s; %s; %s_%s"),
                System.getProperty("os.name"), osVersion, System.getProperty("os.arch"),
                System.getProperty("file.encoding"), System.getProperty("user.language"), System.getProperty("user.country")));
        html.append("</td></tr>");
        html.append("</table>");        
    }
    
    private void writeDatabaseInformation(final StringBuilder html, final DatabaseInfo dbInfo){
        writeHeader(html, mp.translate("About", "Database Information"));

        if (dbInfo.getProductName()!=null 
            && dbInfo.getProductVersion()!=null 
            && !dbInfo.getProductVersion().isEmpty()
            && dbInfo.getProductVersion().contains(dbInfo.getProductName())){
            html.append("<table border=\"0\"><tr><td>");
            html.append(mp.translate("About", "Database server:"));                    
            if (dbInfo.getProductVersion().length()>100){
                html.append("</td></tr><tr><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
                html.append(dbInfo.getProductVersion());
                html.append("</td></tr></table><table border=\"0\">");
            }else{
                html.append("&nbsp;</td><td>");
                html.append(dbInfo.getProductVersion());
                html.append("</td></tr>");
            }                    
        }else{
            html.append("<table border=\"0\"><tr><td>");
            html.append(mp.translate("About", "Database server:"));                    
            if (dbInfo.getProductName()==null || dbInfo.getProductName().isEmpty()){
                html.append("&nbsp;</td><td>");
                html.append(mp.translate("About", "Unknown"));
                html.append("</td></tr>");
            }else{
                if (dbInfo.getProductName().length()>100){
                    html.append("</td></tr><tr><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
                    html.append(dbInfo.getProductName());
                    html.append("</td></tr></table><table border=\"0\">");
                }else{
                    html.append("&nbsp;</td><td>");                            
                    html.append(dbInfo.getProductName());
                    html.append("</td></tr>");
                }                                                
            }                    
            html.append("<tr><td>");
            html.append(mp.translate("About", "Database server version:"));                    
            if (dbInfo.getProductVersion()==null || dbInfo.getProductVersion().isEmpty()){
                html.append("&nbsp;</td><td>");
                html.append(mp.translate("About", "Unknown"));
                html.append("</td></tr>");
            }else{
                if (dbInfo.getProductVersion().length()>100){
                    html.append("</td></tr><tr><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
                    html.append(dbInfo.getProductVersion());
                    html.append("</td></tr></table><table border=\"0\">");
                }else{
                    html.append("&nbsp;</td><td>");
                    html.append(dbInfo.getProductVersion());
                    html.append("</td></tr>");
                }
            }
        }
        html.append("<tr><td>");
        html.append(mp.translate("About", "Database client:"));
        html.append("&nbsp;</td><td>");
        if (dbInfo.getDriverName()==null || dbInfo.getDriverName().isEmpty()){
            html.append(mp.translate("About", "Unknown"));
        }else{
            html.append(dbInfo.getDriverName());
            if (dbInfo.getDriverVersion()!=null && !dbInfo.getDriverVersion().isEmpty()){
                html.append("&nbsp;");
                html.append(dbInfo.getDriverVersion());
            }
        }
        html.append("</td></tr>");
        html.append("</table>");        
    }
    
    private void writeProductInstallationOptions(final StringBuilder html, final ProductInstallationOptions options){
        final List<String> optionNames = new ArrayList<>(options.getOptionNames());        
        if (!optionNames.isEmpty()){
            Collections.sort(optionNames);
            writeHeader(html, mp.translate("About", "Product Installation Options"));
            html.append("<table border=\"1\" width=\"100%\">");
            html.append("<tr><th width=\"70%\">");
            html.append(mp.translate("About", "Parameter"));
            html.append("</th>");
            html.append("<th width=\"30%\">");
            html.append(mp.translate("About", "Value"));
            html.append("</th></tr>");
            String value;
            for (String optionName: optionNames){
                value = options.getOptionValue(optionName);
                if (value==null || value.isEmpty()){
                    value = mp.translate("About", "Enabled");
                }
                html.append("<tr><td>");
                html.append(optionName);
                html.append("</td><td align=\"center\"> ");
                html.append(value);
                html.append("</td></tr>");                
            }
            html.append("</table>");
        }
    }
    
    private static void writeHeader(final StringBuilder html, final String header){
        html.append("<p align=\"center\"><h3>");
        html.append(header);
        html.append("</h3></p>");        
    }
    
}