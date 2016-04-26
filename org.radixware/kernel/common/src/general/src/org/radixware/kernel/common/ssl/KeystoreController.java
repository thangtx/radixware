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

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.FailedLoginException;
import org.radixware.kernel.common.enums.EKeyStoreType;
import org.radixware.kernel.common.enums.ERadixApplication;
import org.radixware.kernel.common.exceptions.InvalidServerKeystoreSettingsException;
import org.radixware.kernel.common.exceptions.KeystoreControllerException;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.common.utils.SystemTools;

/**
 * Class for working with keystores
 *
 */
public class KeystoreController {

    public final static String KEYSTORE_FILE_TYPE = "JCEKS"; //"JKS"
    private final static int TRIES = 15;
    private KeyStore keystore = null;
    private String keystoreName = "";
    private final EKeyStoreType type;
    private static String serverKeystorePath = null;
    private static EKeyStoreType serverKeystoreType = null;
    private static char[] serverKeystorePassword = null;
    private static KeystoreController serverKeystoreController = null;
    private static long serverKeystoreModificationTime = 0;
    private String pkcs11ProviderName;
    private String bcProviderName;

    public String getKeyStoreName() {
        return keystoreName;
    }

    public KeyStore getKeystore() {
        return keystore;
    }

    public EKeyStoreType getType() {
        return type;
    }

    /**
     * Default constructor, creates an empty file-based keystore
     *
     * @throws KeystoreControllerException
     */
    public KeystoreController() throws KeystoreControllerException {
        try {
            keystore = KeyStore.getInstance(KEYSTORE_FILE_TYPE);
            keystore.load(null, null);
        } catch (GeneralSecurityException e) {
            throw new KeystoreControllerException(e.getMessage(), e);
        } catch (IOException e) {
            throw new KeystoreControllerException(e.getMessage(), e);
        }

        type = EKeyStoreType.FILE;
    }

