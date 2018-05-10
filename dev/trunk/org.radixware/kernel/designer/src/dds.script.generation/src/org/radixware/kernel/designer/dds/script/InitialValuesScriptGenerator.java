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

package org.radixware.kernel.designer.dds.script;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsIndexDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EDatabaseType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.common.utils.ValueToSqlConverter;

/**
 * Tables initial values script generator.
 *
 */
class InitialValuesScriptGenerator {

    private static int calcSize(DdsTableDef table) {
        if (table != null) {
            final DdsIndexDef pk = table.getPrimaryKey();
            if (pk.getColumnsInfo().size() > 0) {
                final DdsColumnDef firstPkColumn = pk.getColumnsInfo().get(0).findColumn();
                if (firstPkColumn != null) {
                    return firstPkColumn.getInitialValues().size();
                }
            }
        }
        return 0;
    }

    private static void check(DdsTableDef table, final boolean isOld) {
        final int requiredSize = calcSize(table);
        
        if (requiredSize > 0) {
            for (DdsColumnDef column : table.getColumns().get(EScope.LOCAL_AND_OVERWRITE)) { // по всем, т.к. всё должно совпадать
                if (column.isGeneratedInDb()) {
                    final int size = column.getInitialValues().size();
                    
                    if (size > 0) {
                        if (size != requiredSize 
                                && !isOld // RADIX-14803
                                ) {
                            throw new DefinitionError("Erronerous initial values sizes.", table);
                        }
                        if (column.isPrimaryKey() && column.getInitialValues().contains(null)) {
                            throw new DefinitionError("Initial values contains NULL for primary key.", table);
                        }
                    } else if (column.isPrimaryKey()) {
                        throw new DefinitionError("Initial values contains no values for primary key.", table);
                    }
                }
            }
            if (table.getPrimaryKey().getColumnsInfo().size() == 0) {
                throw new DefinitionError("Erronerous primary key.", table);
            }
        }
    }

    private static boolean isPkStructureEquals(final DdsIndexDef oldPk, final DdsIndexDef newPk) {
        final int size = oldPk.getColumnsInfo().size();
        
        if (newPk.getColumnsInfo().size() != size) {
            return false;
        }
        else {
            for (int i = 0; i < size; i++) {
                if (!oldPk.getColumnsInfo().get(i).getColumnId().equals(newPk.getColumnsInfo().get(i).getColumnId())) {
                    return false;
                }
            }
            return true;
        }
    }

    private static class Key extends ArrayList<ValAsStr> {
        boolean used = false;
        final int idx;

        public Key(DdsTableDef table, int idx) {
            super(table.getPrimaryKey().getColumnsInfo().size());
            this.idx = idx;
            for (DdsIndexDef.ColumnInfo pkPolumnInfo : table.getPrimaryKey().getColumnsInfo()) {
                DdsColumnDef column = pkPolumnInfo.getColumn();
                this.add(column.getInitialValues().get(idx));
            }
        }
    }

    private static List<Key> getKeys(DdsTableDef table) {
        final int size = calcSize(table);
        
        if (size > 0) {
            final List<Key> keys = new ArrayList<>(size);
            
            for (int idx = 0; idx < size; idx++) {
                keys.add(new Key(table,idx));
            }
            return keys;
        } else {
            return null;
        }
    }

    private static HashMap<Key, Key> getKeysMap(DdsTableDef table, List<Key> keys) {
        if (keys != null) {
            final HashMap<Key, Key> keysMap = new HashMap<>(keys.size());
            for (Key key : keys) {
                if (keysMap.containsKey(key)) {
                    throw new DefinitionError("Duplicated initial value: " + key.toString() + ".", table);
                }
                keysMap.put(key, key);
            }
            return keysMap;
        } else {
            return null;
        }
    }

    private static String extractValue(final DdsColumnDef column, final int idx) {
        final ValAsStr valAsStr = column.getInitialValues().get(idx);
        final EValType valType = column.getValType();
        return ValueToSqlConverter.toSql(valAsStr, valType);
    }

