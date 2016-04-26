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

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.server.types.ProfileStatisticEntry;
import org.radixware.kernel.server.utils.ProfileStatistic;


public class InstanceProfiler {
    private final Instance instance;
    private final Object statSem = new Object();
    /**
     * @GuarededBy("statSem")
     */
    private ProfileStatistic stat = new ProfileStatistic();

    InstanceProfiler(final Instance inst) {
        instance = inst;
    }

    /**
     * @NotThreadsafe("Shoud be called from instance thread only")
     */
    void close() {
        flush();
    }
    /**
     * @NotThreadsafe("Shoud be called from instance thread only")
     */
    void flush() {
        Collection<ProfileStatisticEntry> lst;
        synchronized(statSem){
            if (stat.isEmpty()){
                return;
            }
            lst = stat.getResult();
            stat.clear();
        }
        flush(lst);
    }

    /**
     * @Threadsafe
     * @param result
     */
    public void register(final Collection<ProfileStatisticEntry> lst) {
        synchronized(statSem){
            for (ProfileStatisticEntry e : lst)
                stat.register(e);
        }
    }

    /**
     * @NotThreadsafe("Shoud be called from instance thread only")
     */
    private void flush(final Collection<ProfileStatisticEntry> lst){
        instance.getDbQueries().save(lst);
    }
}
