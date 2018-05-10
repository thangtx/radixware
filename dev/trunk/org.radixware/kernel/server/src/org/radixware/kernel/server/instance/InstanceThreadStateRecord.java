/*
 * Copyright (c) 2008-2018, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */

package org.radixware.kernel.server.instance;

import java.io.IOException;
import java.lang.management.ThreadInfo;
import org.radixware.kernel.common.enums.EInstanceThreadKind;
import java.sql.SQLException;
import java.util.Arrays;
import org.radixware.kernel.common.trace.LocalTracerThreadGroup;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.common.utils.Hex;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.server.instance.arte.ArteInstance;
import org.radixware.kernel.server.jdbc.RadixConnection;
import org.radixware.kernel.server.jdbc.RadixPreparedStatement;
import org.radixware.kernel.server.jdbc.RadixResultSet;
import org.radixware.kernel.server.monitoring.ArteWaitStats;
import org.radixware.kernel.server.trace.IServerThread;
import org.radixware.kernel.server.units.Unit;
import org.radixware.kernel.server.units.UnitThread;

public class InstanceThreadStateRecord {
    final boolean forced;
    final Thread.State threadState;
    final EInstanceThreadKind threadKind;
    
    final int instanceId;
    final long regTimeMillis;
    final long threadId;
    final String name;
    final Long ancestorThreadId; // Server (IServerThread) Thread Id, ancestor of non-Server (non-IServerThread) thread
    final Long unitId;
    final Long arteSeq;
    final Long arteSerial;
    final String dbSid;
    final String dbSerial;
    final String traceContexts;
    final Long cpuDiffMillis;
    final Long dbDiffMillis;
    final Long extDiffMillis;
    final Long queueDiffMillis;
    final Integer uptimeSec;
    final String stack;
    final String stackDigestSha1;
    final String stackTop;
    final String lockName;
    final String lockOwnerName;
    final Long rqStartTimeMillis;
    final String extData;

    public InstanceThreadStateRecord(boolean forced, Thread thread, EInstanceThreadKind threadKind, int instanceId, long regTimeMillis, long threadId, String name, Long ancestorThreadId, Long unitId, Long arteSeq, Long arteSerial, String dbSid, String dbSerial, String traceContexts, Long cpuDiffMillis, Long dbDiffMillis, Long extDiffMillis, Long queueDiffMillis, Integer uptimeSec, String stack, byte[] compressedStack, String stackTop, String lockName, String lockOwnerName, Long rqStartTimeMillis, String extData) {
        this.forced = forced;
        this.threadState = thread != null ? thread.getState() : null;
        this.threadKind = threadKind;
        this.instanceId = instanceId;
        this.regTimeMillis = regTimeMillis;
        this.threadId = threadId;
        this.name = name;
        this.ancestorThreadId = ancestorThreadId;
        this.unitId = unitId;
        this.arteSeq = arteSeq;
        this.arteSerial = arteSerial;
        this.dbSid = dbSid;
        this.dbSerial = dbSerial;
        this.traceContexts = traceContexts;
        this.cpuDiffMillis = cpuDiffMillis;
        this.dbDiffMillis = dbDiffMillis;
        this.extDiffMillis = extDiffMillis;
        this.queueDiffMillis = queueDiffMillis;
        this.uptimeSec = uptimeSec;
        
        String _stack;
        try {
            _stack = stack != null ? stack : Utils.decompressString(compressedStack);
        } catch (IOException ex) {
            _stack = "Failed to decompress GZIP-packed stack: " + ExceptionTextFormatter.exceptionStackToString(ex);
        }
        this.stack = _stack;
        this.stackDigestSha1 = Hex.encode(Utils.digestSha1(stack));
        this.stackTop = stackTop;
        this.lockName = lockName;
        this.lockOwnerName = lockOwnerName;
        this.rqStartTimeMillis = rqStartTimeMillis;
        this.extData = extData;
    }
    
