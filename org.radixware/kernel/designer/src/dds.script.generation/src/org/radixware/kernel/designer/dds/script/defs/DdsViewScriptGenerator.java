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

import org.radixware.kernel.common.defs.dds.DdsViewDef;
import org.radixware.kernel.common.enums.EDdsViewWithOption;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.designer.dds.script.IDdsDefinitionScriptGenerator;
import org.radixware.kernel.designer.dds.script.IScriptGenerationHandler;


public class DdsViewScriptGenerator implements IDdsDefinitionScriptGenerator<DdsViewDef> {

    protected DdsViewScriptGenerator() {
    }

    @Override
    public void getDropScript(CodePrinter cp, DdsViewDef view) {
        String result = "drop view " + view.getDbName();
        cp.printCommand(result);
    }

    @Override
    public boolean isModifiedToDrop(DdsViewDef oldView, DdsViewDef newView) {
        if (oldView.getWithOption() != newView.getWithOption()) {
            return true;
        }
        Sqml oldQuery = oldView.getQuery();
        Sqml newQuery = newView.getQuery();
        if (!DdsScriptGeneratorUtils.isTranslatedSqmlEquals(oldQuery, newQuery)) {
            return true;
        }

        return false;
    }

    @Override
    public void getCreateScript(CodePrinter cp, DdsViewDef view, IScriptGenerationHandler handler) {
        if (handler != null) {
            handler.onGenerationStarted(view, cp);
        }

        cp.print("create or replace view ");
        cp.print(view.getDbName());
        cp.println(" as");

        Sqml viewQuery = view.getQuery();
        DdsScriptGeneratorUtils.translateSqml(cp, viewQuery);

        switch (view.getWithOption()) {
            case CHECK_OPTION:
                cp.print("\nwith check option");
                break;
            case READ_ONLY:
                cp.print("\nwith read only");
                break;
        }

        cp.printCommandSeparator();
        getRunRoleScript(cp, view);
    }

    @Override
    public void getRunRoleScript(CodePrinter cp, DdsViewDef view) {
        cp.print("grant select");
        if (view.getWithOption() != EDdsViewWithOption.READ_ONLY) {
            cp.print(", delete");
        }
        cp.print(" on ");
        cp.print(view.getDbName());
        cp.print(" to &USER&_RUN_ROLE");
        cp.printCommandSeparator();
        // System.out.println("grant select on " + view.getDbName() + " to &USER&_RUN_ROLE\n/");
    }

    @Override
    public void getAlterScript(CodePrinter cp, DdsViewDef oldView, DdsViewDef newView) {
        String oldDbName = oldView.getDbName();
        String newDbName = newView.getDbName();
        if (!oldDbName.equals(newDbName)) {
            cp.print("rename ");
            cp.print(oldDbName);
            cp.print(" to ");
            cp.print(newDbName);
            cp.printCommandSeparator();
            getRunRoleScript(cp, newView);
        }
    }

    public static final class Factory {

        private Factory() {
        }

        public static DdsViewScriptGenerator newInstance() {
            return new DdsViewScriptGenerator();
        }
    }
}
