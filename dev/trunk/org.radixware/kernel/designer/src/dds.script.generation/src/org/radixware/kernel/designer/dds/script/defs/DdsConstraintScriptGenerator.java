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

import java.util.EnumSet;
import org.radixware.kernel.common.defs.dds.DdsConstraintDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.defs.dds.DdsUniqueConstraintDef;
import org.radixware.kernel.common.defs.dds.DdsViewDef;
import org.radixware.kernel.common.enums.EDdsConstraintDbOption;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.designer.dds.script.IDdsDefinitionScriptGenerator;


public abstract class DdsConstraintScriptGenerator<T extends DdsConstraintDef> implements IDdsDefinitionScriptGenerator<T> {

    protected DdsConstraintScriptGenerator() {
    }

    protected abstract DdsTableDef findOwnerTable(T constraint);

    @Override
    public void getDropScript(CodePrinter cp, T constraint) {
        DdsTableDef ownerTable = findOwnerTable(constraint);

        cp.print("alter ");
        if (ownerTable instanceof DdsViewDef) {
            cp.print("view ");
        } else {
            cp.print("table ");
        }
        cp.print(ownerTable.getDbName());
        cp.print(" drop constraint " + constraint.getDbName());
        if (constraint instanceof DdsUniqueConstraintDef) {
            cp.print(" cascade");
        }
        cp.printCommandSeparator();
    }

    @Override
    public boolean isModifiedToDrop(T oldConstraint, T newConstraint) {
        boolean oldDeferrable = oldConstraint.getDbOptions().contains(EDdsConstraintDbOption.DEFERRABLE);
        boolean newDeferrable = newConstraint.getDbOptions().contains(EDdsConstraintDbOption.DEFERRABLE);
        if (oldDeferrable != newDeferrable) {
            return true;
        }
        boolean oldInitiallyDeferred = oldConstraint.getDbOptions().contains(EDdsConstraintDbOption.INITIALLY_DEFERRED);
        boolean newInitiallyDeferred = newConstraint.getDbOptions().contains(EDdsConstraintDbOption.INITIALLY_DEFERRED);
        if (oldInitiallyDeferred != newInitiallyDeferred) {
            return true;
        }
        return false;
    }

    @Override
    public void getAlterScript(CodePrinter cp, T oldConstraint, T newConstraint) {
        DdsTableDef ownerTable = findOwnerTable(newConstraint);

        String oldDbName = oldConstraint.getDbName();
        String newDbName = newConstraint.getDbName();
        if (!oldDbName.equals(newDbName)) {
            cp.print("alter ");
            if (ownerTable instanceof DdsViewDef) {
                cp.print("view ");
            } else {
                cp.print("table ");
            }
            cp.print(ownerTable.getDbName());
            cp.print(" rename constraint ");
            cp.print(oldDbName);
            cp.print(" to ");
            cp.print(newDbName);
            cp.printCommandSeparator();
        }

        boolean oldRely = oldConstraint.getDbOptions().contains(EDdsConstraintDbOption.RELY);
        boolean newRely = newConstraint.getDbOptions().contains(EDdsConstraintDbOption.RELY);
        boolean relyChanged = (oldRely != newRely);

        boolean oldDisable = oldConstraint.getDbOptions().contains(EDdsConstraintDbOption.DISABLE);
        boolean newDisable = newConstraint.getDbOptions().contains(EDdsConstraintDbOption.DISABLE);
        boolean disableChanged = (oldDisable != newDisable);

        boolean oldNovalidate = oldConstraint.getDbOptions().contains(EDdsConstraintDbOption.NOVALIDATE);
        boolean newNovalidate = newConstraint.getDbOptions().contains(EDdsConstraintDbOption.NOVALIDATE);
        boolean novalidateChanged = (oldNovalidate != newNovalidate);

        if (relyChanged || disableChanged || novalidateChanged) {
            cp.print("alter ");
            if (ownerTable instanceof DdsViewDef) {
                cp.print("view ");
            } else {
                cp.print("table ");
            }
            cp.print(ownerTable.getDbName());
            cp.print(" modify constraint ");
            cp.print(newConstraint.getDbName());

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

    public void getCreateDbOptionsScript(CodePrinter cp, EnumSet<EDdsConstraintDbOption> dbOptions) {
        if (dbOptions.contains(EDdsConstraintDbOption.DEFERRABLE)) {
            cp.print(" deferrable");
        }
        if (dbOptions.contains(EDdsConstraintDbOption.INITIALLY_DEFERRED)) {
            cp.print(" initially deferred");
        }
        if (dbOptions.contains(EDdsConstraintDbOption.RELY)) {
            cp.print(" rely");
        }
        if (dbOptions.contains(EDdsConstraintDbOption.DISABLE)) {
            cp.print(" disable");
        }
        if (dbOptions.contains(EDdsConstraintDbOption.NOVALIDATE)) {
            cp.print(" novalidate");
        }
    }
}
