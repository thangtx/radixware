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

package org.radixware.kernel.common.sc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.enums.EPortSecurityProtocol;
import org.radixware.kernel.common.utils.SystemPropUtils;
import org.radixware.kernel.common.utils.net.SapAddress;
import org.radixware.kernel.common.utils.net.SocketServerChannel;


public class SapClientOptions {

    private static final int INITIAL_BUSY_BLOCK_MILLIS = 50;
    private static final int MAX_BUSY_BLOCK_MILLIS = SystemPropUtils.getIntSystemProp("rdx.sapclientopts.max.busy.block.millis", 1000);
    private String name;
    private int priority = 1;
    private long blockingPeriodMillis;
    private int connectTimeoutMillis;
    private long id;
    private SapAddress address;
    private EPortSecurityProtocol securityProtocol;
    private List<String> clientKeyAliases;
    private List<String> serverCertAliases;
    private List<String> cipherSuites;
    private SoapServiceOptions soapServiceOptions;
    private Map<String, String> additionalAttrs;
    private int busyBlockMillis = INITIAL_BUSY_BLOCK_MILLIS;
    private boolean blockedBecauseBusy = false;
    private long blockTime = -1;
    private SapClientOptions successor;

    public SapClientOptions() {
    }

    public SapClientOptions(final SapClientOptions ops) {
        name = ops.name;
        priority = ops.priority;
        blockingPeriodMillis = ops.blockingPeriodMillis;
        connectTimeoutMillis = ops.connectTimeoutMillis;
        address = ops.address == null ? null : new SapAddress(ops.address);
        securityProtocol = ops.securityProtocol;
        clientKeyAliases = ops.clientKeyAliases == null ? null : new ArrayList<>(ops.clientKeyAliases);
        serverCertAliases = ops.serverCertAliases == null ? null : new ArrayList<>(ops.serverCertAliases);
        cipherSuites = ops.cipherSuites == null ? null : new ArrayList<>(ops.cipherSuites);
        blockTime = -1;
        soapServiceOptions = ops.soapServiceOptions;
        id = ops.getId();
    }

    public SapClientOptions getSuccessor() {
        return successor;
    }

    public void setSuccessor(SapClientOptions successor) {
        this.successor = successor;
    }

    public Map<String, String> getAdditionalAttrs() {
        return additionalAttrs;
    }

    public void setAdditionalAttrs(Map<String, String> additionalAttrs) {
        this.additionalAttrs = additionalAttrs;
    }

    public void block() {
        setBlockTime(System.currentTimeMillis() + getBlockingPeriodMillis());
    }

    public void block(int millis) {
        setBlockTime(System.currentTimeMillis() + millis);
    }

    /**
     * Connection to sap is succeeded, but server responded with busy fault. In
     * case of continuous busy responses progressive block timeouts are used.
     * Current timeout value can be reset with {@linkplain  setNotBusy()}
     */
    public void blockOnBusy() {
        block(busyBlockMillis);
        busyBlockMillis *= 2;
        if (busyBlockMillis > MAX_BUSY_BLOCK_MILLIS) {
            busyBlockMillis = MAX_BUSY_BLOCK_MILLIS;
        }
        blockedBecauseBusy = true;
        if (successor != null) {
            successor.copyBlockStateFrom(this);
        }
    }

    public void setNotBusy() {
        busyBlockMillis = INITIAL_BUSY_BLOCK_MILLIS;
    }

    public boolean wasBlockedBecauseBusy() {
        return blockedBecauseBusy;
    }

    public void setSoapServiceOptions(SoapServiceOptions soapServiceOptions) {
        this.soapServiceOptions = soapServiceOptions;
    }

    public SoapServiceOptions getSoapServiceOptions() {
        return soapServiceOptions;
    }

    public List<String> getCipherSuites() {
        return cipherSuites;
    }

