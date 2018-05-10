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

package org.radixware.kernel.designer.dds.actualizer;


import org.radixware.kernel.common.enums.EDeleteMode;
import org.radixware.kernel.common.types.Id;
import java.sql.*;
import java.text.*;
import java.util.*;

import org.radixware.kernel.common.defs.*;
import org.radixware.kernel.common.defs.dds.*;
import org.radixware.kernel.common.defs.dds.providers.DdsVisitorProvider;
import org.radixware.kernel.common.defs.dds.providers.DdsVisitorProviderFactory;
import org.radixware.kernel.common.defs.value.*;
import org.radixware.kernel.common.enums.*;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.utils.RadixObjectsUtils;
import org.radixware.kernel.designer.dds.script.*;

public class Actualizer {

    private final IDbDriver driver;
    private final RadixObject scope;
    private final DdsModelDef reModel; // Reverse engineering container.
    //
    private final Map<String, DdsTableDef> dbName2Table = new HashMap<String, DdsTableDef>();
    private final List<DdsViewDef> dbName2View = new ArrayList<DdsViewDef>();
    private final List<DdsSequenceDef> dbName2Sequence = new ArrayList<DdsSequenceDef>();
    private final Map<String, DdsReferenceDef> dbName2Reference = new TreeMap<String, DdsReferenceDef>();

    public static final class Factory {

        private Factory() {
        }

        public static Actualizer newInstance(IDbDriver driver, RadixObject scope) {
            return new Actualizer(driver, scope);
        }
    }

    protected Actualizer(IDbDriver driver, RadixObject scope) {
        this.driver = driver;
        this.scope = scope;

        final DdsModule reModule = DdsModule.Factory.getDefault().newInstance("reModule");
        reModel = reModule.getModelManager().findModel();
    }

    private class Visitor implements IVisitor {

        @Override
        public void accept(final RadixObject radixObject) {

            if (radixObject instanceof DdsViewDef) {
                final DdsViewDef newView = (DdsViewDef) radixObject;
                if (newView.isGeneratedInDb()) {
                    dbName2View.add(newView.getClipboardSupport().copy());
                }
            } else if (radixObject instanceof DdsTableDef) {
                final DdsTableDef newTable = (DdsTableDef) radixObject;
                if (newTable.isGeneratedInDb()) {
                    final String tableDbName = newTable.getDbName();
                    final DdsTableDef addedTable = dbName2Table.get(tableDbName);
                    if (addedTable == null) {
                        dbName2Table.put(tableDbName, newTable.getClipboardSupport().copy());
                    } else {
                        // overwrite - add copies of other columns, indices, triggers
                        final Definitions<DdsColumnDef> addedColumns = addedTable.getColumns().getLocal();
                        for (DdsColumnDef newColumn : newTable.getColumns().getLocal()) {
                            addedColumns.add(newColumn.getClipboardSupport().copy());
                        }
                        final Definitions<DdsIndexDef> addedIndices = addedTable.getIndices().getLocal();
                        for (DdsIndexDef newIndex : newTable.getIndices().getLocal()) {
                            addedIndices.add(newIndex.getClipboardSupport().copy());
                        }
                        final Definitions<DdsTriggerDef> addedTriggers = addedTable.getTriggers().getLocal();
                        for (DdsTriggerDef newTrigger : newTable.getTriggers().getLocal()) {
                            addedTriggers.add(newTrigger.getClipboardSupport().copy());
                        }
                    }
                }
            } else if (radixObject instanceof DdsSequenceDef) {
                final DdsSequenceDef newSeq = (DdsSequenceDef) radixObject;
                //Sequences are always generated in database.
                dbName2Sequence.add(newSeq.getClipboardSupport().copy());
            } else if (radixObject instanceof DdsReferenceDef) {
                final DdsReferenceDef newRef = (DdsReferenceDef) radixObject;
                if (newRef.isGeneratedInDb()) {
                    dbName2Reference.put(newRef.getDbName(), newRef.getClipboardSupport().copy());
                }
            }
        }
    }

    private void copyPlSqlObjects() {
        // copy packages and types
        scope.visit(new IVisitor() {

            @Override
            public void accept(RadixObject radixObject) {
                final RadixObject copy = radixObject.getClipboardSupport().copy();
                if (copy instanceof DdsPackageDef) {
                    reModel.getPackages().add((DdsPackageDef) copy);
                } else {
                    reModel.getTypes().add((DdsTypeDef) copy);
                }
            }
        }, DdsVisitorProviderFactory.newPlSqlObjectProvider());

//        // copy triggers
//        scope.visit(new IVisitor() {
//
//            @Override
//            public void accept(RadixObject radixObject) {
//                final DdsTriggerDef sourceTrigger = (DdsTriggerDef) radixObject;
//                final DdsTableDef sourceTable = sourceTrigger.getOwnerTable();
//                final DdsTableDef reTable = reModel.getTables().findById(sourceTable.getId());
//                if (reTable != null) {
//                    final DdsTriggerDef copiedTrigger = sourceTrigger.getClipboardSupport().copy();
//                    reTable.getTriggers().getLocal().add(copiedTrigger);
//                }
//            }
//        }, DdsVisitorProviderFactory.newTriggerProvider());
    }

    public String getActualizationScript() {

        dbName2Table.clear();
        dbName2View.clear();
        dbName2Sequence.clear();
        dbName2Reference.clear();

        scope.visit(new Visitor(), DdsVisitorProviderFactory.newDdsModelItemVisitorProvider());

        try {
            initDbTrgCols();
            importTables();
            importViews();
            importSequences();
            importReferences();
        } catch (SQLException cause) {
            throw new RadixError("Unable to import definition for actualization."
                    + " Error code: " + cause.getErrorCode() + "."
                    + " SQL state: " + cause.getSQLState() + ".", cause);
        }

        copyPlSqlObjects();

        final ScriptGenerator scriptGenerator = ScriptGeneratorImpl.Factory.newAlterInstance(reModel, scope);
        final CodePrinter cp = CodePrinter.Factory.newSqlPrinter();
        scriptGenerator.generateModificationScript(cp);
        final String result = cp.toString();
        return result;
    }

    private class DbTrgCol {

        public String triggerName;
        public String columnName;
    }
    private final List<DbTrgCol> dbTrgCols = new ArrayList<DbTrgCol>();

    private void initDbTrgCols() throws SQLException {

        dbTrgCols.clear();
        IResult trgColsSet = driver.getTrgCols();
        try {
            while (trgColsSet.next()) {
                final DbTrgCol dbTrgCol = new DbTrgCol();
                dbTrgCol.triggerName = trgColsSet.getString("TRIGGER_NAME"); //may be null
                dbTrgCol.columnName = trgColsSet.getString("COLUMN_NAME"); //may be null

                if (dbTrgCol.triggerName != null && dbTrgCol.columnName != null) {
                    dbTrgCols.add(dbTrgCol);
                }
            }
        } finally {
            trgColsSet.close();
        }

        Collections.sort(dbTrgCols, new Comparator<DbTrgCol>() {

            @Override
            public int compare(DbTrgCol o1, DbTrgCol o2) {
                return o1.triggerName.compareTo(o2.triggerName);
            }
        });
    }

