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

package org.radixware.kernel.common.client.widgets;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.ETextOptionsMarker;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.meta.mask.validators.EValidatorState;
import org.radixware.kernel.common.client.text.ITextOptions;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.enums.ESelectorRowStyle;


public class ListWidgetController {
    
    public interface IListWidgetTable{        
        void setRowSelected(int row, boolean isSelected);
        boolean isRowSelected(int row);
        
        void setText(int row, String text);
        void setDescription(int row, String text);
        void setIcon(int row, Icon icon);
        
        void setRowOptions(int row, boolean isSelectable, boolean canSetCurrent);
        
        void setRowHidden(int row, boolean isHidden);
        boolean isRowHidden(int row);
        
        void setCurrentRowIndex(int row);
        int getCurrentRowIndex();        
        
        ListWidgetItem getListWidgetItem(int row);        
        int getRowCount();
        void setRowToolTip(int row, String toolTip);
        void applyTextOptions(int row, ITextOptions options);
        
        void clearContent();
        void insertRow(ListWidgetItem item, int row);
        int addRow(ListWidgetItem item);
        void removeRow(int row);
        
        void swapRows(int row1, int row2);
        
        void setItemsDescriptionVisible(boolean isVisible);
        void setMultiSelectionEnabled(boolean isEnabled);
        
        void setSingleSelectionEnabled(boolean isEnabled);
        boolean isSingleSelectionEnabled();
    }
    
    public interface IListWidgetPresenter{
        boolean isSortItemsChecked();
        void setSortItemsChecked(boolean isChecked);
        void setSortItemsVisible(boolean isVisible);
        
        void setSelectionLabelText(String text);
        void setSelectionLabelVisible(boolean isVisible);
                
        void setFilterVisible(boolean isVisible);        
        void applyTextOptions(final ITextOptions textOptions);
    }
    
    private final static String SELECTION_COLOR_SETTING_KEY = 
        SettingNames.SYSTEM+"/"+SettingNames.SELECTOR_GROUP+"/"+SettingNames.Selector.COMMON_GROUP+"/"+SettingNames.Selector.Common.SELECTED_ROW_COLOR;

    private final static Comparator<ListWidgetItem> DEFAULT_COMPARATOR = new Comparator<ListWidgetItem>() {
        @Override
        public int compare(final ListWidgetItem o1, final ListWidgetItem o2) {
            final String text1 = o1.getText()==null ? "" : o1.getText();
            final String text2 = o2.getText()==null ? "" : o2.getText();
            return text1.compareTo(text2);
        }
    };
        
    private final IClientEnvironment environment;
    private final IListWidgetTable table;
    private final IListWidgetPresenter presenter;    
    
    private final ListWidgetItem.IChangeAttrributeListener listWidgetItemListener = new ListWidgetItem.IChangeAttrributeListener(){
        @Override
        public void attributeChanged(final ListWidgetItem item, final ListWidgetItem.EAttribute attribute) {
            afterChangeListItemAttribute(item, attribute);
        }        
    };
            
    private final List<ListWidgetItem> logicalItems = new ArrayList<>();
    private final List<Integer> selectedRows = new ArrayList<>();
    private final EnumSet<IListWidget.EFeatures> features = EnumSet.noneOf(IListWidget.EFeatures.class);
    private ITextOptions textOptions;
    private Comparator<ListWidgetItem> itemsComparator;
    private List<IListWidget.ICurrentItemListener> currentItemListeners;
    private List<IListWidget.IDoubleClickListener> doubleClickListeners;
    private List<IListWidget.IFilterListener> filterListeners;
    private List<IListWidget.ISelectionListener> selectionListeners;
    
    private int currentVisualRow = -1;
    private ListWidgetItem currentItem;
    private String currentFilter;
    private boolean blockSelectionListeners;
    private boolean blockRangeSelection;
    private boolean internalSelectionChange;
    private boolean internalCurrentRowChange;
    
    public ListWidgetController(final IClientEnvironment environmnet,
                                              final IListWidgetPresenter presenter,
                                              final IListWidgetTable table){
        this.environment = environmnet;
        this.presenter = presenter;
        this.table = table;
    }
    
