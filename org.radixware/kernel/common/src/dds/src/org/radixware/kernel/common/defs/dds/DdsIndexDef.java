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
import java.util.EnumSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.defs.ClipboardSupport;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.dds.utils.DbNameUtils;
import org.radixware.kernel.common.defs.dds.utils.TablespaceCalculator;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EOrder;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;

/**
 * Metainformation about database table index.
 *
 */
public class DdsIndexDef extends DdsDefinition implements IDdsTableItemDef {

    /**
     * Опция индекса в базе данных. Используются при генерации скрипта создания
     * и модификации индекса. Смотрите спецификацию Oracle для получения
     * подробной информации о каждой опции. Если какая-либо опция не
     * поддерживается версией базы данных проекта, то она игнорируется при
     * генерации скрипта создания и модификации индекса.
     */
    public static enum EDbOption {

        UNIQUE,
        INVISIBLE,
        BITMAP,
        LOCAL,
        NOLOGGING,
        REVERSE
    }

    /**
     * Информация о колонке {@link DdsIndexDef индекса}.
     */
    public static class ColumnInfo extends DdsColumnInfo {

        protected ColumnInfo() {
            super();
        }

        protected ColumnInfo(org.radixware.schemas.ddsdef.Index.Columns.Column xColumnInfo) {
            super(Id.Factory.loadFrom(xColumnInfo.getColumnId()));
            if (xColumnInfo.isSetOrder()) {
                this.order = xColumnInfo.getOrder();
            }
        }
        private EOrder order = EOrder.ASC;

        /**
         * Get order type.
         *
         * @return order type.
         */
        public EOrder getOrder() {
            return order;
        }

        public void setOrder(EOrder order) {
            if (this.order != order) {
                this.order = order;
                setEditState(EEditState.MODIFIED);
            }
        }

        @Override
        protected DdsTableDef findTable() {
            final DdsIndexDef ownerIndex = getOwnerIndex();
            if (ownerIndex != null) {
                return ownerIndex.getOwnerTable();
            } else {
                return null;
            }
        }

        public DdsIndexDef getOwnerIndex() {
            for (RadixObject owner = getContainer(); owner != null; owner = owner.getContainer()) {
                if (owner instanceof DdsIndexDef) {
                    return (DdsIndexDef) owner;
                }
            }
            return null;
        }

        public static final class Factory {

            private Factory() {
            }

            public static ColumnInfo newInstance() {
                return new ColumnInfo();
            }

            public static ColumnInfo loadFrom(org.radixware.schemas.ddsdef.Index.Columns.Column xColumnInfo) {
                return new ColumnInfo(xColumnInfo);
            }
        }
    }

    public class ColumnsInfo extends RadixObjects<ColumnInfo> {

        public ColumnsInfo(DdsIndexDef owner) {
            super(owner);
        }

        public ColumnInfo add(DdsColumnDef column, EOrder order) {
            Id columnId = column.getId();
            return add(columnId, order);
        }

        public ColumnInfo add(Id columnId, EOrder order) {
            ColumnInfo columnInfo = ColumnInfo.Factory.newInstance();
            columnInfo.setColumnId(columnId);
            columnInfo.setOrder(order);
            add(columnInfo);
            return columnInfo;
        }
    }
    private final ColumnsInfo columnsInfo = new ColumnsInfo(this);

    /**
     * Получить список информаций о колонках индекса (идентификаторы и
     * направления сортировки).
     */
    public ColumnsInfo getColumnsInfo() {
        return columnsInfo;
    }
    private String dbName = "";

    /**
     * Получить имя индекса в базе данных. Используется при построении SQL
     * запросов, в т.ч. для генерации скрипта индекса.
     */
    @Override
    public String getDbName() {
        return dbName;
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
        if (this.autoDbName != autoDbName) {
            this.autoDbName = autoDbName;
            setEditState(EEditState.MODIFIED);
        }
    }
    private final EnumSet<EDbOption> dbOptions = EnumSet.noneOf(EDbOption.class);

    /**
     * Получить опции индекса в базе данных.
     */
    public EnumSet<EDbOption> getDbOptions() {
        return dbOptions;
    }
    private String tablespaceDbName = null;

    /**
     * Получить имя табличного пространства индекса в базе данных.
     *
     * @return имя табличного пространства индекса в базе данных или null, если
     * используется значение по умолчанию в проекте.
     */
    public String getTablespaceDbName() {
        return tablespaceDbName;
    }

