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

package org.radixware.wps;

import java.io.File;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.CertificateRevokedException;
import java.security.cert.X509Certificate;
import java.util.Collections;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import org.radixware.kernel.common.client.eas.ISslContextFactory;
import org.radixware.kernel.common.client.eas.connections.ConnectionOptions;
import org.radixware.kernel.common.client.exceptions.FileException;
import org.radixware.kernel.common.enums.EAuthType;
import org.radixware.kernel.common.exceptions.CertificateUtilsException;
import org.radixware.kernel.common.exceptions.KeystoreControllerException;
import org.radixware.kernel.common.ssl.CertificateUtils;
import org.radixware.kernel.common.ssl.KeystoreController;


public final class SslContextFactory implements ISslContextFactory{
    
    private static class DummyTrustManager implements X509TrustManager{
        
        private final static X509Certificate[] EMPTY_ARR = new X509Certificate[]{};
        private final static DummyTrustManager INSTANCE = new DummyTrustManager();
        
        private DummyTrustManager(){            
        }

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return EMPTY_ARR;
        }       
        
        public static DummyTrustManager getInstance(){
            return INSTANCE;
        }
    }
    
    private static class SslTrustManager implements X509TrustManager{
        
        private final X509TrustManager internal;
        private final X509Certificate  clientCertificate;
        
        public SslTrustManager(final KeystoreController keystoreController, final X509Certificate clientCertificate) throws KeystoreControllerException, CertificateUtilsException{
            this.clientCertificate = clientCertificate;
            final TrustManagerFactory trustManagerFactory;
            try{
                trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
                keystoreController.initTrustManagerFactory(trustManagerFactory);
            }catch(NoSuchAlgorithmException exception){
                throw new CertificateUtilsException(exception.getMessage(), exception);
            }
            final TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
            X509TrustManager x509TrustManager = null;
            for (int i = 0; i < trustManagers.length; i++) {
                if (trustManagers[i] instanceof X509TrustManager) {
                    x509TrustManager = (X509TrustManager)trustManagers[i];
                    break;
                }
            }
            if (x509TrustManager == null){
                throw new CertificateUtilsException("Failed to initialize X509TrustManager");
            }else{
                internal = x509TrustManager;
            }            
        }
        
        public SslTrustManager(final KeystoreController keystoreController) throws KeystoreControllerException, CertificateUtilsException{
            this(keystoreController,null);
        }

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            internal.checkClientTrusted(chain, authType);
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            try{
                internal.checkServerTrusted(chain, authType);
            }catch(CertificateExpiredException | CertificateNotYetValidException | CertificateRevokedException exception){
                throw exception;
            }catch(CertificateException exception){
                if (clientCertificate==null){
                    throw exception;
                }
                try{
                    clientCertificate.verify(chain[0].getPublicKey());
                }catch(Exception verifyException){
                    throw exception;
                }
            }
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return internal.getAcceptedIssuers();
        }
        
    }
    
    private static volatile KeystoreController EMPTY_KEY_STORE;
            
    private final ConnectionOptions connection;    
    private final WpsEnvironment environment;
    
    public SslContextFactory(final ConnectionOptions connection, final WpsEnvironment environment){
        this.connection = connection;
        this.environment = environment;
    }
    
    @Override
    public SSLContext createSslContext(final char[] keyStorePwd) throws KeystoreControllerException, CertificateUtilsException {        
        if (connection.getAuthType()==EAuthType.CERTIFICATE){
            final KeystoreController keyStoreController = KeystoreController.getServerKeystoreController();
            String alias = WebServerRunParams.getCertificateAlias();
            if (alias==null){
                final String[] aliases = keyStoreController.getRsaKeyAliases();
                alias = aliases!=null && aliases.length>0 ? aliases[0] : null;
            }
            if (alias==null){
                final String message = 
                    environment.getMessageProvider().translate("ExplorerError", "There are no accessible certificates for authentication");
                throw new KeystoreControllerException(message);
            }
            final X509Certificate certificate = keyStoreController.getCertificate(alias);
            final KeyManager[] keyManagers = 
                    CertificateUtils.createServerKeyManagers(Collections.singletonList(alias));
            final X509TrustManager trustManager = createTrustManager(certificate);
            
            try {
                final SSLContext serverSslContext = SSLContext.getInstance("TLS");
                serverSslContext.init(keyManagers, new TrustManager[]{trustManager}, null);
                return serverSslContext;
            } catch (GeneralSecurityException e) {
                throw new CertificateUtilsException(e.getMessage(), e);
            }
        }else{
            final X509TrustManager trustManager;
            if (connection.getAuthType()==EAuthType.KERBEROS){
                //trust to any server certificate
                trustManager = DummyTrustManager.getInstance();
            }else{
                trustManager = createTrustManager(null);
            }
            return CertificateUtils.prepareSslContext(getEmptyKeyStore(), null, trustManager, null);
        }
    }
    
    private X509TrustManager createTrustManager(final X509Certificate clientCertificate) throws KeystoreControllerException, CertificateUtilsException{
        final File trustStoreFile = getTrustStoreFile();
        final KeystoreController keystoreController;
        if (trustStoreFile==null){
            keystoreController = KeystoreController.getServerKeystoreController();
        }else{
            keystoreController = 
                    KeystoreController.newClientInstance(trustStoreFile, connection.getSslOptions().getTrustStorePassword());
        }
        return new SslTrustManager(keystoreController,clientCertificate);        
    }
                
    private File getTrustStoreFile(){
        final ConnectionOptions.SslOptions sslOptions = connection.getSslOptions();
        final String trustStorePath = sslOptions.getTrustStoreFilePath();
        if (trustStorePath==null || trustStorePath.isEmpty()){
            return null;
        }
        final File trustStoreFile;
        if (sslOptions.isTrustStorePathRelative()) {
            final String connectionsFilePath = WebServerRunParams.getConnectionsFile();
            File connectionsFile = new File(connectionsFilePath);
            trustStoreFile = new File(connectionsFile.getParentFile(), trustStorePath);
        } else {
            trustStoreFile = new File(trustStorePath);
        }
        if (!trustStoreFile.canRead()){
            final FileException exception = 
                new FileException(environment, FileException.EExceptionCode.CANT_READ, trustStoreFile.getAbsolutePath());
            environment.getTracer().warning(exception.getMessage());
            return null;
        }else{
            return trustStoreFile;
        }
    }    
    
    private static synchronized KeystoreController getEmptyKeyStore() throws KeystoreControllerException{
        if (EMPTY_KEY_STORE==null){
            EMPTY_KEY_STORE = new KeystoreController();
        }
        return EMPTY_KEY_STORE;                
    }
}
