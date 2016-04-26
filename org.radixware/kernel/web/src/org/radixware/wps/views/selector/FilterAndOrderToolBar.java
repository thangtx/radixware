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

import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.exceptions.CommonFilterNotFoundException;
import org.radixware.kernel.common.client.exceptions.InvalidPropertyValueException;
import org.radixware.kernel.common.client.exceptions.InvalidSortingException;
import org.radixware.kernel.common.client.exceptions.ModelPropertyException;
import org.radixware.kernel.common.client.exceptions.PropertyIsMandatoryException;
import org.radixware.kernel.common.client.meta.RadSortingDef;
import org.radixware.kernel.common.client.models.FilterModel;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.groupsettings.FilterSettingsStorage;
import org.radixware.kernel.common.client.models.groupsettings.IGroupSetting;
import org.radixware.kernel.common.client.models.groupsettings.Sortings;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.client.views.IDialog;
import org.radixware.kernel.common.client.views.UserEICreator;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Id;

import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.rwt.Container;
import org.radixware.wps.rwt.Dialog;
import org.radixware.wps.rwt.InputBox;
import org.radixware.wps.rwt.UIObject;
import org.radixware.wps.rwt.VerticalBox;


class FilterAndOrderToolBar extends Container {
    
    private static class EnterUserExplorerItemTitleDialog extends Dialog {

        private final InputBox<String> leTitle = new InputBox<>(new InputBox.ValueController<String>() {
            @Override
            public String getValue(final String text) throws InputBox.InvalidStringValueException {
                return text;
            }
        });        
        private final VerticalBox content = new VerticalBox();

        public EnterUserExplorerItemTitleDialog(final WpsEnvironment environment, final String initialTitle) {
            super(environment, null);            
            setupUi();
            leTitle.setValue(initialTitle);
        }

        private void setupUi() {
            html.setAttr("dlgId", "enter-password");        
            final String windowTitle = 
                getEnvironment().getMessageProvider().translate("Selector", "Enter Explorer Item Title");            
            setWindowTitle(windowTitle);
            final Icon winowIcon = 
                getEnvironment().getApplication().getImageManager().getIcon(ClientIcon.Selector.INSERT_INTO_TREE);
            final String labelText = 
                getEnvironment().getMessageProvider().translate("Selector", "Explorer item title:");            
            setWindowIcon(winowIcon);            
            leTitle.setFocused(true);            
            leTitle.setLabelVisible(true);
            leTitle.setLabel(labelText);
            content.add(leTitle);
            content.setSizePolicy(SizePolicy.MINIMUM_EXPAND, SizePolicy.MINIMUM_EXPAND);        
            add(content);
            content.setTop(3);
            content.setLeft(3);
            content.getAnchors().setRight(new Anchors.Anchor(1, -3));
            content.getAnchors().setBottom(new Anchors.Anchor(1, -3));                    
            //setAutoHeight(true);
            setHeight(90);         
            setMaxHeight(90);
            setMinimumHeight(90);
            setWidth(500);
            addCloseAction(EDialogButtonType.OK).setDefault(true);
            addCloseAction(EDialogButtonType.CANCEL);
            leTitle.addValueChangeListener(new InputBox.ValueChangeListener<String>() {
                @Override
                public void onValueChanged(String oldValue, String newValue) {
                    refresh();
                }
            });
        }
        
        public String getTitle() {
            return leTitle.getValue();
        }

        @SuppressWarnings("unused")
        private void refresh() {
            setActionEnabled(EDialogButtonType.OK, !leTitle.getValue().isEmpty());
        }

        @Override
        protected IDialog.DialogResult onClose(final String action, final IDialog.DialogResult actionResult) {
            if (actionResult == IDialog.DialogResult.ACCEPTED) {
                if (leTitle.getValue() == null || leTitle.getValue().isEmpty()) {
                    refresh();
                    return null;
                }
            }
            return actionResult;
        }
    }
    
    public interface IFliterChangeListener {

        public void filterSelected(FilterModel filter);
    }
    private final IFliterChangeListener defaultFilterListener = new IFliterChangeListener() {

        @Override
        public void filterSelected(FilterModel filter) {
        }
    };
    private final GroupModel model;
    private final ChooseGroupSetting<FilterModel> chooseFilter;
    private final ChooseGroupSetting<RadSortingDef> chooseSorting;
    private final FilterSettingsStorage filterSettings;
    private final UserEICreator explorerItemsCreator;
    private final WpsEnvironment environment;
    private FilterModel currentFilter;
    private FilterParameters currentFilterView;    

