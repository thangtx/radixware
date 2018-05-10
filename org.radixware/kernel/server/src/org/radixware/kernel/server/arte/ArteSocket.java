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

package org.radixware.kernel.server.arte;

import java.util.List;
import java.util.Map;
import javax.net.ssl.SSLSession;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.enums.EAadcMember;
import org.radixware.kernel.common.exceptions.*;
import org.radixware.kernel.common.sc.SapClientOptions;
import org.radixware.kernel.common.soap.RadixSoapMessage;
import org.radixware.kernel.common.utils.SoapFormatter;
import org.radixware.kernel.common.utils.net.SocketsDisconnectWatcher;
import org.radixware.kernel.server.exceptions.ArteSocketException;
import org.radixware.kernel.server.exceptions.ArteSocketTimeout;
import org.radixware.kernel.server.units.arte.ArteUnit;


public abstract class ArteSocket {

    protected SSLSession sslSession = null;

    protected ArteSocket() {
    }

    /**
     * Get inner content of the soap request
     *
     * @param timeout
     * @return
     * @throws ArteSocketException
     * @throws ArteSocketTimeout
     * @throws InterruptedException
     * @deprecated use {@linkplain recvSoapRequest(int) } and extract inner
     * content of the XmlObject if necessary.
     */
    @Deprecated
    abstract public XmlObject recvRequest(final int timeout) throws ArteSocketException, ArteSocketTimeout, InterruptedException;

    /**
     * Receive full soap request
     *
     * @param timeout
     * @return
     * @throws ArteSocketException
     * @throws ArteSocketTimeout
     * @throws InterruptedException
     */
    abstract public XmlObject recvSoapRequest(final int timeout, final Map<String, String> headerAttrs) throws ArteSocketException, ArteSocketTimeout, InterruptedException;

    abstract public XmlObject invokeResource(final XmlObject obj, final Class resultClass, final int timeout) throws ResourceUsageException, ResourceUsageTimeout, InterruptedException;
    
        abstract public XmlObject invokeInternalService(final XmlObject obj, final Class resultClass, String service, int keepConnectTime, final int timeout, final EAadcMember targetAadcMember) throws ServiceCallException, ServiceCallTimeout, ServiceCallFault, InterruptedException;
    
    abstract public XmlObject invokeInternalService(final XmlObject obj, final Class resultClass, String service, int keepConnectTime, final int timeout, final int connectTimeout, final EAadcMember targetAadcMember) throws ServiceCallException, ServiceCallTimeout, ServiceCallFault, InterruptedException;

    abstract public XmlObject invokeService(final XmlObject obj, final Class resultClass, Long systemId, String service, String scpName, int keepConnectTime, final int timeout, final EAadcMember targetAadcMember) throws ServiceCallException, ServiceCallTimeout, ServiceCallFault, InterruptedException;

    abstract public XmlObject invokeService(final XmlObject obj, final Map<String, String> soapRequestParams, final Class resultClass, final Long systemId, final String serviceUti, final String scpName, final int keepConnectTime, final int timeout, final EAadcMember targetAadcMember) throws ServiceCallException, ServiceCallTimeout, ServiceCallFault, InterruptedException;

    abstract public XmlObject invokeService(final XmlObject obj, final Map<String, String> soapRequestParams, final Class resultClass, final Long systemId, final String serviceUri, final String scpName, final List<SapClientOptions> additionalSaps, final int keepConnectTime, final int timeout, final EAadcMember targetAadcMember) throws ServiceCallException, ServiceCallTimeout, ServiceCallFault, InterruptedException;
    
    abstract public XmlObject invokeService(final XmlObject obj, final Map<String, String> soapRequestParams, final Class resultClass, final Long systemId, final String serviceUri, final String scpName, final List<SapClientOptions> additionalSaps, final int keepConnectTime, final int timeout, final int connectTimeout, final EAadcMember targetAadcMember) throws ServiceCallException, ServiceCallTimeout, ServiceCallFault, InterruptedException;
    
    abstract public XmlObject invokeService(final RadixSoapMessage soapMess, final String scpName, final EAadcMember targetAadcMember) throws ServiceCallException, ServiceCallTimeout, ServiceCallFault, InterruptedException;

    @Deprecated
    abstract public XmlObject invokeInternalService(final XmlObject obj, final Class resultClass, String service, int keepConnectTime, final int timeout) throws ServiceCallException, ServiceCallTimeout, ServiceCallFault, InterruptedException;
    
    @Deprecated
    abstract public XmlObject invokeInternalService(final XmlObject obj, final Class resultClass, String service, int keepConnectTime, final int timeout, final int connectTimeout) throws ServiceCallException, ServiceCallTimeout, ServiceCallFault, InterruptedException;

    @Deprecated
    abstract public XmlObject invokeService(final XmlObject obj, final Class resultClass, Long systemId, String service, String scpName, int keepConnectTime, final int timeout) throws ServiceCallException, ServiceCallTimeout, ServiceCallFault, InterruptedException;

    @Deprecated
    abstract public XmlObject invokeService(final XmlObject obj, final Map<String, String> soapRequestParams, final Class resultClass, final Long systemId, final String serviceUti, final String scpName, final int keepConnectTime, final int timeout) throws ServiceCallException, ServiceCallTimeout, ServiceCallFault, InterruptedException;

    @Deprecated
    abstract public XmlObject invokeService(final XmlObject obj, final Map<String, String> soapRequestParams, final Class resultClass, final Long systemId, final String serviceUri, final String scpName, final List<SapClientOptions> additionalSaps, final int keepConnectTime, final int timeout) throws ServiceCallException, ServiceCallTimeout, ServiceCallFault, InterruptedException;
    
    @Deprecated
    abstract public XmlObject invokeService(final XmlObject obj, final Map<String, String> soapRequestParams, final Class resultClass, final Long systemId, final String serviceUri, final String scpName, final List<SapClientOptions> additionalSaps, final int keepConnectTime, final int timeout, final int connectTimeout) throws ServiceCallException, ServiceCallTimeout, ServiceCallFault, InterruptedException;
    
    @Deprecated
    abstract public XmlObject invokeService(final RadixSoapMessage soapMess, final String scpName) throws ServiceCallException, ServiceCallTimeout, ServiceCallFault, InterruptedException;

    abstract public void sendResponse(final XmlObject obj, final boolean keepConnect) throws ArteSocketException, ArteSocketTimeout, InterruptedException;

    abstract public void sendFault(final ServiceProcessFault fault, final List<SoapFormatter.ResponseTraceItem> trace) throws ArteSocketException, ArteSocketTimeout, InterruptedException;

    abstract public List<SapClientOptions> getServiceSaps(final Long systemId, final String serviceUri, final String scpName) throws ServiceCallException;

    abstract public boolean breakSignaled();

    abstract public String getLocalAddress();

    abstract public String getRemoteAddress();

    abstract public long getSapId();

    abstract public ArteUnit getUnit();

    abstract public SocketsDisconnectWatcher getSocketsDisconnectWatcher();

    public final void setSslSession(SSLSession sslSession) {
        this.sslSession = sslSession;
    }

    public final SSLSession getSslSession() {
        return sslSession;
    }
}