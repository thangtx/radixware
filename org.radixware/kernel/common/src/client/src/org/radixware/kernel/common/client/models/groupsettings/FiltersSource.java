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

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import org.apache.xmlbeans.XmlException;
import org.radixware.kernel.common.client.IClientApplication;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.AdsVersion;
import org.radixware.kernel.common.client.exceptions.ClientException;
import org.radixware.kernel.common.client.meta.RadClassPresentationDef;
import org.radixware.kernel.common.client.meta.filters.RadCommonFilter;
import org.radixware.kernel.common.client.meta.filters.RadFilterDef;
import org.radixware.kernel.common.client.meta.RadSelectorPresentationDef;
import org.radixware.kernel.common.client.meta.filters.RadUserFilter;
import org.radixware.kernel.common.client.models.FilterModel;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.IContext;
import org.radixware.kernel.common.types.Id;


final class FiltersSource extends GroupSettingsSource<FilterModel, RadUserFilter>{
    
    private static class ConnectionListener extends IClientEnvironment.ConnectionListener{
        
        private final IClientEnvironment environmentInstance;
        
        public ConnectionListener(final IClientEnvironment environment){
            environmentInstance = environment;
        }

        @Override
        public void afterCloseConnection(boolean forced) {
            synchronized(SEMAPHORE){
                clearCacheForEnvironment(environmentInstance);
            }
            CommonFiltersCache.clear(environmentInstance);                    
        }
    }
    
    private static class VersionListener implements AdsVersion.VersionListener{
        
        private final IClientEnvironment environmentInstance;
        
        public VersionListener(final IClientEnvironment environment){
            environmentInstance = environment;
        }        

        @Override
        public void versionUpdated() {
            synchronized(SEMAPHORE){
                clearCacheForEnvironment(environmentInstance);
            }
            CommonFiltersCache.clear(environmentInstance);
        }        
    }
    
    private static final Map<IClientEnvironment,GroupSettingsCache<RadUserFilter>> CACHE_BY_ENVIRONMENT = new WeakHashMap<>();
    private static final Object SEMAPHORE = new Object();
    
    private final GroupModel group;
    private final boolean canCreateNew;
    private List<RadCommonFilter> commonFilters;
    private Map<Id,RadCommonFilter> commonFiltersById;
    
    public FiltersSource(final GroupModel group){
        super(group.getEnvironment(), 
              group.getConfigStoreGroupName(), 
              Filters.class.getSimpleName(),
              getSettingsCache(group.getEnvironment())
              );
        this.group = group;
        canCreateNew = group.getSelectorPresentationDef().isCustomFiltersEnabled()
               && group.getEnvironment().isCustomFiltersAccessible();        
    }

    @Override
    protected RadUserFilter getCustomSettingDefinition(final FilterModel filter) {
        if (filter.getFilterDef() instanceof RadUserFilter){
            return (RadUserFilter)filter.getFilterDef();
        }
        throw new IllegalArgumentException("Instance of user filter expected");           
    }

    @Override
    protected FilterModel createCustomGroupSetting(final RadUserFilter definition) {
        try{
            return definition.createModel(new IContext.Filter(group));
        }catch(Exception exception){
            final String reason = 
                ClientException.getExceptionReason(environment.getMessageProvider(), exception);
            final String message =
                environment.getMessageProvider().translate("ExplorerError", "Failed to load custom filter: %s");
            environment.getTracer().debug(String.format(message,reason));
            return null;
        }
    }

    @Override
    protected String serializeCustomDefinition(final RadUserFilter filterDef) {
        final String definitionAsStr = filterDef.saveToString();
        filterDef.switchToFixedState();
        return definitionAsStr;
    }

    @Override
    protected RadUserFilter loadCustomDefinition(final String definitionAsStr) {
        try{
            return RadUserFilter.Factory.loadFromString(environment, 
                                                         definitionAsStr,
                                                         group.getSelectorPresentationDef(),
                                                         true);
        }catch(XmlException exception){
            final String reason = 
                ClientException.getExceptionReason(environment.getMessageProvider(), exception);
            final String message =
                environment.getMessageProvider().translate("ExplorerError", "Failed to load custom filter: %s");
            final String stack = ClientException.exceptionStackToString(exception);
            environment.getTracer().debug(String.format(message,reason)+"\n"+stack);
            return null;
        }
    }

    @Override
    public boolean canCreateNew() {
        return canCreateNew;
    }
    
