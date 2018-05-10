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
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;
import org.radixware.kernel.server.jdbc.RadixCallableStatement;
import oracle.jdbc.OracleCallableStatement;
import oracle.sql.ARRAY;
import oracle.sql.BFILE;
import oracle.sql.BINARY_DOUBLE;
import oracle.sql.BINARY_FLOAT;
import oracle.sql.BLOB;
import oracle.sql.CHAR;
import oracle.sql.CLOB;
import oracle.sql.CustomDatum;
import oracle.sql.CustomDatumFactory;
import oracle.sql.DATE;
import oracle.sql.Datum;
import oracle.sql.INTERVALDS;
import oracle.sql.INTERVALYM;
import oracle.sql.NUMBER;
import oracle.sql.OPAQUE;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;
import oracle.sql.RAW;
import oracle.sql.REF;
import oracle.sql.ROWID;
import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;
import oracle.sql.TIMESTAMP;
import oracle.sql.TIMESTAMPLTZ;
import oracle.sql.TIMESTAMPTZ;
import org.radixware.kernel.server.jdbc.DBOperationLoggerInterface;
import org.radixware.kernel.server.jdbc.NullParameterValue;


public class RadixCallableStatement4ORACLE extends RadixPreparedStatement4ORACLE implements RadixCallableStatement, OracleCallableStatement {

    private final OracleCallableStatement delegate;

    public RadixCallableStatement4ORACLE(final Connection radixConnection, CallableStatement delegate, final String sql, final DBOperationLoggerInterface logger) throws SQLException {
        super(radixConnection, delegate, sql, logger);
        this.delegate = (OracleCallableStatement) delegate;
    }

    @Override
    public void close() throws SQLException {
        super.close();
    }
    
    @Override
    public ARRAY getARRAY(int i) throws SQLException {
        return delegate.getARRAY(i);
    }

    @Override
    public InputStream getAsciiStream(int i) throws SQLException {
        return delegate.getAsciiStream(i);
    }

    @Override
    public BFILE getBFILE(int i) throws SQLException {
        return delegate.getBFILE(i);
    }

    @Override
    public BFILE getBfile(int i) throws SQLException {
        return delegate.getBfile(i);
    }

    @Override
    public InputStream getBinaryStream(int i) throws SQLException {
        return delegate.getBinaryStream(i);
    }

    @Override
    public InputStream getBinaryStream(String string) throws SQLException {
        return delegate.getBinaryStream(string);
    }

    @Override
    public BLOB getBLOB(int i) throws SQLException {
        return delegate.getBLOB(i);
    }

    @Override
    public CHAR getCHAR(int i) throws SQLException {
        return delegate.getCHAR(i);
    }

    @Override
    public Reader getCharacterStream(int i) throws SQLException {
        return objWrapper.wrapReader(delegate.getCharacterStream(i));
    }

    @Override
    public CLOB getCLOB(int i) throws SQLException {
        return delegate.getCLOB(i);
    }

    @Override
    public ResultSet getCursor(int i) throws SQLException {
        return delegate.getCursor(i);
    }

    @Override
    public Object getCustomDatum(int i, CustomDatumFactory cdf) throws SQLException {
        return delegate.getCustomDatum(i, cdf);
    }

    @Override
    public Object getORAData(int i, ORADataFactory oradf) throws SQLException {
        return delegate.getORAData(i, oradf);
    }

    @Override
    public Object getAnyDataEmbeddedObject(int i) throws SQLException {
        return delegate.getAnyDataEmbeddedObject(i);
    }

    @Override
    public DATE getDATE(int i) throws SQLException {
        return delegate.getDATE(i);
    }

    @Override
    public NUMBER getNUMBER(int i) throws SQLException {
        return delegate.getNUMBER(i);
    }

    @Override
    public OPAQUE getOPAQUE(int i) throws SQLException {
        return delegate.getOPAQUE(i);
    }

    @Override
    public Datum getOracleObject(int i) throws SQLException {
        return delegate.getOracleObject(i);
    }

    @Override
    public RAW getRAW(int i) throws SQLException {
        return delegate.getRAW(i);
    }

    @Override
    public REF getREF(int i) throws SQLException {
        return delegate.getREF(i);
    }

    @Override
    public ROWID getROWID(int i) throws SQLException {
        return delegate.getROWID(i);
    }

