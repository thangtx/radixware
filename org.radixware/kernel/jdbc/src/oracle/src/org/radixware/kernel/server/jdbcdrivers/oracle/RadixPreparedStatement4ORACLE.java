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
package org.radixware.kernel.server.jdbcdrivers.oracle;

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
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Arrays;
import java.util.Calendar;
import java.util.concurrent.Callable;
import org.radixware.kernel.server.jdbc.RadixPreparedStatement;
import oracle.jdbc.OracleParameterMetaData;
import oracle.jdbc.OraclePreparedStatement;
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
import org.radixware.kernel.server.jdbc.DBOperationLoggerInterface;
import org.radixware.kernel.server.jdbc.EDbOperationType;
import org.radixware.kernel.server.jdbc.NullParameterValue;
import org.radixware.kernel.server.jdbc.RadixParametrizedStatement;

public class RadixPreparedStatement4ORACLE extends RadixStatement4ORACLE implements RadixPreparedStatement, OraclePreparedStatement, RadixParametrizedStatement {

    protected final Object[] lastParmValues;
    
    private final OraclePreparedStatement delegate;
    private final String preparedSql;
    protected final DBOperationLoggerInterface logger;

    public RadixPreparedStatement4ORACLE(final Connection radixConnection, PreparedStatement delegate, final String sql, final DBOperationLoggerInterface logger) throws SQLException {
        super(radixConnection, delegate, logger);
        this.delegate = (OraclePreparedStatement) delegate;
        preparedSql = sql;
        this.logger = logger;
        this.lastParmValues = new Object[delegate.getParameterMetaData().getParameterCount()];
    }

    @Override
    public void close() throws SQLException {
        super.close();
        Arrays.fill(lastParmValues,null);
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
        logger.beforeDbOperation(preparedSql, EDbOperationType.SEND_BATCH);
        try {
            return delegate.sendBatch();
        } finally {
            logger.afterDbOperation();
        }
    }

    @Override
    public void setARRAY(int i, ARRAY array) throws SQLException {
        delegate.setARRAY(i, array);
        lastParmValues[i-1] = array;
    }

    @Override
    public void setBfile(int i, BFILE bfile) throws SQLException {
        delegate.setBfile(i, bfile);
        lastParmValues[i-1] = bfile;
    }

    @Override
    public void setBFILE(int i, BFILE bfile) throws SQLException {
        delegate.setBFILE(i, bfile);
        lastParmValues[i-1] = bfile;
    }

    @Override
    public void setBLOB(int i, BLOB blob) throws SQLException {
        delegate.setBLOB(i, blob);
        lastParmValues[i-1] = blob;
    }

    @Override
    public void setCHAR(int i, CHAR c) throws SQLException {
        delegate.setCHAR(i, c);
        lastParmValues[i-1] = c;
    }

    @Override
    public void setCLOB(int i, CLOB clob) throws SQLException {
        delegate.setCLOB(i, clob);
        lastParmValues[i-1] = clob;
    }

    @Override
    public void setCursor(int i, ResultSet rs) throws SQLException {
        delegate.setCursor(i, rs);
        lastParmValues[i-1] = rs;
    }

    @Override
    public void setCustomDatum(int i, CustomDatum cd) throws SQLException {
        delegate.setCustomDatum(i, cd);
        lastParmValues[i-1] = cd;
    }

    @Override
    public void setORAData(int i, ORAData orad) throws SQLException {
        delegate.setORAData(i, orad);
        lastParmValues[i-1] = orad;
    }

    @Override
    public void setDATE(int i, DATE date) throws SQLException {
        delegate.setDATE(i, date);
        lastParmValues[i-1] = date;
    }

    @Override
    public void setExecuteBatch(int i) throws SQLException {
        delegate.setExecuteBatch(i);
    }

    @Override
    public void setFixedCHAR(int i, String string) throws SQLException {
        delegate.setFixedCHAR(i, string);
        lastParmValues[i-1] = string;
    }

    @Override
    public void setNUMBER(int i, NUMBER number) throws SQLException {
        delegate.setNUMBER(i, number);
        lastParmValues[i-1] = number;
    }

