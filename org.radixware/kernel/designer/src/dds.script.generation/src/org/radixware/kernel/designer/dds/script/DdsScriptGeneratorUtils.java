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
package org.radixware.kernel.designer.dds.script;

import org.radixware.kernel.common.scml.ScmlCodePrinter;
import java.util.Collections;
import java.util.Set;
import org.radixware.kernel.common.defs.dds.DdsDefinition;
import org.radixware.kernel.common.defs.dds.DdsIndexDef;
import org.radixware.kernel.common.defs.dds.DdsPlSqlHeaderDef;
import org.radixware.kernel.common.defs.dds.DdsPlSqlObjectDef;
import org.radixware.kernel.common.defs.dds.DdsPlSqlPartDef;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.defs.dds.DdsTriggerDef;
import org.radixware.kernel.common.defs.dds.DdsUniqueConstraintDef;
import org.radixware.kernel.common.enums.EDatabaseType;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.sqlscript.parser.SQLPreprocessor;
import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.common.sqml.tags.ISqlTag;
import org.radixware.kernel.common.sqml.translate.ISqmlPreprocessorConfig;
import org.radixware.kernel.common.sqml.translate.SqmlPreprocessor;
import org.radixware.kernel.common.sqml.translate.SqmlTranslator;
import org.radixware.kernel.common.sqml.translate.SqmlTranslator.TagPostprocessor;
import static org.radixware.kernel.designer.dds.script.ScriptGeneratorImpl.SCRIPT_GENERATION_KEY;
import org.radixware.kernel.designer.dds.script.defs.DdsReferenceScriptGenerator;
import static org.radixware.kernel.designer.dds.script.defs.DdsTableScriptGenerator.printCopyDataFromIntermediateScript;
import static org.radixware.kernel.designer.dds.script.defs.DdsTableScriptGenerator.printRenameToIntermediateScript;
import org.radixware.kernel.designer.dds.script.defs.DdsUniqueConstraintScriptGenerator;

/**
 * Утилиты для генерации DDS скриптов.
 *
 */
public class DdsScriptGeneratorUtils {
    private static final String IS_INSTALLATION_SCRIPT = "rdx.is.installation.script";
    
    public static void setIsInstallationScript(final CodePrinter cp, boolean isInstallation) {
        cp.putProperty(IS_INSTALLATION_SCRIPT, isInstallation);
    }    
    
    public static boolean isInstallationScript(final CodePrinter cp) {
        if (cp.getProperty(IS_INSTALLATION_SCRIPT) != null) {
            return (Boolean) cp.getProperty(IS_INSTALLATION_SCRIPT);
        }
        return false;
    }
    
    private static final TagPostprocessor ESCAPING_TAG = new TagPostprocessor(){
                                    @Override
                                    public String postprocessTag(final String tagContent, final ISqlTag tag) {
                                        if (tagContent.contains(".")) {
                                            final String[] splitted = tagContent.split("\\.");
                                            final StringBuilder sb = new StringBuilder();
                                            
                                            splitted[splitted.length-1] = DdsScriptInternalUtils.escapeDbName(splitted[splitted.length-1]);
                                            for (String item : splitted) {
                                                sb.append('.').append(item);
                                            }
                                            return sb.toString().substring(1);
                                        }
                                        else {
                                            return DdsScriptInternalUtils.escapeDbName(tagContent);
                                        }
                                    }
                                };

    private DdsScriptGeneratorUtils() {
    }

    public static boolean isNewGenerationMode() {
        if (System.getProperties().containsKey(SCRIPT_GENERATION_KEY)) {
            return "new".equals(System.getProperty(SCRIPT_GENERATION_KEY));
        }
        else {
            return false;
        }
    }
    
    /**
     * Транслировать SQML в SQL и записать всё в CodePrinter.
     */
    public static void translateSqml(final CodePrinter cp, final Sqml sqml) {
        if (cp == null) {
            throw new IllegalArgumentException("Code printer can't be null");
        }
        else if (sqml != null) {
            SqmlTranslator sqmlt = SqmlTranslator.Factory.newInstance();
            sqmlt.translate(sqml, cp);
        }
    }

