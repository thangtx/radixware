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

package org.radixware.kernel.server.reports.fo;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportCell;

public class ZebraStripingInfo {
    private final List<Double> ranges = new LinkedList<>();
    private final Map<AdsReportCell, Integer> storeRanges = new HashMap<>();
    private int correction = 1;
    
    public void cleanupRanges() {
        correction = (ranges.size() + (correction - 1)) % 2 == 0 ? 1 : 2;
        ranges.clear();
    }
    
    public void clear() {
        correction = 0;
        ranges.clear();
        storeRanges.clear();
    }
    
    public void nextCorrection() {
        correction = ++correction % 2;
        ranges.clear();
    }
    
    
    
    public int getLogicalRowIndexByCellTop(AdsReportCell cell, double top, boolean inside, boolean storeIndex) {
        Integer value = storeRanges.get(cell);
        if (storeIndex && value != null) {
            return value;
        }
        int result = correction;
        if (ranges.isEmpty()) {
            if (inside)
                ranges.add(top);
        } else {
            for (int i = ranges.size() - 1; i >= 0; i--) {
                double height = ranges.get(i);
                if (top > height) {
                    if (i == ranges.size() - 1) {
                        ranges.add(top);
                        result = ranges.size() + correction - 1;
                        break;
                    } else {//we will not change any settings and just return index of match +1
                        result =  i + correction;
                        break;
                    }
                } else if (top == height) {
                    result = i + correction;
                    break;
                }
            }
        }
        if (storeIndex) {
            if (!storeRanges.containsKey(cell)) {
                storeRanges.put(cell, result);
            }
        } else {
            storeRanges.remove(cell);
        }
        return result;
    }
 
}