    @Override
    public void setBinaryFloat(int i, float f) throws SQLException {
        delegate.setBinaryFloat(i, f);
        lastParmValues[i-1] = f;
    }

    @Override
    public void setBinaryFloat(int i, BINARY_FLOAT bnrflt) throws SQLException {
        delegate.setBinaryFloat(i, bnrflt);
        lastParmValues[i-1] = bnrflt;
    }

    @Override
    public void setBinaryDouble(int i, double d) throws SQLException {
        delegate.setBinaryDouble(i, d);
        lastParmValues[i-1] = d;
    }

    @Override
    public void setBinaryDouble(int i, BINARY_DOUBLE bnrdbl) throws SQLException {
        delegate.setBinaryDouble(i, bnrdbl);
        lastParmValues[i-1] = bnrdbl;
    }

    @Override
    public void setOPAQUE(int i, OPAQUE opaque) throws SQLException {
        delegate.setOPAQUE(i, opaque);
        lastParmValues[i-1] = opaque;
    }

    @Override
    public void setOracleObject(int i, Datum datum) throws SQLException {
        delegate.setOracleObject(i, datum);
        lastParmValues[i-1] = datum;
    }

    @Override
    public void setStructDescriptor(int i, StructDescriptor sd) throws SQLException {
        delegate.setStructDescriptor(i, sd);
        lastParmValues[i-1] = sd;
    }

    @Override
    public void setRAW(int i, RAW raw) throws SQLException {
        delegate.setRAW(i, raw);
        lastParmValues[i-1] = raw;
    }

    @Override
    public void setREF(int i, REF ref) throws SQLException {
        delegate.setREF(i, ref);
        lastParmValues[i-1] = ref;
    }

    @Override
    public void setRefType(int i, REF ref) throws SQLException {
        delegate.setRefType(i, ref);
        lastParmValues[i-1] = ref;
    }

    @Override
    public void setROWID(int i, ROWID rowid) throws SQLException {
        delegate.setROWID(i, rowid);
        lastParmValues[i-1] = rowid;
    }

    @Override
    public void setSTRUCT(int i, STRUCT struct) throws SQLException {
        delegate.setSTRUCT(i, struct);
        lastParmValues[i-1] = struct;
    }

    @Override
    public void setTIMESTAMP(int i, TIMESTAMP tmstmp) throws SQLException {
        delegate.setTIMESTAMP(i, tmstmp);
        lastParmValues[i-1] = tmstmp;
    }

    @Override
    public void setTIMESTAMPTZ(int i, TIMESTAMPTZ t) throws SQLException {
        delegate.setTIMESTAMPTZ(i, t);
        lastParmValues[i-1] = t;
    }

    @Override
    public void setTIMESTAMPLTZ(int i, TIMESTAMPLTZ t) throws SQLException {
        delegate.setTIMESTAMPLTZ(i, t);
        lastParmValues[i-1] = t;
    }

    @Override
    public void setINTERVALYM(int i, INTERVALYM i1) throws SQLException {
        delegate.setINTERVALYM(i, i1);
        lastParmValues[i-1] = i1;
    }

    @Override
    public void setINTERVALDS(int i, INTERVALDS i1) throws SQLException {
        delegate.setINTERVALDS(i, i1);
        lastParmValues[i-1] = i1;
    }

    @Override
    public void setNullAtName(String string, int i, String string1) throws SQLException {
        delegate.setNullAtName(string, i, string1);
        lastParmValues[i-1] = string1;
    }

    @Override
    public void setNullAtName(String string, int i) throws SQLException {
        delegate.setNullAtName(string, i);
        lastParmValues[i-1] = i;
    }

    @Override
    public void setBooleanAtName(String string, boolean bln) throws SQLException {
        delegate.setBooleanAtName(string, bln);
        storeParameterValue(string,bln);
    }

    @Override
    public void setByteAtName(String string, byte b) throws SQLException {
        delegate.setByteAtName(string, b);
        storeParameterValue(string,b);
    }

