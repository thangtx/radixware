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

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.defs.dds.DdsIndexDef.ColumnInfo;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.WrongFormatError;
import org.radixware.kernel.common.types.IKernelEnum;
import org.radixware.kernel.common.types.IKernelIntEnum;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.common.utils.ValueFormatter;
import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.server.dbq.ObjectExistsQuery;
import org.radixware.kernel.server.exceptions.EntityObjectNotExistsError;
import org.radixware.kernel.server.exceptions.WrongPidFormatError;

public class Pid extends org.radixware.kernel.common.types.Pid {
    
    public static enum EEmptyPkPolicy {
        RESTORE_VIA_SK,
        CREATE_NULL_OBJ
    }    

    private final Arte arte;
    private DdsTableDef table;
    private List<Object> pkVals;
    
    //Published methods
    //Constructors    
    /**
     * Create "Null object". Is used in new entity getPid() if primary key is
     * empty. Different "Null objects" are not equal.
     *
     * @param table
     * @param asStr
     */
    Pid(final Arte arte, final DdsTableDef table) {
        super(table.getId(), table.getPrimaryKey().getColumnsInfo().size());        
        this.arte = arte;
    }

    public Pid(final Arte arte, final Id entityId, final String str) {
        this(arte, arte.getDefManager().getTableDef(entityId), str);
    }

    @Deprecated
    public Pid(final Arte arte, final Entity entity, final String str) {
        this(arte, entity.getDdsMeta(), str);
    }

    public Pid(final Arte arte, final DdsTableDef table, final String str) {
        super(table.getId(), str);
        if (str == null || str.length() == 0) {
            throw new WrongPidFormatError(table, str, "Wrong Pid string format. Pid string is \"" + String.valueOf(str) + "\"");
        }
        this.table = table;
        this.arte = arte;
        final List<String> valsAsStr = new ArrayList<>(3);
        int start = 0;
        int i = str.indexOf(PK_VAL_DELIMETER);
        while (i != -1) {
            if (i < 1 || str.charAt(i - 1) != '\\') {
                valsAsStr.add(str.substring(start, i));
                start = i + 1;
            }
            i = str.indexOf(PK_VAL_DELIMETER, i + 1);
        }
        if (start < str.length()) {
            valsAsStr.add(str.substring(start));
        } else {
            throw new WrongPidFormatError(table, str, "Wrong Pid string format. Pid string is \"" + String.valueOf(str) + "\"");
        }
        final int pkSize = table.getPrimaryKey().getColumnsInfo().size();
        if (valsAsStr.size() != pkSize) {
            throw new WrongPidFormatError(table, str, "Wrong Pid string format. Pid string is \"" + String.valueOf(str) + "\"");
        }
        pkVals = new ArrayList<>(pkSize);
        i = 0;
        final StringBuilder normalizedPidAsStr = new StringBuilder();
        try {
            for (ColumnInfo pkCol : table.getPrimaryKey().getColumnsInfo()) {
                if (i > 0) {
                    normalizedPidAsStr.append(PK_VAL_DELIMETER);
                }
                final String valAsStr = valsAsStr.get(i);
                final Object val = valFromStr(pkCol.getColumn().getValType(), valAsStr);
                appendVal2PidStr(normalizedPidAsStr, val, pkCol.getColumn().getPrecision());
                pkVals.add(val);
                if (valAsStr == null) {
                    setIsNullObject(true);
                }
                i++;
            }
        } catch (WrongFormatError error) {
            throw new WrongPidFormatError(table, str, error.getMessage());
        }
        pkVals = Collections.unmodifiableList(pkVals);
        setPidAsStr(normalizedPidAsStr.toString());
    }

    public Pid(final Arte arte, final Id entityId, final Map<Id, Object> key) {
        this(arte, arte.getDefManager().getTableDef(entityId), key);
    }

    @Deprecated
    public Pid(final Arte arte, final Entity entity, final Map<Id, Object> key) {
        this(arte, entity.getDdsMeta(), key);
    }

