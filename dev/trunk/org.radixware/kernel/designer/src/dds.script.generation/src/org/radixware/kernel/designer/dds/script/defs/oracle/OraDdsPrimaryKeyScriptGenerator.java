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

package org.radixware.kernel.designer.dds.script.defs.oracle;

import java.util.EnumSet;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsIndexDef;
import org.radixware.kernel.common.defs.dds.DdsPrimaryKeyDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.defs.dds.DdsUniqueConstraintDef;
import org.radixware.kernel.common.defs.dds.utils.DbTypeUtils;
import org.radixware.kernel.common.defs.dds.utils.TablespaceCalculator;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.designer.dds.script.DdsScriptGeneratorUtils;
import org.radixware.kernel.designer.dds.script.IDdsDefinitionScriptGenerator;
import org.radixware.kernel.designer.dds.script.IScriptGenerationHandler;
import org.radixware.kernel.designer.dds.script.ScriptGenerator;
import org.radixware.kernel.designer.dds.script.ScriptGeneratorImpl;


public class OraDdsPrimaryKeyScriptGenerator implements IDdsDefinitionScriptGenerator<DdsPrimaryKeyDef> {

    protected OraDdsPrimaryKeyScriptGenerator() {
    }

    @Override
    public boolean isModifiedToDrop(DdsPrimaryKeyDef oldDefinition, DdsPrimaryKeyDef newDefinition) {
        DdsTableDef oldTbl = oldDefinition.getOwnerTable();
        DdsTableDef newTbl = newDefinition.getOwnerTable();
        OraDdsTableScriptGenerator tableScriptGenerator = OraDdsTableScriptGenerator.Factory.newInstance();
        if (tableScriptGenerator.isModifiedToDrop(oldTbl, newTbl)) {
            return true;
        }

        int size = oldDefinition.getColumnsInfo().size();
        if (newDefinition.getColumnsInfo().size() != size) {
            return true;
        }
        for (int i = 0; i < size; i++) {
            DdsIndexDef.ColumnInfo oldColumnInfo = oldDefinition.getColumnsInfo().get(i);
            DdsIndexDef.ColumnInfo newColumnInfo = newDefinition.getColumnsInfo().get(i);

            Id oldColumnId = oldColumnInfo.getColumnId();
            Id newColumnId = newColumnInfo.getColumnId();
            if (!oldColumnId.equals(newColumnId)) {
                return true;
            }
            String oldColumnDbType = oldColumnInfo.getColumn().getDbType();
            String newColumnDbType = newColumnInfo.getColumn().getDbType();
            if (!DbTypeUtils.isDbTypeEquals(oldColumnDbType, newColumnDbType)) {
                return true;
            }

            if (oldColumnInfo.getOrder() != newColumnInfo.getOrder()) {
                return true;
            }
        }

        EnumSet<DdsIndexDef.EDbOption> oldDbOptions = EnumSet.copyOf(oldDefinition.getDbOptions());
        EnumSet<DdsIndexDef.EDbOption> newDbOptions = EnumSet.copyOf(newDefinition.getDbOptions());
        oldDbOptions.remove(DdsIndexDef.EDbOption.NOLOGGING);
        newDbOptions.remove(DdsIndexDef.EDbOption.NOLOGGING);
        newDbOptions.remove(DdsIndexDef.EDbOption.REVERSE);
        oldDbOptions.remove(DdsIndexDef.EDbOption.REVERSE);
        if (oldDbOptions.contains(DdsIndexDef.EDbOption.BITMAP)) {
            oldDbOptions.remove(DdsIndexDef.EDbOption.UNIQUE);
        }
        if (newDbOptions.contains(DdsIndexDef.EDbOption.BITMAP)) {
            newDbOptions.remove(DdsIndexDef.EDbOption.UNIQUE);
        }
        return !oldDbOptions.equals(newDbOptions);
    }

    @Override
    public void getDropScript(CodePrinter cp, DdsPrimaryKeyDef definition) {
        cp.print("drop index ");
        cp.print(definition.getDbName());
        cp.printCommandSeparator();
    }

