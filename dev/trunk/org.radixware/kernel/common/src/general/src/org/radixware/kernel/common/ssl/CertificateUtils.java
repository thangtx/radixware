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
package org.radixware.kernel.common.ssl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.net.Socket;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.RSAKeyGenParameterSpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.naming.InvalidNameException;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;
import javax.net.ssl.*;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.cms.SignerInformationStore;
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoVerifierBuilder;

import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.PEMWriter;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.x509.X509V3CertificateGenerator;

import org.radixware.kernel.common.exceptions.CertificateUtilsException;
import org.radixware.kernel.common.exceptions.KeystoreControllerException;
import org.radixware.kernel.common.types.ArrBin;
import org.radixware.kernel.common.utils.DebugLog;
import org.radixware.kernel.common.utils.Hex;
import org.radixware.kernel.common.utils.SystemPropUtils;

/**
 * Class for standard operations over certificates and keys
 *
 */
public final class CertificateUtils {

    public static final String KEY_MANAGER_ALGO = SystemPropUtils.getBooleanSystemProp("rdx.use.default.algo.for.key.manager", true) ? KeyManagerFactory.getDefaultAlgorithm() : "SunX509";
    public static final String ALGORITHM_MD5 = "MD5";
    public static final String ALGORITHM_SHA1 = "SHA1";

    static {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    }

