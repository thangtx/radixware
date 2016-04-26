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

import java.lang.reflect.Method;
import org.radixware.kernel.common.enums.EDeleteMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.defs.*;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.defs.dds.utils.DbNameUtils;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.ENamingPolicy;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.radixdoc.DocumentOptions;
import org.radixware.kernel.common.radixdoc.IRadixdocPage;
import org.radixware.kernel.common.radixdoc.IRadixdocProvider;
import org.radixware.kernel.common.radixdoc.RadixdocSupport;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;

import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.schemas.radixdoc.Page;

/**
 * Метаинформация о связях между {@link DdsTableDef таблицами}. Используются для
 * генерации внешних ключей {@link DdsTableDef таблиц}. Связь визульно
 * отображается на диаграмме {@link DdsModelDef модели} в виде полилинии со
 * стрелкой со стороны родительской таблицы. Визуально связь выходит из дочерней
 * {@link DdsTableDef таблицы} и входит в родительскую
 * {@link DdsTableDef таблицу}. Для визуализации допускается использовать
 * {@link DdsExtTableDef ссылки на таблицы} вместо {@link DdsTableDef таблиц}.
 *
 */
public class DdsReferenceDef extends DdsConstraintDef implements IRadixdocProvider {


    /**
     * Logical type of {@link DdsReferenceDef reference}.
     */
    public enum EType {

        /**
         * Связь для уточнения типа объекта. 1/N->1 Например Diebold<-ATM->NDC.
         */
        MASTER_DETAIL,
        /**
         * Link N->1. Например, Transaction->Currency.
         */
        LINK
    }

    /**
     * Способ поиска дочерних записей при удалении объекта.<br> См.
     * {@link DdsReferenceDef#getRestrictCheckMode()}.
     */
    public enum ERestrictCheckMode {

        /**
         * Всегда проверять существование подобъектов.
         */
        ALWAYS,
        /**
         * Не проверять существование подобъектов.
         */
        NEVER,
        /**
         * Проверять существование подобъектов только если удаляется один
         * объект.
         */
        ONLY_FOR_SINGLE_OBJECT
    }
    private Id childTableId = null;

    /**
     * Получить идентификатор дочерней {@link DdsTableDef таблицы} связи.
     */
    public Id getChildTableId() {
        return childTableId;
    }
//
//    /**
//     * Find child {@link DdsTableDef table}.
//     * @return child table or null it not found.
//     */
//    public DdsTableDef findChildTable() {
//        final DdsModule module = getModule();
//        if (module == null) {
//            return null;
//        }
//        return module.getDdsTableSearcher().findById(getChildTableId());
//    }

    /**
     * Find child tables with respect to overwrite{@link DdsTableDef table}.
     *
     * @return child table or null it not found.
     */
    public DdsTableDef findChildTable(RadixObject context) {
        return findTableById(context, getChildTableId());
    }

    public DdsTableDef getChildTable(RadixObject context) {
        DdsTableDef childTable = findChildTable(context);
        if (childTable != null) {
            return childTable;
        }
        throw new DefinitionNotFoundError(getChildTableId());
    }

    /**
     * Find parent tables with respect to overwrite {@link DdsTableDef table}.
     *
     * @return child table or null it not found.
     */
    public DdsTableDef findParentTable(RadixObject context) {
        return findTableById(context, getParentTableId());
    }

    public DdsTableDef getParentTable(RadixObject context) {
        DdsTableDef childTable = findParentTable(context);
        if (childTable != null) {
            return childTable;
        }
        throw new DefinitionNotFoundError(getParentTableId());
    }

