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

import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QCursor;
import com.trolltech.qt.gui.QFocusEvent;
import com.trolltech.qt.gui.QResizeEvent;
import com.trolltech.qt.gui.QShowEvent;
import com.trolltech.qt.gui.QSizePolicy;
import com.trolltech.qt.gui.QToolBar;
import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.errors.CantOpenSelectorError;
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
import org.radixware.kernel.common.exceptions.ServiceCallFault;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.explorer.env.ExplorerSettings;
import org.radixware.kernel.explorer.utils.WidgetUtils;
import org.radixware.kernel.explorer.views.MainWindow;
import org.radixware.kernel.explorer.views.Splitter;
import org.radixware.kernel.explorer.widgets.ExplorerWidget;
import org.radixware.kernel.explorer.widgets.selector.IExplorerSelectorWidget;


class SelectorMainWindow extends MainWindow implements ISelectorMainWindow {

    private final Splitter splitter;
    private final QWidget centralWidget;
    private final ExplorerWidget content;
    private final Selector selector;
    private final FilterSettingsStorage filterSettings;
    private FilterAndOrderToolBar filterAndOrderToolbar;
    private QSize currentSizeHint, currentMinimumSizeHint;
    private FilterModel initialFilter;
    private RadSortingDef initialSorting;
    private boolean updateToolBarHeightScheduled = false;

    @Override
    public void addToolBar(IToolBar toolBar) {
        if (toolBar instanceof QToolBar) {
            addToolBar((QToolBar) toolBar);
        }
    }

    private static class UpdateToolBarHeightEvent extends QEvent {

        public UpdateToolBarHeightEvent() {
            super(QEvent.Type.User);
        }
    }
    
    private static class CreateToolBarEvent extends QEvent {
        
        public final GroupModel group;
        
        public CreateToolBarEvent(final GroupModel group){
            super(QEvent.Type.User);
            this.group = group;
        }
        
        
    }
    
    public void prepareFilterAndOrderToolbar(final GroupModel group){
        QApplication.postEvent(this, new CreateToolBarEvent(group));
    }

    public SelectorMainWindow(final Selector ownerSelector) {
        super(ownerSelector);
        setObjectName("selector_main_window");
        this.selector = ownerSelector;
        filterSettings = selector.getEnvironment().getFilterSettingsStorage();

        setSizePolicy(QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Expanding);
        setContextMenuPolicy(Qt.ContextMenuPolicy.NoContextMenu);

        centralWidget = new QWidget(this) {

            @Override
            protected void focusInEvent(QFocusEvent event) {
                super.focusInEvent(event);
                if (selector.isDisabled() && filterAndOrderToolbar != null) {
                    filterAndOrderToolbar.setFocus();
                } else {
                    content.setFocus();
                }
            }
        };
        centralWidget.setObjectName("selector_central_widget");

        centralWidget.setLayout(WidgetUtils.createVBoxLayout(centralWidget));

        splitter = new Splitter(centralWidget, (ExplorerSettings) selector.getEnvironment().getConfigStore());
        splitter.setOrientation(Qt.Orientation.Vertical);
        centralWidget.layout().addWidget(splitter);

        content = new ExplorerWidget(ownerSelector.getEnvironment(), splitter) {

            @Override
            protected void focusInEvent(QFocusEvent event) {
                super.focusInEvent(event);
                if (selector.getSelectorWidget() != null && ((IExplorerSelectorWidget) selector.getSelectorWidget()).asQWidget() != null) {
                    ((IExplorerSelectorWidget) selector.getSelectorWidget()).asQWidget().setFocus();
                }
            }
        };
        content.setObjectName("selector_content_widget");

        splitter.addWidget(content);
        setCentralWidget(centralWidget);
    }

    @Override
    protected void focusInEvent(QFocusEvent event) {
        super.focusInEvent(event);
        if (centralWidget != null) {
            centralWidget.setFocus();
        }
    }

