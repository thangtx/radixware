/*
 * Copyright (c) 2008-2017, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.kernel.server.jdbcdrivers.oracle;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.math.BigDecimal;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import oracle.jdbc.LargeObjectAccessMode;
import oracle.jdbc.OracleConnection;
import oracle.sql.CLOB;
import oracle.sql.ClobDBAccess;
import org.radixware.kernel.server.jdbc.DBOperationLoggerInterface;
import org.radixware.kernel.server.jdbc.wrappers.RadixJdbcWrapper;
import org.radixware.kernel.server.jdbc.wrappers.IRadixJdbcWrapper;

public class RadixCLOB4Oracle extends CLOB {
//    private static final List<Class> CLASSES_TO_COPY = ReflectionUtils.getHierarchy(CLOB.class);
//    private static final List<Field> FIELDS_TO_COPY = ReflectionUtils.getFields(CLASSES_TO_COPY);

    final CLOB delegate;
    final DBOperationLoggerInterface logger;
    final IRadixJdbcWrapper wrapper;

    public RadixCLOB4Oracle(CLOB delegate, DBOperationLoggerInterface logger) {
        this.delegate = delegate;
        this.logger = logger;
        this.wrapper = new RadixJdbcWrapper(logger, "oracle.sql.CLOB");
//        ReflectionUtils.copyFields(FIELDS_TO_COPY, delegate, this);
    }

    private void beforeDbOper() {
        logger.beforeDbOperation("oracle.sql.CLOB");
    }

    private void afterDbOper() {
        logger.afterDbOperation();
    }

    @Override
    public long length() throws SQLException {
        return delegate.length();
    }
    
    @Override
    public String getSubString(long pos, int length) throws SQLException {
        beforeDbOper();
        try {
            return delegate.getSubString(pos, length);
        } finally {
            afterDbOper();
        }
    }

    @Override
    public Reader getCharacterStream() throws SQLException {
        beforeDbOper();
        try {
            return wrapper.wrapReader(delegate.getCharacterStream());
        } finally {
            afterDbOper();
        }
    }

    @Override
    public InputStream getAsciiStream() throws SQLException {
        beforeDbOper();
        try {
            return wrapper.wrapStream(delegate.getAsciiStream());
        } finally {
            afterDbOper();
        }
    }

    @Override
    public long position(String searchstr, long start) throws SQLException {
        beforeDbOper();
        try {
            return delegate.position(searchstr, start);
        } finally {
            afterDbOper();
        }
    }

    @Override
    public long position(Clob searchstr, long start) throws SQLException {
        beforeDbOper();
        try {
            return delegate.position(searchstr, start);
        } finally {
            afterDbOper();
        }
    }

    @Override
    public int setString(long pos, String str) throws SQLException {
        beforeDbOper();
        try {
            return delegate.setString(pos, str);
        } finally {
            afterDbOper();
        }
    }

    @Override
    public int setString(long pos, String str, int offset, int len) throws SQLException {
        beforeDbOper();
        try {
            return delegate.setString(pos, str, offset, len);
        } finally {
            afterDbOper();
        }
    }

    @Override
    public OutputStream setAsciiStream(long pos) throws SQLException {
        beforeDbOper();
        try {
            return wrapper.wrapStream(delegate.setAsciiStream(pos));
        } finally {
            afterDbOper();
        }
    }

    @Override
    public Writer setCharacterStream(long pos) throws SQLException {
        beforeDbOper();
        try {
            return wrapper.wrapWriter(delegate.setCharacterStream(pos));
        } finally {
            afterDbOper();
        }
    }

    @Override
    public void truncate(long len) throws SQLException {
        beforeDbOper();
        try {
            delegate.truncate(len);
        } finally {
            afterDbOper();
        }
    }

    @Override
    public void free() throws SQLException {
        beforeDbOper();
        try {
            delegate.free();
        } finally {
            afterDbOper();
        }
    }

    @Override
    public Reader getCharacterStream(long pos, long length) throws SQLException {
        beforeDbOper();
        try {
            return wrapper.wrapReader(delegate.getCharacterStream(pos, length));
        } finally {
            afterDbOper();
        }
    }

//     all other methods - delegate to final CLOB delegate (default generated methods)
    @Override
    public boolean isNCLOB() {
        return delegate.isNCLOB();
    }

    @Override
    public int getChars(long l, int i, char[] chars) throws SQLException {
        return delegate.getChars(l, i, chars);
    }

    @Override
    public Writer getCharacterOutputStream() throws SQLException {
        return delegate.getCharacterOutputStream();
    }

    @Override
    public OutputStream getAsciiOutputStream() throws SQLException {
        return delegate.getAsciiOutputStream();
    }

    @Override
    public byte[] getLocator() {
        return delegate.getLocator();
    }

    @Override
    public void setLocator(byte[] bytes) {
        delegate.setLocator(bytes);
    }

    @Override
    public int putChars(long l, char[] chars) throws SQLException {
        return delegate.putChars(l, chars);
    }

    @Override
    public int putChars(long l, char[] chars, int i) throws SQLException {
        return delegate.putChars(l, chars, i);
    }

    @Override
    public int putChars(long l, char[] chars, int i, int i1) throws SQLException {
        return delegate.putChars(l, chars, i, i1);
    }

    @Override
    public int putString(long l, String string) throws SQLException {
        return delegate.putString(l, string);
    }

    @Override
    public int getChunkSize() throws SQLException {
        return delegate.getChunkSize();
    }

    @Override
    public int getBufferSize() throws SQLException {
        return delegate.getBufferSize();
    }

    @Override
    public boolean isEmptyLob() throws SQLException {
        return delegate.isEmptyLob();
    }

    @Override
    public boolean isSecureFile() throws SQLException {
        return delegate.isSecureFile();
    }

    @Override
    public OutputStream getAsciiOutputStream(long l) throws SQLException {
        return delegate.getAsciiOutputStream(l);
    }

    @Override
    public Writer getCharacterOutputStream(long l) throws SQLException {
        return delegate.getCharacterOutputStream(l);
    }

    @Override
    public InputStream getAsciiStream(long l) throws SQLException {
        return delegate.getAsciiStream(l);
    }

    @Override
    public Reader getCharacterStream(long l) throws SQLException {
        return delegate.getCharacterStream(l);
    }

    @Override
    public void trim(long l) throws SQLException {
        delegate.trim(l);
    }

    @Override
    public void freeTemporary() throws SQLException {
        delegate.freeTemporary();
    }

    @Override
    public boolean isTemporary() throws SQLException {
        return delegate.isTemporary();
    }

    @Override
    public void open(LargeObjectAccessMode loam) throws SQLException {
        delegate.open(loam);
    }

    @Override
    public void open(int i) throws SQLException {
        delegate.open(i);
    }

    @Override
    public void close() throws SQLException {
        delegate.close();
    }

    @Override
    public boolean isOpen() throws SQLException {
        return delegate.isOpen();
    }

    @Override
    public Object toJdbc() throws SQLException {
        return delegate.toJdbc();
    }

    @Override
    public boolean isConvertibleTo(Class type) {
        return delegate.isConvertibleTo(type);
    }

    @Override
    public Reader characterStreamValue() throws SQLException {
        return delegate.characterStreamValue();
    }

    @Override
    public InputStream asciiStreamValue() throws SQLException {
        return delegate.asciiStreamValue();
    }

    @Override
    public InputStream binaryStreamValue() throws SQLException {
        return delegate.binaryStreamValue();
    }

    @Override
    public String stringValue() throws SQLException {
        return delegate.stringValue();
    }

    @Override
    public Object makeJdbcArray(int i) {
        return delegate.makeJdbcArray(i);
    }

    @Override
    public ClobDBAccess getDBAccess() throws SQLException {
        return delegate.getDBAccess();
    }

    @Override
    public Connection getJavaSqlConnection() throws SQLException {
        return delegate.getJavaSqlConnection();
    }

    @Override
    public void setPhysicalConnectionOf(Connection cnctn) {
        delegate.setPhysicalConnectionOf(cnctn);
    }

    @Override
    public OracleConnection getOracleConnection() throws SQLException {
        return delegate.getOracleConnection();
    }

    @Override
    public oracle.jdbc.internal.OracleConnection getInternalConnection() throws SQLException {
        return delegate.getInternalConnection();
    }

    @Override
    public oracle.jdbc.driver.OracleConnection getConnection() throws SQLException {
        return delegate.getConnection();
    }

    @Override
    public boolean equals(Object o) {
        return delegate.equals(o);
    }

    @Override
    public byte[] shareBytes() {
        return delegate.shareBytes();
    }

    @Override
    public long getLength() {
        return delegate.getLength();
    }

    @Override
    public void setBytes(byte[] bytes) {
        delegate.setBytes(bytes);
    }

    @Override
    public void setShareBytes(byte[] bytes) {
        delegate.setShareBytes(bytes);
    }

    @Override
    public byte[] getBytes() {
        return delegate.getBytes();
    }

    @Override
    public InputStream getStream() {
        return delegate.getStream();
    }

    @Override
    public String stringValue(Connection cnctn) throws SQLException {
        return delegate.stringValue(cnctn);
    }

    @Override
    public boolean booleanValue() throws SQLException {
        return delegate.booleanValue();
    }

    @Override
    public int intValue() throws SQLException {
        return delegate.intValue();
    }

    @Override
    public long longValue() throws SQLException {
        return delegate.longValue();
    }

    @Override
    public float floatValue() throws SQLException {
        return delegate.floatValue();
    }

    @Override
    public double doubleValue() throws SQLException {
        return delegate.doubleValue();
    }

    @Override
    public byte byteValue() throws SQLException {
        return delegate.byteValue();
    }

    @Override
    public BigDecimal bigDecimalValue() throws SQLException {
        return delegate.bigDecimalValue();
    }

    @Override
    public Date dateValue() throws SQLException {
        return delegate.dateValue();
    }

    @Override
    public Time timeValue() throws SQLException {
        return delegate.timeValue();
    }

    @Override
    public Time timeValue(Calendar clndr) throws SQLException {
        return delegate.timeValue(clndr);
    }

    @Override
    public Timestamp timestampValue() throws SQLException {
        return delegate.timestampValue();
    }

    @Override
    public Timestamp timestampValue(Calendar clndr) throws SQLException {
        return delegate.timestampValue(clndr);
    }
}