    @Override
    public STRUCT getSTRUCT(int i) throws SQLException {
        return delegate.getSTRUCT(i);
    }

    @Override
    public INTERVALYM getINTERVALYM(int i) throws SQLException {
        return delegate.getINTERVALYM(i);
    }

    @Override
    public INTERVALDS getINTERVALDS(int i) throws SQLException {
        return delegate.getINTERVALDS(i);
    }

    @Override
    public TIMESTAMP getTIMESTAMP(int i) throws SQLException {
        return delegate.getTIMESTAMP(i);
    }

    @Override
    public TIMESTAMPTZ getTIMESTAMPTZ(int i) throws SQLException {
        return delegate.getTIMESTAMPTZ(i);
    }

    @Override
    public TIMESTAMPLTZ getTIMESTAMPLTZ(int i) throws SQLException {
        return delegate.getTIMESTAMPLTZ(i);
    }

    @Override
    public InputStream getUnicodeStream(int i) throws SQLException {
        return delegate.getUnicodeStream(i);
    }

    @Override
    public InputStream getUnicodeStream(String string) throws SQLException {
        return delegate.getUnicodeStream(string);
    }

    @Override
    public void registerOutParameter(int i, int i1, int i2, int i3) throws SQLException {
        delegate.registerOutParameter(i, i1, i2, i3);
    }

    @Override
    public void registerOutParameterBytes(int i, int i1, int i2, int i3) throws SQLException {
        delegate.registerOutParameterBytes(i, i1, i2, i3);
    }

    @Override
    public void registerOutParameterChars(int i, int i1, int i2, int i3) throws SQLException {
        delegate.registerOutParameterChars(i, i1, i2, i3);
    }

    @Override
    public int sendBatch() throws SQLException {
        return delegate.sendBatch();
    }

    @Override
    public void setExecuteBatch(int i) throws SQLException {
        delegate.setExecuteBatch(i);
    }

    @Override
    public Object getPlsqlIndexTable(int i) throws SQLException {
        return delegate.getPlsqlIndexTable(i);
    }

    @Override
    public Object getPlsqlIndexTable(int i, Class type) throws SQLException {
        return delegate.getPlsqlIndexTable(i, type);
    }

    @Override
    public Datum[] getOraclePlsqlIndexTable(int i) throws SQLException {
        return delegate.getOraclePlsqlIndexTable(i);
    }

    @Override
    public void registerIndexTableOutParameter(int i, int i1, int i2, int i3) throws SQLException {
        delegate.registerIndexTableOutParameter(i, i1, i2, i3);
    }

    @Override
    public void setBinaryFloat(String string, BINARY_FLOAT bnrflt) throws SQLException {
        delegate.setBinaryFloat(string, bnrflt);
        storeParameterValue(string,bnrflt);
    }

    @Override
    public void setBinaryDouble(String string, BINARY_DOUBLE bnrdbl) throws SQLException {
        delegate.setBinaryDouble(string, bnrdbl);
        storeParameterValue(string,bnrdbl);
    }

    @Override
    public void setStringForClob(String string, String string1) throws SQLException {
        delegate.setStringForClob(string, string1);
        storeParameterValue(string,string1);
    }

    @Override
    public void setBytesForBlob(String string, byte[] bytes) throws SQLException {
        delegate.setBytesForBlob(string, bytes);
        storeParameterValue(string,bytes);
    }

    @Override
    public void registerOutParameter(String string, int i, int i1, int i2) throws SQLException {
        delegate.registerOutParameter(string, i, i1, i2);
    }

    @Override
    public void setNull(String string, int i, String string1) throws SQLException {
        delegate.setNull(string, i, string1);
        storeParameterValue(string,NullParameterValue.instance);
    }

    @Override
    public void setNull(String string, int i) throws SQLException {
        delegate.setNull(string, i);
        storeParameterValue(string,NullParameterValue.instance);
    }

    @Override
    public void setBoolean(String string, boolean bln) throws SQLException {
        delegate.setBoolean(string, bln);
        storeParameterValue(string,bln);
    }

    @Override
    public void setByte(String string, byte b) throws SQLException {
        delegate.setByte(string, b);
        storeParameterValue(string,b);
    }

    @Override
    public void setShort(String string, short s) throws SQLException {
        delegate.setShort(string, s);
        storeParameterValue(string,s);
    }