    public FilterAndOrderToolBar(final IClientEnvironment environment, final GroupModel group, final FilterModel initialFilter, final RadSortingDef initialSorting) {
        super();
        html.setAttr("onkeydown", "$RWT.filterParameters.keyDown");
        this.environment = (WpsEnvironment) environment;
        model = group;
        explorerItemsCreator = new UserEICreator(group) {
            @Override
            protected String getUserExplorerItemTitle(final String initialTitle) {
                final EnterUserExplorerItemTitleDialog dialog = 
                    new EnterUserExplorerItemTitleDialog(FilterAndOrderToolBar.this.environment, initialTitle);
                if (dialog.execDialog()==Dialog.DialogResult.ACCEPTED){
                    return dialog.getTitle();
                }else{
                    return null;
                }
            }
        };
        filterSettings = environment.getFilterSettingsStorage();
        final String filtersManagerTitle = getEnvironment().getMessageProvider().translate("SelectorAddons", "Filters manager...");
        final String filterTitle = getEnvironment().getMessageProvider().translate("Selector", "Filter:");
        chooseFilter = new ChooseGroupSetting<FilterModel>(environment, group.getFilters(), filtersManagerTitle, filterTitle) {

            @Override
            protected void groupSettingChanged(FilterModel setting) {

                changeCurrentFilter(setting);
            }
        };
        chooseFilter.setObjectName("filter choice");
        chooseFilter.setNoItemText(getEnvironment().getMessageProvider().translate("Selector", "<none>"));
        final String sortingsManagerTitle = getEnvironment().getMessageProvider().translate("SelectorAddons", "Sortings manager...");
        final String sortingTitle = getEnvironment().getMessageProvider().translate("Selector", "Sorting:");
        chooseSorting = new ChooseGroupSetting<RadSortingDef>(environment, group.getSortings(), sortingsManagerTitle, sortingTitle) {

            @Override
            protected boolean isSettingVisible(RadSortingDef sorting) {
                return group.getSortings().isAcceptable(sorting, currentFilter);
            }

            @Override
            protected boolean isMandatory() {
                return group.getSortings().isObligatory(currentFilter);
            }

            @Override
            protected boolean canOpenSettingsManager() {
                return group.getSortings().canCreateNew(currentFilter);
            }

            @Override
            protected void groupSettingChanged(RadSortingDef setting) {
                changeCurrentSorting(setting);
            }
        };
        chooseSorting.setNoItemText(getEnvironment().getMessageProvider().translate("Selector", "<None>"));
        chooseSorting.setObjectName("sorting choice");
        final UIObject insertButton = (UIObject)explorerItemsCreator.getToolButton();
        if (initialSorting != null) {
            chooseSorting.setCurrentAddon(initialSorting);
        }
        if (initialFilter != null) {            
            setCurrentFilterImpl(initialFilter, false);
        }
        chooseFilter.setLeft(5);
        if (insertButton!=null){
            int w = 30;            
            insertButton.setWidth(w);
            insertButton.setPreferredWidth(w);            
            chooseFilter.getAnchors().setRight(new Anchors.Anchor(0.5f, -5-15));
            chooseSorting.getAnchors().setLeft(new Anchors.Anchor(0.5f, 5));
            chooseSorting.getAnchors().setRight(new Anchors.Anchor(1f, -10-30));
            insertButton.getAnchors().setLeft(new Anchors.Anchor(1f, -5-30));
            //insertButton.getAnchors().setRight(new Anchors.Anchor(1f, -5));
        }else{
            chooseFilter.getAnchors().setRight(new Anchors.Anchor(0.5f, -5));
            chooseSorting.getAnchors().setLeft(new Anchors.Anchor(0.5f, 5));            
            chooseSorting.getAnchors().setRight(new Anchors.Anchor(1f, -5));
        }                
        chooseFilter.setTop(2);
        chooseSorting.setTop(2);
        add(chooseFilter);
        add(chooseSorting);
        if (insertButton!=null){
            insertButton.setTop(2);
            add(insertButton);
        }
        setVCoverage(100);
    }

    @Override
    public final WpsEnvironment getEnvironment() {
        return environment;
    }

    @SuppressWarnings("unused")
    private void changeCurrentFilter(final IGroupSetting groupSetting) {
        final FilterModel filter = (FilterModel) groupSetting;
        if (currentFilter != filter) {
            if (!changeCurrentFilterImpl(filter,false)){
                chooseFilter.setCurrentAddon(currentFilter);
                return;
            }
        }
        updateEditButtons();

    }

    @SuppressWarnings("unused")
    private void changeCurrentSorting(final IGroupSetting groupSetting) {
        if (currentFilter == null && isSelectorEnabled()) {
            if (!applySorting((RadSortingDef) groupSetting)){
                chooseSorting.setCurrentAddon(model.getCurrentSorting());
            }
        }
        updateEditButtons();
    }

