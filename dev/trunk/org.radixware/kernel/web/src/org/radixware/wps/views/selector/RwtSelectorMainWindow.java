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

package org.radixware.wps.views.selector;

import org.radixware.kernel.common.client.exceptions.ClientException;
import org.radixware.kernel.common.client.exceptions.CommonFilterIsObsoleteException;
import org.radixware.kernel.common.client.exceptions.CommonFilterNotFoundException;
import org.radixware.kernel.common.client.exceptions.ModelException;
import org.radixware.kernel.common.client.meta.RadSortingDef;
import org.radixware.kernel.common.client.models.FilterModel;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.IContext;
import org.radixware.kernel.common.client.models.groupsettings.FilterSettingsStorage;
import org.radixware.kernel.common.client.views.ISelector.ISelectorMainWindow;
import org.radixware.kernel.common.client.widgets.actions.IToolBar;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.html.Div;
import org.radixware.kernel.common.types.Id;
import org.radixware.wps.rwt.AbstractContainer;
import org.radixware.wps.rwt.UIObject;



class RwtSelectorMainWindow implements ISelectorMainWindow {
    
    private boolean isDefaultFilterInited;
    private final FilterSettingsStorage filterSettings;
    
    @Override
    public boolean isAnyFilter() {
        final GroupModel group = getGroupModel();
        if (group == null) {
            return false;
        }
        if (selectorComponent.isFilterAndOrderVisible()) {
            return selectorComponent.getCurrentFilter() != null;
        } else if (isDefaultFilterInited) {
            return false;
        } else {
            isDefaultFilterInited = true;
            if (filterSettings.isFilterSettingsStored(group)) {
                final FilterSettingsStorage.FilterSettings settings = filterSettings.getFilterSettings(group);
                if (settings.getLastFilterId() != null) {
                    return true;
                } else if (!group.getFilters().isObligatory()) {
                    return false;
                }
            }
            FilterModel filter = group.getFilters().getDefaultFilter();
            if (filter == null && group.getFilters().isObligatory()) {
                filter = group.getFilters().getFirstPredefined();
            }
            return filter != null;
        }
    }
    
    private static class ToolBarHandle extends AbstractContainer {
        
        public ToolBarHandle() {
            super(new Div());
            setSizePolicy(SizePolicy.EXPAND, SizePolicy.PREFERRED);
            setPreferredHeight(30);
            html.setCss("border-bottom", "solid 1px #BBB");
        }
    }
    private ToolBarHandle tbHandle = null;
    private RwtSelector selectorComponent;
    
    RwtSelectorMainWindow(RwtSelector selector) {
        this.selectorComponent = selector;
        this.tbHandle = new ToolBarHandle();
        selectorComponent.add(0, this.tbHandle);
        filterSettings = selector.getEnvironment().getFilterSettingsStorage();
        checkTbHandleVisibility();
    }
    
    @Override
    public boolean isFilterAndOrderToolbarVisible() {
        return selectorComponent.isFilterAndOrderVisible();
    }
    
    @Override
    public boolean setupInitialFilterAndSorting() throws InterruptedException {
        final GroupModel group = getGroupModel();
        if (group == null) {
            return false;
        }
        final Id lastUsedSortingId;
        if (!group.getSortings().getLastUsed().isEmpty()) {
            lastUsedSortingId = group.getSortings().getLastUsed().get(0).getId();
        } else {
            lastUsedSortingId = null;
        }
        return setupInitialFilter(lastUsedSortingId) || setupInitialSorting(lastUsedSortingId);
    }
    
    private RadSortingDef initialSorting;
    private FilterModel initialFilter;
    
    private boolean setupInitialSorting(final Id lastUsedSortingId) throws InterruptedException {
        final RadSortingDef sorting = getSortingForFilter(null, lastUsedSortingId);
        if (sorting != null && sorting.isValid()) {
            final GroupModel group = getGroupModel();
            try {
                group.setSorting(sorting);
                initialSorting = sorting;
                return true;
            } catch (ServiceClientException exception) {
                final String traceMessage = selectorComponent.getEnvironment().getMessageProvider().translate("Selector", "Can't apply previous sorting '%s': %s\n%s");
                final String reason = ClientException.getExceptionReason(group.getEnvironment().getMessageProvider(), exception);
                final String stack = ClientException.exceptionStackToString(exception);
                group.getEnvironment().getTracer().error(String.format(traceMessage, sorting.getTitle(), reason, stack));
            }
        }
        return false;
    }
    
