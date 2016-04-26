/*
* Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at http://mozilla.org/MPL/2.0/.
* This Source Code is distributed WITHOUT ANY WARRANTY; including any 
* implied warranties but not limited to warranty of MERCHANTABILITY 
* or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
* License, v. 2.0. for more details.
*/

package org.radixware.kernel.server.monitoring;

import org.radixware.kernel.server.types.ProfileStatisticEntry;

/**
 *
 * @author dsafonov
 */
class DurationStat extends AbstractStat<ProfileStatisticEntry> {
    private long curMinNanos = Long.MAX_VALUE;
    private long curMaxNanos = 0;
    private long curTotalNanos = 0;
    private long curCnt = 0;

    public DurationStat(final MetricParameters parameters, final long curPeriodStartMillis) {
        super(parameters, curPeriodStartMillis);
    }

    @Override
    public StatValue closePeriod() {
        return new StatValue(curMinNanos < Long.MAX_VALUE ? 1. * curMinNanos / 1000000 : 0, 1. * curMaxNanos / 1000000, curCnt > 0 ? (1. * curTotalNanos / 1000000) / curCnt : 0.);
    }

    @Override
    public void newPeriodStarted() {
        curCnt = 0;
        curMinNanos = Long.MAX_VALUE;
        curMaxNanos = 0;
        curTotalNanos = 0;
    }

    @Override
    protected void appendInternal(final RegisteredItem<ProfileStatisticEntry> entry) {
        curCnt += entry.val.getCount();
        curTotalNanos += entry.val.getDurationNanos();
        curMinNanos = Math.min(entry.val.getMinDurationNanos(), curMinNanos);
        curMaxNanos = Math.max(entry.val.getMaxDurationNanos(), curMaxNanos);
    }

}
