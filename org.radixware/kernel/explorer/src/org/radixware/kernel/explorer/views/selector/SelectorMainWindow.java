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
import com.trolltech.qt.core.QEventFilter;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QFocusEvent;
import com.trolltech.qt.gui.QPushButton;
import com.trolltech.qt.gui.QShowEvent;
import com.trolltech.qt.gui.QSizePolicy;
import com.trolltech.qt.gui.QStackedWidget;
import com.trolltech.qt.gui.QToolBar;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import java.util.EnumSet;
import org.radixware.kernel.common.client.env.ClientIcon;
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
import org.radixware.kernel.common.client.views.IFilterParametersView;
import org.radixware.kernel.common.client.views.ISelector.ISelectorMainWindow;
import org.radixware.kernel.common.client.widgets.actions.IToolBar;
import org.radixware.kernel.common.exceptions.ServiceCallFault;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.env.ExplorerSettings;
import org.radixware.kernel.explorer.utils.WidgetUtils;
import org.radixware.kernel.explorer.views.MainWindow;
import org.radixware.kernel.explorer.views.Splitter;
import org.radixware.kernel.explorer.widgets.ExplorerWidget;
import org.radixware.kernel.explorer.widgets.selector.IExplorerSelectorWidget;


class SelectorMainWindow extends MainWindow implements ISelectorMainWindow {
    
    private static enum State {SELECTOR_CONTENT, FILTER_PARAMS};
    
    private final Splitter splitter;
    private final QWidget centralWidget;
    private final ExplorerWidget content;
    private final QStackedWidget contentHolder;    
    private final Selector selector;
    private final FilterSettingsStorage filterSettings;
    private final QEventFilter layoutEventListener = new QEventFilter(this){
        @Override
        public boolean eventFilter(final QObject target, final QEvent event) {
            if (SelectorMainWindow.this.state==State.FILTER_PARAMS){
                SelectorMainWindow.this.scheduleUpdateFilterAndOrderToolbarHeight();
            }
            return false;
        }
    };
    private FilterAndOrderToolBar filterAndOrderToolbar;    
    private int currentHeightHint = -1;
    private int currentMinimumHeightHint = -1;
    private FilterModel initialFilter;
    private RadSortingDef initialSorting;
    private int initialFilterToolBarHeight = -1;
    private int storedFilterToolBarHeight = -1;
    private State state;
    private boolean wasShown;
    private boolean filterParamsCollapsedAfterCreation;
    private boolean updateToolBarHeightScheduled;
    private boolean toolbarCreated;
    private boolean toolbarVisibleAfterCreation;

    @Override
    public void addToolBar(final IToolBar toolBar) {
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
        if (!toolbarCreated){
            QApplication.postEvent(this, new CreateToolBarEvent(group));
        }
    }

