/*
 * Copyright (c) 2008-2018, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. This Source Code is distributed
 * WITHOUT ANY WARRANTY; including any implied warranties but not limited to
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Mozilla Public License, v. 2.0. for more details.
 */
package org.radixware.kernel.server.jdbc;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.radixware.kernel.common.types.IKernelEnum;
import twitter4j.internal.logging.Logger;
import org.radixware.kernel.common.enums.EDatabaseType;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.types.IKernelCharEnum;
import org.radixware.kernel.common.types.IKernelIntEnum;
import org.radixware.kernel.common.types.IKernelStrEnum;
import org.radixware.kernel.common.utils.Maps;
import org.radixware.kernel.server.dbq.OraExCodes;
import org.radixware.kernel.server.exceptions.SqlNoDataFound;
import org.radixware.kernel.server.exceptions.SqlResourceBusy;

public class ExtendedDbQueries implements IExtendedDbQueries {

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
    private boolean keeping = true;

    public ExtendedDbQueries(final Connection conn, final Stmt[] repo) {
        this(new IDatabaseConnectionAccess() {
            @Override
            public EDatabaseType getDatabaseType() {
                return EDatabaseType.ORACLE;
            }

            @Override
            public Connection getConnection() throws SQLException {
                return conn;
            }
        }, repo);
    }

