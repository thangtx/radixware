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
package org.radixware.kernel.common.defs.dds;

import org.radixware.kernel.common.defs.dds.radixdoc.DdsTableRadixdocSupport;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.defs.ClipboardSupport;
import org.radixware.kernel.common.defs.ClipboardSupport.Transfer;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.HierarchyIterator;
import org.radixware.kernel.common.defs.IFilter;
import org.radixware.kernel.common.defs.IOverwritable;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.dds.providers.DdsVisitorProviderFactory;
import org.radixware.kernel.common.defs.dds.utils.DbNameUtils;
import org.radixware.kernel.common.defs.dds.utils.TablespaceCalculator;
import org.radixware.kernel.common.enums.EDdsTableExtOption;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EDocGroup;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.common.radixdoc.DocumentOptions;
import org.radixware.kernel.common.radixdoc.IRadixdocPage;
import org.radixware.kernel.common.radixdoc.IRadixdocProvider;
import org.radixware.kernel.common.radixdoc.RadixdocSupport;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.dds.DdsSegment;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.schemas.radixdoc.Page;

/**
 * Matainformation about table in database. Table can be overritten. Overritten
 * table can add own columns, indices, triggers. Overritten table has the same
 * identifier and located in module with the same identifier in upper layer.
 *
 */
public class DdsTableDef extends DdsDefinition implements IPlacementSupport, IDdsAutoDbNamedDefinition, IOverwritable, IRadixdocProvider {
    
    @Override
    public void afterOverwrite() {
        // NOTHING
    }

    @Override
    public boolean allowOverwrite() {
        return true;
    }

    @Override
    public boolean needsDocumentation() {
        return true;
    }

    /**
     * Information about audit. See rx_audit.doc.
     */
    public class AuditInfo {

        private final DdsAuditMask auditMask;
        private Id auditReferenceId = null;

        public AuditInfo() {
            this.auditMask = new DdsAuditMask(DdsTableDef.this);
        }

        /**
         * Получить идентификатор {@link DdsReferenceDef связи} для аудита.
         */
        public Id getAuditReferenceId() {
            return auditReferenceId;
        }

        public void setAuditReferenceId(Id auditReferenceId) {
            if (!Utils.equals(this.auditReferenceId, auditReferenceId)) {
                this.auditReferenceId = auditReferenceId;
                DdsTableDef.this.setEditState(EEditState.MODIFIED);
            }
        }

        /**
         * Find audit reference.
         *
         * @return reference or null it not found or not defined.
         */
        public DdsReferenceDef findAuditReference() {
            if (auditReferenceId != null) {
                DdsModule ownerModule = getModule();
                if (ownerModule != null) {
                    return ownerModule.getDdsReferenceSearcher().findById(auditReferenceId).get();
                }
            }

            return null;
        }

        /**
         * Find audit reference.
         *
         * @return audit reference or null if not defined.
         * @throws DefinitionNotFoundError if defined and not found.
         */
        public DdsReferenceDef getAuditReference() {
            if (auditReferenceId != null) {
                DdsReferenceDef parentFamilyReference = findAuditReference();
                if (parentFamilyReference != null) {
                    return parentFamilyReference;
                }
                throw new DefinitionNotFoundError(auditReferenceId);
            } else {
                return null;
            }
        }

        protected AuditInfo(org.radixware.schemas.ddsdef.Table xTable) {
            super();

            this.auditMask = new DdsAuditMask(DdsTableDef.this);

            if (xTable.isSetAuditMask()) {
                this.auditMask.loadFrom(xTable.getAuditMask());
            }

            if (xTable.isSetAuditReferenceId()) {
                this.auditReferenceId = Id.Factory.loadFrom(xTable.getAuditReferenceId());
            }
        }

        public boolean isEnabledForInsert() {
            return auditMask.isInsert();
        }

        public void setEnabledForInsert(boolean enabled) {
            auditMask.setInsert(enabled);
        }

        public boolean isEnabledForUpdate() {
            return auditMask.isUpdate();
        }

        public void setEnabledForUpdate(boolean enabled) {
            auditMask.setUpdate(enabled);
        }

        public boolean isEnabledForDelete() {
            return auditMask.isDelete();
        }

        public void setEnabledForDelete(boolean enabled) {
            auditMask.setDelete(enabled);
        }

        public void appendTo(org.radixware.schemas.ddsdef.Table xTable) {
            if (auditMask.toLong() != 0) {
                xTable.setAuditMask(auditMask.toLong());

                if (auditReferenceId != null) {
                    xTable.setAuditReferenceId(auditReferenceId.toString());
                }
            }
        }

