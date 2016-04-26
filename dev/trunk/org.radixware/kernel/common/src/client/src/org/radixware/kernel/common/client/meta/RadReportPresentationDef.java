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
package org.radixware.kernel.common.client.meta;

import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.errors.ModelCreationError;
import org.radixware.kernel.common.client.errors.NoDefinitionWithSuchIdError;
import org.radixware.kernel.common.client.errors.NoDefinitionWithSuchNameError;
import org.radixware.kernel.common.client.meta.editorpages.IEditorPagesHolder;
import org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef;
import org.radixware.kernel.common.client.meta.editorpages.RadEditorPages;
import org.radixware.kernel.common.client.models.IContext;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.models.ReportParamDialogModel;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.client.types.Restrictions;
import org.radixware.kernel.common.client.views.IView;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.types.Id;

/**
 * Презентационные атрибуты отчета Предоставляет доступ к описаниям страниц,
 * свойств и команд отчета.
 *
 */
public class RadReportPresentationDef extends TitledDefinition implements IModelDefinition, IEditorPagesHolder {

    /**
     * Страницы редактора, определенные в данном отчете
     */
    protected final RadEditorPageDef[] pages;
    private final Id contextParameterId;
    private final Id contextClassId;
    private final boolean isSubreport;
    private final String description;
    private final boolean isExportToCsvEnabled;
    private final boolean isExportToTxtEnabled;
    private final boolean isMultiFileReport;

    @Override
    public RadEditorPageDef findEditorPageById(final Id pageId) {
        if (pages != null && pages.length > 0) {
            for (RadEditorPageDef page : pages) {
                if (page.getId().equals(pageId)) {
                    return page;
                }
            }
        }
        throw new NoDefinitionWithSuchIdError((Definition) this, NoDefinitionWithSuchIdError.SubDefinitionType.EDITOR_PAGE, pageId);
    }
    /**
     * Идентификаторы и уровни вложенности всех страниц отчета расположенные в
     * порядке развертывания в глубину дерева страниц.
     */
    protected final RadEditorPages.PageOrder[] pageOrder;
    private RadEditorPages editorPages = null;

    @Override
    public final RadEditorPages getEditorPages() {
        if (editorPages == null) {
            editorPages = new RadEditorPages(pageOrder, this);
        }

        return editorPages;
    }

    @Override
    public boolean isCommandDefExistsById(final Id id) {
        if (commandOrder != null && commandOrder.length > 0) {
            for (Id commandId : commandOrder) {
                if (commandId.equals(id)) {
                    return true;
                }
            }
        }
        return false;
    }

    private final Id iconId;

    private final int sizeX;
    private final int sizeY;

    @SuppressWarnings("PMD.ArrayIsStoredDirectly")
    public RadReportPresentationDef(final Id id,
            final String name,
            final Id titleId,
            final Id iconId,
            final RadPropertyDef[] properties,
            final RadEditorPageDef[] editorPages,
            final RadEditorPages.PageOrder[] pageOrder,
            final RadCommandDef[] commands,
            final Id[] commandOrder,
            final Id contextParameterId,
            final Id contextClassId,
            final boolean isSubreport,
            final String description,
            final boolean isExportToCsvEnabled,
            final int sizeX,
            final int sizeY) {
        this(id,
                name,
                titleId,
                iconId,
                properties,
                editorPages,
                pageOrder,
                commands,
                commandOrder,
                contextParameterId,
                contextClassId,
                isSubreport,
                description,
                isExportToCsvEnabled,
                false,
                sizeX,
                sizeY);
    }

    @SuppressWarnings("PMD.ArrayIsStoredDirectly")
    public RadReportPresentationDef(final Id id,
            final String name,
            final Id titleId,
            final Id iconId,
            final RadPropertyDef[] properties,
            final RadEditorPageDef[] editorPages,
            final RadEditorPages.PageOrder[] pageOrder,
            final RadCommandDef[] commands,
            final Id[] commandOrder,
            final Id contextParameterId,
            final Id contextClassId,
            final boolean isSubreport,
            final String description,
            final boolean isExportToCsvEnabled,
            final boolean isMultiFileReport,
            final int sizeX,
            final int sizeY) {
        this(id,
                name,
                titleId,
                iconId,
                properties,
                editorPages,
                pageOrder,
                commands,
                commandOrder,
                contextParameterId,
                contextClassId,
                isSubreport,
                description,
                isExportToCsvEnabled,
                false,
                isMultiFileReport,
                0,
                0);
    }

