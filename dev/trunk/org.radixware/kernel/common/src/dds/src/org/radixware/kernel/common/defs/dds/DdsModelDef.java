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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.radixware.kernel.common.defs.ClipboardSupport;
import org.radixware.kernel.common.defs.ClipboardSupport.Transfer;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjectInitializationPolicy;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.dds.utils.TablespaceCalculator;
import org.radixware.kernel.common.defs.localization.ILocalizingBundleDef;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;

/**
 * <p>
 * DDS модель - метаинформация об объектах базы данных (таблицы, пакеты, и
 * т.д.).</p>
 * <p>
 * Полностью отражает структуру базы данных и дополняет ее различной
 * информацией. Имеет визуальное представление в виде диаграммы. На основании
 * модели генерируются скрипты создания и модификации базы данных, отображаются
 * списки для выбора объектов и прочее. Многие объекты вышестоящих слоев
 * ссылаются на объекты DDS модели (обычно через GUID), с обеспечением контроля
 * целостности.</p>
 * <p>
 * DDS модель представляет собой иерархическую структуру следующих объектов:</p>
 *
 * <ul>
 * <li>{@link DdsModelDef DDS модель}
 * <ul>
 * <li>{@link DdsColumnTemplateDef Домены}
 * <li>{@link DdsTableDef Таблицы}
 * <ul>
 * <li>{@link DdsColumnDef Колонки}
 * <li>{@link DdsIndexDef Индексы}
 * <li>{@link DdsTriggerDef Триггеры}
 * </ul>
 * <li>{@link DdsViewDef Представления таблиц (DdsViewDef)}
 * <ul>
 * <li>{@link DdsColumnDef Колонки}
 * <li>{@link DdsIndexDef Индексы}
 * <li>{@link DdsTriggerDef Триггеры}
 * </ul>
 * <li>{@link DdsExtTableDef Ссылки на таблицы других моделей}
 * <li>{@link DdsReferenceDef Cвязи между таблицами}
 * <li>{@link DdsSequenceDef Последовательности}
 * <li>{@link DdsPackageDef Пакеты}
 * <ul>
 * <li>{@link DdsPlSqlObjectDef#getHeader() Заголовок пакета}- список
 * {@link DdsPrototypeDef прототипов функций}, {@link DdsCustomTextDef произвольного текста}
 * <li>{@link DdsPlSqlObjectDef#getBody() Тело пакета}- список
 * {@link DdsPrototypeDef прототипов функций}, {@link DdsFunctionDef функций}, {@link DdsCustomTextDef произвольного текста}
 * </ul>
 * <li>{@link DdsTypeDef Типы}
 * <ul>
 * <li>{@link DdsPlSqlObjectDef#getHeader() Заголовок типа}- список
 * {@link DdsPrototypeDef прототипов функций}, {@link DdsCustomTextDef произвольного текста}
 * <li>{@link DdsPlSqlObjectDef#getBody() Тело типа}- список
 * {@link DdsPrototypeDef прототипов функций}, {@link DdsFunctionDef функций}, {@link DdsCustomTextDef произвольного текста}
 * </ul>
 * <li>{@link DdsAccessPartitionFamily Семейства партиций доступа}
 * <li>История {@link DdsUpgrade Обновлений}
 * <li>{@link DdsLabelDef Надписи}
 * </ul>
 * </ul>
 *
 */
public class DdsModelDef extends DdsDefinition {

    public static final String MODEL_TYPE_TITLE = "Model";
    public static final String MODEL_TYPES_TITLE = "Models";

    protected DdsModelDef(Id id) {
        super(id, "");
    }

    @Override
    public String getName() {
        RadixObject owner = getModule();
        if (owner != null) {
            return owner.getName();
        } else {
            return MODEL_TYPE_TITLE;
        }
    }

    @Override
    public boolean setName(String name) {
        throw new UnsupportedOperationException("Attempt to set DDS model name");
    }

    @Override
    public void setDescription(String description) {
        throw new UnsupportedOperationException("Attempt to set DDS model description");
    }

    private class DdsApfs extends DdsDefinitions<DdsAccessPartitionFamilyDef> {

        public DdsApfs() {
            super(DdsModelDef.this);
        }

        @Override
        protected ClipboardSupport.CanPasteResult canPaste(Transfer objectInClipboard) {
            ClipboardSupport.CanPasteResult result = super.canPaste(objectInClipboard);
            if (result != ClipboardSupport.CanPasteResult.YES) {
                return result;
            }

            if (objectInClipboard.getObject() instanceof DdsAccessPartitionFamilyDef) {
                return !this.contains((DdsAccessPartitionFamilyDef) objectInClipboard.getObject()) ? ClipboardSupport.CanPasteResult.YES : ClipboardSupport.CanPasteResult.NO;
            } else {
                return ClipboardSupport.CanPasteResult.NO;
            }
        }

        @Override
        public RadixIcon getIcon() {
            return DdsDefinitionIcon.ACCESS_PARTITION_FAMILY;
        }

        @Override
        public String getName() {
            return DdsAccessPartitionFamilyDef.APF_TYPES_TITLE;
        }
    }
    private final DdsDefinitions<DdsAccessPartitionFamilyDef> apfs = new DdsApfs();

    /**
     * Получить список семейств партиций доступа модели.
     *
     * @return список семейств партиций доступа.
     */
    public DdsDefinitions<DdsAccessPartitionFamilyDef> getAccessPartitionFamilies() {
        return apfs;
    }

    private class DdsSequences extends DdsDefinitions<DdsSequenceDef> {

        public DdsSequences() {
            super(DdsModelDef.this);
        }

