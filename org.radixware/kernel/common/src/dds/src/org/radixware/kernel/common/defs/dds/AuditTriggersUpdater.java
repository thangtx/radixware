/*
 * Copyright (c) 2008-2018, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. This Source Code is distributed
 * WITHOUT ANY WARRANTY; including any implied warranties but not limited to
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Mozilla Public License, v. 2.0. for more details.
 */

package org.radixware.kernel.common.defs.dds;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import org.apache.xmlbeans.XmlException;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.dds.DdsTableDef.AuditInfo;
import org.radixware.kernel.common.defs.dds.DdsTriggerDef.EActuationTime;
import org.radixware.kernel.common.defs.dds.DdsTriggerDef.ETriggeringEvent;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.scml.Scml.Text;
import org.radixware.kernel.common.sqml.tags.DbFuncCallTag;
import org.radixware.kernel.common.sqml.tags.SequenceDbNameTag;
import org.radixware.kernel.common.sqml.tags.TableSqlNameTag;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.schemas.xscml.Sqml;


public class AuditTriggersUpdater {

    /**
     * Check for audit trigger actual state.
     *
     * @return true if audit triggers is actual.
     */
    public static boolean check(DdsTableDef table) {
        return checkOrUpdate(table, true /*
                 * checkOnly
                 */);
    }

    /**
     * Update audit triggers of the table.
     */
    public static void update(DdsTableDef table) {
        checkOrUpdate(table, false /*
                 * checkOnly
                 */);
    }

    private static boolean checkOrUpdate(DdsTableDef table, boolean checkOnly) {
        final AuditInfo auditInfo = table.getAuditInfo();
        if (!checkOrUpdateTrigger(table, ETriggeringEvent.ON_UPDATE, "AuditUpdate", auditInfo.isEnabledForUpdate(), checkOnly)) {
            if (checkOnly) {
                return false;
            }
        }
        if (!checkOrUpdateTrigger(table, ETriggeringEvent.ON_INSERT, "AuditInsert", auditInfo.isEnabledForInsert(), checkOnly)) {
            if (checkOnly) {
                return false;
            }
        }
        if (!checkOrUpdateTrigger(table, ETriggeringEvent.ON_DELETE, "AuditDelete", auditInfo.isEnabledForDelete(), checkOnly)) {
            if (checkOnly) {
                return false;
            }
        }
        return true;
    }

    private static boolean hasLocalAuditColumns(DdsTableDef table) {
        for (DdsColumnDef column : table.getColumns().getLocal()) {
            if (column.getAuditInfo().isOnUpdate() || column.getAuditInfo().isSaveValues()) {
                return true;
            }
        }
        return false;
    }

    private static boolean equals(DdsTriggerDef.ColumnsInfo columnsInfo, List<DdsColumnDef> columns) {
        int size = columnsInfo.size();
        if (columns.size() != size) {
            return false;
        }

        for (int i = 0; i < size; i++) {
            if (columnsInfo.get(i).getColumnId() != columns.get(i).getId()) {
                return false;
            }
        }
        return true;
    }

