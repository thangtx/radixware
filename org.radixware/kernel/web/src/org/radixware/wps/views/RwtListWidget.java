/*
* Copyright (c) 2008-2016, Compass Plus Limited. All rights reserved.
*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at http://mozilla.org/MPL/2.0/.
* This Source Code is distributed WITHOUT ANY WARRANTY; including any 
* implied warranties but not limited to warranty of MERCHANTABILITY 
* or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
* License, v. 2.0. for more details.
*/

package org.radixware.wps.views;

import java.awt.Color;
import java.util.Collection;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EKeyboardModifier;
import org.radixware.kernel.common.client.enums.ETextAlignment;
import org.radixware.kernel.common.client.enums.ETextOptionsMarker;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.text.ITextOptions;
import org.radixware.kernel.common.client.text.TextOptions;
import org.radixware.kernel.common.client.types.FilterRules;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.kernel.common.client.widgets.IListWidget;
import org.radixware.kernel.common.client.widgets.ListWidgetController;
import org.radixware.kernel.common.client.widgets.ListWidgetItem;
import org.radixware.kernel.common.enums.EKeyEvent;
import org.radixware.wps.icons.WpsIcon;
import org.radixware.wps.rwt.CheckBox;
import org.radixware.wps.rwt.Grid;
import org.radixware.wps.rwt.IGrid;
import org.radixware.wps.rwt.Label;
import org.radixware.wps.rwt.UIObject;
import org.radixware.wps.rwt.VerticalBoxContainer;
import org.radixware.wps.rwt.events.ClickHtmlEvent;
import org.radixware.wps.rwt.events.KeyDownEventFilter;
import org.radixware.wps.rwt.events.KeyDownHtmlEvent;
import org.radixware.wps.text.WpsTextOptions;
import org.radixware.wps.views.editors.valeditors.ValStrEditorController;

public class RwtListWidget extends VerticalBoxContainer implements IListWidget{
    
    private static interface IListWidgetTableListener{
        void onSelectionClick(final int rowIndex, final boolean isSelected, final boolean isShiftKeyPressed, final boolean isCtrlKeyPressed);
        void onCellClick(final int rowIndex, final boolean isShiftKeyPressed, final boolean isCtrlKeyPressed);
        void onChangeRow(final int previousRowIndex, final int currentRowIndex, final boolean isShiftKeyPressed, final boolean isCtrlKeyPressed);
        void onSelectAll();
    }
    
    private final static class ListWidgetTable extends Grid implements ListWidgetController.IListWidgetTable{ 
        
        private final static class RowFilterRules extends FilterRules{
            
            private final ListWidgetItem item;
            
            public RowFilterRules(final ListWidgetItem listItem){
                item = listItem;
            }

            @Override
            public boolean isMatchToSomeFilter(final String filterText) {
                return item.isMatchToFilter(filterText);
            }          

            @Override
            public FilterRules copy(){
                return new RowFilterRules(item);
            }            
        }
        
        private final class CellRendererProvider implements IGrid.CellRendererProvider{
            
            private final IGrid.SelectableCellRenderer.ISelectionCellClickListener listener = 
                new IGrid.SelectableCellRenderer.ISelectionCellClickListener() {
                    @Override
                    public void onClick(final Object cellId, final boolean isSelected, final EnumSet<EKeyboardModifier> keyboardModifiers) {
                        ListWidgetTable.this.onSelectionCellClick((Grid.Cell)cellId, isSelected, keyboardModifiers);
                    }
                };
            
            @Override
            public CellRenderer newCellRenderer(final int r, final int c) {
                if (c==0){
                    final Grid.Cell cell = ListWidgetTable.this.getRow(r).getCell(c);
                    final IGrid.SelectableCellRenderer renderer = new IGrid.SelectableCellRenderer(cell);
                    renderer.addListener(listener);
                    return renderer;
                }else{
                    return new Grid.DefaultRenderer();
                }
            }            
        }        
        
        private final static int SELECTION_COLUMN = 0;
        private final static int TITLE_COLUMN = 1;
        private final static int DESCRIPTION_COLUMN = 2;
                
        private final IClientEnvironment environment;
        private final IListWidgetTableListener tableListener;
                
