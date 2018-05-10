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
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.enums.ENamingPolicy;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.sqml.Sqml;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.defs.ClipboardSupport;
import org.radixware.kernel.common.defs.ClipboardSupport.Transfer;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjectInitializationPolicy;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EDocGroup;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.utils.Utils;

/**
 * Метаинформация о функции в {@link DdsPlSqlObjectDef PL/SQL объекте}.
 *
 */
public class DdsFunctionDef extends DdsPlSqlObjectItemDef implements IDdsDbDefinition {

    private class PurityLevel extends DdsPurityLevel {

        public PurityLevel() {
        }

        @Override
        protected void onChanged() {
            DdsFunctionDef.this.setEditState(EEditState.MODIFIED);
        }
    }
    private final Sqml body = new DdsSqml(this);

    /**
     * Получить SQML выражение, по которому строится тело функции.
     */
    public Sqml getBody() {
        return body;
    }
    private EValType resultValType = null;

    /**
     * Получить тип возвращаемого результата. При смене используется для
     * автоматической генерации имени типа результата функции в базе данных.
     *
     * @return тип возвращаемого результата или null, если ф-я не возвращает
     * результат (процедура).
     */
    public EValType getResultValType() {
        return resultValType;
    }

    public void setResultValType(EValType resultValType) {
        if (!Utils.equals(this.resultValType, resultValType)) {
            this.resultValType = resultValType;
            this.setEditState(EEditState.MODIFIED);
        }
    }

    @Override
    public String getDbName() {
        return getName();
    }

    private static class DdsParameters extends DdsDefinitions<DdsParameterDef> {

        public DdsParameters(DdsFunctionDef ownerFunction) {
            super(ownerFunction);
        }

        @Override
        protected ClipboardSupport.CanPasteResult canPaste(Transfer objectInClipboard) {
            ClipboardSupport.CanPasteResult result = super.canPaste(objectInClipboard);
            if (result != ClipboardSupport.CanPasteResult.YES) {
                return result;
            }

            return objectInClipboard.getObject() instanceof DdsParameterDef ? ClipboardSupport.CanPasteResult.YES : ClipboardSupport.CanPasteResult.NO;
        }
    }
    private final DdsDefinitions<DdsParameterDef> parameters = new DdsParameters(this);

    /**
     * Получить список параметров функции.
     */
    public DdsDefinitions<DdsParameterDef> getParameters() {
        return parameters;
    }
    private final DdsPurityLevel purityLevel = new PurityLevel();

    public DdsPurityLevel getPurityLevel() {
        return purityLevel;
    }

    public static class ReliesOnTableInfo extends RadixObject {

        private Id tableId = null;

        protected ReliesOnTableInfo() {
        }

        public Id getTableId() {
            return tableId;
        }

        public void setTableId(Id tableId) {
            if (!Utils.equals(this.tableId, tableId)) {
                this.tableId = tableId;
                this.setEditState(EEditState.MODIFIED);
            }
        }

        /**
         * Find relies on table.
         *
         * @return table or null if not found.
         */
        public DdsTableDef findTable() {
            final DdsFunctionDef function = getOwnerFunction();
            if (function != null) {
                final DdsModule module = function.getModule();
                if (module != null) {
                    return module.getDdsTableSearcher().findById(getTableId()).get();
                }
            }
            return null;
        }

        /**
         * Find relies on table.
         *
         * @throws DefinitionNotFoundError if not found.
         */
        public DdsTableDef getTable() {
            DdsTableDef table = findTable();
            if (table == null) {
                throw new DefinitionNotFoundError(tableId);
            }
            return table;
        }

        public DdsFunctionDef getOwnerFunction() {
            for (RadixObject owner = getContainer(); owner != null; owner = owner.getContainer()) {
                if (owner instanceof DdsFunctionDef) {
                    return (DdsFunctionDef) owner;
                }
            }
            return null;
        }

        public static final class Factory {

            private Factory() {
            }

            public static ReliesOnTableInfo newInstance() {
                return new ReliesOnTableInfo();
            }
        }
    }

    public class ReliesOnTablesInfo extends RadixObjects<ReliesOnTableInfo> {

        public ReliesOnTablesInfo(DdsFunctionDef owner) {
            super(owner);
        }

        public ReliesOnTableInfo add(Id reliesOnTableId) {
            ReliesOnTableInfo reliesOnTableInfo = ReliesOnTableInfo.Factory.newInstance();
            reliesOnTableInfo.setTableId(reliesOnTableId);
            add(reliesOnTableInfo);
            return reliesOnTableInfo;
        }
    }
    private final ReliesOnTablesInfo reliesOnTables = new ReliesOnTablesInfo(this);