    @Override
    public void setShortAtName(String string, short s) throws SQLException {
        delegate.setShortAtName(string, s);
        storeParameterValue(string,s);
    }

    @Override
    public void setIntAtName(String string, int i) throws SQLException {
        delegate.setIntAtName(string, i);
        storeParameterValue(string,i);
    }

    @Override
    public void setLongAtName(String string, long l) throws SQLException {
        delegate.setLongAtName(string, l);
        storeParameterValue(string,l);
    }

    @Override
    public void setFloatAtName(String string, float f) throws SQLException {
        delegate.setFloatAtName(string, f);
        storeParameterValue(string,f);
    }

    @Override
    public void setDoubleAtName(String string, double d) throws SQLException {
        delegate.setDoubleAtName(string, d);
        storeParameterValue(string,d);
    }

    @Override
    public void setBinaryFloatAtName(String string, float f) throws SQLException {
        delegate.setBinaryFloatAtName(string, f);
        storeParameterValue(string,f);
    }

    @Override
    public void setBinaryFloatAtName(String string, BINARY_FLOAT bnrflt) throws SQLException {
        delegate.setBinaryFloatAtName(string, bnrflt);
        storeParameterValue(string,bnrflt);
    }

    @Override
    public void setBinaryDoubleAtName(String string, double d) throws SQLException {
        delegate.setBinaryDoubleAtName(string, d);
        storeParameterValue(string,d);
    }

    @Override
    public void setBinaryDoubleAtName(String string, BINARY_DOUBLE bnrdbl) throws SQLException {
        delegate.setBinaryDoubleAtName(string, bnrdbl);
        storeParameterValue(string,bnrdbl);
    }

    @Override
    public void setBigDecimalAtName(String string, BigDecimal bd) throws SQLException {
        delegate.setBigDecimalAtName(string, bd);
        storeParameterValue(string,bd);
    }

    @Override
    public void setStringAtName(String string, String string1) throws SQLException {
        delegate.setStringAtName(string, string1);
        storeParameterValue(string,string1);
    }

    @Override
    public void setStringForClob(int i, String string) throws SQLException {
        delegate.setStringForClob(i, string);
        storeParameterValue(string,string);
    }

    @Override
    public void setStringForClobAtName(String string, String string1) throws SQLException {
        delegate.setStringForClobAtName(string, string1);
        storeParameterValue(string,string1);
    }

    @Override
    public void setFixedCHARAtName(String string, String string1) throws SQLException {
        delegate.setFixedCHARAtName(string, string1);
        storeParameterValue(string,string1);
    }

    @Override
    public void setCursorAtName(String string, ResultSet rs) throws SQLException {
        delegate.setCursorAtName(string, rs);
        storeParameterValue(string,rs);
    }

    @Override
    public void setROWIDAtName(String string, ROWID rowid) throws SQLException {
        delegate.setROWIDAtName(string, rowid);
        storeParameterValue(string,rowid);
    }

    @Override
    public void setArrayAtName(String string, Array array) throws SQLException {
        delegate.setArrayAtName(string, array);
        storeParameterValue(string,array);
    }

    @Override
    public void setARRAYAtName(String string, ARRAY array) throws SQLException {
        delegate.setARRAYAtName(string, array);
        storeParameterValue(string,array);
    }

    @Override
    public void setOPAQUEAtName(String string, OPAQUE opaque) throws SQLException {
        delegate.setOPAQUEAtName(string, opaque);
        storeParameterValue(string,opaque);
    }

    @Override
    public void setStructDescriptorAtName(String string, StructDescriptor sd) throws SQLException {
        delegate.setStructDescriptorAtName(string, sd);
        storeParameterValue(string,sd);
    }

    @Override
    public void setSTRUCTAtName(String string, STRUCT struct) throws SQLException {
        delegate.setSTRUCTAtName(string, struct);
        storeParameterValue(string,struct);
    }

    @Override
    public void setRAWAtName(String string, RAW raw) throws SQLException {
        delegate.setRAWAtName(string, raw);
        storeParameterValue(string,raw);
    }

