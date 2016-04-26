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

package org.radixware.kernel.explorer.editors.editmask.consteditor;

import com.trolltech.qt.core.Qt;
import com.trolltech.qt.core.Qt.CheckState;
import com.trolltech.qt.core.Qt.ItemFlag;
import com.trolltech.qt.gui.QAbstractItemView.SelectionBehavior;
import com.trolltech.qt.gui.QComboBox;
import com.trolltech.qt.gui.QHeaderView.ResizeMode;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QTableWidget;
import com.trolltech.qt.gui.QTableWidgetItem;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EEditMaskOption;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.meta.RadEnumPresentationDef;
import org.radixware.kernel.common.client.meta.RadEnumPresentationDef.Item;
import org.radixware.kernel.common.client.meta.RadEnumPresentationDef.Items;
import org.radixware.kernel.common.enums.EEditMaskEnumCorrection;
import org.radixware.kernel.common.enums.EEditMaskEnumOrder;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.editors.editmask.IEditMaskEditorSetting;




public class ConstSetListCorrectionSetting extends QWidget implements IEditMaskEditorSetting {
    
    private final QTableWidget tableOfCorrectionOptions;
    private final QComboBox cbChooseCorrectionOption;
    private final RadEnumPresentationDef enumPresentation;
    private final List<Id> checkedItemsList = new LinkedList<Id>();
    private final MessageProvider messageProvider;
       
    public ConstSetListCorrectionSetting(final IClientEnvironment environment,
                                         final QWidget parent,
                                         final RadEnumPresentationDef enumPresentation) {
        super(parent);
        this.messageProvider = environment.getMessageProvider();
        this.enumPresentation = enumPresentation;
        final QVBoxLayout layout = new QVBoxLayout();
        final QLabel label = new QLabel(this);
        final String labelText = environment.getMessageProvider().translate("EditMask", "Item list correction");
        label.setText(labelText);
        
        cbChooseCorrectionOption = new QComboBox(this);
        String itemText =  messageProvider.translate("EditMask", "None");
        cbChooseCorrectionOption.addItem(itemText, EEditMaskEnumCorrection.NONE);
        itemText = messageProvider.translate("EditMask", "Exclude");
        cbChooseCorrectionOption.addItem(itemText, EEditMaskEnumCorrection.EXCLUDE);
        itemText = messageProvider.translate("EditMask", "Include");
        cbChooseCorrectionOption.addItem(itemText, EEditMaskEnumCorrection.INCLUDE);
        cbChooseCorrectionOption.activatedIndex.connect((QWidget)this, "onCorrectionOptionChange()");
        
        tableOfCorrectionOptions = new QTableWidget(enumPresentation.getItems().size(), 4, this);
        setUpTableOfOptions();
        layout.addWidget(label);
        layout.addWidget(cbChooseCorrectionOption);
        layout.addWidget(tableOfCorrectionOptions);
        this.setLayout(layout);
        cbChooseCorrectionOption.activatedIndex.emit(0);
    }

    private void setUpTableOfOptions() {
        tableOfCorrectionOptions.itemClicked.connect((QWidget)this, "onItemClick(QTableWidgetItem)");
        tableOfCorrectionOptions.verticalHeader().setVisible(false);
        tableOfCorrectionOptions.setSelectionBehavior(SelectionBehavior.SelectRows);
        final Items enumItems = enumPresentation.getItems();
        
        fillTableWithValues(enumItems);
        
        tableOfCorrectionOptions.resizeColumnsToContents();
        tableOfCorrectionOptions.horizontalHeader().setResizeMode(1, ResizeMode.Stretch);
        tableOfCorrectionOptions.horizontalHeader().setResizeMode(2, ResizeMode.Stretch);
        tableOfCorrectionOptions.horizontalHeader().setResizeMode(3, ResizeMode.Stretch);
        tableOfCorrectionOptions.adjustSize();
    }

