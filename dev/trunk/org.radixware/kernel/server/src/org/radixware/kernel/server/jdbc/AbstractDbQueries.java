/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */

package org.radixware.kernel.server.jdbc;

import java.lang.reflect.Field;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDbQueries implements IDbQueries {
    protected AbstractDbQueries(){}

    @Override
    public Iterable<PreparedStatement> getPreparedStatements() {
        final List<PreparedStatement> result = new ArrayList<>();
        
        for (Field f : this.getClass().getDeclaredFields()) {
            if (PreparedStatement.class.isAssignableFrom(f.getType()) && !f.isAnnotationPresent(DontCheckDbQuery.class)) {
                try{this.getClass().getDeclaredField(f.getName()+"SQL");
                } catch (NoSuchFieldException ex) {
                    throw new IllegalStateException("Field ["+f.getName()+"] doesn't have appropriative SQL text field ["+f.getName()+"SQL]");
                } catch (SecurityException ex) {
                }
                try{f.setAccessible(true);
                    final PreparedStatement stmt = (PreparedStatement) f.get(this);
                    if (stmt != null) {
                       result.add(stmt);
                    }
                } catch (IllegalAccessException ex) {
                }
            }
        }
        return result;
    }

    @Override
    public Iterable<String> getPeparedStatementsSourceSQL() {
        final List<String> result = new ArrayList<>();
        
        for (Field f : this.getClass().getDeclaredFields()) {
            if (PreparedStatement.class.isAssignableFrom(f.getType()) && !f.isAnnotationPresent(DontCheckDbQuery.class)) {
                try{final Field str = this.getClass().getDeclaredField(f.getName()+"SQL");
                    
                    str.setAccessible(true);
                    final String value = (String) str.get(null);
                    
                    if (value != null) {
                        result.add(value);
                    }
                } catch (NoSuchFieldException ex) {
                    throw new IllegalStateException("Field ["+f.getName()+"] doesn't have appropriative SQL text field ["+f.getName()+"SQL]");
                } catch (SecurityException | IllegalAccessException ex) {
                }
            }
        }
        return result;
    }

    @Override
    public int getPreparedStatementsCount() {
        int count = 0;
        
        for (Field f : this.getClass().getDeclaredFields()) {
            if (PreparedStatement.class.isAssignableFrom(f.getType()) && !f.isAnnotationPresent(DontCheckDbQuery.class)) {
                count++;
            }
        }
        return count;
    }

    @Override
    public int getTotalPreparedStatementsCount() {
        int count = 0;
        
        for (Field f : this.getClass().getDeclaredFields()) {
            if (PreparedStatement.class.isAssignableFrom(f.getType())) {
                count++;
            }
        }
        return count;
    }
    
    @Override
    public void prepareAll(Connection conn) throws SQLException {
        try{for (Field f : this.getClass().getDeclaredFields()) {
                if (PreparedStatement.class.isAssignableFrom(f.getType()) && !f.isAnnotationPresent(DontCheckDbQuery.class)) {
                    try{final Field sql = this.getClass().getDeclaredField(f.getName()+"SQL");

                        sql.setAccessible(true);
                        final String sqlText = (String) sql.get(null);

                        if (sqlText != null && !sqlText.isEmpty()) {

                            try{final PreparedStatement pstmt = CallableStatement.class.isAssignableFrom(f.getType()) ? prepareCall(conn,sqlText) : prepareQuery(conn,sqlText);
                            
                                try{final ParameterMetaData pmd = pstmt.getParameterMetaData();
                                
                                    for (int index = 1; index <= pmd.getParameterCount(); index++) {
                                        pstmt.setObject(index, null);
                                    }
                                    pstmt.execute();
                                } catch (SQLException exc) {
                                    if (!validErrorMessage(exc.getMessage())) {
                                        throw new IllegalArgumentException("Prepared statement field ["+f.getName()+"] has wrong SQL text string ["+f.getName()+"SQL]="+sqlText+" : "+exc.getMessage());
                                    }
                                } finally {
                                    pstmt.clearParameters();
                                }

                                f.setAccessible(true);
                                f.set(this,pstmt);
                            } catch (SQLException exc) {
                                throw new IllegalArgumentException("Prepared statement field ["+f.getName()+"] has wrong SQL text string ["+f.getName()+"SQL]="+sqlText+" : "+exc.getMessage());
                            }
                        }
                        else {
                            throw new IllegalArgumentException("Prepared statement field ["+f.getName()+"] has null or empty appropriative SQL text string ["+f.getName()+"SQL]");
                        }
                    } catch (NoSuchFieldException ex) {
                        throw new IllegalStateException("Field ["+f.getName()+"] doesn't have appropriative SQL text field ["+f.getName()+"SQL]");
                    } catch (SecurityException | IllegalAccessException ex) {
                    }
                }
            }
        } finally {
            conn.rollback();
        }
    }

    @Override
    public void closeAll() {
        for (Field f : this.getClass().getDeclaredFields()) {
            if (PreparedStatement.class.isAssignableFrom(f.getType()) && !f.isAnnotationPresent(DontCheckDbQuery.class)) {
                try{f.setAccessible(true);
                    final PreparedStatement pstmt = ((PreparedStatement) f.get(this));
                    
                    if (pstmt != null) {
                        pstmt.close();
                        f.set(this,null);
                    }
                } catch (SQLException | SecurityException | IllegalAccessException ex) {
                }
            }
        }
    }

    public static boolean validErrorMessage(final String errorMessage) {
        return errorMessage.toLowerCase().contains("missing in or out parameter") || errorMessage.toLowerCase().contains("cannot insert null") || errorMessage.toLowerCase().contains("no data found") || errorMessage.toLowerCase().contains("numeric or value error");
    }    
    
    protected PreparedStatement prepareQuery(final Connection conn, final String query) throws SQLException {
        if (conn == null) {
            throw new SQLException("Database connection is not established");
        }
        else if (query == null || query.isEmpty()) {
            throw new IllegalArgumentException("Attempt to prepare null or empty SQL query string!");
        }
        else {
            return conn.prepareStatement(query);
        }
    }

    protected CallableStatement prepareCall(final Connection conn, final String query) throws SQLException {
        if (conn == null) {
            throw new SQLException("Database connection is not established");
        }
        else if (query == null || query.isEmpty()) {
            throw new IllegalArgumentException("Attempt to prepare null or empty SQL query string!");
        }
        else {
            return conn.prepareCall(query);
        }
    }
    
}
