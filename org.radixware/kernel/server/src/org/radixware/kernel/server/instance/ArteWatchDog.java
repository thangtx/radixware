/*
 * Copyright (c) 2008-2018, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. This Source Code is distributed
 * WITHOUT ANY WARRANTY; including any implied warranties but not limited to
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Mozilla Public License, v. 2.0. for more details.
 */
package org.radixware.kernel.server.instance;

import java.util.List;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.common.utils.SystemPropUtils;
import org.radixware.kernel.server.instance.arte.ArteInstance;
import org.radixware.kernel.server.instance.arte.ArtePool;
import org.radixware.kernel.server.jdbc.RadixConnection;

public class ArteWatchDog extends Thread {

    private static final int CHECK_DISCONNECT_INTERVAL_MILLIS = 5000;
    private static final int CHECK_ACTIVITY_INTERVAL_MILLIS = SystemPropUtils.getIntSystemProp("rdx.arte.check.activity.interval.millis", 100);
    private static final int ARTE_DEACTIVATION_DB_MILLIS = SystemPropUtils.getIntSystemProp("rdx.arte.deactivation.db.millis", 2 * CHECK_ACTIVITY_INTERVAL_MILLIS);
    private static final int SLEEP_MILLIS = Math.min(CHECK_ACTIVITY_INTERVAL_MILLIS, CHECK_DISCONNECT_INTERVAL_MILLIS);
    public static final String THREAD_NAME_MARKER = "ArteWatchDog";

    //
    private final Instance instance;
    private volatile boolean stopRequested = false;
    private long lastDisconnectCheckMillis = 0;
    private long lastActivityCheckMillis = 0;
    private volatile int deactivationsByLongDbQuery = 0;

    public ArteWatchDog(final Instance instance) {
        super("Instance #" + instance.getId() + " " + THREAD_NAME_MARKER);
        setDaemon(true);
        this.instance = instance;
    }

    @Override
    public void run() {
        while (!stopRequested && !Thread.currentThread().isInterrupted()) {
            final long curMillis = System.currentTimeMillis();
            final boolean checkDisconnect = curMillis - lastDisconnectCheckMillis > CHECK_DISCONNECT_INTERVAL_MILLIS;
            if (checkDisconnect) {
                lastDisconnectCheckMillis = curMillis;
            }
            final boolean checkActivity = instance.isUseActiveArteLimits() && (curMillis - lastActivityCheckMillis > CHECK_ACTIVITY_INTERVAL_MILLIS);
            if (checkActivity) {
                lastActivityCheckMillis = curMillis;
            }
            final ArtePool pool = instance.getArtePool();
            if (pool != null) {
                final List<ArteInstance> arteList = pool.getInstances(false);
                if (arteList != null) {
                    for (ArteInstance arteInstance : arteList) {
                        final RadixConnection conn = (RadixConnection) arteInstance.getDbConnection();
                        if (checkDisconnect && conn != null) {
                            try {
                                if (conn.closeIfClientDisconnected()) {
                                    instance.getTrace().put(EEventSeverity.EVENT, "'" + arteInstance.getThread().getName() + "' current db operation was cancelled because client disconnected, excuting sql: \n" + conn.getSqlWhenForciblyClosed(), EEventSource.INSTANCE);
                                }
                            } catch (Exception ex) {
                                instance.getTrace().put(EEventSeverity.WARNING, "Exception on ARTE #" + arteInstance.getSeqNumber() + " status checking: " + ExceptionTextFormatter.throwableToString(ex), EEventSource.INSTANCE);
                            }
                        }
                        if (checkActivity && conn != null) {
                            try {
                                if (conn .deactivateArteIfQueryIsLonger(ARTE_DEACTIVATION_DB_MILLIS)) {
                                    deactivationsByLongDbQuery++;
                                }
                            } catch (Exception ex) {
                                instance.getTrace().put(EEventSeverity.WARNING, "Exception on ARTE #" + arteInstance.getSeqNumber() + " activity checking: " + ExceptionTextFormatter.throwableToString(ex), EEventSource.INSTANCE);
                            }
                        }
                    }
                }
            }
            try {
                Thread.sleep(SLEEP_MILLIS);
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

    public int getDeactivationsByLongDbQuery() {
        return deactivationsByLongDbQuery;
    }

}