        public boolean isEnabled() {
            return auditMask.isEnabled();
        }
    }
    private final AuditInfo auditInfo;
    private DdsColumnDef savedClassGuidColumn;

    /**
     * @return information about audit of the table.
     */
    public AuditInfo getAuditInfo() {
        DdsTableDef overwritten = findOverwritten();
        if (overwritten != null) {
            return overwritten.getAuditInfo();
        } else {
            return auditInfo;
        }
    }

    private static abstract class DdsTableItemInheritanceIterator<T extends DdsDefinition & IDdsTableItemDef> extends HierarchyIterator<ExtendableDefinitions<T>> {

        DdsTableDef curTable;
        DdsTableDef nextTable = null;

        private DdsTableItemInheritanceIterator(DdsTableDef init, HierarchyIterator.Mode mode) {
            super(mode);
            curTable = init;
            nextTable = init;
        }

        @Override
        public boolean hasNext() {
            if (nextTable == null && curTable != null) {
                nextTable = curTable.findOverwritten();
            }
            return nextTable != null;
        }

        @Override
        public final Chain<ExtendableDefinitions<T>> next() {
            if (hasNext()) {
                curTable = nextTable;
                nextTable = null;
            }
            return Chain.newInstance(curTable == null ? null : getItems(curTable));
        }

        public abstract ExtendableDefinitions<T> getItems(DdsTableDef table);
    }

    private class DdsLocalColumns extends DdsDefinitions<DdsColumnDef> {

        public DdsLocalColumns() {
            super();
        }

        @Override
        protected ClipboardSupport.CanPasteResult canPaste(Transfer objectInClipboard) {
            ClipboardSupport.CanPasteResult result = super.canPaste(objectInClipboard);
            if (result != ClipboardSupport.CanPasteResult.YES) {
                return result;
            }

            return objectInClipboard.getObject() instanceof DdsColumnDef ? ClipboardSupport.CanPasteResult.YES : ClipboardSupport.CanPasteResult.NO;
        }

        @Override
        public RadixIcon getIcon() {
            return DdsDefinitionIcon.COLUMN;
        }
    }

    @Override
    public void setFrozen(boolean frozen) {
        super.setFrozen(frozen);
        columns.setFrozen(frozen);
        columns.getLocal().findById(getId());//any non-null ID can be used, need to initialize internal caches
        columns.getLocal().setFrozen(true);
    }

    private class DdsColumns extends DdsExtendableDefinitions<DdsColumnDef> {

        protected DdsColumns() {
            super(DdsTableDef.this, new DdsLocalColumns());
        }
        
        @Override
        public String getName() {
            return "Columns";
        }

        @Override
        protected HierarchyIterator<ExtendableDefinitions<DdsColumnDef>> newIterator(EScope scope, HierarchyIterator.Mode mode) {
            return new DdsTableItemInheritanceIterator<DdsColumnDef>(getOwnerTable(), mode) {
                @Override
                public ExtendableDefinitions<DdsColumnDef> getItems(DdsTableDef table) {
                    return table.getColumns();
                }
            };
        }
    }
    private final DdsExtendableDefinitions<DdsColumnDef> columns = new DdsColumns();

    /**
     * Получить список колонок таблицы.
     */
    public DdsExtendableDefinitions<DdsColumnDef> getColumns() {
        return columns;
    }
    private final static String CLASS_GUID_COLUMN_DB_NAME = "CLASSGUID";

    /**
     * Find column that contain class GUID.
     *
     * @return Class GUID column or null if not found.
     */
    public DdsColumnDef findClassGuidColumn() {
        if (savedClassGuidColumn != null) {
            return savedClassGuidColumn;
        } else {
            DdsColumnDef column = getColumns().findByDbName(CLASS_GUID_COLUMN_DB_NAME);
            if (isFrozen()) {
                savedClassGuidColumn = column;
            }
            return column;
        }
    }

    /**
     * Find column that contain class GUID.
     *
     * @throws DefinitionError if not found.
     */
    public DdsColumnDef getClassGuidColumn() {
        DdsColumnDef classGuidColumn = findClassGuidColumn();
        if (classGuidColumn == null) {
            throw new DefinitionError(CLASS_GUID_COLUMN_DB_NAME + " column not found.", this);
        }
        return classGuidColumn;
    }

    private class DdsLocalIndices extends DdsDefinitions<DdsIndexDef> {

        boolean isLoading = false;

        public DdsLocalIndices() {
            super();
        }

