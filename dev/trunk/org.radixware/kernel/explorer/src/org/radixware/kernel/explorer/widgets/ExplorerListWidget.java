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

package org.radixware.kernel.explorer.widgets;

import com.trolltech.qt.QSignalEmitter;
import com.trolltech.qt.QtPropertyReader;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QAbstractItemView;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QCheckBox;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QHeaderView;
import com.trolltech.qt.gui.QItemDelegate;
import com.trolltech.qt.gui.QKeyEvent;
import com.trolltech.qt.gui.QKeySequence;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QStyle;
import com.trolltech.qt.gui.QTableWidget;
import com.trolltech.qt.gui.QTableWidgetItem;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import java.awt.Color;
import java.util.Collection;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.text.ITextOptions;
import org.radixware.kernel.common.client.text.TextOptions;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.client.widgets.IListWidget;
import org.radixware.kernel.common.client.widgets.ListWidgetController;
import org.radixware.kernel.common.client.widgets.ListWidgetItem;
import org.radixware.kernel.explorer.editors.valeditors.ValStrEditor;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.text.ExplorerFont;
import org.radixware.kernel.explorer.text.ExplorerTextOptions;
import org.radixware.kernel.explorer.utils.WidgetUtils;


public class ExplorerListWidget extends ExplorerWidget implements IListWidget{ 
    
    private final static class ListWidgetTable extends QTableWidget implements ListWidgetController.IListWidgetTable{
        
        private final static Qt.ItemFlags SELECTABLE_FLAGS = new Qt.ItemFlags(Qt.ItemFlag.ItemIsEnabled, Qt.ItemFlag.ItemIsSelectable);
        private final static Qt.ItemFlags NOT_SELECTABLE_FLAGS = new Qt.ItemFlags(Qt.ItemFlag.ItemIsEnabled);
        private final static Qt.ItemFlags CHECKABLE_FLAGS = new Qt.ItemFlags(Qt.ItemFlag.ItemIsEnabled, Qt.ItemFlag.ItemIsSelectable, Qt.ItemFlag.ItemIsUserCheckable);                
        
        private final static int SELECTION_COLUMN = 0;
        private final static int TITLE_COLUMN = 1;
        private final static int DESCRIPTION_COLUMN = 2;
        private final static int ROW_MARGIN = 0;
                
        private final static int EXT_TITLE_ALIGNMENT = new Qt.Alignment(Qt.AlignmentFlag.AlignRight, Qt.AlignmentFlag.AlignVCenter).value();        
        
        public final QSignalEmitter.Signal0 onSelectAll = new QSignalEmitter.Signal0();
                        
        public ListWidgetTable(final QWidget parent){
            super(0,3,parent);
            verticalHeader().setVisible(false);
            final QHeaderView horizontalHeader = horizontalHeader();
            horizontalHeader.setVisible(false);
            setSelectionMode(QAbstractItemView.SelectionMode.SingleSelection);
            setSelectionBehavior(QAbstractItemView.SelectionBehavior.SelectRows);
            setGridStyle(Qt.PenStyle.NoPen);
            setShowGrid(false);
            horizontalHeader.setResizeMode(SELECTION_COLUMN, QHeaderView.ResizeMode.Fixed);
            horizontalHeader.resizeSection(SELECTION_COLUMN, calcSelectionColumnWidth());
            horizontalHeader.setSectionHidden(SELECTION_COLUMN, true);
            horizontalHeader.setSectionHidden(DESCRIPTION_COLUMN, true);
            horizontalHeader.setStretchLastSection(true);
            setHorizontalScrollMode(QAbstractItemView.ScrollMode.ScrollPerPixel);
            setItemDelegate(new QItemDelegate(this));
            verticalHeader().setResizeMode(QHeaderView.ResizeMode.Fixed);
        }
        
        private int calcSelectionColumnWidth(){
            final int margin = style().pixelMetric(QStyle.PixelMetric.PM_FocusFrameHMargin) + 1;
            final int check_width = style().pixelMetric(QStyle.PixelMetric.PM_IndicatorWidth);
            return check_width+2*margin;
        }        
        
        @Override
        protected void keyPressEvent(final QKeyEvent event) {            
            if (event.matches(QKeySequence.StandardKey.SelectAll)){
                onSelectAll.emit();
            }else{
                super.keyPressEvent(event);
            }
        }        

        @Override
        public void setRowSelected(final int row, final boolean isSelected) {
            item(row, SELECTION_COLUMN).setCheckState(isSelected ? Qt.CheckState.Checked : Qt.CheckState.Unchecked);
        }