    public final void onCurrentRowChanged(final int currentRow, 
                                                           final int previousRow,
                                                           final boolean isShiftKeyPressed,
                                                           final boolean isCtrlKeyPressed){
        if (!internalCurrentRowChange){
            if (currentRow>=0 
                && previousRow>=0 
                && currentRow!=previousRow 
                && !blockRangeSelection
                && isMultiSelectEnabled()){
                if (isShiftKeyPressed){
                    changeRangeSelection(currentRow, previousRow, true);
                }else if (isCtrlKeyPressed){
                    changeRangeSelection(currentRow, previousRow, false);
                }
            }
            currentVisualRow = currentRow;
            final ListWidgetItem item = getItemByVisualRow(currentVisualRow);
            if (currentItem!=item){
                currentItem = item;
                notifyCurrentItemListeners(currentItem);            
                checkIfSelectionChanged();
            }
        }
    }    
    
    public final void onCellClick(final int row, final boolean isShiftKeyPressed, final boolean isCtrlKeyPressed) {
        final int currentRow = table.getCurrentRowIndex();
        if (currentRow>=0 && row>=0 && row!=currentRow && isMultiSelectEnabled()){
            if (isShiftKeyPressed){
                changeRangeSelection(currentRow, row, true);
            }else if (isCtrlKeyPressed){
                changeRangeSelection(currentRow, row, false);
            }
        }
    }
    
    public final void onCellDoubleClick(final int row){
        final ListWidgetItem listItem = getItemByVisualRow(row);
        if (listItem!=null){
            notifyDoubleClickListeners(listItem);
        }
    }
    
    public final void onSelectionChanged(final int row){
        if (!internalSelectionChange && isMultiSelectEnabled()){
            final ListWidgetItem listItem = getItemByVisualRow(row);
            table.applyTextOptions(row, getTextOptions(listItem));
            checkIfSelectionChanged();
            if (getCurrentRow()<0){
                setCurrent(listItem);
            }
        }
    }
    
    public final void onFilterChanged(final String newFilter){
        if (!Objects.equals(currentFilter, newFilter)){
            currentFilter = newFilter;
            applyCurrentFilter();
        }
    }
    
    public final void add(final ListWidgetItem item) {
        if (item!=null){
            if (isSortingEnabled()){
                logicalItems.add(item);
                applySorting();
            }else{
                logicalItems.add(item);
                addRow(item, -1);
            }
            item.addListener(listWidgetItemListener);
            updateAdditionalColumn();
        }
    }    
    
    public void insert(final ListWidgetItem item, final int row) {
        if (item!=null){
            if (isSortingEnabled()){
                logicalItems.add(row, item);
                applySorting();
            }else{
                if (row>-1){
                    logicalItems.add(row,item);
                }else{
                    logicalItems.add(item);
                }
                addRow(item, row);
            }
            item.addListener(listWidgetItemListener);
            updateAdditionalColumn();
        }
    }    
    
    public final void clear() {
        for (ListWidgetItem item: logicalItems){
            item.removeListener(listWidgetItemListener);
        }
        table.clearContent();
        logicalItems.clear();
        currentItem = null;
        currentVisualRow = -1;        
    }    
    
    public final void remove(final ListWidgetItem item) {
        final int row = logicalItems.indexOf(item);
        if (row>-1){
            removeRow(row);
        }
    }
    
    public final void removeRow(final int row) {
        if (row>-1 && row<logicalItems.size()){
            final ListWidgetItem item = logicalItems.get(row);
            item.removeListener(listWidgetItemListener);
            table.removeRow(getVisualRow(row));
            logicalItems.remove(row);            
            updateAdditionalColumn();
        }
    }    
    
    public final void swapItems(final ListWidgetItem item1, final ListWidgetItem item2) {
        final int row1 = logicalItems.indexOf(item1);
        final int row2 = logicalItems.indexOf(item2);
        swapRows(row1, row2);
    }
    