    @Override
    public void setInt(String string, int i) throws SQLException {
        delegate.setInt(string, i);
        storeParameterValue(string,i);
    }

    @Override
    public void setLong(String string, long l) throws SQLException {
        delegate.setLong(string, l);
        storeParameterValue(string,l);
    }

    @Override
    public void setFloat(String string, float f) throws SQLException {
        delegate.setFloat(string, f);
        storeParameterValue(string,f);
    }

    @Override
    public void setBinaryFloat(String string, float f) throws SQLException {
        delegate.setBinaryFloat(string, f);
        storeParameterValue(string,f);
    }

    @Override
    public void setBinaryDouble(String string, double d) throws SQLException {
        delegate.setBinaryDouble(string, d);
        storeParameterValue(string,d);
    }

    @Override
    public void setDouble(String string, double d) throws SQLException {
        delegate.setDouble(string, d);
        storeParameterValue(string,d);
    }

    @Override
    public void setBigDecimal(String string, BigDecimal bd) throws SQLException {
        delegate.setBigDecimal(string, bd);
        storeParameterValue(string,bd);
    }

    @Override
    public void setString(String string, String string1) throws SQLException {
        delegate.setString(string, string1);
        storeParameterValue(string,string1);
    }

    @Override
    public void setFixedCHAR(String string, String string1) throws SQLException {
        delegate.setFixedCHAR(string, string1);
        storeParameterValue(string,string1);
    }

    @Override
    public void setCursor(String string, ResultSet rs) throws SQLException {
        delegate.setCursor(string, rs);
        storeParameterValue(string,rs);
    }

    @Override
    public void setROWID(String string, ROWID rowid) throws SQLException {
        delegate.setROWID(string, rowid);
        storeParameterValue(string,rowid);
    }

    @Override
    public void setRAW(String string, RAW raw) throws SQLException {
        delegate.setRAW(string, raw);
        storeParameterValue(string,raw);
    }

    @Override
    public void setCHAR(String string, CHAR c) throws SQLException {
        delegate.setCHAR(string, c);
        storeParameterValue(string,c);
    }

    @Override
    public void setDATE(String string, DATE date) throws SQLException {
        delegate.setDATE(string, date);
        storeParameterValue(string,date);
    }

    @Override
    public void setNUMBER(String string, NUMBER number) throws SQLException {
        delegate.setNUMBER(string, number);
        storeParameterValue(string,number);
    }

    @Override
    public void setBLOB(String string, BLOB blob) throws SQLException {
        delegate.setBLOB(string, blob);
        storeParameterValue(string,blob);
    }

    @Override
    public void setBlob(String string, Blob blob) throws SQLException {
        delegate.setBlob(string, blob);
        storeParameterValue(string,blob);
    }

    @Override
    public void setCLOB(String string, CLOB clob) throws SQLException {
        delegate.setCLOB(string, clob);
        storeParameterValue(string,clob);
    }

    @Override
    public void setClob(String string, Clob clob) throws SQLException {
        delegate.setClob(string, clob);
        storeParameterValue(string,clob);
    }

    @Override
    public void setBFILE(String string, BFILE bfile) throws SQLException {
        delegate.setBFILE(string, bfile);
        storeParameterValue(string,bfile);
    }

    @Override
    public void setBfile(String string, BFILE bfile) throws SQLException {
        delegate.setBfile(string, bfile);
        storeParameterValue(string,bfile);
    }

    @Override
    public void setBytes(String string, byte[] bytes) throws SQLException {
        delegate.setBytes(string, bytes);
        storeParameterValue(string,bytes);
    }

    @Override
    public void setDate(String string, Date date) throws SQLException {
        delegate.setDate(string, date);
        storeParameterValue(string,date);
    }

    @Override
    public void setTime(String string, Time time) throws SQLException {
        delegate.setTime(string, time);
        storeParameterValue(string,time);
    }

    @Override
    public void setTimestamp(String string, Timestamp tmstmp) throws SQLException {
        delegate.setTimestamp(string, tmstmp);
        storeParameterValue(string,tmstmp);
    }

    @Override
    public void setINTERVALYM(String string, INTERVALYM i) throws SQLException {
        delegate.setINTERVALYM(string, i);
        storeParameterValue(string,i);
    }

    @Override
    public void setINTERVALDS(String string, INTERVALDS i) throws SQLException {
        delegate.setINTERVALDS(string, i);
        storeParameterValue(string,i);
    }

