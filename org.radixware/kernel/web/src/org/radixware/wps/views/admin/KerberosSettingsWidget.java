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
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.meta.mask.EditMaskList;
import org.radixware.kernel.common.enums.EClientAuthentication;
import org.radixware.kernel.starter.config.ConfigEntry;
import org.radixware.kernel.starter.config.ConfigFileParseException;
import org.radixware.wps.ERemoteKerberosAuthScheme;
import org.radixware.wps.WebServerRunParams;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.rwt.ValueEditor;
import org.radixware.wps.views.editors.valeditors.ValBoolEditorController;
import org.radixware.wps.views.editors.valeditors.ValEditorController;
import org.radixware.wps.views.editors.valeditors.ValListEditorController;
import org.radixware.wps.views.editors.valeditors.ValStrEditorController;

public class KerberosSettingsWidget extends GroupWidget implements ConfigWidget {

    private final WpsEnvironment env;
    private final ValListEditorController kerberosAuthRequiredEditorController;
    private final ValBoolEditorController disableSPNEGOAuthEditorController;
    private final ValStrEditorController principalNameEditorController;
    private final ValStrEditorController keytabPathEditorController;
    private final ValListEditorController<Long> remoteAuthSchemeEditorController;
    private final ValBoolEditorController credentialsDelegationAllowedEditorController;
    private final ValBoolEditorController useCertificateEditorController;
    private final ValBoolEditorController downgradeNtlmEditorController;
    private WebServerRunParams.KrbWpsOptions appliedKrbOptions = null;
    private boolean wasSaved = false;
    private final Map<ValEditorController, String> valEditorMap = new HashMap<>();

    public KerberosSettingsWidget(WpsEnvironment env, String title) {
        super(title);
        this.env = env;
        final EditMaskList clientAuthenticationEditMask = new EditMaskList();
        for (EClientAuthentication clientAuthentication : EClientAuthentication.values()) {
            clientAuthenticationEditMask.addItem(env.getMessageProvider().translate("ConnectionEditor", clientAuthentication.name()), clientAuthentication.ordinal());
        }
        kerberosAuthRequiredEditorController = new ValListEditorController(env);
        kerberosAuthRequiredEditorController.setMandatory(true);
        kerberosAuthRequiredEditorController.setEditMask(clientAuthenticationEditMask);
        disableSPNEGOAuthEditorController = new ValBoolEditorController(env);
        disableSPNEGOAuthEditorController.setMandatory(true);
        principalNameEditorController = new ValStrEditorController(env);
        principalNameEditorController.setMandatory(true);
        keytabPathEditorController = new ValStrEditorController(env);
        keytabPathEditorController.setMandatory(true);
        final EditMaskList authSchemeEditMask = new EditMaskList();
        for (ERemoteKerberosAuthScheme authSceme : ERemoteKerberosAuthScheme.values()) {
            authSchemeEditMask.addItem(authSceme.name(), Long.valueOf(authSceme.ordinal()));
        }
        remoteAuthSchemeEditorController = new ValListEditorController<>(env, authSchemeEditMask);
        remoteAuthSchemeEditorController.setMandatory(true);
        credentialsDelegationAllowedEditorController = new ValBoolEditorController(env);
        credentialsDelegationAllowedEditorController.setMandatory(true);
        useCertificateEditorController = new ValBoolEditorController(env);
        useCertificateEditorController.setMandatory(true);
        downgradeNtlmEditorController = new ValBoolEditorController(env);
        downgradeNtlmEditorController.setMandatory(true);
        kerberosAuthRequiredEditorController.addValueChangeListener(new ValueEditor.ValueChangeListener() {

            @Override
            public void onValueChanged(Object oldValue, Object newValue) {
                boolean isEnabled = false;
                if (!newValue.equals(Long.valueOf(EClientAuthentication.None.ordinal()))) {
                    isEnabled = true;
                }
                KerberosSettingsWidget.this.setKerberosOptionsEnabled(isEnabled);
            }
        });
        MessageProvider mp = env.getMessageProvider();
        addNewRow(mp.translate("AdminPanel", "Kerberos authentication required:"), kerberosAuthRequiredEditorController);
        addNewRow(mp.translate("AdminPanel", "Disable SPNEGO authentication:"), disableSPNEGOAuthEditorController);
        addNewRow(mp.translate("AdminPanel", "Principal name:"), principalNameEditorController);
        addNewRow(mp.translate("AdminPanel", "Keytab path:"), keytabPathEditorController);
        addNewRow(mp.translate("AdminPanel", "Identification data transfer:"), remoteAuthSchemeEditorController);
        addNewRow(mp.translate("AdminPanel", "Credentials delegation allowed:"), credentialsDelegationAllowedEditorController);
        addNewRow(mp.translate("AdminPanel", "Use certificate:"), useCertificateEditorController);
        addNewRow(mp.translate("AdminPanel", "Use kerberos instead of NTLM:"), downgradeNtlmEditorController);
        valEditorMap.put(disableSPNEGOAuthEditorController, "disableSPNEGOAuth");
        valEditorMap.put(principalNameEditorController, "wpsSpn");
        valEditorMap.put(keytabPathEditorController, "keyTabFile");
        valEditorMap.put(credentialsDelegationAllowedEditorController, "useDelegatedCredentials");
        valEditorMap.put(useCertificateEditorController, "fallbackToCertificateAuth");
        valEditorMap.put(downgradeNtlmEditorController, "downgradeNtlm");
    }

