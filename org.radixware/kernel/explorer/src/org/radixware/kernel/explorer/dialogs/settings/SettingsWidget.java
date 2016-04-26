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
import java.util.Map;
import java.util.WeakHashMap;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.explorer.env.ExplorerSettings;
import org.radixware.kernel.explorer.widgets.ExplorerWidget;

public abstract class SettingsWidget extends ExplorerWidget {

     public final String group, subGroup, name;
    protected final static Map<IClientEnvironment, ExplorerSettings> defaultSettings = new WeakHashMap<IClientEnvironment, ExplorerSettings>(1);	

    public SettingsWidget(IClientEnvironment environment, final QWidget parent, final String gr, final String sub, final String n) {
        super(environment, parent);
        group = gr;
        subGroup = sub;
        name = n;
    }

    protected final ExplorerSettings getDefaultSettings() {
        ExplorerSettings s = defaultSettings.get(getEnvironment());
        if (s == null) {
            s = new ExplorerSettings(getEnvironment());
            defaultSettings.put(getEnvironment(), s);
        }
        return s;
    }

    @SuppressWarnings("unchecked")
    public abstract void readSettings(ExplorerSettings src);

    public abstract void writeSettings(ExplorerSettings dst);

    public String getSettingCfgName() {
        if (subGroup == null || subGroup.isEmpty()) {
            return SettingNames.SYSTEM + "/" + group + "/" + name;
        } else {
            return SettingNames.SYSTEM + "/" + group + "/" + subGroup + "/" + name;
        }
    }

    public abstract void restoreDefaults();
}