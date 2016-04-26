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

package org.radixware.kernel.designer.debugger.impl.ui;

import java.util.concurrent.Executor;
import org.netbeans.spi.debugger.ContextProvider;
import org.netbeans.spi.viewmodel.AsynchronousModelFilter;
import org.netbeans.spi.viewmodel.UnknownTypeException;
import org.radixware.kernel.designer.debugger.RadixDebugger;


public class RadixAsynchronousModelFilter implements AsynchronousModelFilter {

    private Executor executor;

    public RadixAsynchronousModelFilter(ContextProvider contextProvider) {
        RadixDebugger debugger = contextProvider.lookupFirst(null, RadixDebugger.class);
        executor = debugger.getRequestProcessor();
    }

    @Override
    public Executor asynchronous(Executor original, CALL asynchCall, Object node) throws UnknownTypeException {
        switch (asynchCall) {
            case CHILDREN:
            case VALUE:
                return executor;
            case SHORT_DESCRIPTION:
            case DISPLAY_NAME:
                return CURRENT_THREAD;
            default:
                return DEFAULT;
        }
    }
}