        @Override
        protected ClipboardSupport.CanPasteResult canPaste(Transfer objectInClipboard) {
            ClipboardSupport.CanPasteResult result = super.canPaste(objectInClipboard);
            if (result != ClipboardSupport.CanPasteResult.YES) {
                return result;
            }

            if (objectInClipboard.getObject() instanceof DdsSequenceDef) {
                return !this.contains((DdsSequenceDef) objectInClipboard.getObject()) ? ClipboardSupport.CanPasteResult.YES : ClipboardSupport.CanPasteResult.NO;
            } else {
                return ClipboardSupport.CanPasteResult.NO;
            }
        }

        @Override
        public RadixIcon getIcon() {
            return DdsDefinitionIcon.SEQUENCE;
        }

        @Override
        public String getName() {
            return DdsSequenceDef.SEQUENCE_TYPES_TITLE;
        }
    }
    private final DdsDefinitions<DdsSequenceDef> sequences = new DdsSequences();

    /**
     * Get list of sequences in the model.
     */
    public DdsDefinitions<DdsSequenceDef> getSequences() {
        return sequences;
    }

    /**
     * Database attributes of the model.
     */
    public class DbAttributes extends RadixObject {

        @Override
        public String getName() {
            return DB_ATTRIBUTES_TYPE_TITLE;
        }
        public static final String DB_ATTRIBUTES_TYPE_TITLE = "Database Attributes";
        public static final String DB_ATTRIBUTES_TYPES_TITLE = "Databases Attributes";

        @Override
        public RadixIcon getIcon() {
            return DdsDefinitionIcon.DATABASE_ATTRIBUTES;
        }

        public DdsModelDef getOwnerModel() {
            return (DdsModelDef) getContainer();
        }
        private String defaultTablespaceForIndices = "";

        /**
         * Default tablespace database name for all indices whole the model. См.
         * {@link TablespaceCalculator}.
         *
         * @return default tablespace database name for indices or null or empty
         * string if value is not defined.
         */
        public String getDefaultTablespaceForIndices() {
            return defaultTablespaceForIndices;
        }

        public void setDefaultTablespaceForIndices(String defaultTablespaceForIndices) {
            if (!Utils.equals(this.defaultTablespaceForIndices, defaultTablespaceForIndices)) {
                this.defaultTablespaceForIndices = defaultTablespaceForIndices;
                setEditState(EEditState.MODIFIED);
            }
        }
        private String defaultTablespaceForTables = "";

        /**
         * Default tablespace database name for all tables whole the model. См.
         * {@link TablespaceCalculator}.
         *
         * @return default tablespace database name for tables or null or empty
         * string if value is not defined.
         */
        public String getDefaultTablespaceForTables() {
            return defaultTablespaceForTables;
        }

        public void setDefaultTablespaceForTables(String defaultTablespaceForTables) {
            if (!Utils.equals(this.defaultTablespaceForTables, defaultTablespaceForTables)) {
                this.defaultTablespaceForTables = defaultTablespaceForTables;
                setEditState(EEditState.MODIFIED);
            }
        }
        private String dbNamePrefix = "";

        /**
         * Получить префикс имен в базе данных для некоторых типов объектов
         * модели, например, {@link DdsTableDef таблиц}. Используется для
         * автоматической генерации имен объектов в базе данных. Помогает
         * избежать проблему совпадения имен объектов в базе данных между
         * объектами разных моделей. Обычно в качестве значения выступает
         * трехбуквенная абревиатура имени модели.
         */
        public String getDbNamePrefix() {
            return dbNamePrefix;
        }

        public void setDbNamePrefix(String dbNamePrefix) {
            if (!Utils.equals(this.dbNamePrefix, dbNamePrefix)) {
                this.dbNamePrefix = dbNamePrefix;
                setEditState(EEditState.MODIFIED);
            }
        }

        protected DbAttributes() {
            super();
            this.setContainer(DdsModelDef.this);
        }

        protected DbAttributes(org.radixware.schemas.ddsdef.ModelDocument.Model xModel) {
            this();
            loadFrom(xModel);
        }

        protected void loadFrom(org.radixware.schemas.ddsdef.ModelDocument.Model xModel) {
            if (xModel.isSetDbNamePrefix()) {
                this.dbNamePrefix = xModel.getDbNamePrefix();
            }

            if (xModel.isSetTablespaceForIndices()) {
                this.defaultTablespaceForIndices = xModel.getTablespaceForIndices();
            }

            if (xModel.isSetTablespaceForTables()) {
                this.defaultTablespaceForTables = xModel.getTablespaceForTables();
            }
        }
    }
    private final DbAttributes dbAttributes = new DbAttributes();

    /**
     * Get database attributes of the model.
     *
     * @return database attributes.
     */
    public DbAttributes getDbAttributes() {
        return dbAttributes;
    }

    private class DdsLabels extends DdsDefinitions<DdsLabelDef> {

        public DdsLabels() {
            super(DdsModelDef.this);
        }

        @Override
        protected ClipboardSupport.CanPasteResult canPaste(Transfer objectInClipboard) {
            ClipboardSupport.CanPasteResult result = super.canPaste(objectInClipboard);
            if (result != ClipboardSupport.CanPasteResult.YES) {
                return result;
            }

            if (objectInClipboard.getObject() instanceof DdsLabelDef) {
                return !this.contains((DdsLabelDef) objectInClipboard.getObject()) ? ClipboardSupport.CanPasteResult.YES : ClipboardSupport.CanPasteResult.NO;
            } else {
                return ClipboardSupport.CanPasteResult.NO;
            }
        }