    private void importTables() throws SQLException {

        Iterator<Map.Entry<String, DdsTableDef>> iterDdsTables = dbName2Table.entrySet().iterator();
        while (iterDdsTables.hasNext()) {
            DdsTableDef ddsTable = iterDdsTables.next().getValue();
            String tableDbName = ddsTable.getDbName();

            IResult tableSet = driver.getTable(tableDbName);
            try {
                if (!tableSet.next()) {
                    iterDdsTables.remove();
                    continue;
                }

                reModel.getTables().add(ddsTable);

                String tablespace = tableSet.getString("TABLESPACE_NAME"); //may be null

                if (tablespace != null && !tablespace.equals("")) {
                    ddsTable.setTablespace(tablespace);
                } //else if (!dbAttributes.containsKey(EDdsTableAdditionalDbAttribute.PARTITION)) {
                //  dbAttributes.put(EDdsTableAdditionalDbAttribute.PARTITION, "!!! This Table Is Partitioned In Database !!!");
                // }

                String temporary = tableSet.getString("TEMPORARY");
                boolean isTemporary = "Y".equals(temporary);
                ddsTable.setGlobalTemporary(isTemporary);

                String duration = tableSet.getString("DURATION");
                boolean isOnCommitPreserveRows = isTemporary && "SYS$SESSION".equals(duration);
                ddsTable.setOnCommitPreserveRows(isOnCommitPreserveRows);
                //

                DdsIndexDef pkIndex = ddsTable.getPrimaryKey();
                String pkIndDbName = "#";

                IResult pkSet = driver.getPK(tableDbName);
                try {
                    if (pkSet.next()) {
                        pkIndDbName = pkSet.getString("INDEX_NAME"); //not null
                        IResult pkIndSet = driver.getPKIndex(pkIndDbName);
                        try {
                            pkIndSet.next();

                            pkIndex.setDbName(pkIndDbName);
                            pkIndex.setName(pkIndDbName);
                            pkIndex.setAutoDbName(false);
                            pkIndex.setGeneratedInDb(true);

                            String pkTablespace = pkIndSet.getString("TABLESPACE_NAME"); //may be null
                            if (!ddsTable.isGlobalTemporary() && pkTablespace != null && !pkTablespace.equals("")) {
                                pkIndex.setTablespaceDbName(pkTablespace);
                            }

                            EnumSet<DdsIndexDef.EDbOption> pkIndDbOptions = pkIndex.getDbOptions();

                            checkDbOption(pkIndDbOptions, DdsIndexDef.EDbOption.UNIQUE, pkIndSet.getString("UNIQUENESS") /*may be null*/, "UNIQUE");
                            checkDbOption(pkIndDbOptions, DdsIndexDef.EDbOption.INVISIBLE, pkIndSet.getString("VISIBILITY") /*may be null*/, "INVISIBLE");
                            checkDbOption(pkIndDbOptions, DdsIndexDef.EDbOption.BITMAP, pkIndSet.getString("INDEX_TYPE") /*may be null*/, "BITMAP");
                            checkDbOption(pkIndDbOptions, DdsIndexDef.EDbOption.LOCAL, pkIndSet.getString("PARTITIONED") /*may be null*/, "YES");
                            checkDbOption(pkIndDbOptions, DdsIndexDef.EDbOption.NOLOGGING, pkIndSet.getString("LOGGING") /*may be null*/, "NO");

                            String pkDbName = pkSet.getString("CONSTRAINT_NAME"); //not null

                            DdsUniqueConstraintDef pkUniqConstr = pkIndex.getUniqueConstraint();
                            pkUniqConstr.setDbName(pkDbName);
                            pkUniqConstr.setName(pkDbName);
                            pkUniqConstr.setAutoDbName(false);

                            EnumSet<EDdsConstraintDbOption> pkDbOptions = pkUniqConstr.getDbOptions();

                            checkDbOption(pkDbOptions, EDdsConstraintDbOption.DEFERRABLE, pkSet.getString("DEFERRABLE") /*may be null*/, "DEFERRABLE");
                            checkDbOption(pkDbOptions, EDdsConstraintDbOption.INITIALLY_DEFERRED, pkSet.getString("DEFERRED") /*may be null*/, "DEFERRED");
                            boolean isPkDisable = checkDbOption(pkDbOptions, EDdsConstraintDbOption.DISABLE, pkSet.getString("STATUS") /*may be null*/, "DISABLED");
                            boolean isPkNovalidate = checkDbOption(pkDbOptions, EDdsConstraintDbOption.NOVALIDATE, pkSet.getString("VALIDATED") /*may be null*/, "NOT VALIDATED");

                            boolean isPkRelyInModel = pkDbOptions.contains(EDdsConstraintDbOption.RELY);
                            String rely = pkSet.getString("RELY"); //may be null
                            boolean isPkRelyInDB = rely != null && rely.equals("RELY");

                            if (isPkRelyInModel && !isPkRelyInDB) {
                                pkDbOptions.remove(EDdsConstraintDbOption.RELY);
                            } else if (!isPkRelyInModel && isPkRelyInDB && (isPkDisable || isPkNovalidate)) {
                                pkDbOptions.add(EDdsConstraintDbOption.RELY);
                            }

                            RadixObjects<DdsIndexDef.ColumnInfo> columnsInfo = pkIndex.getColumnsInfo();
                            columnsInfo.clear();

                            IResult pkColsSet = driver.getPKCols(pkDbName);
                            try {
                                while (pkColsSet.next()) {
                                    DdsIndexDef.ColumnInfo columnInfo = DdsIndexDef.ColumnInfo.Factory.newInstance();
                                    String pkColDbName = pkColsSet.getString("COLUMN_NAME"); //may be null
                                    if (pkColDbName != null) {
                                        columnInfo.setColumnId(ddsTable.getColumns().findByDbName(pkColDbName).getId());
                                        columnsInfo.add(columnInfo);
                                    }
                                }
                            } finally {
                                pkColsSet.close();
                            }
                        } finally {
                            pkIndSet.close();
                        }
                    } else {
                        pkIndex.setGeneratedInDb(false);
                    }
                } finally {
                    pkSet.close();
                }

                updateTableColumns(ddsTable);
                updateTableIndices(ddsTable, pkIndDbName);
                updateTableTriggers(ddsTable);
            } finally {
                tableSet.close();
            }
        }
    }

    private class DbColumn {

        String dataType;
        int dataLenght;
        int dataPrecision;
        int dataScale;
        String dataDefault;
        String nullable;
        String charUsed;
        int charLength;
        String virtualColumn;
    }

    private class DbColCheck {

        String constraintName;
        String searchCondition;
    }