        public ListWidgetTable(final IClientEnvironment environment, 
                                           final IListWidgetTableListener tableListener){
            this.environment = environment;
            this.tableListener = tableListener;
            addColumn("Selection").setFixedWidth(24);
            addColumn("Title").setSizePolicy(EColumnSizePolicy.STRETCH);
            addColumn("Description").setSizePolicy(EColumnSizePolicy.BY_CONTENT);
            getColumn(SELECTION_COLUMN).setVisible(false);
            getColumn(DESCRIPTION_COLUMN).setVisible(false);
            setBrowserFocusFrameEnabled(false);
            setBorderVisible(false);
            setSelectionMode(SelectionMode.ROW);
            setSelectionStyle(EnumSet.of(ESelectionStyle.BACKGROUND_COLOR));
            setHeaderVisible(false);
            setBorderBoxSizingEnabled(true);
            setRendererProvider(new CellRendererProvider());
            this.addCurrentRowListener(new CurrentRowListener() {
                @Override
                public void currentRowChanged(Row oldRow, Row newRow) {
                }

                @Override
                public boolean beforeChangeCurrentRow(final Row oldRow, final Row newRow) {
                    if (newRow!=null && newRow.getUserData() instanceof Integer){
                        return ((Integer)newRow.getUserData())>0;
                    }else{
                        return true;
                    }
                }
            });
            subscribeToEvent(new KeyDownEventFilter(EKeyEvent.VK_A, EKeyboardModifier.CTRL));
        }        
        
        private void onSelectionCellClick(final Grid.Cell cell, final boolean isSelected, final EnumSet<EKeyboardModifier> keyboardModifiers){
            final Row row = cell.getRow();
            if (row!=null && row.getUserData() instanceof Integer && ((Integer)row.getUserData())>1){
                final boolean newValue;
                if (cell.getValue() instanceof Boolean){
                    newValue = !(Boolean)cell.getValue();
                }else{
                    newValue = true;
                }
                cell.setValue(newValue);
                tableListener.onSelectionClick(getRowIndex(row), 
                                                                          newValue, 
                                                                          keyboardModifiers.contains(EKeyboardModifier.SHIFT), 
                                                                          keyboardModifiers.contains(EKeyboardModifier.CTRL));
            }            
        }

        @Override
        protected boolean processKeyDownHtmlEvent(final KeyDownHtmlEvent event) {
            if (event.getButton()==EKeyEvent.VK_A.getValue().intValue()){
                tableListener.onSelectAll();
                return true;
            }else{
                return super.processKeyDownHtmlEvent(event);
            }            
        }
        
                
        @Override
        protected void processCellClickEvent(final Cell cell,
                                             final ClickHtmlEvent event,
                                             final UIObject cellEditor,
                                             final UIObject rendererUi
                                             ){
            final Cell currentCell = getCurrentCell();
            final boolean isShiftKeyPressed = event.getKeyboardModifiers().contains(EKeyboardModifier.SHIFT);
            final boolean isCtrlKeyPressed = event.getKeyboardModifiers().contains(EKeyboardModifier.CTRL);
            final int rowIndex = getRowIndex(cell.getRow());
            if (currentCell == cell) {                
                tableListener.onCellClick(rowIndex, isShiftKeyPressed, isCtrlKeyPressed);
            } else {
                if (currentCell == null || currentCell.finishEdit(true)) {
                    setCurrentCell(cell);                    
                }                
                final int previousRow = currentCell==null ? -1 : getRowIndex(currentCell.getRow());
                if (rowIndex==previousRow){
                    tableListener.onCellClick(rowIndex, isShiftKeyPressed, isCtrlKeyPressed);
                }else{
                    tableListener.onChangeRow(previousRow, rowIndex, isShiftKeyPressed, isCtrlKeyPressed);
                }
            }
        }                

        @Override
        public void setRowSelected(final int row, final boolean isSelected) {
            getRow(row).getCell(SELECTION_COLUMN).setValue(isSelected);
            tableListener.onSelectionClick(row, isSelected, false, false);            
        }

        @Override
        public boolean isRowSelected(final int row) {
            return getRow(row).getCell(SELECTION_COLUMN).getValue()==Boolean.TRUE;
        }

        @Override
        public void setText(final int row, final String text) {
            getRow(row).getCell(TITLE_COLUMN).setValue(text);
        }

        @Override
        public void setDescription(final int row, final String text) {
            getRow(row).getCell(DESCRIPTION_COLUMN).setValue(text);
        }