    @Override
    public void setCHARAtName(String string, CHAR c) throws SQLException {
        delegate.setCHARAtName(string, c);
        storeParameterValue(string,c);
    }

    @Override
    public void setDATEAtName(String string, DATE date) throws SQLException {
        delegate.setDATEAtName(string, date);
        storeParameterValue(string,date);
    }

    @Override
    public void setNUMBERAtName(String string, NUMBER number) throws SQLException {
        delegate.setNUMBERAtName(string, number);
        storeParameterValue(string,number);
    }

    @Override
    public void setBLOBAtName(String string, BLOB blob) throws SQLException {
        delegate.setBLOBAtName(string, blob);
        storeParameterValue(string,blob);
    }

    @Override
    public void setBlobAtName(String string, Blob blob) throws SQLException {
        delegate.setBlobAtName(string, blob);
        storeParameterValue(string,blob);
    }

    @Override
    public void setBlobAtName(String string, InputStream in, long l) throws SQLException {
        delegate.setBlobAtName(string, in, l);
        storeParameterValue(string,in);
    }

    @Override
    public void setBlobAtName(String string, InputStream in) throws SQLException {
        delegate.setBlobAtName(string, in);
        storeParameterValue(string,in);
    }

    @Override
    public void setCLOBAtName(String string, CLOB clob) throws SQLException {
        delegate.setCLOBAtName(string, clob);
        storeParameterValue(string,clob);
    }

    @Override
    public void setClobAtName(String string, Clob clob) throws SQLException {
        delegate.setClobAtName(string, clob);
        storeParameterValue(string,clob);
    }

    @Override
    public void setClobAtName(String string, Reader reader, long l) throws SQLException {
        delegate.setClobAtName(string, reader, l);
        storeParameterValue(string,reader);
    }

    @Override
    public void setClobAtName(String string, Reader reader) throws SQLException {
        delegate.setClobAtName(string, reader);
        storeParameterValue(string,reader);
    }

    @Override
    public void setBFILEAtName(String string, BFILE bfile) throws SQLException {
        delegate.setBFILEAtName(string, bfile);
        storeParameterValue(string,bfile);
    }

    @Override
    public void setBfileAtName(String string, BFILE bfile) throws SQLException {
        delegate.setBfileAtName(string, bfile);
        storeParameterValue(string,bfile);
    }

    @Override
    public void setBytesAtName(String string, byte[] bytes) throws SQLException {
        delegate.setBytesAtName(string, bytes);
        storeParameterValue(string,bytes);
    }

    @Override
    public void setBytesForBlob(int i, byte[] bytes) throws SQLException {
        delegate.setBytesForBlob(i, bytes);
        lastParmValues[i-1] = bytes;
    }

    @Override
    public void setBytesForBlobAtName(String string, byte[] bytes) throws SQLException {
        delegate.setBytesForBlobAtName(string, bytes);
        storeParameterValue(string,bytes);
    }

    @Override
    public void setDateAtName(String string, Date date) throws SQLException {
        delegate.setDateAtName(string, date);
        storeParameterValue(string,date);
    }

    @Override
    public void setDateAtName(String string, Date date, Calendar clndr) throws SQLException {
        delegate.setDateAtName(string, date, clndr);
        storeParameterValue(string,date);
    }

    @Override
    public void setTimeAtName(String string, Time time) throws SQLException {
        delegate.setTimeAtName(string, time);
        storeParameterValue(string,time);
    }

    @Override
    public void setTimeAtName(String string, Time time, Calendar clndr) throws SQLException {
        delegate.setTimeAtName(string, time, clndr);
        storeParameterValue(string,time);
    }

    @Override
    public void setTimestampAtName(String string, Timestamp tmstmp) throws SQLException {
        delegate.setTimestampAtName(string, tmstmp);
        storeParameterValue(string,tmstmp);
    }

    @Override
    public void setTimestampAtName(String string, Timestamp tmstmp, Calendar clndr) throws SQLException {
        delegate.setTimestampAtName(string, tmstmp, clndr);
        storeParameterValue(string,tmstmp);
    }

