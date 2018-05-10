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
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.dds.DdsCheckConstraintDef;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsDefinition;
import org.radixware.kernel.common.defs.dds.DdsExtendableDefinitions;
import org.radixware.kernel.common.defs.dds.DdsIndexDef;
import org.radixware.kernel.common.defs.dds.DdsModelDef;
import org.radixware.kernel.common.defs.dds.DdsModule;
import org.radixware.kernel.common.defs.dds.DdsPackageDef;
import org.radixware.kernel.common.defs.dds.DdsPlSqlBodyDef;
import org.radixware.kernel.common.defs.dds.DdsPlSqlHeaderDef;
import org.radixware.kernel.common.defs.dds.DdsPrimaryKeyDef;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.defs.dds.DdsSequenceDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.defs.dds.DdsTriggerDef;
import org.radixware.kernel.common.defs.dds.DdsTypeDef;
import org.radixware.kernel.common.defs.dds.DdsUniqueConstraintDef;
import org.radixware.kernel.common.defs.dds.DdsViewDef;
import org.radixware.kernel.common.defs.dds.IDdsTableItemDef;
import org.radixware.kernel.common.enums.EDatabaseType;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.repository.dds.DdsSegment;
import org.radixware.kernel.common.rights.SystemTablesSearcher;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.common.types.Id;

public class ScriptGeneratorImpl implements ScriptGenerator {
    public static final String SCRIPT_GENERATION_KEY = "rdx.script.generation.mode";
    public static final String SCRIPT_GENERATION_KEY_OLD = "old";
    public static final String SCRIPT_GENERATION_KEY_NEW = "new ";
    
    private static final Class<? extends DdsDefinition>[] ENTITIES = new Class[]{DdsViewDef.class, DdsTableDef.class, DdsPlSqlHeaderDef.class, DdsPlSqlBodyDef.class, DdsReferenceDef.class, DdsColumnDef.class, DdsIndexDef.class, DdsPrimaryKeyDef.class, DdsUniqueConstraintDef.class, DdsCheckConstraintDef.class, DdsTriggerDef.class, DdsSequenceDef.class};

    private final EDatabaseType dbType;
    private final DefinitionPairs definitionPairs;
    private final boolean newGenerationMode;

    private void Thread(Runnable runnable) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private enum EScriptGenerationMode {
        ALTER, CREATE
    }
    private final EScriptGenerationMode scriptGenerationMode;
    private final MultiBaseScriptGeneratorRepo sgRepo;

    public ScriptGeneratorImpl(final Collection<DdsDefinition> oldDefinitions, final Collection<DdsDefinition> newDefinitions) {
        this(oldDefinitions,newDefinitions,EDatabaseType.ORACLE,DdsScriptGeneratorUtils.isNewGenerationMode());
    }

    public ScriptGeneratorImpl(final Collection<DdsDefinition> oldDefinitions, final Collection<DdsDefinition> newDefinitions, final EDatabaseType dbType) {
        this(oldDefinitions,newDefinitions,dbType,DdsScriptGeneratorUtils.isNewGenerationMode());
    }
    
