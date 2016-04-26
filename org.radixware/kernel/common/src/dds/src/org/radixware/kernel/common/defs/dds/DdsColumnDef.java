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
import java.util.ArrayList;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.sqml.Sqml;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.defs.ClipboardSupport;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObjectIcon;
import org.radixware.kernel.common.defs.dds.utils.DbNameUtils;
import org.radixware.kernel.common.defs.value.RadixDefaultValue;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.common.sqml.ISqmlProperty;
import org.radixware.kernel.common.sqml.Sqml.Tag;
import org.radixware.kernel.common.utils.Utils;

/**
 * Метаинформация о колонке {@link DdsTableDef таблицы} в базе данных.
 *
 */
public class DdsColumnDef extends DdsColumnTemplateDef implements IDdsTableItemDef, ISqmlProperty {

    private String dbName = "";

    /**
     * Получить имя колонки в базе данных. Используется для генерации SQL
     * выражений.
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
    private boolean notNull = false;

    /**
     * Является-ли значение колонки обязательным (т.е. не может содержать null).
     */
    public boolean isNotNull() {
        return notNull;
    }

    public void setNotNull(boolean notNull) {
        if (!Utils.equals(this.notNull, notNull)) {
            this.notNull = notNull;
            this.setEditState(EEditState.MODIFIED);
        }
    }

