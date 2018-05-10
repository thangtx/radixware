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

package org.radixware.wps.views;

import java.util.List;
import org.radixware.kernel.common.client.widgets.actions.Action;
import org.radixware.wps.ConnectionToolBar;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.rwt.VerticalBox;


public class NavigationView extends VerticalBox {

    private ConnectionToolBar connectionToolBar;

    public NavigationView(WpsEnvironment env) {
        connectionToolBar = new ConnectionToolBar(env);
        add(connectionToolBar);
        connectionToolBar.updateTranslations(env.getMessageProvider());
        connectionToolBar.afterConnect();        
        connectionToolBar.getHtml().getParent().setCss("border-bottom", "solid 1px #BBB");
    }

    public void updateToolBarState(final List<Action> addActions) {
        connectionToolBar.updateNavigationState(addActions);
    }
}
