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

package org.radixware.kernel.common.client.eas;

import java.io.*;
import java.math.BigInteger;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.exceptions.SslTrustManagerException;


public abstract class AbstractSslTrustManager implements X509TrustManager {

    private static final String TRUST_MANAGER_IMPLEMENTATION = "SunX509";
    private static final char[] DEFAULT_TRUSTSTORE_PWD = new char[]{'p', 'a', 's', 's', 'w', 'o', 'r', 'd', '1', '2', '3', '4'};

    /**
     * STORE - save a certificate in a trusted store ACCEPT - accept a
     * certificate temporarily REJECT - reject a certificate
     */
    public enum Confirmation {

        STORE, ACCEPT, REJECT
    };

    protected enum ConfirmationReason {

        UNKNOWN, NOT_VALID
    };
    private X509TrustManager defaultManager;
    private final String trustStorePath;
    private final char[] trustStorePwd;
    private final List<Certificate> tempTrustedCerts = new ArrayList<>();
    private final IClientEnvironment environment;
    private final X509Certificate clientCertificate;

    public AbstractSslTrustManager(final IClientEnvironment environment, final String trustStorePath, final X509Certificate clientCertificate) {
        this(environment, trustStorePath, null, clientCertificate);
    }

    public AbstractSslTrustManager(final IClientEnvironment environment, final String trustStorePath, final char[] trustStorePwd, final X509Certificate clientCertificate) {
        this.trustStorePath = trustStorePath;
        this.environment = environment;
        this.trustStorePwd = trustStorePwd == null ? DEFAULT_TRUSTSTORE_PWD : trustStorePwd;
        this.clientCertificate = clientCertificate;
        try {
            reloadManager();
        } catch (SslTrustManagerException ex) {
            processException(ex);
        }
    }

    /**
     * The method provides a front end to check if a certificate is trusted
     *
     * @param cert certificate to be confirmed
     * @param reason reason why the certificate must be confirmed. It defines
     * ability to save the confirmation permanently.
     * @return
     */
    protected abstract Confirmation confirm(final X509Certificate cert, ConfirmationReason reason) throws SslTrustManagerException;

    protected boolean isClientSignedByServer(final X509Certificate serverCertificate) {
        try {
            if (clientCertificate == null) {
                return false;
            }
            clientCertificate.verify(serverCertificate.getPublicKey());
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    private void processException(final Exception ex) {
        environment.getTracer().error(ex);
    }

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        defaultManager.checkClientTrusted(chain, authType);
    }

    /**
     * Checks is the server certificates chain is valid. If the server
     * certificate is in user's trust-store, then it's accepted. Otherwise, user
     * is asked to make a decision on the server certificate trustiness.
     *
     * @param chain
     * @param authType
     * @throws CertificateException
     */
    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        final X509Certificate root = chain[0];
        try {
            for (int i = 0; i < chain.length; i++) {
                chain[i].checkValidity();
            }
            defaultManager.checkServerTrusted(chain, authType);
        } catch (CertificateExpiredException | CertificateNotYetValidException notValid) {
            handleUserResponse(root, ConfirmationReason.NOT_VALID);
        } catch (CertificateRevokedException revoked) {
            final String message = environment.getMessageProvider().translate("AbstractSslTrustManager", "The server certificate is revoked");
            throw new CertificateException(message, revoked);
        } catch (CertificateException ex) {
            handleUserResponse(root, ConfirmationReason.UNKNOWN);
        }
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return defaultManager.getAcceptedIssuers();
    }

    private void addTrustedCertificate(final Certificate cert, final boolean permanent) {
        if (permanent) {
            try {
                final KeyStore trustStore = readTrustStore();
                if (trustStore != null) {
                    // creating pseudo-random alias
                    byte[] hexAlias = Arrays.copyOfRange(cert.getPublicKey().getEncoded(), 0, 8);
                    String alias = String.format("%2X", new BigInteger(1, hexAlias));
                    trustStore.setCertificateEntry(alias, cert);
                    saveTrustStore(trustStore);
                }
            } catch (KeyStoreException ex) {
                // failed to save a certificate, so no need to refresh the manager
                return;
            }
        } else {
            tempTrustedCerts.add(cert);
        }
        try {
            reloadManager();
        } catch (SslTrustManagerException ex) {
            processException(ex);
        }
    }

    private void reloadManager() throws SslTrustManagerException {
        try {
            final KeyStore keyStore = readTrustStore();
            for (Certificate c : tempTrustedCerts) {
                keyStore.setCertificateEntry(UUID.randomUUID().toString(), c);
            }

            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TRUST_MANAGER_IMPLEMENTATION);
            tmf.init(keyStore);
            TrustManager[] managers = tmf.getTrustManagers();
            for (int i = 0; i < managers.length; i++) {
                if (managers[i] instanceof X509TrustManager) {
                    defaultManager = (X509TrustManager) managers[i];
                    return;
                }
            }
        } catch (KeyStoreException ex) {
            throw new SslTrustManagerException(ex.getLocalizedMessage(), ex);
        } catch (NoSuchAlgorithmException ex) {
            throw new SslTrustManagerException(ex.getLocalizedMessage(), ex);
        }
    }

    private KeyStore readTrustStore() throws KeyStoreException {
        KeyStore keyStore = null;
        FileInputStream tsReader = null;

        try {
            File ksFile = new File(trustStorePath);
            if (!ksFile.exists()) {
                ksFile.createNewFile();
                keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
                keyStore.load(null, null);
                final FileOutputStream keystoreWriter = new FileOutputStream(ksFile);
                keyStore.store(keystoreWriter, trustStorePwd);
                keystoreWriter.close();
            }

            tsReader = new FileInputStream(trustStorePath);
            keyStore = KeyStore.getInstance("JCEKS");
            keyStore.load(tsReader, trustStorePwd);
            return keyStore;
        } catch (FileNotFoundException ex) {
            // never occurs
            Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new KeyStoreException("Failed to read the keystore", ex);
        } finally {
            try {
                if (tsReader != null) {
                    tsReader.close();
                }
            } catch (IOException ex) {
                processException(ex);
            }
        }
        return keyStore;
    }

    private void saveTrustStore(final KeyStore ks) throws KeyStoreException {
        try (FileOutputStream stream = new FileOutputStream(trustStorePath)) {
            ks.store(stream, trustStorePwd);
        } catch (Exception ex) {
            throw new KeyStoreException("Failed to write the keystore on the disk", ex);
        }
    }

    private void handleUserResponse(final X509Certificate currentCertificate, ConfirmationReason reason) throws CertificateException {
        try {
            final Confirmation conf;
            if (isClientSignedByServer(currentCertificate) && reason == ConfirmationReason.UNKNOWN) {
                conf = Confirmation.ACCEPT;
            } else {
                conf = confirm(currentCertificate, reason);
            }

            boolean permanent = false;
            switch (conf) {
                case REJECT:
                    final String message = environment.getMessageProvider().translate("AbstractSslTrustManager", "Untrusted certificate");
                    throw new CertificateException(message);
                case STORE:
                    permanent = true;
                    break;
                case ACCEPT:
                    permanent = false;
                    break;
            }
            addTrustedCertificate(currentCertificate, permanent);
        } catch (SslTrustManagerException stme) {
            throw new CertificateException(stme);
        } catch (NullPointerException npe) {
            final String message = environment.getMessageProvider().translate("AbstractSslTrustManager", "Trust manager was not properly initialized");
            throw new CertificateException(message);
        }
    }
}