    private void createFilterAndOrderToolbar(final GroupModel group) {
        if (filterAndOrderToolbar==null){
            filterAndOrderToolbar = new FilterAndOrderToolBar(group.getEnvironment(), splitter, group, initialFilter, initialSorting);        
            //filterAndOrderToolbar.refresh();
            initialFilter = null;
            initialSorting = null;
            filterAndOrderToolbar.setObjectName("filters_and_order_toolbar");
            splitter.insertWidget(0, filterAndOrderToolbar);

            splitter.setCollapsible(0, false);
            splitter.setCollapsible(1, false);
            if (!isAnyFilter()) {
                filterAndOrderToolbar.setVisible(false);
            }
            filterAndOrderToolbar.onFilterSelected.connect(this,
                    "scheduleUpdateFilterAndOrderToolbarHeight()");
        }
    }
    
    private final static QCursor SPLITTER_CURSOR = new QCursor(Qt.CursorShape.SplitVCursor);
    private final static QCursor APP_CURSOR = new QCursor(Qt.CursorShape.ArrowCursor);

    private void scheduleUpdateFilterAndOrderToolbarHeight() {
        if (!updateToolBarHeightScheduled){
            updateToolBarHeightScheduled = true;
            QApplication.postEvent(this, new UpdateToolBarHeightEvent());
        }
    }

    @Override
    protected void customEvent(QEvent event) {
        if (event instanceof UpdateToolBarHeightEvent) {
            event.accept();
            updateToolBarHeightScheduled = false;
            updateFilterAndOrderToolbarHeight();            
        }else if (event instanceof CreateToolBarEvent){
            event.accept();
            createFilterAndOrderToolbar(((CreateToolBarEvent)event).group);
        }
        super.customEvent(event);
    }

    private void updateFilterAndOrderToolbarHeight() {
        if (filterAndOrderToolbar != null && filterAndOrderToolbar.nativeId() != 0
                && (!Utils.equals(currentSizeHint, filterAndOrderToolbar.sizeHint())
                || !Utils.equals(currentMinimumSizeHint, filterAndOrderToolbar.minimumSizeHint()))) {
            currentSizeHint = filterAndOrderToolbar.sizeHint();
            currentMinimumSizeHint = filterAndOrderToolbar.minimumSizeHint();
            final int height = filterAndOrderToolbar.sizeHint().height();
            final int minimumHeight = filterAndOrderToolbar.minimumSizeHint().height();
            final int maxHeight = Math.max(height, minimumHeight);
            filterAndOrderToolbar.setMaximumHeight(maxHeight);
            splitter.moveToPosition(maxHeight);
            filterAndOrderToolbar.repaint();
            final int minHeight = Math.min(height, minimumHeight);
            if (height == minHeight) {
                splitter.handle(0).setCursor(APP_CURSOR);
            } else {
                splitter.handle(0).setCursor(SPLITTER_CURSOR);
            }

            scheduleUpdateFilterAndOrderToolbarHeight();
        }
    }
    private boolean wasShown;

    @Override
    protected void showEvent(QShowEvent event) {
        super.showEvent(event);
        if (!wasShown) {
            if (filterAndOrderToolbar != null) {
                scheduleUpdateFilterAndOrderToolbarHeight();
            }
            wasShown = true;
        }

    }

    @Override
    public boolean event(QEvent event) {
        if (event.type() == QEvent.Type.LayoutRequest) {
            scheduleUpdateFilterAndOrderToolbarHeight();
        }
        return super.event(event);
    }

    @Override
    protected void resizeEvent(QResizeEvent event) {
        super.resizeEvent(event);
        if (filterAndOrderToolbar != null
                && filterAndOrderToolbar.getCurrentFilter() != null
                && event.oldSize().width() != event.size().width()) {

            final int height = filterAndOrderToolbar.sizeHint().height();
            final int minHeight = Math.min(height, filterAndOrderToolbar.minimumSizeHint().height());
            final int maxHeight = Math.max(height, filterAndOrderToolbar.minimumSizeHint().height());
            filterAndOrderToolbar.setMaximumHeight(maxHeight);

            if (height == minHeight) {
                splitter.handle(0).setCursor(APP_CURSOR);
            } else {
                splitter.handle(0).setCursor(SPLITTER_CURSOR);
            }

        }
    }

