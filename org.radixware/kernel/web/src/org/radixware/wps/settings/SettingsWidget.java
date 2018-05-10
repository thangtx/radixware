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

import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.meta.mask.validators.ValidationResult;
import org.radixware.wps.DefaultSettings;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.WpsSettings;
import org.radixware.wps.rwt.Container;
import org.radixware.wps.rwt.UIObject;


abstract class SettingsWidget extends Container {

    private final WpsEnvironment environment;
    protected String subGroup, group, name;
    private final String settingsConfigName;

    public SettingsWidget(final WpsEnvironment env, 
                          final UIObject parent, 
                          final String group, 
                          final String subGroup, 
                          final String name) {
        super();
        this.setParent(parent);
        environment = env;
        this.group = group;
        this.subGroup = subGroup;
        this.name = name;
        final StringBuilder cfgNameBuilder = new StringBuilder(SettingNames.SYSTEM);
        cfgNameBuilder.append('/');
        cfgNameBuilder.append(group);
        cfgNameBuilder.append('/');
        if (subGroup != null && !subGroup.isEmpty()){
            cfgNameBuilder.append(subGroup);
            cfgNameBuilder.append('/');
        }
        cfgNameBuilder.append(name);
        settingsConfigName = cfgNameBuilder.toString();        
    }
    
    protected final WpsEnvironment getWpsEnvironment(){
        return environment;
    }

    public final String getSettingCfgName() {
        return settingsConfigName;
    }
/*        
    protected void setDefaultValue(Object val) {
        this.defaultValue = val;
    }

    protected Object getDefaultValue() {
        return defaultValue;
    }*/
    
    protected final String readDefaultValue(){
        return DefaultSettings.getInstance().getValue(getSettingCfgName());
    }
    

    @SuppressWarnings("unchecked")
    public abstract void readSettings(WpsSettings src);

    public abstract void writeSettings(WpsSettings dst);

    public abstract void restoreDefaults();
    
    public boolean validate() {
        return true;
    }
}
