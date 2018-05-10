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

public class InstanceJMXState {

    private final int arteCount;
    private final double avgActiveArte;
    private final int avgArteProcessTimeMs;
    private final int avgArteCpuProcessTimeMs;
    private final int avgArteDbProcessTimeMs;
    private final int avgArteExtProcessTimeMs;
    private final int avgArteOtherProcessTimeMs;
    private final int deactivationsByLongDbQuery;
    private final long totalArteRequests;
    private final int normalActiveActiveArte;
    private final int arteActivations;
    private final int artePoolLoadPercentNormal;
//    private final int[] arteTemperature;

    public InstanceJMXState(int arteCount,
            double avgActiveArte,
            int avgArteProcessTimeMs,
            int avgArteCpuProcessTimeMs,
            int avgArteDbProcessTimeMs,
            int avgArteExtProcessTimeMs,
            int avgArteOtherProcessTimeMs,
            int deactivationsByLongDbQuery,
            long totalArteRequests,
            int normalActiveActiveArte,
            int arteActivations,
            int artePoolLoadPercentNormal) {

        this.arteCount = arteCount;
        this.avgActiveArte = avgActiveArte;
        this.avgArteProcessTimeMs = avgArteProcessTimeMs;
        this.avgArteCpuProcessTimeMs = avgArteCpuProcessTimeMs;
        this.avgArteDbProcessTimeMs = avgArteDbProcessTimeMs;
        this.avgArteExtProcessTimeMs = avgArteExtProcessTimeMs;
        this.avgArteOtherProcessTimeMs = avgArteOtherProcessTimeMs;
        this.deactivationsByLongDbQuery = deactivationsByLongDbQuery;
        this.totalArteRequests = totalArteRequests;
        this.normalActiveActiveArte = normalActiveActiveArte;
        this.arteActivations = arteActivations;
        this.artePoolLoadPercentNormal = artePoolLoadPercentNormal;
    }

    public int getArtePoolLoadPercentNormal() {
        return artePoolLoadPercentNormal;
    }

    public int getNormalActiveActiveArte() {
        return normalActiveActiveArte;
    }

    public long getTotalArteRequests() {
        return totalArteRequests;
    }

    public int getAvgArteCpuProcessTimeMs() {
        return avgArteCpuProcessTimeMs;
    }

    public int getAvgArteDbProcessTimeMs() {
        return avgArteDbProcessTimeMs;
    }

    public int getAvgArteExtProcessTimeMs() {
        return avgArteExtProcessTimeMs;
    }

    public int getAvgArteOtherProcessTimeMs() {
        return avgArteOtherProcessTimeMs;
    }

    public int getArteCount() {
        return arteCount;
    }

    public double getAvgActiveArte() {
        return avgActiveArte;
    }

    public int getAvgArteProcessTimeMs() {
        return avgArteProcessTimeMs;
    }

    public int getDeactivationsByLongDbQuery() {
        return deactivationsByLongDbQuery;
    }

    public int getArteActivations() {
        return arteActivations;
    }

}
