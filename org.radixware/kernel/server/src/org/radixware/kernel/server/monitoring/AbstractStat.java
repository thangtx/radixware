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

import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.exceptions.RadixError;


public abstract class AbstractStat<T> {

    private long curPeriodStartMillis;
    private final List<IStatValue> values = new ArrayList<>();
    private MetricParameters parameters;
    private EMetricState state = EMetricState.ACTIVE;

    public AbstractStat(final MetricParameters parameters, final long curPeriodStartMillis) {
        this.curPeriodStartMillis = curPeriodStartMillis;
        this.parameters = parameters;
    }

    public final void appendAll(final List<RegisteredItem<T>> items) {
        for (RegisteredItem<T> item : items) {
            append(item);
        }
    }

    public final void append(final RegisteredItem<T> item) {
        if (item.regTimeMillis < getCurPeriodStartMillis()) {
            return;
        }
        processToTime(item.regTimeMillis);
        appendInternal(item);
    }


    public List<MetricRecord> popRecords() {
        final ArrayList<MetricRecord> records = new ArrayList<>();
        for (IStatValue value : values) {
            records.add(new MetricRecord(parameters, value));
        }
        values.clear();
        return records;
    }

    protected void processToTime(final long timeMillis) {
        if (getParameters().getPeriodMillis() <= 0) {
            throw new RadixError("Period must be greater than 0");
        }
        if (curPeriodStartMillis < 0) {
            return;//not started yet
        }
        while (timeMillis > curPeriodStartMillis + getParameters().getPeriodMillis()) {
            final StatValue value = closePeriod();
            values.add(new StatValue(value, getCurPeriodStartMillis(), getCurPeriodStartMillis() + getParameters().getPeriodMillis()));
            curPeriodStartMillis += getParameters().getPeriodMillis();
            newPeriodStarted();
        }
    }

    public final void flush(final long timeMillis) {
        processToTime(timeMillis);
    }

    protected void reset(final long newStartPeriodMillis) {
        curPeriodStartMillis = newStartPeriodMillis;
        values.clear();
    }

    protected abstract StatValue closePeriod();

    protected abstract void newPeriodStarted();

    protected abstract void appendInternal(RegisteredItem<T> item);

    public EMetricState getState() {
        return state;
    }

    public void setState(final EMetricState state) {
        this.state = state;
    }

    public MetricParameters getParameters() {
        return parameters;
    }

    public void setParameters(final MetricParameters parameters) {
        this.parameters = parameters;
    }

    public long getCurPeriodStartMillis() {
        return curPeriodStartMillis;
    }
}
