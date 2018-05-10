/*
 * Copyright (c) 2008-2017, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. This Source Code is distributed
 * WITHOUT ANY WARRANTY; including any implied warranties but not limited to
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Mozilla Public License, v. 2.0. for more details.
 */
package org.radixware.kernel.server.sap;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import javax.net.ssl.SSLContext;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EPortSecurityProtocol;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.exceptions.ServiceProcessFault;
import org.radixware.kernel.common.exceptions.ServiceProcessServerFault;
import org.radixware.kernel.common.ssl.CertificateUtils;
import org.radixware.kernel.common.trace.LocalTracer;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.common.utils.SoapFormatter;
import org.radixware.kernel.server.aio.Event;
import org.radixware.kernel.server.aio.EventDispatcher;
import org.radixware.kernel.server.aio.EventHandler;
import org.radixware.kernel.server.aio.ServiceServer;
import org.radixware.kernel.server.aio.ServiceServer.InvocationEvent;
import org.radixware.kernel.server.exceptions.DatabaseError;
import org.radixware.kernel.common.cache.ObjectCache;
import org.radixware.kernel.server.instance.Instance;
import org.radixware.kernel.server.instance.aadc.AadcManager;
import org.radixware.kernel.server.soap.DefaultServerMessageProcessorFactory;
import org.radixware.schemas.systeminstancecontrol.ExceptionEnum;

/**
 * Базовый класс для классов реализующих service access point.
 *
 *
 */
public abstract class Sap implements EventHandler {

    private final EventDispatcher dispatcher;
    protected final LocalTracer tracer;
    private final SapDbQueries dbQueries;
    private SapOptions options = null;
    private ServiceServer service = null;
    private java.sql.Connection dbConnection = null;
    private final int maxSeanceCount;
    private final int rqWaitTimeout;
    private final ObjectCache cache;
    private final String resourceKeyPefix;
    private final AadcManager aadcManager;

    public Sap(final EventDispatcher dispatcher, final LocalTracer tracer, final int maxSeanceCount, final int rqWaitTimeout, final String resourceKeyPrefix) {
        this.dispatcher = dispatcher;
        this.tracer = tracer;
        this.dbQueries = new SapDbQueries(this);
        this.maxSeanceCount = maxSeanceCount;
        this.rqWaitTimeout = rqWaitTimeout;
        this.cache = new ObjectCache();
        this.resourceKeyPefix = resourceKeyPrefix;
        final Instance curInst = Instance.get();
        if (curInst != null) {
            aadcManager = curInst.getAadcManager();
        } else {
            aadcManager = null;
        }
    }

    public String getResourceKeyPefix() {
        return resourceKeyPefix;
    }

    public boolean start(final java.sql.Connection dbConnection) throws InterruptedException {
        setDbConnection(dbConnection);
        try {
            options = readOptions();
        } catch (SQLException e) {
            throw new DatabaseError(Messages.ERR_CANT_READ_SERVICE_OPTIONS + ": " + ExceptionTextFormatter.exceptionStackToString(e), e);
        }
        if (options == null) {
            throw new RadixError(Messages.ERR_CANT_READ_SERVICE_OPTIONS);
        }
        return start(dbConnection, options);
    }

    private boolean start(final java.sql.Connection dbConnection, final SapOptions options) throws InterruptedException {
        setDbConnection(dbConnection);
        this.options = options;
        SSLContext sslContext = null;
        if (options.getSecurityProtocol().isTls()) {
            try {
                sslContext = CertificateUtils.prepareServerSslContext(options.getServerKeyAliases(), options.getClientCertAliases());
            } catch (Throwable e) {
                final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
                tracer.put(EEventSeverity.ERROR, Messages.ERR_ON_CREATE_SSL_CONTEXT + ": " + e.getMessage() + "\n" + exStack, null, null, false);
                return false;
            }
        }
        service = new ServiceServer(
                dispatcher,
                tracer, options.getAddress(),
                maxSeanceCount,
                rqWaitTimeout,
                sslContext,
                options.getSecurityProtocol(),
                options.getCheckClientCert(),
                options.getCipherSuites(),
                new DefaultServerMessageProcessorFactory(options, tracer, cache),
                resourceKeyPefix);
        try {
            service.start();
        } catch (Throwable e) {
            final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
            final String o = options.toString();
            tracer.put(EEventSeverity.ERROR, Messages.ERR_ON_SERVICE_START + " " + o + ": " + exStack, Messages.MLS_ID_ERR_ON_SERVICE_START, new ArrStr(o, exStack), false);
            service.stop();
            service = null;
        }
        if (isStarted()) {
            dispatcher.waitEvent(new ServiceServer.InvocationEvent(service), this, -1);
            dbSapIsActive();
            final String o = options.toString();
            tracer.put(EEventSeverity.EVENT, Messages.SERVICE_STARTED + " " + o, Messages.MLS_ID_SERVICE_STARTED, new ArrStr(o), false);
        }
        return isStarted();
    }