    private void updateTableColumns(DdsTableDef ddsTable) throws SQLException {

        Map<String, DdsColumnDef> ddsColumns = new TreeMap<String, DdsColumnDef>(); //Key = column.dbName
        Definitions<DdsColumnDef> cols = ddsTable.getColumns().getLocal();
        for (int iCol = 0, nCol = cols.size(); iCol < nCol; ++iCol) {
            DdsColumnDef ddsColumn = cols.get(iCol);
            if (ddsColumn.isGeneratedInDb()) {
                ddsColumns.put(ddsColumn.getDbName(), ddsColumn);
            }
        }

        String tableDbName = ddsTable.getDbName();

        Map<String, DbColumn> dbColumns = new TreeMap<String, DbColumn>(); //Key = COLUMN_NAME
        IResult colsSet = driver.getColumns(tableDbName);
        try {
            while (colsSet.next()) {
                String columnName = colsSet.getString("COLUMN_NAME"); //not null

                DbColumn dbColumn = new DbColumn();
                dbColumn.dataType = colsSet.getString("DATA_TYPE"); //may be null
                dbColumn.dataLenght = colsSet.getInt("DATA_LENGTH"); //not null
                dbColumn.dataPrecision = colsSet.getInt("DATA_PRECISION"); //may be null (returns 0)
                dbColumn.dataScale = colsSet.getInt("DATA_SCALE"); //may be null (returns 0)
                dbColumn.dataDefault = colsSet.getString("DATA_DEFAULT"); //may be null
                dbColumn.nullable = colsSet.getString("NULLABLE"); //may be null
                dbColumn.charUsed = colsSet.getString("CHAR_USED"); //may be null
                dbColumn.charLength = colsSet.getInt("CHAR_LENGTH"); //may be null (returns 0)
                dbColumn.virtualColumn = colsSet.getString("VIRTUAL_COLUMN"); //may be null

                if (!columnName.equals("")) {
                    dbColumns.put(columnName, dbColumn);
                }
            }
        } finally {
            colsSet.close();
        }

        Map<String, DbColCheck> dbColChecks = new TreeMap<String, DbColCheck>(); //Key = COLUMN_NAME
        IResult colChecksSet = driver.getColChecks(tableDbName);
        try {
            while (colChecksSet.next()) {
                String columnName = colChecksSet.getString("COLUMN_NAME"); //may be null

                DbColCheck dbColCheck = new DbColCheck();
                dbColCheck.constraintName = colChecksSet.getString("CONSTRAINT_NAME"); //not null
                dbColCheck.searchCondition = colChecksSet.getString("SEARCH_CONDITION"); //may be null

                if (columnName != null && !columnName.equals("")) {
                    dbColChecks.put(columnName, dbColCheck);
                }
            }
        } finally {
            colChecksSet.close();
        }

        Iterator<Map.Entry<String, DdsColumnDef>> iterDdsColumns = ddsColumns.entrySet().iterator();
        Iterator<Map.Entry<String, DbColumn>> iterDbColumns = dbColumns.entrySet().iterator();
        Iterator<Map.Entry<String, DbColCheck>> iterDbColChecks = dbColChecks.entrySet().iterator();

        Map.Entry<String, DbColumn> dbColEntry = null;
        boolean needNextDbCol = true;
        Map.Entry<String, DbColCheck> dbColCheckEntry = null;
        Map.Entry<String, DdsColumnDef> ddsColEntry = null;
        boolean needNextDbColCheck = true;
        boolean needNextDdsColCheck = true;
        while (iterDbColumns.hasNext() || !needNextDbCol) {
            if (needNextDbCol) {
                dbColEntry = iterDbColumns.next();
            }

            DdsColumnDef ddsColumn = null;

            boolean ddsColsHasNext = !needNextDdsColCheck || iterDdsColumns.hasNext();

            if (ddsColsHasNext) {
                if (needNextDdsColCheck) {
                    ddsColEntry = iterDdsColumns.next();
                }
                ddsColumn = ddsColEntry.getValue();
                if (ddsColEntry.getKey().compareTo(dbColEntry.getKey()) < 0) {
                    Definitions<DdsColumnDef> columns = ddsTable.getColumns().getLocal();
                    columns.remove(ddsColumn);
                    iterDdsColumns.remove();

                    needNextDbCol = false;
                    needNextDdsColCheck = true;
                    continue;
                }
            }

            needNextDbCol = true;
            needNextDdsColCheck = true;

            if (!ddsColsHasNext || ddsColEntry.getKey().compareTo(dbColEntry.getKey()) > 0) {
                String columnDbName = dbColEntry.getKey();
                ddsColumn = DdsColumnDef.Factory.newInstance(columnDbName);

                ddsColumn.setDbName(columnDbName);
                ddsColumn.setAutoDbName(false);
                ddsColumn.setGeneratedInDb(true);
                ddsColumn.setSequenceId(null);
                ddsColumn.setValType(EValType.NATIVE_DB_TYPE);

                Definitions<DdsColumnDef> columns = ddsTable.getColumns().getLocal();
                columns.add(ddsColumn);
                needNextDdsColCheck = false;
            }

            DbColumn dbColumn = dbColEntry.getValue();

            ddsColumn.setAutoDbType(true);
            ddsColumn.setLength(dbColumn.dataLenght);
            ddsColumn.setNotNull(!(dbColumn.nullable != null && dbColumn.nullable.equals("Y")));
            ddsColumn.setPrecision(0);

            boolean isEmptyCheck = true;

            if (iterDbColChecks.hasNext() || !needNextDbColCheck) {
                if (needNextDbColCheck) {
                    dbColCheckEntry = iterDbColChecks.next();
                }
                if (dbColCheckEntry != null && dbColEntry.getKey().equals(dbColCheckEntry.getKey())) {
                    DbColCheck dbColCheck = dbColCheckEntry.getValue();

                    DdsCheckConstraintDef checkConstraint = ddsColumn.getCheckConstraint();
                    if (checkConstraint == null) {
                        checkConstraint = DdsCheckConstraintDef.Factory.newInstance();
                        ddsColumn.setCheckConstraint(checkConstraint);
                    }
                    checkConstraint.setDbName(dbColCheck.constraintName);
                    checkConstraint.setAutoDbName(false);
                    checkConstraint.getCondition().setSql(dbColCheck.searchCondition);

                    needNextDbColCheck = true;
                    isEmptyCheck = false;
                }
            }

            if (isEmptyCheck) {
                ddsColumn.setCheckConstraint(null);
                needNextDbColCheck = false;
            }

            String charUsed = dbColumn.charUsed != null && dbColumn.charUsed.equals("C") ? "char" : "byte";
            String dbType = dbColumn.dataType != null ? dbColumn.dataType : "";

            String tempDbType = ddsColumn.getDbType();
            ddsColumn.setDbType(dbType);

            if (dbType.equals("CHAR")) {
                ddsColumn.setLength(dbColumn.charLength);
                if (dbColumn.charLength == 1) {
                    ddsColumn.setValType(EValType.CHAR);
                    ddsColumn.setDbType(dbType + "(1 " + charUsed + ")");
                    if (charUsed.equals("byte")) {
                        ddsColumn.setAutoDbType(false);
                    }
                } else {
                    ddsColumn.setValType(EValType.STR);
                    ddsColumn.setDbType(dbType + "(" + dbColumn.charLength + " " + charUsed + ")");
                    ddsColumn.setAutoDbType(false);
                }
            } else if (dbType.equals("VARCHAR")) {
                ddsColumn.setLength(dbColumn.charLength);
                ddsColumn.setValType(EValType.STR);
                ddsColumn.setDbType(dbType + "(" + dbColumn.charLength + " " + charUsed + ")");
                ddsColumn.setAutoDbType(false);
            } else if (dbType.equals("VARCHAR2")) {
                ddsColumn.setLength(dbColumn.charLength);
                ddsColumn.setValType(EValType.STR);
                ddsColumn.setDbType(dbType + "(" + dbColumn.charLength + " " + charUsed + ")");
                if (charUsed.equals("byte")) {
                    ddsColumn.setAutoDbType(false);
                }
            } else if (dbType.equals("LONG")) {
                ddsColumn.setValType(EValType.STR);
                ddsColumn.setAutoDbType(false);
            } else if (dbType.equals("FLOAT")) {
                ddsColumn.setLength(dbColumn.dataPrecision);
                ddsColumn.setPrecision(dbColumn.dataScale);
                ddsColumn.setValType(EValType.NUM);
                ddsColumn.setDbType(dbType + "(" + dbColumn.dataPrecision + ")");
                ddsColumn.setAutoDbType(false);
            } else if (dbType.equals("NUMBER")) {
                ddsColumn.setLength(dbColumn.dataPrecision);
                ddsColumn.setPrecision(dbColumn.dataScale);

                if (dbColumn.dataPrecision == 0 && dbColumn.dataScale == 0) {
                    ddsColumn.setValType(EValType.NUM);
                } else if (dbColumn.dataPrecision == 1 && dbColumn.dataScale == 0) {
                    ddsColumn.setValType(EValType.BOOL);
                } else if (dbColumn.dataPrecision < 10 && dbColumn.dataScale == 0) {
                    ddsColumn.setValType(EValType.INT);
                } else {
                    ddsColumn.setValType(EValType.NUM);
                }
                if (dbColumn.dataPrecision != 0) {
                    ddsColumn.setDbType(dbType + "(" + dbColumn.dataPrecision + "," + dbColumn.dataScale + ")");
                } //else {
                //  ddsColumn.setDbType(dbType + "(*," + dbColumn.dataScale + ")");
                //}

                EValType valType = ddsColumn.getValType();
                String columnDbType = ddsColumn.getDbType();

                if ((valType != EValType.BOOL || !columnDbType.equals("NUMBER(1,0)"))
                        && (valType != EValType.INT || dbColumn.dataScale != 0)
                        && (valType != EValType.NUM || !columnDbType.equals("NUMBER(15,2)"))) {
                    ddsColumn.setAutoDbType(false);
                }
            } else if (dbType.equals("RAW")) {
                ddsColumn.setValType(EValType.BIN);
                ddsColumn.setDbType(dbType + "(" + ddsColumn.getLength() + ")");
                ddsColumn.setAutoDbType(false);
            } else if (dbType.equals("LONG RAW")) {
                ddsColumn.setValType(EValType.BIN);
                ddsColumn.setAutoDbType(false);
            } else if (dbType.equals("BLOB")) {
                ddsColumn.setValType(EValType.BLOB);
            } else if (dbType.equals("CLOB")) {
                ddsColumn.setValType(EValType.CLOB);
            } else if (dbType.equals("DATE")) {
                ddsColumn.setValType(EValType.DATE_TIME);
            } else {
                if (dbType.substring(0, 9).equals("TIMESTAMP")) {
                    ddsColumn.setValType(EValType.DATE_TIME);
                    if (tempDbType.toUpperCase().equals("TIMESTAMP") && dbType.substring(9, 12).equals("(6)")) {
                        ddsColumn.setDbType(tempDbType);
                    }
                }
                ddsColumn.setAutoDbType(false);
            }

            if (dbColumn.virtualColumn != null && dbColumn.virtualColumn.equals("YES")) {
                ddsColumn.setDefaultValue(null);
                ddsColumn.getExpression().setSql(dbColumn.dataDefault);
            } else {
                String origDefVal = null;
                String dataDefVal = dbColumn.dataDefault;

                if (dataDefVal == null || dataDefVal.isEmpty() || dataDefVal.trim().toUpperCase().equals("NULL")) {
                    origDefVal = null;
                } else if (dataDefVal.contains("CHR(")) {
                    IResult origValSet = driver.getOrigValue(dataDefVal);
                    try {
                        origValSet.next();
                        origDefVal = origValSet.getString("VALUE"); //may be null
                    } finally {
                        origValSet.close();
                    }
                } else {
                    dataDefVal = dataDefVal.trim();
                    int defDataLen = dataDefVal.length();
                    if (dataDefVal.charAt(0) == '\'' && dataDefVal.charAt(defDataLen - 1) == '\'') {
                        origDefVal = dataDefVal.substring(1, defDataLen - 1);
                    } else {
                        origDefVal = dataDefVal;
                    }
                }

                Object origDefValObject = null;
                ERadixDefaultValueChoice defValChoice = ERadixDefaultValueChoice.VAL_AS_STR;
                if (origDefVal != null) {
                    switch (ddsColumn.getValType()) {
                        case CHAR:
                            origDefValObject = new Character(origDefVal.charAt(0));
                            break;

                        case BOOL:
                            origDefValObject = new Boolean(origDefVal.charAt(0) == '0' ? false : true);
                            break;

                        case INT:
                            origDefValObject = new Integer(origDefVal);
                            break;

                        case NUM:
                            origDefValObject = new Double(origDefVal);
                            break;

                        case DATE_TIME:
                            DateFormat dateFormat = DateFormat.getInstance();
                            try {
                                origDefValObject = dateFormat.parse(origDefVal);
                            } catch (ParseException e) {
                                origDefValObject = origDefVal;
                                if (origDefVal.equals("SYSDATE")) {
                                    defValChoice = ERadixDefaultValueChoice.DATE_TIME;
                                } else if (origDefVal.equals("SYSTIMESTAMP")) {
                                    defValChoice = ERadixDefaultValueChoice.EXACT_DATE_TIME;
                                }
                            }
                            break;

                        default:
                            origDefValObject = origDefVal;
                    }
                }

                RadixDefaultValue radixValue = null;
                switch (defValChoice) {
                    case DATE_TIME:
                        radixValue = RadixDefaultValue.Factory.newDateTime();
                        break;
                    case EXACT_DATE_TIME:
                        radixValue = RadixDefaultValue.Factory.newExactDateTime();
                        break;
                    case VAL_AS_STR:
                        ValAsStr origDefValAsStr = ValAsStr.Factory.newInstance(origDefValObject, ddsColumn.getValType());
                        radixValue = RadixDefaultValue.Factory.newValAsStr(origDefValAsStr);
                        break;
                }

                ddsColumn.setDefaultValue(radixValue);
                ddsColumn.removeExpression();
            }
        }

        while (iterDdsColumns.hasNext()) {
            DdsColumnDef ddsColumn = iterDdsColumns.next().getValue();
            Definitions<DdsColumnDef> columns = ddsTable.getColumns().getLocal();
            columns.remove(ddsColumn);
            iterDdsColumns.remove();
        }
    }