    public SelectorMainWindow(final Selector ownerSelector) {
        super(ownerSelector);
        setObjectName("selector_main_window");
        this.selector = ownerSelector;
        layoutEventListener.setProcessableEventTypes(EnumSet.of(QEvent.Type.LayoutRequest));
        filterSettings = selector.getEnvironment().getFilterSettingsStorage();

        setSizePolicy(QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Expanding);
        setContextMenuPolicy(Qt.ContextMenuPolicy.NoContextMenu);

        centralWidget = new QWidget(this) {

            @Override
            protected void focusInEvent(final QFocusEvent event) {
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
        splitter.onIllegalMove.connect(this, "onSplitterIllegalMove(int, int)");
        
        contentHolder = new QStackedWidget();
        contentHolder.setObjectName("selector_content_stacked_widget");

        content = new ExplorerWidget(ownerSelector.getEnvironment(), (QWidget)null) {

            @Override
            protected void focusInEvent(final QFocusEvent event) {
                super.focusInEvent(event);
                if (selector.getSelectorWidget() != null && ((IExplorerSelectorWidget) selector.getSelectorWidget()).asQWidget() != null) {
                    ((IExplorerSelectorWidget) selector.getSelectorWidget()).asQWidget().setFocus();
                }
            }
        };
        content.setObjectName("selector_content_widget");
        
        contentHolder.addWidget(content);
        
        final QWidget wdgApplyFilter = new QWidget();
        wdgApplyFilter.setObjectName("selector_apply_filter_widget");
        final QVBoxLayout layout = new QVBoxLayout(wdgApplyFilter);
        layout.setAlignment(new Qt.Alignment(Qt.AlignmentFlag.AlignTop,Qt.AlignmentFlag.AlignHCenter));
        final QPushButton pbApplyFilter = new QPushButton(wdgApplyFilter);
        pbApplyFilter.setObjectName("selector_apply_filter_button");
        pbApplyFilter.setSizePolicy(QSizePolicy.Policy.Fixed, QSizePolicy.Policy.Fixed);
        pbApplyFilter.setText(selector.getEnvironment().getMessageProvider().translate("Selector", "Apply Filter"));
        pbApplyFilter.setIcon(ExplorerIcon.getQIcon(ClientIcon.Dialog.BUTTON_OK));
        pbApplyFilter.clicked.connect(this,"applyFilterAndOrderChanges()");
        layout.addWidget(pbApplyFilter);
        contentHolder.addWidget(wdgApplyFilter);
        
        splitter.addWidget(contentHolder);
        setCentralWidget(centralWidget);
    }

    @Override
    protected void focusInEvent(final QFocusEvent event) {
        super.focusInEvent(event);
        if (centralWidget != null) {
            centralWidget.setFocus();
        }
    }

    private void createFilterAndOrderToolbar(final GroupModel group) {
        if (!toolbarCreated){
            toolbarCreated = true;
            filterAndOrderToolbar = new FilterAndOrderToolBar(group.getEnvironment(), splitter, group, initialFilter, initialSorting);
            filterAndOrderToolbar.setObjectName("filters_and_order_toolbar");
            splitter.insertWidget(0, filterAndOrderToolbar);

            splitter.setCollapsible(0, false);
            splitter.setCollapsible(1, false);
            splitter.setStretchFactor(0, 0);
            splitter.setStretchFactor(1, 1);
            if (!isAnyFilter() && !toolbarVisibleAfterCreation) {
                filterAndOrderToolbar.setVisible(false);
            }
            filterAndOrderToolbar.onFilterSelected.connect(this, "scheduleUpdateFilterAndOrderToolbarHeight()");            
            filterAndOrderToolbar.filterParamsExpanded.connect(this, "updateFilterAndOrderToolbarHeightForcedly()");
            filterAndOrderToolbar.filterParamsCollapsed.connect(this, "scheduleUpdateFilterAndOrderToolbarHeight()");
            if (filterParamsCollapsedAfterCreation && collapseFilterParams()){
                filterParamsCollapsedAfterCreation = false;
            }
            installEventFilter(layoutEventListener);
        }
    }
    
    private void scheduleUpdateFilterAndOrderToolbarHeight() {
        if (!updateToolBarHeightScheduled){
            updateToolBarHeightScheduled = true;
            QApplication.postEvent(this, new UpdateToolBarHeightEvent());
        }
    }

    @Override
    protected void customEvent(final QEvent event) {
        if (event instanceof UpdateToolBarHeightEvent) {
            event.accept();
            if (updateToolBarHeightScheduled){
                updateToolBarHeightScheduled = false;
                updateFilterAndOrderToolbarHeight();
            }
        }else if (event instanceof CreateToolBarEvent){
            event.accept();
            createFilterAndOrderToolbar(((CreateToolBarEvent)event).group);
        }
        super.customEvent(event);
    }

    private void updateFilterAndOrderToolbarHeight() {
        if (filterAndOrderToolbar != null && filterAndOrderToolbar.nativeId() != 0
                && (currentHeightHint!=filterAndOrderToolbar.sizeHint().height()
                       || currentMinimumHeightHint!=filterAndOrderToolbar.minimumSizeHint().height())) {            
            currentMinimumHeightHint = filterAndOrderToolbar.minimumSizeHint().height();
            currentHeightHint = filterAndOrderToolbar.sizeHint().height();
            final int height;
            if (initialFilterToolBarHeight>0){
                height = Math.min(initialFilterToolBarHeight, filterAndOrderToolbar.sizeHint().height());
            }else{
                height = filterAndOrderToolbar.sizeHint().height();
            }
            filterAndOrderToolbar.setMaximumHeight(Math.max(currentHeightHint, currentMinimumHeightHint));
            splitter.moveToPosition(Math.max(height, currentMinimumHeightHint));
            filterAndOrderToolbar.repaint();
            scheduleUpdateFilterAndOrderToolbarHeight();
        }else{
            initialFilterToolBarHeight = -1;
        }
    }

    @Override
    protected void showEvent(final QShowEvent event) {
        super.showEvent(event);
        if (!wasShown) {
            if (filterAndOrderToolbar != null) {
                scheduleUpdateFilterAndOrderToolbarHeight();
            }
            wasShown = true;
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
            if (group.getCurrentFilter()!=null){
                return true;
            }            
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
        if (filterAndOrderToolbar!=null){
            filterAndOrderToolbar.applyChanges();
        }
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
            if (filterAndOrderToolbar==null){
                toolbarVisibleAfterCreation = true;
            }else{
                filterAndOrderToolbar.setVisible(true);
                }

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
        if (group==null){
            return false;
        }
        if (filterAndOrderToolbar == null || group.getView()==null) {//while group.getView() is null refresh of FilterAndOrderToolbar do nothing and result of filterAndOrderToolbar.getCurrentFilter() may be not actualized
            return initialFilter != null && group.getCurrentFilter() == null;
        } else {
            return filterAndOrderToolbar.getCurrentFilter() != group.getCurrentFilter()
                    && !filterAndOrderToolbar.filterWasApplied();
        }
    }

    @Override
    public boolean setupInitialFilterAndSorting() throws InterruptedException { 
        initialFilter = null;
        initialSorting = null;        
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
            final boolean result = setupInitialFilter(lastUsedSortingId) //find and apply initial filter with last used sorting                            
                                             || setupInitialSorting(lastUsedSortingId);
            if (isInitialFilterNeedToBeApplyed()){
                createFilterAndOrderToolbar(group);
            }
            return result;
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
            isContextFilterDefined = !context.getFilters().isEmpty();
        }else{
            isContextFilterDefined = false;
        }
        final FilterModel firstPredefined = isContextFilterDefined ? null : group.getFilters().getFirstPredefined();        
        if (filterSettings.isFilterSettingsStored(group)) {
            final FilterSettingsStorage.FilterSettings settings = filterSettings.getFilterSettings(group);
            final Id lastFilterId = settings.getLastFilterId();
            final Id lastSortingId = settings.getLastSortingId();
            if (lastFilterId != null) {
                FilterModel defaultFilter = group.getFilters().getDefaultFilter(lastFilterId, !isContextFilterDefined);    //В тесте вместо default filter - invalid            
                if (defaultFilter!=null && defaultFilter.getId().equals(lastFilterId) && setStoredFilterAndSorting(lastFilterId,lastSortingId)) {
                    return true;
                } else {
                    defaultFilter = group.getFilters().getDefaultFilter(null, !isContextFilterDefined);    //В тесте вместо default filter - invalid
                    if (defaultFilter != null && defaultFilter.getId() != lastFilterId && 
                           setDefaultFilter(defaultFilter, lastSortingId)
                          ) {
                    return true;
                    }
                }
                return group.getFilters().isObligatory() ? setDefaultFilter(firstPredefined, lastSortingId) : false;
            } else {
                return group.getFilters().isObligatory() ? setDefaultFilter(firstPredefined, lastUsedSortingId) : false;
            }
        } else if (setDefaultFilter(group.getFilters().getDefaultFilter(null, !isContextFilterDefined), lastUsedSortingId)) {
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
            if (setInitialSettings(lastFilter, selector.getEnvironment().getMessageProvider().translate("Selector", "previous filter"), sorting)){
                if (settings.filterParamsWasCollapsed()){
                    filterParamsCollapsedAfterCreation = !collapseFilterParams();
                    initialFilterToolBarHeight = -1;
                }else{
                    initialFilterToolBarHeight = settings.getFilterToolBarHeight();                    
                }
                return true;
            }else{                
                return false;
            }
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
            final RadSortingDef sorting = getSortingForFilter(filter, storedSortingId);
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

    private boolean setInitialSettings(final FilterModel filter, 
                                                      final String filterType,
                                                      final RadSortingDef sorting) throws ServiceClientException, InterruptedException {
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
                    if (filterAndOrderToolbar!=null && initialFilter!=null){
                        filterAndOrderToolbar.setCurrentFilter(initialFilter, false);
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
        return group.getSortings().getDefaultSorting(filter, storedSortingId);            
    }

    private boolean allParametersPersistent(final FilterModel filter) {
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
    public void switchToSelectorContent(){
        if (state!=State.SELECTOR_CONTENT){
            state = State.SELECTOR_CONTENT;
            contentHolder.setCurrentIndex(0);
            setFilterParametersCollapsable(true);
            if (filterParamsCollapsedAfterCreation){
                filterParamsCollapsedAfterCreation = false;
                initialFilterToolBarHeight = -1;
                collapseFilterParams();
            }else{                
                if (storedFilterToolBarHeight>0){                    
                    initialFilterToolBarHeight = storedFilterToolBarHeight;
                    storedFilterToolBarHeight = -1;
                }
                updateFilterAndOrderToolbarHeightForcedly();
            }
        }
    }
    
    @Override
    public void switchToApplyFilter(){
        if (state!=State.FILTER_PARAMS){
            state = State.FILTER_PARAMS;
            if (filterAndOrderToolbar==null){
                createFilterAndOrderToolbar(getGroupModel());
            }
            filterParamsCollapsedAfterCreation = false;
            initialFilterToolBarHeight = -1;
            if (wasShown){
                storedFilterToolBarHeight = filterAndOrderToolbar.height();
            }
            contentHolder.setCurrentIndex(1);
            setFilterParametersCollapsable(false);
        }
    }
        
    private void setFilterParametersCollapsable(final boolean isCollapsable){
        final IFilterParametersView filterView = getFilterView();
        if (filterView!=null){
            filterView.setCollapsable(isCollapsable);
            filterAndOrderToolbar.updateGeometry();
        }
    }
    
    private boolean collapseFilterParams(){
        final IFilterParametersView filterView = getFilterView();
        if (filterView!=null && filterView.isCollapsable()){
            filterView.collapse();
            return true;
        }else{
            return false;
        }
    }
    
    private IFilterParametersView getFilterView(){
        if (filterAndOrderToolbar==null){
            return null;
        }else{
            final FilterModel currentFilter = filterAndOrderToolbar.getCurrentFilter();            
            return currentFilter==null || !currentFilter.hasParameters() ? null : currentFilter.getFilterView();
        }
    }
    
    @SuppressWarnings("unused")
    private void onSplitterIllegalMove(final int movePos, final int closestLegalPos){
        final IFilterParametersView filterView = getFilterView();
        if (filterView!=null){
            final int delta = Math.abs(closestLegalPos - movePos);
            final int maxDelta = 50;
            if (!filterView.isCollapsed() && filterView.isCollapsable() && movePos<closestLegalPos && delta>maxDelta){
                filterView.collapse();
                updateGeometry();
            }else if (filterView.isCollapsed() && movePos>closestLegalPos && delta>maxDelta){
                filterView.expand();
                updateGeometry();                    
            }
        }
    }
        
    private void updateFilterAndOrderToolbarHeightForcedly(){
        currentHeightHint = -1;
        scheduleUpdateFilterAndOrderToolbarHeight();
    }        

    @Override
    public final void clear() {
        if (filterAndOrderToolbar != null) {            
            filterAndOrderToolbar.close();
            removeEventFilter(layoutEventListener);
            updateToolBarHeightScheduled = false;
        }
        splitter.onIllegalMove.disconnect(this);
    }

    @Override
    public void removeToolBarBreak(final IToolBar toolBar) {
        super.removeToolBarBreak((QToolBar) toolBar);
    }
}
