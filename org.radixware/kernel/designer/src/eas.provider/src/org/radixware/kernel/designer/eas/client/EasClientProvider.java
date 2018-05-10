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

package org.radixware.kernel.designer.eas.client;

import java.security.cert.X509Certificate;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.eas.AbstractSslContextFactory;
import org.radixware.kernel.common.client.eas.AbstractSslTrustManager;
import org.radixware.kernel.common.client.eas.EasClient;
import org.radixware.kernel.common.client.eas.IEasClient;
import org.radixware.kernel.common.client.eas.connections.ConnectionOptions;
import org.radixware.kernel.common.client.exceptions.SslTrustManagerException;
import org.radixware.kernel.common.client.utils.ISecretStore;
import org.radixware.kernel.common.client.utils.TokenProcessor;
import org.radixware.kernel.common.enums.EAuthType;
import org.radixware.kernel.common.exceptions.CertificateUtilsException;
import org.radixware.kernel.common.exceptions.KeystoreControllerException;


public class EasClientProvider {    
    
    private final ConnectionOptions connection;
    private final IClientEnvironment environment;
 
    public EasClientProvider(final IClientEnvironment environment, final ConnectionOptions connection){
        this.connection = connection;
        this.environment = environment;
    }
    
    public IEasClient createEasClient(final char[] password) throws KeystoreControllerException, CertificateUtilsException{
        if (connection.getSslOptions() == null) {
            return new EasClient(environment, 
                                            connection.getInitialServerAddresses(),
                                            connection.getStationName(), 
                                            connection.getAuthType(), 
                                            connection.getAddressTranslationFilePath(),
                                            connection.isSapDiscoveryEnabled());
        } else {
            final AbstractSslContextFactory sslContextFactory;
            if (connection.getAuthType()==EAuthType.KERBEROS){
                sslContextFactory = new AbstractSslContextFactory(connection, environment){
                    @Override
                    public AbstractSslTrustManager createSslTrustManager(IClientEnvironment environment, String trustStorePath, X509Certificate certificate) {
                        return new AbstractSslTrustManager(environment, trustStorePath, certificate){
                            @Override
                            protected AbstractSslTrustManager.Confirmation confirm(X509Certificate cert, AbstractSslTrustManager.ConfirmationReason reason) throws SslTrustManagerException {
                                return AbstractSslTrustManager.Confirmation.ACCEPT;//server autorized by kerberos
                            }                            
                        };
                    }
                };            
            }else{
                sslContextFactory = new AbstractSslContextFactory(connection, environment){
                    @Override
                    public AbstractSslTrustManager createSslTrustManager(IClientEnvironment environment, String trustStorePath, X509Certificate certificate) {
                        return new AbstractSslTrustManager(environment, trustStorePath, certificate) {
                            @Override
                            protected AbstractSslTrustManager.Confirmation confirm(X509Certificate xc, AbstractSslTrustManager.ConfirmationReason cr) throws SslTrustManagerException {
                                return AbstractSslTrustManager.Confirmation.ACCEPT;//server authorized via kerberos
                            }
                        };
                    }
                };
            }
            final ISecretStore secretStore = environment.getApplication().newSecretStore();
            secretStore.setSecret(connection.getAuthType()==EAuthType.KERBEROS ? new byte[]{} : new TokenProcessor().encrypt(password));
            return new EasClient(environment, 
                                connection.getInitialServerAddresses(),
                                connection.getStationName(), 
                                connection.getAuthType(),
                                connection.getAddressTranslationFilePath(),
                                connection.isSapDiscoveryEnabled(),
                                sslContextFactory,
                                secretStore);
        }
        
    }
    
}
