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

package org.radixware.kernel.common.sqlscript.connection;

import java.sql.CallableStatement;
import org.radixware.kernel.common.sqlscript.parser.spi.SQLDialogHandler;
import org.radixware.kernel.common.sqlscript.parser.SQLScriptException;
import org.radixware.kernel.common.sqlscript.parser.SQLPosition;
import org.radixware.kernel.common.utils.Reference;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;


public class SQLConnection {
    private final List<String>  pseudoListing = new ArrayList<>();
            
    private Connection con;
    private String dbUrl;
    private String user;
    private boolean useDBMS = true;

    public SQLConnection() {
    }

    public SQLConnection(Connection con, String dburl) {
        this.con = con;
        this.dbUrl = dburl;
    }

    public SQLConnection(String dbUrl, String user, SQLDialogHandler dialogHandler) throws SQLException {
        if (dialogHandler == null) {
            throw new SQLScriptException("The login dialog is not defined");
        }
        for (;;) {
            final Reference<String> password = new Reference<>();
            if (dialogHandler.getLoginData(new Reference<>(user), password)) {
                throw new SQLScriptException("The login has been cancelled");
            }
            try {
                login(dbUrl, user, password.get());
                break;
            } catch (SQLException ex) {
                dialogHandler.handleError("Login error: " + ex.getMessage(), true, false);
            }
        }
    }

    public SQLConnection(String dbUrl, String name, String password) throws SQLException {
        login(dbUrl, name, password);
    }

    public boolean isConnected() {
        return con != null;
    }

    public Connection getConnection() {
        return con;
    }

    protected Connection createConnection(String dbUrl, String user, String password) throws SQLException {
        return DriverManager.getConnection(dbUrl, user, password);
    }

    public void login(String dbUrl, String user, String password) throws SQLException {
        logoff();
        Connection currCon = createConnection(dbUrl, user, password);
        if (currCon == null) {
            return;
        }
        con = currCon;
        this.dbUrl = dbUrl;
        this.user = user;
        enableDbmsOutput();
    }

    public void login(String dbUrl, String user, SQLDialogHandler loginDialog) throws SQLException {
        logoff();
        if (loginDialog == null) {
            throw new SQLScriptException("The login dialog is not defined");
        }
        for (int i = 0; i < 3; i++) {
            final Reference<String> user_ref = new Reference<>(user);
            final Reference<String> password_ref = new Reference<>();
            if (!loginDialog.getLoginData(user_ref, password_ref)) {
                throw new SQLScriptException("The login has been cancelled");
            }
            try {
                login(dbUrl, user, password_ref.get());
                return;
            } catch (Exception ex) {
                loginDialog.showErrorMessage(ex);
            }
        }
        throw new SQLScriptException("The login maximum retries was exceed");
    }

    public void logoff() throws SQLException {
        if (con != null) {
            con.close();
            con = null;
        }
        dbUrl = null;
        user = null;
        pseudoListing.clear();
    }

    public String getDatabaseUrl() {
        if (con == null) {
            return null;
        }
        return dbUrl;
    }

    public String getUser() {
        if (con == null) {
            return null;
        }
        return user;
    }

    public void commit() throws SQLException {
        checkConnection();
        con.commit();
    }

    public void rollback() throws SQLException {
        checkConnection();
        con.rollback();
    }

    public void execute(String statement) throws SQLException {
        checkConnection();
        try (Statement stmt = con.createStatement()) {
            stmt.execute(statement);
        }
    }

    public void execute(String statement, Object[] params) throws SQLException {
        checkConnection();
        try (PreparedStatement stmt = con.prepareStatement(statement)) {
            int index = 1;
            for (Object val : params) {
                stmt.setObject(index++, val);
            }
            stmt.execute();
        }
    }

    public ResultSet select(String query) throws SQLException {
        checkConnection();
        final Statement stmt = con.createStatement();
        try {
            return stmt.executeQuery(query);
        } catch (SQLException ex) {
            stmt.close();
            throw ex;
        }
    }

    public ResultSet select(String query, Object[] params) throws SQLException {
        checkConnection();
        PreparedStatement stmt = con.prepareStatement(query);
        try {
            int index = 1;
            for (Object val : params) {
                stmt.setObject(index++, val);
            }
            return stmt.executeQuery();
        } catch (SQLException ex) {
            stmt.close();
            throw ex;
        }
    }

    public void insert(String table, String formatString, List<Object> params, String fields) throws SQLException {
        checkConnection();
        String fields_descr = "";
        if (fields != null) {
            fields_descr = " (" + fields + ")";
        }
        try (PreparedStatement stmt = con.prepareStatement("insert into " + table + fields_descr + " values (" + formatString + ")")) {
            int index = 0;
            for (Object val : params) {
                stmt.setObject(index++, val);
            }
            stmt.executeUpdate();
        }
    }

