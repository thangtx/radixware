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
package org.radixware.kernel.server.reports;

import java.util.HashMap;
import java.util.Map;
import org.radixware.kernel.common.types.Id;

class SummaryCollector {

    private class Value {

        private double sum;
        private double max;
        private double min;

        public Value(double value) {
            this.sum = value;
            this.max = value;
            this.min = value;
        }

        public void add(double value) {
            this.sum += value;
            this.max = Math.max(this.max, value);
            this.min = Math.min(this.min, value);
        }

        public double getSum() {
            return sum;
        }

        public double getMin() {
            return min;
        }

        public double getMax() {
            return max;
        }
    }

    private Map<Id, Value> fieldId2Value = new HashMap<Id, Value>();
    private int recordCount = 0;

    private int subItemCount = 0;
    private boolean fixedState = false;
    private ReportStateInfo stateInfo;

    public SummaryCollector(ReportStateInfo stateInfo) {
        this.stateInfo = new ReportStateInfo(stateInfo);
    }

    public SummaryCollector() {
        stateInfo = new ReportStateInfo(null);
    }

    public void addFieldValue(final Id fieldId, double value) {
        if (fieldId2Value.containsKey(fieldId)) {
            fieldId2Value.get(fieldId).add(value);
        } else {
            fieldId2Value.put(fieldId, new Value(value));
        }
    }

    public void clear() {
        this.recordCount = 0;
        this.subItemCount = 0;
        this.fieldId2Value.clear();
    }

    public double getFieldSumById(final Id fieldId) {
        if (this.fieldId2Value.containsKey(fieldId)) {
            return this.fieldId2Value.get(fieldId).sum;
        } else {
            return 0;
        }
    }

    public double getFieldMinById(final Id fieldId) {
        if (this.fieldId2Value.containsKey(fieldId)) {
            return this.fieldId2Value.get(fieldId).min;
        } else {
            return 0;
        }
    }

    public double getFieldMaxById(final Id fieldId) {
        if (this.fieldId2Value.containsKey(fieldId)) {
            return this.fieldId2Value.get(fieldId).max;
        } else {
            return 0;
        }
    }

    public boolean isPageCountKnown() {
        return fixedState;
    }

    public void incRecordCount() {
        this.recordCount++;
    }

    public void incPageCount(int fileNumber) {
        stateInfo.incPageCount(fileNumber);
    }

    public void incFileCount() {
        stateInfo.incFileCount();
    }

    public int getRecordCount() {
        return this.recordCount;
    }

    public int getPageCount(int fileNumber) {
        return stateInfo.getPageCount(fileNumber);
    }

    public int getTotalPageCount() {
        return stateInfo.getTotalPageCount();
    }

    public int getFileCount() {
        return stateInfo.getFileCount();
    }

    public Long getFileCountIfComputed() {
        return stateInfo.getFileCountIfComputed();
    }

    public ReportStateInfo getStateInfo() {
        return stateInfo;
    }

    public int getSubItemCount() {
        return this.subItemCount;
    }

    public void incSubItemCount() {
        this.subItemCount++;
    }

    public void resetSubItemCount() {
        this.subItemCount = 0;
    }
}