    /**
     * Получить список {@link DdsTableDef таблиц}, от содержимого которых
     * зависит возвращаемый функцией результат. Используется при генерации
     * скрипта создания функции для оптимизаций в Oracle.
     */
    public ReliesOnTablesInfo getReliesOnTablesInfo() {
        return reliesOnTables;
    }
    String resultDbType = "";

    /**
     * Получить имя типа возвращаемого результата.
     */
    public String getResultDbType() {
        return resultDbType;
    }

    public void setResultDbType(String resultDbType) {
        if (!Utils.equals(this.resultDbType, resultDbType)) {
            this.resultDbType = resultDbType;
            setEditState(EEditState.MODIFIED);
        }
    }
    boolean cachedResult = false;

    /**
     * Кэширует-ли функция результат. Используется для генерации скрипта
     * создания функции в базе данных.
     */
    public boolean isCachedResult() {
        return cachedResult;
    }

    public void setCachedResult(boolean cachedResult) {
        if (!Utils.equals(this.cachedResult, cachedResult)) {
            this.cachedResult = cachedResult;
            setEditState(EEditState.MODIFIED);
        }
    }
    private boolean deterministic = false;

    /**
     * Возвращает-ли функция один и тот же результат при тех же значениях
     * параметров. Используется для генерации скрипта создания функции в базе
     * данных.
     */
    public boolean isDeterministic() {
        return deterministic;
    }

    public void setDeterministic(boolean deterministic) {
        if (!Utils.equals(this.deterministic, deterministic)) {
            this.deterministic = deterministic;
            setEditState(EEditState.MODIFIED);
        }
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        this.getParameters().visit(visitor, provider);
        body.visit(visitor, provider);
    }

    protected DdsFunctionDef(final String name) {
        super(EDefinitionIdPrefix.DDS_FUNCTION, name);
        getBody().setSql("is\nbegin\nend;");
    }

