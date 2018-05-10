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

package org.radixware.kernel.common.trace;

import java.lang.ref.WeakReference;

public class LocalTracerThreadGroup extends ThreadGroup implements ILocalTracerProvider {
    
    private volatile WeakReference<ILocalTracerProvider> localTraceProviderRef;
    private volatile long threadId = -1;
    
    public LocalTracerThreadGroup() {
        super(null);
    }
    
    public ILocalTracerProvider getLocalTraceProvider() {
        return localTraceProviderRef.get();
    }

    public void setLocalTraceProvider(ILocalTracerProvider localTraceProvider) {
        this.localTraceProviderRef = new WeakReference<>(localTraceProvider);
        final Thread t = localTraceProvider instanceof Thread ? (Thread)localTraceProvider : null;
        this.threadId = t == null ? -1 : t.getId();
    }
    
    public long getThreadId() {
        return threadId;
    }
    
    @Override
    public LocalTracer getLocalTracer() {
        final ILocalTracerProvider provider = getLocalTraceProvider();
        return provider == null ? null : provider.getLocalTracer();
    }
    
    public final LocalTracerThreadGroup getRootLocalTracerThreadGroup() {
        LocalTracerThreadGroup group = this;
        while (group.getParent() instanceof LocalTracerThreadGroup) {
            group = (LocalTracerThreadGroup) group.getParent();
        }
        return group;
    }
    
    public Thread[] getAllThreads() {
        final int estimatedThreadCount = this.activeCount() + 100;
        final Thread[] estimatedThreads = new Thread[estimatedThreadCount];
        this.enumerate(estimatedThreads);
        
        int threadCount = 0;
        for (Thread t: estimatedThreads) {
            if (t != null) {
                ++threadCount;
            }
        }
        
        final Thread[] threads = new Thread[threadCount];
        threadCount = 0;
        for (Thread t : estimatedThreads) {
            if (t != null) {
                threads[threadCount++] = t;
            }
        }
        
        return threads;
    }
    
    
    
    
    public static LocalTracerThreadGroup findLocalTracerThreadGroup(Thread sourceThread) {
        ThreadGroup group = sourceThread.getThreadGroup();
        while (group != null && !(group instanceof LocalTracerThreadGroup)) {
            group = group.getParent();
        }
        return (LocalTracerThreadGroup) group;
    }
    
    public static LocalTracerThreadGroup findLocalTracerThreadGroup() {
        return LocalTracerThreadGroup.findLocalTracerThreadGroup(Thread.currentThread());
    }
    
    public static LocalTracer findLocalTracer() {
        final LocalTracerThreadGroup lttg = findLocalTracerThreadGroup();
        final LocalTracer lt = lttg == null ? null : lttg.getLocalTracer();
        return lt;
    }

}
