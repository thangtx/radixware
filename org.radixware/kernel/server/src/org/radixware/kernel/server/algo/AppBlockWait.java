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

package org.radixware.kernel.server.algo;

import java.math.BigDecimal;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.server.exceptions.DatabaseError;
import org.radixware.kernel.server.types.Algorithm;

public class AppBlockWait {
    //return 0 - wait
    static public int invoke(final Algorithm algo) throws Exception {
        final AlgorithmExecutor executor = algo.getExecutor();
        final BigDecimal timeout = (BigDecimal)algo.getProperty("timeout");
        final Id strobId =Id.Factory.loadFrom((String)algo.getProperty("strobId"));
        if (executor.syncExecution || timeout == null || timeout.compareTo(new BigDecimal(AlgorithmExecutor.WAIT_TIMEOUT_BOUNDARY)) < 0) {
            //исполнить с синхронным ожиданием события
            if (timeout != null)
                java.lang.Thread.sleep(Math.round(timeout.doubleValue()*1000));
            return 0;
        } else {
            algo.scheduleTimeoutJob(timeout.doubleValue(), executor.getPropertyId("timeoutWID")); //подписка на onTimeout
            if (strobId != null)
                algo.scheduleStrob(strobId); //подписка на onStrob
            return -1;
        }
    }
    public static int resume(@SuppressWarnings("unused") final Algorithm algo) {
        final Arte arte = algo.getArte();
        final AlgorithmExecutor executor = algo.getExecutor();
        final Id strobId = executor.getCurrentThread().resumedStrobId;
        algo.setProperty("strobed", Boolean.valueOf(strobId != null));
        if (strobId != null)
            try {
                Long timeoutWID = (Long)algo.getProperty("timeoutWID");
                if (timeoutWID != null)
                    arte.getJobQueue().unsheduleJob(timeoutWID);
            } catch (RuntimeException e) {
                executor.trace(EEventSeverity.ERROR, e.getMessage());
            }
        return 0;
    }
}