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


public class RunningTask {

    final long taskId;
    final String title;
    final long secSinceExpectedStart;
    final long expectedDurationSec;
    final long schedulingTimeMillis;

    public RunningTask(long taskId, String taskName, long secSinceExpectedStart, long expectedDurationSec, long schedulingTimeMillis) {
        this.taskId = taskId;
        this.secSinceExpectedStart = secSinceExpectedStart;
        this.expectedDurationSec = expectedDurationSec;
        this.schedulingTimeMillis = schedulingTimeMillis;
        this.title = taskName;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + (int) (this.taskId ^ (this.taskId >>> 32));
        hash = 89 * hash + (int) (this.schedulingTimeMillis ^ (this.schedulingTimeMillis >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RunningTask other = (RunningTask) obj;
        if (this.taskId != other.taskId) {
            return false;
        }
        if (this.schedulingTimeMillis != other.schedulingTimeMillis) {
            return false;
        }
        return true;
    }
    
    public String getFullTitle() {
        return "#" + taskId + " '" + title + "'";
    }
    
}
