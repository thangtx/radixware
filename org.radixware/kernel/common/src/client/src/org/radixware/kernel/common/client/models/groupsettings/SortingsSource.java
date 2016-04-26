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
import org.radixware.kernel.common.client.meta.filters.RadFilterDef;
import org.radixware.kernel.common.client.meta.RadSelectorPresentationDef;
import org.radixware.kernel.common.client.meta.RadSortingDef;
import org.radixware.kernel.common.client.meta.RadUserSorting;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.IContext;
import org.radixware.kernel.common.types.Id;


final class SortingsSource extends GroupSettingsSource<RadSortingDef,RadUserSorting>{
    
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
        }        
    }    
    
    private static final Map<IClientEnvironment,GroupSettingsCache<RadUserSorting>> CACHE_BY_ENVIRONMENT = new WeakHashMap<>();
    private static final Object SEMAPHORE = new Object();
    
    
    private final GroupModel group;
    private final boolean canCreateNew;
    
    public SortingsSource(final GroupModel group){
        super(group.getEnvironment(),
              group.getDefinition().getId().toString(),
              Sortings.class.getSimpleName(),
              getSettingsCache(group.getEnvironment())
              );        
        this.group = group;
        if (!group.getEnvironment().isCustomSortingsAccessible()){
            canCreateNew = false;
        }else{
            canCreateNew = 
                    group.getSelectorPresentationDef().isCustomSortingEnabled() || 
                    isCustomSortingAllowedForSomeFilter();
                    
        }
    }
    
    private boolean isCustomSortingAllowedForSomeFilter(){
        final Collection<RadFilterDef> filters = FiltersSource.getPredefinedFilterDefinitions(group);
        for (RadFilterDef filterDef: filters){
            if (filterDef.isAnyCustomSortingEnabled()){
                return true;
            }
        }
        return false;
    }
    
    private List<RadSortingDef> filterAllowedPredefinedSortings(final Collection<RadSortingDef> sortings){
        final Collection<RadFilterDef> filters = FiltersSource.getPredefinedFilterDefinitions(group);
        final List<RadSortingDef> allowedSortings = new LinkedList<>();
        for (RadSortingDef sortingDef: sortings){
            for (RadFilterDef filterDef: filters){
                if (filterDef.isBaseSortingEnabledById(sortingDef.getId())){
                    allowedSortings.add(sortingDef);
                    break;
                }
            }
        }
        return allowedSortings;
    }

    @Override
    protected RadUserSorting getCustomSettingDefinition(final RadSortingDef sorting) {
        if (sorting==null){
            throw new NullPointerException("argument cannot be null");
        }
        if (sorting instanceof RadUserSorting){
            return (RadUserSorting)sorting;
        }
        throw new IllegalArgumentException("Instance of RadUserSorting class expected but "+sorting.getClass().getName()+" found");

    }

    @Override
    protected RadSortingDef createCustomGroupSetting(final RadUserSorting sorting) {
        return sorting;
    }

    @Override
    protected String serializeCustomDefinition(final RadUserSorting sorting) {
        final String sortingAsString = sorting.saveToString();
        sorting.switchToFixedState();
        return sortingAsString;
    }

    @Override
    protected RadUserSorting loadCustomDefinition(final String sortingAsStr) {
        try{
            return RadUserSorting.Factory.loadFromString(environment, 
                                                         group.getSelectorPresentationDef().getOwnerClassId(), 
                                                         sortingAsStr, 
                                                         true);
        }catch(XmlException exception){
            final String reason = 
                ClientException.getExceptionReason(environment.getMessageProvider(), exception);
            final String message =
                environment.getMessageProvider().translate("ExplorerError", "Failed to load custom sorting: %s");
            final String stack = ClientException.exceptionStackToString(exception);
            environment.getTracer().debug(String.format(message,reason)+"\n"+stack);
            return null;            
        }
    }

    @Override
    public List<RadSortingDef> getPredefinedGroupSettings() {
        final RadSelectorPresentationDef presentation = group.getSelectorPresentationDef();
        final Collection<RadSortingDef> allSortings;
        if (group.getContext() instanceof IContext.TableSelect) {
            final IContext.TableSelect context = (IContext.TableSelect) group.getContext();
            final Id actualClassId = context.explorerItemDef.getModelDefinitionClassId();
            final RadClassPresentationDef actualClass = environment.getDefManager().getClassPresentationDef(actualClassId);
            allSortings =  actualClass.getSortings();
        } else {
            allSortings = presentation.getClassPresentation().getSortings();
        }
        final List<RadSortingDef> sortings = new LinkedList<>();
        if (presentation.isAnySortingAcceptable()){
            sortings.addAll(allSortings);
        }else{
            //Sorting can be allowed for presentation or for some filter
            sortings.addAll(filterAllowedPredefinedSortings(allSortings));
            for (RadSortingDef sorting: presentation.getSortings()){
                if (!sortings.contains(sorting)){
                    sortings.add(sorting);
                }
            }            
        }        
        return sortings;
    }

    @Override
    public boolean canCreateNew() {
        return canCreateNew;
    }       
    
    private static GroupSettingsCache<RadUserSorting> getSettingsCache(final IClientEnvironment environment) {
        synchronized(SEMAPHORE){
            GroupSettingsCache<RadUserSorting> cache = CACHE_BY_ENVIRONMENT.get(environment);
            if (cache==null){
                final ConnectionListener connectionListener = new ConnectionListener(environment);
                final VersionListener versionListener = new VersionListener(environment);                
                cache = new GroupSettingsCache<>(Sortings.class.getSimpleName(),connectionListener,versionListener);
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
    }    
    
    public static void clearCache(final IClientEnvironment environment){
        synchronized(SEMAPHORE){
            clearCacheForEnvironment(environment);
        }
    }    
    
    private static void clearCacheForEnvironment(final IClientEnvironment environment){
        final GroupSettingsCache cache = CACHE_BY_ENVIRONMENT.remove(environment);
        if (cache!=null){
            cache.removeListeners(environment);                    
        }        
    }
}
