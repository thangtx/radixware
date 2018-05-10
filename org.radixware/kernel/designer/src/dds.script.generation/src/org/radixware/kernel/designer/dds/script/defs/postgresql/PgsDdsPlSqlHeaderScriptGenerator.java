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
import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.defs.dds.DdsCustomTextDef;
import org.radixware.kernel.common.defs.dds.DdsFunctionDef;
import org.radixware.kernel.common.defs.dds.DdsPlSqlHeaderDef;
import org.radixware.kernel.common.defs.dds.DdsPlSqlObjectDef;
import org.radixware.kernel.common.defs.dds.DdsPlSqlObjectItemDef;
import org.radixware.kernel.common.defs.dds.DdsPrototypeDef;
import org.radixware.kernel.common.defs.dds.DdsTypeDef;
import org.radixware.kernel.common.defs.dds.DdsTypeFieldDef;
import org.radixware.kernel.common.defs.dds.DdsPurityLevel;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.designer.dds.script.DdsScriptGeneratorUtils;
import org.radixware.kernel.designer.dds.script.IScriptGenerationHandler;


public class PgsDdsPlSqlHeaderScriptGenerator extends PgsDdsPlSqlPartScriptGenerator<DdsPlSqlHeaderDef> {

    public PgsDdsPlSqlHeaderScriptGenerator() {
    }


    private static class ItemSeparator {

        private enum EState {

            BEGIN,
            NORMAL,
            CUSTOM_TEXT_PRINTED
        }
        private EState state = EState.BEGIN;
        private final DdsPlSqlObjectDef plSqlObject;

        public ItemSeparator(final DdsPlSqlObjectDef plSqlObject) {
            this.plSqlObject = plSqlObject;
        }

        public void print(final CodePrinter cp, final EState state) {
            switch (this.state) {
                case BEGIN:
                    if (plSqlObject instanceof DdsTypeDef) {
                        cp.println('(');
                    } else {
                        cp.println();
                    }
                    break;
                case NORMAL:
                    if (plSqlObject instanceof DdsTypeDef) {
                        cp.println(',');
                    } else {
                        cp.println(';');
                    }
                    break;
                case CUSTOM_TEXT_PRINTED:
                    cp.println();
                    break;
            }
            this.state = state;
        }

        public EState getState() {
            return state;
        }
    }

    private void printPurityLevel(CodePrinter cp, DdsPurityLevel purityLevel, String methodName, ItemSeparator itemSeparator) {
        final List<String> levels = new ArrayList<>();

        if (purityLevel.isRNDS()) {
            levels.add("RNDS");
        }
        if (purityLevel.isRNPS()) {
            levels.add("RNPS");
        }
        if (purityLevel.isWNDS()) {
            levels.add("WNDS");
        }
        if (purityLevel.isWNPS()) {
            levels.add("WNPS");
        }
        if (purityLevel.isTrust()) {
            levels.add("TRUST");
        }

        for (String level : levels) {
            itemSeparator.print(cp, ItemSeparator.EState.NORMAL);
            cp.print("\tPRAGMA RESTRICT_REFERENCES (").print(methodName).print(", ").print(level).print(')');
        }
    }

    @Override
    public void getCreateScript(CodePrinter cp, DdsPlSqlHeaderDef header, IScriptGenerationHandler handler) {
        DdsPlSqlObjectDef plSqlObject = header.getPlSqlObjectDef();

        if (handler != null) {
            handler.onGenerationStarted(plSqlObject, cp);
        }

        String plSqlObjectDescriptionLines[] = DdsScriptGeneratorUtils.splitDescription(plSqlObject.getDescription());
        if (plSqlObjectDescriptionLines.length > 0) {
            for (String plSqlObjectDescriptionLine : plSqlObjectDescriptionLines) {
                cp.print("-- ");
                cp.println(plSqlObjectDescriptionLine);
            }
        }

        cp.print("create or replace ");
        printSqlClassName(cp, header);
        cp.print(' ').print(plSqlObject.getDbName()).print(" as");

        final ItemSeparator itemSeparator = new ItemSeparator(plSqlObject);

        // fields
        if (plSqlObject instanceof DdsTypeDef) {
            final DdsTypeDef type = (DdsTypeDef) plSqlObject;
            cp.print(' ');
            cp.print(type.getDbType());

            for (DdsTypeFieldDef field : type.getFields()) {
                if (handler != null) {
                    handler.onGenerationStarted(field, cp);
                }

                itemSeparator.print(cp, ItemSeparator.EState.NORMAL);

                String fieldDescriptionLines[] = DdsScriptGeneratorUtils.splitDescription(field.getDescription());
                if (fieldDescriptionLines.length > 1) {
                    for (String fieldDescriptionLine : fieldDescriptionLines) {
                        cp.print("\t-- ").print(fieldDescriptionLine);
                    }
                }

                cp.print('\t').print(field.getDbName()).print(' ').print(field.getDbType());

                if (fieldDescriptionLines.length == 1) {
                    cp.print(" -- ").print(fieldDescriptionLines[0]);
                }
            }
        }

        // items
        for (DdsPlSqlObjectItemDef item : plSqlObject.getHeader().getItems()) {
            if (handler != null) {
                handler.onGenerationStarted(item, cp);
            }

            if (item instanceof DdsPrototypeDef) {
                final DdsPrototypeDef prototype = (DdsPrototypeDef) item;
                final DdsFunctionDef function = prototype.getFunction();
                if (function.isGeneratedInDb()) {
                    itemSeparator.print(cp, ItemSeparator.EState.NORMAL);
                    cp.println();
                    PgsDdsFunctionScriptGenerator.getHeaderScript(cp, function);
                    DdsPurityLevel purityLevel = function.getPurityLevel();
                    printPurityLevel(cp, purityLevel, function.getDbName(), itemSeparator);
                }
            } else if (item instanceof DdsCustomTextDef) {
                final DdsCustomTextDef customText = (DdsCustomTextDef) item;
                final String customTextSql = DdsScriptGeneratorUtils.translateSqml(customText.getText());
                if (!customTextSql.isEmpty()) {
                    itemSeparator.print(cp, ItemSeparator.EState.CUSTOM_TEXT_PRINTED);
                    cp.println();
                    PgsDdsCustomTextScriptGenerator.getScript(cp, customText);
                }
            } else {
                throw new DefinitionError("Illegal item on PL/SQL object header: " + String.valueOf(item) + ".", item);
            }
        }

        DdsPurityLevel purityLevel = plSqlObject.getPurityLevel();
        printPurityLevel(cp, purityLevel, "DEFAULT", itemSeparator);

        if (plSqlObject instanceof DdsTypeDef) {
            if (itemSeparator.getState() != ItemSeparator.EState.BEGIN) {
                cp.println().print(')');
            }
        } else {
            if (itemSeparator.getState() == ItemSeparator.EState.NORMAL) {
                cp.print(';');
            }
            cp.println().print("end");
        }

        cp.print(';').printCommandSeparator();
        getRunRoleScript(cp, header);
    }

    @Override
    public void getReCreateScript(CodePrinter printer, DdsPlSqlHeaderDef definition, boolean storeData) {
    }

    @Override
    public void getEnableDisableScript(CodePrinter cp, DdsPlSqlHeaderDef definition, boolean enable) {
    }
    
    public static final class Factory {

        private Factory() {
        }

        public static PgsDdsPlSqlHeaderScriptGenerator newInstance() {
            return new PgsDdsPlSqlHeaderScriptGenerator();
        }
    }
}
