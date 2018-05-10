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

package org.radixware.kernel.designer.dds.script.defs.postgresql;

import org.radixware.kernel.designer.dds.script.defs.postgresenterprise.*;
import java.util.EnumSet;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsIndexDef;
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


public class PgsDdsIndexScriptGenerator implements IDdsDefinitionScriptGenerator<DdsIndexDef> {

    protected PgsDdsIndexScriptGenerator() {
    }

    @Override
    public void getDropScript(CodePrinter cp, DdsIndexDef idx) {
        cp.print("drop index ").print(idx.getDbName()).printCommandSeparator();
    }

    @Override
    public boolean isModifiedToDrop(DdsIndexDef oldIdx, DdsIndexDef newIdx) {
        DdsTableDef oldTbl = oldIdx.getOwnerTable();
        DdsTableDef newTbl = newIdx.getOwnerTable();
        PgsDdsTableScriptGenerator tableScriptGenerator = PgsDdsTableScriptGenerator.Factory.newInstance();
        if (tableScriptGenerator.isModifiedToDrop(oldTbl, newTbl)) {
            return true;
        }

        int size = oldIdx.getColumnsInfo().size();
        if (newIdx.getColumnsInfo().size() != size) {
            return true;
        }
        for (int i = 0; i < size; i++) {
            DdsIndexDef.ColumnInfo oldColumnInfo = oldIdx.getColumnsInfo().get(i);
            DdsIndexDef.ColumnInfo newColumnInfo = newIdx.getColumnsInfo().get(i);

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

        EnumSet<DdsIndexDef.EDbOption> oldDbOptions = EnumSet.copyOf(oldIdx.getDbOptions());
        EnumSet<DdsIndexDef.EDbOption> newDbOptions = EnumSet.copyOf(newIdx.getDbOptions());
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
    public void getCreateScript(CodePrinter cp, DdsIndexDef idx, IScriptGenerationHandler handler) {
        if (handler != null) {
            handler.onGenerationStarted(idx, cp);
        }

        DdsTableDef table = idx.getOwnerTable();
        String tableDbName = table.getDbName();

        cp.print("create");

        if (idx.getDbOptions().contains(DdsIndexDef.EDbOption.BITMAP)) {
            cp.print(" bitmap");
        } else if (idx.getDbOptions().contains(DdsIndexDef.EDbOption.UNIQUE)) {
            cp.print(" unique");
        }

        cp.print(" index ").print(idx.getDbName()).print(" on ").print(tableDbName).print(" (");

        boolean columnFlag = false;
        for (DdsIndexDef.ColumnInfo columnInfo : idx.getColumnsInfo()) {
            if (columnFlag) {
                cp.print(", ");
            } else {
                columnFlag = true;
            }
            DdsColumnDef column = columnInfo.getColumn();
            cp.print(column.getDbName()).print(' ').print(columnInfo.getOrder().getValue().toLowerCase());
        }

        cp.print(')');

        if (idx.getDbOptions().contains(DdsIndexDef.EDbOption.NOLOGGING)) {
            cp.print(" nologging");
        }

        if (idx.getDbOptions().contains(DdsIndexDef.EDbOption.LOCAL)) {
            DdsScriptGeneratorUtils.printIfPartitioningEnabled(cp, " local");
        }

        String tablespace = TablespaceCalculator.calcTablespaceForIndex(idx);
        if (!tablespace.isEmpty()) {
            cp.print(" tablespace ").print(tablespace);
        }

        if (idx.getDbOptions().contains(DdsIndexDef.EDbOption.INVISIBLE)) {
            cp.print(" invisible");
        }

        if (idx.getDbOptions().contains(DdsIndexDef.EDbOption.REVERSE)) {
            cp.print(" reverse");
        }
        cp.printCommandSeparator();
    }

    @Override
    public void getAlterScript(CodePrinter cp, DdsIndexDef oldIdx, DdsIndexDef newIdx) {
        String oldDbName = oldIdx.getDbName();
        String newDbName = newIdx.getDbName();
        if (!oldDbName.equals(newDbName)) {
            cp.print("alter index ").print(oldDbName).print(" rename to ").print(newDbName).printCommandSeparator();
        }
        boolean oldNoLogging = oldIdx.getDbOptions().contains(DdsIndexDef.EDbOption.NOLOGGING);
        boolean newNoLogging = newIdx.getDbOptions().contains(DdsIndexDef.EDbOption.NOLOGGING);
        if (oldNoLogging != newNoLogging) {
            cp.print("alter index ").print(newIdx.getDbName()).print(' ').print(newNoLogging ? "nologging" : "logging").printCommandSeparator();
        }

        String oldTablespace = TablespaceCalculator.calcTablespaceForIndex(oldIdx);
        String newTablespace = TablespaceCalculator.calcTablespaceForIndex(newIdx);
        if (!oldTablespace.equals(newTablespace) && !newTablespace.isEmpty()) {
            cp.print("alter index ").print(newIdx.getDbName()).print(" rebuild tablespace ").print(newTablespace).printCommandSeparator();
        }
        boolean oldReverse = oldIdx.getDbOptions().contains(DdsIndexDef.EDbOption.REVERSE);
        boolean newReverse = newIdx.getDbOptions().contains(DdsIndexDef.EDbOption.REVERSE);
        if (oldReverse != newReverse) {
            cp.print("alter index ").print(newIdx.getDbName()).print(" rebuild ").print(newReverse ? "reverse" : "noreverse").printCommandSeparator();
        }
    }

    @Override
    public void getReCreateScript(CodePrinter printer, DdsIndexDef definition, boolean storeData) {
        printReCreateScript(definition,printer);
    }

    @Override
    public void getEnableDisableScript(CodePrinter cp, DdsIndexDef definition, boolean enable) {
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
        public static PgsDdsIndexScriptGenerator newInstance() {
            return new PgsDdsIndexScriptGenerator();
        }
    }

    @Override
    public void getRunRoleScript(CodePrinter printer, DdsIndexDef definition) {

    }

}
