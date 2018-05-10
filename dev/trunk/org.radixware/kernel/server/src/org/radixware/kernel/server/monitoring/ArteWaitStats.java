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

import org.radixware.kernel.server.arte.ArteProfiler;

public class ArteWaitStats {

    private final long cpuNanos;
    private final long dbNanos;
    private final long extNanos;
    private final long idleNanos;
    private final long queueNanos;

    public ArteWaitStats(long cpuNanos, long dbNanos, long extNanos, final long idleNanos, final long queueNanos) {
        this.cpuNanos = cpuNanos;
        this.dbNanos = dbNanos;
        this.extNanos = extNanos;
        this.idleNanos = idleNanos;
        this.queueNanos = queueNanos;
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

    public long getIdleNanos() {
        return idleNanos;
    }

    /**
     * Use getQueueNanos instead
     */
    @Deprecated
    public long getOtherNanos() {
        return queueNanos;
    }

    public long getQueueNanos() {
        return queueNanos;
    }

    public long totalNanos() {
        return cpuNanos + dbNanos + extNanos + idleNanos + queueNanos;
    }

    public ArteWaitStats substractFrom(final ArteWaitStats from) {
        return from == null ? null : new ArteWaitStats(from.getCpuNanos() - cpuNanos, from.getDbNanos() - dbNanos, from.getExtNanos() - extNanos, from.getIdleNanos() - idleNanos, from.getQueueNanos() - queueNanos);
    }

    public ArteWaitStats add(final long nanos, final ArteProfiler.EWaitType waitType) {
        return new ArteWaitStats(
                waitType == ArteProfiler.EWaitType.CPU ? cpuNanos + nanos : cpuNanos,
                waitType == ArteProfiler.EWaitType.DB ? dbNanos + nanos : dbNanos,
                waitType == ArteProfiler.EWaitType.EXT ? extNanos + nanos : extNanos,
                waitType == ArteProfiler.EWaitType.IDLE ? idleNanos + nanos : idleNanos,
                waitType == ArteProfiler.EWaitType.QUEUE ? queueNanos + nanos : queueNanos
        );
    }

    @Override
    public String toString() {
        return "(nanos) Cpu: " + cpuNanos + "; Db: " + dbNanos + "; Ext: " + extNanos + "; Idle: " + idleNanos + "; Other: " + idleNanos;
    }

}
