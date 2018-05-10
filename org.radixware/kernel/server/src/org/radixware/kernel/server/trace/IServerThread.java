/*
 * Copyright (c) 2008-2017, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. This Source Code is distributed
 * WITHOUT ANY WARRANTY; including any implied warranties but not limited to
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Mozilla Public License, v. 2.0. for more details.
 */

package org.radixware.kernel.server.trace;

import java.lang.management.ThreadInfo;
import org.radixware.kernel.common.trace.ILocalTracerProvider;
import org.radixware.kernel.common.trace.IRadixTraceProvider;
import org.radixware.kernel.server.instance.IConnectionProvider;
import org.radixware.kernel.server.instance.InstanceThreadStateRecord;
import org.radixware.kernel.server.instance.ThreadStateSelfRegistrator;


public interface IServerThread extends IRadixTraceProvider, ILocalTracerProvider, IConnectionProvider {

    /**
     * 
     * @return true if this thread can be treated as dead
     */
    public boolean isAborted();

    long getStartTimeNanos();

    InstanceThreadStateRecord getThreadStateRecord(ThreadInfo threadInfo);
    
    ThreadStateSelfRegistrator getThreadStateSelfRegistrator();
}
