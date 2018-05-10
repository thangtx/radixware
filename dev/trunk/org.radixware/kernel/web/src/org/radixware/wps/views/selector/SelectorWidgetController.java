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

import java.util.EnumSet;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.dialogs.IFindAndReplaceDialog;
import org.radixware.kernel.common.client.enums.EKeyboardModifier;
import org.radixware.kernel.common.client.enums.ESelectorColumnHeaderMode;
import org.radixware.kernel.common.client.env.ClientSettings;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException;
import org.radixware.kernel.common.client.meta.RadClassPresentationDef;
import org.radixware.kernel.common.client.meta.RadPropertyDef;
import org.radixware.kernel.common.client.meta.RadSelectorPresentationDef;
import org.radixware.kernel.common.client.meta.RadSortingDef;
import org.radixware.kernel.common.client.models.BrokenEntityModel;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.GroupModelAsyncReader;
import org.radixware.kernel.common.client.models.items.SelectorColumnModelItem;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.client.utils.ClientValueFormatter;
import org.radixware.kernel.common.client.views.IProgressHandle;
import org.radixware.kernel.common.client.views.ISelector;
import org.radixware.kernel.common.client.widgets.selector.ISelectorWidget;
import org.radixware.kernel.common.client.widgets.selector.SelectorSortUtils;
import org.radixware.kernel.common.enums.EEditPossibility;
import org.radixware.kernel.common.enums.ESelectorColumnSizePolicy;
import org.radixware.kernel.common.enums.ESelectorColumnVisibility;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Id;
import org.radixware.wps.icons.WpsIcon;
import org.radixware.wps.rwt.IGrid;
import org.radixware.wps.rwt.IGrid.EColumnSizePolicy;

public abstract class SelectorWidgetController {
    private final static String ROWS_LIMIT_FOR_SEARCH_CONFIG_PATH = 
        SettingNames.SYSTEM+"/"+SettingNames.SELECTOR_GROUP+"/"+SettingNames.Selector.COMMON_GROUP+"/"+SettingNames.Selector.Common.ROWS_LIMIT_FOR_SEARCH;
    private final static class SelectorColumnDescriptor implements IGrid.ColumnDescriptor {

        private final SelectorColumnModelItem column;

        public SelectorColumnDescriptor(final SelectorColumnModelItem column) {
            this.column = column;
        }

        @Override
        public String getTitle() {
            return column.getTitle();
        }
        
        public SelectorColumnModelItem getSelectorColumn(){
            return column;
        }
    }    

    private static final int STRING_MAX_LENGTH = 200;
    private final GroupModel model;
    private boolean readMore = true;
    private int lastReaded = -1;
    private final Object readLock = new Object();
    private final RadSelectorPresentationDef presentation;
    private final ISelector selector;
    private List<SelectorColumnModelItem> columns = new LinkedList<>();
    private final static String STATE_KEY = "headerState";
    private final static String STATE_FORMAT_VERSION_KEY = "headerStateFormatVersion";
    private FindInSelectorDialog findDialog;
    private final IProgressHandle searchProgressHandle;;
    
    private GroupModelAsyncReader.Listener listener = new GroupModelAsyncReader.Listener() {
        @Override
        public void afterChangeGroupModel() {
            reloadView();
        }
    };

    protected abstract void onContentsChanged();

    public abstract int getRowCount();

    protected abstract void clearWidget();

    protected abstract void addRow(EntityModel entity);
    
    protected abstract void addColumn(final int index, final SelectorColumnModelItem column);
    
    protected abstract void removeColumn(final int index);
    
    protected abstract void clearRows();       

    public SelectorWidgetController(GroupModel model, ISelector selector) {
        this.model = model;
        this.presentation = model.getSelectorPresentationDef();
        for (RadSelectorPresentationDef.SelectorColumn sc : presentation.getSelectorColumns()) {
            if (sc.getVisibility() != ESelectorColumnVisibility.NEVER) {
                columns.add(model.getSelectorColumn(sc.getPropertyId()));
            }
        }
        this.model.getAsyncReader().addListener(listener);
        this.selector = selector;
        IClientEnvironment env = model.getEnvironment();
        searchProgressHandle = env.getProgressHandleManager().newStandardProgressHandle();
    }

