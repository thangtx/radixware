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

package org.radixware.kernel.explorer.dialogs.settings;

import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.explorer.env.ExplorerSettings;
import org.radixware.kernel.explorer.env.IExplorerSettings;
import org.radixware.kernel.explorer.widgets.ExplorerWidget;

abstract class SettingsWidget extends ExplorerWidget {

    protected final String group, subGroup, name;
    private final String settingCfgName;    

    public SettingsWidget(final IClientEnvironment environment, 
                                     final QWidget parent, 
                                     final String gr, 
                                     final String sub, 
                                     final String n) {
        super(environment, parent);        
        group = gr;
        subGroup = sub;
        name = n;
        final StringBuilder keyBuilder = new StringBuilder(SettingNames.SYSTEM);        
        if (group!=null && !group.isEmpty()){
            keyBuilder.append('/');
            keyBuilder.append(group);            
        }
        if (subGroup!=null && !subGroup.isEmpty()){
            keyBuilder.append('/');
            keyBuilder.append(subGroup);            
        }
        if (name!=null && !name.isEmpty()){
            keyBuilder.append('/');
            keyBuilder.append(name);
        }
        settingCfgName = keyBuilder.toString();
    }

    protected final IExplorerSettings getDefaultSettings() {
        return ((ExplorerSettings)getEnvironment().getConfigStore()).getDefaultSettings();
    }

    @SuppressWarnings("unchecked")
    public abstract void readSettings(IExplorerSettings src);

    public abstract void writeSettings(IExplorerSettings dst);

    public final String getSettingCfgName() {
        return settingCfgName;
    }

    public abstract void restoreDefaults();
    
    public boolean validate() {
        return true;
    }
}