    @SuppressWarnings("PMD.ArrayIsStoredDirectly")
    public RadReportPresentationDef(final Id id,
            final String name,
            final Id titleId,
            final Id iconId,
            final RadPropertyDef[] properties,
            final RadEditorPageDef[] editorPages,
            final RadEditorPages.PageOrder[] pageOrder,
            final RadCommandDef[] commands,
            final Id[] commandOrder,
            final Id contextParameterId,
            final Id contextClassId,
            final boolean isSubreport,
            final String description,
            final boolean isExportToCsvEnabled,
            final boolean isExportToTxtEnabled,
            final boolean isMultiFileReport,
            final int sizeX,
            final int sizeY) {
        super(id, name, titleId);
        this.iconId = iconId;
        this.properties = properties;
        this.pageOrder = pageOrder;
        this.commandOrder = commandOrder;
        this.isExportToTxtEnabled = isExportToTxtEnabled;
        this.commands = commands;
        this.pages = editorPages;
        this.contextParameterId = contextParameterId;
        this.contextClassId = contextClassId;
        this.isSubreport = isSubreport;
        this.description = description;
        this.isExportToCsvEnabled = isExportToCsvEnabled;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.isMultiFileReport = isMultiFileReport;
    }

    public RadReportPresentationDef(final Id id,
            final String name,
            final Id titleId,
            final Id iconId,
            final RadPropertyDef[] properties,
            final RadEditorPageDef[] editorPages,
            final RadEditorPages.PageOrder[] pageOrder,
            final RadCommandDef[] commands,
            final Id[] commandOrder,
            final Id contextParameterId,
            final Id contextClassId,
            final boolean isSubreport,
            final String description,
            final boolean isExportToCsvEnabled) {
        this(id,
                name,
                titleId,
                iconId,
                properties,
                editorPages,
                pageOrder,
                commands,
                commandOrder,
                contextParameterId,
                contextClassId,
                isSubreport,
                description,
                isExportToCsvEnabled,
                0,
                0);

    }

    @Override
    public ReportParamDialogModel createModel(final IContext.Abstract context) {
        if (context == null) {
            throw new NullPointerException("Context must be not null");
        }
        final Model model = createModelImpl(context.getEnvironment());
        model.setContext(context);
        return (ReportParamDialogModel) model;
    }

    protected Model createModelImpl(final IClientEnvironment environment) {
        final Id modelClassId = Id.Factory.changePrefix(getId(), EDefinitionIdPrefix.ADS_REPORT_MODEL_CLASS);
        try {
            final Class<Model> classModel = environment.getDefManager().getDefinitionModelClass(modelClassId);
            final Constructor<Model> constructor
                    = classModel.getConstructor(IClientEnvironment.class, RadReportPresentationDef.class);
            return constructor.newInstance(environment, this);
        } catch (Exception e) {
            throw new ModelCreationError(ModelCreationError.ModelType.REPORT_MODEL, this, null, e);
        }
    }

    @Override
    public IView createStandardView(final IClientEnvironment environment) {
        return getApplication().getStandardViewsFactory().newStandardReportParametersDialog(environment);
    }

    @Override
    public Icon getIcon() {
        return getIcon(iconId);
    }

    @Override
    public Id getOwnerClassId() {
        return getId();
    }
    //Свойства:
    /**
     * Собственные свойства отчета. Заполняется в конструкторе наследника
     */
    protected final RadPropertyDef[] properties;
    private Map<Id, RadPropertyDef> propertiesById = null;
    private Map<String, RadPropertyDef> propertiesByName = null;

    /**
     * Метод возвращает метоописание свойства по идентификатору. Если свойство
     * не найдено генерируется ошибка NoDefinitionWithSuchIdError
     *
     * @param propertyId - идентификатор свойства
     * @return Презентационные атрибуты свойства с идентификатором propertyId
     */
    @Override
    public final RadPropertyDef getPropertyDefById(final Id propertyId) {
        if (propertiesById == null) {
            fillPropertiesById();
        }

        if (propertiesById.containsKey(propertyId)) {
            return propertiesById.get(propertyId);
        }

        throw new NoDefinitionWithSuchIdError((Definition) this, NoDefinitionWithSuchIdError.SubDefinitionType.PROPERTY, propertyId);
    }

    @Override
    public boolean isPropertyDefExistsById(final Id id) {
        if (propertiesById == null) {
            fillPropertiesById();
        }

        if (propertiesById.containsKey(id)) {
            return true;
        }

        return false;
    }

