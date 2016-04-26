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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.defs.ClipboardSupport;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.IDbType;
import org.radixware.kernel.common.defs.dds.utils.DbTypeUtils;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.common.utils.Utils;

/**
 * Информация о часто используемых типах {@link DdsColumnDef колонок}.
 * {@link DdsColumnDef Колонка} может наследоваться от шаблона, указывая, какие
 * атрибуты наследуются. Наследуемые значения атрибутов синхронизируются при
 * изменении шаблона.
 *
 */
public class DdsColumnTemplateDef extends DdsDefinition implements IDbType {

    /**
     * Группа атрибутов шаблона колонки. См. описание класса.
     */
    public enum EAttribute {

        /**
         * Атрибут {@link DdsColumnTemplateDef#getEnumId()}
         */
        ENUM,
        /**
         * Все атрибуты шаблона колонки, кроме
         * {@link DdsColumnTemplateDef#getEnumId()}}.
         */
        DATABASE
    }
    private int length = 9;
    protected static final byte INIT_STATUS_DB_TYPE = DdsDefinition.INIT_STATUS_RESERVERD_MAX << 1;

    private boolean isDbTypeInited() {
        return (initStatus & INIT_STATUS_DB_TYPE) != 0;
    }

    /**
     * Получить длину поля колонки в базе данных.
     */
    @Override
    public int getLength() {
        return length;
    }

    @Override
    public void setLength(int length) {
        if (!Utils.equals(this.length, length)) {
            this.length = length;
            this.setEditState(EEditState.MODIFIED);
        }
    }
    private int precision = 0;

    /**
     * Получить точность (количество знаков после запятой) поля колонки в базе
     * данных. Актуально только для колонок типа Num, ArrayNum.
     */
    @Override
    public int getPrecision() {
        return precision;
    }

    @Override
    public void setPrecision(int precision) {
        if (!Utils.equals(this.precision, precision)) {
            this.precision = precision;
            this.setEditState(EEditState.MODIFIED);
        }
    }
    private String dbType = null;

    /**
     * Получить имя типа колонки в базе данных, например, 'NUMBER(9,0)'.
     * Используется для генерации скрипта создания колонки.
     */
    @Override
    public String getDbType() {
        synchronized (this) {
            if (!isDbTypeInited()) {
                dbType = calcAutoDbType();
                this.initStatus |= INIT_STATUS_DB_TYPE;
            }
            return dbType;
        }
    }

    public void setDbType(String dbType) {
        getDbType();
        if (!Utils.equals(this.dbType, dbType)) {
            this.dbType = dbType;
            this.setEditState(EEditState.MODIFIED);
        }
    }
    private boolean autoDbType = true;

    /**
     * Является-ли имя типа колонки в базе данных автогенерируемым. Если имя
     * автогенерируемое, то оно обновляется при изменении модели. Генерация
     * имени производится на основании логического типа колонки (см.
     * {@link #getValType()}), а так же ее размера (см. {@link #getLength()})и
     * точности (см. {@link #getPrecision()}). Не рекомендуется придерживаться
     * формата генерации, он может измениться.
     */
    public boolean isAutoDbType() {
        return autoDbType;
    }

    public void setAutoDbType(boolean autoDbType) {
        if (!Utils.equals(this.autoDbType, autoDbType)) {
            this.autoDbType = autoDbType;
            this.setEditState(EEditState.MODIFIED);
        }
    }
    private Id nativeDbTypeId = null;

    public Id getNativeDbTypeId() {
        return nativeDbTypeId;
    }

    public void setNativeDbTypeId(Id nativeDbTypeId) {
        if (!Utils.equals(this.nativeDbTypeId, nativeDbTypeId)) {
            this.nativeDbTypeId = nativeDbTypeId;
            this.setEditState(EEditState.MODIFIED);
        }
    }

    /**
     * Find native database type of the column template. return type or null if
     * not found.
     */
    public DdsTypeDef findNativeDbType() {
        final Id thisNativeDbTypeId = getNativeDbTypeId();
        return getModule().getDdsTypeSearcher().findById(thisNativeDbTypeId).get();
    }

    /**
     * Find native database type of the column template. Function is actual only
     * if value type is NATIVE_DB_TYPE.
     *
     * @throws DefinitionNotFoundError if not found.
     */
    public DdsTypeDef getNativeDbType() {
        final DdsTypeDef nativeDbType = findNativeDbType();
        if (nativeDbType != null) {
            return nativeDbType;
        } else {
            throw new DefinitionNotFoundError(getNativeDbTypeId());
        }
    }

    @Override
    public final String calcAutoDbType() {

        return DbTypeUtils.calcAutoDbType(valType, length, precision, getNativeDbTypeId(), getModule());
    }
    private EValType valType = EValType.INT;

    /**
     * Получить логический тип колонки.
     */
    @Override
    public EValType getValType() {
        return valType;
    }

