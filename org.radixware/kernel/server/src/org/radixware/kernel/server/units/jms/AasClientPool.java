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

package org.radixware.kernel.server.units.jms;

import java.sql.SQLException;
import java.util.LinkedList;
import javax.jms.JMSException;
import javax.jms.Message;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.server.units.Messages;


class AasClientPool {
    private final LinkedList<AdsUnitAdapter> busy;
    private final LinkedList<AdsUnitAdapter> free;
    
    private JmsHandlerUnit unit = null;
    AasClientPool(final JmsHandlerUnit unit) {
        busy = new LinkedList<>();
        free = new LinkedList<>();
        this.unit = unit;
    }

    final void onRequest(final Message msg) throws JMSException {
        try {
            captureClient().onRequest(msg);
        } catch (SQLException ex) {
            final String exStack = ExceptionTextFormatter.exceptionStackToString(ex);
            unit.getTrace().put(EEventSeverity.ERROR, Messages.ERR_IN_DB_QRY + exStack, Messages.MLS_ID_ERR_IN_DB_QRY, new ArrStr(unit.getTitle(), exStack), unit.getEventSource(), false);
        } catch (JmsHandlerRqCntLimitExeededException ex) {
            final String limitStr = String.valueOf(unit.getOptions().inSeanceCnt);
            unit.getTrace().put(EEventSeverity.ERROR, JmsHandlerMessages.ERR_IN_RQ_LIMIT_EXEEDED + ": " + limitStr, JmsHandlerMessages.MLS_ID_ERR_IN_RQ_LIMIT_EXEEDED, new ArrStr(limitStr, unit.getTitle()), unit.getEventSource(), false);
        }
    }

    private long getSize() {
        return busy.size() + free.size();
    }
    
    public boolean isBusy() {
        return unit.getOptions().inSeanceCnt != null && busy.size() >= unit.getOptions().inSeanceCnt.longValue();
    }

    private AdsUnitAdapter captureClient() throws SQLException, JmsHandlerRqCntLimitExeededException {
        if (isStopped()) {
            throw new IllegalUsageError("AAS client pool is stopped");
        }
        final AdsUnitAdapter client;
        if (free.isEmpty()) {
            if (unit.getOptions().inSeanceCnt == null || getSize() < unit.getOptions().inSeanceCnt.longValue()) {
                client = new AdsUnitAdapter(unit);
            } else {
                throw new JmsHandlerRqCntLimitExeededException(unit.getOptions().inSeanceCnt.longValue());
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
        unit = null;
    }

    boolean isStopped() {
        return unit == null;
    }
}