    /**
     * @return true if audit triggers was actual, false otherwise.
     */
    private static boolean checkOrUpdateTrigger(DdsTableDef table, ETriggeringEvent event, String name, boolean enable, boolean checkOnly) {
        final String auditLogType = event.getValue();
        final String prefix = "at" + auditLogType.toLowerCase();
        final String triggerDbName = prefix + table.getId().toString().substring(3);
        DdsTriggerDef trigger = table.getTriggers().findByDbName(triggerDbName);
        if (enable && (table.findOverwritten() == null || hasLocalAuditColumns(table))) {
            if (trigger == null) {
                if (checkOnly) {
                    return false;
                }
                trigger = DdsTriggerDef.Factory.newInstance(name);
                table.getTriggers().getLocal().add(trigger);
            } else if (trigger.getOwnerTable() != table) {
                // override trigger
                if (checkOnly) {
                    return false;
                }
                trigger = DdsTriggerDef.Factory.newInstance(trigger.getId(), name);
                table.getTriggers().getLocal().add(trigger);
            }

            final boolean overwriteRequired = (trigger.findOverwritten() != null);
            if (trigger.isOverwrite() != overwriteRequired) {
                if (checkOnly) {
                    return false;
                }
                trigger.setOverwrite(overwriteRequired);
            }

            if (trigger.isAutoDbName()) {
                if (checkOnly) {
                    return false;
                }
                trigger.setAutoDbName(false);
            }

            if (!Utils.equals(trigger.getDbName(), triggerDbName)) {
                if (checkOnly) {
                    return false;
                }
                trigger.setDbName(triggerDbName);
            }

            if (!Utils.equals(trigger.getTriggeringEvents(), EnumSet.of(event))) {
                if (checkOnly) {
                    return false;
                }
                trigger.getTriggeringEvents().clear();
                trigger.getTriggeringEvents().add(event);
            }

            if (!trigger.isForEachRow()) {
                if (checkOnly) {
                    return false;
                }
                trigger.setForEachRow(true);
            }

            if (trigger.getActuationTime() != EActuationTime.AFTER) {
                if (checkOnly) {
                    return false;
                }
                trigger.setActuationTime(EActuationTime.AFTER);
            }

            if (!trigger.isDisabled()) {
                if (checkOnly) {
                    return false;
                }
                trigger.setDisabled(true);
            }

            if (!Utils.equals(trigger.getName(), name)) {
                if (checkOnly) {
                    return false;
                }
                trigger.setName(name);
            }

            if (trigger.getType() != DdsTriggerDef.EType.FOR_AUDIT) {
                if (checkOnly) {
                    return false;
                }
                trigger.setType(DdsTriggerDef.EType.FOR_AUDIT);
            }

            if (event == ETriggeringEvent.ON_UPDATE) {
                final List<DdsColumnDef> auditColumns = new ArrayList<DdsColumnDef>();
                for (DdsColumnDef col : table.getColumns().get(EScope.ALL)) {
                    if (col.isGeneratedInDb() && !col.isPrimaryKey() && col.getAuditInfo().isOnUpdate()) {
                        auditColumns.add(col);
                    }
                }
                if (!equals(trigger.getColumnsInfo(), auditColumns)) {
                    if (checkOnly) {
                        return false;
                    }
                    trigger.getColumnsInfo().clear();
                    for (DdsColumnDef col : auditColumns) {
                        trigger.getColumnsInfo().add(col);
                    }
                }
            }

            if (!checkOrUpdateTriggerBody(table, event, trigger, checkOnly)) {
                if (checkOnly) {
                    return false;
                }
            }
        } else {
            if (trigger != null && trigger.getOwnerTable() == table) {
                if (checkOnly) {
                    return false;
                }
                trigger.delete();
            }
        }
        return true;
    }
    private static final String auditLogSeq = "sqn2FY3TI3TMXNBDKV7ABINU7DB2Q";
    private static final String auditLogTable = "tblXXXFKJLLMLNBDKV7ABINU7DB2Q";
    private static final Id RDX_SYSTEM_TABLE_ID = Id.Factory.loadFrom("tblX5TD7JDVVHWDBROXAAIT4AGD7E");
    private static final Id RDX_SCHEME_TABLE_ID = Id.Factory.loadFrom("tblJBWGL34ATTNBDPK5ABQJO5ADDQ");
    //private static final Id RDX_SCHEMEITEM_TABLE_ID = Id.Factory.loadFrom("tblFEIALUQDMPNBDKV7ABINU7DB2Q");
    //private static final Id STOREDURATION_COLUMN_ID = Id.Factory.loadFrom("colAUV5IYAEMPNBDKV7ABINU7DB2Q");
    //private static final Id SAVEDATA_COLUMN_ID = Id.Factory.loadFrom("colWNMPOZQEMPNBDKV7ABINU7DB2Q");

    private static DdsReferenceDef checkAndFindAuditReference(DdsTableDef table) {
        final DdsReferenceDef auditRef = table.getAuditInfo().findAuditReference();
        if (auditRef == null) {
            return null;
        }

        DdsReferenceDef ref = auditRef;
        for (int i = 0; i < 1000; i++) {
            if (ref.findChildTable(table) == null) {
                return null;
            }
            for (DdsReferenceDef.ColumnsInfoItem item : ref.getColumnsInfo()) {
                if (item.findChildColumn() == null || item.findParentColumn() == null) {
                    return null;
                }
            }
            final DdsTableDef parent = ref.findParentTable(table);
            if (parent == null) {
                return null;
            }
            if (parent.getId() == RDX_SCHEME_TABLE_ID) {
                if (ref.getColumnsInfo().size() == 1) {
                    return auditRef;
                } else {
                    return null;
                }
            }
            ref = parent.getAuditInfo().findAuditReference();
            if (ref == null) {
                return null;
            }
        }
        return null;
    }

