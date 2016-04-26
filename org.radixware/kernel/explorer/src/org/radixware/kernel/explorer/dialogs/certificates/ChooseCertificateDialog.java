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

package org.radixware.kernel.explorer.dialogs.certificates;

import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QFont;
import com.trolltech.qt.gui.QListWidget;
import com.trolltech.qt.gui.QListWidgetItem;
import com.trolltech.qt.gui.QPalette;
import com.trolltech.qt.gui.QTextEdit;
import com.trolltech.qt.gui.QToolButton;
import com.trolltech.qt.gui.QWidget;
import java.security.cert.X509Certificate;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.ssl.CertificateUtils;
import org.radixware.kernel.explorer.dialogs.ExplorerDialog;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.utils.html.HtmlBuilder;


final class ChooseCertificateDialog extends ExplorerDialog {    
    
    private final static int CERTIFICATE_DESCRIPTION_ROLE = Qt.ItemDataRole.UserRole+1;
    private final static String[] X509_FIELDS = new String[]{"CN","OU","O","L","ST","C","E"};   
    
    private final QListWidget certList = new QListWidget(this);
    private final QTextEdit certDetails = new QTextEdit(this);
    private final QToolButton detailedBtn = new QToolButton(this);
    
    private final Map<String, String> x509FieldTitles = new HashMap<>();        
    
    public ChooseCertificateDialog(final Map<String,X509Certificate> certificates, final IClientEnvironment environment, final QWidget parent){
        super(environment, parent);
        initX509Fields(getEnvironment().getMessageProvider());
        setupUI();
        for (Map.Entry<String,X509Certificate> entry: certificates.entrySet()){
            addListItem(entry.getKey(), entry.getValue());        
        }
        certList.setCurrentRow(0);
        setMinimumHeight(400);
    }
    
    private void setupUI(){        
        setWindowIcon(ExplorerIcon.getQIcon(CertificateManager.Icons.IMPORT_KEY));
        layout().addWidget(certList);
        layout().addWidget(detailedBtn);
        layout().addWidget(certDetails);
        certList.setObjectName("lwCertificates");
        certList.setIconSize(new QSize(24,24));        
        certList.currentItemChanged.connect(this,"updateCertDetails(QListWidgetItem)");
        certList.doubleClicked.connect(this,"accept()");
        detailedBtn.setObjectName("tbDetails");
        final QFont detailBtnFont = new QFont(detailedBtn.font());
        detailBtnFont.setPointSize(detailBtnFont.pointSize() - 1);
        detailBtnFont.setUnderline(true);
        detailBtnFont.setItalic(true);
        detailedBtn.setFont(detailBtnFont);
        detailedBtn.setToolButtonStyle(Qt.ToolButtonStyle.ToolButtonTextBesideIcon);
        detailedBtn.setAutoRaise(true);
        detailedBtn.setCheckable(true);
        detailedBtn.setText(Application.translate("ChooseCertificateDialog", "show detailed information"));
        detailedBtn.toggled.connect(this, "onDetailsToggle(boolean)");
        certDetails.setObjectName("teDetails");
        certDetails.setReadOnly(true);
        final QPalette palette = new QPalette(certDetails.palette());
        palette.setColor(QPalette.ColorRole.Base, palette.color(QPalette.ColorRole.Window));        
        certDetails.setPalette(palette); 
        certDetails.setVisible(false);
        addButtons(EnumSet.of(EDialogButtonType.OK,EDialogButtonType.CANCEL),true);
    }
                