        @Override
        protected ClipboardSupport.CanPasteResult canPaste(Transfer objectInClipboard) {
            ClipboardSupport.CanPasteResult result = super.canPaste(objectInClipboard);
            if (result != ClipboardSupport.CanPasteResult.YES) {
                return result;
            }

            return (objectInClipboard.getObject() instanceof DdsIndexDef)
                    && (!(objectInClipboard.getObject() instanceof DdsPrimaryKeyDef)) ? ClipboardSupport.CanPasteResult.YES : ClipboardSupport.CanPasteResult.NO;
        }

        @Override
        public RadixIcon getIcon() {
            return DdsDefinitionIcon.INDEX;
        }

        @Override
        protected void onAdd(DdsIndexDef definition) {
            super.onAdd(definition);
            if (!isLoading) {
                if (getOwnerDefinition() instanceof DdsViewDef) {
                    definition.setGeneratedInDb(false);
                }
            }
        }

        @Override
        public String toString() {
            return "DdsLocalIndices{" + "isLoading=" + isLoading + "}: "+super.toString();
        }
    }

    private class DdsIndices extends DdsExtendableDefinitions<DdsIndexDef> {

        protected DdsIndices() {
            super(DdsTableDef.this, new DdsLocalIndices());
        }

        @Override
        protected HierarchyIterator<ExtendableDefinitions<DdsIndexDef>> newIterator(EScope scope, HierarchyIterator.Mode mode) {
            return new DdsTableItemInheritanceIterator<DdsIndexDef>(getOwnerTable(), mode) {
                @Override
                public ExtendableDefinitions<DdsIndexDef> getItems(DdsTableDef table) {
                    return table.getIndices();
                }
            };
        }
        
        @Override
        public String getName() {
            return "Indices";
        }

        @Override
        public String toString() {
            return "DdsIndices{" + '}';
        }
        
        
    }
    private final DdsExtendableDefinitions<DdsIndexDef> indices = new DdsIndices();

    /**
     * Получить список индексов таблицы.
     */
    public DdsExtendableDefinitions<DdsIndexDef> getIndices() {
        return indices;
    }

    private class DdsLocalTriggers extends DdsDefinitions<DdsTriggerDef> {

        public DdsLocalTriggers() {
            super();
        }

        @Override
        protected ClipboardSupport.CanPasteResult canPaste(Transfer objectInClipboard) {
            ClipboardSupport.CanPasteResult result = super.canPaste(objectInClipboard);
            if (result != ClipboardSupport.CanPasteResult.YES) {
                return result;
            }

            return (objectInClipboard.getObject() instanceof DdsTriggerDef) ? ClipboardSupport.CanPasteResult.YES : ClipboardSupport.CanPasteResult.NO;
        }

        @Override
        public RadixIcon getIcon() {
            return DdsDefinitionIcon.TRIGGER;
        }
    }

    private class DdsTriggers extends DdsExtendableDefinitions<DdsTriggerDef> {

        protected DdsTriggers() {
            super(DdsTableDef.this, new DdsLocalTriggers());
        }

        @Override
        protected HierarchyIterator<ExtendableDefinitions<DdsTriggerDef>> newIterator(EScope scope, HierarchyIterator.Mode mode) {
            return new DdsTableItemInheritanceIterator<DdsTriggerDef>(getOwnerTable(), mode) {
                @Override
                public ExtendableDefinitions<DdsTriggerDef> getItems(DdsTableDef table) {
                    return table.getTriggers();
                }
            };
        }
        
        @Override
        public String getName() {
            return "Triggers";
        }
    }
    private final DdsExtendableDefinitions<DdsTriggerDef> triggers = new DdsTriggers();

    /**
     * Получить список триггеров таблицы.
     */
    public DdsExtendableDefinitions<DdsTriggerDef> getTriggers() {
        return triggers;
    }
    private String dbName = "";

    /**
     * Получить имя таблицы в базе данных. Используется для генерации SQL
     * выражений.
     */
    @Override
    public String getDbName() {
        DdsTableDef overwritten = findOverwritten();
        if (overwritten != null) {
            return overwritten.getDbName();
        } else {
            return dbName;
        }
    }

    @Override
    public void setDbName(String dbName) {
        if (!Utils.equals(this.dbName, dbName)) {
            this.dbName = dbName;
            this.setEditState(EEditState.MODIFIED);
        }
    }
    private boolean autoDbName = true;

    @Override
    public boolean isAutoDbName() {
        return autoDbName;
    }

    public void setAutoDbName(boolean autoDbName) {
        if (!Utils.equals(this.autoDbName, autoDbName)) {
            this.autoDbName = autoDbName;
            setEditState(EEditState.MODIFIED);
        }
    }
    boolean generatedInDb = true;

