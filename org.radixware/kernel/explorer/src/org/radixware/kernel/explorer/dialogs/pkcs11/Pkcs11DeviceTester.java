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

package org.radixware.kernel.explorer.dialogs.pkcs11;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import javax.crypto.Cipher;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.X509KeyManager;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.eas.connections.Pkcs11Config;
import org.radixware.kernel.common.client.exceptions.FileException;
import org.radixware.kernel.common.client.exceptions.Pkcs11Exception;
import org.radixware.kernel.common.client.localization.MessageProvider;


public class Pkcs11DeviceTester implements Callable<Void> {
    
    public class Pkcs11DeviceTestException extends Exception {
        private static final long serialVersionUID = -8438841549848478822L;
        public Pkcs11DeviceTestException(final String message) {
            super(message);
        }
        
        public Pkcs11DeviceTestException(final String message, final Throwable cause) {
            super(message, cause);
        }
    }
    
    public class Pkcs11NoSignatureSupport extends Exception {
        private static final long serialVersionUID = 3880205945976682446L;
        public Pkcs11NoSignatureSupport(final String message) {
            super(message);
        }
        public Pkcs11NoSignatureSupport(final String message, final Throwable cause) {
            super(message, cause);
        }
    }
       
    public class Pkcs11NoRsaSupport extends Exception {
        private static final long serialVersionUID = 9047411171486406450L;
        public Pkcs11NoRsaSupport(final String message) {
            super(message);
        }
    }
    
    public class Pkcs11InitializationException extends Exception {
        private static final long serialVersionUID = -7224839182661680057L;
        public Pkcs11InitializationException(final String message) {
            super(message);
        }

        private Pkcs11InitializationException(final String message, final Throwable cause) {
            super(message, cause);
        }
    }
    
    private static final String KEY_MANAGER = "SunX509";
    private static final String MESSAGE = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String CIPHER = "Cipher";
    private static final String SIGNATURE = "Signature";
    
    private PrivateKey privateKey;
    private PublicKey publicKey;    
    
    private final KeyStore.PasswordProtection pwd;
    private final String alias;
    private final boolean autoDetectSlotIndex;
    private final IClientEnvironment environment;
    private final Pkcs11Config pkcs11Config;
    private final String configFile;
        
    @SuppressWarnings("PMD.ArrayIsStoredDirectly")
    public Pkcs11DeviceTester(final IClientEnvironment environment, 
                              final Pkcs11Config config, 
                              final String configFilePath,
                              final String keyAlias, 
                              final char[] password,
                              final boolean autoDetectSlotIndex
                              ){
        this.environment = environment;
        pkcs11Config = config;
        configFile = configFilePath;
        this.autoDetectSlotIndex = autoDetectSlotIndex;
        this.alias = keyAlias;        
        pwd = new KeyStore.PasswordProtection(password);        
    }
    
    private Provider createProvider() throws Pkcs11DeviceTestException, Pkcs11Exception{
        try{
            pkcs11Config.writeToFile(configFile);
        }catch(IOException exception){
            final FileException fileException = 
                new FileException(environment, FileException.EExceptionCode.CANT_WRITE, configFile, exception);
            throw new Pkcs11DeviceTestException(fileException.getMessage());//NOPMD
        }
        try{
            final Class<?> c = Class.forName("sun.security.pkcs11.SunPKCS11");
            final Constructor constructor = c.getDeclaredConstructor(String.class);
            return (Provider)constructor.newInstance(configFile);
        } catch (ClassNotFoundException e){
            throw new Pkcs11DeviceTestException("Can't use PKCS#11 keystores: provider not found (Windows x64?)", e);
        } catch (IllegalAccessException e){
            throw new Pkcs11DeviceTestException("Can't use PKCS#11 keystores: "+e.getMessage(), e);
        } catch (IllegalArgumentException e){
            throw new Pkcs11DeviceTestException("Can't use PKCS#11 keystores: "+e.getMessage(), e);
        } catch (InstantiationException e){
            throw new Pkcs11DeviceTestException("Can't use PKCS#11 keystores: "+e.getMessage(), e);
        } catch (InvocationTargetException e){
            throw new Pkcs11Exception(e.getCause());//NOPMD
        } catch (NoSuchMethodException e){
            throw new Pkcs11DeviceTestException("Can't use PKCS#11 keystores: "+e.getMessage(), e);
        } catch (SecurityException e){
            throw new Pkcs11DeviceTestException("Can't use PKCS#11 keystores: "+e.getMessage(), e);
        }        
    }
    