    public ExtendedDbQueries(final IDatabaseConnectionAccess access, final Stmt[] repo) {
        if (access == null) {
            throw new IllegalArgumentException("Connection access interface can't be null");
        } else if (repo == null || repo.length == 0) {
            throw new IllegalArgumentException("Statements repo array can't be null or empty");
        } else {
            int id = Integer.MIN_VALUE;

            this.repo = repo.clone();
            Arrays.sort(this.repo, new Comparator<Stmt>() {
                @Override
                public int compare(Stmt o1, Stmt o2) {
                    return o1.stmtId - o2.stmtId;
                }
            });

            for (Stmt item : this.repo) {
                if (item.getStmtId() == id) {
                    throw new IllegalArgumentException("Duplicate statement id for the [" + id + "] was dectected, text is " + item.getText());
                }
                id = item.getStmtId();
            }
            this.access = access;
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
    public Iterable<PreparedStatement> getPreparedStatements() {
        try {
            return getUsedStatements();
        } catch (SQLException ex) {
            throw new IllegalArgumentException("Error getting prepared statements: " + ex.getMessage());
        }
    }

    @Override
    public Iterable<String> getPeparedStatementsSourceSQL() {
        final List<String> result = new ArrayList<>();

        for (Stmt item : repo) {
            result.add(item.text);
        }
        return result;
    }

    @Override
    public int getPreparedStatementsCount() {
        return size();
    }

    @Override
    public int getTotalPreparedStatementsCount() {
        return size();
    }

    @Override
    public void prepareAll() throws SQLException {
        prepareAll(getConnectionAccess().getConnection());
    }

    @Override
    public void prepareAll(final Connection conn) throws SQLException {
        if (conn == null) {
            throw new IllegalArgumentException("Cannection can't be null!");
        } else {
            final boolean autoCommit = conn.getAutoCommit();

            try {
                conn.setAutoCommit(false);
                for (Stmt item : repo) {
                    final PreparedStatement pstmt = getPreparedStatement(item.stmtId);

                    try {
                        final ParameterMetaData pmd = pstmt.getParameterMetaData();

                        for (int index = 1; index < pmd.getParameterCount(); index++) {
                            pstmt.setObject(index, null);
                        }
                        pstmt.execute();
                        pstmt.clearParameters();
                    } catch (SQLException exc) {
                        if (!validErrorMessage(exc.getMessage())) {
                            throw new IllegalArgumentException("Prepared statement id [" + item.stmtId + "] has wrong SQL text string =" + item.text + " : " + exc.getMessage());
                        }
                    } finally {
                        pstmt.clearParameters();
                    }
                }
            } finally {
                conn.setAutoCommit(autoCommit);
            }
        }
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
            throw new IllegalArgumentException("Unknown statement id [" + stmtId + "]");
        } else {
            try {
                return ((RadixConnection) getConnectionAccess().getConnection()).normalizeSQLString(repo[location]);
            } catch (SQLException ex) {
                throw new IllegalArgumentException("Error converting statement string [" + repo[location].getText() + "] to the database-specific form: " + ex.getMessage());
            }
        }
    }

    @Override
    public PreparedStatement getPreparedStatement(int stmtId) throws SQLException {
        return getPreparedStatement(stmtId, true);
    }

    @Override
    public PreparedStatement getPreparedStatement(int stmtId, boolean keep) throws SQLException {
        final int location = Arrays.binarySearch(repoIds, stmtId);

        if (location < 0) {
            throw new IllegalArgumentException("Unknown statement id [" + stmtId + "]");
        } else if (!keep) {
            return getConnectionAccess().getConnection().prepareStatement(getStatementText(stmtId));
        } else {
            if (shadow[location] == null || shadow[location].isClosed() || shadow[location].getConnection().isClosed()) {
                shadow[location] = getConnectionAccess().getConnection().prepareStatement(getStatementText(stmtId));
            }
            return shadow[location];
        }
    }

    @Override
    public boolean isKeeping() {
        return keeping;
    }

    @Override
    public void sekKeeping(final boolean keep) {
        keeping = keep;
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
    public int execute(final int stmtId, final Object... parameters) throws SQLException {
        if (isKeeping()) {
            return bound(getPreparedStatement(stmtId), parameters).executeUpdate();
        } else {
            try (final PreparedStatement pstmt = getPreparedStatement(stmtId, false)) {
                return bound(pstmt, parameters).executeUpdate();
            }
        }
    }

    @Override
    public int query(final IResultSetCallback callback, final int statement, final Object... parameters) throws SQLException {
        if (callback == null) {
            throw new IllegalArgumentException("Callback can't be null");
        } else if (Arrays.binarySearch(repoIds, statement) < 0) {
            throw new IllegalArgumentException("Unknown statement id [" + statement + "]");
        } else if (isKeeping()) {
            try (final ResultSet rs = bound(getPreparedStatement(statement), parameters).executeQuery()) {
                int count = 0;

                while (rs.next()) {
                    count++;
                    if (!callback.process(rs)) {
                        break;
                    }
                }
                return count;
            }
        } else {
            try (final PreparedStatement pstmt = getPreparedStatement(statement, false);
                    final ResultSet rs = bound(pstmt, parameters).executeQuery()) {
                int count = 0;

                while (rs.next()) {
                    count++;
                    if (!callback.process(rs)) {
                        break;
                    }
                }
                return count;
            }
        }
    }

    @Override
    public <T> IDbCursor<T> query(final Class<T> awaited, final int stmtId, final Object... parameters) throws SQLException {
        return createCursor(bound(getPreparedStatement(stmtId, isKeeping()), parameters).executeQuery(), awaited, stmtId);
    }

    @Override
    public <T> IDbCursor<T> query(final Class<T> awaited, final T fill, final int stmtId, final Object... parameters) throws SQLException {
        return createCursor(bound(getPreparedStatement(stmtId, isKeeping()), parameters).executeQuery(), awaited, fill, stmtId);
    }
    
    private <T> T fetchSingleRecord(Class<T> awaited, int statement, boolean nullable, Object... parameters) throws Exception {
        try (IExtendedDbQueries.IDbCursor<T> cursor = query(awaited, statement, parameters)) {
            for (T item : cursor.list(awaited)) {
                return item;
            }
        } catch (SQLException ex) {
//            if (ex.getErrorCode() == OraExCodes.NO_DATA_FOUND) {
//                throw new SqlNoDataFound(ex);
//            } else
            if (ex.getErrorCode() == OraExCodes.RESOURCE_BUSY) {
                throw new SqlResourceBusy(ex);
            }
            throw ex;
        }
        if (nullable) {
            return null;
        } else {
            throw new SqlNoDataFound();
        }
    }
    
    @Override
    public <T> T record(Class<T> awaited, int statement, Object... parameters) throws Exception {
        return fetchSingleRecord(awaited, statement, false, parameters);
    }
    
    @Override
    public <T> T recordOrNull(Class<T> awaited, int statement, Object... parameters) throws Exception {
        return fetchSingleRecord(awaited, statement, true, parameters);
    }

    @Override
    public <T> int query(Class<T> awaited, IDbCallback<T> callback, int statement, Object... parameters) throws SQLException {
        if (awaited == null) {
            throw new IllegalArgumentException("Awaited class can't be null");
        } else if (callback == null) {
            throw new IllegalArgumentException("Callback can't be null");
        } else if (Arrays.binarySearch(repoIds, statement) < 0) {
            throw new IllegalArgumentException("Unknown statement id [" + statement + "]");
        } else {
            try {
                return query(awaited, callback, awaited.newInstance(), statement, parameters);
            } catch (InstantiationException | IllegalAccessException ex) {
                throw new SQLException("Can't create instance for the class [" + awaited.getName() + "] to store data in it. Check default constructor presence and accessibility");
            }
        }
    }

    @Override
    public <T> int query(Class<T> awaited, IDbCallback<T> callback, T fill, int statement, Object... parameters) throws SQLException {
        if (awaited == null) {
            throw new IllegalArgumentException("Awaited class can't be null");
        } else if (callback == null) {
            throw new IllegalArgumentException("Callback can't be null");
        } else if (fill == null) {
            throw new IllegalArgumentException("Fill store can't be null");
        } else if (Arrays.binarySearch(repoIds, statement) < 0) {
            throw new IllegalArgumentException("Unknown statement id [" + statement + "]");
        } else {
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
                throw new SQLException("SQL error in [" + getStatementText(statement) + "]: " + exc.getMessage());
            }
        }
    }

    @Override
    public boolean exists(int statement, Object... parameters) throws SQLException {
        if (Arrays.binarySearch(repoIds, statement) < 0) {
            throw new IllegalArgumentException("Unknown statement id [" + statement + "]");
        } else {
            return query(new IResultSetCallback() {
                @Override
                public boolean process(ResultSet rs) {
                    return false;
                }
            }, statement, parameters) > 0;
        }
    }

    @Override
    public <T> T load(Class<T> awaited, int statement, Object... parameters) throws SQLException {
        if (awaited == null) {
            throw new IllegalArgumentException("Awaited class can't be null");
        } else if (Arrays.binarySearch(repoIds, statement) < 0) {
            throw new IllegalArgumentException("Unknown statement id [" + statement + "]");
        } else {
            try {
                final T fill = awaited == Long.class ? null : awaited.newInstance();
                return load(awaited, fill, statement, parameters);
            } catch (InstantiationException | IllegalAccessException ex) {
                throw new SQLException("Can't create instance for the class [" + awaited.getName() + "] to store data in it. Check default constructor presence and accessibility");
            }
        }
    }

    @Override
    public <T> T load(Class<T> awaited, T fill, int statement, Object... parameters) throws SQLException {
        if (awaited == null) {
            throw new IllegalArgumentException("Awaited class can't be null");
        } else if (Arrays.binarySearch(repoIds, statement) < 0) {
            throw new IllegalArgumentException("Unknown statement id [" + statement + "]");
        } else if (fill == null && awaited != Long.class) {
            throw new IllegalArgumentException("Fill store can't be null");
        } else {
            try (final IDbCursor<T> cursor = query(awaited, fill, statement, parameters)) {
                if (cursor.isEmpty()) {
                    throw new SQLException("No any data was selected for the load method call. Stmt=[" + getStatementText(statement) + "], parameters are " + Arrays.toString(parameters));
                }
                T result = null;

                for (T item : cursor.list(awaited)) {
                    result = item;
                }

                if (cursor.totalRead() > 1) {
                    throw new SQLException("More than 1 record was selected for the load method call. Stmt=[" + getStatementText(statement) + "], parameters are " + Arrays.toString(parameters));
                } else {
                    return result;
                }
            } catch (SQLException exc) {
                throw new SQLException("SQL error in [" + getStatementText(statement) + "]: " + exc.getMessage());
            } catch (Exception exc) {
                throw new SQLException("SQL error in [" + getStatementText(statement) + "]: " + exc.getMessage(), exc);
            }
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

    protected boolean validErrorMessage(final String errorMessage) {
        return errorMessage.contains("Missing IN or OUT parameter");
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
                        if (rs.wasNull()) {
                            desc.set(template, null);
                        }
                    } else if (IKernelEnum.class.isAssignableFrom(desc.getType())) {
                        final IKernelEnum[] enumConstants = (IKernelEnum[])desc.getType().getEnumConstants();
                        // final Object readed = rs.getObject(index);
                        // don't work due to type mapping mismatches (e.g. got: BigDecimal, required: Long)
                        final boolean isIntEnum = enumConstants[0] instanceof IKernelIntEnum;
                        final Object readed = isIntEnum ? rs.getLong(index) : rs.getString(index);
                        if (!rs.wasNull()) {
                            for (IKernelEnum e: enumConstants) {
                                if (e.getValue() == null && readed == null || e.getValue().equals(readed)) {
                                    desc.set(template, e);
                                    continue next;
                                }
                            }
                            throw new IllegalArgumentException("Value [" + readed + "] doesn't have an appropriative pair in the [" + desc.getType() + "] enumeration for field [" + desc.getName() + "]");
                        }
                    } else if (desc.getType().isEnum()) {
                        final String value = rs.getString(index);

                        if (!rs.wasNull()) {
                            desc.set(template, desc.getType().getMethod("valueOf", String.class).invoke(null, value));
                        }
                    } else if (List.class.isAssignableFrom(desc.getType())) {
                        final String value = rs.getString(index);
                        if (!rs.wasNull()) {
                            final Type type = ((ParameterizedType) desc.getGenericType()).getActualTypeArguments()[0];
                            final Class<?> clazz = (Class<?>)type;
                            final List<?> list;
                            if (clazz == String.class) {
                                list = Collections.unmodifiableList(ArrStr.fromValAsStr(value));
                            } else {
                                throw new IllegalArgumentException("List<" + clazz.getCanonicalName() + "> not supported yet");
                            }
                            desc.set(template, list);
                        }
                    } else if (Map.class.isAssignableFrom(desc.getType())) {
                        final String value = rs.getString(index);
                        if (!rs.wasNull()) {
                            final Type typeArgs[] = ((ParameterizedType) desc.getGenericType()).getActualTypeArguments();
                            final Class<?> keyClass = (Class<?>)typeArgs[0];
                            final Class<?> valueClass = (Class<?>) typeArgs[1];
                            final Map<?, ?> map;
                            if (keyClass == String.class && valueClass == String.class) {
                                final Map<String, String> keyValues = Maps.fromKeyValuePairsString(value);
                                map = Collections.unmodifiableMap(keyValues);
                            } else {
                                throw new IllegalArgumentException("Map<" + keyClass.getCanonicalName() + ", " + valueClass.getCanonicalName() + "> not supported yet");
                            }
                            desc.set(template, map);
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
    
    protected Object toSqlParam(String s) {
        return StringUtils.isEmpty(s) ? new Null(Types.VARCHAR) : s;
    }
    
    protected Object toSqlParam(Long l) {
        return l == null ? new Null(Types.BIGINT) : l;
    }
    
    protected Object toSqlParam(Integer i) {
        return i == null ? new Null(Types.BIGINT) : i;
    }
    
    protected Object toSqlParam(IKernelStrEnum e) {
        return e == null || StringUtils.isEmpty(e.getValue()) ? new Null(Types.VARCHAR) : e.getValue();
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
                } else if (parameters[index] instanceof IKernelStrEnum) {
                    stmt.setString(index + 1, ((IKernelStrEnum)parameters[index]).getValue());
                } else if (parameters[index] instanceof IKernelIntEnum) {
                    stmt.setLong(index + 1, ((IKernelIntEnum)parameters[index]).getValue());
                } else if (parameters[index] instanceof IKernelCharEnum) {
                    stmt.setString(index + 1, ((IKernelCharEnum) parameters[index]).getValue().toString());
                }
                else {
                    throw new UnsupportedOperationException("Data type [" + parameters[index].getClass() + "] for parameter [" + (index + 1) + "] is not supported yet");
                }
            }
        }

        return stmt;
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

        private final RadixResultSet rs;
        private final Class<T> awaited;
        private final int stmtId;
        private final T area;
        private final boolean isEmpty;
        private final boolean needClosePstmt = isKeeping();
        private int readed = 0;
        private SQLException exc = null;
        private Map<String, Field> classFields = new HashMap<String, Field>();
        private final Exception classException;

        public InternalDbCursor(final ResultSet rs, final Class<T> awaited, final int stmtId) throws SQLException {
            this.rs = (RadixResultSet)rs;
            this.awaited = awaited;
            this.stmtId = stmtId;
            isEmpty = !rs.next();
            Exception tmpClassException;
            T tmpArea = null;
            try {
                tmpArea = awaited.newInstance();
                for (Field item : awaited.getFields()) {
                    item.setAccessible(true);
                    classFields.put(item.getName().toUpperCase(), item);
                }
                tmpClassException = null;
            } catch (InstantiationException | IllegalAccessException ex) {
                tmpClassException = new SQLException("Class instance [" + awaited + "] can't be created: " + ex.getMessage() + ". Check visibilities for default constructor of the class");
            }
            classException = tmpClassException;
            area = tmpArea;
        }

        public InternalDbCursor(final ResultSet rs, final Class<T> awaited, final T area, final int stmtId) throws SQLException {
            this.rs = (RadixResultSet)rs;
            this.awaited = awaited;
            this.stmtId = stmtId;
            isEmpty = !rs.next();

            this.area = area;
            for (Field item : awaited.getFields()) {
                item.setAccessible(true);
                classFields.put(item.getName().toUpperCase(), item);
            }
            classException = null;
        }

        @Override
        public void close() throws Exception {
            final Statement pstmt = needClosePstmt ? rs.getStatement() : null;

            rs.close();
            if (pstmt != null) {
                pstmt.close();
            }
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
                                    if (classException != null) {
                                        Logger.getLogger(ExtendedDbQueries.class).error("There was class creation exception", classException);
                                    }
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
