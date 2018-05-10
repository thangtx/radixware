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
import org.radixware.kernel.common.defs.dds.DdsFunctionDef;
import org.radixware.kernel.common.defs.dds.DdsParameterDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.defs.dds.DdsTypeDef;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.designer.dds.script.DdsScriptGeneratorUtils;
import org.radixware.kernel.designer.dds.script.IScriptGenerationHandler;
import org.radixware.kernel.common.scml.ScmlCodePrinter;


public class PgsDdsFunctionScriptGenerator {

    public static void getHeaderScript(CodePrinter cp, DdsFunctionDef function) {
        String descriptionLines[] = DdsScriptGeneratorUtils.splitDescription(function.getDescription());
        for (String descriptionLine : descriptionLines) {
            cp.print("\t-- ").println(descriptionLine);
        }

        cp.print('\t');

        if (function.getOwnerPlSqlObject() instanceof DdsTypeDef) {
            cp.print("member ");
        }
        EValType resultValType = function.getResultValType();
        cp.print(resultValType != null ? "function " : "procedure ");

// TODO: generate link to function for usefull navigation - commented, so, generated sql contains package name.
//        if (cp instanceof ScmlCodePrinter) {
//            final ScmlCodePrinter scmlCodePrinter = (ScmlCodePrinter) cp;
//            final DbFuncCallTag tag = DbFuncCallTag.Factory.newInstance();
//            tag.setFunctionId(function.getId());
//            tag.setParamsDefined(false);
//            scmlCodePrinter.print(tag);
//        } else {
            cp.print(function.getDbName());
//        }

        boolean paramPrintedFlag = false;
        for (DdsParameterDef param : function.getParameters()) {
            if (paramPrintedFlag) {
                cp.print(",\n\t\t");
            } else {
                cp.print("(\n\t\t");
                paramPrintedFlag = true;
            }

            final String paramDescriptionLines[] = DdsScriptGeneratorUtils.splitDescription(param.getDescription());
            for (String paramDescriptionLine : paramDescriptionLines) {
                cp.print("-- ").print(paramDescriptionLine).print("\n\t\t");
            }

            cp.print(param.getDbName()).print(' ');

            switch (param.getDirection()) {
                case IN:
                    cp.print("in");
                    break;
                case BOTH:
                    cp.print("in out");
                    break;
                case OUT:
                    cp.print("out");
                    break;
            }

            cp.print(' ').print(param.getDbType());

            final String defaultValueSql = param.getDefaultVal();
            if (defaultValueSql == null) {
                cp.print(" := null");
            } else if (!defaultValueSql.isEmpty()) {
                cp.print(" := ").print(defaultValueSql);
            }
        }

        if (paramPrintedFlag) {
            cp.print("\n\t)");
        }

        if (resultValType != null) {
            cp.print(" return ").print(function.getResultDbType());
        }

        if (function.isDeterministic()) {
            cp.print(" deterministic");
        }

        if (function.isCachedResult()) {
            cp.print(" RESULT_CACHE");
        }
    }
    
    public static void getScript(CodePrinter cp, DdsFunctionDef function) {
        getScript(cp, function, null);
    }

    public static void getScript(CodePrinter cp, DdsFunctionDef function, final IScriptGenerationHandler handler) {
        getHeaderScript(cp, function);

        boolean reliesOnTablePrintedFlag = false;
        for (DdsFunctionDef.ReliesOnTableInfo reliesOnTableInfo : function.getReliesOnTablesInfo()) {
            DdsTableDef table = reliesOnTableInfo.findTable();
            if (table == null) { // выявится проверкой
                continue; // чтобы старые ф-и собирались
            }
            if (reliesOnTablePrintedFlag) {
                cp.print(',');
            } else {
                cp.print(" RELIES_ON(");
                reliesOnTablePrintedFlag = true;
            }
            cp.print(table.getDbName());
        }
        if (reliesOnTablePrintedFlag) {
            cp.print(')');
        }

        cp.println();
        final Sqml body = function.getBody();
        
        if (handler != null) {
            handler.onGenerationStarted(body, cp);
        }

        cp.enterBlock(1);
        if (cp instanceof ScmlCodePrinter) {
            final ScmlCodePrinter scmlCodePrinter = (ScmlCodePrinter) cp;
            scmlCodePrinter.print(body);
        } else {
            final String bodySql = DdsScriptGeneratorUtils.translateSqml(body);
            cp.print(bodySql);
        }
        cp.leaveBlock(1);
//        final String bodySqlLines[] = bodySql.split("\\n");
//        for (String bodySqlLine : bodySqlLines) {
//            cp.print("\n\t");
//            cp.print(bodySqlLine);
//        }

    }
}
