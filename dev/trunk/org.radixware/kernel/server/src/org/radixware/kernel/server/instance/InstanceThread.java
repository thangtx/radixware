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

import org.radixware.kernel.common.enums.EInstanceThreadKind;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.lang.management.ThreadInfo;
import java.sql.Connection;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.trace.IRadixTrace;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.server.jdbc.RadixConnection;
import org.radixware.kernel.server.jdbc.RadixDataSource;
import org.radixware.kernel.server.trace.ServerThread;

public final class InstanceThread extends ServerThread {

    private static final long PAUSE_BEFORE_AUTO_RESTART_MILLIS = 5000;

    InstanceThread(final Instance instance, final RadixDataSource radixDataSource, final String proxyOraUser, final Connection dbConnection, final int instanceId, final String instanceName) {
        super(null, null, instance.getClass().getClassLoader(), instance.getTrace().newTracer(EEventSource.INSTANCE.getValue()));
        this.instance = instance;
        this.radixDataSource = radixDataSource;
        this.proxyOraUser = proxyOraUser;
        this.dbConnection = dbConnection;
        this.instanceId = instanceId;
        this.instanceName = instanceName;
        setupUncaughtExceptionHandler();
    }

    private void setupUncaughtExceptionHandler() {
        setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(final Thread t, final Throwable e) {
                //write crash information for diagnostic purpose
                final Date date = new Date();
                final File f = new File("instance-crash-log-" + date.toString().replace(' ', '-'));
                PrintStream ps = null;
                try {
                    ps = new PrintStream(f);
                    ps.println("Exception in '" + t.getName() + "':");
                    e.printStackTrace(ps);
                } catch (FileNotFoundException ex) {
                    //do nothing
                    Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                } finally {
                    if (ps != null) {
                        ps.close();
                    }
                }
            }
        });
    }

    @Override
    public final void run() {
        boolean autoRestart = false;
        try {
            try {
                instance.setState(InstanceState.STARTING);
                instance.startImpl(radixDataSource, proxyOraUser, dbConnection, instanceId, instanceName);
                instance.setState(InstanceState.STARTED);
                instance.runImpl();
            } catch (Throwable e) {
                if (e instanceof InterruptedException || Thread.currentThread().isInterrupted()) {
                    instance.getTrace().put(EEventSeverity.EVENT, Messages.INSTANCE_THREAD_INTERRUPTED, Messages.MLS_ID_INSTANCE_THREAD_INTERRUPTED, new ArrStr(instance.getFullTitle()), EEventSource.INSTANCE, false);
                } else {
                    final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
                    instance.getTrace().put(EEventSeverity.ERROR, Messages.INSTANCE_THREAD_INTERRUPTED_BY_EXCEPTION + exStack, Messages.MLS_ID_INSTANCE_THREAD_INTERRUPTED_BY_EXCEPTION, new ArrStr(instance.getFullTitle(), exStack), EEventSource.INSTANCE, false);
                    autoRestart = true;
                }
            } finally {
                interrupted();
                try {
                    instance.setState(InstanceState.STOPPING);
                    instance.stopImpl();
                    instance.setState(InstanceState.STOPPED);
                    instance.setDbConnection(null, null, null);
                    radixDataSource.close();
                } catch (Throwable e) {
                    final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
                    instance.getTrace().put(EEventSeverity.ERROR, Messages.ERR_ON_INSTANCE_STOP + exStack, Messages.MLS_ID_ERR_ON_INSTANCE_STOP, new ArrStr(instance.getFullTitle(), exStack), EEventSource.INSTANCE, false);
                }
                instance.getTrace().stopFileLogging();
            }
        } finally {
            if (autoRestart) {
                boolean awake = false;
                try {
                    Thread.sleep(PAUSE_BEFORE_AUTO_RESTART_MILLIS);
                    awake = true;
                } catch (InterruptedException ex) {
                }
                if (awake) {
                    instance.restartServer("autorestart on unexpected stop");
                }
            }
        }
    }

    @Override
    public Connection getConnection() {
        return instance.getDbConnection();
    }

    @Override
    public IRadixTrace getTrace() {
        return instance.getTrace();
    }

    @Override
    public boolean isAborted() {
        return false;
    }
    
    @Override
    public InstanceThreadStateRecord getThreadStateRecord(ThreadInfo threadInfo) {
        return InstanceThreadStateRecord.createRecord(EInstanceThreadKind.INSTANCE, this, threadInfo, null, null, null, (RadixConnection) getConnection());
    }
    
    final private RadixDataSource radixDataSource;
    final private String proxyOraUser;
    final private Connection dbConnection;
    final private int instanceId;
    final private String instanceName;
    final private Instance instance;
}
