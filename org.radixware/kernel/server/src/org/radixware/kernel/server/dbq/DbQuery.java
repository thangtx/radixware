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
package org.radixware.kernel.server.dbq;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsIndexDef;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.enums.OracleTypeNames;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.types.*;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.common.utils.SystemPropUtils;
import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.server.arte.services.eas.EasValueConverter;
import org.radixware.kernel.server.exceptions.DatabaseError;
import org.radixware.kernel.server.exceptions.DbQueryBuilderError;
import org.radixware.kernel.server.exceptions.InvalidPropertyValueError;
import org.radixware.kernel.server.exceptions.UniqueConstraintViolation;
import org.radixware.kernel.server.instance.Instance;
import org.radixware.kernel.server.instance.InstanceState;
import org.radixware.kernel.server.jdbc.RadixConnection;
import org.radixware.kernel.server.jdbc.RadixPreparedStatement;
import org.radixware.kernel.server.meta.clazzes.RadClassDef;
import org.radixware.kernel.server.meta.clazzes.RadPropDef;
import org.radixware.kernel.server.types.Entity;
import org.radixware.kernel.server.types.Pid;
import org.radixware.kernel.server.utils.SrvValAsStr;

public abstract class DbQuery {

    public static final boolean LOG_CLOSE_FROM_NON_ARTE_THREAD = SystemPropUtils.getBooleanSystemProp("rdx.log.close.dbquery.outside.arte", false);

    protected final DdsTableDef table;
    final List<Field> fields;
    protected volatile PreparedStatement query; // volatile because it is read in finalize
    final List<Param> params;
    protected final Arte arte;
    private volatile boolean isBusy = true; // volatile because it is read in finalize
    private volatile boolean deletedFromCache = false; // volatile because it is read in finalize
    protected final String sqlQuery;
    private final boolean wantCloseStatementOnFree;
    private final List<Clob> temporaryClobs = new ArrayList<>();
    private final List<Blob> temporaryBlobs = new ArrayList<>();
    private final ISetParamCallbackHandler setParamCallbackHandler;

    DbQuery(
            final Arte arte,
            final DdsTableDef table,
            final List<Field> fields,
            final List<Param> params,
            final String sqlQuery) {
        this.arte = arte;
        this.table = table;
        this.fields = fields;
        this.params = params;
        isBusy = true;
        this.sqlQuery = sqlQuery;
        final Instance inst = Instance.get();
        if (inst != null) {
            wantCloseStatementOnFree = inst.isOraImplicitCacheEnabled();
        } else {
            wantCloseStatementOnFree = false;
        }
        setParamCallbackHandler = new ISetParamCallbackHandler() {

            @Override
            public void afterCreateTemporaryClob(Clob clob) {
                temporaryClobs.add(clob);
            }

            @Override
            public void afterCreateTemporaryBlob(Blob blob) {
                temporaryBlobs.add(blob);
            }
        };
    }

    protected PreparedStatement asStatement() {
        return query;
    }

    protected int getDefaultFetchSize() {
        return 0;
    }

    protected PreparedStatement prepareStatement() throws SQLException {
        return arte.getDefManager().getDbQueryBuilder().prepareStatementWithProfiling(sqlQuery);
    }

    protected boolean canCloseStatmentOnFree() {
        return false;
    }

    protected boolean isReadOnly() {
        return false;
    }

    final boolean getIsBusy() {
        return isBusy;
    }

    protected boolean isDeletedFromCache() {
        return deletedFromCache;
    }

    protected void releaseTemporaryLobs() {
        try {
            if (arte != null && arte.getDbConnection() != null) {
                arte.getDbConnection().freeTemporaryClobs(temporaryClobs);
                temporaryClobs.clear();
                arte.getDbConnection().freeTemporaryBlobs(temporaryBlobs);
                temporaryBlobs.clear();
            }
        } catch (Throwable t) {
            arte.getTrace().put(EEventSeverity.WARNING, "Error while releasing automatically created LOBs: " + ExceptionTextFormatter.throwableToString(t), EEventSource.ARTE_DB);
        }
    }