    private RadSortingDef getSortingForFilter(final FilterModel filter, final Id storedSortingId) {
        final GroupModel group = getGroupModel();
        final RadSortingDef sorting = storedSortingId == null ? null : group.getSortings().findById(storedSortingId);
        if (sorting == null || !sorting.isValid() || !group.getSortings().isAcceptable(sorting, filter)) {
            return group.getSortings().getDefaultSorting(filter);
        }
        return sorting;
    }
    
    private boolean setupInitialFilter(final Id lastUsedSortingId) throws InterruptedException {
        final GroupModel group = selectorComponent.getGroupModel();
        final boolean isContextFilterDefined;
        if (group.getContext() instanceof IContext.TableSelect){
            final IContext.TableSelect context = (IContext.TableSelect)group.getContext();
            isContextFilterDefined = context.getFilter()!=null;
        }else{
            isContextFilterDefined = false;
        }
        final FilterModel defaultFilter = isContextFilterDefined ? null : group.getFilters().getDefaultFilter();        
        final FilterModel firstPredefined = isContextFilterDefined ? null : group.getFilters().getFirstPredefined();                
        if (filterSettings.isFilterSettingsStored(group)) {
            final FilterSettingsStorage.FilterSettings settings = filterSettings.getFilterSettings(group);
            final Id lastFilterId = settings.getLastFilterId();
            final Id lastSortingId = settings.getLastSortingId();
            if (lastFilterId != null) {
                if (setStoredFilterAndSorting(lastFilterId, lastSortingId)) {
                    return true;
                } else if (defaultFilter != null && defaultFilter.getId() != lastFilterId
                        && setDefaultFilter(defaultFilter, lastSortingId)) {
                    return true;
                } else {
                    return group.getFilters().isObligatory() ? setDefaultFilter(firstPredefined, lastSortingId) : false;
                }
            } else {
                return group.getFilters().isObligatory() ? setDefaultFilter(firstPredefined, lastUsedSortingId) : false;
            }
        } else if (setDefaultFilter(defaultFilter, lastUsedSortingId)) {
            return true;
        } else {
            return group.getFilters().isObligatory() ? setDefaultFilter(firstPredefined, lastUsedSortingId) : false;
        }
    }
    
    private boolean setStoredFilterAndSorting(final Id lastFilterId, final Id lastSortingId) throws InterruptedException {
        final GroupModel group = getGroupModel();
        if (group == null || group.getCurrentFilter() != null) {
            return false;
        }
        //read in config
        final FilterSettingsStorage.FilterSettings settings = filterSettings.getFilterSettings(group);
        final FilterModel lastFilter = group.getFilters().findById(lastFilterId);
        if (lastFilter == null || !lastFilter.isValid()) {
            return false;
        }
        final RadSortingDef sorting = getSortingForFilter(lastFilter, lastSortingId);
        if ((settings.lastFilterWasApplyed() && lastFilter.canApply()) || !lastFilter.hasParameters() || allParametersPersistent(lastFilter)) {
            return setInitialSettings(lastFilter, selectorComponent.getEnvironment().getMessageProvider().translate("Selector", "previous filter"), sorting);
        } else {
            initialFilter = lastFilter;
            initialSorting = sorting;
            return true;
        }
    }
    
    private boolean setDefaultFilter(final FilterModel filter, final Id storedSortingId) throws InterruptedException {
        final GroupModel group = getGroupModel();
        if (group == null || group.getCurrentFilter() != null) {
            return false;
        }
        if (filter != null && filter.isValid()) {
            final RadSortingDef sorting = storedSortingId == null ? group.getSortings().getDefaultSorting(filter) : getSortingForFilter(filter, storedSortingId);
            if (!filter.hasParameters() || allParametersPersistent(filter)) {
                return setInitialSettings(filter, selectorComponent.getEnvironment().getMessageProvider().translate("Selector", "default filter"), sorting);
            } else {
                initialFilter = filter;
                initialSorting = sorting;
                return true;
            }
        }
        return false;
    }
    