    /**
     * Генерируется-ли таблица в базе данных. Таблица платформы может не
     * генерироваться в базе данных, но существовать в ней, обычно это системные
     * таблицы, метаинформация о которых вытащена в
     * {@link DdsModelDef DDS модель}.
     */
    @Override
    public boolean isGeneratedInDb() {
        final DdsTableDef overwritten = findOverwritten();
        if (overwritten != null) {
            return overwritten.isGeneratedInDb();
        } else {
            return generatedInDb;
        }
    }

    public void setGeneratedInDb(boolean generatedInDb) {
        if (!Utils.equals(this.generatedInDb, generatedInDb)) {
            this.generatedInDb = generatedInDb;
            setEditState(EEditState.MODIFIED);
        }
    }
    private boolean hidden = false;

    /**
     * Должна-ли таблица показываться пользователю. Используется на более
     * высоких уровнях, когда требуется скрыть от пользователя некоторые таблицы
     * в списках в целях дополнительной безопасности.
     */
    public boolean isHidden() {
        DdsTableDef overwritten = findOverwritten();
        if (overwritten != null) {
            return overwritten.isHidden();
        } else {
            return hidden;
        }
    }

    public void setHidden(boolean hidden) {
        if (!Utils.equals(this.hidden, hidden)) {
            this.hidden = hidden;
            setEditState(EEditState.MODIFIED);
        }
    }
    private final DdsPrimaryKeyDef primaryKey;

    /**
     * Получить метаинформацию о первичном ключе таблицы в базе данных.
     * Первичный ключ у таблицы RadixWare обязан быть всегда.
     */
    public DdsPrimaryKeyDef getPrimaryKey() {
        DdsTableDef overwritten = findOverwritten();
        if (overwritten != null) {
            return overwritten.getPrimaryKey();
        } else {
            return primaryKey;
        }
    }
    private final DdsDefinitionPlacement placement;

    @Override
    public DdsDefinitionPlacement getPlacement() {
        return placement;
    }

    protected DdsTableDef(final Id id, final String name) {
        super(id, name);
        this.primaryKey = DdsPrimaryKeyDef.Factory.newInstance(this);
        this.placement = DdsDefinitionPlacement.Factory.newInstance(this);
        this.auditInfo = new AuditInfo();
    }

    protected DdsTableDef(DdsTableDef overwrittenTable) {
        this(overwrittenTable.getId(), overwrittenTable.getName());
        overwrite = true;
    }

    protected DdsTableDef(final String name) {
        this(Id.Factory.newInstance(EDefinitionIdPrefix.DDS_TABLE), name);
    }