    private void fillPropertiesById() {
        if (properties != null && properties.length > 0) {
            propertiesById = new HashMap<>(properties.length * 2);
            for (RadPropertyDef property : properties) {
                propertiesById.put(property.getId(), property);
            }
        } else {
            propertiesById = Collections.emptyMap();
        }
    }

    /**
     * Метод возвращает метоописание свойства по имени.
     *
     * @param propertyId - идентификатор свойства
     * @return Презентационные атрибуты свойства с именем propertyName
     */
    public final RadPropertyDef getPropertyDefByName(final String propertyName) {
        if (propertiesByName == null) {
            fillPropertiesByName();
        }

        if (propertiesByName.containsKey(propertyName)) {
            return propertiesByName.get(propertyName);
        }
        throw new NoDefinitionWithSuchNameError((Definition) this, NoDefinitionWithSuchNameError.SubDefinitionType.PROPERTY, propertyName);
    }

    public boolean isPropertyDefExistsByName(final String propertyName) {
        if (propertiesByName == null) {
            fillPropertiesByName();
        }

        return propertiesByName.containsKey(propertyName);
    }

    private void fillPropertiesByName() {
        if (properties != null && properties.length > 0) {
            propertiesByName = new HashMap<>(properties.length * 2);
            for (RadPropertyDef property : properties) {
                propertiesByName.put(property.getName(), property);
            }
        } else {
            propertiesByName = Collections.emptyMap();
        }
    }

    /*
     * Возвращает набор идентификаторов свойств, определенных в данном отчете в
     * порядке переданном в конструкторе
     */
    public List<Id> getPropertyIds() {
        if (properties == null || properties.length == 0) {
            return Collections.emptyList();
        }
        final List<Id> propertyIds = new ArrayList<>(properties.length);
        for (RadPropertyDef propertyDef : properties) {
            propertyIds.add(propertyDef.getId());
        }
        return Collections.unmodifiableList(propertyIds);
    }
    //Команды:
    /**
     * Собственные команды отчета.
     */
    protected RadCommandDef[] commands = null;
    /**
     * Идентификаторы всех команд доступных в отчете
     */
    protected Id[] commandOrder = null;
    private List<RadCommandDef> enabledCommands = null;

    private RadCommandDef findCommandDef(final Id commandId) {
        if (commands != null && commands.length > 0) {
            for (RadCommandDef command : commands) {
                if (command.getId().equals(commandId)) {
                    return command;
                }
            }
        }
        if (commandId.getPrefix() == EDefinitionIdPrefix.CONTEXTLESS_COMMAND) {
            return getDefManager().getContextlessCommandDef(commandId);
        }
        return null;
    }

    private void linkCommands() {
        enabledCommands = new ArrayList<>();
        if (commandOrder != null && commandOrder.length > 0) {
            RadCommandDef command;
            for (Id commandId : commandOrder) {
                command = findCommandDef(commandId);
                enabledCommands.add(command);
            }
        }
    }

    /**
     * Метод возвращает набор команд, определенных в данном отчете Команды,
     * идентификаторы которых переданы в списке commandOrder, будут первыми.
     *
     * @return List<CommandDef>
     */
    @Override
    public final List<RadCommandDef> getEnabledCommands() {
        if (enabledCommands == null) {
            linkCommands();
        }
        return Collections.unmodifiableList(enabledCommands);
    }

    /**
     * Метод возвращает метоописание команды по идентификатору. Если команда не
     * найдена генерируется ошибка NoDefinitionWithSuchIdError
     *
     * @param commandId - идентификатор команды
     * @return Презентационные атрибуты команды с идентификатором commandId
     */
    @Override
    public final RadCommandDef getCommandDefById(final Id commandId) {
        final RadCommandDef command = findCommandDef(commandId);
        if (command == null) {
            throw new NoDefinitionWithSuchIdError((Definition) this, NoDefinitionWithSuchIdError.SubDefinitionType.COMMAND, commandId);
        }
        return command;
    }

    @Override
    public Restrictions getRestrictions() {
        return Restrictions.NO_RESTRICTIONS;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public Id getContextParameterId() {
        return contextParameterId;
    }

    public Id getContextClassId() {
        return contextClassId;
    }

    public boolean getIsSubreport() {
        return isSubreport;
    }

    public boolean isExportToCsvEnabled() {
        return isExportToCsvEnabled;
    }

    public boolean isExportToTxtEnabled() {
        return isExportToTxtEnabled;
    }

    public int getDefaultWidth() {
        return sizeX;
    }

    public int getDefaultHeight() {
        return sizeY;
    }

    public boolean isMultiFileReport() {
        return isMultiFileReport;
    }

}
