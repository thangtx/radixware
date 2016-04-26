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


public class FrequencyPerSecondStat extends AbstractStat<Double> {
    private double curCnt = 0;

    public FrequencyPerSecondStat(final MetricParameters parameters, final long curPeriodStartMillis) {
        super(parameters, curPeriodStartMillis);
    }

    @Override
    protected void appendInternal(final RegisteredItem<Double> entry) {
        curCnt += entry.val == null ? 0 : entry.val;
    }

    @Override
    public void newPeriodStarted() {
        curCnt = 0;
    }

    @Override
    public StatValue closePeriod() {
        return new StatValue(null, null, curCnt / (getParameters().getPeriodMillis() / 1000));
    }
    
}