    public void free() {
        isBusy = false;
        releaseTemporaryLobs();
        if (isDeletedFromCache() || (wantCloseStatementOnFree && canCloseStatmentOnFree())) {
            closeQuery();
        }
    }

    private void closeQuery() {
        try {
            if (query != null) {
                try {
                    if (LOG_CLOSE_FROM_NON_ARTE_THREAD && Thread.currentThread() != arte.getProcessorThread() && arte.getProcessorThread().isAlive()) {
                        final Instance instance = Instance.get();
                        if (instance.getState() == InstanceState.STARTED) {
                            instance.getTrace().putFloodControlled("non-closed-db-query-" + Objects.hashCode(sqlQuery) + "-" + arte.getSeqNumber(), EEventSeverity.WARNING, "Non-closed sql from " + arte.getProcessorThread().getName() + ":" + sqlQuery, null, null, EEventSource.ARTE.getValue(), -1, false, null);
                        }
                    }
                } catch (Exception ex) {
                    //ignore
                }
                query.close();
            }
        } catch (SQLException e) {
            arte.getTrace().put(EEventSeverity.ERROR, "Can't close statement of DbQuery: " + arte.getTrace().exceptionStackToString(e), EEventSource.ARTE_DB);
        } finally {
            query = null;
        }
    }

    final void deletedFromCache(final boolean closeIfNotBusy) {
        deletedFromCache = true;
        if (closeIfNotBusy && !isBusy) {
            free();
        }
    }

    protected void prepareQuery() throws DbQueryBuilderError {
        if (query == null) {
            try {
                query = prepareStatement();
                ((RadixPreparedStatement) query).setReadOnly(isReadOnly());
                final int defaultFetchSize = getDefaultFetchSize();
                if (defaultFetchSize > 0) {
                    query.setFetchSize(defaultFetchSize);
                }
            } catch (SQLException ex) {
                throw new DbQueryBuilderError("Unable to prepare query", ex);
            }
        }
    }

    protected void reprepareQuery() throws DbQueryBuilderError {
        closeQuery();
        prepareQuery();
    }

    protected final void prepare() {
        prepareQuery();
        if (params != null) {
            int i = 1;
            for (Param param : params) {
                if (param instanceof InputEntityPropParam) {
                    final Entity ent = arte.getEntityObject(((InputEntityPropParam) param).pid);
                    final Id propId = ((InputEntityPropParam) param).propId;
                    final RadPropDef propDef = ent.getRadMeta().getPropById(propId);
                    setParam(i, propDef.getValType(), propDef.getDbType(), ent.getProp(propId), propDef.getName());
                } else if (param instanceof InputEntityPidParam) {
                    final Pid pid = ((InputEntityPidParam) param).pid;
                    setParam(i, EValType.STR, OracleTypeNames.VARCHAR2, pid.toString(), null);
                } else if (param instanceof InputTypifiedValParam) {
                    final InputTypifiedValParam p = (InputTypifiedValParam) param;
                    setParam(i, p.valueType, p.valueType.getDefaultDbType(), p.value, null);
                }
                i++;
            }
        }
    }

    public static final Object getFieldVal(final Arte arte, final ResultSet rs, final int fieldIdx, final EValType valType, final String dbType) {
        return getFieldVal(arte, rs, null, fieldIdx, valType, dbType);
    }

    public static final Object getFieldVal(final Arte arte, final ResultSet rs, final String fieldAlias, final EValType valType, final String dbType) {
        return getFieldVal(arte, rs, fieldAlias, 0, valType, dbType);
    }

