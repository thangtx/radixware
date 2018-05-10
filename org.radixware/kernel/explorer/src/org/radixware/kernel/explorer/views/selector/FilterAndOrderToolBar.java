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

package org.radixware.kernel.explorer.views.selector;

import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.core.Qt.AlignmentFlag;
import com.trolltech.qt.core.Qt.WidgetAttribute;
import com.trolltech.qt.gui.QCloseEvent;
import com.trolltech.qt.gui.QDialog;
import com.trolltech.qt.gui.QFrame;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QKeyEvent;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QLayout;
import com.trolltech.qt.gui.QLineEdit;
import com.trolltech.qt.gui.QToolButton;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import java.util.Collection;
import java.util.EnumSet;
import java.util.LinkedList;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.eas.RequestHandle;
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
import org.radixware.kernel.common.client.views.UserEICreator;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.dialogs.ExplorerDialog;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.utils.WidgetUtils;
import org.radixware.kernel.explorer.views.FilterParameters;
import org.radixware.kernel.explorer.widgets.ExplorerFrame;
import org.radixware.kernel.explorer.widgets.commands.CommandButtonsPanel;


public class FilterAndOrderToolBar extends ExplorerFrame {
    
    private static class EnterUserExplorerItemTitleDialog extends ExplorerDialog{
        
        private final QLineEdit leTitle = new QLineEdit(this);        
        private int minimumWidth;
        private String resultTitle;
        
        public EnterUserExplorerItemTitleDialog(final IClientEnvironment environment, final QWidget parent, final String initialTitle){
            super(environment,parent);
            setAttribute(WidgetAttribute.WA_DeleteOnClose, true);
            setupUi();
            leTitle.setText(initialTitle);
            final int leTitleWidth = leTitle.fontMetrics().width(initialTitle) + 10;
            final int initialWidth = minimumWidth - leTitle.sizeHint().width() + leTitleWidth; 
            resize(dialogLayout().sizeHint().height(), initialWidth);
        }
        
        private void setupUi(){
            final String windowTitle = 
                getEnvironment().getMessageProvider().translate("Selector", "Enter Explorer Item Title");
            setWindowTitle(windowTitle);
            setWindowIcon(ExplorerIcon.getQIcon(Selector.Icons.INSERT_INTO_TREE));
            final QHBoxLayout editorLayout = WidgetUtils.createHBoxLayout(null);
            editorLayout.setWidgetSpacing(8);
            final String labelText = 
                getEnvironment().getMessageProvider().translate("Selector", "Explorer item title:");
            final QLabel lbTitle = new QLabel(labelText, this);
            WidgetUtils.applyDefaultTextOptions(lbTitle);
            WidgetUtils.applyDefaultTextOptions(leTitle);
            editorLayout.addWidget(lbTitle);
            editorLayout.addWidget(leTitle);
            dialogLayout().addLayout(editorLayout);            
            addButtons(EnumSet.of(EDialogButtonType.OK, EDialogButtonType.CANCEL), true);            
            dialogLayout().setSizeConstraint(QLayout.SizeConstraint.SetMinAndMaxSize);
            minimumWidth = lbTitle.sizeHint().width()+leTitle.sizeHint().width()+40;
            getButton(EDialogButtonType.OK).setEnabled(false);
            leTitle.textChanged.connect(this,"onTextChanged()");
        }

        @Override
        public void done(int result) {
            if (result==QDialog.DialogCode.Accepted.value()){
                resultTitle = leTitle.text();
            }
            super.done(result);
        }
        
        
        
        @SuppressWarnings("unused")
        private void onTextChanged(){
            getButton(EDialogButtonType.OK).setEnabled(!leTitle.text().isEmpty());
        }
        
        public String getExplorerItemTitle(){
            return resultTitle;
        }                

        @Override
        protected QSize layoutMaximumSize(final QSize size) {
            size.setHeight(dialogLayout().sizeHint().height());
            return size;
        }
        
        @Override
        protected QSize layoutMinimumSize(final QSize size) {
            size.setHeight(dialogLayout().sizeHint().height());
            size.setWidth(minimumWidth);
            return size;
        }
    }
    
