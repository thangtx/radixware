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

package org.radixware.kernel.explorer.editors.editmask.listeditor;

import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.gui.QTableWidgetItem;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EEditMaskOption;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.explorer.editors.editmask.IEditMaskEditorSetting;
import org.radixware.schemas.editmask.EditMaskList.Item;


final class ListValuesTableSetting extends QWidget implements IEditMaskEditorSetting {
    private final RdxListEditorWidget valuesTable;
    
    public ListValuesTableSetting(final IClientEnvironment environment, final QWidget parent, final EValType type) {
        super(parent);
        valuesTable = new RdxListEditorWidget(environment, this, type);
                
        final QVBoxLayout layout = new QVBoxLayout();
        layout.setMargin(0);
        layout.addWidget(valuesTable);
        this.setLayout(layout);
    }
    
    @Override
    public void addToXml(final org.radixware.schemas.editmask.EditMask editMask) {
        final org.radixware.schemas.editmask.EditMaskList editMaskList = editMask.getList(); 
        final int rowCount = valuesTable.rowCount();
        org.radixware.schemas.editmask.EditMaskList.Item item = null;
        String title, value;
        valuesTable.finishEditing();
        for(int i = 0; i < rowCount; i++) {
            item = editMaskList.addNewItem();
            value = valuesTable.item(i, 0).text();
            item.setValue(value);
            title = valuesTable.item(i, 1).text();
            item.setTitle(title);
        }
    }

    @Override
    public void loadFromXml(final org.radixware.schemas.editmask.EditMask editMask) {
        valuesTable.clearContents();
        valuesTable.setRowCount(0);
        final List<Item> items = editMask.getList().getItemList();
        int index = 0;
        for(Item i : items) {
            addNewRow();
            final String value = i.getValue();
            valuesTable.setItem( index, 0, new QTableWidgetItem(value) );
            final String title = i.getTitle();
            valuesTable.setItem( index++, 1, new QTableWidgetItem(title) );
        }
    }

    @Override
    public EEditMaskOption getOption() {
        return EEditMaskOption.LIST_VALUES;
    }

    @Override
    public void setDefaultValue() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object getValue() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public boolean validate() {
        return valuesTable.validate();
    }
    
    public void addNewRow() {
        final int rowCount = valuesTable.rowCount();
        valuesTable.setRowCount(rowCount + 1);
        valuesTable.setItem(rowCount, 0, new QTableWidgetItem());
        final QTableWidgetItem item = new QTableWidgetItem("New item " + (rowCount +1));
        valuesTable.setItem(rowCount, 1, item);
    }
    
    public void removeRow() {
        final int index = getSelectedRowIndex();
        /*final QTableWidgetItem valItem = */valuesTable.takeItem(index, 0);
        /*final QTableWidgetItem titleItem =*/ valuesTable.takeItem(index, 1);
        valuesTable.removeRow(index);
    }
    
    /**
     * Swaps QTableWidgetItem objects inside of QTableWidget as far as stepOfSwap directs.
     * @param index
     * @param stepOfSwap 
     */
    private void swapAtIndex(final int index, final int stepOfSwap) {
        // No need to move if: stepOfSwap==0; index points to the first item when moving up;
        //                      index points to the last item when moving down.
        final boolean methodCallCondition = (stepOfSwap > 0 && index < valuesTable.rowCount() - 1) ||
                (stepOfSwap < 0 && index > 0);
        if(methodCallCondition) {
            final int columnIndex = valuesTable.currentColumn();
            //swap values items
            QTableWidgetItem currentItem = valuesTable.takeItem(index, 0);
            QTableWidgetItem upperItem = valuesTable.takeItem(index + stepOfSwap, 0);
            valuesTable.setItem(index + stepOfSwap, 0, currentItem);
            valuesTable.setItem(index, 0, upperItem);
            //swap titles items
            currentItem = valuesTable.takeItem(index, 1);
            upperItem = valuesTable.takeItem(index + stepOfSwap, 1);
            valuesTable.setItem(index + stepOfSwap, 1, currentItem);
            valuesTable.setItem(index, 1, upperItem);
            valuesTable.setCurrentCell(index + stepOfSwap, columnIndex);
            //update model
            final QModelIndex modelIndex = valuesTable.model().index(index, (columnIndex+1)%2);
            valuesTable.update(modelIndex);
        }
    }
    
    public void moveRowUp() {
        swapAtIndex(getSelectedRowIndex(), -1);
    }
    
    public void moveRowDown() {
        swapAtIndex(getSelectedRowIndex(), 1);
    }
    
    public int getSelectedRowIndex() {
        return valuesTable.currentRow();
    }
    
}