    public static void translateSqmlWithEscaping(final CodePrinter cp, final Sqml sqml) {
        if (cp == null) {
            throw new IllegalArgumentException("Code printer can't be null");
        }
        else if (sqml != null) {
            SqmlTranslator sqmlt = SqmlTranslator.Factory.newInstance(ESCAPING_TAG);
            sqmlt.translate(sqml, cp);
        }
    }
    
    public static Sqml preprocessSqml(final Sqml sqml, final ISqmlPreprocessorConfig config) {
        if (sqml == null) {
            return null;
        }
        else if (config == null) {
            throw new IllegalArgumentException("Proprocessor configuration can't be null");
        }
        else {
            final SqmlPreprocessor preprocessor = new SqmlPreprocessor();
            final Sqml preprocessedSqml = preprocessor.preprocess(sqml, config);
            preprocessedSqml.setEnvironment(sqml.getEnvironment());
            return preprocessedSqml;
        }
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

    public static String translateSqmlWithEscaping(Sqml sqml) {
        final CodePrinter cp = CodePrinter.Factory.newSqlPrinter();
        translateSqmlWithEscaping(cp, sqml);
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

    private static void printPlSqlPart(final CodePrinter cp, final DdsPlSqlPartDef plSqlPart, IScriptGenerationHandler handler, final boolean newGenerationMode) {
        if (newGenerationMode) {
            final ScriptGenerator sg = ScriptGeneratorImpl.Factory.newCreationInstance(plSqlPart);
            sg.getDefinitionScriptGenerator(plSqlPart).getCreateScript(cp, plSqlPart, handler);
        }
        else {
            final org.radixware.kernel.designer.dds.script.defs.DdsPlSqlPartScriptGenerator partScriptGenerator = (plSqlPart instanceof DdsPlSqlHeaderDef ? new org.radixware.kernel.designer.dds.script.defs.DdsPlSqlHeaderScriptGenerator() : new org.radixware.kernel.designer.dds.script.defs.DdsPlSqlBodyScriptGenerator());
            partScriptGenerator.getCreateScript(cp, plSqlPart, handler);
        }
    }

    private static void printPlSqlObject(final CodePrinter cp, final DdsPlSqlObjectDef plSqlObject, IScriptGenerationHandler handler, final boolean newGenerationMode) {
        printPlSqlPart(cp, plSqlObject.getHeader(), handler, newGenerationMode);
        if (!plSqlObject.getBody().getItems().isEmpty()) {
            printPlSqlPart(cp, plSqlObject.getBody(), handler, newGenerationMode);
        }
    }

    public static void printPartitioningIf(final CodePrinter cp) {
        cp.print("#IF " + Layer.DatabaseOption.TARGET_DB_TYPE + " == \"" + EDatabaseType.ORACLE + "\" and " + SQLPreprocessor.FUNC_IS_ENABLED + "(\"" + Layer.TargetDatabase.PARTITIONING_OPTION.replace("\\", "\\\\") + "\") THEN");
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
        return getPlSqlPartScript(plSqlPart, handler, isNewGenerationMode());
    }
    
    public static String getPlSqlPartScript(final DdsPlSqlPartDef plSqlPart, IScriptGenerationHandler handler, final boolean newGenerationMode) {
        final CodePrinter cp = CodePrinter.Factory.newSqlPrinter();
        printPlSqlPart(cp, plSqlPart, handler, newGenerationMode);
        return cp.toString();
    }

    public static String getPlSqlObjectScript(final DdsPlSqlObjectDef plSqlObject, IScriptGenerationHandler handler) {
        return getPlSqlObjectScript(plSqlObject,handler,isNewGenerationMode());
    }
    
    public static String getPlSqlObjectScript(final DdsPlSqlObjectDef plSqlObject, IScriptGenerationHandler handler, final boolean newGenerationMode) {
        final CodePrinter cp = CodePrinter.Factory.newSqlPrinter();
        printPlSqlObject(cp, plSqlObject, handler, newGenerationMode);
        return cp.toString();
    }

    public static Sqml calcPlSqlPartSqml(final DdsPlSqlPartDef plSqlPart, IScriptGenerationHandler handler) {
        return calcPlSqlPartSqml(plSqlPart, handler, isNewGenerationMode());
    }
    
    public static Sqml calcPlSqlPartSqml(final DdsPlSqlPartDef plSqlPart, IScriptGenerationHandler handler, final boolean newGenerationMode) {
        final Sqml result = Sqml.Factory.newInstance();
        result.setEnvironment(plSqlPart.getPlSqlObjectDef().getSqmlEnvironment());
        final CodePrinter sqmlCodePrinter = new ScmlCodePrinter(result);
        printPlSqlPart(sqmlCodePrinter, plSqlPart, handler, newGenerationMode);
        return result;
    }

    public static Sqml calcPlSqlObjectSqml(final DdsPlSqlObjectDef plSqlObject, IScriptGenerationHandler handler) {
        return calcPlSqlObjectSqml(plSqlObject, handler, isNewGenerationMode());
    }
    
    public static Sqml calcPlSqlObjectSqml(final DdsPlSqlObjectDef plSqlObject, IScriptGenerationHandler handler, final boolean newGenerationMode) {
        final Sqml result = Sqml.Factory.newInstance();
        result.setEnvironment(plSqlObject.getSqmlEnvironment());
        final CodePrinter sqmlCodePrinter = new ScmlCodePrinter(result);
        printPlSqlObject(sqmlCodePrinter, plSqlObject, handler, newGenerationMode);
        return result;
    }

    public static String getTriggeCreationScript(final DdsTriggerDef trigger, IScriptGenerationHandler handler) {
        final ScriptGenerator sg = ScriptGeneratorImpl.Factory.newCreationInstance(trigger);
        final CodePrinter cp = CodePrinter.Factory.newSqlPrinter();
        sg.getDefinitionScriptGenerator(trigger).getCreateScript(cp, trigger, handler);
        return cp.toString();
    }

    public static String generateCreationScript(Set<DdsDefinition> definitions) {
        
        if (isNewGenerationMode()) {
            final ScriptGenerator scriptGenerator = ScriptGeneratorImpl.Factory.newCreationInstance(definitions);
            final CodePrinter cp = CodePrinter.Factory.newSqlPrinter();
            
            scriptGenerator.generateCompactModificationScript(cp,null);
            return cp.toString();
        }
        else {
            final CodePrinter cp = CodePrinter.Factory.newSqlPrinter();
            final ScriptGenerator scriptGenerator = ScriptGeneratorImpl.Factory.newCreationInstance(definitions);
            
            scriptGenerator.generateModificationScript(cp);
            return cp.toString();
        }
    }

    public static String generateRunRoleScript(Set<DdsDefinition> definitions) {
        final ScriptGenerator scriptGenerator = new ScriptGeneratorImpl(null, definitions);
        final CodePrinter cp = CodePrinter.Factory.newSqlPrinter();
        scriptGenerator.generateRunRoleScript(cp);
        final String creationScript = cp.toString();
        return creationScript;
    }

    public static String generateReCreateScript(final DdsDefinition definition) {
        final CodePrinter printer = CodePrinter.Factory.newSqlPrinter();
        
        if (isNewGenerationMode()) {
            ScriptGeneratorImpl.Factory.newCreationInstance(definition).getDefinitionScriptGenerator(definition).getReCreateScript(printer, definition, true);
            if (printer.length() == 0) {
                return null;
            }
        }
        else {
            if (definition instanceof DdsTableDef) {
                printReCreateScript((DdsTableDef) definition, true, printer);
            } else if (definition instanceof DdsIndexDef) {
                printReCreateScript((DdsIndexDef) definition, printer);
            } else {
                return null;
            }
        }
        return printer.toString();
    }


    public static void printReCreateScript(final DdsTableDef table, final boolean storeData, final CodePrinter printer) {

        final ScriptGenerator instance = ScriptGeneratorImpl.Factory.newCreationInstance(table);
        //disable foreign key constraints   
        final String tmpName;
        final Set<DdsReferenceDef> incomingRefs = printDisconnectIncomingReferences(table, printer);
        final Set<DdsReferenceDef> outgoingRefs = printDisconnectOutgoingReferences(table, printer);

        if (storeData) {
            tmpName = printRenameToIntermediateScript(table, printer);
        } else {
            tmpName = null;
        }

        IDdsDefinitionScriptGenerator generator = instance.getDefinitionScriptGenerator(table);

        if (!storeData) {
            generator.getDropScript(printer, table);
        }

        instance.getDefinitionScriptGenerator(table).getCreateScript(printer, table, IScriptGenerationHandler.NOOP_HANDLER);
        if (storeData) {
            printCopyDataFromIntermediateScript(table, tmpName, printer);
        }

        instance.generateModificationScript(printer, Collections.singleton(table));

        printConnectIncomingReferences(incomingRefs, printer);
        printConnectOutgoingReferences(outgoingRefs, printer);

    }

    public static void printConnectIncomingReferences(Set<DdsReferenceDef> incomingRefs, CodePrinter printer) {
        if (incomingRefs != null) {
            for (DdsReferenceDef ref : incomingRefs) {
                DdsReferenceScriptGenerator.getEnableDisableScript(ref, printer, true);
            }
        }
    }

    public static void printConnectOutgoingReferences(Set<DdsReferenceDef> incomingRefs, CodePrinter printer) {
        if (incomingRefs != null) {
            for (DdsReferenceDef ref : incomingRefs) {
                DdsReferenceScriptGenerator.getEnableDisableScript(ref, printer, true);
            }
        }
    }
    
    public static Set<DdsReferenceDef> printDisconnectIncomingReferences(DdsTableDef table, CodePrinter printer) {
        final Set<DdsReferenceDef> incomingRefs = table.collectIncomingReferences();
        for (DdsReferenceDef ref : incomingRefs) {
            DdsReferenceScriptGenerator.getEnableDisableScript(ref, printer, false);
        }
        return incomingRefs;
    }

    public static Set<DdsReferenceDef> printDisconnectOutgoingReferences(DdsTableDef table, CodePrinter printer) {
        final Set<DdsReferenceDef> outgoinfRefs = table.collectOutgoingReferences();
        for (DdsReferenceDef ref : outgoinfRefs) {
            DdsReferenceScriptGenerator.getEnableDisableScript(ref, printer, false);
        }
        return outgoinfRefs;
    }
    
    public static void printReCreateScript(DdsIndexDef index, CodePrinter printer) {
        final ScriptGenerator instance = ScriptGeneratorImpl.Factory.newCreationInstance(index);
        IDdsDefinitionScriptGenerator<DdsIndexDef> generator = instance.getDefinitionScriptGenerator(index);
        
        //drop constraint
        DdsUniqueConstraintDef constraint = index.getOwnerTable().getPrimaryKey().getUniqueConstraint();
        boolean recreateConstraint = false;
        if (constraint != null && constraint.getOwnerIndex() == index) {
            DdsUniqueConstraintScriptGenerator.Factory.newInstance().getDropScript(printer, constraint);
            recreateConstraint = true;
        }
        //drop index
        generator.getDropScript(printer, index);
        //generate create script
        generator.getCreateScript(printer, index, IScriptGenerationHandler.NOOP_HANDLER);
        //add constraint if any
        if (recreateConstraint) {
            DdsUniqueConstraintScriptGenerator.Factory.newInstance().getCreateScript(printer, constraint, IScriptGenerationHandler.NOOP_HANDLER);
        }
    }
}
