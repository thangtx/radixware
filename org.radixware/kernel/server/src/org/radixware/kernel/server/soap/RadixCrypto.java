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

package org.radixware.kernel.server.soap;

import java.io.IOException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertPath;
import java.security.cert.CertPathValidator;
import java.security.cert.Certificate;
import java.security.cert.PKIXParameters;
import java.security.cert.TrustAnchor;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import org.apache.ws.security.WSPasswordCallback;
import org.apache.ws.security.WSSecurityException;
import static org.apache.ws.security.components.crypto.CryptoBase.NAME_CONSTRAINTS_OID;
import org.apache.ws.security.components.crypto.CryptoType;
import org.apache.ws.security.components.crypto.Merlin;
import static org.apache.ws.security.components.crypto.Merlin.KEYSTORE_PRIVATE_PASSWORD;
import org.radixware.kernel.common.exceptions.KeystoreControllerException;
import org.radixware.kernel.common.ssl.KeystoreController;


public class RadixCrypto extends Merlin {

    private final List<String> trustedCertAliases;

    public RadixCrypto(List<String> trustedCertAliases) throws KeystoreControllerException {
        this.trustedCertAliases = trustedCertAliases == null ? null : new ArrayList<>(trustedCertAliases);
        setKeyStore(KeystoreController.getServerKeystoreController().getKeystore());
        setTrustStore(getKeyStore());
    }

    @Override
    public X509Certificate[] getX509Certificates(CryptoType cryptoType) throws WSSecurityException {
        X509Certificate[] result = super.getX509Certificates(cryptoType);
        //search by alias is used in outgoing activities and there is no need to filter it's results
        if (cryptoType.getType() == CryptoType.TYPE.ALIAS) {
            return result;
        }
        if (trustedCertAliases == null) {
            return result;
        }
        if (result == null || trustedCertAliases.isEmpty()) {
            return null;
        }

        final List<X509Certificate> filteredResult = new ArrayList<>();
        for (X509Certificate cert : result) {
            for (String alias : trustedCertAliases) {
                try {
                    final Certificate trustedCert = keystore.getCertificate(alias);
                    if (trustedCert != null && trustedCert.equals(cert)) {
                        filteredResult.add(cert);
                    }
                } catch (KeyStoreException ex) {
                    //skip
                }
            }
        }
        return filteredResult.toArray(new X509Certificate[filteredResult.size()]);
    }

    /**
     * Overridden for taking trustedCertAliases into account
     */
    @Override
    public boolean verifyTrust(X509Certificate[] certs, boolean enableRevocation) throws WSSecurityException {
        if (trustedCertAliases == null) {
            return true;
        }
        if (trustedCertAliases.isEmpty()) {
            return false;
        }
        try {
            List<X509Certificate> certificates = Arrays.asList(certs);
            CertPath certPath = getCertificateFactory().generateCertPath(certificates);
            Set<TrustAnchor> trustAnchors = new HashSet<>();

            for (String alias : trustedCertAliases) {
                X509Certificate trustedCert = (X509Certificate) truststore.getCertificate(alias);
                if (trustedCert != null) {
                    TrustAnchor anchor = new TrustAnchor(trustedCert, trustedCert.getExtensionValue(NAME_CONSTRAINTS_OID));
                    trustAnchors.add(anchor);
                }
            }

            PKIXParameters pkixParameters = new PKIXParameters(trustAnchors);
            pkixParameters.setRevocationEnabled(enableRevocation);

            if (enableRevocation && crlCertStore != null) {
                pkixParameters.addCertStore(crlCertStore);
            }

            String provider = getCryptoProvider();
            CertPathValidator validator = null;
            if (provider == null || provider.length() == 0) {
                validator = CertPathValidator.getInstance("PKIX");
            } else {
                validator = CertPathValidator.getInstance("PKIX", provider);
            }
            validator.validate(certPath, pkixParameters);
            return true;
        } catch (java.security.NoSuchProviderException | java.security.NoSuchAlgorithmException | java.security.cert.CertificateException | java.security.InvalidAlgorithmParameterException | java.security.cert.CertPathValidatorException | java.security.KeyStoreException | NullPointerException e) {
            throw new WSSecurityException(WSSecurityException.FAILURE, "certpath", new Object[]{e.getMessage()}, e);
        }
    }