    public final void swapRows(final int row1, final int row2) {
        if (row1>-1 && row2>-1 && row1!=row2){
            if (isSortingEnabled()){
                final ListWidgetItem item1 = logicalItems.get(row1);
                final ListWidgetItem item2 = logicalItems.get(row2);
                logicalItems.set(row1, item2);
                logicalItems.set(row2, item1);
                applySorting();
            }else{
                blockSelectionListeners = true;
                try{
                    final ListWidgetItem item1 = table.getListWidgetItem(row1);
                    final ListWidgetItem item2 = table.getListWidgetItem(row2);
                    table.swapRows(row1, row2);

                    logicalItems.set(row1, item2);
                    logicalItems.set(row2, item1);

                    updateRow(row1, item2);
                    updateRow(row2, item1);
                    
                    applyCurrentFilter();
                }finally{
                    blockSelectionListeners = false;
                }
            }
        }
    }
    
    public final void setCurrent(final ListWidgetItem item) {
        final int row = logicalItems.indexOf(item);
        if (row>-1){
            setCurrentRow(row);
        }
    }    
        
    public final ListWidgetItem getCurrent() {
        return currentItem;
    }
    
    public final void setCurrentRow(final int row) {        
        table.setCurrentRowIndex(getVisualRow(row));
    }
    
    public final int getCurrentRow() {
        return currentItem==null ? -1 : logicalItems.indexOf(currentItem);
    }    
    
    public final ListWidgetItem getItem(final int row) {
        return row>-1 && row<logicalItems.size() ? logicalItems.get(row) : null;
    }
    
    public final void setItems(final List<ListWidgetItem> items) {
        clear();
        if (items!=null){
            for (ListWidgetItem item: items){
                if (item!=null){
                    this.logicalItems.add(item);
                }
            }
        }
        refillTable(getOrderedItems());
        updateAdditionalColumn();
    }
    
    public final List<ListWidgetItem> getItems() {
        return Collections.unmodifiableList(logicalItems);
    }
    
    public final int count() {
        return logicalItems.size();
    }
    
    public final void setFeatures(final EnumSet<IListWidget.EFeatures> features) {
        if (!Objects.equals(features, this.features)){
            final boolean keepSorting = isSortingEnabled();
            final String keepFilter = currentFilter;
            this.features.clear();            
            if (features!=null){
                this.features.addAll(features);
            }
            applyFeatures();
            if (keepSorting!=isSortingEnabled()){
                applySorting();
            }else if (!Objects.equals(keepFilter, currentFilter)){
                applyCurrentFilter();
            }
        }
    }
    
    public final EnumSet<IListWidget.EFeatures> getFeatures() {
        return features.clone();
    }        
    
    public final boolean isItemSelected(final ListWidgetItem listItem){
        for (int row=0, count=table.getRowCount(); row<count; row++){
            if (getItemByVisualRow(row)==listItem){
                return table.isRowSelected(row);
            }
        }
        return false;
    }              
    
    public final void setTextOptions(final ITextOptions textOptions){
        this.textOptions = textOptions;
        for (int row=0,count=table.getRowCount(); row<count; row++){
            final ITextOptions options = getTextOptions(getItemByVisualRow(row));
            table.applyTextOptions(row, options);
        }
        presenter.applyTextOptions(textOptions);
    }    
    
    public final ITextOptions getTextOptions(){
        if (textOptions==null){
            return environment.getTextOptionsProvider().getOptions(EnumSet.of(ETextOptionsMarker.EDITOR), ESelectorRowStyle.NORMAL);
        }else{
            return textOptions;
        }
    }    
    
    public final void setSelectedItems(final Collection<ListWidgetItem> selectedItems) {
        if (isMultiSelectEnabled()){
            blockSelectionListeners = true;
            try{
                for (int row=0,count=table.getRowCount(); row<count; row++){
                    table.setRowSelected(row, selectedItems!=null && selectedItems.contains(getItemByVisualRow(row)));
                }
            }finally{
                blockSelectionListeners = false;
            }
            checkIfSelectionChanged();
        }
    }    
    
    public final List<ListWidgetItem> getSelectedItems() {
        final List<ListWidgetItem> selectedItems = new LinkedList<>();
        for (Integer row: selectedRows){
            selectedItems.add(logicalItems.get(row));
        }
        return selectedItems;
    }    
    
    public final List<Integer> getSelectedRows() {
        return Collections.unmodifiableList(selectedRows);
    }    
    