    protected ScriptGeneratorImpl(final Collection<DdsDefinition> oldDefinitions, final Collection<DdsDefinition> newDefinitions, final EDatabaseType dbType, final boolean newGenerationMode) {
        if (dbType == null) {
            throw new IllegalArgumentException("Database type can't be null");
        }
        else {
            this.newGenerationMode = newGenerationMode;
            this.dbType = dbType;
            this.scriptGenerationMode = (oldDefinitions != null ? EScriptGenerationMode.ALTER : EScriptGenerationMode.CREATE);
            this.sgRepo = new MultiBaseScriptGeneratorRepo();

            if (this.newGenerationMode) {
                this.definitionPairs = new DefinitionPairs(oldDefinitions, newDefinitions, this.sgRepo.getOrderingComparators());
                for (Class<? extends DdsDefinition> item : ENTITIES) {
                    ddsDefinitionClass2Generator.put(item,this.sgRepo.getDefinitionScriptGenerator(item));
                }
            }
            else {
                this.definitionPairs = new DefinitionPairs(oldDefinitions, newDefinitions);
                registerScriptGenerator(DdsViewDef.class, org.radixware.kernel.designer.dds.script.defs.DdsViewScriptGenerator.Factory.newInstance());
                registerScriptGenerator(DdsTableDef.class, org.radixware.kernel.designer.dds.script.defs.DdsTableScriptGenerator.Factory.newInstance());
                registerScriptGenerator(DdsPlSqlHeaderDef.class, org.radixware.kernel.designer.dds.script.defs.DdsPlSqlHeaderScriptGenerator.Factory.newInstance());
                registerScriptGenerator(DdsPlSqlBodyDef.class, org.radixware.kernel.designer.dds.script.defs.DdsPlSqlBodyScriptGenerator.Factory.newInstance());
                registerScriptGenerator(DdsReferenceDef.class, org.radixware.kernel.designer.dds.script.defs.DdsReferenceScriptGenerator.Factory.newInstance());
                registerScriptGenerator(DdsColumnDef.class, org.radixware.kernel.designer.dds.script.defs.DdsColumnScriptGenerator.Factory.newInstance());
                registerScriptGenerator(DdsIndexDef.class, org.radixware.kernel.designer.dds.script.defs.DdsIndexScriptGenerator.Factory.newInstance());
                registerScriptGenerator(DdsPrimaryKeyDef.class, org.radixware.kernel.designer.dds.script.defs.DdsIndexScriptGenerator.Factory.newInstance());
                registerScriptGenerator(DdsUniqueConstraintDef.class, org.radixware.kernel.designer.dds.script.defs.DdsUniqueConstraintScriptGenerator.Factory.newInstance());
                registerScriptGenerator(DdsCheckConstraintDef.class, org.radixware.kernel.designer.dds.script.defs.DdsCheckConstraintScriptGenerator.Factory.newInstance());
                registerScriptGenerator(DdsTriggerDef.class, org.radixware.kernel.designer.dds.script.defs.DdsTriggerScriptGenerator.Factory.newInstance());
                registerScriptGenerator(DdsSequenceDef.class, org.radixware.kernel.designer.dds.script.defs.DdsSequenceScriptGenerator.Factory.newInstance());
            }
        }
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
            final DdsDefinition oldDefinition = pair.getOldDefinition();
            final DdsDefinition newDefinition = pair.getNewDefinition();
            
            if ((oldDefinition instanceof DdsModelDef) || (newDefinition instanceof DdsModelDef)) {
                pair.setNeedToAlter(false);
                pair.setNeedToDrop(false);
                pair.setNeedToCreate(false);
            } else if (oldDefinition != null && newDefinition != null) {
                final IDdsDefinitionScriptGenerator definitionScriptGenerator = getDefinitionScriptGenerator(newDefinition);
                
                if (definitionScriptGenerator.isModifiedToDrop(oldDefinition, newDefinition)) {
                    // calc modified to drop
                    pair.setNeedToAlter(false);
                    pair.setNeedToDrop(true);
                    pair.setNeedToCreate(true);
                }
            }
        }

        final HashMap<Id,DdsDefinition> droppedTableIds = new HashMap<>();
        final HashMap<Id,DdsDefinition> createdTableIds2Table = new HashMap<>();