    @Override
    public void setTIMESTAMP(String string, TIMESTAMP tmstmp) throws SQLException {
        delegate.setTIMESTAMP(string, tmstmp);
        storeParameterValue(string,tmstmp);
    }

    @Override
    public void setTIMESTAMPTZ(String string, TIMESTAMPTZ t) throws SQLException {
        delegate.setTIMESTAMPTZ(string, t);
        storeParameterValue(string,t);
    }

    @Override
    public void setTIMESTAMPLTZ(String string, TIMESTAMPLTZ t) throws SQLException {
        delegate.setTIMESTAMPLTZ(string, t);
        storeParameterValue(string,t);
    }

    @Override
    public void setAsciiStream(String string, InputStream in, int i) throws SQLException {
        delegate.setAsciiStream(string, in, i);
        storeParameterValue(string,i);
    }

    @Override
    public void setBinaryStream(String string, InputStream in, int i) throws SQLException {
        delegate.setBinaryStream(string, in, i);
        storeParameterValue(string,i);
    }

    @Override
    public void setUnicodeStream(String string, InputStream in, int i) throws SQLException {
        delegate.setUnicodeStream(string, in, i);
        storeParameterValue(string,in);
    }

    @Override
    public void setCharacterStream(String string, Reader reader, int i) throws SQLException {
        delegate.setCharacterStream(string, reader, i);
        storeParameterValue(string,reader);
    }

    @Override
    public void setDate(String string, Date date, Calendar clndr) throws SQLException {
        delegate.setDate(string, date, clndr);
        storeParameterValue(string,date);
    }

    @Override
    public void setTime(String string, Time time, Calendar clndr) throws SQLException {
        delegate.setTime(string, time, clndr);
        storeParameterValue(string,time);
    }

    @Override
    public void setTimestamp(String string, Timestamp tmstmp, Calendar clndr) throws SQLException {
        delegate.setTimestamp(string, tmstmp, clndr);
        storeParameterValue(string,tmstmp);
    }

    @Override
    public void setURL(String string, URL url) throws SQLException {
        delegate.setURL(string, url);
        storeParameterValue(string,url);
    }

    @Override
    public void setArray(String string, Array array) throws SQLException {
        delegate.setArray(string, array);
        storeParameterValue(string,array);
    }

    @Override
    public void setARRAY(String string, ARRAY array) throws SQLException {
        delegate.setARRAY(string, array);
        storeParameterValue(string,array);
    }

    @Override
    public void setOPAQUE(String string, OPAQUE opaque) throws SQLException {
        delegate.setOPAQUE(string, opaque);
        storeParameterValue(string,opaque);
    }

    @Override
    public void setStructDescriptor(String string, StructDescriptor sd) throws SQLException {
        delegate.setStructDescriptor(string, sd);
        storeParameterValue(string,sd);
    }

    @Override
    public void setSTRUCT(String string, STRUCT struct) throws SQLException {
        delegate.setSTRUCT(string, struct);
        storeParameterValue(string,struct);
    }

    @Override
    public void setCustomDatum(String string, CustomDatum cd) throws SQLException {
        delegate.setCustomDatum(string, cd);
        storeParameterValue(string,cd);
    }

    @Override
    public void setORAData(String string, ORAData orad) throws SQLException {
        delegate.setORAData(string, orad);
        storeParameterValue(string,orad);
    }

    @Override
    public void setObject(String string, Object o, int i, int i1) throws SQLException {
        delegate.setObject(string, o, i, i1);
        storeParameterValue(string,o);
    }

    @Override
    public void setObject(String string, Object o, int i) throws SQLException {
        delegate.setObject(string, o, i);
        storeParameterValue(string,o);
    }

    @Override
    public void setRefType(String string, REF ref) throws SQLException {
        delegate.setRefType(string, ref);
        storeParameterValue(string,ref);
    }

    @Override
    public void setRef(String string, Ref ref) throws SQLException {
        delegate.setRef(string, ref);
        storeParameterValue(string,ref);
    }

    @Override
    public void setREF(String string, REF ref) throws SQLException {
        delegate.setREF(string, ref);
        storeParameterValue(string,ref);
    }

    @Override
    public void setObject(String string, Object o) throws SQLException {
        delegate.setObject(string, o);
        storeParameterValue(string,o);
    }

