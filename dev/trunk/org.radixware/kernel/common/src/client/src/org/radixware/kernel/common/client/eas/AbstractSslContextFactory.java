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

import java.nio.file.Paths;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLContext;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.eas.connections.ConnectionOptions;
import org.radixware.kernel.common.enums.EKeyStoreType;
import org.radixware.kernel.common.exceptions.CertificateUtilsException;
import org.radixware.kernel.common.exceptions.KeystoreControllerException;
import org.radixware.kernel.common.ssl.CertificateUtils;
import org.radixware.kernel.common.ssl.KeystoreController;
import org.radixware.kernel.common.types.Id;



public abstract class AbstractSslContextFactory implements ISslContextFactory{
    
    protected final ConnectionOptions connection;
    protected final IClientEnvironment environment;
    private KeystoreController keystoreController;
    private AbstractSslTrustManager sslTrustManager;
    
    public AbstractSslContextFactory(final ConnectionOptions connection, final IClientEnvironment environment){
        this.connection = connection;
        this.environment = environment;
    }
    
    @Override
    public SSLContext createSslContext(final char[] keyStorePwd) throws KeystoreControllerException, CertificateUtilsException{
        final ConnectionOptions.SslOptions sslOptions = connection.getSslOptions();
        if (keystoreController!=null){
            connection.onClose();//
            keystoreController = null;
        }
        
        keystoreController = createKeyStoreController(keyStorePwd);
        
        final char[] keyPassword;
        if (sslOptions.useSSLAuth()) {
            if (sslOptions.getKeyStoreType() == EKeyStoreType.PKCS11) {
                keyPassword = new char[]{};
            } else {
                keyPassword = keyStorePwd;
            }
        } else {
            keyPassword = null;
        }
        final String certAlias = connection.getSslOptions().getCertificateAlias();
        final AbstractSslTrustManager trustManager = getSslTrustManager(keyStorePwd);
        return 
            CertificateUtils.prepareSslContext(keystoreController, keyPassword, trustManager, certAlias);
    }
    
    private KeystoreController createKeyStoreController(final char[] keyStorePwd) throws KeystoreControllerException{
        if (connection.getSslOptions().getKeyStoreType() == EKeyStoreType.PKCS11) {
            return connection.getKeyStoreController(keyStorePwd);
        } else {
            return connection.getKeyStoreController();
        }
        
    }
    
    private AbstractSslTrustManager getSslTrustManager(final char[] keyStorePwd){
        if (sslTrustManager==null){
            final String trustStorePath;
            if (connection.getSslOptions().getTrustStoreFilePath()==null || connection.getSslOptions().getTrustStoreFilePath().isEmpty()){
                trustStorePath = calcDefaultTrustKeyStorePath(connection.getId());
            }else{
                trustStorePath = connection.getSslOptions().getTrustStoreFilePath();
            }
            try{
                sslTrustManager = 
                    createSslTrustManager(environment, trustStorePath, connection.getCertificate(keyStorePwd));                    
            }catch(Exception ex){
                environment.getTracer().error(ex);
            }
        }
        return sslTrustManager;
    }
    
    private String calcDefaultTrustKeyStorePath(final Id connectionId){
        final String workPath = environment.getWorkPath();
        final String keystoreFile = connectionId==null ? "keystore" : connectionId.toString() + ".jceks";
        return Paths.get(workPath, keystoreFile).toString();
    }
    
    
    public abstract AbstractSslTrustManager createSslTrustManager(final IClientEnvironment environment, final String trustStorePath, X509Certificate certificate);
}