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
package org.radixware.kernel.common.types;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.exceptions.WrongFormatError;
import org.radixware.kernel.common.utils.ValueConverter;

public class Pid {

    private int hashCode;
    private Id tableId;
    private String asStr;

    /**
     * Конструирует идентификатор сущности по его строковому представлению
     *
     * @param tableId - идентификатор таблицы, данные из которой предоставляет
     * сущность
     * @param str - строковое представление идентификатора
     */
    public Pid(final Id tableId, final String str) {
        this.tableId = tableId;
        this.asStr = str;
        int start = 0;        
    }

    /**
     * Конструирует идентификатор сущности по идентификатору таблицы и значениям
     * первичных ключей.
     *
     * @param tableId - идентификатор таблицы, данные которой предоставляет
     * сущность
     * @param key - массив значений первичных ключей.
     */
    public Pid(final Id tableId, final ArrayList<Object> key) {
        if (tableId.getPrefix() == EDefinitionIdPrefix.ADS_ENTITY_CLASS
                || tableId.getPrefix() == EDefinitionIdPrefix.ADS_APPLICATION_CLASS) {
            this.tableId = Id.Factory.changePrefix(tableId, EDefinitionIdPrefix.DDS_TABLE);
        } else {
            this.tableId = tableId;
        }
        if (key == null || key.isEmpty()) {
            throw new IllegalUsageError("key array shoud not be empty");
        }
        build(key);
    }

    /**
     * Конструирует идентификатор сущности по идентификатору таблицы и значениям
     * первичных ключей.
     *
     * @param tableId - идентификатор таблицы, данные которой предоставляет
     * сущность
     * @param propVal1 - значение ключа
     * @param propVal2 - значение ключа
     * @param propVal3 - значение ключа
     */
    public Pid(
            final Id tableId,
            final Object propVal1,
            final Object propVal2,
            final Object propVal3) {
        this.tableId = tableId;
        final ArrayList<Object> map = new ArrayList<Object>(5);
        map.add(propVal1);
        if (propVal2 != null) {
            map.add(propVal2);
        }
        if (propVal3 != null) {
            map.add(propVal3);
        }
        build(map);
    }

    /**
     * Конструирует идентификатор сущности по идентификатору таблицы и значениям
     * первичных ключей.
     *
     * @param tableId - идентификатор таблицы, данные которой предоставляет
     * сущность
     * @param propVal1 - значение ключа
     * @param propVal2 - значение ключа
     */
    public Pid(
            final Id tableId,
            final Object propVal1,
            final Object propVal2) {
        this.tableId = tableId;
        final ArrayList<Object> map = new ArrayList<Object>(3);
        map.add(propVal1);
        if (propVal2 != null) {
            map.add(propVal2);
        }
        build(map);
    }

    /**
     * Конструирует идентификатор сущности по идентификатору таблицы и значению
     * первичного ключа.
     *
     * @param entityId - идентификатор таблицы, данные которой предоставляет
     * сущность
     * @param propVal - значение ключа
     */
    public Pid(final Id entityId, final Object propVal) {
        if (entityId.getPrefix() == EDefinitionIdPrefix.ADS_ENTITY_CLASS
                || entityId.getPrefix() == EDefinitionIdPrefix.ADS_APPLICATION_CLASS) {
            this.tableId = Id.Factory.changePrefix(entityId, EDefinitionIdPrefix.DDS_TABLE);
        } else {
            tableId = entityId;
        }
        final ArrayList<Object> map = new ArrayList<Object>(1);
        map.add(propVal);
        build(map);
    }

    public Pid(Pid pid) {
        tableId = pid.tableId;
        asStr = pid.asStr;
        hashCode = pid.hashCode;
    }

    public static Pid fromStr(String asStrWithTableId) {
        int index = asStrWithTableId.indexOf("\n");
        if (index < 0) {
            return null;
        } else {
            String tableIdAsStr = asStrWithTableId.substring(0, index);
            String pidAsStr = asStrWithTableId.substring(index + 1);
            return new Pid(Id.Factory.loadFrom(tableIdAsStr), pidAsStr);
        }

    }

    /**
     * Возвращает идентификатор таблицы, данные которой представляет сущность.
     *
     * @return идентификатор таблицы.
     */
    public Id getTableId() {
        return tableId;
    }

    /**
     * Два идентификатора сущности считаются одинаковыми, когда совпадают их
     * строковые представления и идентификаторы их таблиц.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof Pid) {
            Pid p = (Pid) o;
            return tableId.equals(p.tableId) && asStr.equals(p.asStr);
        }

        return false;
    }

    /**
     * @return строка, содержащая идентификатор таблицы и строковое
     * предстваление идентификатора сущности.
     * @see #toString()
     */
    public String toStr() {
        return tableId.toString() + '\n' + asStr;
    }

    /**
     * @return строковое представление идентификатора сущности.
     */
    @Override
    public String toString() {
        return asStr;
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

//private methods
    private void build(ArrayList<Object> key) {
        key = normalizeKeyVals(key);

        if (!key.isEmpty()) {
            final StringBuilder tmp = new StringBuilder();
            boolean isFirstVal = true;
            for (Object val : key) {
                if (isFirstVal) {
                    isFirstVal = false;
                } else {
                    tmp.append('~');
                }
                appendVal2PidStr(tmp, val);
            }
            asStr = tmp.toString();
        } else {
            asStr = "";
        }
        hashCode = (tableId + asStr).hashCode();
    }

    private ArrayList<Object> normalizeKeyVals(final ArrayList<Object> key) {
        // разгребаем последствия атобоксинга
        final ArrayList<Object> res = new ArrayList<Object>(key.size() * 2 + 1);
        for (Object val : key) {
            if (val instanceof Number) {
                if (!(val instanceof Long)
                        && !(val instanceof BigDecimal)) {
                    if (val instanceof Double || val instanceof Float) {
                        val = new BigDecimal(((Number) val).doubleValue());
                    } else {
                        val = Long.valueOf(((Number) val).longValue());
                    }
                }
            } else if (val instanceof Date) {
                if (!(val instanceof Timestamp)) {
                    val = new Timestamp(((Date) val).getTime());
                }
            }
            res.add(val);
        }
        return res;
    }

    private void appendVal2PidStr(final StringBuilder pidStr, final Object val) {
        if (val == null) {
            return;
        }
        if (val instanceof Number || val instanceof IKernelIntEnum) {
            pidStr.append(val.toString());
        } else if (val instanceof Boolean) {
            pidStr.append(((Boolean) val).booleanValue() ? '1' : '0');
        } else if (val instanceof java.sql.Timestamp) {
            pidStr.append(ValueConverter.floraValueOf((java.sql.Timestamp) val));
        } else {//searching bad chars only in String fields
            String tmp = val.toString();
            for (int i = 0; i < tmp.length(); i++) {
                switch (tmp.charAt(i)) {
                    case '\\':
                        pidStr.append("\\\\");
                        break;
                    case '~':
                        pidStr.append("\\~");
                        break;
                    case '\n':
                        pidStr.append("\\n");
                        break;
                    case '\r':
                        pidStr.append("\\r");
                        break;
                    case '\t':
                        pidStr.append("\\t");
                        break;
                    case ' ':
                        pidStr.append("\\ ");
                        break;
                    default:
                        pidStr.append(tmp.charAt(i));
                }
                //pidStr.append(tmp);

            }
        }
    }
}