    @Override
    public void setOracleObject(String string, Datum datum) throws SQLException {
        delegate.setOracleObject(string, datum);
        storeParameterValue(string,datum);
    }

    @Override
    public void registerOutParameter(int parameterIndex, int sqlType) throws SQLException {
        delegate.registerOutParameter(parameterIndex, sqlType);
    }

    @Override
    public void registerOutParameter(int parameterIndex, int sqlType, int scale) throws SQLException {
        delegate.registerOutParameter(parameterIndex, sqlType, scale);
    }

    @Override
    public boolean wasNull() throws SQLException {
        return delegate.wasNull();
    }

    @Override
    public String getString(int parameterIndex) throws SQLException {
        return delegate.getString(parameterIndex);
    }

    @Override
    public boolean getBoolean(int parameterIndex) throws SQLException {
        return delegate.getBoolean(parameterIndex);
    }

    @Override
    public byte getByte(int parameterIndex) throws SQLException {
        return delegate.getByte(parameterIndex);
    }

    @Override
    public short getShort(int parameterIndex) throws SQLException {
        return delegate.getShort(parameterIndex);
    }

    @Override
    public int getInt(int parameterIndex) throws SQLException {
        return delegate.getInt(parameterIndex);
    }

    @Override
    public long getLong(int parameterIndex) throws SQLException {
        return delegate.getLong(parameterIndex);
    }

    @Override
    public float getFloat(int parameterIndex) throws SQLException {
        return delegate.getFloat(parameterIndex);
    }

    @Override
    public double getDouble(int parameterIndex) throws SQLException {
        return delegate.getDouble(parameterIndex);
    }

    @Override
    public BigDecimal getBigDecimal(int parameterIndex, int scale) throws SQLException {
        return delegate.getBigDecimal(parameterIndex, scale);
    }

    @Override
    public byte[] getBytes(int parameterIndex) throws SQLException {
        return delegate.getBytes(parameterIndex);
    }

    @Override
    public Date getDate(int parameterIndex) throws SQLException {
        return delegate.getDate(parameterIndex);
    }

    @Override
    public Time getTime(int parameterIndex) throws SQLException {
        return delegate.getTime(parameterIndex);
    }

    @Override
    public Timestamp getTimestamp(int parameterIndex) throws SQLException {
        return delegate.getTimestamp(parameterIndex);
    }

    @Override
    public Object getObject(int parameterIndex) throws SQLException {
        return objWrapper.wrapObject(delegate.getObject(parameterIndex));
    }

    @Override
    public BigDecimal getBigDecimal(int parameterIndex) throws SQLException {
        return delegate.getBigDecimal(parameterIndex);
    }

    @Override
    public Object getObject(int parameterIndex, Map<String, Class<?>> map) throws SQLException {
        return delegate.getObject(parameterIndex, map);
    }

    @Override
    public Ref getRef(int parameterIndex) throws SQLException {
        return delegate.getRef(parameterIndex);
    }

    @Override
    public Blob getBlob(int parameterIndex) throws SQLException {
        return objWrapper.wrapBlob(delegate.getBlob(parameterIndex));
    }

    @Override
    public Clob getClob(int parameterIndex) throws SQLException {
        return objWrapper.wrapClob(delegate.getClob(parameterIndex));
    }

    @Override
    public Array getArray(int parameterIndex) throws SQLException {
        return delegate.getArray(parameterIndex);
    }

    @Override
    public Date getDate(int parameterIndex, Calendar cal) throws SQLException {
        return delegate.getDate(parameterIndex, cal);
    }

    @Override
    public Time getTime(int parameterIndex, Calendar cal) throws SQLException {
        return delegate.getTime(parameterIndex, cal);
    }

    @Override
    public Timestamp getTimestamp(int parameterIndex, Calendar cal) throws SQLException {
        return delegate.getTimestamp(parameterIndex, cal);
    }

    @Override
    public void registerOutParameter(int parameterIndex, int sqlType, String typeName) throws SQLException {
        delegate.registerOutParameter(parameterIndex, sqlType, typeName);
    }

    @Override
    public void registerOutParameter(String parameterName, int sqlType) throws SQLException {
        delegate.registerOutParameter(parameterName, sqlType);
    }

    @Override
    public void registerOutParameter(String parameterName, int sqlType, int scale) throws SQLException {
        delegate.registerOutParameter(parameterName, sqlType, scale);
    }

