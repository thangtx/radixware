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

package org.radixware.kernel.server.types;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Timestamp;
import java.util.HashMap;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.enums.EAutoUpdateReason;
import org.radixware.kernel.common.enums.EComparisonOperator;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.exceptions.WrongFormatError;
import org.radixware.kernel.common.repository.DbOptionValue;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.sqml.translate.SqmlPreprocessor;
import org.radixware.kernel.common.types.Bin;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.server.dbq.sqml.ServerPreprocessorConfig;
import org.radixware.kernel.server.exceptions.DatabaseError;
import org.radixware.kernel.server.meta.clazzes.IRadPropReadAccessor;
import org.radixware.kernel.server.meta.clazzes.IRadPropWriteAccessor;
import org.radixware.kernel.server.meta.clazzes.RadPropDef;
import org.radixware.kernel.server.meta.clazzes.RadCursorRefFieldPropDef;
import org.radixware.kernel.server.utils.SrvValAsStr;

public abstract class Cursor extends SqlClass implements java.lang.AutoCloseable{

    private java.sql.ResultSet resultSet;
    //private oracle.jdbc.OracleResultSet resultSet;
    private java.sql.PreparedStatement statement;

    protected Cursor(final Arte arte) { // for unit tests
        super(arte);
    }

    protected Cursor() {
        this(null);
    }

    // for publication of Cursor and using without PreparedStatementCache
    public void open(final java.sql.ResultSet resultSet) {
        open(null, resultSet);
    }

    protected void open(final java.sql.PreparedStatement statement, final java.sql.ResultSet resultSet) {
        if (!isClosed()) {
            close();
        }
        this.statement = statement;
        this.resultSet = resultSet;
        getArte().registerCursor(this);
    }

    @Override
    protected final void executeQuery(PreparedStatement statement) throws SQLException {
        final Arte arte = getArte();
        java.sql.ResultSet rs = null;
        String sec = org.radixware.kernel.common.enums.ETimingSection.RDX_SQLCLASS_QRYEXEC.getValue() + ":" + getRadMeta().getName();
        arte.getProfiler().enterTimingSection(sec);
        try {
            rs = statement.executeQuery();
        } finally {
            arte.getProfiler().leaveTimingSection(sec);
        }
        open(statement, rs);
    }

    public boolean isClosed() {
        return resultSet == null;
    }
    
    private void ensureOpened() {
        if (isClosed()) {
            throw new IllegalStateException("Cursor is closed");
        }
    }

    protected static boolean checkPreprocessorCondition(
            final String dbType,
            final boolean checkVersion,
            final EComparisonOperator op,
            final String version,
            final boolean checkOptions,
            final List<DbOptionValue> options,
            final String layerUri,
            final Arte arte) {
        return SqmlPreprocessor.checkCondition(dbType, checkVersion, op, version, checkOptions, options, new ServerPreprocessorConfig(arte == null ? null : arte.getDbConfiguration()));
    }

    public boolean isBeforeFirst() {
        ensureOpened();
        try {
            return resultSet.isBeforeFirst();
        } catch (SQLException e) {
            throw new DatabaseError(e);
        }
    }

    public boolean isAfterLast() {
        ensureOpened();
        try {
            return resultSet.isAfterLast();
        } catch (SQLException e) {
            throw new DatabaseError(e);
        }
    }

    public boolean isFirst() {
        ensureOpened();
        try {
            return resultSet.isFirst();
        } catch (SQLException e) {
            throw new DatabaseError(e);
        }
    }

    public boolean isLast() {
        ensureOpened();
        try {
            return resultSet.isLast();
        } catch (SQLException e) {
            throw new DatabaseError(e);
        }
    }

    public void beforeFirst() {
        ensureOpened();
        try {
            resultSet.beforeFirst();
        } catch (SQLException e) {
            throw new DatabaseError(e);
        }
    }

    public void afterLast() {
        ensureOpened();
        try {
            resultSet.afterLast();
        } catch (SQLException e) {
            throw new DatabaseError(e);
        }
    }

    public boolean first() {
        ensureOpened();
        try {
            return resultSet.first();
        } catch (SQLException e) {
            throw new DatabaseError(e);
        }
    }

