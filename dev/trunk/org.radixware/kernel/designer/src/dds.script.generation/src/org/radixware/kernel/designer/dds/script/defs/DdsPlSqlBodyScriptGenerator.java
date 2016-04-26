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

import org.radixware.kernel.common.defs.dds.DdsCustomTextDef;
import org.radixware.kernel.common.defs.dds.DdsFunctionDef;
import org.radixware.kernel.common.defs.dds.DdsPlSqlBodyDef;
import org.radixware.kernel.common.defs.dds.DdsPlSqlObjectDef;
import org.radixware.kernel.common.defs.dds.DdsPlSqlObjectItemDef;
import org.radixware.kernel.common.defs.dds.DdsPrototypeDef;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.designer.dds.script.IScriptGenerationHandler;


public class DdsPlSqlBodyScriptGenerator extends DdsPlSqlPartScriptGenerator<DdsPlSqlBodyDef> {

    protected DdsPlSqlBodyScriptGenerator() {
    }

    @Override
    protected void printSqlClassName(CodePrinter cp, DdsPlSqlBodyDef body) {
        super.printSqlClassName(cp, body);
        cp.print(" body");
    }

    @Override
    public void getCreateScript(CodePrinter cp, DdsPlSqlBodyDef body, IScriptGenerationHandler handler) {
        DdsPlSqlObjectDef plSqlObject = body.getPlSqlObjectDef();
        if (handler != null) {
            handler.onGenerationStarted(plSqlObject, cp);
        }

        cp.print("create or replace ");
        printSqlClassName(cp, body);
        cp.print(' ');
        cp.print(plSqlObject.getDbName());
        cp.print(" as");

        for (DdsPlSqlObjectItemDef item : plSqlObject.getBody().getItems()) {
            if (handler != null) {
                handler.onGenerationStarted(item, cp);
            }

            if (item instanceof DdsFunctionDef) {
                final DdsFunctionDef function = (DdsFunctionDef) item;
                if (function.isGeneratedInDb()) {
                    cp.print("\n\n");
                    DdsFunctionScriptGenerator.getScript(cp, function, handler);
                }
            } else if (item instanceof DdsPrototypeDef) {
                final DdsPrototypeDef prototype = (DdsPrototypeDef) item;
                final DdsFunctionDef function = prototype.getFunction();
                if (function.isGeneratedInDb()) {
                    cp.print("\n\n");
                    DdsFunctionScriptGenerator.getHeaderScript(cp, function);
                    cp.print(';');
                }
            } else if (item instanceof DdsCustomTextDef) {
                cp.print("\n\n");
                final DdsCustomTextDef customText = (DdsCustomTextDef) item;
                DdsCustomTextScriptGenerator.getScript(cp, customText);
            } else {
                throw new DefinitionError("Illegal item on PL/SQL object body: " + String.valueOf(item) + ".", body);
            }
        }

        cp.println();
        cp.print("end");
        cp.print(';');
        cp.printCommandSeparator();
    }

    public static final class Factory {

        private Factory() {
        }

        public static DdsPlSqlBodyScriptGenerator newInstance() {
            return new DdsPlSqlBodyScriptGenerator();
        }
    }
}