        for (DefinitionPair pair : definitionPairs) {
            final DdsDefinition oldDefinition = pair.getOldDefinition();
            final DdsDefinition newDefinition = pair.getNewDefinition();
            
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
                final DdsDefinition oldDefinition = pair.getOldDefinition();
                Id oldTableId = null;
                
                if (oldDefinition instanceof IDdsTableItemDef) {
                    final IDdsTableItemDef tableItem = (IDdsTableItemDef) oldDefinition;
                    
                    oldTableId = tableItem.getOwnerTable().getId();
                } else if (oldDefinition instanceof DdsUniqueConstraintDef) {
                    final DdsUniqueConstraintDef oldUniqueConstraint = (DdsUniqueConstraintDef) oldDefinition;
                    final DdsIndexDef oldIndex = oldUniqueConstraint.getOwnerIndex();
                    
                    oldTableId = oldIndex.getOwnerTable().getId();
                } else if (oldDefinition instanceof DdsCheckConstraintDef) {
                    final DdsCheckConstraintDef oldCheckConstraint = (DdsCheckConstraintDef) oldDefinition;
                    final DdsColumnDef oldColumn = oldCheckConstraint.getOwnerColumn();
                    
                    oldTableId = oldColumn.getOwnerTable().getId();
                } else if (oldDefinition instanceof DdsReferenceDef) {
                    final DdsReferenceDef oldReference = (DdsReferenceDef) oldDefinition;
                    
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
                final DdsDefinition newDefinition = pair.getNewDefinition();
                
                if (newDefinition instanceof DdsColumnDef) {
                    final DdsColumnDef newColumnDef = (DdsColumnDef) newDefinition;
                    final DdsTableDef newTable = newColumnDef.getOwnerTable();
                    
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
            
            return SystemTablesSearcher.USER2ROLE_ID.equals(tableId) || SystemTablesSearcher.USERGROUP2ROLE_ID.equals(tableId);
        } else if (def instanceof DdsPackageDef) {
            final DdsPackageDef packageDef = (DdsPackageDef) def;
            String packageDbName = packageDef.getDbName();
            if (packageDbName == null) {
                return false;
            }
            packageDbName = packageDbName.toUpperCase();
            return packageDbName.startsWith("ACS$");
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
                    cp.print("begin\n\tRDX_ACS.AcsUtilsBuild();\nend;").printCommandSeparator();
                    cp.print("grant execute on RDX_ACS_UTILS to &USER&_RUN_ROLE").printCommandSeparator();
                    return;
                }
            }
        }
    }
    @SuppressWarnings("unchecked")
    private final HashMap<Class<? extends DdsDefinition>, IDdsDefinitionScriptGenerator> ddsDefinitionClass2Generator = new HashMap<>();

    private final <T extends DdsDefinition> void registerScriptGenerator(Class<T> ddsDefinitionClass, IDdsDefinitionScriptGenerator<T> definitionScriptGenerator) {
        ddsDefinitionClass2Generator.put(ddsDefinitionClass, definitionScriptGenerator);
    }

    @SuppressWarnings("unchecked")
    @Override
    public final <T extends DdsDefinition> IDdsDefinitionScriptGenerator<T> getDefinitionScriptGenerator(T definition) {
        if (definition == null) {
            throw new IllegalArgumentException("Definition can't be null");
        }
        else if (this.newGenerationMode) {
            return (IDdsDefinitionScriptGenerator<T>) sgRepo.getDefinitionScriptGenerator(definition.getClass());
        }
        else {
            final IDdsDefinitionScriptGenerator definitionScriptGenerator = this.ddsDefinitionClass2Generator.get(definition.getClass());
            if (definitionScriptGenerator == null) {
                if (!this.newGenerationMode && (definition instanceof DdsModelDef)) {
                    return new IDdsDefinitionScriptGenerator(){
                        @Override 
                        public boolean isModifiedToDrop(DdsDefinition oldDefinition, DdsDefinition newDefinition) {
                            return false;
                        }

                        @Override
                        public void getDropScript(CodePrinter cp, DdsDefinition definition) {
                        }

                        @Override
                        public void getCreateScript(CodePrinter cp, DdsDefinition definition, IScriptGenerationHandler handler) {
                        }

                        @Override
                        public void getAlterScript(CodePrinter cp, DdsDefinition oldDefinition, DdsDefinition newDefinition) {
                        }

                        @Override
                        public void getRunRoleScript(CodePrinter printer, DdsDefinition definition) {
                        }

                        @Override
                        public void getReCreateScript(CodePrinter printer, DdsDefinition definition, boolean storeData) {
                        }

                        @Override
                        public void getEnableDisableScript(CodePrinter cp, DdsDefinition definition, boolean enable) {
                        }
                    };
                }
                else {
                    throw new DefinitionError("Unsupported definition type in ScriptGenerator: '" + String.valueOf(definition.getClass()) + ".", definition);
                }
            }
            return definitionScriptGenerator;
        }
    }

    @Override
    public void generateCompatibilityLog(final OutputStream os) {
        if (os == null) {
            throw new IllegalArgumentException("Log printer can't be null");
        }
        else {
            final Set<Id> pkModifiedTable = new HashSet<>();
            final Set<Id> viewsDeleted = new HashSet<>();

            calculatePairOperations();
            try(final PrintWriter wr = new PrintWriter(os)) {
                for (DefinitionPair pair : definitionPairs.forDatabase(dbType,false)) {
                    if (pair.isNeedToDrop()) {
                        final DdsDefinition oldDefinition = pair.getOldDefinition();
                        
                        if (oldDefinition instanceof DdsPlSqlHeaderDef) {
                            wr.println("Removing of "+oldDefinition.getName()+" package header");
                        }
                        if (oldDefinition instanceof DdsPlSqlBodyDef) {
                            wr.println("Removing of "+oldDefinition.getName()+" package body");
                        }
                        if (oldDefinition instanceof DdsViewDef) {
                            wr.println("Removing of "+oldDefinition.getName()+" view");
                        }
                        if (oldDefinition instanceof DdsTableDef) {
                            wr.println("Removing of "+oldDefinition.getName()+" table");
                        }
                        if (oldDefinition instanceof DdsColumnDef) {
                            wr.println("Removing of "+oldDefinition.getName()+" column in the "+((DdsColumnDef)oldDefinition).getOwnerTable().getName());
                        }
                    }
                }

                for (DefinitionPair pair : definitionPairs.forDatabase(dbType,true)) {
                    final DdsDefinition oldDefinition = pair.getOldDefinition();
                    final DdsDefinition newDefinition = pair.getNewDefinition();

                    if (pair.isNeedToAlter()) {
                        if ((oldDefinition instanceof DdsColumnDef) && !((DdsColumnDef)oldDefinition).getDbType().equals(((DdsColumnDef)newDefinition).getDbType())) {
                            wr.println("Modifying type of "+oldDefinition.getName()+" column in the "+((DdsColumnDef)oldDefinition).getOwnerTable().getName()+" table");
                        }
                        if ((oldDefinition instanceof DdsColumnDef) && !((DdsColumnDef)oldDefinition).getDbName().equals(((DdsColumnDef)newDefinition).getDbName())) {
                            wr.println("Changing name of "+oldDefinition.getName()+" column in the "+((DdsColumnDef)oldDefinition).getOwnerTable().getName()+" table");
                        }
                        if ((oldDefinition instanceof DdsIndexDef) && !indexExprEquals((DdsIndexDef)oldDefinition,(DdsIndexDef)newDefinition)) {
                            wr.println("Changing index expression of "+((DdsIndexDef)oldDefinition).getOwnerTable()+" index "+((DdsIndexDef)oldDefinition).getDbName());
                        }
                        if ((oldDefinition instanceof DdsPrimaryKeyDef) && !indexExprEquals((DdsIndexDef)oldDefinition,(DdsIndexDef)newDefinition)) {
                            wr.println("Changing primary key expression of "+((DdsIndexDef)oldDefinition).getOwnerTable());
                        }
                    }
                }
            }
        }
    }

    @Override
    public void generateModificationScript(CodePrinter cp) {
        generateModificationScript(cp, null);
    }

    @Override
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
                //missing generator
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void generateModificationScript(CodePrinter cp, Set<? extends DdsDefinition> exclude) {
        generateModificationScript(cp,EDatabaseType.ORACLE,exclude);
    }

    @Override
    public void generateModificationScript(final CodePrinter cp, final EDatabaseType dbType, Set<? extends DdsDefinition> exclude) {
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
        if (cp == null) {
            throw new IllegalArgumentException("Code printer can't be null");
        }
        else if (dbType == null) {
            throw new IllegalArgumentException("Database type can't be null");
        }
        else {
            final Set<Id> pkModifiedTable = new HashSet<>();
            final Set<Id> viewsDeleted = new HashSet<>();

            calculatePairOperations();

            DdsScriptGeneratorUtils.setIsInstallationScript(cp, scriptGenerationMode == EScriptGenerationMode.CREATE);
            for (DefinitionPair pair : definitionPairs.forDatabase(dbType,false)) {
                if (pair.isNeedToDrop()) {
                    final DdsDefinition oldDefinition = pair.getOldDefinition();

                    generateDropScript(cp,dbType,oldDefinition);
                    if (oldDefinition instanceof DdsUniqueConstraintDef) {
                        final DdsIndexDef index = ((DdsUniqueConstraintDef) oldDefinition).getOwnerIndex();

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

            long id = System.nanoTime();
            for (DefinitionPair pair : definitionPairs.forDatabase(dbType,true)) {
                final DdsDefinition oldDefinition = pair.getOldDefinition();
                final DdsDefinition newDefinition = pair.getNewDefinition();
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
                            if (newGenerationMode) {
                                InitialValuesScriptGenerator.getAlterScript(cp, definitionPairs);
                            }
                            else {
                                InitialValuesScriptGenerator.getAlterScript(cp, definitionPairs);
                            }
                        wasInitialValues = true;
                    }
                }

                if (pair.isNeedToAlter()) {
                    generateAlterScript(cp,dbType,oldDefinition,newDefinition);
                } else if (pair.isNeedToCreate()) {
                    final boolean neadCreateAfterModify;
                    if (newDefinition instanceof DdsUniqueConstraintDef) {
                        DdsIndexDef index = ((DdsUniqueConstraintDef) newDefinition).getOwnerIndex();
                        if (index instanceof DdsPrimaryKeyDef && pkModifiedTable.contains(index.getOwnerTable().getId())) {
                            neadCreateAfterModify = true;
                        }
                        else {
                            neadCreateAfterModify = false;
                        }
                    }
                    else {
                        neadCreateAfterModify = false;
                    }
                    generateCreateScript(cp,dbType,newDefinition,neadCreateAfterModify);
                }
            }

            appendRightSystemBuild(cp);

            for (DefinitionPair pair : definitionPairs) {
                DdsDefinition oldDefinition = pair.getOldDefinition();
                DdsDefinition newDefinition = pair.getNewDefinition();
                if ((oldDefinition == null) && (newDefinition instanceof DdsModelDef)) {
                    IDdsDefinitionScriptGenerator definitionScriptGenerator = getDefinitionScriptGenerator(newDefinition);
                    definitionScriptGenerator.getCreateScript(cp, newDefinition, null);
                }
            }
            
            // end scripts
            appendBeginOrEndScripts(cp, EModelScriptType.END);
        }
    }

    @Override
    public void generateCreateScript(final CodePrinter cp, final EDatabaseType dbType, final DdsDefinition definition, final boolean createAfterModifyPK) {
        if (cp == null) {
            throw new IllegalArgumentException("Code printer can't be null");
        }
        else if (dbType == null) {
            throw new IllegalArgumentException("Database type can't be null");
        }
        else if (definition == null) {
            throw new IllegalArgumentException("Definition to generate can't be null");
        }
        else {
            final IDdsDefinitionScriptGenerator definitionScriptGenerator = getDefinitionScriptGenerator(definition);
            
            definitionScriptGenerator.getCreateScript(cp, definition, null /* handler*/);
            if (definition instanceof DdsUniqueConstraintDef) {
                final DdsIndexDef index = ((DdsUniqueConstraintDef) definition).getOwnerIndex();
                
                if (index instanceof DdsPrimaryKeyDef && createAfterModifyPK) {
                    if (!newGenerationMode) {
                        org.radixware.kernel.designer.dds.script.defs.DdsUniqueConstraintScriptGenerator uniqueScriptGenerator = (org.radixware.kernel.designer.dds.script.defs.DdsUniqueConstraintScriptGenerator) definitionScriptGenerator;
                        uniqueScriptGenerator.afterModifyPkScript(cp, (DdsPrimaryKeyDef) index);
                    }
                }
            }
        }
    }

    @Override
    public void generateAlterScript(final CodePrinter cp, final EDatabaseType dbType, final DdsDefinition oldDefinition, final DdsDefinition newDefinition) {
        if (cp == null) {
            throw new IllegalArgumentException("Code printer can't be null");
        }
        else if (dbType == null) {
            throw new IllegalArgumentException("Database type can't be null");
        }
        else if (oldDefinition == null) {
            throw new IllegalArgumentException("Old definition to generate can't be null");
        }
        else if (newDefinition == null) {
            throw new IllegalArgumentException("New definition to generate can't be null");
        }
        else {
            final IDdsDefinitionScriptGenerator definitionScriptGenerator = getDefinitionScriptGenerator(newDefinition);
            
            definitionScriptGenerator.getAlterScript(cp, oldDefinition, newDefinition);
        }
    }

    @Override
    public void generateDropScript(final CodePrinter cp, final EDatabaseType dbType, final DdsDefinition definition) {
        if (cp == null) {
            throw new IllegalArgumentException("Code printer can't be null");
        }
        else if (dbType == null) {
            throw new IllegalArgumentException("Database type can't be null");
        }
        else if (definition == null) {
            throw new IllegalArgumentException("Definition to generate can't be null");
        }
        else {
            final IDdsDefinitionScriptGenerator definitionScriptGenerator = getDefinitionScriptGenerator(definition);

            definitionScriptGenerator.getDropScript(cp, definition);
        }
    }

    @Override
    public void generateCompactModificationScript(final CodePrinter cp, final Set<? extends DdsDefinition> exclude) {
        if (cp == null) {
            throw new IllegalArgumentException("Code printer can't be null");
        }
        else if (dbType == null) {
            throw new IllegalArgumentException("Database type can't be null");
        }
        else {
            final Set<Id> pkModifiedTable = new HashSet<>();
            final Map<EDatabaseType,String> variants = new HashMap<>();

            calculatePairOperations();

            for (DefinitionPair pair : definitionPairs.forDatabase(EDatabaseType.ENTERPRISEDB,false)) {
                if (pair.isNeedToDrop()) {
                    final DdsDefinition oldDefinition = pair.getOldDefinition();

                    variants.clear();
                    for (EDatabaseType item : EDatabaseType.values()) {
                        final CodePrinter cpLocal = CodePrinter.Factory.newSqlPrinter(item);
                        
                        generateDropScript(cpLocal,item,oldDefinition);
                        variants.put(item,cpLocal.toString());
                    }
                    cp.print(reduce(variants));
                    if (oldDefinition instanceof DdsUniqueConstraintDef) {
                        final DdsIndexDef index = ((DdsUniqueConstraintDef) oldDefinition).getOwnerIndex();

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

            long id = System.nanoTime();
            for (DefinitionPair pair : definitionPairs.forDatabase(EDatabaseType.ENTERPRISEDB,true)) {
                final DdsDefinition oldDefinition = pair.getOldDefinition();
                final DdsDefinition newDefinition = pair.getNewDefinition();
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
                        variants.clear();
                        for (EDatabaseType item : EDatabaseType.values()) {
                            final CodePrinter cpLocal = CodePrinter.Factory.newSqlPrinter(item);

                            InitialValuesScriptGenerator.getAlterScript(cpLocal, definitionPairs);
                            variants.put(item,cpLocal.toString());
                        }
                        cp.print(reduce(variants));
                        wasInitialValues = true;
                    }
                }

                if (pair.isNeedToAlter()) {
                    variants.clear();
                    for (EDatabaseType item : EDatabaseType.values()) {
                        final CodePrinter cpLocal = CodePrinter.Factory.newSqlPrinter(item);
                        
                        generateAlterScript(cpLocal,item,oldDefinition,newDefinition);
                        variants.put(item,cpLocal.toString());
                    }
                    cp.print(reduce(variants));
                } else if (pair.isNeedToCreate()) {
                    final boolean neadCreateAfterModify;
                    if (newDefinition instanceof DdsUniqueConstraintDef) {
                        DdsIndexDef index = ((DdsUniqueConstraintDef) newDefinition).getOwnerIndex();
                        if (index instanceof DdsPrimaryKeyDef && pkModifiedTable.contains(index.getOwnerTable().getId())) {
                            neadCreateAfterModify = true;
                        }
                        else {
                            neadCreateAfterModify = false;
                        }
                    }
                    else {
                        neadCreateAfterModify = false;
                    }
                    
                    variants.clear();
                    for (EDatabaseType item : EDatabaseType.values()) {
                        final CodePrinter cpLocal = CodePrinter.Factory.newSqlPrinter(item);
                        
                        generateCreateScript(cpLocal,item,newDefinition,neadCreateAfterModify);
                        variants.put(item,cpLocal.toString());
                    }
                    cp.print(reduce(variants));
                    if (oldDefinition instanceof DdsTypeDef) {
                        cp.print("-- !!! old index: ").println(DdsScriptInternalUtils.getSequentialNumber4TheDefinitionInTheModule((DdsTypeDef)oldDefinition));
                    }
                    if (newDefinition instanceof DdsTypeDef) {
                        cp.print("-- !!! new index: ").println(DdsScriptInternalUtils.getSequentialNumber4TheDefinitionInTheModule((DdsTypeDef)newDefinition));
                    }
                }
            }

            appendRightSystemBuild(cp);

            for (DefinitionPair pair : definitionPairs) {
                DdsDefinition oldDefinition = pair.getOldDefinition();
                DdsDefinition newDefinition = pair.getNewDefinition();
                if ((oldDefinition == null) && (newDefinition instanceof DdsModelDef)) {
                    variants.clear();
                    for (EDatabaseType item : EDatabaseType.values()) {
                        final CodePrinter cpLocal = CodePrinter.Factory.newSqlPrinter(item);
                        
                        generateCreateScript(cpLocal,item,newDefinition,false);
                        variants.put(item,cpLocal.toString());
                    }
                    cp.print(reduce(variants));
                }
            }
            
            appendBeginOrEndScripts(cp, EModelScriptType.END);
        }
    }

    static String reduce(final Map<EDatabaseType,String> variants) {
        final Set<String> identities = new HashSet<>();
        
        for (Map.Entry<EDatabaseType, String> item : variants.entrySet()) { // Test identity of generated content
            identities.add(item.getValue());
        }
        
        if (identities.size() == 1) {   // Content generated is identical
            for (String item : identities) {
                return item;
            }
            return null;
        }
        else {  // Content is different. Split it into lines and make individulal reduction for every line splitted
            final String[][] sources = new String[EDatabaseType.values().length][];     // Split content by lines
            final DdsScriptInternalUtils.Prescription[] editP = new DdsScriptInternalUtils.Prescription[EDatabaseType.values().length];
            final int[] indexes = new int[EDatabaseType.values().length];
            final int[] lines = new int[EDatabaseType.values().length];
            
            for (EDatabaseType item : EDatabaseType.values()) { // Calculate edetor prescriptions by the Levenstain algorithm
                sources[item.ordinal()] = variants.get(item).split("\\\n");
                editP[item.ordinal()] = DdsScriptInternalUtils.calcLevenstain(sources[0],sources[item.ordinal()]);
            }
            
            final StringBuilder result = new StringBuilder();
            final Map<EDatabaseType,String> content = new HashMap<>();
            final Map<String,Set<EDatabaseType>> invertedContent = new HashMap<>();

            for (;;) {      // Output splitted data
                boolean allOutside = true;
                
                for (int curs = 0; curs < indexes.length; curs++) { // All the prescriptions need be processed
                    if (indexes[curs] < sources[curs].length) {
                        allOutside = false;
                    }
                }
                if (allOutside) {   // All prescriptions were processed
                    break;
                }
                else {
                    content.clear();
                    for (int curs = 0; curs < indexes.length; curs++) { // Test instertion relative to ORACLE
                        if (indexes[curs] < sources[curs].length && editP[curs].route[indexes[curs]][0] == DdsScriptInternalUtils.LEV_INSERT) {
                            content.put(EDatabaseType.values()[curs],sources[curs][lines[curs]++]);
                            indexes[curs]++;
                        }
                    }
                    if (content.size() > 0) {   // Insertions were detected
                        for (Map.Entry<EDatabaseType, String> item : content.entrySet()) {
                            if (!isReallyEmpty(item.getValue())) {
                                result.append("#IF DB_TYPE == \"").append(item.getKey()).append("\" THEN\n").append(item.getValue()).append("\n#ENDIF\n");
                            }
                        }
                    }
                    else {
                        boolean changes = false;
                        int databasesProcessed = 0;

                        content.clear();
                        for (int curs = 0; curs < indexes.length; curs++) { // Test deletions and changes relative to ORACLE
                            if (lines[curs] < sources[curs].length) {
                                content.put(EDatabaseType.values()[curs],sources[curs][lines[curs]++]);
                                if (editP[curs].route[indexes[curs]][0] == DdsScriptInternalUtils.LEV_DELETE) {
                                    content.remove(EDatabaseType.values()[curs]);
                                    lines[curs]--;
                                    changes = true;
                                }
                                else if (editP[curs].route[indexes[curs]][0] == DdsScriptInternalUtils.LEV_REPLACE) {
                                    changes = true;
                                }
                                indexes[curs]++;
                                databasesProcessed++;
                            }
                        }
                        if (changes || databasesProcessed < sources.length) {  // There were differences in the data or some database descriptors were exhausted
                            invertedContent.clear();    // ORing identical descriptions
                            for (Map.Entry<EDatabaseType, String> item : content.entrySet()) {
                                if (!invertedContent.containsKey(item.getValue())) {
                                    invertedContent.put(item.getValue(),new HashSet<EDatabaseType>());
                                }
                                invertedContent.get(item.getValue()).add(item.getKey());
                            }
                            
                            for (Map.Entry<String,Set<EDatabaseType>> item : invertedContent.entrySet()) {
                                if (!isReallyEmpty(item.getKey())) {
                                    String prefix = "#IF DB_TYPE == \"";
                                    
                                    for (EDatabaseType db : item.getValue()) {
                                        result.append(prefix).append(db);
                                        prefix = "\" OR DB_TYPE == \"";
                                    }
                                    result.append("\" THEN\n").append(item.getKey()).append("\n#ENDIF\n");                                    
                                }
                            }
                        }
                        else {  // All the data are identical
                            for (Map.Entry<EDatabaseType, String> item : content.entrySet()) {
                                result.append(item.getValue()).append('\n');
                                break;
                            }
                        }
                    }
                }
            }
            
            content.clear();    invertedContent.clear();
            return result.toString();
        }
    }

    private boolean indexExprEquals(final DdsIndexDef first, final DdsIndexDef second) {
        final StringBuilder sb1 = new StringBuilder(), sb2 = new StringBuilder();
        
        for (DdsIndexDef.ColumnInfo columnInfo : first.getColumnsInfo()) {
            DdsColumnDef column = columnInfo.getColumn();            
            sb1.append(column.getDbName().toLowerCase()).append(' ').append(columnInfo.getOrder().getValue().toLowerCase()).append(' ');
        }    
        for (DdsIndexDef.ColumnInfo columnInfo : second.getColumnsInfo()) {
            DdsColumnDef column = columnInfo.getColumn();            
            sb2.append(column.getDbName().toLowerCase()).append(' ').append(columnInfo.getOrder().getValue().toLowerCase()).append(' ');
        }
        return sb1.toString().equals(sb2.toString());
    }
        
    private static boolean isReallyEmpty(final String value) {
        for (int index = 0, maxIndex = value.length(); index < maxIndex; index++) {
            if (value.charAt(index) > ' ') {
                return false;
            }
        }
        return true;
    }
    
    
    public static final class Factory {

        private Factory() {
        }

        /**
         * Создать генератор скриптов изменений между модифицированными и
         * захваченными DDS моделями указанного сегмента (без подсегментов).
         *
         * @param segment
         * @return 
         * @throws IOException if unable to load some model.
         */
        public static ScriptGenerator newAlterInstance(final DdsSegment segment) throws IOException {
            if (segment == null) {
                throw new IllegalArgumentException("Segment can't be null");
            }
            else {
                final Collection<DdsDefinition> fixedDefinitions = new ArrayList<>();
                final Collection<DdsDefinition> modifiedDefinitions = new ArrayList<>();
                
                ScriptDefinitionsCollector.collect(segment, fixedDefinitions, modifiedDefinitions);
                return new ScriptGeneratorImpl(fixedDefinitions, modifiedDefinitions);
            }
        }

        /**
         * Создать генератор скриптов изменений между модифицированными и
         * захваченными DDS моделями указанного сегмента и подсегментов.
         *
         * @param module
         * @return 
         * @throws IOException if unable to load some model.
         */
        public static ScriptGenerator newAlterInstance(final DdsModule module) throws IOException {
            if (module == null) {
                throw new IllegalArgumentException("Module can't be null");
            }
            else {
                final Collection<DdsDefinition> fixedDefinitions = new ArrayList<>();
                final Collection<DdsDefinition> modifiedDefinitions = new ArrayList<>();
                
                ScriptDefinitionsCollector.collect(module, fixedDefinitions, modifiedDefinitions);
                return new ScriptGeneratorImpl(fixedDefinitions, modifiedDefinitions);
            }
        }

        /**
         * Создать генератор скриптов изменений между текущими дефинициями
         * указанных контекстов.
         * @param contextWithOldDefinitions
         * @param contextWithNewDefinitions
         * @return 
         */
        public static ScriptGenerator newAlterInstance(final RadixObject contextWithOldDefinitions, final  RadixObject contextWithNewDefinitions) {
            if (contextWithOldDefinitions == null) {
                throw new IllegalArgumentException("Old context can't be null");
            }
            else if (contextWithNewDefinitions == null) {
                throw new IllegalArgumentException("New context can't be null");
            }
            else {
                final Collection<DdsDefinition> oldDefinitions = new ArrayList<>();
                final Collection<DdsDefinition> newDefinitions = new ArrayList<>();
                
                ScriptDefinitionsCollector.collect(contextWithOldDefinitions, oldDefinitions);
                ScriptDefinitionsCollector.collect(contextWithNewDefinitions, newDefinitions);
                return new ScriptGeneratorImpl(oldDefinitions, newDefinitions);
            }
        }

        /**
         * Создать генератор скриптов создания дефиниций указанного контекста.
         * @param contextWithNewDefinitions
         * @return 
         */
        public static ScriptGenerator newCreationInstance(final RadixObject contextWithNewDefinitions) {
            if (contextWithNewDefinitions == null) {
                throw new IllegalArgumentException("Context can't be null");
            }
            else {
                return newCreationInstance(contextWithNewDefinitions, false);
            }
        }

        public static ScriptGenerator newCreationInstance(final RadixObject contextWithNewDefinitions, final boolean excludeContext) {
            if (contextWithNewDefinitions == null) {
                throw new IllegalArgumentException("Context can't be null");
            }
            else {
                final List<DdsDefinition> newDefinitions = new ArrayList<>();
                
                ScriptDefinitionsCollector.collect(contextWithNewDefinitions, newDefinitions);
                if (excludeContext) {
                    newDefinitions.remove(contextWithNewDefinitions);
                }
                return new ScriptGeneratorImpl(null, newDefinitions);
            }
        }

        /**
         * Создать генератор скриптов создания дефиниций указанного контекста.
         * @param definitions
         * @return 
         */
        public static ScriptGenerator newCreationInstance(final Set<DdsDefinition> definitions) {
            if (definitions == null) {
                throw new IllegalArgumentException("Definintions can't be null");
            }
            else {
                final List<DdsDefinition> newDefinitions = new ArrayList<>();
                
                for (DdsDefinition definition : definitions) {
                    ScriptDefinitionsCollector.collect(definition, newDefinitions);
                }
                return new ScriptGeneratorImpl(null, newDefinitions);
            }
        }
    }
}
