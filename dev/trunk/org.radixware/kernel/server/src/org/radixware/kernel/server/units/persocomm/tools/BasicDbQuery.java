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
package org.radixware.kernel.server.units.persocomm.tools;

import org.radixware.kernel.server.units.persocomm.interfaces.IDatabaseConnectionAccess;
import org.radixware.kernel.server.units.persocomm.interfaces.IExtendedDbQueries;
import java.lang.reflect.Field;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.radixware.kernel.common.types.IKernelEnum;
import twitter4j.internal.logging.Logger;

public class BasicDbQuery implements IExtendedDbQueries {

    private static final Set<Class<?>> WRAPPERS = new HashSet<Class<?>>() {
        {
            add(Byte.class);
            add(Short.class);
            add(Integer.class);
            add(Long.class);
            add(Float.class);
            add(Double.class);
            add(Boolean.class);
        }
    };

    private final IDatabaseConnectionAccess access;
    private final Stmt[] repo;
    private final int[] repoIds;
    private final PreparedStatement[] shadow;

    public BasicDbQuery(final IDatabaseConnectionAccess access, final Stmt[] repo /* Need to be ordered by statement id!*/) {
        if (access == null) {
            throw new IllegalArgumentException("Connection access interface can't be null");
        } else if (repo == null || repo.length == 0) {
            throw new IllegalArgumentException("Statements repo array can't be null or empty");
        } else {
            int id = Integer.MIN_VALUE;

            for (Stmt item : repo) {
                if (item.getStmtId() < id) {
                    throw new IllegalArgumentException("Unsorted statement repo array was detected in the parameter list");
                } else if (item.getStmtId() == id) {
                    throw new IllegalArgumentException("Duplicate statement id for the [" + id + "] was dectected, text is " + item.getText());
                }
                /*
                 else {
                 id = item.getStmtId();

                 try {
                 if (access.needCloseConnection()) {
                 try (final Connection conn = access.getConnection();
                 final PreparedStatement pstmt = conn.prepareStatement(item.getText())) {
                 }
                 } else {
                 try (final PreparedStatement pstmt = access.getConnection().prepareStatement(item.getText())) {
                 }
                 }
                 } catch (SQLException e) {
                 throw new IllegalArgumentException("Statement id [" + id + "], text is " + item.getText() + " - SQL exception on preparation: " + e.getMessage());
                 }
                 }
                 */
            }
            this.access = access;
            this.repo = repo;
            this.shadow = new PreparedStatement[repo.length];
            this.repoIds = new int[repo.length];

            for (int index = 0; index < this.repo.length; index++) {
                this.repoIds[index] = this.repo[index].getStmtId();
            }
        }
    }

    protected IDatabaseConnectionAccess getConnectionAccess() {
        return access;
    }

    @Override
    public int size() {
        return repo.length;
    }

    @Override
    public Iterable<Integer> getAllIdentifiers() {
        return new Iterable<Integer>() {
            @Override
            public Iterator<Integer> iterator() {
                return new Iterator<Integer>() {
                    private int index = 0;

                    @Override
                    public boolean hasNext() {
                        return index < repo.length;
                    }

                    @Override
                    public Integer next() {
                        return repo[index++].getStmtId();
                    }

                    @Override
                    public void remove() {
                    }
                };
            }
        };
    }

    @Override
    public Iterable<Integer> getUsedIdentifiers() throws SQLException {
        int count = 0;
        for (PreparedStatement item : shadow) {
            if (item != null && !item.isClosed()) {
                count++;
            }
        }

        final int[] ids = new int[count];

        count = 0;
        for (int index = 0; index < repo.length; index++) {
            if (shadow[index] != null && !shadow[index].isClosed()) {
                ids[count++] = repo[index].getStmtId();
            }
        }

        return new Iterable<Integer>() {
            @Override
            public Iterator<Integer> iterator() {
                return new Iterator<Integer>() {
                    private int index = 0;

                    @Override
                    public boolean hasNext() {
                        return index < ids.length;
                    }

                    @Override
                    public Integer next() {
                        return ids[index++];
                    }

                    @Override
                    public void remove() {
                    }
                };
            }
        };
    }

    @Override
    public String getStatementText(int stmtId) {
        final int location = Arrays.binarySearch(repoIds, stmtId);

        if (location < 0) {
            throw new IllegalArgumentException();
        } else {
            return repo[location].getText();
        }
    }

