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
package org.radixware.kernel.designer.dds.script.defs;

import org.radixware.kernel.designer.dds.script.DdsScriptGeneratorUtils;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.defs.dds.utils.DbTypeUtils;
import org.radixware.kernel.common.defs.value.RadixDefaultValue;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.ERadixDefaultValueChoice;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.common.utils.ValueToSqlConverter;
import org.radixware.kernel.designer.dds.script.IDdsDefinitionScriptGenerator;
import org.radixware.kernel.designer.dds.script.IScriptGenerationHandler;

public class DdsColumnScriptGenerator implements IDdsDefinitionScriptGenerator<DdsColumnDef> {

    protected DdsColumnScriptGenerator() {
    }

    @Override
    public void getDropScript(final CodePrinter cp, final DdsColumnDef column) {
        if (cp == null) {
            throw new IllegalArgumentException("Code printer can't be null");
        }
        else if (column == null) {
            throw new IllegalArgumentException("Column description can't be null");
        }
        else {
            final DdsTableDef table = column.getOwnerTable();
            cp.print("alter table ").print(table.getDbName()).print(" drop column ").print(column.getDbName()).printCommandSeparator();
        }
    }

    @Override
    public boolean isModifiedToDrop(DdsColumnDef oldColumn, DdsColumnDef newColumn) {
        if (oldColumn == null) {
            throw new IllegalArgumentException("Old column can't be null");
        }
        else if (newColumn == null) {
            throw new IllegalArgumentException("New column can't be null");
        }
        else {
            final DdsTableDef oldTbl = oldColumn.getOwnerTable();
            final DdsTableDef newTbl = newColumn.getOwnerTable();
            final DdsTableScriptGenerator tableScriptGenerator = DdsTableScriptGenerator.Factory.newInstance();
            
            if (tableScriptGenerator.isModifiedToDrop(oldTbl, newTbl)) {
                return true;
            }

            final Sqml oldExpression = oldColumn.getExpression();
            final Sqml newExpression = newColumn.getExpression();
            
            if (!DdsScriptGeneratorUtils.isTranslatedSqmlEquals(oldExpression, newExpression)) {
                return true;
            }

            if (newExpression != null) {
                final boolean oldNotNull = oldColumn.isNotNull();
                final boolean newNotNull = newColumn.isNotNull();
                
                if (oldNotNull != newNotNull) {
                    return true;
                }

                final String oldDbType = oldColumn.getDbType();
                final String newDbType = newColumn.getDbType();
                
                if (!DbTypeUtils.isDbTypeConvertable(oldDbType, newDbType)) {
                    return true;
                }
            }

            return false;
        }
    }

    private String translateDefaultValue(DdsColumnDef column) {
        RadixDefaultValue defaultValue = column.getDefaultValue();
        if (defaultValue != null) {
            ERadixDefaultValueChoice choice = defaultValue.getChoice();
            switch (choice) {
                case DATE_TIME:
                    return "SYSDATE";
                case EXACT_DATE_TIME:
                    return "SYSTIMESTAMP";
                case VAL_AS_STR:
                    ValAsStr valAsStr = defaultValue.getValAsStr();
                    EValType valType = column.getValType();
                    String sql = ValueToSqlConverter.toSql(valAsStr, valType);
                    return sql;
                case EXPRESSION:
                    return defaultValue.getExpression();
                default:
                    throw new DefinitionError("Illegal default value choice '" + String.valueOf(choice) + "'.", column);
            }
        } else {
            return null;
        }
    }

    public void getColumnScript(final CodePrinter cp, final DdsColumnDef column) {
        if (cp == null) {
            throw new IllegalArgumentException("Code printer can't be null");
        }
        else if (column == null) {
            throw new IllegalArgumentException("Column description can't be null");
        }
        else {
            cp.print(column.getDbName()).print(' ').print(column.getDbType());

            final Sqml expression = column.getExpression();
            
            if (expression != null) {
                cp.print(" as (");
                DdsScriptGeneratorUtils.translateSqml(cp, expression);
                cp.print(")");
            }

            final String defaultValueAsSql = translateDefaultValue(column);
            
            if (defaultValueAsSql != null) {
                cp.print(" default ").print(defaultValueAsSql);
            }

            cp.print(column.isNotNull() ? " not null" : " null");
        }
    }