    @Override
    public void getCreateScript(CodePrinter cp, DdsPrimaryKeyDef definition, IScriptGenerationHandler handler) {
        if (handler != null) {
            handler.onGenerationStarted(definition, cp);
        }

        DdsTableDef table = definition.getOwnerTable();
        String tableDbName = table.getDbName();

        cp.print("create");

        if (definition.getDbOptions().contains(DdsIndexDef.EDbOption.BITMAP)) {
            cp.print(" bitmap");
        } else if (definition.getDbOptions().contains(DdsIndexDef.EDbOption.UNIQUE)) {
            cp.print(" unique");
        }

        cp.print(" index ");
        cp.print(definition.getDbName());
        cp.print(" on ");
        cp.print(tableDbName);
        cp.print(" (");

        boolean columnFlag = false;
        for (DdsIndexDef.ColumnInfo columnInfo : definition.getColumnsInfo()) {
            if (columnFlag) {
                cp.print(", ");
            } else {
                columnFlag = true;
            }
            DdsColumnDef column = columnInfo.getColumn();
            cp.print(column.getDbName());
            cp.print(' ');
            cp.print(columnInfo.getOrder().getValue().toLowerCase());
        }

        cp.print(')');

        if (definition.getDbOptions().contains(DdsIndexDef.EDbOption.NOLOGGING)) {
            cp.print(" nologging");
        }

        if (definition.getDbOptions().contains(DdsIndexDef.EDbOption.LOCAL)) {
            DdsScriptGeneratorUtils.printIfPartitioningEnabled(cp, " local");
        }

        String tablespace = TablespaceCalculator.calcTablespaceForIndex(definition);
        if (!tablespace.isEmpty()) {
            cp.print(" tablespace ");
            cp.print(tablespace);
        }

        if (definition.getDbOptions().contains(DdsIndexDef.EDbOption.INVISIBLE)) {
            cp.print(" invisible");
        }

        if (definition.getDbOptions().contains(DdsIndexDef.EDbOption.REVERSE)) {
            cp.print(" reverse");
        }
        cp.printCommandSeparator();
    }

    @Override
    public void getAlterScript(CodePrinter cp, DdsPrimaryKeyDef oldDefinition, DdsPrimaryKeyDef newDefinition) {
        String oldDbName = oldDefinition.getDbName();
        String newDbName = newDefinition.getDbName();
        if (!oldDbName.equals(newDbName)) {
            cp.print("alter index ");
            cp.print(oldDbName);
            cp.print(" rename to ");
            cp.print(newDbName);
            cp.printCommandSeparator();
        }
        boolean oldNoLogging = oldDefinition.getDbOptions().contains(DdsIndexDef.EDbOption.NOLOGGING);
        boolean newNoLogging = newDefinition.getDbOptions().contains(DdsIndexDef.EDbOption.NOLOGGING);
        if (oldNoLogging != newNoLogging) {
            cp.print("alter index ");
            cp.print(newDefinition.getDbName());
            cp.print(' ');
            cp.print(newNoLogging ? "nologging" : "logging");
            cp.printCommandSeparator();
        }

        String oldTablespace = TablespaceCalculator.calcTablespaceForIndex(oldDefinition);
        String newTablespace = TablespaceCalculator.calcTablespaceForIndex(newDefinition);
        if (!oldTablespace.equals(newTablespace) && !newTablespace.isEmpty()) {
            cp.print("alter index ");
            cp.print(newDefinition.getDbName());
            cp.print(" rebuild tablespace ");
            cp.print(newTablespace);
            cp.printCommandSeparator();
        }
        boolean oldReverse = oldDefinition.getDbOptions().contains(DdsIndexDef.EDbOption.REVERSE);
        boolean newReverse = newDefinition.getDbOptions().contains(DdsIndexDef.EDbOption.REVERSE);
        if (oldReverse != newReverse) {
            cp.print("alter index ");
            cp.print(newDefinition.getDbName());
            cp.print(" rebuild ");
            cp.print(newReverse ? "reverse" : "noreverse");
            cp.printCommandSeparator();
        }
    }

    @Override
    public void getRunRoleScript(CodePrinter printer, DdsPrimaryKeyDef definition) {
    }

    @Override
    public void getReCreateScript(CodePrinter printer, DdsPrimaryKeyDef definition, boolean storeData) {
        printReCreateScript(definition, printer);
    }

    @Override
    public void getEnableDisableScript(CodePrinter cp, DdsPrimaryKeyDef definition, boolean enable) {
    }

    private static void printReCreateScript(DdsIndexDef index, CodePrinter printer) {
//        DdsIndexScriptGenerator generator = new DdsIndexScriptGenerator();
        final ScriptGenerator instance = ScriptGeneratorImpl.Factory.newCreationInstance(index);
        IDdsDefinitionScriptGenerator<DdsIndexDef> generator = instance.getDefinitionScriptGenerator(index);
        
        //drop constraint
        DdsUniqueConstraintDef constraint = index.getOwnerTable().getPrimaryKey().getUniqueConstraint();
        boolean recreateConstraint = false;
        if (constraint != null && constraint.getOwnerIndex() == index) {
            instance.getDefinitionScriptGenerator(constraint).getDropScript(printer, constraint);
            recreateConstraint = true;
        }
        //drop index
        generator.getDropScript(printer, index);
        //generate create script
        generator.getCreateScript(printer, index, IScriptGenerationHandler.NOOP_HANDLER);
        //add constraint if any
        if (recreateConstraint) {
            instance.getDefinitionScriptGenerator(constraint).getCreateScript(printer, constraint, IScriptGenerationHandler.NOOP_HANDLER);
        }
    }
    
    
    public static final class Factory {

        private Factory() {
        }

        @SuppressWarnings("unchecked")
        public static OraDdsPrimaryKeyScriptGenerator newInstance() {
            return new OraDdsPrimaryKeyScriptGenerator();
        }
    }
}