    @Override
    public PreparedStatement getPreparedStatement(int stmtId) throws SQLException {
        final int location = Arrays.binarySearch(repoIds, stmtId);

        if (location < 0) {
            throw new IllegalArgumentException();
        } else {
            if (shadow[location] == null || shadow[location].isClosed() || shadow[location].getConnection().isClosed()) {
                shadow[location] = getConnectionAccess().getConnection().prepareStatement(getStatementText(stmtId));
            }
            return shadow[location];
        }
    }

    @Override
    public Iterable<PreparedStatement> getUsedStatements() throws SQLException {
        int count = 0;
        for (PreparedStatement item : shadow) {
            if (item != null && !item.isClosed()) {
                count++;
            }
        }

        final PreparedStatement[] stmts = new PreparedStatement[count];

        count = 0;
        for (int index = 0; index < repo.length; index++) {
            if (shadow[index] != null && !shadow[index].isClosed()) {
                stmts[count++] = shadow[index];
            }
        }

        return new Iterable<PreparedStatement>() {
            @Override
            public Iterator<PreparedStatement> iterator() {
                return new Iterator<PreparedStatement>() {
                    private int index = 0;

                    @Override
                    public boolean hasNext() {
                        return index < stmts.length;
                    }

                    @Override
                    public PreparedStatement next() {
                        return stmts[index++];
                    }

                    @Override
                    public void remove() {
                    }
                };
            }
        };
    }

    @Override
    public void execute(final int stmtId, final Object... parameters) throws SQLException {
        bound(getPreparedStatement(stmtId), parameters).executeUpdate();
    }

    @Override
    public <T> IDbCursor<T> query(final Class<T> awaited, final int stmtId, final Object... parameters) throws SQLException {
        return createCursor(bound(getPreparedStatement(stmtId), parameters).executeQuery(), awaited, stmtId);
    }

    @Override
    public <T> IDbCursor<T> query(final Class<T> awaited, final T fill, final int stmtId, final Object... parameters) throws SQLException {
        return createCursor(bound(getPreparedStatement(stmtId), parameters).executeQuery(), awaited, fill, stmtId);
    }

    @Override
    public <T> int query(Class<T> awaited, IDbCallback<T> callback, int statement, Object... parameters) throws SQLException {
        try (final IDbCursor<T> query = query(awaited, statement, parameters)) {
            if (!query.isEmpty()) {
                for (T item : query.list(awaited)) {
                    if (!callback.process(query.actualId(), item)) {
                        break;
                    }
                }
                return query.totalRead();
            } else {
                return 0;
            }
        } catch (Exception exc) {
            exc.printStackTrace();
            throw new SQLException(exc.getMessage(), exc);
        }
    }

    @Override
    public <T> int query(Class<T> awaited, IDbCallback<T> callback, T fill, int statement, Object... parameters) throws SQLException {
        try (final IDbCursor<T> query = query(awaited, fill, statement, parameters)) {
            if (!query.isEmpty()) {
                for (T item : query.list(awaited)) {
                    if (!callback.process(query.actualId(), item)) {
                        break;
                    }
                }
                return query.totalRead();
            } else {
                return 0;
            }
        } catch (Exception exc) {
            throw new SQLException(exc.getMessage(), exc);
        }
    }

    @Override
    public boolean exists(int statement, Object... parameters) throws SQLException {
        try (final IDbCursor<Object> cursor = query(Object.class, statement, parameters)) {
            return !cursor.isEmpty();
        } catch (SQLException exc) {
            throw exc;
        } catch (Exception exc) {
            throw new SQLException(exc);
        }
    }

    @Override
    public <T> T load(Class<T> awaited, int statement, Object... parameters) throws SQLException {
        try (final IDbCursor<T> cursor = query(awaited, statement, parameters)) {
            if (cursor.isEmpty()) {
                throw new IllegalStateException("No any data was selected for the load method call. Stmt=[" + getStatementText(statement) + "], parameters are " + Arrays.toString(parameters));
            }
            T result = null;

            Iterable<T> x = cursor.list(awaited);
            for (T item : x) {
                result = item;
            }

            if (cursor.totalRead() > 1) {
                throw new IllegalStateException("More than 1 record was selected for the load method call. Stmt=[" + getStatementText(statement) + ", parameters are " + Arrays.toString(parameters));
            } else {
                return result;
            }
        } catch (SQLException exc) {
            throw exc;
        } catch (Exception exc) {
            throw new SQLException(exc);
        }
    }

