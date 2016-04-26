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
import org.radixware.kernel.common.enums.EAuthType;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.eas.CreateSessionRs;


public interface IPrimaryEasSession extends IEasSession{
    
    public CreateSessionRs open(final IEasClient soapConnection,
            final String stationName,
            final String userName,
            final String password,
            EAuthType authType,
            final Id desiredExplorerRootId)
            throws ServiceClientException, InterruptedException;
    
    public CreateSessionRs open(final IEasClient soapConnection,
            final String stationName,
            final X509Certificate[] userCertificates,
            final Id desiredExplorerRootId)
            throws ServiceClientException, InterruptedException;    
    
    public CreateSessionRs open(final IEasClient soapConnection,
            final String stationName,
            final IKerberosCredentialsProvider krbCredentialsProvider,
            final ISpnegoGssTokenProvider authDelegate,
            final Id desiredExplorerRootId,
            final X509Certificate[] userCertificates)
            throws ServiceClientException, InterruptedException;      
}
