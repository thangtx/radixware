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

package org.radixware.kernel.common.client.dashboard;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Properties;

/**
 *
 * @author npopov
 */
public class DashTimeRangeProp implements IDashInheritableProp {
    
    private EDashPropSource propSource;
    private final HistoricalDiagramSettings.TimeRange timeRange;
    private final Timestamp timeFrom;
    private final Timestamp timeTo;
    private int periodUnit;
    private int period;

    public DashTimeRangeProp(EDashPropSource propSource, HistoricalDiagramSettings.TimeRange timeRange, Timestamp timeFrom, Timestamp timeTo, double period, int periodUnit) {
        this.propSource = propSource;
        this.timeRange = timeRange;
        final long curTime = System.currentTimeMillis();
        this.timeFrom = timeFrom != null ? new Timestamp(timeFrom.getTime()) : new Timestamp(curTime - 60 * 60 * 1000) ;
        this.timeTo = timeTo != null ? new Timestamp(timeTo.getTime()) : new Timestamp(curTime);
        setPeriod(period, periodUnit);
    }
    
    public DashTimeRangeProp(DashTimeRangeProp prop, EDashPropSource src) {
        this.propSource = src;
        this.timeRange = prop.getTimeRange();
        this.timeFrom = prop.getTimeFrom();
        this.timeTo = prop.getTimeTo();
        setPeriod(prop.getPeriod(), prop.getPeriodUnit());
    }

    @Override
    public EDashPropSource getPropSource() {
        return propSource;
    }
    
    public HistoricalDiagramSettings.TimeRange getTimeRange() {
        return timeRange;
    }

    public Timestamp getTimeFrom() {
        return new Timestamp(timeFrom.getTime());
    }

    public Timestamp getTimeTo() {
        return new Timestamp(timeTo.getTime());
    }

    public int getPeriodUnit() {
        return periodUnit;
    }

    public int getPeriod() {
        return period;
    }
    
    private void setPeriod(double res, int timeUnit) {
        if (!(Double.compare(res, Math.round(res)) == 0)) {
            if (timeUnit == Calendar.HOUR) {
                period = (int) (res * 60 * 60 * 1000);
            } else if (timeUnit == Calendar.MINUTE) {
                period = (int) (res * 60 * 1000);
            } else if (timeUnit == Calendar.SECOND) {
                period = (int) (res * 1000);
            }
            periodUnit = Calendar.MILLISECOND;
        } else {
            period = (int) res;
            periodUnit = timeUnit;
        }
    }
    
    public long getLastSeconds() {
        long periodSec = period;
        switch (periodUnit) {
            case java.util.Calendar.HOUR:
                periodSec = (periodSec * 60 * 60);
                break;
            case java.util.Calendar.MINUTE:
                periodSec = (periodSec * 60);
                break;
            case java.util.Calendar.SECOND:
                //do nothing
                break;
        }
        return periodSec;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof DashTimeRangeProp)) {
            return false;
        }
        DashTimeRangeProp other = (DashTimeRangeProp) obj;
        if (propSource != other.getPropSource()) {
            return false;
        }
        if (timeRange != other.getTimeRange()) {
            return false;
        }
        if (timeFrom.getTime() != other.getTimeFrom().getTime()) {
            return false;
        }
        if (timeTo.getTime() != other.getTimeTo().getTime()) {
            return false;
        }
        if (getLastSeconds() != other.getLastSeconds()) {
            return false;
        }
        return true;
    }
    
    private enum FieldNamesToStore {
        PROP_SOURCE,
        TIME_RANGE,
        TIME_FROM,
        TIME_TO,
        PERIOD,
        PERIOD_UNIT
    }
    
    public Properties store() {
        Properties prop = new Properties();
        prop.setProperty(FieldNamesToStore.PROP_SOURCE.toString(), propSource.toString());
        prop.setProperty(FieldNamesToStore.TIME_RANGE.toString(), timeRange.toString());
        prop.setProperty(FieldNamesToStore.TIME_FROM.toString(), Long.toString(timeFrom.getTime()));
        prop.setProperty(FieldNamesToStore.TIME_TO.toString(), Long.toString(timeTo.getTime()));
        prop.setProperty(FieldNamesToStore.PERIOD.toString(), Integer.toString(period));
        prop.setProperty(FieldNamesToStore.PERIOD_UNIT.toString(), Integer.toString(periodUnit));
        return prop;
    }
    
    public static DashTimeRangeProp load(Properties prop) throws IllegalArgumentException {
        EDashPropSource src = EDashPropSource.valueOf(loadField(prop, FieldNamesToStore.PROP_SOURCE));
        HistoricalDiagramSettings.TimeRange timeRange = HistoricalDiagramSettings.TimeRange.valueOf(loadField(prop, FieldNamesToStore.TIME_RANGE));
        Timestamp timeFrom = new Timestamp(Long.parseLong(loadField(prop, FieldNamesToStore.TIME_FROM)));
        Timestamp timeTo = new Timestamp(Long.parseLong(loadField(prop, FieldNamesToStore.TIME_TO)));
        int period = Integer.parseInt(loadField(prop, FieldNamesToStore.PERIOD));
        int periodUnit = Integer.parseInt(loadField(prop, FieldNamesToStore.PERIOD_UNIT));
        return new DashTimeRangeProp(src, timeRange, timeFrom, timeTo, period, periodUnit);
    }
    
    private static String loadField(Properties prop, FieldNamesToStore fieldName) throws IllegalArgumentException {
        String fieldAsStr = prop.getProperty(fieldName.toString());
        if (fieldAsStr == null) {
            throw new IllegalArgumentException("Field " + fieldName.toString() + " not found in explorer settings.");
        }
        return fieldAsStr;
    }
}
