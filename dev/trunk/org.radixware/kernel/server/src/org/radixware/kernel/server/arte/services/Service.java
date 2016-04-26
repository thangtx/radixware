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
package org.radixware.kernel.server.arte.services;

import java.io.IOException;
import java.util.*;

import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.enums.EEventContextType;

import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.utils.SoapFormatter;
import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.server.arte.ArteSocket;
import org.radixware.kernel.common.exceptions.ServiceProcessClientFault;
import org.radixware.kernel.common.exceptions.ServiceProcessFault;
import org.radixware.kernel.server.trace.TraceBuffer;
import org.radixware.kernel.common.trace.TraceItem;
import org.radixware.kernel.common.trace.TraceProfile;
import org.radixware.kernel.common.utils.SystemPropUtils;
import org.radixware.kernel.server.SrvRunParams;
import org.radixware.kernel.server.instance.arte.ArteProcessor;
import org.radixware.kernel.server.trace.ServerTrace;
import org.radixware.kernel.server.trace.TraceContext;
import org.radixware.kernel.server.trace.TraceProfiles;
import org.radixware.kernel.server.units.Unit;
import org.xmlsoap.schemas.soap.envelope.Body;
import org.xmlsoap.schemas.soap.envelope.Envelope;

/**
 * Сервис предоставляемый в рамках ARTE.
 *
 *
 *
 */
public abstract class Service {

    final private int recvTimeout;
    protected final Arte arte;

    protected Service(final Arte arte, final int recvTimeout) {
        this.recvTimeout = recvTimeout;
        this.arte = arte;
    }

    public Arte getArte() {
        return arte;
    }

    public void close() {
    }

    protected XmlObject extractActualRequest(final XmlObject soapRequestContent) {
        try {
            if (soapRequestContent instanceof Body) {
                return SoapFormatter.getInnerContent(soapRequestContent);
            } else {
                return soapRequestContent;
            }
        } catch (IOException ex) {
            throw new RadixError("Unable to extract request content", ex);
        }
    }

    protected RequestTraceBuffer createRequestTraceBuffer() {
        return new RequestTraceBuffer();
    }

    public void process(final ArteSocket port) throws InterruptedException {
        final RequestTraceBuffer traceBuffer = createRequestTraceBuffer();
        Object clientTraceTarget = null;
        Object unitTraceTarget = null;
        if (port.getUnit() != null) {
            unitTraceTarget = arte.getTrace().addTargetBuffer(EEventSeverity.DEBUG.getName(), new CopyToUnitTraceBuffer(port.getUnit(), arte));
        }
        try {
            if (port.getUnit() != null) {
                arte.getTrace().enterContext(EEventContextType.SYSTEM_UNIT, port.getUnit().getId());
            }
            try {
                Map<String, String> headerAttrs = new HashMap<>();
                XmlObject request = extractActualRequest(port.recvSoapRequest(recvTimeout, headerAttrs));
                if (request == null) {
                    //client disconnected
                    //Trace.put(EEventSeverity.WARNING, "Empty request received by \"" + getServiceWsdl()+"\" service", EEventSource.ARTE);
                    return;
                }
                arte.serviceRqProcessingStarted(request);
                String traceProfile = getTraceProfile(request);
                clientTraceTarget = arte.getTrace().addTargetBuffer(traceProfile, traceBuffer);

                prepare(port, request, headerAttrs);
                boolean commit = false;
                XmlObject response = null;
                while (request != null) {
                    checkInterrupted();
                    arte.checkBreak();
                    try {
                        checkClientCanTrace(traceProfile);
                    } catch (ServiceProcessClientFault flt) {
                        traceBuffer.clear();
                        arte.getTrace().changeTargetProfile(clientTraceTarget, EEventSeverity.NONE.getName());
                        throw flt;
                    }
                    response = processRequest(port, request, headerAttrs);
                    traceBuffer.resetLimit();
                    if (response == null) {
                        throw new RadixError("SOAP Service didn't return any response", null);
                    }
                    checkInterrupted();
                    commit = shouldCommit(request);
                    if (isLastRequest(request)) {
                        break;
                    }
                    arte.checkBreak();
                    if (commit) {
                        arte.commit();
                    }
                    prepareResponseForSend(request, response, traceBuffer);
                    traceBuffer.clear();
                    port.sendResponse(response, true);
                    arte.serviceRqProcessingStopped();
                    request = extractActualRequest(port.recvSoapRequest(recvTimeout, headerAttrs));
                    if (request != null) {
                        arte.serviceRqProcessingStarted(request);
                        traceProfile = getTraceProfile(request);
                        arte.getTrace().changeTargetProfile(clientTraceTarget, traceProfile);
                    }
                }
                arte.endTransaction(commit);
                if (port.getUnit() != null) {
                    checkTraceContextIsOnlyUnit(port.getUnit());
                }
                prepareResponseForSend(request, response, traceBuffer);
                port.sendResponse(response, false);
            } catch (InterruptedException ex) {
                rollbackAndEndTransaction();
                throw ex;
            } catch (Throwable ex) {
                rollbackAndEndTransaction();
                if (port.getUnit() == null) {//init request
                    throw new RuntimeException(ex);
                }
                sendFault(port, ex, traceBuffer);
            } finally {
                try {
                    delTraceTarget(clientTraceTarget);
                    if (arte.isInTransaction()) {
                        arte.getTrace().put(EEventSeverity.ALARM, "Service client disconnected but ARTE seance is still opened.\nTrying to do rollback and close transcation by force.", EEventSource.ARTE);
                        rollbackAndEndTransaction();
                    }
                } finally {
                    try {
                        arte.serviceRqProcessingStopped();
                        arte.getTrace().flush();
                    } finally {
                        try {
                            arte.getTrace().leaveContext(EEventContextType.SYSTEM_UNIT, port.getUnit().getId());
                        } catch (Exception ex) {
                            //do nothing
                        } finally {
                            arte.getTrace().clearContextStack();
                        }
                    }
                    if (Thread.currentThread().isInterrupted()) {
                        throw new InterruptedException();
                    }
                }
            }
        } finally {
            delTraceTarget(unitTraceTarget);
        }
    }

