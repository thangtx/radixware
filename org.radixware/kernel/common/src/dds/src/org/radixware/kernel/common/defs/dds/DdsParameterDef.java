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

import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.defs.ClipboardSupport;
import org.radixware.kernel.common.defs.RadixObjectIcon;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EDocGroup;
import org.radixware.kernel.common.enums.EParamDirection;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.utils.Utils;

/**
 * Метаинформация о параметре PL/SQL функции.
 */
public class DdsParameterDef extends DdsDefinition implements IDdsDbDefinition {

    /**
     * Получить функцию, которой принадлежит параметр.
     */
    public DdsFunctionDef getOwnerFunction() {
        return (DdsFunctionDef) getOwnerDefinition();
    }

    @Override
    public String getDbName() {
        return getName();
    }
    private EParamDirection direction = EParamDirection.IN;

    /**
     * Получить направление передачи параметра в функции.
     */
    public EParamDirection getDirection() {
        return direction;
    }

    public void setDirection(EParamDirection direction) {
        if (!Utils.equals(this.direction, direction)) {
            this.direction = direction;
            setEditState(EEditState.MODIFIED);
        }
    }
    private EValType valType = EValType.INT;

    /**
     * Получить логический тип параметра.
     * При смене используется для автоматической генерации имени типа параметра в базе данных.
     */
    public EValType getValType() {
        return valType;
    }

    public void setValType(EValType valType) {
        if (!Utils.equals(this.valType, valType)) {
            this.valType = valType;
            setEditState(EEditState.MODIFIED);
        }
    }
    private String dbType = "integer";

    /**
     * Получить имя типа параметра.
     */
    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        if (!Utils.equals(this.dbType, dbType)) {
            this.dbType = dbType;
            setEditState(EEditState.MODIFIED);
        }
    }
    private String defaultVal = "";

    /**
     * Получить значение параметра по умолчанию.
     * Формат значения - как есть, т.е. без преобразований подставляется в скрипт создания функции.
     * Может быть null, подставится в скрипте как =null.
     * @return значение параметра по умолчанию или пустую строку, если оно не задано.
     */
    public String getDefaultVal() {
        return defaultVal;
    }

    public void setDefaultVal(String defaultVal) {
        if (!Utils.equals(this.defaultVal, defaultVal)) {
            this.defaultVal = defaultVal;
            setEditState(EEditState.MODIFIED);
        }
    }

    @Override
    public boolean isGeneratedInDb() {
        final DdsFunctionDef ownerFunction = getOwnerFunction();
        return ownerFunction.isGeneratedInDb();
    }

    protected DdsParameterDef(final String name) {
        super(EDefinitionIdPrefix.DDS_FUNC_PARAM, name);
    }

    public DdsParameterDef(org.radixware.schemas.ddsdef.PlSqlFunction.Params.Param xParam) {
        super(xParam);
        this.dbType = xParam.getDbType();
        this.direction = xParam.getDirection();
        this.valType = xParam.getValType();
        if (xParam.isNilDefaultVal()) {
            this.defaultVal = null;
        } else if (xParam.isSetDefaultVal()) {
            this.defaultVal = xParam.getDefaultVal();
        } else {
            this.defaultVal = "";
        }
    }

    public static final class Factory {

        private Factory() {
            super();
        }

        public static DdsParameterDef newInstance(final String name) {
            return new DdsParameterDef(name);
        }

        public static DdsParameterDef loadFrom(org.radixware.schemas.ddsdef.PlSqlFunction.Params.Param xParam) {
            return new DdsParameterDef(xParam);
        }
    }

    @Override
    public RadixIcon getIcon() {
        final EValType curValType = getValType();
        if (curValType != null) {
            return RadixObjectIcon.getForValType(valType);
        } else {
            return RadixObjectIcon.UNKNOWN;
        }
    }

    private class DdsParameterClipboardSupport extends DdsClipboardSupport<DdsParameterDef> {

        public DdsParameterClipboardSupport() {
            super(DdsParameterDef.this);
        }

        @Override
        protected XmlObject copyToXml() {
            org.radixware.schemas.ddsdef.PlSqlFunction.Params.Param xParameter = org.radixware.schemas.ddsdef.PlSqlFunction.Params.Param.Factory.newInstance();
            DdsModelWriter.writeParameter(DdsParameterDef.this, xParameter);
            return xParameter;
        }

        @Override
        protected DdsParameterDef loadFrom(XmlObject xmlObject) {
            org.radixware.schemas.ddsdef.PlSqlFunction.Params.Param xParameter = (org.radixware.schemas.ddsdef.PlSqlFunction.Params.Param) xmlObject;
            return DdsParameterDef.Factory.loadFrom(xParameter);
        }
    }

    @Override
    public ClipboardSupport<? extends DdsParameterDef> getClipboardSupport() {
        return new DdsParameterClipboardSupport();
    }

    @Override
    public EDocGroup getDocGroup() {
        return EDocGroup.PACKAGE_FUNC_PARAMETER;
    }
}
