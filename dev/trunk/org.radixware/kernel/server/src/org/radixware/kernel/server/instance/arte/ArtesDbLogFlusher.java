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
package org.radixware.kernel.server.instance.arte;

import java.sql.*;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import oracle.jdbc.OraclePreparedStatement;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.trace.IRadixTrace;
import org.radixware.kernel.common.trace.LocalTracer;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.server.instance.Instance;
import org.radixware.kernel.server.trace.DbLog;
import org.radixware.kernel.server.trace.DbLog.Item;
import org.radixware.kernel.server.trace.IServerThread;

public class ArtesDbLogFlusher extends Thread implements IServerThread {

    private enum EFlushResult {

        OK,
        NOT_CONNECTED,
        BAD_ITEM;
    }
    private static final int MAX_QUEUE_SIZE = 1000;
    private static final int MAX_OFFERED_QUEUE_SIZE = 1000;
    private static final long CONNECTION_KEEP_ALIVE_REQUEST_PERIOD_MILLIS = 60000;
    private static final long MIN_ERROR_INTERVAL = 1000;
    private static final Queue<DbLog.Item> OFFERED_ITEMS = new ArrayDeque<>();
    private final Instance instance;
    private final LocalTracer tracer;
    private OraclePreparedStatement qryPut = null;
    private Connection dbConnection;
    private final Queue<DbLog.Item> itemsQueue = new ArrayDeque<>();
    private long lastErrorMillis = 0;
    private boolean needPause = false;

    public ArtesDbLogFlusher(final Instance instance) {
        super();
        this.instance = instance;
        setName("Artes DbLog Flusher of instance #" + instance.getId());
        setContextClassLoader(instance.getClass().getClassLoader());
        tracer = instance.getTrace().newTracer(EEventSource.ARTE.getValue());
    }

    @Override
    public IRadixTrace getTrace() {
        return instance.getTrace();
    }

    @Override
    public Connection getConnection() {
        return null;
    }