    @Override
    public <T> T load(Class<T> awaited, T fill, int statement, Object... parameters) throws SQLException {
        try (final IDbCursor<T> cursor = query(awaited, fill, statement, parameters)) {
            if (cursor.isEmpty()) {
                throw new IllegalStateException("No any data was selected for the load method call. Stmt=[" + getStatementText(statement) + "], parameters are " + Arrays.toString(parameters));
            }
            T result = null;

            Iterable<T> x = cursor.list(awaited);
            for (T item : x) {
                result = item;
            }

            if (cursor.totalRead() > 1) {
                throw new IllegalStateException("More than 1 record was selected for the load method call. Stmt=[" + getStatementText(statement) + "], parameters are " + Arrays.toString(parameters));
            } else {
                return result;
            }
        } catch (SQLException exc) {
            throw exc;
        } catch (Exception exc) {
            throw new SQLException(exc);
        }
    }

    @Override
    public void closeAll() {
        try {
            for (PreparedStatement item : getUsedStatements()) {
                try {
                    item.close();
                } catch (SQLException ex) {
                };
            }
        } catch (SQLException ex) {
        }
    }

    protected <T> IDbCursor<T> createCursor(final ResultSet rs, final Class<T> awaited, final int stmtId) throws SQLException {
        return this.new InternalDbCursor(rs, awaited, stmtId);
    }

    protected <T> IDbCursor<T> createCursor(final ResultSet rs, final Class<T> awaited, final T fill, final int stmtId) throws SQLException {
        return this.new InternalDbCursor(rs, awaited, fill, stmtId);
    }

    protected <T> T createRecord(final ResultSet rs, final Class<T> awaited, final Map<String, Field> fiedls, final int stmtId, final T template) throws Exception {
        final ResultSetMetaData rsmd = rs.getMetaData();

        next:
        for (int index = 1; index <= rsmd.getColumnCount(); index++) {
            if (fiedls.containsKey(rsmd.getColumnName(index).toUpperCase())) {
                final Field desc = fiedls.get(rsmd.getColumnName(index).toUpperCase());

                try {
                    if (desc.getType().isPrimitive()) {
                        switch (desc.getType().getSimpleName()) {
                            case "byte":
                                desc.setByte(template, rs.getByte(index));
                                break;
                            case "short":
                                desc.setShort(template, rs.getShort(index));
                                break;
                            case "int":
                                desc.setInt(template, rs.getInt(index));
                                break;
                            case "long":
                                desc.setLong(template, rs.getLong(index));
                                break;
                            case "float":
                                desc.setFloat(template, rs.getFloat(index));
                                break;
                            case "double":
                                desc.setDouble(template, rs.getDouble(index));
                                break;
                            case "boolean":
                                desc.setBoolean(template, rs.getBoolean(index));
                                break;
                            default:
                                throw new UnsupportedOperationException("Setting for the [" + desc.getType() + "] primitive class is not implemented");
                        }
                    } else if (WRAPPERS.contains(desc.getType())) {
                        switch (desc.getType().getSimpleName()) {
                            case "Byte":
                                desc.set(template, Byte.valueOf(rs.getByte(index)));
                                break;
                            case "Short":
                                desc.set(template, Short.valueOf(rs.getShort(index)));
                                break;
                            case "Integer":
                                desc.set(template, Integer.valueOf(rs.getInt(index)));
                                break;
                            case "Long":
                                desc.set(template, Long.valueOf(rs.getLong(index)));
                                break;
                            case "Float":
                                desc.set(template, Float.valueOf(rs.getFloat(index)));
                                break;
                            case "Double":
                                desc.set(template, Double.valueOf(rs.getDouble(index)));
                                break;
                            case "Boolean":
                                desc.set(template, Boolean.valueOf(rs.getBoolean(index)));
                                break;
                            default:
                                throw new UnsupportedOperationException("Setting for the [" + desc.getType() + "] primitive class is not implemented");
                        }
                    } else if (IKernelEnum.class.isAssignableFrom(desc.getType())) {
                        final String readed = rs.getString(index);

                        if (!rs.wasNull()) {
                            for (Object t : desc.getType().getEnumConstants()) {
                                if (t instanceof IKernelEnum) {
                                    if (((IKernelEnum) t).getValue() == null && readed == null || ((IKernelEnum) t).getValue().equals(readed)) {
                                        desc.set(template, t);
                                        continue next;
                                    }
                                }
                            }
                            throw new IllegalArgumentException("Value [" + readed + "] doesn't have an appropriative pair in the [" + desc.getType() + "] enumeration for field [" + desc.getName() + "]");
                        }
                    } else if (desc.getType().isEnum()) {
                        final String value = rs.getString(index);

                        if (!rs.wasNull()) {
                            desc.set(template, desc.getType().getMethod("valueOf", String.class).invoke(null, value));
                        }
                    } else {
                        final Object val = rs.getObject(index);

                        desc.set(template, rs.wasNull() ? null : desc.getType().cast(val));
                    }
                } catch (IllegalArgumentException | IllegalAccessException ex) {
                    throw new SQLException("Problem seting value for the  [" + rsmd.getColumnName(index).toUpperCase() + "] field: " + ex.getClass().getSimpleName() + " " + ex.getMessage());
                }
            } else {
                throw new SQLException("Problem seting value: unknown field [" + rsmd.getColumnName(index).toUpperCase() + "] in the class [" + awaited + "]");
            }
        }

        return awaited.cast(template);
    }

