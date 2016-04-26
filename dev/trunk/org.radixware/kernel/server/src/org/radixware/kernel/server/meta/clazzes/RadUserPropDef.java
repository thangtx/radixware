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

package org.radixware.kernel.server.meta.clazzes;

import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlbeans.XmlObject;

import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.defs.value.RadixDefaultValue;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EPropInitializationPolicy;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.enums.OracleTypeNames;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.types.ArrBin;
import org.radixware.kernel.server.types.ArrBlob;
import org.radixware.kernel.common.types.ArrBool;
import org.radixware.kernel.common.types.ArrChar;
import org.radixware.kernel.server.types.ArrClob;
import org.radixware.kernel.common.types.ArrDateTime;
import org.radixware.kernel.common.types.ArrInt;
import org.radixware.kernel.common.types.ArrNum;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.types.Bin;
import org.radixware.kernel.common.types.IKernelEnum;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.server.arte.services.eas.EasValueConverter;
import org.radixware.kernel.server.dbq.userprops.UserPropUtils;
import org.radixware.kernel.server.exceptions.DatabaseError;
import org.radixware.kernel.server.types.ArrEntity;
import org.radixware.kernel.server.types.Entity;
import org.radixware.kernel.server.types.Pid;

public class RadUserPropDef extends RadPropDef {

    private final Id ownerEntityId;
    private final boolean isUpdateAuditOn;

    public RadUserPropDef(
            final Id id,
            final String name,
            final Id titleId,
            final EValType valType,
            final Id constSetId,
            final boolean isValInheritable,
            final ValAsStr valInheritMarkValAsStr,
            final ValInheritancePath[] valInheritPathes,
            final EPropInitializationPolicy initPolicy, final RadixDefaultValue initVal,
            final Id ownerEntityId,
            final boolean isUpdateAuditOn,
            final IRadPropAccessor accessor) {
        super(id, name, titleId, valType, constSetId, isValInheritable, valInheritMarkValAsStr, valInheritPathes, initPolicy, initVal, accessor);
        this.ownerEntityId = ownerEntityId;
        this.isUpdateAuditOn = isUpdateAuditOn;
    }

    @Override
    public String getDbType() {
        if (getValType() == EValType.PARENT_REF) {
            return OracleTypeNames.VARCHAR2;
        } else if (getValType() == EValType.XML) {
            return OracleTypeNames.CLOB;
        }
        return getValType().getDefaultDbType();
    }

