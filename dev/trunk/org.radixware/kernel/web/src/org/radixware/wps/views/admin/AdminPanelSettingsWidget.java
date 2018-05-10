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

import java.util.List;
import org.radixware.kernel.starter.config.ConfigEntry;
import org.radixware.kernel.starter.config.ConfigFileParseException;
import org.radixware.wps.WebServerRunParams;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.views.editors.valeditors.ValStrEditorController;

public class AdminPanelSettingsWidget extends GroupWidget implements ConfigWidget {

    private final ValStrEditorController adminPanelUrl;
    private final ValStrEditorController adminUsers;

    public AdminPanelSettingsWidget(WpsEnvironment env, String title) {
        super(title);
        adminPanelUrl = new ValStrEditorController(env);
        adminPanelUrl.setMandatory(true);
        adminPanelUrl.setEnabled(false);
        addNewRow(env.getMessageProvider().translate("AdminPanel", "Admin panel URL:"), adminPanelUrl);
        adminUsers = new ValStrEditorController(env);
        adminUsers.setMandatory(true);
        adminUsers.setEnabled(false);
        addNewRow(env.getMessageProvider().translate("AdminPanel", "Admin users:"), adminUsers);
    }

    @Override
    public boolean validate() {
        return true;
    }

    @Override
    public void write(List<String> toRemove, List<ConfigEntry> toWrite) throws ConfigFileParseException {
    }

    @Override
    public void save() {
    }

    @Override
    public void reread() {
        adminPanelUrl.setValue(WebServerRunParams.getAdminPanelUrlParam());
        StringBuilder builder = new StringBuilder();
        for (String value : WebServerRunParams.getAdminUserNames()) {
            builder.append(value);
        }
        adminUsers.setValue(builder.toString());
    }

    @Override
    public void load() {
    }
}