    protected static final Object getFieldVal(final Arte arte, final ResultSet rs, final String fieldAlias, final int fieldIdx, final EValType valType, final String dbType) {
        try {
            switch (valType) {
                case STR:
                case PARENT_REF:
                case OBJECT:
                    return fieldAlias != null ? rs.getString(fieldAlias) : rs.getString(fieldIdx);
                case INT: {
                    final long x = fieldAlias != null ? rs.getLong(fieldAlias) : rs.getLong(fieldIdx);
                    return rs.wasNull() ? null : Long.valueOf(x);
                }
                case NUM:
                    return fieldAlias != null ? rs.getBigDecimal(fieldAlias) : rs.getBigDecimal(fieldIdx);
                case DATE_TIME:
                    return fieldAlias != null ? rs.getTimestamp(fieldAlias) : rs.getTimestamp(fieldIdx);
                case BOOL: {
                    final boolean x = fieldAlias != null ? rs.getBoolean(fieldAlias) : rs.getBoolean(fieldIdx);
                    return rs.wasNull() ? null : Boolean.valueOf(x);
                }
                case BIN: {
                    if (fieldAlias != null) {
                        if (rs.getObject(fieldAlias) == null) {
                            return null;
                        } else {
                            return new Bin(rs.getBytes(fieldAlias));
                        }
                    } else {
                        if (rs.getObject(fieldIdx) == null) {
                            return null;
                        } else {
                            return new Bin(rs.getBytes(fieldIdx));
                        }
                    }
                }
                case CHAR: {
                    final String tmp = fieldAlias != null ? rs.getString(fieldAlias) : rs.getString(fieldIdx);
                    if (tmp != null && tmp.length() > 0) {
                        return Character.valueOf(tmp.charAt(0));
                    } else {
                        return null;
                    }
                }
                case CLOB:
                    return fieldAlias != null ? rs.getClob(fieldAlias) : rs.getClob(fieldIdx);
                case BLOB:
                    return fieldAlias != null ? rs.getBlob(fieldAlias) : rs.getBlob(fieldIdx);

                case ARR_BIN:
                case ARR_BOOL:
                case ARR_STR:
                case ARR_REF:
                case ARR_CHAR:
                case ARR_INT:
                case ARR_NUM:
                case ARR_DATE_TIME: {
                    final String s;
                    if (OracleTypeNames.CLOB.equals(dbType.toUpperCase())) {
                        final java.sql.Clob c = fieldAlias != null ? rs.getClob(fieldAlias) : rs.getClob(fieldIdx);
                        if (c == null) {
                            return null;
                        }
                        //TODO поддержка clob-а в качестве valasstr
                        if (c.length() > Integer.MAX_VALUE) {
                            throw new IllegalUsageError("Can't convert array clob presentation to string: Clob data size is too big", null);
                        }
                        final int len = (int) c.length();
                        s = c.getSubString(1, len);
                    } else {
                        s = fieldAlias != null ? rs.getString(fieldAlias) : rs.getString(fieldIdx);
                        if (s == null) {
                            return null;
                        }
                    }
                    return SrvValAsStr.fromStr(arte, s, valType);
                }
                default:
                    throw new DbQueryBuilderError("Unsupported field value type:  \"" + valType.getName() + "\"", null);
            }
        } catch (SQLException e) {
            throw new DbQueryBuilderError("Can't get field value: " + ExceptionTextFormatter.getExceptionMess(e), e);
        }
    }

    protected final void setParam(final int idx, final EValType valType, final String dbType, final Object val, final String nameForDebug) {
        setParam(arte, query, idx, valType, dbType, val, nameForDebug, setParamCallbackHandler);
    }

    public static final void setParam(final Arte arte, final PreparedStatement stmt, final int idx, final EValType valType, final String dbType, final Object val, final String nameForDebug) {
        setParam(arte, stmt, idx, valType, dbType, val, nameForDebug, null);
    }

