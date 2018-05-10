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
import org.radixware.kernel.common.defs.dds.DdsConstraintDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.defs.dds.DdsUniqueConstraintDef;
import org.radixware.kernel.common.defs.dds.DdsViewDef;
import org.radixware.kernel.common.enums.EDdsConstraintDbOption;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.designer.dds.script.IDdsDefinitionScriptGenerator;


public abstract class OraDdsConstraintScriptGenerator<T extends DdsConstraintDef> implements IDdsDefinitionScriptGenerator<T> {

    protected OraDdsConstraintScriptGenerator() {
    }

    protected abstract DdsTableDef findOwnerTable(T constraint);

    @Override
    public void getDropScript(final CodePrinter cp, final T constraint) {
        if (cp == null) {
            throw new IllegalArgumentException("Code printer can't be null");
        }
        else if (constraint == null) {
            throw new IllegalArgumentException("Constraint can't be null");
        }
        else {
            final DdsTableDef ownerTable = findOwnerTable(constraint);

            cp.print("alter ").print(ownerTable instanceof DdsViewDef ? "view " : "table ").print(ownerTable.getDbName()).print(" drop constraint ").print(constraint.getDbName());
            if (constraint instanceof DdsUniqueConstraintDef) {
                cp.print(" cascade");
            }
            cp.printCommandSeparator();
        }
    }

    @Override
    public boolean isModifiedToDrop(final T oldConstraint, final T newConstraint) {
        if (oldConstraint == null) {
            throw new IllegalArgumentException("Old constraint can't be null");
        }
        else if (newConstraint == null) {
            throw new IllegalArgumentException("New constraint can't be null");
        }
        else {
            boolean oldDeferrable = oldConstraint.getDbOptions().contains(EDdsConstraintDbOption.DEFERRABLE);
            boolean newDeferrable = newConstraint.getDbOptions().contains(EDdsConstraintDbOption.DEFERRABLE);
            if (oldDeferrable != newDeferrable) {
                return true;
            }
            boolean oldInitiallyDeferred = oldConstraint.getDbOptions().contains(EDdsConstraintDbOption.INITIALLY_DEFERRED);
            boolean newInitiallyDeferred = newConstraint.getDbOptions().contains(EDdsConstraintDbOption.INITIALLY_DEFERRED);
            return oldInitiallyDeferred != newInitiallyDeferred;
        }
    }

    @Override
    public void getAlterScript(final CodePrinter cp, final T oldConstraint, final T newConstraint) {
        if (cp == null) {
            throw new IllegalArgumentException("Code printer can't be null");
        }
        else if (oldConstraint == null) {
            throw new IllegalArgumentException("Old constraint can't be null");
        }
        else if (newConstraint == null) {
            throw new IllegalArgumentException("New constraint can't be null");
        }
        else {
            final DdsTableDef ownerTable = findOwnerTable(newConstraint);
            final String oldDbName = oldConstraint.getDbName();
            final String newDbName = newConstraint.getDbName();
            
            if (!oldDbName.equals(newDbName)) {
                cp.print("alter ");
                if (ownerTable instanceof DdsViewDef) {
                    cp.print("view ");
                } else {
                    cp.print("table ");
                }
                cp.print(ownerTable.getDbName()).print(" rename constraint ").print(oldDbName).print(" to ").print(newDbName).printCommandSeparator();
            }

            final boolean oldRely = oldConstraint.getDbOptions().contains(EDdsConstraintDbOption.RELY);
            final boolean newRely = newConstraint.getDbOptions().contains(EDdsConstraintDbOption.RELY);
            final boolean relyChanged = (oldRely != newRely);

            final boolean oldDisable = oldConstraint.getDbOptions().contains(EDdsConstraintDbOption.DISABLE);
            final boolean newDisable = newConstraint.getDbOptions().contains(EDdsConstraintDbOption.DISABLE);
            final boolean disableChanged = (oldDisable != newDisable);

            final boolean oldNovalidate = oldConstraint.getDbOptions().contains(EDdsConstraintDbOption.NOVALIDATE);
            final boolean newNovalidate = newConstraint.getDbOptions().contains(EDdsConstraintDbOption.NOVALIDATE);
            final boolean novalidateChanged = (oldNovalidate != newNovalidate);

            if (relyChanged || disableChanged || novalidateChanged) {
                cp.print("alter ").print(ownerTable instanceof DdsViewDef ? "view " : "table ").print(ownerTable.getDbName()).print(" modify constraint ").print(newConstraint.getDbName());

                if (relyChanged) {
                    cp.print(newRely ? " rely" : " norely");
                }
                if (disableChanged) {
                    cp.print(newDisable ? " disable" : " enable");
                }
                if (novalidateChanged) {
                    cp.print(newNovalidate ? " novalidate" : " validate");
                }
                cp.printCommandSeparator();
            }
        }
    }

    @Deprecated
    public void getCreateDbOptionsScript(final CodePrinter cp, final EnumSet<EDdsConstraintDbOption> dbOptions) {
        if (cp == null) {
            throw new IllegalArgumentException("Code printer can't be null");
        }
        else if (dbOptions == null) {
            throw new IllegalArgumentException("Database options can't be null");
        }
        else {
            cp.print(getCreateDbOptionsScript(dbOptions));
        }
    }

    public String getCreateDbOptionsScript(final EnumSet<EDdsConstraintDbOption> dbOptions) {
        if (dbOptions == null) {
            throw new IllegalArgumentException("Database options can't be null");
        }
        else {
            final StringBuilder sb = new StringBuilder();
            
            if (dbOptions.contains(EDdsConstraintDbOption.DEFERRABLE)) {
                sb.append(" deferrable");
            }
            if (dbOptions.contains(EDdsConstraintDbOption.INITIALLY_DEFERRED)) {
                sb.append(" initially deferred");
            }
            if (dbOptions.contains(EDdsConstraintDbOption.RELY)) {
                sb.append(" rely");
            }
            if (dbOptions.contains(EDdsConstraintDbOption.DISABLE)) {
                sb.append(" disable");
            }
            if (dbOptions.contains(EDdsConstraintDbOption.NOVALIDATE)) {
                sb.append(" novalidate");
            }
            return sb.toString();
        }
    }
}