    private boolean setInitialSettings(final FilterModel filter, final String filterType, final RadSortingDef sorting) throws InterruptedException {
        final GroupModel group = getGroupModel();
        try {
            group.applySettings(filter, sorting);
            initialFilter = filter;
            initialSorting = sorting;
            return true;
        } catch (ModelException exception) {
            final String traceMessage = selectorComponent.getEnvironment().getMessageProvider().translate("Selector", "Can't apply %s '%s': %s\n%s");
            final String reason = ClientException.getExceptionReason(group.getEnvironment().getMessageProvider(), exception);
            final String stack = ClientException.exceptionStackToString(exception);
            group.getEnvironment().getTracer().error(String.format(traceMessage, filterType, filter.getTitle(), reason, stack));
            return false;
        } catch (CommonFilterIsObsoleteException exception) {
            group.getEnvironment().getTracer().debug(exception.getLocalizedMessage(group.getEnvironment().getMessageProvider()));
            final FilterModel filterModel = group.getFilters().findById(exception.getNewFilter().getId());
            if (filterModel == null) {
                return false;
            } else {
                if (filterModel.getFilterDef().getParameters().isEmpty()) {
                    return setInitialSettings(filterModel, filterType, sorting);
                } else {
                    initialFilter = filter;
                    initialSorting = sorting;
                    return true;
                }
            }
        } catch (CommonFilterNotFoundException exception) {
            group.getEnvironment().getTracer().debug(exception.getLocalizedMessage(group.getEnvironment().getMessageProvider()));
            return false;
        } catch (ServiceClientException exception) {
            final String traceMessage = selectorComponent.getEnvironment().getMessageProvider().translate("Selector", "Can't apply %s '%s': %s\n%s");
            final String reason = ClientException.getExceptionReason(group.getEnvironment().getMessageProvider(), exception);
            final String stack = ClientException.exceptionStackToString(exception);
            group.getEnvironment().getTracer().error(String.format(traceMessage, filterType, filter.getTitle(), reason, stack));
            return false;
        }
    }
    
    private boolean allParametersPersistent(FilterModel filter) {
        return filter != null && filter.getFilterDef().getParameters().allParametersPersistent(filter.getEnvironment()) && filter.canApply();
    }
    
    @Override
    public void applyFilterAndOrderChanges() {
        selectorComponent.applyFilterAndOrderChanges();
    }
    
    @Override
    public void updateFilterAndOrderToolbarVisible(boolean isVisible) {
        selectorComponent.showFilterAndOrder(isVisible, initialFilter, initialSorting);
    }
    
    @Override
    public boolean someGroupSettingIsMandatory() {
        if (getGroupModel() == null) {
            return false;
        }
        return getGroupModel().getFilters().isObligatory();
    }
    
    @Override
    public void refreshFilterAndOrderToolbar() {
        selectorComponent.refreshFilterAndOrder();
    }
    
    @Override
    public boolean isInitialFilterNeedToBeApplyed() {
        final GroupModel group = getGroupModel();
        if (selectorComponent.isFilterAndOrderVisible()) {
            return selectorComponent.getCurrentFilter() != group.getCurrentFilter()
                    && selectorComponent.isDisabled();
        } else {
            return group != null && initialFilter != null && group.getCurrentFilter() == null;
        }
    }
    
    private GroupModel getGroupModel(){
        return (GroupModel)selectorComponent.getModel();
    }
    
    @Override
    public void addToolBar(IToolBar toolBar) {
        tbHandle.add((UIObject) toolBar);
        ((UIObject) toolBar).setHeight(25);
        checkTbHandleVisibility();
    }
    
    @Override
    public void addToolBarBreak() {
    }
    
    final void checkTbHandleVisibility() {
        boolean hideToolBar = true;
        for (UIObject obj : tbHandle.getChildren()) {
            if (obj.isVisible()) {
                hideToolBar = false;
                break;
            }
        }
        if (hideToolBar) {
            if (tbHandle != null) {
                tbHandle.setVisible(false);
            }
        } else {
            if (tbHandle != null) {
                tbHandle.setVisible(true);
            }
        }
        if (hideToolBar) {
            selectorComponent.mainSplitter.setTop(0);
        } else {
            selectorComponent.mainSplitter.setTop(32);
        }
        
    }
    
    @Override
    public void removeToolBarBreak(IToolBar toolBar) {
    }
    
    @Override
    public void setUpdatesEnabled(boolean enabled) {
    }
    
    @Override
    public void clear() {
        selectorComponent.closeFilterAndOrder();
        selectorComponent.clear();
    }
}
