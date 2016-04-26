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
package org.radixware.kernel.common.defs.ads.clazz.sql;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.DefinitionSearcher;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.RadixObjects.ContainerChangedEvent;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.AdsDefinitions;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsParameterPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsSqlClassExecuteMethod;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsSystemMethodDef;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.defs.ads.module.ModuleDefinitions;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.CodeWriter;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.defs.ads.src.clazz.sql.AdsSqlClassWriter;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.enums.EParamDirection;
import org.radixware.kernel.common.enums.EPropNature;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.common.sqml.ads.AdsSqmlEnvironment;
import org.radixware.kernel.common.utils.Utils;

/**
 * SQL Class Definition.
 *
 */
public abstract class AdsSqlClassDef extends AdsClassDef {

    public static final String PLATFORM_CLASS_NAME = "org.radixware.kernel.server.types.SqlClass";
    public static final int FORMAT_VERSION = 7;
    private static final int DEFAULT_CACHE_SIZE = 10;
    private int cacheSize = DEFAULT_CACHE_SIZE;
    private boolean noTest = false;
    private boolean dbReadOnly = false;
    //
    private final RadixObjects.ContainerChangesListener listener = new RadixObjects.ContainerChangesListener() {
        @Override
        public void onEvent(ContainerChangedEvent e) {
            updateMethodsParams();
        }
    };

    public void updateMethodsParams() {
        if (getMethods() != null) {
            AdsMethodDef method = getMethods().findById(AdsSystemMethodDef.ID_STMT_EXECUTE, ExtendableDefinitions.EScope.LOCAL).get();
            if (method instanceof AdsSqlClassExecuteMethod) {
                ((AdsSqlClassExecuteMethod) method).checkProfileState();
            }
        }
    }

    protected AdsSqlClassDef(org.radixware.schemas.adsdef.ClassDefinition xDef) {
        super(xDef);

        final org.radixware.schemas.adsdef.SqlStatement xStatement = xDef.getSql();
        if (xStatement != null) {
            getSource().loadFrom(xStatement.getSource());
            getUsedTables().loadFrom(xStatement.getUsedTables());
            if (xStatement.isSetCacheSize()) {
                this.cacheSize = xStatement.getCacheSize();
            }
            if (xStatement.isSetNoTest()) {
                noTest = xStatement.getNoTest();
            }
            if (xStatement.isSetIsReadOnly()) {
                dbReadOnly = xStatement.getIsReadOnly();
            }

        }
        getProperties().getLocal().getContainerChangesSupport().addEventListener(listener);
    }

    protected AdsSqlClassDef(Id id, String name) {
        super(id, name);
        getProperties().getLocal().getContainerChangesSupport().addEventListener(listener);
    }

    // for overwrite
    protected AdsSqlClassDef(AdsSqlClassDef source) {
        super(source);
        getProperties().getLocal().getContainerChangesSupport().addEventListener(listener);
    }

    public boolean isIgnoreSqlCheck() {
        return noTest;
    }

    public void setIgnoreSqlCheck(boolean ignore) {
        if (noTest != ignore) {
            noTest = ignore;
            setEditState(EEditState.MODIFIED);
        }
    }

    @Override
    public long getFormatVersion() {
        return FORMAT_VERSION;
    }

    @Override
    public void appendTo(final org.radixware.schemas.adsdef.ClassDefinition xClassDef, ESaveMode saveMode) {
        super.appendTo(xClassDef, saveMode);
        if (saveMode == ESaveMode.NORMAL) {
            final org.radixware.schemas.adsdef.SqlStatement xStatement = xClassDef.addNewSql();
            getSource().appendTo(xStatement.addNewSource());
            getUsedTables().appendTo(xStatement);
            if (cacheSize != DEFAULT_CACHE_SIZE) {
                xStatement.setCacheSize(cacheSize);
            }
            if (noTest) {
                xStatement.setNoTest(true);
            }
            xStatement.setIsReadOnly(isDbReadOnly());
        }
    }

    public boolean isParentPropsAllowed() {
        for (AdsPropertyDef p : getProperties().get(ExtendableDefinitions.EScope.ALL)) {
            if (p.getValue().getType().getTypeId() == EValType.PARENT_REF && p.getNature() != EPropNature.FIELD_REF) {
                return true;
            }
        }
        return false;
    }

    public void setDbReadOnly(boolean dbReadOnly) {
        if (!Utils.equals(this.dbReadOnly, dbReadOnly)) {
            this.dbReadOnly = dbReadOnly;
            setEditState(EEditState.MODIFIED);
        }
    }

    /**
     * @return true if cursor SQL query is not modified database.
     */
    public boolean isDbReadOnly() {
        return dbReadOnly;
    }

    @Override
    public boolean isFinal() {
        return true;
    }

    @Override
    public boolean canChangeFinality() {
        return false;
    }

    @Override
    public boolean isSaveable() {
        return true;
    }

    @Override
    public Set<ERuntimeEnvironmentType> getTypeUsageEnvironments() {
        return EnumSet.of(ERuntimeEnvironmentType.SERVER);
    }

    @Override
    public void afterOverwrite() {
    }

    @Override
    public boolean isSuitableContainer(AdsDefinitions collection) {
        return collection instanceof ModuleDefinitions;
    }

    /**
     * Allows to cache prepared SQL statements in Server. Sometimes generated
     * SQL is differs for differs values of parametes, so it is possible to
     * cache differs SQL. Prepared spatements cached in static field of
     * generated java class for this definition.
     *
     * @return number of cached prepared SQL statements.
     */
    public int getCacheSize() {
        return cacheSize;
    }