    public static final void setParam(final Arte arte, final PreparedStatement stmt, final int idx, final EValType valType, final String dbType, final Object val, final String nameForDebug, final ISetParamCallbackHandler callbackHandler) {
        try {
            if (val == null) {
                stmt.setObject(idx, null);
                return;
            }
            switch (valType) {
                case STR: {
                    final String strVal;
                    if (val instanceof IKernelStrEnum) {
                        strVal = ((IKernelStrEnum) val).getValue();
                    } else {
                        strVal = (String) val;
                    }
                    stmt.setString(idx, strVal);
                    if (strVal.length() > 4000) {
                        arte.getTrace().put(EEventSeverity.WARNING, "String query parameter value length exceeds 4000." + (nameForDebug == null ? "" : " Name: " + nameForDebug), EEventSource.ARTE_DB);
                    }
                    break;
                }
                case PARENT_REF:
                case OBJECT: {
                    final String strVal;
                    if (val instanceof Entity) {
                        strVal = ((Entity) val).getPid().toString();
                    } else if (val instanceof Pid) {
                        strVal = ((Pid) val).toString();
                    } else {
                        strVal = (String) val;
                    }
                    stmt.setString(idx, strVal);
                    if (strVal.length() > 4000) {
                        arte.getTrace().put(EEventSeverity.WARNING, "String query parameter value length exceeds 4000." + (nameForDebug == null ? "" : " Name: " + nameForDebug), EEventSource.ARTE_DB);
                    }
                    break;
                }
                case INT:
                    if (val instanceof IKernelIntEnum) {
                        stmt.setLong(idx, ((IKernelIntEnum) val).getValue().longValue());
                    } else {
                        stmt.setLong(idx, ((Long) val).longValue());
                    }
                    break;
                case NUM:
                    stmt.setBigDecimal(idx, (BigDecimal) val);
                    break;
                case DATE_TIME:
                    stmt.setTimestamp(idx, (java.sql.Timestamp) val);
                    break;
                case BOOL:
                    stmt.setBoolean(idx, ((Boolean) val).booleanValue());
                    break;
                case BIN:
                    stmt.setBytes(idx, ((Bin) val).get());
                    break;
                case CHAR:
                    if (val instanceof IKernelCharEnum) {
                        stmt.setString(idx, ((IKernelCharEnum) val).getValue().toString());
                    } else {
                        stmt.setString(idx, ((Character) val).toString());
                    }
                    break;
                case CLOB:
                    final Clob clob;
                    if (val instanceof XmlObject) {
                        clob = EasValueConverter.xml2Clob(arte, (XmlObject) val);
                        if (callbackHandler != null) {
                            callbackHandler.afterCreateTemporaryClob(clob);
                        }
                    } else if (val instanceof String) {
                        clob = EasValueConverter.str2Clob(arte, (String) val);
                        if (callbackHandler != null) {
                            callbackHandler.afterCreateTemporaryClob(clob);
                        }
                    } else {
                        clob = (java.sql.Clob) val;
                    }
                    stmt.setClob(idx, clob);
                    break;
                case BLOB:
                    final Blob blob;
                    if (val instanceof XmlObject) {
                        blob = EasValueConverter.xml2Blob(arte, (XmlObject) val);
                        if (callbackHandler != null) {
                            callbackHandler.afterCreateTemporaryBlob(blob);
                        }
                    } else {
                        blob = (java.sql.Blob) val;
                    }
                    stmt.setBlob(idx, blob);
                    break;
                case ARR_BOOL:
                case ARR_BIN:
                case ARR_STR:
                case ARR_REF:
                case ARR_CHAR:
                case ARR_INT:
                case ARR_NUM:
                case ARR_DATE_TIME: {
                    final String s = SrvValAsStr.toStr(arte, val, valType);
                    if (dbType != null && OracleTypeNames.CLOB.equals(dbType.toUpperCase())) {
                        //TODO поддержка clob-а в качестве valasstr
                        final Clob c = arte.getDbConnection().createTemporaryClob();
                        if (callbackHandler != null) {
                            callbackHandler.afterCreateTemporaryClob(c);
                        }
                        c.setString(1, s);
                        stmt.setClob(idx, c);
                    } else {
                        stmt.setString(idx, s);
                    }
                }
                break;
                default:
                    throw new DbQueryBuilderError("Unsupported property type " + valType.getName() + " of InputPropProp", null);
            }
        } catch (SQLException e) {
            throw new DbQueryBuilderError("Can't set InputPropParam: " + ExceptionTextFormatter.getExceptionMess(e), e);
        }
    }