    private static boolean checkOrUpdateTriggerBody(DdsTableDef table, ETriggeringEvent event, DdsTriggerDef trigger, boolean checkOnly) {
        final String auditLogType = event.getValue();

        final SqmlBuilder sqmlb = new SqmlBuilder();
        final String prefix = (event == ETriggeringEvent.ON_INSERT ? ":new." : ":old.");
        final DdsReferenceDef auditRef = null;//checkAndFindAuditReference(table);
        sqmlb.println("declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);");
        sqmlb.println("begin");
        sqmlb.println("   begin");

        if (table.getId() == RDX_SYSTEM_TABLE_ID) {
            // impossible to select from 'RDX_SYSTEM' - mutable trigger exception, used only via prefix (audited only first system)
            sqmlb.println("      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData");
            sqmlb.println("      from RDX_AU_SCHEMEITEM");
            sqmlb.println("      where RDX_AU_SCHEMEITEM.SCHEMEGUID=RDX_AUDIT_VARS.DEFAULTAUDITSCHEME");
        } else {
            if (event == ETriggeringEvent.ON_DELETE) {
                sqmlb.println("      vSchemeId := RDX_AUDIT_VARS.DEFAULTAUDITSCHEME;");
                sqmlb.println("      if vSchemeId IS NULL then ");
                sqmlb.println("           select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;");
                sqmlb.println("      end if;");
            } else {
                sqmlb.println("      select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;");
            }
            sqmlb.println("      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData");
            if (auditRef != null) {
                sqmlb.print("      from RDX_AU_SCHEMEITEM");
                DdsTableDef childTable = null;
                DdsTableDef parentTable = auditRef.findParentTable(table);
                DdsReferenceDef ref = auditRef;
                while (parentTable.getId() != RDX_SCHEME_TABLE_ID) {
                    if (childTable == null) {
                        sqmlb.print(", (select ");
                        boolean colPrinted = false;
                        for (DdsReferenceDef.ColumnsInfoItem item : ref.getColumnsInfo()) {
                            final DdsColumnDef childColumn = item.findChildColumn();
                            if (colPrinted) {
                                sqmlb.print(", ");
                            } else {
                                colPrinted = true;
                            }
                            sqmlb.print(prefix + childColumn.getDbName() + " " + childColumn.getDbName());
                        }
                        sqmlb.print(" from dual) D$");
                    }
                    sqmlb.print(" left join " + parentTable.getDbName() + " on ");
                    boolean colPrinted = false;
                    for (DdsReferenceDef.ColumnsInfoItem item : ref.getColumnsInfo()) {
                        final DdsColumnDef childColumn = item.findChildColumn();
                        final DdsColumnDef parentColumn = item.findParentColumn();
                        if (colPrinted) {
                            sqmlb.print(" and ");
                        } else {
                            colPrinted = true;
                        }
                        sqmlb.print((childTable != null ? childTable.getDbName() : "D$") + "." + childColumn.getDbName() + "=" + parentTable.getDbName() + "." + parentColumn.getDbName());
                    }
                    ref = parentTable.getAuditInfo().findAuditReference();
                    childTable = parentTable;
                    parentTable = ref.findParentTable(table);
                }

                sqmlb.print(
                        "\n      where RDX_AU_SCHEMEITEM.SCHEMEGUID=NVL("
                        + (childTable != null ? childTable.getDbName() + "." : prefix)
                        + ref.getColumnsInfo().get(0).findChildColumn().getDbName()
                        + ", vSchemeId)\n");
            } else {
                // use default scheme
                sqmlb.println("      from RDX_AU_SCHEMEITEM");
                sqmlb.println("      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId");
            }
        }
        final DdsReferenceDef masterRef = table.findMasterReference();
        final DdsTableDef masterTable = (masterRef != null ? masterRef.findParentTable(masterRef) : null);
        final Id ownerTableId = (masterTable != null ? masterTable.getId() : table.getId());

        sqmlb.println("         and RDX_AU_SCHEMEITEM.TABLEID = '" + ownerTableId.toString() + "'");
        sqmlb.println("         and RDX_AU_SCHEMEITEM.EVENTTYPE = '" + auditLogType + "';");

        sqmlb.println("   exception");
        sqmlb.println("      when no_data_found then return;"); // audit disabled in default schema doesn't defined or schema is inactive
        sqmlb.println("   end;");

        /*
         * org.radixware.kernel.common.sqml.Sqml sqml =
         * org.radixware.kernel.common.sqml.Sqml.Factory.newInstance(); String
         * prefix = event == ETriggeringEvent.ON_INSERT ? ":new." : ":old.";
         * addStringToSqml(sqml, "declare vStoreDuration integer; vSaveData
         * integer; vPID varchar2(4000); vChangeData clob; vSchemeId
         * varchar2(50);\n"); addStringToSqml(sqml, "begin\n"); StringBuilder
         * into = new StringBuilder(); StringBuilder from = new StringBuilder();
         * StringBuilder where = new StringBuilder(); TableSqlNameTag rdxSystem
         * = TableSqlNameTag.Factory.newInstance();
         * rdxSystem.setTableId(Id.Factory.loadFrom(RDX_SYSTEM_TABLE_ID));
         * rdxSystem.setSql("RDX_SYSTEM");
         *
         * if (table.getId().toString().equals(RDX_SYSTEM_TABLE_ID)) {
         * addStringToSqml(sqml, " vSchemeId := " + prefix +
         * "DEFAULTAUDITSCHEMEID;\n"); } else { if (auditReference(table, 1,
         * prefix, into, from, where)) { if
         * (table.getDbName().toUpperCase().equals("INSTITUTION")) {
         * addStringToSqml(sqml, " select "); addStringToSqml(sqml, "NVL(" +
         * into + ",(select DEFAULTAUDITSCHEMEID from ");
         * sqml.getItems().add(rdxSystem); addStringToSqml(sqml, " where
         * ID=1))"); addStringToSqml(sqml, " into vSchemeId from " + from + "
         * where " + where + ";\n"); } else { addStringToSqml(sqml, " select " +
         * into + " into vSchemeId from " + from + " where " + where + ";\n"); }
         * addStringToSqml(sqml, " select STOREDURATION, SAVEDATA into
         * vStoreDuration, vSaveData from "); TableSqlNameTag schemeItem =
         * TableSqlNameTag.Factory.newInstance();
         * schemeItem.setTableId(Id.Factory.loadFrom(schemeItemTable));
         * schemeItem.setSql("RDX_AU_SCHEMEITEM");
         * sqml.getItems().add(schemeItem); addStringToSqml(sqml, " where
         * SCHEMEGUID=vSchemeId and TABLEID = '" + table.getId().toString() + "'
         * and EVENTTYPE = '" + eventType + "';\n"); } else {
         * addStringToSqml(sqml, " select STOREDURATION, SAVEDATA into
         * vStoreDuration, vSaveData from "); TableSqlNameTag schemeItem =
         * TableSqlNameTag.Factory.newInstance();
         * schemeItem.setTableId(Id.Factory.loadFrom(schemeItemTable));
         * schemeItem.setSql("RDX_AU_SCHEMEITEM");
         * sqml.getItems().add(schemeItem); addStringToSqml(sqml, " ITEM, ");
         * sqml.getItems().add(rdxSystem); addStringToSqml(sqml, " SYS");
         * addStringToSqml(sqml, "\n where SYS.ID = 1 and ITEM.SCHEMEGUID =
         * SYS.DEFAULTAUDITSCHEMEID "); addStringToSqml(sqml, " and ITEM.TABLEID
         * = '" + table.getId().toString() + "' and ITEM.EVENTTYPE = '" +
         * eventType + "';\n"); } }
         */

        sqmlb.println("   vPID:=" + table.getPidScript(prefix.substring(0, 4)) + ";");
        final org.radixware.kernel.common.sqml.Sqml sqml = sqmlb.getSqml();

        boolean flag = false;
        org.radixware.kernel.common.sqml.Sqml sqml_columns = org.radixware.kernel.common.sqml.Sqml.Factory.newInstance();
        for (DdsColumnDef cur : table.getColumns().get(EScope.ALL)) {
            if (cur.isGeneratedInDb() && !cur.isPrimaryKey() && isSaveValueColumn(cur, event)) {
                if (event == ETriggeringEvent.ON_UPDATE) {
                    DbFuncCallTag funcTag = getForUpdate(cur);
                    if (funcTag == null) {
                        continue;
                    }
                    flag = true;
                    addStringToSqml(sqml_columns, "\n            ");
                    sqml_columns.getItems().add(funcTag);
                    addStringToSqml(sqml_columns, "(vChangeData,'" + cur.getId().toString() + "',:old." + cur.getDbName() + ",:new." + cur.getDbName() + ");");
                } else {
                    DbFuncCallTag funcTag = getTagForInsertDelete(cur);
                    if (funcTag == null) {
                        continue;
                    }
                    flag = true;
                    addStringToSqml(sqml_columns, "\n            ");
                    sqml_columns.getItems().add(funcTag);
                    addStringToSqml(sqml_columns, "(vChangeData,'" + prefix.substring(1) + cur.getId().toString() + "'," + prefix + cur.getDbName() + ");");
                }
            }
        }
        if (flag) {
            addStringToSqml(sqml, "   if vSaveData=1 then");
            Sqml tmp = Sqml.Factory.newInstance();
            sqml_columns.appendTo(tmp);

            sqml.appendFrom(tmp);

            addStringToSqml(sqml, "\n   end if;\n");
        }

        // search ClassGuid in MasterTable
        if (masterTable != null && event != ETriggeringEvent.ON_DELETE) { // except delete to avoin mutating triggers
            addStringToSqml(sqml, "\n   begin");
            addStringToSqml(sqml, "\n       select CLASSGUID into vClassGuid from " + masterTable.getDbName() + " M");
            addStringToSqml(sqml, "\n       where ");
            boolean printed = false;
            for (DdsReferenceDef.ColumnsInfoItem colInfo : masterRef.getColumnsInfo()) {
                if (printed) {
                    addStringToSqml(sqml, " and ");
                } else {
                    printed = true;
                }
                final DdsColumnDef parentColumn = colInfo.findParentColumn();
                final DdsColumnDef childColumn = colInfo.findChildColumn();
                if (parentColumn != null && childColumn != null) {
                    addStringToSqml(sqml, "M." + parentColumn.getDbName() + "=" + prefix + childColumn.getDbName());
                }
            }
            sqmlb.print(";\n   exception");
            sqmlb.print("\n      when no_data_found then null;"); // avoid mutable trigger exception if several masters deleted by one delete
            sqmlb.print("\n   end;");
        } else {
            final DdsColumnDef classGuidColumn = table.findClassGuidColumn();
            if (classGuidColumn != null) {
                addStringToSqml(sqml, "\n   vClassGuid := " + prefix + classGuidColumn.getDbName() + ";");
            } else {
                addStringToSqml(sqml, "\n   vClassGuid := '" + String.valueOf(Id.Factory.changePrefix(table.getId(), EDefinitionIdPrefix.ADS_ENTITY_CLASS)) + "';");
            }
        }

        addStringToSqml(sqml, "\n   insert into ");

        final TableSqlNameTag auditLog = TableSqlNameTag.Factory.newInstance();
        auditLog.setTableId(Id.Factory.loadFrom(auditLogTable));
        auditLog.setSql("RDX_AU_AUDITLOG");
        sqml.getItems().add(auditLog);

        addStringToSqml(sqml, " (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)\n");
        addStringToSqml(sqml, "   values(");

        final SequenceDbNameTag sq = SequenceDbNameTag.Factory.newInstance();
        sq.setSequenceId(Id.Factory.loadFrom(auditLogSeq));
        sq.setSql("SQN_RDX_AU_AUDITLOGID");
        sqml.getItems().add(sq);

        addStringToSqml(sqml, ".NextVal,SYSTIMESTAMP,vStoreDuration,");
        DbFuncCallTag user = DbFuncCallTag.Factory.newInstance();
        user.setFunctionId(Id.Factory.loadFrom("dfnH4JNIHGXCTOBDCKFAAMPGXSZKU"));
        user.setSql("RDX_Arte.getUserName");
        sqml.getItems().add(user);

        addStringToSqml(sqml, ",");

        DbFuncCallTag station = DbFuncCallTag.Factory.newInstance();
        station.setFunctionId(Id.Factory.loadFrom("dfnI4SB4XOXCTOBDCKFAAMPGXSZKU"));
        station.setSql("RDX_Arte.getStationName");
        sqml.getItems().add(station);

        addStringToSqml(sqml, ",'" + String.valueOf(ownerTableId) + "',vClassGuid,vPID,'" + auditLogType + "',vChangeData);\n");
        addStringToSqml(sqml, "end;");

        org.radixware.kernel.common.sqml.Sqml sqmlOrig = trigger.getBody();
        org.radixware.schemas.xscml.Sqml sqmlOld = org.radixware.schemas.xscml.Sqml.Factory.newInstance();
        sqmlOrig.appendTo(sqmlOld);

        org.radixware.schemas.xscml.Sqml sqmlNew = org.radixware.schemas.xscml.Sqml.Factory.newInstance();
        sqml.appendTo(sqmlNew);

        if (!sqmlOld.xmlText().equals(sqmlNew.xmlText())) {
            if (checkOnly) {
                return false;
            }
            trigger.getBody().loadFrom(sqmlNew);
        }

        return true;
    }