    /**
     * @param newPk - it is needed to get new column names in db.
     */
    private static String buildKeyCondition(DdsTableDef table, int idx, DdsIndexDef newPk) {
        final StringBuilder sb = new StringBuilder();
        String prefix = "where ";
        int pkColumnIdx = 0;
        
        for (DdsIndexDef.ColumnInfo pkPolumnInfo : table.getPrimaryKey().getColumnsInfo()) {
            final DdsColumnDef column = pkPolumnInfo.getColumn();
            final String columnNewDbName = newPk.getColumnsInfo().get(pkColumnIdx).getColumn().getDbName();
            
            sb.append(prefix).append(columnNewDbName).append('=').append(extractValue(column, idx));
            prefix = " and ";
            pkColumnIdx++;
        }
        return sb.toString();                
    }

    private static void getDeleteScript(CodePrinter cp, DdsTableDef table, int idx, DdsIndexDef newPk) {
        cp.print("delete from ").print(table.getDbName()).print(' ');
        cp.print(buildKeyCondition(table, idx, newPk)).printCommandSeparator();
    }

    private static void getInsertScript(CodePrinter cp, DdsTableDef table, int idx) {
        final CodePrinter values = CodePrinter.Factory.newSqlPrinter();
        boolean columnPrintedFlag = false;
        
        cp.print("insert into ").print(table.getDbName()).print(" (");
        for (DdsColumnDef column : table.getColumns().getLocal()) { // первичный ключ может быть только в самой базовой таблице, поэтому insert для не базовой не вызовется
            if (column.isGeneratedInDb() && column.getInitialValues().size() > 0) {
                if (columnPrintedFlag) {
                    cp.print(", ");
                    values.print(",\n\t");
                } else {
                    columnPrintedFlag = true;
                    values.print("\n\t");
                }
                if (cp.getProperty(CodePrinter.DATABASE_TYPE) == EDatabaseType.ENTERPRISEDB) {
                    cp.print(DdsScriptInternalUtils.escapeDbName(column.getDbName()));
                }
                else {
                    cp.print(column.getDbName());
                }
                final ValAsStr valAsStr = column.getInitialValues().get(idx);
                final EValType valType = column.getValType();
                values.print(ValueToSqlConverter.toSql(valAsStr, valType));
            }
        }

        cp.print(") values (").print(values.toString()).print(")").printCommandSeparator();
    }

    private static void getUpdateScript(CodePrinter cp, DdsTableDef oldTable, int oldIdx, DdsTableDef newTable, int newIdx) {
        boolean printedFlag = false;
        for (DdsColumnDef newColumn : newTable.getColumns().getLocal()) {
            if (newColumn.isGeneratedInDb() && newColumn.getInitialValues().size() > 0 && !newColumn.isPrimaryKey()) {
                final ValAsStr newValue = newColumn.getInitialValues().get(newIdx);
                final DdsColumnDef oldColumn = oldTable.getColumns().findById(newColumn.getId(), EScope.LOCAL_AND_OVERWRITE).get();
                final ValAsStr oldValue = (oldColumn != null && oldColumn.getInitialValues().size() > 0 
                        && oldColumn.getInitialValues().size()>oldIdx//RADIX-14803
                        ? oldColumn.getInitialValues().get(oldIdx) : null);
                final boolean changed = !Utils.equals(oldValue, newValue);
                if (changed) {
                    if (!printedFlag) {
                        cp.print("update ").print(newTable.getDbName()).println(" set");
                        printedFlag = true;
                    } else {
                        cp.print(",\n");
                    }
                    cp.print('\t').print(newColumn.getDbName()).print('=').print(extractValue(newColumn, newIdx));
                }
            }
        }
        if (printedFlag) {
            DdsIndexDef newPk = newTable.getPrimaryKey();
            cp.println().print(buildKeyCondition(newTable, newIdx, newPk)).printCommandSeparator();
        }
    }

