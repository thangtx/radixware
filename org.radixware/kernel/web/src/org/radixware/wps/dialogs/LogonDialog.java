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

package org.radixware.wps.dialogs;

import java.security.cert.X509Certificate;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.meta.RadEnumPresentationDef;
import org.radixware.kernel.common.client.meta.mask.EditMaskList;
import org.radixware.kernel.common.client.meta.mask.EditMaskStr;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.ssl.CertificateUtils;
import org.radixware.kernel.common.types.Id;
import org.radixware.wps.WebServerRunParams;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.rwt.CheckBox;
import org.radixware.wps.rwt.Dialog;
import org.radixware.wps.rwt.GridLayout;
import org.radixware.wps.rwt.InputBox;
import org.radixware.wps.rwt.Label;
import org.radixware.wps.rwt.UIObject;
import org.radixware.wps.views.editors.valeditors.ValListEditorController;
import org.radixware.wps.views.editors.valeditors.ValStrEditorController;


public class LogonDialog extends Dialog {
    
    private final Id ISO_LANGUAGE_CONST_ID = Id.Factory.loadFrom("acs2G44GBNBE5D7RO2QHQSVRZNLG4");

    private final GridLayout layout = new GridLayout();
    private final GridLayout.Row passwordRow;
    private final GridLayout.Row useCertRow;
    private boolean certificateAvailable = false;
    private ValStrEditorController edUserName;
    private ValStrEditorController edPassword;
    private ValStrEditorController edStation;
    private ValListEditorController<String> edLanguage;
    private CheckBox cbUserCert;
    private X509Certificate[] cert;
    private Label lbUserName;
    private Label lbStationName;

    public LogonDialog(final WpsEnvironment env) {
        this(env, null, null, true);
    }

    public LogonDialog(final WpsEnvironment env, final String predefinedUserName) {
        this(env, predefinedUserName, null, true);
    }
    