    private class DbIndex {

        String tablespaceName;
        String uniqueness;
        String visibility;
        String indexType;
        String partitioned;
        String logging;
    }

    private class DbIndCheck {

        String constraintName;
        String rely;
        String status;
        String validated;
        String deferrable;
        String deferred;
    }

    private void updateTableIndices(DdsTableDef ddsTable, String pkDbName) throws SQLException {

        Map<String, DdsIndexDef> ddsIndices = new TreeMap<String, DdsIndexDef>(); //Key = index.dbName
        Definitions<DdsIndexDef> inds = ddsTable.getIndices().getLocal();
        for (int iIdx = 0, nIdx = inds.size(); iIdx < nIdx; ++iIdx) {
            DdsIndexDef ddsIndex = inds.get(iIdx);
            if (ddsIndex.isGeneratedInDb()) {
                ddsIndices.put(ddsIndex.getDbName(), ddsIndex);
            }
        }

        String tableDbName = ddsTable.getDbName();

        Map<String, DbIndex> dbIndices = new TreeMap<String, DbIndex>(); //Key = INDEX_NAME
        IResult indsSet = driver.getIndices(tableDbName, pkDbName);
        try {
            while (indsSet.next()) {
                String indexName = indsSet.getString("INDEX_NAME"); //not null

                DbIndex dbIndex = new DbIndex();
                dbIndex.tablespaceName = indsSet.getString("TABLESPACE_NAME"); //may be null
                dbIndex.uniqueness = indsSet.getString("UNIQUENESS"); //may be null
                dbIndex.visibility = indsSet.getString("VISIBILITY"); //may be null
                dbIndex.indexType = indsSet.getString("INDEX_TYPE"); //may be null
                dbIndex.partitioned = indsSet.getString("PARTITIONED"); //may be null
                dbIndex.logging = indsSet.getString("LOGGING"); //may be null

                if (!indexName.equals("")) {
                    dbIndices.put(indexName, dbIndex);
                }
            }
        } finally {
            indsSet.close();
        }

        Map<String, DbIndCheck> dbIndChecks = new TreeMap<String, DbIndCheck>(); //Key = INDEX_NAME
        IResult indChecksSet = driver.getIndChecks(tableDbName, pkDbName);
        try {
            while (indChecksSet.next()) {
                String indexName = indChecksSet.getString("INDEX_NAME"); //may be null

                DbIndCheck dbIndCheck = new DbIndCheck();
                dbIndCheck.constraintName = indChecksSet.getString("CONSTRAINT_NAME"); //not null
                dbIndCheck.rely = indChecksSet.getString("RELY"); //may be null
                dbIndCheck.status = indChecksSet.getString("STATUS"); //may be null
                dbIndCheck.validated = indChecksSet.getString("VALIDATED"); //may be null
                dbIndCheck.deferrable = indChecksSet.getString("DEFERRABLE"); //may be null
                dbIndCheck.deferred = indChecksSet.getString("DEFERRED"); //may be null

                if (indexName != null && !indexName.equals("")) {
                    dbIndChecks.put(indexName, dbIndCheck);
                }
            }
        } finally {
            indChecksSet.close();
        }

        Iterator<Map.Entry<String, DdsIndexDef>> iterDdsIndices = ddsIndices.entrySet().iterator();
        Iterator<Map.Entry<String, DbIndex>> iterDbIndices = dbIndices.entrySet().iterator();
        Iterator<Map.Entry<String, DbIndCheck>> iterDbIndChecks = dbIndChecks.entrySet().iterator();

        Map.Entry<String, DbIndex> dbIndEntry = null;
        boolean needNextDbIndex = true;
        Map.Entry<String, DbIndCheck> dbIndCheckEntry = null;
        boolean needNextDbIndCheck = true;
        while (iterDbIndices.hasNext() || !needNextDbIndex) {
            if (needNextDbIndex) {
                dbIndEntry = iterDbIndices.next();
            }

            Map.Entry<String, DdsIndexDef> ddsIndEntry = null;
            DdsIndexDef ddsIndex = null;

            boolean ddsIndicesHasNext = iterDdsIndices.hasNext();

            if (ddsIndicesHasNext) {
                ddsIndEntry = iterDdsIndices.next();
                ddsIndex = ddsIndEntry.getValue();
                if (ddsIndEntry.getKey().compareTo(dbIndEntry.getKey()) < 0) {
                    Definitions<DdsIndexDef> indices = ddsTable.getIndices().getLocal();
                    indices.remove(ddsIndex);
                    iterDdsIndices.remove();

                    needNextDbIndex = false;
                    continue;
                }
            }

            needNextDbIndex = true;

            if (!ddsIndicesHasNext || ddsIndEntry.getKey().compareTo(dbIndEntry.getKey()) > 0) {
                String indexDbName = dbIndEntry.getKey();
                ddsIndex = DdsIndexDef.Factory.newInstance(indexDbName);

                ddsIndex.setDbName(indexDbName);
                ddsIndex.setAutoDbName(false);
                ddsIndex.setGeneratedInDb(true);

                Definitions<DdsIndexDef> indices = ddsTable.getIndices().getLocal();
                indices.add(ddsIndex);
            }

            DbIndex dbIndex = dbIndEntry.getValue();

            if (!ddsTable.isGlobalTemporary() && dbIndex.tablespaceName != null && !dbIndex.tablespaceName.equals("")) {
                ddsIndex.setTablespaceDbName(dbIndex.tablespaceName);
            }

            EnumSet<DdsIndexDef.EDbOption> ddsIndDbOptions = ddsIndex.getDbOptions();

            checkDbOption(ddsIndDbOptions, DdsIndexDef.EDbOption.UNIQUE, dbIndex.uniqueness, "UNIQUE");
            checkDbOption(ddsIndDbOptions, DdsIndexDef.EDbOption.INVISIBLE, dbIndex.visibility, "INVISIBLE");
            checkDbOption(ddsIndDbOptions, DdsIndexDef.EDbOption.BITMAP, dbIndex.indexType, "BITMAP");
            checkDbOption(ddsIndDbOptions, DdsIndexDef.EDbOption.LOCAL, dbIndex.partitioned, "YES");
            checkDbOption(ddsIndDbOptions, DdsIndexDef.EDbOption.NOLOGGING, dbIndex.logging, "NO");

            boolean isEmptyCheck = true;

            if (iterDbIndChecks.hasNext() || !needNextDbIndCheck) {
                if (needNextDbIndCheck) {
                    dbIndCheckEntry = iterDbIndChecks.next();
                }
                if (dbIndCheckEntry != null && dbIndEntry.getKey().equals(dbIndCheckEntry.getKey())) {
                    DbIndCheck dbIndCheck = dbIndCheckEntry.getValue();

                    DdsUniqueConstraintDef uniqueConstr = ddsIndex.getUniqueConstraint();
                    if (uniqueConstr == null) {
                        uniqueConstr = DdsUniqueConstraintDef.Factory.newInstance();
                        ddsIndex.setUniqueConstraint(uniqueConstr);
                    }
                    EnumSet<EDdsConstraintDbOption> ddsUniqConstrDbOptions = uniqueConstr.getDbOptions();

                    uniqueConstr.setDbName(dbIndCheck.constraintName);
                    uniqueConstr.setName(dbIndCheck.constraintName);
                    uniqueConstr.setAutoDbName(false);

                    checkDbOption(ddsUniqConstrDbOptions, EDdsConstraintDbOption.RELY, dbIndCheck.rely, "RELY");
                    checkDbOption(ddsUniqConstrDbOptions, EDdsConstraintDbOption.DISABLE, dbIndCheck.status, "DISABLED");
                    checkDbOption(ddsUniqConstrDbOptions, EDdsConstraintDbOption.NOVALIDATE, dbIndCheck.validated, "NOT VALIDATED");
                    checkDbOption(ddsUniqConstrDbOptions, EDdsConstraintDbOption.DEFERRABLE, dbIndCheck.deferrable, "DEFERRABLE");
                    checkDbOption(ddsUniqConstrDbOptions, EDdsConstraintDbOption.INITIALLY_DEFERRED, dbIndCheck.deferred, "DEFERRED");

                    needNextDbIndCheck = true;
                    isEmptyCheck = false;
                }
            }

            if (isEmptyCheck) {
                DdsUniqueConstraintDef uniqueConstr = ddsIndex.getUniqueConstraint();
                if (uniqueConstr != null && !uniqueConstr.getDbOptions().contains(EDdsConstraintDbOption.DISABLE)) {
                    ddsIndex.setUniqueConstraint(null);
                }
                needNextDbIndCheck = false;
            }

            RadixObjects<DdsIndexDef.ColumnInfo> columnsInfo = ddsIndex.getColumnsInfo();
            columnsInfo.clear();

            IResult indColsSet = driver.getIndCols(tableDbName, dbIndEntry.getKey());
            try {
                while (indColsSet.next()) {
                    DdsIndexDef.ColumnInfo colInfo = DdsIndexDef.ColumnInfo.Factory.newInstance();
                    colInfo.setColumnId(getIndColId(indColsSet, ddsTable));

                    String descend = indColsSet.getString("DESCEND"); //may be null
                    if (descend != null && descend.equals("DESC")) {
                        colInfo.setOrder(EOrder.DESC);
                    } else {
                        colInfo.setOrder(EOrder.ASC);
                    }

                    columnsInfo.add(colInfo);
                }
            } finally {
                indColsSet.close();
            }
        }

        while (iterDdsIndices.hasNext()) {
            DdsIndexDef ddsIndex = iterDdsIndices.next().getValue();
            Definitions<DdsIndexDef> indices = ddsTable.getIndices().getLocal();
            indices.remove(ddsIndex);
            iterDdsIndices.remove();
        }
    }

