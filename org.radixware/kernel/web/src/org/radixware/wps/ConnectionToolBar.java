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

package org.radixware.wps;

import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.widgets.actions.Action;
import org.radixware.wps.rwt.ToolBar;


public class ConnectionToolBar extends ToolBar {  

    private List<Action> addActions = new LinkedList<>();
    private WpsEnvironment env;

    public ConnectionToolBar(WpsEnvironment env, boolean navigatorMode) {
        this.env = env;
        this.addAction(env.connectAction);
        this.addAction(env.disconnectAction);
        this.addAction(env.checkForUpdatesAction);
        this.addAction(env.changePasswordAction);
        this.addAction(env.appearanceSettingsAction);
        if (navigatorMode) {
        }
//        if (WebServerRunParams.getIsDevelopmentMode()) {
//            this.addAction(env.checkForUpdatesAction);
//        }
        setHeight(25);
    }

    public void updateNavigationState(List<Action> addActions) {
        for (Action a : this.addActions) {
            removeAction(a);
        }
        this.addActions.clear();
        for (Action a : addActions) {
            this.addAction(a);
            this.addActions.add(a);
        }
    }
}