    private void fillTableWithValues(final Items enumItems) {
        tableOfCorrectionOptions.clear();
        tableOfCorrectionOptions.setRowCount(0);
        int index = 0;
        
        QTableWidgetItem checkBoxItem = new QTableWidgetItem("");
        tableOfCorrectionOptions.setHorizontalHeaderItem(0, checkBoxItem);
        String itemText = messageProvider.translate("EditMask", "Name");
        QTableWidgetItem nameItem = new QTableWidgetItem(itemText);
        tableOfCorrectionOptions.setHorizontalHeaderItem(1, nameItem);
        itemText = messageProvider.translate("EditMask", "Title");
        QTableWidgetItem titleItem = new QTableWidgetItem(itemText);
        tableOfCorrectionOptions.setHorizontalHeaderItem(2, titleItem);
        itemText = messageProvider.translate("EditMask", "Value");
        QTableWidgetItem valueItem = new QTableWidgetItem(itemText);
        tableOfCorrectionOptions.setHorizontalHeaderItem(3, valueItem);
        
        
        for(RadEnumPresentationDef.Item i : enumItems) {
            tableOfCorrectionOptions.insertRow(index);
            
            checkBoxItem = new QTableWidgetItem();
            checkBoxItem.setFlags(ItemFlag.ItemIsSelectable, ItemFlag.ItemIsEnabled/*, ItemFlag.ItemIsUserCheckable*/);
            checkBoxItem.setCheckState(CheckState.Unchecked);
            checkBoxItem.setData(Qt.ItemDataRole.UserRole, i.getId());
            tableOfCorrectionOptions.setItem(index, 0, checkBoxItem);
            
                        
            nameItem = new QTableWidgetItem();
            nameItem.setFlags(ItemFlag.ItemIsEnabled, ItemFlag.ItemIsSelectable);
            nameItem.setText(i.getName());
            tableOfCorrectionOptions.setItem(index, 1, nameItem);
            
            titleItem = new QTableWidgetItem();
            titleItem.setFlags(ItemFlag.ItemIsEnabled, ItemFlag.ItemIsSelectable);
            titleItem.setText(i.getTitle());
            tableOfCorrectionOptions.setItem(index, 2, titleItem);
            
            valueItem = new QTableWidgetItem();
            valueItem.setFlags(ItemFlag.ItemIsEnabled, ItemFlag.ItemIsSelectable);
            valueItem.setText(i.getValue().toString());
            tableOfCorrectionOptions.setItem(index, 3, valueItem);
            
            index++;
        }
    }
    
    @Override
    public void addToXml(final org.radixware.schemas.editmask.EditMask editMask) {
        final org.radixware.schemas.editmask.EditMaskEnum editMaskEnum = 
                editMask.getEnum();
        editMaskEnum.setCorrection((EEditMaskEnumCorrection)cbChooseCorrectionOption.itemData(cbChooseCorrectionOption.currentIndex()));
        editMaskEnum.setCorrectionItems(checkedItemsList);
    }

    @Override
    public void loadFromXml(final org.radixware.schemas.editmask.EditMask editMask) {
        final org.radixware.schemas.editmask.EditMaskEnum editMaskEnum = 
                editMask.getEnum();
        if(editMaskEnum.isSetCorrection()) {
            cbChooseCorrectionOption.setCurrentIndex(cbChooseCorrectionOption.findData(
                    editMaskEnum.getCorrection()
            ));
            onCorrectionOptionChange();
        }
        if(editMaskEnum.isSetCorrectionItems()) {
            reset();
             
            final List<Id> list = editMaskEnum.getCorrectionItems();
            final List<Item> enumItems = enumPresentation.getItems().asList();
            int index;
            for(Id id : list) {
                index = 0;
                checkedItemsList.add(id);
            }
        }
    }

    @Override
    public EEditMaskOption getOption() {
        return EEditMaskOption.CONST_CORRECTION;
    }

    @Override
    public void setDefaultValue() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object getValue() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @SuppressWarnings("unused")
    private void onItemClick(final QTableWidgetItem item) {
        final int index = item.row();
        final QTableWidgetItem itemToToggle = tableOfCorrectionOptions.item(index, 0);
        final Id clickedId = (Id) tableOfCorrectionOptions.item(index, 0).data(Qt.ItemDataRole.UserRole);
        
        if(itemToToggle.checkState() == CheckState.Unchecked) {
            checkedItemsList.add(clickedId);
            itemToToggle.setCheckState(CheckState.Checked);
        } else {
            checkedItemsList.remove(clickedId);
            itemToToggle.setCheckState(CheckState.Unchecked);
        }
    }
    
    private void onCorrectionOptionChange() {
        final String itemText = messageProvider.translate("EditMask", "None");
        if(cbChooseCorrectionOption.currentText().equals(itemText)) {
            tableOfCorrectionOptions.setDisabled(true);
            for(int i = 0; i < tableOfCorrectionOptions.rowCount(); i++) {
                tableOfCorrectionOptions.item(i, 0).setCheckState(CheckState.Unchecked);
            }
        } else {
            tableOfCorrectionOptions.setEnabled(true);
        }
    }
    
    public void onSort(final EEditMaskEnumOrder order) {
        final Items items = enumPresentation.getItems();
        items.sort(RadEnumPresentationDef.ItemsOrder.fromEditMaskEnumOrder(order));
        fillTableWithValues(items);
        
        final int rowCount = tableOfCorrectionOptions.rowCount();
        //restore check states of items
        for(int i = 0; i < rowCount; i++) {
            final QTableWidgetItem currentItem = tableOfCorrectionOptions.item(i, 0);
            for(Id id : checkedItemsList) {
                if( ((Id)currentItem.data(Qt.ItemDataRole.UserRole)).equals(id)) {
                    currentItem.setCheckState(CheckState.Checked);
                } else {
                    continue;
                }
            }
        }
        
    }

    private void reset() {
        final int rowCount = tableOfCorrectionOptions.rowCount();
        //reset check states of items
        for(int i = 0; i < rowCount; i++) {
            tableOfCorrectionOptions.item(i, 0).setCheckState(CheckState.Unchecked);
        }
        checkedItemsList.clear();
    }
    
}