    private static void addStringToSqml(org.radixware.kernel.common.sqml.Sqml sqml, String string) {
        Text text = Text.Factory.newInstance(string);
        sqml.getItems().add(text);
    }

//    private static boolean auditReference(DdsTableDef table, int depth, String prefix, StringBuilder into, StringBuilder from, StringBuilder where) {
//        if (table.getId().toString().equals("tblJBWGL34ATTNBDPK5ABQJO5ADDQ")) {
//            if (depth == 1) {
//                return false;
//            }
//            String curTable = "T" + Integer.toString(depth);
//            into.append(curTable);
//            into.append(".GUID");
//            return true;
//        }
//        Id id = table.getAuditInfo().getAuditReferenceId();
//        if (id != null) {
//            DdsReferenceDef ref = null;
//            for (DdsReferenceDef cur : table.collectOutgoingReferences()) {
//                if (cur.getId().equals(id)) {
//                    ref = cur;
//                    break;
//                }
//            }
//            if (ref != null) {
//                DdsTableDef nextTable = ref.getParentTable();
//                String curTable = "T" + Integer.toString(depth);
//                String nextTableAlias = "T" + Integer.toString(depth + 1);
//                if (!from.toString().equals("")) {
//                    from.append(",");
//                }
//                from.append(nextTable.getDbName());
//                from.append(" ");
//                from.append(nextTableAlias);
//                ColumnsInfoItems columnsInfoItems = ref.getColumnsInfo();
//                for (ColumnsInfoItem cur : columnsInfoItems) {
//                    DdsColumnDef child = cur.getChildColumn();
//                    DdsColumnDef parent = cur.getParentColumn();
//                    if (!where.toString().equals("")) {
//                        where.append(" and ");
//                    }
//                    if (depth > 1) {
//                        where.append(curTable);
//                        where.append(".");
//                        where.append(child.getDbName());
//                        where.append("=");
//                        where.append(nextTableAlias);
//                        where.append(".");
//                        where.append(parent.getDbName());
//                    } else {
//                        where.append(prefix);
//                        where.append(child.getDbName());
//                        where.append("=");
//                        where.append(nextTableAlias);
//                        where.append(".");
//                        where.append(parent.getDbName());
//                    }
//                }
//                return auditReference(nextTable, depth + 1, prefix, into, from, where);
//            }
//        }
//        return false;
//    }
    private static boolean isSaveValueColumn(DdsColumnDef column, ETriggeringEvent event) {
        switch (event) {
            case ON_DELETE:
                return column.getAuditInfo().isSaveValueOnDelete();
            case ON_UPDATE:
                return column.getAuditInfo().isSaveValuesOnUpdate();
            case ON_INSERT:
                return column.getAuditInfo().isSaveValueOnInsert();
        }
        return false;
    }

