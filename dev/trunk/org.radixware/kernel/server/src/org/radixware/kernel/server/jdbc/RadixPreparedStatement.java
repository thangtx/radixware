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

package org.radixware.kernel.server.jdbc;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import oracle.jdbc.OracleParameterMetaData;
import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleResultSetCache;
import oracle.jdbc.dcn.DatabaseChangeRegistration;
import oracle.sql.ARRAY;
import oracle.sql.BFILE;
import oracle.sql.BINARY_DOUBLE;
import oracle.sql.BINARY_FLOAT;
import oracle.sql.BLOB;
import oracle.sql.CHAR;
import oracle.sql.CLOB;
import oracle.sql.CustomDatum;
import oracle.sql.DATE;
import oracle.sql.Datum;
import oracle.sql.INTERVALDS;
import oracle.sql.INTERVALYM;
import oracle.sql.NUMBER;
import oracle.sql.OPAQUE;
import oracle.sql.ORAData;
import oracle.sql.RAW;
import oracle.sql.REF;
import oracle.sql.ROWID;
import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;
import oracle.sql.TIMESTAMP;
import oracle.sql.TIMESTAMPLTZ;
import oracle.sql.TIMESTAMPTZ;
import org.radixware.kernel.server.arte.Arte;


public class RadixPreparedStatement implements OraclePreparedStatement {

    private final OraclePreparedStatement delegate;
    private final String preparedSql;
    private final DbOperationLogger logger;
    private boolean readOnly = false;
    private final RadixConnection radixConnection;

    public RadixPreparedStatement(final RadixConnection radixConnection, OraclePreparedStatement delegate, final String sql) {
        this.delegate = delegate;
        this.radixConnection = radixConnection;
        preparedSql = sql;
        logger = new DbOperationLogger(radixConnection);
    }