    /**
     * Overridden for taking trustedCertAliases into account
     */
    @Override
    public boolean verifyTrust(PublicKey publicKey) throws WSSecurityException {
        if (trustedCertAliases == null) {
            return true;
        }
        if (trustedCertAliases.isEmpty()) {
            return false;
        }
        try {
            for (String alias : trustedCertAliases) {
                Certificate certificateToCheck;
                final Certificate[] chains = getTrustStore().getCertificateChain(alias);
                if (chains == null || chains.length == 0) {
                    certificateToCheck = getTrustStore().getCertificate(alias);
                } else {
                    certificateToCheck = chains[0];
                }
                if (certificateToCheck instanceof X509Certificate) {
                    if (publicKey.equals(((X509Certificate) certificateToCheck).getPublicKey())) {
                        return true;
                    }
                }
            }
        } catch (KeyStoreException e) {
            return false;
        }
        return false;
    }

    /**
     * Gets the private key corresponding to the certificate.
     *
     * @param certificate The X509Certificate corresponding to the private key
     * @param callbackHandler The callbackHandler needed to get the password
     * @return The private key
     */
    @Override
    public PrivateKey getPrivateKey(
            X509Certificate certificate,
            CallbackHandler callbackHandler) throws WSSecurityException {
        if (keystore == null) {
            throw new WSSecurityException("The keystore is null");
        }
        if (callbackHandler == null) {
            throw new WSSecurityException("The CallbackHandler is null");
        }

        String identifier = findKeyIdentifier(certificate, keystore);
        try {
            if (identifier == null || !keystore.isKeyEntry(identifier)) {
                String msg = "Cannot find key for alias: [" + identifier + "]";
                throw new WSSecurityException(msg);
            }
            String password = getPassword(identifier, callbackHandler);
            if (password == null && privatePasswordSet) {
                password = properties.getProperty(KEYSTORE_PRIVATE_PASSWORD);
                if (password != null) {
                    password = password.trim();
                }
            }
            Key keyTmp = keystore.getKey(identifier, password == null
                    ? new char[]{} : password.toCharArray());
            if (!(keyTmp instanceof PrivateKey)) {
                String msg = "Key is not a private key, alias: [" + identifier + "]";
                throw new WSSecurityException(msg);
            }
            return (PrivateKey) keyTmp;
        } catch (KeyStoreException | UnrecoverableKeyException | NoSuchAlgorithmException ex) {
            throw new WSSecurityException(
                    WSSecurityException.FAILURE, "noPrivateKey", new Object[]{ex.getMessage()}, ex);
        }
    }

    private String findKeyIdentifier(X509Certificate cert, KeyStore store)
            throws WSSecurityException {
        String result = null;
        try {

            for (Enumeration<String> e = store.aliases(); e.hasMoreElements();) {
                String alias = e.nextElement();

                Certificate[] certs = store.getCertificateChain(alias);
                Certificate retrievedCert = null;
                if (certs == null || certs.length == 0) {
                    // no cert chain, so lets check if getCertificate gives us a  result.
                    retrievedCert = store.getCertificate(alias);
                    if (retrievedCert == null) {
                        continue;
                    }
                } else {
                    retrievedCert = certs[0];
                }
                if (!(retrievedCert instanceof X509Certificate)) {
                    continue;
                }
                if (retrievedCert.equals(cert)) {
                    if (store.isKeyEntry(alias)) {
                        return alias;
                    }
                    result = alias;
                }
            }
        } catch (KeyStoreException e) {
            throw new WSSecurityException(WSSecurityException.FAILURE, "keystore", null, e);
        }
        return result;
    }

    private String getPassword(
            String identifier,
            CallbackHandler cb) throws WSSecurityException {
        WSPasswordCallback pwCb =
                new WSPasswordCallback(identifier, WSPasswordCallback.DECRYPT);
        try {
            Callback[] callbacks = new Callback[]{pwCb};
            cb.handle(callbacks);
        } catch (IOException | UnsupportedCallbackException e) {
            throw new WSSecurityException(
                    WSSecurityException.FAILURE,
                    "noPassword",
                    new Object[]{identifier},
                    e);
        }

        return pwCb.getPassword();
    }
}
