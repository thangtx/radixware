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
package org.radixware.kernel.designer.dds.script.defs.postgresenterprise;

import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsModule;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.defs.dds.DdsTypeDef;
import org.radixware.kernel.common.defs.dds.utils.DbTypeUtils;
import org.radixware.kernel.common.defs.value.RadixDefaultValue;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.ERadixDefaultValueChoice;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.common.utils.ValueToSqlConverter;
import org.radixware.kernel.designer.dds.script.DdsScriptGeneratorUtils;
import org.radixware.kernel.designer.dds.script.IDdsDefinitionScriptGenerator;
import org.radixware.kernel.designer.dds.script.IScriptGenerationHandler;
import org.radixware.kernel.designer.dds.script.DdsScriptInternalUtils;

public class EdbDdsColumnScriptGenerator implements IDdsDefinitionScriptGenerator<DdsColumnDef> {
    public static final String NUMBER = "NUMBER";
    public static final String CHAR = "CHAR";
    public static final String DATE = "DATE";
    public static final String VARCHAR2 = "VARCHAR2";
    public static final String TIMESTAMP = "TIMESTAMP";
    public static final String RAW = "RAW";
    public static final String CLOB = "CLOB";
    public static final String BLOB = "BLOB";

    protected EdbDdsColumnScriptGenerator() {
    }

    @Override
    public void getDropScript(CodePrinter cp, DdsColumnDef column) {
        cp.print("alter table ").print(column.getOwnerTable().getDbName()).print(" drop column ").print(DdsScriptInternalUtils.escapeDbName(column.getDbName())).printCommandSeparator();
    }