    //////////////////////////// User Property Misc //////////////////////////////////
    /**
     * Data type mapping: EValType.BOOL - Boolean (ArrBool) EValType.INT - Long
     * (ArrInt) EValType.NUM - BigDecimal (ArrNum) EValType.STR - String
     * (ArrStr) EValType.CHAR - Character (ArrChar) EValType.MEMO - String
     * (ArrStr) EValType.DATE_TIME - Timestamp (ArrDateTime) EValType.REF - Pid
     * (ArrEntity) EValType.OBJECT - Pid (ArrEntity) EValType.BIN - Bin (ArrBin)
     * EValType.CLOB - String (ArrClob) EValType.BLOB - Bin (ArrBlob)
     */
    public Object getValue(final Entity owner) {
        try {
            final ResultSet rs = createAndExecUsualStatement(owner.getArte(), owner.getEntityId().toString(), owner.getPid().toString(), getId().toString(),
                    new SimpleStatementProcessor() {

                        @Override
                        public ResultSet execThisQuery(PreparedStatement stmt) throws SQLException {
                            return execStmt(owner.getArte(), stmt, false);
                        }

                        @Override
                        public String getQuery() {
                            return "select " + UserPropUtils.getGetterNameByType(getValType()) + "(?,?,?)" + " from dual";
                        }

                        @Override
                        public String getStmtKey() {
                            return "get" + getValType().getName();
                        }
                    });
            try {
                if (!rs.next()) {
                    return null;
                }
                switch (getValType()) {
                    case BOOL: {
                        final boolean val = rs.getBoolean(1);
                        return rs.wasNull() ? null : Boolean.valueOf(val);
                    }
                    case ARR_BOOL:
                        return ArrBool.fromValAsStr(rs.getString(1));
                    case INT: {
                        final long val = rs.getLong(1);
                        return rs.wasNull() ? null : Long.valueOf(val);
                    }
                    case ARR_INT:
                        return ArrInt.fromValAsStr(rs.getString(1));
                    case NUM:
                        return rs.getBigDecimal(1);
                    case ARR_NUM:
                        return ArrNum.fromValAsStr(rs.getString(1));
                    case STR:
                        return rs.getString(1);
                    case ARR_STR:
                        return ArrStr.fromValAsStr(rs.getString(1));
                    case CHAR: {
                        final String str = rs.getString(1);
                        return str == null ? null : str.charAt(0);
                    }
                    case ARR_CHAR:
                        return ArrChar.fromValAsStr(rs.getString(1));
                    case DATE_TIME:
                        return rs.getTimestamp(1);
                    case ARR_DATE_TIME:
                        return ArrDateTime.fromValAsStr(rs.getString(1));
                    case ARR_REF:
                        return ArrEntity.fromValAsStr(owner.getArte(), rs.getString(1));
                    case PARENT_REF:
                    case OBJECT: {
                        final String pidStr = rs.getString(1);
                        if (pidStr == null) {
                            return null;
                        }
                        final Id entId = ((RadUserRefPropDef) this).getDestinationEntityId();
                        return owner.getArte().getEntityObject(new Pid(owner.getArte(), entId, pidStr));
                    }
                    case BIN: {
                        final byte[] bytes = rs.getBytes(1);
                        return bytes == null ? null : new Bin(bytes);
                    }
                    case ARR_BIN:
                        return ArrBin.fromValAsStr(rs.getString(1));
                    case CLOB:
                        return rs.getClob(1);
                    case XML:
                        return rs.getClob(1);
                    case ARR_CLOB:
                        return ArrClob.fromValAsStr(owner.getArte(), rs.getString(1));
                    case BLOB:
                        return rs.getBlob(1);
                    case ARR_BLOB:
                        return ArrBlob.fromValAsStr(owner.getArte(), rs.getString(1));
                    default:
                        throw new DefinitionError("Unsupported user property type \"" + getValType().getName() + "\" in TDacUserProperty::getValue()", null);
                }
            } finally {
                rs.close();
            }
        } catch (SQLException e) {
            throw new DatabaseError(e.getMessage(), e);
        }
    }

