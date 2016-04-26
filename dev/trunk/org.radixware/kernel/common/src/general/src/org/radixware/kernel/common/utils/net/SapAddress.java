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

package org.radixware.kernel.common.utils.net;

import java.net.InetSocketAddress;
import java.util.Objects;
import org.radixware.kernel.common.utils.CompositeInetSocketAddress;
import org.radixware.kernel.common.utils.io.pipe.PipeAddress;


public final class SapAddress {

    public static enum EKind {
        INET_SOCKET_ADDRESS,
        INTERNAL_PIPE_ADDRESS,
        JMS_ADDRESS
    };
    
    private final EKind kind;
    private final InetSocketAddress remoteInetAddr;
    private final InetSocketAddress localInetAddr;
    private final PipeAddress pipeAddr;
    private final JmsAddress jmsAddress;

    public SapAddress(final SapAddress addr) {
        kind = addr.kind;
        if (addr.remoteInetAddr != null)
            remoteInetAddr = new InetSocketAddress(addr.remoteInetAddr.getAddress(), addr.remoteInetAddr.getPort());
        else
            remoteInetAddr = null;
        if (addr.localInetAddr != null)
            localInetAddr = new InetSocketAddress(addr.localInetAddr.getAddress(), addr.localInetAddr.getPort());
        else
            localInetAddr = null;
        if (addr.pipeAddr != null)
            pipeAddr = new PipeAddress(addr.pipeAddr);
        else
            pipeAddr = null;
        if (addr.jmsAddress != null)
            jmsAddress = new JmsAddress(addr.jmsAddress);
        else
            jmsAddress = null;
    }
    
    public SapAddress(final CompositeInetSocketAddress compositeInetAddress) {
        this(compositeInetAddress.getRemoteAddress(), compositeInetAddress.getLocalAddress());
    }

    public SapAddress(final InetSocketAddress remoteInetAddr) {
        this(remoteInetAddr, null);
    }

    public SapAddress(final InetSocketAddress remoteInetAddr, final InetSocketAddress localInetAddr) {
        this.remoteInetAddr = remoteInetAddr;
        this.localInetAddr = localInetAddr;
        kind = EKind.INET_SOCKET_ADDRESS;
        pipeAddr = null;
        jmsAddress = null;
    }

    public SapAddress(final PipeAddress pipeAddr) {
        this.pipeAddr = pipeAddr;
        kind = EKind.INTERNAL_PIPE_ADDRESS;
        remoteInetAddr = null;
        localInetAddr = null;
        jmsAddress = null;
    }

    public SapAddress(final JmsAddress jmsAddress) {
        this.jmsAddress = jmsAddress;
        kind = EKind.JMS_ADDRESS;
        remoteInetAddr = null;
        localInetAddr = null;
        pipeAddr = null;
    }

    public InetSocketAddress getRemoteInetAddress() {
        return remoteInetAddr;
    }

    public InetSocketAddress getLocalInetAddress() {
        return localInetAddr;
    }

    public JmsAddress getJmsAddress() {
        return jmsAddress;
    }

    public EKind getKind() {
        return kind;
    }

    public PipeAddress getPipeAddress() {
        return pipeAddr;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SapAddress other = (SapAddress) obj;
        if (this.kind != other.kind) {
            return false;
        }
        if (!Objects.equals(this.remoteInetAddr, other.remoteInetAddr)) {
            return false;
        }
        if (!Objects.equals(this.localInetAddr, other.localInetAddr)) {
            return false;
        }
        if (!Objects.equals(this.pipeAddr, other.pipeAddr)) {
            return false;
        }
        if (!Objects.equals(this.jmsAddress, other.jmsAddress)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (this.kind != null ? this.kind.hashCode() : 0);
        hash = 89 * hash + Objects.hashCode(this.remoteInetAddr);
        hash = 89 * hash + Objects.hashCode(this.localInetAddr);
        hash = 89 * hash + Objects.hashCode(this.pipeAddr);
        hash = 89 * hash + Objects.hashCode(this.jmsAddress);
        return hash;
    }

    @Override
    public String toString() {
        final String desc;
        switch (kind) {
            case INET_SOCKET_ADDRESS:
                desc = "inetAddr=" + remoteInetAddr + (localInetAddr != null ? "<" + localInetAddr.toString() : "");
                break;
            case INTERNAL_PIPE_ADDRESS:
                desc = "pipeAddr=" + pipeAddr;
                break;
            case JMS_ADDRESS:
                desc = "JMS Options:\n" + jmsAddress.getOptions();
                break;
            default:
                desc = "<unknown>";
        }
        return "SapAddress {" + desc + '}';
    }
}
