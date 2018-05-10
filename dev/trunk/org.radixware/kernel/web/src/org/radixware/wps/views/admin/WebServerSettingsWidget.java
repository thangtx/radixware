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
import java.util.Map.Entry;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.starter.config.ConfigEntry;
import org.radixware.kernel.starter.config.ConfigFileParseException;
import org.radixware.wps.WebServerRunParams;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.views.editors.valeditors.ValBoolEditorController;
import org.radixware.wps.views.editors.valeditors.ValEditorController;
import org.radixware.wps.views.editors.valeditors.ValIntEditorController;
import org.radixware.wps.views.editors.valeditors.ValStrEditorController;

public class WebServerSettingsWidget extends GroupWidget implements ConfigWidget {

    private final WpsEnvironment env;
    private final MessageProvider mp;
    private final ValStrEditorController connectionsFileEditorController;
    private final ValStrEditorController settingsDatabasePathEditorController;
    private final ValIntEditorController sessionInactiveIntervalEditorController;
    private final ValBoolEditorController sshRequiredEditorController;
    private final ValBoolEditorController developmentModeEditorController;
    private final ValBoolEditorController restoreTreePositionEditorController;
    private String appliedConnectionsFile = null;
    private String appliedSettingsDatabaseDir = null;
    private int appliedSessionInactiveInterval = 0;
    private boolean wasSaved = false;
    private final Map<ValEditorController, String> valEditorMap = new HashMap<>();

    public WebServerSettingsWidget(WpsEnvironment env, String title) {
        super(title);
        this.env = env;
        mp = env.getMessageProvider();
        connectionsFileEditorController = new ValStrEditorController(env);
        connectionsFileEditorController.setMandatory(true);
        settingsDatabasePathEditorController = new ValStrEditorController(env);
        sessionInactiveIntervalEditorController = new ValIntEditorController(env);
        sshRequiredEditorController = new ValBoolEditorController(env);
        sshRequiredEditorController.setMandatory(true);
        sshRequiredEditorController.setEnabled(false);
        developmentModeEditorController = new ValBoolEditorController(env);
        developmentModeEditorController.setMandatory(true);
        developmentModeEditorController.setEnabled(false);
        restoreTreePositionEditorController = new ValBoolEditorController(env);
        restoreTreePositionEditorController.setMandatory(true);
        restoreTreePositionEditorController.setEnabled(false);        

        addNewRow(mp.translate("AdminPanel", "Connections file:"), connectionsFileEditorController);
        addNewRow(mp.translate("AdminPanel", "Settings database dir:"), settingsDatabasePathEditorController);
        addNewRow(mp.translate("AdminPanel", "Session inactive interval:"), sessionInactiveIntervalEditorController);
        addNewRow(mp.translate("AdminPanel", "SSH required:"), sshRequiredEditorController);
        addNewRow(mp.translate("AdminPanel", "Development mode:"), developmentModeEditorController);
        addNewRow(mp.translate("AdminPanel", "Restore tree position:"), restoreTreePositionEditorController);

        valEditorMap.put(connectionsFileEditorController, "connectionsFile");
        valEditorMap.put(settingsDatabasePathEditorController, "settingsDatabasePath");
        valEditorMap.put(sessionInactiveIntervalEditorController, "sessionMaxInactiveInterval");
        valEditorMap.put(sshRequiredEditorController, "sshRequired");
        valEditorMap.put(developmentModeEditorController, "development");
        valEditorMap.put(restoreTreePositionEditorController, "restoreTreePosition");
    }

    @Override
    public boolean validate() {
        String connectionsFile = connectionsFileEditorController.getValue();
        if (connectionsFile == null || connectionsFile.isEmpty()) {
            env.messageError(mp.translate("AdminPanel", "Invalid value"), mp.translate("AdminPanel", "Connections file value must be set"));
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void write(List<String> toRemove, List<ConfigEntry> toWrite) throws ConfigFileParseException {
        for (Entry<ValEditorController, String> entry : valEditorMap.entrySet()) {
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
    }

    @Override
    public void save() {
        appliedConnectionsFile = connectionsFileEditorController.getValue();
        WebServerRunParams.setConnectionsFile(appliedConnectionsFile);
        appliedSettingsDatabaseDir = settingsDatabasePathEditorController.getValue();
        WebServerRunParams.setSettingsDatabaseDir(appliedSettingsDatabaseDir);
        Long sessionInactiveInterval = sessionInactiveIntervalEditorController.getValue();
        appliedSessionInactiveInterval = sessionInactiveInterval == null ? 0 : sessionInactiveInterval.intValue();
        WebServerRunParams.setSessionInactiveInterval(appliedSessionInactiveInterval);
        wasSaved = true;
    }

    @Override
    public void reread() {
        WebServerRunParams runParams = WebServerRunParams.readFromFile();
        connectionsFileEditorController.setValue(runParams.getConnectionsFile());
        settingsDatabasePathEditorController.setValue(runParams.getSettingsDatabaseDir());
        sessionInactiveIntervalEditorController.setValue(Long.valueOf(runParams.getSessionInactiveInteraval()));
        sshRequiredEditorController.setValue(WebServerRunParams.isSshRequired());
        developmentModeEditorController.setValue(WebServerRunParams.getIsDevelopmentMode());
        restoreTreePositionEditorController.setValue(WebServerRunParams.restoreTreePosition());
    }

    @Override
    public void load() {
        WebServerRunParams runParams = env.getRunParams();
        connectionsFileEditorController.setValue(wasSaved ? appliedConnectionsFile : runParams.getConnectionsFile());
        settingsDatabasePathEditorController.setValue(wasSaved ? appliedSettingsDatabaseDir : runParams.getSettingsDatabaseDir());
        sessionInactiveIntervalEditorController.setValue(wasSaved ? Long.valueOf(appliedSessionInactiveInterval) : Long.valueOf(runParams.getSessionInactiveInteraval()));
    }

}
