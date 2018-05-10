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
import org.radixware.kernel.designer.dds.script.IScriptGenerationHandler;
import org.radixware.kernel.common.defs.dds.DdsCheckConstraintDef;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.defs.dds.DdsViewDef;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.sqml.Sqml;

public class DdsCheckConstraintScriptGenerator extends DdsConstraintScriptGenerator<DdsCheckConstraintDef> {

    protected DdsCheckConstraintScriptGenerator() {
    }

    @Override
    protected DdsTableDef findOwnerTable(DdsCheckConstraintDef constraint) {
        DdsColumnDef column = constraint.getOwnerColumn();
        DdsTableDef table = column.getOwnerTable();
        return table;
    }

    @Override
    public boolean isModifiedToDrop(final DdsCheckConstraintDef oldCheckConstraint, final DdsCheckConstraintDef newCheckConstraint) {
        if (oldCheckConstraint == null) {
            throw new IllegalArgumentException("Old constraint can't be null");
        }
        else if (newCheckConstraint == null) {
            throw new IllegalArgumentException("New constraint can't be null");
        }
        else {
            final DdsColumnDef oldColumn = oldCheckConstraint.getOwnerColumn();
            final DdsColumnDef newColumn = newCheckConstraint.getOwnerColumn();
            final DdsColumnScriptGenerator columnScriptGenerator = DdsColumnScriptGenerator.Factory.newInstance();
            
            if (columnScriptGenerator.isModifiedToDrop(oldColumn, newColumn)) {
                return true;
            }

            if (super.isModifiedToDrop(oldCheckConstraint, newCheckConstraint)) {
                return true;
            }

            final Sqml oldConstraintCondition = oldCheckConstraint.getCondition();
            final Sqml newConstraintCondition = newCheckConstraint.getCondition();
            return !DdsScriptGeneratorUtils.isTranslatedSqmlEquals(oldConstraintCondition, newConstraintCondition);
        }
    }

    @Override
    public void getRunRoleScript(final CodePrinter printer, final DdsCheckConstraintDef definition) {
        if (printer == null) {
            throw new IllegalArgumentException("Code printer can't be null");
        }
        else if (definition == null) {
            throw new IllegalArgumentException("Check constraint printer can't be null");
        }
    }

    @Override
    public void getCreateScript(CodePrinter cp, DdsCheckConstraintDef checkConstraint, IScriptGenerationHandler handler) {
        if (cp == null) {
            throw new IllegalArgumentException("Code printer can't be null");
        }
        else if (checkConstraint == null) {
            throw new IllegalArgumentException("Check constraint printer can't be null");
        }
        else {
            if (handler != null) {
                handler.onGenerationStarted(checkConstraint, cp);
            }

            final DdsColumnDef column = checkConstraint.getOwnerColumn();
            final DdsTableDef table = column.getOwnerTable();
            final String tableDbName = table.getDbName();

            cp.print("alter ").print(table instanceof DdsViewDef ? "view " : "table ").print(tableDbName).print(" add constraint ").print(checkConstraint.getDbName()).print(" check (");
            DdsScriptGeneratorUtils.translateSqml(cp, checkConstraint.getCondition());
            cp.print(")");

            getCreateDbOptionsScript(cp, checkConstraint.getDbOptions());

            cp.printCommandSeparator();
        }
    }

    @Override
    public void getReCreateScript(final CodePrinter printer, final DdsCheckConstraintDef definition, final boolean storeData) {
        if (printer == null) {
            throw new IllegalArgumentException("Code printer can't be null");
        }
        else if (definition == null) {
            throw new IllegalArgumentException("Check constraint printer can't be null");
        }
    }

    @Override
    public void getEnableDisableScript(final CodePrinter cp, final DdsCheckConstraintDef definition, final boolean enable) {
        if (cp == null) {
            throw new IllegalArgumentException("Code printer can't be null");
        }
        else if (definition == null) {
            throw new IllegalArgumentException("Check constraint printer can't be null");
        }
    }

    public static final class Factory {

        private Factory() {
        }

        public static DdsCheckConstraintScriptGenerator newInstance() {
            return new DdsCheckConstraintScriptGenerator();
        }
    }
}
