/*
 * Copyright (c) 2008-2017, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. This Source Code is distributed
 * WITHOUT ANY WARRANTY; including any implied warranties but not limited to
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Mozilla Public License, v. 2.0. for more details.
 */
package org.radixware.kernel.server.units.netport;

import java.net.InetSocketAddress;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import org.radixware.kernel.common.enums.EClientAuthentication;
import org.radixware.kernel.common.enums.ELinkLevelProtocolKind;
import org.radixware.kernel.common.enums.EPortSecurityProtocol;
import org.radixware.kernel.common.ssl.KeystoreController;
import org.radixware.kernel.server.trace.TraceProfiles;

/**
 * @Immutable
 */
public final class NetChannelOptions {

    final InetSocketAddress bindAddress;
    final InetSocketAddress connectAddress;
    final String addressString;
    final Long recvTimeoutMillis;
    final Long sendTimeoutMillis;
    final Long keepConnectTimeoutMillis;
    final TraceProfiles traceProfiles;
    final Long maxSessionCount;
    final boolean isListener;
    final boolean isConnectReadyNtfOn;
    final boolean isDisconnectNtfOn;
    final EPortSecurityProtocol securityProtocol;
    final List<String> serverKeyAliases;
    final List<String> clientCertAliases;
    final EClientAuthentication clientAuthentication;
    final boolean useKeepAlive;
    private final long keystoreModificationTime;
    final ELinkLevelProtocolKind linkLevelProtocolKind;
    final String inFrame;
    final String outFrame;
    final String title;
    final boolean isSyncMode;
    final boolean isBusySessionCountOn;
    final boolean isOutHttp;
    final boolean isInHttp;
    final String aadcAffinityHandlerClassName;

    NetChannelOptions(final InetSocketAddress bindAddress,
            final InetSocketAddress connectAddress,
            final String addressString,
            final Long maxSessionCount,
            final Long recvTimeoutMillis, final Long sendTimeoutMillis, final Long keepConnectTimeoutMillis,
            final TraceProfiles traceProfiles, final boolean isListener,
            final boolean isConnectReadyNtfOn, final boolean isDisconnectNtfOn,
            final EPortSecurityProtocol securityProtocol,
            final List<String> serverKeyAliases, final List<String> clientCertAliases,
            final EClientAuthentication checkClientCert,
            final boolean useKeepAlive,
            final ELinkLevelProtocolKind linkLevelProtocolKind,
            final String inFrame,
            final String outFrame,
            final String title,
            final boolean isSyncMode,
            final boolean isBusySessionCountOn,
            final String aadcAffinityHandlerClassName) {
        this.bindAddress = bindAddress;
        this.connectAddress = connectAddress;
        this.maxSessionCount = maxSessionCount;
        this.recvTimeoutMillis = recvTimeoutMillis;
        this.sendTimeoutMillis = sendTimeoutMillis;
        this.keepConnectTimeoutMillis = keepConnectTimeoutMillis;
        this.traceProfiles = traceProfiles;
        this.isListener = isListener;
        this.isConnectReadyNtfOn = isConnectReadyNtfOn;
        this.isDisconnectNtfOn = isDisconnectNtfOn;
        this.securityProtocol = securityProtocol;
        this.serverKeyAliases = serverKeyAliases;
        this.clientCertAliases = clientCertAliases;
        this.clientAuthentication = checkClientCert;
        this.keystoreModificationTime = KeystoreController.getServerKeystoreModificationTime();
        this.useKeepAlive = useKeepAlive;
        this.addressString = addressString;
        this.linkLevelProtocolKind = linkLevelProtocolKind;
        this.inFrame = inFrame;
        this.outFrame = outFrame;
        this.title = title;
        this.isSyncMode = isSyncMode;
        this.isBusySessionCountOn = isBusySessionCountOn && isSyncMode;
        isOutHttp = isHttpFrame(outFrame);
        isInHttp = isHttpFrame(inFrame);
        this.aadcAffinityHandlerClassName = aadcAffinityHandlerClassName;
    }
    
