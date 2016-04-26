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

package org.radixware.kernel.common.utils;

import java.net.InetSocketAddress;

public final class CompositeInetSocketAddress {

    private final InetSocketAddress remoteAddress;
    private final InetSocketAddress localAddress;

    public CompositeInetSocketAddress(final InetSocketAddress remoteAddress, final InetSocketAddress localAddress) {
        this.remoteAddress = remoteAddress;
        this.localAddress = localAddress;
    }

    public InetSocketAddress getLocalAddress() {
        return localAddress;
    }

    public InetSocketAddress getRemoteAddress() {
        return remoteAddress;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CompositeInetSocketAddress other = (CompositeInetSocketAddress) obj;
        if (this.remoteAddress != other.remoteAddress && (this.remoteAddress == null || !this.remoteAddress.equals(other.remoteAddress))) {
            return false;
        }
        if (this.localAddress != other.localAddress && (this.localAddress == null || !this.localAddress.equals(other.localAddress))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + (this.remoteAddress != null ? this.remoteAddress.hashCode() : 0);
        hash = 23 * hash + (this.localAddress != null ? this.localAddress.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "{\n\tRemote address: " + remoteAddress + "\n\tLocal address: " + localAddress + "\n}";
    }
}