    @Override
    public void setINTERVALYMAtName(String string, INTERVALYM i) throws SQLException {
        delegate.setINTERVALYMAtName(string, i);
        storeParameterValue(string,i);
    }

    @Override
    public void setINTERVALDSAtName(String string, INTERVALDS i) throws SQLException {
        delegate.setINTERVALDSAtName(string, i);
        storeParameterValue(string,i);
    }

    @Override
    public void setTIMESTAMPAtName(String string, TIMESTAMP tmstmp) throws SQLException {
        delegate.setTIMESTAMPAtName(string, tmstmp);
        storeParameterValue(string,tmstmp);
    }

    @Override
    public void setTIMESTAMPTZAtName(String string, TIMESTAMPTZ t) throws SQLException {
        delegate.setTIMESTAMPTZAtName(string, t);
        storeParameterValue(string,t);
    }

    @Override
    public void setTIMESTAMPLTZAtName(String string, TIMESTAMPLTZ t) throws SQLException {
        delegate.setTIMESTAMPLTZAtName(string, t);
        storeParameterValue(string,t);
    }

    @Override
    public void setAsciiStreamAtName(String string, InputStream in, int i) throws SQLException {
        delegate.setAsciiStreamAtName(string, in, i);
        storeParameterValue(string,in);
    }

    @Override
    public void setAsciiStreamAtName(String string, InputStream in, long l) throws SQLException {
        delegate.setAsciiStreamAtName(string, in, l);
        storeParameterValue(string,in);
    }

    @Override
    public void setAsciiStreamAtName(String string, InputStream in) throws SQLException {
        delegate.setAsciiStreamAtName(string, in);
        storeParameterValue(string,in);
    }

    @Override
    public void setBinaryStreamAtName(String string, InputStream in, int i) throws SQLException {
        delegate.setBinaryStreamAtName(string, in, i);
        storeParameterValue(string,in);
    }

    @Override
    public void setBinaryStreamAtName(String string, InputStream in, long l) throws SQLException {
        delegate.setBinaryStreamAtName(string, in, l);
        storeParameterValue(string,in);
    }

    @Override
    public void setBinaryStreamAtName(String string, InputStream in) throws SQLException {
        delegate.setBinaryStreamAtName(string, in);
        storeParameterValue(string,in);
    }

    @Override
    public void setCharacterStreamAtName(String string, Reader reader, long l) throws SQLException {
        delegate.setCharacterStreamAtName(string, reader, l);
        storeParameterValue(string,reader);
    }

    @Override
    public void setCharacterStreamAtName(String string, Reader reader) throws SQLException {
        delegate.setCharacterStreamAtName(string, reader);
        storeParameterValue(string,reader);
    }

    @Override
    public void setUnicodeStreamAtName(String string, InputStream in, int i) throws SQLException {
        delegate.setUnicodeStreamAtName(string, in, i);
        storeParameterValue(string,in);
    }

    @Override
    public void setCustomDatumAtName(String string, CustomDatum cd) throws SQLException {
        delegate.setCustomDatumAtName(string, cd);
        storeParameterValue(string,cd);
    }

    @Override
    public void setORADataAtName(String string, ORAData orad) throws SQLException {
        delegate.setORADataAtName(string, orad);
        storeParameterValue(string,orad);
    }

    @Override
    public void setObjectAtName(String string, Object o, int i, int i1) throws SQLException {
        delegate.setObjectAtName(string, o, i, i1);
        storeParameterValue(string,o);
    }

    @Override
    public void setObjectAtName(String string, Object o, int i) throws SQLException {
        delegate.setObjectAtName(string, o, i);
        storeParameterValue(string,o);
    }

    @Override
    public void setRefTypeAtName(String string, REF ref) throws SQLException {
        delegate.setRefTypeAtName(string, ref);
        storeParameterValue(string,ref);
    }

    @Override
    public void setRefAtName(String string, Ref ref) throws SQLException {
        delegate.setRefAtName(string, ref);
        storeParameterValue(string,ref);
    }

    @Override
    public void setREFAtName(String string, REF ref) throws SQLException {
        delegate.setREFAtName(string, ref);
        storeParameterValue(string,ref);
    }