    public final Signal1<FilterModel> onFilterSelected = new Signal1<>();
    public final Signal0 filterParamsExpanded = new Signal0();
    public final Signal0 filterParamsCollapsed = new Signal0();
    private final GroupModel model;
    private final ChooseGroupSetting<FilterModel> chooseFilter;
    private final ChooseGroupSetting<RadSortingDef> chooseSorting;
    private final QWidget filterAndOrderSelection = new QWidget(this);    
    private final QToolButton applyButton = new QToolButton(this);    
    private final UserEICreator explorerItemsCreator;
    private final FilterSettingsStorage filterSettings;
    private FilterModel currentFilter;
    private FilterParameters currentFilterView;
    private final CommandButtonsPanel commandsPanel;


    @SuppressWarnings("LeakingThisInConstructor")
    public FilterAndOrderToolBar(final IClientEnvironment environment, final QWidget parent, final GroupModel group, final FilterModel initialFilter, final RadSortingDef initialSorting) {
        super(environment, parent);
        model = group;
        explorerItemsCreator = new UserEICreator(group) {
            @Override
            protected String getUserExplorerItemTitle(final String initialTitle) {
                final EnterUserExplorerItemTitleDialog dialog = 
                    new EnterUserExplorerItemTitleDialog(model.getEnvironment(), FilterAndOrderToolBar.this, initialTitle);
                if (dialog.exec()==QDialog.DialogCode.Accepted.value()){
                    return dialog.getExplorerItemTitle();
                }else{
                    return null;
                }
            }
        };
        filterSettings = environment.getFilterSettingsStorage();
        {
            final QVBoxLayout layout = new QVBoxLayout(this);
            layout.setAlignment(new Qt.Alignment(Qt.AlignmentFlag.AlignTop));
            layout.setContentsMargins(0, 6, 0, 4);
            setLayout(layout);
            layout.addWidget(filterAndOrderSelection);
            filterAndOrderSelection.setLayout(WidgetUtils.createHBoxLayout(filterAndOrderSelection));
            ((QHBoxLayout) filterAndOrderSelection.layout()).setContentsMargins(4, 0, 4, 0);
            ((QHBoxLayout) filterAndOrderSelection.layout()).setWidgetSpacing(8);
        }
        {
            commandsPanel = new CommandButtonsPanel(environment, null);
            commandsPanel.setFrameShape(QFrame.Shape.StyledPanel);
            commandsPanel.setParent(this);
            commandsPanel.stateChanged.connect(this, "onCommandsPanelStateChanged()");
            layout().addWidget(commandsPanel);
            onCommandsPanelStateChanged();
        }
        final QHBoxLayout filterLayout = WidgetUtils.createHBoxLayout(null);
        filterLayout.setWidgetSpacing(8);
        filterLayout.setAlignment(new Qt.Alignment(Qt.AlignmentFlag.AlignLeft));
        filterLayout.addWidget(new QLabel(getEnvironment().getMessageProvider().translate("Selector", "Filter:"), parent));
        {
            final String filtersManagerTitle = getEnvironment().getMessageProvider().translate("SelectorAddons", "Filters manager...");
            final EnumSet<GroupSettingsTree.ShowMode> showMode = 
                EnumSet.of(GroupSettingsTree.ShowMode.SHOW_LAST_USED, GroupSettingsTree.ShowMode.SHOW_INVALID_SETTINGS);
            chooseFilter = 
                new ChooseGroupSetting<FilterModel>(environment, filterAndOrderSelection, group.getFilters(), showMode, filtersManagerTitle){

                    @Override
                    protected boolean isSettingsLoaded() {
                        return group.settingsWasRead();
                    }
                    
                    @Override
                    protected RequestHandle startSettingsLoading(){
                        return group.readCommonSettingsAsync();
                    }             
                };
            chooseFilter.setNoItemText(getEnvironment().getMessageProvider().translate("Selector", "<none>"));            
            chooseFilter.setObjectName("filter choice");
            chooseFilter.addonChanged.connect(this, "changeCurrentFilter(IGroupSetting)");
            chooseFilter.editButtonClicked.connect(this, "onEditFilterButtonClick()");
            chooseFilter.openSettingsManager.connect(this, "openFiltersManager()");
            filterLayout.addWidget(chooseFilter);
            ((QHBoxLayout) filterAndOrderSelection.layout()).addLayout(filterLayout);
        }
        final QHBoxLayout sortingLayout = WidgetUtils.createHBoxLayout(null);
        sortingLayout.setWidgetSpacing(8);
        sortingLayout.setAlignment(new Qt.Alignment(Qt.AlignmentFlag.AlignLeft));
        sortingLayout.addWidget(new QLabel(getEnvironment().getMessageProvider().translate("Selector", "Sorting:"), parent));
        {
            final String sortingsManagerTitle = getEnvironment().getMessageProvider().translate("SelectorAddons", "Sortings manager...");
            final EnumSet<GroupSettingsTree.ShowMode> showMode = 
                EnumSet.of(GroupSettingsTree.ShowMode.SHOW_LAST_USED);
            chooseSorting = 
                new ChooseGroupSetting<RadSortingDef>(environment, filterAndOrderSelection, group.getSortings(), showMode, sortingsManagerTitle){

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
            };
            
            chooseSorting.setNoItemText(getEnvironment().getMessageProvider().translate("Selector", "<none>"));            
            chooseSorting.setObjectName("sorting choice");                        
            chooseSorting.addonChanged.connect(this, "changeCurrentSorting(IGroupSetting)");
            chooseSorting.editButtonClicked.connect(this, "onEditSortingButtonClick()");
            chooseSorting.openSettingsManager.connect(this, "openSortingsManager()");
            sortingLayout.addWidget(chooseSorting);
            ((QHBoxLayout) filterAndOrderSelection.layout()).addLayout(sortingLayout);
        }
        {
            applyButton.setFixedHeight(chooseFilter.sizeHint().height() + 2);
            applyButton.setToolButtonStyle(Qt.ToolButtonStyle.ToolButtonTextBesideIcon);
            applyButton.setText(getEnvironment().getMessageProvider().translate("Selector", "Apply settings"));
            applyButton.setObjectName("apply button");
            applyButton.setIcon(ExplorerIcon.getQIcon(ClientIcon.Dialog.BUTTON_OK));
            applyButton.setCheckable(true);
            applyButton.setVisible(false);
            applyButton.clicked.connect(this, "onApplyButtonClick()");
            applyButton.toggled.connect(this, "onApplyButtonToggled(boolean)");
            ((QHBoxLayout) filterAndOrderSelection.layout()).addWidget(applyButton, 0, AlignmentFlag.AlignRight);
        }
        {
            final QWidget insertButton = (QWidget)explorerItemsCreator.getToolButton();
            if (insertButton!=null){
                insertButton.setParent(this);
                filterAndOrderSelection.layout().addWidget(insertButton);
            }
        }
        setSizePolicy(com.trolltech.qt.gui.QSizePolicy.Policy.Preferred, com.trolltech.qt.gui.QSizePolicy.Policy.Maximum);
        if (initialSorting!=null){
            chooseSorting.setCurrentAddon(initialSorting);
        }
        if (initialFilter != null) {
            chooseFilter.setCurrentAddon(initialFilter);
            setCurrentFilterImpl(initialFilter, false);
        }
        chooseFilter.adjustSize();
        chooseSorting.adjustSize();
    }
    