    private DdsTableDef findTableById(final RadixObject context, final Id tableId) {

        final DdsModule module = getModule();
        if (module == null) {
            return null;
        }
        final DdsTableDef table = module.getDdsTableSearcher().findById(tableId).get();
        if (table == null) {
            return null;
        }
        final DdsModule tableModule = table.getModule();
        if (tableModule == null) {
            return null;
        }
        final Id tableModuleId = tableModule.getId();
        final Layer thisLayer = getLayer();
        Layer contextLayer;

        if (context instanceof Branch) {
            throw new IllegalUsageError("Branch object can not be used like context of child/parent table lookup");
        } else if (context instanceof Layer) {
            contextLayer = (Layer) context;
        } else {
            contextLayer = context.getLayer();
        }

        final DdsTableDef tableInContextLayer = contextLayer == null ? null : Layer.HierarchyWalker.walk(contextLayer, new Layer.HierarchyWalker.Acceptor<DdsTableDef>() {
            @Override
            public void accept(HierarchyWalker.Controller<DdsTableDef> controller, Layer contextLayer) {
                if (contextLayer == thisLayer) {
                    controller.setResultAndStop(null);
                    return;
                }
                final DdsModule tableModuleInContextLayer = (DdsModule) contextLayer.getDds().getModules().findById(tableModuleId);
                if (tableModuleInContextLayer != null) {
                    final DdsTableDef tableInContextLayer = tableModuleInContextLayer.getDdsTableSearcher().findInsideById(tableId);
                    if (tableInContextLayer != null) {
                        controller.setResultAndStop(tableInContextLayer);
                    }
                }
            }
        });
        if (tableInContextLayer != null) {
            return tableInContextLayer;
        }

        return table;
    }

    /**
     * Find child {@link DdsTableDef table}.
     *
     * @throws DefinitionNotFoundError
     */
//    public DdsTableDef getChildTable() {
//        DdsTableDef childTable = findChildTable();
//        if (childTable != null) {
//            return childTable;
//        }
//        throw new DefinitionNotFoundError(getChildTableId());
//    }
    public void setChildTableId(Id childTableId) {
        if (!Utils.equals(this.childTableId, childTableId)) {
            this.childTableId = childTableId;
            setEditState(EEditState.MODIFIED);
        }
    }
    private Id parentTableId = null;

    /**
     * Получить идентификатор родительской {@link DdsTableDef таблицы} связи.
     */
    public Id getParentTableId() {
        return parentTableId;
    }

    public void setParentTableId(Id parentTableId) {
        if (!Utils.equals(this.parentTableId, parentTableId)) {
            this.parentTableId = parentTableId;
            setEditState(EEditState.MODIFIED);
        }
    }
//    /**
//     * Find parent {@link DdsTableDef table}.
//     * @return parent table or null it not found.
//     */
//    public DdsTableDef findParentTable() {
//        final DdsModule module = getModule();
//        if (module == null) {
//            return null;
//        }
//        return module.getDdsTableSearcher().findById(getParentTableId());
//    }
    /**
     * Find parent {@link DdsTableDef table}.
     *
     * @throws DefinitionNotFoundError
     */
//    public DdsTableDef getParentTable() {
//        DdsTableDef parentTable = findParentTable();
//        if (parentTable != null) {
//            return parentTable;
//        }
//        throw new DefinitionNotFoundError(getParentTableId());
//    }
    private Id extChildTableId = null;

    /**
     * Получить идентификатор {@link DdsExtTableDef ссылки на дочернюю таблицу},
     * с помощью которой связь визуально привязана к {@link DdsTableDef таблице}
     * на диаграмме.
     */
    public Id getExtChildTableId() {
        return extChildTableId;
    }

    /**
     * Find child {@link DdsExtTableDef external table}.
     *
     * @return external child table or null if not found.
     */
    public DdsExtTableDef findExtChildTable() {
        return this.getOwnerModel().getExtTables().findById(getExtChildTableId());
    }

    public void setExtChildTableId(Id extChildTableId) {
        if (!Utils.equals(this.extChildTableId, extChildTableId)) {
            this.extChildTableId = extChildTableId;
            setEditState(EEditState.MODIFIED);
        }
    }
    private Id extParentTableId;

