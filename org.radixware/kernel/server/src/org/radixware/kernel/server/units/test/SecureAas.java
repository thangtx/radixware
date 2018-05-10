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

package org.radixware.kernel.server.units.test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.exceptions.ServiceProcessClientFault;
import org.radixware.kernel.common.exceptions.ServiceProcessFault;
import org.radixware.kernel.common.exceptions.ServiceProcessServerFault;
import org.radixware.kernel.common.trace.LocalTracer;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.server.aio.Event;
import org.radixware.kernel.server.aio.EventDispatcher;
import org.radixware.kernel.server.aio.ServiceServer;
import org.radixware.kernel.server.arte.services.aas.AasValueConverter;
import org.radixware.kernel.server.instance.Instance;
import org.radixware.kernel.server.jdbc.DelegateDbQueries;
import org.radixware.kernel.server.jdbc.Stmt;
import org.radixware.kernel.server.jdbc.IDbQueries;
import org.radixware.kernel.server.jdbc.RadixConnection;
import org.radixware.kernel.server.sap.Sap;
import org.radixware.kernel.server.sap.SapQueries;
import org.radixware.kernel.server.units.AsyncEventHandlerUnit;
import org.radixware.kernel.server.units.UnitView;
import org.radixware.schemas.aas.ExceptionEnum;
import org.radixware.schemas.aas.InvokeMess;
import org.radixware.schemas.aas.InvokeRq;
import org.radixware.schemas.aas.InvokeRs;
import org.radixware.schemas.aas.Value;
import org.radixware.schemas.aasWsdl.InvokeDocument;


public class SecureAas extends AsyncEventHandlerUnit {

    public static long TEST_SECURE_AAS_UNIT_TYPE = 4010;
    private static final String qryStartImplStmtSQL = "select val from rdx_upvalnum where defid='pruFFPIUAKMM5HTLDUJX7CDRKDZ2M' and ownerentityid='tbl5HP4XTP3EGWDBRCRAAIT4AGD7E' and ownerPid=?";
    private static final Stmt qryStartImplStmt = new Stmt(qryStartImplStmtSQL,Types.VARCHAR);
    
    
    private volatile Options options;
    private final IDbQueries delegate = new DelegateDbQueries(this, null);
    private SecureAasSap sap;

    private SecureAas(){        
    }
    
    public SecureAas(Instance instModel, Long id, String title) {
        super(instModel, id, title);
    }