    public final void setItemSelected(final ListWidgetItem item, final boolean isSelected) {
        if (item==null){
            throw new IllegalArgumentException("item cannot be null");
        }
        if (isMultiSelectEnabled()){
            for (int row=0,count=table.getRowCount(); row<count; row++){
                if (item.equals(getItemByVisualRow(row))){
                    table.setRowSelected(row, isSelected);
                }
            }
        }
    }
    
    private int addRow(final ListWidgetItem item, final int row){        
        final int newRow;
        internalSelectionChange = true;
        try{
            if (row>-1){            
                table.insertRow(item, row);
                newRow = row;
            }else{
                newRow = table.addRow(item);
            }
        }finally{
            internalSelectionChange = false;
        }
        updateRow(newRow, item);
        applyCurrentFilter(newRow);
        item.addListener(listWidgetItemListener);
        return newRow;
    }
    
    private void refillTable(final List<ListWidgetItem> orderedItems){
        final List<ListWidgetItem> selectedItems;
        if (isMultiSelectEnabled()){
            selectedItems = new LinkedList<>();
            for (int row=0,count=table.getRowCount(); row<count; row++){
                if (table.isRowSelected(row)){
                    selectedItems.add(table.getListWidgetItem(row));
                }
            }
        }else{
            selectedItems = Collections.emptyList();
        }
        
        internalCurrentRowChange = true;
        internalSelectionChange = true;
        blockSelectionListeners = true;
        try{
            final ListWidgetItem keepCurrentItem = currentItem;            
            currentItem = null;
            currentVisualRow = -1;
            table.clearContent();            
            
            for (ListWidgetItem item : orderedItems){
                int row = addRow(item, -1);
                if (item==keepCurrentItem && !table.isRowHidden(row)){
                    table.setCurrentRowIndex(row);
                    currentItem = item;
                    currentVisualRow = row;
                }
            }
            if (keepCurrentItem!=null && currentItem==null){                
                setCurrentToFirstVisibleRow();
                if (currentItem==null){
                    notifyCurrentItemListeners(null);
                }
            }
            
            for (ListWidgetItem item: selectedItems){
                setItemSelected(item, true);
            }            
        }finally{
            internalCurrentRowChange = false;
            internalSelectionChange = false;
            blockSelectionListeners = false;
        }
        checkIfSelectionChanged();
    }    

    private void afterChangeListItemAttribute(final ListWidgetItem listItem, final ListWidgetItem.EAttribute attribute){
        if (isSortingEnabled() &&
            (attribute==ListWidgetItem.EAttribute.TEXT 
            || attribute==ListWidgetItem.EAttribute.VALUE 
            || attribute==ListWidgetItem.EAttribute.USER_DATA)){
            applySorting();
        }else{
            final int logicalRow = logicalItems.indexOf(listItem);
            final int visualRow = getVisualRow(logicalRow);
            switch(attribute){
                case TEXT:
                case ICON:
                    updateGeneralCell(visualRow, listItem);
                    break;
                case TOOL_TIP:
                case IS_SELECTABLE:
                    updateCommonOptions(visualRow, listItem);
                    break;
                case EXT_INFO:
                    updateAdditionalCell(visualRow, listItem);
                    updateAdditionalColumn();
                    break;
                case TEXT_OPTIONS:
                case VALIDATION_RESULT:
                    final ITextOptions options = getTextOptions(listItem);
                    table.applyTextOptions(visualRow, options);
                    break;
            }
            applyCurrentFilter(visualRow);
        }
    }    
    
    private boolean isMultiSelectEnabled(){
        return features.contains(IListWidget.EFeatures.MULTI_SELECT);
    }
    
    private boolean isSelectionNotEmpty() {
        return features.contains(IListWidget.EFeatures.SELECTION_NOT_EMPTY);
    }
    
    private boolean isSelectionLabelVisible(){
        return features.contains(IListWidget.EFeatures.SELECTION_LABEL);
    }
    
    private boolean isSortingEnabled(){
        if (features.contains(IListWidget.EFeatures.AUTO_SORTING)){
            return true;
        }
        if (features.contains(IListWidget.EFeatures.MANUAL_SORTING)){
            return presenter.isSortItemsChecked();
        }
        return false;
    }    
    