    public void setTablespaceDbName(String tablespaceDbName) {
        if (!Utils.equals(this.tablespaceDbName, tablespaceDbName)) {
            this.tablespaceDbName = tablespaceDbName;
            setEditState(EEditState.MODIFIED);
        }
    }
    private DdsUniqueConstraintDef uniqueConstraint = null;

    /**
     * Получить метаинформацию по unique сonstraint индекса.
     *
     * @return unique constraint или null, если по индексу не создается unique
     * constraint
     */
    public DdsUniqueConstraintDef getUniqueConstraint() {
        return uniqueConstraint;
    }

    /**
     * Является-ли индекс вторичным ключем.
     */
    // перекрыт 
    public boolean isSecondaryKey() {
        return (getUniqueConstraint() != null);
    }

    public void setUniqueConstraint(DdsUniqueConstraintDef uniqueConstraint) {
        if (this.uniqueConstraint == uniqueConstraint) {
            return;
        }
        if (this.uniqueConstraint != null) {
            this.uniqueConstraint.setOwnerIndex(null);
        }
        this.uniqueConstraint = uniqueConstraint;
        if (uniqueConstraint != null) {
            uniqueConstraint.setOwnerIndex(this);
        }
        setEditState(EEditState.MODIFIED);
    }
    boolean generatedInDb = true;

    /**
     * @return true if index and its table are generated in database, false
     * otherwise. Учитывается при генерации скриптов создания и модификации базы
     * данных. Обычно индексы всегда генерируются в базе данных, за исключением
     * крайне редких случаев, когда индекс используется для создания виртуальных
     * {@link DdsReferenceDef связей} между {@link DdsTableDef таблицами}.
     */
    @Override
    public boolean isGeneratedInDb() {
        if (!generatedInDb) {
            return false;
        }

        final DdsTableDef ownerTable = getOwnerTable();
        return ownerTable.isGeneratedInDb();
    }

    public void setGeneratedInDb(boolean generatedInDb) {
        if (this.generatedInDb != generatedInDb) {
            this.generatedInDb = generatedInDb;
            setEditState(EEditState.MODIFIED);
        }
    }

    protected DdsIndexDef(final String name) {
        super(EDefinitionIdPrefix.DDS_INDEX, name);
    }

    protected DdsIndexDef(org.radixware.schemas.ddsdef.Index xIndex) {
        super(xIndex);

        this.dbName = xIndex.getDbName();

        if (xIndex.isSetAutoDbName()) {
            this.autoDbName = xIndex.getAutoDbName();
        }

        if (xIndex.isSetUniqueConstraint()) {
            this.uniqueConstraint = DdsUniqueConstraintDef.Factory.loadFrom(xIndex.getUniqueConstraint());
            this.uniqueConstraint.setOwnerIndex(this);
        }

        if (xIndex.isSetGenerateInDb()) {
            this.generatedInDb = xIndex.getGenerateInDb();
        }

        if (xIndex.isSetTablespace()) {
            this.tablespaceDbName = xIndex.getTablespace();
        }

        if (xIndex.isSetUnique()) {
            final boolean unique = xIndex.getUnique();
            if (unique) {
                this.dbOptions.add(DdsIndexDef.EDbOption.UNIQUE);
            }
        }

        if (xIndex.isSetInvisible()) {
            final boolean invisible = xIndex.getInvisible();
            if (invisible) {
                this.dbOptions.add(DdsIndexDef.EDbOption.INVISIBLE);
            }
        }

        if (xIndex.isSetBitmap()) {
            final boolean bitmap = xIndex.getBitmap();
            if (bitmap) {
                this.dbOptions.add(DdsIndexDef.EDbOption.BITMAP);
            }
        }

        if (xIndex.isSetLocal()) {
            final boolean local = xIndex.getLocal();
            if (local) {
                this.dbOptions.add(DdsIndexDef.EDbOption.LOCAL);
            }
        }

        if (xIndex.isSetNologging()) {
            final boolean nologging = xIndex.getNologging();
            if (nologging) {
                this.dbOptions.add(DdsIndexDef.EDbOption.NOLOGGING);
            }
        }

        if (xIndex.isSetReverse()) {
            final boolean reverse = xIndex.getReverse();
            if (reverse) {
                this.dbOptions.add(DdsIndexDef.EDbOption.REVERSE);
            }
        }

        if (xIndex.isSetColumns()) {
            List<org.radixware.schemas.ddsdef.Index.Columns.Column> xColumns = xIndex.getColumns().getColumnList();
            for (org.radixware.schemas.ddsdef.Index.Columns.Column xColumn : xColumns) {
                DdsIndexDef.ColumnInfo columnInfo = DdsIndexDef.ColumnInfo.Factory.loadFrom(xColumn);
                this.getColumnsInfo().add(columnInfo);
            }
        }
        if (xIndex.isSetDeprecated()) {
            this.isDeprecated = xIndex.getDeprecated();
        }
    }
    private boolean isDeprecated = false;

