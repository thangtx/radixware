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
package org.radixware.kernel.common.svn.client.impl;

import java.net.Socket;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.X509ExtendedKeyManager;
import javax.net.ssl.X509KeyManager;
import org.radixware.kernel.common.svn.RadixSvnException;
import org.radixware.kernel.common.svn.client.SvnCredentials;
import org.radixware.kernel.common.svn.client.SvnRepository;

/**
 *
 * @author akrylov
 */
public class KeyManager extends X509ExtendedKeyManager {

    private String prefefinedAlias;
    private javax.net.ssl.KeyManager[] loaded;
    private final SvnRepository repository;

    public KeyManager(SvnRepository repository) {
        this.repository = repository;
    }

    @Override
    public String[] getClientAliases(String location, Principal[] principals) {
        if (!initialize()) {
            return null;
        }

        for (Iterator<X509KeyManager> it = filterX509(loaded).iterator(); it.hasNext();) {
            final X509KeyManager keyManager = it.next();
            final String[] clientAliases = keyManager.getClientAliases(location, principals);
            if (clientAliases != null) {
                return clientAliases;
            }
        }

        return null;
    }

    @Override
    public String chooseClientAlias(String[] strings, Principal[] principals, Socket socket) {
        if (!initialize()) {
            return null;
        }
        if (prefefinedAlias != null) {
            return prefefinedAlias;
        }
        for (Iterator<X509KeyManager> it = filterX509(loaded).iterator(); it.hasNext();) {
            final X509KeyManager keyManager = it.next();
            final String clientAlias = keyManager.chooseClientAlias(strings, principals, socket);
            if (clientAlias != null) {
                return clientAlias;
            }
        }
        return null;
    }

    @Override
    public String[] getServerAliases(String location, Principal[] principals) {
        if (!initialize()) {
            return null;
        }

        for (Iterator<X509KeyManager> it = filterX509(loaded).iterator(); it.hasNext();) {
            final X509KeyManager keyManager = it.next();
            final String[] serverAliases = keyManager.getServerAliases(location, principals);
            if (serverAliases != null) {
                return serverAliases;
            }
        }

        return null;
    }

    @Override
    public String chooseServerAlias(String location, Principal[] principals, Socket socket) {
        if (!initialize()) {
            return null;
        }

        for (Iterator<X509KeyManager> it = filterX509(loaded).iterator(); it.hasNext();) {
            final X509KeyManager keyManager = it.next();
            final String serverAlias = keyManager.chooseServerAlias(location, principals, socket);
            if (serverAlias != null) {
                return serverAlias;
            }
        }

        return null;
    }

    @Override
    public X509Certificate[] getCertificateChain(String location) {
        if (!initialize()) {
            return null;
        }
        for (Iterator<X509KeyManager> it = filterX509(loaded).iterator(); it.hasNext();) {
            final X509KeyManager keyManager = it.next();
            final X509Certificate[] certificateChain = keyManager.getCertificateChain(location);
            if (certificateChain != null) {
                return certificateChain;
            }
        }
        return null;
    }

    @Override
    public PrivateKey getPrivateKey(String string) {
        if (!initialize()) {
            return null;
        }

        for (Iterator<X509KeyManager> it = filterX509(loaded).iterator(); it.hasNext();) {
            final X509KeyManager keyManager = it.next();
            final PrivateKey privateKey = keyManager.getPrivateKey(string);
            if (privateKey != null) {
                return privateKey;
            }
        }
        return null;
    }

    @Override
    public String chooseEngineServerAlias(String keyType, Principal[] issuers, SSLEngine engine) {
        return super.chooseEngineServerAlias(keyType, issuers, engine); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String chooseEngineClientAlias(String[] keyType, Principal[] issuers, SSLEngine engine) {
        return super.chooseEngineClientAlias(keyType, issuers, engine); //To change body of generated methods, choose Tools | Templates.
    }

    private boolean initialize() {
        if (loaded != null) {
            return true;
        }
        try {
            loaded = getKeyManagers(repository);
        } catch (RadixSvnException ex) {

            return false;
        }

        return true;
    }

    private static List<X509KeyManager> filterX509(javax.net.ssl.KeyManager[] keyManagers) {
        final List<X509KeyManager> x509KeyManagers = new ArrayList<X509KeyManager>();
        for (int index = 0; index < keyManagers.length; index++) {
            final javax.net.ssl.KeyManager keyManager = keyManagers[index];
            if (keyManager instanceof X509KeyManager) {
                x509KeyManagers.add((X509KeyManager) keyManager);
            }
        }
        return x509KeyManagers;
    }

    public static javax.net.ssl.KeyManager[] getKeyManagers(SvnRepository repository) throws RadixSvnException {
        KeyManagerFactory kmf;
        try {
            kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        } catch (NoSuchAlgorithmException ex) {
            throw new RadixSvnException("Unable to create SSL context: no default algorithm", ex);
        }
        try {

            SvnCredentials credentials = repository.getCredentials();
            if (credentials == null) {
                return new javax.net.ssl.KeyManager[0];
            }
            KeyStore ks = KeyStore.getInstance("PKCS12");
            ks.load(credentials.getCertificateData(), credentials.getCertificatePassword());
//            Enumeration<String> aliases = ks.aliases();
//            while (aliases.hasMoreElements()) {
//                System.out.println(aliases.nextElement());
//            }
            kmf.init(ks, repository.getCredentials().getPassPhrase());
            return kmf.getKeyManagers();
        } catch (RadixSvnException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RadixSvnException("Unable to create SSL context", ex);
        }
    }
}
