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
package org.radixware.kernel.common.sc;

import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.exceptions.ServiceProcessFault;

public class FailedSapsInfo {

    private final List<FailedSapInfo> infos;
    private final int busyCount;
    private final int invalidVerCount;
    private final int aadcMismatchCount;
    private final int unavailableCount;
    private final long maxAvailableVersion;

    public FailedSapsInfo(List<FailedSapInfo> infos) {
        this.infos = new ArrayList<>(infos);
        int invalidVer = 0;
        int unavailable = 0;
        int aadcMismatch = 0;
        int busy = 0;
        long maxVer = 0;
        for (FailedSapInfo info : this.infos) {
            if (info.getBusyFaultCode() != null && info.getBusyFaultCode().equals(ServiceProcessFault.FAULT_CODE_SERVER_BUSY_INVALID_VERSION)) {
                invalidVer++;
                if (info.getAvailableVersion() != null) {
                    maxVer = Math.max(maxVer, info.getAvailableVersion());
                }
            } else if (info.getBusyFaultCode() != null && info.getBusyFaultCode().equals(ServiceProcessFault.FAULT_CODE_SERVER_BUSY)) {
                busy++;
            } else if (info.getAvailable() == null) {
                aadcMismatch++;
            } else {
                unavailable++;
            }
        }
        busyCount = busy;
        unavailableCount = unavailable;
        invalidVerCount = invalidVer;
        aadcMismatchCount = aadcMismatch;
        maxAvailableVersion = maxVer;
    }

    public int getTotalCount() {
        return infos.size();
    }

    public List<FailedSapInfo> getInfos() {
        return infos;
    }

    public int getBusyCount() {
        return busyCount;
    }

    public int getInvalidVerCount() {
        return invalidVerCount;
    }

    public int getUnavailableCount() {
        return unavailableCount;
    }

    public int getAadcMismatchCount() {
        return aadcMismatchCount;
    }

    public long getMaxAvailableVersion() {
        return maxAvailableVersion;
    }

}