    public Pid(final Entity entity, final Map<Id, Object> key) {
        this(entity.getArte(), entity.getDdsMeta(), key);
    }

    public Pid(final Arte arte, final DdsTableDef table, final Map<Id, Object> key) {
        this(arte, table, key, EEmptyPkPolicy.RESTORE_VIA_SK);
    }

    public Pid(final Arte arte, final DdsTableDef table, final Map<Id, Object> key, final EEmptyPkPolicy emptyPkPolicy) {
        super(table.getId(), "");
        this.table = table;
        this.arte = arte;
        build(key, emptyPkPolicy);
    }

    public Pid(
            final Arte arte,
            final Id entityId,
            final Id propId1, final Object propVal1,
            final Id propId2, final Object propVal2,
            final Id propId3, final Object propVal3) {
        super(arte.getDefManager().getTableDef(entityId).getId(), "");
        table = arte.getDefManager().getTableDef(entityId);
        this.arte = arte;
        final Map<Id, Object> map = new HashMap<>(5);
        map.put(propId1, propVal1);
        if (propId2 != null) {
            map.put(propId2, propVal2);
        }
        if (propId3 != null) {
            map.put(propId3, propVal3);
        }
        build(map, EEmptyPkPolicy.RESTORE_VIA_SK);
    }

    public Pid(
            final Arte arte,
            final Id entityId,
            final Id propId1, final Object propVal1,
            final Id propId2, final Object propVal2) {
        super(arte.getDefManager().getTableDef(entityId).getId(), "");
        table = arte.getDefManager().getTableDef(entityId);
        this.arte = arte;
        final Map<Id, Object> map = new HashMap<>(3);
        map.put(propId1, propVal1);
        if (propId2 != null) {
            map.put(propId2, propVal2);
        }
        build(map, EEmptyPkPolicy.RESTORE_VIA_SK);
    }

    public Pid(final Arte arte, final Id entityId, final Id propId, final Object propVal) {
        super(arte.getDefManager().getTableDef(entityId).getId(), "");
        table = arte.getDefManager().getTableDef(entityId);
        this.arte = arte;
        final Map<Id, Object> map = new HashMap<>(1);
        map.put(propId, propVal);
        build(map, EEmptyPkPolicy.RESTORE_VIA_SK);
    }
    
    public Arte getArte() {
        return arte;
    }   

    public Id getEntityId() {
        return table.getId();
    }

    public DdsTableDef getTable() {
        return table;
    }

    public boolean isExistsInDb() {
        if (isNullObject()) {
            return false;
        }
        final ObjectExistsQuery q = getArte().getDefManager().getDbQueryBuilder().buildExsistsQuery(table);
        try {
            return q.isExists(this);
        } finally {
            q.free();
        }
    }

    public boolean isExistsInCache() {
        return getArte().getCache().isRegistered(this);
    }

    /**
     * Service method for ARTE developers
     *
     * @return
     */
    public List<Object> getPkVals() {
        return pkVals;
    }