    static Collection<RadFilterDef> getPredefinedFilterDefinitions(final GroupModel group){
        final RadSelectorPresentationDef presentation;
        if (group.getGroupContext().getRootGroupContext()==null ||
            !group.getGroupContext().getRootGroupContext().getSelectorPresentationDef().getOwnerClassId().equals(group.getSelectorPresentationDef().getOwnerClassId())){
            presentation = group.getSelectorPresentationDef();
        }else{
            presentation = group.getGroupContext().getRootGroupContext().getSelectorPresentationDef();
        }
        //fill predefined filters
        if (presentation.isAnyFilterAcceptable() && (group.getContext() instanceof IContext.TableSelect)) {
            //if we can use any filter then add filters from context class presentation
            final IContext.TableSelect context = (IContext.TableSelect) group.getContext();
            final Id actualClassId = context.explorerItemDef.getModelDefinitionClassId();
            final RadClassPresentationDef actualClass = 
                    group.getEnvironment().getDefManager().getClassPresentationDef(actualClassId);
            return actualClass.getFilters();
        } else {
            return presentation.getFilters();
        }        
    }

    @Override
    public List<FilterModel> getPredefinedGroupSettings() {
        final Collection<RadFilterDef> predefinedFilters = getPredefinedFilterDefinitions(group);
        final List<FilterModel> settings = new LinkedList<>();
        for (RadFilterDef filterDef: predefinedFilters){
            settings.add( filterDef.createModel(new IContext.Filter(group)) );
        }
        return settings;
    }
    
    private void fillCommonFilters(){
        commonFilters = 
            CommonFiltersCache.getInstance(environment).get(group.getSelectorPresentationDef().getId());
        if (commonFilters==null){
            commonFilters = Collections.emptyList();
            commonFiltersById = Collections.emptyMap();
        }else{
            commonFiltersById = new HashMap<>();
            for (RadCommonFilter filter: commonFilters){
                commonFiltersById.put(filter.getId(), filter);
            }
        }
    }

    @Override
    public List<Id> getCustomGroupSettingIds() {
        final List<Id> settingIds = new LinkedList<>();
        settingIds.addAll(super.getCustomGroupSettingIds());
        if (commonFilters==null){
            fillCommonFilters();
        }        
        for (RadCommonFilter commonFilterDef: commonFilters){
            settingIds.add( commonFilterDef.getId() );
        }
        return settingIds;
    }

    @Override
    public FilterModel getCustomGroupSetting(final Id settingId) {
        final FilterModel filter = super.getCustomGroupSetting(settingId);
        if (filter==null){
            if (commonFiltersById==null){
                fillCommonFilters();
            }
            final RadCommonFilter commonFilter = commonFiltersById.get(settingId);
            if (commonFilter!=null){
                return commonFilter.createModel(new IContext.Filter(group));
            }
        }
        return filter;
    }

    @Override
    public void invalidate() {
        commonFilters = null;
        commonFiltersById = null;
        super.invalidate();
    }        
    
    private static GroupSettingsCache<RadUserFilter> getSettingsCache(final IClientEnvironment environment) {
        synchronized(SEMAPHORE){
            GroupSettingsCache<RadUserFilter> cache = CACHE_BY_ENVIRONMENT.get(environment);
            if (cache==null){
                final ConnectionListener connectionListener = new ConnectionListener(environment);
                final VersionListener versionListener = new VersionListener(environment);
                cache = new GroupSettingsCache<>(Filters.class.getSimpleName(),connectionListener,versionListener);
                CACHE_BY_ENVIRONMENT.put(environment, cache);
                environment.addConnectionListener(connectionListener);
                environment.getDefManager().getAdsVersion().addVersionListener(versionListener);
            }
            return cache;
        }
    }        
    
    public static void clearCache(final IClientApplication application){
        synchronized(SEMAPHORE){
            final List<IClientEnvironment> environments = new LinkedList<>();
            for(IClientEnvironment environment: CACHE_BY_ENVIRONMENT.keySet()){
                if (environment.getApplication()==application){
                    environments.add(environment);
                }
            }
            for (IClientEnvironment environment: environments){
                clearCacheForEnvironment(environment);
            }            
        }
        CommonFiltersCache.clear(application);
    }
    
    public static void clearCache(final IClientEnvironment environment){
        synchronized(SEMAPHORE){
            clearCacheForEnvironment(environment);
        }
        CommonFiltersCache.clear(environment);
    }
    
    private static void clearCacheForEnvironment(final IClientEnvironment environment){
        final GroupSettingsCache cache = CACHE_BY_ENVIRONMENT.remove(environment);
        if (cache!=null){
            cache.removeListeners(environment);                    
        }        
    }    
}