        @Override
        public boolean isRowSelected(final int row) {
            return item(row, SELECTION_COLUMN).checkState()==Qt.CheckState.Checked;
        }

        @Override
        public void setText(final int row, final String text) {
            item(row, TITLE_COLUMN).setText(text);
            resizeByContent();
        }

        @Override
        public void setDescription(final int row, final String text) {
            item(row, DESCRIPTION_COLUMN).setText(text);
            resizeByContent();
        }

        @Override
        public void setIcon(final int row, final Icon icon) {
            if (icon==null){
                item(row, TITLE_COLUMN).setIcon(null);
            }else{
                item(row, TITLE_COLUMN).setIcon(ExplorerIcon.getQIcon(icon));
            }
        }

        @Override
        public void setRowOptions(final int row, final boolean isSelectable, final boolean canSetCurrent) {
            for (int column=0, count=columnCount(); column<count; column++){
                if (canSetCurrent){
                    item(row, column).setFlags(column==0 && isSelectable ? CHECKABLE_FLAGS : SELECTABLE_FLAGS);
                }else{
                    item(row, column).setFlags(NOT_SELECTABLE_FLAGS);
                }
            }
        }

        @Override
        public void setCurrentRowIndex(final int row) {
            setCurrentItem(item(row,TITLE_COLUMN));
        }

        @Override
        public int getCurrentRowIndex() {
            return currentRow();
        }

        @Override
        public ListWidgetItem getListWidgetItem(final int row) {
            return (ListWidgetItem)item(row,TITLE_COLUMN).data(Qt.ItemDataRole.UserRole);
        }

        @Override
        public int getRowCount() {
            return rowCount();
        }

        @Override
        public void setRowToolTip(final int row, final String toolTip) {
            for (int column=0, count=columnCount(); column<count; column++){
                item(row,column).setToolTip(toolTip);
            }
        }

        @Override
        public void applyTextOptions(final int row, final ITextOptions options) {
            if (options instanceof ExplorerTextOptions){
                final ExplorerTextOptions textOptions = (ExplorerTextOptions)options;
                textOptions.applyTo(item(row,SELECTION_COLUMN));
                textOptions.applyTo(item(row,TITLE_COLUMN));
                textOptions.changeForegroundColor(Color.GRAY).applyTo(item(row,DESCRIPTION_COLUMN));
                verticalHeader().resizeSection(row, getRowHeight(textOptions));
            }
        }        
        
        private int getRowHeight(final ExplorerTextOptions options){
            return options.getFont().getQFontMetrics().height()+2*ROW_MARGIN;
        }

        @Override
        public void insertRow(final ListWidgetItem item, final int row) {
            this.insertRow(row);
            setupRow(item, row);
        }

        @Override
        public int addRow(final ListWidgetItem item) {
            final int row = rowCount();
            setRowCount(row+1);
            setupRow(item, row);
            return row;
        }        
        
        @Override
        public void clearContent(){
            clear();
            setRowCount(0);
        }
        
        private void setupRow(final ListWidgetItem item, final int row){
            final String itemName = item.getName();
            QTableWidgetItem tableItem = new QTableWidgetItem();
            tableItem.setCheckState(Qt.CheckState.Unchecked);
            if (itemName!=null && !itemName.isEmpty()){
                tableItem.setData(WidgetUtils.MODEL_ITEM_ROW_NAME_DATA_ROLE, itemName);
            }
            setItem(row, SELECTION_COLUMN, tableItem);
            
            tableItem = new QTableWidgetItem();
            tableItem.setData(Qt.ItemDataRole.UserRole, item);
            if (itemName!=null && !itemName.isEmpty()){
                tableItem.setData(WidgetUtils.MODEL_ITEM_CELL_NAME_DATA_ROLE, itemName);
            }
            if (item.getValue() instanceof String){
                tableItem.setData(WidgetUtils.MODEL_ITEM_CELL_VALUE_DATA_ROLE, item.getValue());
                tableItem.setData(WidgetUtils.MODEL_ITEM_CELL_VALUE_IS_NULL_DATA_ROLE, Boolean.FALSE);
            }else if (item.getValue()==null){
                tableItem.setData(WidgetUtils.MODEL_ITEM_CELL_VALUE_IS_NULL_DATA_ROLE, Boolean.TRUE);
            }
            setItem(row, TITLE_COLUMN, tableItem);
            
            tableItem = new QTableWidgetItem();
            tableItem.setTextAlignment(EXT_TITLE_ALIGNMENT);
            setItem(row, DESCRIPTION_COLUMN, tableItem);
        }

