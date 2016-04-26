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
package org.radixware.kernel.server.instance;

import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.xml.namespace.QName;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;

import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.exceptions.ServiceProcessClientFault;
import org.radixware.kernel.common.exceptions.ServiceProcessFault;
import org.radixware.kernel.common.exceptions.ServiceProcessServerFault;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.common.utils.SoapFormatter;
import org.radixware.kernel.server.aio.EventDispatcher;
import org.radixware.kernel.server.aio.ServiceServer;
import org.radixware.kernel.server.exceptions.InvalidInstanceState;
import org.radixware.kernel.server.exceptions.InvalidUnitState;
import org.radixware.kernel.server.sap.Sap;
import org.radixware.kernel.server.trace.TraceBuffer;
import org.radixware.kernel.common.trace.TraceItem;
import org.radixware.kernel.server.SrvRunParams.UnableToLoadOptionsFromFile;
import org.radixware.kernel.server.SrvRunParams.ConfigFileNotSpecifiedException;
import org.radixware.kernel.server.SrvRunParams.OptionProcessingResult;
import org.radixware.kernel.server.aio.ServiceServerSeance;
import org.radixware.kernel.server.instance.arte.ArteInstance;
import org.radixware.kernel.server.units.Unit;
import org.radixware.kernel.server.units.UnitListener;
import org.radixware.kernel.server.units.UnitState;
import org.radixware.schemas.systeminstancecontrol.ActionStateEnum;
import org.radixware.schemas.systeminstancecontrol.ActualizeVerMess;
import org.radixware.schemas.systeminstancecontrol.ApplyConfigFileOptionsMess;
import org.radixware.schemas.systeminstancecontrol.ApplyConfigFileOptionsRs;
import org.radixware.schemas.systeminstancecontrol.ArteCommandMess;
import org.radixware.schemas.systeminstancecontrol.ExceptionEnum;
import org.radixware.schemas.systeminstancecontrol.FillArteTableMess;
import org.radixware.schemas.systeminstancecontrol.GetArteClassLoadingProfileMess;
import org.radixware.schemas.systeminstancecontrol.GetDiagnosticInfoMess;
import org.radixware.schemas.systeminstancecontrol.GetDiagnosticInfoRq;
import org.radixware.schemas.systeminstancecontrol.GetDiagnosticInfoRs;
import org.radixware.schemas.systeminstancecontrol.GetStatusMess;
import org.radixware.schemas.systeminstancecontrol.InstanceMaintenanceMess;
import org.radixware.schemas.systeminstancecontrol.MakeThreadDumpHtmlRq;
import org.radixware.schemas.systeminstancecontrol.MakeThreadDumpHtmlRs;
import org.radixware.schemas.systeminstancecontrol.MakeThreadDumpMess;
import org.radixware.schemas.systeminstancecontrol.ReloadArtePoolMess;
import org.radixware.schemas.systeminstancecontrol.Response;
import org.radixware.schemas.systeminstancecontrol.RestartAllUnitsMess;
import org.radixware.schemas.systeminstancecontrol.RestartUnitMess;
import org.radixware.schemas.systeminstancecontrol.SimpleResponse;
import org.radixware.schemas.systeminstancecontrol.StartAllUnitsMess;
import org.radixware.schemas.systeminstancecontrol.StartUnitMess;
import org.radixware.schemas.systeminstancecontrol.StopAllUnitsMess;
import org.radixware.schemas.systeminstancecontrol.StopUnitMess;
import org.radixware.schemas.systeminstancecontrol.TraceLevelEnum;
import org.radixware.schemas.systeminstancecontrol.UnitCommandMess;
import org.radixware.schemas.systeminstancecontrol.UnitRequest;
import org.radixware.schemas.systeminstancecontrolWsdl.ActualizeVerDocument;
import org.radixware.schemas.systeminstancecontrolWsdl.ApplyConfigFileOptionsDocument;
import org.radixware.schemas.systeminstancecontrolWsdl.ArteCommandDocument;
import org.radixware.schemas.systeminstancecontrolWsdl.FillArteTableDocument;
import org.radixware.schemas.systeminstancecontrolWsdl.GetArteClassLoadingProfileDocument;
import org.radixware.schemas.systeminstancecontrolWsdl.GetDiagnosticInfoDocument;
import org.radixware.schemas.systeminstancecontrolWsdl.GetStatusDocument;
import org.radixware.schemas.systeminstancecontrolWsdl.InstanceMaintenanceDocument;
import org.radixware.schemas.systeminstancecontrolWsdl.MakeThreadDumpDocument;
import org.radixware.schemas.systeminstancecontrolWsdl.ReloadArtePoolDocument;
import org.radixware.schemas.systeminstancecontrolWsdl.RestartAllUnitsDocument;
import org.radixware.schemas.systeminstancecontrolWsdl.RestartUnitDocument;
import org.radixware.schemas.systeminstancecontrolWsdl.StartAllUnitsDocument;
import org.radixware.schemas.systeminstancecontrolWsdl.StartUnitDocument;
import org.radixware.schemas.systeminstancecontrolWsdl.StopAllUnitsDocument;
import org.radixware.schemas.systeminstancecontrolWsdl.StopUnitDocument;
import org.radixware.schemas.systeminstancecontrolWsdl.UnitCommandDocument;

