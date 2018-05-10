/*
 * Copyright (c) 2008-2018, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. This Source Code is distributed
 * WITHOUT ANY WARRANTY; including any implied warranties but not limited to
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Mozilla Public License, v. 2.0. for more details.
 */

package org.radixware.kernel.server.arte.services.sb;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLPeerUnverifiedException;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.enums.ELdapX500AttrType;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.exceptions.ServiceProcessClientFault;
import org.radixware.kernel.common.exceptions.ServiceProcessFault;
import org.radixware.kernel.common.exceptions.ServiceProcessServerFault;
import org.radixware.kernel.common.ssl.CertificateUtils;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.server.arte.ArteSocket;
import org.radixware.kernel.server.arte.ArteTransactionParams;
import org.radixware.kernel.server.arte.services.Service;
import org.radixware.kernel.server.types.Pid;
import org.radixware.schemas.aas.ExceptionEnum;

public class SbService extends Service {
    
    private static final class SbServiceTransactinParams extends ArteTransactionParams{
        
        public SbServiceTransactinParams(final Long version){
            super(version, null, null, null, null, null, null, null, null, null, null);
        }
    }
    
    private static final Id ENTITY_ID = Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E");
    private static final String PROCESS_METHOD = "mth4LMGQAO7HNAAHEQGEKUGRB4CWA";
    
    private final String serviceUri;
    
    public SbService(Arte arte, String serviceUri, int recvTimeout) {
        super(arte, recvTimeout);
        this.serviceUri = serviceUri;
    }
    
    @Override
    protected void checkClientCanTrace(String traceProfile) throws ServiceProcessClientFault {
    }

    @Override
    protected ServiceProcessFault errorToFault(final Throwable exception){
        if (exception instanceof ServiceProcessFault)
            return (ServiceProcessFault)exception;
        final String reason;
        if (exception instanceof RuntimeException)
            reason = ExceptionEnum.SERVER_MALFUNCTION.toString();
        else
            reason = ExceptionEnum.SERVER_EXCEPTION.toString();
        final String preprocessedExceptionStack = getArte().getTrace().exceptionStackToString(exception);
        getArte().getTrace().put(EEventSeverity.WARNING, "Unhandled exception in service \"" + serviceUri + "\" request processing:\n" + preprocessedExceptionStack, EEventSource.AAS);
    	return new ServiceProcessServerFault(reason, "Unhandled " + exception.getClass().getName() + (exception.getMessage() != null ? ":\n" + exception.getMessage() : ""), exception, preprocessedExceptionStack) ;
    }
    

    @Override
    protected String getServiceWsdl() {
        return serviceUri;
    }

    @Override
    protected String getTraceProfile(XmlObject request) throws ServiceProcessFault {
        return null;
    }

    @Override
    protected boolean isLastRequest(XmlObject request) throws ServiceProcessFault {
        return true;
    }

    @Override
    protected XmlObject extractActualRequest(XmlObject soapRequestContent) {
        return soapRequestContent;
    }
    
    @Override
    protected void prepare(ArteSocket port, XmlObject request, Map<String, String> headerAttrs) throws ServiceProcessFault {
        arte.startTransaction(new SbServiceTransactinParams(arte.getEffectiveRequestVersion()));
    }
	
    @Override
    protected void prepareResponseForSend(XmlObject request, XmlObject response, RequestTraceBuffer traceBuffer) throws ServiceProcessFault {
    }

    public String getCertCn(ArteSocket port) {
        try {
            if (port.getSslSession() != null) {
                final String dn = port.getSslSession().getPeerPrincipal().getName();
                return CertificateUtils.parseDistinguishedName(dn).get(ELdapX500AttrType.COMMON_NAME.getValue());
            }
        } catch (SSLPeerUnverifiedException ex) {
            //if ssl settings dont require certificate check, there is no reason to generate warning,
            //and if check is required, then request processing will fail before this.
            //getArte().getTrace().put(EEventSeverity.WARNING, "Unable to identify user by given certificate: " + ExceptionTextFormatter.throwableToString(e), EEventSource.ARTE.getValue(), false);
            Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
        }
        return null;
    }    

    @Override
    protected XmlObject processRequest(ArteSocket port, XmlObject request, Map<String, String> headerAttrs) throws ServiceProcessFault, InterruptedException {
        getArte().onStartRequestExecution(Arte.genUserRequestExecutorId(getArte().getUserName()));
        try {
            final Pid pid = new Pid(arte, ENTITY_ID, String.valueOf(port.getUnit().getId()));
            return (XmlObject)arte.invoke(pid, PROCESS_METHOD, 
                new Class[] {String.class, String.class, String.class, Map.class, XmlObject.class},
                new Object[] {port.getLocalAddress(), port.getRemoteAddress(), getCertCn(port), headerAttrs, request}
            );
        } catch (Exception e) {
            if (e instanceof RuntimeException)
                throw (RuntimeException) e;
            if (e instanceof ServiceProcessFault)
                throw (ServiceProcessFault) e;
            if (e instanceof InterruptedException)
                throw (InterruptedException) e;
            throw new RadixError(e.getMessage(), e);
        } finally {
            getArte().onFinishRequestExecution();            
        }
    }

    @Override
    protected boolean shouldCommit(XmlObject request) throws ServiceProcessFault {
        return true;
    }
}