    private static DbFuncCallTag getForUpdate(DdsColumnDef column) {
        DbFuncCallTag tag = DbFuncCallTag.Factory.newInstance();
        switch (column.getValType()) {
            case BOOL:
            case INT:
                tag.setFunctionId(Id.Factory.loadFrom("dfn5WOW6KKZRBAC3MFHWD3KNPIZVU"));
                tag.setSql("RDX_AUDIT.addChangedValueInt");
                break;
            case CHAR:
            case STR:
                tag.setFunctionId(Id.Factory.loadFrom("dfnMTT5CAY2LBE4LP7GHG3E6RBO3M"));
                tag.setSql("RDX_AUDIT.addChangedValueStr");
                break;
            case NUM:
                tag.setFunctionId(Id.Factory.loadFrom("dfnMECGK3G2QRHZ7AIE2B6FNZ3SEE"));
                tag.setSql("RDX_AUDIT.addChangedValueNum");
                break;
            case DATE_TIME:
                tag.setFunctionId(Id.Factory.loadFrom("dfnVCIU2LTOBZCS3K4H324KCMG6WA"));
                tag.setSql("RDX_AUDIT.addChangedValueDateTime");
                break;
            case CLOB:
                tag.setFunctionId(Id.Factory.loadFrom("dfnGCKTYCNAVJHDXKG7F7L3UGQXBU"));
                tag.setSql("RDX_AUDIT.addChangedValueClob");
                break;
            case BLOB:
                tag.setFunctionId(Id.Factory.loadFrom("dfnXOLIGZFXLRDSVOEWXDULS4F574"));
                tag.setSql("RDX_AUDIT.addChangedValueBlob");
                break;
            case BIN:
                tag.setFunctionId(Id.Factory.loadFrom("dfn35IR2YZLPVFMRGV4RJG46NG5JI"));
                tag.setSql("RDX_AUDIT.addChangedValueRaw");
                break;
            case ARR_STR:
            case ARR_NUM:
            case ARR_INT:
            case ARR_DATE_TIME:
            case ARR_CHAR:
            case ARR_BIN:
            case ARR_REF:
                if (column.getLength() > 0) { // varchar2
                    tag.setFunctionId(Id.Factory.loadFrom("dfnMTT5CAY2LBE4LP7GHG3E6RBO3M"));
                    tag.setSql("RDX_AUDIT.addChangedValueStr");
                } else { // clob
                    tag.setFunctionId(Id.Factory.loadFrom("dfnGCKTYCNAVJHDXKG7F7L3UGQXBU"));
                    tag.setSql("RDX_AUDIT.addChangedValueClob");
                }
                break;
            default:
                return null;
        }
        return tag;
    }

