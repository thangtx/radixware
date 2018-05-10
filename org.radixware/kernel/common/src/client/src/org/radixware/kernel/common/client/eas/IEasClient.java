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

import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.client.utils.ISecretStore;
import org.radixware.kernel.common.exceptions.CertificateUtilsException;
import org.radixware.kernel.common.exceptions.KeystoreControllerException;
import org.radixware.kernel.common.exceptions.ServiceCallException;
import org.radixware.kernel.common.exceptions.ServiceCallFault;
import org.radixware.kernel.common.exceptions.ServiceCallTimeout;
import org.radixware.kernel.common.exceptions.ServiceClientException;


public interface IEasClient {       

    XmlObject sendRequest(final XmlObject request, 
                                        final String scpName, 
                                        final long definitionVersion, 
                                        final int timeoutSec,
                                        final IAadcMemberSwitchController handler) throws ServiceCallException, ServiceCallTimeout, ServiceCallFault, ServiceClientException, InterruptedException;

    XmlObject sendCallbackResponse(final XmlObject response, final int timeoutSec) throws ServiceCallException, ServiceCallTimeout, ServiceCallFault, ServiceClientException, InterruptedException;

    XmlObject sendFaultMessage(final String message, 
                                                final Throwable error, 
                                                final int timeoutSec) throws ServiceCallException, ServiceCallTimeout, ServiceCallFault, ServiceClientException, InterruptedException;
    
    void renewSslContext(final ISecretStore secretStore) throws KeystoreControllerException, CertificateUtilsException;
    
    void close();        
}