    public void setCipherSuites(List<String> cipherSuites) {
        if (cipherSuites == null) {
            this.cipherSuites = null;
        } else if (cipherSuites == SocketServerChannel.SUITE_ANY || cipherSuites == SocketServerChannel.SUITE_ANY_STRONG) {
            this.cipherSuites = cipherSuites;
        } else {
            this.cipherSuites = Collections.unmodifiableList(new ArrayList<>(cipherSuites));
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * @return the priority
     */
    public int getPriority() {
        return priority;
    }

    /**
     * @param priority the priority to set
     */
    public void setPriority(final int priority) {
        this.priority = Math.max(0, priority);
    }

    /**
     * In Millis
     *
     * @return the blockingPeriod
     */
    public long getBlockingPeriodMillis() {
        return blockingPeriodMillis;
    }

    /**
     * In Millis
     *
     * @param blockingPeriod the blockingPeriod to set
     */
    public void setBlockingPeriodMillis(final long blockingPeriod) {
        this.blockingPeriodMillis = Math.max(0, blockingPeriod);
    }

    /**
     * In Millis
     *
     * @return the connectTimeout
     */
    public int getConnectTimeoutMillis() {
        return connectTimeoutMillis;
    }

    /**
     * In Millis
     *
     * @param connectTimeout the connectTimeout to set
     */
    public void setConnectTimeoutMillis(final int connectTimeout) {
        this.connectTimeoutMillis = Math.max(0, connectTimeout);
    }

    /**
     * @return the address
     */
    public SapAddress getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(final SapAddress address) {
        this.address = address;
    }

    /**
     * @return the securityProtocol
     */
    public EPortSecurityProtocol getSecurityProtocol() {
        return securityProtocol;
    }

    /**
     * @param securityProtocol the securityProtocol to set
     */
    public void setSecurityProtocol(final EPortSecurityProtocol securityProtocol) {
        this.securityProtocol = securityProtocol;
    }

    /**
     * @return client key aliases to be used during ssl handshaking
     */
    public List<String> getClientKeyAliases() {
        return clientKeyAliases;
    }

    /**
     *
     * @param clientKeyAliases client key aliases to be set
     */
    public void setClientKeyAliases(final List<String> clientKeyAliases) {
        if (clientKeyAliases == null) {
            this.clientKeyAliases = null;
        } else {
            this.clientKeyAliases = Collections.unmodifiableList(new ArrayList<>(clientKeyAliases));
        }
    }

    /**
     * @return server certificate aliases to be accepted during ssl handshaking
     */
    public List<String> getServerCertAliases() {
        return serverCertAliases;
    }

    /**
     *
     * @param serverCertAliases server certificate aliases to be set
     */
    public void setServerCertAliases(final List<String> serverCertAliases) {
        if (serverCertAliases == null) {
            this.serverCertAliases = null;
        } else {
            this.serverCertAliases = Collections.unmodifiableList(new ArrayList<>(serverCertAliases));
        }
    }

    /**
     * @return the blockTime, milliseconds
     */
    public long getBlockTime() {
        return blockTime;
    }

    /**
     * @param blockTime the blockTime to set, milliseconds
     */
    public void setBlockTime(final long blockTime) {
        this.blockTime = blockTime;
        //if this is a block because of busy, this flag will be set by blockOnBusy() immediately after setBlockTime
        blockedBecauseBusy = false;
        if (successor != null) {
            successor.copyBlockStateFrom(this);
        }
    }

    public void unblock() {
        blockTime = -1;
        blockedBecauseBusy = false;
    }

    public String getShortDescription() {
        return "Sap#" + id + " '" + name + "'";
    }

    public void copyBlockStateFrom(final SapClientOptions src) {
        if (src != null) {
            blockTime = src.blockTime;
            busyBlockMillis = src.busyBlockMillis;
            blockedBecauseBusy = src.blockedBecauseBusy;
        }
    }

    @Override
    public String toString() {
        return getShortDescription() + " (" + getAddress().toString() + ")";
    }

    /**
     * Used to search for cached physical connections. Should change when
     * parameters affecting the way physical connection is established change.
     *
     * @return
     */
    public String getConnectionId() {
        return address == null ? "<null>" : address.toString()
                + "-" + (securityProtocol == null ? "null" : securityProtocol.getValue())
                + (securityProtocol == EPortSecurityProtocol.SSL ? "-" + listAsStr("serverAliases", serverCertAliases) + "-" + listAsStr("clientAliases", clientKeyAliases) + "-" + listAsStr("cipherSuites", cipherSuites) : "");
    }

    private String listAsStr(final String listName, final List list) {
        final StringBuilder sb = new StringBuilder();
        sb.append(listName);
        sb.append("{");
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                if (i > 0) {
                    sb.append(",");
                }
                sb.append(list.get(i));
            }
        }
        sb.append("}");
        return sb.toString();
    }
}