    public void setReadOnly(final boolean readOnly) {
        this.readOnly = readOnly;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    @Override
    public void defineParameterTypeBytes(int i, int i1, int i2) throws SQLException {
        delegate.defineParameterTypeBytes(i, i1, i2);
    }

    @Override
    public void defineParameterTypeChars(int i, int i1, int i2) throws SQLException {
        delegate.defineParameterTypeChars(i, i1, i2);
    }

    @Override
    public void defineParameterType(int i, int i1, int i2) throws SQLException {
        delegate.defineParameterType(i, i1, i2);
    }

    @Override
    public int getExecuteBatch() {
        return delegate.getExecuteBatch();
    }

    @Override
    public int sendBatch() throws SQLException {
        logger.beforeDbOperation("send batch " + preparedSql);
        try {
            return delegate.sendBatch();
        } finally {
            logger.afterDbOperation();
        }
    }

    @Override
    public void setARRAY(int i, ARRAY array) throws SQLException {
        delegate.setARRAY(i, array);
    }

    @Override
    public void setBfile(int i, BFILE bfile) throws SQLException {
        delegate.setBfile(i, bfile);
    }

    @Override
    public void setBFILE(int i, BFILE bfile) throws SQLException {
        delegate.setBFILE(i, bfile);
    }

    @Override
    public void setBLOB(int i, BLOB blob) throws SQLException {
        delegate.setBLOB(i, blob);
    }

    @Override
    public void setCHAR(int i, CHAR c) throws SQLException {
        delegate.setCHAR(i, c);
    }

    @Override
    public void setCLOB(int i, CLOB clob) throws SQLException {
        delegate.setCLOB(i, clob);
    }

    @Override
    public void setCursor(int i, ResultSet rs) throws SQLException {
        delegate.setCursor(i, rs);
    }

    @Override
    public void setCustomDatum(int i, CustomDatum cd) throws SQLException {
        delegate.setCustomDatum(i, cd);
    }

    @Override
    public void setORAData(int i, ORAData orad) throws SQLException {
        delegate.setORAData(i, orad);
    }

    @Override
    public void setDATE(int i, DATE date) throws SQLException {
        delegate.setDATE(i, date);
    }

    @Override
    public void setExecuteBatch(int i) throws SQLException {
        delegate.setExecuteBatch(i);
    }

    @Override
    public void setFixedCHAR(int i, String string) throws SQLException {
        delegate.setFixedCHAR(i, string);
    }

    @Override
    public void setNUMBER(int i, NUMBER number) throws SQLException {
        delegate.setNUMBER(i, number);
    }

    @Override
    public void setBinaryFloat(int i, float f) throws SQLException {
        delegate.setBinaryFloat(i, f);
    }

    @Override
    public void setBinaryFloat(int i, BINARY_FLOAT bnrflt) throws SQLException {
        delegate.setBinaryFloat(i, bnrflt);
    }

    @Override
    public void setBinaryDouble(int i, double d) throws SQLException {
        delegate.setBinaryDouble(i, d);
    }

    @Override
    public void setBinaryDouble(int i, BINARY_DOUBLE bnrdbl) throws SQLException {
        delegate.setBinaryDouble(i, bnrdbl);
    }

    @Override
    public void setOPAQUE(int i, OPAQUE opaque) throws SQLException {
        delegate.setOPAQUE(i, opaque);
    }

    @Override
    public void setOracleObject(int i, Datum datum) throws SQLException {
        delegate.setOracleObject(i, datum);
    }

    @Override
    public void setStructDescriptor(int i, StructDescriptor sd) throws SQLException {
        delegate.setStructDescriptor(i, sd);
    }

    @Override
    public void setRAW(int i, RAW raw) throws SQLException {
        delegate.setRAW(i, raw);
    }

    @Override
    public void setREF(int i, REF ref) throws SQLException {
        delegate.setREF(i, ref);
    }

    @Override
    public void setRefType(int i, REF ref) throws SQLException {
        delegate.setRefType(i, ref);
    }

    @Override
    public void setROWID(int i, ROWID rowid) throws SQLException {
        delegate.setROWID(i, rowid);
    }

    @Override
    public void setSTRUCT(int i, STRUCT struct) throws SQLException {
        delegate.setSTRUCT(i, struct);
    }

    @Override
    public void setTIMESTAMP(int i, TIMESTAMP tmstmp) throws SQLException {
        delegate.setTIMESTAMP(i, tmstmp);
    }

    @Override
    public void setTIMESTAMPTZ(int i, TIMESTAMPTZ t) throws SQLException {
        delegate.setTIMESTAMPTZ(i, t);
    }

    @Override
    public void setTIMESTAMPLTZ(int i, TIMESTAMPLTZ t) throws SQLException {
        delegate.setTIMESTAMPLTZ(i, t);
    }

    @Override
    public void setINTERVALYM(int i, INTERVALYM i1) throws SQLException {
        delegate.setINTERVALYM(i, i1);
    }

    @Override
    public void setINTERVALDS(int i, INTERVALDS i1) throws SQLException {
        delegate.setINTERVALDS(i, i1);
    }

    @Override
    public void setNullAtName(String string, int i, String string1) throws SQLException {
        delegate.setNullAtName(string, i, string1);
    }

    @Override
    public void setNullAtName(String string, int i) throws SQLException {
        delegate.setNullAtName(string, i);
    }

    @Override
    public void setBooleanAtName(String string, boolean bln) throws SQLException {
        delegate.setBooleanAtName(string, bln);
    }

    @Override
    public void setByteAtName(String string, byte b) throws SQLException {
        delegate.setByteAtName(string, b);
    }

    @Override
    public void setShortAtName(String string, short s) throws SQLException {
        delegate.setShortAtName(string, s);
    }

    @Override
    public void setIntAtName(String string, int i) throws SQLException {
        delegate.setIntAtName(string, i);
    }

    @Override
    public void setLongAtName(String string, long l) throws SQLException {
        delegate.setLongAtName(string, l);
    }

    @Override
    public void setFloatAtName(String string, float f) throws SQLException {
        delegate.setFloatAtName(string, f);
    }

    @Override
    public void setDoubleAtName(String string, double d) throws SQLException {
        delegate.setDoubleAtName(string, d);
    }

    @Override
    public void setBinaryFloatAtName(String string, float f) throws SQLException {
        delegate.setBinaryFloatAtName(string, f);
    }

    @Override
    public void setBinaryFloatAtName(String string, BINARY_FLOAT bnrflt) throws SQLException {
        delegate.setBinaryFloatAtName(string, bnrflt);
    }

    @Override
    public void setBinaryDoubleAtName(String string, double d) throws SQLException {
        delegate.setBinaryDoubleAtName(string, d);
    }

    @Override
    public void setBinaryDoubleAtName(String string, BINARY_DOUBLE bnrdbl) throws SQLException {
        delegate.setBinaryDoubleAtName(string, bnrdbl);
    }

    @Override
    public void setBigDecimalAtName(String string, BigDecimal bd) throws SQLException {
        delegate.setBigDecimalAtName(string, bd);
    }

    @Override
    public void setStringAtName(String string, String string1) throws SQLException {
        delegate.setStringAtName(string, string1);
    }

    @Override
    public void setStringForClob(int i, String string) throws SQLException {
        delegate.setStringForClob(i, string);
    }

    @Override
    public void setStringForClobAtName(String string, String string1) throws SQLException {
        delegate.setStringForClobAtName(string, string1);
    }

    @Override
    public void setFixedCHARAtName(String string, String string1) throws SQLException {
        delegate.setFixedCHARAtName(string, string1);
    }

    @Override
    public void setCursorAtName(String string, ResultSet rs) throws SQLException {
        delegate.setCursorAtName(string, rs);
    }

    @Override
    public void setROWIDAtName(String string, ROWID rowid) throws SQLException {
        delegate.setROWIDAtName(string, rowid);
    }

    @Override
    public void setArrayAtName(String string, Array array) throws SQLException {
        delegate.setArrayAtName(string, array);
    }

    @Override
    public void setARRAYAtName(String string, ARRAY array) throws SQLException {
        delegate.setARRAYAtName(string, array);
    }

    @Override
    public void setOPAQUEAtName(String string, OPAQUE opaque) throws SQLException {
        delegate.setOPAQUEAtName(string, opaque);
    }

    @Override
    public void setStructDescriptorAtName(String string, StructDescriptor sd) throws SQLException {
        delegate.setStructDescriptorAtName(string, sd);
    }

    @Override
    public void setSTRUCTAtName(String string, STRUCT struct) throws SQLException {
        delegate.setSTRUCTAtName(string, struct);
    }

    @Override
    public void setRAWAtName(String string, RAW raw) throws SQLException {
        delegate.setRAWAtName(string, raw);
    }

    @Override
    public void setCHARAtName(String string, CHAR c) throws SQLException {
        delegate.setCHARAtName(string, c);
    }

    @Override
    public void setDATEAtName(String string, DATE date) throws SQLException {
        delegate.setDATEAtName(string, date);
    }

    @Override
    public void setNUMBERAtName(String string, NUMBER number) throws SQLException {
        delegate.setNUMBERAtName(string, number);
    }

    @Override
    public void setBLOBAtName(String string, BLOB blob) throws SQLException {
        delegate.setBLOBAtName(string, blob);
    }

    @Override
    public void setBlobAtName(String string, Blob blob) throws SQLException {
        delegate.setBlobAtName(string, blob);
    }

    @Override
    public void setBlobAtName(String string, InputStream in, long l) throws SQLException {
        delegate.setBlobAtName(string, in, l);
    }

    @Override
    public void setBlobAtName(String string, InputStream in) throws SQLException {
        delegate.setBlobAtName(string, in);
    }

    @Override
    public void setCLOBAtName(String string, CLOB clob) throws SQLException {
        delegate.setCLOBAtName(string, clob);
    }

    @Override
    public void setClobAtName(String string, Clob clob) throws SQLException {
        delegate.setClobAtName(string, clob);
    }

    @Override
    public void setClobAtName(String string, Reader reader, long l) throws SQLException {
        delegate.setClobAtName(string, reader, l);
    }

    @Override
    public void setClobAtName(String string, Reader reader) throws SQLException {
        delegate.setClobAtName(string, reader);
    }

    @Override
    public void setBFILEAtName(String string, BFILE bfile) throws SQLException {
        delegate.setBFILEAtName(string, bfile);
    }

    @Override
    public void setBfileAtName(String string, BFILE bfile) throws SQLException {
        delegate.setBfileAtName(string, bfile);
    }

    @Override
    public void setBytesAtName(String string, byte[] bytes) throws SQLException {
        delegate.setBytesAtName(string, bytes);
    }

    @Override
    public void setBytesForBlob(int i, byte[] bytes) throws SQLException {
        delegate.setBytesForBlob(i, bytes);
    }

    @Override
    public void setBytesForBlobAtName(String string, byte[] bytes) throws SQLException {
        delegate.setBytesForBlobAtName(string, bytes);
    }

    @Override
    public void setDateAtName(String string, Date date) throws SQLException {
        delegate.setDateAtName(string, date);
    }

    @Override
    public void setDateAtName(String string, Date date, Calendar clndr) throws SQLException {
        delegate.setDateAtName(string, date, clndr);
    }

    @Override
    public void setTimeAtName(String string, Time time) throws SQLException {
        delegate.setTimeAtName(string, time);
    }

    @Override
    public void setTimeAtName(String string, Time time, Calendar clndr) throws SQLException {
        delegate.setTimeAtName(string, time, clndr);
    }

    @Override
    public void setTimestampAtName(String string, Timestamp tmstmp) throws SQLException {
        delegate.setTimestampAtName(string, tmstmp);
    }

    @Override
    public void setTimestampAtName(String string, Timestamp tmstmp, Calendar clndr) throws SQLException {
        delegate.setTimestampAtName(string, tmstmp, clndr);
    }

    @Override
    public void setINTERVALYMAtName(String string, INTERVALYM i) throws SQLException {
        delegate.setINTERVALYMAtName(string, i);
    }

    @Override
    public void setINTERVALDSAtName(String string, INTERVALDS i) throws SQLException {
        delegate.setINTERVALDSAtName(string, i);
    }

    @Override
    public void setTIMESTAMPAtName(String string, TIMESTAMP tmstmp) throws SQLException {
        delegate.setTIMESTAMPAtName(string, tmstmp);
    }

    @Override
    public void setTIMESTAMPTZAtName(String string, TIMESTAMPTZ t) throws SQLException {
        delegate.setTIMESTAMPTZAtName(string, t);
    }

    @Override
    public void setTIMESTAMPLTZAtName(String string, TIMESTAMPLTZ t) throws SQLException {
        delegate.setTIMESTAMPLTZAtName(string, t);
    }

    @Override
    public void setAsciiStreamAtName(String string, InputStream in, int i) throws SQLException {
        delegate.setAsciiStreamAtName(string, in, i);
    }

    @Override
    public void setAsciiStreamAtName(String string, InputStream in, long l) throws SQLException {
        delegate.setAsciiStreamAtName(string, in, l);
    }

    @Override
    public void setAsciiStreamAtName(String string, InputStream in) throws SQLException {
        delegate.setAsciiStreamAtName(string, in);
    }

    @Override
    public void setBinaryStreamAtName(String string, InputStream in, int i) throws SQLException {
        delegate.setBinaryStreamAtName(string, in, i);
    }

    @Override
    public void setBinaryStreamAtName(String string, InputStream in, long l) throws SQLException {
        delegate.setBinaryStreamAtName(string, in, l);
    }

    @Override
    public void setBinaryStreamAtName(String string, InputStream in) throws SQLException {
        delegate.setBinaryStreamAtName(string, in);
    }

    @Override
    public void setCharacterStreamAtName(String string, Reader reader, long l) throws SQLException {
        delegate.setCharacterStreamAtName(string, reader, l);
    }

    @Override
    public void setCharacterStreamAtName(String string, Reader reader) throws SQLException {
        delegate.setCharacterStreamAtName(string, reader);
    }

    @Override
    public void setUnicodeStreamAtName(String string, InputStream in, int i) throws SQLException {
        delegate.setUnicodeStreamAtName(string, in, i);
    }

    @Override
    public void setCustomDatumAtName(String string, CustomDatum cd) throws SQLException {
        delegate.setCustomDatumAtName(string, cd);
    }

    @Override
    public void setORADataAtName(String string, ORAData orad) throws SQLException {
        delegate.setORADataAtName(string, orad);
    }

    @Override
    public void setObjectAtName(String string, Object o, int i, int i1) throws SQLException {
        delegate.setObjectAtName(string, o, i, i1);
    }

    @Override
    public void setObjectAtName(String string, Object o, int i) throws SQLException {
        delegate.setObjectAtName(string, o, i);
    }

    @Override
    public void setRefTypeAtName(String string, REF ref) throws SQLException {
        delegate.setRefTypeAtName(string, ref);
    }

    @Override
    public void setRefAtName(String string, Ref ref) throws SQLException {
        delegate.setRefAtName(string, ref);
    }

    @Override
    public void setREFAtName(String string, REF ref) throws SQLException {
        delegate.setREFAtName(string, ref);
    }

    @Override
    public void setObjectAtName(String string, Object o) throws SQLException {
        delegate.setObjectAtName(string, o);
    }

    @Override
    public void setOracleObjectAtName(String string, Datum datum) throws SQLException {
        delegate.setOracleObjectAtName(string, datum);
    }

    @Override
    public void setURLAtName(String string, URL url) throws SQLException {
        delegate.setURLAtName(string, url);
    }

    @Override
    public void setCheckBindTypes(boolean bln) {
        delegate.setCheckBindTypes(bln);
    }

    @Override
    public void setPlsqlIndexTable(int i, Object o, int i1, int i2, int i3, int i4) throws SQLException {
        delegate.setPlsqlIndexTable(i, o, i1, i2, i3, i4);
    }

    @Override
    public void setFormOfUse(int i, short s) {
        delegate.setFormOfUse(i, s);
    }

    @Override
    public void setDisableStmtCaching(boolean bln) {
        delegate.setDisableStmtCaching(bln);
    }

    @Override
    public OracleParameterMetaData OracleGetParameterMetaData() throws SQLException {
        return delegate.OracleGetParameterMetaData();
    }

    @Override
    public void registerReturnParameter(int i, int i1) throws SQLException {
        delegate.registerReturnParameter(i, i1);
    }

    @Override
    public void registerReturnParameter(int i, int i1, int i2) throws SQLException {
        delegate.registerReturnParameter(i, i1, i2);
    }

    @Override
    public void registerReturnParameter(int i, int i1, String string) throws SQLException {
        delegate.registerReturnParameter(i, i1, string);
    }

    @Override
    public ResultSet getReturnResultSet() throws SQLException {
        return delegate.getReturnResultSet();
    }

    @Override
    public void setNCharacterStreamAtName(String string, Reader reader, long l) throws SQLException {
        delegate.setNCharacterStreamAtName(string, reader, l);
    }

    @Override
    public void setNCharacterStreamAtName(String string, Reader reader) throws SQLException {
        delegate.setNCharacterStreamAtName(string, reader);
    }

    @Override
    public void setNClobAtName(String string, NClob nclob) throws SQLException {
        delegate.setNClobAtName(string, nclob);
    }

    @Override
    public void setNClobAtName(String string, Reader reader, long l) throws SQLException {
        delegate.setNClobAtName(string, reader, l);
    }

    @Override
    public void setNClobAtName(String string, Reader reader) throws SQLException {
        delegate.setNClobAtName(string, reader);
    }

    @Override
    public void setNStringAtName(String string, String string1) throws SQLException {
        delegate.setNStringAtName(string, string1);
    }

    @Override
    public void setRowIdAtName(String string, RowId rowid) throws SQLException {
        delegate.setRowIdAtName(string, rowid);
    }

    @Override
    public void setSQLXMLAtName(String string, SQLXML sqlxml) throws SQLException {
        delegate.setSQLXMLAtName(string, sqlxml);
    }

    @Override
    public void clearDefines() throws SQLException {
        delegate.clearDefines();
    }

    @Override
    public void defineColumnType(int i, int i1) throws SQLException {
        delegate.defineColumnType(i, i1);
    }

    @Override
    public void defineColumnType(int i, int i1, int i2) throws SQLException {
        delegate.defineColumnType(i, i1, i2);
    }

    @Override
    public void defineColumnType(int i, int i1, int i2, short s) throws SQLException {
        delegate.defineColumnType(i, i1, i2, s);
    }

    @Override
    public void defineColumnTypeBytes(int i, int i1, int i2) throws SQLException {
        delegate.defineColumnTypeBytes(i, i1, i2);
    }

    @Override
    public void defineColumnTypeChars(int i, int i1, int i2) throws SQLException {
        delegate.defineColumnTypeChars(i, i1, i2);
    }

    @Override
    public void defineColumnType(int i, int i1, String string) throws SQLException {
        delegate.defineColumnType(i, i1, string);
    }

    @Override
    public int getRowPrefetch() {
        return delegate.getRowPrefetch();
    }

    @Override
    public void setResultSetCache(OracleResultSetCache orsc) throws SQLException {
        delegate.setResultSetCache(orsc);
    }

    @Override
    public void setRowPrefetch(int i) throws SQLException {
        delegate.setRowPrefetch(i);
    }

    @Override
    public int getLobPrefetchSize() {
        return delegate.getLobPrefetchSize();
    }

    @Override
    public void setLobPrefetchSize(int i) throws SQLException {
        delegate.setLobPrefetchSize(i);
    }

    @Override
    public void closeWithKey(String string) throws SQLException {
        delegate.closeWithKey(string);
    }

    @Override
    public int creationState() {
        return delegate.creationState();
    }

    @Override
    public boolean isNCHAR(int i) throws SQLException {
        return delegate.isNCHAR(i);
    }

    @Override
    public void setDatabaseChangeRegistration(DatabaseChangeRegistration dcr) throws SQLException {
        delegate.setDatabaseChangeRegistration(dcr);
    }

    @Override
    public String[] getRegisteredTableNames() throws SQLException {
        return delegate.getRegisteredTableNames();
    }

    @Override
    public long getRegisteredQueryId() throws SQLException {
        return delegate.getRegisteredQueryId();
    }

    @Override
    public ResultSet executeQuery() throws SQLException {
        final RadixConnection.DbOperationInfo info = new RadixConnection.DbOperationInfo(Arte.get(), readOnly);
        radixConnection.beforeDbOperation(info);
        boolean wasException = true;
        try {
            logger.beforeDbOperation(preparedSql);
            try {
                final ResultSet result = delegate.executeQuery();
                wasException = false;
                return result;
            } finally {
                logger.afterDbOperation();
            }
        } finally {
            radixConnection.afterDbOperation(info, wasException);
        }
    }

    @Override
    public int executeUpdate() throws SQLException {
        if (isReadOnly()) {
            throw new SQLException("RadixPreparedStatment is marked as readonly, but executeUpdate is invoked");
        }
        final RadixConnection.DbOperationInfo info = new RadixConnection.DbOperationInfo(Arte.get(), readOnly);
        radixConnection.beforeDbOperation(info);
        boolean wasException = true;
        try {
            logger.beforeDbOperation(preparedSql);
            try {
                final int result = delegate.executeUpdate();
                wasException = false;
                return result;
            } finally {
                logger.afterDbOperation();
            }
        } finally {
            radixConnection.afterDbOperation(info, wasException);
        }
    }

    @Override
    public void setNull(int parameterIndex, int sqlType) throws SQLException {
        delegate.setNull(parameterIndex, sqlType);
    }

    @Override
    public void setBoolean(int parameterIndex, boolean x) throws SQLException {
        delegate.setBoolean(parameterIndex, x);
    }

    @Override
    public void setByte(int parameterIndex, byte x) throws SQLException {
        delegate.setByte(parameterIndex, x);
    }

    @Override
    public void setShort(int parameterIndex, short x) throws SQLException {
        delegate.setShort(parameterIndex, x);
    }

    @Override
    public void setInt(int parameterIndex, int x) throws SQLException {
        delegate.setInt(parameterIndex, x);
    }

    @Override
    public void setLong(int parameterIndex, long x) throws SQLException {
        delegate.setLong(parameterIndex, x);
    }

    @Override
    public void setFloat(int parameterIndex, float x) throws SQLException {
        delegate.setFloat(parameterIndex, x);
    }

    @Override
    public void setDouble(int parameterIndex, double x) throws SQLException {
        delegate.setDouble(parameterIndex, x);
    }

    @Override
    public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {
        delegate.setBigDecimal(parameterIndex, x);
    }

    @Override
    public void setString(int parameterIndex, String x) throws SQLException {
        delegate.setString(parameterIndex, x);
    }

    @Override
    public void setBytes(int parameterIndex, byte[] x) throws SQLException {
        delegate.setBytes(parameterIndex, x);
    }

    @Override
    public void setDate(int parameterIndex, Date x) throws SQLException {
        delegate.setDate(parameterIndex, x);
    }

    @Override
    public void setTime(int parameterIndex, Time x) throws SQLException {
        delegate.setTime(parameterIndex, x);
    }

    @Override
    public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
        delegate.setTimestamp(parameterIndex, x);
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {
        delegate.setAsciiStream(parameterIndex, x, length);
    }

    @Override
    public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {
        delegate.setUnicodeStream(parameterIndex, x, length);
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {
        delegate.setBinaryStream(parameterIndex, x, length);
    }

    @Override
    public void clearParameters() throws SQLException {
        delegate.clearParameters();
    }

    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {
        delegate.setObject(parameterIndex, x, targetSqlType);
    }

    @Override
    public void setObject(int parameterIndex, Object x) throws SQLException {
        delegate.setObject(parameterIndex, x);
    }

    @Override
    public boolean execute() throws SQLException {
        final RadixConnection.DbOperationInfo info = new RadixConnection.DbOperationInfo(Arte.get(), readOnly);
        radixConnection.beforeDbOperation(info);
        boolean wasException = true;
        try {
            logger.beforeDbOperation(preparedSql);
            try {
                final boolean result = delegate.execute();
                wasException = false;
                return result;
            } finally {
                logger.afterDbOperation();
            }
        } finally {
            radixConnection.afterDbOperation(info, wasException);
        }
    }

    @Override
    public void addBatch() throws SQLException {
        delegate.addBatch();
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {
        delegate.setCharacterStream(parameterIndex, reader, length);
    }

    @Override
    public void setRef(int parameterIndex, Ref x) throws SQLException {
        delegate.setRef(parameterIndex, x);
    }

    @Override
    public void setBlob(int parameterIndex, Blob x) throws SQLException {
        delegate.setBlob(parameterIndex, x);
    }

    @Override
    public void setClob(int parameterIndex, Clob x) throws SQLException {
        delegate.setClob(parameterIndex, x);
    }

    @Override
    public void setArray(int parameterIndex, Array x) throws SQLException {
        delegate.setArray(parameterIndex, x);
    }

    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
        return delegate.getMetaData();
    }

    @Override
    public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {
        delegate.setDate(parameterIndex, x, cal);
    }

    @Override
    public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {
        delegate.setTime(parameterIndex, x, cal);
    }

    @Override
    public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
        delegate.setTimestamp(parameterIndex, x, cal);
    }

    @Override
    public void setNull(int parameterIndex, int sqlType, String typeName) throws SQLException {
        delegate.setNull(parameterIndex, sqlType, typeName);
    }

    @Override
    public void setURL(int parameterIndex, URL x) throws SQLException {
        delegate.setURL(parameterIndex, x);
    }

    @Override
    public ParameterMetaData getParameterMetaData() throws SQLException {
        return delegate.getParameterMetaData();
    }

    @Override
    public void setRowId(int parameterIndex, RowId x) throws SQLException {
        delegate.setRowId(parameterIndex, x);
    }

    @Override
    public void setNString(int parameterIndex, String value) throws SQLException {
        delegate.setNString(parameterIndex, value);
    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException {
        delegate.setNCharacterStream(parameterIndex, value, length);
    }

    @Override
    public void setNClob(int parameterIndex, NClob value) throws SQLException {
        delegate.setNClob(parameterIndex, value);
    }

    @Override
    public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
        delegate.setClob(parameterIndex, reader, length);
    }

    @Override
    public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {
        delegate.setBlob(parameterIndex, inputStream, length);
    }

    @Override
    public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
        delegate.setNClob(parameterIndex, reader, length);
    }