    @Override
    public void registerOutParameter(String parameterName, int sqlType, String typeName) throws SQLException {
        delegate.registerOutParameter(parameterName, sqlType, typeName);
    }

    @Override
    public URL getURL(int parameterIndex) throws SQLException {
        return delegate.getURL(parameterIndex);
    }

    @Override
    public String getString(String parameterName) throws SQLException {
        return delegate.getString(parameterName);
    }

    @Override
    public boolean getBoolean(String parameterName) throws SQLException {
        return delegate.getBoolean(parameterName);
    }

    @Override
    public byte getByte(String parameterName) throws SQLException {
        return delegate.getByte(parameterName);
    }

    @Override
    public short getShort(String parameterName) throws SQLException {
        return delegate.getShort(parameterName);
    }

    @Override
    public int getInt(String parameterName) throws SQLException {
        return delegate.getInt(parameterName);
    }

    @Override
    public long getLong(String parameterName) throws SQLException {
        return delegate.getLong(parameterName);
    }

    @Override
    public float getFloat(String parameterName) throws SQLException {
        return delegate.getFloat(parameterName);
    }

    @Override
    public double getDouble(String parameterName) throws SQLException {
        return delegate.getDouble(parameterName);
    }

    @Override
    public byte[] getBytes(String parameterName) throws SQLException {
        return delegate.getBytes(parameterName);
    }

    @Override
    public Date getDate(String parameterName) throws SQLException {
        return delegate.getDate(parameterName);
    }

    @Override
    public Time getTime(String parameterName) throws SQLException {
        return delegate.getTime(parameterName);
    }

    @Override
    public Timestamp getTimestamp(String parameterName) throws SQLException {
        return delegate.getTimestamp(parameterName);
    }

    @Override
    public Object getObject(String parameterName) throws SQLException {
        return objWrapper.wrapObject(delegate.getObject(parameterName));
    }

    @Override
    public BigDecimal getBigDecimal(String parameterName) throws SQLException {
        return delegate.getBigDecimal(parameterName);
    }

    @Override
    public Object getObject(String parameterName, Map<String, Class<?>> map) throws SQLException {
        return delegate.getObject(parameterName, map);
    }

    @Override
    public Ref getRef(String parameterName) throws SQLException {
        return delegate.getRef(parameterName);
    }

    @Override
    public Blob getBlob(String parameterName) throws SQLException {
        return objWrapper.wrapBlob(delegate.getBlob(parameterName));
    }

    @Override
    public Clob getClob(String parameterName) throws SQLException {
        return objWrapper.wrapClob(delegate.getClob(parameterName));
    }

    @Override
    public Array getArray(String parameterName) throws SQLException {
        return delegate.getArray(parameterName);
    }

    @Override
    public Date getDate(String parameterName, Calendar cal) throws SQLException {
        return delegate.getDate(parameterName, cal);
    }

    @Override
    public Time getTime(String parameterName, Calendar cal) throws SQLException {
        return delegate.getTime(parameterName, cal);
    }

    @Override
    public Timestamp getTimestamp(String parameterName, Calendar cal) throws SQLException {
        return delegate.getTimestamp(parameterName, cal);
    }

    @Override
    public URL getURL(String parameterName) throws SQLException {
        return delegate.getURL(parameterName);
    }

    @Override
    public RowId getRowId(int parameterIndex) throws SQLException {
        return delegate.getRowId(parameterIndex);
    }

    @Override
    public RowId getRowId(String parameterName) throws SQLException {
        return delegate.getRowId(parameterName);
    }

    @Override
    public void setRowId(String parameterName, RowId x) throws SQLException {
        delegate.setRowId(parameterName, x);
        storeParameterValue(parameterName,x);
    }

    @Override
    public void setNString(String parameterName, String value) throws SQLException {
        delegate.setNString(parameterName, value);
        storeParameterValue(parameterName,value);
    }

    @Override
    public void setNCharacterStream(String parameterName, Reader value, long length) throws SQLException {
        delegate.setNCharacterStream(parameterName, value, length);
        storeParameterValue(parameterName,value);
    }

    @Override
    public void setNClob(String parameterName, NClob value) throws SQLException {
        delegate.setNClob(parameterName, value);
        storeParameterValue(parameterName,value);
    }