    public void setValType(EValType valType) {
        if (!Utils.equals(this.valType, valType)) {
            this.valType = valType;
            this.setEditState(EEditState.MODIFIED);
        }
    }

    /**
     * For DdsColumnDef.
     */
    protected DdsColumnTemplateDef(final EDefinitionIdPrefix idPrefix, final String name) {
        super(idPrefix, name);
    }

    protected DdsColumnTemplateDef(final String name) {
        this(EDefinitionIdPrefix.DDS_COLUMN_TEMPLATE, name);
    }

    protected DdsColumnTemplateDef(org.radixware.schemas.ddsdef.ColumnTemplate xColumnTemplate) {
        super(xColumnTemplate);

        this.valType = xColumnTemplate.getValType();
        this.length = xColumnTemplate.getLength();

        if (xColumnTemplate.isSetPrecision()) {
            this.precision = xColumnTemplate.getPrecision();
        }

        if (xColumnTemplate.isSetAutoDbType()) {
            this.autoDbType = xColumnTemplate.getAutoDbType();
        }

        if (xColumnTemplate.isSetNativeDbTypeId()) {
            this.nativeDbTypeId = Id.Factory.loadFrom(xColumnTemplate.getNativeDbTypeId());
        }

        this.dbType = xColumnTemplate.getDbType();
        this.initStatus |= INIT_STATUS_DB_TYPE;
        if (xColumnTemplate.isSetDeprecated()) {
            this.isDeprecated = xColumnTemplate.getDeprecated();
        }
    }

    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder(100);
        result.append(super.toString());
        result.append("; ValType: ");
        result.append(String.valueOf(valType));
        result.append("; DbType: ");
        result.append(getDbType());
        result.append("; Length: ");
        result.append(length);
        if (valType == EValType.NUM) {
            result.append("; Precision: ");
            result.append(precision);
        }

        return result.toString();
    }

    public static final class Factory {

        private Factory() {
        }

        public static DdsColumnTemplateDef newInstance(final String name) {
            return new DdsColumnTemplateDef(name);
        }

        public static DdsColumnTemplateDef loadFrom(org.radixware.schemas.ddsdef.ColumnTemplate xColumnTemplate) {
            return new DdsColumnTemplateDef(xColumnTemplate);
        }
    }

    @Override
    public RadixIcon getIcon() {
        return DdsDefinitionIcon.COLUMN_TEMPLATE;
    }

    @Override
    public void collectDependences(List<Definition> list) {
        super.collectDependences(list);

        if (this.getValType() == EValType.NATIVE_DB_TYPE) {
            DdsTypeDef nativeDbType = findNativeDbType();
            if (nativeDbType != null) {
                list.add(nativeDbType);
            }
        }
    }

    private class DdsColumnTemplateClipboardSupport extends DdsClipboardSupport<DdsColumnTemplateDef> {

        public DdsColumnTemplateClipboardSupport() {
            super(DdsColumnTemplateDef.this);
        }

        @Override
        protected XmlObject copyToXml() {
            org.radixware.schemas.ddsdef.ColumnTemplate xColumnTemplate = org.radixware.schemas.ddsdef.ColumnTemplate.Factory.newInstance();
            DdsModelWriter.writeColumnTemplate(DdsColumnTemplateDef.this, xColumnTemplate);
            return xColumnTemplate;
        }

        @Override
        protected DdsColumnTemplateDef loadFrom(XmlObject xmlObject) {
            org.radixware.schemas.ddsdef.ColumnTemplate xColumnTemplate = (org.radixware.schemas.ddsdef.ColumnTemplate) xmlObject;
            return DdsColumnTemplateDef.Factory.loadFrom(xColumnTemplate);
        }

        @Override
        protected Method getDecoderMethod() {
            try {
                return DdsColumnTemplateDef.Factory.class.getDeclaredMethod("loadFrom", org.radixware.schemas.ddsdef.ColumnTemplate.class);
            } catch (NoSuchMethodException | SecurityException ex) {
                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
            }
            return null;
        }
    }

    @Override
    public ClipboardSupport<? extends DdsColumnTemplateDef> getClipboardSupport() {
        return new DdsColumnTemplateClipboardSupport();
    }

    @Override
    protected void appendAdditionalToolTip(StringBuilder sb) {
        super.appendAdditionalToolTip(sb);

        sb.append("<br>Value type: ").append(getValType().getName());
        sb.append("<br>Database type: ").append(getDbType());
    }
    public static final String COLUMN_TEMPLATE_TYPE_TITLE = "Column Template";
    public static final String COLUMN_TEMPLATE_TYPES_TITLE = "Column Templates";

    @Override
    public String getTypeTitle() {
        return COLUMN_TEMPLATE_TYPE_TITLE;
    }

    @Override
    public String getTypesTitle() {
        return COLUMN_TEMPLATE_TYPES_TITLE;
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
}