    @Override
    public boolean isDeprecated() {
        return isDeprecated || super.isDeprecated();
    }

    public void setDeprecated(boolean isDeprecated) {
        this.isDeprecated = isDeprecated;
        setEditState(EEditState.MODIFIED);
    }

    @Override
    public RadixIcon getIcon() {
        return DdsDefinitionIcon.INDEX;
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        if (uniqueConstraint != null) {
            uniqueConstraint.visit(visitor, provider);
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(super.toString());
        result.append("; DbName: ");
        result.append(dbName);
        result.append("; GeneratedInDb: ");
        result.append(generatedInDb);
        result.append("; DbOptions: ");
        result.append(dbOptions);
        result.append("; Tablespace: ");
        result.append(TablespaceCalculator.calcTablespaceForIndex(this));

        return result.toString();
    }

    public static final class Factory {

        private Factory() {
        }

        public static DdsIndexDef newInstance(final String name) {
            return new DdsIndexDef(name);
        }

        public static DdsIndexDef loadFrom(org.radixware.schemas.ddsdef.Index xIndex) {
            return new DdsIndexDef(xIndex);
        }
    }

    /**
     * @return true if index is unique or primary or secondary key.
     */
    public boolean isUnique() {
        return getUniqueConstraint() != null || getDbOptions().contains(EDbOption.UNIQUE);
    }

    @Override
    public DdsTableDef getOwnerTable() {
        return (DdsTableDef) getOwnerDefinition();
    }

    @Override
    public String calcAutoDbName() {
        final DdsTableDef table = getOwnerTable();
        final String tableName = table.getName();

        final DdsModelDef ownerModel = getOwnerModel();
        final String dbNamePrefix = ownerModel.getDbAttributes().getDbNamePrefix();

        final String name = this.getName();
        final String result = DbNameUtils.calcAutoDbName("IDX", dbNamePrefix, tableName, name);
        return result;

//        DdsTableDef table = getOwnerTable();
//        String tableDbName = table.getDbName();
//        String name = this.getName();
//        String result = DbNameUtils.calcAutoDbName("IDX", tableDbName, name);
//        return result;
    }

    @Override
    public void collectDependences(List<Definition> list) {
        super.collectDependences(list);

        for (ColumnInfo info : this.getColumnsInfo()) {
            DdsColumnDef column = info.findColumn();
            if (column != null) {
                list.add(column);
            }
        }
    }

    private class DdsIndexClipboardSupport extends DdsClipboardSupport<DdsIndexDef> {

        public DdsIndexClipboardSupport() {
            super(DdsIndexDef.this);
        }

//        @Override
//        public boolean canCopy() {
//            return !(DdsIndexDef.this instanceof DdsPrimaryKeyDef);
//        }
        @Override
        protected XmlObject copyToXml() {
            org.radixware.schemas.ddsdef.Index xIndex = org.radixware.schemas.ddsdef.Index.Factory.newInstance();
            DdsModelWriter.writeIndex(DdsIndexDef.this, xIndex);
            return xIndex;
        }

        @Override
        protected DdsIndexDef loadFrom(XmlObject xmlObject) {
            org.radixware.schemas.ddsdef.Index xIndex = (org.radixware.schemas.ddsdef.Index) xmlObject;
            return DdsIndexDef.Factory.loadFrom(xIndex);
        }

        @Override
        protected Method getDecoderMethod() {
            try {
                return DdsIndexDef.Factory.class.getDeclaredMethod("loadFrom", org.radixware.schemas.ddsdef.Index.class);
            } catch (NoSuchMethodException | SecurityException ex) {
                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
            }
            return null;
        }
    }

    @Override
    public ClipboardSupport<? extends DdsIndexDef> getClipboardSupport() {
        return new DdsIndexClipboardSupport();
    }

    @Override
    protected void appendAdditionalToolTip(StringBuilder sb) {
        super.appendAdditionalToolTip(sb);
        sb.append("<br>Database name: " + getDbName());
    }
}
