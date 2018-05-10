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
package org.radixware.kernel.server.pipe;

/**
 * Address of the {@link RemotePipeTunnel}. In contrast to {@link PipeAddress},
 * does not hold any information specific to particular pipes or services.
 */
public class RemotePipeTunnelAddress {
    private final String remoteHost;
    private final int remotePort;
    private final int remoteInstanceId;

    RemotePipeTunnelAddress(String remoteHost, int remotePort, int remoteInstanceId) {
        this.remoteHost = remoteHost;
        this.remotePort = remotePort;
        this.remoteInstanceId = remoteInstanceId;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((remoteHost == null) ? 0 : remoteHost.hashCode());
        result = prime * result + remotePort;
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
        RemotePipeTunnelAddress other = (RemotePipeTunnelAddress) obj;
        if (remoteInstanceId != other.remoteInstanceId) {
            return false;
        }
        if (remotePort != other.remotePort) {
            return false;
        }
        if (remoteHost == null) {
            if (other.remoteHost != null) {
                return false;
            }
        } else if (!remoteHost.equals(other.remoteHost)) {
            return false;
        }
        return true;
    }

    public int getRemoteInstanceId() {
        return remoteInstanceId;
    }

    public String getRemoteHost() {
        return remoteHost;
    }

    public int getRemotePort() {
        return remotePort;
    }
    
}