    private Provider prepareProvider() throws Pkcs11DeviceTestException, Pkcs11Exception, KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException, Pkcs11ConfigurationException{
        final Provider provider = createProvider();        
        final KeyStore.Builder ksBuilder = KeyStore.Builder.newInstance("pkcs11", provider, new KeyStore.CallbackHandlerProtection(new CallbackHandler() {
            @Override
            public void handle(final Callback[] callbacks) throws IOException, UnsupportedCallbackException {
                for (int i = 0; i<callbacks.length; i++){
                    if (callbacks[i] instanceof javax.security.auth.callback.PasswordCallback){
                        //prompt the user for sensitive information
                        final PasswordCallback passwordCallback = (PasswordCallback)callbacks[i];
                        passwordCallback.setPassword(pwd.getPassword());
                    } else{
                        throw new UnsupportedCallbackException(callbacks[i], "Unrecognized Callback");
                    }
                }
            }
        }));
        final KeyStore keystore = ksBuilder.getKeyStore();
        keystore.load(null, null);
        try{
            return initKeys(keystore, pwd.getPassword(), alias) ? provider : null;
        }catch(Pkcs11ConfigurationException exception){
            if (provider!=null){
                try {
                    ((AuthProvider)provider).logout();
                } catch (Exception ex) {
                    environment.getTracer().warning(ex.getLocalizedMessage());
                }
            }
            return null;
        }
    }
    
    private Provider detectSlotIndex() throws Exception{        
        Provider provider;
        Exception exceptionOnCreateProvider = null;
        try{
            provider = prepareProvider();
        }catch(Pkcs11DeviceTestException exception){
            throw exception;//NOPMD
        }catch(Exception exception){
            exceptionOnCreateProvider = exception;
            provider = null;
        }
        final int initialSlotIndex = Integer.parseInt(pkcs11Config.getFieldValue(Pkcs11Config.Field.SLOTLI));
        if (provider==null){
            for (int i=0; i<100; i++){
                if (i!=initialSlotIndex){
                    pkcs11Config.setFieldValue(Pkcs11Config.Field.SLOTLI, String.valueOf(i));
                    try{
                        provider = prepareProvider();
                    }catch(Pkcs11DeviceTestException exception){
                        throw exception;//NOPMD
                    }catch(Exception exception){
                        continue;
                    }
                    if (provider!=null){
                        break;
                    }
                }
            }
        }
        if (provider==null && exceptionOnCreateProvider!=null){
            throw exceptionOnCreateProvider;
        }
        return provider;
    }
    
    @Override
    @SuppressWarnings("UseSpecificCatch")
    public Void call() throws ExecutionException {
        final Provider provider;
        try{
            provider = autoDetectSlotIndex ? detectSlotIndex() : prepareProvider();
        }catch(Exception exception){
            throw new ExecutionException(exception);
        }
        if (provider==null){
            final String message = 
                environment.getMessageProvider().translate("PKCS11", "No certificate with the given alias is found");
            throw new ExecutionException(message,null);
        }        
        
        final List<String> cipherAlgorithms = new LinkedList<>();
        final List<String> signAlgorithms = new LinkedList<>();
        for(Provider.Service s : provider.getServices()) {
            //We are not interested in a secret-key cryptography to establish TLS connections
            if(s.getType().equals(CIPHER) && s.getAlgorithm().contains("RSA")) {
                cipherAlgorithms.add(s.getAlgorithm());
            } else if(s.getType().equals(SIGNATURE)) {
                signAlgorithms.add(s.getAlgorithm());
            } else { continue; }
        }        
        // Signature test
        try {
            runSignTests(provider, signAlgorithms);
        } catch (Pkcs11NoSignatureSupport ex) {
            throw new ExecutionException(ex);
        }
        // Cipher test
        try {
            runCipherTests(provider, cipherAlgorithms);
        } catch (Pkcs11NoRsaSupport ex) {
            throw new ExecutionException(ex);
        }
        try {
            pwd.destroy();
            ((AuthProvider)provider).logout();
        } catch (Exception ex) {
            environment.getTracer().warning(ex.getLocalizedMessage());
        }
        return null;
    }
    