    private boolean changeCurrentFilterImpl(final FilterModel filter, final boolean quiet) {
        if (filter!=null && !filter.isValid()){
            if (!quiet){
                final String title = getEnvironment().getMessageProvider().translate("Selector", "Unable to Apply Filter");
                final String message = getEnvironment().getMessageProvider().translate("Selector", "Unable to apply obsolete filter \'%1$s\'");
                getEnvironment().messageError(title, String.format(message, filter.getTitle()));
            }
            return false;
        }            
        if (filter != null && filter.hasParameters() && model.getGroupView() != null && !model.getGroupView().disable()) {
            return false;
        }
        final RadSortingDef choosedSorting = chooseSorting.getCurrentAddon(), newSorting;
        if (choosedSorting!=null && !model.getSortings().isAcceptable(choosedSorting, filter)){
            newSorting = model.getSortings().getDefaultSorting(filter);
        }else{
            newSorting = null;//do not change current sorting
        }            
        if (filter == null || !filter.hasParameters() || allParametersPersistent(filter)) {
            if (!applyFilter(filter, newSorting)) {
                setCurrentFilterImpl(currentFilter, true);
                chooseFilter.setCurrentAddon(currentFilter);
            }
        } else {
            setCurrentFilterImpl(filter, true);
            if (newSorting!=null){
                chooseSorting.setCurrentAddon(newSorting);
            }                
        }
        if (model.getGroupView() != null) {
            model.getGroupView().repaint();
        }
        if (currentFilterView != null) {
            currentFilterView.setFocused(true);
        }
        return true;
    }

    public boolean filterWasApplied() {
        return currentFilter != null;
    }

    public void applyChanges() {
        applyFilter(currentFilter, null);
    }

    private RadSortingDef getSorting() {
        final RadSortingDef sorting = chooseSorting.getCurrentAddon();
        return sorting == null || !sorting.isValid() ? null : sorting;
    }

    private Id getSortingId() {
        return getSorting() == null ? null : getSorting().getId();
    }

    private boolean applyFilter(final FilterModel filter, final RadSortingDef sorting) {
        try {
            applySettings(filter, sorting==null ? getSorting() : sorting);
            final FilterSettingsStorage.FilterSettings settings = filterSettings.getFilterSettings(model);
            settings.saveLastFilter(filter != null ? filter.getId() : null, getSortingId(), true);
            return true;
        } catch (ModelPropertyException ex) {
            filter.showException(getEnvironment().getMessageProvider().translate("Selector", "Can't Apply Filter"), ex);
        } catch (InterruptedException ex) {
        } catch (CommonFilterNotFoundException exception) {
            final String message = getEnvironment().getMessageProvider().translate("ExplorerError", "Filter '%s' was removed");
            final String title = getEnvironment().getMessageProvider().translate("Selector", "Can't Apply Filter");
            getEnvironment().messageInformation(title, String.format(message, filter.getTitle()));
        } catch (Exception ex) {
            model.showException(getEnvironment().getMessageProvider().translate("Selector", "Can't Apply Filter"), ex);
        }
        return false;
    }

    private void applySettings(final FilterModel filter, final RadSortingDef sorting) throws PropertyIsMandatoryException, InvalidPropertyValueException, ServiceClientException, InterruptedException {
        final Sortings sortings = model.getSortings();
        try {
            model.applySettings(filter, sorting);
        } catch (InvalidSortingException exception) {
            if (sorting == null) {
                throw exception;
            }
            final RadSortingDef defaultSorting = sortings.getDefaultSorting(filter);
            if (sorting == defaultSorting) {
                applySettings(filter, null);
            } else {
                applySettings(filter, defaultSorting);
            }
        }
    }

    private boolean applySorting(final RadSortingDef sorting) {
        try {
            model.setSorting(sorting);
            return true;
        } catch (InterruptedException ex) {
        } catch (Exception ex) {

            model.showException(getEnvironment().getMessageProvider().translate("Selector", "Can't Apply Sorting"), ex);
        }
        return false;
    }

    public void setCurrentFilter(final FilterModel filter, final boolean apply) {
        if (apply) {
            setCurrentFilterImpl(filter, true);
            applyFilter(filter, null);
        } else {
            if (changeCurrentFilterImpl(filter,true)){
                chooseFilter.setCurrentAddon(filter);
            }
        }
    }

