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

package org.radixware.kernel.common.builder.check.dds;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.radixware.kernel.common.builder.check.common.RadixObjectCheckerRegistration;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.conventions.DbNameConventions;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.IFilter;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.dds.AuditTriggersUpdater;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsIndexDef;
import org.radixware.kernel.common.defs.dds.DdsPrimaryKeyDef;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.defs.dds.DdsViewDef;
import org.radixware.kernel.common.defs.dds.providers.DdsVisitorProviderFactory;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EDdsTableExtOption;
import org.radixware.kernel.common.enums.EDeleteMode;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;


@RadixObjectCheckerRegistration
public class DdsTableChecker<T extends DdsTableDef> extends DdsDefinitionChecker<T> {

    private static final Id UPVALREF_TABLE_ID = Id.Factory.loadFrom("tblZVJUSFASRDNBDCBEABIFNQAABA");
    private static final String UPDEFID_COLUMN_NAME = DbNameConventions.PROP_DEF_ID_DB_COL_NAME.toUpperCase();
    private static final String UPOWNERENTITY_ID_COLUMN_NAME = DbNameConventions.VAL_OWNER_ENTITY_ID_DB_COL_NAME.toUpperCase();
    private static final String UPOWNERPID_COLUMN_NAME = DbNameConventions.VAL_OWNER_PID_DB_COL_NAME.toUpperCase();

    public DdsTableChecker() {
    }

    @Override
    public Class<? extends RadixObject> getSupportedClass() {
        return DdsTableDef.class;
    }

    private boolean isIndexEquals(DdsIndexDef idx, String[] columnDbNames) {
        int count = columnDbNames.length;
        if (idx.getColumnsInfo().size() != count) {
            return false;
        }
        for (int i = 0; i < count; i++) {
            DdsColumnDef column = idx.getColumnsInfo().get(i).findColumn();
            if (column == null || !Utils.equals(column.getDbName(), columnDbNames[i])) {
                return false;
            }
        }
        return true;
    }

    private void checkInitialValues(T table, IProblemHandler problemHandler) {
        final DdsPrimaryKeyDef pk = table.getPrimaryKey();
        if (pk.getColumnsInfo().isEmpty()) {
            return;
        }

        final DdsColumnDef firstPkColumn = pk.getColumnsInfo().get(0).findColumn();
        if (firstPkColumn == null) {
            return;
        }

        final int size = firstPkColumn.getInitialValues().size();

        if (size > 0 && table instanceof DdsViewDef) {
            error(table, problemHandler, "Database view has initial values");
        }

        if (size > 0 && table.isGlobalTemporary()) {
            error(table, problemHandler, "Global temporary table has initial values"); // there is no sense; it is impossible to generate modification script.
        }

        boolean sizeEquals = true;

        for (DdsColumnDef column : table.getColumns().get(EScope.LOCAL_AND_OVERWRITE)) {
            final int columnInitialValuesSize = column.getInitialValues().size();
            if (columnInitialValuesSize != size && (columnInitialValuesSize != 0 || column.isPrimaryKey())) {
                error(column, problemHandler, "Column has " + columnInitialValuesSize + " initial values instead of " + size);
                sizeEquals = false;
            }
        }

        if (size > 0 && sizeEquals) {
            final List<DdsColumnDef> pkColumns = new ArrayList<>();
            for (DdsIndexDef.ColumnInfo info : pk.getColumnsInfo()) {
                final DdsColumnDef pkColumn = info.findColumn();
                if (pkColumn != null) {
                    pkColumns.add(pkColumn);
                }
            }
            final Set<List<ValAsStr>> pids = new HashSet<>(size);
            final int pkColumnCount = pkColumns.size();
            for (int i = 0; i < size; i++) {
                final List<ValAsStr> pid = new ArrayList<>(pkColumnCount);
                for (DdsColumnDef pkColumn : pkColumns) {
                    final ValAsStr value = pkColumn.getInitialValues().get(i);
                    pid.add(value);
                }
                if (!pids.add(pid)) {
                    final StringBuilder sb = new StringBuilder();
                    boolean addedFlag = false;
                    for (DdsColumnDef pkColumn : pkColumns) {
                        sb.append(pkColumn.getName());
                        sb.append("=");
                        final ValAsStr value = pkColumn.getInitialValues().get(i);
                        sb.append(String.valueOf(value));
                        pid.add(pkColumn.getInitialValues().get(i));
                        if (addedFlag) {
                            sb.append(", ");
                        } else {
                            addedFlag = true;
                        }
                    }
                    final String pidAsStr = sb.toString();
                    error(table, problemHandler, "Duplicated initial value (" + pidAsStr + ")");
                }
            }
        }
    }

