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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.defs.dds.DdsTriggerDef;
import org.radixware.kernel.common.enums.EDatabaseType;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.scml.NestedCodePrinter;
import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.designer.dds.script.DdsScriptGeneratorUtils;
import org.radixware.kernel.designer.dds.script.IDdsDefinitionScriptGenerator;
import org.radixware.kernel.designer.dds.script.IScriptGenerationHandler;
import org.radixware.kernel.common.scml.ScmlCodePrinter;

/**
 * Trigger Script Generator
 */
public class EdbDdsTriggerScriptGenerator implements IDdsDefinitionScriptGenerator<DdsTriggerDef> {

    protected EdbDdsTriggerScriptGenerator() {
    }

    @Override
    public void getDropScript(CodePrinter cp, DdsTriggerDef trigger) {
        cp.print("drop trigger ").print(trigger.getDbName()).printCommandSeparator();
    }

    @Override
    public boolean isModifiedToDrop(DdsTriggerDef oldTrigger, DdsTriggerDef newTrigger) {
        DdsTableDef oldTbl = oldTrigger.getOwnerTable();
        DdsTableDef newTbl = newTrigger.getOwnerTable();
        EdbDdsTableScriptGenerator tableScriptGenerator = EdbDdsTableScriptGenerator.Factory.newInstance();
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
            case AFTER: cp.print("after"); break;
            case BEFORE: cp.print("before"); break;
            case INSTEAD_OF: cp.print("instead of"); break;
            default: throw new DefinitionError("Illegal trigger actuation time: '" + String.valueOf(actuationTime) + "'", trigger);
        }

        final StringBuilder flags = new StringBuilder();
        
        if (trigger.getTriggeringEvents().contains(DdsTriggerDef.ETriggeringEvent.ON_DELETE)) {
            flags.append("or delete ");
        }
        if (trigger.getTriggeringEvents().contains(DdsTriggerDef.ETriggeringEvent.ON_INSERT)) {
            flags.append("or insert ");
        }
        if (trigger.getTriggeringEvents().contains(DdsTriggerDef.ETriggeringEvent.ON_UPDATE)) {
            flags.append("or update ");
            
            int columnCount = trigger.getColumnsInfo().size();
            
            if (columnCount > 0) {
                final List<String> columnDbNames = new ArrayList<>(columnCount);
                
                for (DdsTriggerDef.ColumnInfo columnInfo : trigger.getColumnsInfo()) {
                    columnDbNames.add(columnInfo.getColumn().getDbName());
                }
                Collections.sort(columnDbNames);
                
                String  prefix = "of ";
                for (String columnDbName : columnDbNames) {
                    flags.append(prefix).append(columnDbName);
                    prefix = ", ";
                }
            }
        }
        if (flags.length() > 0) {
            cp.print(flags.toString().substring(2));
        }

        cp.print(" on ").println(table.getDbName());

        if (trigger.isForEachRow()) {
            cp.println("for each row");
        }
//        if (trigger.isDisabled()) {
//            cp.println("disable");
//        }
        
        final CodePrinter temp = CodePrinter.Factory.newSqlPrinter(CodePrinter.Factory.newSqlPrinter(),EDatabaseType.ENTERPRISEDB);
        final Sqml body = trigger.getBody();
        
        DdsScriptGeneratorUtils.translateSqml(temp, body);

        final String bodyString = temp.toString();
        cp.print(bodyString.replace("return;","return null;")).printCommandSeparator();

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

        public static EdbDdsTriggerScriptGenerator newInstance() {
            return new EdbDdsTriggerScriptGenerator();
        }
    }

    @Override
    public void getRunRoleScript(CodePrinter printer, DdsTriggerDef definition) {

    }

}
