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

package org.radixware.kernel.server.arte.services.aas;

import java.util.Map;
import org.apache.xmlbeans.XmlObject;

import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.server.arte.services.Service;
import org.radixware.kernel.common.exceptions.ServiceProcessClientFault;
import org.radixware.kernel.common.exceptions.ServiceProcessFault;
import org.radixware.kernel.common.exceptions.ServiceProcessServerFault;
import org.radixware.kernel.server.arte.ArteSocket;
import org.radixware.kernel.server.arte.ArteTransactionParams;
import org.radixware.kernel.server.types.Pid;
import org.radixware.kernel.server.units.arte.ArteUnit;
import org.radixware.schemas.aas.ExceptionEnum;
import org.radixware.schemas.aas.InvokeMess;
import org.radixware.schemas.aas.InvokeRq;
import org.radixware.schemas.aas.InvokeRs;
import org.radixware.schemas.aas.TransactionEnum;
import org.radixware.schemas.aas.Value;
import org.radixware.schemas.aasWsdl.InvokeDocument;

public final class ArteAccessService extends Service {
    
    private final static class AasTransactionParams extends ArteTransactionParams{
        
        public AasTransactionParams(final Long version, final String userName){
            super(version, null, userName, null, null, null, null, null);
        }        
    }
    
//Service URI

    public static final String SERVICE_WSDL = "http://schemas.radixware.org/aas.wsdl";
    public static final String SERVICE_XSD = "http://schemas.radixware.org/aas.xsd";

//Constructor
    public ArteAccessService(final Arte arte, final int recvTimeout) {
        super(arte, recvTimeout);
    }

//Protected methods    
    @Override
    protected void prepare(final ArteSocket port, final XmlObject request, final Map<String, String> headerAttrs) throws ServiceProcessClientFault {
        final InvokeRq rqParams = getInvokeRq(request);
        if (rqParams.isSetSetSavepoint()) // TODO ? SavePoints in AAS
        {
            throw new RadixError("TODO SavePoints in AAS");
        }
        final InvokeRq invokeRq = getInvokeRq(request);
        getArte().startTransaction(new AasTransactionParams(arte.getEffectiveRequestVersion(), invokeRq.getUser()));
    }

    @Override
    protected boolean isLastRequest(final XmlObject request) throws ServiceProcessClientFault {
        switch (getTranWordIntValue(getInvokeRq(request))) {
            case TransactionEnum.INT_NO:
            case TransactionEnum.INT_FINISH:
            case TransactionEnum.INT_COMMIT_FINISH:
            case TransactionEnum.INT_ROLLBACK_FINISH:
                return true;
            default:
                return false;
        }
    }

    @Override
    protected boolean shouldCommit(final XmlObject request) throws ServiceProcessClientFault {
        switch (getTranWordIntValue(getInvokeRq(request))) {
            case TransactionEnum.INT_NO:
            case TransactionEnum.INT_AUTO_COMMIT:
            case TransactionEnum.INT_FINISH:
            case TransactionEnum.INT_COMMIT_FINISH:
            case TransactionEnum.INT_ROLLBACK_FINISH:
                return true;
            default:
                return false;
        }
    }

    @Override
    protected XmlObject processRequest(final ArteSocket port, final XmlObject request, final Map<String, String> headerAttrs) throws ServiceProcessFault, InterruptedException {
        try {
            getArte().onStartRequestExecution(null);
            final InvokeRq invokeRq = getInvokeRq(request);
            if (invokeRq.isSetThreadPriority()) {
                getArte().getTrace().put(EEventSeverity.WARNING.getValue(), "ThreadPriority attribute is not supported in AAS anymore", EEventSource.ARTE.getValue());
            }
            return doProcessRequest(port.getUnit(), invokeRq);
        } catch (Throwable e) {
            throw AasFaults.exception2Fault(getArte(), e, "Request raises exception");
        } finally {
            getArte().onFinishRequestExecution();
        }
    }