    private boolean isHttpFrame(final String frameStr) {
        return frameStr != null && frameStr.endsWith("]H");//dirty
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final NetChannelOptions other = (NetChannelOptions) obj;
        if (!Objects.equals(this.bindAddress, other.bindAddress)) {
            return false;
        }
        if (!Objects.equals(this.connectAddress, other.connectAddress)) {
            return false;
        }
        if (!Objects.equals(this.recvTimeoutMillis, other.recvTimeoutMillis)) {
            return false;
        }
        if (!Objects.equals(this.sendTimeoutMillis, other.sendTimeoutMillis)) {
            return false;
        }
        if (!Objects.equals(this.keepConnectTimeoutMillis, other.keepConnectTimeoutMillis)) {
            return false;
        }
        if (!Objects.equals(this.traceProfiles, other.traceProfiles)) {
            return false;
        }
        if (!Objects.equals(this.maxSessionCount, other.maxSessionCount)) {
            return false;
        }
        if (this.isListener != other.isListener) {
            return false;
        }
        if (this.useKeepAlive != other.useKeepAlive) {
            return false;
        }
        if (this.isSyncMode != other.isSyncMode) {
            return false;
        }
        if (this.isBusySessionCountOn != other.isBusySessionCountOn) {
            return false;
        }
        if (this.isConnectReadyNtfOn != other.isConnectReadyNtfOn) {
            return false;
        }
        if (this.isDisconnectNtfOn != other.isDisconnectNtfOn) {
            return false;
        }
        if (this.securityProtocol != other.securityProtocol) {
            return false;
        }
        if (!Objects.equals(this.serverKeyAliases, other.serverKeyAliases)) {
            return false;
        }
        if (!Objects.equals(this.clientCertAliases, other.clientCertAliases)) {
            return false;
        }
        if (this.clientAuthentication != other.clientAuthentication) {
            return false;
        }
        if (this.keystoreModificationTime != other.keystoreModificationTime) {
            return false;
        }
        if (!Objects.equals(this.linkLevelProtocolKind, other.linkLevelProtocolKind)) {
            return false;
        }
        if (!Objects.equals(this.inFrame, other.inFrame)) {
            return false;
        }
        if (!Objects.equals(this.outFrame, other.outFrame)) {
            return false;
        }
        if (!Objects.equals(this.aadcAffinityHandlerClassName, other.aadcAffinityHandlerClassName)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.bindAddress);
        hash = 47 * hash + Objects.hashCode(this.connectAddress);
        hash = 47 * hash + Objects.hashCode(this.recvTimeoutMillis);
        hash = 47 * hash + Objects.hashCode(this.sendTimeoutMillis);
        hash = 47 * hash + Objects.hashCode(this.keepConnectTimeoutMillis);
        hash = 47 * hash + Objects.hashCode(this.traceProfiles);
        hash = 47 * hash + Objects.hashCode(this.maxSessionCount);
        hash = 47 * hash + (this.isListener ? 1 : 0);
        hash = 47 * hash + (this.isSyncMode ? 1 : 0);
        hash = 47 * hash + (this.isBusySessionCountOn ? 1 : 0);
        hash = 47 * hash + (this.useKeepAlive ? 1 : 0);
        hash = 47 * hash + (this.isConnectReadyNtfOn ? 1 : 0);
        hash = 47 * hash + (this.isDisconnectNtfOn ? 1 : 0);
        hash = 47 * hash + (this.securityProtocol != null ? this.securityProtocol.hashCode() : 0);
        hash = 47 * hash + Objects.hashCode(this.serverKeyAliases);
        hash = 47 * hash + Objects.hashCode(this.clientCertAliases);
        hash = 47 * hash + (this.clientAuthentication != null ? this.clientAuthentication.hashCode() : 0);
        hash = 47 * hash + (int) (this.keystoreModificationTime ^ (this.keystoreModificationTime >>> 32));
        hash = 47 * hash + Objects.hashCode(this.linkLevelProtocolKind);
        hash = 47 * hash + Objects.hashCode(this.inFrame);
        hash = 47 * hash + Objects.hashCode(this.outFrame);
        hash = 47 * hash + Objects.hashCode(this.aadcAffinityHandlerClassName);
        return hash;
    }

