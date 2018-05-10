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

import org.radixware.kernel.common.defs.dds.DdsPackageDef;
import org.radixware.kernel.common.defs.dds.DdsPlSqlObjectDef;
import org.radixware.kernel.common.defs.dds.DdsPlSqlPartDef;
import org.radixware.kernel.common.defs.dds.DdsTypeDef;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.designer.dds.script.IDdsDefinitionScriptGenerator;
import org.radixware.kernel.designer.dds.script.IScriptGenerationHandler;

public abstract class DdsPlSqlPartScriptGenerator<T extends DdsPlSqlPartDef> implements IDdsDefinitionScriptGenerator<T> {

    protected DdsPlSqlPartScriptGenerator() {
    }

    protected void printSqlClassName(CodePrinter cp, T part) {
        DdsPlSqlObjectDef plSqlObject = part.getPlSqlObjectDef();
        if (plSqlObject instanceof DdsTypeDef) {
            cp.print("type");
        } else if (plSqlObject instanceof DdsPackageDef) {
            cp.print("package");
//            JOptionPane.showMessageDialog(null,"CALL: "+Arrays.toString(new Throwable("CATCH!").getStackTrace()));
        } else {
            throw new IllegalStateException("Illegal object in " + DdsPlSqlHeaderScriptGenerator.class.getName() + ": " + String.valueOf(plSqlObject));
        }
    }

    @Override
    public boolean isModifiedToDrop(T oldPart, T newPart) {
        // PL/SQL objects are recreated by 'create or replace',
        // so, if it renamed, it is need to drop object with old name.
        return !oldPart.getPlSqlObjectDef().getDbName().equals(newPart.getPlSqlObjectDef().getDbName());
    }

    @Override
    public void getDropScript(CodePrinter cp, T part) {
        cp.print("drop ");
        printSqlClassName(cp, part);
        cp.print(" ").print(part.getPlSqlObjectDef().getDbName()).printCommandSeparator();
    }

    @Override
    public void getAlterScript(CodePrinter cp, T oldPart, T newPart) {
        final String oldScript = getCreateScript(oldPart, null /*hundler*/);
        final String newScript = getCreateScript(newPart, null /*hundler*/);
        if (!oldScript.equals(newScript)) {
            cp.print(newScript);
        }
    }

    private String getCreateScript(T part, IScriptGenerationHandler handler) {
        final CodePrinter cp = CodePrinter.Factory.newSqlPrinter();
        
        if (handler != null) {
            handler.onGenerationStarted(part, cp);
        }
        
        getCreateScript(cp, part, handler);

        return cp.toString();
    }

    @Override
    public void getRunRoleScript(CodePrinter cp, T part) {
        cp.print("grant execute on ").print(part.getPlSqlObjectDef().getDbName()).print(" to &USER&_RUN_ROLE").printCommandSeparator();
    }
}