    private void addListItem(final String alias, final X509Certificate certificate){
        final QListWidgetItem item = new QListWidgetItem(certList);
        final MessageProvider mp = getEnvironment().getMessageProvider();
        item.setIcon(ExplorerIcon.getQIcon(CertificateManager.Icons.CERTIFICATE));        
        final Format dateFormat = SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT);
        final String subjectDNAsStr = certificate.getSubjectX500Principal().getName();
        final Map<String,String> subjectDn = CertificateUtils.parseDistinguishedName(subjectDNAsStr);
        final String owner = subjectDn.containsKey("CN") ? subjectDn.get("CN") : alias;
        final String issuerDNAsStr = certificate.getIssuerX500Principal().getName();
        final Map<String,String> issuerDn;
        if (issuerDNAsStr!=null && !issuerDNAsStr.isEmpty()){
            issuerDn = CertificateUtils.parseDistinguishedName(issuerDNAsStr);
            if (issuerDn.containsKey("CN")){
                final String titleTemplate = mp.translate("ChooseCertificateDialog","%1s\nIssuer: %2s\nValid from %3s to %4s");
                item.setText(String.format(titleTemplate, owner, issuerDn.get("CN"), dateFormat.format(certificate.getNotBefore()), dateFormat.format(certificate.getNotAfter())));
            }else{
                final String titleTemplate = mp.translate("ChooseCertificateDialog","%1s\nValid from %2s to %3s");
                item.setText(String.format(titleTemplate, owner, issuerDn.get("CN"), dateFormat.format(certificate.getNotBefore()), dateFormat.format(certificate.getNotAfter())));
            }
        }else{
            issuerDn = null;
        }
        item.setData(Qt.ItemDataRole.UserRole, alias);
        item.setData(CERTIFICATE_DESCRIPTION_ROLE, buildCertificateDescription(certificate, mp, subjectDn, issuerDn));
        item.setFlags(Qt.ItemFlag.ItemIsEnabled, Qt.ItemFlag.ItemIsSelectable);
    }
     
    private String buildCertificateDescription(final X509Certificate certificate, final MessageProvider mp, final Map<String,String> subjectDN, final Map<String,String> issuerDN){
        final StringBuilder details = new StringBuilder();

        details.append("<b>");
        details.append(mp.translate("CertificateConfirmationDialog", "Certificate"));
        details.append("</b>");
        {
            final HtmlBuilder htmlTable = new HtmlBuilder();
            htmlTable.startTable();
            for (String field: X509_FIELDS){
                if (subjectDN.containsKey(field)){
                    htmlTable.startRow();
                    htmlTable.appendCell(x509FieldTitles.get(field));
                    htmlTable.appendCell("");
                    htmlTable.appendCell(subjectDN.get(field));
                    htmlTable.endRow();
                }
            }            
            final Format dateFormat = SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT);
            htmlTable.startRow();
            htmlTable.appendCell(mp.translate("CertificateConfirmationDialog","Valid from:"));
            htmlTable.appendCell("");
            htmlTable.appendCell(dateFormat.format(certificate.getNotBefore()));
            htmlTable.endRow();
            htmlTable.startRow();
            htmlTable.appendCell(mp.translate("CertificateConfirmationDialog","Valid until:"));
            htmlTable.appendCell("");
            htmlTable.appendCell(dateFormat.format(certificate.getNotAfter()));
            htmlTable.endRow();
            htmlTable.endTable();
            details.append(htmlTable.toString());            
        }        
        
        if (issuerDN!=null && !issuerDN.isEmpty()){
            details.append("<b>");
            details.append(mp.translate("CertificateConfirmationDialog", "Issuer"));
            details.append("</b>");       
            final HtmlBuilder htmlTable = new HtmlBuilder();
            htmlTable.startTable();
            for (String field: X509_FIELDS){
                if (issuerDN.containsKey(field)){
                    htmlTable.startRow();
                    htmlTable.appendCell(x509FieldTitles.get(field));
                    htmlTable.appendCell("");
                    htmlTable.appendCell(issuerDN.get(field));
                    htmlTable.endRow();
                }
            }
            htmlTable.endTable();
            details.append(htmlTable.toString());
        }
        return details.toString();
    }
    
    public String getChoosenCertificateAlias(){
        return certList.currentItem()==null ? null : (String)certList.currentItem().data(Qt.ItemDataRole.UserRole);
    }
    
    @SuppressWarnings("unused")
    private void updateCertDetails(final QListWidgetItem currentItem){
        if (currentItem==null){
            certDetails.clear();
        }else{
            certDetails.setHtml((String)currentItem.data(CERTIFICATE_DESCRIPTION_ROLE));
        }
    }
    
    @SuppressWarnings("unused")
    private void onDetailsToggle(final boolean enabled){
        certDetails.setVisible(enabled);
        if (enabled){
            detailedBtn.setText(Application.translate("ChooseCertificateDialog", "hide detailed information"));
        }else{
            detailedBtn.setText(Application.translate("ChooseCertificateDialog", "show detailed information"));
        }
    }
    
    private void initX509Fields(final MessageProvider mp) {
        x509FieldTitles.put("CN", mp.translate("CertificateConfirmationDialog", "Common name (CN):"));
        x509FieldTitles.put("OU", mp.translate("CertificateConfirmationDialog","Organizational unit (OU):"));
        x509FieldTitles.put("O", mp.translate("CertificateConfirmationDialog","Organization (O):"));
        x509FieldTitles.put("L", mp.translate("CertificateConfirmationDialog","Locality (L):"));
        x509FieldTitles.put("ST", mp.translate("CertificateConfirmationDialog","State (ST):"));
        x509FieldTitles.put("C", mp.translate("CertificateConfirmationDialog","Country (C):"));
        x509FieldTitles.put("E", mp.translate("CertificateConfirmationDialog","E-mail (E):"));
    }    
}
