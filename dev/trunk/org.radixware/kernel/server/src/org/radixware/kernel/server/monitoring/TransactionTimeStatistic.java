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

package org.radixware.kernel.server.monitoring;


public class TransactionTimeStatistic {

    private final long cpuNanos;
    private final long dbNanos;
    private final long extNanos;

    public TransactionTimeStatistic(long cpuNanos, long dbNanos, long extNanos) {
        this.cpuNanos = cpuNanos;
        this.dbNanos = dbNanos;
        this.extNanos = extNanos;
    }

    public long getCpuNanos() {
        return cpuNanos;
    }

    public long getDbNanos() {
        return dbNanos;
    }

    public long getExtNanos() {
        return extNanos;
    }
    
    public long totalNanos() {
        return cpuNanos + dbNanos + extNanos;
    }

    @Override
    public String toString() {
        return "(nanos) Cpu: " + cpuNanos + "; Db: " + dbNanos + "; Ext: " + extNanos;
    }
}