    protected long getActualRecordId(final ResultSet rs) throws SQLException {
        try {
            return rs.getLong("id");
        } catch (SQLException exc) {
            throw new SQLException("Can't read recod identifier (possibly [id] field is missing in the query): " + exc.getMessage());
        }
    }

    protected <T> T nvl(final ResultSet rs, final T value, final T defaultValue) throws SQLException {
        return rs.wasNull() ? defaultValue : value;
    }

    private PreparedStatement bound(final PreparedStatement stmt, final Object... parameters) throws SQLException {
        final ParameterMetaData metaData = stmt.getParameterMetaData();

        if (metaData.getParameterCount() != parameters.length) {
            throw new IllegalArgumentException("Different number of parameters in the prepared statement [" + metaData.getParameterCount() + "] and parameter's list [" + parameters.length + "]");
        } else {
            for (int index = 0; index < parameters.length; index++) {
                if (parameters[index] == null) {
                    throw new IllegalArgumentException("Parameter [" + (index + 1) + "] can't be null. Use new Null(datatype) instead!");
                } else if (parameters[index] instanceof Null) {
                    stmt.setNull(index + 1, ((Null) parameters[index]).getType());
                } else if (parameters[index] instanceof String) {
                    stmt.setString(index + 1, (String) parameters[index]);
                } else if (parameters[index] instanceof Long) {
                    stmt.setLong(index + 1, (Long) parameters[index]);
                } else if (parameters[index] instanceof Integer) {
                    stmt.setInt(index + 1, (Integer) parameters[index]);
                } else if (parameters[index] instanceof java.sql.Date) {
                    stmt.setDate(index + 1, (java.sql.Date) parameters[index]);
                } else if (parameters[index] instanceof Timestamp) {
                    stmt.setTimestamp(index + 1, (Timestamp) parameters[index]);
                } else if (parameters[index] instanceof java.util.Date) {
                    stmt.setDate(index + 1, new java.sql.Date(((java.util.Date) parameters[index]).getTime()));
                } else if (parameters[index] instanceof Double) {
                    stmt.setDouble(index + 1, (Double) parameters[index]);
                } else if (parameters[index] instanceof Float) {
                    stmt.setFloat(index + 1, (Float) parameters[index]);
                } else if (parameters[index] instanceof Blob) {
                    stmt.setBlob(index + 1, (Blob) parameters[index]);
                } else if (parameters[index] instanceof Clob) {
                    stmt.setClob(index + 1, (Clob) parameters[index]);
                } else if (parameters[index] instanceof Short) {
                    stmt.setShort(index + 1, (Short) parameters[index]);
                } else if (parameters[index] instanceof Byte) {
                    stmt.setByte(index + 1, (Byte) parameters[index]);
                } else if (parameters[index] instanceof byte[]) {
                    stmt.setBytes(index + 1, (byte[]) parameters[index]);
                } else if (parameters[index] instanceof Boolean) {
                    stmt.setBoolean(index + 1, (Boolean) parameters[index]);
                } else if (parameters[index] instanceof Array) {
                    stmt.setArray(index + 1, (Array) parameters[index]);
                } else {
                    throw new UnsupportedOperationException("Data type [" + parameters[index].getClass() + "] for parameter [" + (index + 1) + "] is not supported yet");
                }
            }
        }

        return stmt;
    }

    public static class Stmt implements Comparable<Stmt> {