    protected DdsTableDef(org.radixware.schemas.ddsdef.Table xTable) {
        super(xTable);
        this.primaryKey = DdsPrimaryKeyDef.Factory.newInstance(this, xTable.getPrimaryKey());
        this.dbName = xTable.getDbName();

        if (xTable.isSetIsOverwrite()) {
            overwrite = xTable.getIsOverwrite();
        }

        if (xTable.isSetAutoDbName()) {
            this.autoDbName = xTable.getAutoDbName();
        }

        if (xTable.isSetGenerateInDb()) {
            this.generatedInDb = xTable.getGenerateInDb();
        }

        if (xTable.isSetHidden()) {
            this.hidden = xTable.getHidden();
        }

        this.auditInfo = new AuditInfo(xTable);

        if (xTable.isSetUserExtMask()) {
            long userExtMask = xTable.getUserExtMask();
            for (EDdsTableExtOption extOption : EDdsTableExtOption.values()) {
                if ((extOption.getValue() & userExtMask) != 0L) {
                    extOptions.add(extOption);
                }
            }
        }

        this.placement = DdsDefinitionPlacement.Factory.loadFrom(this, xTable.getPlacement());

        if (xTable.isSetColumns()) {
            final List<org.radixware.schemas.ddsdef.Column> xColumns = xTable.getColumns().getColumnList();
            for (org.radixware.schemas.ddsdef.Column xColumn : xColumns) {
                final DdsColumnDef column = DdsColumnDef.Factory.loadFrom(xColumn);
                this.getColumns().getLocal().add(column);
            }
        }

        if (xTable.isSetDbOptions()) {
            List<org.radixware.schemas.ddsdef.Table.DbOptions.DbOption> xDbOptionsList = xTable.getDbOptions().getDbOptionList();
            for (org.radixware.schemas.ddsdef.Table.DbOptions.DbOption xDbOption : xDbOptionsList) {
                final String dbOptionName = xDbOption.getName();
                final String dbOptionValue = (xDbOption.isSetValue() ? xDbOption.getValue() : null);
                if ("global temporary".equals(dbOptionName)) {
                    this.globalTemporary = true;
                } else if ("on commit preserve rows".equals(dbOptionName)) {
                    this.onCommitPreserveRows = true;
                } else if ("partition".equals(dbOptionName)) {
                    this.partition.setSql(dbOptionValue);
                } else if ("tablespace".equals(dbOptionName)) {
                    this.tablespace = dbOptionValue;
                }
            }
        }

        if (xTable.isSetTablespace()) {
            this.tablespace = xTable.getTablespace();
        }

        if (xTable.isSetPartition()) {
            this.partition.loadFrom(xTable.getPartition());
        }

        if (xTable.isSetOnCommitPreserveRows()) {
            this.onCommitPreserveRows = xTable.getOnCommitPreserveRows();
        }

        if (xTable.isSetGlobalTemporary()) {
            this.globalTemporary = xTable.getGlobalTemporary();
        }

        if (xTable.isSetIndices()) {

            try {
                ((DdsLocalIndices) this.getIndices().getLocal()).isLoading = true;
                final List<org.radixware.schemas.ddsdef.Index> xIndices = xTable.getIndices().getIndexList();
                for (org.radixware.schemas.ddsdef.Index xIndex : xIndices) {
                    final DdsIndexDef index = DdsIndexDef.Factory.loadFrom(xIndex);
                    this.getIndices().getLocal().add(index);
                }
            } finally {
                ((DdsLocalIndices) this.getIndices().getLocal()).isLoading = false;
            }
        }

        if (xTable.isSetTriggers()) {
            final List<org.radixware.schemas.ddsdef.Trigger> xTriggers = xTable.getTriggers().getTriggerList();
            for (org.radixware.schemas.ddsdef.Trigger xTrigger : xTriggers) {
                final DdsTriggerDef trigger = DdsTriggerDef.Factory.loadFrom(xTrigger);
                this.getTriggers().getLocal().add(trigger);
            }
        }
        if (xTable.isSetDeprecated()) {
            this.isDeprecated = xTable.getDeprecated();
        }
    }
    private boolean isDeprecated = false;

    @Override
    public boolean isDeprecated() {
        return isDeprecated || super.isDeprecated();
    }

    public void setDeprecated(boolean isDeprecated) {
        if (this.isDeprecated != isDeprecated) {
            this.isDeprecated = isDeprecated;
            fireNameChange();
            setEditState(EEditState.MODIFIED);
        }
    }
    private final EnumSet<EDdsTableExtOption> extOptions = EnumSet.noneOf(EDdsTableExtOption.class);

    /**
     * Get extension options.
     */
    public EnumSet<EDdsTableExtOption> getExtOptions() {
        DdsTableDef overwritten = findOverwritten();
        if (overwritten != null) {
            return overwritten.getExtOptions();
        } else {
            return extOptions;
        }
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        if (!isOverwrite()) {
            this.getPrimaryKey().visit(visitor, provider);
        }
        this.getColumns().visit(visitor, provider);
        this.getIndices().visit(visitor, provider);
        this.getTriggers().visit(visitor, provider);
        if (this.getPartition() != null) {
            this.getPartition().visit(visitor, provider);
        }
    }