/**
 * Класс, реализующий сервис удаленного управления инстанцией.
 *
 *
 *
 */
final class InstanceRcSap extends Sap {

    public static final String SERVICE_WSDL = "http://schemas.radixware.org/systeminstancecommand.wsdl";
    public static final String SERVICE_XSD = "http://schemas.radixware.org/systeminstancecommand.xsd";
    private static final QName traceProfileQName = new QName(SERVICE_XSD, "TraceProfile");
    //
    private final Instance instance;
    private final Map<UnitCommand, ServiceServerSeance> seanceByUnitCommand = new HashMap<>();

    public InstanceRcSap(final Instance instance, final EventDispatcher dispatcher) {
        super(
                dispatcher,
                instance.getTrace().newTracer(EEventSource.INSTANCE_SERVICE.getValue()),
                10, //maxSeanceCount
                30 //rqWaitTimeout
        );
        this.instance = instance;
    }

    public void respondToUnitCommands(final List<UnitCommandResponse> responses) {
        if (responses != null) {
            for (UnitCommandResponse response : responses) {
                final ServiceServerSeance seance = seanceByUnitCommand.remove(response.getRequestCommand());
                if (seance != null && seance.isConnected()) {
                    if (response.getException() != null) {
                        seance.response(errorToFault(response.getException()), false);
                    } else {
                        seance.response(createUnitCommandResponseDoc(response.getResponse()), false);
                    }
                } else {
                    instance.getTrace().put(EEventSeverity.WARNING, "Unable to send response for unit command (no connected seance): " + response.asStr(), EEventSource.INSTANCE_SERVICE);
                }
            }
        }
        final List<UnitCommand> toRemove = new ArrayList<>();
        for (Map.Entry<UnitCommand, ServiceServerSeance> entry : seanceByUnitCommand.entrySet()) {
            if (!entry.getValue().isConnected()) {
                toRemove.add(entry.getKey());
            }
        }
        for (UnitCommand itemToRemove : toRemove) {
            seanceByUnitCommand.remove(itemToRemove);
        }
    }

    private XmlObject createUnitCommandResponseDoc(final XmlObject responseContent) {
        UnitCommandDocument xDoc = UnitCommandDocument.Factory.newInstance();
        xDoc.ensureUnitCommand().ensureUnitCommandRs().setResult(ActionStateEnum.DONE);
        if (responseContent != null) {
            xDoc.ensureUnitCommand().ensureUnitCommandRs().setResponse(responseContent);
        }
        return xDoc;
    }