    //private methods    
    private void build(Map<Id, Object> key, final EEmptyPkPolicy emptyPkPolicy) {
        key = normalizeKeyVals(key);
        final int pkSize = table.getPrimaryKey().getColumnsInfo().size();
        pkVals = new ArrayList<>(pkSize);
        boolean isPkEmpty = false;
        for (int i = 0; i < pkSize; i++) {
            final Id colId = table.getPrimaryKey().getColumnsInfo().get(i).getColumn().getId();
            pkVals.add(key.get(colId));
            if (pkVals.get(i) == null) {
                isPkEmpty = true;
            }
        }
        if (!isPkEmpty) {
            pkVals = Collections.unmodifiableList(pkVals);
        } else {
            if (emptyPkPolicy == EEmptyPkPolicy.RESTORE_VIA_SK) {
                try {
                    pkVals = getArte().getDefManager().getDbQueryBuilder().getPkVals(table, key);
                } catch (EntityObjectNotExistsError e) {
                    throw e;
                } catch (RuntimeException e) {
                    throw new IllegalArgumentException("Can't build PK using known object properties: " + ExceptionTextFormatter.getExceptionMess(e), e);
                }
            } else {
                setIsNullObject(true);
                pkVals = Collections.unmodifiableList(pkVals);
            }
        }

        if (!pkVals.isEmpty()) {
            final StringBuilder tmp = new StringBuilder();
            boolean isFirstVal = true;
            int i = 0;
            for (Object val : pkVals) {
                if (isFirstVal) {
                    isFirstVal = false;
                } else {
                    tmp.append(PK_VAL_DELIMETER);
                }
                appendVal2PidStr(tmp, val, table.getPrimaryKey().getColumnsInfo().get(i).getColumn().getPrecision());
                i++;
            }
            setPidAsStr(tmp.toString());
        } else {
            setPidAsStr("");
        }
    }

    private Map<Id, Object> normalizeKeyVals(final Map<Id, Object> key) {
        // разгребаем последствия атобоксинга
        final Map<Id, Object> res = new HashMap<>(key.size() * 2 + 1);
        for (Map.Entry<Id, Object> keyItem : key.entrySet()) {
            Object val = keyItem.getValue();
            if (val instanceof IKernelEnum) {
                val = ((IKernelEnum) val).getValue();
            }
            if (val instanceof Number) {
                if (!(val instanceof Long) && !(val instanceof BigDecimal)) {
                    if (val instanceof Double || val instanceof Float) {
                        val = new BigDecimal(((Number) val).doubleValue());
                    } else {
                        val = ((Number) val).longValue();
                    }
                }
            } else if (val instanceof Date) {
                if (!(val instanceof Timestamp)) {
                    val = new Timestamp(((Date) val).getTime());
                }
            }
            res.put(keyItem.getKey(), val);
        }
        return res;
    }
    
    private Object valFromStr(final EValType valType, final String valStr) {
        if (valStr == null || valStr.length() == 0) {
            return null;
        }
        if (valType.equals(EValType.INT)) {
            return Long.valueOf(valStr);
        } else if (valType.equals(EValType.NUM)) {
            return new BigDecimal(valStr);
        } else if (valType.equals(EValType.DATE_TIME)) {
            return Timestamp.valueOf(valStr);
        } else if (valType.equals(EValType.BOOL)) {
            if ("0".equals(valStr)) {
                return Boolean.FALSE;
            } else {
                return Boolean.TRUE;
            }
        } else if (valType.equals(EValType.STR) || valType.equals(EValType.CHAR)) {
            final StringBuilder res = new StringBuilder();
            for (int i = 0; i < valStr.length(); i++) {
                if (valStr.charAt(i) == '\\') {
                    i++;
                    if (i == valStr.length()) {
                        throw new WrongFormatError("Can't restore Pid from string: incomplete masked char", null);
                    }
                    switch (valStr.charAt(i)) {
                        case 'n':
                            res.append('\n');
                            break;
                        case 'r':
                            res.append('\r');
                            break;
                        case 't':
                            res.append('\t');
                            break;
                        case '\\':
                            res.append('\\');
                            break;
                        case PK_VAL_DELIMETER:
                            res.append(PK_VAL_DELIMETER);
                            break;
                        case ' ':
                            res.append(' ');
                            break;
                        default:
                            throw new WrongFormatError("Can't restore Pid from string: unsupported masked char '\\" + valStr.charAt(i) + "'", null);
                    }
                } else {
                    res.append(valStr.charAt(i));
                }
            }
            if (valType.equals(EValType.CHAR)) {
                if (res.length() == 0) {
                    return null;
                } else {
                    return res.charAt(0);
                }
            } else {
                return res.toString();
            }
        } else {
            throw new WrongFormatError("Can't restore Pid from string: unsupported primary key property type " + valType, null);
        }
    }
}
