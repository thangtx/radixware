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

import java.util.Set;
import org.radixware.kernel.common.defs.dds.DdsDefinition;
import org.radixware.kernel.common.defs.dds.DdsIndexDef;
import org.radixware.kernel.common.defs.dds.DdsPlSqlHeaderDef;
import org.radixware.kernel.common.defs.dds.DdsPlSqlObjectDef;
import org.radixware.kernel.common.defs.dds.DdsPlSqlPartDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.defs.dds.DdsTriggerDef;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.sqlscript.parser.SQLPreprocessor;
import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.common.sqml.translate.ISqmlPreprocessorConfig;
import org.radixware.kernel.common.sqml.translate.SqmlPreprocessor;
import org.radixware.kernel.common.sqml.translate.SqmlTranslator;
import org.radixware.kernel.designer.dds.script.IScriptGenerationHandler;
import org.radixware.kernel.designer.dds.script.ScriptGenerator;

/**
 * Утилиты для генерации DDS скриптов.
 *
 */
public class DdsScriptGeneratorUtils {

    private DdsScriptGeneratorUtils() {
    }

    /**
     * Транслировать SQML в SQL и записать всё в CodePrinter.
     */
    public static void translateSqml(CodePrinter cp, Sqml sqml) {
        if (sqml != null) {
            SqmlTranslator sqmlt = SqmlTranslator.Factory.newInstance();
            sqmlt.translate(sqml, cp);
        }
    }

    public static Sqml preprocessSqml(final Sqml sqml, final ISqmlPreprocessorConfig config) {
        if (sqml == null) {
            return null;
        }
        final SqmlPreprocessor preprocessor = new SqmlPreprocessor();
        final Sqml preprocessedSqml = preprocessor.preprocess(sqml, config);
        preprocessedSqml.setEnvironment(sqml.getEnvironment());
        return preprocessedSqml;
    }

    public static void preprocessAndTranslate(final CodePrinter cp, final Sqml sqml, final ISqmlPreprocessorConfig config) {
        translateSqml(cp, preprocessSqml(sqml, config));
    }

    public static String preprocessAndTranslate(final Sqml sqml, final ISqmlPreprocessorConfig config) {
        return translateSqml(preprocessSqml(sqml, config));
    }

    public static String trimLineTrails(String s) {
        return s.replaceFirst("\\s+\\z", "");
    }

    /**
     * Транслировать SQML в SQL.
     *
     * @return SQL expression.
     */
    public static String translateSqml(Sqml sqml) {
        final CodePrinter cp = CodePrinter.Factory.newSqlPrinter();
        translateSqml(cp, sqml);
        final String sql = cp.toString();
        return trimLineTrails(sql);
    }

    /**
     * Транслировать и сравнить SQL двух SQML.
     *
     * @return true, если трансляция обоих SQML вернула одинаковый результат,
     * false otherwise.
     */
    public static boolean isTranslatedSqmlEquals(Sqml oldSqml, Sqml newSqml) {
        String oldSql = translateSqml(oldSqml);
        String newSql = translateSqml(newSqml);
        return oldSql.equals(newSql);
    }

    public static String[] splitDescription(String description) {
        String trimmedDescription = trimLineTrails(description).trim();
        if (trimmedDescription.isEmpty()) {
            return new String[0];
        } else {
            return trimmedDescription.split("\\n");
        }
    }

    private static void printPlSqlPart(final CodePrinter cp, final DdsPlSqlPartDef plSqlPart, IScriptGenerationHandler handler) {
        final DdsPlSqlPartScriptGenerator partScriptGenerator = (plSqlPart instanceof DdsPlSqlHeaderDef ? new DdsPlSqlHeaderScriptGenerator() : new DdsPlSqlBodyScriptGenerator());
        partScriptGenerator.getCreateScript(cp, plSqlPart, handler);
    }

    private static void printPlSqlObject(final CodePrinter cp, final DdsPlSqlObjectDef plSqlObject, IScriptGenerationHandler handler) {
        printPlSqlPart(cp, plSqlObject.getHeader(), handler);
        if (!plSqlObject.getBody().getItems().isEmpty()) {
            printPlSqlPart(cp, plSqlObject.getBody(), handler);
        }
    }