    @SuppressWarnings("unused")
    private void onCommandsPanelStateChanged() {
        commandsPanel.setVisible(commandsPanel.isSomeButtonVisible());
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
        if (!applyButton.isEnabled() || applyButton.isChecked() || currentFilter==null || currentFilterView==null){
            if (!applySorting((RadSortingDef)groupSetting)){
                chooseSorting.setCurrentAddon(model.getCurrentSorting());
            }
        }
        updateEditButtons();
    }    

    private boolean changeCurrentFilterImpl(final FilterModel filter, final boolean quiet) {
        setUpdatesEnabled(false);
        try {
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
                newSorting = model.getSortings().getDefaultSorting(filter, null);
            }else{
                newSorting = null;//keep current sorting
            }
            if (filter == null || !filter.hasParameters() || allParametersPersistent(filter)) {
                if (!applyFilter(filter,newSorting)) {
                    setCurrentFilterImpl(currentFilter, true);
                    chooseFilter.setCurrentAddon(currentFilter);
                }
            } else {
                setCurrentFilterImpl(filter, true);
                chooseFilter.setCurrentAddon(currentFilter);
                if (newSorting!=null){
                    chooseSorting.setCurrentAddon(newSorting);
                }
            }
        } finally {
            setUpdatesEnabled(true);
            updateGeometry();
        }
        if (model.getGroupView() != null) {
            model.getGroupView().repaint();
        }
        if (currentFilterView != null) {
            currentFilterView.setFocus();
        }
        return true;
    }

    public boolean filterWasApplied() {
        return currentFilter != null && applyButton.isChecked();
    }

    @SuppressWarnings("unused")
    private void onApplyButtonClick() {
        if (applyButton.isChecked()) {
            applyFilter(currentFilter,null);
        } else if (model.getGroupView() != null) {
            model.getGroupView().disable();
        }
    }
    
    public boolean applyChanges(){
        return applyFilter(currentFilter,null);
    }

    @SuppressWarnings("unused")
    private void onApplyButtonToggled(boolean checked) {
        if (checked) {
            applyButton.setText(getEnvironment().getMessageProvider().translate("Selector", "Change settings"));
            applyButton.setIcon(ExplorerIcon.getQIcon(ClientIcon.Dialog.BUTTON_CANCEL));
        } else {
            applyButton.setText(getEnvironment().getMessageProvider().translate("Selector", "Apply settings"));
            applyButton.setIcon(ExplorerIcon.getQIcon(ClientIcon.Dialog.BUTTON_OK));
        }
    }

    @SuppressWarnings("unused")
    private void onEditFilterButtonClick() {
        if (currentFilter == null || currentFilter.isCommon()) {
            openFiltersManager();
        } else {
            currentFilter.openEditor(this);
            if (currentFilter.wasModified()) {
                changeCurrentFilterImpl(currentFilter,false);
            }
            chooseFilter.refresh();            
        }
    }
    
    @SuppressWarnings("unused")
    private void onEditSortingButtonClick() {
        final RadSortingDef sorting = getSorting();
        if (sorting==null){
            openSortingsManager();
        }
        else{
            final Collection<String> restrictedNames = 
                new LinkedList<>(model.getSortings().getAllSettingTitles());
            restrictedNames.remove(sorting.getName());
            sorting.openEditor(getEnvironment(), this, restrictedNames, false);
            if (sorting.wasModified()){
                changeCurrentSorting(sorting);
            }
        }
    }    
        
    private void openFiltersManager() {
        final FilterModel filter = model.getFilters().openSettingsManager(this);
        if (filter == null) {
            chooseFilter.refresh();
        } else {
            if (changeCurrentFilterImpl(filter,false)){
                chooseFilter.setCurrentAddon(filter);
            }
        }
    }
        
    private void openSortingsManager() {
        final RadSortingDef sorting = model.getSortings().openSettingsManager(this);
        final boolean canApplySorting = 
                sorting!=null &&
                model.getSortings().isAcceptable(sorting, currentFilter) &&
                (!applyButton.isEnabled() || applyButton.isChecked() || currentFilter==null || currentFilterView==null);
        if (canApplySorting){
            if (applySorting(sorting)){
                chooseSorting.setCurrentAddon(sorting);
            }
        }else{
            chooseSorting.refresh();
        }
    }    
    
    private RadSortingDef getSorting(){
        final RadSortingDef sorting = chooseSorting.getCurrentAddon();
        return sorting==null || !sorting.isValid() ? null : sorting;                
    }
    
    private Id getSortingId(){
        return getSorting()==null ? null : getSorting().getId(); 
    }    
    
    private boolean applyFilter(final FilterModel filter, final RadSortingDef sorting) {
        try {
            applySettings(filter, sorting==null ? getSorting() : sorting);
            if (this.model.getCurrentFilter()!=filter){
                return false;
            }
            final FilterSettingsStorage.FilterSettings settings = filterSettings.getFilterSettings(model);
            settings.saveLastFilter(filter != null ? filter.getId() : null, getSortingId(), true, isFilterParamsCollapsed(), -1);
            return true;
        } catch (ModelPropertyException ex){
            updateApplyButton();
            setUpdatesEnabled(true);
            updateGeometry();
            filter.showException(getEnvironment().getMessageProvider().translate("Selector", "Can't Apply Filter"), ex);
        } catch (InterruptedException ex) {
            updateApplyButton();
        } catch (CommonFilterNotFoundException exception){
            updateApplyButton();
            final String message = getEnvironment().getMessageProvider().translate("ExplorerError", "Filter '%s' was removed");
            final String title = getEnvironment().getMessageProvider().translate("Selector", "Can't Apply Filter");
            getEnvironment().messageInformation(title, String.format(message,filter.getTitle()));
        }        
        catch (Exception ex) {
            updateApplyButton();
            model.showException(getEnvironment().getMessageProvider().translate("Selector", "Can't Apply Filter"), ex);
        }
        return false;        
    }
    
    private void applySettings(final FilterModel filter, final RadSortingDef sorting) throws PropertyIsMandatoryException, InvalidPropertyValueException, ServiceClientException, InterruptedException{
        final Sortings sortings = model.getSortings();
        try {
            model.applySettings(filter, sorting);
        }
        catch(InvalidSortingException exception){
            if (sorting==null){
                throw exception;
            }
            final RadSortingDef defaultSorting = sortings.getDefaultSorting(filter, null);
            if (sorting==defaultSorting){
                applySettings(filter, null);
            }
            else{
                applySettings(filter, defaultSorting);
            }
        }
    }
    
    private boolean applySorting(final RadSortingDef sorting) {
        try {
            model.setSorting(sorting);
            return model.getCurrentSorting()==sorting;
        }catch (InterruptedException ex) {
            updateApplyButton();
        }
        catch (Exception ex) {
            updateApplyButton();
            model.showException(getEnvironment().getMessageProvider().translate("Selector", "Can't Apply Sorting"), ex);
        }
        return false;
    }        

    @Override
    protected void keyPressEvent(QKeyEvent event) {
        if ((event.key() == Qt.Key.Key_Enter.value() || event.key() == Qt.Key.Key_Return.value())
                && currentFilter != null && currentFilterView != null && currentFilterView.isEnabled()) {
            final QWidget focusedWidget = currentFilterView.focusWidget();
            currentFilter.finishEdit();
            if (currentFilter.canApply()) {
                applyFilter(currentFilter,null);
                if (focusedWidget != null && focusedWidget.nativeId() > 0) {
                    focusedWidget.setFocus();
                }
            }
        } else {
            super.keyPressEvent(event);
        }
    }

    @Override
    public QSize sizeHint() {
        if (currentFilterView != null && currentFilterView.nativeId()!=0 && !currentFilterView.isHidden()) {
            return super.sizeHint();
        } else {
            return minimumSizeHint();
        }
    }
    
    public void setCurrentFilter(final FilterModel filter, final boolean apply){
        if (apply){
            setCurrentFilterImpl(filter, true);
            applyFilter(filter,null);
        }
        else{            
            if (changeCurrentFilterImpl(filter,true)){
                chooseFilter.setCurrentAddon(filter);
                if (currentFilterView!=null){
                    applyButton.setChecked(false);
                    onApplyButtonClick();
                }
            }
        }
    }

    private void setCurrentFilterImpl(final FilterModel filter, final boolean invalidate) {
        commandsPanel.clear();
        if (currentFilter != null) {
            if (currentFilterView != null) {
                currentFilterView.close(true);
                layout().removeWidget(currentFilterView);
                currentFilterView.disableGarbageCollection();
                currentFilterView.disposeLater();
                currentFilterView = null;
            }
        }
        if (filter != null && invalidate) {
            filter.invalidate();
        }
        if (filter != null && !filter.getAccessibleCommandIds().isEmpty()) {
            for (Id commandId : filter.getAccessibleCommandIds()) {
                commandsPanel.addButton(filter.getCommand(commandId));
            }
            commandsPanel.bind();
            onCommandsPanelStateChanged();
        } else {
            commandsPanel.setVisible(false);
        }

        currentFilter = filter != null ? filter : null;

        //currentFilterViewBlocked = false;
        if (currentFilter != null && currentFilter.hasParameters()) {
            currentFilterView = (FilterParameters) currentFilter.createView();
            currentFilterView.setAttribute(Qt.WidgetAttribute.WA_DeleteOnClose);
            currentFilterView.setObjectName("filter view");
            layout().addWidget(currentFilterView);
            currentFilterView.open(currentFilter);
            currentFilterView.collapsed.connect(filterParamsCollapsed);
            currentFilterView.expanded.connect(filterParamsExpanded);
            currentFilterView.show();
        }

        final FilterSettingsStorage.FilterSettings settings = filterSettings.getFilterSettings(model);
        settings.saveLastFilter(currentFilter != null ? currentFilter.getId() : null, getSortingId(), false, false, -1);

        updateApplyButton();
        onFilterSelected.emit(filter);
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

    public void refresh() {
        if (currentFilter != model.getCurrentFilter() && model.getGroupView() != null && !model.getGroupView().isDisabled()) {
            applyButton.setChecked(model.getCurrentFilter() != null);            
            setCurrentFilterImpl(model.getCurrentFilter(), false);
        } else {
            updateApplyButton();
        }
        if (currentFilter!=chooseFilter.getCurrentAddon()){
            chooseFilter.setCurrentAddon(currentFilter);
        }        
        else{
            chooseFilter.refresh();
        }
        if (getSorting()!=model.getCurrentSorting() && (!applyButton.isEnabled() || applyButton.isChecked())){
            chooseSorting.setCurrentAddon(model.getCurrentSorting());
        }
        else{
            chooseSorting.refresh();
        }
        updateEditButtons();
        explorerItemsCreator.updateInsertButton();
    }
    
    private void updateEditButtons(){
        if (chooseSorting.getCurrentAddon()!=null){
            if (chooseSorting.getCurrentAddon().isUserDefined()){
                chooseSorting.setEditButtonHint(getEnvironment().getMessageProvider().translate("SelectorAddons", "Edit Sorting"));
            }
            else{
                chooseSorting.setEditButtonHint(getEnvironment().getMessageProvider().translate("SelectorAddons", "View Sorting"));
            }
        }
        else if (model.getSortings().canOpenSettingsManager()){
            chooseSorting.setEditButtonHint(getEnvironment().getMessageProvider().translate("SelectorAddons", "Open Sortings Manager"));
        }
        
        if (currentFilter!=null){
            if (currentFilter.isUserDefined()){
                chooseSorting.setEditButtonHint(getEnvironment().getMessageProvider().translate("SelectorAddons", "Edit Sorting"));
            }
            else if (currentFilter.isCommon()){
                chooseFilter.setEditButtonHint(getEnvironment().getMessageProvider().translate("SelectorAddons", "Open Filters Manager"));
            }
            else{
                chooseSorting.setEditButtonHint(getEnvironment().getMessageProvider().translate("SelectorAddons", "View Sorting"));
            }
        }
        else if (model.getFilters().canOpenSettingsManager()){
            chooseFilter.setEditButtonHint(getEnvironment().getMessageProvider().translate("SelectorAddons", "Open Filters Manager"));
        }
        chooseFilter.setEditButtonVisible(currentFilter!=null || model.getFilters().canOpenSettingsManager());
        chooseSorting.setEditButtonVisible(chooseSorting.getCurrentAddon()!=null || model.getSortings().canOpenSettingsManager());
    }

    private void updateApplyButton() {
        applyButton.setChecked((model.getGroupView() == null || !model.getGroupView().isDisabled())
                && model.getCurrentFilter() == currentFilter
                && currentFilter != null);
        applyButton.setEnabled(currentFilter != null && currentFilterView != null);
    }

    public void storeSettings() {
        final FilterSettingsStorage.FilterSettings settings = filterSettings.getFilterSettings(model);
        if (currentFilter != null) {
            final int height = currentFilterView==null ? -1 : height();
            settings.saveLastFilter(currentFilter.getId(), getSortingId(), applyButton.isChecked(), isFilterParamsCollapsed(), height);
        } else {
            settings.saveLastFilter(null, null, false, false, -1);
        }
    }
    
    private boolean isFilterParamsCollapsed(){
        return currentFilter!=null
                  && currentFilterView!=null 
                  && currentFilterView.isCollapsable()
                  && currentFilterView.isCollapsed();
    }

    @Override
    protected void closeEvent(QCloseEvent event) {
        if (currentFilterView != null) {
            currentFilterView.close(true);
        }
        if (chooseFilter != null) {
            chooseFilter.addonChanged.disconnect();
            chooseFilter.editButtonClicked.disconnect();
            chooseFilter.close();
        }
        super.closeEvent(event);
    }
}
