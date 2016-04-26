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

package org.radixware.kernel.explorer.editors;

import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QDragMoveEvent;
import com.trolltech.qt.gui.QDropEvent;
import com.trolltech.qt.gui.QKeyEvent;
import com.trolltech.qt.gui.QListWidget;
import com.trolltech.qt.gui.QListWidgetItem;
import com.trolltech.qt.gui.QWidget;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.meta.RadEnumPresentationDef;
import org.radixware.kernel.common.client.meta.RadEnumPresentationDef.Item;
import org.radixware.kernel.common.client.meta.RadEnumPresentationDef.Items;
import org.radixware.kernel.common.enums.EEditMaskEnumOrder;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.env.ExplorerIcon;


public final class EnumItemsListWidget extends QListWidget {
    
    public final static class SelectionPart{
        
        private final int startRow;
        private final int count;
        
        public SelectionPart(final int startRow, final int count){
            this.startRow = startRow;
            this.count = count;
        }

        public int getStartRow() {
            return startRow;
        }

        public int getCount() {
            return count;
        }
    }
    
    private boolean isSortingEnabled = false;
    private boolean isMoveOperationsEnabled = true;
    private EEditMaskEnumOrder sortingOrder;
    private final RadEnumPresentationDef enumDef;
    public final Signal0 ctrlRightPressed = new Signal0();
    public final Signal0 ctrlLeftPressed = new Signal0();
    public final Signal0 deletePressed = new Signal0();
    public final Signal2<List<SelectionPart>, SelectionPart> afterDrop = new Signal2<>();
    
    public EnumItemsListWidget(final QWidget parent, final Items items) {
        super(parent);
        setSelectionMode(SelectionMode.ExtendedSelection);
        setDragEnabled(true);
        setDragDropMode(DragDropMode.DragDrop);       
        
        if(items == null) {
            enumDef = null;
        } else {
            enumDef = items.getEnumPresentationDef();
            init(items);
        }
    }

    private void init(final Items items) {
        clear();
        QListWidgetItem listItem;
        String text;
        for(Item i : items) {
            if (i.getTitle()!=null && !i.getTitle().isEmpty()){
                text = i.getTitle();
            }else{
                text = i.getName();
            }
            listItem = new QListWidgetItem();
            listItem.setText(text);
            listItem.setData(Qt.ItemDataRole.UserRole, i.getId().toString());
            listItem.setIcon(ExplorerIcon.getQIcon(i.getIcon()));
            addItem(listItem);
            listItem.setFlags(Qt.ItemFlag.ItemIsDragEnabled, Qt.ItemFlag.ItemIsDropEnabled, Qt.ItemFlag.ItemIsEnabled, Qt.ItemFlag.ItemIsSelectable);
        }
    }
    
    @Override
    protected void dragMoveEvent(final QDragMoveEvent e) {
        if (!isMoveOperationsEnabled && e.source()==this){
            e.setDropAction(Qt.DropAction.IgnoreAction);
        }else{
            e.setDropAction(Qt.DropAction.MoveAction);
        }        
    }
    
    

    @Override
    protected void dropEvent(final QDropEvent event) {
        if (!isMoveOperationsEnabled && event.source()==this){
            event.setDropAction(Qt.DropAction.IgnoreAction);
            return;
        }
        final QListWidget eventSource = (QListWidget) event.source();
  
        if(this != eventSource) {
            this.clearSelection();
        } 
        final QModelIndex indexUnderCursor = this.indexAt(event.pos());
        int currentInsertionIndex = (indexUnderCursor == null) || !isMoveOperationsEnabled ? 
                -1 : indexUnderCursor.row();
        final List<QListWidgetItem> selection = eventSource.selectedItems();
        final List<SelectionPart> selectionParts = getSelectionParts(eventSource);

        int startIndex = currentInsertionIndex<0 ? count() : currentInsertionIndex+1;        
        for(QListWidgetItem selectedItem : selection) {
            final int selectedIndex = eventSource.row(selectedItem);
            final QListWidgetItem currentItem = eventSource.takeItem(selectedIndex);

            //if list is empty or area is empty ...
            if(currentInsertionIndex < 0) {
                this.addItem(currentItem);
            } else {
                this.insertItem(currentInsertionIndex++, currentItem);
            }
            setCurrentItem(currentItem);
        }

        if(isSortingEnabled) {
            sort();
        }
        if (event.source()!=this){
            afterDrop.emit(selectionParts, new SelectionPart(startIndex, selection.size()));
        }
    }

