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

package org.radixware.kernel.server.units.job.executor;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.server.utils.PriorityResourceManager;


final class AasClientPool {

    private static final long OVERLOAD_WARN_PERIOD_MILLIS = 60000;
    private JobExecutorUnit unit;
    private final List<JeAasClient> busy;
    private final List<JeAasClient> free;
    private static final String SCP_ANY = "any-free-client";
    private final List<JeAasClient> usedOnLastIteration = new ArrayList<>();
    private final PriorityResourceManager priorityManager = new PriorityResourceManager();

    public static enum EInvokeResult {

        ALL_CLIENTS_ARE_BUSY,
        ERROR_ON_INVOKE,
        OK
    }

    AasClientPool(final JobExecutorUnit unit) {
        this.unit = unit;
        busy = new LinkedList<>();
        free = new LinkedList<>();
    }

    public void newIteration() {
        usedOnLastIteration.clear();
    }
    
    public void onUnitOptionsChanged() {
        priorityManager.setOptions(new PriorityResourceManager.Options(
                unit.getOptions().parallelCount,
                unit.getOptions().aboveNormalDelta,
                unit.getOptions().highDelta,
                unit.getOptions().veryHighDelta,
                unit.getOptions().criticalDelta));
    }

    EInvokeResult invoke(final Job job) throws SQLException {
        final JeAasClient client = captureClient(job);
        if (client != null) {
            try {
                usedOnLastIteration.add(client);
                client.invoke(job);
                if (client.isLastRqFaulted()) {
                    return EInvokeResult.ERROR_ON_INVOKE;
                } else {
                    return EInvokeResult.OK;
                }
            } catch (RuntimeException ex) {
                client.closeActiveSeances();
                freeClient(client);
                throw ex;
            }
        } else {
            unit.getDbQueries().doPriorityBoosting();
            return EInvokeResult.ALL_CLIENTS_ARE_BUSY;
        }
    }

    private long getSize() {
        return busy.size() + free.size();
    }

    private JeAasClient captureClient(final Job job) throws SQLException {
        if (isStopped()) {
            throw new IllegalUsageError("AAS client pool is stopped");
        }
        final PriorityResourceManager.Ticket ticket = priorityManager.requestTicketNow(job.getRadixPriority());
        if (ticket != null) {
            try {
                JeAasClient client = captureFreeClient(job.getScpName());//capture free client with appropriate scp
                if (client == null) {
                    client = captureFreeClient(SCP_ANY);//capture any free client
                }
                if (client == null) {
                    client = new JeAasClient(unit);
                }
                busy.add(client);
                client.setPriorityTicket(ticket);
                return client;
            } catch (RuntimeException ex) {
                priorityManager.releaseTicket(ticket);
                throw ex;
            }
        } else {
            return null;
        }
    }

    private JeAasClient captureFreeClient(final String scpName) {
        if (free.isEmpty()) {
            return null;
        }
        Iterator<JeAasClient> freeIterator = free.iterator();
        while (freeIterator.hasNext()) {
            final JeAasClient freeClient = freeIterator.next();
            if (Utils.equals(freeClient.getLastScpName(), scpName) || scpName == SCP_ANY) {
                freeIterator.remove();
                return freeClient;
            }
        }
        return null;
    }

    final void freeClient(final JeAasClient client) {
        if (isStopped()) {
            throw new IllegalUsageError("AAS client pool is stopped");
        }
        busy.remove(client);
        priorityManager.releaseTicket(client.getPriorityTicket());
        client.setPriorityTicket(null);
        if (getSize() < getMaximumThreadCount()) {
            free.add(client);
        } else {
            client.stop();
        }
    }

    private long getMaximumThreadCount() {
        return unit.getOptions().parallelCount
                + unit.getOptions().aboveNormalDelta
                + unit.getOptions().highDelta
                + unit.getOptions().veryHighDelta
                + unit.getOptions().criticalDelta;
    }

    final void stop() {
        for (JeAasClient c : free) {
            c.stop();
        }
        free.clear();
        final JeAasClient[] arrBusy = new JeAasClient[busy.size()];
        busy.toArray(arrBusy);
        for (JeAasClient c : arrBusy) {
            c.interruptWaitingForConnectionSeances();
        }
        unit.processAllEventsForAasClientPoolStop();
        for (JeAasClient c : arrBusy) {
            c.stop();
        }
        busy.clear();
        unit = null;
    }

    boolean isStopped() {
        return unit == null;
    }

    void disconnectAllFree() {
        for (JeAasClient c : free) {
            c.closeActiveSeances();
        }
    }

    long getBusyCount() {
        return busy.size();
    }

    final boolean isAllLastRqFaulted() {
        if (busy.isEmpty() && free.isEmpty()) {
            return false;
        }
        for (JeAasClient c : free) {
            if (!c.isLastRqFaulted()) {
                return false;
            }
        }
        for (JeAasClient c : busy) {
            if (!c.isLastRqFaulted()) {
                return false;
            }
        }
        return true;
    }

    final boolean isAllRqFromLastIterationFaulted() {
        if (usedOnLastIteration.isEmpty()) {
            return false;
        }
        for (JeAasClient client : usedOnLastIteration) {
            if (!client.isLastRqFaulted()) {
                return false;
            }
        }
        return true;
    }
}
