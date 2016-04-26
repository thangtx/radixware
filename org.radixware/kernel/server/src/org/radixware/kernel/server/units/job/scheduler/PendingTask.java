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

package org.radixware.kernel.server.units.job.scheduler;

import java.sql.Timestamp;
import org.radixware.kernel.common.enums.ETaskStatus;


class PendingTask {

    final long taskId;
    final Timestamp execTime;
    final Timestamp lastExecTime;
    final Timestamp schedulingTime;
    final ETaskStatus status;
    final boolean hasRelatedJobs;
    final boolean isActualizableByRelatedJobs;
    final boolean skipIfExecuting;
    final String title;
    final long expiredPolicy;

    PendingTask(
            final long taskId,
            final String title,
            final Timestamp execTime,
            final Timestamp lastExecTime,
            final Timestamp schedulingTime,
            final ETaskStatus status,
            final boolean hasRelatedJobs,
            final boolean isActualizableByRelatedJobs,
            final boolean skipIfExecuting,
            final long expiredPolicy) {
        this.taskId = taskId;
        this.title = title;
        this.execTime = execTime;
        this.lastExecTime = lastExecTime;
        this.schedulingTime = schedulingTime;
        this.status = status;
        this.hasRelatedJobs = hasRelatedJobs;
        this.isActualizableByRelatedJobs = isActualizableByRelatedJobs;
        this.skipIfExecuting = skipIfExecuting;
        this.expiredPolicy = expiredPolicy;
    }
}