    public void fillInsertStatementParams(RadixPreparedStatement stmt, String digestPrefix) throws SQLException {
        int i = 1;
        stmt.setInt(i++, instanceId);
        stmt.setLong(i++, regTimeMillis);
        stmt.setLong(i++, threadId);
        stmt.setString(i++, threadKind == null ? null : threadKind.getValue());
        stmt.setBoolean(i++, forced);
        stmt.setString(i++, name);
        stmt.setLong(i++, ancestorThreadId);
        stmt.setLong(i++, unitId);
        stmt.setLong(i++, arteSeq);
        stmt.setLong(i++, arteSerial);
        stmt.setString(i++, dbSid);
        stmt.setString(i++, dbSerial);
        stmt.setString(i++, traceContexts);
        stmt.setLong(i++, cpuDiffMillis);
        stmt.setLong(i++, dbDiffMillis);
        stmt.setLong(i++, extDiffMillis);
        stmt.setLong(i++, queueDiffMillis);
        stmt.setInt(i++, uptimeSec);
        stmt.setString(i++, digestPrefix + stackDigestSha1);
        stmt.setString(i++, lockName);
        stmt.setString(i++, lockOwnerName);
        stmt.setLong(i++, rqStartTimeMillis);
        stmt.setString(i++, extData);
    }
    
    private static boolean skipStackTraceTopElement(StackTraceElement element) {
//        return false;
        final String className = element.getClassName();
        final String methodName = element.getMethodName();
        if (className == null || methodName == null) {
            return false;
        }
        
        final boolean skip = "org.radixware.kernel.server.trace.Trace".equals(className)
                || "org.radixware.kernel.server.trace.ServerTrace".equals(className)
                || "org.radixware.kernel.server.trace.ServerTrace$1".equals(className)
                || "org.radixware.kernel.server.trace.ServerThreadLog".equals(className)
                || "org.radixware.kernel.starter.log.DelegateLogFactory$DelegateLogger".equals(className)
                || "org.radixware.kernel.starter.log.StarterLog".equals(className)
                || "org.radixware.kernel.server.instance.RadixLog4jAppender".equals(className)
                || className.startsWith("org.apache.log4j")
                || className.startsWith("org.slf4j")
                || ("org.radixware.kernel.server.arte.ArteProfiler".equals(className)
                && "leaveTimingSection".equals(methodName));
        return skip;
    }
    
    private static boolean skipStackTraceBottomElement(StackTraceElement element) {
//        return false;
        final String className = element.getClassName();
        final boolean skip = className != null && className.startsWith("java.");
        return skip;
    }
    
    public static InstanceThreadStateRecord create(Thread thread, ThreadInfo threadInfo) {
        if (thread instanceof IServerThread) {
            return ((IServerThread)thread).getThreadStateRecord(threadInfo);
        } else {
            return createRecord(EInstanceThreadKind.SERVER_CHILD, thread, threadInfo, null, null, null, null);
        }
    }
    
