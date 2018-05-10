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
import org.radixware.wps.views.editors.valeditors.ValIntEditorController;

public class FileUploadingSettingsWidget extends GroupWidget implements ConfigWidget {

    private final WpsEnvironment env;
    private final ValIntEditorController uploadSoftLimitEditorController;
    private final ValIntEditorController uploadHardLimitEditorController;
    private int appliedUploadSoftLimit = 10;
    private boolean wasSaved = false;


    public FileUploadingSettingsWidget(WpsEnvironment env, String title) {
        super(title);
        this.env = env;
        uploadSoftLimitEditorController = new ValIntEditorController(env);
        uploadHardLimitEditorController = new ValIntEditorController(env);
        uploadHardLimitEditorController.setMandatory(true);
        uploadHardLimitEditorController.setEnabled(false);
        addNewRow(getEnvironment().getMessageProvider().translate("AdminPanel", "Require confirmation if the size exceeds::"), uploadSoftLimitEditorController);
        addNewRow(getEnvironment().getMessageProvider().translate("AdminPanel", "Prohibit if the size exceeds:"), uploadHardLimitEditorController);
    }

    @Override
    public boolean validate() {
        return true;
    }

    @Override
    public void write(List<String> toRemove, List<ConfigEntry> toWrite) throws ConfigFileParseException {
        if (uploadSoftLimitEditorController.getValue() == null || uploadSoftLimitEditorController.getValue() == 10) {
            toRemove.add("uploadFileSizeSoftLimitMb");
        } else {
            toWrite.add(new ConfigEntry("uploadFileSizeSoftLimitMb", uploadSoftLimitEditorController.getValue().toString()));
        }
    }

    @Override
    public void save() {
        Long uploadSoftLimit = uploadSoftLimitEditorController.getValue();
        appliedUploadSoftLimit = uploadSoftLimit == null ? 10 : uploadSoftLimit.intValue();
        WebServerRunParams.setUploadSoftLimitMb(appliedUploadSoftLimit);
        wasSaved = true;
    }

    @Override
    public void reread() {
        WebServerRunParams runParams = WebServerRunParams.readFromFile();
        uploadSoftLimitEditorController.setValue(Long.valueOf(runParams.getUploadSoftLimitMb()));
        uploadHardLimitEditorController.setValue(Long.valueOf(WebServerRunParams.getUploadHardLimitMb()));
    }

    @Override
    public void load() {
        WebServerRunParams runParams = env.getRunParams();
        uploadSoftLimitEditorController.setValue(wasSaved ? Long.valueOf(appliedUploadSoftLimit) : Long.valueOf(runParams.getUploadSoftLimitMb()));
        uploadHardLimitEditorController.setValue(Long.valueOf(WebServerRunParams.getUploadHardLimitMb()));
    }

}