    private class DbTrigger {

        String triggerType;
        String triggerBody;
        String triggeringEvent;
    }

    private void updateTableTriggers(DdsTableDef ddsTable) throws SQLException {

        Map<String, DdsTriggerDef> ddsTriggers = new TreeMap<String, DdsTriggerDef>(); //Key = trg.dbName
        Definitions<DdsTriggerDef> trgs = ddsTable.getTriggers().getLocal();
        for (int iTrg = 0, nTrg = trgs.size(); iTrg < nTrg; ++iTrg) {
            DdsTriggerDef ddsTrigger = trgs.get(iTrg);
            //Triggers are always generated in database.
            ddsTriggers.put(ddsTrigger.getDbName().toUpperCase(), ddsTrigger);
        }

        Map<String, DbTrigger> dbTriggers = new TreeMap<String, DbTrigger>(); //Key = TRIGGER_NAME
        IResult trgsSet = driver.getTriggers(ddsTable.getDbName());
        try {
            while (trgsSet.next()) {
                String triggerName = trgsSet.getString("TRIGGER_NAME"); //may be null

                DbTrigger dbTrigger = new DbTrigger();
                dbTrigger.triggerType = trgsSet.getString("TRIGGER_TYPE"); //may be null
                dbTrigger.triggerBody = trgsSet.getString("TRIGGER_BODY"); //may be null
                dbTrigger.triggeringEvent = trgsSet.getString("TRIGGERING_EVENT"); //may be null

                if (triggerName != null && !triggerName.equals("")) {
                    dbTriggers.put(triggerName, dbTrigger);
                }
            }
        } finally {
            trgsSet.close();
        }

        Iterator<Map.Entry<String, DdsTriggerDef>> iterDdsTrgs = ddsTriggers.entrySet().iterator();
        Iterator<Map.Entry<String, DbTrigger>> iterDbTrgs = dbTriggers.entrySet().iterator();

        Map.Entry<String, DbTrigger> dbTrgEntry = null;
        boolean needNextDbTrigger = true;
        while (iterDbTrgs.hasNext() || !needNextDbTrigger) {
            if (needNextDbTrigger) {
                dbTrgEntry = iterDbTrgs.next();
            }

            Map.Entry<String, DdsTriggerDef> ddsTrgEntry = null;
            DdsTriggerDef ddsTrigger = null;

            boolean ddsTrgsHasNext = iterDdsTrgs.hasNext();

            if (ddsTrgsHasNext) {
                ddsTrgEntry = iterDdsTrgs.next();
                ddsTrigger = ddsTrgEntry.getValue();
                if (ddsTrgEntry.getKey().compareTo(dbTrgEntry.getKey()) < 0) {
                    Definitions<DdsTriggerDef> triggers = ddsTable.getTriggers().getLocal();
                    triggers.remove(ddsTrigger);
                    iterDdsTrgs.remove();

                    needNextDbTrigger = false;
                    continue;
                }
            }

            needNextDbTrigger = true;

            if (!ddsTrgsHasNext || ddsTrgEntry.getKey().compareTo(dbTrgEntry.getKey()) > 0) {
                String trgDbName = dbTrgEntry.getKey();
                ddsTrigger = DdsTriggerDef.Factory.newInstance(trgDbName);

                ddsTrigger.setDbName(trgDbName);
                ddsTrigger.setAutoDbName(false);

                Definitions<DdsTriggerDef> triggers = ddsTable.getTriggers().getLocal();
                triggers.add(ddsTrigger);
            }

            DbTrigger dbTrigger = dbTrgEntry.getValue();

            if (dbTrigger.triggerType != null) {
                ddsTrigger.setForEachRow(dbTrigger.triggerType.contains("EACH ROW"));

                if (dbTrigger.triggerType != null && dbTrigger.triggerType.substring(0, 5).equals("AFTER")) {
                    ddsTrigger.setActuationTime(DdsTriggerDef.EActuationTime.AFTER);
                } else if (dbTrigger.triggerType != null && dbTrigger.triggerType.substring(0, 6).equals("BEFORE")) {
                    ddsTrigger.setActuationTime(DdsTriggerDef.EActuationTime.BEFORE);
                } else {
                    ddsTrigger.setActuationTime(DdsTriggerDef.EActuationTime.INSTEAD_OF);
                }
            } else {
                ddsTrigger.setForEachRow(false);
                ddsTrigger.setActuationTime(null);
            }

            EnumSet<DdsTriggerDef.ETriggeringEvent> trgEvents = ddsTrigger.getTriggeringEvents();
            trgEvents.clear();

            if (dbTrigger.triggeringEvent != null) {
                if (dbTrigger.triggeringEvent.contains("DELETE")) {
                    trgEvents.add(DdsTriggerDef.ETriggeringEvent.ON_DELETE);
                }
                if (dbTrigger.triggeringEvent.contains("INSERT")) {
                    trgEvents.add(DdsTriggerDef.ETriggeringEvent.ON_INSERT);
                }
                if (dbTrigger.triggeringEvent.contains("UPDATE")) {
                    trgEvents.add(DdsTriggerDef.ETriggeringEvent.ON_UPDATE);
                }
            }

            ddsTrigger.getBody().setSql(dbTrigger.triggerBody);

            if (trgEvents.contains(DdsTriggerDef.ETriggeringEvent.ON_UPDATE)) {
                RadixObjects<DdsTriggerDef.ColumnInfo> columnsInfo = ddsTrigger.getColumnsInfo();
                columnsInfo.clear();

                int curTrgCol = 0;
                int trgColSize = dbTrgCols.size();

                while (curTrgCol < trgColSize && dbTrgCols.get(curTrgCol).triggerName.compareTo(dbTrgEntry.getKey()) < 0) {
                    ++curTrgCol;
                }

                DdsExtendableDefinitions<DdsColumnDef> columns = ddsTable.getColumns();
                while (curTrgCol < trgColSize) {
                    DbTrgCol dbTrgCol = dbTrgCols.get(curTrgCol);
                    if (!dbTrgCol.triggerName.equals(dbTrgEntry.getKey())) {
                        break;
                    }

                    DdsTriggerDef.ColumnInfo colInfo = DdsTriggerDef.ColumnInfo.Factory.newInstance();
                    colInfo.setColumnId(ddsTable.getColumns().findByDbName(dbTrgCol.columnName).getId());
                    columnsInfo.add(colInfo);
                    ++curTrgCol;
                }

                if (columnsInfo.size() == columns.getLocal().size()) {
                    columnsInfo.clear();
                }
            }
        }

        while (iterDdsTrgs.hasNext()) {
            DdsTriggerDef ddsTrg = iterDdsTrgs.next().getValue();
            Definitions<DdsTriggerDef> triggers = ddsTable.getTriggers().getLocal();
            triggers.remove(ddsTrg);
            iterDdsTrgs.remove();
        }
    }

