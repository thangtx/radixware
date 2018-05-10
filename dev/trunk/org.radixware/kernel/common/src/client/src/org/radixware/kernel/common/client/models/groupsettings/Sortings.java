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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.client.IClientApplication;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.dialogs.ISortingsManagerDialog;
import org.radixware.kernel.common.client.meta.RadSelectorPresentationDef;
import org.radixware.kernel.common.client.meta.RadSortingDef;
import org.radixware.kernel.common.client.meta.RadUserSorting;
import org.radixware.kernel.common.client.models.FilterModel;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.IContext;
import org.radixware.kernel.common.client.views.IDialog;
import org.radixware.kernel.common.client.views.IDialog.DialogResult;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.types.Id;


public class Sortings extends GroupSettings<RadSortingDef>{
        
    private final GroupModel group; 
    
    private final Map<Id,Id> defaultSortingIdByFilterId = new HashMap<>();    
    
    public Sortings(final GroupModel group){
        super(new SortingsSource(group));
        this.group = group;
    }
    
    public boolean isAcceptable(final RadSortingDef sorting, final FilterModel filter){
        if (!sorting.isValid())
            return false;
        if (sorting.isUserDefined()){
            return canCreateNew(filter);
        }
        else{
            if (filter==null){
                final RadSelectorPresentationDef presentation = group.getSelectorPresentationDef();
                return presentation.isAnySortingAcceptable() || 
                       presentation.getSortings().contains(sorting);
            }else{
                return filter.getFilterDef().isBaseSortingEnabledById(sorting.getId());
            }
        }
    }
    
    public RadSortingDef getDefaultSorting(final FilterModel filter){
        return getDefaultSorting(filter, null);
    }
    
    public RadSortingDef getDefaultSorting(final FilterModel filter, final Id suggestedSortingId){
        if (defaultSortingIdByFilterId.containsKey(filter==null ? null : filter.getId())){
            final Id sortingId = defaultSortingIdByFilterId.get(filter==null ? null : filter.getId());
            return sortingId==null ? null : findById(sortingId);
        }
        final RadSortingDef sorting = suggestedSortingId==null ? null : findById(suggestedSortingId);
        if (sorting!=null && sorting.isValid() && isAcceptable(sorting, filter)){
            return sorting;
        }
        
        if (filter==null){
            return group.getSelectorPresentationDef().getDefaultSortingDef();
        }
        else{
            if (group.getContext() instanceof IContext.TableSelect){
                final RadSortingDef contextSorting = ((IContext.TableSelect)group.getContext()).getSorting();
                if (contextSorting!=null && isAcceptable(contextSorting, filter)){
                    return contextSorting;
                }
            }
            final Id sortingId = filter.getFilterDef().getDefaultSortingId();
            return sortingId==null ? null : findById(sortingId);
        }
    }
    
    public void setDefaultSortingId(final Id sortingId, final Id filterId){
        if (filterId==null){
            setDefaultSortingIdImpl(sortingId, null);
        }else{
            final FilterModel filter = group.getFilters().findById(filterId);
            if (filter==null){
                final String traceMessage = 
                    group.getEnvironment().getMessageProvider().translate("TraceMessage", "Filter #%1s was not found in \'%2s\'");
                group.getEnvironment().getTracer().debug(String.format(traceMessage, filterId, group.getTitle()));
            }else{
                setDefaultSortingIdImpl(sortingId, filter);
            }
        }        
    }
    
    private void setDefaultSortingIdImpl(final Id sortingId, final FilterModel filter){
        if (sortingId==null){
            setDefaultSorting(null, filter);
        }else{
            final RadSortingDef sorting = findById(sortingId);
            if (sorting==null){
                final String traceMessage = 
                    group.getEnvironment().getMessageProvider().translate("TraceMessage", "Sorting #%1s was not found in \'%2s\'");
                group.getEnvironment().getTracer().debug(String.format(traceMessage, sortingId, group.getTitle()));
            }else{
                setDefaultSorting(sorting, filter);
            }
        }        
    }
    