        @Override
        public void swapRows(final int row1, final int row2) {
            final QTableWidgetItem selection1 = item(row1, SELECTION_COLUMN);                           
            final QTableWidgetItem general1 = item(row1, TITLE_COLUMN);            
            final ListWidgetItem item1 = (ListWidgetItem)general1.data(Qt.ItemDataRole.UserRole);
            final Qt.CheckState checkState1 = selection1.checkState();

            final QTableWidgetItem selection2 = item(row2, SELECTION_COLUMN);
            final QTableWidgetItem general2 = item(row2, TITLE_COLUMN);
            final ListWidgetItem item2 = (ListWidgetItem)general2.data(Qt.ItemDataRole.UserRole);
            final Qt.CheckState checkState2 = selection2.checkState();

            general1.setData(Qt.ItemDataRole.UserRole, item2);
            selection1.setData(Qt.ItemDataRole.UserRole, item2);
            selection1.setCheckState(checkState2);

            general2.setData(Qt.ItemDataRole.UserRole, item1);                
            selection2.setData(Qt.ItemDataRole.UserRole, item1);
            selection2.setCheckState(checkState1);
        }

        @Override
        public void setItemsDescriptionVisible(final boolean isVisible) {
            final boolean sectionVisible = !horizontalHeader().isSectionHidden(DESCRIPTION_COLUMN);
            if (sectionVisible!=isVisible){
                horizontalHeader().setSectionHidden(DESCRIPTION_COLUMN, !isVisible);
            }
            resizeByContent();
        }
        
        private void resizeByContent(){
            resizeColumnToContents(TITLE_COLUMN);
            if (!horizontalHeader().isSectionHidden(DESCRIPTION_COLUMN)){
                resizeColumnToContents(DESCRIPTION_COLUMN);
            }
            updateRowsHeight();
        }        
        
        private void updateRowsHeight(){
            final QHeaderView view = verticalHeader();
            for (int row=0,count=rowCount(); row<count; row++){
                view.resizeSection(row, getRowHeight( row ) );
            }
        }
        
        private int getRowHeight(final int row){
            final ExplorerFont font = ExplorerFont.Factory.getFont(item(row, TITLE_COLUMN).font());
            return font.getQFontMetrics().height()+2*ROW_MARGIN;
        }        

        @Override
        public void setMultiSelectionEnabled(final boolean isEnabled) {
            horizontalHeader().setSectionHidden(SELECTION_COLUMN, !isEnabled);
        }

        @Override
        public void setSingleSelectionEnabled(final boolean isEnabled) {
            if (isEnabled){
                setSelectionMode(QAbstractItemView.SelectionMode.SingleSelection);
            }else{
                setSelectionMode(QAbstractItemView.SelectionMode.NoSelection);
            }
        }

        @Override
        public boolean isSingleSelectionEnabled() {
            return selectionMode()==QAbstractItemView.SelectionMode.SingleSelection;
        }    
    }
    
    private final class ListWidgetPresenter implements ListWidgetController.IListWidgetPresenter{

        @Override
        public boolean isSortItemsChecked() {
            return cbSortItems.isChecked();
        }

        @Override
        public void setSortItemsChecked(final boolean isChecked) {
            cbSortItems.setChecked(isChecked);
        }

        @Override
        public void setSortItemsVisible(final boolean isVisible) {
            cbSortItems.setVisible(isVisible);
        }

        @Override
        public void setSelectionLabelText(final String text) {
            lbSelection.setText(text);
        }

        @Override
        public void setSelectionLabelVisible(final boolean isVisible) {
            lbSelection.setVisible(isVisible);
        }

        @Override
        public void setFilterVisible(final boolean isVisible) {
            lbFilter.setVisible(isVisible);
            veFilter.setVisible(isVisible);            
            if (isVisible){
                veFilter.setFocus();
            }else{
                veFilter.setValue("");                
                table.setFocus();
            }
        }
        
        @Override
        public void applyTextOptions(final ITextOptions options) {
            if (options instanceof ExplorerTextOptions){
                final ExplorerTextOptions textOptions = (ExplorerTextOptions)options;               
                textOptions.applyTo(cbSortItems);
                textOptions.changeBackgroundColor(null).applyTo(lbFilter);
                veFilter.setDefaultTextOptions(textOptions);
                textOptions.changeBackgroundColor(null).applyTo(lbSelection);
            }
        }
    }
    
