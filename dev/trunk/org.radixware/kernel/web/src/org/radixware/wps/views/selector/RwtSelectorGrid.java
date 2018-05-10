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
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EFontWeight;
import org.radixware.kernel.common.client.enums.EKeyboardModifier;
import org.radixware.kernel.common.client.enums.ETextAlignment;
import org.radixware.kernel.common.client.enums.ETextOptionsMarker;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.models.BrokenEntityModel;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.EntityObjectsSelection;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.GroupModelReader;
import org.radixware.kernel.common.client.models.items.ModelItem;
import org.radixware.kernel.common.client.models.items.SelectorColumnModelItem;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.types.GroupRestrictions;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.client.widgets.actions.Action;
import org.radixware.kernel.common.client.widgets.actions.IMenu;
import org.radixware.kernel.common.client.widgets.actions.IToolBar;
import org.radixware.kernel.common.client.widgets.selector.IMultiSelectionWidget;
import org.radixware.kernel.common.client.widgets.selector.ISelectorDataExportOptionsDialog;
import org.radixware.kernel.common.client.widgets.selector.ISelectorWidget;
import org.radixware.kernel.common.client.models.GroupModelCsvWriter;
import org.radixware.kernel.common.client.models.GroupModelXlsxWriter;
import org.radixware.kernel.common.client.types.AggregateFunctionCall;
import org.radixware.kernel.common.client.types.MatchOptions;
import org.radixware.kernel.common.client.types.SelectorColumnsStatistic;
import org.radixware.kernel.common.client.views.IDialog;
import org.radixware.kernel.common.client.views.IProgressHandle;
import org.radixware.kernel.common.client.widgets.IToolButton;
import org.radixware.kernel.common.client.widgets.selector.SelectorModelDataLoader;
import org.radixware.kernel.common.enums.EAggregateFunction;
import org.radixware.kernel.common.enums.EKeyEvent;
import org.radixware.kernel.common.enums.EMimeType;
import org.radixware.kernel.common.enums.ESelectionMode;
import org.radixware.kernel.common.enums.ESelectorColumnAlign;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.html.Div;
import org.radixware.kernel.common.html.Html;
import org.radixware.kernel.common.types.Id;
import org.radixware.wps.EWebBrowserEngineType;
import org.radixware.wps.HttpQuery;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.WpsSettings;
import org.radixware.wps.dialogs.BrokenEntityMessageDialog;
import org.radixware.wps.rwt.Alignment;
import org.radixware.wps.rwt.CheckBox;
import org.radixware.wps.rwt.Dialog;
import org.radixware.wps.rwt.Events;
import org.radixware.wps.rwt.Grid;
import org.radixware.wps.rwt.IGrid;
import org.radixware.wps.rwt.RwtMenu;
import org.radixware.wps.rwt.UIObject;
import org.radixware.wps.rwt.events.ClickHtmlEvent;
import org.radixware.wps.rwt.events.HtmlEvent;
import org.radixware.wps.rwt.events.KeyDownEventFilter;
import org.radixware.wps.rwt.events.KeyDownHtmlEvent;
import org.radixware.wps.rwt.events.MouseClickEventFilter;
import org.radixware.wps.settings.ISettingsChangeListener;
import org.radixware.wps.text.WpsTextOptions;
import org.radixware.wps.views.RwtAction;

public class RwtSelectorGrid extends Grid implements ISelectorWidget, IMultiSelectionWidget {
    
    private final static char CORNER_HEADER_CELL_SYMBOL = '\u2714';
    private final static Color DEFAULT_SELECTION_COLOR = new Color(0xFF, 0xFF, 0x66);
    private final static String MULTIPLE_SELECTION_ENABLED_BY_DEFAULT_CONFIG_PATH =
        SettingNames.SYSTEM + "/" + SettingNames.SELECTOR_GROUP + "/" + SettingNames.Selector.COMMON_GROUP + "/" + SettingNames.Selector.Common.MULTIPLE_SELECTION_MODE_ENABLED_BY_DEFAULT;    
    
    private static class RowSelectionCheckBox extends CheckBox{
        
        public RowSelectionCheckBox(){
            super();            
            getContentElement().setCss("display", "none");
            getHtml().setCss("border-collapse","collapse");
            getHtml().setCss("table-layout","fixed");
            getHtml().setCss("border","none");
            clearClickHandlers();
        }
    }
    
    private final class SelectionColumnCellRenderer extends UIObject implements CellRenderer{
        
        private final CheckBox check;
        private final Pid objectPid;
        
        public SelectionColumnCellRenderer(final Pid pid){
            super(new Div());
            objectPid = pid;            
            check = new RowSelectionCheckBox(){
                @Override
                protected void processHtmlEvent(final HtmlEvent event) {
                    final ClickHtmlEvent clickEvent = (ClickHtmlEvent)event;      
                    if (clickEvent.getButton()==MouseEvent.BUTTON1){
                        RwtSelectorGrid.this.onSelectionColumnRowClick(objectPid, clickEvent.getKeyboardModifiers());
                    }                    
                    this.setFocused(true);//to prevent restore focus on current cell and auto scroll to current row                    
                }
            };
            check.setParent(RwtSelectorGrid.this);
            check.subscribeToEvent(new MouseClickEventFilter(EKeyboardModifier.ANY));            
            html.add(check.getHtml());
            html.addClass("renderer");
            html.setCss("vertical-align", "middle");
            html.setCss("padding-left", "4px");
            html.setCss("overflow", "hidden");
            html.setCss("width", "100%");
            html.setCss("height", "100%"); 
            html.setAttr("nolayout", "true");
        }

        @Override
        public void update(final int r, final int c, final Object value) {
            if (value instanceof Boolean){
                check.setSelected((Boolean)value);
            }
        }

        @Override
        public void selectionChanged(final int r, 
                                     final int c, 
                                     final Object value, 
                                     final ICell cell, 
                                     final boolean isSelected) {            
        }

        @Override
        public void rowSelectionChanged(final boolean isRowSelected) {
        }

        @Override
        public UIObject getUI() {
            return this;
        }

        @Override
        public UIObject findObjectByHtmlId(final String id) {
            final UIObject result = super.findObjectByHtmlId(id);
            return result==null ? check.findObjectByHtmlId(id) : result;
        }

        @Override
        public void visit(final Visitor visitor) {
            super.visit(visitor);
            check.visit(visitor);
        }        
    }
    
    private final class CellRendererProvider implements IGrid.CellRendererProvider{

        @Override
        public CellRenderer newCellRenderer(final int r, final int c) {
            if (c==0){
                if (RwtSelectorGrid.this.getGroupModel().isBrokenEntity(r)){
                    return new Grid.DefaultRenderer();
                }else{
                    final EntityModel entityModel;
                    try{
                        entityModel = RwtSelectorGrid.this.getGroupModel().getEntity(r);
                    }catch(BrokenEntityObjectException | ServiceClientException | InterruptedException ex){
                        //we do not expect exceptions here
                        getEnvironment().getTracer().error(ex);
                        return new Grid.DefaultRenderer();
                    }                    
                    return new SelectionColumnCellRenderer(entityModel.getPid());
                }
            }else{
                return PropertyCellRendererProvider.getInstanceForGrid().newCellRenderer(r, c);
            }
        }        
    }
    
    private final class SelectionColumnHeaderCell extends IGrid.AbstractColumnHeaderCell{
        
        private final Html checkBoxIndicator = new Html("label");
        private final Html checkBox = new Div();
        private Boolean checked = Boolean.FALSE;
        
        public SelectionColumnHeaderCell(final IClientEnvironment environment){
            getHtml().setCss("overflow", "hidden");
            getHtml().setCss("vertical-align", "middle");
            getHtml().setCss("width", "100%");
            getHtml().add(checkBox);
            checkBox.setCss("position", "relative");
            checkBox.setCss("top", "4px");
            if (((WpsEnvironment)environment).getBrowserEngineType()==EWebBrowserEngineType.GECKO){
                checkBox.setCss("left", "6px");
            }else{
                checkBox.setCss("left", "8px");
            }
            checkBox.setCss("width", "12px");
            checkBox.setCss("height", "10px");
            checkBox.setCss("padding-bottom", "2px");
            checkBox.setCss("background-color", "white");
            checkBox.setCss("vertical-align", "middle");
            checkBox.setCss("border", "solid 1px #BBB");
            checkBox.setAttr("onclick", "default");
            
            checkBoxIndicator.setCss("vertical-align", "text-top");
            checkBoxIndicator.addClass("rwt-ui-element-text");
            checkBoxIndicator.setAttr("onclick", "default");
            checkBox.add(checkBoxIndicator);
            setPartiallySelected();
        }
        
        public void setUserCanSelect(final boolean canSelect){
            checkBoxIndicator.setAttr("onclick", canSelect ? "default" : null);
        }
        
        public boolean isUserCanSelect(){
            return checkBoxIndicator.getAttr("onclick")!=null;
        }
        
        @Override
        public String getTitle() {
            return null;
        }

        @Override
        public void setTitle(final String title) {
        }

        @Override
        public void processAction(final String actionName, final String actionParam) {
            if (Events.EVENT_NAME_ONCLICK.equals(actionName)){
                RwtSelectorGrid.this.onSelectionColumnHeaderClick();
            }
            super.processAction(actionName, actionParam);
        }                
        
        @Override
        public UIObject findObjectByHtmlId(final String id) {
            if (getHtmlId().equals(id) || checkBox.getId().equals(id) || checkBoxIndicator.getId().equals(id)){
                return this;
            }else{
                return null;
            }
        }
        
