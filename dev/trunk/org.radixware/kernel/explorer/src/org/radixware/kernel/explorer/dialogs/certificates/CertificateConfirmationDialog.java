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

import com.trolltech.qt.core.Qt.AlignmentFlag;
import com.trolltech.qt.core.Qt.WidgetAttribute;
import com.trolltech.qt.gui.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.eas.AbstractSslTrustManager.Confirmation;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.explorer.dialogs.ExplorerDialog;
import org.radixware.kernel.explorer.env.ExplorerIcon;


public class CertificateConfirmationDialog extends ExplorerDialog {

    private static class CertificateValidityIcons extends ClientIcon {
        public CertificateValidityIcons(final String filename) {
            super(filename);
        }
        
        public static final CertificateValidityIcons VALID = new CertificateValidityIcons("classpath:images/ok.svg");
        public static final CertificateValidityIcons INVALID = new CertificateValidityIcons("classpath:images/cancel.svg");
    }
    
    private static final Map<String, String> X509FIELDS = new HashMap<>();
    private final X509Certificate certificate;
    private Confirmation result = Confirmation.REJECT;
    /**
     * Setting this field determines the order of a incoming certificate acceptance.
     * This is essential due to acceptance can be temporary or permanent.
     */
    private boolean permanent = false;
    
    public CertificateConfirmationDialog(final IClientEnvironment environment, final QWidget parent, final X509Certificate cert, final boolean reasonUnknown) {
        super(environment, parent);
        
        this.certificate = cert;
        initX509Fields(environment.getMessageProvider());
        
        setAttribute(WidgetAttribute.WA_DeleteOnClose);
        final String dlgTitle = environment.getMessageProvider().translate("CertificateConfirmationDialog", "Add Security Exception");
        setWindowTitle(dlgTitle);
        setUpUi(reasonUnknown);
    }

    private void setUpUi(final boolean reasonUknown) {
        final QLabel messageLabel = new QLabel(
                getEnvironment().getMessageProvider().translate("CertificateConfirmationDialog", "Do you trust this certificate?"),
                this);
        dialogLayout().addWidget(messageLabel, 0, AlignmentFlag.AlignTop);
        
        final QGroupBox subjectInfo = createSubjectInfo();
        dialogLayout().addWidget(subjectInfo, 1, AlignmentFlag.AlignTop);
        
        final QGroupBox issuerInfo = createIssuerInfo();
        dialogLayout().addWidget(issuerInfo, 1, AlignmentFlag.AlignTop);
        
        
                
        if(reasonUknown) {
            final QCheckBox chboxPermanent = new QCheckBox(
                    getEnvironment().getMessageProvider().translate("CertificateConfirmationDialog", "Store this exception permanently"),
                    this);
            chboxPermanent.toggled.connect(this, "onTogglePermanence(Boolean)");
            dialogLayout().addWidget(chboxPermanent,0, AlignmentFlag.AlignBottom);
            addButtons(EnumSet.of(EDialogButtonType.YES,EDialogButtonType.NO),true);
            chboxPermanent.toggled.connect(getButton(EDialogButtonType.NO), "setDisabled(boolean)");
            chboxPermanent.setChecked(false);
        }else{
            addButtons(EnumSet.of(EDialogButtonType.YES,EDialogButtonType.NO),true);
        }
        dialogLayout().setSizeConstraint(QLayout.SizeConstraint.SetFixedSize);
    }
    
    private QGroupBox createSubjectInfo() {
        final MessageProvider mp = getEnvironment().getMessageProvider();
        final QGroupBox subjectInfo = new QGroupBox(
                mp.translate("CertificateConfirmationDialog", "Certificate"),
                this);
        final QGridLayout grid = new QGridLayout();
        subjectInfo.setLayout(grid);
        final String info = certificate.getSubjectDN().toString();
        displayDN(info, grid, subjectInfo);
        
        int currentRow = info.split(",").length;
               
        grid.addWidget(new QLabel(mp.translate("CertificateConfirmationDialog","Valid from:"), subjectInfo), currentRow, 0);
        grid.addWidget(new QLabel(mp.translate("CertificateConfirmationDialog","Valid until:"), subjectInfo), currentRow + 1, 0);
        final Format format = SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT);
        
        grid.addWidget(new QLabel(format.format(certificate.getNotBefore()), subjectInfo), currentRow, 1);
        grid.addWidget(new QLabel(format.format(certificate.getNotAfter()), subjectInfo), currentRow + 1, 1);
        currentRow = currentRow + 2;
        final QLabel certValidLabel = new QLabel(
                mp.translate("CertificateConfirmationDialog", "Certificate validity status:"),
                subjectInfo);
        grid.addWidget(certValidLabel, currentRow, 0);
        
        final QLabel validMark = new QLabel(subjectInfo);
        QIcon validIcon = null;
        try {
            certificate.checkValidity();
            validIcon = ExplorerIcon.getQIcon(CertificateValidityIcons.VALID);
        } catch(CertificateException ex) {
            validIcon = ExplorerIcon.getQIcon(CertificateValidityIcons.INVALID);
        } finally {
            validMark.setPixmap(validIcon.pixmap(24, 24));
        }
        grid.addWidget(validMark, currentRow, 1);
        
        return subjectInfo;
    }
    
    private QGroupBox createIssuerInfo() {
        final QGroupBox issuerInfo = new QGroupBox(
                getEnvironment().getMessageProvider().translate("CertificateConfirmationDialog", "Issuer"),
                this);
        final QGridLayout grid = new QGridLayout();
        issuerInfo.setLayout(grid);
        
        final String issuerDN = certificate.getIssuerDN().toString();
        displayDN(issuerDN, grid, issuerInfo);
        return issuerInfo;
    }

    public Confirmation getConfirmation() {
        return result;
    }

    @Override
    public void accept() {
        result = permanent ? Confirmation.STORE : Confirmation.ACCEPT;
        super.accept();
    }
    
    private void onTogglePermanence(final Boolean flag) {
        permanent = flag;
    }
    
    private static void displayDN(final String dn, final QGridLayout grid, final QWidget parent) {
        final String[] keysValues = dn.split(",");
        int i = 0;
        for(String s : keysValues) {
            final String[] entries = s.split("=");
            if(!entries[1].isEmpty()) {
                final QLabel key = new QLabel(X509FIELDS.get(entries[0].trim()), parent);
                grid.addWidget(key, i, 0);
                final QLabel value = new QLabel(entries[1], parent);
                grid.addWidget(value, i, 1);
                i++;
            }
        }
    }
    
    private static void initX509Fields(final MessageProvider mp) {
        X509FIELDS.put("CN", mp.translate("CertificateConfirmationDialog", "Common name (CN):"));
        X509FIELDS.put("OU", mp.translate("CertificateConfirmationDialog","Organizational unit (OU):"));
        X509FIELDS.put("O", mp.translate("CertificateConfirmationDialog","Organization (O):"));
        X509FIELDS.put("L", mp.translate("CertificateConfirmationDialog","Locality (L):"));
        X509FIELDS.put("ST", mp.translate("CertificateConfirmationDialog","State (ST):"));
        X509FIELDS.put("C", mp.translate("CertificateConfirmationDialog","Country (C):"));
        X509FIELDS.put("E", mp.translate("CertificateConfirmationDialog","E-mail (E):"));
    }
}