        @Override
        public void setIcon(final int row, final Icon icon) {
            if (icon==null){
                getRow(row).getCell(TITLE_COLUMN).setIcon(null);
            }else{
                getRow(row).getCell(TITLE_COLUMN).setIcon((WpsIcon)icon);
            }
        }

        @Override
        public void setRowOptions(final int row, final boolean isSelectable, final boolean canSetCurrent) {
            final Integer flag;
            if (canSetCurrent){
                flag = isSelectable ? 2 : 1;
            }else{
                flag = 0;
            }
            getRow(row).setUserData(flag);
        }
        
        @Override
        public void setRowHidden(final int row, final boolean isHidden) {
            getRow(row).setVisible(!isHidden);
        }

        @Override
        public boolean isRowHidden(final int row) {
            return !getRow(row).isVisible();
        }

        @Override
        public void setCurrentRowIndex(final int row) {           
            final int currentRow = getCurrentRowIndex();
            this.setCurrentCell(getRow(row).getCell(TITLE_COLUMN));
            tableListener.onChangeRow(currentRow, getCurrentRowIndex(), false, false);
        }

        @Override
        public int getCurrentRowIndex() {
            final Row row = getCurrentRow();
            return row==null ? -1 : getRowIndex(row);
        }

        @Override
        public ListWidgetItem getListWidgetItem(final int row) {
            return (ListWidgetItem)getRow(row).getCell(TITLE_COLUMN).getUserData();
        }

        @Override
        public void setRowToolTip(final int row, final String toolTip) {
            getRow(row).setToolTip(toolTip);
        }

        @Override
        public void applyTextOptions(final int row, final ITextOptions options) {
            final WpsTextOptions textOptions = (WpsTextOptions)options;
            getRow(row).getCell(SELECTION_COLUMN).setTextOptions(textOptions);
            getRow(row).getCell(TITLE_COLUMN).setTextOptions(textOptions);
            final WpsTextOptions descriptionCellOptions = 
                textOptions.changeBackgroundColor(Color.gray).changeAlignment(ETextAlignment.RIGHT);
            getRow(row).getCell(DESCRIPTION_COLUMN).setTextOptions(descriptionCellOptions);
        }

        @Override
        public void clearContent() {
            clearRows();
        }

        @Override
        public void insertRow(final ListWidgetItem item, final int row) {
            setupRow(insertRow(row), item);
        }

        @Override
        public int addRow(final ListWidgetItem item) {
            setupRow(addRow(), item);
            return getRowCount()-1;
        }
        
        private void setupRow(final Row row, final ListWidgetItem item){
            row.getCell(TITLE_COLUMN).setUserData(item);
            row.setFilterRules(new RowFilterRules(item));
            row.getHtml().removeChoosableMarker();
            final String itemName = item.getName();
            if (itemName!=null && !itemName.isEmpty()){
                row.setObjectName(itemName);
            }
        }

        @Override
        public void removeRow(final int row) {
            removeRow(getRow(row));
        }

        @Override
        public void setItemsDescriptionVisible(final boolean isVisible) {
            getColumn(DESCRIPTION_COLUMN).setVisible(isVisible);
        }

        @Override
        public void setMultiSelectionEnabled(final boolean isEnabled) {
            getColumn(SELECTION_COLUMN).setVisible(isEnabled);
        }

        @Override
        public void setSingleSelectionEnabled(boolean isEnabled) {
            setSelectionMode(isEnabled ? SelectionMode.ROW : SelectionMode.NONE);
        }

        @Override
        public boolean isSingleSelectionEnabled() {
            return getSelectionMode()==SelectionMode.ROW;
        }        
    }
    
    private final class ListWidgetPresenter implements ListWidgetController.IListWidgetPresenter{

        @Override
        public boolean isSortItemsChecked() {
            return cbSortItems.isSelected();
        }

        @Override
        public void setSortItemsChecked(final boolean isChecked) {
            cbSortItems.setSelected(isChecked);
        }

        @Override
        public void setSortItemsVisible(boolean isVisible) {
            cbSortItems.setVisible(isVisible);
        }

        @Override
        public void setSelectionLabelText(final String text) {
            lbSelection.setText(text);
        }

        @Override
        public void setSelectionLabelVisible(boolean isVisible) {
            lbSelection.setVisible(isVisible);
        }

