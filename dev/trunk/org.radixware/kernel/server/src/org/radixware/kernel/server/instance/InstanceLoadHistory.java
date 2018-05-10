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
package org.radixware.kernel.server.instance;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author dsafonov
 */
public class InstanceLoadHistory {

    private final List<LoadHistoryItem> items = new LinkedList<>();
    private final int maxSampleCount;

    public InstanceLoadHistory(int maxSampeCount) {
        this.maxSampleCount = maxSampeCount;
    }

    public void store(final int instCpuLoadPercent, final int hostCpuLoadPercent, final int heapUsagePercent, final int avgActiveArteCount) {
        items.add(new LoadHistoryItem(instCpuLoadPercent, hostCpuLoadPercent, heapUsagePercent, avgActiveArteCount));
        if (items.size() > maxSampleCount) {
            items.remove(0);
        }
    }

    public List<LoadHistoryItem> getItems() {
        return Collections.unmodifiableList(items);
    }
    
    public void clear() {
        items.clear();
    }

    public static class LoadHistoryItem {

        private final int instCpuLoadPercent;
        private final int hostCpuLoadPercent;
        private final int heapUsagePercent;
        private final int avgActiveArteCount;
        private final long timestampMillis = System.currentTimeMillis();

        public LoadHistoryItem(int instCpuLoadPercent, int hostCpuLoadPercent, int heapUsagePercent, int avgActiveArteCount) {
            this.instCpuLoadPercent = instCpuLoadPercent;
            this.hostCpuLoadPercent = hostCpuLoadPercent;
            this.heapUsagePercent = heapUsagePercent;
            this.avgActiveArteCount = avgActiveArteCount;
        }

        public int getInstCpuLoadPercent() {
            return instCpuLoadPercent;
        }

        public int getHostCpuLoadPercent() {
            return hostCpuLoadPercent;
        }

        public int getHeapUsagePercent() {
            return heapUsagePercent;
        }

        public int getAvgActiveArteCount() {
            return avgActiveArteCount;
        }

        public long getTimestampMillis() {
            return timestampMillis;
        }

    }

}
