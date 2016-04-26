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

import org.netbeans.api.debugger.DebuggerEngine.Destructor;
import org.netbeans.api.debugger.Session;
import org.netbeans.spi.debugger.ContextProvider;
import org.netbeans.spi.debugger.DebuggerEngineProvider;
import org.radixware.kernel.designer.debugger.impl.KernelSourcePathProvider;


public class RadixDebuggerEngineProvider extends DebuggerEngineProvider {

    private Destructor destructor;
    private Session session;

    public RadixDebuggerEngineProvider(ContextProvider contextProvider) {
        session = contextProvider.lookupFirst(null, Session.class);
    }

    @Override
    public String[] getLanguages() {
        return new String[]{"Jml"};
    }

    @Override
    public String getEngineTypeID() {
        return RadixDebugger.ENGINE_ID;
    }

    @Override
    public Object[] getServices() {
        return new Object[]{KernelSourcePathProvider.getInstance()};
    }

    @Override
    public void setDestructor(Destructor desctuctor) {
        this.destructor = desctuctor;
    }

    Destructor getDestructor() {
        return destructor;
    }
}