    private void importViews() throws SQLException {

        for (int iView = 0, nView = dbName2View.size(); iView < nView; ++iView) {
            DdsViewDef ddsView = dbName2View.get(iView);
            String viewDbName = ddsView.getDbName();

            IResult querySet = driver.getViewQuery(viewDbName);
            try {
                if (!querySet.next()) {
                    dbName2View.remove(iView);
                    --iView;
                    --nView;
                    continue;
                }

                reModel.getViews().add(ddsView);

                String query = querySet.getString("TEXT"); //may be null

                IResult withOptionSet = driver.getViewOption(viewDbName);
                try {
                    if (withOptionSet.next()) {
                        String dbWithOption = withOptionSet.getString("CONSTRAINT_TYPE"); //may be null
                        if (dbWithOption.equals("O")) {
                            ddsView.setWithOption(EDdsViewWithOption.READ_ONLY);
                            query = extractFromViewQuery(query, "with read only");
                        } else if (dbWithOption.equals("V")) {
                            ddsView.setWithOption(EDdsViewWithOption.CHECK_OPTION);
                            query = extractFromViewQuery(query, "with check option");
                        }
                    } else {
                        ddsView.setWithOption(EDdsViewWithOption.NONE);
                    }

                    ddsView.getSqml().setSql(query);
                } finally {
                    withOptionSet.close();
                }
            } finally {
                querySet.close();
            }

        }
    }