    @Override
    public void setClob(String parameterName, Reader reader, long length) throws SQLException {
        delegate.setClob(parameterName, reader, length);
        storeParameterValue(parameterName,reader);
    }

    @Override
    public void setBlob(String parameterName, InputStream inputStream, long length) throws SQLException {
        delegate.setBlob(parameterName, inputStream, length);
        storeParameterValue(parameterName,inputStream);
    }

    @Override
    public void setNClob(String parameterName, Reader reader, long length) throws SQLException {
        delegate.setNClob(parameterName, reader, length);
        storeParameterValue(parameterName,reader);
    }

    @Override
    public NClob getNClob(int parameterIndex) throws SQLException {
        return delegate.getNClob(parameterIndex);
    }

    @Override
    public NClob getNClob(String parameterName) throws SQLException {
        return delegate.getNClob(parameterName);
    }

    @Override
    public void setSQLXML(String parameterName, SQLXML xmlObject) throws SQLException {
        delegate.setSQLXML(parameterName, xmlObject);
        storeParameterValue(parameterName,xmlObject);
    }

    @Override
    public SQLXML getSQLXML(int parameterIndex) throws SQLException {
        return delegate.getSQLXML(parameterIndex);
    }

    @Override
    public SQLXML getSQLXML(String parameterName) throws SQLException {
        return delegate.getSQLXML(parameterName);
    }

    @Override
    public String getNString(int parameterIndex) throws SQLException {
        return delegate.getNString(parameterIndex);
    }

    @Override
    public String getNString(String parameterName) throws SQLException {
        return delegate.getNString(parameterName);
    }

    @Override
    public Reader getNCharacterStream(int parameterIndex) throws SQLException {
        return delegate.getNCharacterStream(parameterIndex);
    }

    @Override
    public Reader getNCharacterStream(String parameterName) throws SQLException {
        return delegate.getNCharacterStream(parameterName);
    }

    @Override
    public Reader getCharacterStream(String parameterName) throws SQLException {
        return delegate.getCharacterStream(parameterName);
    }

    @Override
    public void setAsciiStream(String parameterName, InputStream x, long length) throws SQLException {
        delegate.setAsciiStream(parameterName, x, length);
        storeParameterValue(parameterName,x);
    }

    @Override
    public void setBinaryStream(String parameterName, InputStream x, long length) throws SQLException {
        delegate.setBinaryStream(parameterName, x, length);
        storeParameterValue(parameterName,x);
    }

    @Override
    public void setCharacterStream(String parameterName, Reader reader, long length) throws SQLException {
        delegate.setCharacterStream(parameterName, reader, length);
        storeParameterValue(parameterName,reader);
    }

    @Override
    public void setAsciiStream(String parameterName, InputStream x) throws SQLException {
        delegate.setAsciiStream(parameterName, x);
        storeParameterValue(parameterName,x);
    }

    @Override
    public void setBinaryStream(String parameterName, InputStream x) throws SQLException {
        delegate.setBinaryStream(parameterName, x);
        storeParameterValue(parameterName,x);
    }

    @Override
    public void setCharacterStream(String parameterName, Reader reader) throws SQLException {
        delegate.setCharacterStream(parameterName, reader);
        storeParameterValue(parameterName,reader);
    }

    @Override
    public void setNCharacterStream(String parameterName, Reader value) throws SQLException {
        delegate.setNCharacterStream(parameterName, value);
        storeParameterValue(parameterName,value);
    }

    @Override
    public void setClob(String parameterName, Reader reader) throws SQLException {
        delegate.setClob(parameterName, reader);
        storeParameterValue(parameterName,reader);
    }

    @Override
    public void setBlob(String parameterName, InputStream inputStream) throws SQLException {
        delegate.setBlob(parameterName, inputStream);
        storeParameterValue(parameterName,inputStream);
    }

    @Override
    public void setNClob(String parameterName, Reader reader) throws SQLException {
        delegate.setNClob(parameterName, reader);
        storeParameterValue(parameterName,reader);
    }

    @Override
    public <T> T getObject(int parameterIndex, Class<T> type) throws SQLException {
        return objWrapper.wrapObject(delegate.getObject(parameterIndex, type), type);
    }

    @Override
    public <T> T getObject(String parameterName, Class<T> type) throws SQLException {
        return objWrapper.wrapObject(delegate.getObject(parameterName, type), type);
    }
}