    public static final void setParam(final Arte arte, final PreparedStatement stmt, final int idx, final Object val) {
        try {
            stmt.setObject(idx, val);
        } catch (SQLException e) {
            throw new DbQueryBuilderError("Can't set InputPropParam: " + ExceptionTextFormatter.getExceptionMess(e), e);
        }
    }

    @Override
    protected void finalize() throws Throwable {
        final PreparedStatement statement = query;
        if (statement != null) {
            if (statement.getConnection() instanceof RadixConnection) {
                ((RadixConnection) statement.getConnection()).scheduleStatementToClose(statement);
                if (LOG_CLOSE_FROM_NON_ARTE_THREAD) {
                    final Instance instance = Instance.get();
                    instance.getTrace().put(EEventSeverity.WARNING, arte.getProcessorThread().getName() + ": schedule statement for close: " + query + " : " + sqlQuery, EEventSource.INSTANCE);
                }
            } else {
                statement.close();
            }
        }
        super.finalize();
    }

    protected static void checkIfItIsUniqueConstraintViolated(final Arte arte, final SQLException e, final DdsTableDef tab, final RadClassDef classMeta) throws UniqueConstraintViolation {
        if (e.getErrorCode() == OraExCodes.UNIQUE_CONSTRAINT_VIOLATED) {
            final String mess = e.getMessage().toUpperCase();
            if (tab.getPrimaryKey().getDbName() != null && mess.contains("." + tab.getPrimaryKey().getDbName().toUpperCase() + ")")) {
                //Значение поля "%1" в таблице "%2" должно быть уникальным
                final String mls = MultilingualString.get(arte, Messages.MLS_OWNER_ID, Messages.MLS_ID_DBQ_UNIQUE_CONSTRAINT_VIOLATION_IDX);
                throw new UniqueConstraintViolation(classMeta, tab, tab.getPrimaryKey(), String.format(mls, tab.getPrimaryKey().getName(), tab.getName()), e);
            }
            for (DdsIndexDef idx : tab.getIndices().get(ExtendableDefinitions.EScope.ALL)) {
                if (errMatchesDbName(mess, idx.getDbName()) || errMatchesDbName(mess, idx.getUniqueConstraint() == null ? null : idx.getUniqueConstraint().getDbName())) {
                    final String mls = MultilingualString.get(arte, Messages.MLS_OWNER_ID, Messages.MLS_ID_DBQ_UNIQUE_CONSTRAINT_VIOLATION_IDX);
                    throw new UniqueConstraintViolation(classMeta, tab, idx, String.format(mls, "'" + idx.getName() + "'"), e);
                }
            }
        }
    }