    @Override
    public String toString() {
        return "{\n\t" + NetPortHandlerMessages.ADDRESS + String.valueOf(bindAddress) + "; \n\t"
                + NetPortHandlerMessages.REMOTE_ADDRESS + String.valueOf(connectAddress) + "; \n\t"
                + NetPortHandlerMessages.LINK_LEVEL_PROTOCOL + String.valueOf(linkLevelProtocolKind) + "; \n\t"
                + NetPortHandlerMessages.IN_FRAME + String.valueOf(inFrame) + "; \n\t"
                + NetPortHandlerMessages.OUT_FRAME + String.valueOf(outFrame) + "; \n\t"
                + NetPortHandlerMessages.MAX_SESSION_COUNT + (maxSessionCount == null ? "-" : String.valueOf(maxSessionCount)) + "; \n\t"
                + NetPortHandlerMessages.SYNC_MODE + (isSyncMode ? NetPortHandlerMessages.ON : NetPortHandlerMessages.OFF) + "; \n\t"
                + NetPortHandlerMessages.RECV_TIMEOUT_SEC + (recvTimeoutMillis == null ? "-" : String.valueOf(recvTimeoutMillis.longValue() / 1000)) + "; \n\t"
                + NetPortHandlerMessages.SEND_TIMEOUT_SEC + (sendTimeoutMillis == null ? "-" : String.valueOf(sendTimeoutMillis.longValue() / 1000)) + "; \n\t"
                + NetPortHandlerMessages.KEEP_CONNECT_TIMEOUT_SEC + (keepConnectTimeoutMillis == null ? "-" : String.valueOf(keepConnectTimeoutMillis.longValue() / 1000)) + "; \n\t"
                + NetPortHandlerMessages.IS_LISTENER + (isListener ? NetPortHandlerMessages.ON : NetPortHandlerMessages.OFF) + "; \n\t"
                + "TCP keepalive: " + (useKeepAlive ? NetPortHandlerMessages.ON : NetPortHandlerMessages.OFF) + "; \n\t"
                + NetPortHandlerMessages.IS_CONNECT_READY_NTF_ON + (isConnectReadyNtfOn ? NetPortHandlerMessages.ON : NetPortHandlerMessages.OFF) + "; \n\t"
                + NetPortHandlerMessages.IS_DISCONNECT_NTF_ON + (isDisconnectNtfOn ? NetPortHandlerMessages.ON : NetPortHandlerMessages.OFF) + "; \n\t"
                + NetPortHandlerMessages.TRACE_PROFILE + String.valueOf(traceProfiles).replace("\n", "\n\t") + "; \n\t"
                + (aadcAffinityHandlerClassName == null ? "" : "AADC Affinity Handler: " + aadcAffinityHandlerClassName + "; \n\t")
                + NetPortHandlerMessages.SECURITY_PROTOCOL + (securityProtocol.isTls() ? "secure connection" : "plaintext connection")
                + (securityProtocol.isTls()
                        ? " {\n\t\t" + NetPortHandlerMessages.SERVER_KEY_ALIASES + (serverKeyAliases == null ? NetPortHandlerMessages.ANY : serverKeyAliases.toString())
                        + "; \n\t\t" + NetPortHandlerMessages.CLIENT_CERT_ALIASES + (clientCertAliases == null ? NetPortHandlerMessages.ANY : clientCertAliases.toString())
                        + "; \n\t\t" + NetPortHandlerMessages.CHECK_CLIENT_CERT + String.valueOf(clientAuthentication)
                        + (keystoreModificationTime > 0 ? "; \n\t\t" + NetPortHandlerMessages.SERVER_KEYSTORE_MODIFICATION_TIME + (new Date(keystoreModificationTime).toString()) : "")
                        + "; \n\t} \n"
                        : "; \n")
                + "}";
    }
}
