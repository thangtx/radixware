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

package org.radixware.kernel.designer.debugger.actions;

import java.util.Collections;
import java.util.Set;
import org.netbeans.api.debugger.ActionsManager;
import org.netbeans.spi.debugger.ActionsProvider;
import org.netbeans.spi.debugger.ActionsProviderListener;
import org.netbeans.spi.debugger.ContextProvider;
import org.radixware.kernel.designer.debugger.RadixDebugger;


public class KillActionProvider extends ActionsProvider {

    private RadixDebugger debugger;
    private ContextProvider lookupProvider;

    public KillActionProvider(ContextProvider lookupProvider) {
        debugger = lookupProvider.lookupFirst(null, RadixDebugger.class);
        this.lookupProvider = lookupProvider;
    }

    @Override
    public Set getActions() {
        return Collections.singleton(ActionsManager.ACTION_KILL);
    }

    @Override
    public void doAction(Object action) {
        debugger.finish();
    }

    @Override
    public boolean isEnabled(Object action) {
        return true;
    }

    @Override
    public void addActionsProviderListener(ActionsProviderListener l) {
    }

    @Override
    public void removeActionsProviderListener(ActionsProviderListener l) {
    }
}
