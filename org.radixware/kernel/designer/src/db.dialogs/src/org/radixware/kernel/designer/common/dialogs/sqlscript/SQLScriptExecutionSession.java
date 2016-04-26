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

package org.radixware.kernel.designer.common.dialogs.sqlscript;

import java.util.EventListener;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.netbeans.api.db.explorer.ConnectionManager;
import org.netbeans.editor.WeakEventListenerList;
import org.openide.nodes.Node;
import org.openide.util.RequestProcessor;
import org.radixware.kernel.common.sqlscript.parser.IPauseObject;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;


public class SQLScriptExecutionSession implements Node.Cookie {

    //private final static String SQLSCRIPT_EXECUTION_SESSION = "sqlscript-execution-session";
    //request processor capable for 20 parallel executors. XXX: why 20?
    private final RequestProcessor requestProcessor = new RequestProcessor("SQLScriptExecutionSession@" + Integer.toHexString(System.identityHashCode(this)), 20);
    private final List<SQLScriptExecutor> executors = new LinkedList<SQLScriptExecutor>();
    private final WeakEventListenerList listeners = new WeakEventListenerList();

    public synchronized boolean isActive() {
        return executors.size() > 0;
    }

    public synchronized void start(final List<SQLScriptExecutor> executors) {
        if (executors == null) {
            throw new NullPointerException();
        }
        if (executors.isEmpty()) {
            throw new IllegalArgumentException("Executor list is empty!");
        }
        if (abortPrevious()) {
            for (SQLScriptExecutor executor : executors) {
                startExecutor(executor, false);
            }
        }
    }

    public void start(final SQLScriptExecutor executor, boolean stepMode) {
        if (abortPrevious()) {
            startExecutor(executor, stepMode);
        }
    }

    /**
     * Checks whether some executors are already running and prompts user to terminate them if necessary
     * @return true if all executions were terminated or there were non running executors
     * and false if user decided to continue previously ran executions.
     */
    private boolean abortPrevious() {
        synchronized (executors) {
            if (isActive()) {
                final String message = executors.size() == 1 ? "Another execution is already running. Do you want to abort it?" : "Another executions are already running. Do you want to abort them?";
                final int result = JOptionPane.showConfirmDialog(null, message, "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (result == JOptionPane.YES_OPTION) {
                    end();
                    return true;
                } else {
                    return false;
                }
            }
            return true;
        }
    }

    private void startExecutor(final SQLScriptExecutor executor, boolean stepMode) {
        ConnectionManager.getDefault().showConnectionDialog(executor.getConnection());
        boolean connected = executor.getConnection().getJDBCConnection() != null;
        if (connected) {
            synchronized (executors) {
                executors.add(executor);
                executor.getMonitor().getPauseObject().setStepMode(stepMode);
                notifyListeners();
                requestProcessor.post(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            executor.run();
                        } finally {
                            end(executor);
                        }
                    }
                });
            }
        } else {
            DialogUtils.messageError("Database connection is not established.");
        }
    }

    /**
     * List of executors managed by this session.
     * @return
     */
    public List<SQLScriptExecutor> getExecutors() {
        return executors;
    }

    /**
     * Cancel all active executions in this session
     */
    public void end() {

        synchronized (executors) {
            if (isActive()) {
                while (!executors.isEmpty()) {
                    end(executors.get(0));
                }
            }
        }
    }

    private void end(final SQLScriptExecutor executor) {
        synchronized (executors) {
            if (executor == null) {
                throw new NullPointerException("Can not end null executor");
            }
            if (!executors.contains(executor)) {
                //throw new IllegalStateException("Specified executor is not running in this session.");
                //executor was cancelled by user
                return;
            }
            executor.cancel();
            if (executor.getMonitor() instanceof DesignerSQLMonitor) {
                final IPauseObject pauseObject = executor.getMonitor().getPauseObject();
                if (pauseObject.isStepMode()) {
                    pauseObject.allowNextStep();//allow SQLScript to cancel execution
                }
            }
            executors.remove(executor);
            if (!isActive()) {
                notifyListeners();
            }
        }
    }

    public void addWeakListener(ChangeListener listener) {
        listeners.add(ChangeListener.class, listener);
    }

    public void removeListener(ChangeListener listener) {
        listeners.remove(ChangeListener.class, listener);
    }

    private void notifyListeners() {
        for (EventListener listener : listeners.getListeners(ChangeListener.class)) {
            ((ChangeListener) listener).stateChanged(new ChangeEvent(this));
        }
    }
//    public static SQLScriptExecutionSession getInstance(final JTextComponent textComponent) {
//        Object session = textComponent.getClientProperty(SQLSCRIPT_EXECUTION_SESSION);
//        if (!(session instanceof SQLScriptExecutionSession)) {
//            session = new SQLScriptExecutionSession();
//            textComponent.putClientProperty(SQLSCRIPT_EXECUTION_SESSION, session);
//        }
//        return (SQLScriptExecutionSession) session;
//    }
}