        public void setSelected(){
            if (!isSelected()){
                setForeground(Color.black);
                setCheckState(Boolean.TRUE);
            }
        }
        
        public boolean isSelected(){
            return checked==Boolean.TRUE;
        }
        
        public void setPartiallySelected(){
            if (!isPartiallySelected()){                
                setForeground(Color.gray);                
                setCheckState(null);
            }
        }
        
        public boolean isPartiallySelected(){
            return checked==null;
        }
        
        public void setUnselected(){
            if (!isUnselected()){                
                setForeground(Color.black);
                setCheckState(Boolean.FALSE);
            }
        }
        
        private String getCheckStateSymbol(final Boolean checkState){
            return checkState==Boolean.FALSE ? "\u200B" : "\u2714";
        }
        
        private void setCheckState(final Boolean checkState){
            checked = checkState;
            checkBoxIndicator.setInnerText(getCheckStateSymbol(checkState));
        }
        
        public boolean isUnselected(){
            return checked==Boolean.FALSE;
        }
        
        public void setForeground(final Color c){
            checkBox.setCss("color", c == null ? null : color2Str(c));
            checkBoxIndicator.setCss("color", c == null ? null : color2Str(c));
        }
    }
    
    private static enum ESelectAction{SELECT,UNSELECT,INVERT};
    