    @Override
    public void setObjectAtName(String string, Object o) throws SQLException {
        delegate.setObjectAtName(string, o);
        storeParameterValue(string,o);
    }

    @Override
    public void setOracleObjectAtName(String string, Datum datum) throws SQLException {
        delegate.setOracleObjectAtName(string, datum);
        storeParameterValue(string,datum);
    }

    @Override
    public void setURLAtName(String string, URL url) throws SQLException {
        delegate.setURLAtName(string, url);
        storeParameterValue(string,url);
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
        return objWrapper.wrapResultSet(delegate.getReturnResultSet());
    }

    @Override
    public void setNCharacterStreamAtName(String string, Reader reader, long l) throws SQLException {
        delegate.setNCharacterStreamAtName(string, reader, l);
        storeParameterValue(string,reader);
    }

    @Override
    public void setNCharacterStreamAtName(String string, Reader reader) throws SQLException {
        delegate.setNCharacterStreamAtName(string, reader);
        storeParameterValue(string,reader);
    }

    @Override
    public void setNClobAtName(String string, NClob nclob) throws SQLException {
        delegate.setNClobAtName(string, nclob);
        storeParameterValue(string,nclob);
    }

    @Override
    public void setNClobAtName(String string, Reader reader, long l) throws SQLException {
        delegate.setNClobAtName(string, reader, l);
        storeParameterValue(string,reader);
    }

    @Override
    public void setNClobAtName(String string, Reader reader) throws SQLException {
        delegate.setNClobAtName(string, reader);
        storeParameterValue(string,reader);
    }

    @Override
    public void setNStringAtName(String string, String string1) throws SQLException {
        delegate.setNStringAtName(string, string1);
        storeParameterValue(string,string1);
    }

    @Override
    public void setRowIdAtName(String string, RowId rowid) throws SQLException {
        delegate.setRowIdAtName(string, rowid);
        storeParameterValue(string,rowid);
    }

    @Override
    public void setSQLXMLAtName(String string, SQLXML sqlxml) throws SQLException {
        delegate.setSQLXMLAtName(string, sqlxml);
        storeParameterValue(string,sqlxml);
    }

    @Override
    public ResultSet executeQuery() throws SQLException {
        return executeWrappedOperationThrowsSqlEx(new Callable<ResultSet>() {

            @Override
            public ResultSet call() throws Exception {
                return objWrapper.wrapResultSet(delegate.executeQuery());
            }

        }, isReadOnly(), preparedSql);
    }

    @Override
    public int executeUpdate() throws SQLException {
        return executeWrappedOperationThrowsSqlEx(new Callable<Integer>() {

            @Override
            public Integer call() throws Exception {
                return delegate.executeUpdate();
            }

        }, false, preparedSql);
    }

    @Override
    public void setNull(int parameterIndex, int sqlType) throws SQLException {
        delegate.setNull(parameterIndex, sqlType);
        lastParmValues[parameterIndex-1] = NullParameterValue.instance;
    }

    @Override
    public void setBoolean(int parameterIndex, boolean x) throws SQLException {
        delegate.setBoolean(parameterIndex, x);
        lastParmValues[parameterIndex-1] = x;
    }

    @Override
    public void setByte(int parameterIndex, byte x) throws SQLException {
        delegate.setByte(parameterIndex, x);
        lastParmValues[parameterIndex-1] = x;
    }

    @Override
    public void setShort(int parameterIndex, short x) throws SQLException {
        delegate.setShort(parameterIndex, x);
        lastParmValues[parameterIndex-1] = x;
    }

    @Override
    public void setInt(int parameterIndex, int x) throws SQLException {
        delegate.setInt(parameterIndex, x);
        lastParmValues[parameterIndex-1] = x;
    }

    @Override
    public void setLong(int parameterIndex, long x) throws SQLException {
        delegate.setLong(parameterIndex, x);
        lastParmValues[parameterIndex-1] = x;
    }

    @Override
    public void setFloat(int parameterIndex, float x) throws SQLException {
        delegate.setFloat(parameterIndex, x);
        lastParmValues[parameterIndex-1] = x;
    }

