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

package org.radixware.kernel.server.units.jms;

import java.net.URL;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.enums.EJmsMessageFormat;
import org.radixware.kernel.common.enums.EUnitType;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.sc.SapClientOptions;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.server.aio.EventDispatcher.TimerEvent;
import org.radixware.kernel.server.aio.*;
import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.server.exceptions.UnsupportedUnitTypeException;
import org.radixware.kernel.server.instance.Instance;
import org.radixware.kernel.server.jdbc.RadixConnection;
import org.radixware.kernel.server.units.AsyncEventHandlerUnit;
import org.radixware.kernel.server.units.Messages;
import org.radixware.kernel.server.units.UnitView;
import org.radixware.schemas.jmshandler.Base;


public final class JmsHandlerUnit extends AsyncEventHandlerUnit implements EventHandler {

    static final int DEFAULT_AAS_CALL_TIMEOUT_MILLIS = 60 * 1000; // 1 min
    static final Id HANDLER_ENTITY_ID = Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E");
    private volatile Options options = null;
    private volatile AasClientPool aasClientPool = null;
    private volatile JmsHandler jmsHandler = null;
    private volatile JmsHandlerInterfaceSap service = null;

    static final class Options {

        final EJmsMessageFormat jmsMessFormat;
        final Clob jmsConnectProps;
        final Clob jmsMessProps;
        final String jmsLogin;
        final String jmsPassword;
        final String msRqQueueName;
        final String msRsQueueName;
        final Boolean isClient;
        final Long inSeanceCnt;
        final Long outSeanceCnt;
        final Long rsTimeout;
        final long sapId;

        Options(
                final EJmsMessageFormat jmsMessFormat,
                final Clob jmsConnectProps,
                final Clob jmsMessProps,
                final String jmsLogin,
                final String jmsPassword,
                final String msRqQueueName,
                final String msRsQueueName,
                final Boolean isClient,
                final Long inSeanceCnt,
                final Long outSeanceCnt,
                final Long rsTimeout,
                final long sapId) {

            this.jmsMessFormat = jmsMessFormat;
            this.jmsConnectProps = jmsConnectProps;
            this.jmsMessProps = jmsMessProps;
            this.jmsLogin = jmsLogin;
            this.jmsPassword = jmsPassword;
            this.msRqQueueName = msRqQueueName;
            this.msRsQueueName = msRsQueueName;
            this.isClient = isClient;
            this.inSeanceCnt = inSeanceCnt;
            this.outSeanceCnt = outSeanceCnt;
            this.rsTimeout = rsTimeout;
            this.sapId = sapId;
        }

        private String getText(final Clob clob) {
            String text = null;
            if (clob != null) {
                try {
                    text = clob.getSubString(1L, (int) clob.length());
                } catch (SQLException ex) {
                    Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                }
            }
            return text;
        }

        @Override
        public boolean equals(final Object o) {
            if (o == null || !(o instanceof Options)) {
                return false;
            }
            final Options opt = (Options) o;
            return (jmsMessFormat == null ? opt.jmsMessFormat == null : jmsMessFormat.equals(opt.jmsMessFormat))
                    && (jmsConnectProps == null ? opt.jmsConnectProps == null : getText(jmsConnectProps).equals(getText(opt.jmsConnectProps)))
                    && (jmsMessProps == null ? opt.jmsMessProps == null : getText(jmsMessProps).equals(getText(opt.jmsMessProps)))
                    && (jmsLogin == null ? opt.jmsLogin == null : jmsLogin.equals(opt.jmsLogin))
                    && (jmsPassword == null ? opt.jmsPassword == null : jmsPassword.equals(opt.jmsPassword))
                    && (msRqQueueName == null ? opt.msRqQueueName == null : msRqQueueName.equals(opt.msRqQueueName))
                    && (msRsQueueName == null ? opt.msRsQueueName == null : msRsQueueName.equals(opt.msRsQueueName))
                    && (inSeanceCnt == null ? opt.inSeanceCnt == null : inSeanceCnt.equals(opt.inSeanceCnt))
                    && (outSeanceCnt == null ? opt.outSeanceCnt == null : outSeanceCnt.equals(opt.outSeanceCnt))
                    && (rsTimeout == null ? opt.rsTimeout == null : rsTimeout.equals(opt.rsTimeout))
                    && (isClient == null ? opt.isClient == null : isClient.equals(opt.isClient))
                    && sapId == opt.sapId;
        }