        @Override
        public RadixIcon getIcon() {
            return DdsDefinitionIcon.LABEL;
        }

        @Override
        public String getName() {
            return DdsLabelDef.LABEL_TYPES_TITLE;
        }
    }
    private final DdsDefinitions<DdsLabelDef> labels = new DdsLabels();

    /**
     * Get list of labels in the model.
     */
    public DdsDefinitions<DdsLabelDef> getLabels() {
        return labels;
    }

    private class DdsColumnTemplates extends DdsDefinitions<DdsColumnTemplateDef> {

        public DdsColumnTemplates() {
            super(DdsModelDef.this);
        }

        @Override
        protected ClipboardSupport.CanPasteResult canPaste(Transfer objectInClipboard) {
            ClipboardSupport.CanPasteResult result = super.canPaste(objectInClipboard);
            if (result != ClipboardSupport.CanPasteResult.YES) {
                return result;
            }

            if (objectInClipboard.getObject() instanceof DdsColumnTemplateDef) {
                return !this.contains((DdsColumnTemplateDef) objectInClipboard.getObject()) ? ClipboardSupport.CanPasteResult.YES : ClipboardSupport.CanPasteResult.NO;
            } else {
                return ClipboardSupport.CanPasteResult.NO;
            }
        }

        @Override
        public RadixIcon getIcon() {
            return DdsDefinitionIcon.COLUMN_TEMPLATES;
        }

        @Override
        public String getName() {
            return DdsColumnTemplateDef.COLUMN_TEMPLATE_TYPES_TITLE;
        }
    }
    private final DdsDefinitions<DdsColumnTemplateDef> columnTemplates = new DdsColumnTemplates();

    /**
     * Get list of column templates in the model.
     */
    public DdsDefinitions<DdsColumnTemplateDef> getColumnTemplates() {
        return columnTemplates;
    }

    private class DdsExtTables extends DdsDefinitions<DdsExtTableDef> {

        public DdsExtTables() {
            super(DdsModelDef.this);
        }

        @Override
        protected ClipboardSupport.CanPasteResult canPaste(Transfer objectInClipboard) {
            ClipboardSupport.CanPasteResult result = super.canPaste(objectInClipboard);
            if (result != ClipboardSupport.CanPasteResult.YES) {
                return result;
            }

            if (objectInClipboard.getObject() instanceof DdsExtTableDef) {
                return !this.contains((DdsExtTableDef) objectInClipboard.getObject()) ? ClipboardSupport.CanPasteResult.YES : ClipboardSupport.CanPasteResult.NO;
            } else {
                return ClipboardSupport.CanPasteResult.NO;
            }
        }

        @Override
        public RadixIcon getIcon() {
            return DdsDefinitionIcon.EXT_TABLE;
        }

        @Override
        public String getName() {
            return DdsExtTableDef.EXT_TABLE_TYPES_TITLE;
        }
    }
    private final DdsDefinitions<DdsExtTableDef> extTables = new DdsExtTables();

    /**
     * Get list of external tables in the model.
     */
    public DdsDefinitions<DdsExtTableDef> getExtTables() {
        return extTables;
    }

    private class DdsPackages extends DdsDefinitions<DdsPackageDef> {

        public DdsPackages() {
            super(DdsModelDef.this);
        }

        @Override
        protected ClipboardSupport.CanPasteResult canPaste(Transfer objectInClipboard) {
            ClipboardSupport.CanPasteResult result = super.canPaste(objectInClipboard);
            if (result != ClipboardSupport.CanPasteResult.YES) {
                return result;
            }

            if (objectInClipboard.getObject() instanceof DdsPackageDef) {
                return !this.contains((DdsPackageDef) objectInClipboard.getObject()) ? ClipboardSupport.CanPasteResult.YES : ClipboardSupport.CanPasteResult.NO;
            } else {
                return ClipboardSupport.CanPasteResult.NO;
            }
        }

        @Override
        public RadixIcon getIcon() {
            return DdsDefinitionIcon.PACKAGE;
        }

        @Override
        public String getName() {
            return DdsPackageDef.PACKAGE_TYPES_TITLE;
        }
    }
    private final DdsDefinitions<DdsPackageDef> packages = new DdsPackages();

    /**
     * Get list of packages in the model.
     */
    public DdsDefinitions<DdsPackageDef> getPackages() {
        return packages;
    }

    private final class DdsReferences extends DdsDefinitions<DdsReferenceDef> {

        public DdsReferences() {
            super(DdsModelDef.this);
        }

        @Override
        protected ClipboardSupport.CanPasteResult canPaste(Transfer objectInClipboard) {
            ClipboardSupport.CanPasteResult result = super.canPaste(objectInClipboard);
            if (result != ClipboardSupport.CanPasteResult.YES) {
                return result;
            }

            if (objectInClipboard.getObject() instanceof DdsReferenceDef) {
                return !this.contains((DdsReferenceDef) objectInClipboard.getObject()) ? ClipboardSupport.CanPasteResult.YES : ClipboardSupport.CanPasteResult.NO;
            } else {
                return ClipboardSupport.CanPasteResult.NO;
            }
        }

        @Override
        public RadixIcon getIcon() {
            return DdsDefinitionIcon.REFERENCE;
        }

        @Override
        public String getName() {
            return DdsReferenceDef.REFERENCE_TYPES_TITLE;
        }
    }
    private final DdsDefinitions<DdsReferenceDef> references = new DdsReferences();

    /**
     * Получить список связей между таблицами модели.
     */
    public DdsDefinitions<DdsReferenceDef> getReferences() {
        return references;
    }