        @Override
        public void setFilterVisible(final boolean isVisible) {
            final UIObject editor = (UIObject)filter.getValEditor();
            if (isVisible){
                editor.setVisible(true);                
                editor.setFocused(true);
            }else{
                filter.setValue(null);
                filter.removeTextOptionsMarkers(ETextOptionsMarker.MANDATORY_VALUE);
                editor.setVisible(false);
                table.setFocused(true);
            }
        }

        @Override
        public void applyTextOptions(final ITextOptions textOptions) {
            if (textOptions instanceof WpsTextOptions){
                final WpsTextOptions options = (WpsTextOptions)textOptions;
                cbSortItems.setTextOptions(options);
                filter.setDefaultTextOptions(options);
                lbSelection.setTextOptions(options.changeBackgroundColor(null));                
            }
        }
        
    }
    
    private final static class RwtListWidgetController extends ListWidgetController{
        
        private final ListWidgetTable table;
        
        public RwtListWidgetController(final IClientEnvironment environmnet,
                                                        final IListWidgetPresenter presenter,
                                                        final ListWidgetTable table){
            super(environmnet, presenter, table);
            this.table = table;
        }

        @Override
        protected void applyCurrentFilter() {
            //do nothing
        }

        @Override
        protected void applyCurrentFilter(final int visualRow) {
            table.getRow(visualRow).applyFilter();
        }        
    }
    
    private final IClientEnvironment environment;
    
    private final ValStrEditorController filter;
    private final ListWidgetTable table;
    private final CheckBox cbSortItems = new CheckBox();
    private final Label lbSelection = new Label();
    private final ListWidgetController controller;
    private final IListWidgetTableListener tableListener = new IListWidgetTableListener(){
        @Override
        public void onSelectionClick(int rowIndex, boolean isSelected, boolean isShiftKeyPressed, boolean isCtrlKeyPressed) {
            if (isShiftKeyPressed || isCtrlKeyPressed){
                controller.onCellClick(rowIndex, isShiftKeyPressed, isCtrlKeyPressed);
            }else{
                controller.onSelectionChanged(rowIndex);
            }
        }

        @Override
        public void onCellClick(int rowIndex, boolean isShiftKeyPressed, boolean isCtrlKeyPressed) {
            controller.onCellClick(rowIndex, isShiftKeyPressed, isCtrlKeyPressed);
        }

        @Override
        public void onChangeRow(int previousRowIndex, int currentRowIndex, boolean isShiftKeyPressed, boolean isCtrlKeyPressed) {
            controller.onCurrentRowChanged(currentRowIndex, previousRowIndex, isShiftKeyPressed, isCtrlKeyPressed);
        }

        @Override
        public void onSelectAll() {
            controller.selectAllItems();
        }                
    };
    
    
    public RwtListWidget(final IClientEnvironment environment){
        this.environment = environment;
        filter = new ValStrEditorController(environment);
        table = new ListWidgetTable(environment, tableListener);
        controller = new RwtListWidgetController(environment, new ListWidgetPresenter(), table);
        setupUi();
    }
    
    private void setupUi(){
        final MessageProvider mp = getEnvironment().getMessageProvider();
        
        filter.setMandatory(true);
        filter.removeTextOptionsMarkers(ETextOptionsMarker.MANDATORY_VALUE);
        filter.setLabel(mp.translate("Value","Filter:"));
        filter.setLabelVisible(true);        
        add((UIObject)filter.getValEditor());
        
        addSpace();
        addSpace();
        table.setFilterEditor(filter);        
        table.addFilterListener(new IGrid.FilterListener() {
            @Override
            public void afterApplyFilter(final String filter) {
                controller.afterApplyFilter(filter);
                ((UIObject)RwtListWidget.this.filter.getValEditor()).setFocused(true);
            }
        });
        table.addDoubleClickListener(new Grid.DoubleClickListener() {
            @Override
            public void onDoubleClick(final Grid.Row row, final Grid.Cell cell) {
                controller.onCellDoubleClick(table.getRowIndex(row));
            }
        });        
        add(table);
        
        addSpace();
        cbSortItems.setText(mp.translate("Value", "&Sort"));
        cbSortItems.addClickHandler(new IButton.ClickHandler(){
            @Override
            public void onClick(final IButton source) {
                controller.applySorting();
            }            
        });                
        add(cbSortItems);
        
        addSpace();
        add(lbSelection);
        setAutoSize(table, true);
        
        controller.applyFeatures();
    }