    private String extractFromViewQuery(String query, String strToExtract) {
        if (query != null) {
            int extractIndex = query.lastIndexOf(strToExtract);
            if (extractIndex >= 0) {
                query = query.substring(0, extractIndex);
            }
        }
        return query;
    }

    private void importSequences() throws SQLException {

        for (int iSeq = 0, nSeq = dbName2Sequence.size(); iSeq < nSeq; ++iSeq) {
            DdsSequenceDef ddsSeq = dbName2Sequence.get(iSeq);

            IResult seqSet = driver.getSequence(ddsSeq.getDbName());
            try {
                if (!seqSet.next()) {
                    dbName2Sequence.remove(iSeq);
                    --iSeq;
                    --nSeq;
                    continue;
                }

                reModel.getSequences().add(ddsSeq);

                Long cache = getSeqAttr(seqSet, "CACHE_SIZE");
                if (ddsSeq.getCache() != null || cache != null && cache != 0) {
                    ddsSeq.setCache(cache);
                }

                Long increment = getSeqAttr(seqSet, "INCREMENT_BY");
                if (ddsSeq.getIncrementBy() != null || increment != null && increment != 1) {
                    ddsSeq.setIncrementBy(increment);
                }

                Long minValue = getSeqAttr(seqSet, "MIN_VALUE");
                if (ddsSeq.getMinValue() != null || minValue != null && minValue != 1) {
                    ddsSeq.setMinValue(minValue);
                }

                Long maxValue = getSeqAttr(seqSet, "MAX_VALUE");
                if (ddsSeq.getMaxValue() != maxValue) {
                    ddsSeq.setMaxValue(maxValue);
                }

                String cycled = seqSet.getString("CYCLE_FLAG"); //may be null
                ddsSeq.setCycled(cycled != null && cycled.equals("Y"));

                String ordered = seqSet.getString("ORDER_FLAG"); //may be null
                ddsSeq.setOrdered(ordered != null && ordered.equals("Y"));
            } finally {
                seqSet.close();
            }
        }
    }

    private Long getSeqAttr(IResult seqSet, String attrDbName) throws SQLException {

        Long attr;
        String attrStr = seqSet.getString(attrDbName);
        try {
            attr = Long.decode(attrStr);
        } catch (NumberFormatException e) {
            attr = null;
        }
        return attr;
    }

    private class DbReference {

        public String r_constraintName;
        public String deleteRule;
        public String rely;
        public String status;
        public String validated;
        public String deferrable;
        public String deferred;
        public String childTable;
        public String parentTable;
        public String parentIndex;
    }