    @Override
    public void check(T table, IProblemHandler problemHandler) {
        super.check(table, problemHandler);

        if (!table.isOverwrite()) {
            checkForDbNameDuplication(table, problemHandler);
        }

        // app classes
        DdsColumnDef classGuidColumn = table.findClassGuidColumn();
        if (table.getExtOptions().contains(EDdsTableExtOption.ENABLE_APPLICATION_CLASSES)) {
            if (classGuidColumn == null) {
                error(table, problemHandler, "Application classes used without ClassGuid column");
            }
        } else {
            if (classGuidColumn != null && !classGuidColumn.isDeprecated()) {
                error(table, problemHandler, "ClassGuid column used without application classes");
            }
        }


        // ------------------- User classes ----------

        if (table.getExtOptions().contains(EDdsTableExtOption.USE_AS_USER_PROPERTIES_OBJECT)) {

            String[] dbNames = {UPDEFID_COLUMN_NAME, UPOWNERENTITY_ID_COLUMN_NAME, UPOWNERPID_COLUMN_NAME};
            for (String dbName : dbNames) {
                if (table.getColumns().findByDbName(dbName) == null) {
                    error(table, problemHandler, "Column with database name " + dbName + " not found in table used as user property object");
                }
            }

            boolean isIndexExistFlag = isIndexEquals(table.getPrimaryKey(), dbNames);
            if (!isIndexExistFlag) {
                for (DdsIndexDef idx : table.getIndices().get(EScope.LOCAL_AND_OVERWRITE)) {
                    if (idx.isUnique() && (isIndexExistFlag = isIndexEquals(idx, dbNames))) {
                        break;
                    }
                }
            }
            if (!isIndexExistFlag) {
                error(table, problemHandler, "Unically index not found for columns UPDEFID, UPOWNERENTITYID, UPOWNERPID in table that used as user property object");
            }


            boolean isReferenceToUpValRefExists = false;//this reference is required for cascade deletion

            final Set<DdsReferenceDef> references = table.collectOutgoingReferences();
            if (references != null) {
                for (DdsReferenceDef ref : references) {
                    if (UPVALREF_TABLE_ID.equals(ref.getParentTableId())) {
                        if (ref.getColumnsInfo() != null) {
                            List<String> toFind = new ArrayList<>();
                            toFind.add(UPDEFID_COLUMN_NAME);
                            toFind.add(UPOWNERENTITY_ID_COLUMN_NAME);
                            toFind.add(UPOWNERPID_COLUMN_NAME);
                            for (DdsReferenceDef.ColumnsInfoItem item : ref.getColumnsInfo()) {
                                final DdsColumnDef parentCol = item.findParentColumn();
                                final DdsColumnDef childCol = item.findChildColumn();
                                if (parentCol != null && childCol != null) {
                                    if (toFind.contains(childCol.getDbName())) {
                                        if (childCol.getDbName().substring(2).equals(parentCol.getDbName())) {
                                            toFind.remove(childCol.getDbName());
                                            continue;
                                        }
                                    }
                                    toFind.add("wrong-column-marker");
                                }
                            }
                            if (toFind.isEmpty() && ref.getDeleteMode() == EDeleteMode.CASCADE) {
                                isReferenceToUpValRefExists = true;
                            }
                        }
                    }
                }
            }

            if (!isReferenceToUpValRefExists) {
                error(table, problemHandler, "There should be an outgoing reference (UPDEFID, UPOWNERENTITYID, UPOWNERPID)->(DEFID, OWNERENTITYID, OWNERPID) with DELETE CASCADE option in table that is used as user property object");
            }
        }

        // initial values
        checkInitialValues(table, problemHandler);

        // override
        if (table.isOverwrite()) {
            DdsTableDef overwritten = table.findOverwritten();
            if (overwritten != null) {
                if (overwritten instanceof DdsViewDef) {
                    error(table, problemHandler, "Overwriting of database view");
                }
                if (overwritten.isGlobalTemporary()) {
                    error(table, problemHandler, "Overwriting of global temporaty table");
                }
                if (!Utils.equals(table.getName(), overwritten.getName())) {
                    warning(table, problemHandler, "Name does not match the name of overwritten '" + overwritten.getQualifiedName() + "'");
                }
            } else {
                error(table, problemHandler, "Overwritten table not found");
            }
        }

        // partitioned global temporaty
        if (table.isGlobalTemporary() && !table.getPartition().getItems().isEmpty()) {
            error(table, problemHandler, "Partitioned global temporary table");
        }

        // audit
        final DdsTableDef.AuditInfo auditInfo = table.getAuditInfo();
        if (auditInfo.isEnabled()) {
            final Id auditReferenceId = auditInfo.getAuditReferenceId();
            if (auditReferenceId != null) {
                final DdsReferenceDef ref = auditInfo.findAuditReference();
                if (ref != null) {
                    final IFilter<DdsReferenceDef> filter = DdsVisitorProviderFactory.newReferenceForAuditFilter();
                    if (ref.findChildTable(table) != table || !filter.isTarget(ref)) {
                        error(table, problemHandler, "Illegal audit reference: #" + auditReferenceId);
                    }
                } else {
                    error(table, problemHandler, "Audit reference not found: #" + auditReferenceId);
                }
            }

            if (table.isDetailTable()) {
                for (DdsReferenceDef ref : table.collectOutgoingReferences()) {
                    if (ref.getType() == DdsReferenceDef.EType.MASTER_DETAIL) {
                        if (!Utils.equals(ref.getId(), table.getAuditInfo().getAuditReferenceId())) {
                            final IFilter<DdsReferenceDef> filter = DdsVisitorProviderFactory.newReferenceForAuditFilter();
                            if (filter.isTarget(ref)) {
                                error(table, problemHandler, "Audit reference must be reference to master table");
                            }
                        }
                    }
                }
            }
        }

        if (!AuditTriggersUpdater.check(table)) {
            error(table, problemHandler, "Audit triggers are not actual");
        }
    }
}