    protected XmlObject doProcessRequest(final ArteUnit unit, final InvokeRq invokeRq) throws ServiceProcessFault, InterruptedException, Exception {
        //Transaction
        switch (getTranWordIntValue(invokeRq)) {
            case TransactionEnum.INT_COMMIT:
            case TransactionEnum.INT_COMMIT_FINISH:
                getArte().commit();
                break;
            case TransactionEnum.INT_ROLLBACK:
            case TransactionEnum.INT_ROLLBACK_FINISH:
                getArte().rollback();
                break;
        }
        final Object[] paramVals;
        try {
            if (invokeRq.isSetParameters()) {
                paramVals = new Object[invokeRq.getParameters().getItemList().size()];
                int i = 0;
                for (Value paramXml : invokeRq.getParameters().getItemList()) {
                    paramVals[i] = AasValueConverter.aasXmlVal2ObjVal(getArte(), paramXml);
                    i++;
                }
            } else {
                paramVals = new Object[]{};
            }
        } catch (Throwable e) {
            throw AasFaults.exception2Fault(getArte(), e, "Can't restore parameters from request XML");
        }
        final Class[] paramTypes = new Class[paramVals.length];
        int i = 0;
        for (Object val : paramVals) {
            if (val == null) {
                paramTypes[i] = null;
            } else {
                paramTypes[i] = val.getClass();
                if (val instanceof XmlObject && val.getClass().getName().endsWith("Impl")) {
                    Class[] ifs = val.getClass().getInterfaces();
                    if (ifs != null && ifs.length > 0) {
                        paramTypes[i] = ifs[0];
                    }
                }
            }
            i++;
        }
        final Object returnVal;
        try {
            if (invokeRq.getMethodId() == null) {
                throw new ServiceProcessClientFault(ExceptionEnum.APPLICATION_ERROR.toString(), "\"MethodId\" is required in InvokeRq", null, null);
            }
            if (invokeRq.isSetPID()) { //aec method
                final Id entityId;
                if (invokeRq.isSetEntityId()) {
                    entityId = invokeRq.getEntityId();
                } else if (invokeRq.isSetClassId()) {
                    entityId = getArte().getDefManager().getClassEntityId(invokeRq.getClassId());
                } else {
                    throw new ServiceProcessClientFault(ExceptionEnum.APPLICATION_ERROR.toString(), "\"ClassId\"or \"EntityId\" is required in InvokeRq", null, null);
                }
                returnVal = getArte().invoke(new Pid(arte, entityId, invokeRq.getPID()), invokeRq.getMethodId(), paramTypes, paramVals);
            } else if (invokeRq.isSetClassId()) //static method
            {
                returnVal = getArte().invokeByClassId(invokeRq.getClassId(), invokeRq.getMethodId(), paramTypes, paramVals);
            } else if (invokeRq.isSetClassName()) {
                returnVal = getArte().invokeByClassName(invokeRq.getClassName(), invokeRq.getMethodId(), paramTypes, paramVals);
            } else {
                throw new ServiceProcessClientFault(ExceptionEnum.APPLICATION_ERROR.toString(), "\"ClassId\", \"ClassName\" or \"PID\" is required in InvokeRq", null, null);
            }
        } catch (RuntimeException e) {
            throw AasFaults.exception2Fault(getArte(), e, "Can't invoke requested method");
        }
        final InvokeDocument res = InvokeDocument.Factory.newInstance();
        final InvokeRs rsStruct = res.addNewInvoke().addNewInvokeRs();
        try {
            AasValueConverter.objVal2AasXmlVal(returnVal, rsStruct.addNewReturnValue());
        } catch (Throwable e) {
            throw AasFaults.exception2Fault(getArte(), e, "Can't write return value to response XML");
        }
        return res;
    }

    @Override
    protected ServiceProcessFault errorToFault(final Throwable exception) {
        if (exception instanceof ServiceProcessFault) {
            return (ServiceProcessFault) exception;
        }
        final String reason;
        if (exception instanceof RuntimeException) {
            reason = ExceptionEnum.SERVER_MALFUNCTION.toString();
        } else {
            reason = ExceptionEnum.SERVER_EXCEPTION.toString();
        }
        final String preprocessedExceptionStack = getArte().getTrace().exceptionStackToString(exception);
        getArte().getTrace().put(EEventSeverity.WARNING, "Unhandled exception in service \"" + SERVICE_WSDL + "\" request processing:\n" + preprocessedExceptionStack, EEventSource.AAS);
        return new ServiceProcessServerFault(reason, "Unhandled " + exception.getClass().getName() + (exception.getMessage() != null ? ":\n" + exception.getMessage() : ""), exception, preprocessedExceptionStack);
    }

    @Override
    protected final String getServiceWsdl() {
        return SERVICE_WSDL;
    }

    @Override
    protected String getTraceProfile(final XmlObject request) throws ServiceProcessClientFault {
        return EEventSeverity.NONE.getName();//RADIX-2514
        //return getInvokeRq(request).getTraceProfile();
    }

    private InvokeRq getInvokeRq(final XmlObject rqMess) throws ServiceProcessClientFault {
        InvokeMess mess = null;
        if (rqMess instanceof InvokeMess) {
            mess = (InvokeMess) rqMess;
        } else if (rqMess instanceof InvokeDocument) {
            mess = ((InvokeDocument) rqMess).getInvoke();
        } else {
            try {
                mess = InvokeMess.Factory.parse(rqMess.newReader());
            } catch (Exception ex) {
                //really not InvokeRq
                if (getArte().getTrace().getMinSeverity(EEventSource.AAS) <= EEventSeverity.DEBUG.getValue().longValue()) {
                    final String dbgMess =
                            "AAS received wrong request: " + rqMess.getDomNode().getLocalName() + "\n"
                            + "Request class: " + rqMess.getClass().getName() + ", classLoader: " + rqMess.getClass().getClassLoader().toString() + "\n"
                            + "AAS requests' classLoader: " + InvokeMess.class.getClassLoader().toString() + "\n"
                            + "Thread's context classLoader: " + Thread.currentThread().getContextClassLoader();
                    getArte().getTrace().put(EEventSeverity.DEBUG, dbgMess, EEventSource.AAS);
                }
                final String preprocessedExceptionStack = getArte().getTrace().exceptionStackToString(ex);
                throw new ServiceProcessClientFault(ExceptionEnum.INVALID_REQUEST.toString(), "Request \"" + rqMess.getClass().getName() + "\" is not supported \nby \"" + SERVICE_WSDL + "\" service", ex, preprocessedExceptionStack);
            }

        }

        final InvokeRq invokeRq = mess.getInvokeRq();
        if (invokeRq == null) {
            throw new ServiceProcessClientFault(ExceptionEnum.INVALID_REQUEST.toString(), "Parameter \"InvokeRq\" is required", null, null);
        }
        return invokeRq;
    }

    private static final int getTranWordIntValue(final InvokeRq request) {
        if (request.isSetTransaction()) {
            return request.getTransaction().intValue();
        } else {
            return TransactionEnum.INT_NO;
        }
    }

    @Override
    protected void checkClientCanTrace(final String traceProfile) {
    }

    @Override
    protected void prepareResponseForSend(XmlObject request, XmlObject response, RequestTraceBuffer traceBuffer) throws ServiceProcessFault {
        //do nothing
    }
}