    public static void getAlterScript(final CodePrinter cp, final DdsTableDef oldTable, final DdsTableDef newTable) {
        //RADIX-14803 - add flag 'isOld' for check function
        check(oldTable, true);
        check(newTable, false);

        if (oldTable != null && newTable != null && calcSize(oldTable) > 0 && !isPkStructureEquals(oldTable.getPrimaryKey(), newTable.getPrimaryKey())) {
            return; // impossible to generate.
        }

        final List<Key> oldKeys = getKeys(oldTable);
        final List<Key> newKeys = getKeys(newTable);

        final HashMap<Key, Key> oldKeysMap = getKeysMap(oldTable, oldKeys);
        getKeysMap(newTable, newKeys); // check for duplication

        if (newKeys != null) {
            for (Key newKey : newKeys) {
                final Key oldKey = (oldKeysMap != null ? oldKeysMap.get(newKey) : null);
                if (oldKey != null) {
                    getUpdateScript(cp, oldTable, oldKey.idx, newTable, newKey.idx);
                    oldKey.used = true;
                } else {
                    getInsertScript(cp, newTable, newKey.idx);
                }
            }
        }

        if (oldKeys != null && newTable != null) {  // NPE!
            final DdsIndexDef newPk = newTable.getPrimaryKey();
            for (Key oldKey : oldKeys) {
                if (!oldKey.used) {
                    getDeleteScript(cp, oldTable, oldKey.idx, newPk);
                }
            }
        }
    }

    private static class InitialValuePair {

        public DdsTableDef oldTable;
        public DdsTableDef newTable;

        public InitialValuePair() {
            super();
        }

        public void add(DdsColumnDef oldColumn, DdsColumnDef newColumn) {
            if (oldTable == null && oldColumn != null) {
                oldTable = oldColumn.getOwnerTable();
            }
            if (newTable == null && newColumn != null) {
                newTable = newColumn.getOwnerTable();
            }
        }
    }

    public static void getAlterScript(final CodePrinter cp, final DefinitionPairs definitionPairs) {
        // сложности из-за того, что перекрытые таблицы отсутствуют в definitionPairs, присутствуют только их колонки
        final HashMap<Id, InitialValuePair> initialValuePairs = new HashMap<>();
        for (DefinitionPair definitionPair : definitionPairs) {
            final DdsColumnDef oldColumn = (definitionPair.getOldDefinition() instanceof DdsColumnDef ? (DdsColumnDef) definitionPair.getOldDefinition() : null);
            final DdsColumnDef newColumn = (definitionPair.getNewDefinition() instanceof DdsColumnDef ? (DdsColumnDef) definitionPair.getNewDefinition() : null);
            if (oldColumn != null || newColumn != null) {
                final Id tableId = (oldColumn != null ? oldColumn.getOwnerTable().getId() : newColumn.getOwnerTable().getId());
                InitialValuePair initialValuePair = initialValuePairs.get(tableId);
                if (initialValuePair == null) {
                    initialValuePair = new InitialValuePair();
                    initialValuePairs.put(tableId, initialValuePair);
                }
                initialValuePair.add(oldColumn, newColumn);
            }
        }

        final List<Map.Entry<Id, InitialValuePair>> values = new ArrayList<>(initialValuePairs.entrySet());
        Collections.sort(values, new Comparator<Map.Entry<Id, InitialValuePair>>() {

            @Override
            public int compare(Map.Entry<Id, InitialValuePair> o1, Map.Entry<Id, InitialValuePair> o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
        });
        
        for (final Map.Entry<Id, InitialValuePair> entry : values) {
            DdsTableDef oldTable = entry.getValue().oldTable;
            DdsTableDef newTable = entry.getValue().newTable;
            if (oldTable == null) {
                oldTable = newTable.findOverwritten();
            }
            if (newTable == null) {
                newTable = oldTable.findOverwritten();
            }
            getAlterScript(cp, oldTable, newTable);
        }
    }
}