    public boolean next() {
        ensureOpened();
        try {
            return resultSet.next();
        } catch (SQLException e) {
            throw new DatabaseError(e);
        }
    }

    public boolean previous() {
        ensureOpened();
        try {
            return resultSet.previous();
        } catch (SQLException e) {
            throw new DatabaseError(e);
        }
    }

    public boolean relative(final int x) {
        ensureOpened();
        try {
            return resultSet.relative(x);
        } catch (SQLException e) {
            throw new DatabaseError(e);
        }
    }

    public boolean absolute(final int x) {
        ensureOpened();
        try {
            return resultSet.absolute(x);
        } catch (SQLException e) {
            throw new DatabaseError(e);
        }
    }

    public boolean last() {
        ensureOpened();
        try {
            return resultSet.last();
        } catch (SQLException e) {
            throw new DatabaseError(e);
        }
    }

    public int getFetchSize() {
        ensureOpened();
        try {
            return resultSet.getFetchSize();
        } catch (SQLException e) {
            throw new DatabaseError(e);
        }
    }

    public void setFetchSize(final int x) {
        ensureOpened();
        try {
            resultSet.setFetchSize(x);
        } catch (SQLException e) {
            throw new DatabaseError(e);
        }
    }

    public void setFetchDirection(final int x) {
        ensureOpened();
        try {
            resultSet.setFetchDirection(x);
        } catch (SQLException e) {
            throw new DatabaseError(e);
        }
    }

    public int getFetchDirection() {
        ensureOpened();
        try {
            return resultSet.getFetchDirection();
        } catch (SQLException e) {
            throw new DatabaseError(e);
        }
    }

    public void moveToInsertRow() {
        ensureOpened();
        try {
            resultSet.moveToInsertRow();
        } catch (SQLException e) {
            throw new DatabaseError(e);
        }
    }

    public void moveToCurrentRow() {
        ensureOpened();
        try {
            resultSet.moveToCurrentRow();
        } catch (SQLException e) {
            throw new DatabaseError(e);
        }
    }

    public void updateRow() {
        ensureOpened();
        try {
            resultSet.updateRow();
        } catch (SQLException e) {
            throw new DatabaseError(e);
        }
    }

    public void insertRow() {
        ensureOpened();
        try {
            resultSet.insertRow();
        } catch (SQLException e) {
            throw new DatabaseError(e);
        }
    }

    public void deleteRow() {
        ensureOpened();
        try {
            resultSet.deleteRow();
        } catch (SQLException e) {
            throw new DatabaseError(e);
        }
    }

    public void cancelRowUpdates() {
        ensureOpened();
        try {
            resultSet.cancelRowUpdates();
        } catch (SQLException e) {
            throw new DatabaseError(e);
        }
    }

    public void refreshRow() {
        ensureOpened();
        try {
            resultSet.refreshRow();
        } catch (SQLException e) {
            throw new DatabaseError(e);
        }
    }

    public boolean rowDeleted() {
        ensureOpened();
        try {
            return resultSet.rowDeleted();
        } catch (SQLException e) {
            throw new DatabaseError(e);
        }
    }

    public boolean rowInserted() {
        ensureOpened();
        try {
            return resultSet.rowInserted();
        } catch (SQLException e) {
            throw new DatabaseError(e);
        }
    }

    public boolean rowUpdated() {
        ensureOpened();
        try {
            return resultSet.rowUpdated();
        } catch (SQLException e) {
            throw new DatabaseError(e);
        }
    }

    public void clearWarnings() {
        ensureOpened();
        try {
            resultSet.clearWarnings();
        } catch (SQLException e) {
            throw new DatabaseError(e);
        }
    }

    public java.sql.SQLWarning getWarnings() {
        ensureOpened();
        try {
            return resultSet.getWarnings();
        } catch (SQLException e) {
            throw new DatabaseError(e);
        }
    }

    public java.sql.ResultSet getResultSet() {
        return resultSet;
    }

    // overrided in generated classes, return static instance.
    protected PreparedStatementsCache getPreparedStatementsCache() {
        return null; // for publication of Cursor and using without PreparedStatementCache
    }