    @Override
    protected void keyPressEvent(final QKeyEvent event) {
        final int key = event.key();
        final Qt.KeyboardModifiers modifiers = event.modifiers();
        if(modifiers.isSet(Qt.KeyboardModifier.ControlModifier)) { 
            if(key == Qt.Key.Key_Down.value() ) {
                moveDown();
            } else if(key == Qt.Key.Key_Up.value()) {
                moveUp();
            } else if(key == Qt.Key.Key_Right.value()) {
                ctrlRightPressed.emit();
            } else if(key == Qt.Key.Key_Left.value()) {
                ctrlLeftPressed.emit();
            }
            return;
        }
        
        if(key == Qt.Key.Key_Delete.value()) {
            deletePressed.emit();
        } else {
            super.keyPressEvent(event);
        }
    }
    
    public EEditMaskEnumOrder getSortingOrder(){
        return sortingOrder;
    }
    
    public void setSortingOrder(final EEditMaskEnumOrder order) {
        if (order!=null){
            isSortingEnabled = true;
            sortingOrder = order;
            sort();
        }else{
            sortingOrder = null;
        }
    }
    
    public boolean sortingEnabled() {
        return isSortingEnabled;
    }
    
    public void sort() {
        if(isSortingEnabled) {
            selectAll();
            final List<QListWidgetItem> listOfItem = selectedItems();
            final Items radEnumItems = enumDef.getEmptyItems();
            for(QListWidgetItem i : listOfItem) {
                final Id id = Id.Factory.loadFrom( (String)i.data(Qt.ItemDataRole.UserRole) );
                radEnumItems.addItemWithId(id);
            }
            radEnumItems.sort(RadEnumPresentationDef.ItemsOrder.fromEditMaskEnumOrder(sortingOrder));
            init(radEnumItems);
        }
    }
    
    public void moveUp() {
        if(isSortingEnabled || !isMoveOperationsEnabled) { 
            return;
        }
        final int from = currentRow();
        if(from > 0) {
            swap(from, from - 1);
            setCurrentRow(from - 1);
        }
    }
    
    public void moveDown() {
        if(isSortingEnabled || !isMoveOperationsEnabled) { 
            return;
        }
        final int from = currentRow();
        if(from < count() - 1) {
            swap(from, from + 1);
            setCurrentRow(from + 1);
        }
    }
    
    public void setMoveOperationEnabled(final boolean isEnabled){
        isMoveOperationsEnabled = isEnabled;
    }
    
    public boolean isMoveOperationEnabled(){
        return isMoveOperationsEnabled;
    }
    
    private void swap(final int from, final int to) {
        final QListWidgetItem itemToMove = takeItem(from);
        insertItem(to, itemToMove);
    }
    
    public QListWidgetItem getItemByUserData(final Object data) {
        for (int row=count()-1; row>=0; row--){
            final QListWidgetItem item = item(row);
            if (item.data(Qt.ItemDataRole.UserRole).equals(data)){
                return item;
            }
        }
        
        return null;
    }        
    
    public List<SelectionPart> getSelectionParts(){
        return getSelectionParts(this);
    }
    
    private static List<SelectionPart> getSelectionParts(final QListWidget listWidget){
        final List<QListWidgetItem> selectedItems = listWidget.selectedItems();
        final List<SelectionPart> parts = new LinkedList<>();
        int currentStartRow = -1; int currentCount = -1;
        for (int row=0, count=listWidget.count(); row<count; row++){
            if (selectedItems.contains(listWidget.item(row))){
                if (row-currentCount==currentStartRow){
                    currentCount++;
                }else{
                    if (currentCount>0){
                        parts.add(new SelectionPart(currentStartRow, currentCount));
                    }
                    currentStartRow = row;
                    currentCount = 1;
                }
            }
        }
        if (currentCount>0){
            parts.add(new SelectionPart(currentStartRow, currentCount));
        }
        return parts;        
    }
}