    public final void setItemsComparator(final Comparator<ListWidgetItem> comparator) {
        itemsComparator = comparator;
        if (isSortingEnabled()){
            applySorting();
        }
    }    

    public final Comparator<ListWidgetItem> getItemsComparator() {
        return itemsComparator==null ? DEFAULT_COMPARATOR : itemsComparator;
    }        
    
    public final void setSelectionEnabled(boolean isEnabled) {
        table.setSingleSelectionEnabled(isEnabled);
        for (int row=0,count=table.getRowCount(); row<count; row++){
            updateCommonOptions(row, currentItem);
        }
    }

    public final boolean isSelectionEnabled() {
        return table.isSingleSelectionEnabled();
    }            
    
    public final void addCurrentItemListener(final IListWidget.ICurrentItemListener listener) {
        if (listener!=null){
            if (currentItemListeners==null){
                currentItemListeners = new LinkedList<>();
            }
            if (!currentItemListeners.contains(listener)){
                currentItemListeners.add(listener);
            }
        }
    }
    
    public final void removeCurrentItemListener(final IListWidget.ICurrentItemListener listener) {
        if (listener!=null && currentItemListeners!=null){
            currentItemListeners.remove(listener);
            if (currentItemListeners.isEmpty()){
                currentItemListeners = null;
            }
        }
    }
    
    private void notifyCurrentItemListeners(final ListWidgetItem item){
        if (currentItemListeners!=null){
            final List<IListWidget.ICurrentItemListener> copy = new LinkedList<>(currentItemListeners);
            for (IListWidget.ICurrentItemListener listener: copy){
                listener.currentItemChanged(item);
            }
        }
    }
    
    public final void addDoubleClickListener(final IListWidget.IDoubleClickListener listener) {
        if (listener!=null){
            if (doubleClickListeners==null){
                doubleClickListeners = new LinkedList<>();
            }
            if (!doubleClickListeners.contains(listener)){
                doubleClickListeners.add(listener);
            }
        }
    }
           
    public final void removeDoubleClickListener(final IListWidget.IDoubleClickListener listener) {
        if (listener!=null && doubleClickListeners!=null){
            doubleClickListeners.remove(listener);
            if (doubleClickListeners.isEmpty()){
                doubleClickListeners = null;
            }
        }
    }                
    
    private void notifyDoubleClickListeners(final ListWidgetItem item){
        if (doubleClickListeners!=null){
            final List<IListWidget.IDoubleClickListener> listeners = new LinkedList<>(doubleClickListeners);
            for (IListWidget.IDoubleClickListener listener: listeners){
                listener.itemDoubleClick(item);
            }
        }
    }

    public final void addFilterListener(final IListWidget.IFilterListener listener) {
        if (listener!=null){
            if (filterListeners==null){
                filterListeners = new LinkedList<>();
            }
            if (!filterListeners.contains(listener)){
                filterListeners.add(listener);
            }
        }
    }

    public final void removeFilterListener(final IListWidget.IFilterListener listener) {
        if (listener!=null && filterListeners!=null){
            filterListeners.remove(listener);
            if (filterListeners.isEmpty()){
                filterListeners = null;
            }
        }
    }
    
    private void notifyFilterListeners(final String filterText){
        if (filterListeners!=null){
            final List<IListWidget.IFilterListener> listeners = new LinkedList<>(filterListeners);
            for (IListWidget.IFilterListener listener: listeners){
                listener.afterApplyFilter(filterText);
            }
        }
    }    

    public final void addSelectionListener(final IListWidget.ISelectionListener listener) {
        if (listener!=null){
            if (selectionListeners==null){
                selectionListeners = new LinkedList<>();
            }
            if (!selectionListeners.contains(listener)){
                selectionListeners.add(listener);
            }
        }
    }

    public final void removeSelectionListener(final IListWidget.ISelectionListener listener) {
        if (listener!=null && selectionListeners!=null){
            selectionListeners.remove(listener);
            if (selectionListeners.isEmpty()){
                selectionListeners = null;
            }
        }
    }
    