    public void close() {
        if (isClosed()) {
            return;
        }

        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                getArte().getTrace().put(EEventSeverity.ERROR, "Can't close cursor resultSet: " + getArte().getTrace().exceptionStackToString(e), EEventSource.ARTE_DB);
            }
            resultSet = null;
        }

        if (statement != null) {
            getPreparedStatementsCache().close(getArte(), statement); // usually not close, only marked as free
            statement = null;
        }

        getArte().unregisterCursor(this);
    }

    // Ref fields
    protected Entity getFieldEntity(final Id id) {
        final RadPropDef prop = getRadMeta().getPropById(id);
        if (!(prop instanceof RadCursorRefFieldPropDef)) {
            throw new DefinitionError("Field with #" + id + " is not reference");
        }
        final HashMap<Id, Object> key = new HashMap<Id, Object>(7);
        final RadCursorRefFieldPropDef refField = (RadCursorRefFieldPropDef) prop;
        for (RadCursorRefFieldPropDef.RefMapItem item : refField.getRefMap()) {
            final Object keyVal = getField(item.getCursorPropId());
            if (keyVal == null) {
                return null;
            }
            key.put(item.getPropId(), keyVal);
        }
        List<RadCursorRefFieldPropDef.RefMapItem> otherProps = refField.getOtherPropMap();
        if (otherProps.isEmpty()) {
            return getArte().getEntityObject(new Pid(getArte(), refField.getDestEntityId(), key));
        } else {
            EntityPropVals propVals = new EntityPropVals();
            for (RadCursorRefFieldPropDef.RefMapItem item : otherProps) {
                final Object propVal = getField(item.getCursorPropId());
                propVals.putPropValById(item.getPropId(), propVal);
            }
            return getArte().getEntityObject(new Pid(getArte(), refField.getDestEntityId(), key), propVals, false);
        }
    }

    protected void setFieldEntity(final Id id, final Entity x) {
        final RadPropDef prop = getRadMeta().getPropById(id);
        if (!(prop instanceof RadCursorRefFieldPropDef)) {
            throw new DefinitionError("Field with ID=\"" + id + "\" is not reference");
        }
        final RadCursorRefFieldPropDef refField = (RadCursorRefFieldPropDef) prop;
        for (RadCursorRefFieldPropDef.RefMapItem item : refField.getRefMap()) {
            setField(item.getCursorPropId(), x.getProp(item.getPropId()));
        }
    }

    private final Object getField(final Id id) {
        if (getRadMeta() != null) {
            final RadPropDef prop = getRadMeta().getPropById(id);
            if (prop.getAccessor() instanceof IRadPropReadAccessor) {
                return ((IRadPropReadAccessor) prop.getAccessor()).get(this, id);
            } else {
                throw new IllegalUsageError("Field #" + id + " has no read accessor");
            }
        }
        throw new DefinitionNotFoundError(id);
    }

    private final void setField(final Id id, final Object x) {
        if (getRadMeta() != null) {
            final RadPropDef prop = getRadMeta().getPropById(id);
            if (prop.getAccessor() instanceof IRadPropWriteAccessor) {
                ((IRadPropWriteAccessor) prop.getAccessor()).set(this, id, x);
            } else {
                throw new IllegalUsageError("Field #" + id + " has no write accessor");
            }
        }
        throw new DefinitionNotFoundError(id);
    }

    public boolean autoUpdate(@SuppressWarnings("unused") final EAutoUpdateReason r) {
        boolean res = false;
        if (rowUpdated()) {
            updateRow();
            res = true;
        }
        if (rowInserted()) {
            insertRow();
            res = true;
        }
        return res;
    }
    
    protected Object getArrObject(final int idx, final EValType valType){
        ensureOpened();
        if (!valType.isArrayType()){
            return null;
        }
        try {
            int type = resultSet.getMetaData().getColumnType(idx);
        
            if (type == 2005){
                Clob clob = getClob(idx);
                if (clob == null){
                    return null;
                }

                try {
                    if (clob.length() > Integer.MAX_VALUE) {
                        throw new WrongFormatError("Can't convert Clob value to string: Clob data size is too big", null);
                    }

                    final int len = (int) clob.length(); 
                    return SrvValAsStr.fromStr(getArte(),clob.getSubString(1, len), valType);
                } catch (SQLException e) {
                    throw new DatabaseError("Can't convert Clob value to string: " + ExceptionTextFormatter.getExceptionMess(e), e);
                }
            } else {
                String value = getString(idx);
                return SrvValAsStr.fromStr(value, valType);
            }
        } catch (SQLException e) {
            throw new DatabaseError(e);
        }
    }
    
    protected void updateArrObject(final int idx, final Object arr, final EValType valType){
        ensureOpened();
        if (!valType.isArrayType()){
            return;
        }
        
        String value  = SrvValAsStr.toStr(arr, valType);
        try {
            int type = resultSet.getMetaData().getColumnType(idx);
            if (type == 2005){
                Clob clob = getArte().getDbConnection().createTemporaryClob();
                clob.setString(1, value);
                updateClob(idx, clob);
            } else {
                updateString(idx, value);
            }
        } catch (SQLException e) {
            throw new DatabaseError(e);
        }
        
    }