    public static InstanceThreadStateRecord createRecord(
            final EInstanceThreadKind threadKind,
            final Thread thread, 
            final ThreadInfo threadInfo, 
            final Arte arte, 
            final ArteInstance arteInstance, 
            final ArteWaitStats arteWaitsDiff, 
            final RadixConnection connection
    ) {
        if (thread == null || thread.getName().equals("License Client Thread") || !thread.isAlive()) {
            return null;
        }
                
        final boolean forced = Thread.currentThread() != thread;
        final Instance instance = Instance.get();
        final IServerThread serverThread = thread instanceof IServerThread ? (IServerThread) thread : null;
        final UnitThread unitThread = thread instanceof UnitThread ? (UnitThread) thread : null;
        final Unit unit = unitThread == null ? null : unitThread.getUnit();
        final Long startTimeNanos = serverThread == null ? null : serverThread.getStartTimeNanos();
        final long nowNanos = System.nanoTime();
        final Integer uptimeSec = startTimeNanos == null ? null : (int)((nowNanos - startTimeNanos) / 1000000000);
        
        Long ancestorThreadId = null;
        if (serverThread == null) {
            final LocalTracerThreadGroup serverGroup = LocalTracerThreadGroup.findLocalTracerThreadGroup(thread);
            if (serverGroup != null) {
                ancestorThreadId = serverGroup.getThreadId();
            }
        }

        StackTraceElement[] stackTrace = thread.getStackTrace();
        String lockName = null;
        String lockOwnerName = null;
        if (!forced) {
            int topIdx = 0;
            int bottomIdx = stackTrace.length - 1;
            while (bottomIdx > 0 && skipStackTraceBottomElement(stackTrace[bottomIdx])) {
                --bottomIdx;
            }
            if (stackTrace.length > 7) {
                topIdx = 7; // step right after 'org.radixware.kernel.server.instance.ArteStateWriter.gatherCurrentThreadState'
                while (topIdx <= bottomIdx && skipStackTraceTopElement(stackTrace[topIdx])) {
                    ++topIdx;
                }
            }
            bottomIdx = Math.min(bottomIdx, topIdx + 511); // limited to 512 stack frames
            stackTrace = Arrays.copyOfRange(stackTrace, topIdx, bottomIdx + 1);
        } else {
            stackTrace = threadInfo.getStackTrace();
            lockName = threadInfo.getLockName();
            lockOwnerName = threadInfo.getLockOwnerName();
        }
        
        final String stackTraceStr = Utils.stackToString(stackTrace, "at ");
        
        final InstanceThreadStateRecord record = new InstanceThreadStateRecord(
                forced,
                thread,
                threadKind,
                instance.getId(),
                System.currentTimeMillis(),
                thread.getId(), 
                thread.getName(),
                ancestorThreadId,
                unit == null ? null : unit.getId(),
                arte == null ? null : arte.getSeqNumber(),
                arteInstance == null ? null : arteInstance.getSerial(),
                connection == null ? null : connection.getSid(),
                connection == null ? null : connection.getSerial(),
                arte == null ? null : arte.getTrace().getNormalizedContextStackAsStr(),
                arteWaitsDiff == null ? null : arteWaitsDiff.getCpuNanos() / 1000000,
                arteWaitsDiff == null ? null : arteWaitsDiff.getDbNanos() / 1000000,
                arteWaitsDiff == null ? null : arteWaitsDiff.getExtNanos() / 1000000,
                arteWaitsDiff == null ? null : arteWaitsDiff.getQueueNanos() / 1000000,
                uptimeSec,
                stackTraceStr.isEmpty() ? "<stacktrace unavailable>" : stackTraceStr,
                null,
                stackTrace.length == 0 ? "<stacktrace unavailable>" : stackTrace[0].toString(),
                lockName,
                lockOwnerName,
                arte == null ? null : arte.getRqProcessingStartMillis(),
                null // extData
                );
        return record;
    }
    
    public static InstanceThreadStateRecord createRecord(RadixResultSet rs) throws SQLException {
        final int instanceId = rs.getInt("instanceId");
        final long regTimeMillis = rs.getLong("regTimeMillis");
        final long threadId = rs.getLong("threadId");
        final String threadKind = rs.getString("threadKind");
        final boolean forced = rs.getBoolean("forced");
        final String name = rs.getString("name");
        final Long ancestorThreadId = rs.getNullableLong("ancestorThreadId");
        final Long unitId = rs.getNullableLong("unitId");
        final Long arteSeq = rs.getNullableLong("arteSeq");
        final Long arteSerial = rs.getNullableLong("arteSerial");
        final String dbSid = rs.getString("dbSid");
        final String dbSerial = rs.getString("dbSerial");
        final String traceContexts = rs.getString("traceContexts");
        final Long cpuDiffMillis = rs.getNullableLong("cpuDiffMillis");
        final Long dbDiffMillis = rs.getNullableLong("dbDiffMillis");
        final Long extDiffMillis = rs.getNullableLong("extDiffMillis");
        final Long queueDiffMillis = rs.getNullableLong("queueDiffMillis");
        final Integer uptimeSec = rs.getNullableInt("uptimeSec");
        final byte[] compressedStack = rs.getBytes("compressedStack");
        final String lockName = rs.getString("lockName");
        final String lockOwnerName = rs.getString("lockOwnerName");
        final Long rqStartTimeMillis = rs.getNullableLong("rqStartTimeMillis");
        final String extData = rs.getString("extData");
        
        final InstanceThreadStateRecord record = new InstanceThreadStateRecord(forced, null, EInstanceThreadKind.getForValue(threadKind), instanceId, regTimeMillis, threadId, name, ancestorThreadId, unitId, arteSeq, arteSerial, dbSid, dbSerial, traceContexts, cpuDiffMillis, dbDiffMillis, extDiffMillis, queueDiffMillis, uptimeSec, null, compressedStack, null, lockName, lockOwnerName, rqStartTimeMillis, extData);
        return record;
    }
    
}