    private boolean initKeys(final KeyStore keystore, final char[] password, final String alias) throws Pkcs11ConfigurationException {
        final MessageProvider messageProvider = environment.getMessageProvider();
        try {
            final KeyManagerFactory kmf = KeyManagerFactory.getInstance(KEY_MANAGER);
            kmf.init(keystore, password);
            final X509KeyManager keyManager = (X509KeyManager)kmf.getKeyManagers()[0];
            final X509Certificate[] certificates = keyManager.getCertificateChain(alias);
            if(certificates == null) {
                return false;
            }
            publicKey = certificates[0].getPublicKey();
            privateKey = keyManager.getPrivateKey(alias);
            return true;
        } catch (NoSuchAlgorithmException | KeyStoreException ex) {
            final String message = messageProvider.translate("PKCS11", "Keystore initialization error");
            throw new Pkcs11ConfigurationException(message, ex);
        } catch (UnrecoverableKeyException ex) {
            final String message = messageProvider.translate("PKCS11", "Can't recover the key (probably password is wrong)");
            throw new Pkcs11ConfigurationException(message, ex);
        }
    }
    
    private void signTest(final Provider provider, final PrivateKey privateKey, final PublicKey publicKey, final String algorithm) throws Pkcs11DeviceTestException, NoSuchAlgorithmException {
        final MessageProvider messageProvider = environment.getMessageProvider();
        try {
            final Signature signature = Signature.getInstance(algorithm, provider);
            signature.initSign(privateKey);
            signature.update(MESSAGE.getBytes());
            final byte[] signedData = signature.sign();
            
            signature.initVerify(publicKey);
            signature.update(MESSAGE.getBytes());
            final boolean success = signature.verify(signedData);
                        
            if(!success) {
                throw new Pkcs11DeviceTestException("Signature test failed");
            }
        } catch (InvalidKeyException ex) {
            final String message = messageProvider.translate("PKCS11", "Invalid key.");
            throw new Pkcs11DeviceTestException(message, ex);
        } catch (SignatureException ex) {
            final String message = messageProvider.translate("PKCS11", "Signature verification failed.");
            throw new Pkcs11DeviceTestException(message, ex);
        }
    }
    
    @SuppressWarnings("UseSpecificCatch")
    private void cipherTest(final Provider provider, final PrivateKey privateKey, final PublicKey publicKey, final String algorithm) throws Pkcs11DeviceTestException, NoSuchAlgorithmException {
        try {
            final Cipher encipher = Cipher.getInstance(algorithm, provider);
            encipher.init(Cipher.ENCRYPT_MODE, publicKey);
            encipher.update(MESSAGE.getBytes());
            final byte[] encrypted = encipher.doFinal();
            
            final Cipher decipher = Cipher.getInstance(algorithm, provider);
            decipher.init(Cipher.DECRYPT_MODE, privateKey);
            decipher.update(encrypted);
            final byte[] decrypted = decipher.doFinal();
            if(!MESSAGE.equals(new String(decrypted))) {
                throw new Pkcs11DeviceTestException("Encryption test failed");
            }
        } catch (Exception ex) {
            throw new Pkcs11DeviceTestException("Crypto test failed!", ex);
        }
    }
    
    private void runSignTests(final Provider provider, final List<String> algorithms) throws Pkcs11NoSignatureSupport {
        int succeded = 0;
        
        for(String s : algorithms) {
            try {
                signTest(provider, privateKey, publicKey, s);
                succeded++;
            } catch (Pkcs11DeviceTestException | NoSuchAlgorithmException ex) {
                continue;
            }
        }
        if(succeded == 0) {
            throw new Pkcs11NoSignatureSupport(
                    environment
                    .getMessageProvider()
                    .translate("PKCS11", "Signatures are not supported by your token"
                    ));
        }
    }
    
    private void runCipherTests(final Provider provider, final List<String> algorithms) throws Pkcs11NoRsaSupport {
        int succeded = 0;
        
        for(String s : algorithms) {
            try {
                cipherTest(provider, privateKey, publicKey, s);
                succeded++;
            } catch (Pkcs11DeviceTestException | NoSuchAlgorithmException ex) {
                continue;
            }
        }
        if(succeded == 0) {
            throw new Pkcs11NoRsaSupport(
                    environment
                    .getMessageProvider()
                    .translate("PKCS11", "No public-key cryptography support"
                    ));
        }
    }
}
