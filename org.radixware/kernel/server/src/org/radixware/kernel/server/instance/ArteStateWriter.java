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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import oracle.jdbc.OraclePreparedStatement;
import org.apache.commons.logging.LogFactory;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.server.instance.arte.ArteInstance;
import static org.radixware.kernel.server.instance.arte.ArteInstance.EState.BUSY;
import static org.radixware.kernel.server.instance.arte.ArteInstance.EState.FREE;
import static org.radixware.kernel.server.instance.arte.ArteInstance.EState.INIT;
import org.radixware.kernel.server.instance.arte.IArteRequest;

public class ArteStateWriter extends Thread {

    private static final int VALIDATE_DB_CONNECTION_TIMEOUT_SEC = 5;
    private static final int VALIDATE_DB_CONNECTION_PERIOD_MILLIS = 10000;
    private final Instance instance;
    private volatile boolean stopRequested = false;
    private Connection dbConnection;
    private PreparedStatement clearSt;
    private PreparedStatement insertSt;
    private long lastValidateDbConnection = 0;
    private final Object curHandleSem = new Object();
    private UpdateRequestHandle curHandle;

    public ArteStateWriter(Instance instance) {
        this.instance = instance;
        setDaemon(true);
        setName("ArteStateWriter");
        try {
            openDbConnection();
        } catch (SQLException ex) {
            LogFactory.getLog(getClass()).debug("Error on db connection opening", ex);
        }
    }

    private void openDbConnection() throws SQLException {
        dbConnection = instance.openNewDbConnection(getName(), null);
    }

    private void closeDbConnection() {
        if (dbConnection != null) {
            try {
                dbConnection.close();
            } catch (SQLException ex) {
                LogFactory.getLog(getClass()).debug("Error on db connection closing", ex);
            } finally {
                dbConnection = null;
                clearSt = null;
                insertSt = null;
            }
        }
    }

    private void checkDbConnection() throws SQLException {
        final long curMillis = System.currentTimeMillis();
        if (dbConnection == null || dbConnection.isClosed()) {
            closeDbConnection();
            openDbConnection();
        } else if (curMillis - lastValidateDbConnection >= VALIDATE_DB_CONNECTION_PERIOD_MILLIS) {
            if (!dbConnection.isValid(VALIDATE_DB_CONNECTION_TIMEOUT_SEC)) {
                closeDbConnection();
                openDbConnection();
            }
            lastValidateDbConnection = curMillis;
        }
    }

    public UpdateRequestHandle requestUpdate() {
        synchronized (curHandleSem) {
            if (curHandle == null) {
                curHandle = new UpdateRequestHandle();
                curHandleSem.notifyAll();
            }
            return curHandle;
        }
    }

    @Override
    public void run() {
        try {
            while (!stopRequested && !Thread.currentThread().isInterrupted()) {
                try {
                    final UpdateRequestHandle handle;
                    synchronized (curHandleSem) {
                        if (curHandle != null) {
                            handle = curHandle;
                        } else {
                            curHandleSem.wait();
                            handle = curHandle;
                        }
                    }
                    if (handle != null) {
                        handle.setResult(doFillArteTable());
                        synchronized (curHandleSem) {
                            curHandle = null;
                        }
                    }
                } catch (InterruptedException ex) {
                    return;
                }
            }
        } finally {
            closeDbConnection();
        }
    }

    public void requestStop() {
        stopRequested = true;
        interrupt();
    }

    private boolean doFillArteTable() {
        try {
            checkDbConnection();
            fillArtePoolTable(dbConnection);
            return true;
        } catch (Exception ex) {
            instance.getTrace().putFloodControlled("ArteStateWriterSqlEx",
                    EEventSeverity.WARNING,
                    "Error on updating ARTE pool state: " + ExceptionTextFormatter.throwableToString(ex),
                    null, null, EEventSource.INSTANCE.getValue(), -1, false, null);
            if (dbConnection != null) {
                try {
                    dbConnection.rollback();
                } catch (Exception ex1) {
                    LogFactory.getLog(getClass()).error("Error on ArteStateWriter rollback", ex1);
                }
            }
        }
        return false;
    }

    private void fillArtePoolTable(final Connection connection) throws SQLException {
        if (connection == null) {
            throw new SQLException("Connection is null");
        }

        if (clearSt == null) {
            final String sqlClearArteTable = "DELETE FROM RDX_ARTEINSTANCE WHERE INSTANCEID=?";
            clearSt = connection.prepareStatement(sqlClearArteTable);
        }
        clearSt.setLong(1, instance.getId());
        clearSt.execute();

        if (insertSt == null) {
            final String sqlInsertArteData = "INSERT INTO RDX_ARTEINSTANCE (INSTANCEID, SERIAL, SEQ, STATE, UNITID, SID, BUSYTIMEMILLIS, LIFEMILLIS) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            insertSt = connection.prepareStatement(sqlInsertArteData);
            ((OraclePreparedStatement) insertSt).setExecuteBatch(200);
        }
        final long curTime = System.currentTimeMillis();
        for (ArteInstance arteInst : instance.getArtePool().getInstances(false)) {
            insertSt.setLong(1, instance.getId());
            insertSt.setLong(2, arteInst.getSerial());
            insertSt.setLong(3, arteInst.getSeqNumber());

            final Long sid = arteInst.getDbSessionId();
            if (sid != null) {
                insertSt.setLong(6, sid);
            } else {
                insertSt.setNull(6, java.sql.Types.INTEGER);
            }

            //Default values. May be changed if state = BUSY.
            insertSt.setNull(5, java.sql.Types.INTEGER);
            insertSt.setNull(7, java.sql.Types.INTEGER);

            String arteState = "";
            switch (arteInst.getState()) {
                case FREE:
                    arteState = ArteInstance.EState.FREE.name();
                    break;
                case BUSY:
                    final IArteRequest rq = arteInst.getRequest();
                    if (rq != null) {
                        arteState = ArteInstance.EState.BUSY.name();
                        insertSt.setLong(5, rq.getUnit().getId());
                        insertSt.setLong(7, System.currentTimeMillis() - rq.getCreateTimeMillis());
                    }
                    break;
                case INIT:
                    arteState = ArteInstance.EState.INIT.name();
                    break;
                default:
                    throw new IllegalStateException("Not defined state of Arte instance: " + arteInst.getState());
            }
            insertSt.setString(4, arteState);
            insertSt.setLong(8, curTime - arteInst.getStartTime());

            insertSt.execute();
        }
        ((OraclePreparedStatement) insertSt).sendBatch();
        connection.commit();
    }

    public static class UpdateRequestHandle {

        private boolean result;
        private final CountDownLatch latch = new CountDownLatch(1);

        public boolean await(final long timeoutMillis) throws InterruptedException {
            if (latch.await(timeoutMillis, TimeUnit.MILLISECONDS)) {
                return result;
            } else {
                return false;
            }
        }

        protected void setResult(final boolean result) {
            this.result = result;
            latch.countDown();
        }
    }
}
