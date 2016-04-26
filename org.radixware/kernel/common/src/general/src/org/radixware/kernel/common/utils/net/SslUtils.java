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

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLSocket;
import org.radixware.kernel.common.enums.ESslCipherSuite;

public class SslUtils {

    private static String ALLOW_SSLV3 = "radix.allow.sslv3";

    public static String[] calculateCipherSuites(final Collection<String> requestedSuites, final Collection<String> supportedSuites) {
        final List<String> cipherCandidates = new ArrayList<>();
        if (requestedSuites == SocketServerChannel.SUITE_ANY_STRONG) {
            for (ESslCipherSuite suite : ESslCipherSuite.values()) {
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

    public static void excludeSslV3(final SSLEngine engine, boolean isClient) {
        engine.setEnabledProtocols(excludeSslv3FromArray(engine.getEnabledProtocols(), isClient));
    }

    public static void excludeSslV3(final Socket socket, boolean isClient) {
        if (socket instanceof SSLSocket) {
            final SSLSocket sslSocket = (SSLSocket) socket;
            sslSocket.setEnabledProtocols(excludeSslv3FromArray(sslSocket.getEnabledProtocols(), isClient));
        }
    }

    //http://www.oracle.com/technetwork/java/javase/documentation/cve-2014-3566-2342133.html
    public static String[] excludeSslv3FromArray(final String[] protocols, boolean isClient) {
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
            SslUtils.excludeSslV3(sslSocket, false);
            final String[] enabledSuites = SslUtils.calculateCipherSuites(securityOptions.getCipherSuites(), Arrays.asList(sslSocket.getSupportedCipherSuites()));
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
}