    @Override
    public final boolean isFilterAndOrderToolbarVisible() {
        return filterAndOrderToolbar != null && !filterAndOrderToolbar.isHidden();
    }
    
    private GroupModel getGroupModel(){
        return (GroupModel)selector.getModel();
    }

    @Override
    public final boolean isAnyFilter() {
        final GroupModel group = getGroupModel();
        if (group == null) {
            return false;
        }
        if (filterAndOrderToolbar == null) {
            //read in config
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
        } else {
            return filterAndOrderToolbar.getCurrentFilter() != null;
        }
    }

    @Override
    public void applyFilterAndOrderChanges() {
        if (filterAndOrderToolbar!=null)
            filterAndOrderToolbar.applyChanges();
    }   
    

    @Override
    public final void updateFilterAndOrderToolbarVisible(final boolean isVisible) {
        if (isVisible) {
            final GroupModel group = getGroupModel();
            if (group == null) {
                return;
            }

            final boolean someGroupSettingCanBeChoosen = !group.getFilters().isEmpty()
                                || group.getFilters().canCreateNew()
                                || !group.getSortings().isEmpty()
                                || group.getSortings().canCreateNew();

            if (!someGroupSettingCanBeChoosen) {
                return;
            }

            createFilterAndOrderToolbar(group);

            filterAndOrderToolbar.setVisible(true);

        } else {
            if (filterAndOrderToolbar != null
                    && !someGroupSettingIsMandatory()
                    && !isAnyFilter()) {
                filterAndOrderToolbar.setVisible(false);
            }
        }
    }

    public final void setCurrentFilter(final FilterModel filter, final boolean apply) {
        filterAndOrderToolbar.setCurrentFilter(filter, apply || filter.getFilterDef().getParameters().isEmpty());
    }

    @Override
    public final void refreshFilterAndOrderToolbar() {
        if (filterAndOrderToolbar != null) {
            filterAndOrderToolbar.refresh();
        }
    }

    @Override
    public final boolean isInitialFilterNeedToBeApplyed() {
        final GroupModel group = getGroupModel();
        if (filterAndOrderToolbar == null || group.getView()==null) {
            return group != null && initialFilter != null && group.getCurrentFilter() == null;
        } else {
            return filterAndOrderToolbar.getCurrentFilter() != group.getCurrentFilter()
                    && !filterAndOrderToolbar.filterWasApplied();
        }
    }

    @Override
    public boolean setupInitialFilterAndSorting() throws InterruptedException { 
        final GroupModel group = getGroupModel();
        if (group == null) {
            return false;
        }             
        final Id lastUsedSortingId;
        if (!group.getSortings().getLastUsed().isEmpty()){
            lastUsedSortingId = group.getSortings().getLastUsed().get(0).getId();
        }
        else{
            lastUsedSortingId = null;
        }
        try{            
            return setupInitialFilter(lastUsedSortingId) //find and apply initial filter with last used sorting
                   || setupInitialSorting(lastUsedSortingId);
        }        
        catch(ServiceClientException exception){
            throw new CantOpenSelectorError(group, exception);
        }
    }
    
    private boolean setupInitialSorting(final Id lastUsedSortingId) throws ServiceClientException, InterruptedException{        
        final RadSortingDef sorting = getSortingForFilter(null, lastUsedSortingId);
        if (sorting!=null && sorting.isValid()){
            final GroupModel group = getGroupModel();
            try{
                group.setSorting(sorting);
                initialSorting = sorting;
                return true;
            }
            catch (ServiceCallFault exception) {
                final String traceMessage = selector.getEnvironment().getMessageProvider().translate("Selector", "Can't apply previous sorting '%s': %s\n%s");
                final String reason = ClientException.getExceptionReason(group.getEnvironment().getMessageProvider(), exception);
                final String stack = ClientException.exceptionStackToString(exception);
                group.getEnvironment().getTracer().error(String.format(traceMessage, sorting.getTitle(), reason, stack));                
            }            
        }
        return false;
    }
    
