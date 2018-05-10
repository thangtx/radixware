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

import org.radixware.kernel.designer.dds.script.IScriptGenerationHandler;
import org.radixware.kernel.common.defs.dds.DdsCheckConstraintDef;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.defs.dds.DdsViewDef;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.designer.dds.script.DdsScriptGeneratorUtils;

public class EdbDdsCheckConstraintScriptGenerator extends EdbDdsConstraintScriptGenerator<DdsCheckConstraintDef> {

    protected EdbDdsCheckConstraintScriptGenerator() {
    }

    @Override
    protected DdsTableDef findOwnerTable(DdsCheckConstraintDef constraint) {
        DdsColumnDef column = constraint.getOwnerColumn();
        DdsTableDef table = column.getOwnerTable();
        return table;
    }

    @Override
    public boolean isModifiedToDrop(DdsCheckConstraintDef oldCheckConstraint, DdsCheckConstraintDef newCheckConstraint) {
        DdsColumnDef oldColumn = oldCheckConstraint.getOwnerColumn();
        DdsColumnDef newColumn = newCheckConstraint.getOwnerColumn();
        EdbDdsColumnScriptGenerator columnScriptGenerator = EdbDdsColumnScriptGenerator.Factory.newInstance();
        if (columnScriptGenerator.isModifiedToDrop(oldColumn, newColumn)) {
            return true;
        }

        if (super.isModifiedToDrop(oldCheckConstraint, newCheckConstraint)) {
            return true;
        }

        Sqml oldConstraintCondition = oldCheckConstraint.getCondition();
        Sqml newConstraintCondition = newCheckConstraint.getCondition();
        return !DdsScriptGeneratorUtils.isTranslatedSqmlEquals(oldConstraintCondition, newConstraintCondition);
    }

    @Override
    public void getRunRoleScript(CodePrinter printer, DdsCheckConstraintDef definition) {
        
    }
    

    @Override
    public void getCreateScript(CodePrinter cp, DdsCheckConstraintDef checkConstraint, IScriptGenerationHandler handler) {
        if (handler != null) {
            handler.onGenerationStarted(checkConstraint, cp);
        }

        final DdsColumnDef column = checkConstraint.getOwnerColumn();
        final DdsTableDef table = column.getOwnerTable();
        final String tableDbName = table.getDbName();

        cp.print("alter ");
        if (table instanceof DdsViewDef) {
            cp.print("view ");
        } else {
            cp.print("table ");
        };
        cp.print(tableDbName).print(" add constraint ").print(checkConstraint.getDbName()).print(" check (");
        final Sqml condition = checkConstraint.getCondition();
        DdsScriptGeneratorUtils.translateSqml(cp, condition);
        cp.print(")");

        getCreateDbOptionsScript(cp, checkConstraint.getDbOptions());

        cp.printCommandSeparator();
    }

    @Override
    public void getReCreateScript(CodePrinter printer, DdsCheckConstraintDef definition, boolean storeData) {
    }

    @Override
    public void getEnableDisableScript(CodePrinter cp, DdsCheckConstraintDef definition, boolean enable) {
    }

    public static final class Factory {

        private Factory() {
        }

        public static EdbDdsCheckConstraintScriptGenerator newInstance() {
            return new EdbDdsCheckConstraintScriptGenerator();
        }
    }
}
