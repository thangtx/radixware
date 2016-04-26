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
package org.radixware.kernel.server.trace;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OraclePreparedStatement;
import oracle.sql.*;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.ETimingSection;
import org.radixware.kernel.common.trace.LocalTracer;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.server.SrvRunParams;
import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.server.instance.arte.ArtesDbLogFlusher;

/**
 * Лог в базе данных.
 *
 * Реализует механизм регистрации событий в таблице БД RDX_EVENTLOG
 *
 * @Threadsafe
 *
 *
 *
 */
public class DbLog {

    private static final int MAX_BUFFER_SIZE = 200;
    private static final int MAX_VARCHAR2_SIZE = 4000;
    private Arte arte;

    public static final class Item {

        public final Timestamp time;
        public final EEventSeverity severity;
        public final String code;
        public final List<String> words;
        public final String source;
        public final String contextTypes;
        public final String contextIds;
        public final String userName;
        public final String stationName;
        public final boolean isSensitive;

        public Item(
                final Timestamp time,
                final EEventSeverity severity,
                final String code,
                final List<String> words,
                final String source,
                final String contextTypes,
                final String contextIds,
                final String userName,
                final String stationName,
                final boolean isSensitive) {
            this.time = time;
            this.severity = severity;
            this.code = code;
            this.words = words != null ? Collections.unmodifiableList(words) : null;
            this.source = source;
            this.contextIds = contextIds;
            this.contextTypes = contextTypes;
            this.userName = userName;
            this.stationName = stationName;
            this.isSensitive = isSensitive;
        }
    }

    private static class ItemWrapper {

        private final Item item;
        private final String wordsStr;

        public ItemWrapper(Item item, String wordsStr) {
            this.item = item;
            this.wordsStr = wordsStr;
        }

        public Item getItem() {
            return item;
        }

        public String getWordsStr() {
            return wordsStr;
        }
    }
    private Connection dbConnection;
    private OraclePreparedStatement qryPut = null;
    private OraclePreparedStatement qryBulkPut = null;
    private final LocalTracer tracer;
    private List<Item> buffer;
    private final Object bufSem;
    private final Object logSem;
    private StructDescriptor eventRecordStructDesc;
    private ArrayDescriptor evetRecordArrDesc;

    public DbLog(final Connection dbConnection, final LocalTracer tracer, final Arte arte) {
        this.dbConnection = dbConnection;
        this.arte = arte;
        this.tracer = tracer;
        this.bufSem = new Object();
        this.logSem = new Object();
        this.buffer = new ArrayList<Item>(MAX_BUFFER_SIZE);
    }

    public void put(
            final EEventSeverity severity,
            final String code,
            final List<String> words,
            final String source,
            final String contextTypes,
            final String contextIds,
            final boolean isSensitive,
            final long timeMillis) {
        put(severity, code, words, source, contextTypes, contextIds, isSensitive, timeMillis, false);
    }

    public void put(
            final EEventSeverity severity,
            final String code,
            final List<String> words,
            final String source,
            final String contextTypes,
            final String contextIds,
            final boolean isSensitive,
            final long timeMillis,
            final boolean flushNow) {
        final Item it = new Item(
                new Timestamp(timeMillis),
                severity,
                code,
                words,
                source,
                contextTypes,
                contextIds,
                arte == null ? null : arte.getUserName(),
                arte == null ? null : arte.getStationName(),
                isSensitive);
        synchronized (bufSem) {
            buffer.add(it);
            if (buffer.size() == MAX_BUFFER_SIZE || flushNow) {
                flush();
            }
        }
    }
    volatile boolean isInCantRegisterEvInDbLog = false;

    private void cantRegisterEvInDbLog(final SQLException e) {
        if (isInCantRegisterEvInDbLog) //for the case when tracer will call this.put()
        {
            return;
        }
        try {
            isInCantRegisterEvInDbLog = true;
            if (qryPut != null) {
                try {
                    qryPut.close();
                } catch (SQLException ex) {
                    //do nothing
                    Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                }
                qryPut = null;
            }
            tracer.put(EEventSeverity.ERROR, "Can't register event in RDX_EVENTLOG table: " + ExceptionTextFormatter.exceptionStackToString(e), null, null, false);
        } finally {
            isInCantRegisterEvInDbLog = false;
        }
    }
    private Long lastEventId = null;

    public Long getLastEventId() {
        return lastEventId;
    }