    @Override
    public final void add(final ListWidgetItem item) {
        controller.add(item);
    }

    @Override
    public final void insert(final ListWidgetItem item, final int row) {
        controller.insert(item, row);
    }

    @Override
    public final void clear() {
        controller.clear();
    }

    @Override
    public final void remove(final ListWidgetItem item) {
        controller.remove(item);
    }

    @Override
    public final void removeRow(final int row) {
        controller.removeRow(row);
    }

    @Override
    public final void swapItems(final ListWidgetItem item1, final ListWidgetItem item2) {
        controller.swapItems(item1, item2);
    }

    @Override
    public final void swapRows(final int row1, final int row2) {
        controller.swapRows(row1, row2);
    }

    @Override
    public final void setCurrent(final ListWidgetItem item) {
        controller.setCurrent(item);
    }

    @Override
    public final ListWidgetItem getCurrent() {
        return controller.getCurrent();
    }

    @Override
    public final void setCurrentRow(final int row) {        
        controller.setCurrentRow(row);
    }

    @Override
    public final int getCurrentRow() {
        return controller.getCurrentRow();
    }

    @Override
    public final ListWidgetItem getItem(final int row) {
        return controller.getItem(row);
    }

    @Override
    public void setItems(final List<ListWidgetItem> items) {
        controller.setItems(items);
    }

    @Override
    public final List<ListWidgetItem> getItems() {
        return controller.getItems();
    }

    @Override
    public final int count() {
        return controller.count();
    }

    @Override
    public final void setFeatures(final EnumSet<EFeatures> features) {
        controller.setFeatures(features);
    }
    
    @Override
    public final EnumSet<EFeatures> getFeatures() {
        return controller.getFeatures();
    }
    
    @Override    
    public final void setTextOptions(final TextOptions textOptions){
        controller.setTextOptions(textOptions);
    }
    
    @Override
    public WpsTextOptions getTextOptions(){
        return (WpsTextOptions) controller.getTextOptions();
    }

    @Override
    public void setItemsComparator(final Comparator<ListWidgetItem> comparator) {
        controller.setItemsComparator(comparator);
    }

    @Override
    public Comparator<ListWidgetItem> getItemsComparator() {
        return controller.getItemsComparator();
    }        
    
    @Override
    public void setSelectionEnabled(final boolean isEnabled) {
        controller.setSelectionEnabled(isEnabled);
    }

    @Override
    public boolean isSelectionEnabled() {
        return controller.isSelectionEnabled();
    }        
    
    @Override
    public void setSelectedItems(final Collection<ListWidgetItem> selectedItems) {
        controller.setSelectedItems(selectedItems);
    }

    @Override
    public List<ListWidgetItem> getSelectedItems() {
        return controller.getSelectedItems();
    }

    @Override
    public List<Integer> getSelectedRows() {
        return controller.getSelectedRows();
    }

    @Override
    public void setItemSelected(final ListWidgetItem item, final boolean isSelected) {
        controller.setItemSelected(item, isSelected);
    }     

    @Override
    public boolean isItemSelected(final ListWidgetItem item) {
        return controller.isItemSelected(item);
    }        

    @Override
    public void setFocusToFilterString() {        
        if (((UIObject)filter.getValEditor()).isVisible()){
            ((UIObject)filter.getValEditor()).setFocused(true);
        }
    }

    @Override
    public final void addCurrentItemListener(final ICurrentItemListener listener) {
        controller.addCurrentItemListener(listener);
    }

    @Override
    public final void removeCurrentItemListener(final ICurrentItemListener listener) {
        controller.removeCurrentItemListener(listener);
    }

    @Override
    public final void addDoubleClickListener(final IDoubleClickListener listener) {
        controller.addDoubleClickListener(listener);
    }
        
    @Override
    public final void removeDoubleClickListener(final IDoubleClickListener listener) {
        controller.removeDoubleClickListener(listener);
    }

    @Override
    public final void addFilterListener(final IFilterListener listener) {
        controller.addFilterListener(listener);
    }

    @Override
    public final void removeFilterListener(final IFilterListener listener) {
        controller.removeFilterListener(listener);
    }

    @Override
    public void addSelectionListener(final ISelectionListener listener) {
        controller.addSelectionListener(listener);
    }

    @Override
    public void removeSelectionListener(final ISelectionListener listener) {
        controller.removeSelectionListener(listener);
    }

}