    public ServiceServer getServiceServer() {
        return service;
    }

    public boolean isStarted() {
        return service != null;
    }

    protected void logErrorInService(final Throwable t, final String serviceWsdl) {
        final String exStack = ExceptionTextFormatter.throwableToString(t);
        tracer.put(EEventSeverity.WARNING, Messages.ERR_UNHANDLED_IN_SERVICE + " \"" + serviceWsdl + "\":\n" + exStack, null, null, false);
    }

    protected ServiceProcessFault throwableToFault(Throwable exception) {
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
        return new ServiceProcessServerFault(reason, "Unhandled " + exStack, exception, exStack);
    }

    public void rereadOptions() throws SQLException, InterruptedException {
        cache.maintenance();
        final SapOptions newOptions = readOptions();
        if (newOptions == null) {
            return;
        }
        if (isStarted() && !options.equals(newOptions)) {
            tracer.debug("Service options changed", false);
            final Connection db = getDbConnection();
            stop();
            start(db, newOptions);
        } else if (!isStarted()) {
            start(getDbConnection(), newOptions);
        }
    }

    public void dbSapSelfCheck() {
        if (!isStarted()) {
            return;
        }
        try {
            dbQueries.setDbSelfCheck();
        } catch (SQLException e) {
            final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
            tracer.put(EEventSeverity.ERROR, Messages.ERR_IN_DB_QRY + ": " + exStack, Messages.MLS_ID_ERR_IN_DB_QRY, new ArrStr(exStack), false);
        }
        if (aadcManager != null) {
            aadcManager.sapIsAlive(getId(), System.currentTimeMillis());
        }
    }

    public void stop() {
        if (!isStarted()) {
            return;
        }
        service.stop();
        dispatcher.unsubscribe(new ServiceServer.InvocationEvent(service));
        service = null;
        dbSapIsNotActive();
        final String o = options.toString();
        tracer.put(EEventSeverity.EVENT, Messages.SERVICE_STOPPED + " " + o, Messages.MLS_ID_SERVICE_STOPPED, new ArrStr(o), false);
        setDbConnection(null);
    }

    @Override
    public void onEvent(final Event ev) {
        if (!isStarted()) {
            throw new IllegalUsageError("Invalid event " + ev);
        }
        if (ev instanceof ServiceServer.InvocationEvent) {
            final InvocationEvent event = (ServiceServer.InvocationEvent) ev;
            if (event.isExpired) {
                event.seance.close();
            } else {
                try {
                    processFullRequest(event);
                } finally {
                    if (!isShuttingDown()) {
                        dispatcher.waitEvent(new ServiceServer.InvocationEvent(service), this, -1);
                    }
                }
            }
        } else {
            service.onEvent(ev);
        }
    }

    public abstract long getId();

    public java.sql.Connection getDbConnection() {
        return dbConnection;
    }

    public void setDbConnection(final java.sql.Connection dbConnection) {
        dbQueries.closeAll();
        this.dbConnection = dbConnection;
    }

    LocalTracer getTracer() {
        return tracer;
    }

    private void dbSapIsActive() throws InterruptedException {
        while (!isShuttingDown()) {
            try {
                dbQueries.setDbActiveState(true);
                return;
            } catch (SQLException e) {
                final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
                tracer.put(EEventSeverity.ERROR, Messages.ERR_IN_DB_QRY + ": " + exStack, Messages.MLS_ID_ERR_IN_DB_QRY, new ArrStr(exStack), false);
                restoreDbConnection();
            }
        }
    }

    private void dbSapIsNotActive() {
        if (getDbConnection() == null)//DBP-1607 if stopping while unit is trying to restore db connection then dbConnection == null
        {
            return;
        }
        try {
            dbQueries.setDbActiveState(false);
        } catch (SQLException e) {
            final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
            tracer.put(EEventSeverity.ERROR, Messages.ERR_IN_DB_QRY + ": " + exStack, Messages.MLS_ID_ERR_IN_DB_QRY, new ArrStr(exStack), false);
        }
    }

    protected SapOptions readOptions() throws SQLException, InterruptedException {
        return dbQueries.readOptions();
    }

    abstract protected boolean isShuttingDown();

    abstract protected void restoreDbConnection() throws InterruptedException;

    protected void processFullRequest(ServiceServer.InvocationEvent event) {
        try {
            process(new InvocationEvent(event.source, event.seance, SoapFormatter.getInnerContent(event.rqEnvBodyContent), event.rqFrameAttrs));
        } catch (IOException ex) {
            throw new RadixError("Unable to extract request content", ex);
        }
    }

    abstract protected void process(ServiceServer.InvocationEvent event);
}