    public List<SelectorColumnModelItem> getSelectorColumns() {
        return columns;
    }

    public GroupModel getModel() {
        return model;
    }

    public void refresh(final Property prop) {
    }
    
    public static String getTextToDisplay(final Property property) {
        if (property.isUnacceptableInputRegistered()){
            return property.getUnacceptableInput().getText();
        }else{        
            String valueAsStr = property.getValueAsString();
            if (valueAsStr != null && valueAsStr.length() > STRING_MAX_LENGTH)//DBP-1658
            {
                valueAsStr = valueAsStr.substring(0, STRING_MAX_LENGTH - 1) + "...";
            }
            valueAsStr = valueAsStr == null ? "null" : valueAsStr.replace('\n', ' ');
            final boolean cantEdit = property.isReadonly()
                    || (!property.hasOwnValue() && property.isValueDefined())
                    || property.isCustomEditOnly()
                    || !property.isEnabled()
                    || property.getEditPossibility() == EEditPossibility.PROGRAMMATICALLY;

            if (cantEdit) {
                valueAsStr = property.getOwner().getDisplayString(property.getId(), property.getValueObject(), valueAsStr, !property.hasOwnValue());
            }
            return valueAsStr;
        }
    }

    private void setReadMore(boolean readMore) {
        this.readMore = readMore;
        onContentsChanged();
    }

    private void reset() {
        synchronized (readLock) {
            setReadMore(true);
            lastReaded = -1;
        }
    }

    private void clear() {
        clearWidget();
    }

    public void reread() throws InterruptedException, ServiceClientException {
        final GroupModel group = model;
        if (group != null) {
            reset();
            group.reset();
            clear();
            readMore(group);
        }
    }

    private void reloadView() {
        final GroupModel group = model;
        if (group != null) {
            EntityModel model = selector.getCurrentEntity();
            Pid pid = null;
            if (model != null) {
                pid = model.getPid();
            }
            reset();
            clear();
            readMore(group, group.getEntitiesCount());
            if (pid != null) {
                ISelectorWidget widget = selector.getSelectorWidget();
                if (widget instanceof RwtSelectorGrid) {
                    if (((RwtSelectorGrid) widget).selectEntity(pid)) {
                        return;
                    }
                } else {
                    int index = group.findEntityByPid(pid);
                    if (index >= 0) {
                        try {
                            model = group.getEntity(index);
                            selector.setCurrentEntity(model);
                        } catch (BrokenEntityObjectException | ServiceClientException | InterruptedException ex) {
                            Logger.getLogger(SelectorWidgetController.class.getName()).log(Level.FINE, ex.getMessage(), ex);
                        }
                    }
                }
            }
        }
    }
    
    public void displayLoadedEntities(final GroupModel model){
        if (getRowCount()<model.getEntitiesCount()){
            readMore(model, model.getEntitiesCount() - getRowCount());
        }
    }

    public void readMore(GroupModel model) {
        if (model == null) {
            model = this.model;
        }
        readMore(model, model.getReadPageSize());
    }
    
    private void readMore(final GroupModel model, final int count) {
        synchronized (readLock) {
            if (!readMore) {
                return;
            }
            for (int i = 0; i < count; i++) {
                try {
                    lastReaded++;
                    EntityModel entity;
                    try {
                        entity = model.getEntity(lastReaded);
                        if (entity == null) {
                            setReadMore(false);
                            break;
                        }
                        addRow(entity);

                    } catch (BrokenEntityObjectException ex) {
                        addRow(new BrokenEntityModel(model.getEnvironment(), model.getSelectorPresentationDef(), ex));
                    }
                } catch (ServiceClientException ex) {
                    setReadMore(false);
                    getModel().showException(ex);
                    break;
                } catch (InterruptedException ex) {
                    setReadMore(false);
                    break;
                } catch (RuntimeException e) {
                    setReadMore(false);
                    throw e;
                }
            }
        }
    }