    /**
     * Входит-ли колонка в первичный ключ. Подсчитывается через наличие ссылки
     * на колонку в первичном ключе таблицы.
     */
    public boolean isPrimaryKey() {
        final DdsPrimaryKeyDef pk = getOwnerTable().getPrimaryKey();
        for (DdsIndexDef.ColumnInfo columnInfo : pk.getColumnsInfo()) {
            if (columnInfo.getColumnId().equals(this.getId())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void check(Tag source, IProblemHandler problemHandler) {
        //do nothing
    }

    /**
     * Информация об аудите {@link DdsTableDef таблицы}. См. rx_audit.doc.
     */
    public class AuditInfo {

        private final DdsAuditMask auditMask;
        private boolean onUpdate = false;

        public AuditInfo() {
            this.auditMask = new DdsAuditMask(DdsColumnDef.this);
        }

        protected AuditInfo(org.radixware.schemas.ddsdef.Column xColumn) {
            super();

            this.auditMask = new DdsAuditMask(DdsColumnDef.this);

            if (xColumn.isSetAuditMask()) {
                this.auditMask.loadFrom(xColumn.getAuditMask());
            }

            if (xColumn.isSetAuditOnUpdate()) {
                this.onUpdate = xColumn.getAuditOnUpdate();
            } else {
                this.onUpdate = auditMask.isUpdate();
            }
        }

        public void appendTo(org.radixware.schemas.ddsdef.Column xColumn) {
            if (auditMask.toLong() != 0) {
                xColumn.setAuditMask(auditMask.toLong());
            }
            if (onUpdate != auditMask.isUpdate()) {
                xColumn.setAuditOnUpdate(onUpdate);
            }
        }

        public boolean isSaveValueOnInsert() {
            return auditMask.isInsert();
        }

        public void setSaveValueOnInsert(boolean saveValue) {
            auditMask.setInsert(saveValue);
        }

        public boolean isSaveValuesOnUpdate() {
            return auditMask.isUpdate();
        }

        public void setSaveValuesOnUpdate(boolean saveValues) {
            auditMask.setUpdate(saveValues);
        }

        public boolean isSaveValueOnDelete() {
            return auditMask.isDelete();
        }

        public void setSaveValueOnDelete(boolean saveValue) {
            auditMask.setDelete(saveValue);
        }

        public boolean isOnUpdate() {
            return onUpdate;
        }

        public void setOnUpdate(boolean onUpdate) {
            if (this.onUpdate != onUpdate) {
                this.onUpdate = onUpdate;
                setEditState(EEditState.MODIFIED);
            }
        }

        public boolean isSaveValues() {
            return auditMask.isEnabled();
        }
    }
    private final AuditInfo auditInfo;

    /**
     * Получить информацию об аудите таблицы.
     */
    public AuditInfo getAuditInfo() {
        return auditInfo;
    }
    private final Object expressionLock = new Object();
    private DdsSqml expression = null;

    /**
     * Получить SQML выражение, по которому строится SQL скрипт для получения
     * значения виртуальной колонки.
     *
     * @return SQML выражение, или null, если колонка не виртуальная.
     */
    @Override
    public Sqml getExpression() {
        return expression;
    }

    public boolean isExpression() {
        return expression != null;
    }

    /**
     * Create new expression for the column
     *
     * @return new SQML instance, attached to the column.
     */
    public Sqml createNewExpression() {
        synchronized (expressionLock) {
            if (this.expression != null) {
                this.expression.getItems().clear();
            } else {
                this.expression = new DdsSqml(this);
            }
            return this.expression;
        }
    }

    public void removeExpression() {
        synchronized (expressionLock) {
            if (this.expression != null) {
                this.expression.unsetOwner();
                this.expression = null;
            }
        }
    }
    private Id sequenceId = null;

    /**
     * Получить указатель на {@link DdsSequenceDef последовательность} к
     * которому привязана колонка. Используется на более высоких уровнях для
     * автоматического получения значений колонки в добавляемой записи.
     */
    public Id getSequenceId() {
        return sequenceId;
    }

    public void setSequenceId(Id sequenceId) {
        if (!Utils.equals(this.sequenceId, sequenceId)) {
            this.sequenceId = sequenceId;
            setEditState(EEditState.MODIFIED);
        }
    }

    /**
     * Find {@link DdsSequenceDef} of the column.
     *
     * @return sequence or null it not found.
     */
    public DdsSequenceDef findSequence() {
        final DdsModule module = getModule();
        if (module != null) {
            return module.getDdsSequenceSearcher().findById(getSequenceId()).get();
        } else {
            return null;
        }
    }

    /**
     * Find {@link DdsSequenceDef}.
     *
     * @throws DefinitionNotFoundError
     */
    public DdsSequenceDef getSequence() {
        DdsSequenceDef sequence = findSequence();
        if (sequence != null) {
            return sequence;
        }
        throw new DefinitionNotFoundError(getSequenceId());
    }
    boolean generatedInDb = true;

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
    private boolean hidden = false;

    /**
     * Должна-ли колонка показываться пользователю. Используется на более
     * высоких уровнях, когда требуется скрыть от пользователя некоторые колонки
     * в списках в целях дополнительной безопасности.
     */
    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        if (this.hidden != hidden) {
            this.hidden = hidden;
            setEditState(EEditState.MODIFIED);
        }
    }
    private final List<ValAsStr> initialValues = new ArrayList<ValAsStr>();

    /**
     * Получить список начальных значений колонки в таблице. Используется при
     * генерации скрипта создания и изменения базы данных. Используйте
     * RadixWareValueConverter для преобразования в конечное значение. См. также
     * {@link #getValType()}. Размеры списков начальных значений всех колонок
     * {@link DdsTableDef таблицы} должны совпадать, за исключением, если список
     * пуст, это означает, что для данной колонки не заданы начальные значения,
     * т.е. всегда используется значение по умолчанию.
     *
     * @return список начальных значений.
     */
    public List<ValAsStr> getInitialValues() {
        return initialValues;
    }
    private DdsCheckConstraintDef checkConstraint = null;

    /**
     * Получить метаинформацию об ограничении, накладываемом на значение колонки
     * в базу данных.
     *
     * @return check constraint or null if not defined.
     */
    public DdsCheckConstraintDef getCheckConstraint() {
        return checkConstraint;
    }

    public void setCheckConstraint(DdsCheckConstraintDef checkConstraint) {
        if (this.checkConstraint == checkConstraint) {
            return;
        }
        if (this.checkConstraint != null) {
            this.checkConstraint.setOwnerColumn(null);
        }
        this.checkConstraint = checkConstraint;
        if (checkConstraint != null) {
            checkConstraint.setOwnerColumn(this);
        }
        setEditState(EEditState.MODIFIED);
    }

    @Override
    public RadixIcon getIcon() {
        final EValType curValType = getValType();
        if (curValType != null) {
            return RadixObjectIcon.getForValType(curValType);
        } else {
            return RadixObjectIcon.UNKNOWN;
        }
    }

    protected DdsColumnDef(final String name) {
        super(EDefinitionIdPrefix.DDS_COLUMN, name);
        this.auditInfo = new AuditInfo();
    }

    protected DdsColumnDef(org.radixware.schemas.ddsdef.Column xColumn) {
        super(xColumn);

        if (xColumn.isSetGenerateInDb()) {
            this.generatedInDb = xColumn.getGenerateInDb();
        }

        this.dbName = xColumn.getDbName();

        this.notNull = xColumn.getNotNull();

        if (xColumn.isSetAutoDbName()) {
            this.autoDbName = xColumn.getAutoDbName();
        }

        if (xColumn.isSetSequenceId()) {
            this.sequenceId = Id.Factory.loadFrom(xColumn.getSequenceId());
        }

        if (xColumn.isSetTemplateId()) {
            this.templateId = Id.Factory.loadFrom(xColumn.getTemplateId());
        }

        if (xColumn.isSetCheckConstraint()) {
            this.checkConstraint = DdsCheckConstraintDef.Factory.loadFrom(xColumn.getCheckConstraint());
            this.checkConstraint.setOwnerColumn(this);
        }

        this.auditInfo = new AuditInfo(xColumn);

        if (xColumn.isSetHidden()) {
            this.hidden = xColumn.getHidden();
        }

        if (xColumn.isSetInitialValues()) {
            final List<String> xInitialValues = xColumn.getInitialValues().getValueList();
            for (String initialValue : xInitialValues) {
                // null всегда выгружается в виде пустой строки, 
                // чтобы обеспечить одинаковое количество записей начальных значений всех колонок таблицы.
                if (initialValue != null && !initialValue.isEmpty()) {
                    this.initialValues.add(ValAsStr.Factory.loadFrom(initialValue));
                } else {
                    this.initialValues.add(null);
                }
            }
        }

        if (xColumn.isSetDefaultVal()) {
            this.defaultValue = RadixDefaultValue.Factory.loadFrom(xColumn.getDefaultVal());
        }

        if (xColumn.isSetExpression()) {
            createNewExpression().loadFrom(xColumn.getExpression());
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
        result.append("; NotNull: ");
        result.append(notNull);
        result.append("; DefaultValue: ");
        result.append(defaultValue);

        return result.toString();
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        if (checkConstraint != null) {
            checkConstraint.visit(visitor, provider);
        }

        Sqml thisExpression = expression;
        if (thisExpression != null) {
            thisExpression.visit(visitor, provider);
        }
    }

    @Override
    public DdsTableDef getOwnerTable() {
        return (DdsTableDef) getOwnerDefinition();
    }

    @Override
    public DdsTableDef findOwnerTable() {
        return (DdsTableDef) getOwnerDefinition();
    }

    public static final class Factory {

        private Factory() {
        }

        public static DdsColumnDef newInstance(final String name) {
            return new DdsColumnDef(name);
        }

        public static DdsColumnDef loadFrom(org.radixware.schemas.ddsdef.Column xColumn) {
            return new DdsColumnDef(xColumn);
        }
    }
    private Id templateId = null;

    /**
     * Get {@link DdsColumnTemplateDef} identifier.
     */
    public Id getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Id templateId) {
        if (!Utils.equals(this.templateId, templateId)) {
            this.templateId = templateId;
            setEditState(EEditState.MODIFIED);
        }
    }

    /**
     * Find {@link DdsColumnTemplateDef}.
     *
     * @return column template or null it not found.
     */
    public DdsColumnTemplateDef findTemplate() {
        final DdsModule module = getModule();
        if (module != null) {
            return module.getDdsColumnTemplateSearcher().findById(getTemplateId()).get();
        } else {
            return null;
        }
    }

    /**
     * Find {@link DdsColumnTemplateDef}.
     *
     * @throws DefinitionNotFoundError
     */
    public DdsColumnTemplateDef getTemplate() {
        DdsColumnTemplateDef template = findTemplate();
        if (template != null) {
            return template;
        }
        throw new DefinitionNotFoundError(getTemplateId());
    }
    private RadixDefaultValue defaultValue = null;

    /**
     * Получить начальное значение колонки в базе данных. Подставляется при
     * добавлении записи в таблицу, если значение колонки не указано явным
     * образом.
     *
     * @return начальное значение колонки в базе данных, в т.ч. null.
     */
    public RadixDefaultValue getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(RadixDefaultValue defaultValue) {
        if (!Utils.equals(this.defaultValue, defaultValue)) {
            this.defaultValue = defaultValue;
            this.setEditState(EEditState.MODIFIED);
        }
    }

    @Override
    public String calcAutoDbName() {
        String name = getName();
        return DbNameUtils.calcAutoDbName(name);
    }

    @Override
    public void collectDependences(List<Definition> list) {
        super.collectDependences(list);

        DdsSequenceDef sequence = findSequence();
        if (sequence != null) {
            list.add(sequence);
        }
        DdsColumnTemplateDef template = findTemplate();
        if (template != null) {
            list.add(template);
        }
    }

    private class DdsColumnClipboardSupport extends DdsClipboardSupport<DdsColumnDef> {

        public DdsColumnClipboardSupport() {
            super(DdsColumnDef.this);
        }

        @Override
        protected XmlObject copyToXml() {
            org.radixware.schemas.ddsdef.Column xColumn = org.radixware.schemas.ddsdef.Column.Factory.newInstance();
            DdsModelWriter.writeColumn(DdsColumnDef.this, xColumn);
            return xColumn;
        }

        @Override
        protected DdsColumnDef loadFrom(XmlObject xmlObject) {
            org.radixware.schemas.ddsdef.Column xColumn = (org.radixware.schemas.ddsdef.Column) xmlObject;
            return DdsColumnDef.Factory.loadFrom(xColumn);
        }

        @Override
        protected Method getDecoderMethod() {
            try {
                return Factory.class.getDeclaredMethod("loadFrom", org.radixware.schemas.ddsdef.Column.class);
            } catch (NoSuchMethodException | SecurityException ex) {
                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
            }
            return null;
        }
    }

    @Override
    public ClipboardSupport<? extends DdsColumnDef> getClipboardSupport() {
        return new DdsColumnClipboardSupport();
    }

    @Override
    protected void appendAdditionalToolTip(StringBuilder sb) {
        final List<String> attributes = new ArrayList<String>();
        if (isPrimaryKey()) {
            attributes.add("Primary key");
        }
        if (isNotNull()) {
            attributes.add("Not null");
        }
        if (!isGeneratedInDb()) {
            attributes.add("Virtual");
        }
        if (getExpression() != null) {
            attributes.add("Expression");
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

        super.appendAdditionalToolTip(sb);
    }

    /**
     * @return true if this column used in one of outgoing references of this
     * table. It is not recommended to use this function frequently, because
     * gathering of outgoing references required some time.
     */
    public boolean isForeignKey() {
        final DdsTableDef table = getOwnerTable();
        for (DdsReferenceDef reference : table.collectOutgoingReferences()) {
            for (DdsReferenceDef.ColumnsInfoItem item : reference.getColumnsInfo()) {
                if (Utils.equals(item.getChildColumnId(), getId())) {
                    return true;
                }
            }
        }
        return false;
    }
    public static final String COLUMN_TYPE_TITLE = "Column";
    public static final String COLUMN_TYPES_TITLE = "Columns";

    @Override
    public String getTypeTitle() {
        return COLUMN_TYPE_TITLE;
    }

    @Override
    public String getTypesTitle() {
        return COLUMN_TYPES_TITLE;
    }
}