    public void update(String table, String formatString, List<Object> params, String modifiers) throws SQLException {
        checkConnection();
        final PreparedStatement stmt;
        if (modifiers != null) {
            stmt = con.prepareStatement("update " + table + " set " + formatString + " " + modifiers);
        } else {
            stmt = con.prepareStatement("update " + table + " set " + formatString);
        }
        try {
            int index = 0;
            for (Object val : params) {
                stmt.setObject(index++, val);
            }
            stmt.executeUpdate();
        } finally {
            stmt.close();
        }
    }

    public void deleteFrom(String table, String modifiers, List<Object> params) throws SQLException {
        checkConnection();
        final PreparedStatement stmt;
        if (modifiers != null) {
            stmt = con.prepareStatement("delete from " + table + " " + modifiers);
        } else {
            stmt = con.prepareStatement("delete from " + table);
        }
        try {
            int index = 0;
            for (Object val : params) {
                stmt.setObject(index++, val);
            }
            stmt.executeUpdate();
        } finally {
            stmt.close();
        }
    }

    public void drop(String objectType, String objectName) throws SQLException {
        execute("drop " + objectType + " " + objectName);
    }

    public void compile(String objectType, String objectName) throws SQLException {
        String type = objectType.toUpperCase();
        switch (type) {
            case "JAVA CLASS":
                execute("ALTER JAVA CLASS \"" + objectName + "\" COMPILE");
                break;
            case "JAVA SOURCE":
                execute("ALTER JAVA SOURCE \"" + objectName + "\" COMPILE");
                break;
            case "PACKAGE PACKAGE":
                execute("ALTER PACKAGE " + objectName + " COMPILE PACKAGE");
                break;
            case "PACKAGE BODY":
                execute("ALTER PACKAGE " + objectName + " COMPILE BODY");
                break;
            case "TYPE BODY":
                execute("ALTER TYPE " + objectName + " COMPILE BODY");
                break;
            default:
                execute("ALTER " + type + " " + objectName + " COMPILE");
                break;
        }
    }

    public void showErrors(String objectType, String objectName,
            List<String> errorsList, int startLine,
            SQLPosition errorPosition) throws SQLException {
        ResultSet cursor = select("select line, position, text from all_errors where owner = USER and type = ? and name = ?",
                new Object[]{objectType, objectName});
        try {
            while (cursor.next()) {
                int line = startLine + cursor.getInt(1) - 1;
                int column = cursor.getInt(2);
                if (cursor.isFirst()) {
                    if (errorPosition != null) {
                        errorPosition.setLine(line);
                        errorPosition.setColumn(column);
                    }
                }
                errorsList.add(Integer.toString(line) + ":" + Integer.toString(column) + ": " + cursor.getString(3));
            }
        } finally {
            cursor.getStatement().close();
        }
    }

    public void enableDbmsOutput() throws SQLException {
        if (useDBMS) {
            checkConnection();
            try(final CallableStatement stmt = con.prepareCall("{call dbms_output.enable()}")) {
                stmt.execute();
            }
        }
    }

    public void dbmsOut(final String msg) throws SQLException {
        if (msg != null && !msg.isEmpty()) {
            if (useDBMS) {
                checkConnection();
                try(final CallableStatement stmt = con.prepareCall("{call dbms_output.put_line(?)}")){
                    stmt.setString(1, msg);
                    stmt.execute();
//                } catch (SQLException exc) {
//                    pseudoListing.add(msg);
//                    useDBMS = false;
                }
            }
            else {
                pseudoListing.add(msg);
            }
        }
    }

    public void getDbmsOutput(final List<String> out) throws SQLException {
        if (out == null) {
            throw new IllegalArgumentException("Out list can't be null");
        }
        else {
            if (useDBMS) {
                checkConnection();
                try(final CallableStatement stmt = con.prepareCall("{call dbms_output.get_line(?, ?)}")) {
                    stmt.registerOutParameter(1, java.sql.Types.VARCHAR);
                    stmt.registerOutParameter(2, java.sql.Types.INTEGER);
                    for (;;) {
                        stmt.execute();
                        if (stmt.getInt(2) != 0) {
                            break;
                        }
                        out.add(stmt.getString(1));
                    }
//                } catch (SQLException exc) {
//                    useDBMS = false;
//                    out.addAll(pseudoListing);
                }
            }
            else {
                out.addAll(pseudoListing);
            }
        }
    }

    public void getInvalidObjects(String objectType, List<String> objects) throws SQLException {
        ResultSet cursor = select("select object_name from user_objects where status != 'VALID' and object_type = ?",
                new Object[]{objectType});
        try {
            while (cursor.next()) {
                objects.add(cursor.getString(1));
            }
        } finally {
            cursor.getStatement().close();
        }
    }

    private void checkConnection() throws SQLScriptException {
        if (con == null) {
            throw new SQLScriptException("Not connected");
        }
    }
}
