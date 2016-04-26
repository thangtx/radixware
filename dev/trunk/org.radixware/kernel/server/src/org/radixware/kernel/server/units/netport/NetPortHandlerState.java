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
package org.radixware.kernel.server.units.netport;

public class NetPortHandlerState {

    private final int minAasResponseMs;
    private final int maxAasResponseMs;
    private final int avgAasResponseMs;
    private final int minTotalResponseMs;
    private final int maxTotalResponseMs;
    private final int avgTotalResponseMs;
    private final int queueSize;
    private final int cpuUsagePercent;
    private final int queuePutsPerSecond;
    private final int queueRemovesPerSecond;
    private final int minActiveAasSeances;
    private final int maxActiveAasSeances;
    private final int avgActiveAasSeances;

    public NetPortHandlerState(int minAasResponseMs, int maxAasResponseMs, int avgAasResponseMs, int minTotalResponseMs, int maxTotalResponseMs, int avgTotalResponseMs, int queueSize, int cpuUsagePercent, int queuePutsPerSecond, int queueRemovesPerSecond, int minActiveAasSeances, int maxActiveAasSeances, int avgActiveAasSeances) {
        this.minAasResponseMs = minAasResponseMs;
        this.maxAasResponseMs = maxAasResponseMs;
        this.avgAasResponseMs = avgAasResponseMs;
        this.minTotalResponseMs = minTotalResponseMs;
        this.maxTotalResponseMs = maxTotalResponseMs;
        this.avgTotalResponseMs = avgTotalResponseMs;
        this.queueSize = queueSize;
        this.cpuUsagePercent = cpuUsagePercent;
        this.queuePutsPerSecond = queuePutsPerSecond;
        this.queueRemovesPerSecond = queueRemovesPerSecond;
        this.minActiveAasSeances = minActiveAasSeances;
        this.maxActiveAasSeances = maxActiveAasSeances;
        this.avgActiveAasSeances = avgActiveAasSeances;
    }

    public int getMinAasResponseMs() {
        return minAasResponseMs;
    }

    public int getMaxAasResponseMs() {
        return maxAasResponseMs;
    }

    public int getAvgAasResponseMs() {
        return avgAasResponseMs;
    }

    public int getMinTotalResponseMs() {
        return minTotalResponseMs;
    }

    public int getMaxTotalResponseMs() {
        return maxTotalResponseMs;
    }

    public int getAvgTotalResponseMs() {
        return avgTotalResponseMs;
    }

    public int getQueueSize() {
        return queueSize;
    }

    public int getCpuUsagePercent() {
        return cpuUsagePercent;
    }

    public int getQueuePutsPerSecond() {
        return queuePutsPerSecond;
    }

    public int getQueueRemovesPerSecond() {
        return queueRemovesPerSecond;
    }

    public int getMinActiveAasSeances() {
        return minActiveAasSeances;
    }

    public int getMaxActiveAasSeances() {
        return maxActiveAasSeances;
    }

    public int getAvgActiveAasSeances() {
        return avgActiveAasSeances;
    }

}