    public DdsFunctionDef(org.radixware.schemas.ddsdef.PlSqlFunction xFunction) {
        super(xFunction);

        if (xFunction.isSetGenerateInDb()) {
            this.generatedInDb = xFunction.getGenerateInDb();
        }

        if (xFunction.isSetResultDbType()) {
            this.resultDbType = xFunction.getResultDbType();
        }
        if (!RadixObjectInitializationPolicy.get().isRuntime()) {
            this.body.loadFrom(xFunction.getBody());
        }
        this.deterministic = xFunction.getDeterministic();
        this.cachedResult = xFunction.getCacheResult();

        this.purityLevel.loadFromBitMask(xFunction.getPurityLevelMask());

        if (xFunction.isSetResultValType()) {
            this.resultValType = xFunction.getResultValType();
        }

        if (xFunction.isSetReliesOnTableIds()) {
            for (Object reliesOnTableIdAsStr : xFunction.getReliesOnTableIds()) {
                Id reliesOnTableId = Id.Factory.loadFrom((String) reliesOnTableIdAsStr);
                this.getReliesOnTablesInfo().add(reliesOnTableId);
            }
        }
        if (xFunction.isSetParams()) {
            List<org.radixware.schemas.ddsdef.PlSqlFunction.Params.Param> xmlParams = xFunction.getParams().getParamList();
            for (org.radixware.schemas.ddsdef.PlSqlFunction.Params.Param xmlParam : xmlParams) {
                DdsParameterDef param = DdsParameterDef.Factory.loadFrom(xmlParam);
                this.getParameters().add(param);
            }
        }
        if (xFunction.isSetDeprecated()) {
            this.isDeprecated = xFunction.getDeprecated();
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

    public static final class Factory {

        private Factory() {
        }

        public static DdsFunctionDef newInstance(final String name) {
            return new DdsFunctionDef(name);
        }

        public static DdsFunctionDef loadFrom(org.radixware.schemas.ddsdef.PlSqlFunction xFunction) {
            return new DdsFunctionDef(xFunction);
        }
    }
    boolean generatedInDb = true;

    @Override
    public boolean isGeneratedInDb() {
        if (!generatedInDb) {
            return false;
        }
        final DdsPlSqlObjectDef ownerPlSqlObject = getOwnerPlSqlObject();
        return ownerPlSqlObject.isGeneratedInDb();
    }

    public void setGeneratedInDb(boolean generatedInDb) {
        if (this.generatedInDb != generatedInDb) {
            this.generatedInDb = generatedInDb;
            setEditState(EEditState.MODIFIED);
        }
    }

    @Override
    public RadixIcon getIcon() {
        return DdsDefinitionIcon.FUNCTION;
    }

    @Override
    public void collectDependences(List<Definition> list) {
        super.collectDependences(list);

        for (ReliesOnTableInfo info : getReliesOnTablesInfo()) {
            DdsTableDef table = info.findTable();
            if (table != null) {
                list.add(table);
            }
        }
    }

    private static class DdsFunctionTransfer extends DdsTransfer<DdsFunctionDef> {

        private final boolean wasPublic;

        public DdsFunctionTransfer(DdsFunctionDef source) {
            super(source);
            this.wasPublic = source.isPublic();
        }

        @Override
        protected void afterPaste() {
            super.afterPaste();
            if (wasPublic) {
                getObject().setPublic(true);
            }
        }
    }

    private class DdsFunctionClipboardSupport extends DdsClipboardSupport<DdsFunctionDef> {

        public DdsFunctionClipboardSupport() {
            super(DdsFunctionDef.this);
        }

        @Override
        protected XmlObject copyToXml() {
            org.radixware.schemas.ddsdef.PlSqlFunction xFunction = org.radixware.schemas.ddsdef.PlSqlFunction.Factory.newInstance();
            DdsModelWriter.writeFunction(DdsFunctionDef.this, xFunction);
            return xFunction;
        }

        @Override
        protected DdsFunctionDef loadFrom(XmlObject xmlObject) {
            org.radixware.schemas.ddsdef.PlSqlFunction xFunction = (org.radixware.schemas.ddsdef.PlSqlFunction) xmlObject;
            return DdsFunctionDef.Factory.loadFrom(xFunction);
        }

        @Override
        protected Transfer<DdsFunctionDef> newTransferInstance() {
            return new DdsFunctionTransfer(radixObject);
        }

        @Override
        protected Method getDecoderMethod() {
            try {
                return DdsFunctionDef.Factory.class.getDeclaredMethod("loadFrom", org.radixware.schemas.ddsdef.PlSqlFunction.class);
            } catch (NoSuchMethodException | SecurityException ex) {
                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
            }
            return null;
        }
    }

    @Override
    public ClipboardSupport<? extends DdsFunctionDef> getClipboardSupport() {
        return new DdsFunctionClipboardSupport();
    }

    /**
     * @return true if ownet PL/SQL object contains prototype to this function
     * in its header.
     */
    public boolean isPublic() {
        final DdsPlSqlObjectDef plSqlObject = getOwnerPlSqlObject();
        if (plSqlObject != null) {
            for (DdsPlSqlObjectItemDef item : plSqlObject.getHeader().getItems()) {
                if (item instanceof DdsPrototypeDef) {
                    final DdsPrototypeDef prototype = (DdsPrototypeDef) item;
                    if (prototype.findFunction() == this) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Create or remove prototype to tis function in header of owner PL/SQL
     * object.
     */
    public void setPublic(boolean isPublic) {
        final DdsPlSqlObjectDef plSqlObject = getOwnerPlSqlObject();
        assert plSqlObject != null;

        if (isPublic() == isPublic) {
            return;
        }

        if (isPublic) {
            final DdsPrototypeDef prototype = DdsPrototypeDef.Factory.newInstance();
            final Id functionId = this.getId();
            prototype.setFunctionId(functionId);
            plSqlObject.getHeader().getItems().add(prototype);
        } else {
            for (DdsPlSqlObjectItemDef item : plSqlObject.getHeader().getItems().list()) {
                if (item instanceof DdsPrototypeDef) {
                    final DdsPrototypeDef prototype = (DdsPrototypeDef) item;
                    if (prototype.findFunction() == this) {
                        prototype.delete();
                    }
                }
            }
        }
    }
    public static final String FUNCTION_TYPE_TITLE = "Function";
    public static final String FUNCTION_TYPES_TITLE = "Functions";

    @Override
    public String getTypeTitle() {
        return FUNCTION_TYPE_TITLE;
    }

    @Override
    public String getTypesTitle() {
        return FUNCTION_TYPES_TITLE;
    }

    @Override
    public boolean delete() {
        final DdsPlSqlObjectDef ownerPlSqlObject = getOwnerPlSqlObject();

        for (DdsPlSqlObjectItemDef item : ownerPlSqlObject.getHeader().getItems()) {
            if (item instanceof DdsPrototypeDef) {
                final DdsPrototypeDef prototype = (DdsPrototypeDef) item;
                if (prototype.findFunction() == this) {
                    prototype.delete();
                }
            }
        }

        for (DdsPlSqlObjectItemDef item : ownerPlSqlObject.getBody().getItems()) {
            if (item instanceof DdsPrototypeDef) {
                final DdsPrototypeDef prototype = (DdsPrototypeDef) item;
                if (prototype.findFunction() == this) {
                    prototype.delete();
                }
            }
        }

        super.delete(); // after for 'getOwnerPlSqlObject()' and 'prototype.findFunction() == this';
        return true;
    }

    @Override
    public ENamingPolicy getNamingPolicy() {
        return ENamingPolicy.IDENTIFIER; // profile must be unique, not function name
    }

    @Override
    public EDocGroup getDocGroup() {
        return EDocGroup.PACKAGE_FUNC;
    }
}