    protected static void checkIfItIsIntegrityConstraintViolated(final Arte arte, final SQLException e, final DdsTableDef tab) throws UniqueConstraintViolation {
        /* if ((e.getErrorCode() != OraExCodes.INTEGRITY_CONSTRAINT_VIOLATED_CHILD_RECORD_FOUND) &&
         (e.getErrorCode() != OraExCodes.INTEGRITY_CONSTRAINT_VIOLATED_PARENT_KEY_NOT_FOUND)){
         return;
         }       
         if (e.getErrorCode() == OraExCodes.INTEGRITY_CONSTRAINT_VIOLATED_CHILD_RECORD_FOUND) {//ORA_02292
         String childTableDbName=getTableDbNameFromFK(e.getMessage());
         if(childTableDbName!=null){
         for (DdsReferenceDef reference : tab.collectOutgoingReferences()) {
         for (DdsReferenceDef.ColumnsInfoItem item : reference.getColumnsInfo()) {    
         DdsColumnDef childColumn=item.findChildColumn();
         DdsTableDef childTable=childColumn.getOwnerTable();
         String tableDbName=childTable.getDbName();
         if (childTableDbName.equals(tableDbName)) {
         final String mls = MultilingualString.get(arte, Messages.MLS_OWNER_ID, Messages.MLS_ID_DBQ_INTEGRITY_CONSTRAINT_VIOLATION);
         resultMess=String.format(mls, childColumn.getName(),childTable.getName(),item.findParentColumn().getName(),tab.getName());
         //Значение поля %1 таблицы %2 должны соответствовать значениям поля %3 таблицы %4
         }
         }
         }  
         }
         throw new DatabaseError(resultMess, e);
         }else*/
        String resultMess = e.getMessage();
        if ((e.getErrorCode() == OraExCodes.INTEGRITY_CONSTRAINT_VIOLATED_CHILD_RECORD_FOUND)
                || (e.getErrorCode() == OraExCodes.INTEGRITY_CONSTRAINT_VIOLATED_PARENT_KEY_NOT_FOUND)) {//ORA_02291
            String fk = getFKName(e.getMessage());
            for (DdsReferenceDef reference : tab.collectOutgoingReferences()) {
                if (reference.getDbName().equals(fk)) {
                    final String mls;
                    if (reference.getColumnsInfo().size() == 1) {
                        mls = MultilingualString.get(arte, Messages.MLS_OWNER_ID, Messages.MLS_ID_DBQ_INTEGRITY_CONSTRAINT_VIOLATION);
                    } else {
                        mls = MultilingualString.get(arte, Messages.MLS_OWNER_ID, Messages.MLS_ID_DBQ_INTEGRITY_CONSTRAINT_VIOLATION_SEVERAL_COLUMNS);
                    }
                    String childColumns = "";
                    String parentColumns = "";
                    String childTableName = reference.findChildTable(tab).getName();
                    String parentTableName = reference.findParentTable(tab).getName();
                    for (DdsReferenceDef.ColumnsInfoItem item : reference.getColumnsInfo()) {
                        DdsColumnDef parentColumn = item.findParentColumn();
                        DdsColumnDef childColumn = item.findChildColumn();
                        childColumns += childColumn.getName() + ", ";
                        parentColumns += parentColumn.getName() + ", ";
                    }
                    if (!childColumns.isEmpty()) {
                        childColumns = childColumns.substring(0, childColumns.length() - 2);
                    }
                    if (!parentColumns.isEmpty()) {
                        parentColumns = parentColumns.substring(0, parentColumns.length() - 2);
                    }

                    if (e.getErrorCode() == OraExCodes.INTEGRITY_CONSTRAINT_VIOLATED_CHILD_RECORD_FOUND) {
                        String mess = MultilingualString.get(arte, Messages.MLS_OWNER_ID, Messages.MLS_ID_DBQ_INTEGRITY_CONSTRAINT_VIOLATION_CHILD_REF);
                        resultMess = String.format(mess, childTableName);
                    } else {
                        String mess = MultilingualString.get(arte, Messages.MLS_OWNER_ID, Messages.MLS_ID_DBQ_INTEGRITY_CONSTRAINT_VIOLATION_NO_PARENT_RECORD);
                        resultMess = String.format(mess, parentTableName);
                    }

                    resultMess = resultMess + "\n" + String.format(mls, childColumns, childTableName, parentColumns, parentTableName);
                    //Значение поля %1 таблицы %2 должны соответствовать значениям поля %3 таблицы %4                   
                }
            }
            throw new DatabaseError(resultMess, e);
        }

    }

    protected static void checkIfItIsNullConstraintViolated(final Arte arte, final SQLException e, final DdsTableDef tab) throws UniqueConstraintViolation {
        String resultMess = e.getMessage();
        if (e.getErrorCode() == OraExCodes.NOT_NULL_CONSTRAINT_VIOLATED) {
            String columnDbName = getColumnDbName(resultMess);
            if (columnDbName != null) {
                for (DdsColumnDef column : tab.getColumns().get(ExtendableDefinitions.EScope.ALL)) {
                    if (column.getDbName().equals(columnDbName)) {
                        final String mls = MultilingualString.get(arte, Messages.MLS_OWNER_ID, Messages.MLS_ID_DBQ_NOT_NULL_CONSTRAINT_VIOLATION);
                        resultMess = String.format(mls, column.getName(), tab.getName());
                        //Необходимо указать значение столбца "%1" в таблице "%2"
                    }
                }
            }
            throw new DatabaseError(resultMess, e);
        }

    }

