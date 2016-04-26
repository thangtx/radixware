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

package org.radixware.kernel.server.units.nethub;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Map;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.types.Bin;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.server.units.Messages;
import org.radixware.kernel.server.units.nethub.AdsUnitAdminAdapter.InvalidMessageType;
import org.radixware.kernel.server.units.nethub.exceptions.NetHubInRqCntLimitExeededException;


class AasClientPool {

    private final AdsUnitAdminAdapter adminClient;
    private final AdsUnitRecvAdapter recvClient;
    private final LinkedList<AdsUnitAdapter> busy;
    private final LinkedList<AdsUnitAdapter> free;
    private NetHubUnit unit = null;

    AasClientPool(final NetHubUnit unit) throws SQLException {
        busy = new LinkedList<AdsUnitAdapter>();
        free = new LinkedList<AdsUnitAdapter>();
        adminClient = new AdsUnitAdminAdapter(unit);
        recvClient = new AdsUnitRecvAdapter(unit);
        this.unit = unit;
    }

    final void onRequest(final byte[] mess, final String uniqueKey) {
        try {
            captureClient().onRequest(mess, uniqueKey);
        } catch (SQLException ex) {
            final String exStack = ExceptionTextFormatter.exceptionStackToString(ex);
            unit.getTrace().put(EEventSeverity.ERROR, Messages.ERR_IN_DB_QRY + exStack, Messages.MLS_ID_ERR_IN_DB_QRY, new ArrStr(unit.getTitle(), exStack), unit.getEventSource(), false);
        } catch (NetHubInRqCntLimitExeededException ex) {
            final String limitStr = String.valueOf(unit.getOptions().inSeanceCnt);
            unit.getTrace().put(EEventSeverity.ERROR, NetHubMessages.ERR_IN_RQ_LIMIT_EXEEDED + ": " + limitStr, NetHubMessages.MLS_ID_ERR_IN_RQ_LIMIT_EXEEDED, new ArrStr(limitStr, unit.getTitle()), unit.getEventSource(), false);
        }
    }

    final void onInvalidMessage(final byte[] mess, final long type) {
        if (type == InvalidMessageType.DUPLICATE && !unit.getOptions().toProcessDuplicatedRq)
            return;
        if (type == InvalidMessageType.UNCORRELATED && !unit.getOptions().toProcessUncorrelatedRs)
            return;
        try {
            captureClient().onInvalidMessage(mess, type);
        } catch (SQLException ex) {
            final String exStack = ExceptionTextFormatter.exceptionStackToString(ex);
            unit.getTrace().put(EEventSeverity.ERROR, Messages.ERR_IN_DB_QRY + exStack, Messages.MLS_ID_ERR_IN_DB_QRY, new ArrStr(unit.getTitle(), exStack), unit.getEventSource(), false);
        } catch (NetHubInRqCntLimitExeededException ex) {
            final String limitStr = String.valueOf(unit.getOptions().inSeanceCnt);
            unit.getTrace().put(EEventSeverity.ERROR, NetHubMessages.ERR_IN_RQ_LIMIT_EXEEDED + ": " + limitStr, NetHubMessages.MLS_ID_ERR_IN_RQ_LIMIT_EXEEDED, new ArrStr(limitStr, unit.getTitle()), unit.getEventSource(), false);
        }
    }
    
    final void onAfterRecv(final byte[] mess, final Map<Bin, Map<String, String>> requests) {
        recvClient.onAfterRecv(mess, requests);
    }

    final void onConnect() {
        unit.getNetHubDbQueries().setConnected(true);
        if (!unit.getOptions().toProcessConnect)
            return;
        adminClient.onConnect();
    }
    
    final void onDisconnect() {
        unit.getNetHubDbQueries().setConnected(false);
        final AdsUnitAdapter[] arrBusy = new AdsUnitAdapter[busy.size()];
        busy.toArray(arrBusy);
        for (AdsUnitAdapter c : arrBusy) {
            c.closeActiveSeances();
            freeClient(c);
        }
        if (!unit.getOptions().toProcessDisconnect)
            return;
        adminClient.onDisconnect();
    }

    final void onInactivityTimer() {
        adminClient.onInactivityTimer();
    }
    
    final void beforeStart() {
        if (!unit.getOptions().toProcessStart) {
            unit.onBeforeStartProcessed();
        } else
            adminClient.beforeStart();
    }

    final void beforeStop() {
        if (!unit.getOptions().toProcessStop) {
            unit.onBeforeStopProcessed();
        } else
            adminClient.beforeStop();
    }
    
    private long getSize() {
        return busy.size() + free.size();
    }

    private AdsUnitAdapter captureClient() throws SQLException, NetHubInRqCntLimitExeededException {
        if (isStopped()) {
            throw new IllegalUsageError("AAS client pool is stopped");
        }
        final AdsUnitAdapter client;
        if (free.isEmpty()) {
            if (unit.getOptions().inSeanceCnt == null || getSize() < unit.getOptions().inSeanceCnt.longValue()) {
                client = new AdsUnitAdapter(unit);
            } else {
                throw new NetHubInRqCntLimitExeededException(unit.getOptions().inSeanceCnt.longValue());
            }
        } else {
            client = free.pop();
        }
        busy.push(client);
        return client;
    }

    final void freeClient(final AdsUnitAdapter client) {
        if (isStopped()) {
            throw new IllegalUsageError("AAS client pool is stopped");
        }
        busy.remove(client);
        if (unit.getOptions().inSeanceCnt == null || getSize() < unit.getOptions().inSeanceCnt.longValue()) {
            free.add(client);
        } else {
            client.stop();
        }
    }

    final void stop() {
        for (AdsUnitAdapter c : free) {
            c.stop();
        }
        free.clear();
        final AdsUnitAdapter[] arrBusy = new AdsUnitAdapter[busy.size()];
        busy.toArray(arrBusy);
        for (AdsUnitAdapter c : arrBusy) {
            c.stop();
        }
        busy.clear();
        adminClient.stop();
        unit = null;
    }

    boolean isStopped() {
        return unit == null;
    }
}