    private void delTraceTarget(final Object targetTraceHandle) {
        try {
            if (targetTraceHandle != null) {
                arte.getTrace().delTarget(targetTraceHandle);
            }
        } catch (Throwable e) {
            arte.getTrace().put(EEventSeverity.ERROR, "Can't delete trace target: " + arte.getTrace().exceptionStackToString(e), EEventSource.ARTE);
        }
    }

    protected abstract void checkClientCanTrace(String traceProfile) throws ServiceProcessClientFault;

    private void checkTraceContextIsOnlyUnit(final Unit unit) {
        final List<TraceContext> contextStack = arte.getTrace().getContextStack();
        boolean valid;
        if (contextStack == null || contextStack.size() != 1) {
            valid = false;
        } else {
            valid = EEventContextType.SYSTEM_UNIT.getValue().equals(contextStack.get(0).type) && (String.valueOf(unit.getId()).equals(contextStack.get(0).id));
        }
        if (!valid) {
            final String contextStackAsString = arte.getTrace().getContextStackAsStr();
            arte.getTrace().clearContextStack();
            arte.getTrace().put(EEventSeverity.WARNING, "Invalid trace context at the and of seance in ARTE # " + arte.getSeqNumber() + ": " + contextStackAsString, EEventSource.ARTE);
        }
    }

    private void rollbackAndEndTransaction() {
        try {
            arte.endTransaction(false);
        } catch (Throwable arteEx) {
            if (arte.getDbConnection() == null || arte.getDbConnection().getRadixConnection() == null || !arte.getDbConnection().getRadixConnection().isForciblyClosed()) {
                arte.getTrace().put(EEventSeverity.ALARM, "Can't do rollback and end transaction: " + arte.getTrace().exceptionStackToString(arteEx), EEventSource.ARTE);
            } else {
                final String reason = arte.getDbConnection().getRadixConnection().getForcedCloseReason();
                arte.getTrace().put(EEventSeverity.ERROR, "Arte database connection was forcibly closed" + (reason == null ? "" : " (" + reason + ")"), EEventSource.ARTE);
            }
        }
    }

    private final void sendFault(final ArteSocket port, final Throwable ex, final RequestTraceBuffer traceBuffer) {
        if (arte.needBreak()) {
            return;
        }
        if (ex instanceof ServiceProcessFault) {
            sendFault(port, (ServiceProcessFault) ex, traceBuffer);
        } else {
            sendFault(port, errorToFault(ex), traceBuffer);
        }
    }

    private final void sendFault(final ArteSocket port, final ServiceProcessFault flt, final RequestTraceBuffer trace) {
        try {
            if (!arte.needBreak()) {
                port.sendFault(flt, trace == null ? null : trace.asResponseTraceList());
            }
        } catch (Throwable portEx) {
            arte.getTrace().put(EEventSeverity.WARNING, "Can't send fault to terminal: " + arte.getTrace().exceptionStackToString(portEx) + "\nFault is: " + arte.getTrace().exceptionStackToString(flt), EEventSource.ARTE);
        }
    }

    protected abstract void prepare(ArteSocket port, XmlObject request, Map<String, String> headerAttrs) throws ServiceProcessFault, InterruptedException;

    protected abstract XmlObject processRequest(ArteSocket port, XmlObject request, Map<String, String> headerAttrs) throws ServiceProcessFault, InterruptedException;

    protected abstract void prepareResponseForSend(XmlObject request, XmlObject response, RequestTraceBuffer traceBuffer) throws ServiceProcessFault;

