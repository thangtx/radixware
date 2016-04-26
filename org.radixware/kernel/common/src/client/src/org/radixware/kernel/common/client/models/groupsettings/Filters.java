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

package org.radixware.kernel.common.client.models.groupsettings;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.IClientApplication;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.dialogs.IFiltersManagerDialog;
import org.radixware.kernel.common.client.exceptions.ExceptionMessage;
import org.radixware.kernel.common.client.meta.filters.RadCommonFilter;
import org.radixware.kernel.common.client.meta.filters.RadFilterDef;
import org.radixware.kernel.common.client.meta.RadSelectorPresentationDef;
import org.radixware.kernel.common.client.meta.filters.RadFilterUserParamDef;
import org.radixware.kernel.common.client.meta.filters.RadUserFilter;
import org.radixware.kernel.common.client.meta.sqml.ISqmlParameter;
import org.radixware.kernel.common.client.models.FilterModel;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.IContext;
import org.radixware.kernel.common.client.views.IDialog.DialogResult;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.groupsettings.CustomFilter;


public class Filters extends GroupSettings<FilterModel> {
    
    private final GroupModel group;
    private final IClientEnvironment environment;
    private final Collection<Id> commonFilterIds = new LinkedList<>();
    
    private Id customDefaultFilterId;
    private boolean customDefaultFilterDefined;
    private Boolean filterIsObligatory;

    public Filters(final GroupModel group) {
        super(MergedGroupSettingsSource.mergeIfNecessary(group, new FiltersSource(group)));
        this.group = group;
        environment = group.getEnvironment();
        for (FilterModel filter: getAll()){
            if (filter.getFilterDef() instanceof RadCommonFilter){
                commonFilterIds.add(filter.getId());
            }
        }
    }
    
    public final void addCommonFilter(CustomFilter xFilter){
        final RadSelectorPresentationDef selectorPresentation = getGroupModel().getSelectorPresentationDef();
        RadCommonFilter commonFilter=RadCommonFilter.Factory.loadFrom(environment, selectorPresentation, xFilter);
        addCommonFilter(commonFilter,selectorPresentation.getId());
        actualizeGroups();
    }
    
    private boolean addCommonFilter(final RadCommonFilter filterDef,final Id presentationId){
        if (!environment.getApplication().isExtendedMetaInformationAccessible()){
            final List<ISqmlParameter> params = filterDef.getParameters().getAll();
            for (ISqmlParameter parameter : params){
                if (parameter instanceof RadFilterUserParamDef
                    && !((RadFilterUserParamDef)parameter).isBasedOnPresentableProperty()){
                    final String messageTemplate = "Information about parameter of common filter \'%1$s\' is not accessible. Ignoring this filter.";
                    environment.getTracer().debug(String.format(messageTemplate, filterDef.getTitle()));
                    return false;
                }
            }
        }
        final FilterModel newFilter;
        try{
            newFilter = filterDef.createModel(new IContext.Filter(group));
            CommonFiltersCache.getInstance(environment).put(presentationId, filterDef);
        }
        catch(Exception exception){
            final ExceptionMessage exceptionMessage = new ExceptionMessage(environment, exception);
            final String messageTitle = environment.getMessageProvider().translate("TraceMessage", "Can't load common filter #%s");
            final String formattedMessageTitle = 
                String.format(messageTitle,filterDef.getId().toString());

            environment.getTracer().error(formattedMessageTitle+"\n"+exceptionMessage.getDialogMessage());
            final StringBuilder debugMessage = new StringBuilder();
            debugMessage.append(formattedMessageTitle);
            debugMessage.append("\n");
            org.radixware.schemas.eas.CommonFilter commonFilter=org.radixware.schemas.eas.CommonFilter.Factory.newInstance();
            filterDef.writeToXml(commonFilter);
            debugMessage.append(commonFilter.xmlText());
            debugMessage.append("\n");
            debugMessage.append(exceptionMessage.getDetails());
            debugMessage.append("\n");
            debugMessage.append(exceptionMessage.getTrace());
            environment.getTracer().debug(debugMessage.toString());
            return false;
        }
        commonFilterIds.add(filterDef.getId());
        putSetting(newFilter);        
        return true;
    }
    
