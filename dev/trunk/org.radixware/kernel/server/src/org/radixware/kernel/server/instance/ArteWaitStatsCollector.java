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

package org.radixware.kernel.server.instance;

import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.server.instance.arte.ArteInstance;
import org.radixware.kernel.server.jdbc.RadixConnection;


public class ArteWaitStatsCollector extends Thread {

    private static final int COLLECT_INTERVAL_MILLIS = 5000;
    //
    private final Instance instance;
    private volatile boolean stopRequested = false;

    public ArteWaitStatsCollector(final Instance instance) {
        super("Instance #" + instance.getId() + " ArteProfilingDataCollector");
        setDaemon(true);
        this.instance = instance;
    }

    @Override
    public void run() {
        while (!stopRequested && !Thread.currentThread().isInterrupted()) {
            for (ArteInstance arteInstance : instance.getArtePool().getInstances(false)) {
                arteInstance.flushWaitStats();
            }
            try {
                Thread.sleep(COLLECT_INTERVAL_MILLIS);
            } catch (InterruptedException ex) {
                if (!stopRequested) {
                    instance.getTrace().put(EEventSeverity.WARNING, getName() + " was unexpectedly interrupted", EEventSource.INSTANCE);
                }
                return;
            }
        }
    }
    
    public void requestStop() {
        stopRequested = true;
        interrupt();
    }
}