    public static void printPartitioningIf(final CodePrinter cp) {
        cp.print("#IF " + Layer.DatabaseOption.TARGET_DB_TYPE + " == \"" + Layer.TargetDatabase.ORACLE_DB_TYPE + "\" and " + SQLPreprocessor.FUNC_IS_ENABLED + "(\"" + Layer.TargetDatabase.PARTITIONING_OPTION.replace("\\", "\\\\") + "\") THEN");
    }

    public static void printEndIf(final CodePrinter cp) {
        cp.print("#ENDIF");
    }

    public static void printIfPartitioningEnabled(final CodePrinter cp, final String sql) {
        cp.println();
        printPartitioningIf(cp);
        cp.println();
        cp.print(sql);
        cp.println();
        printEndIf(cp);
        cp.println();
    }

    public static String getPlSqlPartScript(final DdsPlSqlPartDef plSqlPart, IScriptGenerationHandler handler) {
        final CodePrinter cp = CodePrinter.Factory.newSqlPrinter();
        printPlSqlPart(cp, plSqlPart, handler);
        return cp.toString();
    }

    public static String getPlSqlObjectScript(final DdsPlSqlObjectDef plSqlObject, IScriptGenerationHandler handler) {
        final CodePrinter cp = CodePrinter.Factory.newSqlPrinter();
        printPlSqlObject(cp, plSqlObject, handler);
        return cp.toString();
    }

    public static Sqml calcPlSqlPartSqml(final DdsPlSqlPartDef plSqlPart, IScriptGenerationHandler handler) {
        final Sqml result = Sqml.Factory.newInstance();
        result.setEnvironment(plSqlPart.getPlSqlObjectDef().getSqmlEnvironment());
        final CodePrinter sqmlCodePrinter = new ScmlCodePrinter(result);
        printPlSqlPart(sqmlCodePrinter, plSqlPart, handler);
        return result;
    }

    public static Sqml calcPlSqlObjectSqml(final DdsPlSqlObjectDef plSqlObject, IScriptGenerationHandler handler) {
        final Sqml result = Sqml.Factory.newInstance();
        result.setEnvironment(plSqlObject.getSqmlEnvironment());
        final CodePrinter sqmlCodePrinter = new ScmlCodePrinter(result);
        printPlSqlObject(sqmlCodePrinter, plSqlObject, handler);
        return result;
    }

    public static String getTriggeCreationScript(final DdsTriggerDef trigger, IScriptGenerationHandler handler) {
        final DdsTriggerScriptGenerator sg = DdsTriggerScriptGenerator.Factory.newInstance();
        final CodePrinter cp = CodePrinter.Factory.newSqlPrinter();
        sg.getCreateScript(cp, trigger, handler);
        return cp.toString();
    }

    public static String generateCreationScript(Set<DdsDefinition> definitions) {
        final ScriptGenerator scriptGenerator = ScriptGenerator.Factory.newCreationInstance(definitions);
        final CodePrinter cp = CodePrinter.Factory.newSqlPrinter();
        scriptGenerator.generateModificationScript(cp);
        final String creationScript = cp.toString();
        return creationScript;
    }

    public static String generateRunRoleScript(Set<DdsDefinition> definitions) {
        final ScriptGenerator scriptGenerator = new ScriptGenerator(null, definitions);
        final CodePrinter cp = CodePrinter.Factory.newSqlPrinter();
        scriptGenerator.generateRunRoleScript(cp);
        final String creationScript = cp.toString();
        return creationScript;
    }

    public static String generateReCreateScript(final DdsDefinition definition) {
        final CodePrinter printer = CodePrinter.Factory.newSqlPrinter();
        if (definition instanceof DdsTableDef) {
            DdsTableScriptGenerator.printReCreateScript((DdsTableDef) definition, true, printer);
        } else if (definition instanceof DdsIndexDef) {
            DdsIndexScriptGenerator.printReCreateScript((DdsIndexDef) definition, printer);
        } else {
            return null;
        }
        return printer.toString();
    }
}