        public boolean changed(final Options opt) {
            return !(jmsConnectProps == null ? opt.jmsConnectProps == null : getText(jmsConnectProps).equals(getText(opt.jmsConnectProps)))
                    && (jmsMessProps == null ? opt.jmsMessProps == null : getText(jmsMessProps).equals(getText(opt.jmsMessProps)))
                    && (jmsLogin == null ? opt.jmsLogin == null : jmsLogin.equals(opt.jmsLogin))
                    && (jmsPassword == null ? opt.jmsPassword == null : jmsPassword.equals(opt.jmsPassword))
                    && (msRqQueueName == null ? opt.msRqQueueName == null : msRqQueueName.equals(opt.msRqQueueName))
                    && (msRsQueueName == null ? opt.msRsQueueName == null : msRsQueueName.equals(opt.msRsQueueName));
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 29 * hash + (jmsMessFormat != null ? jmsMessFormat.hashCode() : 0);
            hash = 29 * hash + (jmsConnectProps != null ? jmsConnectProps.hashCode() : 0);
            hash = 29 * hash + (jmsMessProps != null ? jmsMessProps.hashCode() : 0);
            hash = 29 * hash + (jmsLogin != null ? jmsLogin.hashCode() : 0);
            hash = 29 * hash + (jmsPassword != null ? jmsPassword.hashCode() : 0);
            hash = 29 * hash + (msRqQueueName != null ? msRqQueueName.hashCode() : 0);
            hash = 29 * hash + (msRsQueueName != null ? msRsQueueName.hashCode() : 0);
            hash = 29 * hash + (inSeanceCnt != null ? inSeanceCnt.hashCode() : 0);
            hash = 29 * hash + (outSeanceCnt != null ? outSeanceCnt.hashCode() : 0);
            hash = 29 * hash + (rsTimeout != null ? rsTimeout.hashCode() : 0);
            hash = 29 * hash + (isClient != null ? isClient.hashCode() : 0);
            hash = 29 * hash + (int) sapId;
            return hash;
        }

        @Override
        public String toString() {
            return "{\n\t"
                    + JmsHandlerMessages.JMS_MESS_FORMAT + (jmsMessFormat == null ? "-" : String.valueOf(jmsMessFormat)) + "; \n\t"
                    + JmsHandlerMessages.JMS_CONNECT_PROPS + (jmsConnectProps == null ? "-" : getText(jmsConnectProps)) + "; \n\t"
                    + JmsHandlerMessages.JMS_MESS_PROPS + (jmsMessProps == null ? "-" : getText(jmsMessProps)) + "; \n\t"
                    + JmsHandlerMessages.JMS_LOGIN + (jmsLogin == null ? "-" : String.valueOf(jmsLogin)) + "; \n\t"
                    + JmsHandlerMessages.JMS_PASSWORD + (jmsPassword == null ? "-" : String.valueOf(jmsPassword)) + "; \n\t"
                    + JmsHandlerMessages.MS_RQ_QUEUE_NAME + (msRqQueueName == null ? "-" : String.valueOf(msRqQueueName)) + "; \n\t"
                    + JmsHandlerMessages.MS_RS_QUEUE_NAME + (msRsQueueName == null ? "-" : String.valueOf(msRsQueueName)) + "; \n\t"
                    + JmsHandlerMessages.IN_SEANCE_CNT + (inSeanceCnt == null ? "-" : String.valueOf(inSeanceCnt)) + "; \n\t"
                    + JmsHandlerMessages.OUT_SEANCE_CNT + (outSeanceCnt == null ? "-" : String.valueOf(outSeanceCnt)) + "; \n\t"
                    + JmsHandlerMessages.RS_TIMEOUT + (rsTimeout == null ? "-" : String.valueOf(rsTimeout)) + "; \n\t"
                    + "}";
        }
    }
    private final JmsDbQueries nhDbQueries;

    public JmsHandlerUnit(final Instance instModel, final Long id, final String title) throws UnsupportedUnitTypeException {
        super(instModel, id, title);
        nhDbQueries = new JmsDbQueries(this);
    }

    @Override
    public String getUnitTypeTitle() {
        return JmsHandlerMessages.UNIT_TYPE_TITLE;
    }

    @Override
    public Long getUnitType() {
        return EUnitType.JMS_HANDLER.getValue();
    }

    final AasClientPool getAasClientPool() {
        return aasClientPool;
    }

    final JmsHandler getJmsHandler() {
        return jmsHandler;
    }

    final JmsHandlerInterfaceSap getService() {
        return service;
    }
    private ServiceManifestLoader manifestLoader = null;

    final ServiceManifestLoader getManifestLoader() {
        return manifestLoader;
    }

    @Override
    protected boolean startImpl() throws Exception {
        if (!super.startImpl()) {
            return false;
        }
        options = nhDbQueries.readOptions();
        manifestLoader = new ServiceManifestServerLoader() {
            @Override
            protected Connection getDbConnection() {
                return JmsHandlerUnit.this.getDbConnection();
            }

            @Override
            protected Arte getArte() {
                return null;
            }
        };

        jmsHandler = new JmsHandler(this);
        if (!jmsHandler.start()) {
            return false;
        }

        aasClientPool = new AasClientPool(this);
        if (options.isClient) {
            service = new JmsHandlerInterfaceSap(this);
            if (!service.start(getDbConnection())) {
                return false;
            }
        } else {
            service = null;
        }

        final String optionsStr = options.toString();
        getTrace().put(EEventSeverity.EVENT, Messages.START_OPTIONS + optionsStr, Messages.MLS_ID_START_OPTIONS, new ArrStr(getTitle(), optionsStr), getEventSource(), false);
        getDispatcher().waitEvent(new EventDispatcher.TimerEvent(), this, System.currentTimeMillis());
        return true;
    }

