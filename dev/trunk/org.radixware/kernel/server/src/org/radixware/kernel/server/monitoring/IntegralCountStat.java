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

import java.util.List;

public class IntegralCountStat extends AbstractStat<Integer> {

    private int curMinValue = Integer.MAX_VALUE;
    private int curMaxValue = Integer.MIN_VALUE;
    private int curValue = 0;
    private long curSum = 0;
    private long lastValueSetupTime = -1;

    public IntegralCountStat(final MetricParameters parameters) {
        super(parameters, -1);
    }

    @Override
    protected StatValue closePeriod() {
        curSum += ((getCurPeriodStartMillis() + getParameters().getPeriodMillis()) - lastValueSetupTime) * curValue;
        return new StatValue((double) curMinValue, (double) curMaxValue, ((double) curSum) / getParameters().getPeriodMillis());
    }

    @Override
    protected void newPeriodStarted() {
        curMinValue = curMaxValue = curValue;
        lastValueSetupTime = getCurPeriodStartMillis();
        curSum = 0;
    }

    @Override
    protected void appendInternal(final RegisteredItem<Integer> item) {
        if (item != null && item.val != null) {
            curMinValue = Math.min(curMinValue, item.val);
            curMaxValue = Math.max(curMaxValue, item.val);
            curSum += curValue * (item.regTimeMillis - lastValueSetupTime);
            curValue = item.val;
            lastValueSetupTime = item.regTimeMillis;
        }
    }

    public void start(final RegisteredItem<Integer> item) {
        reset(item.regTimeMillis);
        appendInternal(item);
    }

    @Override
    protected void reset(long newStartPeriodMillis) {
        curMinValue = Integer.MAX_VALUE;
        curMaxValue = Integer.MIN_VALUE;
        curValue = 0;
        curSum = 0;
        super.reset(newStartPeriodMillis);
    }

    public boolean isStarted() {
        return lastValueSetupTime > -1;
    }

    public IStatValue flushAndGetLastValue() {
        flush(System.currentTimeMillis());
        final List<MetricRecord> records = popRecords();
        if (records != null && !records.isEmpty()) {
            return records.get(records.size() - 1).getStatValue();
        }
        return null;
    }

    public int getCurrentValue() {
        return curValue;
    }

    public int getCurrentMinValue() {
        return curMinValue;
    }

    public int getCurrentMaxValue() {
        return curMaxValue;
    }

    public double calcCurAvgValue() {
        final long curMillis = System.currentTimeMillis();
        if (curMillis <= getCurPeriodStartMillis()) {
            return 0;
        }
        final long sum = curSum + (curMillis - lastValueSetupTime) * curValue;
        return ((double) sum) / (curMillis - getCurPeriodStartMillis());
    }
}
