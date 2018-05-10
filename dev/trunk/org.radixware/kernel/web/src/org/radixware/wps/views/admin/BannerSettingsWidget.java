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
import org.radixware.kernel.starter.config.ConfigEntry;
import org.radixware.kernel.starter.config.ConfigFileParseException;
import org.radixware.wps.WebServerRunParams;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.rwt.Banner;
import org.radixware.wps.views.editors.valeditors.ValEditorController;
import org.radixware.wps.views.editors.valeditors.ValStrEditorController;

public class BannerSettingsWidget extends GroupWidget implements ConfigWidget{

    private final WpsEnvironment env;
    private final ValStrEditorController bannerDirPathController;
    private final ValStrEditorController bannerFileNameController;
    private final ValStrEditorController bannerFrameHeightController;
    private final ValStrEditorController bannerFrameStyleController;
    private final Map<ValEditorController, String> valEditorMap = new HashMap<>();
    private Banner.Options appliedBannerOptions = null;
    private boolean wasSaved = false;
    
    public BannerSettingsWidget(WpsEnvironment env, String title) {
        super(title);
        this.env = env;
        MessageProvider mp = env.getMessageProvider();
        bannerDirPathController = new ValStrEditorController(env);
        bannerFileNameController = new ValStrEditorController(env);
        bannerFrameHeightController = new ValStrEditorController(env);
        bannerFrameStyleController = new ValStrEditorController(env);
        valEditorMap.put(bannerDirPathController, "bannerDir");
        valEditorMap.put(bannerFileNameController, "bannerFile");
        valEditorMap.put(bannerFrameHeightController, "bannerFrameHeight");
        valEditorMap.put(bannerFrameStyleController, "bannerFrameStyle");
        addNewRow(mp.translate("AdminPanel", "Banner directory path:"), bannerDirPathController);
        addNewRow(mp.translate("AdminPanel", "Banner file name:"), bannerFileNameController);
        addNewRow(mp.translate("AdminPanel", "Banner frame height:"), bannerFrameHeightController);
        addNewRow(mp.translate("AdminPanel", "Banner frame style:"), bannerFrameStyleController);
    }
    
    @Override
    public boolean validate() {
        return true;
    }

    @Override
    public void write(List<String> toRemove, List<ConfigEntry> toWrite) throws ConfigFileParseException {
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

    @Override
    public void save() {
        String bannerDirPath = bannerDirPathController.getValue();
        String bannerFileName = bannerFileNameController.getValue();
        if (bannerDirPath != null && !bannerDirPath.isEmpty() && bannerFileName != null && !bannerFileName.isEmpty()) {
            appliedBannerOptions = new Banner.Options(bannerDirPath, bannerFileName, bannerFrameStyleController.getValue(), bannerFrameHeightController.getValue());
            WebServerRunParams.setBannerOptions(appliedBannerOptions);
        } else {
            WebServerRunParams.setBannerOptions(null);
        }
        wasSaved = true;
    }

    @Override
    public void reread() {
        Banner.Options bannerOptions = WebServerRunParams.readFromFile().getBannerOptions();
        bannerDirPathController.setValue(bannerOptions == null ? null : bannerOptions.getDirPath());
        bannerFileNameController.setValue(bannerOptions == null ? null : bannerOptions.getFileName());
        bannerFrameHeightController.setValue(bannerOptions == null ? null : bannerOptions.getFrameHeight());
        bannerFrameStyleController.setValue(bannerOptions == null ? null : bannerOptions.getFrameStyle());
    }

    @Override
    public void load() {
        Banner.Options bannerOptions = wasSaved ? appliedBannerOptions : env.getRunParams().getBannerOptions();
        bannerDirPathController.setValue(bannerOptions == null ? null : bannerOptions.getDirPath());
        bannerFileNameController.setValue(bannerOptions == null ? null : bannerOptions.getFileName());
        bannerFrameHeightController.setValue(bannerOptions == null ? null : bannerOptions.getFrameHeight());
        bannerFrameStyleController.setValue(bannerOptions == null ? null : bannerOptions.getFrameStyle());
    }
    
}