    public void setValue(final Entity owner, Object value) {
        if (getValType().isArrayType()) {
            processMerge(value == null ? null : value.toString(), owner, new ValueSetter() {

                @Override
                public void set(final PreparedStatement stmt, final Object o, final int paramIdx) throws SQLException {
                    stmt.setString(paramIdx, (String) o);
                }
            });
            return;
        }
        if (value instanceof IKernelEnum) {
            value = ((IKernelEnum) value).getValue();
        }

        switch (getValType()) {
            case BOOL: {
                processMerge(value, owner, new ValueSetter() {

                    @Override
                    public void set(final PreparedStatement stmt, final Object o, final int paramIdx) throws SQLException {
                        if (o == null) {
                            stmt.setNull(paramIdx, java.sql.Types.BIT);
                        } else {
                            stmt.setBoolean(paramIdx, ((Boolean) o).booleanValue());
                        }
                    }
                });
            }
            break;
            case INT: {
                processMerge(value, owner, new ValueSetter() {

                    @Override
                    public void set(PreparedStatement stmt, Object o, int paramIdx) throws SQLException {
                        if (o == null) {
                            stmt.setNull(paramIdx, java.sql.Types.BIGINT);
                        } else {
                            stmt.setLong(paramIdx, ((Long) o).longValue());
                        }
                    }
                });
            }
            break;
            case NUM: {
                processMerge(value, owner, new ValueSetter() {

                    @Override
                    public void set(PreparedStatement stmt, Object o, int paramIdx) throws SQLException {
                        stmt.setBigDecimal(paramIdx, (BigDecimal) o);
                    }
                });
            }
            break;
            case STR: {
                processMerge(value, owner, new ValueSetter() {

                    @Override
                    public void set(PreparedStatement stmt, Object o, int paramIdx) throws SQLException {
                        stmt.setString(paramIdx, (String) o);
                    }
                });
            }
            break;

            case CHAR: {
                processMerge(value, owner, new ValueSetter() {

                    @Override
                    public void set(PreparedStatement stmt, Object o, int paramIdx) throws SQLException {
                        stmt.setString(paramIdx, (o == null ? null : o.toString()));
                    }
                });
            }
            break;
            case PARENT_REF:
            case OBJECT: {
                processMerge(value, owner, new ValueSetter() {

                    @Override
                    public void set(PreparedStatement stmt, Object o, int paramIdx) throws SQLException {
                        final Pid pid;
                        if (o instanceof Entity) {
                            pid = ((Entity) o).getPid();
                        } else {
                            pid = (Pid) o;
                        }
                        stmt.setString(paramIdx, pid == null ? null : pid.toString());
                    }
                });
            }
            break;
            case DATE_TIME: {
                processMerge(value, owner, new ValueSetter() {

                    @Override
                    public void set(PreparedStatement stmt, Object o, int paramIdx) throws SQLException {
                        stmt.setTimestamp(paramIdx, o == null ? null : (Timestamp) o);
                    }
                });
            }
            break;
            case BIN: {
                processMerge(value, owner, new ValueSetter() {

                    @Override
                    public void set(PreparedStatement stmt, Object o, int paramIdx) throws SQLException {
                        final Bin bin = (Bin) o;
                        stmt.setBytes(paramIdx, bin != null ? bin.get() : null);
                    }
                });
            }
            break;
            case XML: {
                processMerge(value, owner, new ValueSetter() {

                    @Override
                    public void set(PreparedStatement stmt, Object o, int paramIdx) throws SQLException {
                        final Clob clob = EasValueConverter.xml2Clob(owner.getArte(), (XmlObject) o);
                        stmt.setClob(paramIdx, clob);
                    }
                });
            }
            break;
            case CLOB: {
                processMerge(value, owner, new ValueSetter() {

                    @Override
                    public void set(PreparedStatement stmt, Object o, int paramIdx) throws SQLException {
                        Clob clob = (java.sql.Clob) o;
                        stmt.setClob(paramIdx, clob);
                    }
                });
            }
            break;
            case BLOB: {
                processMerge(value, owner, new ValueSetter() {

                    @Override
                    public void set(PreparedStatement stmt, Object o, int paramIdx) throws SQLException {
                        stmt.setBlob(paramIdx, (Blob) o);
                    }
                });
            }
            break;
            default:
                throw new DefinitionError("Unsupported data type: " + getValType().getName(), null);

        }
    }

    public void delValue(final Pid ownerPid) {
        final Arte arte = ownerPid.getArte();
        final EValType checkValType = getValType() == EValType.XML ? EValType.CLOB : getValType();
        createAndExecUsualStatement(arte, ownerPid.getEntityId().toString(), arte.getEntityObject(ownerPid).getRadMeta().getId().toString(), ownerPid.toString(),
                new SimpleStatementProcessor() {

                    @Override
                    public ResultSet execThisQuery(final PreparedStatement stmt) throws SQLException {
                        stmt.setString(4, getId().toString());
                        stmt.setInt(5, getIsUpdateAuditOn() ? 1 : 0);
                        stmt.setString(6, getIsUpdateAuditOn() ? ownerPid.getArte().getEntityObject(ownerPid).getAuditSchemeId() : null);
                        execStmt(arte, stmt, true);
                        return null;
                    }

                    @Override
                    public String getQuery() {
                        return "begin RDX_Entity.delUserPropVal(?,?,?,?," + checkValType.getValue().toString() + ", ?, ?); end;";
                    }

                    @Override
                    public String getStmtKey() {
                        return "delValue" + getValType().getName();
                    }
                });
    }