    private final QLabel lbFilter = new QLabel(this);
    private final ValStrEditor veFilter;
    private final ListWidgetTable table = new ListWidgetTable(this);
    private final QCheckBox cbSortItems = new QCheckBox(this);    
    private final QLabel lbSelection = new QLabel(this);
    
    private final ListWidgetController controller;
        
    public ExplorerListWidget(final IClientEnvironment environment, final QWidget parentWidget){
        super(environment, parentWidget);
        veFilter = new ValStrEditor(environment, parentWidget);
        veFilter.setMandatory(true);
        controller = new ListWidgetController(environment, new ListWidgetPresenter(), table);
        setupUi();
    }
    
    private void setupUi(){
        final MessageProvider mp = getEnvironment().getMessageProvider();
        final QVBoxLayout mainLayout = WidgetUtils.createVBoxLayout(this);
        
        final QHBoxLayout filterLayout = new QHBoxLayout();
        lbFilter.setText(mp.translate("Value", "&Filter:"));
        getTextOptions().changeBackgroundColor(null).applyTo(lbFilter);
        filterLayout.setWidgetSpacing(3);
        filterLayout.setContentsMargins(0, 0, 0, 10);
        filterLayout.addWidget(lbFilter);
        filterLayout.addWidget(veFilter);
        lbFilter.setBuddy(veFilter);
        veFilter.valueChanged.connect(this,"onFilterChanged()");
        mainLayout.addLayout(filterLayout);
        
        table.currentItemChanged.connect(this,"onCurrentItemChanged(QTableWidgetItem, QTableWidgetItem)");
        table.itemClicked.connect(this,"onItemClick(QTableWidgetItem)");
        table.itemDoubleClicked.connect(this,"onItemDoubleClick(QTableWidgetItem)");                
        table.cellChanged.connect(this,"onCellChanged(Integer, Integer)");
        table.onSelectAll.connect(controller,"selectAllItems()");
        mainLayout.addWidget(table);
        
        cbSortItems.setText(mp.translate("Value", "&Sort"));
        cbSortItems.toggled.connect(controller,"applySorting()");
        mainLayout.addWidget(cbSortItems);
        QWidget.setTabOrder(veFilter, table);
        QWidget.setTabOrder(table, cbSortItems);
        
        mainLayout.addWidget(lbSelection);
        
        controller.applyFeatures();
    }
        
    @SuppressWarnings("unused")
    private void onCurrentItemChanged(final QTableWidgetItem current, final QTableWidgetItem previous){
        final int currentRow = current==null ? -1 : current.row();
        final int previousRow = previous==null ? -1 : previous.row();
        controller.onCurrentRowChanged(currentRow, previousRow, isShiftKeyPressed(), isCtrlKeyPressed());
    }
    
    @SuppressWarnings("unused")
    private void onItemClick(final QTableWidgetItem item) {       
        if (item!=null){
            controller.onCellClick(item.row(), isShiftKeyPressed(), isCtrlKeyPressed());
        }
    }
    
    @SuppressWarnings("unused")
    private void onItemDoubleClick(final QTableWidgetItem item){
        if (item!=null){
            controller.onCellDoubleClick(item.row());
        }
    }
    
    @SuppressWarnings("unused")
    private void onCellChanged(final Integer row, final Integer column){
        if (row!=null && column.intValue()==ListWidgetTable.SELECTION_COLUMN){
            controller.onSelectionChanged(row);
        }
    }
    
    @SuppressWarnings("unused")
    private void onFilterChanged(){
        final String newFilter;
        if (veFilter.getValue()==null || veFilter.getValue().isEmpty()){
            newFilter = null;
        }else{
            newFilter = veFilter.getValue();
        }
        controller.onFilterChanged(newFilter);
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
    @QtPropertyReader(name = "textOptions", enabled = false)
    public ExplorerTextOptions getTextOptions(){
        final ITextOptions textOptions = controller.getTextOptions();
        return textOptions==null ? ExplorerTextOptions.getDefault() : (ExplorerTextOptions)textOptions;
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
        if (!veFilter.isHidden()){
            veFilter.setFocus();
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
    
    private static boolean isShiftKeyPressed(){
        final Qt.KeyboardModifiers modifiers = QApplication.keyboardModifiers();
        return modifiers.value()==Qt.KeyboardModifier.ShiftModifier.value();
    }
    
    private static boolean isCtrlKeyPressed(){
        final Qt.KeyboardModifiers modifiers = QApplication.keyboardModifiers();
        return modifiers.value()==Qt.KeyboardModifier.ControlModifier.value();
    }            
}