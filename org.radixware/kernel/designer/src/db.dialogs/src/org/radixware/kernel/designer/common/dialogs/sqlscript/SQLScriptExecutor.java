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

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.concurrent.CancellationException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.util.Exceptions;
import org.radixware.kernel.common.sqlscript.connection.SQLConnection;
import org.netbeans.api.db.explorer.DatabaseConnection;
import org.radixware.kernel.common.components.ICancellable;
import org.radixware.kernel.common.sqlscript.parser.SQLPreprocessor;
import org.radixware.kernel.common.sqlscript.parser.SQLScript;
import org.radixware.kernel.common.sqlscript.parser.SQLScriptException;
import org.radixware.kernel.common.sqlscript.parser.spi.SQLDialogHandler;
import org.radixware.kernel.common.sqlscript.parser.spi.SQLMonitor;
import org.radixware.kernel.common.sqlscript.parser.spi.VariablesProvider;

/**
 * Executes SQLScript in Designer.
 */
public class SQLScriptExecutor implements Runnable {

    protected static final Logger logger = Logger.getLogger(SQLScriptExecutor.class.getName());
    final VariablesProvider variablesProvider;
    final SQLDialogHandler dialogHandler;
    final SQLMonitor monitor;
    private boolean started = false;
    private boolean cancelled = false;
    private DatabaseConnection dbconn;
    private String script;
    private String scriptName;

    public SQLScriptExecutor(final String script, final DatabaseConnection dbconn, final String scriptName, VariablesProvider variablesProvider, SQLDialogHandler dialogHandler, SQLMonitor monitor) {
        logger.setLevel(Level.ALL);
        this.variablesProvider = variablesProvider;
        this.dialogHandler = dialogHandler;
        this.monitor = monitor;
        this.dbconn = dbconn;
        this.script = script;
        this.scriptName = scriptName;
    }

    public SQLMonitor getMonitor() {
        return monitor;
    }

    public void cancel() {
        cancelled = true;
    }

    public DatabaseConnection getConnection() {
        return dbconn;
    }

    @Override
    public void run() {
        if (started) {
            throw new IllegalStateException("You can call SQLScriptExecutor.run() only once");
        }
        started = true;
        logger.log(Level.INFO, "Run {0}", this.toString());
        try {
            final SQLConnection sqlConn = new DesignerSQLConnectionAdapter(dbconn.getJDBCConnection(), dbconn.getDatabaseURL());
            final SQLPreprocessor sqlPreprocessor = new SQLPreprocessor(script, scriptName, variablesProvider);
            final String preprocessedScript;

            try {
                preprocessedScript = sqlPreprocessor.preprocess(SQLPreprocessor.PreprocessBehavior.PT_REPLACE_UNUSED_BLOCKS_TO_COMMENT, null);
            } catch (SQLScriptException ex) {
                monitor.printErrors(Arrays.asList(new String[]{createErrorMessage(ex)}));
                monitor.printErrors(Arrays.asList(new String[]{"Unable to continue due to previous errors."}));
                return;
            } catch (IOException ex) {
                monitor.printErrors(Arrays.asList(new String[]{ex.getMessage()}));
                monitor.printErrors(Arrays.asList(new String[]{"Unable to continue due to previous errors."}));
                return;
            } catch (CancellationException ex) {
                monitor.printErrors(Arrays.asList(new String[]{"CANCELLED"}));
                return;
            }

            final SQLScript sqlScript = new SQLScript(sqlConn, dbconn.getDatabaseURL(), preprocessedScript, scriptName, variablesProvider, dialogHandler, monitor);
            try {
                sqlScript.execute(new ICancellable() {
                    @Override
                    public boolean wasCancelled() {
                        return cancelled;
                    }

                    @Override
                    public boolean cancel() {
                        return cancelled = true;
                    }
                });
            } catch (SQLScriptException ex) {
                monitor.printErrors(Arrays.asList(new String[]{createErrorMessage(ex)}));
                monitor.printErrors(Arrays.asList(new String[]{"Unable to continue due to previous errors."}));
                return;
            } catch (IOException ex) {
                monitor.printErrors(Arrays.asList(new String[]{"Unable to continue due to previous errors."}));
                return;
            } catch (CancellationException ex) {
                monitor.printErrors(Arrays.asList(new String[]{"CANCELLED"}));
                return;
            }
            monitor.printDbmsOutput(Arrays.asList(new String[]{"COMPLETED"}));
            monitor.completed();
        } finally {
            if (monitor instanceof DesignerSQLMonitor) {
                logger.log(Level.INFO, "Clear monitor of {0}", this.toString());
                ((DesignerSQLMonitor) monitor).clear();
            }
        }
    }

    private String createErrorMessage(SQLScriptException ex) {
        if (ex.getPosition() != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(ex.getPosition().getSourceLine());
            sb.append(':');
            sb.append(ex.getPosition().getColumn());
            sb.append(": [SQLScriptException] ");
            sb.append(ex.getMessage());
            return sb.toString();
        } else {
            return "[SQLScriptException] " + ex.getMessage();
        }
    }

    private static class DesignerSQLConnectionAdapter extends SQLConnection {

        public DesignerSQLConnectionAdapter(String dbUrl, String name, String password) throws SQLException {
            super(dbUrl, name, password);
        }

        public DesignerSQLConnectionAdapter(String dbUrl, String user, SQLDialogHandler dialogHandler) throws SQLException {
            super(dbUrl, user, dialogHandler);
        }

        public DesignerSQLConnectionAdapter(Connection con, String dburl) {
            super(con, dburl);
            try {
                enableDbmsOutput();
            } catch (SQLException ex) {
                Exceptions.printStackTrace(ex);
            }
        }

        @Override
        public void logoff() throws SQLException {
            //it's illegal to directly close connection managed by Natbeans ConnectionManager
        }
    }
}