    @Override
    public RadixIcon getIcon() {
        if (isOverwrite()) {
            return DdsDefinitionIcon.TABLE_OVERWRITE;
        } else {
            return DdsDefinitionIcon.TABLE;
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(super.toString());
        result.append("; Overwrite: ");
        result.append(isOverwrite());
        result.append("; DbName: ");
        result.append(dbName);
        result.append("; GeneratedInDb: ");
        result.append(generatedInDb);

        if (!(this instanceof DdsViewDef)) {
            result.append("; Tablespace: ");
            result.append(TablespaceCalculator.calcTablespaceForTable(this));
        }

        return result.toString();
    }

    public static final class Factory {

        private Factory() {
        }

        public static DdsTableDef newInstance(final String name) {
            return new DdsTableDef(name);
        }

        public static DdsTableDef newOverwrite(DdsTableDef overwrittenTable) {
            return new DdsTableDef(overwrittenTable);
        }

        public static DdsTableDef loadFrom(org.radixware.schemas.ddsdef.Table xTable) {
            return new DdsTableDef(xTable);
        }

        /**
         * Do not use this function instead of table editor. This functions
         * allows to simplify table editor.
         */
        public static DdsTableDef newInstance(DdsTableDef src, DdsModelDef ownerModel) {
            final DdsTableDef table = src.getClipboardSupport().copy();
            table.setContainer(ownerModel.getTables());
            return table;
        }
    }

    public DdsTableDef findOverwritten() {
        DdsModule module = getModule();
        if (module != null) {
            for (DdsModule ovr = module.findOverwritten(); ovr != null; ovr = ovr.findOverwritten()) {
                DdsModelDef model = ovr.getModelManager().findModel();
                if (model != null) {
                    DdsTableDef base = model.getTables().findById(this.getId());
                    if (base != null) {
                        return base;
                    }
                }
            }
        }
        return null;
    }
    private boolean overwrite = false;

    @Override
    public boolean setOverwrite(boolean overwrite) {
        if (!Utils.equals(this.overwrite, overwrite)) {
            this.overwrite = overwrite;
            setEditState(EEditState.MODIFIED);
            return true;
        }
        return false;
    }
    // switch to public access

    @Override
    public boolean isOverwrite() {
        return overwrite;
    }

    private Set<DdsReferenceDef> collectReferences(IFilter<DdsReferenceDef> primaryFilter, IFilter<DdsReferenceDef> secondaryFilter) {
        final Set<DdsReferenceDef> result = new HashSet<DdsReferenceDef>();
        final Branch branch = DdsTableDef.this.getBranch();
        if (branch == null) {
            return Collections.emptySet();
        }
        for (Layer layer : branch.getLayers()) {
            if (layer.isLocalizing()) {
                continue;
            }
            for (DdsModule module : ((DdsSegment) layer.getDds()).getModules()) {
                final DdsModelDef model = module.getModelManager().findModel();
                if (model != null) {
                    for (DdsReferenceDef ref : model.getReferences()) {
                        if (primaryFilter.isTarget(ref)) {
                            if (secondaryFilter == null || secondaryFilter.isTarget(ref)) {
                                result.add(ref);
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * Collect all outgoing references for the table from the whole branch.
     *
     * @return list of outgoing references, not null.
     * @throws NullPointerException if table is not in branch.
     */
    public Set<DdsReferenceDef> collectOutgoingReferences() {
        return collectOutgoingReferences(null);
    }

    /**
     * Collect all incoming references for the table from the whole branch.
     *
     * @return list of incoming references, not null.
     * @throws NullPointerException if table is not in branch.
     */
    public Set<DdsReferenceDef> collectIncomingReferences() {
        return collectIncomingReferences(null);
    }

    /**
     * Collect all outgoing references for the table from the whole branch.
     *
     * @return list of outgoing references, not null.
     * @throws NullPointerException if table is not in branch.
     */
    public Set<DdsReferenceDef> collectOutgoingReferences(IFilter<DdsReferenceDef> secondaryFilter) {
        final IFilter<DdsReferenceDef> primaryFiler = DdsVisitorProviderFactory.newOutgoingReferencesFilter(this);
        return collectReferences(primaryFiler, secondaryFilter);
    }

    /**
     * Collect all incoming references for the table from the whole branch.
     *
     * @return list of incoming references, not null.
     * @throws NullPointerException if table is not in branch.
     */
    public Set<DdsReferenceDef> collectIncomingReferences(IFilter<DdsReferenceDef> secondaryFilter) {
        final IFilter<DdsReferenceDef> primaryFiler = DdsVisitorProviderFactory.newIncomingReferencesFilter(this);
        return collectReferences(primaryFiler, secondaryFilter);
    }

    @Override
    public String calcAutoDbName() {
        DdsModelDef ownerModel = getOwnerModel();
        String name = getName();
        String dbNamePrefix = ownerModel.getDbAttributes().getDbNamePrefix();
        return DbNameUtils.calcAutoDbName(dbNamePrefix, name);
    }

    @Override
    public void collectDependences(List<Definition> list) {
        super.collectDependences(list);

        final DdsTableDef overwritten = findOverwritten();
        if (overwritten != null) {
            list.add(overwritten);
        }

        final DdsReferenceDef auditReference = getAuditInfo().findAuditReference();
        if (auditReference != null) {
            list.add(auditReference);
        }
    }

    /**
     * @return true if index columns structure satisfied to specified one.
     */
    private boolean isSatisfied(final List<Id> columnIds, final DdsIndexDef index) {
        int size = columnIds.size();
        if (size == 0 || index.getColumnsInfo().size() != size) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            Id idxColumnId = index.getColumnsInfo().get(i).getColumnId();
            Id listColumnId = columnIds.get(i);
            if (!Utils.equals(idxColumnId, listColumnId)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Find primary or secondary key that satisfied to specified list of column
     * identifiers. Order of identifiers is taken into account.
     *
     * @return index or null if not found.
     */
    public DdsIndexDef findKey(final List<Id> columnIds) {
        DdsIndexDef pk = getPrimaryKey();
        if (isSatisfied(columnIds, pk)) {
            return pk;
        }
        for (DdsIndexDef idx : getIndices().get(EScope.LOCAL_AND_OVERWRITE)) {
            if (idx.isSecondaryKey()) {
                if (isSatisfied(columnIds, idx)) {
                    return idx;
                }
            }
        }
        return null;
    }

    private class DdsTableClipboardSupport extends DdsClipboardSupport<DdsTableDef> {

        public DdsTableClipboardSupport() {
            super(DdsTableDef.this);
        }

        @Override
        protected XmlObject copyToXml() {
            org.radixware.schemas.ddsdef.Table xTable = org.radixware.schemas.ddsdef.Table.Factory.newInstance();
            DdsModelWriter.writeTable(DdsTableDef.this, xTable);
            return xTable;
        }

        @Override
        protected DdsTableDef loadFrom(XmlObject xmlObject) {
            org.radixware.schemas.ddsdef.Table xTable = (org.radixware.schemas.ddsdef.Table) xmlObject;
            return DdsTableDef.Factory.loadFrom(xTable);
        }

        @Override
        public CanPasteResult canPaste(List<Transfer> objectsInClipboard, DuplicationResolver resolver) {
            if ((getColumns().getLocal().getClipboardSupport().canPaste(objectsInClipboard, resolver)) == CanPasteResult.YES
                    || (getIndices().getLocal().getClipboardSupport().canPaste(objectsInClipboard, resolver)) == CanPasteResult.YES
                    || (getTriggers().getLocal().getClipboardSupport().canPaste(objectsInClipboard, resolver)) == CanPasteResult.YES) {
                return CanPasteResult.YES;
            } else {
                return super.canPaste(objectsInClipboard, resolver);
            }
        }

        @Override
        public void paste(List<Transfer> objectsInClipboard, DuplicationResolver resolver) {
            if (getColumns().getLocal().getClipboardSupport().canPaste(objectsInClipboard, resolver) == CanPasteResult.YES) {
                getColumns().getLocal().getClipboardSupport().paste(objectsInClipboard, resolver);
            } else if (getIndices().getLocal().getClipboardSupport().canPaste(objectsInClipboard, resolver) == CanPasteResult.YES) {
                getIndices().getLocal().getClipboardSupport().paste(objectsInClipboard, resolver);
            } else if (getTriggers().getLocal().getClipboardSupport().canPaste(objectsInClipboard, resolver) == CanPasteResult.YES) {
                getTriggers().getLocal().getClipboardSupport().paste(objectsInClipboard, resolver);
            } else {
                super.paste(objectsInClipboard, resolver);
            }
        }

        @Override
        protected Method getDecoderMethod() {
            try {
                return DdsTableDef.Factory.class.getDeclaredMethod("loadFrom", org.radixware.schemas.ddsdef.Table.class);
            } catch (NoSuchMethodException | SecurityException ex) {
                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
            }
            return null;
        }
    }

    @Override
    public ClipboardSupport<? extends DdsTableDef> getClipboardSupport() {
        return new DdsTableClipboardSupport();
    }

    @Override
    protected void appendAdditionalToolTip(StringBuilder sb) {
        super.appendAdditionalToolTip(sb);

        List<String> attributes = new ArrayList<String>();
        if (isOverwrite()) {
            attributes.add("Overwrite");
        }
        if (!isGeneratedInDb()) {
            attributes.add("Virtual");
        }
        if (isHidden()) {
            attributes.add("Hidden");
        }

        if (!attributes.isEmpty()) {
            sb.append("<br>Attributes: ");
            boolean added = false;
            for (String attribute : attributes) {
                if (added) {
                    sb.append(", ");
                } else {
                    added = true;
                }
                sb.append(attribute);
            }
        }

        sb.append("<br>Database name: " + getDbName());
    }
    public static final String TABLE_TYPE_TITLE = "Table";
    public static final String TABLE_TYPES_TITLE = "Tables";

    @Override
    public String getTypeTitle() {
        return TABLE_TYPE_TITLE;
    }

    @Override
    public String getTypesTitle() {
        return TABLE_TYPES_TITLE;
    }

    private static final void appendColDbName(final StringBuilder str, final DdsColumnDef col, final String tabAlias) {
        if (tabAlias != null) {
            str.append(tabAlias);
            str.append('.');
        }
        str.append(col.getDbName());
    }

    /**
     * Generate table PID script.
     *
     * @return sql
     * @throws DefinitionNotFoundError if one of primary key column not found.
     */
    public String getPidScript(final String tabAlias) {
        final StringBuilder res = new StringBuilder();
        boolean isFirstPkProp = true;
        for (DdsIndexDef.ColumnInfo pkProp : getPrimaryKey().getColumnsInfo()) {
            if (isFirstPkProp) {
                isFirstPkProp = false;
            } else {
                res.append("||'~'||");
            }

            final DdsColumnDef col = pkProp.getColumn();

            switch (col.getValType()) {
                case STR:
                case CHAR:
                    res.append("RDX_ENTITY.PackPIDStr(");
                    appendColDbName(res, col, tabAlias);
                    res.append(')');
                    break;
                case DATE_TIME:
                    res.append("to_char(");
                    appendColDbName(res, col, tabAlias);
                    if (col.getPrecision() != 0) {
                        res.append(",'yyyy-mm-dd hh24:mi:ss.FF").append(col.getPrecision()).append("')");
                    } else {
                        res.append(",'yyyy-mm-dd hh24:mi:ss')");
                    }
                    break;
                case NUM:
                    res.append("Replace(");
                    appendColDbName(res, col, tabAlias);
                    res.append(", ',', '.')");
                    break;
                case BLOB:
                case CLOB:
                case BIN:
                    res.append("RawToHex(");
                    appendColDbName(res, col, tabAlias);
                    res.append(')');
                    break;
                default: // int, bool
                    appendColDbName(res, col, tabAlias);
                    break;
            }
        }
        return res.toString();
    }

    @Override
    public boolean delete() {
        final boolean result = super.delete();
        if (getContainer() != null) {
            setContainer(null); // for DdsTableEditor
        }
        return result;
    }

    public boolean isMasterTable() {
        for (DdsReferenceDef ref : this.collectIncomingReferences()) {
            if (ref.getType() == DdsReferenceDef.EType.MASTER_DETAIL) {
                return true;
            }
        }
        return false;
    }

    public boolean isDetailTable() {
        return findMasterReference() != null;
    }

    public DdsReferenceDef findMasterReference() {
        for (DdsReferenceDef ref : this.collectOutgoingReferences()) {
            if (ref.getType() == DdsReferenceDef.EType.MASTER_DETAIL) {
                return ref;
            }
        }
        return null;
    }
    private String tablespace = null;

    public String getTablespace() {
        final DdsTableDef overwritten = findOverwritten();
        if (overwritten != null) {
            return overwritten.getTablespace();
        } else {
            return tablespace;
        }
    }

    public void setTablespace(final String tablespace) {
        if (!Utils.equals(this.tablespace, tablespace)) {
            this.tablespace = tablespace;
            setEditState(EEditState.MODIFIED);
        }
    }
    private final Sqml partition = new DdsSqml(this);

    public Sqml getPartition() {
        final DdsTableDef overwritten = findOverwritten();
        if (overwritten != null) {
            return overwritten.getPartition();
        } else {
            return partition;
        }
    }
    private boolean globalTemporary = false;

    public boolean isGlobalTemporary() {
        final DdsTableDef overwritten = findOverwritten();
        if (overwritten != null) {
            return overwritten.isGlobalTemporary();
        } else {
            return globalTemporary;
        }
    }

    public void setGlobalTemporary(boolean globalTemporary) {
        if (this.globalTemporary != globalTemporary) {
            this.globalTemporary = globalTemporary;
            setEditState(EEditState.MODIFIED);
        }
    }
    private boolean onCommitPreserveRows = false;

    public boolean isOnCommitPreserveRows() {
        final DdsTableDef overwritten = findOverwritten();
        if (overwritten != null) {
            return overwritten.isOnCommitPreserveRows();
        } else {
            return onCommitPreserveRows;
        }
    }

    public void setOnCommitPreserveRows(boolean onCommitPreserveRows) {
        if (this.onCommitPreserveRows != onCommitPreserveRows) {
            this.onCommitPreserveRows = onCommitPreserveRows;
            setEditState(EEditState.MODIFIED);
        }
    }

    @Override
    public RadixdocSupport<? extends RadixObject> getRadixdocSupport() {
        return new RadixdocSupport(this) {

            @Override
            public IRadixdocPage document(Page page, DocumentOptions options) {
                return new DdsTableRadixdocSupport((DdsTableDef) getSource(), page, options);
            }

        };
    }

    @Override
    public boolean isRadixdocProvider() {
        return true;
    }

    @Override
    public EDocGroup getDocGroup() {
        return EDocGroup.TABLE;
    }
}