    @Override
    public void setDouble(int parameterIndex, double x) throws SQLException {
        delegate.setDouble(parameterIndex, x);
        lastParmValues[parameterIndex-1] = x;
    }

    @Override
    public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {
        delegate.setBigDecimal(parameterIndex, x);
        lastParmValues[parameterIndex-1] = x;
    }

    @Override
    public void setString(int parameterIndex, String x) throws SQLException {
        delegate.setString(parameterIndex, x);
        lastParmValues[parameterIndex-1] = x;
    }

    @Override
    public void setBytes(int parameterIndex, byte[] x) throws SQLException {
        delegate.setBytes(parameterIndex, x);
        lastParmValues[parameterIndex-1] = x;
    }

    @Override
    public void setDate(int parameterIndex, Date x) throws SQLException {
        delegate.setDate(parameterIndex, x);
        lastParmValues[parameterIndex-1] = x;
    }

    @Override
    public void setTime(int parameterIndex, Time x) throws SQLException {
        delegate.setTime(parameterIndex, x);
        lastParmValues[parameterIndex-1] = x;
    }

    @Override
    public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
        delegate.setTimestamp(parameterIndex, x);
        lastParmValues[parameterIndex-1] = x;
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {
        delegate.setAsciiStream(parameterIndex, x, length);
        lastParmValues[parameterIndex-1] = x;
    }

    @Override
    public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {
        delegate.setUnicodeStream(parameterIndex, x, length);
        lastParmValues[parameterIndex-1] = x;
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {
        delegate.setBinaryStream(parameterIndex, x, length);
        lastParmValues[parameterIndex-1] = x;
    }

    @Override
    public void clearParameters() throws SQLException {
        delegate.clearParameters();
        Arrays.fill(lastParmValues,null);
    }

    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {
        delegate.setObject(parameterIndex, x, targetSqlType);
        lastParmValues[parameterIndex-1] = x;
    }

    @Override
    public void setObject(int parameterIndex, Object x) throws SQLException {
        delegate.setObject(parameterIndex, x);
        lastParmValues[parameterIndex-1] = x;
    }

    @Override
    public boolean execute() throws SQLException {
        return executeWrappedOperationThrowsSqlEx(new Callable<Boolean>() {

            @Override
            public Boolean call() throws Exception {
                return delegate.execute();
            }

        }, isReadOnly(), preparedSql);
    }

    @Override
    public void addBatch() throws SQLException {
        delegate.addBatch();
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {
        delegate.setCharacterStream(parameterIndex, reader, length);
        lastParmValues[parameterIndex-1] = reader;
    }

    @Override
    public void setRef(int parameterIndex, Ref x) throws SQLException {
        delegate.setRef(parameterIndex, x);
        lastParmValues[parameterIndex-1] = x;
    }

    @Override
    public void setBlob(int parameterIndex, Blob x) throws SQLException {
        delegate.setBlob(parameterIndex, x);
        lastParmValues[parameterIndex-1] = x;
    }

    @Override
    public void setClob(int parameterIndex, Clob x) throws SQLException {
        delegate.setClob(parameterIndex, x);
    }

    @Override
    public void setArray(int parameterIndex, Array x) throws SQLException {
        delegate.setArray(parameterIndex, x);
        lastParmValues[parameterIndex-1] = x;
    }

    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
        return delegate.getMetaData();
    }

    @Override
    public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {
        delegate.setDate(parameterIndex, x, cal);
        lastParmValues[parameterIndex-1] = x;
    }

    @Override
    public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {
        delegate.setTime(parameterIndex, x, cal);
        lastParmValues[parameterIndex-1] = x;
    }

    @Override
    public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
        delegate.setTimestamp(parameterIndex, x, cal);
        lastParmValues[parameterIndex-1] = x;
    }

    @Override
    public void setNull(int parameterIndex, int sqlType, String typeName) throws SQLException {
        delegate.setNull(parameterIndex, sqlType, typeName);
        lastParmValues[parameterIndex-1] = NullParameterValue.instance;
    }