        private final int stmtId;
        private final String text;

        public Stmt(final int stmtId) {
            this.stmtId = stmtId;
            this.text = null;
        }

        public Stmt(final int stmtId, final String text) {
            this.stmtId = stmtId;
            this.text = text;
        }

        @Override
        public int compareTo(Stmt arg0) {
            return this.getStmtId() - arg0.getStmtId();
        }

        public int getStmtId() {
            return stmtId;
        }

        ;
        public String getText() {
            return text;
        }
    ;

    }

    protected static class Null {

        private final int sqlType;

        public Null(final int sqlType) {
            this.sqlType = sqlType;
        }

        public int getType() {
            return sqlType;
        }
    }

    protected class InternalDbCursor<T> implements IDbCursor<T> {

        private final ResultSet rs;
        private final Class<T> awaited;
        private final int stmtId;
        private final T area;
        private final boolean isEmpty;
        private int readed = 0;
        private SQLException exc = null;
        private Map<String, Field> classFields = new HashMap<String, Field>();

        public InternalDbCursor(final ResultSet rs, final Class<T> awaited, final int stmtId) throws SQLException {
            this.rs = rs;
            this.awaited = awaited;
            this.stmtId = stmtId;
            isEmpty = !rs.next();

            try {
                this.area = awaited.newInstance();
                for (Field item : awaited.getFields()) {
                    item.setAccessible(true);
                    classFields.put(item.getName().toUpperCase(), item);
                }
            } catch (InstantiationException | IllegalAccessException ex) {
                throw new SQLException("Class instance [" + awaited + "] can't be created: " + ex.getMessage() + ". Check visibilities for default constructor of the class");
            }
        }

        public InternalDbCursor(final ResultSet rs, final Class<T> awaited, final T area, final int stmtId) throws SQLException {
            this.rs = rs;
            this.awaited = awaited;
            this.stmtId = stmtId;
            isEmpty = !rs.next();

            this.area = area;
            for (Field item : awaited.getFields()) {
                item.setAccessible(true);
                classFields.put(item.getName().toUpperCase(), item);
            }
        }

        @Override
        public void close() throws Exception {
            rs.close();
            if (exc != null) {
                throw exc;
            }
        }

        @Override
        public boolean isEmpty() {
            return isEmpty;
        }

        @Override
        public int totalRead() {
            return readed;
        }

        @Override
        public <T> Iterable<T> list(final Class<T> awaited) {
            if (isEmpty()) {
                return new Iterable<T>() {
                    @Override
                    public Iterator<T> iterator() {
                        return new Iterator<T>() {
                            @Override
                            public boolean hasNext() {
                                return false;
                            }

                            @Override
                            public T next() {
                                return null;
                            }

                            @Override
                            public void remove() {
                            }
                        };
                    }
                };
            } else {
                return new Iterable<T>() {
                    @Override
                    public Iterator<T> iterator() {
                        return new Iterator<T>() {
                            private boolean first = true;
                            private T result;

                            @Override
                            public boolean hasNext() {
                                try {
                                    if (first) {
                                        result = createRecord(rs, awaited, classFields, stmtId, awaited.cast(area));
                                        first = false;
                                        return true;
                                    } else {
                                        if (rs.next()) {
                                            result = createRecord(rs, awaited, classFields, stmtId, awaited.cast(area));
                                            return true;
                                        } else {
                                            return false;
                                        }
                                    }
                                } catch (Exception e) {
                                    Logger.getLogger(BasicDbQuery.class).error("Error reading record from db", e);
                                    exc = e instanceof SQLException ? (SQLException) e : new SQLException(e.getMessage(), e);
                                    return false;
                                }
                            }

                            @Override
                            public T next() {
                                readed++;
                                return result;
                            }

                            @Override
                            public void remove() {
                            }
                        };
                    }
                };
            }
        }

        @Override
        public <T> List<T> collection(final Class<T> awaited) throws SQLException {
            final List<T> result = new ArrayList<T>();

            if (!isEmpty) {
                try {
                    do {
                        result.add(createRecord(rs, awaited, classFields, stmtId, awaited.cast(area)));
                        readed++;
                    } while (rs.next());
                } catch (SQLException ex) {
                    throw ex;
                } catch (Exception ex) {
                    throw new SQLException(ex.getMessage(), ex);
                }
            }

            return result;
        }

        @Override
        public Long actualId() throws SQLException {
            return getActualRecordId(rs);
        }
    }
}