    private void setCurrentFilterImpl(final FilterModel filter, final boolean invalidate) {
        // commandsPanel.clear();
        if (currentFilter != null) {
            if (currentFilterView != null) {
                currentFilterView.close(true);
                remove(currentFilterView);
                currentFilterView = null;
            }
        }
        if (filter != null && invalidate) {
            filter.invalidate();
        }

        currentFilter = filter != null ? filter : null;
        chooseFilter.setCurrentAddon(currentFilter);
        //currentFilterViewBlocked = false;
        if (currentFilter != null && currentFilter.hasParameters()) {
            currentFilterView = (FilterParameters) currentFilter.createView();
            currentFilterView.setObjectName("filter view");
            add(currentFilterView);
            currentFilterView.setLeft(5);
            currentFilterView.getAnchors().setRight(new Anchors.Anchor(1, -5));
            currentFilterView.getAnchors().setTop(new Anchors.Anchor(1, 5, chooseFilter));
            currentFilterView.getAnchors().setBottom(new Anchors.Anchor(1, -5));
            //currentFilterView.setHeight(100);
            currentFilterView.open(currentFilter);
            currentFilterView.setVisible(true);
        }

        final FilterSettingsStorage.FilterSettings settings = filterSettings.getFilterSettings(model);
        settings.saveLastFilter(currentFilter != null ? currentFilter.getId() : null, getSortingId(), false);

        defaultFilterListener.filterSelected(filter);
    }

    public FilterModel getCurrentFilter() {
        return currentFilter;
    }

    public RadSortingDef getCurrentSorting() {
        return getSorting();
    }

    private static boolean allParametersPersistent(FilterModel filter) {
        return filter != null
                && filter.getFilterDef().getParameters().allParametersPersistent(filter.getEnvironment())
                && filter.canApply();
    }

    private boolean isSelectorEnabled(){
        return model.getGroupView() != null && !model.getGroupView().isDisabled();
    }
    
    public void refresh() {

        if (currentFilter != model.getCurrentFilter() && isSelectorEnabled()) {
            // applyButton.setChecked(model.getCurrentFilter() != null);
            setCurrentFilterImpl(model.getCurrentFilter(), false);
        } else {
            //    updateApplyButton();
        }
        if (currentFilter != chooseFilter.getCurrentAddon()) {
            chooseFilter.setCurrentAddon(currentFilter);
        } else {
            chooseFilter.refresh();
        }
        if (getSorting() != model.getCurrentSorting()) {
            chooseSorting.setCurrentAddon(model.getCurrentSorting());
        } else {
            chooseSorting.refresh();
        }
        updateEditButtons();
        explorerItemsCreator.updateInsertButton();
    }

    private void updateEditButtons() {
        /* if (chooseSorting.getCurrentAddon() != null) {
        if (chooseSorting.getCurrentAddon().isUserDefined()) {
        chooseSorting.setEditButtonHint(getEnvironment().getMessageProvider().translate("SelectorAddons", "Edit Sorting"));
        } else {
        chooseSorting.setEditButtonHint(getEnvironment().getMessageProvider().translate("SelectorAddons", "View Sorting"));
        }
        } else if (model.getSortings().canOpenSettingsManager()) {
        chooseSorting.setEditButtonHint(getEnvironment().getMessageProvider().translate("SelectorAddons", "Open Sortings Manager"));
        }
        
        if (currentFilter != null) {
        if (currentFilter.isUserDefined()) {
        chooseSorting.setEditButtonHint(getEnvironment().getMessageProvider().translate("SelectorAddons", "Edit Filter"));
        } else if (currentFilter.isCommon()) {
        chooseFilter.setEditButtonHint(getEnvironment().getMessageProvider().translate("SelectorAddons", "Open Filters Manager"));
        } else {
        chooseSorting.setEditButtonHint(getEnvironment().getMessageProvider().translate("SelectorAddons", "View Filter"));
        }
        } else if (model.getFilters().canOpenSettingsManager()) {
        chooseFilter.setEditButtonHint(getEnvironment().getMessageProvider().translate("SelectorAddons", "Open Filters Manager"));
        }
        chooseFilter.setEditButtonVisible(currentFilter != null || model.getFilters().canOpenSettingsManager());
        chooseSorting.setEditButtonVisible(chooseSorting.getCurrentAddon() != null || model.getSortings().canOpenSettingsManager());*/
    }

    public void storeSettings() {
        final FilterSettingsStorage.FilterSettings settings = filterSettings.getFilterSettings(model);
        if (currentFilter != null) {
            settings.saveLastFilter(currentFilter.getId(), getSortingId(), isSelectorEnabled());
        } else {
            settings.saveLastFilter(null, null, false);
        }
    }

    public void close() {
        if (currentFilterView != null) {
            currentFilterView.close(true);
            currentFilterView = null;
        }
    }
    
    @Override
    protected String[] clientScriptsRequired() {
        return new String[]{"client.js"};        
    }

    @Override
    public void processAction(String actionName, String actionParam) {
        if ("apply-filter".equals(actionName) && currentFilter != null && 
            currentFilterView != null && currentFilterView.isEnabled() && 
            currentFilter.canApply()
            ){
            applyFilter(currentFilter, null);
        }
        super.processAction(actionName, actionParam);
    }               
}