    @Override
    public boolean isModifiedToDrop(DdsColumnDef oldColumn, DdsColumnDef newColumn) {
        DdsTableDef oldTbl = oldColumn.getOwnerTable();
        DdsTableDef newTbl = newColumn.getOwnerTable();
        EdbDdsTableScriptGenerator tableScriptGenerator = EdbDdsTableScriptGenerator.Factory.newInstance();
        if (tableScriptGenerator.isModifiedToDrop(oldTbl, newTbl)) {
            return true;
        }

        Sqml oldExpression = oldColumn.getExpression();
        Sqml newExpression = newColumn.getExpression();
        if (!DdsScriptGeneratorUtils.isTranslatedSqmlEquals(oldExpression, newExpression)) {
            return true;
        }

        if (newExpression != null) {
            boolean oldNotNull = oldColumn.isNotNull();
            boolean newNotNull = newColumn.isNotNull();
            if (oldNotNull != newNotNull) {
                return true;
            }

            String oldDbType = buildDbType(oldColumn);
            String newDbType = buildDbType(newColumn);
            if (!DbTypeUtils.isDbTypeConvertable(oldDbType, newDbType)) {
                return true;
            }
        }

        return false;
    }
    private String translateDefaultValue(DdsColumnDef column) {
        RadixDefaultValue defaultValue = column.getDefaultValue();
        if (defaultValue != null) {
            ERadixDefaultValueChoice choice = defaultValue.getChoice();
            switch (choice) {
                case DATE_TIME:
                    return "current_date";
                case EXACT_DATE_TIME:
                    return "current_timestamp";
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

    public void getColumnScript(CodePrinter cp, DdsColumnDef column) {
        cp.print(DdsScriptInternalUtils.escapeDbName(column.getDbName())).print(' ').print(buildDbType(column));
        
/*        Sqml expression = column.getExpression();
        if (expression != null) {
            cp.print(" as (");
            DdsScriptGeneratorUtils.translateSqml(cp, expression);
            cp.print(")");
//            throw new UnsupportedOperationException("Expressilns are not supported in the columns");
        }
*/
        String defaultValueAsSql = translateDefaultValue(column);
        if (defaultValueAsSql != null) {
            cp.print(" default ").print(defaultValueAsSql);
        }

        cp.print(column.isNotNull() ? " not null" : " null");
    }

    private static void getRenameScript(CodePrinter cp, String tableDbName, String oldDbName, String newDbName) {
        cp.print("alter table ").print(tableDbName).print(" rename column ").print(oldDbName).print(" to ").print(newDbName).printCommandSeparator();
    }

    @Override
    public void getCreateScript(CodePrinter cp, DdsColumnDef column, IScriptGenerationHandler handler) {
        if (handler != null) {
            handler.onGenerationStarted(column, cp);
        }

        DdsTableDef table = column.getOwnerTable();
        cp.print("alter table ").println(table.getDbName()).print('\t').print("add ");
        getColumnScript(cp, column);
        cp.printCommandSeparator();
    }

    @Override
    public void getAlterScript(CodePrinter cp, DdsColumnDef oldColumn, DdsColumnDef newColumn) {
        final DdsTableDef table = newColumn.getOwnerTable();
        final String tableDbName = table.getDbName();

        final String oldDbName = DdsScriptInternalUtils.escapeDbName(oldColumn.getDbName());
        final String newDbName = DdsScriptInternalUtils.escapeDbName(newColumn.getDbName());

        final String oldDbType = buildDbType(oldColumn);
        final String newDbType = buildDbType(newColumn);
        final boolean dbTypeChanged = !DbTypeUtils.isDbTypeEquals(oldDbType, newDbType);

        // convert CLOB to varchar2 or any type to any
        if (dbTypeChanged && !DbTypeUtils.isDbTypeConvertable(oldDbType, newDbType) && !newColumn.isExpression()) {
            if ("CLOB".equals(oldDbType.toUpperCase()) && newDbType.toUpperCase().startsWith("VARCHAR2")) {
                final DdsColumnDef tmpColumn = newColumn.getClipboardSupport().copy();
                tmpColumn.setNotNull(false);
                final String tmpDbName = "TMP_CLOB_TO_VARCHAR2_";
                tmpColumn.setDbName(tmpDbName);

                cp.print("alter table ").print(tableDbName).print(" add ");
                getColumnScript(cp, tmpColumn);
                cp.printCommandSeparator();

                cp.print("update " + tableDbName + " set " + tmpDbName + " = dbms_lob.substr(" + oldDbName + ", 4001, 1)");
                cp.printCommandSeparator();

                getDropScript(cp, oldColumn);

                getRenameScript(cp, tableDbName, tmpDbName, newDbName);

                if (newColumn.isNotNull()) {
                    cp.print("alter table ").print(tableDbName).print(" modify ( ").print(newDbName).print(" not null)").printCommandSeparator();
                }

                return;
            } else if ("CLOB".equals(newDbType.toUpperCase()) && oldDbType.toUpperCase().startsWith("VARCHAR2")) {//RADIX-5703
                final DdsColumnDef tmpColumn = newColumn.getClipboardSupport().copy();
                final String tmpDbName = "TMP_VARCHAR2_TO_CLOB";
                tmpColumn.setDbName(tmpDbName);
                tmpColumn.setNotNull(false);
                cp.print("alter table ").print(tableDbName).print(" add ");
                getColumnScript(cp, tmpColumn);
                cp.printCommandSeparator();

                cp.print("update ").print(tableDbName).print(" set ").print(tmpDbName).print(" = ").print(oldDbName).printCommandSeparator();

                getDropScript(cp, oldColumn);

                getRenameScript(cp, tableDbName, tmpDbName, newDbName);

                if (newColumn.isNotNull()) {
                    cp.print("alter table ").print(tableDbName).print(" modify ( ").print(newDbName).print(" not null)").printCommandSeparator();
                }

                return;
            } else {
                final DdsColumnDef tmpColumn = newColumn.getClipboardSupport().copy();
                final String tmpDbName = "TMP_ANYTYPE_TO_ANYTYPE";
                tmpColumn.setDbName(tmpDbName);
                tmpColumn.setNotNull(false);
                cp.print("alter table ").print(tableDbName).print(" add ");
                getColumnScript(cp, tmpColumn);
                cp.printCommandSeparator();

                cp.print("update ").print(tableDbName).print(" set ").print(tmpDbName).print(" = ").print(oldDbName);
                cp.print(" -- modify this query for correct types conversion").printCommandSeparator();

                getDropScript(cp, oldColumn);

                getRenameScript(cp, tableDbName, tmpDbName, newDbName);

                if (newColumn.isNotNull()) {
                    cp.print("alter table ").print(tableDbName).print(" modify ( ").print(newDbName).print(" not null)").printCommandSeparator();
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
                cp.print(' ').print("default ").print(newDefaultValueAsSql != null ? newDefaultValueAsSql : "null");
            }

            if (mandatoryChanged) {
                cp.print(' ').print(newColumn.isNotNull() ? "not null" : "null");
            }

            cp.print(")").printCommandSeparator();
        }
    }

    @Override
    public void getRunRoleScript(CodePrinter printer, DdsColumnDef definition) {

    }

    @Override
    public void getReCreateScript(CodePrinter printer, DdsColumnDef definition, boolean storeData) {
    }

    @Override
    public void getEnableDisableScript(CodePrinter cp, DdsColumnDef definition, boolean enable) {
    }

    public static final class Factory {

        private Factory() {
        }

        public static EdbDdsColumnScriptGenerator newInstance() {
            return new EdbDdsColumnScriptGenerator();
        }
    }

    public static String buildDbType(final DdsColumnDef column) {
        return buildDbType(column.getValType(),column.getLength(),column.getPrecision(),column.getNativeDbTypeId(),column.getModule());
    }
    
    public static String buildDbType(EValType valType, int length, int precision, Id nativeDbTypeId, DdsModule context) {
        switch (valType) {
            case BOOL: return NUMBER + "(1,0)";
            case CHAR: return CHAR + "(1)";
            case DATE_TIME: return TIMESTAMP + "(" + String.valueOf(precision) + ")";
            case INT: return length > 0 ? NUMBER + "(" + String.valueOf(length) + ",0)" : NUMBER + "(*,0)";
            case NUM:
                if (length > 0 && precision > 0) {
                    return NUMBER + "(" + String.valueOf(length) + "," + String.valueOf(precision) + ")";
                } else if (length > 0) {
                    return NUMBER + "(" + String.valueOf(length) + ")";
                } else if (precision > 0) {
                    return NUMBER + "(*," + String.valueOf(precision) + ")";
                } else {
                    return NUMBER;
                }
            case BLOB: return BLOB;
            case BIN: return RAW + "(" + String.valueOf(length) + ")";
            case STR: return VARCHAR2 + "(" + String.valueOf(length) + ")";
            case CLOB: return CLOB;
            case ARR_BLOB:
            case ARR_BIN:
            case ARR_BOOL:
            case ARR_CLOB:
            case ARR_CHAR:
            case ARR_DATE_TIME:
            case ARR_INT:
            case ARR_NUM:
            case ARR_STR:
            case ARR_REF: return length > 0 ? VARCHAR2 + "(" + String.valueOf(length) + ")" : CLOB;
            case NATIVE_DB_TYPE:
                if (context == null) {
                    return "";
                }
                final DdsTypeDef nativeDbType = context.getDdsTypeSearcher().findById(nativeDbTypeId).get();
                return (nativeDbType != null ? nativeDbType.getDbName() : "");
            default: return "";
        }
    }
}
