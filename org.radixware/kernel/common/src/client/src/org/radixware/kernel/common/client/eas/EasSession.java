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

import java.security.cert.X509Certificate;
import org.radixware.kernel.common.auth.PasswordHash;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.utils.ISecretStore;
import org.radixware.kernel.common.enums.EAuthType;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.eas.CreateSessionRs;


public class EasSession extends AbstractEasSession implements IPrimaryEasSession {

    public EasSession(final IClientEnvironment environment, final IResponseNotificationScheduler scheduler) {
        super(environment,scheduler,null);
    }
    
    public EasSession(final IClientEnvironment environment) {
        super(environment, null ,null, null);
    }    

    public EasSession(final IClientEnvironment environment, final ISecretStore secretStore,final IResponseNotificationScheduler scheduler) {
        super(environment, secretStore, scheduler, null);
    }
    
    public EasSession(final IClientEnvironment environment, final ISecretStore secretStore) {
        super(environment, secretStore, null, null);
    }    

    @Override
    public CreateSessionRs open(final IEasClient soapConnection,
            final String stationName,
            final String userName,
            final String password,
            final EAuthType authType,
            final Id desiredExplorerRoot,
            final Long replacedSessionId,
            final boolean isWebDriverEnabled)
            throws ServiceClientException, InterruptedException {
        if (isOpened()) {
            close(false);
        }
        final AbstractEasSessionParameters parameters;
        switch (authType) {
            case PASSWORD:
                final EasSessionPwdParameters pwdParameters = 
                        new EasSessionPwdParameters(userName, stationName, null, isWebDriverEnabled);
                if (getSecretStore()==null){
                    setTokenCalculator(pwdParameters.createTokenCalculator(password));
                }else{
                    setTokenCalculator(pwdParameters.createTokenCalculator(getSecretStore()));
                }
                parameters = pwdParameters;
                break;
            case CERTIFICATE:
                final EasSessionCertParameters certParameters = 
                    new EasSessionCertParameters(userName, stationName, null, isWebDriverEnabled);
                setTokenCalculator(certParameters.createTokenCalculator());
                parameters = certParameters;
                break;
            default:
                throw new IllegalArgumentException("authType \'" + authType.getName() + "\' is not supported");
        }
        setConnection(soapConnection);
        return createSession(parameters, false, desiredExplorerRoot, replacedSessionId);
    }

    @Override
    public CreateSessionRs open(final IEasClient soapConnection,
            final String stationName,
            final X509Certificate[] userCertificates,
            final Id desiredExplorerRootId,
            final Long replacedSessionId,
            final boolean isWebDriverEnabled) throws ServiceClientException, InterruptedException {
        if (isOpened()) {
            close(false);
        }
        final EasSessionCertParameters parameters = 
            new EasSessionCertParameters(null, stationName, userCertificates, isWebDriverEnabled);
        setTokenCalculator(parameters.createTokenCalculator());
        setConnection(soapConnection);
        return createSession(parameters, false, desiredExplorerRootId, replacedSessionId);
    }

    public CreateSessionRs open(final IEasClient soapConnection,
            final String stationName,
            final String userName,
            final PasswordHash pwdHash,
            final Id desiredExplorerRoot,
            final Long replacedSessionId,
            final boolean isWebDriverEnabled)
            throws ServiceClientException, InterruptedException {
        if (isOpened()) {
            close(false);
        }        
        final EasSessionPwdParameters parameters = 
            new EasSessionPwdParameters(userName, stationName, null, isWebDriverEnabled);
        setTokenCalculator(parameters.createTokenCalculator(pwdHash.copy()));
        setConnection(soapConnection);
        return createSession(parameters, false, desiredExplorerRoot, replacedSessionId);
    }

    @Override
    public CreateSessionRs open(IEasClient soapConnection,
            String stationName,
            final IKerberosCredentialsProvider krbCredentialsProvider,
            final ISpnegoGssTokenProvider authDelegate,
            Id desiredExplorerRootId,
            final X509Certificate[] userCertificates,
            final Long replacedSessionId,
            final boolean isWebDriverEnabled) throws ServiceClientException, InterruptedException {
        if (isOpened()) {
            close(false);
        }
        setConnection(soapConnection);
        final AbstractEasSessionParameters parameters = 
            new EasSessionKrbParameters(null, stationName, krbCredentialsProvider, authDelegate, userCertificates, isWebDriverEnabled);
        return createSession(parameters, false, desiredExplorerRootId, replacedSessionId);
    }    
}
