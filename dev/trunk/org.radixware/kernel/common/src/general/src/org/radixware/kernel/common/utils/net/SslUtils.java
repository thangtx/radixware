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
package org.radixware.kernel.common.utils.net;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLSocket;
import org.radixware.kernel.common.enums.EPortSecurityProtocol;
import org.radixware.kernel.common.enums.ESslCipherSuite;

public class SslUtils {

    private static final String ALLOW_SSLV3 = "radix.allow.sslv3";

    public static String[] calculateCipherSuites(EPortSecurityProtocol securityProtocol, final Collection<String> requestedSuites, final Collection<String> supportedSuites) {
        final List<String> cipherCandidates = new ArrayList<>();
        if (requestedSuites == SocketServerChannel.SUITE_ANY_STRONG) {
            for (ESslCipherSuite suite : getStrongCipherSuites(securityProtocol)) {
                cipherCandidates.add(suite.getValue());
            }
        } else if (requestedSuites == SocketServerChannel.SUITE_ANY) {
            cipherCandidates.addAll(supportedSuites);
        } else {
            cipherCandidates.addAll(requestedSuites);
        }
        cipherCandidates.retainAll(supportedSuites);
        String[] ciphersToUserArr = new String[cipherCandidates.size()];
        return cipherCandidates.toArray(ciphersToUserArr);
    }
    
    public static void ensureTlsVersion(EPortSecurityProtocol securityProtocol, SSLEngine engine, boolean isClient) {
        final String[] protocols = filterProtocols(securityProtocol, engine.getEnabledProtocols(), isClient);
        engine.setEnabledProtocols(protocols);
    }
    
    public static void ensureTlsVersion(EPortSecurityProtocol securityProtocol, Socket socket, boolean isClient) {
        if (socket instanceof SSLSocket) {
            final SSLSocket sslSocket = (SSLSocket) socket;
            final String[] protocols = filterProtocols(securityProtocol, sslSocket.getEnabledProtocols(), isClient);
            sslSocket.setEnabledProtocols(protocols);
        }
    }
    
    private static String[] filterProtocols(EPortSecurityProtocol securityProtocol, String[] enabledProtocols, boolean isClient) {
        final String[] protocols = securityProtocol == EPortSecurityProtocol.TLSv1_2
                ? filterProtocolsTls_v12(enabledProtocols)
                : filterProtocolsTls_v10(enabledProtocols, isClient);
        return protocols;
    }
    
    //http://www.oracle.com/technetwork/java/javase/documentation/cve-2014-3566-2342133.html
    public static String[] filterProtocolsTls_v10(final String[] protocols, boolean isClient) {
        if (System.getProperties().containsKey(ALLOW_SSLV3)) {
            return protocols;
        }
        final List<String> enabledProtocols = new ArrayList<>();
        if (protocols != null) {
            for (String protocol : protocols) {
                if (protocol != null && !protocol.toLowerCase().contains("sslv3") && !(isClient && protocol.toLowerCase().contains("sslv2hello"))) {
                    enabledProtocols.add(protocol);
                }
            }
        }
        return enabledProtocols.toArray(new String[enabledProtocols.size()]);
    }
    
    // https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#SSLContext
    // https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#jssenames
    public static String[] filterProtocolsTls_v12(final String[] protocols) {
        final List<String> filteredProtocols = new ArrayList<>();
        if (protocols != null) {
            for (String protocol : protocols) {
                final String proto = protocol == null ? null : protocol.toLowerCase();
                if (proto != null && !proto.contains("ssl") && !proto.equals("tlsv1") && !proto.equals("tlsv1.1")) {
                    filteredProtocols.add(protocol);
                }
            }
        }
        return filteredProtocols.toArray(new String[filteredProtocols.size()]);
    }

    public static SSLSocket createSslSocket(final Socket socket, SSLContext sslContext, final SocketServerChannel.SecurityOptions securityOptions) throws IOException {
        final String clientInfo = (socket == null || socket.getRemoteSocketAddress() == null) ? "null" : socket.getRemoteSocketAddress().toString();
        try {
            final SSLSocket sslSocket = (SSLSocket) sslContext.getSocketFactory().createSocket(socket, socket.getInetAddress().getHostAddress(), socket.getPort(), true);
            sslSocket.setUseClientMode(false);
            switch (securityOptions.getClientAuthMode()) {
                case Required:
                    sslSocket.setNeedClientAuth(true);
                    break;
                case Enabled:
                    sslSocket.setWantClientAuth(true);
                    break;
                default:
                    sslSocket.setNeedClientAuth(false);
            }
            ensureTlsVersion(securityOptions.getSecurityProtocol(), sslSocket, false);
            final String[] enabledSuites = SslUtils.calculateCipherSuites(securityOptions.getSecurityProtocol(), securityOptions.getCipherSuites(), Arrays.asList(sslSocket.getSupportedCipherSuites()));
            if (enabledSuites == null || enabledSuites.length == 0) {
                throw new IOException("None of the requested cipher suites are supported");
            }
            sslSocket.setEnabledCipherSuites(enabledSuites);
            sslSocket.startHandshake();
            return sslSocket;
        } catch (Exception ex) {
            throw new IOException("Error on establishin connection with " + clientInfo, ex);
        }
    }
    
    public static List<ESslCipherSuite> getStrongCipherSuites(EPortSecurityProtocol securityProtocol) {
        final int minVersion = securityProtocol == EPortSecurityProtocol.TLSv1_2 ? 12
                : (System.getProperties().containsKey(ALLOW_SSLV3) ? 3: 10);
        List<ESslCipherSuite> suites = new ArrayList<>();
        for (ESslCipherSuite suite : ESslCipherSuite.values()) {
            if (suite.version >= minVersion) {
                suites.add(suite);
            }
        }
        return suites;
    }
    
    public static List<String> getStrongCipherSuitesAsStrList(EPortSecurityProtocol securityProtocol) {
        List<String> list = new ArrayList<>();
        for (ESslCipherSuite cs : getStrongCipherSuites(securityProtocol)) {
            list.add(cs.getValue());
        }
        return list;
    }
}