    /**
     * Получить идентификатор
     * {@link DdsExtTableDef ссылки на родительскую таблицу}, с помощью которой
     * связь визуально привязана к {@link DdsTableDef таблице} на диаграмме.
     */
    public Id getExtParentTableId() {
        return extParentTableId;
    }

    /**
     * Find parent {@link DdsExtTableDef external table}.
     *
     * @return external parent table or null if not found.
     */
    public DdsExtTableDef findExtParentTable() {
        return this.getOwnerModel().getExtTables().findById(getExtParentTableId());
    }

    public void setExtParentTableId(Id extParentTableId) {
        if (!Utils.equals(this.extParentTableId, extParentTableId)) {
            this.extParentTableId = extParentTableId;
            setEditState(EEditState.MODIFIED);
        }
    }
    private EType type = EType.LINK;

    /**
     * Получить логический тип связи.
     */
    public EType getType() {
        return type;
    }

    public void setType(EType type) {
        if (!Utils.equals(this.type, type)) {
            this.type = type;
            setEditState(EEditState.MODIFIED);
        }
    }
    boolean generatedInDb = true;

    /**
     * Генерируется-ли связь в базе данных. Обычно не генерируются связи
     * созданные только для вызова модального селектора (в проводнике).
     */
    @Override
    public boolean isGeneratedInDb() {
        return generatedInDb;
    }

    public void setGeneratedInDb(boolean generatedInDb) {
        if (this.generatedInDb != generatedInDb) {
            this.generatedInDb = generatedInDb;
            setEditState(EEditState.MODIFIED);
        }
    }
    private EDeleteMode deleteMode = EDeleteMode.NONE;

    /**
     * Получить действие при удалении объекта.
     */
    public EDeleteMode getDeleteMode() {
        return deleteMode;
    }

    public final void setDeleteMode(EDeleteMode deleteMode) {
        if (!Utils.equals(this.deleteMode, deleteMode)) {
            this.deleteMode = deleteMode;
            setEditState(EEditState.MODIFIED);
        }
    }

    /**
     * Элемент ассоциативного списка {@link DdsColumnDef колонок} дочерней и
     * родительской {@link DdsTableDef таблицы}, по которым строится вторичный
     * ключ в базе данных. Представляет собой одну {@link DdsColumnDef колонку}
     * дочерней и одну {@link DdsColumnDef колонку} родительской
     * {@link DdsTableDef таблицы}. Типы {@link DdsColumnDef колонок} должны
     * совпадать.
     */
    public static class ColumnsInfoItem extends RadixObject {

        private Id childColumnId = null;

        protected DdsReferenceDef getOwnerReference() {
            for (RadixObject owner = getContainer(); owner != null; owner = owner.getContainer()) {
                if (owner instanceof DdsReferenceDef) {
                    return (DdsReferenceDef) owner;
                }
            }
            return null;
        }

        /**
         * Идентификатор {@link DdsColumnDef колонки} дочерней
         * {@link DdsTableDef таблицы}.
         */
        public Id getChildColumnId() {
            return childColumnId;
        }

        public void setChildColumnId(Id childColumnId) {
            if (!Utils.equals(this.childColumnId, childColumnId)) {
                this.childColumnId = childColumnId;
                setEditState(EEditState.MODIFIED);
            }
        }