    private void notifySelectionListeners(final List<ListWidgetItem> selectedItems){
        if (selectionListeners!=null){
            final List<IListWidget.ISelectionListener> listeners = new LinkedList<>(selectionListeners);
            for (IListWidget.ISelectionListener listener: listeners){
                listener.selectionChanged(selectedItems);
            }
        }
    }
    
    public final void applyFeatures(){
        presenter.setFilterVisible(features.contains(IListWidget.EFeatures.FILTERING));
        final boolean isManualSorting = 
            features.contains(IListWidget.EFeatures.MANUAL_SORTING) && !features.contains(IListWidget.EFeatures.AUTO_SORTING);
        presenter.setSortItemsVisible(isManualSorting);
        if (!isManualSorting){
            presenter.setSortItemsChecked(false);
        }
        table.setMultiSelectionEnabled(isMultiSelectEnabled());
        if (isSelectionLabelVisible()){
            updateSelectionLabel();
            presenter.setSelectionLabelVisible(true);
        }else{
            presenter.setSelectionLabelVisible(false);
        }
    }
    
    public final void selectAllItems(){
        changeRangeSelection(0, table.getRowCount()-1, true);
    }
    
    private void checkIfSelectionChanged(){
        if (!blockSelectionListeners){            
            final List<Integer> currentSelection;
            if (isMultiSelectEnabled()){
                currentSelection = new LinkedList<>();
                for (int i=0,count=logicalItems.size(); i<count; i++){
                    if (isItemSelected(logicalItems.get(i))){
                        currentSelection.add(i);
                    }
                }
                if (currentSelection.isEmpty() && currentVisualRow>=0 && isSelectionNotEmpty()){                    
                    currentSelection.add(getLogicalRow(currentVisualRow));
                }
            }else{
                if (currentVisualRow>=0){
                    currentSelection = Collections.singletonList(getLogicalRow(currentVisualRow));
                }else{
                    currentSelection = Collections.emptyList();
                }
            }
            if (!currentSelection.equals(selectedRows)){
                selectedRows.clear();
                selectedRows.addAll(currentSelection);
                Collections.sort(selectedRows);
                notifySelectionListeners(getSelectedItems());
                updateSelectionLabel();
            }            
        }
    }
    
    private ListWidgetItem getItemByVisualRow(final int row){
        if (isSortingEnabled()){
            if (row>-1 && row<table.getRowCount()){
                return table.getListWidgetItem(row);
            }else{
                return null;
            }
        }else{
            return getItem(row);
        }
    }    
    
    private int getVisualRow(final int logicalRow){
        if (isSortingEnabled()){
            if (logicalRow<0 || logicalRow>=logicalItems.size()){
                return -1;
            }else{
                final ListWidgetItem item = logicalItems.get(logicalRow);
                for (int row=0, count=table.getRowCount(); row<count; row++){
                    if (getItemByVisualRow(row)==item){
                        return row;
                    }
                }
                return -1;
            }
        }else{
            return logicalRow;
        }
    }
    
    private int getLogicalRow(final int visualRow){
        if (isSortingEnabled()){
            final ListWidgetItem item = table.getListWidgetItem(visualRow);
            return logicalItems.indexOf(item);
        }else{
            return visualRow;
        }
    }
    
    private List<ListWidgetItem> getOrderedItems(){
        final List<ListWidgetItem> orderedItems = new ArrayList<>(logicalItems);
        if (isSortingEnabled()){
            Collections.sort(orderedItems,getItemsComparator());
            return orderedItems;                        
        }else{
            return orderedItems;
        }
    }    
    
    private void changeRangeSelection(final int visualRow1, final int visualRow2, final boolean isChecked){
        blockSelectionListeners = true;
        try{
            for (int row=Math.min(visualRow1, visualRow2),last=Math.max(visualRow1, visualRow2); row<=last; row++){
                if (!table.isRowHidden(row)){
                    table.setRowSelected(row, isChecked);
                }
            }
        }finally{
            blockSelectionListeners = false;
        }
        checkIfSelectionChanged();
    }
    
