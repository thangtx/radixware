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

package org.radixware.kernel.designer.debugger;

import org.netbeans.spi.debugger.ContextProvider;
import org.netbeans.spi.debugger.SessionProvider;


public class RadixDebuggerLaunchingSessionProvider extends SessionProvider {

    private ContextProvider contextProvider;
    private boolean isExplorerDebug = false;

    public RadixDebuggerLaunchingSessionProvider(ContextProvider contextProvider) {
        this.contextProvider = contextProvider;
        RadixDebuggerConnector connector = contextProvider.lookupFirst(null, RadixDebuggerConnector.class);
        if (connector != null) {
            isExplorerDebug = connector.isExplorerDebug();
        }
    }

    @Override
    public String getSessionName() {
        return "RadixWare Debugger Session " + (isExplorerDebug ? "(Explorer)" : "(Server)");
    }

    @Override
    public String getLocationName() {
        return "localhost";
    }

    @Override
    public String getTypeID() {
        return RadixDebugger.SESSION_ID;
    }

    @Override
    public Object[] getServices() {
        return new Object[0];
    }
}