//setters for untypified cursor	

    protected void updateString(final int idx, final String val) {
        ensureOpened();
        try {
            resultSet.updateString(idx, val);
        } catch (SQLException e) {
            throw new DatabaseError(e);
        }
    }

    protected void updateLong(final int idx, final Long val) {
        ensureOpened();
        try {
            if (val == null) {
                resultSet.updateNull(idx);
            } else {
                resultSet.updateLong(idx, val.longValue());
            }
        } catch (SQLException e) {
            throw new DatabaseError(e);
        }
    }

    protected void updateBigDecimal(final int idx, final BigDecimal val) {
        ensureOpened();
        try {
            resultSet.updateBigDecimal(idx, val);
        } catch (SQLException e) {
            throw new DatabaseError(e);
        }
    }

    protected void updateTimestamp(final int idx, final Timestamp val) {
        ensureOpened();
        try {
            resultSet.updateTimestamp(idx, val);
        } catch (SQLException e) {
            throw new DatabaseError(e);
        }
    }

    protected void updateBoolean(final int idx, final boolean val) {
        ensureOpened();
        try {
            resultSet.updateBoolean(idx, val);
        } catch (SQLException e) {
            throw new DatabaseError(e);
        }
    }

    protected void updateBoolean(final int idx, final Boolean val) {
        ensureOpened();
        try {
            if (val == null) {
                resultSet.updateNull(idx);
            } else {
                resultSet.updateBoolean(idx, val.booleanValue());
            }
        } catch (SQLException e) {
            throw new DatabaseError(e);
        }
    }

    protected void updateBin(final int idx, final Bin val) {
        ensureOpened();
        try {
            if (val == null) {
                resultSet.updateNull(idx);
            } else {
                resultSet.updateBytes(idx, val.get());
            }
        } catch (SQLException e) {
            throw new DatabaseError(e);
        }
    }

    protected void updateChar(final int idx, final Character val) {
        ensureOpened();
        try {
            if (val == null) {
                resultSet.updateNull(idx);
            } else {
                resultSet.updateString(idx, val.toString());
            }
        } catch (SQLException e) {
            throw new DatabaseError(e);
        }
    }

    protected void updateClob(final int idx, final Clob val) {
        ensureOpened();
        try {
            resultSet.updateClob(idx, val);
        } catch (SQLException e) {
            throw new DatabaseError(e);
        }
    }

    protected void updateBlob(final int idx, final Blob val) {
        ensureOpened();
        try {
            resultSet.updateBlob(idx, val);
        } catch (SQLException e) {
            throw new DatabaseError(e);
        }
    }

    protected void updateObject(final int idx, final Object val) {
        ensureOpened();
        try {
            resultSet.updateObject(idx, val);
        } catch (SQLException e) {
            throw new DatabaseError(e);
        }
    }

    protected void updateSqlXml(final int idx, final SQLXML val) {
        ensureOpened();
        try {
            resultSet.updateSQLXML(idx, val);
        } catch (SQLException e) {
            throw new DatabaseError(e);
        }
    }

    protected void updateString(final String idx, final String val) {
        ensureOpened();
        try {
            resultSet.updateString(idx, val);
        } catch (SQLException e) {
            throw new DatabaseError(e);
        }
    }

    protected void updateLong(final String idx, final Long val) {
        ensureOpened();
        try {
            if (val == null) {
                resultSet.updateNull(idx);
            } else {
                resultSet.updateLong(idx, val.longValue());
            }
        } catch (SQLException e) {
            throw new DatabaseError(e);
        }
    }

    protected void updateBigDecimal(final String idx, final BigDecimal val) {
        ensureOpened();
        try {
            resultSet.updateBigDecimal(idx, val);
        } catch (SQLException e) {
            throw new DatabaseError(e);
        }
    }

    protected void updateTimestamp(final String idx, final Timestamp val) {
        ensureOpened();
        try {
            resultSet.updateTimestamp(idx, val);
        } catch (SQLException e) {
            throw new DatabaseError(e);
        }
    }

    protected void updateBoolean(final String idx, final boolean val) {
        ensureOpened();
        try {
            resultSet.updateBoolean(idx, val);
        } catch (SQLException e) {
            throw new DatabaseError(e);
        }
    }

    protected void updateBoolean(final String idx, final Boolean val) {
        ensureOpened();
        try {
            if (val == null) {
                resultSet.updateNull(idx);
            } else {
                resultSet.updateBoolean(idx, val.booleanValue());
            }
        } catch (SQLException e) {
            throw new DatabaseError(e);
        }
    }

    protected void updateBin(final String idx, final Bin val) {
        ensureOpened();
        try {
            if (val == null) {
                resultSet.updateNull(idx);
            } else {
                resultSet.updateBytes(idx, val.get());
            }
        } catch (SQLException e) {
            throw new DatabaseError(e);
        }
    }

    protected void updateChar(final String idx, final Character val) {
        ensureOpened();
        try {
            if (val == null) {
                resultSet.updateNull(idx);
            } else {
                resultSet.updateString(idx, val.toString());
            }
        } catch (SQLException e) {
            throw new DatabaseError(e);
        }
    }

    protected void updateClob(final String idx, final Clob val) {
        ensureOpened();
        try {
            resultSet.updateClob(idx, val);
        } catch (SQLException e) {
            throw new DatabaseError(e);
        }
    }

    protected void updateBlob(final String idx, final Blob val) {
        ensureOpened();
        try {
            resultSet.updateBlob(idx, val);
        } catch (SQLException e) {
            throw new DatabaseError(e);
        }
    }

    protected void updateObject(final String idx, final Object val) {
        ensureOpened();
        try {
            resultSet.updateObject(idx, val);
        } catch (SQLException e) {
            throw new DatabaseError(e);
        }
    }

    protected void updateSqlXml(final String idx, final SQLXML val) {
        ensureOpened();
        try {
            resultSet.updateSQLXML(idx, val);
        } catch (SQLException e) {
            throw new DatabaseError(e);
        }
    }

    //getters for untypified cursor
    protected String getString(final int idx) {
        ensureOpened();
        try {
            return resultSet.getString(idx);
        } catch (SQLException e) {
            throw new DatabaseError(e);
        }
    }

    protected Long getLong(final int idx) {
        ensureOpened();
        try {
            final long res = resultSet.getLong(idx);
            if (resultSet.wasNull()) {
                return null;
            }
            return Long.valueOf(res);
        } catch (SQLException e) {
            throw new DatabaseError(e);
        }
    }

    protected BigDecimal getBigDecimal(final int idx) {
        ensureOpened();
        try {
            return resultSet.getBigDecimal(idx);
        } catch (SQLException e) {
            throw new DatabaseError(e);
        }
    }

    protected Timestamp getTimestamp(final int idx) {
        ensureOpened();
        try {
            return resultSet.getTimestamp(idx);
        } catch (SQLException e) {
            throw new DatabaseError(e);
        }
    }

    protected Boolean getBoolean(final int idx) {
        ensureOpened();
        try {
            final boolean res = resultSet.getBoolean(idx);
            if (resultSet.wasNull()) {
                return null;
            }
            return Boolean.valueOf(res);
        } catch (SQLException e) {
            throw new DatabaseError(e);
        }
    }

    protected Bin getBin(final int idx) {
        ensureOpened();
        try {
            final byte[] res = resultSet.getBytes(idx);
            if (res == null) {
                return null;
            }
            return new Bin(res);
        } catch (SQLException e) {
            throw new DatabaseError(e);
        }
    }

    protected Character getChar(final int idx) {
        ensureOpened();
        try {
            final String res = resultSet.getString(idx);
            if (res == null || res.length() == 0) {
                return null;
            } else {
                return Character.valueOf(res.charAt(0));
            }
        } catch (SQLException e) {
            throw new DatabaseError(e);
        }
    }

    protected Clob getClob(final int idx) {
        ensureOpened();
        try {
            return resultSet.getClob(idx);
        } catch (SQLException e) {
            throw new DatabaseError(e);
        }
    }

    protected Blob getBlob(final int idx) {
        ensureOpened();
        try {
            return resultSet.getBlob(idx);
        } catch (SQLException e) {
            throw new DatabaseError(e);
        }
    }
    
    protected Object getObject(final int idx) {
        ensureOpened();
        try {
            return resultSet.getObject(idx);
        } catch (SQLException e) {
            throw new DatabaseError(e);
        }
    }

    protected SQLXML getSqlXml(final int idx) {
        ensureOpened();
        try {
            return resultSet.getSQLXML(idx);
        } catch (SQLException e) {
            throw new DatabaseError(e);
        }
    }

    protected String getString(final String idx) {
        ensureOpened();
        try {
            return resultSet.getString(idx);
        } catch (SQLException e) {
            throw new DatabaseError(e);
        }
    }

    protected Long getLong(final String idx) {
        ensureOpened();
        try {
            final long res = resultSet.getLong(idx);
            if (resultSet.wasNull()) {
                return null;
            }
            return Long.valueOf(res);
        } catch (SQLException e) {
            throw new DatabaseError(e);
        }
    }

    protected BigDecimal getBigDecimal(final String idx) {
        ensureOpened();
        try {
            return resultSet.getBigDecimal(idx);
        } catch (SQLException e) {
            throw new DatabaseError(e);
        }
    }

    protected Timestamp getTimestamp(final String idx) {
        ensureOpened();
        try {
            return resultSet.getTimestamp(idx);
        } catch (SQLException e) {
            throw new DatabaseError(e);
        }
    }

    protected Boolean getBoolean(final String idx) {
        ensureOpened();
        try {
            final boolean res = resultSet.getBoolean(idx);
            if (resultSet.wasNull()) {
                return null;
            }
            return Boolean.valueOf(res);
        } catch (SQLException e) {
            throw new DatabaseError(e);
        }
    }

    protected Bin getBin(final String idx) {
        ensureOpened();
        try {
            final byte[] res = resultSet.getBytes(idx);
            if (res == null) {
                return null;
            }
            return new Bin(res);
        } catch (SQLException e) {
            throw new DatabaseError(e);
        }
    }

    protected Character getChar(final String idx) {
        ensureOpened();
        try {
            final String res = resultSet.getString(idx);
            if (res == null || res.length() == 0) {
                return null;
            } else {
                return Character.valueOf(res.charAt(0));
            }
        } catch (SQLException e) {
            throw new DatabaseError(e);
        }
    }

    protected Clob getClob(final String idx) {
        ensureOpened();
        try {
            return resultSet.getClob(idx);
        } catch (SQLException e) {
            throw new DatabaseError(e);
        }
    }

    protected Blob getBlob(final String idx) {
        ensureOpened();
        try {
            return resultSet.getBlob(idx);
        } catch (SQLException e) {
            throw new DatabaseError(e);
        }
    }

    protected Object getObject(final String idx) {
        ensureOpened();
        try {
            return resultSet.getObject(idx);
        } catch (SQLException e) {
            throw new DatabaseError(e);
        }
    }

    protected SQLXML getSqlXml(final String idx) {
        ensureOpened();
        try {
            return resultSet.getSQLXML(idx);
        } catch (SQLException e) {
            throw new DatabaseError(e);
        }
    }
}