    /**
     * Generates an RSA key and self-signs it
     */
    public static KeystoreRsaKeyEntry generateAndSignKey(final int keySize, final int publicExponent, final String distinguishedName, final Date expiration, final String alias)
            throws CertificateUtilsException {
        final KeyPairGenerator keyPairGenerator;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(new RSAKeyGenParameterSpec(keySize, BigInteger.valueOf(publicExponent)));
        } catch (GeneralSecurityException e) {
            throw new CertificateUtilsException(e.getMessage(), e);
        }
        final KeyPair keyPair = keyPairGenerator.generateKeyPair();
        final X509Certificate certificate = createSelfSignedCertificate(keyPair, expiration, distinguishedName);
        return new KeystoreRsaKeyEntry(alias, new X509Certificate[]{certificate}, keyPair.getPrivate());
    }

    public static KeystoreRsaKeyEntry generateAndSignKey(final int keySize, final int publicExponent, final String distinguishedName, final Date expiration)
            throws CertificateUtilsException {
        return generateAndSignKey(keySize, publicExponent, distinguishedName, expiration, "");
    }

    /**
     * Calculates a certificate's fingerprint
     */
    public static byte[] getCertificateFingerprint(final String algorithm, final X509Certificate certificate)
            throws CertificateUtilsException {
        final byte[] encodedCertificateInfo;
        final MessageDigest messageDigest;
        try {
            encodedCertificateInfo = certificate.getEncoded();
            messageDigest = MessageDigest.getInstance(algorithm);
        } catch (GeneralSecurityException e) {
            throw new CertificateUtilsException(e.getMessage(), e);
        }

        return messageDigest.digest(encodedCertificateInfo);
    }

    /**
     * Calculates certificate's fingerprint and brings it to readable form
     */
    public static String getCertificateFingerprintAsString(final String algorithm, final X509Certificate certificate)
            throws CertificateUtilsException {
        return toHexString(getCertificateFingerprint(algorithm, certificate));
    }

    private static void byte2hex(final byte b, final StringBuffer buf) {
        final char[] hexChars = {'0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        buf.append(hexChars[(b & 0xf0) >> 4]);
        buf.append(hexChars[b & 0x0f]);
    }

    private static String toHexString(final byte[] block) {
        final StringBuffer buf = new StringBuffer();
        final int len = block.length;
        for (int i = 0; i < len; i++) {
            byte2hex(block[i], buf);
            if (i < len - 1) {
                buf.append(":");
            }
        }
        return buf.toString();
    }

    /**
     * Generates self-signed certificate for a key
     */
    private static X509Certificate createSelfSignedCertificate(final KeyPair keyPair, final Date expiration, final String distinguishedName)
            throws CertificateUtilsException {
        final X500Principal issuerDN = new X500Principal(distinguishedName);
        final X500Principal subjectDN = new X500Principal(distinguishedName);
        final PrivateKey privateKey = (PrivateKey) keyPair.getPrivate();
        final PublicKey publicKey = (PublicKey) keyPair.getPublic();
        return generateX509Certificate(privateKey, issuerDN, publicKey, subjectDN, expiration);
    }

    /**
     * Generates a certificate
     */
    private static X509Certificate generateX509Certificate(final PrivateKey issuerPrivateKey, final X500Principal issuerDN, final PublicKey subjectPublicKey, final X500Principal subjectDN, final Date expiration)
            throws CertificateUtilsException {
        final X509V3CertificateGenerator certificateGenerator = new X509V3CertificateGenerator();

        certificateGenerator.setSerialNumber(BigInteger.valueOf(System.currentTimeMillis()));
        certificateGenerator.setIssuerDN(issuerDN);
        certificateGenerator.setNotBefore(new Date(System.currentTimeMillis()));
        certificateGenerator.setNotAfter(expiration);
        certificateGenerator.setSubjectDN(subjectDN);
        certificateGenerator.setPublicKey(subjectPublicKey);
        certificateGenerator.setSignatureAlgorithm("SHA1WithRSA");

        try {
            return certificateGenerator.generate(issuerPrivateKey);
        } catch (GeneralSecurityException e) {
            throw new CertificateUtilsException(e.getMessage(), e);
        } catch (IllegalStateException e) {
            throw new CertificateUtilsException(e.getMessage(), e);
        }
    }

    /**
     * Prepares Certificate Signing Request (CSR) and writes it to a file
     */
    private static void writeCertificateRequest(final String filePath, final X500Principal subject, final PublicKey publicKey, final PrivateKey privateKey, final String provider)
            throws CertificateUtilsException {
        final String signatureAlgorithm = "SHA256withRSA";
        final PKCS10CertificationRequest request;
        try {
            request = new PKCS10CertificationRequest(signatureAlgorithm, subject, publicKey, null, privateKey, provider);
        } catch (GeneralSecurityException e) {
            throw new CertificateUtilsException(e.getMessage(), e);
        }

        writePemEncodedData(request, filePath);
    }

    /**
     * Exports a certificate to a file
     *
     * @param certificate X.509 certificate to be exported
     * @param certFilePath file path
     * @throws CertificateUtilsException
     */
    public static void exportCertificate(final X509Certificate certificate, final String certFilePath)
            throws CertificateUtilsException {
        writePemEncodedData(certificate, certFilePath);
    }

    /**
     * Writes data to a file in PEM-encoded format
     *
     * @param object data to be written
     * @param filePath file path
     * @throws CertificateUtilsException
     */
    private static void writePemEncodedData(final Object object, final String filePath)
            throws CertificateUtilsException {
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(filePath);
            final PEMWriter writer = new PEMWriter(new OutputStreamWriter(outputStream));
            if (object instanceof X509Certificate[]) {
                final X509Certificate[] chain = (X509Certificate[]) object;
                for (X509Certificate certificate : chain) {
                    writer.writeObject(certificate);
                }
            } else if (object instanceof X509Certificate) {
                final X509Certificate certificate = (X509Certificate) object;
                writer.writeObject(certificate);
            } else if (object instanceof PKCS10CertificationRequest) {
                final PKCS10CertificationRequest certificateRequest = (PKCS10CertificationRequest) object;
                writer.writeObject(certificateRequest);
            }
            writer.flush();
        } catch (IOException e) {
            throw new CertificateUtilsException(e.getMessage(), e);
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    //ignore
                }
            }
        }
    }

    /**
     * Reads Certificate Signing Request (CSR), prepares client key's
     * certificate and appends issuer certificate chain to it
     */
    private static X509Certificate[] signCertificate(final byte[] certificateRequest, final Date expiration, final PrivateKey issuerPrivateKey, final Certificate[] issuerCertificateChain)
            throws CertificateUtilsException {
        final X509Certificate issuerCertificate = (X509Certificate) issuerCertificateChain[0];
        final PKCS10CertificationRequest request;
        if (isPemEncoded(certificateRequest)) { //PEM-encoded (Base64)
            final PEMParser reader = new PEMParser(new InputStreamReader(new ByteArrayInputStream(certificateRequest)));
            try {
                PemObject pemObject = reader.readPemObject();
                request = new PKCS10CertificationRequest(pemObject.getContent());
            } catch (IOException e) {
                throw new CertificateUtilsException(e.getMessage(), e);
            }
        } else { //DER-encoded (binary)
            try {
                request = new PKCS10CertificationRequest(certificateRequest);
            } catch (Exception e) {
                throw new CertificateUtilsException(e.getMessage(), e);
            }
        }

        final PublicKey subjectPublicKey;
        final X500Principal subjectDN;
        try {
            subjectPublicKey = request.getPublicKey();
            subjectDN = new X500Principal(request.getCertificationRequestInfo().getSubject().getEncoded());
        } catch (GeneralSecurityException e) {
            throw new CertificateUtilsException(e.getMessage(), e);
        } catch (IOException e) {
            throw new CertificateUtilsException(e.getMessage(), e);
        }

        final X509Certificate subjectCertificate = generateX509Certificate(issuerPrivateKey, issuerCertificate.getSubjectX500Principal(), subjectPublicKey, subjectDN, expiration);

        final X509Certificate[] chain = new X509Certificate[1 + issuerCertificateChain.length];
        chain[0] = subjectCertificate;
        System.arraycopy(issuerCertificateChain, 0, chain, 1, issuerCertificateChain.length);

        return chain;
    }

    /**
     * Checks whether Certificate Signing Request (CSR) is PEM-formatted
     */
    private static boolean isPemEncoded(final byte[] object) {
        final String checkString = "-----BEGIN";
        final byte startBytes[] = Arrays.copyOf(object, checkString.length());
        return Arrays.equals(startBytes, checkString.getBytes());
    }

    /**
     * Builds a distinguished name from components
     */
    public static String buildDistinguishedName(final String CN, final String OU, final String O, final String L, final String ST, final String C) {
        return buildDistinguishedName(CN, OU, O, L, ST, C, "");
    }

    private static void addRdnIfValid(final String type, final String value, final List<Rdn> rdnList) {
        if (value != null && !value.isEmpty()) {
            try {
                rdnList.add(new Rdn(type, value));
            } catch (InvalidNameException exception) {

            }
        }
    }

    public static String buildDistinguishedName(final String CN, final String OU, final String O, final String L, final String ST, final String C, final String UID) {
        final List<Rdn> rdnList = new ArrayList<>();
        addRdnIfValid("C", C, rdnList);
        addRdnIfValid("ST", ST, rdnList);
        addRdnIfValid("L", L, rdnList);
        addRdnIfValid("O", O, rdnList);
        addRdnIfValid("OU", OU, rdnList);
        addRdnIfValid("CN", CN, rdnList);
        addRdnIfValid("UID", UID, rdnList);
        if (rdnList.isEmpty()) {//never leave DN empty
            addRdnIfValid("CN", "unknown", rdnList);
        }
        return new LdapName(rdnList).toString();
    }

    /**
     * Splits a distinguished name into pairs "property-value"
     */
    public static HashMap<String, String> parseDistinguishedName(final String distinguishedName) {
        final HashMap<String, String> map = new HashMap<>();
        final LdapName ldapName;
        try {
            ldapName = new LdapName(distinguishedName);
        } catch (InvalidNameException exception) {
            return map;
        }
        for (Rdn rdn : ldapName.getRdns()) {
            final NamingEnumeration<? extends Attribute> attributes = rdn.toAttributes().getAll();
            try {
                while (attributes.hasMore()) {
                    final Attribute attribute = attributes.next();
                    map.put(attribute.getID(), String.valueOf(Rdn.unescapeValue(String.valueOf(attribute.get()))));
                }
            } catch (NamingException exception) {

            }
        }
        return map;
    }

    /**
     * Prepares an SSLContext
     *
     * @param keystoreController
     * @param sslAuthKeyPassword password for accessing (single) private key in
     * keystore; if ssl self-authentication is not needed then <code>null</code>
     * @return ssl context
     * @throws CertificateUtilsException
     */
    public static SSLContext prepareSslContext(final KeystoreController keystoreController, final char[] sslAuthKeyPassword)
            throws CertificateUtilsException {
        return prepareSslContext(keystoreController, sslAuthKeyPassword, null, null);
    }

    /**
     * Prepares an SSLContext with proper key manager and trust manager. Proper
     * key manager means that a certificate is chosen by alias, instead of
     * picking the first one in the row.
     *
     * @param keystoreController
     * @param sslAuthKeyPassword
     * @param trustManager
     * @param alias alias to chose a certificate by. If null, then the first one
     * is picked.
     * @return
     * @throws CertificateUtilsException
     */
    public static SSLContext prepareSslContext(final KeystoreController keystoreController, final char[] sslAuthKeyPassword, final X509TrustManager trustManager, String alias)
            throws CertificateUtilsException {
        if (keystoreController == null) {
            throw new CertificateUtilsException("KeystoreController is null");
        }
        TrustManagerFactory trustManagerFactory = null;
        try {
            trustManagerFactory = TrustManagerFactory.getInstance(KEY_MANAGER_ALGO);
            keystoreController.initTrustManagerFactory(trustManagerFactory);
        } catch (NoSuchAlgorithmException e) {
            throw new CertificateUtilsException(e.getMessage(), e);
        } catch (KeystoreControllerException e) {
            throw new CertificateUtilsException(e.getMessage(), e);
        }

        KeyManager[] keyManagers = null;
        if (sslAuthKeyPassword != null) {
            final KeyManagerFactory keyManagerFactory;
            try {
                keyManagerFactory = KeyManagerFactory.getInstance(KEY_MANAGER_ALGO);
                keystoreController.initKeyManagerFactory(keyManagerFactory, sslAuthKeyPassword);
            } catch (NoSuchAlgorithmException | KeystoreControllerException e) {
                throw new CertificateUtilsException(e.getMessage(), e);
            }

            keyManagers = keyManagerFactory.getKeyManagers();
        }

        try {
            if (keyManagers != null && alias != null) {
                for (int i = 0; i < keyManagers.length; i++) {
                    if (keyManagers[i] instanceof X509KeyManager) {
                        keyManagers[i] = new x509kmproxy((X509KeyManager) keyManagers[i], alias);
                    }
                }
            }

            final SSLContext sslContext = SSLContext.getInstance("TLS"); //контекст первого провайдера, поддерживающего указанный протокол

            sslContext.init(keyManagers,
                    trustManager == null
                            ? trustManagerFactory.getTrustManagers()
                            : new TrustManager[]{trustManager},
                    null);
            return sslContext;
        } catch (GeneralSecurityException e) {
            throw new CertificateUtilsException(e.getMessage(), e);
        }
    }

    private static final class x509kmproxy implements X509KeyManager {

        private final String alias;
        private final X509KeyManager src;

        public x509kmproxy(X509KeyManager src, String alias) {
            this.alias = alias;
            this.src = src;
        }

        @Override
        public String[] getClientAliases(String keyType, Principal[] issuers) {
            String[] result = src.getClientAliases(keyType, issuers);
            return result;
        }

        @Override
        public String chooseClientAlias(String[] keyType, Principal[] issuers, Socket socket) {
            for (String kt : keyType) {
                String[] aliases = getClientAliases(kt, issuers);
                if (aliases != null) {
                    for (String alias : aliases) {
                        if (this.alias.equals(alias)) {
                            return alias;
                        }
                    }
                }
            }
            return null;
        }

        @Override
        public String[] getServerAliases(String keyType, Principal[] issuers) {
            String[] result = src.getServerAliases(keyType, issuers);
            return result;
        }

        @Override
        public String chooseServerAlias(String keyType, Principal[] issuers, Socket socket) {
            String result = src.chooseServerAlias(keyType, issuers, socket);
            return result;
        }

        @Override
        public X509Certificate[] getCertificateChain(String alias) {
            if (this.alias.equals(alias)) {
                return src.getCertificateChain(alias);
            } else {
                return null;
            }
        }

        @Override
        public PrivateKey getPrivateKey(String a) {
            if (alias.equals(a)) {
                return src.getPrivateKey(alias);
            } else {
                return null;
            }
        }
    };

    public static SSLContext prepareSslContext(final KeystoreController keystoreController, final X509Certificate clientCert, final String alias, final char[] sslAuthKeyPassword)
            throws CertificateUtilsException {
        if (keystoreController == null) {
            throw new CertificateUtilsException("KeystoreController is null");
        }
        final TrustManagerFactory trustManagerFactory;
        try {
            trustManagerFactory = TrustManagerFactory.getInstance(KEY_MANAGER_ALGO);
            keystoreController.initTrustManagerFactory(trustManagerFactory);
        } catch (NoSuchAlgorithmException e) {
            throw new CertificateUtilsException(e.getMessage(), e);
        } catch (KeystoreControllerException e) {
            throw new CertificateUtilsException(e.getMessage(), e);
        }

        KeyManager[] keyManagers = null;
        if (sslAuthKeyPassword != null) {
            final KeyManagerFactory keyManagerFactory;
            try {
                keyManagerFactory = KeyManagerFactory.getInstance(KEY_MANAGER_ALGO);
                keystoreController.initKeyManagerFactory(keyManagerFactory, sslAuthKeyPassword);
            } catch (NoSuchAlgorithmException e) {
                throw new CertificateUtilsException(e.getMessage(), e);
            } catch (KeystoreControllerException e) {
                throw new CertificateUtilsException(e.getMessage(), e);
            }

            keyManagers = keyManagerFactory.getKeyManagers();
        }

        try {
//            if (keyManagers != null) {
//                for (int i = 0; i < keyManagers.length; i++) {
//                    if (keyManagers[i] instanceof X509KeyManager) {
//                        keyManagers[i] = new x509kmproxy((X509KeyManager) keyManagers[i], alias);
//                    }
//                }
//            }

            final SSLContext sslContext = SSLContext.getInstance("TLS"); //контекст первого провайдера, поддерживающего указанный протокол
            sslContext.init(keyManagers, trustManagerFactory.getTrustManagers(), null);
            return sslContext;
        } catch (GeneralSecurityException e) {
            throw new CertificateUtilsException(e.getMessage(), e);
        }
    }

    /**
     * Prepares SSLContext for server classes.
     *
     * @param ownKeyAliases aliases of keys to be used for self-authentication
     * @param trustedCertificateAliases aliases of trusted certificates to be
     * used for peer authentication
     * @return ssl context
     * @throws CertificateUtilsException
     */
    public static SSLContext prepareServerSslContext(final List<String> ownKeyAliases, final List<String> trustedCertificateAliases) throws CertificateUtilsException {
        return prepareServerSslContext(ownKeyAliases, trustedCertificateAliases, "TLS", null);
    }

    public static SSLContext prepareServerSslContext(final List<String> ownKeyAliases, final List<String> trustedCertificateAliases, String protocol, String provider)
            throws CertificateUtilsException {
        final KeyManager[] serverKeyManagers = createServerKeyManagers(ownKeyAliases);
        final TrustManager[] serverTrustManagers = createServerTrustManagers(trustedCertificateAliases);
        try {
            final SSLContext serverSslContext = provider == null ? SSLContext.getInstance(protocol) : SSLContext.getInstance(protocol, provider);
            serverSslContext.init(serverKeyManagers, serverTrustManagers, null);
            return serverSslContext;
        } catch (GeneralSecurityException e) {
            throw new CertificateUtilsException(e.getMessage(), e);
        }
    }

    public static KeyManager[] createServerKeyManagers(final List<String> ownKeyAliases) throws CertificateUtilsException {
        final KeyManagerFactory serverKeyManagerFactory;
        DebugLog.logUpDown("CertificateUtils.createServerKeyManagers: KEY_MANAGER_ALGO = " + KEY_MANAGER_ALGO);
        try {
            serverKeyManagerFactory = KeyManagerFactory.getInstance(KEY_MANAGER_ALGO);
            KeystoreController.initServerKeyManagerFactory(serverKeyManagerFactory);
        } catch (NoSuchAlgorithmException e) {
            throw new CertificateUtilsException(e.getMessage(), e);
        } catch (KeystoreControllerException e) {
            throw new CertificateUtilsException(e.getMessage(), e);
        }

        final KeyManager[] serverKeyManagers = serverKeyManagerFactory.getKeyManagers();
        for (int i = 0; i < serverKeyManagers.length; i++) {
            if (serverKeyManagers[i] instanceof X509KeyManager) {
                serverKeyManagers[i] = new KeyManagerFilter(serverKeyManagers[i], ownKeyAliases);
            }
        }
        return serverKeyManagers;
    }

    public static TrustManager[] createServerTrustManagers(final List<String> trustedCertificateAliases) throws CertificateUtilsException {
        final TrustManagerFactory serverTrustManagerFactory;
        final KeystoreController serverKeyStore;
        try {
            serverTrustManagerFactory = TrustManagerFactory.getInstance(KEY_MANAGER_ALGO);
            KeystoreController.initServerTrustManagerFactory(serverTrustManagerFactory);
            serverKeyStore = KeystoreController.getServerKeystoreController();
        } catch (NoSuchAlgorithmException e) {
            throw new CertificateUtilsException(e.getMessage(), e);
        } catch (KeystoreControllerException e) {
            throw new CertificateUtilsException(e.getMessage(), e);
        }

        final TrustManager[] serverTrustManagers = serverTrustManagerFactory.getTrustManagers();
        for (int i = 0; i < serverTrustManagers.length; i++) {
            if (serverTrustManagers[i] instanceof X509TrustManager) {
                serverTrustManagers[i] = new TrustManagerFilter(serverTrustManagers[i],
                        trustedCertificateAliases,
                        serverKeyStore);
            }
        }
        return serverTrustManagers;
    }

    /**
     * Prepares and writes to a file a Certificate Signing Request (CSR) for the
     * specified alias
     *
     * @param keyEntry key entry to be certified
     * @param filePath CSR file path
     * @param keystoreProviderName keystore's provider name; may be null for
     * file-based keystores
     * @throws CertificateUtilsException
     */
    public static void requestCertificate(final KeystoreRsaKeyEntry keyEntry, final String filePath, final String keystoreProviderName)
            throws CertificateUtilsException {
        final PrivateKey privateKey = (PrivateKey) keyEntry.getKey();
        final Certificate[] chain = keyEntry.getCertificateChain();

        final X509Certificate currentCertificate = (X509Certificate) chain[0];
        final PublicKey publicKey = (PublicKey) currentCertificate.getPublicKey();

        writeCertificateRequest(filePath, currentCertificate.getSubjectX500Principal(), publicKey, privateKey, keystoreProviderName);
    }

    /**
     * Signs client's certificate from requestFilePath and writes the resulting
     * certificate chain into responseFilePath
     *
     * @param signingKeyEntry signing key entry
     * @param requestFilePath request file path
     * @param expiration certificate's expiration date
     * @param responseFilePath response file path
     * @throws CertificateUtilsException
     */
    public static void processCertificateRequest(final KeystoreRsaKeyEntry signingKeyEntry, final String requestFilePath, final Date expiration, final String responseFilePath)
            throws CertificateUtilsException {
        final PrivateKey issuerPrivateKey = (PrivateKey) signingKeyEntry.getKey();
        final Certificate[] issuerCertificateChain = signingKeyEntry.getCertificateChain();

        final int fileLength = (int) new File(requestFilePath).length();
        final byte[] certificateRequest = new byte[fileLength];
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(requestFilePath);
            inputStream.read(certificateRequest);
        } catch (IOException e) {
            throw new CertificateUtilsException(e.getMessage(), e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    //ignore
                }
            }
        }

        final X509Certificate[] certificateChain = signCertificate(certificateRequest, expiration, issuerPrivateKey, issuerCertificateChain);
        writePemEncodedData(certificateChain, responseFilePath);
    }

    /**
     * Reads a certificate chain from a file
     */
    public static X509Certificate[] readCertificateChain(final String filePath)
            throws CertificateUtilsException {
        FileInputStream inputStream = null;
        final CertificateFactory certificateFactory;
        final Collection<? extends Certificate> certificates;
        try {
            inputStream = new FileInputStream(filePath);
            certificateFactory = CertificateFactory.getInstance("X.509");
            certificates = certificateFactory.generateCertificates(inputStream);
        } catch (CertificateException e) {
            throw new CertificateUtilsException(e.getMessage(), e);
        } catch (FileNotFoundException e) {
            throw new CertificateUtilsException(e.getMessage(), e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    //ignore
                }
            }
        }

        return certificates.toArray(new X509Certificate[0]); //stub parameter specifies the type of array elements
    }

    /**
     * Reads a trusted certificate from a file
     */
    public static X509Certificate readTrustedCertificate(final String filePath)
            throws CertificateUtilsException {
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(filePath);
            final CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            final X509Certificate certificate = (X509Certificate) certificateFactory.generateCertificate(inputStream);
            return certificate;
        } catch (CertificateException e) {
            throw new CertificateUtilsException(e.getMessage(), e);
        } catch (FileNotFoundException e) {
            throw new CertificateUtilsException(e.getMessage(), e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    //ignore
                }
            }
        }
    }

    public static String calcCertificateThumbPrint(final Certificate certificate) throws NoSuchAlgorithmException, CertificateEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] der = certificate.getEncoded();
        md.update(der);
        return Hex.encode(md.digest());
    }

    public static java.security.cert.X509Certificate convertFromJavax(javax.security.cert.X509Certificate cert) {
        try {
            byte[] encoded = cert.getEncoded();
            ByteArrayInputStream bis = new ByteArrayInputStream(encoded);
            java.security.cert.CertificateFactory cf
                    = java.security.cert.CertificateFactory.getInstance("X.509");
            return (java.security.cert.X509Certificate) cf.generateCertificate(bis);
        } catch (java.security.cert.CertificateEncodingException e) {
        } catch (javax.security.cert.CertificateEncodingException e) {
        } catch (java.security.cert.CertificateException e) {
        }
        return null;
    }

    public static java.security.cert.X509Certificate[] convertFromJavax(javax.security.cert.X509Certificate[] certs) {
        if (certs == null) {
            return null;
        }
        final List<java.security.cert.X509Certificate> certList = new ArrayList<>();
        java.security.cert.X509Certificate converted;
        for (javax.security.cert.X509Certificate cert : certs) {
            converted = convertFromJavax(cert);
            if (converted == null) {
                break;
            } else {
                certList.add(converted);
            }
        }
        if (certList.isEmpty()) {
            return new java.security.cert.X509Certificate[0];
        }
        return certList.toArray(new java.security.cert.X509Certificate[0]);
    }

    public static X509Certificate[] convertFromArrBin(final ArrBin arrBin) throws CertificateException {
        if (arrBin == null || arrBin.isEmpty()) {
            return new X509Certificate[]{};
        }
        final X509Certificate[] certificates = new X509Certificate[arrBin.size()];
        for (int i = 0; i < arrBin.size(); i++) {
            certificates[i]
                    = (X509Certificate) CertificateUtils.readCertificate("X.509", arrBin.get(i).get());
        }
        return certificates;
    }

    public static Certificate readCertificate(final String type, final byte[] bytes) throws CertificateException {
        final CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
        final ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        return certFactory.generateCertificate(in);
    }

    public static boolean verifySignature(final byte signature[], final byte[] data, final X509Certificate certificate) throws CMSException, OperatorCreationException {
        CMSSignedData s = new CMSSignedData(new CMSProcessableByteArray(data), signature);
        SignerInformationStore signers = s.getSignerInfos();
        SignerInformation signerInfo = (SignerInformation) signers.getSigners().iterator().next();
        return signerInfo.verify(new JcaSimpleSignerInfoVerifierBuilder().setProvider("BC").build(certificate.getPublicKey()));
    }
}
