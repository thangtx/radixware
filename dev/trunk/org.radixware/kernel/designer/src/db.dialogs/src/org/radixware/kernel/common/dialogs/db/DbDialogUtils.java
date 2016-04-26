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

package org.radixware.kernel.common.dialogs.db;

import java.sql.Connection;
import java.util.List;
import org.netbeans.api.db.explorer.ConnectionManager;
import org.netbeans.api.db.explorer.DatabaseConnection;
import org.netbeans.api.db.explorer.DatabaseException;
import org.radixware.kernel.designer.common.dialogs.sqlscript.actions.SelectConnectionsPanel;


public class DbDialogUtils {

    /**
     * This method should be run outside from AWT thread. because it tries to
     * connect to the database synchronously in the caller thread and can freeze
     * UI.
     *
     * @return
     */
    public static Connection connectToDatabase() {
        final DatabaseConnection dbConn = selectDatabaseConnection(true);
        if (dbConn == null) {
            return null;
        }
        if (dbConn.getJDBCConnection() == null) {
            try {
                ConnectionManager.getDefault().connect(dbConn);
            } catch (DatabaseException ex) {
                return null;
            }
        }
        return dbConn.getJDBCConnection();
    }

    /**
     * If mustBeConnected is true, than OK button in dialog would be active if
     * and only if selected connection is established.
     *
     * @param mustBeConnected
     * @return
     */
    public static DatabaseConnection selectDatabaseConnection(boolean mustBeConnected) {
        final SelectConnectionsPanel panel = new SelectConnectionsPanel("Select Connection", false, mustBeConnected);
        if (panel.showModal()) {
            List<DatabaseConnection> list = panel.getSelectedConnections();
            return list.isEmpty() ? null : list.get(0);
        }
        return null;
    }

    public static List<DatabaseConnection> selectDatabaseConnections() {
        final SelectConnectionsPanel panel = new SelectConnectionsPanel("Select Connections", true, false);
        if (panel.showModal()) {
            return panel.getSelectedConnections();
        }
        return null;
    }
}