    /**
     * Constructor for working with a file-based keystore
     *
     * @param keystoreFilePath
     * @param keyStorePassword
     * @throws KeystoreControllerException
     */
    public KeystoreController(final String keystoreFilePath, final char[] keyStorePassword)
            throws KeystoreControllerException {
        try {
            keystore = KeyStore.getInstance(KEYSTORE_FILE_TYPE);

        } catch (KeyStoreException e) {
            throw new KeystoreControllerException(e.getMessage(), e);
        }

        FileInputStream fileInputStream;
        try {
            fileInputStream = new FileInputStream(keystoreFilePath);
        } catch (FileNotFoundException e) {
            fileInputStream = null;
        }
        try {
            keystore.load(fileInputStream, keyStorePassword);
        } catch (GeneralSecurityException e) {
            throw new KeystoreControllerException(e.getMessage(), e);
        } catch (IOException e) {
            throw new KeystoreControllerException(e.getMessage(), e);
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException ex) {
                    Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                }
            }
        }
        keystoreName = keystoreFilePath;
        type = EKeyStoreType.FILE;
    }

    /**
     * Constructor for working with a PKCS#11 keystore
     *
     * @param configurationFilePath
     * @param callbackHandler
     * @throws KeystoreControllerException
     */
    public KeystoreController(final String configurationFilePath, final CallbackHandler callbackHandler)
            throws KeystoreControllerException {
        //Provider provider = new sun.security.pkcs11.SunPKCS11(configurationFilePath); //RADIX-4067
        final Provider provider;
        try {
            final Class c = Class.forName("sun.security.pkcs11.SunPKCS11");
            final Constructor<Provider> constructor = c.getDeclaredConstructor(String.class);
            provider = constructor.newInstance(configurationFilePath);
            pkcs11ProviderName = provider.getName();

        } catch (ClassNotFoundException e) {
            throw new KeystoreControllerException("Can't use PKCS#11 keystores: provider not found (Windows x64?)", e);
        } catch (IllegalAccessException e) {
            throw new KeystoreControllerException("Can't use PKCS#11 keystores: " + e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            throw new KeystoreControllerException("Can't use PKCS#11 keystores: " + e.getMessage(), e);
        } catch (InstantiationException e) {
            throw new KeystoreControllerException("Can't use PKCS#11 keystores: " + e.getMessage(), e);
        } catch (InvocationTargetException e) {
            throw new KeystoreControllerException("Can't use PKCS#11 keystores: " + e.getTargetException().getMessage(), e);
        } catch (NoSuchMethodException e) {
            throw new KeystoreControllerException("Can't use PKCS#11 keystores: " + e.getMessage(), e);
        } catch (SecurityException e) {
            throw new KeystoreControllerException("Can't use PKCS#11 keystores: " + e.getMessage(), e);
        }

        Security.addProvider(provider);
        final KeyStore.Builder builder = KeyStore.Builder.newInstance("PKCS11", provider, new KeyStore.CallbackHandlerProtection(callbackHandler));

        try {
            //have to wait untill the token's driver is loaded
            int i = TRIES;
            while (i-- > 0) {
                try {
                    keystore = builder.getKeyStore();
                    break;
                } catch (KeyStoreException ex) {
                    Throwable cause = ex.getCause();
                    if (cause != null && cause.getCause() != null && (cause.getCause() instanceof FailedLoginException)) {
                        //wrong password
                        cause = cause.getCause();
                        close();
                        throw new KeystoreControllerException("Wrong password", cause);
                    } else {
                        //something other is wrong
                        continue;
                    }
                }
            }
            if (keystore == null) {
                close();
                throw new KeystoreControllerException("Can't instantiate a keystore, because the device driver hasn't been properly loaded");
            }

            keystore.load(null, null);
        } catch (GeneralSecurityException e) {
            close();
            throw new KeystoreControllerException(e.getMessage(), e);
        } catch (IOException e) {
            close();
            throw new KeystoreControllerException(e.getMessage(), e);
        }

        keystoreName = keystore.getProvider().getName();
        type = EKeyStoreType.PKCS11;
    }

    /**
     * Returns a KeystoreController for an Explorer connection
     *
     * @param connectionId GUID of the connection
     * @param type required keystore type
     * @param keystorePassword keystore password
     * @param slotIndex HSM slot index (used only when keystore type is PKCS#11)
     * @return an instance of KeystoreController
     * @throws KeystoreControllerException
     */
    public static KeystoreController newClientInstance(final String workPath,
            final String connectionId,
            final EKeyStoreType type,
            final char[] keystorePassword)
            throws KeystoreControllerException {
        return newInstance(workPath, connectionId + ".jceks", connectionId + ".pkcs11", type, keystorePassword);
    }

    public static KeystoreController newClientInstance(File keystoreFile, final char[] keystorePassword)
            throws KeystoreControllerException {
        final String workPath = SystemTools.getRadixApplicationDataPath(ERadixApplication.EXPLORER).getAbsolutePath();

        return newInstance(keystoreFile, keystorePassword);
    }

    /**
     * Instantiates a PKCS11 keystore controller by temporary configuration
     * file. Can be used to create a temp keystore for reading aliases, testing
     * configuration, etc.
     *
     * @param tempConfig temp configuration file
     * @param type
     * @param keystorePassword
     * @return temp keystore controller
     * @throws KeystoreControllerException
     */
    public static KeystoreController newTempInstance(final String tempConfig, final char[] keystorePassword) throws KeystoreControllerException {
        // have to cut leading slash because of `newInstance` implementation
        final String cutSlashConfigPath = (tempConfig.charAt(0) == '/') ? tempConfig.substring(1, tempConfig.length()) : tempConfig;
        return newInstance("", "", cutSlashConfigPath, EKeyStoreType.PKCS11, keystorePassword);
    }

    /**
     * Returns a KeystoreController for Server
     *
     * @param filePath keystore (or PKCS#11 keystore configuration) file path,
     * absolute or relative to application data path
     * @param type required keystore type
     * @param keystorePassword keystore password
     * @return an instance of KeystoreController
     * @throws KeystoreControllerException
     */
    public static KeystoreController newServerInstance(final String filePath, final EKeyStoreType type, final char[] keystorePassword)
            throws KeystoreControllerException {
        final File file = getServerKeystoreFile(filePath);
        if (file == null) {
            throw new KeystoreControllerException("Server keystore file path is not specified");
        }
        return newInstance(file.getParent(), file.getName(), file.getName(), type, keystorePassword);
    }

    private static KeystoreController newInstance(final String workPath, final String keystoreFileName, final String configFileName, final EKeyStoreType type, final char[] keystorePassword)
            throws KeystoreControllerException {
        final KeystoreController keystoreController;
        try {
            switch (type) {
                case FILE:
                    final String keyStoreFilePath = workPath + "/" + keystoreFileName;
                    keystoreController = new KeystoreController(keyStoreFilePath, keystorePassword);
                    break;
                case PKCS11:
                    final String configFilePath = workPath + "/" + configFileName;
                    inspectConfiguration(configFilePath);
                    keystoreController = new KeystoreController(configFilePath, new ConstantPasswordCallbackHandler(keystorePassword));
                    break;
                default:
                    throw new KeystoreControllerException("Invalid keystore type: " + (type == null ? "null" : type.getValue()));
            }
        } catch (IOException e) {
            throw new KeystoreControllerException(e.getMessage(), e);
        } catch (IllegalStateException e) {
            throw new KeystoreControllerException(e.getMessage(), e);
        }
        return keystoreController;
    }

    private static KeystoreController newInstance(final File keystoreFile, final char[] keystorePassword)
            throws KeystoreControllerException {
        return new KeystoreController(keystoreFile.getAbsolutePath(), keystorePassword);
    }

    /**
     * Saves a file-based keystore
     */
    public void saveKeyStoreToFile(final String keystoreFilePath, final char[] keyStorePassword)
            throws KeystoreControllerException {
        if (type != EKeyStoreType.FILE) {
            throw new KeystoreControllerException("saveKeyStoreToFile() should only be called for a file-based keystore");
        }

        final File file = new File(keystoreFilePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new KeystoreControllerException(e.getMessage(), e);
            }
        }

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(keystoreFilePath);
            keystore.store(fileOutputStream, keyStorePassword);
        } catch (GeneralSecurityException e) {
            throw new KeystoreControllerException(e.getMessage(), e);
        } catch (IOException e) {
            throw new KeystoreControllerException(e.getMessage(), e);
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException ex) {
                    Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                }
            }
        }

        if (keystoreName.isEmpty()) {
            keystoreName = keystoreFilePath;
        }
    }

    /**
     * Saves a PKCS#11 keystore
     */
    public void savePkcs11KeyStore(final char[] password)
            throws KeystoreControllerException {
        if (type != EKeyStoreType.PKCS11) {
            throw new KeystoreControllerException("savePkcs11KeyStore() should only be called for a PKCS#11 keystore");
        }
        try {
            keystore.store(null, password);
        } catch (GeneralSecurityException e) {
            throw new KeystoreControllerException(e.getMessage(), e);
        } catch (IOException e) {
            throw new KeystoreControllerException(e.getMessage(), e);
        }
    }

    public static Key getServerKeyStoreKey(final String alias) throws KeystoreControllerException {
        return getServerKeystoreController().getKey(alias, serverKeystorePassword);
    }

    public static X509Certificate getServerKeyStoreCertificate(final String alias) throws KeystoreControllerException {
        return getServerKeystoreController().getCertificate(alias);
    }

    /**
     * Returns a list of keystore's entries
     */
    public Iterable<KeystoreEntry> getKeyStoreEntries() throws KeystoreControllerException {
        final ArrayList<KeystoreEntry> list = new ArrayList<KeystoreEntry>();
        final Enumeration<String> aliases;
        try {
            aliases = keystore.aliases();
        } catch (KeyStoreException e) {
            throw new KeystoreControllerException(e.getMessage(), e);
        }
        for (; aliases.hasMoreElements();) {
            final String alias = aliases.nextElement();
            try {
                if (keystore.isKeyEntry(alias)) {
                    if (keystore.getCertificateChain(alias) != null) {
                        list.add(new KeystoreRsaKeyEntry(alias, keystore.getCertificateChain(alias)));
                    } else {
                        list.add(new KeystoreDesKeyEntry(alias));
                    }
                } else if (keystore.isCertificateEntry(alias)) {
                    list.add(new KeystoreTrustedCertificateEntry(alias, keystore.getCertificate(alias)));
                }
            } catch (KeyStoreException e) {
                throw new KeystoreControllerException(e.getMessage(), e);
            }
        }

        return list;
    }

    /**
     * Returns a RSA key entry for the specified alias
     */
    public KeystoreRsaKeyEntry getKeystoreRsaKeyEntry(final String alias, final char[] keyPassword)
            throws KeystoreControllerException {
        final PrivateKey privateKey;
        final Certificate[] chain;
        try {
            privateKey = (PrivateKey) keystore.getKey(alias, keyPassword);
            chain = keystore.getCertificateChain(alias);
        } catch (GeneralSecurityException e) {
            throw new KeystoreControllerException(e.getMessage(), e);
        }

        if (privateKey == null) {
            throw new KeystoreControllerException("Key is not present for alias '" + alias + "'");
        }
        if (chain == null || chain.length == 0) {
            throw new KeystoreControllerException("Certificate chain is empty for alias '" + alias + "'");
        }

        return new KeystoreRsaKeyEntry(alias, chain, privateKey);
    }

    /**
     * Returns a DES key entry for the specified alias
     */
    public KeystoreDesKeyEntry getKeystoreDesKeyEntry(final String alias, final char[] keyPassword)
            throws KeystoreControllerException {
        final Key key;
        try {
            key = keystore.getKey(alias, keyPassword);
        } catch (GeneralSecurityException e) {
            throw new KeystoreControllerException(e.getMessage(), e);
        }

        if (key == null) {
            throw new KeystoreControllerException("Key is not present for alias '" + alias + "'");
        }

        return new KeystoreDesKeyEntry(alias, key);
    }

    /**
     * Returns an array of all aliases in keystore
     */
    public String[] getAliases() throws KeystoreControllerException {
        return getAliases(true, true, true);
    }

    /**
     * Returns an array of RSA key aliases in keystore
     */
    public String[] getRsaKeyAliases() throws KeystoreControllerException {
        return getAliases(true, false, false);
    }

    /**
     * Returns an array of trusted certificate aliases in keystore
     */
    public String[] getTrustedCertificateAliases() throws KeystoreControllerException {
        return getAliases(false, true, false);
    }

    /**
     * Returns an array of DES key aliases in keystore
     */
    public String[] getDesKeyAliases() throws KeystoreControllerException {
        return getAliases(false, false, true);
    }

    private String[] getAliases(final boolean includeRsaKeys, final boolean includeTrustedCertificates, final boolean includeDesKeys)
            throws KeystoreControllerException {
        final ArrayList<String> aliasList = new ArrayList<String>();
        final Enumeration<String> aliasEnumeration;
        try {
            aliasEnumeration = keystore.aliases();
        } catch (KeyStoreException e) {
            throw new KeystoreControllerException(e.getMessage(), e);
        }

        while (aliasEnumeration.hasMoreElements()) {
            final String alias = aliasEnumeration.nextElement();
            try {
                if (keystore.isKeyEntry(alias)) {
                    if (keystore.getCertificate(alias) != null) {
                        if (!includeRsaKeys) {
                            continue;
                        }
                    } else {
                        if (!includeDesKeys) {
                            continue;
                        }
                    }
                }
                if (keystore.isCertificateEntry(alias) && !includeTrustedCertificates) {
                    continue;
                }
            } catch (KeyStoreException e) {
                throw new KeystoreControllerException(e.getMessage(), e);
            }
            aliasList.add(alias);
        }

        return aliasList.toArray(new String[0]); //stub parameter specifies the type of array elements
    }

    /**
     * Stores an RSA key and it's certificate chain in keystore
     */
    public void storeRsaKey(final KeystoreRsaKeyEntry entry, final String alias, final char[] keyPassword)
            throws KeystoreControllerException {
        try {
            keystore.setKeyEntry(alias, entry.getKey(), keyPassword, entry.getCertificateChain());
        } catch (KeyStoreException e) {
            throw new KeystoreControllerException(e.getMessage(), e);
        }
    }

    /**
     * Exports an RSA key with the specified alias into a PKCS#12 keystore
     */
    public void exportRsaKeyToPkcs12(final String alias, final char[] keyPassword, final String outerKeyStoreFilePath, final char[] outerKeyStorePassword)
            throws KeystoreControllerException {
        final KeystoreRsaKeyEntry entry;
        try {
            entry = getKeystoreRsaKeyEntry(alias, keyPassword);
        } catch (KeystoreControllerException e) {
            throw new KeystoreControllerException(e.getMessage(), e);
        }

        if (entry.getKey() == null) {
            throw new KeystoreControllerException("Key is not present for alias '" + alias + "'");
        }

        writeRsaKeyToPkcs12(entry, outerKeyStoreFilePath, outerKeyStorePassword);
    }

    /**
     * Writes an RSA key and it's certificate chain into a PKCS#12 keystore
     */
    public static void writeRsaKeyToPkcs12(final KeystoreRsaKeyEntry entry, final String outerKeyStoreFilePath, final char[] outerKeyStorePassword)
            throws KeystoreControllerException {
        //always overwrite contents of PKCS#12 keystore (single keypair should be inside)
        FileInputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {
            final KeyStore outerKeyStore = KeyStore.getInstance("PKCS12");
            final char[] outerKeyPassword = outerKeyStorePassword; //different store and key passwords are not supported for PKCS#12 KeyStores
            outerKeyStore.load(inputStream, outerKeyStorePassword);
            final String outerAlias = "dummy_alias";
            outerKeyStore.setKeyEntry(outerAlias, (PrivateKey) entry.getKey(), outerKeyPassword, entry.getCertificateChain());
            outputStream = new FileOutputStream(outerKeyStoreFilePath);
            outerKeyStore.store(outputStream, outerKeyStorePassword);
        } catch (GeneralSecurityException e) {
            throw new KeystoreControllerException(e.getMessage(), e);
        } catch (IOException e) {
            throw new KeystoreControllerException(e.getMessage(), e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ex) {
                    Logger.getLogger(KeystoreController.class.getName()).log(Level.FINE, ex.getMessage(), ex);
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException ex) {
                    Logger.getLogger(KeystoreController.class.getName()).log(Level.FINE, ex.getMessage(), ex);
                }
            }
        }
    }

    /**
     * Reads an RSA key and it's certificate chain from a PKCS#12 keystore
     */
    public static KeystoreRsaKeyEntry readRsaKeyFromPkcs12(final String outerKeyStoreFilePath, final char[] outerKeyStorePassword)
            throws KeystoreControllerException {
        final KeyStore outerKeyStore;
        final char[] outerKeyPassword = outerKeyStorePassword; //different store and key passwords are not supported for PKCS#12 KeyStores
        PrivateKey privateKey = null;
        Certificate[] chain = null;

        FileInputStream inputStream;
        try {
            inputStream = new FileInputStream(outerKeyStoreFilePath);
        } catch (FileNotFoundException e) {
            inputStream = null;
        }
        try {
            outerKeyStore = KeyStore.getInstance("PKCS12");
            outerKeyStore.load(inputStream, outerKeyStorePassword);
            final Enumeration<String> aliasEnumeration = outerKeyStore.aliases();
            boolean keyFound = false;
            while (aliasEnumeration.hasMoreElements()) {
                final String outerAlias = aliasEnumeration.nextElement();
                if (outerKeyStore.isKeyEntry(outerAlias)) {
                    privateKey = (PrivateKey) outerKeyStore.getKey(outerAlias, outerKeyPassword);
                    chain = outerKeyStore.getCertificateChain(outerAlias);
                    keyFound = true;
                }
            }
            if (!keyFound) {
                throw new KeystoreControllerException("PKCS#12 keystore doesn't contain key entries");
            }
        } catch (GeneralSecurityException e) {
            throw new KeystoreControllerException(e.getMessage(), e);
        } catch (IOException e) {
            throw new KeystoreControllerException(e.getMessage(), e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ex) {
                    Logger.getLogger(KeystoreController.class.getName()).log(Level.FINE, ex.getMessage(), ex);
                }
            }
        }

        return new KeystoreRsaKeyEntry("", chain, privateKey);
    }

    /**
     * Stores a DES key in keystore
     */
    public void storeDesKey(final KeystoreDesKeyEntry entry, final String alias, final char[] keyPassword)
            throws KeystoreControllerException {
        try {
            keystore.setKeyEntry(alias, entry.getKey(), keyPassword, null);
        } catch (KeyStoreException e) {
            throw new KeystoreControllerException(e.getMessage(), e);
        }
    }

    /**
     * Retrieves a DES key from keystore
     */
    public Key getKey(final String alias, final char[] keyPassword)
            throws KeystoreControllerException {
        try {
            return keystore.getKey(alias, keyPassword);
        } catch (KeyStoreException e) {
            throw new KeystoreControllerException(e.getMessage(), e);
        } catch (NoSuchAlgorithmException e) {
            throw new KeystoreControllerException(e.getMessage(), e);
        } catch (UnrecoverableKeyException e) {
            throw new KeystoreControllerException(e.getMessage(), e);
        }
    }

    /**
     * Stores a certificate chain for the specified alias
     */
    public void storeCertificateChain(final X509Certificate[] chain, final String alias, final char[] keyPassword)
            throws KeystoreControllerException {
        if (chain == null || chain.length == 0) {
            throw new KeystoreControllerException("Received cerificate chain is empty");
        }

        final PrivateKey privateKey;
        final X509Certificate currentCertificate;
        try {
            privateKey = (PrivateKey) keystore.getKey(alias, keyPassword);
            currentCertificate = (X509Certificate) keystore.getCertificate(alias);
        } catch (GeneralSecurityException e) {
            throw new KeystoreControllerException(e.getMessage(), e);
        }

        if (privateKey == null) {
            throw new KeystoreControllerException("Key is not present for alias '" + alias + "'");
        }
        if (chain == null || chain.length == 0) {
            throw new KeystoreControllerException("Cerificate chain is empty for alias '" + alias + "'");
        }

        if (!chain[0].getPublicKey().equals(currentCertificate.getPublicKey())) {
            throw new KeystoreControllerException("Certificate doesn't correspond to the keypair's public key");
        }

        storeRsaKey(new KeystoreRsaKeyEntry(alias, chain, privateKey), alias, keyPassword);
    }

    /**
     * Stores a trusted certificate in the specified alias
     */
    public void storeTrustedCertificate(final String alias, final X509Certificate certificate)
            throws KeystoreControllerException {
        try {
            keystore.setCertificateEntry(alias, certificate);
        } catch (KeyStoreException e) {
            throw new KeystoreControllerException(e.getMessage(), e);
        }
    }

    /**
     * Removes an entry with the specified alias
     */
    public void deleteEntry(final String alias) throws KeystoreControllerException {
        try {
            keystore.deleteEntry(alias);
        } catch (KeyStoreException e) {
            throw new KeystoreControllerException(e.getMessage(), e);
        }
    }

    /**
     * Checks whether the specified alias exists and contains an RSA key entry
     */
    public boolean containsRsaKey(final String alias) throws KeystoreControllerException {
        try {
            return keystore.containsAlias(alias) && keystore.isKeyEntry(alias) && keystore.getCertificateChain(alias) != null;
        } catch (KeyStoreException e) {
            throw new KeystoreControllerException(e.getMessage(), e);
        }
    }

    /**
     * Returns a certificate with the specified alias
     */
    public X509Certificate getCertificate(final String alias) throws KeystoreControllerException {
        try {
            return (X509Certificate) keystore.getCertificate(alias);
        } catch (KeyStoreException e) {
            throw new KeystoreControllerException(e.getMessage(), e);
        }
    }

    /**
     * Initializes a TrustManagerFactory with the underlying keystore
     *
     * @param factory TrustManagerFactory to be initialized
     * @throws KeystoreControllerException
     */
    public void initTrustManagerFactory(final TrustManagerFactory factory) throws KeystoreControllerException {
        try {
            factory.init(keystore);
        } catch (KeyStoreException e) {
            throw new KeystoreControllerException(e.getMessage(), e);
        }
    }

    /**
     * Initializes a KeyManagerFactory with the underlying keystore
     *
     * @param factory KeyManagerFactory to be initialized
     * @param keyPassword password for accessing a (single) private key in the
     * keystore
     * @throws KeystoreControllerException
     */
    public void initKeyManagerFactory(final KeyManagerFactory factory, final char[] keyPassword)
            throws KeystoreControllerException {
        try {
            factory.init(keystore, keyPassword);
        } catch (GeneralSecurityException e) {
            throw new KeystoreControllerException(e.getMessage(), e);
        }
    }

    /**
     * Initializes a TrustManagerFactory with the Server's keystore
     *
     * @param factory TrustManagerFactory to be initialized
     * @throws KeystoreControllerException
     */
    public static void initServerTrustManagerFactory(final TrustManagerFactory factory)
            throws KeystoreControllerException {
        checkServerKeystoreModified();
        getServerKeystoreController().initTrustManagerFactory(factory);
    }

    /**
     * Initializes a KeyManagerFactory with the Server's keystore
     *
     * @param factory KeyManagerFactory to be initialized
     * @throws KeystoreControllerException
     */
    public static void initServerKeyManagerFactory(final KeyManagerFactory factory)
            throws KeystoreControllerException {
        checkServerKeystoreModified();
        getServerKeystoreController().initKeyManagerFactory(factory, serverKeystorePassword);
    }

    /*
     * Returns the name of the provider used for keystore instantiation. If
     * keystore type is other than PKCS#11, returns null.
     */
    public String getSecurityProviderName() {
        return (type == EKeyStoreType.PKCS11 ? keystore.getProvider().getName() : null);
    }

    /**
     * Sets password for accessing server's keystore. Must be called before
     * creating SSLContext for any server class.
     *
     * @param password keystore password
     */
    public static void setServerKeystorePassword(final char[] password) {
        serverKeystorePassword = Arrays.copyOf(password, password.length);
        rereadServerKeystoreController();
    }

    /**
     * Sets server's keystore type. Must be called before creating SSLContext
     * for any server class.
     *
     * @param keystoreType keystore type
     */
    public static void setServerKeystoreType(final EKeyStoreType keystoreType) {
        serverKeystoreType = keystoreType;
        rereadServerKeystoreController();
    }

    /**
     * Sets server's keystore (or PKCS#11 keystore configuration file) location.
     *
     * @param keystorePath path to keystore
     */
    public static void setServerKeystorePath(final String keystorePath) {
        serverKeystorePath = keystorePath;
        rereadServerKeystoreController();
        refreshServerKeystoreModificationTime();
    }

    private static boolean refreshServerKeystoreModificationTime() {
        File keystoreFile = getServerKeystoreFile(serverKeystorePath);
        long actualServerKeystoreModificationTime = (keystoreFile == null || !keystoreFile.exists() ? 0 : keystoreFile.lastModified());
        if (serverKeystoreModificationTime < actualServerKeystoreModificationTime) {
            serverKeystoreModificationTime = actualServerKeystoreModificationTime;
            return true;
        }
        return false;
    }

    /**
     * Requires server's keystoreController to be reloaded during the next call
     * of getServerKeystoreController()
     */
    //synchronized in order to avoid races with getServerKeystoreController()
    private synchronized static void rereadServerKeystoreController() {
        serverKeystoreController = null;
    }

    private static void checkServerKeystoreControllerSettings() throws KeystoreControllerException {
        if (serverKeystorePath == null) {
            throw new InvalidServerKeystoreSettingsException("Can't open server's keystore: keystore path was not specified");
        }
        if (serverKeystoreType == null) {
            throw new InvalidServerKeystoreSettingsException("Can't open server's keystore: keystore type was not specified");
        }
        if (serverKeystorePassword == null) {
            throw new InvalidServerKeystoreSettingsException("Can't open server's keystore: keystore password was not specified");
        }
    }

    /**
     * Returns server instance's keystore controller
     *
     * @throws KeystoreControllerException
     */
    //synchronized in order to avoid races with rereadServerKeystoreController()
    public synchronized static KeystoreController getServerKeystoreController()
            throws KeystoreControllerException {
        if (serverKeystoreController == null) {
            checkServerKeystoreControllerSettings();
            KeystoreController controller = newServerInstance(serverKeystorePath, serverKeystoreType, serverKeystorePassword);
            refreshServerKeystoreModificationTime();
            serverKeystoreController = controller;
        }
        return serverKeystoreController;
    }

    public Provider getProvider() {
        return keystore.getProvider();
    }

    /**
     * RADIX-4380 Checks server instance's keystore availability. Check is
     * skipped if keystore path isn't specified or keystore file doesn't exist.
     *
     * @param type
     * @param path
     * @throws KeystoreControllerException when file or password are invalid
     */
    public static void checkServerKeystoreAvailability(final EKeyStoreType type, final String path) throws KeystoreControllerException {
        if (path == null || path.length() == 0) //skip check if path isn't specified in instance settings
        {
            return;
        }
        if (!getServerKeystoreFile(path).exists()) //skip check if keystore file doesn't exist
        {
            return;
        }

        newServerInstance(path, type, serverKeystorePassword); //try to load keystore
    }

    /**
     * Turns the given path of server keystore into absolute path
     *
     * @param path keystore path (relative to application data path or already
     * absolute)
     * @return absolute form of the given path
     */
    public static String getServerKeystoreAbsolutePath(final String path) {
        if (path == null) {
            return null;
        }
        return getServerKeystoreFile(path).getAbsolutePath();
    }

    private static File getServerKeystoreFile(final String path) {
        if (path == null) {
            return null;
        }
        final File file = new File(path);
        return (file.isAbsolute() ? file : SystemTools.getApplicationDataPath(path));
    }

    /**
     * Returns server keystore's last modification time. If server's
     * keystoreController hasn't been created yet, returns 0.
     *
     * @return
     */
    public static long getServerKeystoreModificationTime() {
        return serverKeystoreModificationTime;
    }

    /**
     * Checks whether server keystore's file was modified after the server's
     * keystoreController most recent creation. If true, server's
     * keystoreController will be recreated the next time it is requested.
     *
     * @return true if server keystore was modified
     */
    public static boolean checkServerKeystoreModified() {
        if (serverKeystoreType == EKeyStoreType.FILE) {
            if (refreshServerKeystoreModificationTime()) {
                rereadServerKeystoreController();
                return true;
            }
        }
        return false;
    }

    /**
     * Returns aliases of keys in server's keystore
     *
     * @return key aliases array
     * @throws KeystoreControllerException
     */
    public static String[] getServerKeystoreKeyAliases() throws KeystoreControllerException {
        return getServerKeystoreController().getRsaKeyAliases();
    }

    /**
     * Returns aliases of trusted certificates in server's keystore
     *
     * @return trusted certificate aliases array
     * @throws KeystoreControllerException
     */
    public static String[] getServerKeystoreTrustedCertAliases()
            throws KeystoreControllerException {
        return getServerKeystoreController().getTrustedCertificateAliases();
    }

    /**
     * Checks whether the exception was caused by an incorrect password
     *
     * @param ex
     * @return
     */
    public static boolean isIncorrectPasswordException(final Exception ex) {
        for (Throwable e = ex; e != null; e = e.getCause()) {
            if (e instanceof UnrecoverableKeyException || e instanceof FailedLoginException) {
                return true;
            }
        }
        return false;
    }

    public final void close() throws KeystoreControllerException {
        AuthProvider pkcs11Provider = null;
        try {
            pkcs11Provider = (AuthProvider) Security.getProvider(pkcs11ProviderName);
            if (pkcs11Provider != null) {
                pkcs11Provider.logout();
            }
        } catch (Exception ex) {
            throw new KeystoreControllerException("Error occured on connection close", ex);
        } finally {
            Security.removeProvider(pkcs11ProviderName);
        }
    }

    public static class ConstantPasswordCallbackHandler implements CallbackHandler {

        private final char[] password;

        public ConstantPasswordCallbackHandler(final char[] password) {
            this.password = password;
        }

        @Override
        public void handle(final Callback[] callbacks) throws IOException, UnsupportedCallbackException {
            for (int i = 0; i < callbacks.length; i++) {
                if (callbacks[i] instanceof javax.security.auth.callback.PasswordCallback) {
                    //prompt the user for sensitive information
                    final PasswordCallback passwordCallback = (PasswordCallback) callbacks[i];
                    passwordCallback.setPassword(password);
                } else {
                    throw new UnsupportedCallbackException(callbacks[i], "Unrecognized Callback");
                }
            }
        }
    }

    /**
     * Inspect for configuration file presence. If there's no configuration file
     * on the disk, then a default configuration is created.
     *
     * @param configFilePath path to the configuration file
     * @throws IllegalStateException is thrown when there's platform
     * incompatibility issue
     * @throws IOException is thrown in case of IO error
     */
    private static void inspectConfiguration(final String configFilePath) throws IllegalStateException, IOException {
        final File configFile = new File(configFilePath);
        if (!configFile.exists()) {
            configFile.createNewFile();
            final String libraryPath;
            if (SystemTools.isWindows) {
                if (System.getProperty("os.arch").equalsIgnoreCase("amd64")) {
                    throw new IllegalStateException("PKCS#11 keystore is currently not supported for Windows x64");
                }
                libraryPath = System.getenv("windir") + "/system32/eTPKCS11.dll";
            } else if (SystemTools.isLinux) {
                libraryPath = "/usr/lib/opensc-pkcs11.so";
            } else {
                throw new IllegalStateException("PKCS#11 keystore is currently not supported for this platform");
            }
            final StringBuilder configFileContents = new StringBuilder();
            configFileContents.append("name = HSM");
            configFileContents.append(System.lineSeparator());
            configFileContents.append("slot = 0");
            configFileContents.append(System.lineSeparator());
            configFileContents.append("library = ");
            configFileContents.append(libraryPath);
            FileOutputStream outputStream = null;
            try {
                outputStream = new FileOutputStream(configFilePath);
                outputStream.write(configFileContents.toString().getBytes(FileUtils.XML_ENCODING));
                outputStream.flush();
            } finally {
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException ex) {
                        Logger.getLogger(KeystoreController.class.getName()).log(Level.FINE, ex.getMessage(), ex);
                    }
                }
            }
        }
    }
}
