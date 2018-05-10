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
import java.sql.Connection;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.server.SrvRunParams;

public class DelegateDbQueries implements IDbQueries {
    private final Object owner;
    private final IDatabaseConnectionAccess acc;
    private final List<PreparedStatement> list = new ArrayList<>();
    private final List<Stmt> stmts = new ArrayList<>();

    public DelegateDbQueries(final Object owner, final IDatabaseConnectionAccess acc) {
        if (owner == null) {
            throw new IllegalArgumentException("Owner can't be null");
        }
        else {
            this.owner = owner;     this.acc = acc;
            prepareOwnerInfo(owner,owner.getClass());
        }
    }

    public DelegateDbQueries(final Object owner, final Class ownerClass, final IDatabaseConnectionAccess acc) {
        if (owner == null) {
            throw new IllegalArgumentException("Owner can't be null");
        }
        else if (ownerClass == null) {
            throw new IllegalArgumentException("Owner class can't be null");
        }
        else {
            this.owner = owner;     this.acc = acc;
            prepareOwnerInfo(owner,ownerClass);
        }
    }

    @Override
    public Iterable<PreparedStatement> getPreparedStatements() {
        if (!SrvRunParams.isInTestEnvironment()) {
            throw new IllegalStateException("Attempt to use getPreparedStatements() method in the production mode! It's available in the test environment only!");
        }
        else {
            return list;
        }
    }

    @Override
    public Iterable<String> getPeparedStatementsSourceSQL() {
        final List<String> result = new ArrayList<>();

        for (Stmt item : stmts) {
            result.add(item.getText());
        }
        return result;
    }

    @Override
    public int getPreparedStatementsCount() {
        return stmts.size();
    }

    @Override
    public int getTotalPreparedStatementsCount() {
        return stmts.size();
    }

    @Override
    public void prepareAll() throws SQLException {
        if (!SrvRunParams.isInTestEnvironment()) {
            throw new IllegalStateException("Attempt to use prepareAll() method in the production mode! It's available in the test environment only!");
        }
        else {
            prepareAll(acc.getConnection());
        }
    }

    @Override
    public void prepareAll(Connection conn) throws SQLException {
        if (!SrvRunParams.isInTestEnvironment()) {
            throw new IllegalStateException("Attempt to use prepareAll(Connection) method in the production mode! It's available in the test environment only!");
        }
        else {
            final boolean autoCommit = conn.getAutoCommit();

            try{conn.setAutoCommit(false);
                for (Stmt item : stmts) {
                    try{final PreparedStatement pstmt = ((RadixConnection)conn).prepareStatement(item);

                        try{final ParameterMetaData pmd = pstmt.getParameterMetaData();

                            for (int index = 1; index < pmd.getParameterCount(); index++) {
                                pstmt.setObject(index, null);
                            }
                            pstmt.execute();
                        } catch (SQLException exc) {
                            if (!AbstractDbQueries.validErrorMessage(exc.getMessage())) {
                                throw new IllegalArgumentException("Prepared statement has wrong SQL text string ["+item.getText()+"] : "+exc.getMessage());
                            }
                        } finally {
                            pstmt.clearParameters();
                        }
                        list.add(pstmt);
                    } catch (SQLException exc) {
                        throw new IllegalArgumentException("Prepared statement has wrong SQL text string ["+item.getText()+"] : "+exc.getMessage());
                    }
                }
            } finally {
                conn.rollback();
                conn.setAutoCommit(autoCommit);
            }
        }        
    }

    @Override
    public void closeAll() {
        for (PreparedStatement item : list) {
            try{item.close();
            } catch (SQLException ex) {
            }
        }
        list.clear();
    }

    private void prepareOwnerInfo(final Object owner, final Class<?> ownerClass) {
        for (Field f : ownerClass.getDeclaredFields()) {
            if (Stmt.class.isAssignableFrom(f.getType()) && !f.isAnnotationPresent(DontCheckDbQuery.class)) {
                try{final Field str = ownerClass.getDeclaredField(f.getName()+"SQL");
                    f.setAccessible(true);
                    stmts.add((Stmt) f.get(owner));
                } catch (NoSuchFieldException ex) {
                    throw new IllegalStateException("Field ["+f.getName()+"] doesn't have appropriative SQL text field ["+f.getName()+"SQL]");
                } catch (SecurityException | IllegalAccessException ex) {
                }
            }
        }
    }
}
