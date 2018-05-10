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
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QTableWidgetItem;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EEditMaskOption;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.dialogs.SelectImageDialog;
import org.radixware.kernel.explorer.editors.editmask.IEditMaskEditorSetting;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.schemas.editmask.EditMaskList.Item;

final class ListValuesTableSetting extends QWidget implements IEditMaskEditorSetting {
    private final RdxListEditorWidget valuesTable;
    private final IClientEnvironment environment;
    private SelectImageDialog selectImgDlg;
    public ListValuesTableSetting(final IClientEnvironment environment, final QWidget parent, final EValType type) {
        super(parent);
        this.environment = environment;
        valuesTable = new RdxListEditorWidget(environment, this, type);
        valuesTable.verticalHeader().show();
        valuesTable.cellDoubleClicked.connect(this, "cellDoubleClickedSlot(Integer, Integer)");
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
        Id iconId;
        valuesTable.finishEditing();
        for(int i = 0; i < rowCount; i++) {
            item = editMaskList.addNewItem();
            iconId = (Id)valuesTable.item(i, 0).data(Qt.ItemDataRole.UserRole);
            item.setIconId(iconId);
            value = valuesTable.item(i, 1).text();
            item.setValue(value);
            title = valuesTable.item(i, 2).text();
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
            Id id = i.getIconId();
            final QIcon icon = id == null ? null : ExplorerIcon.getQIcon(environment.getDefManager().getImage(i.getIconId()));
            QTableWidgetItem iconedWidgetItem = new QTableWidgetItem(icon, "");
            iconedWidgetItem.setData(Qt.ItemDataRole.UserRole, i.getIconId());
            iconedWidgetItem.setFlags(Qt.ItemFlag.ItemIsSelectable, Qt.ItemFlag.ItemIsEnabled);
            valuesTable.setItem(index, 0, iconedWidgetItem);
            final String value = i.getValue();
            valuesTable.setItem(index, 1, new QTableWidgetItem(value));
            String title = i.getTitle();
            if (title == null) {
                Id titleId = i.getTitleId();
                title = environment.getApplication().getDefManager().getMlStringValue(i.getTitleOwnerId(), titleId);
            }
            valuesTable.setItem(index++, 2, new QTableWidgetItem(title));
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
            currentItem = valuesTable.takeItem(index, 2);
            upperItem = valuesTable.takeItem(index + stepOfSwap, 2);
            valuesTable.setItem(index + stepOfSwap, 2, currentItem);
            valuesTable.setItem(index, 2, upperItem);
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
    
    private void cellDoubleClickedSlot(Integer row, Integer column) {
        if (column == 0) {
            selectImgDlg = new SelectImageDialog(environment, null);
            selectImgDlg.accepted.connect(this, "getSelectedIcon()");
            selectImgDlg.execDialog();
        }
    }
    
    @SuppressWarnings("unused")
        private void getSelectedIcon() {
            Id iconId = selectImgDlg.getIconId();
            QTableWidgetItem item = valuesTable.selectedItems().get(0);
            QIcon icon = iconId == null ? null : ExplorerIcon.getQIcon(environment.getDefManager().getImage(iconId));
            item.setIcon(icon);
            item.setData(Qt.ItemDataRole.UserRole, iconId);
    }
    
}