    @Override
    public void run() {
        long lastKeepAliveRqMillis = System.currentTimeMillis();
        try {
            while (!instance.isShuttingDown() && !Thread.currentThread().isInterrupted()) {
                if (itemsQueue.isEmpty() || dbConnection == null || needPause) {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException ex) {
                        return;
                    }
                }

                needPause = false;

                final ArtePool artePool = instance.getArtePool();
                final List<ArteInstance> arteInstances = artePool != null ? artePool.getInstances(false) : Collections.EMPTY_LIST;

                if (itemsQueue.size() < MAX_QUEUE_SIZE) {
                    synchronized (OFFERED_ITEMS) {
                        itemsQueue.addAll(OFFERED_ITEMS);
                        OFFERED_ITEMS.clear();
                    }
                }

                for (ArteInstance arte : arteInstances) {
                    if (itemsQueue.size() < MAX_QUEUE_SIZE) {
                        itemsQueue.addAll(arte.popAllDbLogItems());
                    } else {
                        break;
                    }
                }

                if (!itemsQueue.isEmpty() || (System.currentTimeMillis() - lastKeepAliveRqMillis > CONNECTION_KEEP_ALIVE_REQUEST_PERIOD_MILLIS)) {
                    final boolean tryReopenOnPingFail = dbConnection != null;
                    boolean pingFailed = true;

                    Connection conn = getDbConnection();
                    try {
                        if (conn != null && !conn.isValid(5)) {
                            tracer.put(EEventSeverity.WARNING, "ArtesDbLogFlusher database connection was closed", null, null, false);
                            closeDbConnection();
                        } else {
                            lastKeepAliveRqMillis = System.currentTimeMillis();
                            pingFailed = false;
                        }
                    } catch (SQLException ex) {
                        closeDbConnection();
                    }

                    if (pingFailed && (!tryReopenOnPingFail || getDbConnection() == null)) {
                        continue;
                    }

                    while (!itemsQueue.isEmpty() && !Thread.currentThread().isInterrupted()) {
                        EFlushResult result = flush(itemsQueue.peek());
                        if (result == EFlushResult.OK || result == EFlushResult.BAD_ITEM) {
                            itemsQueue.poll();
                        } else {
                            break;
                        }
                    }
                }
            }
        } finally {
            closeDbConnection();
        }
    }

    private void closeDbConnection() {
        if (qryPut != null) {
            try {
                qryPut.close();
            } catch (SQLException e) {
                //do nothing
            }
            qryPut = null;
        }

        if (dbConnection != null) {
            try {
                dbConnection.close();
            } catch (SQLException e) {
                //do nothing
            }
            dbConnection = null;
        }
    }

    private Connection getDbConnection() {
        if (dbConnection == null) {
            try {
                dbConnection = instance.openNewDbConnection("ArteDbLogsFlusher", null);
                dbConnection.setAutoCommit(true);
            } catch (SQLException e) {
                cantRegEvInEventLog(e);
                return null;
            }
        }
        return dbConnection;
    }

    protected EFlushResult flush(final Item item) {
        if (item == null) {
            return EFlushResult.BAD_ITEM;
        }
        final Connection db = getDbConnection();
        if (db == null) {
            return EFlushResult.NOT_CONNECTED;
        }
        Clob wordsClob = null;
        try {
            if (item.words != null) {
                //TODO OPTIMIZATION use special DbpValueConverter methods for Clobs
                final String wordsAsStr = item.words.toString();
                wordsClob = db.createClob();
                wordsClob.setString(1, wordsAsStr);
            }
            if (qryPut == null) {
                final String sql = "begin ?:=rdx_trace.put_internal(?,?,?,?,?,?,?,?,?,?); end;";
                qryPut = (OraclePreparedStatement) db.prepareCall(sql);
            }
            ((CallableStatement) qryPut).registerOutParameter(1, java.sql.Types.INTEGER);
            qryPut.setString(2, item.code);
            qryPut.setClob(3, wordsClob);
            qryPut.setString(4, item.source);
            qryPut.setLong(5, item.severity.getValue().longValue());
            qryPut.setString(6, item.contextTypes);
            qryPut.setString(7, item.contextIds);
            qryPut.setTimestamp(8, item.time);
            qryPut.setString(9, item.userName);
            qryPut.setString(10, item.stationName);
            qryPut.setLong(11, item.isSensitive ? 1 : 0);
            qryPut.executeUpdate();
            return EFlushResult.OK;
        } catch (SQLException e) {
            boolean dbWasClosed = false;
            try {
                dbWasClosed = db.isClosed();
            } catch (SQLException ex) {
                //ingore
            }
            cantRegEvInEventLog(e);
            closeDbConnection();
            if (dbWasClosed) {
                return EFlushResult.NOT_CONNECTED;
            } else {
                return EFlushResult.BAD_ITEM;
            }
        } finally {
            if (wordsClob != null) {
                try {
                    wordsClob.free();
                } catch (SQLException ex) {
                    //do nothing;
                }
            }
        }
    }

    private void cantRegEvInEventLog(final SQLException e) {
        tracer.put(EEventSeverity.ERROR, "Can't register event in RDX_EVENTLOG table from ArtesDbLogFlusher thread: " + ExceptionTextFormatter.exceptionStackToString(e), null, null, false);
        if (System.currentTimeMillis() - lastErrorMillis < MIN_ERROR_INTERVAL) {
            needPause = true;
        }
        lastErrorMillis = System.currentTimeMillis();
    }

    @Override
    public LocalTracer getLocalTracer() {
        return tracer;
    }

    public static int offer(final List<DbLog.Item> items) {
        if (items == null || items.isEmpty()) {
            return 0;
        }
        synchronized (OFFERED_ITEMS) {
            int count = 0;
            for (DbLog.Item item : items) {
                if (OFFERED_ITEMS.size() >= MAX_OFFERED_QUEUE_SIZE) {
                    break;
                }
                OFFERED_ITEMS.add(item);
                count++;
            }
            return count;
        }
    }
}