    private void importReferences() throws SQLException {

        final StringBuilder tablesDbNames = new StringBuilder();
        if (dbName2Table.isEmpty()) {
            tablesDbNames.append("'#'");
        } else {
            boolean added = false;
            for (DdsTableDef ddsTable : dbName2Table.values()) {
                if (added) {
                    tablesDbNames.append(",");
                } else {
                    added = true;
                }
                tablesDbNames.append("'");
                tablesDbNames.append(ddsTable.getDbName());
                tablesDbNames.append("'");
            }
        }

        final Map<String, DbReference> dbRefs = new TreeMap<String, DbReference>(); //Key = CONSTRAINT_NAME
        final IResult refsSet = driver.getRefs(tablesDbNames.toString());
        try {
            while (refsSet.next()) {
                final String constraintName = refsSet.getString("CONSTRAINT_NAME"); //not null

                final DbReference dbRef = new DbReference();
                dbRef.r_constraintName = refsSet.getString("R_CONSTRAINT_NAME"); //may be null
                dbRef.deleteRule = refsSet.getString("DELETE_RULE"); //may be null
                dbRef.rely = refsSet.getString("RELY"); //may be null
                dbRef.status = refsSet.getString("STATUS"); //may be null
                dbRef.validated = refsSet.getString("VALIDATED"); //may be null
                dbRef.deferrable = refsSet.getString("DEFERRABLE"); //may be null
                dbRef.deferred = refsSet.getString("DEFERRED"); //may be null
                dbRef.childTable = refsSet.getString("CHILD_TABLE"); //not null
                dbRef.parentTable = refsSet.getString("PARENT_TABLE"); //not null
                dbRef.parentIndex = refsSet.getString("PARENT_INDEX"); //may be null

                if (!constraintName.isEmpty() && dbRef.r_constraintName != null && !dbRef.r_constraintName.isEmpty()) {
                    dbRefs.put(constraintName, dbRef);
                }
            }
        } finally {
            refsSet.close();
        }

        final Iterator<Map.Entry<String, DdsReferenceDef>> iterDdsRefs = dbName2Reference.entrySet().iterator();
        final Iterator<Map.Entry<String, DbReference>> iterDbRefs = dbRefs.entrySet().iterator();

        Map.Entry<String, DbReference> dbRefEntry = null;
        boolean needNextDbRef = true;
        while (iterDbRefs.hasNext() || !needNextDbRef) {
            if (needNextDbRef) {
                dbRefEntry = iterDbRefs.next();
            }

            Map.Entry<String, DdsReferenceDef> ddsRefEntry = null;
            DdsReferenceDef ddsRef = null;

            boolean ddsRefsHasNext = iterDdsRefs.hasNext();

            if (ddsRefsHasNext) {
                ddsRefEntry = iterDdsRefs.next();
                ddsRef = ddsRefEntry.getValue();
                if (ddsRefEntry.getKey().compareTo(dbRefEntry.getKey()) < 0) {
                    needNextDbRef = false;
                    iterDdsRefs.remove();
                    continue;
                }
            }

            needNextDbRef = true;

            if (!ddsRefsHasNext || ddsRefEntry.getKey().compareTo(dbRefEntry.getKey()) > 0) {
                String refDbName = dbRefEntry.getKey();
                ddsRef = DdsReferenceDef.Factory.newInstance();

                ddsRef.setDbName(refDbName);
                ddsRef.setName(refDbName);
                ddsRef.setAutoDbName(false);
                ddsRef.setGeneratedInDb(true);
            }

            reModel.getReferences().add(ddsRef);
            DbReference dbRef = dbRefEntry.getValue();

            if (dbRef.deleteRule != null) {
                if (dbRef.deleteRule.equals("CASCADE")) {
                    ddsRef.setDeleteMode(EDeleteMode.CASCADE);
                } else if (dbRef.deleteRule.equals("SET NULL")) {
                    ddsRef.setDeleteMode(EDeleteMode.SET_NULL);
                } else if (dbRef.deleteRule.equals("NO ACTION")) {
                    ddsRef.setDeleteMode(EDeleteMode.NONE);
                } else {
                    ddsRef.setDeleteMode(EDeleteMode.RESTRICT);
                }
            } else {
                ddsRef.setDeleteMode(null);
            }

            EnumSet<EDdsConstraintDbOption> ddsRefDbOptions = ddsRef.getDbOptions();

            checkDbOption(ddsRefDbOptions, EDdsConstraintDbOption.RELY, dbRef.rely, "RELY");
            checkDbOption(ddsRefDbOptions, EDdsConstraintDbOption.DISABLE, dbRef.status, "DISABLED");
            checkDbOption(ddsRefDbOptions, EDdsConstraintDbOption.NOVALIDATE, dbRef.validated, "NOT VALIDATED");
            checkDbOption(ddsRefDbOptions, EDdsConstraintDbOption.DEFERRABLE, dbRef.deferrable, "DEFERRABLE");
            checkDbOption(ddsRefDbOptions, EDdsConstraintDbOption.INITIALLY_DEFERRED, dbRef.deferred, "DEFERRED");

            DdsTableDef childTable = getLinkedTable(dbRef.childTable);
            ddsRef.setChildTableId(childTable.getId());
            ddsRef.setExtChildTableId(null);

            DdsTableDef parentTable = getLinkedTable(dbRef.parentTable);
            ddsRef.setParentTableId(parentTable.getId());
            ddsRef.setExtParentTableId(null);
            ddsRef.setParentUnuqueConstraintId(getLinkedUniqConstrId(parentTable, dbRef.parentIndex));

            RadixObjects<DdsReferenceDef.ColumnsInfoItem> columnsInfo = ddsRef.getColumnsInfo();
            columnsInfo.clear();

            IResult dbChildColsSet = driver.getRefCols(dbRefEntry.getKey());
            try {
                IResult dbParentColsSet = driver.getRefCols(dbRef.r_constraintName);
                try {
                    while (dbChildColsSet.next() & dbParentColsSet.next()) {
                        DdsReferenceDef.ColumnsInfoItem colInfoItem = DdsReferenceDef.ColumnsInfoItem.Factory.newInstance();

                        String childColName = dbChildColsSet.getString("COLUMN_NAME"); //may be null
                        String parentColName = dbParentColsSet.getString("COLUMN_NAME"); //may be null

                        if (childColName != null && parentColName != null) {
                            final DdsColumnDef childColumn = childTable.getColumns().findByDbName(childColName);
                            colInfoItem.setChildColumnId(childColumn != null ? childColumn.getId() : null);
                            final DdsColumnDef parentColumn = parentTable.getColumns().findByDbName(parentColName);
                            colInfoItem.setParentColumnId(parentColumn != null ? parentColumn.getId() : null);
                            columnsInfo.add(colInfoItem);
                        }
                    }
                } finally {
                    dbParentColsSet.close();
                }
            } finally {
                dbChildColsSet.close();
            }
        }

        while (iterDdsRefs.hasNext()) {
            DdsReferenceDef ddsRef = iterDdsRefs.next().getValue();
            DdsDefinitions<DdsReferenceDef> references = reModel.getReferences();
            references.remove(ddsRef);
            iterDdsRefs.remove();
        }
    }

    private DdsTableDef findTableByDbNameAroundScope(final String tableDbName) {
        final List<Definition> defs = RadixObjectsUtils.collectAllAround(scope, new DdsVisitorProvider() {

            @Override
            public boolean isTarget(RadixObject radixObject) {
                if (radixObject instanceof DdsTableDef) {
                    final DdsTableDef table = (DdsTableDef) radixObject;
                    if (table.getDbName().equals(tableDbName)) {
                        return true;
                    }
                }
                return false;
            }
        });

        if (!defs.isEmpty()) {
            return (DdsTableDef) defs.get(0);
        } else {
            return null;
        }
    }

    private DdsTableDef getLinkedTable(String tableDbName) {
        DdsTableDef linkedTable = dbName2Table.get(tableDbName);
        if (linkedTable == null) {
            linkedTable = findTableByDbNameAroundScope(tableDbName);
            if (linkedTable != null) {
                linkedTable = linkedTable.getClipboardSupport().copy();
            } else {
                linkedTable = DdsTableDef.Factory.newInstance(tableDbName);
                linkedTable.setDbName(tableDbName);
            }

            linkedTable.setGeneratedInDb(false);
            reModel.getTables().add(linkedTable);
            dbName2Table.put(tableDbName, linkedTable);
        }
        return linkedTable;
    }

    private Id getLinkedUniqConstrId(DdsTableDef linkedTable, String indexDbName) {

        DdsIndexDef pkIndex = linkedTable.getPrimaryKey();
        if (pkIndex.getDbName().equals(indexDbName)) {
            return pkIndex.getUniqueConstraint().getId();
        }
        for (DdsIndexDef index : linkedTable.getIndices().getLocal()) {
            if (index.getDbName().equals(indexDbName)) {
                return index.getUniqueConstraint().getId();
            }
        }
        return null;
    }

    private <TypeDbOption extends Enum<TypeDbOption>> boolean checkDbOption(EnumSet<TypeDbOption> dbOptions, TypeDbOption checkingOption, String optionValue, String optionValueForCompare) {

        boolean dbOptionExistInModel = dbOptions.contains(checkingOption);
        boolean dbOptionExistInDB = optionValue != null && optionValue.equals(optionValueForCompare);

        if (dbOptionExistInModel && !dbOptionExistInDB) {
            dbOptions.remove(checkingOption);
        } else if (!dbOptionExistInModel && dbOptionExistInDB) {
            dbOptions.add(checkingOption);
        }
        return dbOptionExistInDB;
    }

    private Id getIndColId(IResult indColsSet, DdsTableDef ddsTable) throws SQLException {

        String columnDbName = null;
        String hidden = indColsSet.getString("HIDDEN_COLUMN"); //may be null
        String data = indColsSet.getString("DATA_DEFAULT"); //may be null
        if (data != null && hidden != null && hidden.equals("YES")) {
            columnDbName = data.substring(1, data.length() - 1);
        } else {
            columnDbName = indColsSet.getString("COLUMN_NAME"); //may be null
        }
        final DdsColumnDef column = ddsTable.getColumns().findByDbName(columnDbName);
        return column.getId();
    }
}
