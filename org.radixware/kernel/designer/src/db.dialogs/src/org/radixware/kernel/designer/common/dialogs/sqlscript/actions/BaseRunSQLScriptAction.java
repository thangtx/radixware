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

package org.radixware.kernel.designer.common.dialogs.sqlscript.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.netbeans.api.db.explorer.ConnectionManager;
import org.netbeans.api.db.explorer.DatabaseConnection;
import org.openide.loaders.DataObject;
import org.openide.util.Lookup;
import org.radixware.kernel.designer.common.dialogs.actions.AbstractRadixAction;
import org.radixware.kernel.designer.common.dialogs.actions.AbstractRadixContextAwareAction;
import org.radixware.kernel.designer.common.dialogs.sqlscript.DesignerDialogHandler;
import org.radixware.kernel.designer.common.dialogs.sqlscript.DesignerSQLMonitor;
import org.radixware.kernel.designer.common.dialogs.sqlscript.DesignerVariablesProvider;
import org.radixware.kernel.designer.common.dialogs.sqlscript.ISQLScriptProvider;
import org.radixware.kernel.designer.common.dialogs.sqlscript.SQLScriptExecutionSession;
import org.radixware.kernel.designer.common.dialogs.sqlscript.SQLScriptExecutor;
import org.radixware.kernel.designer.common.dialogs.sqlscript.SelectedConnectionsManager;
import org.radixware.kernel.designer.common.dialogs.sqmlnb.DatabaseParameters;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;


public class BaseRunSQLScriptAction extends AbstractRadixContextAwareAction {

    private final boolean stepMode;

    public BaseRunSQLScriptAction(final boolean stepMode, final String name, final String iconBase) {
        super(name, iconBase);
        this.stepMode = stepMode;
    }

    @Override
    public Action createAction(Lookup actionContext) {
        final SQLScriptExecutionSession session = actionContext.lookup(SQLScriptExecutionSession.class);
        if (session == null) {
            return new UnavailableRunSqlScriptActionImpl();
        }
        final SelectedConnectionsManager selectedConnectionManager = actionContext.lookup(SelectedConnectionsManager.class);
        if (selectedConnectionManager == null) {
            return new UnavailableRunSqlScriptActionImpl();
        }

        String scriptName = "sql";
        final DataObject dataObject = actionContext.lookup(DataObject.class);
        if (dataObject != null) {
//            throw new NullPointerException("No dataObject in actionContext");
            scriptName = dataObject.getName();
        }

        ISQLScriptProvider.Factory factory = actionContext.lookup(ISQLScriptProvider.Factory.class);
        if (factory == null && dataObject != null) {
            factory = dataObject.getLookup().lookup(ISQLScriptProvider.Factory.class);
        }

        if (factory == null) {
            return new UnavailableRunSqlScriptActionImpl();
        }

        return new BaseRunSQLScriptActionImpl(session, stepMode, selectedConnectionManager, scriptName, factory, actionContext);
    }

    private static class BaseRunSQLScriptActionImpl extends AbstractRadixAction {

        private final SQLScriptExecutionSession session;
        private final SelectedConnectionsManager selectedConnectionsManager;
        private final boolean stepMode;
        private final String scriptName;
        private final ChangeListener connectionListener;
//        private final ISQLScriptProvider scriptProvider;
        private final Lookup context;
        private final ISQLScriptProvider.Factory factory;

        public BaseRunSQLScriptActionImpl(final SQLScriptExecutionSession session, final boolean stepMode, final SelectedConnectionsManager selectedConnectionsManager, final String scriptName, final ISQLScriptProvider.Factory factory, final Lookup context) {
            this.session = session;
            this.stepMode = stepMode;
            this.selectedConnectionsManager = selectedConnectionsManager;
            this.scriptName = scriptName;
//            this.scriptProvider = scriptProvider;
            this.context = context;
            this.factory = factory;
            connectionListener = new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    boolean enabled;
                    if (selectedConnectionsManager.getSelectedConnections() == null) {
                        enabled = false;
                    } else if (stepMode) {
                        enabled = selectedConnectionsManager.getSelectedConnections().size() == 1;
                    } else {
                        enabled = selectedConnectionsManager.getSelectedConnections().size() > 0;
                    }
                    if (enabled != isEnabled()) {
                        setEnabled(enabled);
                    }
                }
            };
            selectedConnectionsManager.addWeakListener(connectionListener);
            connectionListener.stateChanged(null);
        }

        @Override
        public void actionPerformed(final ActionEvent evt) {
            final List<DatabaseConnection> connectionsList = selectedConnectionsManager.getSelectedConnections();

            if (connectionsList != null && !connectionsList.isEmpty()) {
                ISQLScriptProvider scriptProvider = factory.create(context);
                if (connectionsList.size() == 1) {
                    final DatabaseConnection connection = connectionsList.get(0);
                    DatabaseParameters connectionParameters = prepareConnectionParameters(connection);
                    if (connectionParameters == null) {
                        return;
                    }
                    final SQLScriptExecutor executor = new SQLScriptExecutor(scriptProvider.getScript(), connection, scriptName, new DesignerVariablesProvider(connection, connectionParameters), new DesignerDialogHandler(), new DesignerSQLMonitor(scriptProvider, scriptName, null, true));
                    session.start(executor, stepMode);
                } else {
                    if (stepMode) {
                        DialogUtils.messageError("Execution on multiple connections in step mode is not supported.");
                        return;
                    }
                    List<SQLScriptExecutor> executors = new ArrayList<SQLScriptExecutor>(5);
                    for (int i = 0; i < connectionsList.size(); i++) {
                        final DatabaseConnection connection = connectionsList.get(i);
                        DatabaseParameters connectionParameters = prepareConnectionParameters(connection);
                        if (connectionParameters == null) {
                            continue;
                        }
                        final SQLScriptExecutor executor = new SQLScriptExecutor(scriptProvider.getScript(), connection, scriptName, new DesignerVariablesProvider(connection, connectionParameters), new DesignerDialogHandler(), new DesignerSQLMonitor(scriptProvider, scriptName, connection.getDisplayName(), false));
                        executors.add(executor);
                    }
                    session.start(executors);
                }
            } else {
                if (stepMode) {
                    DialogUtils.messageError("Select one connection, please!");
                } else {
                    DialogUtils.messageError("Select at least one connection, please!");
                }
            }
        }

        @Override
        protected String getMimeTypeForSettings() {
            return "text/x-sql";
        }
    }

    public static DatabaseParameters prepareConnectionParameters(final DatabaseConnection connection) {
        if (connection != null && connection.getJDBCConnection() == null) {
            ConnectionManager.getDefault().showConnectionDialog(connection);
        }
        return DatabaseParameters.get(connection == null ? null : connection.getName(), connection == null ? null : connection.getJDBCConnection());
    }
}
