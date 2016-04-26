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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import org.radixware.kernel.common.client.IClientApplication;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.filters.RadCommonFilter;
import org.radixware.kernel.common.types.Id;


class CommonFiltersCache {
    
    private final Map<Id,List<RadCommonFilter>> commonFilters = new HashMap<>();
    private final Object cacheLock = new Object();
    
    private CommonFiltersCache(){
        
    }

    private final static Map<IClientEnvironment,CommonFiltersCache> COMMON_FILTERS_CACHE = new WeakHashMap<>(16);
    private final static Object SEMAPHORE = new Object();
    
    public static CommonFiltersCache getInstance(final IClientEnvironment environment){
        synchronized(SEMAPHORE){
            CommonFiltersCache instance = COMMON_FILTERS_CACHE.get(environment);
            if (instance==null){
                instance = new CommonFiltersCache();
                COMMON_FILTERS_CACHE.put(environment, instance);
            }
            return instance;
        }
    }
    
    public static void clear(final IClientApplication application){
        synchronized(SEMAPHORE){
            final List<IClientEnvironment> environments = new LinkedList<>();
            for(IClientEnvironment environment: COMMON_FILTERS_CACHE.keySet()){
                if (environment.getApplication()==application){
                    environments.add(environment);
                }
            }
            for (IClientEnvironment environment: environments){
                COMMON_FILTERS_CACHE.remove(environment);
            }            
        }
    }
    
    public static void clear(final IClientEnvironment environment){
        synchronized(SEMAPHORE){
            COMMON_FILTERS_CACHE.remove(environment);
        }
    }

    public void put(final Id selectorPresentationId, final RadCommonFilter filter){
        synchronized(cacheLock){
            if (commonFilters.containsKey(selectorPresentationId)){
                final List<RadCommonFilter> filters = commonFilters.get(selectorPresentationId);
                final int i = findFilterById(filter.getId(), filters);
                if (i>=0){
                    filters.set(i, filter);
                }
                else{
                    filters.add(filter);
                }
            }
            else{
                final List<RadCommonFilter> filters = new ArrayList<>();
                filters.add(filter);
                commonFilters.put(selectorPresentationId, filters);
            }
        }
    }
    
    public List<RadCommonFilter> get(final Id selectorPresentationId){
        synchronized(cacheLock){
            return commonFilters.get(selectorPresentationId);
        }
    }
    
    public void removeCommonFilter(final Id selectorPresentationId, final Id filterId){
        synchronized(cacheLock){
            if (commonFilters.containsKey(selectorPresentationId)){
                final List<RadCommonFilter> filters = commonFilters.get(selectorPresentationId);
                final int i = findFilterById(filterId, filters);
                if (i>=0){
                    filters.remove(i);
                }
            }
        }
    }
    
    public void clear(){
        synchronized(cacheLock){
            commonFilters.clear();
        }
    }
    
    private static int findFilterById(final Id filterId, final List<RadCommonFilter> filters){
        for (int i=0, count=filters.size(); i<count; i++){
            if (filterId.equals(filters.get(i).getId())){
                return i;
            }
        }
        return -1;
    }
}