    private static String getColumnDbName(final String mess) {
        int start = mess.indexOf("(");
        if (start != -1) {
            int end = mess.indexOf(")", start);
            if (end != -1) {
                String s = mess.substring(start + 1, end);
                start = s.lastIndexOf(".", start);
                if (start != -1) {
                    String columnName = s.substring(start + 1, s.length());
                    return columnName.replaceAll("\"", "");
                }
            }
        }
        return null;
    }

    private static String getFKName(final String mess/*,boolean isChildTableName*/) {
        //String tableDbName=null;
        int start = mess.indexOf("(");
        if (start != -1) {
            int end = mess.indexOf(")", start);
            if (end != -1) {
                String s = mess.substring(start + 1, end);
                start = s.indexOf(".");
                if (start != -1) {
                    String fk = s.substring(start + 1, s.length());
                    return fk;
                    /*String[] arrFk=fk.split("_");
                     if(arrFk.length>0 && arrFk[0].equals("FK")){
                     if(arrFk.length==3){
                     tableDbName=isChildTableName ? arrFk[2]:arrFk[1];
                     }else if(arrFk.length>3){
                     String prefix="";
                     for(int i=1;i<arrFk.length-2;i++){
                     prefix+=arrFk[i]+"_";
                     }
                     tableDbName=isChildTableName ? prefix+arrFk[arrFk.length-1]:prefix+arrFk[arrFk.length-2];
                     }
                     }*/
                }
            }
        }
        return null;
    }

    private static boolean errMatchesDbName(final String errMessage, final String dbName) {
        if (errMessage == null || dbName == null) {
            return false;
        }
        return errMessage.contains("." + dbName.toUpperCase() + ")");
    }

    //Subclasses "param"
    public abstract static class Param {
    }

    public abstract static class InputParam extends Param {
    }

    public static class InputTypifiedValParam extends Param {

        final Object value;
        final EValType valueType;

        public InputTypifiedValParam(final Object val, final EValType type) {
            this.value = val;
            this.valueType = type;
        }

        public Object getValue() {
            return value;
        }

        public EValType getValueType() {
            return valueType;
        }

        @Override
        public boolean equals(final Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final InputTypifiedValParam other = (InputTypifiedValParam) obj;
            if (this.value != other.value && (this.value == null || !this.value.equals(other.value))) {
                return false;
            }
            if (this.valueType != other.valueType && (this.valueType == null || !this.valueType.equals(other.valueType))) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            return 7;
        }

        @Override
        public String toString() {
            return getClass().getSimpleName() + "{valueType = " + valueType.getName() + ", value = " + String.valueOf(value) + "}";
        }
    }

    static final class InputPidParam extends InputParam {
    }

    static final class UpDefIdParam extends InputParam {
    }

    static final class UpValOwnerEntityIdParam extends InputParam {
    }

    static final class UpValOwnerPidParam extends InputParam {
    }

    public static final class InputThisPropParam extends InputParam {
        //Fields

        final Id propId;

        //Constructor
        InputThisPropParam(final Id propId) {
            this.propId = propId;
        }
    }

    public static final class InputEntityPropParam extends InputParam {
        //Fields

        final Pid pid;
        final Id propId;

        //Constructor
        public InputEntityPropParam(final Pid pid, final Id propId) {
            this.pid = pid;
            this.propId = propId;
        }

        public Id getPropId() {
            return propId;
        }

        public Pid getPid() {
            return pid;
        }

        @Override
        public boolean equals(final Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final InputEntityPropParam other = (InputEntityPropParam) obj;
            if (this.pid != other.pid && (this.pid == null || !this.pid.equals(other.pid))) {
                return false;
            }
            if (this.propId != other.propId && (this.propId == null || !this.propId.equals(other.propId))) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 89 * hash + (this.pid != null ? this.pid.hashCode() : 0);
            hash = 89 * hash + (this.propId != null ? this.propId.hashCode() : 0);
            return hash;
        }
    }

