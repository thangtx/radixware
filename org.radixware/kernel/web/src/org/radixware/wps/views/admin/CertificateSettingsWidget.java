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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.kernel.common.ssl.KeystoreController;
import org.radixware.kernel.starter.config.ConfigEntry;
import org.radixware.kernel.starter.config.ConfigFileParseException;
import org.radixware.wps.WebServerRunParams;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.dialogs.EnterPasswordDialog;
import org.radixware.wps.rwt.Dialog;
import org.radixware.wps.rwt.ToolButton;
import org.radixware.wps.rwt.ValueEditor;
import org.radixware.wps.views.editors.valeditors.ValEditorController;
import org.radixware.wps.views.editors.valeditors.ValStrEditorController;

public final class CertificateSettingsWidget extends GroupWidget implements ConfigWidget {

    private final WpsEnvironment env;
    private final ValStrEditorController certAttrEditorController;
    private final ValStrEditorController keyStorePathEditorController;
    private final ValStrEditorController certificateAliasEditorController;
//    private final ValStrEditorController pwdEditorController;
    private final Map<ValEditorController, String> valEditorMap = new HashMap<>();
//    private String pwd;
    private String appliedCertAttrForUserName = null;
    private String appliedCertificateAlias = null;
    private String appliedServerKeystorePath = null;
    private String appliedPwd = null;
    private boolean wasSaved = false;
    
    public CertificateSettingsWidget(final WpsEnvironment env, String title) {
        super(title);
        this.env = env;
        MessageProvider mp = env.getMessageProvider();
        certAttrEditorController = new ValStrEditorController(env);
        keyStorePathEditorController = new ValStrEditorController(env);
        keyStorePathEditorController.setEnabled(false);
        keyStorePathEditorController.setMandatory(true);
        certificateAliasEditorController = new ValStrEditorController(env);
        addNewRow(mp.translate("AdminPanel", "Certificate attribute with user name:"), certAttrEditorController);
        addNewRow(mp.translate("AdminPanel", "Server keystore path:"), keyStorePathEditorController);
        addNewRow(mp.translate("PKCS11", "Certificate alias") + ":", certificateAliasEditorController);
        certAttrEditorController.setEnabled(KeystoreController.getServerKeystorePath() != null && !KeystoreController.getServerKeystorePath().isEmpty());
        certificateAliasEditorController.setEnabled(KeystoreController.getServerKeystorePath() != null && !KeystoreController.getServerKeystorePath().isEmpty());
        keyStorePathEditorController.addValueChangeListener(new ValueEditor.ValueChangeListener<String>() {

            @Override
            public void onValueChanged(String oldValue, String newValue) {
                certAttrEditorController.setEnabled(newValue != null && !newValue.isEmpty());
                certificateAliasEditorController.setEnabled(newValue != null && !newValue.isEmpty());
                if (newValue == null) {
                    certAttrEditorController.setValue(null);
                    certificateAliasEditorController.setValue(null);
                }
            }
        });
        valEditorMap.put(certAttrEditorController, "certAttrForAccName");
        valEditorMap.put(certificateAliasEditorController, "certificateAlias");
    }

    @Override
    public boolean validate() {
        return true;
    }

    @Override
    public void write(List<String> toRemove, List<ConfigEntry> toWrite) throws ConfigFileParseException {
        String keyStorePath = keyStorePathEditorController.getValue();
        if (keyStorePath != null && !keyStorePath.isEmpty()) {
            toWrite.add(new ConfigEntry("keyStoreFile", keyStorePath));
            for (Map.Entry<ValEditorController, String> entry : valEditorMap.entrySet()) {
                ValEditorController valEditorController = entry.getKey();
                Object value = valEditorController.getValue();
                String param = entry.getValue();
                if (value == null || ((value instanceof String) && ((String) value).isEmpty())) {
                    toRemove.add(param);
                } else {
                    toWrite.add(new ConfigEntry(param, String.valueOf(value)));
                }
            }
        }
    }

    @Override
    public void save() {
        appliedCertAttrForUserName = certAttrEditorController.getValue();
        appliedCertificateAlias = certificateAliasEditorController.getValue();
        appliedServerKeystorePath = keyStorePathEditorController.getValue();
        if (appliedServerKeystorePath != null && !appliedServerKeystorePath.isEmpty()) {
            WebServerRunParams.setCertAttrForUserName(appliedCertAttrForUserName);
            WebServerRunParams.setCertificateAlias(appliedCertificateAlias);
        }
        wasSaved = true;
    }

    @Override
    public void reread() {
        WebServerRunParams runParams = WebServerRunParams.readFromFile();
        keyStorePathEditorController.setValue(runParams.getCertificateKeystoreFile());
        certAttrEditorController.setValue(runParams.getCertAttrForUserName());
        certificateAliasEditorController.setValue(runParams.getCertificateAlias());
    }

    @Override
    public void load() {
        WebServerRunParams runParams = env.getRunParams();
        certAttrEditorController.setValue(wasSaved ? appliedCertAttrForUserName : runParams.getCertAttrForUserName());
        keyStorePathEditorController.setValue(KeystoreController.getServerKeystorePath());
        certificateAliasEditorController.setValue(wasSaved ? appliedCertificateAlias : runParams.getCertificateAlias());
    }

}
