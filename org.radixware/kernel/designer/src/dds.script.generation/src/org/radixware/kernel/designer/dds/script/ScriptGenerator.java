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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.radixware.kernel.common.defs.HierarchyWalker;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.dds.*;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.dds.DdsSegment;
import org.radixware.kernel.common.rights.SystemTablesSearcher;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.designer.dds.script.defs.*;

public class ScriptGenerator {

    private final DefinitionPairs definitionPairs;

    private enum EScriptGenerationMode {

        ALTER, CREATE
    }
    private final EScriptGenerationMode scriptGenerationMode;

    public ScriptGenerator(Collection<DdsDefinition> oldDefinitions, Collection<DdsDefinition> newDefinitions) {
        super();
        definitionPairs = new DefinitionPairs(oldDefinitions, newDefinitions);
        scriptGenerationMode = (oldDefinitions != null ? EScriptGenerationMode.ALTER : EScriptGenerationMode.CREATE);

        registerScriptGenerator(DdsViewDef.class, DdsViewScriptGenerator.Factory.newInstance());
        registerScriptGenerator(DdsTableDef.class, DdsTableScriptGenerator.Factory.newInstance());
        registerScriptGenerator(DdsPlSqlHeaderDef.class, DdsPlSqlHeaderScriptGenerator.Factory.newInstance());
        registerScriptGenerator(DdsPlSqlBodyDef.class, DdsPlSqlBodyScriptGenerator.Factory.newInstance());
        registerScriptGenerator(DdsReferenceDef.class, DdsReferenceScriptGenerator.Factory.newInstance());
        registerScriptGenerator(DdsColumnDef.class, DdsColumnScriptGenerator.Factory.newInstance());
        registerScriptGenerator(DdsIndexDef.class, DdsIndexScriptGenerator.Factory.newInstance());
        registerScriptGenerator(DdsPrimaryKeyDef.class, DdsIndexScriptGenerator.Factory.newInstance());
        registerScriptGenerator(DdsUniqueConstraintDef.class, DdsUniqueConstraintScriptGenerator.Factory.newInstance());
        registerScriptGenerator(DdsCheckConstraintDef.class, DdsCheckConstraintScriptGenerator.Factory.newInstance());
        registerScriptGenerator(DdsTriggerDef.class, DdsTriggerScriptGenerator.Factory.newInstance());
        registerScriptGenerator(DdsSequenceDef.class, DdsSequenceScriptGenerator.Factory.newInstance());
    }

    private enum EModelScriptType {

        BEGIN, END
    }