    private String tryToExctractUser(final XmlObject xmlObject) {
        if (xmlObject == null) {
            return null;
        }
        try {
            final XmlObject rqPart = SoapFormatter.getInnerContent(xmlObject);
            final Method m = rqPart.getClass().getMethod("getUser");
            return m.invoke(rqPart).toString();
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    protected void process(final ServiceServer.InvocationEvent event) {
        final RequestTraceBuffer traceBuffer = new RequestTraceBuffer();
        try {
            final String traceProfile = getTraceProfile(event.rqEnvBodyContent);
            final Object traceTargetHandler = instance.getTrace().addTargetBuffer(traceProfile, traceBuffer);

            String userSuffix = "";
            final String user = tryToExctractUser(event.rqEnvBodyContent);
            if (user != null && !user.isEmpty()) {
                userSuffix = " (" + user + ")";
            }

            final String reason = Messages.INSTANCE_RC_SAP_COMMAND + userSuffix;

            try {
                final XmlObject rsDoc;
                final Response rs;
                ActionStateEnum.Enum actionResult = ActionStateEnum.FAILED;
                String resultComment = null;
                if (event.rqEnvBodyContent instanceof StartAllUnitsMess) {
                    final StartAllUnitsDocument startDoc = StartAllUnitsDocument.Factory.newInstance();
                    rsDoc = startDoc;
                    rs = startDoc.addNewStartAllUnits().addNewStartAllUnitsRs();
                    try {
                        instance.startAllUnits(reason);
                        actionResult = ActionStateEnum.DONE;
                    } catch (InvalidInstanceState e) {
                        final String exceptionStack = ExceptionTextFormatter.exceptionStackToString(e);
                        throw new ServiceProcessClientFault(ExceptionEnum.WRONG_STATE.toString(), String.valueOf(e.getMessage()), e, exceptionStack);
                    } catch (Throwable e) {
                        if (e instanceof InterruptedException || Thread.currentThread().isInterrupted()) {
                            throw new InterruptedException();
                        }
                        final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
                        instance.getTrace().put(EEventSeverity.ERROR, Messages.ERR_ON_UNITS_START + exStack, Messages.MLS_ID_ERR_ON_UNITS_START, new ArrStr(instance.getFullTitle(), exStack), EEventSource.INSTANCE_SERVICE, false);
                        throw new ServiceProcessServerFault(ExceptionEnum.SERVER_EXCEPTION.toString(), Messages.ERR_ON_INSTANCE_STOP + ExceptionTextFormatter.getExceptionMess(e), e, exStack);
                    }
                } else if (event.rqEnvBodyContent instanceof RestartAllUnitsMess) {
                    final RestartAllUnitsDocument restartDoc = RestartAllUnitsDocument.Factory.newInstance();
                    rsDoc = restartDoc;
                    rs = restartDoc.addNewRestartAllUnits().addNewRestartAllUnitsRs();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                instance.restartAllUnits(reason);
                            } catch (InvalidInstanceState e) {
                                final String exceptionStack = ExceptionTextFormatter.exceptionStackToString(e);
                                throw new ServiceProcessClientFault(ExceptionEnum.WRONG_STATE.toString(), String.valueOf(e.getMessage()), e, exceptionStack);
                            } catch (Throwable e) {
                                final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
                                instance.getTrace().put(EEventSeverity.ERROR, Messages.ERR_ON_UNITS_RESTART + exStack, Messages.MLS_ID_ERR_ON_UNITS_RESTART, new ArrStr(instance.getFullTitle(), exStack), EEventSource.INSTANCE_SERVICE, false);
                                throw new ServiceProcessServerFault(ExceptionEnum.SERVER_EXCEPTION.toString(), Messages.ERR_ON_INSTANCE_STOP + ExceptionTextFormatter.getExceptionMess(e), e, exStack);
                            }
                        }
                    }, "InstanceRcService.restartAllUnits").start();
                    actionResult = ActionStateEnum.SCHEDULED;
                } else if (event.rqEnvBodyContent instanceof StopAllUnitsMess) {
                    final StopAllUnitsDocument stopDoc = StopAllUnitsDocument.Factory.newInstance();
                    rsDoc = stopDoc;
                    rs = stopDoc.addNewStopAllUnits().addNewStopAllUnitsRs();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                instance.stopAllUnits(reason);
                            } catch (InvalidInstanceState e) {
                                final String exceptionStack = ExceptionTextFormatter.exceptionStackToString(e);
                                throw new ServiceProcessClientFault(ExceptionEnum.WRONG_STATE.toString(), String.valueOf(e.getMessage()), e, exceptionStack);
                            } catch (Throwable e) {
                                final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
                                instance.getTrace().put(EEventSeverity.ERROR, Messages.ERR_ON_UNITS_STOP + exStack, Messages.MLS_ID_ERR_ON_UNITS_STOP, new ArrStr(instance.getFullTitle(), exStack), EEventSource.INSTANCE_SERVICE, false);
                                throw new ServiceProcessServerFault(ExceptionEnum.SERVER_EXCEPTION.toString(), Messages.ERR_ON_INSTANCE_STOP + ExceptionTextFormatter.getExceptionMess(e), e, exStack);
                            }
                        }
                    }, "InstanceRcService.stopAllUnits").start();
                    actionResult = ActionStateEnum.SCHEDULED;
                } else if (event.rqEnvBodyContent instanceof StartUnitMess || event.rqEnvBodyContent instanceof RestartUnitMess || event.rqEnvBodyContent instanceof StopUnitMess) {
                    final UnitRequest rq;
                    if (event.rqEnvBodyContent instanceof StartUnitMess) {
                        rq = ((StartUnitMess) event.rqEnvBodyContent).getStartUnitRq();
                        final StartUnitDocument startUnitDoc = StartUnitDocument.Factory.newInstance();
                        rsDoc = startUnitDoc;
                        rs = startUnitDoc.addNewStartUnit().addNewStartUnitRs();
                    } else if (event.rqEnvBodyContent instanceof RestartUnitMess) {
                        rq = ((RestartUnitMess) event.rqEnvBodyContent).getRestartUnitRq();
                        final RestartUnitDocument restartUnitDoc = RestartUnitDocument.Factory.newInstance();
                        rsDoc = restartUnitDoc;
                        rs = restartUnitDoc.addNewRestartUnit().addNewRestartUnitRs();
                    } else { //if (event.rqEnvBodyContent instanceof StopUnitMess) {
                        rq = ((StopUnitMess) event.rqEnvBodyContent).getStopUnitRq();
                        final StopUnitDocument stopUnitDoc = StopUnitDocument.Factory.newInstance();
                        rsDoc = stopUnitDoc;
                        rs = stopUnitDoc.addNewStopUnit().addNewStopUnitRs();
                    }
                    final int unitId = rq.getUnitId();
                    Unit loadedUnit;
                    try {
                        loadedUnit = instance.findUnit(unitId);
                    } catch (InvalidInstanceState e) {
                        final String exceptionStack = ExceptionTextFormatter.exceptionStackToString(e);
                        throw new ServiceProcessClientFault(ExceptionEnum.WRONG_STATE.toString(), e.getMessage(), e, exceptionStack);
                    }
                    if (loadedUnit == null && (event.rqEnvBodyContent instanceof StartUnitMess)) //May be it is a new unit. Let's try to load it.
                    {
                        loadedUnit = instance.tryToLoadUnit(unitId);
                    }
                    if (loadedUnit == null) {
                        throw new ServiceProcessClientFault(ExceptionEnum.UNIT_NOT_FOUND.toString(), Messages.UNIT + " #" + String.valueOf(unitId) + " " + Messages._NOT_FOUND, null, null);
                    }
                    final Unit unit = loadedUnit;
                    final Object unitTraceHandler = unit.getTrace().addTargetBuffer(traceProfile, traceBuffer);
                    try {
                        if (event.rqEnvBodyContent instanceof StartUnitMess) {
                            try {
                                final CountDownLatch operationCompletedLatch = new CountDownLatch(1);
                                if (instance.startUnit(unit, reason)) {

                                    unit.registerListener(new UnitListener() {
                                        @Override
                                        public void stateChanged(Unit unit, UnitState oldState, UnitState newState) {
                                            if (newState == UnitState.STARTED | newState == UnitState.STOPPED | newState == UnitState.START_POSTPONED) {
                                                unit.unregisterListener(this);
                                                operationCompletedLatch.countDown();
                                            }
                                        }
                                    });
                                } else {
                                    operationCompletedLatch.countDown();
                                }
                                operationCompletedLatch.await(10, TimeUnit.SECONDS);
                                if (unit.getState() == UnitState.STARTED) {
                                    actionResult = ActionStateEnum.DONE;
                                } else if (unit.getState() == UnitState.STARTING) {
                                    actionResult = ActionStateEnum.SCHEDULED;
                                } else if (unit.getState() == UnitState.START_POSTPONED) {
                                    actionResult = ActionStateEnum.POSTPONED;
                                    resultComment = unit.getLastPostponeStartReason();
                                } else {
                                    actionResult = ActionStateEnum.FAILED;
                                }
                            } catch (InvalidUnitState e) {
                                throw e;
                            } catch (Throwable e) {
                                if (e instanceof InterruptedException || Thread.currentThread().isInterrupted()) {
                                    throw new InterruptedException();
                                }
                                final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
                                instance.getTrace().put(EEventSeverity.ERROR, Messages.ERR_ON_UNIT_START + " \"" + unit.getFullTitle() + "\": \n" + exStack, Messages.MLS_ID_ERR_ON_UNIT_START, new ArrStr("\"" + unit.getFullTitle() + "\"", exStack), EEventSource.INSTANCE_SERVICE, false);
                                throw new ServiceProcessServerFault(ExceptionEnum.SERVER_EXCEPTION.toString(), Messages.ERR_ON_UNIT_START + ExceptionTextFormatter.getExceptionMess(e), e, exStack);
                            }
                        } else if (event.rqEnvBodyContent instanceof RestartUnitMess) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        if (instance.stopUnit(unit, reason)) {
                                            instance.startUnit(unit, reason);
                                        }
                                    } catch (InvalidUnitState e) {
                                        throw e;
                                    } catch (Throwable e) {
                                        final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
                                        instance.getTrace().put(EEventSeverity.ERROR, Messages.ERR_ON_UNIT_RESTART + " \"" + unit.getFullTitle() + "\": \n" + exStack, Messages.MLS_ID_ERR_ON_UNIT_RESTART, new ArrStr("\"" + unit.getFullTitle() + "\"", exStack), EEventSource.INSTANCE_SERVICE, false);
                                        throw new ServiceProcessServerFault(ExceptionEnum.SERVER_EXCEPTION.toString(), Messages.ERR_ON_UNIT_RESTART + " \"" + unit.getFullTitle() + "\": \n" + ExceptionTextFormatter.getExceptionMess(e), e, exStack);
                                    }
                                }
                            }, "InstanceRcService.restartUnit #" + String.valueOf(unit.getId())).start();
                            actionResult = ActionStateEnum.SCHEDULED;
                        } else { //if (event.rqEnvBodyContent instanceof StopUnitMess) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        instance.stopUnit(unit, reason);
                                    } catch (InvalidUnitState e) {
                                        throw e;
                                    } catch (Throwable e) {
                                        final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
                                        instance.getTrace().put(EEventSeverity.ERROR, Messages.ERR_ON_UNIT_STOP + " \"" + unit.getFullTitle() + "\": \n" + exStack, Messages.MLS_ID_ERR_ON_UNIT_STOP, new ArrStr("\"" + unit.getFullTitle() + "\"", exStack), EEventSource.INSTANCE_SERVICE, false);
                                        throw new ServiceProcessServerFault(ExceptionEnum.SERVER_EXCEPTION.toString(), Messages.ERR_ON_UNIT_STOP + " \"" + unit.getFullTitle() + "\": \n" + ExceptionTextFormatter.getExceptionMess(e), e, exStack);
                                    }
                                }
                            }, "InstanceRcService.stopUnit #" + String.valueOf(unit.getId())).start();
                            actionResult = ActionStateEnum.SCHEDULED;
                        }
                    } catch (InvalidUnitState e) {
                        throw new ServiceProcessClientFault(ExceptionEnum.WRONG_STATE.toString(), String.valueOf(e.getMessage()), e, null);
                    } finally {
                        try {
                            unit.getTrace().delTarget(unitTraceHandler);
                        } catch (Throwable e) {
                            if (e instanceof InterruptedException || Thread.currentThread().isInterrupted()) {
                                throw new InterruptedException();
                            }
                            throw new RadixError("Can't delete trace target: " + ExceptionTextFormatter.getExceptionMess(e), e);
                        }
                    }
                } else if (event.rqEnvBodyContent instanceof ApplyConfigFileOptionsMess) {
                    final List<String> optionsToApply = new LinkedList<String>();
                    for (Object rqStringObj : ((ApplyConfigFileOptionsMess) event.rqEnvBodyContent).getApplyConfigFileOptionsRq().getOptions()) {
                        optionsToApply.add(rqStringObj.toString());
                    }

                    try {
                        final List<OptionProcessingResult> processingResults = instance.applyConfigFileOptions(optionsToApply);
                        final ApplyConfigFileOptionsDocument applyDoc = ApplyConfigFileOptionsDocument.Factory.newInstance();
                        rsDoc = applyDoc;
                        final ApplyConfigFileOptionsRs response = applyDoc.addNewApplyConfigFileOptions().addNewApplyConfigFileOptionsRs();
                        rs = response;
                        final ApplyConfigFileOptionsRs.Results responseResults = response.addNewResults();
                        for (OptionProcessingResult result : processingResults) {
                            final ApplyConfigFileOptionsRs.Results.Item responseItem = responseResults.addNewItem();
                            responseItem.setOption(result.getOptionName());
                            responseItem.setResult(result.isSucceed() ? ActionStateEnum.DONE : ActionStateEnum.FAILED);
                            responseItem.setResultComment(result.getComment());
                        }
                    } catch (ConfigFileNotSpecifiedException ex) {
                        throw new ServiceProcessServerFault(ExceptionEnum.CONFIG_FILE_NOT_SPECIFIED.toString(), "Config file was not specified at server startup", ex, null);
                    } catch (UnableToLoadOptionsFromFile ex) {
                        throw new ServiceProcessServerFault(ExceptionEnum.UNABLE_TO_LOAD_OPTIONS.toString(), "Unable to load options from file", ex, null);
                    }
                } else if (event.rqEnvBodyContent instanceof ActualizeVerMess) {
                    instance.scheduleActualizeVer();
                    final ActualizeVerDocument actualizeDoc = ActualizeVerDocument.Factory.newInstance();
                    rsDoc = actualizeDoc;
                    rs = actualizeDoc.addNewActualizeVer().addNewActualizeVerRs();
                    actionResult = ActionStateEnum.SCHEDULED;
                } else if (event.rqEnvBodyContent instanceof ReloadArtePoolMess) {
                    instance.requestArtePoolReload(reason);
                    final ReloadArtePoolDocument reloadDoc = ReloadArtePoolDocument.Factory.newInstance();
                    rsDoc = reloadDoc;
                    rs = reloadDoc.addNewReloadArtePool().addNewReloadArtePoolRs();
                    actionResult = ActionStateEnum.SCHEDULED;
                } else if (event.rqEnvBodyContent instanceof InstanceMaintenanceMess) {
                    instance.requestMaintenance(reason);
                    final InstanceMaintenanceDocument maintDoc = InstanceMaintenanceDocument.Factory.newInstance();
                    rsDoc = maintDoc;
                    rs = maintDoc.addNewInstanceMaintenance().addNewInstanceMaintenanceRs();
                    actionResult = ActionStateEnum.SCHEDULED;
                } else if (event.rqEnvBodyContent instanceof MakeThreadDumpMess) {
                    final MakeThreadDumpDocument doc = MakeThreadDumpDocument.Factory.newInstance();
                    rsDoc = doc;
                    rs = doc.addNewMakeThreadDump().addNewMakeThreadDumpRs();
                    if (instance.getThreadDumpWriter().requestDump(ThreadDumpWriter.EThreadDumpReason.USER_REQUEST)) {
                        actionResult = ActionStateEnum.SCHEDULED;
                    } else {
                        actionResult = ActionStateEnum.FAILED;
                        resultComment = "Requests are too often or '-tdumpStoreDays' is set to 0";
                    }
                } else if (event.rqEnvBodyContent instanceof UnitCommandMess) {
                    final UnitCommandMess xMess = ((UnitCommandMess) event.rqEnvBodyContent);
                    final Unit targetUnit = instance.findUnit(xMess.getUnitCommandRq().getUnitId());
                    if (targetUnit == null) {
                        throw new IllegalArgumentException("Unit #" + xMess.getUnitCommandRq().getUnitId() + " not found");
                    }
                    final UnitCommand unitCommand = new UnitCommand(xMess.getUnitCommandRq().getRequest(), targetUnit, System.currentTimeMillis());
                    if (!targetUnit.enqueueUnitCommand(unitCommand)) {
                        throw new RuntimeException("Unit is overflowed by commands");
                    } else {
                        instance.getTrace().debug("Command " + unitCommand.getTraceId() + " enqueued to unit commands queue", EEventSource.INSTANCE, false);
                    }
                    seanceByUnitCommand.put(unitCommand, event.seance);
                    rsDoc = null;
                    rs = null;
                } else if (event.rqEnvBodyContent instanceof FillArteTableMess) {
                    final FillArteTableDocument doc = FillArteTableDocument.Factory.newInstance();
                    rsDoc = doc;
                    rs = doc.addNewFillArteTable().addNewFillArteTableRs();

                    ArteStateWriter.UpdateRequestHandle handle = instance.getArteStateWriter().requestUpdate();
                    if (handle.await(2000)) {
                        actionResult = ActionStateEnum.DONE;
                    } else {
                        actionResult = ActionStateEnum.FAILED;
                        resultComment = "Error during process request.";
                    }
                } else if (event.rqEnvBodyContent instanceof GetDiagnosticInfoMess) {
                    GetDiagnosticInfoRq rq = ((GetDiagnosticInfoMess) event.rqEnvBodyContent).getGetDiagnosticInfoRq();

                    final GetDiagnosticInfoDocument doc = GetDiagnosticInfoDocument.Factory.newInstance();
                    rsDoc = doc;
                    rs = doc.addNewGetDiagnosticInfo().addNewGetDiagnosticInfoRs();
                    ((GetDiagnosticInfoRs) rs).setDiagnosticInfoStr(DiagnosticInfoUtils.getDiagnosticSnapshot());

                    actionResult = ActionStateEnum.DONE;
                } else if (event.rqEnvBodyContent instanceof ArteCommandMess) {
                    ArteCommandMess xMess = ((ArteCommandMess) event.rqEnvBodyContent);
                    final long serial = xMess.getArteSerial();
                    ArteInstance arteInst = instance.getArtePool().findArteInstBySerial(serial);
                    if (arteInst == null) {
                        actionResult = ActionStateEnum.FAILED;
                        resultComment = "Can't find ARTE instance with serial: " + serial;
                    }
                    ArteCommandDocument arteDoc = ArteCommandDocument.Factory.newInstance();

                    if (xMess.isSetInterruptArteMess()) {
                        if (arteInst != null) {
                            arteInst.interruptArte();
                            actionResult = ActionStateEnum.DONE;
                        }
                        rsDoc = arteDoc;
                        rs = arteDoc.addNewArteCommand().addNewInterruptArteMess().addNewInterruptArteRs();
                    } else if (xMess.isSetCloseDbConnMess()) {
                        if (arteInst != null) {
                            try {
                                arteInst.closeDbConnectionForcibly(reason);
                                actionResult = ActionStateEnum.DONE;
                            } catch (SQLException ex) {
                                actionResult = ActionStateEnum.FAILED;
                                resultComment = ex.getMessage();
                            }
                        }
                        rsDoc = arteDoc;
                        rs = arteDoc.addNewArteCommand().addNewCloseDbConnMess().addNewCloseDbConnRs();
                    } else if (xMess.isSetMakeThreadDumpHtmlMess()) {
                        MakeThreadDumpHtmlRq rq = xMess.getMakeThreadDumpHtmlMess().getMakeThreadDumpHtmlRq();
                        String arteDumpStr = "";
                        if (arteInst != null) {
                            arteDumpStr = DiagnosticInfoUtils.getArteInstSnapshot(arteInst);
                            actionResult = ActionStateEnum.DONE;
                        }
                        rsDoc = arteDoc;
                        rs = arteDoc.addNewArteCommand().addNewMakeThreadDumpHtmlMess().addNewMakeThreadDumpHtmlRs();
                        ((MakeThreadDumpHtmlRs) rs).setThreadDumpStr(arteDumpStr);
                    } else {
                        rsDoc = null;
                        rs = null;
                    }
                } else if (event.rqEnvBodyContent instanceof GetStatusMess) {
                    final GetStatusDocument doc = GetStatusDocument.Factory.newInstance();
                    rsDoc = doc;
                    rs = doc.ensureGetStatus().ensureGetStatusRs();
                    final long sensitiveTraceEndTimeMillis = Instance.get().getSensitiveTracingFinishMillis();
                    if (sensitiveTraceEndTimeMillis > System.currentTimeMillis()) {
                        final GregorianCalendar calendar = new GregorianCalendar();
                        calendar.setTimeInMillis(sensitiveTraceEndTimeMillis);
                        doc.ensureGetStatus().ensureGetStatusRs().setSensitiveDataTracingFinishTime(calendar);
                    }
                    actionResult = ActionStateEnum.DONE;
                } else if (event.rqEnvBodyContent instanceof GetArteClassLoadingProfileMess) {
                    final GetArteClassLoadingProfileDocument doc = GetArteClassLoadingProfileDocument.Factory.newInstance();
                    rsDoc = doc;
                    rs = doc.ensureGetArteClassLoadingProfile().ensureGetArteClassLoadingProfileRs();
                    for (String className : instance.getArteLoadedClasses()) {
                        doc.ensureGetArteClassLoadingProfile().ensureGetArteClassLoadingProfileRs().addItem(className);
                    }
                    actionResult = ActionStateEnum.DONE;
                } else {
                    final XmlObject rq = event.rqEnvBodyContent;
                    if (instance.getTrace().getMinSeverity() == EEventSeverity.DEBUG.getValue().longValue()) {
                        final String dbgMess
                                = "InstanceRcSap received wrong request: " + rq.getDomNode().getLocalName() + "\n"
                                + "Request class: " + rq.getClass().getName() + ", classLoader: " + rq.getClass().getClassLoader().toString() + "\n"
                                + "InstanceRcSap requests' classLoader: " + StartAllUnitsMess.class.getClassLoader().toString() + "\n"
                                + "Thread's context classLoader: " + Thread.currentThread().getContextClassLoader();
                        instance.getTrace().debug(dbgMess, EEventSource.INSTANCE_SERVICE, false);
                    }
                    throw new ServiceProcessClientFault(ExceptionEnum.INVALID_REQUEST.toString(), "Request \"" + rq.getClass().getName() + "\" is not supported \nby \"" + SERVICE_WSDL + "\" service", null, null);
                }
                if (rsDoc != null) {
                    if (rs instanceof SimpleResponse) {
                        ((SimpleResponse) rs).setResult(actionResult);
                        if (resultComment != null && !resultComment.isEmpty()) {
                            ((SimpleResponse) rs).setResultComment(resultComment);
                        }
                    }
                    writeTrace(rs, traceBuffer);
                    event.seance.response(rsDoc, false);
                }
            } finally {
                try {
                    instance.getTrace().delTarget(traceTargetHandler);
                } catch (Throwable e) {
                    if (e instanceof InterruptedException || Thread.currentThread().isInterrupted()) {
                        throw new InterruptedException();
                    }
                    throw new RadixError("Can't delete trace target: " + ExceptionTextFormatter.getExceptionMess(e), e);
                }
            }
        } catch (InterruptedException e) {
            return;
        } catch (Throwable e) {
            event.seance.response(errorToFault(e), false, traceBuffer.asResponseTraceList());
        }
    }

    private ServiceProcessFault errorToFault(Throwable exception) {
        if (exception instanceof ServiceProcessFault) {
            return (ServiceProcessFault) exception;
        }
        final String reason;
        if (exception instanceof RuntimeException) {
            reason = ExceptionEnum.SERVER_MALFUNCTION.toString();
        } else {
            reason = ExceptionEnum.SERVER_EXCEPTION.toString();
        }
        final String exStack = ExceptionTextFormatter.exceptionStackToString(exception);
        instance.getTrace().put(EEventSeverity.WARNING, Messages.ERR_UNHANDLED_IN_SERVICE + " \"" + SERVICE_WSDL + "\":\n" + exStack, Messages.MLS_ID_ERR_UNHANDLED_IN_SERVICE, new ArrStr(SERVICE_WSDL, exStack), EEventSource.INSTANCE_SERVICE, false);
        return new ServiceProcessServerFault(reason, "Unhandled " + exStack, exception, exStack);
    }

    private String getTraceProfile(final XmlObject request) throws ServiceProcessClientFault {
        final XmlCursor rqCursor = request.newCursor();
        try {
            if (rqCursor.toFirstChild()) {
                final XmlObject[] traceProfile = rqCursor.getObject().selectChildren(traceProfileQName);
                if (traceProfile != null && traceProfile.length > 0) {
                    final XmlCursor trCursor = traceProfile[0].newCursor();
                    try {
                        trCursor.toFirstChild();
                        return trCursor.getTextValue();
                    } finally {
                        trCursor.dispose();
                    }
                }
            }
            return null;
        } finally {
            rqCursor.dispose();
        }
    }

    private static void writeTrace(final Response response, final RequestTraceBuffer traceBuffer) throws ServiceProcessFault {
        final List<TraceItem> trace = traceBuffer.asList();
        if (!trace.isEmpty()) {
            final org.radixware.schemas.systeminstancecontrol.Trace traceXml = response.addNewTrace();
            org.radixware.schemas.systeminstancecontrol.Trace.Item itemXml;
            for (TraceItem item : trace) {
                itemXml = traceXml.addNewItem();
                if (item.severity.equals(EEventSeverity.ALARM)) {
                    itemXml.setLevel(TraceLevelEnum.ALARM);
                } else if (item.severity.equals(EEventSeverity.ERROR)) {
                    itemXml.setLevel(TraceLevelEnum.ERROR);
                } else if (item.severity.equals(EEventSeverity.WARNING)) {
                    itemXml.setLevel(TraceLevelEnum.WARNING);
                } else if (item.severity.equals(EEventSeverity.EVENT)) {
                    itemXml.setLevel(TraceLevelEnum.EVENT);
                } else {
                    itemXml.setLevel(TraceLevelEnum.DEBUG);
                }
                itemXml.setStringValue(item.toString());
            }
        }
    }

    private static final class RequestTraceBuffer implements TraceBuffer {

        private final List<TraceItem> data = new LinkedList<TraceItem>();

        @Override
        synchronized public void put(final TraceItem item) {
            data.add(item);
        }

        @SuppressWarnings("unchecked")
        synchronized public List<SoapFormatter.ResponseTraceItem> asResponseTraceList() {
            final List<SoapFormatter.ResponseTraceItem> list = new LinkedList<SoapFormatter.ResponseTraceItem>();
            for (TraceItem i : data) {
                list.add(new SoapFormatter.ResponseTraceItem(i.severity.getName(), i.toString()));
            }
            return Collections.unmodifiableList(list);
        }

        synchronized public List<TraceItem> asList() {
            return Collections.unmodifiableList(data);
        }

        synchronized public void clear() {
            data.clear();
        }
    }

    @Override
    protected boolean isShuttingDown() {
        return instance.isShuttingDown();
    }

    @Override
    protected void restoreDbConnection() throws InterruptedException {
        instance.restoreDbConnection();
    }

    @Override
    public long getId() {
        return instance.getControlServiceSapId();
    }
}