    public void setDefaultSorting(final RadSortingDef sorting, final FilterModel filter){
        if (sorting!=null && !sorting.isValid()){
            throw new IllegalArgumentException("Sorting is not valid");
        }
        if (sorting!=null && findById(sorting.getId())==null){
            add(sorting, null, getSettingsCount());
        }
        defaultSortingIdByFilterId.put(filter==null ? null : filter.getId(), sorting==null ? null : sorting.getId());
    }
    
    public boolean canCreateNew(final FilterModel filter){        
        if (filter==null){
            return canCreateNew() && group.getSelectorPresentationDef().isCustomSortingEnabled();
        }else{
            return canCreateNew() && filter.getFilterDef().isAnyCustomSortingEnabled();
        }
    }
    
    @Override
    public boolean canOpenSettingsManager() {
        return canCreateNew();
    }                

    @Override
    protected RadSortingDef createNewSetting(final String name, final IGroupSetting base, final IWidget parent) {
        final RadUserSorting userSorting = RadUserSorting.Factory.newInstance(group.getEnvironment(), group.getSelectorPresentationDef().getOwnerClassId(), name);
        if (userSorting.openEditor(group.getEnvironment(), parent, getAllSettingTitles(), false)==IDialog.DialogResult.ACCEPTED) {
            return userSorting;
        }
        return null;        
    }
    
    public RadSortingDef createNewSorting(final IWidget parent){
        String sortingName = group.getEnvironment().getMessageProvider().translate("SelectorAddons", "New Sorting");
        for (int i=1; isSettingWithNameExist(sortingName); i++){
            sortingName = group.getEnvironment().getMessageProvider().translate("SelectorAddons", "New Sorting")+" "+i;
        }
        final RadSortingDef newSorting = create(sortingName, null, null, getSettingsCount(), parent);
        if (newSorting!=null){
            saveAll();
        }
        return newSorting;
    }
    
    public RadSortingDef findSortingByColumns(final List<RadSortingDef.SortingItem> columns, final FilterModel currentFilter){
        for (RadSortingDef sorting: getAll()){
            if (sorting.isValid() && isAcceptable(sorting, currentFilter) && sorting.getSortingColumns().size()==columns.size()){
                boolean accepted = true;
                for (int i=0, size=columns.size(); i<size && accepted; i++){
                    accepted = sorting.getSortingColumns().get(i).equals(columns.get(i));
                }
                if (accepted){
                    return sorting;
                }
            }            
        }
        return null;
    }

    @Override
    public boolean isObligatory() {
        //Дефолтная сортировка применяется сервером форсированно, поэтому состояния "<Без сортировки>" быть не может.
        return group.getSelectorPresentationDef().getDefaultSortingDef()!=null;
    }

    
    public boolean isObligatory(final FilterModel filter) {
        //Дефолтная сортировка применяется сервером форсированно, поэтому состояния "<Без сортировки>" быть не может.
        return isObligatory() || (filter!=null && filter.getFilterDef().getDefaultSortingId()!=null);
    }    

    @Override
    public RadSortingDef openSettingsManager(final IWidget parent) {
        final IClientEnvironment environment = group.getEnvironment();
        final ISortingsManagerDialog dialog = environment.getApplication().getDialogFactory().newSortingsManagerDialog(environment, parent, this);
        if (dialog.execDialog() == DialogResult.ACCEPTED) {
            return dialog.getCurrentSorting();
        }
        return null;
    }

    @Override
    public void invalidate() {
        defaultSortingIdByFilterId.clear();
        super.invalidate();
    }        
    
    public GroupModel getGroupModel(){
        return group;
    }    
    
    public static void clearCache(final IClientApplication application){
        SortingsSource.clearCache(application);
    }    
    
    public static void clearCache(final IClientEnvironment environment){
        SortingsSource.clearCache(environment);
    }
}
