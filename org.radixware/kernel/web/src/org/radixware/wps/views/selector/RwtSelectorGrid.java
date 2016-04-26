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

import java.awt.Color;
import java.util.EnumSet;
import java.util.List;
import org.radixware.kernel.common.client.enums.EKeyboardModifier;
import org.radixware.kernel.common.client.enums.ESelectorColumnHeaderMode;
import org.radixware.kernel.common.client.enums.ETextAlignment;
import org.radixware.kernel.common.client.enums.ETextOptionsMarker;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.models.BrokenEntityModel;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.items.ModelItem;
import org.radixware.kernel.common.client.models.items.SelectorColumnModelItem;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.client.widgets.actions.Action;
import org.radixware.kernel.common.client.widgets.actions.IMenu;
import org.radixware.kernel.common.client.widgets.actions.IToolBar;
import org.radixware.kernel.common.client.widgets.selector.ISelectorWidget;
import org.radixware.kernel.common.client.widgets.selector.SelectorModelDataLoader;
import org.radixware.kernel.common.enums.ESelectorColumnAlign;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Id;
import org.radixware.wps.HttpQuery;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.WpsSettings;
import org.radixware.wps.dialogs.BrokenEntityMessageDialog;
import org.radixware.wps.icons.WpsIcon;
import org.radixware.wps.rwt.Alignment;
import org.radixware.wps.rwt.Grid;
import org.radixware.wps.rwt.IGrid;
import org.radixware.wps.rwt.UIObject;
import org.radixware.wps.settings.ISettingsChangeListener;
import org.radixware.wps.text.WpsTextOptions;
import org.radixware.wps.views.RwtAction;

public class RwtSelectorGrid extends Grid implements ISelectorWidget {

    private final Grid.CurrentRowListener currentRowListener = new Grid.CurrentRowListener() {
        @Override
        public void currentRowChanged(Grid.Row oldRow, Grid.Row newRow) {
            if (oldRow == newRow) {
                return;
            }
            EntityModel newSelection = getEntity(newRow);
            if (newSelection instanceof BrokenEntityModel) {
                selector.showException(new BrokenEntityObjectException((BrokenEntityModel) newSelection));
            } else if (newSelection != null) {
                selector.setCurrentEntity(newSelection);
            }
            actions.refresh();
        }

        @Override
        public boolean beforeChangeCurrentRow(Grid.Row oldRow, Grid.Row newRow) {
            if (oldRow == newRow) {
                return true;
            }
            if (selector.leaveCurrentEntity(false)) {
                return true;
            } else {
                return false;
            }
        }
    };
    private final Grid.DoubleClickListener doubleClickListener = new Grid.DoubleClickListener() {
        @Override
        public void onDoubleClick(Grid.Row row, Grid.Cell cell) {
            EntityModel entity = getEntity(row);
            if (entity instanceof BrokenEntityModel) {
                new BrokenEntityMessageDialog((WpsEnvironment) getEnvironment(), (BrokenEntityModel) entity).execDialog();
            }
        }
    };
    private final Grid.ColumnHeaderClickListener headerClickListener = new Grid.ColumnHeaderClickListener() {
        @Override
        public void onClick(final Grid.Column column, final EnumSet<EKeyboardModifier> keyboardModifiers) {
            if (!selector.isDisabled()) {
                final SelectorColumnModelItem columnItem = (SelectorColumnModelItem) column.getUserData();
                if (columnItem != null) {
                    controller.processColumnHeaderClick(columnItem, keyboardModifiers);
                }
            }
        }
    };
    private final ISettingsChangeListener l = new ISettingsChangeListener() {
        @Override
        public void onSettingsChanged() {
            applySettings();
        }
    };
    private final RwtSelector selector;
    private boolean blockRead = false;
    private final SelectorWidgetController controller;
    private final WpsEnvironment env;
    private final SelectorModelDataLoader allDataLoader;

    @Override
    protected List<Grid.ColumnDescriptor> getAllColumnDescriptors() {
        return controller.getAllColumnDescriptors();
    }

    @Override
    protected void updateColumnsVisibility(final List<Grid.ColumnDescriptor> visible) {
        controller.updateColumnsVisibility(this, visible);
    }

    private Grid.Column addColumn(int index, SelectorColumnModelItem item) {
        final ESelectorColumnHeaderMode headerMode = item.getHeaderMode();
        final String headerTitle = headerMode == ESelectorColumnHeaderMode.ONLY_ICON ? "" : item.getTitle();
        Grid.Column c = addColumn(index, headerTitle);
        updateColumnIconAndToolTip(c, item);
        c.setUserData(item);
        c.setPersistenceKey(item.getId().toString());
        return c;
    }