    @Override
    public void setURL(int parameterIndex, URL x) throws SQLException {
        delegate.setURL(parameterIndex, x);
        lastParmValues[parameterIndex-1] = x;
    }

    @Override
    public ParameterMetaData getParameterMetaData() throws SQLException {
        return delegate.getParameterMetaData();
    }

    @Override
    public void setRowId(int parameterIndex, RowId x) throws SQLException {
        delegate.setRowId(parameterIndex, x);
        lastParmValues[parameterIndex-1] = x;
    }

    @Override
    public void setNString(int parameterIndex, String value) throws SQLException {
        delegate.setNString(parameterIndex, value);
        lastParmValues[parameterIndex-1] = value;
    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException {
        delegate.setNCharacterStream(parameterIndex, value, length);
        lastParmValues[parameterIndex-1] = value;
    }

    @Override
    public void setNClob(int parameterIndex, NClob value) throws SQLException {
        delegate.setNClob(parameterIndex, value);
        lastParmValues[parameterIndex-1] = value;
    }

    @Override
    public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
        delegate.setClob(parameterIndex, reader, length);
        lastParmValues[parameterIndex-1] = reader;
    }

    @Override
    public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {
        delegate.setBlob(parameterIndex, inputStream, length);
        lastParmValues[parameterIndex-1] = inputStream;
    }

    @Override
    public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
        delegate.setNClob(parameterIndex, reader, length);
        lastParmValues[parameterIndex-1] = reader;
    }

    @Override
    public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
        delegate.setSQLXML(parameterIndex, xmlObject);
        lastParmValues[parameterIndex-1] = xmlObject;
    }

    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength) throws SQLException {
        delegate.setObject(parameterIndex, x, targetSqlType, scaleOrLength);
        lastParmValues[parameterIndex-1] = x;
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {
        delegate.setAsciiStream(parameterIndex, x, length);
        lastParmValues[parameterIndex-1] = x;
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {
        delegate.setBinaryStream(parameterIndex, x, length);
        lastParmValues[parameterIndex-1] = x;
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {
        delegate.setCharacterStream(parameterIndex, reader, length);
        lastParmValues[parameterIndex-1] = reader;
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
        delegate.setAsciiStream(parameterIndex, x);
        lastParmValues[parameterIndex-1] = x;
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
        delegate.setBinaryStream(parameterIndex, x);
        lastParmValues[parameterIndex-1] = x;
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
        delegate.setCharacterStream(parameterIndex, reader);
        lastParmValues[parameterIndex-1] = reader;
    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
        delegate.setNCharacterStream(parameterIndex, value);
        lastParmValues[parameterIndex-1] = value;
    }

    @Override
    public void setClob(int parameterIndex, Reader reader) throws SQLException {
        delegate.setClob(parameterIndex, reader);
        lastParmValues[parameterIndex-1] = reader;
    }

    @Override
    public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
        delegate.setBlob(parameterIndex, inputStream);
        lastParmValues[parameterIndex-1] = inputStream;
    }

    @Override
    public void setNClob(int parameterIndex, Reader reader) throws SQLException {
        delegate.setNClob(parameterIndex, reader);
        lastParmValues[parameterIndex-1] = reader;
    }

    @Override
    public Object[] getParamValues() {
        return lastParmValues;
    }

    protected void storeParameterValue(final String name, final Object something) throws SQLException {
        final int index = calculateColumnByName(name);
        
        if (index > 0) {
            lastParmValues[index-1] = something;
        }
    }

    protected int calculateColumnByName(final String name) throws SQLException {
        return 0;   // ???????? No official way to get parameter names
    }

    @Override
    public void setInt(int parameterIndex, Integer x) throws SQLException {
        if (x == null) {
            setNull(parameterIndex, Types.BIGINT);
        } else {
            setInt(parameterIndex, x.intValue());
        }
    }

    @Override
    public void setLong(int parameterIndex, Long x) throws SQLException {
        if (x == null) {
            setNull(parameterIndex, Types.BIGINT);
        } else {
            setLong(parameterIndex, x.longValue());
        }
    }
    
}
