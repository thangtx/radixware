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

public class FailedSapInfo {

    private final String address;
    private final Boolean available;
    private final String busyFaultCode;
    private final Long availableVersion;
    private final Integer aadcMemberId;

    public FailedSapInfo(String address, Boolean available, String busyFaultCode, Long availableVersion, Integer aadcMemberId) {
        this.address = address;
        this.available = available;
        this.busyFaultCode = busyFaultCode;
        this.availableVersion = availableVersion;
        this.aadcMemberId = aadcMemberId;
    }

    public String getAddress() {
        return address;
    }

    public Boolean getAvailable() {
        return available;
    }

    public String getBusyFaultCode() {
        return busyFaultCode;
    }

    public Long getAvailableVersion() {
        return availableVersion;
    }

    public Integer getAadcMemberId() {
        return aadcMemberId;
    }

}
