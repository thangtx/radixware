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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.defs.dds.DdsTriggerDef;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.designer.dds.script.IDdsDefinitionScriptGenerator;
import org.radixware.kernel.designer.dds.script.IScriptGenerationHandler;

/**
 * Trigger Script Generator
 */
public class DdsTriggerScriptGenerator implements IDdsDefinitionScriptGenerator<DdsTriggerDef> {

    protected DdsTriggerScriptGenerator() {
    }

    @Override
    public void getDropScript(CodePrinter cp, DdsTriggerDef trigger) {
        cp.print("drop trigger ").print(trigger.getDbName()).printCommandSeparator();
    }

    @Override
    public boolean isModifiedToDrop(DdsTriggerDef oldTrigger, DdsTriggerDef newTrigger) {
        DdsTableDef oldTbl = oldTrigger.getOwnerTable();
        DdsTableDef newTbl = newTrigger.getOwnerTable();
        DdsTableScriptGenerator tableScriptGenerator = DdsTableScriptGenerator.Factory.newInstance();
        if (tableScriptGenerator.isModifiedToDrop(oldTbl, newTbl)) {
            return true;
        }

        if (oldTrigger.getActuationTime() != newTrigger.getActuationTime()) {
            return true;
        }
        if (!oldTrigger.getTriggeringEvents().equals(newTrigger.getTriggeringEvents())) {
            return true;
        }
        if (newTrigger.getTriggeringEvents().contains(DdsTriggerDef.ETriggeringEvent.ON_UPDATE)) {
            int size = oldTrigger.getColumnsInfo().size();
            if (newTrigger.getColumnsInfo().size() != size) {
                return true;
            }
            for (int i = 0; i < size; i++) {
                Id oldColumnId = oldTrigger.getColumnsInfo().get(i).getColumnId();
                Id newColumnId = oldTrigger.getColumnsInfo().get(i).getColumnId();
                if (!oldColumnId.equals(newColumnId)) {
                    return true;
                }
            }
        }

        if (oldTrigger.isForEachRow() != newTrigger.isForEachRow()) {
            return true;
        }

        Sqml oldBody = oldTrigger.getBody();
        Sqml newBody = newTrigger.getBody();
        return !DdsScriptGeneratorUtils.isTranslatedSqmlEquals(oldBody, newBody);
    }

    @Override
    public void getCreateScript(CodePrinter cp, DdsTriggerDef trigger, IScriptGenerationHandler handler) {
        if (handler != null) {
            handler.onGenerationStarted(trigger, cp);
        }

        DdsTableDef table = trigger.getOwnerTable();

        cp.print("create or replace trigger ").println(trigger.getDbName());

        DdsTriggerDef.EActuationTime actuationTime = trigger.getActuationTime();
        switch (actuationTime) {
            case AFTER:
                cp.print("after");
                break;
            case BEFORE:
                cp.print("before");
                break;
            case INSTEAD_OF:
                cp.print("instead of");
                break;
            default:
                throw new DefinitionError("Illegal trigger actuation time: '" + String.valueOf(actuationTime) + "'", trigger);
        }

        boolean onDelete = trigger.getTriggeringEvents().contains(DdsTriggerDef.ETriggeringEvent.ON_DELETE);
        if (onDelete) {
            cp.print(" delete");
        }
        boolean onInsert = trigger.getTriggeringEvents().contains(DdsTriggerDef.ETriggeringEvent.ON_INSERT);
        if (onInsert) {
            if (onDelete) {
                cp.print(" or");
            }
            cp.print(" insert");
        }
        boolean onUpdate = trigger.getTriggeringEvents().contains(DdsTriggerDef.ETriggeringEvent.ON_UPDATE);
        if (onUpdate) {
            if (onDelete || onInsert) {
                cp.print(" or");
            }
            cp.print(" update");
            int columnCount = trigger.getColumnsInfo().size();
            if (columnCount > 0) {
                cp.print(" of ");
                List<String> columnDbNames = new ArrayList<>(columnCount);
                for (DdsTriggerDef.ColumnInfo columnInfo : trigger.getColumnsInfo()) {
                    DdsColumnDef column = columnInfo.getColumn();
                    columnDbNames.add(column.getDbName());
                }
                Collections.sort(columnDbNames);
                boolean flag = false;
                for (String columnDbName : columnDbNames) {
                    if (flag) {
                        cp.print(", ");
                    } else {
                        flag = true;
                    }
                    cp.print(columnDbName);
                }
            }
        }

        cp.print(" on ").println(table.getDbName());

        if (trigger.isForEachRow()) {
            cp.println("for each row");
        }
        if (trigger.isDisabled()) {
            cp.println("disable");
        }
        Sqml body = trigger.getBody();
        DdsScriptGeneratorUtils.translateSqml(cp, body);

        cp.printCommandSeparator();

        if (trigger.getType() == DdsTriggerDef.EType.FOR_AUDIT) {
            final DdsReferenceDef masterRef = table.findMasterReference();
            final DdsTableDef masterTable = (masterRef != null ? masterRef.findParentTable(masterRef) : null);
            final Id ownerTableId = (masterTable != null ? masterTable.getId() : table.getId());
            final String eventType = trigger.getTriggeringEvents().iterator().next().getValue();
            cp.println("begin");
            cp.println("   RDX_AUDIT.updateTableAuditStateEventType('" + table.getId().toString() + "', '" + eventType + "','" + ownerTableId.toString() + "');");
            cp.print("end;");
            cp.printCommandSeparator();
        }
    }

    @Override
    public void getAlterScript(CodePrinter cp, DdsTriggerDef oldTrigger, DdsTriggerDef newTrigger) {
        String oldDbName = oldTrigger.getDbName();
        String newDbName = newTrigger.getDbName();
        if (!oldDbName.equals(newDbName)) {
            cp.print("alter trigger ").print(oldDbName).print(" rename to ").print(newDbName).printCommandSeparator();
        }

        boolean oldDisable = oldTrigger.isDisabled();
        boolean newDisable = newTrigger.isDisabled();
        boolean disableChanged = (oldDisable != newDisable);
        if (disableChanged) {
            cp.print("alter trigger ").print(newTrigger.getDbName()).print(newDisable ? " disable" : " enable").printCommandSeparator();
        }
    }

    @Override
    public void getReCreateScript(CodePrinter printer, DdsTriggerDef definition, boolean storeData) {
    }

    @Override
    public void getEnableDisableScript(CodePrinter cp, DdsTriggerDef definition, boolean enable) {
    }

    public static final class Factory {

        private Factory() {
        }

        public static DdsTriggerScriptGenerator newInstance() {
            return new DdsTriggerScriptGenerator();
        }
    }

    @Override
    public void getRunRoleScript(CodePrinter printer, DdsTriggerDef definition) {

    }

}