        /**
         * Find child column.
         *
         * @return child column or null if not found.
         */
        public DdsColumnDef findChildColumn() {
            DdsReferenceDef ownerReference = getOwnerReference();
            if (ownerReference != null) {
                DdsTableDef childTable = ownerReference.findChildTable(ownerReference);
                if (childTable != null) {
                    return childTable.getColumns().findById(getChildColumnId(), EScope.LOCAL_AND_OVERWRITE).get();
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }

        /**
         * Find child column.
         *
         * @throws DefinitionNotFoundError
         */
        public DdsColumnDef getChildColumn() {
            DdsColumnDef childColumn = findChildColumn();
            if (childColumn != null) {
                return childColumn;
            }
            throw new DefinitionNotFoundError(getChildColumnId());
        }
        private Id parentColumnId = null;

        /**
         * Идентификатор {@link DdsColumnDef колонки} родительской
         * {@link DdsTableDef таблицы}.
         */
        public Id getParentColumnId() {
            return parentColumnId;
        }

        public void setParentColumnId(Id parentColumnId) {
            if (!Utils.equals(this.parentColumnId, parentColumnId)) {
                this.parentColumnId = parentColumnId;
                setEditState(EEditState.MODIFIED);
            }
        }

        /**
         * Find parent column.
         *
         * @return parent column or null if not found.
         */
        public DdsColumnDef findParentColumn() {
            DdsReferenceDef ownerReference = getOwnerReference();
            if (ownerReference != null) {
                DdsTableDef parentTable = getOwnerReference().findParentTable(ownerReference);
                if (parentTable != null) {
                    return parentTable.getColumns().findById(getParentColumnId(), EScope.LOCAL_AND_OVERWRITE).get();
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }

        /**
         * Find parent column.
         *
         * @throws DefinitionNotFoundError
         */
        public DdsColumnDef getParentColumn() {
            DdsColumnDef parentColumn = findParentColumn();
            if (parentColumn != null) {
                return parentColumn;
            }
            throw new DefinitionNotFoundError(getParentColumnId());
        }

        protected ColumnsInfoItem() {
            super();
        }

        public static final class Factory {

            private Factory() {
            }

            public static ColumnsInfoItem newInstance() {
                return new ColumnsInfoItem();
            }
        }
    }

    public class ColumnsInfoItems extends RadixObjects<ColumnsInfoItem> {

        public ColumnsInfoItems(RadixObject owner) {
            super(owner);
        }

        public ColumnsInfoItem add(DdsColumnDef childColumn, DdsColumnDef parentColumn) {
            ColumnsInfoItem item = ColumnsInfoItem.Factory.newInstance();
            item.setChildColumnId(childColumn != null ? childColumn.getId() : null);
            item.setParentColumnId(parentColumn != null ? parentColumn.getId() : null);
            add(item);
            return item;
        }
    }
    private final ColumnsInfoItems columnsInfo = new ColumnsInfoItems(this);

    /**
     * Получить ассоциативный список колонок дочерней и родительской таблицы, по
     * которым строится вторичный ключ в базе данных. Колонки родительской
     * таблицы дожны образовывать первичный или вторичный ключ.
     */
    public final ColumnsInfoItems getColumnsInfo() {
        return columnsInfo;
    }
    private ERestrictCheckMode restrictCheckMode = ERestrictCheckMode.ALWAYS;

    /**
     * Способ проверки существования дочерних записей при удалении объекта.
     * Используется приложением RadixWare Explorer. Перед удалением записи
     * проводник делает поиск дочерних записей, эта операция может длиться очень
     * долго, поэтому предусмотрена возможность полного или частичного
     * отключения этой проверки.
     */
    public ERestrictCheckMode getRestrictCheckMode() {
        return restrictCheckMode;
    }

    public final void setRestrictCheckMode(ERestrictCheckMode restrictCheckMode) {
        if (!Utils.equals(this.restrictCheckMode, restrictCheckMode)) {
            this.restrictCheckMode = restrictCheckMode;
            setEditState(EEditState.MODIFIED);
        }
    }
    private boolean confirmDelete = true;

    /**
     * Используется приложением RadixWare Explorer: должно-ли требоваться
     * подтверждение при удалении объекта, по которому существуют дочерние
     * объекты.
     */
    public boolean isConfirmDelete() {
        if (this.getType() == EType.MASTER_DETAIL) {
            return false;
        }
        return confirmDelete;
    }

    public void setConfirmDelete(boolean confirmDelete) {
        if (this.confirmDelete != confirmDelete) {
            this.confirmDelete = confirmDelete;
            setEditState(EEditState.MODIFIED);
        }
    }
    private Id parentUniqueConstraintId = null;

    /**
     * Получить идентификатор уникального ключа индекса родительской таблицы, по
     * которому строится связь. См.
     * {@linkplain DdsIndexDef#getKeyConstraintId()}.
     *
     * @return идентификатор уникального ключа, возможен null, если связь
     * виртуальная (не создается в базе) и строится по произвольным колонкам.
     */
    public Id getParentUnuqueConstraintId() {
        return parentUniqueConstraintId;
    }

    public void setParentUnuqueConstraintId(Id parentUnuqueConstraintId) {
        if (!Utils.equals(this.parentUniqueConstraintId, parentUnuqueConstraintId)) {
            this.parentUniqueConstraintId = parentUnuqueConstraintId;
            setEditState(EEditState.MODIFIED);
        }
    }

    /**
     * Найти индекс родительской таблицы, по которому строится связь.
     *
     * @throws DefinitionNotFoundError if not found.
     */
    public DdsIndexDef getParentIndex() {
        DdsIndexDef index = findParentIndex();
        if (index == null) {
            throw new DefinitionNotFoundError(parentUniqueConstraintId);
        }
        return index;
    }

    /**
     * Найти индекс родительской таблицы, по которому строится связь.
     *
     * @return индекс или null, если не найден.
     */
    public DdsIndexDef findParentIndex() {
        DdsTableDef parentTable = findParentTable(this);
        if (parentTable == null) {
            return null;
        }
        DdsIndexDef pk = parentTable.getPrimaryKey();
        Id pkConstraintId = pk.getUniqueConstraint().getId();
        if (pkConstraintId.equals(this.getParentUnuqueConstraintId())) {
            return pk;
        }
        for (DdsIndexDef index : parentTable.getIndices().get(EScope.LOCAL_AND_OVERWRITE)) {
            DdsUniqueConstraintDef uc = index.getUniqueConstraint();
            if (uc != null) {
                final Id ucId = uc.getId();
                if (ucId.equals(this.parentUniqueConstraintId)) {
                    return index;
                }
            }
        }
        return null;
    }

    protected DdsReferenceDef() {
        super(EDefinitionIdPrefix.DDS_REFERENCE);
    }

    public DdsReferenceDef(org.radixware.schemas.ddsdef.Reference xReference) {
        super(xReference);

        org.radixware.schemas.ddsdef.Reference.Type.Enum xType = xReference.getType();
        this.type = DdsReferenceDef.EType.valueOf(xType.toString());
        if (xReference.isSetGenerateInDb()) {
            this.generatedInDb = xReference.getGenerateInDb();
        }
        this.childTableId = Id.Factory.loadFrom(xReference.getChildTableId());
        this.parentTableId = Id.Factory.loadFrom(xReference.getParentTableId());

        if (xReference.isSetExtChildTableId()) {
            this.extChildTableId = Id.Factory.loadFrom(xReference.getExtChildTableId());
        }

        if (xReference.isSetExtParentTableId()) {
            this.extParentTableId = Id.Factory.loadFrom(xReference.getExtParentTableId());
        }

        this.confirmDelete = xReference.getConfirmDelete();

        org.radixware.schemas.ddsdef.Reference.DeleteMode.Enum xDeleteMode = xReference.getDeleteMode();
        this.setDeleteMode(EDeleteMode.valueOf(xDeleteMode.toString()));

        org.radixware.schemas.ddsdef.Reference.RestrictCheckMode.Enum xmlRestrictCheckMode = xReference.getRestrictCheckMode();
        this.setRestrictCheckMode(DdsReferenceDef.ERestrictCheckMode.valueOf(xmlRestrictCheckMode.toString()));

        if (xReference.isSetParentUniqueConstraintId()) {
            this.parentUniqueConstraintId = Id.Factory.loadFrom(xReference.getParentUniqueConstraintId());
        }
        if (xReference.isSetColumns()) {
            final List<org.radixware.schemas.ddsdef.Reference.Columns.Item> xmlColumnInfo = xReference.getColumns().getItemList();
            for (org.radixware.schemas.ddsdef.Reference.Columns.Item xmlColumnInfoItem : xmlColumnInfo) {
                final String childColumnId = xmlColumnInfoItem.getChildColumnId();
                final String parentColumnId = xmlColumnInfoItem.getParentColumnId();
                DdsReferenceDef.ColumnsInfoItem item = DdsReferenceDef.ColumnsInfoItem.Factory.newInstance();
                item.setChildColumnId(Id.Factory.loadFrom(childColumnId));
                item.setParentColumnId(Id.Factory.loadFrom(parentColumnId));
                this.getColumnsInfo().add(item);
            }
        }
    }

    public static final class Factory {

        private Factory() {
        }

        public static DdsReferenceDef newInstance() {
            return new DdsReferenceDef();
        }

        /**
         * Do not use this function instead of reference editor. This functions
         * allows to simplify the editor.
         */
        public static DdsReferenceDef newInstance(DdsReferenceDef src, DdsModelDef ownerModel) {
            final DdsReferenceDef ref = src.getClipboardSupport().copy();
            ref.setContainer(ownerModel.getReferences());
            return ref;
        }

        public static DdsReferenceDef loadFrom(org.radixware.schemas.ddsdef.Reference xReference) {
            return new DdsReferenceDef(xReference);
        }
    }

    @Override
    public RadixIcon getIcon() {
        return DdsDefinitionIcon.REFERENCE;
    }

    @Override
    public String calcAutoDbName() {
        final DdsTableDef childTable = findChildTable(this);
        final DdsTableDef parentTable = findParentTable(this);
        final String childTableName = (childTable != null ? childTable.getName() : (childTableId != null ? childTableId.toString() : "null"));
        final String parentTableName = (parentTable != null ? parentTable.getName() : (parentTableId != null ? parentTableId.toString() : "null"));
        String dbNamePrefix = getOwnerModel().getDbAttributes().getDbNamePrefix();
        final String result = DbNameUtils.calcAutoDbName("FK", dbNamePrefix, childTableName, parentTableName);
        return result;
    }

    @Override
    public String toString() {
        return super.toString()
                + "; Type: " + String.valueOf(type)
                + "; ChildTableId: " + String.valueOf(childTableId)
                + "; ParentTableId: " + String.valueOf(parentTableId)
                + "; GenerateInDb: " + String.valueOf(generatedInDb)
                + "; RestrictCheckMode: " + String.valueOf(restrictCheckMode)
                + "; UniqueConstraintId: " + String.valueOf(parentUniqueConstraintId);
    }

    @Override
    public void collectDependences(List<Definition> list) {
        super.collectDependences(list);

        DdsTableDef parent = findParentTable(this);
        if (parent != null) {
            list.add(parent);
        }
        DdsTableDef child = findChildTable(this);
        if (child != null) {
            list.add(child);
        }
        DdsExtTableDef extParent = findExtParentTable();
        if (extParent != null) {
            list.add(extParent);
        }
        DdsExtTableDef extChild = findExtChildTable();
        if (extChild != null) {
            list.add(extChild);
        }
        DdsIndexDef parentIndex = findParentIndex();
        if (parentIndex != null) {
            list.add(parentIndex);
        }
        for (ColumnsInfoItem item : this.getColumnsInfo()) {
            DdsColumnDef childColumn = item.findChildColumn();
            if (childColumn != null) {
                list.add(childColumn);
            }
            DdsColumnDef parentColumn = item.findParentColumn();
            if (parentColumn != null) {
                list.add(parentColumn);
            }
        }
    }

    private class DdsReferenceClipboardSupport extends DdsClipboardSupport<DdsReferenceDef> {

        public DdsReferenceClipboardSupport() {
            super(DdsReferenceDef.this);
        }

        @Override
        protected XmlObject copyToXml() {
            org.radixware.schemas.ddsdef.Reference xReference = org.radixware.schemas.ddsdef.Reference.Factory.newInstance();
            DdsModelWriter.writeReference(DdsReferenceDef.this, xReference);
            return xReference;
        }

        @Override
        protected DdsReferenceDef loadFrom(XmlObject xmlObject) {
            org.radixware.schemas.ddsdef.Reference xReference = (org.radixware.schemas.ddsdef.Reference) xmlObject;
            return DdsReferenceDef.Factory.loadFrom(xReference);
        }

        @Override
        protected Method getDecoderMethod() {
            try {
                return DdsReferenceDef.Factory.class.getDeclaredMethod("loadFrom", org.radixware.schemas.ddsdef.Reference.class);
            } catch (NoSuchMethodException | SecurityException ex) {
                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
            }
            return null;
        }
    }

    @Override
    public ClipboardSupport<? extends DdsReferenceDef> getClipboardSupport() {
        return new DdsReferenceClipboardSupport();
    }

    @Override
    protected void appendAdditionalToolTip(StringBuilder sb) {
        super.appendAdditionalToolTip(sb);

        List<String> attributes = new ArrayList<String>();

        if (this.getType() == EType.MASTER_DETAIL) {
            attributes.add("Master-Detail");
        }
        if (!isGeneratedInDb()) {
            attributes.add("Virtual");
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

        DdsTableDef childTable = findChildTable(this);
        if (childTable != null) {
            sb.append("<br>Child table: ").append(childTable.getQualifiedName());
        }

        DdsTableDef parentTable = findParentTable(this);
        if (parentTable != null) {
            sb.append("<br>Parent table: ").append(parentTable.getQualifiedName());
        }
    }
    public static final String REFERENCE_TYPE_TITLE = "Reference";
    public static final String REFERENCE_TYPES_TITLE = "References";

    @Override
    public String getTypeTitle() {
        return REFERENCE_TYPE_TITLE;
    }

    @Override
    public String getTypesTitle() {
        return REFERENCE_TYPES_TITLE;
    }

    @Override
    public ENamingPolicy getNamingPolicy() {
        return ENamingPolicy.CALC;
    }

    @Override
    public String getName() {
        final DdsDefinition parent = findParentTable(this);
        final DdsDefinition child = findChildTable(this);

        final StringBuilder sb = new StringBuilder();
        sb.append(child != null ? child.getName() : String.valueOf(childTableId));
        sb.append("=>");
        sb.append(parent != null ? parent.getName() : String.valueOf(parentTableId));

        sb.append(" (");
        boolean first = true;

        for (ColumnsInfoItem item : columnsInfo) {
            final DdsColumnDef childColumn = item.findChildColumn();
            final DdsColumnDef parentColumn = item.findParentColumn();

            if (first) {
                first = false;
            } else {
                sb.append(", ");
            }

            sb.append(childColumn != null ? childColumn.getName() : String.valueOf(item.getChildColumnId()));
            sb.append("=>");
            sb.append(parentColumn != null ? parentColumn.getName() : String.valueOf(item.getParentColumnId()));
        }

        sb.append(")");

        final String result = sb.toString();
        return result;
    }
    
      @Override
    public RadixdocSupport<? extends RadixObject> getRadixdocSupport() {
        return new RadixdocSupport(this) {

            @Override
            public IRadixdocPage document(Page page, DocumentOptions options) {
                return new DdsReferenceRadixdocSupport((DdsReferenceDef) getSource(), page, options);
            }
          
        };
    }

    @Override
    public boolean isRadixdocProvider() {
        return true;
    }
}