    public static final class InputEntityPidParam extends InputParam {
        //Fields

        final Pid pid;

        //Constructor
        public InputEntityPidParam(final Pid pid) {
            this.pid = pid;
        }

        public Pid getPid() {
            return pid;
        }

        @Override
        public boolean equals(final Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final InputEntityPidParam other = (InputEntityPidParam) obj;
            if (this.pid != other.pid && (this.pid == null || !this.pid.equals(other.pid))) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 89 * hash + (this.pid != null ? this.pid.hashCode() : 0);
            return hash;
        }
    }

    public static final class InputChildPropParam extends InputParam {
        //Fields

        final Id propId;

        //Constructor
        public InputChildPropParam(final Id propId) {
            this.propId = propId;
        }

        @Override
        public boolean equals(final Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final InputChildPropParam other = (InputChildPropParam) obj;
            if (this.propId != other.propId && (this.propId == null || !this.propId.equals(other.propId))) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 29 * hash + (this.propId != null ? this.propId.hashCode() : 0);
            return hash;
        }
    }

    public static final class InputParentPropParam extends InputParam {
        //Fields

        final Id propId;

        //Constructor
        public InputParentPropParam(final Id propId) {
            this.propId = propId;
        }

        @Override
        public boolean equals(final Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final InputParentPropParam other = (InputParentPropParam) obj;
            if (this.propId != other.propId && (this.propId == null || !this.propId.equals(other.propId))) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 29 * hash + (this.propId != null ? this.propId.hashCode() : 0);
            return hash;
        }
    }

    public static final class InputGroupPropParam extends InputParam {
        //Fields

        final Id propId;

        //Constructor
        public InputGroupPropParam(final Id propId) {
            this.propId = propId;
        }

        @Override
        public boolean equals(final Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final InputGroupPropParam other = (InputGroupPropParam) obj;
            if (this.propId != other.propId && (this.propId == null || !this.propId.equals(other.propId))) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 29 * hash + (this.propId != null ? this.propId.hashCode() : 0);
            return hash;
        }
    }

    static class Field {
        //Public fields

        private final Arte arte;
        final String alias;
        final int index;
        final Id id;
        private final EValType valType;
        private final String dbType;
        //Constructor    

        Field(final Arte arte, final SqlBuilder.Field builderField) {
            this(arte, builderField.getId(), builderField.getValType(), builderField.getAlias() == null ? null : builderField.getDbType(), builderField.getAlias(), builderField.getIndex());
        }

        Field(final Arte arte, final Id fieldId, final EValType valType, final String dbType, final String alias, final int index) {
            this.alias = alias;
            this.arte = arte;
            this.index = index;
            this.id = fieldId;
            this.valType = valType;
            this.dbType = dbType;
        }

        protected final Object getFieldVal(final ResultSet rs) {
            if (index > 0) {
                return DbQuery.getFieldVal(arte, rs, index, valType, dbType);
            } else {
                return DbQuery.getFieldVal(arte, rs, alias, valType, dbType);
            }
        }

        private static EValType getValType(final Definition prop) {
            if (prop instanceof DdsColumnDef) {
                return ((DdsColumnDef) prop).getValType();
            }
            if (prop instanceof RadPropDef) {
                return ((RadPropDef) prop).getValType();
            }
            throw new IllegalUsageError("Unsupported property");
        }

        private static String getDbType(final Definition prop) {
            if (prop instanceof DdsColumnDef) {
                return ((DdsColumnDef) prop).getDbType();
            }
            if (prop instanceof RadPropDef) {
                return ((RadPropDef) prop).getDbType();
            }
            throw new IllegalUsageError("Unsupported property");
        }
    }

    public static interface ISetParamCallbackHandler {

        public void afterCreateTemporaryClob(Clob clob);

        public void afterCreateTemporaryBlob(Blob blob);
    }
}