    @Override
    public boolean validate() {
        String principalName = principalNameEditorController.getValue();
        String keyTabPath = keytabPathEditorController.getValue();
        if ((Long) kerberosAuthRequiredEditorController.getValue() != EClientAuthentication.None.ordinal()) {
            if (principalName == null || principalName.isEmpty()) {
                env.messageError(env.getMessageProvider().translate("AdminPanel", "Invalid value"), env.getMessageProvider().translate("AdminPanel", "Principal name should be set"));
                return false;
            } else if (keyTabPath == null || keyTabPath.isEmpty()) {
                env.messageError(env.getMessageProvider().translate("AdminPanel", "Invalid value"), env.getMessageProvider().translate("AdminPanel", "Keytab path value should be set"));
                return false;
            }
        }
        return true;
    }

    @Override
    public void write(List<String> toRemove, List<ConfigEntry> toWrite) throws ConfigFileParseException {
        String principalName = principalNameEditorController.getValue();
        String keyTabPath = keytabPathEditorController.getValue();
        if (principalName != null && !principalName.isEmpty() && keyTabPath != null && !keyTabPath.isEmpty()) {
            for (Map.Entry<ValEditorController, String> entry : valEditorMap.entrySet()) {
                ValEditorController valEditorController = entry.getKey();
                Object value = valEditorController.getValue();
                String param = entry.getValue();
                if (valEditorController instanceof ValBoolEditorController) {
                    if (valEditorController.getValue().equals(Boolean.TRUE)) {
                        toWrite.add(new ConfigEntry(param, null));
                    } else {
                        toRemove.add(param);
                    }
                } else {
                    if (value == null || ((value instanceof String) && ((String) value).isEmpty())) {
                        toRemove.add(param);
                    } else {
                        toWrite.add(new ConfigEntry(param, String.valueOf(value)));
                    }
                }
            }
            Long remoteAuthSchemeValue = remoteAuthSchemeEditorController.getValue();
            ERemoteKerberosAuthScheme remoteKerberosAuthScheme = remoteAuthSchemeValue == null
                    ? ERemoteKerberosAuthScheme.DISABLED : ERemoteKerberosAuthScheme.values()[remoteAuthSchemeValue.intValue()];
            toWrite.add(new ConfigEntry("remoteKrbAuth", remoteKerberosAuthScheme.name()));
            Long kerberosAuthRequired = (Long) kerberosAuthRequiredEditorController.getValue();
            EClientAuthentication clientAuthentication = EClientAuthentication.getForValue(kerberosAuthRequired);
            toWrite.add(new ConfigEntry("krbAuthPolicy", clientAuthentication.getName()));
        } else {
            for (Map.Entry<ValEditorController, String> entry : valEditorMap.entrySet()) {
                String param = entry.getValue();
                toRemove.add(param);
                toRemove.add("remoteKrbAuth");
                toRemove.add("krbAuthPolicy");
            }
        }
    }

