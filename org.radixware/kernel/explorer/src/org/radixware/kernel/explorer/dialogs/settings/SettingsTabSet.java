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

package org.radixware.kernel.explorer.dialogs.settings;

import com.trolltech.qt.gui.QTabWidget;
import com.trolltech.qt.gui.QWidget;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;


class SettingsTabSet extends QTabWidget{        
    
    private final List<Integer> openedTabs = new LinkedList<>();
    private final IClientEnvironment environment;        
    private final List<SettingsWidget> settingWidgets;
    private final ISettingsProvider settingsProvider;
    
    public SettingsTabSet(final IClientEnvironment environment, 
                                     final ISettingsProvider settingsProvider,
                                     final List<SettingsWidget> settingWidgets){
        this.environment = environment;
        this.settingWidgets = settingWidgets;
        this.settingsProvider = settingsProvider;
        this.currentChanged.connect(this, "openPage(int)");  
    }

    protected final void openPage(final int tabIndex){
        final QWidget tab = widget(tabIndex);
        if (tab instanceof ISettingsPage){
            final ISettingsPage page = (ISettingsPage)tab;
            if (!openedTabs.contains(tabIndex)){
                openedTabs.add(tabIndex);
                page.open(environment, settingsProvider, settingWidgets);
            }
        }
    }
}