    @Override
    protected boolean startImpl() throws Exception {
        super.startImpl();
        try {
            try (PreparedStatement ps = ((RadixConnection)getDbConnection()).prepareStatement(qryStartImplStmt)) {
                ps.setString(1, Long.toString(getId()));
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        options = new Options(rs.getLong(1));
                    }
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        sap = new SecureAasSap(this);
        sap.start(getDbConnection());
        getDispatcher().waitEvent(new EventDispatcher.TimerEvent(), this, 5000);
        return true;
    }

    @Override
    protected void setDbConnection(RadixConnection dbConnection) {
        super.setDbConnection(dbConnection);
        if (sap != null) {
            sap.setDbConnection(dbConnection);
        }
    }

    @Override
    protected void stopImpl() {
        if (sap != null) {
            sap.stop();
        }
        sap = null;
        super.stopImpl();
    }

    @Override
    public String getUnitTypeTitle() {
        return "Secure AAS (Test)";
    }

    @Override
    public Long getUnitType() {
        return TEST_SECURE_AAS_UNIT_TYPE;
    }

    @Override
    protected UnitView newUnitView() {
        return new UnitView(this);
    }

    @Override
    public void onEvent(Event ev) {
        if (ev instanceof EventDispatcher.TimerEvent) {
            try {
                SapQueries.setDbSelfCheck(sap.getId(), getDbConnection());
                getDbConnection().commit();
                if (!isShuttingDown()) {
                    getDispatcher().waitEvent(new EventDispatcher.TimerEvent(), this, System.currentTimeMillis() + 5000);
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

        }
    }

    private static class Options {

        public final long sapId;

        public Options(long sapId) {
            this.sapId = sapId;
        }
    }

    private static class SecureAasSap extends Sap {

        private final SecureAas unit;

        public SecureAasSap(final SecureAas unit) {
            super(unit.getDispatcher(), new LocalTracer() {
                private final EEventSource source = EEventSource.UNIT;

                @Override
                public void put(EEventSeverity severity, String localizedMess, String code, List<String> words, boolean isSensitive) {
                    unit.getTrace().put(severity, localizedMess, code, words, source, isSensitive);
                }

                @Override
                public long getMinSeverity() {
                    return unit.getTrace().getMinSeverity(source);
                }

                @Override
                public long getMinSeverity(String eventSource) {
                    return unit.getTrace().getMinSeverity(eventSource);
                }
            }, 10, 10, unit.getResourceKeyPrefix() + "/sec_aas_sap");
            this.unit = unit;
        }

        @Override
        public long getId() {
            return unit.options.sapId;
        }

        @Override
        protected boolean isShuttingDown() {
            return unit.isShuttingDown();
        }

        @Override
        protected void restoreDbConnection() throws InterruptedException {
            unit.restoreDbConnection();
        }

        @Override
        protected void process(ServiceServer.InvocationEvent event) {
            Exception exOnRqParse = null;
            InvokeRq rq = null;
            try {
                rq = getInvokeRq(event.rqEnvBodyContent);
            } catch (Exception ex) {
                exOnRqParse = ex;
            }
            if (exOnRqParse == null && rq == null) {
                exOnRqParse = new ServiceProcessClientFault("Invalid request", "Unable to parse request", null, null);
            }
            if (exOnRqParse != null) {
                if (exOnRqParse instanceof ServiceProcessFault) {
                    event.seance.response((ServiceProcessFault) exOnRqParse, false);
                } else {
                    event.seance.response(new ServiceProcessClientFault("Invalid request", "Error on request parse", exOnRqParse, ExceptionTextFormatter.throwableToString(exOnRqParse)), false);
                }
                return;
            }

            XmlObject responce;
            try {
                responce = doProcessRequest(rq);
            } catch (Exception ex) {
                event.seance.response(new ServiceProcessServerFault("Invalid request", "Error on request processing", ex, ExceptionTextFormatter.throwableToString(ex)), false);
                return;
            }

            event.seance.response(responce, true);
        }

        protected XmlObject doProcessRequest(final InvokeRq invokeRq) throws ServiceProcessFault, InterruptedException, Exception {
            final Object[] paramVals;
            if (invokeRq.isSetParameters()) {
                paramVals = new Object[invokeRq.getParameters().getItemList().size()];
                int i = 0;
                for (Value paramXml : invokeRq.getParameters().getItemList()) {
                    paramVals[i] = AasValueConverter.aasXmlVal2ObjVal(null, paramXml);
                    i++;
                }
            } else {
                paramVals = new Object[]{};
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
            if (invokeRq.getMethodId() == null) {
                throw new ServiceProcessClientFault(ExceptionEnum.APPLICATION_ERROR.toString(), "\"MethodId\" is required in InvokeRq", null, null);
            }

            if (invokeRq.isSetClassName()) {
                final Method method = getMethod(getClass().getClassLoader().loadClass(invokeRq.getClassName()), invokeRq.getMethodId(), paramTypes);
                if (!Modifier.isStatic(method.getModifiers())) {
                    throw new IllegalUsageError("Method " + method + " is not static");
                }
                returnVal = method.invoke(null, paramVals);
            } else {
                throw new ServiceProcessClientFault(ExceptionEnum.APPLICATION_ERROR.toString(), "\"ClassId\", \"ClassName\" or \"PID\" is required in InvokeRq", null, null);
            }
            final InvokeDocument res = InvokeDocument.Factory.newInstance();
            final InvokeRs rsStruct = res.addNewInvoke().addNewInvokeRs();
            AasValueConverter.objVal2AasXmlVal(returnVal, rsStruct.addNewReturnValue());
            return res;
        }

        private final Method getMethod(final Class<?> c, final String name, final Class[] paramTypes) throws SecurityException, NoSuchMethodException {
            try {
                final Method method = c.getMethod(name, paramTypes);
                return method;
            } catch (NoSuchMethodException e) {//
                if (paramTypes != null && paramTypes.length > 0) {
                    for (Method m : c.getMethods()) {
                        if (m.getName().equals(name) && paramTypes.length == m.getParameterTypes().length) {
                            boolean ok = true;
                            for (int i = 0; ok && i < paramTypes.length; i++) {
                                if (m.getParameterTypes()[i].isPrimitive() || paramTypes[i] != null && !paramTypes[i].equals(m.getParameterTypes()[i])) {
                                    ok = false;
                                }
                            }
                            if (ok) {
                                return m;
                            }
                        }
                    }
                }
                //RADIX-4589 
                throw e;// throw new RadixError(ERR_CANT_INVOKE_METHOD + ExceptionTextFormatter.getExceptionMess(e), e);
            }
        }

        private InvokeRq getInvokeRq(final XmlObject rqMess) throws Exception {
            InvokeMess mess = null;
            try {
                mess = (InvokeMess) rqMess;
            } catch (ClassCastException e) {
                mess = InvokeMess.Factory.parse(rqMess.newReader());
            }

            final InvokeRq invokeRq = mess.getInvokeRq();
            if (invokeRq == null) {
                throw new ServiceProcessClientFault(ExceptionEnum.INVALID_REQUEST.toString(), "Parameter \"InvokeRq\" is required", null, null);
            }
            return invokeRq;
        }
    }
}