    /**
     * Получить начальный SQML скрипт для модели. Используется для генерации
     * скриптов создания базы данных проекта. Начальные скрипты всех моделей
     * проекта выполняются перед скриптами создания таблиц, связей и т.п.
     *
     * @return конечный скрипт модели.
     */
    private class BeginEndScript extends DdsSqml {

        BeginEndScript() {
            super(DdsModelDef.this);
        }
    }
    private final DdsSqml beginScript = new BeginEndScript();

    public Sqml getBeginScript() {
        return beginScript;
    }
    private final DdsSqml endScript = new BeginEndScript();

    /**
     * Получить конечный SQML скрипт для модели. Используется для генерации
     * скриптов создания базы данных проекта. Конечные скрипты всех моделей
     * проекта выполняются после скриптов создания таблиц, связей и т.п.
     *
     * @return конечный скрипт модели.
     */
    public Sqml getEndScript() {
        return endScript;
    }

    private class DdsTables extends DdsDefinitions<DdsTableDef> {

        public DdsTables() {
            super(DdsModelDef.this);
        }

        @Override
        protected ClipboardSupport.CanPasteResult canPaste(Transfer objectInClipboard) {
            ClipboardSupport.CanPasteResult result = super.canPaste(objectInClipboard);
            if (result != ClipboardSupport.CanPasteResult.YES) {
                return result;
            }

            if ((objectInClipboard.getObject() instanceof DdsTableDef) && !(objectInClipboard.getObject() instanceof DdsViewDef)) {
                return !this.contains((DdsTableDef) objectInClipboard.getObject()) ? ClipboardSupport.CanPasteResult.YES : ClipboardSupport.CanPasteResult.NO;
            } else {
                return ClipboardSupport.CanPasteResult.NO;
            }
        }

        @Override
        public RadixIcon getIcon() {
            return DdsDefinitionIcon.TABLE;
        }

        @Override
        public String getName() {
            return DdsTableDef.TABLE_TYPES_TITLE;
        }
    }
    private final DdsDefinitions<DdsTableDef> tables = new DdsTables();

    /**
     * Получить список таблиц модели.
     */
    public DdsDefinitions<DdsTableDef> getTables() {
        return tables;
    }

    private class DdsTypes extends DdsDefinitions<DdsTypeDef> {

        public DdsTypes() {
            super(DdsModelDef.this);
        }

        @Override
        protected ClipboardSupport.CanPasteResult canPaste(Transfer objectInClipboard) {
            ClipboardSupport.CanPasteResult result = super.canPaste(objectInClipboard);
            if (result != ClipboardSupport.CanPasteResult.YES) {
                return result;
            }

            if (objectInClipboard.getObject() instanceof DdsTypeDef) {
                return !this.contains((DdsTypeDef) objectInClipboard.getObject()) ? ClipboardSupport.CanPasteResult.YES : ClipboardSupport.CanPasteResult.NO;
            } else {
                return ClipboardSupport.CanPasteResult.NO;
            }
        }

        @Override
        public RadixIcon getIcon() {
            return DdsDefinitionIcon.TYPE;
        }

        @Override
        public String getName() {
            return DdsTypeDef.TYPE_TYPES_TITLE;
        }
    }
    private final DdsDefinitions<DdsTypeDef> types = new DdsTypes();

    /**
     * Get list of types in the model.
     */
    public DdsDefinitions<DdsTypeDef> getTypes() {
        return types;
    }

    private class DdsViews extends DdsDefinitions<DdsViewDef> {

        public DdsViews() {
            super(DdsModelDef.this);
        }

        @Override
        protected ClipboardSupport.CanPasteResult canPaste(Transfer objectInClipboard) {
            ClipboardSupport.CanPasteResult result = super.canPaste(objectInClipboard);
            if (result != ClipboardSupport.CanPasteResult.YES) {
                return result;
            }

            if (objectInClipboard.getObject() instanceof DdsViewDef) {
                return !this.contains((DdsViewDef) objectInClipboard.getObject()) ? ClipboardSupport.CanPasteResult.YES : ClipboardSupport.CanPasteResult.NO;
            } else {
                return ClipboardSupport.CanPasteResult.NO;
            }
        }

        @Override
        public RadixIcon getIcon() {
            return DdsDefinitionIcon.VIEW;
        }

        @Override
        public String getName() {
            return DdsViewDef.VIEW_TYPES_TITLE;
        }
    }
    private final DdsDefinitions<DdsViewDef> views = new DdsViews();

    /**
     * Get list of view in the model.
     */
    public DdsDefinitions<DdsViewDef> getViews() {
        return views;
    }

    /**
     * Save model into the specified xml node.
     */
    public void appendTo(org.radixware.schemas.ddsdef.ModelDocument.Model xModel) {
        appendTo(xModel, false);

    }

    protected void appendTo(org.radixware.schemas.ddsdef.ModelDocument.Model xModel, boolean forceModifiedMode) {
        DdsModelWriter.writeModel(this, xModel, forceModifiedMode);
    }

    /**
     * Save model into the specified file.
     *
     * @throws IOException
     */
    public void appendTo(File file) throws IOException {
        DdsModelWriter.writeModel(this, file);
    }

    protected void setOwnerModule(DdsModule ownerModule) {
        setContainer(ownerModule);
    }

