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

package org.radixware.kernel.common.client.meta.filters;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.xmlbeans.XmlException;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.errors.ModelCreationError;
import org.radixware.kernel.common.client.errors.NoDefinitionWithSuchIdError;
import org.radixware.kernel.common.client.exceptions.ClientException;
import org.radixware.kernel.common.client.meta.Definition;
import org.radixware.kernel.common.client.meta.IModelDefinition;
import org.radixware.kernel.common.client.meta.RadCommandDef;
import org.radixware.kernel.common.client.meta.RadPropertyDef;
import org.radixware.kernel.common.client.meta.TitledDefinition;
import org.radixware.kernel.common.client.meta.editorpages.IEditorPagesHolder;
import org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef;
import org.radixware.kernel.common.client.meta.editorpages.RadEditorPages;
import org.radixware.kernel.common.client.meta.sqml.ISqmlParameter;
import org.radixware.kernel.common.client.models.FilterModel;
import org.radixware.kernel.common.client.models.IContext;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.client.types.Restrictions;
import org.radixware.kernel.common.client.views.IView;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.types.Id;

public class RadFilterDef extends TitledDefinition implements IModelDefinition, IEditorPagesHolder {

    private final org.radixware.schemas.xscml.Sqml condition;
    private final org.radixware.schemas.xscml.Sqml conditionFrom;
    protected final List<Id> enabledBaseSortings;
    private final boolean isAnyCustomSortingEnabled;
    private final boolean isAnyBaseSortingEnabled;
    private final Id defaultSortingId;
    protected RadFilterParameters parameters;
    private final Map<Id, RadEditorPageDef> pagesById;
    private final RadEditorPages editorPages;
    private final Id ownerId;
    private final ERuntimeEnvironmentType environmentType;

    @SuppressWarnings("PMD.ArrayIsStoredDirectly")
    public RadFilterDef(
            final Id id,
            final String name,
            final ERuntimeEnvironmentType envType,
            final Id classId,
            final Id titleId,
            final String condition,
            final String conditionFrom,
            final Id[] enabledSortings,
            final boolean cstSortingsEnabled,
            final boolean baseSortingsEnabled,
            final Id defaultSortingId,
            final RadFilterParamDef[] parameters,
            final RadEditorPageDef[] pages, //страницы, объявленные в данном фильтре
            final RadEditorPages.PageOrder[] pageOrder, //порядок страниц в фильтре
            final Id[] commandOrder//бесконтекстные команды фильтра
            ) {
        super(id, name, classId, titleId);
        environmentType = envType;
        ownerId = classId;
        
        org.radixware.schemas.xscml.Sqml condition_ = null;
        try {
            if (condition == null) {
                condition_ = org.radixware.schemas.xscml.Sqml.Factory.newInstance();
            } else {
                final org.radixware.schemas.xscml.SqmlDocument document =
                        org.radixware.schemas.xscml.SqmlDocument.Factory.parse(condition);
                condition_ = document.getSqml();
            }
        } catch (XmlException ex) {
            final String message = getApplication().getMessageProvider().translate("ExplorerError", "Failed to parse condition for filter '%s' #%s: %s");
            getApplication().getTracer().error(String.format(message, name, id, ClientException.getExceptionReason(getApplication().getMessageProvider(), ex)));
        }
        this.condition = condition_;
        
        org.radixware.schemas.xscml.Sqml conditionFrom_ = null;
        try {
            if (conditionFrom != null) {
                final org.radixware.schemas.xscml.SqmlDocument document =
                        org.radixware.schemas.xscml.SqmlDocument.Factory.parse(conditionFrom);
                conditionFrom_ = document.getSqml();
            }
        } catch (XmlException ex) {
            final String message = getApplication().getMessageProvider().translate("ExplorerError", "Failed to parse from condition for filter '%s' #%s: %s");
            getApplication().getTracer().error(String.format(message, name, id, ClientException.getExceptionReason(getApplication().getMessageProvider(), ex)));
            conditionFrom_ = org.radixware.schemas.xscml.Sqml.Factory.newInstance();
        }
        this.conditionFrom = conditionFrom_;        
        
        if (enabledSortings != null && enabledSortings.length > 0) {
            enabledBaseSortings = new ArrayList<>(enabledSortings.length);
            Collections.addAll(enabledBaseSortings, enabledSortings);
        } else {
            enabledBaseSortings = null;
        }
        isAnyBaseSortingEnabled = baseSortingsEnabled;
        isAnyCustomSortingEnabled = cstSortingsEnabled;
        this.defaultSortingId = defaultSortingId;

        this.parameters = new RadFilterParameters(parameters, classId);

        if (pages == null) {
            pagesById = Collections.emptyMap();
        } else {
            pagesById = new HashMap<>(pages.length * 2);
            for (RadEditorPageDef page : pages) {
                pagesById.put(page.getId(), page);
            }            
        }

        editorPages = new RadEditorPages(pageOrder, this);
        this.commandOrder = commandOrder;
    }
        
    public RadFilterDef(
            final Id id,
            final String name,
            final ERuntimeEnvironmentType envType,
            final Id classId,
            final Id titleId,
            final String condition,
            final Id[] enabledSortings,
            final boolean cstSortingsEnabled,
            final boolean baseSortingsEnabled,
            final Id defaultSortingId,
            final RadFilterParamDef[] parameters,
            final RadEditorPageDef[] pages, //страницы, объявленные в данном фильтре
            final RadEditorPages.PageOrder[] pageOrder, //порядок страниц в фильтре
            final Id[] commandOrder//бесконтекстные команды фильтра
            ) {
        this(id,
             name,
             envType,
             classId,
             titleId,
             condition,
             null,
             enabledSortings,
             cstSortingsEnabled,
             baseSortingsEnabled,
             defaultSortingId,
             parameters,
             pages,
             pageOrder,
             commandOrder);        
    }
    