    public boolean hasMoreData() {
        synchronized (readLock) {
            return readMore;
        }
    }

    public FindInSelectorDialog getFindDialog() {
        if (findDialog == null) {
            initFindDialog();
        }
        return findDialog;
    }
    
    public int getRowsLoadingLimit() {
        return selector.getEnvironment().getConfigStore().readInteger(ROWS_LIMIT_FOR_SEARCH_CONFIG_PATH, 100);
    }
    
    public IProgressHandle getSearchProgressHandle() {
        return searchProgressHandle;
    }
    
    private void initFindDialog() {
        findDialog = new FindInSelectorDialog(selector.getEnvironment(), selector.getModel().getConfigStoreGroupName(), false);
        findDialog.addFindActionListener(new IFindAndReplaceDialog.IFindActionListener() {
            @Override
            public void find() {
                findDialog.acceptDialog();
            }
        });
    }
    
    public void processEntityRemoved(Pid pid) {
    }
    
    public final void processErrorOnReceivingData(final Throwable exception) {
        final String title = selector.getModel().getEnvironment().getMessageProvider().translate("ExplorerException", "Error on receiving data");     
        selector.getModel().showException(title, exception);
    }

    public final void processColumnHeaderClick(final SelectorColumnModelItem columnItem,
            final EnumSet<EKeyboardModifier> keyboardModifiers) {
        final Id colId = columnItem.getId();
        final Id classDefId = model.getDefinition().getOwnerClassId();
        final RadClassPresentationDef classPresentation
                = model.getEnvironment().getDefManager().getClassPresentationDef(classDefId);
        final RadPropertyDef propDef = classPresentation.getPropertyDefById(colId);
        if (propDef != null && propDef.canBeUsedInSorting()) {
            final boolean isShiftKeyPressed = keyboardModifiers.contains(EKeyboardModifier.SHIFT);
            SelectorSortUtils.applySort(model, colId, isShiftKeyPressed);
        }
    }

    public final void updateSortingIndicators(final IGrid grid, final int startColumn) {
        final RadSortingDef currentSorting = model.getCurrentSorting();
        SelectorColumnModelItem columnItem;
        RadSortingDef.SortingItem sortingItem;
        for (int i = grid.getColumnCount() - 1; i >= startColumn; i--) {
            columnItem = getSelectorColumn(grid,i);
            if (currentSorting == null) {
                sortingItem = null;
            } else {
                sortingItem
                        = SelectorSortUtils.getSortItemById(currentSorting.getSortingColumns(), columnItem.getId());
            }
            if (sortingItem == null) {
                grid.getColumn(i).hideSortingIndicator();
            } else {
                final RadSortingDef.SortingItem.SortOrder sortOrder;
                if (sortingItem.sortDesc) {
                    sortOrder = RadSortingDef.SortingItem.SortOrder.DESC;
                } else {
                    sortOrder = RadSortingDef.SortingItem.SortOrder.ASC;
                }
                final int indexInSorting = currentSorting.getSortingColumns().indexOf(sortingItem);
                grid.getColumn(i).showSortingIndicator(sortOrder, indexInSorting + 1);
            }
        }
    }

    public void storeHeaderSettings(final IGrid grid) {//for Tree and Grid compability
        final String headerSetting = grid.getHeaderSettings();
        if (headerSetting!=null && !headerSetting.isEmpty()){
            final ClientSettings settings = getModel().getEnvironment().getConfigStore();        
            settings.beginGroup(getModel().getConfigStoreGroupName());
            settings.beginGroup(SettingNames.SYSTEM);
            settings.beginGroup(SettingNames.Selector.COLUMNS_GROUP);
            try {
                settings.writeInteger(STATE_FORMAT_VERSION_KEY, 1);
                settings.setValue(STATE_KEY, headerSetting);                
            } finally {
                settings.endGroup();
                settings.endGroup();
                settings.endGroup();
            }
        }
    }