    /**
     * Найти PL/SQL функцию по идентификатору. Поиск осуществляется в пределах
     * модели.
     *
     * @return function or null if not found.
     */
    public DdsFunctionDef findFunctionById(Id plSqlFuncId) {
        for (DdsPackageDef pkg : packages) {
            DdsPlSqlObjectItemDef item = pkg.getBody().getItems().findById(plSqlFuncId);
            if (item instanceof DdsFunctionDef) {
                return (DdsFunctionDef) item;
            }
        }
        for (DdsTypeDef type : types) {
            DdsPlSqlObjectItemDef item = type.getBody().getItems().findById(plSqlFuncId);
            if (item instanceof DdsFunctionDef) {
                return (DdsFunctionDef) item;
            }
        }
        return null;
    }

    /**
     * Найти PL/SQL функцию по идентификатору. Поиск осуществляется в пределах
     * модели.
     *
     * @throws DefinitionNotFoundError
     */
    public DdsFunctionDef getFunctionById(Id plSqlFuncId) {
        DdsFunctionDef func = findFunctionById(plSqlFuncId);
        if (func == null) {
            throw new DefinitionNotFoundError(plSqlFuncId);
        }
        return func;
    }
//    private EDdsModelEditMode editMode = EDdsModelEditMode.FIXED;
//
//    /**
//     * Флаг, что модель находится только в режиме просмотра.
//     * Модель, открытая из файла model.xml находится в режиме просмотра.
//     * Модель, открытая из файла model_modified.xml находится в режиме редактирования.
//     * @return true, если модель находится в режиме просмотра.
//     */
//    public EDdsModelEditMode getEditMode() {
//        return editMode;
//    }
//
//    void setEditMode(EDdsModelEditMode editMode) {
//        final EDdsModelEditMode oldEditMode = this.editMode;
//        if (oldEditMode != editMode) {
//            this.editMode = editMode;
//            if (editModeChangedSupport != null) {
//                editModeChangedSupport.fireEvent(new EditModeChangedEvent(this, oldEditMode, editMode));
//            }
//        }
//    }
//
//    public static class EditModeChangedEvent extends RadixEvent {
//
//        public final DdsModelDef model;
//        public final EDdsModelEditMode oldEditMode;
//        public final EDdsModelEditMode newEditMode;
//
//        public EditModeChangedEvent(DdsModelDef model, EDdsModelEditMode oldEditMode, EDdsModelEditMode newEditMode) {
//            this.model = model;
//            this.oldEditMode = oldEditMode;
//            this.newEditMode = newEditMode;
//        }
//    }
//
//    public interface EditModeChangeListener extends IRadixEventListener<EditModeChangedEvent> {
//    }
//
//    public class EditModeSupport extends RadixEventSource<EditModeChangeListener, EditModeChangedEvent> {
//    }
//    private EditModeSupport editModeChangedSupport = null;
//
//    public synchronized EditModeSupport getEditModeSupport() {
//        if (editModeChangedSupport == null) {
//            editModeChangedSupport = new EditModeSupport();
//        }
//        return editModeChangedSupport;
//    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        this.getAccessPartitionFamilies().visit(visitor, provider);
        this.getColumnTemplates().visit(visitor, provider);
        this.getSequences().visit(visitor, provider);
        this.getTables().visit(visitor, provider);
        this.getViews().visit(visitor, provider);
        this.getExtTables().visit(visitor, provider);
        this.getReferences().visit(visitor, provider);
        this.getTypes().visit(visitor, provider);
        this.getPackages().visit(visitor, provider);
        this.getLabels().visit(visitor, provider);
        this.beginScript.visit(visitor, provider);
        this.endScript.visit(visitor, provider);
        this.dbAttributes.visit(visitor, provider);
        this.getStringBundle().visit(visitor, provider);
    }

    @Override
    public RadixIcon getIcon() {
        return DdsDefinitionIcon.MODEL;
    }

    private class DdsModelClipboardSupport extends DdsClipboardSupport<DdsModelDef> {

        public DdsModelClipboardSupport() {
            super(DdsModelDef.this);
        }

        @Override
        public boolean canCopy() {
            return false; // it is not allowed to copy the whole model in Designer.
        }

        @Override
        protected XmlObject copyToXml() {
            org.radixware.schemas.ddsdef.ModelDocument.Model xModel = org.radixware.schemas.ddsdef.ModelDocument.Model.Factory.newInstance();
            DdsModelWriter.writeModel(DdsModelDef.this, xModel);
            return xModel;
        }

        @Override
        protected DdsModelDef loadFrom(XmlObject xmlObject) {
            org.radixware.schemas.ddsdef.ModelDocument xModelDocument = (org.radixware.schemas.ddsdef.ModelDocument) xmlObject;
            return DdsModelDef.Factory.loadFrom(xModelDocument.getModel());
        }

        @Override
        public CanPasteResult canPaste(List<Transfer> objectsInClipboard, DuplicationResolver resolver) {
            for (Transfer objectInClipboard : objectsInClipboard) {
                if (!(objectInClipboard.getObject() instanceof DdsDefinition)) {
                    return CanPasteResult.NO;
                }

                List<Transfer> objectInClipboardAsList = Collections.singletonList(objectInClipboard);
                boolean isSupportedClass
                        = getAccessPartitionFamilies().canPaste(objectInClipboardAsList, resolver) == CanPasteResult.YES
                        || getColumnTemplates().canPaste(objectInClipboardAsList, resolver) == CanPasteResult.YES
                        || getExtTables().canPaste(objectInClipboardAsList, resolver) == CanPasteResult.YES
                        || getLabels().canPaste(objectInClipboardAsList, resolver) == CanPasteResult.YES
                        || getPackages().canPaste(objectInClipboardAsList, resolver) == CanPasteResult.YES
                        || getTypes().canPaste(objectInClipboardAsList, resolver) == CanPasteResult.YES
                        || getReferences().canPaste(objectInClipboardAsList, resolver) == CanPasteResult.YES
                        || getSequences().canPaste(objectInClipboardAsList, resolver) == CanPasteResult.YES
                        || getTables().canPaste(objectInClipboardAsList, resolver) == CanPasteResult.YES
                        || getViews().canPaste(objectInClipboardAsList, resolver) == CanPasteResult.YES;

                if (!isSupportedClass) {
                    return CanPasteResult.NO;
                }
            }

            return CanPasteResult.YES;
        }

        @Override
        public void paste(List<Transfer> objectsInClipboard, DuplicationResolver resolver) {
            checkForCanPaste(objectsInClipboard, resolver);

            for (Transfer objectInClipboard : objectsInClipboard) {
                List<Transfer> objectInClipboardAsList = Collections.singletonList(objectInClipboard);

                if (objectInClipboard.getObject() instanceof DdsAccessPartitionFamilyDef) {
                    getAccessPartitionFamilies().getClipboardSupport().paste(objectInClipboardAsList, resolver);
                } else if (objectInClipboard.getObject() instanceof DdsColumnTemplateDef) {
                    getColumnTemplates().getClipboardSupport().paste(objectInClipboardAsList, resolver);
                } else if (objectInClipboard.getObject() instanceof DdsExtTableDef) {
                    getExtTables().getClipboardSupport().paste(objectInClipboardAsList, resolver);
                } else if (objectInClipboard.getObject() instanceof DdsLabelDef) {
                    getLabels().getClipboardSupport().paste(objectInClipboardAsList, resolver);
                } else if (objectInClipboard.getObject() instanceof DdsPackageDef) {
                    getPackages().getClipboardSupport().paste(objectInClipboardAsList, resolver);
                } else if (objectInClipboard.getObject() instanceof DdsViewDef) {
                    getViews().getClipboardSupport().paste(objectInClipboardAsList, resolver);
                } else if (objectInClipboard.getObject() instanceof DdsTableDef) {
                    final DdsTableDef table = (DdsTableDef) objectInClipboard.getObject();
                    for (DdsTriggerDef trigger : table.getTriggers().getLocal()) {
                        if (trigger.getType() == DdsTriggerDef.EType.FOR_AUDIT) {
                            trigger.delete();
                        }
                    }
                    getTables().getClipboardSupport().paste(objectInClipboardAsList, resolver);
                    AuditTriggersUpdater.update(table);
                } else if (objectInClipboard.getObject() instanceof DdsTypeDef) {
                    getTypes().getClipboardSupport().paste(objectInClipboardAsList, resolver);
                } else if (objectInClipboard.getObject() instanceof DdsReferenceDef) {
                    getReferences().getClipboardSupport().paste(objectInClipboardAsList, resolver);
                } else if (objectInClipboard.getObject() instanceof DdsSequenceDef) {
                    getSequences().getClipboardSupport().paste(objectInClipboardAsList, resolver);
                }
            }
        }
    }

    @Override
    public boolean canDelete() {
        return false;
    }

    @Override
    public ClipboardSupport<DdsModelDef> getClipboardSupport() {
        return new DdsModelClipboardSupport();
    }

    public DdsDefinitions[] getDiagramContainers() {
        return new DdsDefinitions[]{
            getTables(),
            getExtTables(),
            getViews(),
            getSequences(),
            getLabels(),
            getReferences()};
    }

    @Override
    public File getFile() {
        final DdsModule module = getModule();
        if (module != null) {
            return module.getModelManager().findModelFile(this);
        } else {
            return null;
        }
    }

    @Override
    public boolean isSaveable() {
        return true;
    }

    protected void beginEnterModificationState() throws IOException {
        getStringBundle();
    }

    protected void enterModificationState() throws IOException {
        save();
    }

    protected void enterFixedState() throws IOException {

        synchronized (this) {
            if (stringBundle != null) {
                stringBundle.save();
            }
        }
        save();
    }

    @Override
    public void save() throws IOException {
        final File file = getFile();
        assert file != null;

        synchronized (this) {
            this.appendTo(file);
            fileLastModifiedTime = file.lastModified();
        }

        setEditState(EEditState.NONE);

        getModule().saveDirectoryXml(); // update model.xml digist
    }
    long fileLastModifiedTime = 0L;

    public long getFileLastModifiedTime() {
        synchronized (this) {
            return fileLastModifiedTime;
        }
    }

    @Override
    public String getTypeTitle() {
        return MODEL_TYPE_TITLE;
    }

    @Override
    public String getTypesTitle() {
        return MODEL_TYPES_TITLE;
    }

    public void loadFrom(org.radixware.schemas.ddsdef.ModelDocument.Model xModel) {
        // name, description is not required, because not xModel is not NamedDefinition.

        this.dbAttributes.loadFrom(xModel);
        if (!RadixObjectInitializationPolicy.get().isRuntime()) {
            this.beginScript.loadFrom(xModel.getBeginScript());
            this.endScript.loadFrom(xModel.getEndScript());
        }

        this.apfs.clear();
        if (xModel.isSetAccessPartitionFamilies()) {
            final List<org.radixware.schemas.ddsdef.AccessPartitionFamily> xApfs = xModel.getAccessPartitionFamilies().getAccessPartitionFamilyList();
            for (org.radixware.schemas.ddsdef.AccessPartitionFamily xApf : xApfs) {
                final DdsAccessPartitionFamilyDef apf = DdsAccessPartitionFamilyDef.Factory.loadFrom(xApf);
                this.apfs.add(apf);
            }
        }

        this.extTables.clear();
        if (xModel.isSetExtTables()) {
            final List<org.radixware.schemas.ddsdef.ExtTable> xmlExtTables = xModel.getExtTables().getExtTableList();
            for (org.radixware.schemas.ddsdef.ExtTable xmlExtTable : xmlExtTables) {
                final DdsExtTableDef extTable = DdsExtTableDef.Factory.loadFrom(xmlExtTable);
                this.extTables.add(extTable);
            }
        }

        this.labels.clear();
        if (xModel.isSetLabels()) {
            final List<org.radixware.schemas.ddsdef.Label> xmlLabels = xModel.getLabels().getLabelList();
            for (org.radixware.schemas.ddsdef.Label xmlLabel : xmlLabels) {
                final DdsLabelDef label = DdsLabelDef.Factory.loadFrom(xmlLabel);
                this.labels.add(label);
            }
        }

        this.packages.clear();
        if (xModel.isSetPackages()) {
            final List<org.radixware.schemas.ddsdef.Package> xmlPkgs = xModel.getPackages().getPackageList();
            for (org.radixware.schemas.ddsdef.Package xmlPkg : xmlPkgs) {
                final DdsPackageDef pkg = DdsPackageDef.Factory.loadFrom(xmlPkg);
                this.packages.add(pkg);
            }
        }

        this.references.clear();
        if (xModel.isSetReferences()) {
            final List<org.radixware.schemas.ddsdef.Reference> xmlReferences = xModel.getReferences().getReferenceList();
            for (org.radixware.schemas.ddsdef.Reference xmlReference : xmlReferences) {
                final DdsReferenceDef reference = DdsReferenceDef.Factory.loadFrom(xmlReference);
                this.references.add(reference);
            }
        }

        this.sequences.clear();
        if (xModel.isSetSequences()) {
            final List<org.radixware.schemas.ddsdef.Sequence> xmlSequences = xModel.getSequences().getSequenceList();
            for (org.radixware.schemas.ddsdef.Sequence xmlSequence : xmlSequences) {
                final DdsSequenceDef sequence = DdsSequenceDef.Factory.loadFrom(xmlSequence);
                this.sequences.add(sequence);
            }
        }

        this.columnTemplates.clear();
        if (xModel.isSetColumnTemplates()) {
            final List<org.radixware.schemas.ddsdef.ColumnTemplate> xmlColumnTemplates = xModel.getColumnTemplates().getColumnTemplateList();
            for (org.radixware.schemas.ddsdef.ColumnTemplate xmlColumnTemplate : xmlColumnTemplates) {
                final DdsColumnTemplateDef columnTemplate = DdsColumnTemplateDef.Factory.loadFrom(xmlColumnTemplate);
                this.columnTemplates.add(columnTemplate);
            }
        }

        this.tables.clear();
        if (xModel.isSetTables()) {
            final List<org.radixware.schemas.ddsdef.Table> xmlTables = xModel.getTables().getTableList();
            for (org.radixware.schemas.ddsdef.Table xmlTable : xmlTables) {
                final DdsTableDef table = DdsTableDef.Factory.loadFrom(xmlTable);
                this.tables.add(table);
            }
        }

        this.types.clear();
        if (xModel.isSetTypes()) {
            final List<org.radixware.schemas.ddsdef.Type> xmlTypes = xModel.getTypes().getTypeList();
            for (org.radixware.schemas.ddsdef.Type xmlType : xmlTypes) {
                final DdsTypeDef type = DdsTypeDef.Factory.loadFrom(xmlType);
                this.types.add(type);
            }
        }

        this.views.clear();
        if (xModel.isSetViews()) {
            final List<org.radixware.schemas.ddsdef.View> xmlViews = xModel.getViews().getViewList();
            for (org.radixware.schemas.ddsdef.View xmlView : xmlViews) {
                final DdsViewDef view = DdsViewDef.Factory.loadFrom(xmlView);
                this.views.add(view);
            }
        }

        this.modifierInfo.loadFrom(xModel);

        if (xModel.isSetStringBundle()) {
            synchronized (this) {
                this.stringBundle = new DdsLocalizingBundleDef(this);
                this.stringBundle.loadFrom(xModel.getStringBundle());
            }
        }

        setEditState(EEditState.NONE);
    }

    public static final class Factory {

        private Factory() {
        }

        public static DdsModelDef newInstance(DdsModule module) {
            final Id id = Id.Factory.changePrefix(module.getId(), EDefinitionIdPrefix.DDS_MODEL);
            return new DdsModelDef(id);
        }

        public static DdsModelDef newTemporaryInstance(DdsModelDef src) {
            org.radixware.schemas.ddsdef.ModelDocument.Model xModel = org.radixware.schemas.ddsdef.ModelDocument.Model.Factory.newInstance();
            DdsModelWriter.writeModel(src, xModel);
            DdsModelDef result = loadFrom(xModel);
            result.fileLastModifiedTime = src.fileLastModifiedTime;
            result.isTemporary = true;
            return result;
        }

        public static DdsModelDef newInstance(DdsModelDef src) {
            org.radixware.schemas.ddsdef.ModelDocument.Model xModel = org.radixware.schemas.ddsdef.ModelDocument.Model.Factory.newInstance();
            DdsModelWriter.writeModel(src, xModel);
            DdsModelDef result = loadFrom(xModel);
            result.fileLastModifiedTime = src.fileLastModifiedTime;
            return result;
        }

        /**
         * @throws DefinitionError
         */
        public static DdsModelDef loadFrom(org.radixware.schemas.ddsdef.ModelDocument.Model xModel) {
            final Id modelId = xModel.getId();
            final DdsModelDef model = new DdsModelDef(modelId);
            model.loadFrom(xModel);
            return model;
        }

        @Deprecated
        private void validate(final org.radixware.schemas.ddsdef.ModelDocument xModelDocument) throws XmlException {
            XmlOptions validateOptions = new XmlOptions();
            ArrayList<org.apache.xmlbeans.XmlError> errorList = new ArrayList<XmlError>();
            validateOptions.setErrorListener(errorList);
            if (!xModelDocument.validate(validateOptions)) {
                throw new XmlException(errorList.get(0).getMessage());
            }
        }

        /**
         * Load DDS model from XML file.
         *
         * @throws IOException
         */
        public static DdsModelDef loadFrom(File file) throws IOException {
            assert (file != null);

            final String ERROR_MESS = "Unable to load DDS model from " + String.valueOf(file);
            final org.radixware.schemas.ddsdef.ModelDocument.Model xModel;
            final long fileTime = file.lastModified();
            try {
                xModel = org.radixware.schemas.ddsdef.ModelDocument.Factory.parse(file).getModel();
            } catch (XmlException e) {
                throw new IOException(ERROR_MESS, e);
            } catch (IllegalArgumentException e) {
                throw new IOException(ERROR_MESS, e);
            } catch (RadixError e) {
                throw new IOException(ERROR_MESS, e);
            }

            final DdsModelDef model = loadFrom(xModel);
            model.fileLastModifiedTime = fileTime;
            return model;
        }

        /**
         * Load DDS model from input stream.
         *
         * @throws IOException
         */
        public static DdsModelDef loadFrom(InputStream inputStream) throws XmlException, IOException {
            final org.radixware.schemas.ddsdef.ModelDocument.Model xModel = org.radixware.schemas.ddsdef.ModelDocument.Factory.parse(inputStream).getModel();
            return loadFrom(xModel);
        }
    }

    @Override
    public boolean isReadOnly() {
        if (super.isReadOnly()) {
            return true;
        }

        final DdsModule module = getModule();
        if (module == null) {
            return false;
        }

        if (!isInBranch()) {
            return false;
        }

        if (!getModifierInfo().isOwn()) {
            return true;
        }

        if (module.getModelManager().getFixedModelIfLoaded() == this) {
            return true;
        }

        return false;
    }

    public class ModifierInfo {

        private String editor = null;
        private String station = null;
        private String filePath = null;

        protected ModifierInfo() {
        }

        private String getSystemUserName() {
            return System.getProperty("user.name");
        }

        private String getSystemStation() {
            try {
                return java.net.InetAddress.getLocalHost().getHostName();
            } catch (UnknownHostException ex) {
                return null;
            }
        }

        private String getModuleDirectory() {
            if (getModule() == null) {
                return null;
            }
            return getModule().getDirectory().getAbsolutePath();
        }

        void setCurrent() {
            isOwnResultCached = false;
            this.editor = getSystemUserName();
            this.station = getSystemStation();
            this.filePath = getModuleDirectory();
            setEditState(EEditState.MODIFIED);
        }

        void clear() {
            isOwnResultCached = false;
            this.editor = null;
            this.station = null;
            this.filePath = null;
        }

        protected void loadFrom(org.radixware.schemas.ddsdef.ModelDocument.Model model) {
            isOwnResultCached = false;
            this.editor = model.isSetEditor() ? model.getEditor() : null;
            this.station = model.isSetStation() ? model.getStation() : null;
            this.filePath = model.isSetFilePath() ? model.getFilePath() : null;
        }

        /**
         * @return user name who edit the model, or null if not defined.
         */
        public String getEditor() {
            return editor;
        }

        /**
         * @return station name where model is edited, or null if not defined.
         */
        public String getStation() {
            return station;
        }

        /**
         * @return full path to model directory where model is edited, or null
         * if not defined.
         */
        public String getFilePath() {
            return filePath;
        }
        private volatile boolean isOwnResultCached = false;
        private volatile boolean isOwnResult = false;

        public boolean isOwn() {
            if (!isOwnResultCached) {
                final String curEditor = getSystemUserName();
                final String curStation = getSystemStation();
                final String curFilePath = getModuleDirectory();
                isOwnResult = Utils.equals(editor, curEditor)
                        && Utils.equals(station, curStation)
                        && Utils.equals(filePath, curFilePath);
                isOwnResultCached = true;
            }
            return isOwnResult;
        }
    }
    private final ModifierInfo modifierInfo = new ModifierInfo();

    /**
     * @return Modifier information.
     */
    public ModifierInfo getModifierInfo() {
        return modifierInfo;
    }

    @Override
    public String toString() {
        final DdsModule module = getModule();
        if (module != null && module.getModelManager().getModifiedModelIfLoaded() == this) {
            return super.toString() + "; modified";
        } else {
            return super.toString();
        }
    }

    @Override
    protected boolean isQualifiedNamePart() {
        return false;
    }
    private boolean isTemporary = false;

    @Override
    protected boolean isTemporary() {
        return isTemporary;
    }

    @Override
    protected boolean isPersistent() {
        return !isTemporary;
    }
    //localization support
    private DdsLocalizingBundleDef stringBundle;

    public DdsLocalizingBundleDef getStringBundle() {
        synchronized (this) {
            if (stringBundle == null) {
                stringBundle = new DdsLocalizingBundleDef(this);
            }
            return stringBundle;
        }
    }

    @Override
    public ILocalizingBundleDef findLocalizingBundle() {
        return getStringBundle();
    }
//    protected boolean isStringBundleLoaded() {
//        synchronized (this) {
//            return stringBundle != null;
//        }
//    }
}