    protected abstract String getTraceProfile(XmlObject request) throws ServiceProcessFault;

    protected abstract boolean isLastRequest(XmlObject request) throws ServiceProcessFault;

    protected abstract boolean shouldCommit(XmlObject request) throws ServiceProcessFault;

    protected abstract ServiceProcessFault errorToFault(Throwable exception);

    protected abstract String getServiceWsdl();

    protected static final void checkInterrupted() throws InterruptedException {
        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }
    }

    public static class RequestTraceBuffer implements TraceBuffer {

        private static final long MAX_RQ_TRACE_BUF_SIZE = SystemPropUtils.getIntSystemProp("rdx.explorer.trace.buf.size", 200);
        private final List<TraceItem> data = new ArrayList<TraceItem>();
        private int limitCounter = 0;

        @Override
        public void put(final TraceItem item) {
            if (limitCounter < MAX_RQ_TRACE_BUF_SIZE) {
                data.add(item);
                limitCounter++;
            } else if (limitCounter == MAX_RQ_TRACE_BUF_SIZE) {
                data.add(new TraceItem(null, EEventSeverity.WARNING, null, new ArrStr("Request trace buffer size exceed " + String.valueOf(MAX_RQ_TRACE_BUF_SIZE) + ". Request trace is cut."), EEventSource.ARTE.getValue()));
                limitCounter++;
            }
        }

        public void resetLimit() {
            limitCounter = 0;
        }

        public List<TraceItem> asList() {
            return Collections.unmodifiableList(data);
        }

        synchronized public List<SoapFormatter.ResponseTraceItem> asResponseTraceList() {
            final List<SoapFormatter.ResponseTraceItem> list = new LinkedList<SoapFormatter.ResponseTraceItem>();
            for (TraceItem i : data) {
                list.add(new SoapFormatter.ResponseTraceItem(i.severity.getName(), i.toString()));
            }
            return Collections.unmodifiableList(list);
        }

        public void clear() {
            data.clear();
        }
    }

    private static class CopyToUnitTraceBuffer implements TraceBuffer {

        private static final boolean COPY_FILE_TRACE = SystemPropUtils.getBooleanSystemProp("rdx.copy.arte.file.trace.to.unit", false);

        private final Unit unit;
        private final TraceProfile arteFileTraceProfile;
        private final TraceProfile arteGuiTraceProfile;
        private final TraceProfile unitGuiTraceProfile;
        private final TraceProfile unitFileTraceProfile;

        public CopyToUnitTraceBuffer(Unit unit, Arte arte) {
            this.unit = unit;
            arteFileTraceProfile = new TraceProfile(arte.getArteTraceProfiles().getFileTraceProfile());
            if (SrvRunParams.getIsGuiOn()) {
                TraceProfile overridenArteGuiTraceProfile = null;
                if (arte.getArteSocket() instanceof ArteProcessor) {
                    final TraceProfiles processorTraceProfiles = ((ArteProcessor) arte.getArteSocket()).getTraceProfiles();
                    if (processorTraceProfiles != null) {
                        overridenArteGuiTraceProfile = new TraceProfile(processorTraceProfiles.getGuiTraceProfile());
                    }
                }
                if (overridenArteGuiTraceProfile != null) {
                    arteGuiTraceProfile = overridenArteGuiTraceProfile;
                } else {
                    arteGuiTraceProfile = new TraceProfile(arte.getArteTraceProfiles().getGuiTraceProfile());
                }
            } else {
                arteGuiTraceProfile = new TraceProfile(EEventSeverity.NONE.getName());
            }
            final TraceProfiles unitProfiles = unit.getTraceProfiles();
            unitGuiTraceProfile = unitProfiles != null && unitProfiles.getGuiTraceProfileObj() != null ? unitProfiles.getGuiTraceProfileObj() : new TraceProfile(EEventSeverity.NONE.getName());
            unitFileTraceProfile = unitProfiles != null && unitProfiles.getFileTraceProfileObj() != null ? unitProfiles.getFileTraceProfileObj() : new TraceProfile(EEventSeverity.NONE.getName());
        }

        @Override
        public void put(TraceItem item) {
            final Collection<ServerTrace.ETraceDestination> destinations = new ArrayList<>();
            if (COPY_FILE_TRACE && arteFileTraceProfile.itemMatch(item) && unitFileTraceProfile.itemMatch(item)) {
                destinations.add(ServerTrace.ETraceDestination.FILE);
            }
            if (arteGuiTraceProfile.itemMatch(item) && unitGuiTraceProfile.itemMatch(item)) {
                destinations.add(ServerTrace.ETraceDestination.GUI);
            }
            if (!destinations.isEmpty()) {
                unit.getTrace().put(
                        item.severity,
                        item.getMess(),
                        item.code,
                        item.words,
                        item.source,
                        item.time,
                        item.isSensitive,
                        destinations);
            }
        }
    }
}