    public RadFilterDef(
        final Id id,
        final String name,
        final Id classId,
        final Id titleId,
        final String condition,
        final Id[] enabledSortings,
        final boolean cstSortingsEnabled,
        final boolean baseSortingsEnabled,
        final Id defaultSortingId,
        final RadFilterParamDef[] parameters,
        final RadEditorPageDef[] pages, //страницы, объявленные в данном фильтре
        final RadEditorPages.PageOrder[] pageOrder, //порядок страниц в фильтре
        final Id[] commandOrder//бесконтекстные команды фильтра
        ) {
        this(id,
             name,
             ERuntimeEnvironmentType.COMMON_CLIENT,
             classId,
             titleId,
             condition,
             null,
             enabledSortings,
             cstSortingsEnabled,
             baseSortingsEnabled,
             defaultSortingId,
             parameters,
             pages,
             pageOrder,
             commandOrder);
    }
    
    public final ERuntimeEnvironmentType getEnvironmentType(){
        return environmentType;
    }

    public boolean isBaseSortingEnabledById(final Id srtId) {
        if (isAnyBaseSortingEnabled) {
            return true;
        }
        return enabledBaseSortings != null && enabledBaseSortings.contains(srtId);
    }
    
    public boolean isCustomSortingsEnabled(){
        return isAnyCustomSortingEnabled;
    }

    public final Id getDefaultSortingId() {
        return defaultSortingId;
    }

    public org.radixware.schemas.xscml.Sqml getCondition() {
        return condition == null ? null : (org.radixware.schemas.xscml.Sqml) condition.copy();
    }
    
    public org.radixware.schemas.xscml.Sqml getConditionFrom() {
        return conditionFrom==null ?  null : (org.radixware.schemas.xscml.Sqml) conditionFrom.copy();
    }

    @Override
    public Id getOwnerClassId() {
        return ownerId;
    }

    @Override
    public RadPropertyDef getPropertyDefById(final Id propertyId) {
        final ISqmlParameter result = parameters.getParameterById(propertyId);
        if (!(result instanceof RadPropertyDef)) {
            throw new NoDefinitionWithSuchIdError((Definition) this, NoDefinitionWithSuchIdError.SubDefinitionType.PROPERTY, propertyId);
        }
        return (RadPropertyDef) result;
    }

    @Override
    public boolean isPropertyDefExistsById(final Id propertyId) {
        return parameters.getParameterById(propertyId) != null;
    }

    public boolean isAnyCustomSortingEnabled() {
        return isAnyCustomSortingEnabled;
    }

    public RadFilterParameters getParameters() {
        return parameters;
    }
    //Команды:
    // Идентификаторы бесконтекстных команд, доступных в фильтре
    protected Id[] commandOrder = null;
    private List<RadCommandDef> enabledCommands = null;
    private final Object commandsSemaphore = new Object();

    private RadCommandDef findCommandDef(final Id commandId) {
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
                if (command!=null){
                    enabledCommands.add(command);
                }
            }
        }
    }

    @Override
    public RadCommandDef getCommandDefById(final Id commandId) {
        final List<RadCommandDef> commands = getEnabledCommands();
        for (RadCommandDef command : commands) {
            if (command.getId().equals(commandId)) {
                return command;
            }
        }
        throw new NoDefinitionWithSuchIdError((Definition) this, NoDefinitionWithSuchIdError.SubDefinitionType.COMMAND, commandId);
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

    @Override
    public List<RadCommandDef> getEnabledCommands() {
        synchronized(commandsSemaphore){
            if (enabledCommands == null) {
                linkCommands();
            }
            return Collections.unmodifiableList(enabledCommands);
        }        
    }

    @Override
    public Restrictions getRestrictions() {
        return Restrictions.NO_RESTRICTIONS;
    }

    @Override
    public Icon getIcon() {
        return null;
    }

    @Override
    public IView createStandardView(final IClientEnvironment environment) {
        return getApplication().getStandardViewsFactory().newStandardFilterParameters(environment);
    }

    @Override
    public FilterModel createModel(final IContext.Abstract context) {
        if (context == null) {
            throw new NullPointerException("Context must be not null");
        }
        final Model model = createModelImpl(context.getEnvironment());
        model.setContext(context);
        return (FilterModel) model;
    }

    protected Model createModelImpl(final IClientEnvironment environment) {
        return createModelImpl(environment, getId(), this);
    }
    
    protected static Model createModelImpl(final IClientEnvironment environment, final Id defId, final RadFilterDef def){
        final Id modelClassId = Id.Factory.changePrefix(defId, EDefinitionIdPrefix.ADS_FILTER_MODEL_CLASS);
        try {
            Constructor<Model> constructor = null;

            Class<Model> classModel = environment.getDefManager().getDefinitionModelClass(modelClassId);
            constructor = classModel.getConstructor(IClientEnvironment.class,RadFilterDef.class);
            return constructor.newInstance(environment, def);
        } catch (Exception e) {
            throw new ModelCreationError(ModelCreationError.ModelType.FILTER_MODEL, def, null, e);
        }        
    }

    @Override
    public RadEditorPageDef findEditorPageById(final Id pageId) {
        return pagesById.get(pageId);
    }

    @Override
    public RadEditorPages getEditorPages() {
        return editorPages;
    }

    @Override
    public String getDescription() {
        final String desc = getApplication().getMessageProvider().translate("DefinitionDescribtion", "filter %s");
        return String.format(desc, super.getDescription());
    }
    
    public boolean isValid(){
        return true;
    }

    public Long getLastUpdateTime(){
        return null;
    }
    
}