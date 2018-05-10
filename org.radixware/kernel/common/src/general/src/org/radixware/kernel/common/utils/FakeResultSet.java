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
package org.radixware.kernel.common.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.CharArrayReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;
import org.apache.poi.ss.usermodel.DateUtil;

public abstract class FakeResultSet implements ResultSet {
    public static final IResultSetLobProvider DEFAULT_LOB_PROVIDER = new IResultSetLobProvider(){
        @Override
        public boolean canGetBlobContent(final String blobReference) {
            if (blobReference != null && !blobReference.isEmpty()) {
                try{new URL(blobReference);
                    return true;
                } catch (MalformedURLException exc) {
                    return false;
                }
            }
            else {
                return false;
            }
        }

        @Override
        public boolean canGetClobContent(final String clobReference) {
            return canGetBlobContent(clobReference);
        }

        @Override
        public byte[] getBlobContent(final String blobReference) throws SQLException {
            if (blobReference == null || blobReference.isEmpty()) {
                throw new SQLException("Can't get BLOB/CLOB content for the given URL: ["+blobReference+"]");
            }
            else {
                try(final InputStream is = new URL(blobReference).openStream();
                    final ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                    
                    byte[] buffer = new byte[8192];                    
                    int     len;
                    
                    while ((len = is.read(buffer)) > 0) {
                        baos.write(buffer,0,len);
                    }
                    baos.flush();
                    return baos.toByteArray();
                } catch (IOException ex) {
                    throw new SQLException("I/O error getting content from URL ["+blobReference+"] : "+ex.getMessage());
                }
            }
        }

        @Override
        public char[] getClobContent(final String clobReference) throws SQLException {
            return new String(getBlobContent(clobReference)).toString().toCharArray();
        }
    };

    
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIME_FORMAT = "HH:mm:ss.SSS";
    private static final String TIMESTAMP_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS";

    private static final Class<?>[] SUPPORTED_CLASSES = {BigDecimal.class, Date.class, Time.class, Timestamp.class, InputStream.class, Reader.class, Ref.class, Blob.class, Clob.class, Array.class, URL.class, RowId.class, NClob.class, SQLXML.class, String.class, Object.class};
    private static final int CLASS_BigDecimal = 0;
    private static final int CLASS_Date = 1;
    private static final int CLASS_Time = 2;
    private static final int CLASS_Timestamp = 3;
    private static final int CLASS_InputStream = 4;
    private static final int CLASS_Reader = 5;
    private static final int CLASS_Ref = 6;
    private static final int CLASS_Blob = 7;
    private static final int CLASS_Clob = 8;
    private static final int CLASS_Array = 9;
    private static final int CLASS_URL = 10;
    private static final int CLASS_RowId = 11;
    private static final int CLASS_NClob = 12;
    private static final int CLASS_SQLXML = 13;
    private static final int CLASS_String = 14;
    private static final int CLASS_Object = 15;

    
    protected final String[] columns;
    private final String tableName;
    private final int cursorType;
    private final IResultSetLobProvider lobProvider;