    public void restoreHeaderSettings(final IGrid grid) {
        final ClientSettings settings = getModel().getEnvironment().getConfigStore();
        settings.beginGroup(getModel().getConfigStoreGroupName());
        settings.beginGroup(SettingNames.SYSTEM);
        settings.beginGroup(SettingNames.Selector.COLUMNS_GROUP);
        try {
            final int formatVersion = settings.readInteger(STATE_FORMAT_VERSION_KEY, 0);            
            String headerSettings = settings.readString(STATE_KEY);
            if (headerSettings != null) {
                if (formatVersion==0){
                    headerSettings = headerSettings.replace(",content;", ",weak_content;").replace(",stretch;", ",weak_stretch;");
                }
                grid.setHeaderSettings(headerSettings);
            }
        } finally {
            settings.endGroup();
            settings.endGroup();
            settings.endGroup();
        }
    }

    public void updateColumnsSizePolicy(final IGrid grid, final int startColumn) {
        final int columnsCount = grid.getColumnCount();
        for (int i = startColumn; i < columnsCount; i++) {
            IGrid.EColumnSizePolicy columnResizeMode = getSizePolicy(grid,i);
            grid.getColumn(i).setSizePolicy(columnResizeMode);
        }
    }
    
    public void updateColumnsVisibility(final IGrid grid, final List<IGrid.ColumnDescriptor> visible, final int startColumn){
        final Set<SelectorColumnModelItem> newVisibleColumns = new HashSet<>();
        for (IGrid.ColumnDescriptor cd : visible) {
            SelectorColumnModelItem item = getSelectorColumnFromColumnDescriptor(cd);
            newVisibleColumns.add(item);
        }
        
        final Set<SelectorColumnModelItem> currentVisibleColumns = new HashSet<>();
        for (int i = startColumn; i < grid.getColumnCount(); i++) {
            currentVisibleColumns.add(getSelectorColumn(grid, i));
        }
        
        int indexInGrid = 0;
        boolean visibleInGrid;
        for (SelectorColumnModelItem item : getSelectorColumns()) {
            visibleInGrid = currentVisibleColumns.contains(item);
            if (newVisibleColumns.contains(item)){
                item.setVisible(true);
                if (!visibleInGrid){
                    addColumn(indexInGrid + startColumn, item);
                }
                indexInGrid++;
            }else{
                item.setVisible(false);
                if (visibleInGrid){
                    removeColumn(indexInGrid + startColumn);
                }
            }
        }
        
        
        if (grid.getColumnCount() == startColumn) {
            clearRows();
        }
        updateSortingIndicators(grid, startColumn);
        updateColumnsSizePolicy(grid, startColumn);
    }
        
    public List<IGrid.ColumnDescriptor> getAllColumnDescriptors() {
        final List<IGrid.ColumnDescriptor> descs = new LinkedList<>();
        if (getModel() != null) {
            for (SelectorColumnModelItem column : getSelectorColumns()) {
                if (!column.isForbidden()){
                    descs.add(new SelectorColumnDescriptor(column));
                }
            }
        }
        return descs;
    }
    
    public List<IGrid.ColumnDescriptor> getVisibleColumnDescriptors(final IGrid grid, final List<IGrid.ColumnDescriptor> all, final int startColumn) {
        final List<IGrid.ColumnDescriptor> descs = new LinkedList<>();
        final Set<SelectorColumnModelItem> items = new HashSet<>();
        for (int i = startColumn; i < grid.getColumnCount(); i++) {
            if (grid.getColumn(i).isVisible()){
                items.add(getSelectorColumn(grid, i));
            }
        }
        for (IGrid.ColumnDescriptor cd : all) {
            if (items.contains(((SelectorColumnDescriptor) cd).getSelectorColumn())) {
                descs.add(cd);
            }
        }
        return descs;        
    }
    
    public void close(){
        model.getAsyncReader().removeListener(listener);  
        model.getAsyncReader().clean();
    }

    private static IGrid.EColumnSizePolicy getSizePolicy(final IGrid grid, final int colIndex){
        final SelectorColumnModelItem column = getSelectorColumn(grid, colIndex);
        final ESelectorColumnSizePolicy sizePolicy = getActualSizePolicy(column);        
        switch(sizePolicy){
            case RESIZE_BY_CONTENT:
                return IGrid.EColumnSizePolicy.WEAK_BY_CONTENT;
            case STRETCH:
                return IGrid.EColumnSizePolicy.WEAK_STRETCH;
            default:
                return IGrid.EColumnSizePolicy.INTERACTIVE;
        }
    }        
    