    private static DbFuncCallTag getTagForInsertDelete(DdsColumnDef column) {
        DbFuncCallTag tag = DbFuncCallTag.Factory.newInstance();
        switch (column.getValType()) {
            case BOOL:
            case INT:
                tag.setFunctionId(Id.Factory.loadFrom("dfnRAERWTZSX3NBDPLKABQJO5ADDQ"));
                tag.setSql("RDX_AUDIT.addValueInt");
                break;
            case CHAR:
            case STR:
                tag.setFunctionId(Id.Factory.loadFrom("dfnPQ3VCQBSX3NBDPLKABQJO5ADDQ"));
                tag.setSql("RDX_AUDIT.addValueStr");
                break;
            case NUM:
                tag.setFunctionId(Id.Factory.loadFrom("dfn32HNHTZNX3NBDPLKABQJO5ADDQ"));
                tag.setSql("RDX_AUDIT.addValueNum");
                break;
            case DATE_TIME:
                tag.setFunctionId(Id.Factory.loadFrom("dfnYH6DZCBSX3NBDPLKABQJO5ADDQ"));
                tag.setSql("RDX_AUDIT.addValueDate");
                break;
            case CLOB:
                tag.setFunctionId(Id.Factory.loadFrom("dfnLRRREOFYJRBR5PIELHOKPQ42XM"));
                tag.setSql("RDX_AUDIT.addValueClob");
                break;
            case BLOB:
                tag.setFunctionId(Id.Factory.loadFrom("dfnIPL6KBI26RCE3PIBZJK2EBHGBU"));
                tag.setSql("RDX_AUDIT.addValueBlob");
                break;
            case BIN:
                tag.setFunctionId(Id.Factory.loadFrom("dfnTGD6QCQLZNCORMEEO6BUPWA7BM"));
                tag.setSql("RDX_AUDIT.addValueRaw");
                break;
            case ARR_STR:
            case ARR_NUM:
            case ARR_INT:
            case ARR_DATE_TIME:
            case ARR_CHAR:
            case ARR_BIN:
            case ARR_REF:
                if (column.getLength() > 0) { // varchar2
                    tag.setFunctionId(Id.Factory.loadFrom("dfnPQ3VCQBSX3NBDPLKABQJO5ADDQ"));
                    tag.setSql("RDX_AUDIT.addValueStr");
                } else { // clob
                    tag.setFunctionId(Id.Factory.loadFrom("dfnLRRREOFYJRBR5PIELHOKPQ42XM"));
                    tag.setSql("RDX_AUDIT.addValueClob");
                }
                break;
            default:
                return null;
        }
        return tag;
    }
}