    private boolean setupInitialFilter(final Id lastUsedSortingId) throws ServiceClientException, InterruptedException{
        final GroupModel group = getGroupModel();
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
                if (setStoredFilterAndSorting(lastFilterId,lastSortingId)) {
                    return true;
                } else if (defaultFilter != null && defaultFilter.getId() != lastFilterId && 
                           setDefaultFilter(defaultFilter, lastSortingId)
                          ) {
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

    private boolean setStoredFilterAndSorting(final Id lastFilterId, final Id lastSortingId) throws ServiceClientException, InterruptedException {
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
            return setInitialSettings(lastFilter, selector.getEnvironment().getMessageProvider().translate("Selector", "previous filter"), sorting);
        } else {
            initialFilter = lastFilter;
            initialSorting = sorting;
            return true;
        }
    }

    private boolean setDefaultFilter(final FilterModel filter, final Id storedSortingId) throws ServiceClientException, InterruptedException {
        final GroupModel group = getGroupModel();
        if (group == null || group.getCurrentFilter() != null) {
            return false;
        }
        if (filter != null && filter.isValid()) {
            final RadSortingDef sorting = storedSortingId==null ? group.getSortings().getDefaultSorting(filter) : getSortingForFilter(filter, storedSortingId);
            if (!filter.hasParameters() || allParametersPersistent(filter)) {
                return setInitialSettings(filter, selector.getEnvironment().getMessageProvider().translate("Selector", "default filter"),sorting);
            } else {
                initialFilter = filter;
                initialSorting = sorting;
                return true;
            }
        }
        return false;
    }

    private boolean setInitialSettings(final FilterModel filter, final String filterType, final RadSortingDef sorting) throws ServiceClientException, InterruptedException {
        final GroupModel group = getGroupModel();
        try {
            group.applySettings(filter, sorting);
            initialFilter = filter;
            initialSorting = sorting;
            refreshFilterAndOrderToolbar();
            return true;
        } catch (ModelException exception) {
            final String traceMessage = selector.getEnvironment().getMessageProvider().translate("Selector", "Can't apply %s '%s': %s\n%s");
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
                    if (filterAndOrderToolbar!=null){
                        if (initialFilter!=null){
                            filterAndOrderToolbar.setCurrentFilter(initialFilter, false);
                        }
                    }
                    return true;
                }
            }
        } catch (CommonFilterNotFoundException exception) {
            group.getEnvironment().getTracer().debug(exception.getLocalizedMessage(group.getEnvironment().getMessageProvider()));
            return false;
        } catch (ServiceCallFault exception) {
            final String traceMessage = selector.getEnvironment().getMessageProvider().translate("Selector", "Can't apply %s '%s': %s\n%s");
            final String reason = ClientException.getExceptionReason(group.getEnvironment().getMessageProvider(), exception);
            final String stack = ClientException.exceptionStackToString(exception);
            group.getEnvironment().getTracer().error(String.format(traceMessage, filterType, filter.getTitle(), reason, stack));
            return false;
        }
    }
    
    private RadSortingDef getSortingForFilter(final FilterModel filter, final Id storedSortingId){
        final GroupModel group = getGroupModel();
        final RadSortingDef sorting = storedSortingId==null ? null : group.getSortings().findById(storedSortingId);
        if (sorting==null || !sorting.isValid() || !group.getSortings().isAcceptable(sorting, filter)){
            return group.getSortings().getDefaultSorting(filter);            
        }
        return sorting;
    }

    private boolean allParametersPersistent(FilterModel filter) {
        return filter != null && filter.getFilterDef().getParameters().allParametersPersistent(filter.getEnvironment()) && filter.canApply();
    }

    @Override
    public final boolean someGroupSettingIsMandatory() {
        if (getGroupModel() == null) {
            return false;
        }
        return getGroupModel().getFilters().isObligatory();
    }

    public final ExplorerWidget getSelectorContent() {
        return content;
    }

    public final void storeSettings() {
        if (filterAndOrderToolbar != null) {
            filterAndOrderToolbar.storeSettings();
        }
    }

    @Override
    public final void clear() {
        if (filterAndOrderToolbar != null) {
            filterAndOrderToolbar.close();
        }
    }

    @Override
    public void removeToolBarBreak(IToolBar toolBar) {
        super.removeToolBarBreak((QToolBar) toolBar);
    }
}
