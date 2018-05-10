/*
 * Copyright (c) 2008-2017, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */

package org.radixware.kernel.server.trace;

import org.radixware.kernel.common.trace.LocalTracer;
import org.radixware.kernel.common.trace.LocalTracerThreadGroup;
import org.radixware.kernel.server.instance.ThreadStateSelfRegistrator;

public abstract class ServerThread extends Thread implements IServerThread {
    protected final long startTimeNanos = System.nanoTime();
    protected final LocalTracer localTracer;
    private final ThreadStateSelfRegistrator registrator = new ThreadStateSelfRegistrator(this);

    public ServerThread(final String name, final Runnable runnable, final ClassLoader ctxClassLoader, final LocalTracer tracer) {
        super(new LocalTracerThreadGroup(), runnable);
        ((LocalTracerThreadGroup) getThreadGroup()).setLocalTraceProvider(this);
        if (name != null) {
            super.setName(name);
        }
        setContextClassLoader(ctxClassLoader);
        this.localTracer = tracer;
    }
    
    @Override
    public LocalTracer getLocalTracer() {
        return localTracer;
    }
    
    @Override
    public long getStartTimeNanos() {
        return startTimeNanos;
    }

    @Override
    public ThreadStateSelfRegistrator getThreadStateSelfRegistrator() {
        return registrator;
    }
}
