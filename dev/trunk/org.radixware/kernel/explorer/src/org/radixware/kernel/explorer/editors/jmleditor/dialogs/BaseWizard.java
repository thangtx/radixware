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

package org.radixware.kernel.explorer.editors.jmleditor.dialogs;

import com.trolltech.qt.gui.QWidget;
import com.trolltech.qt.gui.QWizard;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.explorer.env.ExplorerSettings;



public class BaseWizard extends QWizard {
    private final String key;
    private final ExplorerSettings settings;

    public BaseWizard(final QWidget parent, final ExplorerSettings settings, final String dlgName){
	super(parent);
    this.settings = settings;
    setWizardStyle(WizardStyle.ClassicStyle);
    //Geometry of dialog widgets saving not correctly. 
    //Widgets grow up by time. Fix or delete saving config.
	key = dlgName != null ? SettingNames.SYSTEM + "/" + dlgName + "/wizard_geometry" : SettingNames.SYSTEM + "/" + getClass().getSimpleName() + "/wizard_geometry";
	loadGeometryFromConfig();
	finished.connect(this, "saveGeometryToConfig()");
    }

    @SuppressWarnings("unused")
    protected void saveGeometryToConfig(){
    	settings.writeQByteArray(key, saveGeometry() );
    }

    protected final void loadGeometryFromConfig(){
    	if (settings.contains(key)){
            restoreGeometry(settings.readQByteArray(key));
    	}
    }

}