    public void setCacheSize(int cacheSize) {
        if (!Utils.equals(this.cacheSize, cacheSize)) {
            this.cacheSize = cacheSize;
            setEditState(EEditState.MODIFIED);
        }
    }

    /**
     * Information about table that used in Sql Class. Allows to specify alias
     * for each table, or use one table with several aliases.
     */
    public class UsedTable extends RadixObject {

        private Id tableId;
        private String alias;

        protected UsedTable() {
        }

        protected UsedTable(final org.radixware.schemas.adsdef.SqlStatement.UsedTables.UsedTable xUsedTable) {
            this.tableId = xUsedTable.getTableId();
            this.alias = xUsedTable.getAlias();
        }

        protected void appendTo(final org.radixware.schemas.adsdef.SqlStatement.UsedTables.UsedTable xUsedTable) {
            xUsedTable.setTableId(tableId);
            if (alias != null && !alias.isEmpty()) {
                xUsedTable.setAlias(alias);
            }
        }

        public Id getTableId() {
            return tableId;
        }

        /**
         * @return defined alias or null or empty string if not defined.
         */
        public String getAlias() {
            return alias;
        }

        public void setTableId(Id tableId) {
            if (!Utils.equals(this.tableId, tableId)) {
                this.tableId = tableId;
                setEditState(EEditState.MODIFIED);
            }
        }

        public void setAlias(String alias) {
            if (!Utils.equals(this.alias, alias)) {
                this.alias = alias;
                setEditState(EEditState.MODIFIED);
            }
        }

        /**
         * Find used table.
         *
         * @return used table or null if null found.
         */
        public DdsTableDef findTable() {
            final DefinitionSearcher<DdsTableDef> tableSearcher = AdsSearcher.Factory.newDdsTableSearcher(AdsSqlClassDef.this);
            return tableSearcher.findById(tableId).get();
        }

        /**
         * Find used table.
         *
         * @return used table.
         * @throws DefinitionNotFoundError
         */
        public DdsTableDef getTable() {
            final DdsTableDef usedTable = findTable();
            if (usedTable != null) {
                return usedTable;
            } else {
                throw new DefinitionNotFoundError(tableId);
            }
        }

        @Override
        public void collectDependences(final List<Definition> list) {
            super.collectDependences(list);
            final DdsTableDef table = findTable();
            if (table != null) {
                list.add(table);
            }
        }
    }

    public class UsedTables extends RadixObjects<UsedTable> {

        public UsedTables() {
            super(AdsSqlClassDef.this);
        }

        protected void loadFrom(final org.radixware.schemas.adsdef.SqlStatement.UsedTables xUsedTables) {
            clear();

            if (xUsedTables != null) {
                final List<org.radixware.schemas.adsdef.SqlStatement.UsedTables.UsedTable> xUsedTablesList = xUsedTables.getUsedTableList();
                if (xUsedTablesList != null) {
                    for (org.radixware.schemas.adsdef.SqlStatement.UsedTables.UsedTable xUsedTable : xUsedTablesList) {
                        final UsedTable usedTable = new UsedTable(xUsedTable);
                        add(usedTable);
                    }
                }
            }
        }

        protected void appendTo(final org.radixware.schemas.adsdef.SqlStatement xStatement) {
            if (!isEmpty()) {
                final org.radixware.schemas.adsdef.SqlStatement.UsedTables xUsedTables = xStatement.addNewUsedTables();
                for (UsedTable usedTable : this) {
                    final org.radixware.schemas.adsdef.SqlStatement.UsedTables.UsedTable xUsedTable = xUsedTables.addNewUsedTable();
                    usedTable.appendTo(xUsedTable);
                }
            }
        }

        @Override
        protected void appendAdditionalToolTip(StringBuilder sb) {
            super.appendAdditionalToolTip(sb);
        }

        public UsedTable add(final Id tableId, final String alias) {
            final UsedTable usedTable = new UsedTable();
            usedTable.setTableId(tableId);
            usedTable.setAlias(alias);
            add(usedTable);
            return usedTable;
        }

        public UsedTable add(final DdsTableDef table, final String alias) {
            return add(table.getId(), alias);
        }
    }
    private final UsedTables usedTables = new UsedTables();

    public UsedTables getUsedTables() {
        return usedTables;
    }

    private final class Source extends Sqml {

        public Source() {
            super(AdsSqlClassDef.this);
            final AdsSqmlEnvironment env = AdsSqmlEnvironment.Factory.newInstance(AdsSqlClassDef.this);
            setEnvironment(env);
        }
    }
    private final Sqml source = new Source();

    /**
     * @return source SQML statement.
     */
    public Sqml getSource() {
        return source;
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        getUsedTables().visit(visitor, provider);
        getSource().visit(visitor, provider);
    }

    @Override
    public JavaSourceSupport getJavaSourceSupport() {
        return new ClassJavaSourceSupport() {
            @Override
            @SuppressWarnings("unchecked")
            public CodeWriter getCodeWriter(UsagePurpose purpose) {
                return new AdsSqlClassWriter(this, AdsSqlClassDef.this, purpose);
            }
        };
    }

    @Override
    public ERuntimeEnvironmentType getUsageEnvironment() {
        return ERuntimeEnvironmentType.SERVER;
    }

    public List<AdsParameterPropertyDef> getInputParameters() {
        final List<AdsParameterPropertyDef> result = new ArrayList<>();
        for (AdsPropertyDef prop : this.getProperties().getLocal()) {
            if (prop.getNature() == EPropNature.SQL_CLASS_PARAMETER) {
                final AdsParameterPropertyDef parameterProperty = (AdsParameterPropertyDef) prop;
                if (parameterProperty.calcDirection() != EParamDirection.OUT) {
                    result.add(parameterProperty);
                }
            }
        }
        return result;
    }
}