    public final void setCommonFilters(final org.radixware.schemas.eas.CommonFilters commonFilters){
        if (commonFilters.getItemList()!=null){           
            final RadSelectorPresentationDef presentation = group.getSelectorPresentationDef();
            final Id presentationId = presentation.getId();
            final Collection<Id> newCommonFilterIds = new LinkedList<>();
            Id commonFilterId;
            for (org.radixware.schemas.eas.CommonFilter commonFilter: commonFilters.getItemList()){
                commonFilterId = commonFilter.getId();
                if (commonFilterIds.contains(commonFilterId)){
                    final FilterModel existingFilter = findById(commonFilterId);
                    final RadCommonFilter existingFilterDef = (RadCommonFilter)existingFilter.getFilterDef();
                    if (!existingFilterDef.getLastUpdateTime().equals(commonFilter.getLastUpdateTime())){                        
                        existingFilterDef.actualize(commonFilter.getBaseFilterId(), 
                                                 commonFilter.getTitle(), 
                                                 commonFilter.getCondition(),
                                                 commonFilter.getParameters(),
                                                 commonFilter.getLastUpdateTime()
                                                );
                        //reorder common filter
                        removeSetting(commonFilterId);
                        putSetting(existingFilter);
                    }
                }
                else{
                    final RadCommonFilter filterDef = RadCommonFilter.Factory.loadFrom(environment, presentation, commonFilter);                    
                    if(!addCommonFilter(filterDef, presentationId)){
                        continue;
                    }
                    /*final FilterModel newFilter;
                    try{
                        final RadCommonFilter filterDef = RadCommonFilter.Factory.loadFrom(environment, presentation, commonFilter);
                        newFilter = filterDef.createModel(new IContext.Filter(group));
                        CommonFiltersCache.getInstance(environment).put(presentationId, filterDef);
                    }
                    catch(Exception exception){
                        final ExceptionMessage exceptionMessage = new ExceptionMessage(environment, exception);
                        final String messageTitle = environment.getMessageProvider().translate("TraceMessage", "Can't load common filter #%s");
                        final String formattedMessageTitle = 
                            String.format(messageTitle,commonFilterId.toString());
                            
                        environment.getTracer().error(formattedMessageTitle+"\n"+exceptionMessage.getDialogMessage());
                        final StringBuilder debugMessage = new StringBuilder();
                        debugMessage.append(formattedMessageTitle);
                        debugMessage.append("\n");
                        debugMessage.append(commonFilter.xmlText());
                        debugMessage.append("\n");
                        debugMessage.append(exceptionMessage.getDetails());
                        debugMessage.append("\n");
                        debugMessage.append(exceptionMessage.getTrace());
                        environment.getTracer().debug(debugMessage.toString());
                        continue;
                    }
                    commonFilterIds.add(commonFilter.getId());
                    putSetting(newFilter); */ 
                }
                newCommonFilterIds.add(commonFilterId);
            }
            for (Id filterId: commonFilterIds){
                if (!newCommonFilterIds.contains(filterId)){
                    remove(filterId);
                    CommonFiltersCache.getInstance(environment).removeCommonFilter(presentationId, filterId);
                }
            }
            commonFilterIds.clear();
            commonFilterIds.addAll(newCommonFilterIds);
            actualizeGroups();
        }
    }

    public final GroupModel getGroupModel() {
        return group;
    }

    @Override
    public void invalidate() {
        final List<FilterModel> filters = new ArrayList<>();
        filters.addAll(getAll());
        for (FilterModel filter : filters) {
            filter.clean();
        }
        customDefaultFilterDefined = false;
        customDefaultFilterId = null;
        super.invalidate();
    }

    public FilterModel getDefaultFilter() {
        if (customDefaultFilterDefined){
            return customDefaultFilterId==null ? null : findById(customDefaultFilterId);
        }
        final RadFilterDef def = group.getSelectorPresentationDef().getDefaultFilterDef();
        if (def != null) {
            return findById(def.getId());
        }
        return null;
    }
    
    public void setDefaultFilterId(final Id filterId){
        if (filterId==null){
            setDefaultFilter(null);
        }else{
            final FilterModel filter = findById(filterId);
            if (filter==null){
                final String traceMessage = 
                    group.getEnvironment().getMessageProvider().translate("TraceMessage", "Filter #%1s was not found in \'%2s\'");
                group.getEnvironment().getTracer().debug(String.format(traceMessage, filterId, group.getTitle()));                
            }else{
                setDefaultFilter(filter);
            }
        }
    }
    
    public void setDefaultFilter(final FilterModel filter){
        if (filter==null){
            customDefaultFilterDefined = true;
            customDefaultFilterId = null;
        }else if (filter.isValid()){
            if (findById(filter.getId())==null){
                add(filter, null, getSettingsCount());
            }
            customDefaultFilterDefined = true;
            customDefaultFilterId = filter.getId();
        }else{
            throw new IllegalArgumentException("Filter is not valid");
        }
    }
    
    public void setObligatory(final boolean isObligatory){
        filterIsObligatory = isObligatory;
    }

    @Override
    public boolean isObligatory() {
        if (group.getExplorerItemView()==null || !group.getExplorerItemView().isUserItemView()){
            return filterIsObligatory==null ? group.getSelectorPresentationDef().isFilterObligatory() : filterIsObligatory; 
        }else{
            return false;
        }
    }

    @Override
    public FilterModel createNewSetting(final String name, final IGroupSetting base, final IWidget parent) {
        final Id baseId = base == null ? null : base.getId();
        final RadUserFilter userFilter = RadUserFilter.Factory.newInstance(group.getEnvironment(), name, group.getSelectorPresentationDef(), baseId);
        final FilterModel model = userFilter.createModel(new IContext.Filter(group));
        if (model.openEditor(parent) == DialogResult.ACCEPTED) {
            return model;
        }
        return null;
    }    

    @Override
    public FilterModel openSettingsManager(final IWidget parent) {
        final IFiltersManagerDialog dialog = environment.getApplication().getDialogFactory().newFiltersManagerDialog(environment, parent, this);
        if (dialog.execDialog() == DialogResult.ACCEPTED) {
            return dialog.getCurrentFilter();
        }
        return null;
    }    
    
    @Override
    public boolean canOpenSettingsManager() {
        return environment.getApplication().isExtendedMetaInformationAccessible() && super.canOpenSettingsManager();
    }
    
    public static void clearCache(final IClientApplication application){
        FiltersSource.clearCache(application);
    }
    
    public static void clearCache(final IClientEnvironment environment){
        FiltersSource.clearCache(environment);
    }
}