    private static SelectorColumnModelItem getSelectorColumnFromColumnDescriptor(IGrid.ColumnDescriptor cd){
        return ((SelectorColumnDescriptor)cd).getSelectorColumn();
    }
    
    private static SelectorColumnModelItem getSelectorColumn(final IGrid grid, int colIndex){
        final Object userData = grid.getColumn(colIndex).getUserData();
        return userData instanceof SelectorColumnModelItem ? (SelectorColumnModelItem)userData : null;        
    }

    static boolean canUseStandardCheckBox(final Property property) {
        final RadPropertyDef def = property.getDefinition();
        return def.getType() == EValType.BOOL
                && !def.isInheritable()
                && property.getEnabledCommands().isEmpty();
    }
    
    public static void refreshColumn(final IGrid grid, final SelectorColumnModelItem selectorColumn){
        for (int i = grid.getColumnCount() - 1; i >= 1; i--) {
            final IGrid.IColumn gridColumn = grid.getColumn(i);
            if (gridColumn.getUserData() == selectorColumn) {//NOPMD
                if (selectorColumn.getHeaderMode() == ESelectorColumnHeaderMode.ONLY_ICON) {
                    gridColumn.setTitle("");
                } else {
                    final String headerTitle = 
                            ClientValueFormatter.capitalizeIfNecessary(selectorColumn.getEnvironment(), selectorColumn.getTitle());
                    gridColumn.setTitle(headerTitle);
                }
                updateColumnIconAndToolTip(gridColumn, selectorColumn);
                final IGrid.EColumnSizePolicy columnResizeMode = getSizePolicy(grid,i);
                if (columnResizeMode!=gridColumn.getSizePolicy()){
                    gridColumn.setSizePolicy(columnResizeMode);
                }
                if (gridColumn.isVisible()!=selectorColumn.isVisible()){
                    gridColumn.setVisible(selectorColumn.isVisible());
                }
                break;
            }
        }        
    }
    
    public static void updateColumnIconAndToolTip(final IGrid.IColumn gridColumn, final SelectorColumnModelItem selectorColumn) {
        final ESelectorColumnHeaderMode headerMode = selectorColumn.getHeaderMode();
        if (headerMode == ESelectorColumnHeaderMode.ONLY_TEXT) {
            gridColumn.setIcon(null);
        } else {
            gridColumn.setIcon((WpsIcon) selectorColumn.getHeaderIcon());
        }
        final String toolTip = selectorColumn.getHint();
        if (headerMode == ESelectorColumnHeaderMode.ONLY_ICON && (toolTip == null || toolTip.isEmpty())) {
            gridColumn.setToolTip(selectorColumn.getTitle());
        } else {
            gridColumn.setToolTip(toolTip);
        }
    }
    
    
    public void restoreDefaultSettings(final IGrid grid, final boolean firstColumnMustBeVisible, final int startColumn){
        boolean firstColumn = true;
        final List<IGrid.ColumnDescriptor> visibleColumns = new LinkedList<>();
        for (SelectorColumnModelItem column: getSelectorColumns()){            
            if (getDefaultSizePolicy(column)!=getActualSizePolicy(column)){
                column.setSizePolicy(column.getColumnDef().getSizePolicy());
            }
            if ((firstColumnMustBeVisible && firstColumn)
                || (!column.isForbidden() && isVisible(column.getColumnDef().getVisibility()))){
                visibleColumns.add(new SelectorColumnDescriptor(column));
            }
            firstColumn = false;
        }
        updateColumnsVisibility(grid, visibleColumns, startColumn);
    }
    
    private static ESelectorColumnSizePolicy getDefaultSizePolicy(final SelectorColumnModelItem column){
        final ESelectorColumnSizePolicy policy = column.getColumnDef().getSizePolicy();
        return policy==ESelectorColumnSizePolicy.AUTO ? column.getAutoSizePolicy() : policy;
    }
    