    @Override
    public void save() {
        String principalName = principalNameEditorController.getValue();
        String keyTabPath = keytabPathEditorController.getValue();
        if (principalName != null && !principalName.isEmpty() && keyTabPath != null && !keyTabPath.isEmpty()) {
            Long kerberosAuthRequired = (Long) kerberosAuthRequiredEditorController.getValue();
            EClientAuthentication clientAuthentication = EClientAuthentication.getForValue(kerberosAuthRequired);
            Long remoteAuthSchemeValue = remoteAuthSchemeEditorController.getValue();
            ERemoteKerberosAuthScheme remoteKerberosAuthScheme = remoteAuthSchemeValue == null
                    ? ERemoteKerberosAuthScheme.DISABLED : ERemoteKerberosAuthScheme.values()[remoteAuthSchemeValue.intValue()];
            boolean creditialsDelegationAllowed = credentialsDelegationAllowedEditorController.getValue();
            boolean useCertificate = useCertificateEditorController.getValue();
            boolean downgradeNtlm = downgradeNtlmEditorController.getValue();
            appliedKrbOptions = new WebServerRunParams.KrbWpsOptions(
                    clientAuthentication, principalName, keyTabPath, remoteKerberosAuthScheme, creditialsDelegationAllowed, downgradeNtlm, useCertificate, disableSPNEGOAuthEditorController.getValue());
        } else {
            appliedKrbOptions = null;
        }
        WebServerRunParams.setKerberosOptions(appliedKrbOptions);
        wasSaved = true;
    }

    @Override
    public void reread() {
        WebServerRunParams runParams = WebServerRunParams.readFromFile();
        WebServerRunParams.KrbWpsOptions kerberosOptions = runParams.getKerberosOptions();
        fill(kerberosOptions);
    }

    @Override
    public void load() {
        WebServerRunParams.KrbWpsOptions kerberosOptions = wasSaved ? appliedKrbOptions : env.getRunParams().getKerberosOptions();
        fill(kerberosOptions);
    }

    private void fill(WebServerRunParams.KrbWpsOptions kerberosOptions) {
        EClientAuthentication clientAuthentication;
        if (kerberosOptions == null || !kerberosOptions.isKerberosOptionsEnabled()) {
            clientAuthentication = EClientAuthentication.None;
        } else if (kerberosOptions.isKerberosAuthRequired()) {
            clientAuthentication = EClientAuthentication.Required;
        } else {
            clientAuthentication = EClientAuthentication.Enabled;
        }
        kerberosAuthRequiredEditorController.setValue(Long.valueOf(clientAuthentication.ordinal()));
        disableSPNEGOAuthEditorController.setValue(kerberosOptions == null ? false : !kerberosOptions.isSpnego());
        String principalName = kerberosOptions == null ? null : kerberosOptions.getPrincipalName();
        principalNameEditorController.setValue(principalName);
        String keytabPath = kerberosOptions == null ? null : kerberosOptions.getKeyTabPath();
        keytabPathEditorController.setValue(keytabPath);
        int remoteKerberosAuthDisabledNumber;
        for (remoteKerberosAuthDisabledNumber = 0; remoteKerberosAuthDisabledNumber < ERemoteKerberosAuthScheme.values().length; remoteKerberosAuthDisabledNumber++) {
            if (ERemoteKerberosAuthScheme.values()[remoteKerberosAuthDisabledNumber] == ERemoteKerberosAuthScheme.DISABLED) {
                break;
            }
        }
        remoteAuthSchemeEditorController.setValue(kerberosOptions == null ? Long.valueOf(remoteKerberosAuthDisabledNumber) : kerberosOptions.getRemoteAuthScheme().ordinal());
        credentialsDelegationAllowedEditorController.setValue(kerberosOptions == null ? false : kerberosOptions.isCredentialsDelegationAllowed());
        useCertificateEditorController.setValue(kerberosOptions == null ? false : kerberosOptions.canUseCertificate());
        downgradeNtlmEditorController.setValue(kerberosOptions == null ? false : kerberosOptions.downgradeNtlm());
        boolean isKerberosOptionsEnabled = principalName != null && !principalName.isEmpty() && keytabPath != null && !keytabPath.isEmpty();
        setKerberosOptionsEnabled(isKerberosOptionsEnabled);
    }

    private void setKerberosOptionsEnabled(boolean isEnabled) {
        disableSPNEGOAuthEditorController.setEnabled(isEnabled);
        principalNameEditorController.setEnabled(isEnabled);
        keytabPathEditorController.setEnabled(isEnabled);
        remoteAuthSchemeEditorController.setEnabled(isEnabled);
        credentialsDelegationAllowedEditorController.setEnabled(isEnabled);
        useCertificateEditorController.setEnabled(isEnabled);
        downgradeNtlmEditorController.setEnabled(isEnabled);
    }

}