    private final Grid.CurrentRowListener currentRowListener = new Grid.CurrentRowListener() {
        @Override
        public void currentRowChanged(Grid.Row oldRow, Grid.Row newRow) {
            if (oldRow == newRow) {
                return;
            }
            RwtSelectorGrid.this.currentRowChanged(newRow);
        }

        @Override
        public boolean beforeChangeCurrentRow(Grid.Row oldRow, Grid.Row newRow) {
            if (oldRow == newRow) {
                return true;
            }
            if (selector.leaveCurrentEntity(RwtSelectorGrid.this.forcedChangeCurrentRow)
                || RwtSelectorGrid.this.forcedChangeCurrentRow) {
                RwtSelectorGrid.this.unsubscribeProperties();
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
                new BrokenEntityMessageDialog(env, (BrokenEntityModel) entity).execDialog();
            }
        }
    };
    private final Grid.ColumnHeaderClickListener columnHeaderClickListener = new Grid.ColumnHeaderClickListener() {
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
    
    private final Grid.RowHeaderDoubleClickListener rowHeaderDblClickListener = new Grid.RowHeaderDoubleClickListener() {
        @Override
        public void onDoubleClick(final Row row, final EnumSet<EKeyboardModifier> keyboardModifiers) {
            RwtSelectorGrid.this.onRowHeaderDoubleClick(row);
        }
    };
            
    private final ISettingsChangeListener l = new ISettingsChangeListener() {
        @Override
        public void onSettingsChanged() {
            applySettings();
        }
    };
    private final Grid.CornerHeaderCell.ClickListener cornerHeaderCellClickListener = 
            new Grid.CornerHeaderCell.ClickListener(){
                @Override
                public void onClick(final EnumSet<EKeyboardModifier> modifiers) {
                    onCornerHeaderCellClick();
                }                
            };
    private final GroupModel.SelectionListener selectionHandler = new GroupModel.SelectionListener(){
        @Override
        public void afterSelectionChanged(final EntityObjectsSelection selection) {
            if (!blockSelectionListener)
                updateHeaderCheckState();
        }
    };    
    
    private final RwtSelector selector;
    private boolean blockReadMore = false;
    private boolean blockSelectionListener;
    private boolean cornerHeaderCellClickEnabled;
    private boolean forcedChangeCurrentRow;
    private boolean firstRow = true;
    private final SelectorWidgetController controller;
    private final Column selectionColumn;
    private final SelectionColumnHeaderCell selectionHeaderCell;
    private final WpsEnvironment env;
    private final SelectorModelDataLoader allDataLoader;
    private int numberOfObjectsProcessedInSearch;
    private SelectorModelDataLoader findDataLoader;
    private Map<Property, Grid.Cell> subscribedProperties = new HashMap<>(8);

    @Override
    protected List<Grid.ColumnDescriptor> getAllColumnDescriptors() {
        return controller.getAllColumnDescriptors();
    }

    @Override
    protected void updateColumnsVisibility(final List<Grid.ColumnDescriptor> columnDescriptors) {
        if (columnDescriptors==null){
            super.updateColumnsVisibility(columnDescriptors);
            if (!isSelectionEnabled() && getCurrentCell()!=null && getCurrentCell().getCellIndex()==0){
                final Grid.Row row = getCurrentRow();
                if (row.getCellCount()>0){
                    if (row.getCellCount()>1){
                        setCurrentCell(getCurrentRow().getCell(1));
                    }else{//broken entity model
                        setCurrentCell(getCurrentRow().getCell(0));
                    }
                }                
            }
        }else{
            controller.updateColumnsVisibility(this, columnDescriptors, 1);
        }
    }

    @Override
    protected List<Grid.ColumnDescriptor> getVisibleColumnDescriptors(final List<Grid.ColumnDescriptor> all) {
        return controller.getVisibleColumnDescriptors(this, all, 1);
    }

    @Override
    protected void restoreDefaultColumnSettings() {
        controller.restoreDefaultSettings(this, false, 1);
    }

    @Override
    protected boolean showRestoreDefaultColumnSettingsButton() {
        return controller.isDefaultColumnSettingsChanged(this, false);
    }    
    
    public RwtSelectorGrid(final RwtSelector selector, final GroupModel model) {
        super();
        env = (WpsEnvironment) getEnvironment();
        findDataLoader = new SelectorModelDataLoader(env);
        findDataLoader.setConfirmationMessageText(env.getMessageProvider().translate("Selector", "%1s objects were loaded, but the required one was not found.\nIt is recommended to use filter to find specific objects.\nDo you want to continue searching among the next %2s objects?"));
        findDataLoader.setDontAskButtonVisibleInConfirmationDialog(false);
        setSelectionMode(Grid.SelectionMode.ROW);
        setSelectionStyle(EnumSet.of(IGrid.ESelectionStyle.ROW_FRAME, IGrid.ESelectionStyle.CELL_FRAME));
        setBrowserFocusFrameEnabled(false);
        selectionHeaderCell = new SelectionColumnHeaderCell(env);
        selectionColumn = addColumn("", selectionHeaderCell);
        if (env.getBrowserEngineType()==EWebBrowserEngineType.GECKO){
            selectionColumn.setFixedWidth(28);
        }else if (env.getBrowserEngineType()==EWebBrowserEngineType.MSIE){
            selectionColumn.setFixedWidth(31);
        }else{
            selectionColumn.setFixedWidth(33);
        }
        setRendererProvider(new CellRendererProvider());
        setEditorProvider(PropertyCellEditorProvider.getInstance());        
        env.addSettingsChangeListener(l);

        this.controller = new SelectorWidgetController(model, selector) {
            @Override
            protected void addRow(final EntityModel entity) {
                final Grid.Row row;
                final String rowName;
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
                    rowName = pidAsStr==null ? null : brokenEntityModel.getPid().getTableId().toString()+"_"+pidAsStr;
                } else {
                    row = RwtSelectorGrid.this.addRow();
                    final int rowIndex = getRowCount() - 1;
                    final int columnCount = getColumnCount();        
                    final List<ETextAlignment> alignment = new ArrayList<>(columnCount);
                    alignment.add(ETextAlignment.CENTER);
                    final WpsSettings settings = env.getConfigStore();
                    settings.beginGroup(SettingNames.SYSTEM);
                    settings.beginGroup(SettingNames.SELECTOR_GROUP);
                    settings.beginGroup(SettingNames.Selector.COMMON_GROUP);
                    settings.beginGroup(SettingNames.Selector.Common.BODY_ALIGNMENT);
                    try{
                        for (int i = 1; i <columnCount; i++) {
                            alignment.add(getCellTextAlignment(getColumn(i), settings));
                        }
                    }finally{
                        settings.endGroup();
                        settings.endGroup();
                        settings.endGroup();
                        settings.endGroup();
                    }
                    for (int i = 1; i < columnCount; i++) {
                        initCell(entity, i, rowIndex, alignment.get(i));
                    }
                    
                    if (getGroupModel().getSelection().isObjectSelected(entity)){
                        row.setPrimaryBackgroundColor(getSelectionColor());
                    }
                    final Pid entityPid = entity.getPid();
                    rowName = entityPid.getTableId().toString()+"_"+entityPid.toString();
                    if (RwtSelectorGrid.this.firstRow){
                        RwtSelectorGrid.this.firstRow = false;
                        if (RwtSelectorGrid.this.isSelectionAllowed()){
                            if (settings.readBoolean(MULTIPLE_SELECTION_ENABLED_BY_DEFAULT_CONFIG_PATH, false)){
                               RwtSelectorGrid.this.enableSelection();
                            }
                        }                        
                    }
                }
                row.setUserData(entity);
                if (rowName==null){
                    row.setObjectName("rx_selector_row");
                }else{
                    row.setObjectName("rx_selector_row_"+rowName);
                }
            }

            @Override
            protected void onContentsChanged() {
                checkHasMoreData();
            }

            @Override
            protected void clearWidget() {
                RwtSelectorGrid.this.clearRows();                
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
            protected void addColumn(final int index, final SelectorColumnModelItem selectorColumn) {
                SelectorWidgetController.addColumn(RwtSelectorGrid.this, index, selectorColumn);
                final ETextAlignment cellAlignment = getCellTextAlignment(selectorColumn);
                final EntityObjectsSelection objectsSelection = getGroupModel().getSelection();
                final Row currentRow = getCurrentRow();
                Row row;                
                for (int r = 0; r < getRowCount(); r++) {
                    row = getRow(r);
                    final EntityModel entity = getEntity(row);
                    if  (entity instanceof BrokenEntityModel == false) {
                        if (objectsSelection.isObjectSelected(entity)){
                            initCell(entity, index, r, cellAlignment);
                        }else{
                            initCell(entity, index, r, cellAlignment);
                        }
                        if (currentRow==row){
                            final Cell cell = row.getCell(index);
                            if (cell.getValue() instanceof Property){
                                final Property property= ((Property)cell.getValue());
                                property.subscribe(RwtSelectorGrid.this);
                                subscribedProperties.put(property, cell);
                            }
                        }
                    }
                }    
                selectorColumn.subscribe(RwtSelectorGrid.this);
            }

            @Override
            protected void removeColumn(final int index) {
                final Grid.Column column = getColumn(index);
                if (column.getUserData() instanceof SelectorColumnModelItem){
                    final SelectorColumnModelItem item = (SelectorColumnModelItem)column.getUserData();
                    Property subscribedProperty = null;
                    for (Property property: subscribedProperties.keySet()){
                        if (property.getId().equals(item.getPropertyDef().getId())){
                            subscribedProperty = property;
                            break;
                        }
                    }
                    if (subscribedProperty!=null){
                        subscribedProperty.unsubscribe(RwtSelectorGrid.this);
                        subscribedProperties.remove(subscribedProperty);
                    }
                }
            }            
            
        };
        for (SelectorColumnModelItem item : controller.getSelectorColumns()) {
            if (item.isVisible()) {
                SelectorWidgetController.addColumn(this, -1, item);
            }
        }
        disableSelection();
        this.selector = selector;
        addCurrentRowListener(currentRowListener);
        addDoubleClickListener(doubleClickListener);
        addColumnHeaderClickListener(columnHeaderClickListener);
        addRowHeaderDoubleClickListener(rowHeaderDblClickListener);
        setPersistenceKey("sg" + model.getSelectorPresentationDef().getId().toString());
        blockReadMore = selector.isDisabled();
        final String confirmMovingToLastObjectMessage
                = env.getMessageProvider().translate("RwtSelectorGrid", "Number of loaded objects is %1s.\nDo you want to load next %2s objects?");
        allDataLoader = new SelectorModelDataLoader(env);
        allDataLoader.setConfirmationMessageText(confirmMovingToLastObjectMessage);
        allDataLoader.setDontAskButtonVisibleInConfirmationDialog(false);
        allDataLoader.setProgressHeader(env.getMessageProvider().translate("Selector", "Moving to Last Object"));
        allDataLoader.setProgressTitleTemplate(env.getMessageProvider().translate("Selector", "Moving to Last Object...\nNumber of Loaded Objects: %1s"));
        allDataLoader.setDontAskButtonText(env.getMessageProvider().translate("Selector", "Load All Objects"));
        actions = new Actions(env);        
        setRowHeaderVisible(true);
        applySettings();
        setStickToRight(true);
    }

    private void initCell(final EntityModel entity, final int col, final int row, final ETextAlignment cellAlignment) {
        final Grid.Column c = getColumn(col);
        final SelectorColumnModelItem item = (SelectorColumnModelItem) c.getUserData();
        final Grid.Cell cell = getRow(row).getCell(col);
        final Id propertyId = item.getPropertyDef().getId();
        final Property prop = getProperty(entity, propertyId);
        cell.setValue(prop);
        applyTextOptions(cell, cellAlignment);
        cell.setObjectName("rx_selector_cell_"+propertyId.toString());
    }

    public WpsTextOptions getTextOptions(Property property) {
        if (property == null) {
            return WpsTextOptions.Factory.getOptions(env, ETextOptionsMarker.SELECTOR_ROW);
        } else {
            final EnumSet<ETextOptionsMarker> propertyMarkers = property.getTextOptionsMarkers();
            if (propertyMarkers.contains(ETextOptionsMarker.INVALID_VALUE)) {
                propertyMarkers.remove(ETextOptionsMarker.INVALID_VALUE);
            }
            return (WpsTextOptions) property.getValueTextOptions().getOptions(propertyMarkers);
        }
    }
    
    private Color getSelectionColor(){
        final WpsSettings settings = env.getConfigStore();
        settings.beginGroup(SettingNames.SYSTEM);
        settings.beginGroup(SettingNames.SELECTOR_GROUP);
        settings.beginGroup(SettingNames.Selector.COMMON_GROUP);
        try{
            return settings.readColor(SettingNames.Selector.Common.SELECTED_ROW_COLOR, DEFAULT_SELECTION_COLOR);
        }finally{
            settings.endGroup();
            settings.endGroup();
            settings.endGroup();
        }
    }
    
    private void paintSelection(final int startRow, final int endRow){
        final Color selectionColor = getSelectionColor();
        for (int i = startRow, count = Math.min(endRow, getRowCount()); i < count; i++) {
            final Grid.Row row = getRow(i);
            paintSelection(row, selectionColor);            
        }
    }
    
    private void paintSelection(){
        paintSelection(0, getRowCount());
    }
    
    private void paintSelection(final Grid.Row row, final Color selectionColor){
        final boolean isSelected = isRowSelected(row);
        row.setPrimaryBackgroundColor(isSelected ? selectionColor : null);
        row.getCell(0).setValue(isSelected);
    }

    private void applySettings() {
        final WpsSettings settings = env.getConfigStore();
        settings.beginGroup(SettingNames.SYSTEM);
        settings.beginGroup(SettingNames.SELECTOR_GROUP);
        settings.beginGroup(SettingNames.Selector.COMMON_GROUP);        
        try {
            final int sliderValue = settings.readInteger(SettingNames.Selector.Common.SLIDER_VALUE, 4);
            this.shadeEvenRow(sliderValue);

            setCurrentCellFrameColor(settings.readColor(SettingNames.Selector.Common.FRAME_COLOR, Color.decode("#404040")));
            setCurrentRowFrameColor(settings.readColor(SettingNames.Selector.Common.ROW_FRAME_COLOR, Color.decode("#3399ff")));

            final Alignment alignmentFlag = Alignment.getForValue(settings.readInteger(SettingNames.Selector.Common.TITLES_ALIGNMENT, Alignment.CENTER.ordinal()));
            setColumnsHeaderAlignment(alignmentFlag);
        } finally {
            settings.endGroup();
            settings.endGroup();
            settings.endGroup();
        }
        
        final int columnCount = getColumnCount();        
        final List<ETextAlignment> alignment = new ArrayList<>(columnCount);        
        alignment.add(ETextAlignment.CENTER);
        settings.beginGroup(SettingNames.SYSTEM);
        settings.beginGroup(SettingNames.SELECTOR_GROUP);
        settings.beginGroup(SettingNames.Selector.COMMON_GROUP);
        settings.beginGroup(SettingNames.Selector.Common.BODY_ALIGNMENT);
        try{
            for (int i = 1; i <columnCount; i++) {
                alignment.add(getCellTextAlignment(getColumn(i), settings));
            }
        }finally{
            settings.endGroup();
            settings.endGroup();
            settings.endGroup();
            settings.endGroup();
        }
        
        final Color selectionColor = getSelectionColor();
        for (int j = 0; j < getRowCount(); j++) {
            final Grid.Row row = getRow(j);
            for (int i = 1; i < columnCount; i++) {
                if (row.getCellCount() > i) {
                    final Grid.Cell cell = row.getCell(i);
                    applyTextOptions(cell, alignment.get(i));
                }
            }
            paintSelection(row, selectionColor);
            final boolean isSelected = isRowSelected(row);
            row.setPrimaryBackgroundColor(isSelected ? selectionColor : null);
        }
    }
    
    private ETextAlignment getCellTextAlignment(final Grid.Column column, final WpsSettings settings){
        return getCellTextAlignment((SelectorColumnModelItem)column.getUserData(), settings);
    }
    
    private ETextAlignment getCellTextAlignment(final SelectorColumnModelItem item, final WpsSettings settings){
        final int colAlignment = item.getAlignment().getValue().intValue();
        final String typeName = item.getPropertyDef().getType().getName();
        final ESelectorColumnAlign columnAlignment = 
            ESelectorColumnAlign.getForValue(Long.valueOf(settings.readInteger(typeName, colAlignment)));
        switch (columnAlignment) {
            case CENTER:
                return ETextAlignment.CENTER;
            case RIGHT:
                return ETextAlignment.RIGHT;
            case LEFT:
            case DEFAULT:
                return ETextAlignment.LEFT;
            default:
                return ETextAlignment.LEFT;
        }        
    }
    
    private ETextAlignment getCellTextAlignment(final SelectorColumnModelItem item){
        final WpsSettings settings = env.getConfigStore();
        settings.beginGroup(SettingNames.SYSTEM);
        settings.beginGroup(SettingNames.SELECTOR_GROUP);
        settings.beginGroup(SettingNames.Selector.COMMON_GROUP);
        settings.beginGroup(SettingNames.Selector.Common.BODY_ALIGNMENT);
        try{
            return getCellTextAlignment(item, settings);
        }finally{
            settings.endGroup();
            settings.endGroup();
            settings.endGroup();
            settings.endGroup();            
        }
    }    

    private void applyTextOptions(final Grid.Cell cell) {
        if (cell.getValue() instanceof Property) {
            final ETextAlignment bodyAlignment = 
                getCellTextAlignment((SelectorColumnModelItem)cell.getColumn().getUserData());
            applyTextOptions(cell, bodyAlignment);
        }
    }
    
    private void applyTextOptions(final Grid.Cell cell, final ETextAlignment bodyAlignment) {
        if (cell.getValue() instanceof Property) {
            final Property prop = (Property) cell.getValue();
            final WpsTextOptions options = getTextOptions(prop);
            WpsTextOptions cellOptions
                    = WpsTextOptions.Factory.getOptions(bodyAlignment, options.getForegroundColor(), options.getBackgroundColor());
            final EFontWeight fontWeigth = options.getFont()==null ? EFontWeight.NORMAL : options.getFont().getWeight();
            if (fontWeigth!=EFontWeight.NORMAL){
                cellOptions = cellOptions.changeFontWeight(fontWeigth);
            }
            cell.setTextOptions(cellOptions);            
        }
    }        

    public void storeHeaderSettings() {
        controller.storeHeaderSettings(this);
    }

    public void restoreHeaderSettings() {
        controller.restoreHeaderSettings(this);
    }

    private int getRowIndex(final Collection<Pid> pids) {
        for (int i = 0,count=getRowCount(); i < count; i++) {
            final EntityModel e = getEntity(getRow(i));
            if (pids.contains(e.getPid())) {
                return i;
            }
        }
        return -1;
    }
    
    protected final boolean isRowSelected(final Grid.Row row){
        final EntityModel entityModel = getEntity(row);
        return entityModel!=null && getGroupModel().getSelection().isObjectSelected(entityModel);
    }

    protected EntityModel getEntity(final Grid.Row row) {
        return row == null ? null : (EntityModel) row.getUserData();
    }
    
    protected boolean isBrokenEntity(final Grid.Row row){
        return getEntity(row) instanceof BrokenEntityModel;
    }

    protected Property getProperty(final EntityModel entity, final Id propId) {
        return entity.getProperty(propId);
    }

    protected Id getColumnId(final Id propertyId) {
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
    public void rereadAndSetCurrent(final Pid pid) throws InterruptedException, ServiceClientException {
        rereadInternal(Collections.singleton(pid));
    }

    @Override
    public void rereadAndSetCurrent(final Collection<Pid> pids) throws InterruptedException, ServiceClientException {
        if (pids == null) {
            final EntityModel currentEntityObject = getEntity(getCurrentRow());
            if (currentEntityObject == null || currentEntityObject instanceof BrokenEntityModel) {
                rereadInternal(null);
            }else{
                rereadInternal(Collections.singleton(currentEntityObject.getPid()));
            }
        } else{        
            rereadInternal(pids);
        }
    }        

    @Override
    public void reread() throws InterruptedException, ServiceClientException {
        rereadInternal(null);
    }

    public boolean selectEntity(final Pid pid) {
        final boolean currentEntity
                = selector.getCurrentEntity() != null && selector.getCurrentEntity().getPid().equals(pid);
        int rowIdx = getRowIndex(Collections.singleton(pid));
        if (rowIdx >= 0) {
            Grid.Row row = getRow(rowIdx);
            if (row != null) {
                if (currentEntity) {
                    removeCurrentRowListener(currentRowListener);//do not to change current object in selector
                }
                try {
                    if (row.getCellCount()>1){
                        setCurrentCell(row.getCell(1));
                    }else{//broken entity model
                        setCurrentCell(row.getCell(0));
                    }
                } finally {
                    if (currentEntity) {
                        addCurrentRowListener(currentRowListener);
                    }
                }
                return true;
            }
        }
        return false;
    }

    private void rereadInternal(final Collection<Pid> pids) throws InterruptedException, ServiceClientException {
        setEnabled(true);
        lockInput();
        blockSelectionListener = true;
        blockReadMore = true;
        try {
            controller.reread();
            if (isEnabled()) {
                Grid.Row row;
                if (pids != null && !pids.isEmpty()) {
                    int rowIdx = getRowIndex(pids);
                    if (rowIdx >= 0) {
                        row = getRow(rowIdx);
                        if (row == null && getRowCount() > 0) {
                            row = getRow(0);
                        }
                    } else if (getRowCount() > 0) {
                        row = getRow(0);                        
                    }else{
                        row = null;
                    }
                } else if (getRowCount() > 0) {
                    row = getRow(0);                    
                }else{
                    row = null;
                }
                if (row!=null && row.getCellCount()>0){
                    if (row.getCellCount()>1){
                        setCurrentCell(row.getCell(1));
                    }else{//broken entity model
                        setCurrentCell(row.getCell(0));
                    }
                }
                controller.updateSortingIndicators(this, 1);
            }
        } finally {
            blockSelectionListener = false;
            unlockInput();
            blockReadMore = false;
        }        
        selector.refresh();
        updateHeaderCheckState();
    }

    @Override
    public void clear() {
        super.clearRows();
        blockReadMore = true;
        refreshCornerHeaderCell();
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
        forcedChangeCurrentRow = true;
        try {
            int removedRow = getRowIndex(Collections.singleton(pid));
            if (removedRow < 0) {
                return;
            } else {
                Grid.Row row = getRow(removedRow);
                removeRow(row);
                getGroupModel().removeRow(removedRow);
            }
            if (removingCurrentRow) {              
                unsubscribeProperties();
                selector.leaveCurrentEntity(true);                
                if (removedRow >= getRowCount()) {
                    removedRow = getRowCount() - 1;
                }
                final Grid.Row row;
                if (removedRow > 0) {
                    row = getRow(removedRow);
                } else if (getRowCount() > 0) {
                    row = getRow(0);                    
                } else{      
                    row = null;
                }
                if (row != null && row.getCellCount()>0) {
                    if (row.getCellCount()>1){
                        setCurrentCell(row.getCell(1));
                    }else{//broken entity model
                        setCurrentCell(row.getCell(0));
                    }
                }
                
            }
            controller.readMore(null);
        } finally {
            forcedChangeCurrentRow = false;
            unlockInput();
            actions.refresh();            
        }
    }

    @Override
    public void setupSelectorMenu(IMenu menu) {
        final Action createAction = selector.getActions().getCreateAction();
        menu.insertAction(createAction, actions.beginAction);
        menu.insertAction(createAction, actions.prevAction);
        menu.insertAction(createAction, actions.nextAction);
        menu.insertAction(createAction, actions.endAction);
        menu.insertAction(createAction, actions.findAction);
        menu.insertAction(createAction, actions.findNextAction);
        menu.insertSeparator(createAction);
        final Action showFilterAndOrderToolBarAction =
                selector.getActions().getShowFilterAndOrderToolBarAction();
        final List<Action> allActions = Arrays.asList(((RwtMenu) menu).getActions());
        final int showFilterAndOrderToolBarActionIndex = allActions.indexOf(showFilterAndOrderToolBarAction);
        if (showFilterAndOrderToolBarActionIndex < 1 || showFilterAndOrderToolBarActionIndex == allActions.size() - 1) {
            menu.addAction(actions.exportCSVAction);
            menu.addAction(actions.exportExcelAction);
            menu.addAction(actions.calcStatisticAction);
        } else {
            final Action beforeAction = allActions.get(showFilterAndOrderToolBarActionIndex);
            ((RwtMenu) menu).insertAfterAction(beforeAction, actions.exportExcelAction);
            ((RwtMenu) menu).insertAfterAction(actions.exportExcelAction, actions.exportCSVAction);
            ((RwtMenu) menu).insertAfterAction(actions.exportCSVAction, actions.calcStatisticAction);
        }
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
        public static final Icons EXPORT = new Icons("classpath:images/export_s.svg");
        public static final Icons EXPORTCSV = new Icons("classpath:images/csv.svg");
        public static final Icons EXPORTXLSX = new Icons("classpath:images/xlsx.svg");
    }

    public final class Actions {
        public final RwtAction findAction;
        public final RwtAction findNextAction;
        public final RwtAction prevAction;
        public final RwtAction nextAction;
        public final RwtAction beginAction;
        public final RwtAction endAction;
        public final RwtAction exportAction;
        public final RwtAction exportCSVAction;
        public final RwtAction exportExcelAction;
        public final RwtAction calcStatisticAction;

        public Actions(final WpsEnvironment environment) {
            final MessageProvider mp = environment.getMessageProvider();
            
            Action.ActionListener findListener = new Action.ActionListener() {
                @Override
                public void triggered(Action action) {
                    find();
                }
            };
            findAction = createAction(environment, ClientIcon.CommonOperations.FIND, mp.translate("RwtSelectorGrid", "Find"), "find_obj", findListener);
            
             Action.ActionListener findNextListener = new Action.ActionListener() {
                @Override
                public void triggered(Action action) {
                    findNext();
                }
            };
            findNextAction = createAction(environment, ClientIcon.CommonOperations.FIND_NEXT, mp.translate("RwtSelectorGrid", "Find Next"), "find_next_obj", findNextListener);
            
            Action.ActionListener nextListener = new Action.ActionListener() {
                @Override
                public void triggered(Action action) {
                    selectNextRow();
                }
            };
            nextAction = createAction(environment, Icons.NEXT, mp.translate("RwtSelectorGrid", "Next"), "go_to_next_obj", nextListener);

            Action.ActionListener prevListener = new Action.ActionListener() {
                @Override
                public void triggered(Action action) {
                    selectPrevRow();
                }
            };
            prevAction = createAction(environment, Icons.PREV, mp.translate("RwtSelectorGrid", "Previous"), "go_to_prev_obj", prevListener);

            Action.ActionListener beginListener = new Action.ActionListener() {
                @Override
                public void triggered(Action action) {
                    selectFirstRow();
                }
            };
            beginAction = createAction(environment, Icons.BEGIN, mp.translate("RwtSelectorGrid", "First"), "go_to_first_obj", beginListener);

            Action.ActionListener endListener = new Action.ActionListener() {
                @Override
                public void triggered(Action action) {
                    selectLastRow();
                }
            };
            endAction = createAction(environment, Icons.END, mp.translate("RwtSelectorGrid", "Last"), "go_to_last_obj", endListener);
            
            exportAction = new RwtAction(environment, Icons.EXPORT, mp.translate("Selector", "Export Selector Content in CSV format"));
            
            Action.ActionListener exportListener = new Action.ActionListener() {
                @Override
                public void triggered(Action action) {
                    export();
                }
            };
            exportCSVAction = 
                createAction(environment, Icons.EXPORTCSV, mp.translate("Selector", "Export Selector Content in CSV format"), "export_content", exportListener);
            
            Action.ActionListener exportExcelListener = new Action.ActionListener() {
                @Override
                public void triggered(Action action) {
                    exportExcel();
                }
            };
            exportExcelAction = 
                    createAction(environment, Icons.EXPORTXLSX, mp.translate("Selector", "Export Selector Content in XLSX format"), "export_excel_content()", exportExcelListener);
            
            Action.ActionListener calcSelectionStatisticListener = new Action.ActionListener() {
                @Override
                public void triggered(Action action) {
                    calcSelectionStatistic();
                }
            };            
            calcStatisticAction = 
                createAction(environment, ClientIcon.Selector.CALC_STATISTIC, mp.translate("Selector", "Statistics"), "calc_statistic", calcSelectionStatisticListener);
        }

        private RwtAction createAction(final WpsEnvironment environment,
                final ClientIcon icon,
                final String title,
                final String objectName,
                final Action.ActionListener listener) {
            final RwtAction action
                    = new RwtAction(environment, icon);
            action.setText(title);
            action.setToolTip(title);
            action.setObjectName(objectName);
            action.addActionListener(listener);
            action.setEnabled(false);
            return action;
        }

        public boolean currentEntityDefined() {
            final GroupModel groupModel = getGroupModel();
            if (selector.getCurrentEntity() == null && groupModel.isEmpty()) {
                final int currentIndex = getRowIndex(getCurrentRow());
                return currentIndex > -1 && groupModel.isBrokenEntity(currentIndex);
            } else {
                return true;
            }
        }

        public void refresh() {
            final boolean currentEntityDefined = currentEntityDefined();
            final int rows = getRowCount();
            final int cur = getRowIndex(getCurrentRow()) > -1 && currentEntityDefined ? getRowIndex(getCurrentRow()) : -1;
            findAction.setEnabled(rows > 0 && currentEntityDefined);
            findNextAction.setEnabled(rows > 0 && currentEntityDefined);
            prevAction.setEnabled(true);
            beginAction.setEnabled(true);
            nextAction.setEnabled(true);
            endAction.setEnabled(true);
            exportAction.setEnabled(true);
            exportCSVAction.setEnabled(true);
            exportExcelAction.setEnabled(true);
            if (cur == -1 || cur == 0) {
                prevAction.setEnabled(false);
                beginAction.setEnabled(false);
            }
            if (cur == -1 || cur == rows - 1) {
                nextAction.setEnabled(false);
                endAction.setEnabled(false);
            }
            refreshExportActionToolTip();
            calcStatisticAction.setEnabled(rows > 0 
                                                        && !getGroupModel().getRestrictions().getIsCalcStatisticRestricted()
                                                        && getGroupModel().getSelection().isEmpty());            
        }
        
        void refreshExportActionToolTip(){
            final MessageProvider mp = RwtSelectorGrid.this.getEnvironment().getMessageProvider();
            final EntityObjectsSelection selection = RwtSelectorGrid.this.getGroupModel().getSelection();
            if (selection.isEmpty()){
                exportCSVAction.setToolTip(mp.translate("Selector", "Export Selector Content in CSV format"));
            }else if (selection.isSingleObjectSelected()){
                exportCSVAction.setToolTip(mp.translate("Selector", "Export Selected Object in CSV format"));
            }else{
                exportCSVAction.setToolTip(mp.translate("Selector", "Export Selected Objects in CSV format"));
            }
        }        

        public void close() {
            findAction.close();
            findNextAction.close();
            prevAction.close();
            nextAction.close();
            beginAction.close();
            endAction.close();
            exportCSVAction.close();
            exportExcelAction.close();
            exportAction.close();
            calcStatisticAction.close();
        }        
    }

    @Override
    public void setupSelectorToolBar(IToolBar toolBar) {
        final Action createAction = selector.getActions().getCreateAction();
        toolBar.insertAction(createAction, actions.beginAction);
        toolBar.insertAction(createAction, actions.prevAction);
        toolBar.insertAction(createAction, actions.nextAction);
        toolBar.insertAction(createAction, actions.endAction);
        toolBar.insertAction(createAction, actions.findAction);
        toolBar.addAction(actions.exportAction);
        RwtMenu exportMenu = new RwtMenu();
        exportMenu.addAction(actions.exportCSVAction);
        exportMenu.addAction(actions.exportExcelAction);
        actions.exportAction.setActionMenu(exportMenu);
        toolBar.getWidgetForAction(actions.exportAction).setPopupMode(IToolButton.ToolButtonPopupMode.InstantPopup);
        toolBar.addAction(actions.calcStatisticAction);
    }
    
    public void find() {
        FindInSelectorDialog dialog = controller.getFindDialog();
        int currentIndex = this.getCurrentCell() == null ? 0 : this.getCurrentCell().getColumn().getIndex() - 1;
        dialog.setSelectorColumns(getVisibleColumns(), currentIndex);
        if (dialog.execDialog() == IDialog.DialogResult.ACCEPTED) {
            try {
                selectFirstMatchRow(false);
            } catch (InterruptedException ex) {
                //search was interrupted - nothing to do
            } catch (ServiceClientException ex) {
                controller.processErrorOnReceivingData(ex);
            }
        }
    }    
    
    public void findNext() {
        FindInSelectorDialog dialog = controller.getFindDialog();
        if (dialog != null && dialog.getFindWhat() != null && !dialog.getFindWhat().isEmpty()) {
            try {
                selectFirstMatchRow(true);
            } catch (InterruptedException ex) {
                //search was interrupted - nothing to do
            } catch (ServiceClientException ex) {
                controller.processErrorOnReceivingData(ex);
            }
        } 
    }
    
    private void selectFirstMatchRow(final boolean next) throws InterruptedException, ServiceClientException{
        final FindInSelectorDialog dialog = controller.getFindDialog();
        final boolean forward = dialog.isForwardChecked();            
        final int searchColumnIndexInSelector = dialog.getColumnIdx() + 1;
        final MatchOptions matchOptions = 
            new MatchOptions(dialog.isMatchCaseChecked(), dialog.isWholeWordChecked());            
        final String searchString = dialog.getFindWhat(); 
        Row first = getCurrentRow();
        Row currentRow;
        Property property;
        String displayText;
        numberOfObjectsProcessedInSearch = 0;
        findDataLoader.setLoadingLimit(controller.getRowsLoadingLimit());
        findDataLoader.resetCounters();
        final IProgressHandle progressHandle = controller.getSearchProgressHandle();
        progressHandle.startProgress(selector.getEnvironment().getMessageProvider().translate("Wait Dialog", "Searching..."), true); 
        try {   
            if (next) {
                first = forward ? getNextRow(first) : getPrevRow(first);
            }   
            for (currentRow = first; currentRow != null; currentRow = forward ? getNextRow(currentRow) : getPrevRow(currentRow)) {
                numberOfObjectsProcessedInSearch++;
                if (!selector.getGroupModel().isBrokenEntity(getRowIndex(currentRow))) { 
                    property = (Property)currentRow.getCell(searchColumnIndexInSelector).getValue();
                } else {
                    property = null;
                }
                if (property != null) {
                    displayText = SelectorWidgetController.getTextToDisplay(property);
                    if (displayText != null && property.valueMatchesToSearchString(displayText, searchString, matchOptions)) {
                        setCurrentCell(currentRow.getCell(searchColumnIndexInSelector));
                        return;
                    }
                }
            }
        } finally {
            progressHandle.finishProgress();
        }
        final String title = selector.getEnvironment().getMessageProvider().translate("ExplorerDialog", "Information");
            final String message;
            final boolean isBooleanColumn = getVisibleColumns().get(searchColumnIndexInSelector-1).getPropertyDef().getType() == EValType.BOOL;   
            if (isBooleanColumn) {
                message = selector.getEnvironment().getMessageProvider().translate("ExplorerDialog", "Value not found");
            } else {
                message = selector.getEnvironment().getMessageProvider().translate("ExplorerDialog", "String \'%s\' not found");
            }
            selector.getEnvironment().messageInformation(title, String.format(message, controller.getFindDialog().getFindWhat()));
            find();
    }
    
    private List<SelectorColumnModelItem> getVisibleColumns() {
        List<SelectorColumnModelItem> colList = new LinkedList<>();
        for (SelectorColumnModelItem selectorColModelItem : controller.getSelectorColumns()) {
            if (selectorColModelItem.isVisible()) {
                colList.add(selectorColModelItem);
            }
        }
        return colList;
    }
    
    private Row getNextRow(Row currentRow) throws ServiceClientException, InterruptedException {
        int curRowIndex = getRowIndex(currentRow);
        for (int rowIndex = curRowIndex + 1; hasNextRow(rowIndex); rowIndex++) {
            if (controller.getSearchProgressHandle().wasCanceled()){
                return null;
            }
            if (getRowCount() > rowIndex 
                && !selector.getGroupModel().isBrokenEntity(rowIndex)) {
                return getRow(rowIndex);
            }
        }
        return null;
    }
    
    private boolean hasNextRow(final int rowIndex) throws ServiceClientException, InterruptedException{
        return getRowCount() > rowIndex || (controller.hasMoreData() && readMoreInSearchOperation(selector.getGroupModel()));
    }
    
     private Row getPrevRow(Row currentRow) throws ServiceClientException, InterruptedException {
        int curRowIndex = getRowIndex(currentRow);
        for (int rowIndex = curRowIndex -1; rowIndex >= 0; rowIndex--) {
            if (!selector.getGroupModel().isBrokenEntity(rowIndex)) {
                return getRow(rowIndex);
            }
        }
        return null;
    }
    
    private boolean readMoreInSearchOperation(GroupModel model) throws ServiceClientException, InterruptedException{
        final String waitDialogText = 
            selector.getEnvironment().getMessageProvider().translate("Selector", "Searching...\nNumber of Processed Objects: %1s");
        controller.getSearchProgressHandle().setText(String.format(waitDialogText, numberOfObjectsProcessedInSearch));
        return findDataLoader.loadMore(new RwtSelectorWidgetDelegate(controller, model));
    }
    
    public void selectNextRow() {
        if (getCurrentCell() != null) {
            int currentCellIndex = getCurrentCell().getColumn().getIndex();
            Row nextRow = getRowIndex(getCurrentRow()) + 1 < getRowCount() ? getRow(getRowIndex(getCurrentRow()) + 1) : null;
            if (nextRow != null) {
                final Grid.Cell cell;
                if (nextRow.getCellCount()>currentCellIndex){
                    cell = nextRow.getCell(currentCellIndex);                
                }else if (nextRow.getCellCount()>1){
                    cell = nextRow.getCell(1);
                }else{
                    cell = nextRow.getCellCount()>0 ? nextRow.getCell(0) : null;
                }
                if (cell!=null){
                    setCurrentCell(cell);
                }
            }
        }
    }

    public void selectPrevRow() {
        if (getCurrentCell() != null) {
            final int currentCellIndex = getCurrentCell().getColumn().getIndex();
            final Row prevRow = getRowIndex(getCurrentRow()) - 1 >= 0 ? getRow(getRowIndex(getCurrentRow()) - 1) : null;
            if (prevRow != null) {
                final Grid.Cell cell;
                if (prevRow.getCellCount()>currentCellIndex){
                    cell = prevRow.getCell(currentCellIndex);                
                }else if (prevRow.getCellCount()>1){
                    cell = prevRow.getCell(1);
                }else{
                    cell = prevRow.getCellCount()>0 ? prevRow.getCell(0) : null;
                }
                if (cell!=null){
                    setCurrentCell(cell);
                }
            }
        }
    }

    public void selectFirstRow() {
        if (getRowCount() > 0 ) {
            if (getRow(0).getCellCount()>1){
                setCurrentCell(getRow(0).getCell(1));
            }else{//broken entity model
                setCurrentCell(getRow(0).getCell(0));
            }
        }
    }

    public void selectLastRow() {
        final MessageProvider messageProvider  = env.getMessageProvider();
        final String loadingDialogTitle =  messageProvider.translate("Selector", "Moving to Last Object");
        final String loadingDialogMessage =  
            messageProvider.translate("Selector", "Moving to Last Object...\nNumber of Loaded Objects: %1s");
        final String dontAskText = messageProvider.translate("Selector", "Load All Objects");            
        final int loadedRows = loadAllRows(loadingDialogTitle, loadingDialogMessage, dontAskText);
        if (loadedRows > 0 && selector.leaveCurrentEntity(false)) {
            unsubscribeProperties();
            final int cellIndex = getCurrentCell().getCellIndex();
            final Grid.Row lastRow = getRow(loadedRows - 1);
            final Grid.Cell cell;
            if (lastRow.getCellCount()>cellIndex){
                cell = lastRow.getCell(cellIndex);                
            }else if (lastRow.getCellCount()>1){
                cell = lastRow.getCell(1);
            }else{
                cell = lastRow.getCellCount()>0 ? lastRow.getCell(0) : null;
            }
            if (cell!=null){
                setCurrentCell(cell);
            }
        }
    }
    
    private int loadAllRows(final String title, final String message, final String dontAskBtnText){       
        final SelectorModelDataLoader allDataLoader = new SelectorModelDataLoader(env);
        final GroupModel groupModel = selector.getGroupModel();
        final String confirmMovingToLastObjectMessage =
            env.getMessageProvider().translate("Selector", "Number of loaded objects is %1s.\nDo you want to load next %2s objects?");
        allDataLoader.setConfirmationMessageText(confirmMovingToLastObjectMessage);
        allDataLoader.setProgressHeader(title);
        allDataLoader.setProgressTitleTemplate(message);
        allDataLoader.setDontAskButtonText(dontAskBtnText);
        allDataLoader.setDontAskButtonVisibleInConfirmationDialog(false);
        final int rowsLoadingLimit =
            env.getConfigStore().readInteger(ROWS_LIMIT_FOR_NAVIGATION_CONFIG_PATH, 1000);        
        allDataLoader.setLoadingLimit(rowsLoadingLimit);
        allDataLoader.resetCounters();        
        try {
            return allDataLoader.loadAll(new RwtSelectorWidgetDelegate(controller, groupModel));
        } catch (InterruptedException exception) {
            return groupModel.getEntitiesCount();
        } catch (ServiceClientException exception) {
            final String exceptionTitle = env.getMessageProvider().translate("ExplorerException", "Error on receiving data");
            env.getTracer().error(title, exception);
            groupModel.showException(title, exception);
            return -1;
        }        
    } 
    
    public void export() {
        final GroupModelCsvWriter writer = new GroupModelCsvWriter(getGroupModel()) {
            
            @Override
            protected ISelectorDataExportOptionsDialog createExportOptionsDialog() {
                return new SelectorDataExportOptionsDialog(getGroupModel(), EnumSet.of(SelectorDataExportOptionsDialog.Features.ENCODING));
            }
        };
        final File csvFile = writer.write(selector);
        if (csvFile!=null){
            final String className = 
                getGroupModel().getSelectorPresentationDef().getClassPresentation().getName();
            final String title;
            if (className!=null && !className.isEmpty()){
                final int nameDevider = className.lastIndexOf("::");
                if (nameDevider>0 && nameDevider<className.length()-2){
                    title = className.substring(nameDevider+2);
                }else{
                    title = className;
                }
            }else{
                title = "export";
            }
            ((WpsEnvironment)getEnvironment()).sendFileToTerminal(title, csvFile, EMimeType.CSV.getValue(), false, true);
        }
    }
    
    public void exportExcel() {
        final GroupModelXlsxWriter writer = new GroupModelXlsxWriter(getGroupModel()) {
            
            @Override
            protected ISelectorDataExportOptionsDialog createExportOptionsDialog() {
                return new SelectorDataExportOptionsDialog(getGroupModel(), EnumSet.of(SelectorDataExportOptionsDialog.Features.TIMEZONEFORMAT));
            }
        };
        File file = writer.write(selector);
        if (file != null) {
            final String className = 
                getGroupModel().getSelectorPresentationDef().getClassPresentation().getName();
            final String title;
            if (className!=null && !className.isEmpty()){
                final int nameDevider = className.lastIndexOf("::");
                if (nameDevider>0 && nameDevider<className.length()-2){
                    title = className.substring(nameDevider+2);
                }else{
                    title = className;
                }
            }else{
                title = "export";
            }
            ((WpsEnvironment)getEnvironment()).sendFileToTerminal(title, file, EMimeType.APP_MSOFFICE_SPREADSHEET_X.getValue(), false, true);
        }
    }

    @Override
    public void afterPrepareCreate(final EntityModel childEntity) {
    }

    @Override
    public void refresh(final ModelItem item) {
        if (item instanceof SelectorColumnModelItem) {
            SelectorWidgetController.refreshColumn(this, (SelectorColumnModelItem) item);
        } else if (item instanceof Property) {
            controller.refresh((Property) item);
            final Cell cell = subscribedProperties.get((Property)item);
            applyTextOptions(cell);
        } else if (item == null ){
            refreshCornerHeaderCell();
        }
        actions.refresh();
    }

    @Override
    public boolean setFocus(final Property aThis) {
        return false;
    }

    @Override
    public void bind() {        
        controller.updateSortingIndicators(this, 1);
        controller.updateColumnsSizePolicy(this, 1);
        updateHeaderCheckState();
        controller.displayLoadedEntities(selector.getGroupModel());
        getGroupModel().addSelectionListener(selectionHandler);
        if (getCurrentCell() == null && getRowCount() > 0 && getRow(0).getCellCount()>0) {
            if (getRow(0).getCellCount()>1){
                setCurrentCell(getRow(0).getCell(1));
            }else{//broken entity model
                setCurrentCell(getRow(0).getCell(0));
            }
        }
        refreshCornerHeaderCell();
        subscribeToEvent(new KeyDownEventFilter(EKeyEvent.VK_A, EKeyboardModifier.CTRL));
        subscribeToEvent(new KeyDownEventFilter(EKeyEvent.VK_INSERT));
        subscribeToEvent(new KeyDownEventFilter(EKeyEvent.VK_F, EKeyboardModifier.CTRL));
        subscribeToEvent(new KeyDownEventFilter(EKeyEvent.VK_F3));
        for (int i=getColumnCount()-1; i>=0; i--){
            final SelectorColumnModelItem column = (SelectorColumnModelItem)getColumn(i).getUserData();
            if (column!=null){
                column.subscribe(this);
            }
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
                if ("m".equals(param) && !blockReadMore) {
                    controller.readMore(null);
                    if (getCurrentCell() == null && getRowCount() > 0) {
                        final Grid.Row row = getRow(0);
                        if (row.getCellCount()>1){
                            setCurrentCell(row.getCell(1));
                        }else{//broken entity model
                            setCurrentCell(row.getCell(0));
                        }                        
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
        final StringBuilder sb = new StringBuilder();
        sb.append("vc[");
        for (int i = 1; i < getColumnCount(); i++) {
            SelectorColumnModelItem item = (SelectorColumnModelItem) getColumn(i).getUserData();
            if (i > 0) {
                sb.append(",");
            }
            sb.append(item.getId());
        }
        sb.append("]");
        return sb.toString();
    }
    
    protected void beforeClose(){
        unsubscribeProperties();
        storeHeaderSettings();
        actions.close();
        controller.close();
        for (int i=getColumnCount()-1; i>=0; i--){
            if (getColumn(i).getUserData() instanceof SelectorColumnModelItem){
                ((SelectorColumnModelItem)getColumn(i).getUserData()).unsubscribe(this);                
            }
        }        
        getGroupModel().removeSelectionListener(selectionHandler);        
        if (l != null) {
            env.removeSettingsChangeListener(l);
        }        
    }

    @Override
    public void setParent(final UIObject parent) {
        if (parent == null) {//on close selector
            beforeClose();
        }
        super.setParent(parent);
    }

    @Override
    protected void processCellClickEvent(final Cell cell, 
                                         final ClickHtmlEvent event, 
                                         final UIObject cellEditor, 
                                         final UIObject rendererUi) {
        final int row = getRowIndex(getCurrentRow());
        super.processCellClickEvent(cell, event, cellEditor, rendererUi);
        final int newRowIndex = getRowIndex(getCurrentRow());
        if (row>=0 
            && row!=newRowIndex 
            && isSelectionAllowed() 
            && event.getKeyboardModifiers().size()==1){
            if (event.getKeyboardModifiers().contains(EKeyboardModifier.SHIFT))
                changeSelectionToCurrentRow(row, ESelectAction.SELECT);
            else if (event.getKeyboardModifiers().contains(EKeyboardModifier.CTRL))
                changeSelectionToCurrentRow(row, ESelectAction.UNSELECT);
        }
    }
    
    @Override
    protected boolean processKeyDownHtmlEvent(final KeyDownHtmlEvent event){
        if (event.getButton()==KeyEvent.VK_A 
            && event.getKeyboardModifiers().size()==1
            && event.getKeyboardModifiers().contains(EKeyboardModifier.CTRL)
            && isSelectionAllowed()
            && selectAllRows()){
            afterSelectionChanged();
            updateHeaderCheckState();
            return true;
        } else if (event.getButton()==EKeyEvent.VK_INSERT.getValue().intValue()
                   && event.getKeyboardModifiers().isEmpty()
                   && isSelectionAllowed() && getCurrentRow()!=null
                  ){
            if (changeSelection(getGroupModel().getSelection(), getRowIndex(getCurrentRow()), ESelectAction.INVERT)){
                afterSelectionChanged();
                return moveCursorDown();
            }
            return false;
        } else if (event.getButton()==KeyEvent.VK_F
                   && event.getKeyboardModifiers().size()==1
                   && event.getKeyboardModifiers().contains(EKeyboardModifier.CTRL)
                  ){
            find();
            return true;
        } else if (event.getButton()==KeyEvent.VK_F3
                    && event.getKeyboardModifiers().size()==0
                    && actions.findNextAction.isEnabled()) {
            findNext();
            return true;
        }
        else {
            final int prevRowIndex = getRowIndex(getCurrentRow());
            if (super.processKeyDownHtmlEvent(event)){
                final int curRowIndex = getRowIndex(getCurrentRow());
                if (prevRowIndex!=curRowIndex 
                    && isSelectionAllowed() 
                    && event.getKeyboardModifiers().contains(EKeyboardModifier.SHIFT)){
                    if (changeSelection(getGroupModel().getSelection(), curRowIndex, ESelectAction.INVERT)){
                        afterSelectionChanged();
                    }
                }
                return true;
            }
            return false;
        }
    }
    
    private void afterSelectionChanged(){
        if (!getGroupModel().getSelection().isEmpty()){
            enableSelection();
        }                
        updateHeaderCheckState();        
    }
    
    private void currentRowChanged(final Row newRow){
        unsubscribeProperties();
        final EntityModel newSelection = getEntity(newRow);
        if (newSelection instanceof BrokenEntityModel) {
            selector.showException(new BrokenEntityObjectException((BrokenEntityModel) newSelection));
        } else if (newSelection != null) {
            selector.setCurrentEntity(newSelection);
            subscribeProperties(newRow);
        }
        actions.refresh();
    }
    
    private void onRowHeaderDoubleClick(final Row row) {
        final EntityModel entityModel = getEntity(row);
        if (entityModel!=null
            && entityModel instanceof BrokenEntityModel==false
            && selector.insertEntity(entityModel)==null
            && row.getCellCount()>1){
            if (getCurrentRow()!=row){
                if (row.getCellCount()>0){
                    setCurrentCell(row.getCell(1));
                }else{//broken entity model
                    setCurrentCell(row.getCell(0));
                }          
            }
            if (selector.getCurrentEntity()==entityModel){
                selector.getActions().getEntityActivatedAction().trigger();
            }
        }
    }
    
    private GroupModel getGroupModel() {
        return controller.getModel();
    }
    
    private void updateHeaderCheckState(){
        final EntityObjectsSelection selection = getGroupModel().getSelection();
        if (selection.getSelectionMode()==ESelectionMode.NO_SELECTION || selection.isEmpty()){
            selectionHeaderCell.setUnselected();
        }else if (selection.isAllObjectsSelected()){
            selectionHeaderCell.setSelected();
        }else{
            selectionHeaderCell.setPartiallySelected();
        }
        refreshHeaderCheckable();
        if (isSelectionEnabled()){
            paintSelection();
        }
        selector.getActions().refresh();
        actions.refreshExportActionToolTip();
    }
    
    private void refreshHeaderCheckable(){
        final GroupModel groupModel = getGroupModel();
        final GroupRestrictions  restrictions = getGroupModel().getRestrictions();
        final boolean canSelectAll = !restrictions.getIsMultipleSelectionRestricted();
        if (selectionHeaderCell.isUnselected()){
            selectionHeaderCell.setUserCanSelect(canSelectAll);
        }else{
            selectionHeaderCell.setUserCanSelect(true);
        }     
    }        
    
    private boolean selectAllRows(){        
        final GroupModel groupModel = getGroupModel();
        final boolean hasMoreRows = groupModel.hasMoreRows();
        final EntityObjectsSelection selection = groupModel.getSelection();
        if (hasMoreRows){
            if (groupModel.getRestrictions().getIsSelectAllRestricted()){
                final MessageProvider messageProvider  = selector.getEnvironment().getMessageProvider();
                final String loadingDialogTitle =  messageProvider.translate("Selector", "Selecting All Objects");
                final String loadingDialogMessage =  
                    messageProvider.translate("Selector", "Selecting All Objects...\nNumber of Loaded Objects: %1s");
                final String dontAskText = messageProvider.translate("Selector", "Load All Objects");
                loadAllRows(loadingDialogTitle, loadingDialogMessage, dontAskText);
                if (groupModel.hasMoreRows()){
                    return false;
                }else{
                    selection.selectAllObjectsInGroup();
                }
            }else{
                selection.selectAllObjectsInGroup();
            }
        }else{            
            blockSelectionListener = true;
            try{
                if (selection.getSelectionMode()==ESelectionMode.EXCLUSION){
                    selection.clear();
                }
                final GroupModelReader reader = new GroupModelReader(groupModel);
                for (EntityModel entity: reader){
                    if (groupModel.getEntitySelectionController().isEntityChoosable(entity)){
                        selection.selectObject(entity.getPid());
                    }
                }
            }finally{
                blockSelectionListener = false;
            }
            updateHeaderCheckState();
        }
        enableSelection();
        return true;
    }    

    private boolean isSelectionAllowed(){
        return !selector.getGroupModel().getRestrictions().getIsMultipleSelectionRestricted() 
            && isEnabled()
            && !selector.getGroupModel().isEmpty();
    }    
    
    private void refreshCornerHeaderCell(){
        final boolean isSelectionEnabled = isSelectionAllowed();
        final Grid.CornerHeaderCell corner = getCornerHeaderCell();
        if (isSelectionEnabled && !cornerHeaderCellClickEnabled){            
            corner.addClickListener(cornerHeaderCellClickListener);
            corner.setTitle(String.valueOf(CORNER_HEADER_CELL_SYMBOL));
            cornerHeaderCellClickEnabled = true;
            refreshCornerHeaderCellToolTip(corner);
        }else if (!isSelectionEnabled){
            disableSelection();
            corner.removeClickListener(cornerHeaderCellClickListener);
            corner.setTitle("");
            cornerHeaderCellClickEnabled = false;
            refreshCornerHeaderCellToolTip(corner);
        }
    }

    public void refreshCornerHeaderCellToolTip(final Grid.CornerHeaderCell corner){
        final MessageProvider mp = env.getMessageProvider();
        if (cornerHeaderCellClickEnabled){
            if (isSelectionEnabled()){
                corner.setToolTip(mp.translate("Selector", "Disable Multiple Selection Mode"));
            }else{
                corner.setToolTip(mp.translate("Selector", "Enable Multiple Selection Mode"));
            }            
        }else{
            corner.setToolTip("");
        }
    }      
    
    private void onCornerHeaderCellClick(){
        if (!isEnabled() || getGroupModel().isEmpty()){
            return;
        }
        finishEdit();
        if (isSelectionEnabled()){
            final EntityObjectsSelection selection = getGroupModel().getSelection();
            if (!selection.isEmpty()){
                if (confirmToClearSelection()){
                    selection.clear();
                    if (!selection.isEmpty()){
                        return;//cleaning selection was rejected in some handler
                    }
                }else{
                    return;
                }
            }
            disableSelection();            
        }else{
            enableSelection();
        }
        refreshCornerHeaderCellToolTip(getCornerHeaderCell());
    }
    
    private void onSelectionColumnHeaderClick(){
        finishEdit();
        if (selectionHeaderCell.isUnselected()){
            if (!selectAllRows()){
                updateHeaderCheckState();
            }
        }else{
            if (confirmToClearSelection()){
                getGroupModel().getSelection().clear();
            }
            updateHeaderCheckState();
        }   
    }
    
    private boolean changeSelectionToCurrentRow(final int fromRow, final ESelectAction action){
        if (getCurrentRow()==null){
            return false;
        }else{                        
            final int currentRow = getRowIndex(getCurrentRow());
            final int endRow = Math.max(currentRow, fromRow);
            final int startRow = Math.min(currentRow, fromRow);
            final EntityObjectsSelection selection = getGroupModel().getSelection();
            boolean selectionChanged = false;
            blockSelectionListener = true;
            try{
                for (int row=startRow; row<=endRow; row++){                
                    if (changeSelection(selection, row, action)){
                        selectionChanged = true;
                    }
                }
            }finally{
                blockSelectionListener = false;
            }
            if (selectionChanged){
                enableSelection();
                updateHeaderCheckState();
            }
            return selectionChanged;
        }
    }
    
    private boolean changeSelection(final EntityObjectsSelection selection, final int rowIndex, final ESelectAction action){
        final Grid.Row row = getRow(rowIndex);
        if (!isBrokenEntity(row)){
            final EntityModel entity = getEntity(row);
            final Pid entityPid = entity.getPid();
            if (selector.getGroupModel().getEntitySelectionController().isEntityChoosable(entity)){
                switch(action){
                    case INVERT:
                        selection.invertSelection(entityPid);
                        return true;
                    case SELECT:{
                        if (!selection.isObjectSelected(entityPid)){
                            selection.selectObject(entityPid);
                            return true;
                        }
                        break;
                    }
                    case UNSELECT:{
                        if (selection.isObjectSelected(entityPid)){
                            selection.unselectObject(entityPid);
                            return true;
                        }
                        break;                        
                    }                        
                }
            }
        }
        return false;
    }
    
    private void onSelectionColumnRowClick(final Pid pid, final EnumSet<EKeyboardModifier> modifiers){        
        if (isSelectionEnabled()){
            final int row = getGroupModel().findEntityByPid(pid);
            if (row>=0){
                if (modifiers.size()==1 && modifiers.contains(EKeyboardModifier.SHIFT)){
                    changeSelectionToCurrentRow(row, ESelectAction.SELECT);
                }else if (modifiers.size()==1 && modifiers.contains(EKeyboardModifier.CTRL)){
                    changeSelectionToCurrentRow(row, ESelectAction.UNSELECT);
                }else if (modifiers.isEmpty()){
                    changeSelection(getGroupModel().getSelection(), row, ESelectAction.INVERT);
                }
            }
        }
    }
    
    private boolean confirmToClearSelection(){
        final String title = env.getMessageProvider().translate("ExplorerMessage", "Confirm to Clear Selection");
        final String message = env.getMessageProvider().translate("ExplorerMessage", "Do you really want to clear selection?");
        return env.messageConfirmation(title, message);
    }
        
    private void enableSelection(){
        getColumn(0).setVisible(true);
    }
    
    private void disableSelection(){
        getColumn(0).setVisible(false);
    }
    
    private boolean isSelectionEnabled(){
        return getColumn(0).isVisible();
    }   

    @Override
    public boolean setMultiSelectionEnabled(boolean enabled) {
        if (enabled){
            if (getGroupModel().getRestrictions().getIsMultipleSelectionRestricted()){
                return false;
            }else{
                enableSelection();
            }
        }else{
            disableSelection();
        }
        return true;
    }
    
    protected List<AggregateFunctionCall> getSelectionStatisticParams(){
        final List<Id> compatibleColumns = 
            SelectorColumnsStatistic.getCompatibleColumns(getGroupModel(), getVisibleColumnsOrder());
        if (compatibleColumns.isEmpty()){
            return Collections.singletonList(new AggregateFunctionCall(null, EAggregateFunction.COUNT));
        }
        final SelectionStatisticParamsDialog paramsDialog = 
            new SelectionStatisticParamsDialog(getGroupModel(), compatibleColumns);
        if (paramsDialog.execDialog()==Dialog.DialogResult.ACCEPTED){
            return paramsDialog.getAggregateFunctions();
        }else{
            return Collections.emptyList();
        }
    }
    
    protected boolean confirmToCalcStatistic(final List<AggregateFunctionCall> functionCalls){
        final IClientEnvironment environment = selector.getEnvironment();
        final String confirmationTitle =
                environment.getMessageProvider().translate("Selector", "Confirm to Proceed Operation");
        final String confirmationMessage =
                environment.getMessageProvider().translate("Selector", "This operation may take a lot of time.\nDo you really want to proceed the operation?");
        return environment.messageConfirmation(confirmationTitle, confirmationMessage);        
    }
    
    protected void showSelectionStatistic(final List<AggregateFunctionCall> functionCalls, final SelectorColumnsStatistic statistic){
        final IClientEnvironment environment = selector.getEnvironment();
        if (statistic.getAggregateFunctions().isEmpty()){
            if (statistic.getRowsCount()>-1){
                final String rowCountText = 
                    SelectorColumnsStatistic.getAggregationFunctionTitle(EAggregateFunction.COUNT, environment.getMessageProvider())
                    +": "+String.valueOf(statistic.getRowsCount());
                final String messageTitle = environment.getMessageProvider().translate("Selector", "Statistics for Selection");
                environment.messageInformation(messageTitle, rowCountText);
            }
        }else{
            final Map<Id,Integer> precisionByColumnId = new HashMap<>();
            for (AggregateFunctionCall function: functionCalls){
                if (function.getPrecision()>-1){
                    precisionByColumnId.put(function.getColumnId(), function.getPrecision());
                }
            }            
            final SelectionStatisticResultDialog dialog = 
                new SelectionStatisticResultDialog(getGroupModel(), statistic, getVisibleColumnsOrder(), precisionByColumnId);
            dialog.execDialog();
        }
    }
    
    protected final List<Id> getVisibleColumnsOrder(){
        final List<Id> columnIds = new LinkedList<>();        
        for (int i=1; i<getColumnCount(); i++){
            if (getColumn(i).getUserData() instanceof SelectorColumnModelItem){
                final SelectorColumnModelItem column = (SelectorColumnModelItem)getColumn(i).getUserData();
                columnIds.add(column.getId());
            }            
        }
        return columnIds;
    }
    
    
    public void calcSelectionStatistic(){
        final List<AggregateFunctionCall> aggregateFunctions = getSelectionStatisticParams();
        if (aggregateFunctions!=null 
            && !aggregateFunctions.isEmpty() 
            && (!getGroupModel().hasMoreRows() || confirmToCalcStatistic(aggregateFunctions))){
            final SelectorColumnsStatistic statistic;
            try{
                statistic = getGroupModel().calcStatistic(aggregateFunctions);
            }catch(ServiceClientException ex){
                getGroupModel().showException(ex);
                return;
            }catch(InterruptedException ex){
                return;
            }
            showSelectionStatistic(aggregateFunctions, statistic);
        }        
    }

    private void subscribeProperties(final Row row){
        Property property;
        for (int i=row.getCellCount()-1; i>=0; i-- ){
            if (row.getCell(i).getValue() instanceof Property){
                property = (Property)row.getCell(i).getValue();
                property.subscribe(this);
                subscribedProperties.put(property, row.getCell(i));
            }
        }
    }
    
    private void unsubscribeProperties(){
        if (!subscribedProperties.isEmpty()){
            for (Property property: subscribedProperties.keySet()){
                property.unsubscribe(this);
            }
            subscribedProperties.clear();
        }
    }        
}