    private static ESelectorColumnSizePolicy getActualSizePolicy(final SelectorColumnModelItem column){
        final ESelectorColumnSizePolicy policy = column.getSizePolicy();
        return policy==ESelectorColumnSizePolicy.AUTO ? column.getAutoSizePolicy() : policy;
    }    
    
    public boolean isDefaultColumnSettingsChanged(final IGrid grid, final boolean firstColumnIsAlwaysVisible){        
        final Id lastVisibleColumnId = getLastVisibleColumnId(grid);
        boolean firstColumn = true;
        for (SelectorColumnModelItem column: getSelectorColumns()){            
            if (getDefaultSizePolicy(column)!=getActualSizePolicy(column) 
                && (column.getSizePolicy()!=ESelectorColumnSizePolicy.STRETCH || !column.getId().equals(lastVisibleColumnId))){
                return true;
            }
            final boolean isVisibleByDefault = 
                (firstColumnIsAlwaysVisible && firstColumn) || isVisible(column.getColumnDef().getVisibility());
            if (!column.isForbidden() && isVisibleByDefault!=column.isVisible()){
                return true;
            }
            firstColumn = false;
        }
        return false;
    }
    
    private static Id getLastVisibleColumnId(final IGrid grid){
        SelectorColumnModelItem column;
        for (int i=grid.getColumnCount()-1; i>=0; i--){
            column = getSelectorColumn(grid, i);
            if (column!=null && grid.getColumn(i).isVisible()){
                return column.getId();
            }
        }
        return null;        
    }
    
    private static boolean isVisible(final ESelectorColumnVisibility visibility){
        return visibility==ESelectorColumnVisibility.INITIAL;
    }    
    
    public static IGrid.IColumn addColumn(final IGrid grid, final int index, final SelectorColumnModelItem selectorColumn){
        final ESelectorColumnHeaderMode headerMode = selectorColumn.getHeaderMode();        
        final String headerTitle;
        if (headerMode == ESelectorColumnHeaderMode.ONLY_ICON){            
            headerTitle = "";
        }else{
            headerTitle = 
                ClientValueFormatter.capitalizeIfNecessary(selectorColumn.getEnvironment(), selectorColumn.getTitle());
        }        
        final IGrid.IColumn gridColumn = grid.addColumn(index, headerTitle);
        setupColumn(gridColumn, selectorColumn);
        return gridColumn;
    }
    
    public static void setupColumn(final IGrid.IColumn gridColumn, final SelectorColumnModelItem selectorColumn){
        SelectorWidgetController.updateColumnIconAndToolTip(gridColumn, selectorColumn);
        gridColumn.setUserData(selectorColumn);
        gridColumn.setPersistenceKey(selectorColumn.getId().toString());
        gridColumn.setObjectName("rx_selector_col_#"+selectorColumn.getId().toString());
        gridColumn.setUserCanResizeByContent(true);
        gridColumn.addSizePolicyListener(new IGrid.IColumn.ISizePolicyListener() {
            @Override
            public void sizePolicyChanged(IGrid.IColumn gridColumn, EColumnSizePolicy oldPolicy, EColumnSizePolicy newPolicy) {
                if (gridColumn.getUserData() instanceof SelectorColumnModelItem){
                    final SelectorColumnModelItem selectorColumn = (SelectorColumnModelItem)gridColumn.getUserData();
                    ESelectorColumnSizePolicy sizePolicy;
                    switch(newPolicy){
                        case WEAK_BY_CONTENT:
                            sizePolicy = ESelectorColumnSizePolicy.RESIZE_BY_CONTENT;
                            break;
                        case WEAK_STRETCH:
                            sizePolicy = ESelectorColumnSizePolicy.STRETCH;
                            break;
                        default:
                            sizePolicy = ESelectorColumnSizePolicy.MANUAL_RESIZE;
                    }
                    if (getActualSizePolicy(selectorColumn)!=sizePolicy){
                        selectorColumn.setSizePolicy(sizePolicy);
                    }
                }
            }
        });        
    }
}