    public LogonDialog(final WpsEnvironment env, final String predefinedUserName, final String predefinedStationName, boolean byCertificateOrKerberos) {
        super(env.getDialogDisplayer(), env.getMessageProvider().translate("LogonDialog", "User Login"), false);
        addCloseAction(EDialogButtonType.OK).setDefault(true);
        addCloseAction(EDialogButtonType.CANCEL);
        add(layout);
        layout.getAnchors().setRight(new Anchors.Anchor(1, -3));
        this.useCertRow = new GridLayout.Row();
        this.cbUserCert = new CheckBox();
        this.cbUserCert.setText("Connect to RadixWare server using my certificate");
        GridLayout.Cell cell = new GridLayout.Cell();
        this.useCertRow.add(cell);
        cell = new GridLayout.Cell();
        cell.setFitWidth(true);
        this.useCertRow.add(cell);
        cell.add(cbUserCert);
        cell.setVCoverage(100);
        cbUserCert.addSelectionStateListener(new CheckBox.SelectionStateListener() {
            @Override
            public void onSelectionChange(CheckBox cb) {
                if (cbUserCert.isSelected()) {
                    layout.remove(passwordRow);
                    ((UIObject) edUserName.getValEditor()).setEnabled(false);
                    edUserName.setValue(getUserNameFromCertificate(cert));
                } else {
                    layout.add(passwordRow);
                    ((UIObject) edUserName.getValEditor()).setEnabled(true);
                }
            }
        });
        cbUserCert.setVCoverage(100);

        this.edUserName = new ValStrEditorController(env);
        this.edUserName.setMandatory(true);
        edUserName.setMandatory(true);
        ((UIObject)edUserName.getValEditor()).setObjectName("edUserName");
        this.edPassword = new ValStrEditorController(env);
        edPassword.setMandatory(true);
        ((UIObject)edPassword.getValEditor()).setObjectName("edPassword");
        this.passwordRow = new GridLayout.Row();
        this.edStation = new ValStrEditorController(env);
        this.edStation.setMandatory(true);
        ((UIObject)edStation.getValEditor()).setObjectName("edStation");
        this.edLanguage = new ValListEditorController<>(env);

        cell = new GridLayout.Cell();
        this.passwordRow.add(cell);

        Label label = new Label(env.getMessageProvider().translate("LogonDialog", "Password:"));
        cell.setFitWidth(true);
        cell.add(label);
        cell = new GridLayout.Cell();
        this.passwordRow.add(cell);
        cell.add((UIObject) edPassword.getValEditor());
        cell.setVCoverage(100);


        final GridLayout.Row userNameRow = new GridLayout.Row();

        cell = new GridLayout.Cell();
        userNameRow.add(cell);
        label = lbUserName = new Label(env.getMessageProvider().translate("LogonDialog", "User name:"));
        label.setEnabled(predefinedUserName == null || !byCertificateOrKerberos);
        label.setTextWrapDisabled(true);
        cell.add(label);
        cell.setFitWidth(true);
        cell = new GridLayout.Cell();
        userNameRow.add(cell);
        cell.add((UIObject) edUserName.getValEditor());
        cell.setVCoverage(100);
        ((UIObject) edUserName.getValEditor()).setPersistenceKey("LogonDialog.userName");

        EditMaskStr mask = new EditMaskStr();
        mask.setEmptyStringAllowed(false);
        mask.setNoValueStr(env.getMessageProvider().translate("LogonDialog", "Please enter user name"));
        this.edUserName.setEditMask(mask);
        if (predefinedUserName != null) {
            edUserName.setValue(predefinedUserName);
            if (!byCertificateOrKerberos) {
                edUserName.setReadOnly(true);
            }
        }
        mask = new EditMaskStr();
        mask.setEmptyStringAllowed(true);
        mask.setNoValueStr(env.getMessageProvider().translate("LogonDialog", "Please enter password"));
        this.edPassword.setEditMask(mask);


        ((InputBox) this.edPassword.getValEditor()).setPassword(true);
        layout.add(userNameRow);
        if (predefinedUserName == null || !byCertificateOrKerberos) {
            layout.add(this.passwordRow);
        }

        GridLayout.Row stationRow = new GridLayout.Row();
        layout.add(stationRow);
        cell = new GridLayout.Cell();
        label = lbStationName = new Label(env.getMessageProvider().translate("LogonDialog", "Station:"));
        label.setTextWrapDisabled(true);
        cell.add(label);
        stationRow.add(cell);
        cell.setFitWidth(true);
        cell = new GridLayout.Cell();
        cell.add((UIObject) edStation.getValEditor());
        mask = new EditMaskStr();
        mask.setEmptyStringAllowed(true);
        mask.setNoValueStr(env.getMessageProvider().translate("LogonDialog", "Please enter station name"));
        cell.setVCoverage(100);
        stationRow.add(cell);
        label.setTextWrapDisabled(true);
        this.edStation.setEditMask(mask);
        if (predefinedStationName != null) {
            edStation.setValue(predefinedStationName);
        }

        GridLayout.Row languageRow = new GridLayout.Row();
        layout.add(languageRow);
        cell = new GridLayout.Cell();
        label = new Label(env.getMessageProvider().translate("LogonDialog", "Language:"));
        label.setTextWrapDisabled(true);
        cell.add(label);
        languageRow.add(cell);
        cell.setFitWidth(true);
        cell = new GridLayout.Cell();
        cell.add((UIObject) edLanguage.getValEditor());
        
        final RadEnumPresentationDef enumDef = env.getDefManager().getEnumPresentationDef(ISO_LANGUAGE_CONST_ID);
        final RadEnumPresentationDef .Items enumItems = enumDef.getEmptyItems();
        EIsoLanguage defaultLang = getEnvironment().getLanguage();
        try {
            env.getConfigStore().beginGroup("LogonDialog");
            try {
                String langStr = env.getConfigStore().readString("language", env.getLanguage().getValue());
                if (langStr != null) {
                    defaultLang = EIsoLanguage.getForValue(langStr);
                }
            } catch (NoConstItemWithSuchValueError e) {
            }
            if (predefinedUserName == null) {
                final String configString = env.getConfigStore().readString("userName", env.getUserName());
                if (configString != null && !configString.isEmpty()) {
                    edUserName.setValue(configString);
                }
            }
            {
                if (predefinedStationName == null) {
                    final String configString = env.getConfigStore().readString("stationName", env.getStationName());
                    if (configString != null && !configString.isEmpty()) {
                        edStation.setValue(configString);
                    }
                }
            }
        } finally {
            env.getConfigStore().endGroup();
        }

        if (!env.getSupportedLanguages().contains(defaultLang)) {
            defaultLang = null;
        }
        for (EIsoLanguage lang : env.getSupportedLanguages()) {
            enumItems.addItem(lang);
            if (defaultLang == null) {
                defaultLang = lang;
            }
        }

        if (defaultLang == null) {
            edLanguage.setValue(env.getLanguage().getValue());
        } else {
            edLanguage.setValue(defaultLang.getValue());
        }
        final EditMaskList lngList = new EditMaskList();
        for (RadEnumPresentationDef.Item enumItem: enumItems){
            final String isoCode = (String)enumItem.getValue();            
            final EIsoLanguage language = getIsoLanguageFromIsoCode(isoCode);
            if (language!=null){
                lngList.addItem(enumItem.getTitle(language), isoCode);
            }
        }    
        edLanguage.setEditMask(lngList);
        cell.setVCoverage(100);
        languageRow.add(cell);
        label.setTextWrapDisabled(true);
        edLanguage.setMandatory(true);

        setAutoHeight(true);
        setWidth(350);
        setResizable(false);
    }
    
    
    private static final boolean IGNORE_SSL = true;