    @Override
    protected void stopImpl() {
        if (jmsHandler != null) {
            jmsHandler.stop();
            jmsHandler = null;
        }
        if (service != null) {
            service.stop();
            service = null;
        }
        if (aasClientPool != null) {
            aasClientPool.stop();
            aasClientPool = null;
        }
        manifestLoader = null;
        super.stopImpl();
    }

    @Override
    protected UnitView newUnitView() {
        return new UnitView(this);
    }

    @Override
    protected void setDbConnection(final RadixConnection dbConnection) {
        nhDbQueries.closeAll();
        if (service != null) {
            service.setDbConnection(dbConnection);
        }
        super.setDbConnection(dbConnection);
    }

    @Override
    public String getEventSource() {
        return EEventSource.JMS_HANDLER.getValue();
    }

    @Override
    protected void rereadOptionsImpl() throws SQLException, InterruptedException {
        final Options newOptions = nhDbQueries.readOptions();
        if (newOptions == null || newOptions.equals(options)) {
            return;
        }
        final boolean bNeedJmsRestart = options.changed(newOptions);
        options = newOptions;
        final String newOptionsStr = newOptions.toString();
        getTrace().put(EEventSeverity.EVENT, Messages.OPTIONS_CHANGED + newOptionsStr, Messages.MLS_ID_OPTIONS_CHANGED, new ArrStr(getTitle(), newOptionsStr), getEventSource(), false);
        if (service != null) {
            service.rereadOptions();
            if (!service.isStarted()) {
                requestStopAndPostponedRestart("unable to restart service");
            }
        }
        if (bNeedJmsRestart && !jmsHandler.restart()) {
            requestStopAndPostponedRestart("unable to restart jms handler");
        }
    }

    final JmsDbQueries getNetHubDbQueries() {
        return nhDbQueries;
    }

    final Options getOptions() {
        return options;
    }

    final BytesMessage sendBytes(final byte[] bytes, final Base info) throws JMSException {
        return jmsHandler.sendBytes(bytes, info);
    }

    final BytesMessage sendBytes(final byte[] bytes, final Base info, final String corrMessId) throws JMSException {
        return jmsHandler.sendBytes(bytes, info, corrMessId);
    }

    final TextMessage sendText(final String text, final Base info) throws JMSException {
        return jmsHandler.sendText(text, info);
    }

    final TextMessage sendText(final String text, final Base info, final String corrMessId) throws JMSException {
        return jmsHandler.sendText(text, info, corrMessId);
    }

    final boolean canSend() {
        return jmsHandler.canSend();
    }

    final boolean canReceive() {
        return jmsHandler.canReceive();
    }

    final void onMessage(final Message msg) throws JMSException {
        if (options.isClient) {
            if (!service.onResponse(msg)) {
                service.onUncorrelatedResponse(msg);
            }
        } else {
            aasClientPool.onRequest(msg);
        }
    }
    static final int TIC_MILLIS = 500; // 0.5 sec
    private long lastSapDbIAmAliveMillis = 0;

    @Override
    public void onEvent(final Event ev) {
        if (ev instanceof TimerEvent) {
            final long curMillis = System.currentTimeMillis();
            //sap selfcheck
            if (curMillis - lastSapDbIAmAliveMillis >= DB_I_AM_ALIVE_PERIOD_MILLIS) {
                if (service != null) {
                    service.dbSapSelfCheck();
                }
                lastSapDbIAmAliveMillis = curMillis;
            }
            //jms process            
            if (jmsHandler.canReceive()) {
                jmsHandler.process();
            }
            if (!isShuttingDown()) {
                getDispatcher().waitEvent(new EventDispatcher.TimerEvent(), this, System.currentTimeMillis() + TIC_MILLIS);
            }
        } else {
            throw new IllegalUsageError("Invalid event " + ev);
        }
    }

    final void traceDebug(String mess, String component, boolean isSensetive) {
        if (getTrace().getMinSeverity() == EEventSeverity.DEBUG.getValue().longValue()) {
            getTrace().debug((component != null ? component + ": " : "") + mess, getEventSource(), isSensetive);
        }
    }

    final void traceError(String mess, String component) {
        getTrace().put(EEventSeverity.ERROR, (component != null ? component + ": " : "") + mess, null, null, getEventSource(), false);
    }
}