    @Override
    public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
        delegate.setSQLXML(parameterIndex, xmlObject);
    }

    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength) throws SQLException {
        delegate.setObject(parameterIndex, x, targetSqlType, scaleOrLength);
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {
        delegate.setAsciiStream(parameterIndex, x, length);
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {
        delegate.setBinaryStream(parameterIndex, x, length);
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {
        delegate.setCharacterStream(parameterIndex, reader, length);
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
        delegate.setAsciiStream(parameterIndex, x);
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
        delegate.setBinaryStream(parameterIndex, x);
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
        delegate.setCharacterStream(parameterIndex, reader);
    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
        delegate.setNCharacterStream(parameterIndex, value);
    }

    @Override
    public void setClob(int parameterIndex, Reader reader) throws SQLException {
        delegate.setClob(parameterIndex, reader);
    }

    @Override
    public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
        delegate.setBlob(parameterIndex, inputStream);
    }

    @Override
    public void setNClob(int parameterIndex, Reader reader) throws SQLException {
        delegate.setNClob(parameterIndex, reader);
    }

    @Override
    public ResultSet executeQuery(String sql) throws SQLException {
        return delegate.executeQuery(sql);
    }

    @Override
    public int executeUpdate(String sql) throws SQLException {
        return delegate.executeUpdate(sql);
    }

    @Override
    public void close() throws SQLException {
        delegate.close();
    }

    @Override
    public int getMaxFieldSize() throws SQLException {
        return delegate.getMaxFieldSize();
    }

    @Override
    public void setMaxFieldSize(int max) throws SQLException {
        delegate.setMaxFieldSize(max);
    }

    @Override
    public int getMaxRows() throws SQLException {
        return delegate.getMaxRows();
    }

    @Override
    public void setMaxRows(int max) throws SQLException {
        delegate.setMaxRows(max);
    }

    @Override
    public void setEscapeProcessing(boolean enable) throws SQLException {
        delegate.setEscapeProcessing(enable);
    }

    @Override
    public int getQueryTimeout() throws SQLException {
        return delegate.getQueryTimeout();
    }

    @Override
    public void setQueryTimeout(int seconds) throws SQLException {
        delegate.setQueryTimeout(seconds);
    }

    @Override
    public void cancel() throws SQLException {
        delegate.cancel();
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return delegate.getWarnings();
    }

    @Override
    public void clearWarnings() throws SQLException {
        delegate.clearWarnings();
    }

    @Override
    public void setCursorName(String name) throws SQLException {
        delegate.setCursorName(name);
    }

    @Override
    public boolean execute(String sql) throws SQLException {
        return delegate.execute(sql);
    }

    @Override
    public ResultSet getResultSet() throws SQLException {
        return delegate.getResultSet();
    }

    @Override
    public int getUpdateCount() throws SQLException {
        return delegate.getUpdateCount();
    }

    @Override
    public boolean getMoreResults() throws SQLException {
        return delegate.getMoreResults();
    }

    @Override
    public void setFetchDirection(int direction) throws SQLException {
        delegate.setFetchDirection(direction);
    }

    @Override
    public int getFetchDirection() throws SQLException {
        return delegate.getFetchDirection();
    }

    @Override
    public void setFetchSize(int rows) throws SQLException {
        delegate.setFetchSize(rows);
    }

    @Override
    public int getFetchSize() throws SQLException {
        return delegate.getFetchSize();
    }

    @Override
    public int getResultSetConcurrency() throws SQLException {
        return delegate.getResultSetConcurrency();
    }

    @Override
    public int getResultSetType() throws SQLException {
        return delegate.getResultSetType();
    }

    @Override
    public void addBatch(String sql) throws SQLException {
        delegate.addBatch(sql);
    }

    @Override
    public void clearBatch() throws SQLException {
        delegate.clearBatch();
    }

    @Override
    public int[] executeBatch() throws SQLException {
        return delegate.executeBatch();
    }

    @Override
    public Connection getConnection() throws SQLException {
        return radixConnection;
    }

    @Override
    public boolean getMoreResults(int current) throws SQLException {
        return delegate.getMoreResults(current);
    }

    @Override
    public ResultSet getGeneratedKeys() throws SQLException {
        return delegate.getGeneratedKeys();
    }

    @Override
    public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
        return delegate.executeUpdate(sql, autoGeneratedKeys);
    }

    @Override
    public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
        return delegate.executeUpdate(sql, columnIndexes);
    }

    @Override
    public int executeUpdate(String sql, String[] columnNames) throws SQLException {
        return delegate.executeUpdate(sql, columnNames);
    }

    @Override
    public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
        return delegate.execute(sql, autoGeneratedKeys);
    }

    @Override
    public boolean execute(String sql, int[] columnIndexes) throws SQLException {
        return delegate.execute(sql, columnIndexes);
    }

    @Override
    public boolean execute(String sql, String[] columnNames) throws SQLException {
        return delegate.execute(sql, columnNames);
    }

    @Override
    public int getResultSetHoldability() throws SQLException {
        return delegate.getResultSetHoldability();
    }

    @Override
    public boolean isClosed() throws SQLException {
        return delegate.isClosed();
    }

    @Override
    public void setPoolable(boolean poolable) throws SQLException {
        delegate.setPoolable(poolable);
    }

    @Override
    public boolean isPoolable() throws SQLException {
        return delegate.isPoolable();
    }

    @Override
    public void closeOnCompletion() throws SQLException {
        delegate.closeOnCompletion();
    }

    @Override
    public boolean isCloseOnCompletion() throws SQLException {
        return delegate.isCloseOnCompletion();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return delegate.unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return delegate.isWrapperFor(iface);
    }
}