    private void updateRow(final int row, final ListWidgetItem listItem){
        updateGeneralCell(row, listItem);
        updateAdditionalCell(row, listItem);
        updateCommonOptions(row, listItem);
        table.applyTextOptions(row, getTextOptions(listItem));
    }
        
    private void updateGeneralCell(final int row, final ListWidgetItem listItem){
        table.setText(row, listItem.getText());
        table.setIcon(row, listItem.getIcon());        
    }
    
    private void updateAdditionalCell(final int row, final ListWidgetItem listItem){
        table.setDescription(row, listItem.getExtendedTitle());
    }
    
    private void updateCommonOptions(final int row, final ListWidgetItem listItem){
        table.setRowToolTip(row, listItem.getToolTip());
        final boolean canSetCurrent = isSelectionEnabled();
        if (listItem.isSelectable()){
            table.setRowOptions(row, true, canSetCurrent);
        }else{
            table.setRowOptions(row, false, canSetCurrent);
        }
    }
    
    private ITextOptions getTextOptions(final ListWidgetItem listItem){
        ITextOptions itemOptions;
        if (listItem.getTextOptions()==null){
            itemOptions = getTextOptions();
            if (listItem.getValidationResult()!=null && listItem.getValidationResult().getState()!=EValidatorState.ACCEPTABLE){
                itemOptions = itemOptions.changeForegroundColor(Color.red);
            }
        }else{
           itemOptions = listItem.getTextOptions();
        }
        if (isItemSelected(listItem)){
            return itemOptions.changeBackgroundColor(getSelectionColor());
        }else{
            return itemOptions;
        }
    }
    
    private void updateAdditionalColumn(){
        boolean isAdditionalColumnVisible = false;
        for (ListWidgetItem item: logicalItems){
            if (item.getExtendedTitle()!=null && !item.getExtendedTitle().isEmpty()){
                isAdditionalColumnVisible = true;
            }
        }
        table.setItemsDescriptionVisible(isAdditionalColumnVisible);
    }    
    
    public final void applySorting(){
        refillTable(getOrderedItems());
    }
    
    protected void applyCurrentFilter(){        
        blockRangeSelection = true;
        try{
            for (int row=0,count=table.getRowCount(); row<count; row++){
                table.setRowHidden(row, !getItemByVisualRow(row).isMatchToFilter(currentFilter));
            }
            afterApplyFilter(currentFilter);
        }finally{
            blockRangeSelection = false;
        }
    }
    
    protected void applyCurrentFilter(final int visualRow){
        table.setRowHidden(visualRow, !getItemByVisualRow(visualRow).isMatchToFilter(currentFilter));
    }
    
    public final void afterApplyFilter(final String filter){
        if (currentVisualRow<0 || table.isRowHidden(currentVisualRow)){
            setCurrentToFirstVisibleRow();
        }
        notifyFilterListeners(filter);
    }
    
    private boolean setCurrentToFirstVisibleRow(){
        for (int row=0,count=table.getRowCount(); row<count; row++){
            if (!table.isRowHidden(row)){
                setCurrent(getItemByVisualRow(row));
                return true;
            }
        }
        return false;
    }    
    
    private Color getSelectionColor(){
        final String colorAsStr = environment.getConfigStore().readString(SELECTION_COLOR_SETTING_KEY, "#FFFF66");
        try{
            return Color.decode(colorAsStr);
        }catch(NumberFormatException exception){
            return Color.YELLOW;
        }        
    }
    
    private void updateSelectionLabel(){
        if (isSelectionLabelVisible()){
            final List<ListWidgetItem> selectedItems = getSelectedItems();
            final MessageProvider mp = environment.getMessageProvider();
            if (selectedItems.isEmpty()){
                presenter.setSelectionLabelText(mp.translate("ExplorerDialog", "No items selected"));
            }else if (selectedItems.size()==1){
                final String messageTemplate = mp.translate("ExplorerDialog", "Selected item: %1$s");
                presenter.setSelectionLabelText(String.format(messageTemplate, selectedItems.get(0).getText()));
            }else{
                final String messageTemplate = mp.translate("ExplorerDialog", "Number of selected items: %1$s");
                presenter.setSelectionLabelText(String.format(messageTemplate, selectedItems.size()));
            }
        }
    }    
}