    private void flush(final ItemWrapper itemWrapper) {
        if (dbConnection != null) {
            try {
                if (qryPut == null) {
                    final String sql = "begin ?:=rdx_trace.put_internal(?,?,?,?,?,?,?,?,?,?); end;";
                    qryPut = (OraclePreparedStatement) dbConnection.prepareCall(sql);
                }
                final Item item = itemWrapper.getItem();
                ((CallableStatement) qryPut).registerOutParameter(1, java.sql.Types.INTEGER);
                qryPut.setString(2, item.code);
                qryPut.setStringForClob(3, itemWrapper.getWordsStr());//RADIX-4581
                qryPut.setString(4, item.source);
                qryPut.setLong(5, item.severity.getValue().longValue());
                qryPut.setString(6, item.contextTypes);
                qryPut.setString(7, item.contextIds);
                qryPut.setTimestamp(8, item.time);
                qryPut.setString(9, item.userName);
                qryPut.setString(10, item.stationName);
                qryPut.setLong(11, item.isSensitive ? 1 : 0);
                qryPut.execute();
                lastEventId = ((CallableStatement) qryPut).getLong(1);
            } catch (SQLException e) {
                cantRegisterEvInDbLog(e);
            }
        }
    }

    public void setDbConnection(final Connection connection) {
        synchronized (logSem) {
            if (dbConnection == connection) {
                return;
            }
            flush();
            if (qryPut != null) {
                try {
                    qryPut.close();
                } catch (SQLException ex) {
                    //do nothing
                    Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                }
            }
            qryPut = null;
            if (qryBulkPut != null) {
                try {
                    qryBulkPut.close();
                } catch (SQLException ex) {
                    //do nothing
                    Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                }
                eventRecordStructDesc = null;
                evetRecordArrDesc = null;
            }
            qryBulkPut = null;
            dbConnection = connection;
        }
    }

    public void flush() {
        final List<Item> b = popAllFromBuffer();
        if (b.isEmpty()) {
            return;
        }
        try {
            if (arte != null && (dbConnection == null || dbConnection.isClosed())) {
                ArtesDbLogFlusher.offer(b);
                return;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DbLog.class.getName()).log(Level.SEVERE, null, ex);
        }

        final List<ItemWrapper> forBulkSend = new ArrayList<>(b.size());
        if (arte != null) {
            arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
        }
        try {
            synchronized (logSem) {
                if (dbConnection == null) {
                    return;
                }
                for (Item item : b) {
                    final String wordsStr = item.words == null ? null : item.words.toString();
                    if (wordsStr != null && wordsStr.length() > MAX_VARCHAR2_SIZE) {
                        if (!forBulkSend.isEmpty()) {
                            bulkFlush(forBulkSend);
                            forBulkSend.clear();
                        }
                        flush(new ItemWrapper(item, wordsStr));
                    } else {
                        forBulkSend.add(new ItemWrapper(item, wordsStr));
                    }
                }
                if (!forBulkSend.isEmpty()) {
                    bulkFlush(forBulkSend);
                    forBulkSend.clear();
                }
            }
        } finally {
            if (arte != null) {
                arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
            }
        }
    }

    private void bulkFlush(final List<ItemWrapper> items) {
        try {
            doBulkFlush(items);
        } catch (SQLException ex) {
            cantRegisterEvInDbLog(ex);
        }
    }

    private void doBulkFlush(final List<ItemWrapper> items) throws SQLException {
        if (items == null || items.isEmpty()) {
            return;
        }
        if (qryBulkPut == null) {
            qryBulkPut = (OraclePreparedStatement) dbConnection.prepareCall("begin ?:=rdx_trace.put_records(?); end;");
            eventRecordStructDesc = StructDescriptor.createDescriptor(new SQLName(SrvRunParams.getDbSchema().toUpperCase(), "RDX_EVENT_LOG_RECORD", (OracleConnection) dbConnection), dbConnection);
            evetRecordArrDesc = ArrayDescriptor.createDescriptor(new SQLName(SrvRunParams.getDbSchema().toUpperCase(), "RDX_EVENT_LOG_RECORDS", (OracleConnection) dbConnection), dbConnection);
        }

        final Object[] recordsArray = new Object[items.size()];
        int idx = 0;
        for (ItemWrapper itemWrapper : items) {
            final Item item = itemWrapper.getItem();
            final STRUCT recordStruct = new STRUCT(eventRecordStructDesc, dbConnection, new Object[]{
                item.code,
                itemWrapper.getWordsStr(),
                item.source,
                item.severity.getValue().longValue(),
                item.contextTypes,
                item.contextIds,
                item.time,
                item.userName,
                item.stationName,
                (item.isSensitive ? 1 : 0)
            });
            recordsArray[idx] = recordStruct;
            idx++;
        }
        final ARRAY records = new ARRAY(evetRecordArrDesc, dbConnection, recordsArray);
        qryBulkPut.setARRAY(2, records);
        ((CallableStatement) qryBulkPut).registerOutParameter(1, java.sql.Types.INTEGER);
        qryBulkPut.execute();
        lastEventId = ((CallableStatement) qryBulkPut).getLong(1);
    }

    public final List<Item> popAllFromBuffer() {
        final List<Item> b;
        synchronized (bufSem) {
            if (buffer.isEmpty()) {
                return Collections.emptyList();
            }
            b = buffer;
            buffer = new ArrayList<Item>(MAX_BUFFER_SIZE);
        }
        return b;
    }
}