    public Boolean isValueExist(final Pid ownerPid) {
        final Arte arte = ownerPid.getArte();
        final EValType checkValType = getValType() == EValType.XML ? EValType.CLOB : getValType();

        final ResultSet rs = createAndExecUsualStatement(arte, ownerPid.getEntityId().toString(), ownerPid.toString(), getId().toString(),
                new SimpleStatementProcessor() {

                    @Override
                    public ResultSet execThisQuery(final PreparedStatement stmt) throws SQLException {
                        return execStmt(arte, stmt, false);
                    }

                    @Override
                    public String getQuery() {
                        return "select RDX_Entity.isUserPropValDefined(?,?,?," + checkValType.getValue().toString() + ") from dual ";
                    }

                    @Override
                    public String getStmtKey() {
                        return "isValueExist" + getValType().getName();
                    }
                });
        try {
            rs.next();
            return rs.getInt(1) == 1 ? Boolean.TRUE : Boolean.FALSE;
        } catch (SQLException e) {
            throw new DatabaseError("Error while fetching isValueExist query results: " + ExceptionTextFormatter.getExceptionMess(e), e);
        } finally {
            try {
                rs.close();
            } catch (SQLException ex) {
                //do nothing
                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
            } // by BAO
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////
	/*
     * для предотвращения мутации, при удалении свойств типа Object, ArrObject
     * Pid'ы удаляемых инстанций складываются в массивы которые в дальнейшем
     * удаляются отдельно
     */	// хэш для хранения prepared statements
    //private static HashMap<String, PreparedStatement> stmtsHash = new HashMap<String, PreparedStatement>( 200 );
    //private static List<PreparedStatement> execStmts = new ArrayList<PreparedStatement>(); 
    private static ResultSet createAndExecUsualStatement(final Arte arte, final String par1, final String par2, final String par3, final SimpleStatementProcessor ssProc) {
        try {
            final PreparedStatement stmt = getStmtFromHash(arte, ssProc.getStmtKey(), ssProc.getQuery());
            stmt.setString(1, par1);
            stmt.setString(2, par2);
            stmt.setString(3, par3);
            return ssProc.execThisQuery(stmt);

        } catch (SQLException e) {
            throw new DatabaseError("Can't get/prepare/execute usual statement: " + ExceptionTextFormatter.getExceptionMess(e), e);
        }
    }

    private static PreparedStatement getStmtFromHash(final Arte arte, final String key, final String query) throws SQLException {
        return arte.getDefManager().getDbQueryBuilder().getUpStmtFromHash(key, query);
    }

    private static ResultSet execStmt(final Arte arte, final PreparedStatement stmt, final boolean asUpdate) throws SQLException {
        return arte.getDefManager().getDbQueryBuilder().execUpStmt(stmt, asUpdate);
    }

    private void processMerge(final Object val, final Entity owner, final ValueSetter setter) {
        final Arte arte = owner.getArte();
        try {
            final PreparedStatement stmt;
            stmt = getStmtFromHash(
                    arte,
                    "set" + getValType().getName(),
                    "begin " + UserPropUtils.getSetterNameByType(getValType()) + "(?, ? , ?, ?, ?, ?, ?); end;");
            stmt.setString(1, owner.getEntityId().toString());
            stmt.setString(2, owner.getRadMeta().getId().toString());
            stmt.setString(3, owner.getPid().toString());
            stmt.setString(4, getId().toString());
            setter.set(stmt, val, 5);
            stmt.setInt(6, getIsUpdateAuditOn() ? 1 : 0);
            stmt.setString(7, getIsUpdateAuditOn() ? owner.getAuditSchemeId() : null);
            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseError("Can't execute merge statement", e);
        }
    }

    @Override
    public String getDbName() {
        return null;
    }

    @Override
    public boolean isGeneratedInDb() {
        return false;
    }

    /**
     * @return the ownerEntityId
     */
    public Id getOwnerEntityId() {
        return ownerEntityId;
    }

    /**
     * @return the isUpdateAuditOn
     */
    public boolean getIsUpdateAuditOn() {
        return isUpdateAuditOn;
    }

    private interface SimpleStatementProcessor {

        String getStmtKey();

        String getQuery();

        ResultSet execThisQuery(PreparedStatement stmt) throws SQLException;
    }

    private interface ValueSetter {

        abstract void set(PreparedStatement stmt, Object o, int paramIdx) throws SQLException;
    }
}