    public boolean connect(X509Certificate[] certificate) {
        cert = certificate;
        this.certificateAvailable = IGNORE_SSL ? false : certificate != null && certificate.length > 0;
        if (certificateAvailable) {
            if (useCertRow.getParent() != this.layout) {
                this.layout.add(useCertRow);
            }
        }

        if (certificateAvailable && useCertificate()) {
            this.layout.remove(passwordRow);
        } else {
            if (passwordRow.getParent() != this.layout) {
                this.layout.add(1, passwordRow);
            }
        }


        if (edUserName.getValue() != null && edUserName.getValue().isEmpty()) {
            edUserName.setValue(null);
        }
        if (edUserName.getValue() == null) {
            edUserName.setValue(getUserNameFromCertificate(certificate));
        }
        if (edStation.getValue() != null && edStation.getValue().isEmpty()) {
            edStation.setValue(null);
        }
        edPassword.setValue(null);
        //  certificateAvailable = false;
        //} 
//        else {
//            
//            if (!certificateAvailable) {
//                this.layout.add(0, useCertRow);
//                this.cbUserCert.setSelected(true);
//            }
//            this.edPassword.setValue("");
//            certificateAvailable = true;
//            edUserName.setValue(getUserNameFromCertificate(certificate));
//        }
        return execDialog() == DialogResult.ACCEPTED;
    }

    private boolean selfCheck() {
        List<String> emptyFieldsList = new LinkedList<>();
        ValStrEditorController focused = null;
        MessageProvider mp = getEnvironment().getMessageProvider();
        if (!useCertificate()) {
            if (edUserName.getValue() == null || edUserName.getValue().isEmpty()) {
                emptyFieldsList.add(mp.translate("LogonDialog", "user name"));
                focused = edUserName;
            } 
        }
        if (!WebServerRunParams.getIsDevelopmentMode()) {
            if (edPassword.getValue() == null || edPassword.getValue().isEmpty()) {
                emptyFieldsList.add(mp.translate("LogonDialog", "password"));
                if (focused == null) {
                    focused = edPassword;
                }
            }
        }
        if (edStation.getValue() == null || edStation.getValue().isEmpty()) {
            emptyFieldsList.add(mp.translate("LogonDialog", "station name"));
            if (focused == null) {
                focused = edStation;
            }
        } 
        boolean ok = emptyFieldsList.isEmpty();
        if (!ok) {
            String message = null;
            switch(emptyFieldsList.size()) {
                case 1: message = String.format(mp.translate("LogonDialog", "Please enter %1$s"), emptyFieldsList.get(0));
                        break;
                case 2: message = String.format(mp.translate("LogonDialog", "Please enter %1$s and %2$s"), emptyFieldsList.get(0), emptyFieldsList.get(1));
                        break;
                case 3: message = String.format(mp.translate("LogonDialog", "Please enter %1$s, %2$s and %3$s"), emptyFieldsList.get(0), emptyFieldsList.get(1), emptyFieldsList.get(2));
                        break;
            }
            getEnvironment().messageError("Some parameters were not specified", message); 
            ((UIObject)focused.getValEditor()).setFocused(true);
        }
        return emptyFieldsList.isEmpty();
    }

    public static String getUserNameFromCertificate(X509Certificate[] cert) {
        if (cert == null || cert.length == 0) {
            return null;
        }
        final String clientDN = cert[0].getSubjectX500Principal().getName();
        final Map<String, String> clientDetails = CertificateUtils.parseDistinguishedName(clientDN);
        return clientDetails.get("CN");
    }

    public String getUserName() {
        if (useCertificate()) {
            return getUserNameFromCertificate(cert);
        } else {
            return edUserName.getValue();
        }
    }

    public String getPassword() {
        return edPassword.getValue();
    }

    public boolean useCertificate() {
        return cbUserCert.isSelected();
    }

    public String getStationName() {
        return edStation.getValue();
    }

    public EIsoLanguage getLanguage() {
        return getIsoLanguageFromIsoCode(edLanguage.getValue());
    }

    @Override
    protected DialogResult onClose(String action, DialogResult actionResult) {
        if (actionResult == DialogResult.ACCEPTED) {
            if (!selfCheck()) {
                return null;//do not close
            }
            WpsEnvironment environment = (WpsEnvironment) getEnvironment();
            if (edUserName.getValue() != null && !edUserName.getValue().isEmpty()) {
                environment.writeToCookies("ln", edUserName.getValue());
            }
            if (edStation.getValue() != null && !edStation.getValue().isEmpty()) {
                environment.writeToCookies("ls", edStation.getValue());
            }
            environment.writeToCookies("ll", edLanguage.getValue());
            try {
                getEnvironment().getConfigStore().beginGroup("LogonDialog");
                getEnvironment().getConfigStore().writeString("userName", edUserName.getValue());
                getEnvironment().getConfigStore().writeString("stationName", edStation.getValue());
                getEnvironment().getConfigStore().writeString("language", edLanguage.getValue());
            } finally {
                getEnvironment().getConfigStore().endGroup();
            }
        }
        return actionResult;
    }

    public void clear() {
        if (edPassword != null) {
            edPassword.setValue(null);
        }
    }
    
    private static EIsoLanguage getIsoLanguageFromIsoCode(final String isoCode){
        try{
            return isoCode==null || isoCode.isEmpty() ? null : EIsoLanguage.getForValue(isoCode);
        }catch(NoConstItemWithSuchValueError error){
            return null;
        }
    }
}
