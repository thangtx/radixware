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

package org.radixware.kernel.common.utils.io.pipe;


public class PipeAddress {

    public static final int LOCAL_INSTANCE_ID = -1;
    private final String address;
    private final int localInstanceId;
    private final int remoteInstanceId;
    private final String remoteHost;
    private final int remotePort;

    public PipeAddress(final PipeAddress addr) {
        this.address = addr.address;
        this.localInstanceId = addr.localInstanceId;
        this.remoteInstanceId = addr.remoteInstanceId;
        this.remoteHost = addr.remoteHost;
        this.remotePort = addr.remotePort;
    }
    
    public PipeAddress(final String address) {
    	this(address, LOCAL_INSTANCE_ID, LOCAL_INSTANCE_ID, null, 0);
    }

    public PipeAddress(final String address, final int localInstanceId, final int remoteInstanceId, final String remoteHost, final int remotePort) {
        this.address = address;
    	this.localInstanceId = localInstanceId;
    	this.remoteInstanceId = remoteInstanceId;
        this.remoteHost = remoteHost;
        this.remotePort = remotePort;
    }

    public int getLocalInstanceId() {
        return localInstanceId;
    }
    
    public int getRemoteInstanceId() {
        return remoteInstanceId;
    }
    
    public String getAddress() {
        return address;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((address == null) ? 0 : address.hashCode());
        result = prime * result + localInstanceId;
        result = prime * result + remoteInstanceId;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        PipeAddress other = (PipeAddress) obj;
        if (localInstanceId != other.localInstanceId) {
            return false;
        }
        if (remoteInstanceId != other.remoteInstanceId) {
            return false;
        }
        if (address == null) {
            if (other.address != null) {
                return false;
            }
        } else if (!address.equals(other.address)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "PipeAddress{" + (localInstanceId==LOCAL_INSTANCE_ID?"local":localInstanceId) + ":" + 
                (remoteInstanceId==LOCAL_INSTANCE_ID?"local":remoteInstanceId) + ":" + address + "}";
    }

    public int getRemotePort() {
        return remotePort;
    }

    public String getRemoteHost() {
        return remoteHost;
    }

}