    private void updateColumnIconAndToolTip(final Grid.Column gridColumn, final SelectorColumnModelItem selectorColumn) {
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

    @Override
    protected List<Grid.ColumnDescriptor> getVisibleColumnDescriptors(final List<Grid.ColumnDescriptor> all) {
        return controller.getVisibleColumnDescriptors(this, all);
    }

    public RwtSelectorGrid(final RwtSelector selector, final GroupModel model) {
        setSelectionMode(Grid.SelectionMode.ROW);
        setSelectionStyle(EnumSet.of(IGrid.ESelectionStyle.ROW_FRAME,IGrid.ESelectionStyle.CELL_FRAME));
        setBrowserFocusFrameEnabled(false);
        setRendererProvider(PropertyCellRendererProvider.getInstanceForGrid());
        setEditorProvider(PropertyCellEditorProvider.getInstance());
        env = (WpsEnvironment) getEnvironment();
        env.addSettingsChangeListener(l);        

        this.controller = new SelectorWidgetController(model, selector) {
            @Override
            protected void addRow(final EntityModel entity) {
                final Grid.Row row;
                if (entity instanceof BrokenEntityModel) {
                    row = RwtSelectorGrid.this.addRowWithSpannedCells();
                    final Grid.Cell cell = row.getCell(0);
                    final BrokenEntityModel brokenEntityModel = (BrokenEntityModel) entity;
                    final String pidAsStr = brokenEntityModel.getPid() == null ? "" : brokenEntityModel.getPid().toString();
                    if (brokenEntityModel.getExceptionMessage() == null) {
                        cell.setValue(pidAsStr + " " + brokenEntityModel.getExceptionClass());
                    } else {
                        cell.setValue(pidAsStr + " " + brokenEntityModel.getExceptionClass() + ": " + brokenEntityModel.getExceptionMessage());
                    }
                    cell.setForeground(Color.red);
                    row.setEditable(false);                    
                } else {
                    row = RwtSelectorGrid.this.addRow();                                        
                    int rowIndex = getRowCount() - 1;
                    for (int i = 0; i < getColumnCount(); i++) {
                        initCell(entity, i, rowIndex);
                    }
                }                
                row.setUserData(entity);
            }

            @Override
            protected void onContentsChanged() {
                checkHasMoreData();
            }

            @Override
            protected void clearWidget() {
                clear();
            }
            
            @Override
            protected void clearRows() {
                RwtSelectorGrid.this.clearRows();
            }            

            @Override
            public int getRowCount() {
                return RwtSelectorGrid.this.getRowCount();
            }

            @Override
            protected void addColumn(final int index, final SelectorColumnModelItem column) {
                RwtSelectorGrid.this.addColumn(index, column);
                for (int r = 0; r < getRowCount(); r++) {
                    final EntityModel entity = getEntity(getRow(r));
                    if (entity instanceof BrokenEntityModel == false) {
                        initCell(entity,index,r);
                    }
                }         
            }
            
        };
        for (SelectorColumnModelItem item : controller.getSelectorColumns()) {
            if (item.isVisible()) {
                addColumn(-1, item);
            }
        }
        this.selector = selector;
        addCurrentRowListener(currentRowListener);
        addDoubleClickListener(doubleClickListener);
        addColumnHeaderClickListener(headerClickListener);
        setPersistenceKey("sg" + model.getSelectorPresentationDef().getId().toString());
        blockRead = selector.isDisabled();
        final String confirmMovingToLastObjectMessage
                = env.getMessageProvider().translate("RwtSelectorGrid", "Number of loaded objects is %1s.\nDo you want to load next %2s objects?");
        allDataLoader = new SelectorModelDataLoader(env, selector);
        allDataLoader.setConfirmationMessageText(confirmMovingToLastObjectMessage);
        allDataLoader.setDontAskButtonVisibleInConfirmationDialog(false);
        allDataLoader.setProgressHeader(env.getMessageProvider().translate("Selector", "Moving to Last Object"));
        allDataLoader.setProgressTitleTemplate(env.getMessageProvider().translate("Selector", "Moving to Last Object...\nNumber of Loaded Objects: %1s"));
        allDataLoader.setDontAskButtonText(env.getMessageProvider().translate("Selector", "Load All Objects"));
        actions = new Actions(env);

        applySettings();
        setStickToRight(true);
    }
    
    private void initCell(final EntityModel entity, final int col, int row){
        final Grid.Column c = getColumn(col);
        final SelectorColumnModelItem item = (SelectorColumnModelItem) c.getUserData();
        final Grid.Cell cell = getRow(row).getCell(col);        
        final Property prop = getProperty(entity, item.getPropertyDef().getId());
        cell.setValue(prop);
        applyTextOptions(cell, item.getAlignment());        
    }

    public WpsTextOptions getTextOptions(Property property) {
        if (property == null) {
            return WpsTextOptions.Factory.getOptions((WpsEnvironment) getEnvironment(), ETextOptionsMarker.SELECTOR_ROW);
        } else {
            final EnumSet<ETextOptionsMarker> propertyMarkers = property.getTextOptionsMarkers();
            if (propertyMarkers.contains(ETextOptionsMarker.INVALID_VALUE)) {
                propertyMarkers.remove(ETextOptionsMarker.INVALID_VALUE);
            }
            return (WpsTextOptions) property.getValueTextOptions().getOptions(propertyMarkers);
        }
    }

    private void applySettings() {
        final WpsSettings settings = ((WpsEnvironment) getEnvironment()).getConfigStore();
        try {
            settings.beginGroup(SettingNames.SYSTEM);
            settings.beginGroup(SettingNames.SELECTOR_GROUP);
            settings.beginGroup(SettingNames.Selector.COMMON_GROUP);
            final int sliderValue = settings.readInteger(SettingNames.Selector.Common.SLIDER_VALUE, 4);
            this.shadeEvenRow(sliderValue);
            
            setCurrentCellFrameColor(settings.readColor(SettingNames.Selector.Common.FRAME_COLOR, Color.decode("#404040")));
            setCurrentRowFrameColor(settings.readColor(SettingNames.Selector.Common.ROW_FRAME_COLOR, Color.decode("#3399ff")));
            
            final Alignment alignmentFlag = Alignment.getForValue(settings.readInteger(SettingNames.Selector.Common.TITLES_ALIGNMENT, Alignment.CENTER.ordinal()));
            setHeaderAlignment(alignmentFlag);
        } finally {
            settings.endGroup();
            settings.endGroup();
            settings.endGroup();
        }        
        for (int j = 0; j < getRowCount(); j++) {
            final Grid.Row row = getRow(j);
            for (int i = 0; i < getColumnCount(); i++) {
                final Grid.Column c = getColumn(i);
                final SelectorColumnModelItem item = (SelectorColumnModelItem) c.getUserData();
                if (row.getCellCount() > i) {
                    final Grid.Cell cell = row.getCell(i);
                    applyTextOptions(cell, item.getAlignment());
                }
            }
        }       
    }

    private void applyTextOptions(Grid.Cell cell, ESelectorColumnAlign dataAlign) {
        final WpsSettings settings = ((WpsEnvironment) getEnvironment()).getConfigStore();
        try {
            if (cell.getValue() instanceof Property) {
                final Property prop = (Property) cell.getValue();
                final WpsTextOptions options = getTextOptions(prop);
                settings.beginGroup(SettingNames.SYSTEM);
                settings.beginGroup(SettingNames.SELECTOR_GROUP);
                settings.beginGroup(SettingNames.Selector.COMMON_GROUP);
                settings.beginGroup(SettingNames.Selector.Common.BODY_ALIGNMENT);
                final ESelectorColumnAlign a = ESelectorColumnAlign.getForValue(new Long(settings.readInteger(prop.getType().getName(), dataAlign.getValue().intValue())));

                ETextAlignment bodyAlignment;
                switch (a) {
                    case CENTER:
                        bodyAlignment = ETextAlignment.CENTER;
                        break;
                    case RIGHT:
                        bodyAlignment = ETextAlignment.RIGHT;
                        break;
                    case LEFT:
                    case DEFAULT:
                        bodyAlignment = ETextAlignment.LEFT;
                        break;
                    default:
                        bodyAlignment = ETextAlignment.LEFT;
                        break;
                }
                final WpsTextOptions cellOptions = 
                        WpsTextOptions.Factory.getOptions(bodyAlignment, options.getForegroundColor(), options.getBackgroundColor());
                cell.setTextOptions(cellOptions);
            }
        } finally {
            settings.endGroup();
            settings.endGroup();
            settings.endGroup();
            settings.endGroup();
        }
    }

    public void storeHeaderSettings() {
        controller.storeHeaderSettings(this);
    }

    public void restoreHeaderSettings() {
        controller.restoreHeaderSettings(this);
    }

    private int getRowIndex(Pid pid) {
        for (int i = 0; i < getRowCount(); i++) {
            EntityModel e = getEntity(getRow(i));

            if (e.getPid().equals(pid)) {
                return i;
            }
        }
        return -1;
    }

    protected EntityModel getEntity(Grid.Row row) {
        return row == null ? null : (EntityModel) row.getUserData();
    }

    protected Property getProperty(EntityModel entity, Id propId) {
        return entity.getProperty(propId);
    }

    protected Id getColumnId(Id propertyId) {
        return propertyId;
    }

    @Override
    public void lockInput() {
    }

    @Override
    public void unlockInput() {
    }

    @Override
    public void finishEdit() {
    }

    @Override
    public void rereadAndSetCurrent(Pid pid) throws InterruptedException, ServiceClientException {
        rereadInternal(pid);
    }

    @Override
    public void reread() throws InterruptedException, ServiceClientException {

        rereadInternal(null);

    }

    public boolean selectEntity(Pid pid) {
        final boolean currentEntity = 
            selector.getCurrentEntity()!=null && selector.getCurrentEntity().getPid().equals(pid);
        int rowIdx = getRowIndex(pid);
        if (rowIdx >= 0) {
            Grid.Row row = getRow(rowIdx);
            if (row != null) {
                if (currentEntity){
                    removeCurrentRowListener(currentRowListener);//do not to change current object in selector
                }
                try{
                    setCurrentCell(row.getCell(0));
                }finally{
                    if (currentEntity){
                        addCurrentRowListener(currentRowListener);
                    }
                }                
                return true;
            }
        }
        return false;
    }

    private void rereadInternal(final Pid pid) throws InterruptedException, ServiceClientException {
        setEnabled(true);
        lockInput();
        try {
            controller.reread();
            if (isEnabled()) {
                if (pid != null) {
                    int rowIdx = getRowIndex(pid);
                    if (rowIdx >= 0) {
                        Grid.Row row = getRow(rowIdx);
                        if (row != null) {
                            setCurrentCell(row.getCell(0));
                        } else if (getRowCount() > 0) {
                            setCurrentCell(getRow(0).getCell(0));
                        }
                    } else if (getRowCount() > 0) {
                        setCurrentCell(getRow(0).getCell(0));
                    }
                } else if (getRowCount() > 0) {
                    setCurrentCell(getRow(0).getCell(0));
                }
                controller.updateSortingIndicators(this);
            }
        } finally {
            unlockInput();
        }
        blockRead = false;
        selector.refresh();
    }

    @Override
    public void clear() {
        super.clearRows();
        blockRead = true;
        actions.refresh();
    }

    @Override
    public void entityRemoved(Pid pid) {
        if (pid == null) {
            return;
        }
        final EntityModel currentIndex = getEntity(getCurrentRow());
        if (currentIndex == null) {
            return;
        }
        final Pid currentPid = currentIndex.getPid();
        final boolean removingCurrentRow = pid.equals(currentPid);
        if (removingCurrentRow) {
            selector.getModel().finishEdit();
        }
        lockInput();
        try {
            int removedRow = getRowIndex(pid);
            if (removedRow < 0) {
                return;
            } else {
                Grid.Row row = getRow(removedRow);
                removeRow(row);
            }
            if (removingCurrentRow){
                selector.leaveCurrentEntity(true);
                if (removedRow >= getRowCount()) {
                    removedRow = getRowCount() - 1;
                }
                if (removedRow > 0) {
                    Grid.Row newRow = getRow(removedRow);
                    if (newRow != null) {
                        setCurrentCell(newRow.getCell(0));
                    }
                } else if (getRowCount() > 0) {
                    setCurrentCell(getRow(0).getCell(0));
                }
            }
            controller.readMore(null);

        } finally {
            unlockInput();
            actions.refresh();
        }
    }

    @Override
    public void setupSelectorMenu(IMenu menu) {
    }
    public final Actions actions;
    private final static String ROWS_LIMIT_FOR_NAVIGATION_CONFIG_PATH
            = SettingNames.SYSTEM + "/" + SettingNames.SELECTOR_GROUP + "/" + SettingNames.Selector.COMMON_GROUP + "/" + SettingNames.Selector.Common.ROWS_LIMIT_FOR_NAVIGATION;
    private final static String ROWS_LIMIT_FOR_RESTORING_POSITION_CONFIG_PATH
            = SettingNames.SYSTEM + "/" + SettingNames.SELECTOR_GROUP + "/" + SettingNames.Selector.COMMON_GROUP + "/" + SettingNames.Selector.Common.ROWS_LIMIT_FOR_RESTORING_POSITION;

    private final static class Icons extends ClientIcon.CommonOperations {

        private Icons(final String fileName) {
            super(fileName, true);
        }
        public static final Icons NEXT = new Icons("classpath:images/next.svg");
        public static final Icons PREV = new Icons("classpath:images/prev.svg");
        public static final Icons BEGIN = new Icons("classpath:images/begin.svg");
        public static final Icons END = new Icons("classpath:images/end.svg");
    }

    public final class Actions {

        public final RwtAction prevAction;
        public final RwtAction nextAction;
        public final RwtAction beginAction;
        public final RwtAction endAction;

        public Actions(final WpsEnvironment environment) {
            final MessageProvider mp = environment.getMessageProvider();
            Action.ActionListener nextListener = new Action.ActionListener() {
                @Override
                public void triggered(Action action) {
                    selectNextRow();
                }
            };
            nextAction = createAction(environment, Icons.NEXT, mp.translate("RwtSelectorGrid", "Next"), nextListener);

            Action.ActionListener prevListener = new Action.ActionListener() {
                @Override
                public void triggered(Action action) {
                    selectPrevRow();
                }
            };
            prevAction = createAction(environment, Icons.PREV, mp.translate("RwtSelectorGrid", "Previous"), prevListener);

            Action.ActionListener beginListener = new Action.ActionListener() {
                @Override
                public void triggered(Action action) {
                    selectFirstRow();
                }
            };
            beginAction = createAction(environment, Icons.BEGIN, mp.translate("RwtSelectorGrid", "First"), beginListener);

            Action.ActionListener endListener = new Action.ActionListener() {
                @Override
                public void triggered(Action action) {
                    selectLastRow();
                }
            };
            endAction = createAction(environment, Icons.END, mp.translate("RwtSelectorGrid", "Last"), endListener);
        }

        private RwtAction createAction(final WpsEnvironment environment,
                final ClientIcon icon,
                final String title,
                final Action.ActionListener listener) {
            final RwtAction action
                    = new RwtAction(environment, icon);
            action.setText(title);
            action.setToolTip(title);
            action.addActionListener(listener);
            action.setEnabled(false);
            return action;
        }

        public boolean currentEntityDefined() {
            if (selector.getCurrentEntity() == null && !selector.getGroupModel().isEmpty()) {
                final int currentIndex = getRowIndex(getCurrentRow());
                return currentIndex > -1 && selector.getGroupModel().isBrokenEntity(currentIndex);
            } else {
                return true;
            }
        }

        public void refresh() {
            final boolean currentEntityDefined = currentEntityDefined();
            final int rows = getRowCount();
            final int cur = getRowIndex(getCurrentRow()) > -1 && currentEntityDefined ? getRowIndex(getCurrentRow()) : -1;
            prevAction.setEnabled(true);
            beginAction.setEnabled(true);
            nextAction.setEnabled(true);
            endAction.setEnabled(true);
            if (cur == -1 || cur == 0) {
                prevAction.setEnabled(false);
                beginAction.setEnabled(false);
            }
            if (cur == -1 || cur == rows - 1) {
                nextAction.setEnabled(false);
                endAction.setEnabled(false);
            }
        }

        public void close() {
            prevAction.close();
            nextAction.close();
            beginAction.close();
            endAction.close();
        }
    }

    @Override
    public void setupSelectorToolBar(IToolBar toolBar) {
        final Action createAction = selector.getActions().getCreateAction();
        toolBar.insertAction(createAction, actions.beginAction);
        toolBar.insertAction(createAction, actions.prevAction);
        toolBar.insertAction(createAction, actions.nextAction);
        toolBar.insertAction(createAction, actions.endAction);
    }

    public void selectNextRow() {
        if (getCurrentCell() != null) {
            int currentCellIndex = getCurrentCell().getColumn().getIndex();
            Row nextRow = getRowIndex(getCurrentRow()) + 1 < getRowCount() ? getRow(getRowIndex(getCurrentRow()) + 1) : null;
            if (nextRow != null) {
                setCurrentCell(nextRow.getCell(currentCellIndex));
            }
        }
    }

    public void selectPrevRow() {
        if (getCurrentCell() != null) {
            int currentCellIndex = getCurrentCell().getColumn().getIndex();
            Row prevRow = getRowIndex(getCurrentRow()) - 1 >= 0 ? getRow(getRowIndex(getCurrentRow()) - 1) : null;
            if (prevRow != null) {
                setCurrentCell(prevRow.getCell(currentCellIndex));
            }
        }
    }

    public void selectFirstRow() {
        if (getRowCount() > 0) {
            setCurrentCell(getRow(0).getCell(0));
        }
    }

    public void selectLastRow() {
        final int rowsLoadingLimit
                = selector.getEnvironment().getConfigStore().readInteger(ROWS_LIMIT_FOR_NAVIGATION_CONFIG_PATH, 1000);
        allDataLoader.setLoadingLimit(rowsLoadingLimit);
        allDataLoader.resetCounters();
        int loadedRows = -1;
        try {
            try {
                loadedRows = allDataLoader.loadAll(new RwtSelectorWidgetDelegate(controller, selector.getGroupModel()));
            } catch (InterruptedException exception) {
                loadedRows = selector.getGroupModel().getEntitiesCount();
            } catch (ServiceClientException exception) {
                final String title = getEnvironment().getMessageProvider().translate("ExplorerException", "Error on receiving data");
                getEnvironment().getTracer().error(title, exception);
                selector.getGroupModel().showException(title, exception);
                loadedRows = -1;
            }
            if (loadedRows > 0 && selector.leaveCurrentEntity(false)) {
                int cellIndex = getCurrentCell().getCellIndex();
                setCurrentCell(getRow(loadedRows - 1).getCell(cellIndex));
            }
        } finally {
            if (loadedRows > 0) {
                //allDataLoader.increaseRowsLimit();
            }
        }
    }

    @Override
    public void afterPrepareCreate(EntityModel childEntity) {
    }

    @Override
    public void refresh(final ModelItem item) {
        if (item instanceof SelectorColumnModelItem) {
            final SelectorColumnModelItem selectorColumn = (SelectorColumnModelItem) item;
            final int idx = controller.getSelectorColumns().indexOf(item);
            if (idx >= 0) {
                controller.updateColumnsSizePolicy(this);
            }
            for (int i = getColumnCount() - 1; i >= 0; i--) {
                final Grid.Column gridColumn = getColumn(i);
                if (gridColumn.getUserData() == selectorColumn) {//NOPMD                    
                    if (selectorColumn.getHeaderMode() == ESelectorColumnHeaderMode.ONLY_ICON) {
                        gridColumn.setTitle("");
                    } else {
                        gridColumn.setTitle(selectorColumn.getTitle());
                    }
                    updateColumnIconAndToolTip(gridColumn, selectorColumn);
                    break;
                }
            }
        } else if (item instanceof Property) {
            controller.refresh((Property) item);
        }
        actions.refresh();
    }

    @Override
    public boolean setFocus(Property aThis) {
        return false;
    }

    @Override
    public void bind() {
        controller.updateSortingIndicators(this);
        controller.updateColumnsSizePolicy(this);
        controller.displayLoadedEntities(selector.getGroupModel());
        if (getCurrentCell() == null && getRowCount() > 0) {
            setCurrentCell(getRow(0).getCell(0));
        }        
    }

    @Override
    protected Runnable componentRendered(final HttpQuery query) {
        final Runnable inner = super.componentRendered(query);
        return new Runnable() {
            @Override
            public void run() {
                if (inner != null) {
                    inner.run();
                }
                String param = query.get(html.getId());
                if ("m".equals(param) && !blockRead) {
                    controller.readMore(null);
                    if (getCurrentCell() == null && getRowCount() > 0) {
                        setCurrentCell(getRow(0).getCell(0));
                    }
                }
            }
        };

    }

    @Override
    protected boolean hasMoreData() {
        return controller.hasMoreData();
    }

    @Override
    protected String getConfigStr() {
        StringBuilder sb = new StringBuilder();
        sb.append("vc[");
        for (int i = 0; i < getColumnCount(); i++) {
            SelectorColumnModelItem item = (SelectorColumnModelItem) getColumn(i).getUserData();
            if (i > 0) {
                sb.append(",");
            }
            sb.append(item.getId());
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public void setParent(final UIObject parent) {
        if (parent==null){//on close selector
            controller.close();
            if (l != null) {
                env.removeSettingsChangeListener(l);
            }            
        }
        super.setParent(parent);        
    }
    
    
}