    private void appendBeginOrEndScripts(CodePrinter cp, EModelScriptType type) {
        if (scriptGenerationMode == EScriptGenerationMode.CREATE) {
            for (DefinitionPair pair : definitionPairs) {
                DdsDefinition oldDefinition = pair.getOldDefinition();
                DdsDefinition newDefinition = pair.getNewDefinition();
                if ((oldDefinition == null) && (newDefinition instanceof DdsModelDef)) {
                    DdsModelDef newModel = (DdsModelDef) newDefinition;
                    Sqml sqml = (type == EModelScriptType.BEGIN ? newModel.getBeginScript() : newModel.getEndScript());
                    DdsScriptGeneratorUtils.translateSqml(cp, sqml);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void calculatePairOperations() {
        for (DefinitionPair pair : definitionPairs) {
            DdsDefinition oldDefinition = pair.getOldDefinition();
            DdsDefinition newDefinition = pair.getNewDefinition();
            if ((oldDefinition instanceof DdsModelDef) || (newDefinition instanceof DdsModelDef)) {
                pair.setNeedToAlter(false);
                pair.setNeedToDrop(false);
                pair.setNeedToCreate(false);
            } else if (oldDefinition != null && newDefinition != null) {
                IDdsDefinitionScriptGenerator definitionScriptGenerator = getDefinitionScriptGenerator(newDefinition);
                if (definitionScriptGenerator.isModifiedToDrop(oldDefinition, newDefinition)) {
                    // calc modified to drop
                    pair.setNeedToAlter(false);
                    pair.setNeedToDrop(true);
                    pair.setNeedToCreate(true);
                }
            }
        }

        HashMap<Id, DdsDefinition> droppedTableIds = new HashMap<Id, DdsDefinition>();
        HashMap<Id, DdsDefinition> createdTableIds2Table = new HashMap<Id, DdsDefinition>();

        for (DefinitionPair pair : definitionPairs) {
            DdsDefinition oldDefinition = pair.getOldDefinition();
            DdsDefinition newDefinition = pair.getNewDefinition();
            if (pair.isNeedToDrop() && (oldDefinition instanceof DdsTableDef)) {
                droppedTableIds.put(oldDefinition.getId(), null);
            }
            if (pair.isNeedToCreate() && (newDefinition instanceof DdsTableDef)) {
                createdTableIds2Table.put(newDefinition.getId(), newDefinition);
            }
        }

        // не удалять элементы удаляемых таблиц (они удаляются вместе с таблицей одной командой)
        for (DefinitionPair pair : definitionPairs) {
            if (pair.isNeedToDrop()) {
                DdsDefinition oldDefinition = pair.getOldDefinition();

                Id oldTableId = null;
                if (oldDefinition instanceof IDdsTableItemDef) {
                    IDdsTableItemDef tableItem = (IDdsTableItemDef) oldDefinition;
                    oldTableId = tableItem.getOwnerTable().getId();
                } else if (oldDefinition instanceof DdsUniqueConstraintDef) {
                    DdsUniqueConstraintDef oldUniqueConstraint = (DdsUniqueConstraintDef) oldDefinition;
                    DdsIndexDef oldIndex = oldUniqueConstraint.getOwnerIndex();
                    oldTableId = oldIndex.getOwnerTable().getId();
                } else if (oldDefinition instanceof DdsCheckConstraintDef) {
                    DdsCheckConstraintDef oldCheckConstraint = (DdsCheckConstraintDef) oldDefinition;
                    DdsColumnDef oldColumn = oldCheckConstraint.getOwnerColumn();
                    oldTableId = oldColumn.getOwnerTable().getId();
                } else if (oldDefinition instanceof DdsReferenceDef) {
                    DdsReferenceDef oldReference = (DdsReferenceDef) oldDefinition;
                    oldTableId = oldReference.getChildTableId();
                }

                if (oldTableId != null && droppedTableIds.containsKey(oldTableId)) {
                    pair.setNeedToDrop(false);
                }
            }
        }

        // не создавать новые колонки совсем базовой новой таблицы, такие колонки создаются вместе с таблицей одной командой
        for (DefinitionPair pair : definitionPairs) {
            if (pair.isNeedToCreate()) {
                DdsDefinition newDefinition = pair.getNewDefinition();
                if (newDefinition instanceof DdsColumnDef) {
                    DdsColumnDef newColumnDef = (DdsColumnDef) newDefinition;
                    DdsTableDef newTable = newColumnDef.getOwnerTable();
                    if (createdTableIds2Table.get(newTable.getId()) == newTable) {
                        pair.setNeedToCreate(false);
                    }
                }
            }
        }
    }

    private boolean isRightSystemDef(DdsDefinition def) {
        if (def instanceof IDdsTableItemDef) {
            final IDdsTableItemDef tableItem = (IDdsTableItemDef) def;
            final DdsTableDef table = tableItem.getOwnerTable();
            return isRightSystemDef(table);
        } else if (def instanceof DdsTableDef) {
            final DdsTableDef table = (DdsTableDef) def;
            final Id tableId = table.getId();
            return SystemTablesSearcher.USER2ROLE_ID.equals(tableId)
                    || SystemTablesSearcher.USERGROUP2ROLE_ID.equals(tableId);
        } else {
            return false;
        }
    }

    /**
     * При актуализации системы прав (применяется для модуля RDC и перекрытых) в
     * системных таблицах создаются (точнее синхронизируются) колонки для
     * каждого семейства партиции доступа. После этого нужно пересоздать
     * (перегенерировать и прогнать) в базе пакет работы с системой прав, это
     * делается вызовом RDX_ACS.AcsUtilsBuild().
     */
    private void appendRightSystemBuild(CodePrinter cp) {
        // call RDX_ACS.AcsUtilsBuild() if any right system definition added or removed
        for (DefinitionPair pair : definitionPairs) {
            final DdsDefinition oldDefinition = pair.getOldDefinition();
            final DdsDefinition newDefinition = pair.getNewDefinition();
            if (isRightSystemDef(oldDefinition) || isRightSystemDef(newDefinition)) {
                if (pair.isNeedToCreate() || pair.isNeedToDrop()) {
                    cp.print("begin\n\tRDX_ACS.AcsUtilsBuild();\nend;");
                    cp.printCommandSeparator();
                    return;
                }
            }
        }
    }
    @SuppressWarnings("unchecked")
    private final HashMap<Class<? extends DdsDefinition>, IDdsDefinitionScriptGenerator> ddsDefinitionClass2Generator = new HashMap<>();

    public final <T extends DdsDefinition> void registerScriptGenerator(Class<T> ddsDefinitionClass, IDdsDefinitionScriptGenerator<T> definitionScriptGenerator) {
        ddsDefinitionClass2Generator.put(ddsDefinitionClass, definitionScriptGenerator);
    }

    @SuppressWarnings("unchecked")
    public final <T extends DdsDefinition> IDdsDefinitionScriptGenerator<T> getDefinitionScriptGenerator(T definition) {
        IDdsDefinitionScriptGenerator definitionScriptGenerator = this.ddsDefinitionClass2Generator.get(definition.getClass());
        if (definitionScriptGenerator == null) {
            throw new DefinitionError("Unsupported definition type in ScriptGenerator: '" + String.valueOf(definition.getClass()) + ".", definition);
        }
        return definitionScriptGenerator;
    }

    private final void appendTypesCompileScripts(CodePrinter cp, List<DdsTypeDef> types) {
        for (DdsTypeDef type : types) {
            cp.printCommand("alter type " + type.getDbName() + " compile");
        }
    }

    public void generateModificationScript(CodePrinter cp) {
        generateModificationScript(cp, null);
    }

    public void generateRunRoleScript(CodePrinter cp) {
        for (DefinitionPair pair : definitionPairs) {
            DdsDefinition newDefinition = pair.getNewDefinition();
            if (newDefinition instanceof DdsPlSqlBodyDef) {                
                continue;
            }
            try {
                IDdsDefinitionScriptGenerator definitionScriptGenerator = getDefinitionScriptGenerator(newDefinition);
                definitionScriptGenerator.getRunRoleScript(cp, newDefinition);
            } catch (DefinitionError e) {
                continue;//missing generator
            }

        }
    }

    @SuppressWarnings("unchecked")
    public void generateModificationScript(CodePrinter cp, Set<? extends DdsDefinition> exclude) {
        // ORDER: drop order is reversed to alter and create order.
        // Definitions in pairs are ordered by DefinitionPairs constructor in creation order.
        // Columns of views are ignored by ScriptDefinitionCollector.
        // TableItems of dropped tabled are ignored by DefinitionPairs constructor.
        // Columns of created tables are ignored by DefinitionPairs constructor.
        // Overridings are resolved DefinitionPairs constructor.

        // drop old or renamed triggers
        // drop old or renamed pl/sql objects 
        // drop old or modified references
        // drop old or renamed views
        // drop old or modified unique constraints
        // drop old or modified indices (include primary key)
        // drop old or modified check constraints
        // drop old columns
        // drop old tables
        // drop old sequences
        // begin scripts
        // alter / create or replace new or modified pl/sql types headers
        // compile types
        // alter / create or replace new or modified pl/sql packages headers
        // alter / create new sequences
        // alter / create new tables (include columns)
        // alter / create new columns
        // alter tables initial values (delete, update, insert)
        // alter / create new check constraints
        // alter / create new or modified indices (include primary key)
        // alter / create new or unique constraints
        // alter / create new or modified views
        // alter / create new or modified references
        // alter / create or replace new or modified pl/sql types bodies
        // alter / create or replace new or modified pl/sql packages bodies
        // alter / create new or modified triggers
        // end scripts
        final Set<Id> pkModifiedTable = new HashSet<>();

        calculatePairOperations();

        // drop
        for (int i = definitionPairs.size() - 1; i >= 0; i--) {
            DefinitionPair pair = definitionPairs.get(i);
            if (pair.isNeedToDrop()) {
                DdsDefinition oldDefinition = pair.getOldDefinition();
                @SuppressWarnings("unchecked")
                IDdsDefinitionScriptGenerator definitionScriptGenerator = getDefinitionScriptGenerator(oldDefinition);
                definitionScriptGenerator.getDropScript(cp, oldDefinition);
                if (oldDefinition instanceof DdsUniqueConstraintDef) {
                    DdsIndexDef index = ((DdsUniqueConstraintDef) oldDefinition).getOwnerIndex();
                    if (index instanceof DdsPrimaryKeyDef) {
                        pkModifiedTable.add(index.getOwnerTable().getId());
                    }
                }
            }
        }

        // begin scripts
        appendBeginOrEndScripts(cp, EModelScriptType.BEGIN);

        boolean wasColumns = false;
        boolean wasInitialValues = false;

        // alter & create
        for (DefinitionPair pair : definitionPairs) {
            DdsDefinition oldDefinition = pair.getOldDefinition();
            DdsDefinition newDefinition = pair.getNewDefinition();
            //used for table recreation
            if (exclude != null && exclude.contains(newDefinition)) {
                continue;
            }

            // генерация скриптов начальных значений должна выполняться после колонок, но до индексов и checkConstraint'ов и связей.
            // нельзя генерировать по таблицам в definitionPairs, т.к. там нет Overwrite (иначе тяжело разруливать связки, и игнорировать их при генерации).
            if (!wasInitialValues) {
                boolean isColumn = (oldDefinition instanceof DdsColumnDef) || (newDefinition instanceof DdsColumnDef);
                if (isColumn) {
                    wasColumns = true;
                } else if (wasColumns) {
                    InitialValuesScriptGenerator.getAlterScript(cp, definitionPairs);
                    wasInitialValues = true;
                }
            }

            if (pair.isNeedToAlter()) {
                IDdsDefinitionScriptGenerator definitionScriptGenerator = getDefinitionScriptGenerator(newDefinition);
                definitionScriptGenerator.getAlterScript(cp, oldDefinition, newDefinition);
            } else if (pair.isNeedToCreate()) {
                IDdsDefinitionScriptGenerator definitionScriptGenerator = getDefinitionScriptGenerator(newDefinition);
                definitionScriptGenerator.getCreateScript(cp, newDefinition, null /* handler*/);
                if (newDefinition instanceof DdsUniqueConstraintDef) {
                    DdsIndexDef index = ((DdsUniqueConstraintDef) newDefinition).getOwnerIndex();
                    if (index instanceof DdsPrimaryKeyDef && pkModifiedTable.contains(index.getOwnerTable().getId())) {
                        DdsUniqueConstraintScriptGenerator uniqueScriptGenerator = (DdsUniqueConstraintScriptGenerator) definitionScriptGenerator;
                        uniqueScriptGenerator.afterModifyPkScript(cp, (DdsPrimaryKeyDef) index);
                    }
                }
            }
        }

        appendRightSystemBuild(cp);

        // end scripts
        appendBeginOrEndScripts(cp, EModelScriptType.END);
    }

    public static final class Factory {

        private Factory() {
        }

        /**
         * Создать генератор скриптов изменений между модифицированными и
         * захваченными DDS моделями указанного сегмента (без подсегментов).
         *
         * @throws IOException if unable to load some model.
         */
        public static ScriptGenerator newAlterInstance(DdsSegment segment) throws IOException {
            assert segment != null;

            final Collection<DdsDefinition> fixedDefinitions = new ArrayList<DdsDefinition>();
            final Collection<DdsDefinition> modifiedDefinitions = new ArrayList<DdsDefinition>();
            ScriptDefinitionsCollector.collect(segment, fixedDefinitions, modifiedDefinitions);
            return new ScriptGenerator(fixedDefinitions, modifiedDefinitions);
        }

        /**
         * Создать генератор скриптов изменений между модифицированными и
         * захваченными DDS моделями указанного сегмента и подсегментов.
         *
         * @throws IOException if unable to load some model.
         */
        public static ScriptGenerator newAlterInstance(DdsModule module) throws IOException {
            assert module != null;

            final Collection<DdsDefinition> fixedDefinitions = new ArrayList<DdsDefinition>();
            final Collection<DdsDefinition> modifiedDefinitions = new ArrayList<DdsDefinition>();
            ScriptDefinitionsCollector.collect(module, fixedDefinitions, modifiedDefinitions);
            return new ScriptGenerator(fixedDefinitions, modifiedDefinitions);
        }

        /**
         * Создать генератор скриптов изменений между текущими дефинициями
         * указанных контекстов.
         */
        public static ScriptGenerator newAlterInstance(RadixObject contextWithOldDefinitions, RadixObject contextWithNewDefinitions) {
            assert contextWithOldDefinitions != null;
            assert contextWithNewDefinitions != null;

            final Collection<DdsDefinition> oldDefinitions = new ArrayList<DdsDefinition>();
            final Collection<DdsDefinition> newDefinitions = new ArrayList<DdsDefinition>();
            ScriptDefinitionsCollector.collect(contextWithOldDefinitions, oldDefinitions);
            ScriptDefinitionsCollector.collect(contextWithNewDefinitions, newDefinitions);
            return new ScriptGenerator(oldDefinitions, newDefinitions);
        }

        /**
         * Создать генератор скриптов создания дефиниций указанного контекста.
         */
        public static ScriptGenerator newCreationInstance(RadixObject contextWithNewDefinitions) {
            return newCreationInstance(contextWithNewDefinitions, false);
        }

        public static ScriptGenerator newCreationInstance(RadixObject contextWithNewDefinitions, boolean excludeContext) {
            assert contextWithNewDefinitions != null;

            final List<DdsDefinition> newDefinitions = new ArrayList<DdsDefinition>();
            ScriptDefinitionsCollector.collect(contextWithNewDefinitions, newDefinitions);
            if (excludeContext) {
                newDefinitions.remove(contextWithNewDefinitions);
            }
            return new ScriptGenerator(null, newDefinitions);
        }

        /**
         * Создать генератор скриптов создания дефиниций указанного контекста.
         */
        public static ScriptGenerator newCreationInstance(Set<DdsDefinition> definitions) {
            assert definitions != null;

            final List<DdsDefinition> newDefinitions = new ArrayList<DdsDefinition>();
            for (DdsDefinition definition : definitions) {
                ScriptDefinitionsCollector.collect(definition, newDefinitions);
            }
            return new ScriptGenerator(null, newDefinitions);
        }
    }
}