    private final Statement fakeStmt = (Statement) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class<?>[]{Statement.class}, new InvocationHandler() {
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            return FakeResultSet.this.invoke(proxy, method, args);
        }
    }
    );
    private final Calendar cal = Calendar.getInstance();

    private int fetchSize = 1, fetchDirection = ResultSet.FETCH_UNKNOWN, resultSetPos = 0;
    private boolean closed = false, wasNull = false;

    protected FakeResultSet(final String tableName, final String[] columns, final int cursorType, final IResultSetLobProvider lobProvider) {
        if (tableName == null || tableName.isEmpty()) {
            throw new IllegalArgumentException("Table name can't be null or empty");
        } else if (columns == null) {
            throw new IllegalArgumentException("Column names array can't be null");
        } else if (cursorType != ResultSet.TYPE_FORWARD_ONLY && cursorType != ResultSet.TYPE_SCROLL_INSENSITIVE && cursorType != ResultSet.TYPE_SCROLL_SENSITIVE) {
            throw new IllegalArgumentException("Illegal cursor type [" + cursorType + "]. Can be ResultSet.TYPE_FORWARD_ONLY, ResultSet.TYPE_SCROLL_INSENSITIVE or ResultSet.TYPE_SCROLL_SENSITIVE only");
        } else {
            for (int index = 0; index < columns.length; index++) {
                if (columns[index] == null || columns[index].isEmpty()) {
                    throw new IllegalArgumentException("Column name in the array at position [" + index + "] is null or empty");
                }
            }
            this.tableName = tableName;
            this.columns = columns;
            this.cursorType = cursorType;
            this.lobProvider = lobProvider == null ? DEFAULT_LOB_PROVIDER : lobProvider;
        }
    }

    protected abstract int getResultSetSize();

    protected abstract Object getCell(int row, int col);

    protected abstract ColumnDescriptor[] getColumnDescriptors();

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        testClose();
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        testClose();
        return false;
    }

    @Override
    public boolean next() throws SQLException {
        testClose();
        return absolute(getRow() + 1);
    }

    @Override
    public void close() throws SQLException {
        closed = true;
    }

    @Override
    public boolean wasNull() throws SQLException {
        testClose();
        return wasNull;
    }

    @Override
    public String getString(int columnIndex) throws SQLException {
        return getObject(columnIndex, String.class);
    }

    @Override
    public boolean getBoolean(int columnIndex) throws SQLException {
        return getObject(columnIndex, boolean.class);
    }

    @Override
    public byte getByte(int columnIndex) throws SQLException {
        return getObject(columnIndex, byte.class);
    }

    @Override
    public short getShort(int columnIndex) throws SQLException {
        return getObject(columnIndex, short.class);
    }

    @Override
    public int getInt(int columnIndex) throws SQLException {
        return getObject(columnIndex, int.class);
    }

    @Override
    public long getLong(int columnIndex) throws SQLException {
        return getObject(columnIndex, long.class);
    }

    @Override
    public float getFloat(int columnIndex) throws SQLException {
        return getObject(columnIndex, float.class);
    }

    @Override
    public double getDouble(int columnIndex) throws SQLException {
        return getObject(columnIndex, double.class);
    }

    @Override
    public BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException {
        final BigDecimal result = getObject(columnIndex, BigDecimal.class);

        if (result != null) {
            return result.setScale(scale);
        } else {
            return null;
        }
    }

    @Override
    public byte[] getBytes(int columnIndex) throws SQLException {
        return getObject(columnIndex, byte[].class);
    }

    @Override
    public Date getDate(int columnIndex) throws SQLException {
        return getDate(columnIndex, cal);
    }

    @Override
    public Time getTime(int columnIndex) throws SQLException {
        return getTime(columnIndex, cal);
    }

    @Override
    public Timestamp getTimestamp(int columnIndex) throws SQLException {
        return getTimestamp(columnIndex, cal);
    }

    @Override
    public InputStream getAsciiStream(int columnIndex) throws SQLException {
        return getObject(columnIndex, InputStream.class);
    }

    @Override
    public InputStream getUnicodeStream(int columnIndex) throws SQLException {
        return getObject(columnIndex, InputStream.class);
    }

    @Override
    public InputStream getBinaryStream(int columnIndex) throws SQLException {
        return getObject(columnIndex, InputStream.class);
    }

    @Override
    public String getString(String columnLabel) throws SQLException {
        return getString(toColumnNumber(columnLabel));
    }

    @Override
    public boolean getBoolean(String columnLabel) throws SQLException {
        return getBoolean(toColumnNumber(columnLabel));
    }

    @Override
    public byte getByte(String columnLabel) throws SQLException {
        return getByte(toColumnNumber(columnLabel));
    }

    @Override
    public short getShort(String columnLabel) throws SQLException {
        return getShort(toColumnNumber(columnLabel));
    }

    @Override
    public int getInt(String columnLabel) throws SQLException {
        return getInt(toColumnNumber(columnLabel));
    }

    @Override
    public long getLong(String columnLabel) throws SQLException {
        return getLong(toColumnNumber(columnLabel));
    }

    @Override
    public float getFloat(String columnLabel) throws SQLException {
        return getFloat(toColumnNumber(columnLabel));
    }

    @Override
    public double getDouble(String columnLabel) throws SQLException {
        return getDouble(toColumnNumber(columnLabel));
    }

    @Override
    public BigDecimal getBigDecimal(String columnLabel, int scale) throws SQLException {
        return getBigDecimal(toColumnNumber(columnLabel), scale);
    }

    @Override
    public byte[] getBytes(String columnLabel) throws SQLException {
        return getBytes(toColumnNumber(columnLabel));
    }

    @Override
    public Date getDate(String columnLabel) throws SQLException {
        return getDate(toColumnNumber(columnLabel));
    }

    @Override
    public Time getTime(String columnLabel) throws SQLException {
        return getTime(toColumnNumber(columnLabel));
    }

    @Override
    public Timestamp getTimestamp(String columnLabel) throws SQLException {
        return getTimestamp(toColumnNumber(columnLabel));
    }

    @Override
    public InputStream getAsciiStream(String columnLabel) throws SQLException {
        return getAsciiStream(toColumnNumber(columnLabel));
    }

    @Override
    public InputStream getUnicodeStream(String columnLabel) throws SQLException {
        return getUnicodeStream(toColumnNumber(columnLabel));
    }

    @Override
    public InputStream getBinaryStream(String columnLabel) throws SQLException {
        return getBinaryStream(toColumnNumber(columnLabel));
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return null;
    }

    @Override
    public void clearWarnings() throws SQLException {
    }

    @Override
    public String getCursorName() throws SQLException {
        return getClass().getSimpleName();
    }

    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
        return new ResultSetMetaData() {
            final ColumnDescriptor[] columnList = getColumnDescriptors();

            @Override
            public <T> T unwrap(Class<T> iface) throws SQLException {
                return null;
            }

            @Override
            public boolean isWrapperFor(Class<?> iface) throws SQLException {
                return false;
            }

            @Override
            public int getColumnCount() throws SQLException {
                return columnList.length;
            }

            @Override
            public boolean isAutoIncrement(int column) throws SQLException {
                toColumnIndex(column);
                return false;
            }

            @Override
            public boolean isCaseSensitive(int column) throws SQLException {
                toColumnIndex(column);
                return false;
            }

            @Override
            public boolean isSearchable(int column) throws SQLException {
                toColumnIndex(column);
                return false;
            }

            @Override
            public int getColumnDisplaySize(int column) throws SQLException {
                toColumnIndex(column);
                return 0;
            }

            @Override
            public String getColumnLabel(int column) throws SQLException {
                toColumnIndex(column);
                return getColumnName(column);
            }

            @Override
            public String getCatalogName(int column) throws SQLException {
                toColumnIndex(column);
                return null;
            }

            @Override
            public String getSchemaName(int column) throws SQLException {
                toColumnIndex(column);
                return "RADIX";
            }

            @Override
            public boolean isReadOnly(int column) throws SQLException {
                toColumnIndex(column);
                return true;
            }

            @Override
            public boolean isWritable(int column) throws SQLException {
                toColumnIndex(column);
                return false;
            }

            @Override
            public boolean isDefinitelyWritable(int column) throws SQLException {
                toColumnIndex(column);
                return false;
            }

            @Override
            public String getTableName(int column) throws SQLException {
                toColumnIndex(column);
                return tableName;
            }

            @Override
            public int isNullable(int column) throws SQLException {
                toColumnIndex(column);
                return columnNullableUnknown;
            }

            @Override
            public boolean isCurrency(int column) throws SQLException {
                return columnList[toColumnIndex(column)].isCurrency;
            }

            @Override
            public boolean isSigned(int column) throws SQLException {
                return columnList[toColumnIndex(column)].isSigned;
            }

            @Override
            public String getColumnName(int column) throws SQLException {
                return columnList[toColumnIndex(column)].columnName;
            }

            @Override
            public int getPrecision(int column) throws SQLException {
                return columnList[toColumnIndex(column)].precision;
            }

            @Override
            public int getScale(int column) throws SQLException {
                return columnList[toColumnIndex(column)].scale;
            }

            @Override
            public int getColumnType(int column) throws SQLException {
                return columnList[toColumnIndex(column)].columnType;
            }

            @Override
            public String getColumnTypeName(int column) throws SQLException {
                return columnList[toColumnIndex(column)].columnSQLType;
            }

            @Override
            public String getColumnClassName(int column) throws SQLException {
                return columnList[toColumnIndex(column)].columnClass;
            }

            @Override
            public String toString() {
                return "FakeResultSetMetaData " + Arrays.toString(columnList);
            }

            private int toColumnIndex(final int columnIndex) throws SQLException {
                if (columnIndex < 1 || columnIndex > columnList.length) {
                    throw new SQLException("Column index [" + columnIndex + "] out of range 1.." + columnList.length);
                } else {
                    return columnIndex - 1;
                }
            }
        };
    }

    @Override
    public Object getObject(int columnIndex) throws SQLException {
        return getObject(columnIndex, Object.class);
    }

    @Override
    public Object getObject(String columnLabel) throws SQLException {
        return getObject(toColumnNumber(columnLabel));
    }

    @Override
    public int findColumn(String columnLabel) throws SQLException {
        testClose();
        if (columnLabel == null || columnLabel.isEmpty()) {
            throw new IllegalArgumentException("Column name can't be null or empty");
        } else {
            for (int index = 0; index < columns.length; index++) {
                if (columns[index].equalsIgnoreCase(columnLabel)) {
                    return index + 1;
                }
            }
            throw new SQLException("Column name ["+columnLabel+"] is missing in the result set");
        }
    }

    @Override
    public Reader getCharacterStream(int columnIndex) throws SQLException {
        return getObject(columnIndex, Reader.class);
    }

    @Override
    public Reader getCharacterStream(String columnLabel) throws SQLException {
        return getCharacterStream(toColumnNumber(columnLabel));
    }

    @Override
    public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
        return getObject(columnIndex, BigDecimal.class);
    }

    @Override
    public BigDecimal getBigDecimal(String columnLabel) throws SQLException {
        return getBigDecimal(toColumnNumber(columnLabel));
    }

    @Override
    public boolean isBeforeFirst() throws SQLException {
        testClose();
        return getResultSetPosition() <= 0;
    }

    @Override
    public boolean isAfterLast() throws SQLException {
        testClose();
        return getResultSetPosition() > getResultSetSize();
    }

    @Override
    public boolean isFirst() throws SQLException {
        testClose();
        return getResultSetPosition() == 1;
    }

    @Override
    public boolean isLast() throws SQLException {
        testClose();
        return getResultSetPosition() == getResultSetSize();
    }

    @Override
    public void beforeFirst() throws SQLException {
        testClose();
        setResultSetPosition(0);
    }

    @Override
    public void afterLast() throws SQLException {
        testClose();
        setResultSetPosition(getResultSetSize() + 1);
    }

    @Override
    public boolean first() throws SQLException {
        testClose();
        return absolute(1);
    }

    @Override
    public boolean last() throws SQLException {
        testClose();
        return absolute(getResultSetSize());
    }

    @Override
    public int getRow() throws SQLException {
        testClose();
        return getResultSetPosition();
    }

    @Override
    public boolean absolute(int row) throws SQLException {
        testClose();
        return setResultSetPosition(row);
    }

    @Override
    public boolean relative(int rows) throws SQLException {
        testClose();
        return absolute(getRow() + rows);
    }

    @Override
    public boolean previous() throws SQLException {
        testClose();
        return absolute(getRow() - 1);
    }

    @Override
    public void setFetchDirection(int direction) throws SQLException {
        testClose();
        if (direction != ResultSet.FETCH_FORWARD && direction != ResultSet.FETCH_REVERSE && direction != ResultSet.FETCH_UNKNOWN) {
            throw new SQLException("Invalid fetch direction type [" + direction + "]. Only ResultSet.FETCH_FORWARD, ResultSet.FETCH_REVERSE or ResultSet.FETCH_UNKNOWN are available");
        } else {
            fetchDirection = direction;
        }
    }

    @Override
    public int getFetchDirection() throws SQLException {
        return fetchDirection;
    }

    @Override
    public void setFetchSize(int rows) throws SQLException {
        testClose();
        if (rows <= 0) {
            throw new SQLException("Fetch size [" + rows + "] need be positive!");
        } else {
            fetchSize = rows;
        }
    }

    @Override
    public int getFetchSize() throws SQLException {
        return fetchSize;
    }

    @Override
    public int getType() throws SQLException {
        return cursorType;
    }

    @Override
    public int getConcurrency() throws SQLException {
        return ResultSet.CONCUR_READ_ONLY;
    }

    @Override
    public boolean rowUpdated() throws SQLException {
        testClose();
        return false;
    }

    @Override
    public boolean rowInserted() throws SQLException {
        testClose();
        return false;
    }

    @Override
    public boolean rowDeleted() throws SQLException {
        testClose();
        return false;
    }

    @Override
    public void updateNull(int columnIndex) throws SQLException {
        throw new SQLException("This result set is read-only!");
    }

    @Override
    public void updateBoolean(int columnIndex, boolean x) throws SQLException {
        throw new SQLException("This result set is read-only!");
    }

    @Override
    public void updateByte(int columnIndex, byte x) throws SQLException {
        throw new SQLException("This result set is read-only!");
    }

    @Override
    public void updateShort(int columnIndex, short x) throws SQLException {
        throw new SQLException("This result set is read-only!");
    }

    @Override
    public void updateInt(int columnIndex, int x) throws SQLException {
        throw new SQLException("This result set is read-only!");
    }

    @Override
    public void updateLong(int columnIndex, long x) throws SQLException {
        throw new SQLException("This result set is read-only!");
    }

    @Override
    public void updateFloat(int columnIndex, float x) throws SQLException {
        throw new SQLException("This result set is read-only!");
    }

    @Override
    public void updateDouble(int columnIndex, double x) throws SQLException {
        throw new SQLException("This result set is read-only!");
    }

    @Override
    public void updateBigDecimal(int columnIndex, BigDecimal x) throws SQLException {
        throw new SQLException("This result set is read-only!");
    }

    @Override
    public void updateString(int columnIndex, String x) throws SQLException {
        throw new SQLException("This result set is read-only!");
    }

    @Override
    public void updateBytes(int columnIndex, byte[] x) throws SQLException {
        throw new SQLException("This result set is read-only!");
    }

    @Override
    public void updateDate(int columnIndex, Date x) throws SQLException {
        throw new SQLException("This result set is read-only!");
    }

    @Override
    public void updateTime(int columnIndex, Time x) throws SQLException {
        throw new SQLException("This result set is read-only!");
    }

    @Override
    public void updateTimestamp(int columnIndex, Timestamp x) throws SQLException {
        throw new SQLException("This result set is read-only!");
    }

    @Override
    public void updateAsciiStream(int columnIndex, InputStream x, int length) throws SQLException {
        throw new SQLException("This result set is read-only!");
    }

    @Override
    public void updateBinaryStream(int columnIndex, InputStream x, int length) throws SQLException {
        throw new SQLException("This result set is read-only!");
    }

    @Override
    public void updateCharacterStream(int columnIndex, Reader x, int length) throws SQLException {
        throw new SQLException("This result set is read-only!");
    }

    @Override
    public void updateObject(int columnIndex, Object x, int scaleOrLength) throws SQLException {
        throw new SQLException("This result set is read-only!");
    }

    @Override
    public void updateObject(int columnIndex, Object x) throws SQLException {
        throw new SQLException("This result set is read-only!");
    }

    @Override
    public void updateNull(String columnLabel) throws SQLException {
        throw new SQLException("This result set is read-only!");
    }

    @Override
    public void updateBoolean(String columnLabel, boolean x) throws SQLException {
        throw new SQLException("This result set is read-only!");
    }

    @Override
    public void updateByte(String columnLabel, byte x) throws SQLException {
        throw new SQLException("This result set is read-only!");
    }

    @Override
    public void updateShort(String columnLabel, short x) throws SQLException {
        throw new SQLException("This result set is read-only!");
    }

    @Override
    public void updateInt(String columnLabel, int x) throws SQLException {
        throw new SQLException("This result set is read-only!");
    }

    @Override
    public void updateLong(String columnLabel, long x) throws SQLException {
        throw new SQLException("This result set is read-only!");
    }

    @Override
    public void updateFloat(String columnLabel, float x) throws SQLException {
        throw new SQLException("This result set is read-only!");
    }

    @Override
    public void updateDouble(String columnLabel, double x) throws SQLException {
        throw new SQLException("This result set is read-only!");
    }

    @Override
    public void updateBigDecimal(String columnLabel, BigDecimal x) throws SQLException {
        throw new SQLException("This result set is read-only!");
    }

    @Override
    public void updateString(String columnLabel, String x) throws SQLException {
        throw new SQLException("This result set is read-only!");
    }

    @Override
    public void updateBytes(String columnLabel, byte[] x) throws SQLException {
        throw new SQLException("This result set is read-only!");
    }

    @Override
    public void updateDate(String columnLabel, Date x) throws SQLException {
        throw new SQLException("This result set is read-only!");
    }

    @Override
    public void updateTime(String columnLabel, Time x) throws SQLException {
        throw new SQLException("This result set is read-only!");
    }

    @Override
    public void updateTimestamp(String columnLabel, Timestamp x) throws SQLException {
        throw new SQLException("This result set is read-only!");
    }

    @Override
    public void updateAsciiStream(String columnLabel, InputStream x, int length) throws SQLException {
        throw new SQLException("This result set is read-only!");
    }

    @Override
    public void updateBinaryStream(String columnLabel, InputStream x, int length) throws SQLException {
        throw new SQLException("This result set is read-only!");
    }

    @Override
    public void updateCharacterStream(String columnLabel, Reader reader, int length) throws SQLException {
        throw new SQLException("This result set is read-only!");
    }

    @Override
    public void updateObject(String columnLabel, Object x, int scaleOrLength) throws SQLException {
        throw new SQLException("This result set is read-only!");
    }

    @Override
    public void updateObject(String columnLabel, Object x) throws SQLException {
        throw new SQLException("This result set is read-only!");
    }

    @Override
    public void insertRow() throws SQLException {
        throw new SQLException("This result set is read-only!");
    }

    @Override
    public void updateRow() throws SQLException {
        throw new SQLException("This result set is read-only!");
    }

    @Override
    public void deleteRow() throws SQLException {
        throw new SQLException("This result set is read-only!");
    }

    @Override
    public void refreshRow() throws SQLException {
        testClose();
    }

    @Override
    public void cancelRowUpdates() throws SQLException {
        throw new SQLException("This result set is read-only!");
    }

    @Override
    public void moveToInsertRow() throws SQLException {
        throw new SQLException("This result set is read-only!");
    }

    @Override
    public void moveToCurrentRow() throws SQLException {
        testClose();
    }

    @Override
    public Statement getStatement() throws SQLException {
        return fakeStmt;
    }

    @Override
    public Object getObject(int columnIndex, Map<String, Class<?>> map) throws SQLException {
        return getObject(columnIndex, Object.class);
    }

    @Override
    public Ref getRef(int columnIndex) throws SQLException {
        return getObject(columnIndex, Ref.class);
    }

    @Override
    public Blob getBlob(int columnIndex) throws SQLException {
        return getObject(columnIndex, Blob.class);
    }

    @Override
    public Clob getClob(int columnIndex) throws SQLException {
        return getObject(columnIndex, Clob.class);
    }

    @Override
    public Array getArray(int columnIndex) throws SQLException {
        return getObject(columnIndex, Array.class);
    }

    @Override
    public Object getObject(String columnLabel, Map<String, Class<?>> map) throws SQLException {
        return getObject(toColumnNumber(columnLabel), map);
    }

    @Override
    public Ref getRef(String columnLabel) throws SQLException {
        return getRef(toColumnNumber(columnLabel));
    }

    @Override
    public Blob getBlob(String columnLabel) throws SQLException {
        return getBlob(toColumnNumber(columnLabel));
    }

    @Override
    public Clob getClob(String columnLabel) throws SQLException {
        return getClob(toColumnNumber(columnLabel));
    }

    @Override
    public Array getArray(String columnLabel) throws SQLException {
        return getArray(toColumnNumber(columnLabel));
    }

    @Override
    public Date getDate(int columnIndex, Calendar cal) throws SQLException {
        return getObject(columnIndex, Date.class);
    }

    @Override
    public Date getDate(String columnLabel, Calendar cal) throws SQLException {
        return getDate(toColumnNumber(columnLabel), cal);
    }

    @Override
    public Time getTime(int columnIndex, Calendar cal) throws SQLException {
        return getObject(columnIndex, Time.class);
    }

    @Override
    public Time getTime(String columnLabel, Calendar cal) throws SQLException {
        return getTime(toColumnNumber(columnLabel), cal);
    }

    @Override
    public Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException {
        return getObject(columnIndex, Timestamp.class);
    }

    @Override
    public Timestamp getTimestamp(String columnLabel, Calendar cal) throws SQLException {
        return getTimestamp(toColumnNumber(columnLabel), cal);
    }

    @Override
    public URL getURL(int columnIndex) throws SQLException {
        return getObject(columnIndex, URL.class);
    }

    @Override
    public URL getURL(String columnLabel) throws SQLException {
        return getURL(toColumnNumber(columnLabel));
    }

    @Override
    public void updateRef(int columnIndex, Ref x) throws SQLException {
        throw new SQLException("This result set is read-only!");
    }

    @Override
    public void updateRef(String columnLabel, Ref x) throws SQLException {
        throw new SQLException("This result set is read-only!");
    }

    @Override
    public void updateBlob(int columnIndex, Blob x) throws SQLException {
        throw new SQLException("This result set is read-only!");
    }

    @Override
    public void updateBlob(String columnLabel, Blob x) throws SQLException {
        throw new SQLException("This result set is read-only!");
    }

    @Override
    public void updateClob(int columnIndex, Clob x) throws SQLException {
        throw new SQLException("This result set is read-only!");
    }

    @Override
    public void updateClob(String columnLabel, Clob x) throws SQLException {
        throw new SQLException("This result set is read-only!");
    }

    @Override
    public void updateArray(int columnIndex, Array x) throws SQLException {
        throw new SQLException("This result set is read-only!");
    }

    @Override
    public void updateArray(String columnLabel, Array x) throws SQLException {
        throw new SQLException("This result set is read-only!");
    }

    @Override
    public RowId getRowId(int columnIndex) throws SQLException {
        return getObject(columnIndex, RowId.class);
    }

    @Override
    public RowId getRowId(String columnLabel) throws SQLException {
        return getRowId(toColumnNumber(columnLabel));
    }

    @Override
    public void updateRowId(int columnIndex, RowId x) throws SQLException {
        throw new SQLException("This result set is read-only!");
    }

    @Override
    public void updateRowId(String columnLabel, RowId x) throws SQLException {
        throw new SQLException("This result set is read-only!");
    }

    @Override
    public int getHoldability() throws SQLException {
        return ResultSet.CLOSE_CURSORS_AT_COMMIT;
    }

    @Override
    public boolean isClosed() throws SQLException {
        return closed;
    }

    @Override
    public void updateNString(int columnIndex, String nString) throws SQLException {
        throw new SQLException("This result set is read-only!");
    }

    @Override
    public void updateNString(String columnLabel, String nString) throws SQLException {
        throw new SQLException("This result set is read-only!");
    }

    @Override
    public void updateNClob(int columnIndex, NClob nClob) throws SQLException {
        throw new SQLException("This result set is read-only!");
    }

    @Override
    public void updateNClob(String columnLabel, NClob nClob) throws SQLException {
        throw new SQLException("This result set is read-only!");
    }

    @Override
    public NClob getNClob(int columnIndex) throws SQLException {
        return getObject(columnIndex, NClob.class);
    }

    @Override
    public NClob getNClob(String columnLabel) throws SQLException {
        return getNClob(toColumnNumber(columnLabel));
    }

    @Override
    public SQLXML getSQLXML(int columnIndex) throws SQLException {
        return getObject(columnIndex, SQLXML.class);
    }

    @Override
    public SQLXML getSQLXML(String columnLabel) throws SQLException {
        return getSQLXML(toColumnNumber(columnLabel));
    }

    @Override
    public void updateSQLXML(int columnIndex, SQLXML xmlObject) throws SQLException {
        throw new SQLException("This result set is read-only!");
    }

    @Override
    public void updateSQLXML(String columnLabel, SQLXML xmlObject) throws SQLException {
        throw new SQLException("This result set is read-only!");
    }

    @Override
    public String getNString(int columnIndex) throws SQLException {
        return getObject(columnIndex, String.class);
    }

    @Override
    public String getNString(String columnLabel) throws SQLException {
        return getNString(toColumnNumber(columnLabel));
    }

    @Override
    public Reader getNCharacterStream(int columnIndex) throws SQLException {
        return getObject(columnIndex, Reader.class);
    }

    @Override
    public Reader getNCharacterStream(String columnLabel) throws SQLException {
        return getNCharacterStream(toColumnNumber(columnLabel));
    }

    @Override
    public void updateNCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
        throw new SQLException("This result set is read-only!");
    }

    @Override
    public void updateNCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
        throw new SQLException("This result set is read-only!");
    }

    @Override
    public void updateAsciiStream(int columnIndex, InputStream x, long length) throws SQLException {
        throw new SQLException("This result set is read-only!");
    }

    @Override
    public void updateBinaryStream(int columnIndex, InputStream x, long length) throws SQLException {
        throw new SQLException("This result set is read-only!");
    }

    @Override
    public void updateCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
        throw new SQLException("This result set is read-only!");
    }

    @Override
    public void updateAsciiStream(String columnLabel, InputStream x, long length) throws SQLException {
        throw new SQLException("This result set is read-only!");
    }

    @Override
    public void updateBinaryStream(String columnLabel, InputStream x, long length) throws SQLException {
        throw new SQLException("This result set is read-only!");
    }

    @Override
    public void updateCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
        throw new SQLException("This result set is read-only!");
    }

    @Override
    public void updateBlob(int columnIndex, InputStream inputStream, long length) throws SQLException {
        throw new SQLException("This result set is read-only!");
    }

    @Override
    public void updateBlob(String columnLabel, InputStream inputStream, long length) throws SQLException {
        throw new SQLException("This result set is read-only!");
    }

    @Override
    public void updateClob(int columnIndex, Reader reader, long length) throws SQLException {
        throw new SQLException("This result set is read-only!");
    }

    @Override
    public void updateClob(String columnLabel, Reader reader, long length) throws SQLException {
        throw new SQLException("This result set is read-only!");
    }

    @Override
    public void updateNClob(int columnIndex, Reader reader, long length) throws SQLException {
        throw new SQLException("This result set is read-only!");
    }

    @Override
    public void updateNClob(String columnLabel, Reader reader, long length) throws SQLException {
        throw new SQLException("This result set is read-only!");
    }

    @Override
    public void updateNCharacterStream(int columnIndex, Reader x) throws SQLException {
        throw new SQLException("This result set is read-only!");
    }

    @Override
    public void updateNCharacterStream(String columnLabel, Reader reader) throws SQLException {
        throw new SQLException("This result set is read-only!");
    }

    @Override
    public void updateAsciiStream(int columnIndex, InputStream x) throws SQLException {
        throw new SQLException("This result set is read-only!");
    }

    @Override
    public void updateBinaryStream(int columnIndex, InputStream x) throws SQLException {
        throw new SQLException("This result set is read-only!");
    }

    @Override
    public void updateCharacterStream(int columnIndex, Reader x) throws SQLException {
        throw new SQLException("This result set is read-only!");
    }

    @Override
    public void updateAsciiStream(String columnLabel, InputStream x) throws SQLException {
        throw new SQLException("This result set is read-only!");
    }

    @Override
    public void updateBinaryStream(String columnLabel, InputStream x) throws SQLException {
        throw new SQLException("This result set is read-only!");
    }

    @Override
    public void updateCharacterStream(String columnLabel, Reader reader) throws SQLException {
        throw new SQLException("This result set is read-only!");
    }

    @Override
    public void updateBlob(int columnIndex, InputStream inputStream) throws SQLException {
        throw new SQLException("This result set is read-only!");
    }

    @Override
    public void updateBlob(String columnLabel, InputStream inputStream) throws SQLException {
        throw new SQLException("This result set is read-only!");
    }

    @Override
    public void updateClob(int columnIndex, Reader reader) throws SQLException {
        throw new SQLException("This result set is read-only!");
    }

    @Override
    public void updateClob(String columnLabel, Reader reader) throws SQLException {
        throw new SQLException("This result set is read-only!");
    }

    @Override
    public void updateNClob(int columnIndex, Reader reader) throws SQLException {
        throw new SQLException("This result set is read-only!");
    }

    @Override
    public void updateNClob(String columnLabel, Reader reader) throws SQLException {
        throw new SQLException("This result set is read-only!");
    }

    @Override
    public <T> T getObject(int columnIndex, Class<T> type) throws SQLException {
        final ColumnDescriptor[] descList = getColumnDescriptors();

        if (columnIndex < 1 || columnIndex > descList.length) {
            throw new SQLException("Column index [" + columnIndex + "] out of range 1.." + descList.length);
        } else if (type == null) {
            throw new SQLException("Awaited class can't be null!");
        } else {
            testClose();
            if (getResultSetPosition() < 1 || getResultSetPosition() > getResultSetSize()) {
                throw new SQLException("Cursor position [" + getResultSetPosition() + "] out of range 1.." + getResultSetSize());
            } else {
                wasNull = false;
                final Object value = getCell(getResultSetPosition() - 1, descList[columnIndex - 1].numberInDataset);    // Decode description column number to dataset column number

                if (type.isPrimitive()) {
                    if (value == null || value.toString().isEmpty()) {
                        wasNull = true;
                    }
                    try {
                        if (type == byte.class) {
                            return (T) Byte.valueOf((byte) (value == null || value.toString().isEmpty() ? 0 : toLong(value)));
                        } else if (type == short.class) {
                            return (T) Short.valueOf(((short) (value == null || value.toString().isEmpty() ? 0 : toLong(value))));
                        } else if (type == int.class) {
                            return (T) Integer.valueOf(((int) (value == null || value.toString().isEmpty() ? 0 : toLong(value))));
                        } else if (type == long.class) {
                            return (T) Long.valueOf((value == null || value.toString().isEmpty() ? 0 : toLong(value)));
                        } else if (type == float.class) {
                            return (T) Float.valueOf((float) (value == null || value.toString().isEmpty() ? 0 : toDouble(value)));
                        } else if (type == double.class) {
                            return (T) Double.valueOf((value == null || value.toString().isEmpty() ? 0 : toDouble(value)));
                        } else if (type == boolean.class) {
                            return (T) Boolean.valueOf(value == null || value.toString().isEmpty() ? false : toBoolean(value));
                        } else {
                            throw new UnsupportedOperationException("Getting of primitive type [" + type.getName() + "] is not implemented yet");
                        }
                    } catch (NumberFormatException exc) {
                        throw new SQLException("Row " + getResultSetPosition() + ", col " + columnIndex + ": value [" + value + "] can't be converted to primitive type " + type.getSimpleName());
                    }
                } else if (type.isArray()) {
                    if (type.getComponentType() == byte.class) {
                        if (value == null) {
                            wasNull = true;
                            return null;
                        } else {
                            return (T) toByteArray(value);
                        }
                    } else {
                        throw new UnsupportedOperationException("Getting array of type [" + type.getName() + "] is not implemented yet");
                    }
                } else {
                    if (value == null) {
                        wasNull = true;
                        return null;
                    } else {
                        for (int index = 0; index < SUPPORTED_CLASSES.length; index++) {
                            if (SUPPORTED_CLASSES[index].isAssignableFrom(type)) {
                                switch (index) {
                                    case CLASS_BigDecimal:
                                        try {
                                            return (T) new BigDecimal(value.toString());
                                        } catch (NumberFormatException exc) {
                                            throw new SQLException("Row " + getResultSetPosition() + ", col " + columnIndex + ": value [" + value + "] can't be converted to BigDecimal");
                                        }
                                    case CLASS_Date:
                                        try {
                                            return (T) (new Date(toDate(value).getTime()));
                                        } catch (ParseException exc) {
                                            throw new SQLException("Row " + getResultSetPosition() + ", col " + columnIndex + ": value [" + value + "] can't be converted to Date (format " + DATE_FORMAT + ")");
                                        }
                                    case CLASS_Time:
                                        try {
                                            return (T) (new Time(toDate(value).getTime()));
                                        } catch (ParseException exc) {
                                            throw new SQLException("Row " + getResultSetPosition() + ", col " + columnIndex + ": value [" + value + "] can't be converted to Time (format " + TIME_FORMAT + ")");
                                        }
                                    case CLASS_Timestamp:
                                        try {
                                            return value == null || value.toString().isEmpty() ? null : (T) (new Timestamp(toDate(value).getTime()));
                                        } catch (ParseException exc) {
                                            throw new SQLException("Row " + getResultSetPosition() + ", col " + columnIndex + ": value [" + value + "] can't be converted to Timestamp (format " + TIMESTAMP_FORMAT + ")");
                                        }
                                    case CLASS_InputStream:
                                        return (T) new ByteArrayInputStream(toByteArray(value));
                                    case CLASS_Reader:
                                        return (T) new InputStreamReader(new ByteArrayInputStream(toByteArray(value)));
                                    case CLASS_Blob:
                                        if (lobProvider.canGetBlobContent(value.toString())) {
                                            return (T) new PseudoBlob(lobProvider.getBlobContent(value.toString()));
                                        }
                                        else {
                                            return (T) new PseudoBlob(toByteArray(value));
                                        }
                                    case CLASS_Clob:
                                    case CLASS_NClob:
                                        if (lobProvider.canGetClobContent(value.toString())) {
                                            return (T) new PseudoClob(lobProvider.getClobContent(value.toString()));
                                        }
                                        else {
                                            return (T) new PseudoClob(value.toString().toCharArray());
                                        }
                                    case CLASS_RowId:
                                        return (T) new RowId() {
                                            @Override
                                            public byte[] getBytes() {
                                                return toByteArray(value);
                                            }
                                        };
                                    case CLASS_Ref:
                                        throw new SQLException("Row " + getResultSetPosition() + ", col " + columnIndex + ": implementation restriction: value can't be converted to Ref type");
                                    case CLASS_Array:
                                        throw new SQLException("Row " + getResultSetPosition() + ", col " + columnIndex + ": implementation restriction: value can't be converted to Array type");
                                    case CLASS_URL:
                                        throw new SQLException("Row " + getResultSetPosition() + ", col " + columnIndex + ": implementation restriction: value can't be converted to URI type");
                                    case CLASS_SQLXML:
                                        throw new SQLException("Row " + getResultSetPosition() + ", col " + columnIndex + ": implementation restriction: value can't be converted to SQLXML type");
                                    case CLASS_String:
                                        return (T) value.toString();
                                    case CLASS_Object:
                                        return (T) value;
                                    default:
                                        throw new UnsupportedOperationException("Getting type [" + type.getName() + "] is not implemented yet");
                                }
                            }
                        }
                        throw new UnsupportedOperationException("Getting type [" + type.getName() + "] is not implemented yet");
                    }
                }
            }
        }
    }

    @Override
    public <T> T getObject(String columnLabel, Class<T> type) throws SQLException {
        return getObject(toColumnNumber(columnLabel), type);
    }

    protected Date toDate(final Object value) throws SQLException, ParseException {
        if (value instanceof Date) {
            return (Date) value;
        } else if (value instanceof java.util.Date) {
            return new Date(((java.util.Date) value).getTime());
        } else if (value instanceof Integer) {
            return new Date(((Integer) value).longValue());
        } else if (value instanceof Long) {
            return new Date(((Long) value).longValue());
        } else if (value instanceof Double) {
            return new Date(DateUtil.getJavaDate(((Double) value).doubleValue()).getTime());
        } else {
            return value == null || value.toString().isEmpty() ? null : new Date(new SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).parse(value.toString()).getTime());
        }
    }

    protected long toLong(final Object value) throws SQLException {
        if (value instanceof Date) {
            return ((Date)value).getTime();
        }
        else if (value instanceof java.util.Date) {
            return ((java.util.Date)value).getTime();
        }
        else if (value instanceof Double) {
            return ((Double)value).longValue();
        }
        else if (value instanceof Float) {
            return ((Float)value).longValue();
        }
        else {
            String temp = value.toString().trim();
            
            if (temp.endsWith(".0")) {
                temp = temp.substring(0, temp.length() - 2);
            }
            return Long.valueOf(temp);
        }
    }

    protected double toDouble(final Object value) throws SQLException {
        if (value instanceof Date) {
            return ((Date)value).getTime();
        } else if (value instanceof java.util.Date) {
            return ((java.util.Date) value).getTime();
        }
        else {
            return Double.valueOf(value.toString().trim());
        }
    }

    protected boolean toBoolean(final Object value) throws SQLException {
        return Boolean.valueOf(value.toString().trim());
    }

    protected byte[] toByteArray(final Object value) {
        if (value instanceof byte[]) {
            return (byte[]) value;
        } else {
            return value.toString().getBytes();
        }
    }

    protected void testClose() throws SQLException {
        if (isClosed()) {
            throw new SQLException("Attempt to execute operation on closed result set");
        }
    }

    protected int getResultSetPosition() {
        return resultSetPos;
    }

    protected boolean setResultSetPosition(int pos) throws SQLException {
        if (cursorType == ResultSet.TYPE_FORWARD_ONLY && resultSetPos != pos - 1) {
            throw new SQLException("Attempt to use any but next() on the forward-only cursor");
        } else {
            resultSetPos = pos;
            return pos > 0 && pos <= getResultSetSize();
        }
    }

    private Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if ("toString".equals(method.getName()) && (args == null || args.length == 0)) {
            return "Fake Statement proxy";
        } else if ("getClass".equals(method.getName()) && args.length == 0) {
            return proxy.getClass();
        } else {
            throw new SQLException("Attempt to use fake Statement in the program");
        }
    }

    private int toColumnNumber(final String columnName) throws SQLException {
        final int number = findColumn(columnName);

        if (number > 0) {
            return number;
        } else {
            throw new SQLException("Column name [" + columnName + "] is missing in the result set");
        }
    }

    protected static class ColumnDescriptor {

        final int numberInDataset;
        final String columnName;
        final int columnType;
        final String columnSQLType;
        final String columnClass;
        final boolean isCurrency, isSigned;
        final int precision, scale;

        public ColumnDescriptor(final int numberInDataset, final String columnName, final int columnType, final String columnSQLType, final String columnClass, final boolean isCurrency, final boolean isSigned, final int precision, final int scale) {
            this.numberInDataset = numberInDataset;
            this.columnName = columnName;
            this.columnType = columnType;
            this.columnSQLType = columnSQLType;
            this.columnClass = columnClass;
            this.isCurrency = isCurrency;
            this.isSigned = isSigned;
            this.precision = precision;
            this.scale = scale;
        }

        @Override
        public String toString() {
            return "ColumnDescriptor[" + numberInDataset + "] {columnName=" + columnName + ", columnType=" + columnType + ", columnSQLType="
                    + columnSQLType + ", columnClass=" + columnClass + ", isCurrency=" + isCurrency
                    + ", isSigned=" + isSigned + ", precision=" + precision + ", scale=" + scale + "}]";
        }
    }

    protected static class PseudoBlob implements Blob {

        private final byte[] content;

        public PseudoBlob(final byte[] content) {
            this.content = content;
        }

        @Override
        public long length() throws SQLException {
            return content.length;
        }

        @Override
        public byte[] getBytes(long pos, int length) throws SQLException {
            if (pos <= 0 || pos >= content.length) {
                throw new SQLException("Start position [" + pos + "] out of range 1.." + (content.length - 1));
            } else if (pos + length < 0 || pos + length - 1 > content.length) {
                throw new SQLException("End position [" + (pos + length - 1) + "] out of range 1.." + (content.length - 1));
            } else {
                final byte[] result = new byte[length];

                System.arraycopy(content, (int) pos - 1, result, 0, length);
                return result;
            }
        }

        @Override
        public InputStream getBinaryStream() throws SQLException {
            return new ByteArrayInputStream(content);
        }

        @Override
        public long position(byte[] pattern, long start) throws SQLException {
            return -1;
        }

        @Override
        public long position(Blob pattern, long start) throws SQLException {
            return -1;
        }

        @Override
        public int setBytes(long pos, byte[] bytes) throws SQLException {
            throw new SQLException("This Blob is read-only");
        }

        @Override
        public int setBytes(long pos, byte[] bytes, int offset, int len) throws SQLException {
            throw new SQLException("This Blob is read-only");
        }

        @Override
        public OutputStream setBinaryStream(long pos) throws SQLException {
            throw new SQLException("This Blob is read-only");
        }

        @Override
        public void truncate(long len) throws SQLException {
            throw new SQLException("This Blob is read-only");
        }

        @Override
        public void free() throws SQLException {
            throw new SQLException("This Blob is read-only");
        }

        @Override
        public InputStream getBinaryStream(long pos, long length) throws SQLException {
            return new ByteArrayInputStream(getBytes(pos, (int) length));
        }

        @Override
        public String toString() {
            return "PseudoBlob [content=" + Arrays.toString(content) + "]";
        }
    }

    protected static class PseudoClob implements NClob {

        private final char[] content;

        public PseudoClob(final char[] content) {
            this.content = content;
        }

        @Override
        public long length() throws SQLException {
            return content.length;
        }

        @Override
        public String getSubString(long pos, int length) throws SQLException {
            if (pos <= 0 || pos >= content.length) {
                throw new SQLException("Start position [" + pos + "] out of range 1.." + (content.length - 1));
            } else if (pos + length < 0 || pos + length - 1 > content.length) {
                throw new SQLException("End position [" + (pos + length - 1) + "] out of range 1.." + (content.length - 1));
            } else {
                return new String(content, (int) pos - 1, length);
            }
        }

        @Override
        public Reader getCharacterStream() throws SQLException {
            return new CharArrayReader(content);
        }

        @Override
        public InputStream getAsciiStream() throws SQLException {
            return new ByteArrayInputStream(new String(content).getBytes());
        }

        @Override
        public long position(String searchstr, long start) throws SQLException {
            return -1;
        }

        @Override
        public long position(Clob searchstr, long start) throws SQLException {
            return -1;
        }

        @Override
        public int setString(long pos, String str) throws SQLException {
            throw new SQLException("This Clob is read-only");
        }

        @Override
        public int setString(long pos, String str, int offset, int len) throws SQLException {
            throw new SQLException("This Clob is read-only");
        }

        @Override
        public OutputStream setAsciiStream(long pos) throws SQLException {
            throw new SQLException("This Clob is read-only");
        }

        @Override
        public Writer setCharacterStream(long pos) throws SQLException {
            throw new SQLException("This Clob is read-only");
        }

        @Override
        public void truncate(long len) throws SQLException {
            throw new SQLException("This Clob is read-only");
        }

        @Override
        public void free() throws SQLException {
            throw new SQLException("This Clob is read-only");
        }

        @Override
        public Reader getCharacterStream(long pos, long length) throws SQLException {
            return new StringReader(getSubString(pos, (int) length));
        }

        @Override
        public String toString() {
            return "PseudoClob [content=" + new String(content) + "]";
        }
    }
}
