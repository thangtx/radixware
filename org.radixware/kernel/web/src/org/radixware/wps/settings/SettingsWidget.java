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


package org.radixware.wps.settings;

import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.WpsSettings;
import org.radixware.wps.rwt.Container;
import org.radixware.wps.rwt.UIObject;


public abstract class SettingsWidget extends Container {

    final IClientEnvironment e;
    public String subGroup, group, name;
    protected Object defaultValue;

    public SettingsWidget(WpsEnvironment env, UIObject parent, String group, String subGroup, String name, Object defaultValue) {
        super();
        this.setParent(parent);
        this.e = env;
        this.group = group;
        this.subGroup = subGroup;
        this.name = name;
    }

    public String getSettingCfgName() {
        if (subGroup == null || subGroup.isEmpty()) {
            return SettingNames.SYSTEM + "/" + group + "/" + name;
        } else {
            return SettingNames.SYSTEM + "/" + group + "/" + subGroup + "/" + name;
        }
    }
    
    
    protected void setDefaultValue(Object val) {
        this.defaultValue = val;
    }

    protected Object getDefaultValue() {
        return defaultValue;
    }
    

    @SuppressWarnings("unchecked")
    public abstract void readSettings(WpsSettings src);

    public abstract void writeSettings(WpsSettings dst);

    public abstract void restoreDefaults();
}