    private static void getRenameScript(final CodePrinter cp, final String tableDbName, final String oldDbName, final String newDbName) {
        cp.print("alter table ").print(tableDbName).print(" rename column ").print(oldDbName).print(" to ").print(newDbName).printCommandSeparator();
    }

    @Override
    public void getCreateScript(final CodePrinter cp, final DdsColumnDef column, final IScriptGenerationHandler handler) {
        if (cp == null) {
            throw new IllegalArgumentException("Code printer can't be null");
        }
        else if (column == null) {
            throw new IllegalArgumentException("Column description can't be null");
        }
        else {
            if (handler != null) {
                handler.onGenerationStarted(column, cp);
            }

            final DdsTableDef table = column.getOwnerTable();
            
            cp.print("alter table ").println(table.getDbName()).print("\tadd ");
            getColumnScript(cp, column);
            cp.printCommandSeparator();
        }
    }

    @Override
    public void getAlterScript(CodePrinter cp, DdsColumnDef oldColumn, DdsColumnDef newColumn) {
        if (cp == null) {
            throw new IllegalArgumentException("Code printer can't be null");
        }
        else if (oldColumn == null) {
            throw new IllegalArgumentException("Column description can't be null");
        }
        else if (newColumn == null) {
            throw new IllegalArgumentException("Column description can't be null");
        }
        else {
            final DdsTableDef table = newColumn.getOwnerTable();
            final String tableDbName = table.getDbName();

            final String oldDbName = oldColumn.getDbName();
            final String newDbName = newColumn.getDbName();

            final String oldDbType = oldColumn.getDbType();
            final String newDbType = newColumn.getDbType();
            final boolean dbTypeChanged = !DbTypeUtils.isDbTypeEquals(oldDbType, newDbType);

            // convert CLOB to varchar2 or any type to any
            if (dbTypeChanged && !DbTypeUtils.isDbTypeConvertable(oldDbType, newDbType) && !newColumn.isExpression()) {
                if ("CLOB".equals(oldDbType.toUpperCase()) && newDbType.toUpperCase().startsWith("VARCHAR2")) {
                    final DdsColumnDef tmpColumn = newColumn.getClipboardSupport().copy();
                    tmpColumn.setNotNull(false);
                    final String tmpDbName = "TMP_CLOB_TO_VARCHAR2_";
                    tmpColumn.setDbName(tmpDbName);

                    cp.print("alter table " + tableDbName + " add ");
                    getColumnScript(cp, tmpColumn);
                    cp.printCommandSeparator();

                    cp.print("update " + tableDbName + " set " + tmpDbName + " = dbms_lob.substr(" + oldDbName + ", 4001, 1)").printCommandSeparator();

                    getDropScript(cp, oldColumn);
                    getRenameScript(cp, tableDbName, tmpDbName, newDbName);

                    if (newColumn.isNotNull()) {
                        cp.print("alter table " + tableDbName + " modify ( " + newDbName + " not null)").printCommandSeparator();
                    }

                    return;
                } else if ("CLOB".equals(newDbType.toUpperCase()) && oldDbType.toUpperCase().startsWith("VARCHAR2")) {//RADIX-5703
                    final DdsColumnDef tmpColumn = newColumn.getClipboardSupport().copy();
                    final String tmpDbName = "TMP_VARCHAR2_TO_CLOB";
                    tmpColumn.setDbName(tmpDbName);
                    tmpColumn.setNotNull(false);

                    cp.print("alter table " + tableDbName + " add ");
                    getColumnScript(cp, tmpColumn);
                    cp.printCommandSeparator();

                    cp.print("update " + tableDbName + " set " + tmpDbName + " = " + oldDbName).printCommandSeparator();

                    getDropScript(cp, oldColumn);
                    getRenameScript(cp, tableDbName, tmpDbName, newDbName);

                    if (newColumn.isNotNull()) {
                        cp.print("alter table " + tableDbName + " modify ( " + newDbName + " not null)").printCommandSeparator();
                    }

                    return;
                } else {
                    final DdsColumnDef tmpColumn = newColumn.getClipboardSupport().copy();
                    final String tmpDbName = "TMP_ANYTYPE_TO_ANYTYPE";
                    tmpColumn.setDbName(tmpDbName);
                    tmpColumn.setNotNull(false);

                    cp.print("alter table " + tableDbName + " add ");
                    getColumnScript(cp, tmpColumn);
                    cp.printCommandSeparator();

                    cp.print("update " + tableDbName + " set " + tmpDbName + " = " + oldDbName).print(" -- modify this query for correct types conversion").printCommandSeparator();

                    getDropScript(cp, oldColumn);
                    getRenameScript(cp, tableDbName, tmpDbName, newDbName);

                    if (newColumn.isNotNull()) {
                        cp.print("alter table " + tableDbName + " modify ( " + newDbName + " not null)").printCommandSeparator();
                    }

                    return;
                }
            }

            if (!oldDbName.equals(newDbName)) {
                getRenameScript(cp, tableDbName, oldDbName, newDbName);
            }

            final boolean oldNotNull = oldColumn.isNotNull();
            final boolean newNotNull = newColumn.isNotNull();
            final boolean mandatoryChanged = (oldNotNull != newNotNull);

            final String oldDefaultValueAsSql = translateDefaultValue(oldColumn);
            final String newDefaultValueAsSql = translateDefaultValue(newColumn);
            final boolean defaultValueChanged = !Utils.equals(oldDefaultValueAsSql, newDefaultValueAsSql);

            if (mandatoryChanged || dbTypeChanged || defaultValueChanged) {
                if (dbTypeChanged) {
                    //check if need to generate tuncate script
                    int[] oldDbTypeLen = DbTypeUtils.getDbTypeLength(oldDbType);
                    int[] newDbTypeLen = DbTypeUtils.getDbTypeLength(newDbType);
                    boolean needUpdate = false;
                    if (oldDbTypeLen.length != newDbTypeLen.length) {
                        needUpdate = true;
                    } else {
                        for (int i = 0; i < oldDbTypeLen.length; i++) {
                            if (oldDbTypeLen[i] > newDbTypeLen[i]) {
                                needUpdate = true;
                                break;
                            }
                        }
                    }
                    if (needUpdate) {
                        cp.print("-- update ").print(tableDbName).print(" set ").print(oldDbName);

                        if (newColumn.getValType() == EValType.STR) {
                            cp.print(" = substr(").print(oldDbName).print(", 1, ");
                        } else {
                            cp.print(" = trunc(").print(oldDbName);
                        }

                        cp.print(", ");
                        if (newDbTypeLen.length > 0) {
                            cp.print(newDbTypeLen[0]);
                        } else {
                            cp.print("0");
                        }
                        cp.println(") -- modify this query for correct types conversion");
                    }
                }

                cp.print("alter table ").println(tableDbName).print("\tmodify (").print(newDbName);

                if (dbTypeChanged) {
                    cp.print(' ').print(newDbType);
                }

                if (defaultValueChanged) {
                    cp.print(" default ").print(newDefaultValueAsSql != null ? newDefaultValueAsSql : "null");
                }

                if (mandatoryChanged) {
                    cp.print(' ').print(newColumn.isNotNull() ? "not null" : "null");
                }

                cp.print(")").printCommandSeparator();
            }
        }
    }

    @Override
    public void getRunRoleScript(final CodePrinter printer, final DdsColumnDef definition) {
        if (printer == null) {
            throw new IllegalArgumentException("Code printer can't be null");
        }
        else if (definition == null) {
            throw new IllegalArgumentException("Column description can't be null");
        }
    }

    @Override
    public void getReCreateScript(final CodePrinter printer, final DdsColumnDef definition, final boolean storeData) {
        if (printer == null) {
            throw new IllegalArgumentException("Code printer can't be null");
        }
        else if (definition == null) {
            throw new IllegalArgumentException("Column description can't be null");
        }
    }

    @Override
    public void getEnableDisableScript(CodePrinter cp, DdsColumnDef definition, boolean enable) {
        if (cp == null) {
            throw new IllegalArgumentException("Code printer can't be null");
        }
        else if (definition == null) {
            throw new IllegalArgumentException("Column description can't be null");
        }
    }

    public static final class Factory {

        private Factory() {
        }

        public static DdsColumnScriptGenerator newInstance() {
            return new DdsColumnScriptGenerator();
        }
    }
}
